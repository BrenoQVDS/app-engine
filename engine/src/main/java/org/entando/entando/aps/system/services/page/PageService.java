/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.page.PagesStatus;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelUtilizer;
import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IComponentExistsService;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.entando.entando.aps.system.services.jsonpatch.JsonPatchService;
import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.page.model.PageDtoBuilder;
import org.entando.entando.aps.system.services.page.model.PageSearchDto;
import org.entando.entando.aps.system.services.page.model.PagesStatusDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.aps.system.services.pagemodel.PageModelServiceUtilizer;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetProcessorFactory;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetValidatorFactory;
import org.entando.entando.aps.util.GenericResourceUtils;
import org.entando.entando.aps.util.PageUtils;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.web.common.assembler.PageSearchMapper;
import org.entando.entando.web.common.assembler.PagedMetadataMapper;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageCloneRequest;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.entando.entando.web.page.validator.PageValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

/**
 * @author paddeo
 */
public class PageService implements IComponentExistsService, IPageService,
        GroupServiceUtilizer<PageDto>, PageModelServiceUtilizer<PageDto>, ApplicationContextAware {

    private final EntLogger logger = EntLogFactory.getSanitizedLogger(getClass());

    public static final String ERRCODE_PAGE_NOT_FOUND = "1";
    public static final String ERRCODE_PAGEMODEL_NOT_FOUND = "1";
    public static final String ERRCODE_GROUP_NOT_FOUND = "2";
    public static final String ERRCODE_PARENT_NOT_FOUND = "3";
    public static final String ERRCODE_PAGE_ONLY_DRAFT = "3";
    public static final String ERRCODE_FRAME_INVALID = "2";
    public static final String ERRCODE_WIDGET_INVALID = "4";
    public static final String ERRCODE_STATUS_INVALID = "3";
    public static final String ERRCODE_PAGE_REFERENCES = "5";

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IGroupManager groupManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    @Autowired
    private WidgetValidatorFactory widgetValidatorFactory;

    @Autowired
    private WidgetProcessorFactory widgetProcessorFactory;

    @Autowired
    private IDtoBuilder<IPage, PageDto> dtoBuilder;

    private JsonPatchService<PageDto> jsonPatchService = new JsonPatchService<>(PageDto.class);

    private ApplicationContext applicationContext;

    @Autowired
    private IPageTokenManager pageTokenManager;

    @Autowired
    private PageSearchMapper pageSearchMapper;

    @Autowired
    private PagedMetadataMapper pagedMetadataMapper;

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected IPageModelManager getPageModelManager() {
        return pageModelManager;
    }

    public void setPageModelManager(IPageModelManager pageModelManager) {
        this.pageModelManager = pageModelManager;
    }

    public IGroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    protected IDtoBuilder<IPage, PageDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IPage, PageDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    protected WidgetValidatorFactory getWidgetValidatorFactory() {
        return widgetValidatorFactory;
    }

    public void setWidgetValidatorFactory(WidgetValidatorFactory widgetValidatorFactory) {
        this.widgetValidatorFactory = widgetValidatorFactory;
    }

    protected WidgetProcessorFactory getWidgetProcessorFactory() {
        return widgetProcessorFactory;
    }

    public void setWidgetProcessorFactory(WidgetProcessorFactory widgetProcessorFactory) {
        this.widgetProcessorFactory = widgetProcessorFactory;
    }

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public IPageTokenManager getPageTokenManager() {
        return pageTokenManager;
    }

    public void setPageTokenManager(IPageTokenManager pageTokenManager) {
        this.pageTokenManager = pageTokenManager;
    }

    @Override
    public List<PageDto> getPages(String parentCode,
                                  @Nullable String forLinkingToOwnerGroup, @Nullable Collection<String> forLinkingToExtraGroups) {
        List<PageDto> res = new ArrayList<>();
        IPage parent = this.getPageManager().getDraftPage(parentCode);
        Optional.ofNullable(parent).ifPresent(root -> Optional.ofNullable(root.getChildrenCodes())
                .ifPresent(children -> Arrays.asList(children).forEach(childCode -> {
                    IPage childD = this.getPageManager().getDraftPage(childCode);
                    if (forLinkingToOwnerGroup == null ||
                            GenericResourceUtils.isResourceLinkableByContent(
                                    childD.getGroup(), childD.getExtraGroups(),
                                    forLinkingToOwnerGroup, forLinkingToExtraGroups)
                    ) {
                        PageDto pageDto = dtoBuilder.convert(childD);
                        String pageCode = pageDto.getCode();
                        String token = this.getPageTokenManager().encrypt(pageCode);
                        String urlToken = getUrlToken(token);
                        pageDto.setToken(urlToken);
                        res.add(pageDto);
                    }
                })));
        return res;
    }

    @Override
    public PageDto getPage(String pageCode, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            logger.warn("no page found with code {} and status {}", pageCode, status);
            DataBinder binder = new DataBinder(pageCode);
            BindingResult bindingResult = binder.getBindingResult();
            String errorCode = status.equals(STATUS_DRAFT) ? ERRCODE_PAGE_NOT_FOUND : ERRCODE_PAGE_ONLY_DRAFT;
            bindingResult.reject(errorCode, new String[]{pageCode, status}, "page.NotFound");
            throw new ResourceNotFoundException(bindingResult);
        }
        PageDto pageDto = this.getDtoBuilder().convert(page);
        String token = this.getPageTokenManager().encrypt(pageCode);
        String urlToken = getUrlToken(token);
        pageDto.setToken(urlToken);
        pageDto.setReferences(this.getReferencesInfo(page));
        return pageDto;
    }

    private String getUrlToken(String token) {
        try {
            return URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error encoding token page", e);
            throw new RestServerError("error encoding token page", e);
        }
    }

    public boolean exists(String pageCode, String status) {
        return this.loadPage(pageCode, status) != null;
    }

    @Override
    public boolean exists(String code) {
        return this.exists(code, IPageService.STATUS_DRAFT) || this.exists(code, IPageService.STATUS_ONLINE);
    }

    @Override
    public PageDto addPage(PageRequest pageRequest) {
        this.validateRequest(pageRequest);
        try {
            IPage page = this.createPage(pageRequest);
            this.getPageManager().addPage(page);
            IPage addedPage = this.getPageManager().getDraftPage(page.getCode());
            return this.getDtoBuilder().convert(addedPage);
        } catch (EntException e) {
            logger.error("Error adding page", e);
            throw new RestServerError("error add page", e);
        }
    }

    @Override
    public void removePage(String pageCode) {
        try {
            IPage page = this.getPageManager().getDraftPage(pageCode);
            if (null != page) {
                this.getPageManager().deletePage(pageCode);
            }
        } catch (EntException e) {
            logger.error("Error in delete page {}", pageCode, e);
            throw new RestServerError("error in delete page", e);
        }
    }

    @Override
    public PageDto updatePage(String pageCode, PageRequest pageRequest) {
        IPage oldPage = this.getPageManager().getDraftPage(pageCode);
        if (null == oldPage) {
            throw new ResourceNotFoundException(null, "page", pageCode);
        }
        this.validateRequest(pageRequest);

        if (!oldPage.getGroup().equals(pageRequest.getOwnerGroup())) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(oldPage, "page");
            bindingResult.reject(PageValidator.ERRCODE_GROUP_MISMATCH,
                    new String[]{oldPage.getGroup(), pageRequest.getOwnerGroup()}, "page.update.group.invalid");
            throw new ValidationGenericException(bindingResult);
        }
        try {
            if (!oldPage.getParentCode().equals(pageRequest.getParentCode())) {
                PagePositionRequest pagePositionRequest = new PagePositionRequest();
                pagePositionRequest.setParentCode(pageRequest.getParentCode());
                pagePositionRequest.setCode(pageCode);
                int position = this.getPages(pageCode).size() + 1;
                pagePositionRequest.setPosition(position);
                this.movePage(pageCode, pagePositionRequest);
                oldPage = this.getPageManager().getDraftPage(pageCode);
            }

            IPage newPage = this.updatePage(oldPage, pageRequest);
            this.getPageManager().updatePage(newPage);
            IPage updatePage = this.getPageManager().getDraftPage(pageCode);
            PageDto page = this.getDtoBuilder().convert(updatePage);
            page.setPosition(oldPage.getPosition());
            return page;
        } catch (EntException e) {
            logger.error("Error updating page {}", pageCode, e);
            throw new RestServerError("error in update page", e);
        }
    }

    @Override
    public PageDto getPatchedPage(String pageCode, JsonNode patch) {
        PageDto pageDto = this.getPage(pageCode, STATUS_DRAFT);
        return this.jsonPatchService.applyPatch(patch, pageDto);
    }

    @Override
    public PageDto updatePageStatus(String pageCode, String status) {
        IPage currentPage = this.getPageManager().getDraftPage(pageCode);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageCode, "page");
        if (null == currentPage) {
            throw new ResourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        if (status.equals(STATUS_DRAFT) && null == this.getPageManager().getOnlinePage(pageCode)) {
            return this.getDtoBuilder().convert(currentPage);
        }
        try {
            IPage newPage = null;
            if (status.equals(STATUS_ONLINE)) {
                IPage publicParent = this.getPageManager().getOnlinePage(currentPage.getParentCode());
                if (null == publicParent) {
                    bindingResult.reject(PageValidator.ERRCODE_PAGE_WITH_NO_PUBLIC_PARENT,
                            new String[]{pageCode, currentPage.getParentCode()}, "page.status.parent.unpublished");
                    throw new ValidationGenericException(bindingResult);
                }
                this.getPageManager().setPageOnline(pageCode);
                newPage = this.getPageManager().getOnlinePage(pageCode);
            } else if (status.equals(STATUS_DRAFT)) {
                String[] childCodes = currentPage.getChildrenCodes();
                for (String childCode : childCodes) {
                    IPage publicChild = this.getPageManager().getOnlinePage(childCode);
                    if (null != publicChild) {
                        bindingResult.reject(PageValidator.ERRCODE_PAGE_WITH_PUBLIC_CHILD,
                                new String[]{pageCode}, "page.status.publicChild");
                        throw new ValidationGenericException(bindingResult);
                    }
                }
                Map<String, PageServiceUtilizer> beans = applicationContext.getBeansOfType(PageServiceUtilizer.class);
                if (null != beans) {
                    Iterator<PageServiceUtilizer> iter = beans.values().iterator();
                    while (iter.hasNext()) {
                        PageServiceUtilizer serviceUtilizer = iter.next();
                        List utilizer = serviceUtilizer.getPageUtilizer(pageCode);
                        if (null != utilizer && utilizer.size() > 0) {
                            bindingResult.reject(PageValidator.ERRCODE_REFERENCED_ONLINE_PAGE, new String[]{pageCode}, "page.status.invalid.online.ref");
                            throw new ValidationGenericException(bindingResult);
                        }
                    }
                }
                this.getPageManager().setPageOffline(pageCode);
                newPage = this.getPageManager().getDraftPage(pageCode);
            }
            return this.getDtoBuilder().convert(newPage);
        } catch (ValidationGenericException e) {
            throw e;
        } catch (EntException e) {
            logger.error("Error updating page {} status", pageCode, e);
            throw new RestServerError("error in update page status", e);
        }
    }

    @Override
    public PageDto movePage(String pageCode, PagePositionRequest pageRequest) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageCode, "page");
        if (pageCode.equals(pageRequest.getParentCode())) {
            bindingResult.reject(PageValidator.ERRCODE_INVALID_PARENT, new String[]{pageCode}, "page.movement.parent.invalid.1");
            throw new ValidationGenericException(bindingResult);
        }
        IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode());
        IPage page = this.getPageManager().getDraftPage(pageCode);
        if (parent.isChildOf(pageCode, this.getPageManager())) {
            bindingResult.reject(PageValidator.ERRCODE_INVALID_PARENT,
                    new String[]{pageRequest.getParentCode(), pageCode}, "page.movement.parent.invalid.2");
            throw new ValidationGenericException(bindingResult);
        }
        try {
            if (page.getParentCode().equals(parent.getCode())) {
                int iterations = Math.abs(page.getPosition() - pageRequest.getPosition());
                boolean moveUp = page.getPosition() > pageRequest.getPosition();
                while (iterations-- > 0 && this.getPageManager().movePage(pageCode, moveUp));
            } else {
                this.getPageManager().movePage(page, parent);
                // Page is appended as last element, moving it at desired position:
                int lastPosition = parent.getChildrenCodes().length + 1;
                int iterations = Math.abs(lastPosition - pageRequest.getPosition());
                boolean moveUp = lastPosition > pageRequest.getPosition();
                while (iterations-- > 0 && this.getPageManager().movePage(pageCode, moveUp));
            }
            page = this.getPageManager().getDraftPage(pageCode);
        } catch (EntException e) {
            logger.error("Error moving page {}", pageCode, e);
            throw new RestServerError("error in moving page", e);
        }
        return this.getDtoBuilder().convert(page);
    }

    @Override
    public PageConfigurationDto getPageConfiguration(String pageCode, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            throw new ResourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        PageConfigurationDto pageConfigurationDto = new PageConfigurationDto(page, status);
        return pageConfigurationDto;
    }

    @Override
    public PageConfigurationDto restorePageConfiguration(String pageCode) {
        try {
            IPage pageD = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == pageD) {
                throw new ResourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            IPage pageO = this.loadPage(pageCode, STATUS_ONLINE);
            if (null == pageO) {
                DataBinder binder = new DataBinder(pageCode);
                BindingResult bindingResult = binder.getBindingResult();
                bindingResult.reject(ERRCODE_STATUS_INVALID, new String[]{pageCode}, "page.status.invalid");
                throw new ValidationGenericException(bindingResult);
            }
            pageD.setMetadata(pageO.getMetadata());
            pageD.setWidgets(pageO.getWidgets());
            this.getPageManager().updatePage(pageD);
            PageConfigurationDto pageConfigurationDto = new PageConfigurationDto(pageO, STATUS_ONLINE);
            return pageConfigurationDto;
        } catch (EntException e) {
            logger.error("Error restoring page {} configuration", pageCode, e);
            throw new RestServerError("error in restoring page configuration", e);
        }
    }

    @Override
    public WidgetConfigurationDto getWidgetConfiguration(String pageCode, int frameId, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            throw new ResourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        if (frameId > page.getWidgets().length) {
            throw new ResourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
        }
        Widget widget = page.getWidgets()[frameId];
        if (null == widget) {
            return null;
        }
        return new WidgetConfigurationDto(widget);
    }

    @Override
    public WidgetConfigurationDto updateWidgetConfiguration(String pageCode, int frameId, WidgetConfigurationRequest widgetReq) {
        try {
            IPage page = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == page) {
                throw new ResourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            if (page.getWidgets() == null || frameId > page.getWidgets().length) {
                throw new ResourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
            }
            WidgetType widgetType = this.getWidgetType(widgetReq.getCode());
            if (null == widgetType) {
                throw new ResourceNotFoundException(ERRCODE_WIDGET_INVALID, "widget", String.valueOf(widgetReq.getCode()));
            }
            BeanPropertyBindingResult validation = this.getWidgetValidatorFactory().get(widgetReq.getCode()).validate(widgetReq, page);
            if (null != validation && validation.hasErrors()) {
                throw new ValidationConflictException(validation);
            }
            ApsProperties properties = (ApsProperties) this.getWidgetProcessorFactory().get(widgetReq.getCode()).buildConfiguration(widgetReq);
            Widget widget = new Widget();
            widget.setTypeCode(widgetType.getCode());
            widget.setConfig(properties);
            this.getPageManager().joinWidget(pageCode, widget, frameId);

            ApsProperties outProperties = this.getWidgetProcessorFactory().get(widgetReq.getCode()).extractConfiguration(widget.getConfig());
            return new WidgetConfigurationDto(widget.getTypeCode(), outProperties);
        } catch (EntException e) {
            logger.error("Error in update widget configuration {}", pageCode, e);
            throw new RestServerError("error in update widget configuration", e);
        }
    }

    @Override
    public void deleteWidgetConfiguration(String pageCode, int frameId) {
        try {
            IPage page = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == page) {
                logger.debug("Deleting a widget into a page not found - page ''{}'', pos ''{}''", pageCode, frameId);
                return;
            }
            if (frameId >= page.getWidgets().length) {
                logger.info("the frame to delete with index {} in page {} with model {} does not exists",
                        frameId, pageCode,
                        page.getModelCode());
                return;
            }
            this.pageManager.removeWidget(pageCode, frameId);
        } catch (EntException e) {
            logger.error("Error in delete widget configuration for page {} and frame {}", pageCode, frameId, e);
            throw new RestServerError("error in delete widget configuration", e);
        }
    }

    @Override
    public PageConfigurationDto applyDefaultWidgets(String pageCode) {
        try {
            IPage page = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == page) {
                throw new ResourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            PageModel pageModel = this.getPageModelManager().getPageModel(page.getModelCode());
            Widget[] defaultWidgets = pageModel.getDefaultWidget();
            if (null == defaultWidgets) {
                logger.info("no default widget configuration for model {}", pageModel.getCode());
                return new PageConfigurationDto(page, STATUS_DRAFT);
            }

            Widget[] widgets = mergePageConfiguration(page, defaultWidgets);
            page.setWidgets(widgets);
            this.getPageManager().updatePage(page);
            return new PageConfigurationDto(page, STATUS_DRAFT);

        } catch (EntException e) {
            logger.error("Error setting default widgets for page {}", pageCode, e);
            throw new RestServerError("Error setting default widgets for page " + pageCode, e);
        }
    }

    /**
     * Merge the page configuration with the provided new one.
     * </p>
     *
     * @param page
     * @param newWidgetConfiguration
     * @return
     */
    protected Widget[] mergePageConfiguration(IPage page, Widget[] newWidgetConfiguration) {
        Widget[] widgets = page.getWidgets();
        for (int i = 0; i < newWidgetConfiguration.length; i++) {
            Widget defaultWidget = newWidgetConfiguration[i];
            if (null != defaultWidget) {
                if (null == defaultWidget.getTypeCode()) {
                    logger.info("Widget Type null when adding defaulWidget (of pagemodel '{}') on frame '{}' of page '{}'", page.getModelCode(), i, page.getCode());
                    continue;
                }
                widgets[i] = defaultWidget;
            }
        }
        return widgets;
    }

    public WidgetType getWidgetType(String typeCode) {
        return this.getWidgetTypeManager().getWidgetType(typeCode);
    }

    protected IPage createPage(PageRequest pageRequest) {
        Page page = new Page();
        page.setCode(pageRequest.getCode());
        page.setShowable(pageRequest.isDisplayedInMenu());
        PageModel model = this.getPageModelManager().getPageModel(pageRequest.getPageModel());
        page.setModelCode(model.getCode());
        page.setWidgets(new Widget[model.getFrames().length]);
        page.setCharset(pageRequest.getCharset());
        page.setMimeType(pageRequest.getContentType());
        page.setParentCode(pageRequest.getParentCode());
        page.setUseExtraTitles(pageRequest.isSeo());
        Optional<Map<String, String>> titles = Optional.ofNullable(pageRequest.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.keySet().forEach((lang) -> {
            apsTitles.put(lang, values.get(lang));
        }));
        page.setTitles(apsTitles);
        page.setGroup(pageRequest.getOwnerGroup());
        Optional<List<String>> groups = Optional.ofNullable(pageRequest.getJoinGroups());
        groups.ifPresent(values -> values.forEach((group) -> {
            page.addExtraGroup(group);
        }));
        page.setParentCode(pageRequest.getParentCode());
        if (pageRequest.getParentCode() != null) {
            IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode());
            page.setParentCode(parent.getCode());
        }
        PageMetadata metadata = page.getMetadata();
        this.valueMetadataFromRequest(metadata, pageRequest);
        page.setMetadata(metadata);
        return page;
    }

    protected IPage updatePage(IPage oldPage, PageRequest pageRequest) {
        Page page = new Page();
        PageMetadata metadata = oldPage.getMetadata();
        if (metadata == null) {
            metadata = new PageMetadata();
        }
        this.valueMetadataFromRequest(metadata, pageRequest);
        page.setMetadata(metadata);
        page.setCode(pageRequest.getCode());
        page.setShowable(pageRequest.isDisplayedInMenu());
        if (!oldPage.getModelCode().equals(pageRequest.getPageModel())) {
            PageModel model = this.getPageModelManager().getPageModel(pageRequest.getPageModel());
            model.setCode(pageRequest.getPageModel());
            page.setModelCode(pageRequest.getPageModel());
            page.setWidgets(new Widget[model.getFrames().length]);
        } else {
            page.setWidgets(oldPage.getWidgets());
        }
        page.setCharset(pageRequest.getCharset());
        page.setMimeType(pageRequest.getContentType());
        page.setParentCode(pageRequest.getParentCode());
        page.setUseExtraTitles(pageRequest.isSeo());
        Optional<Map<String, String>> titles = Optional.ofNullable(pageRequest.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.keySet().forEach((lang) -> {
            apsTitles.put(lang, values.get(lang));
        }));
        page.setTitles(apsTitles);
        page.setGroup(pageRequest.getOwnerGroup());
        if (page.getExtraGroups() != null) {
            List<String> oldGroups = new ArrayList<>(page.getExtraGroups());
            oldGroups.forEach(page::removeExtraGroup);
        }
        if (pageRequest.getJoinGroups() != null) {
            pageRequest.getJoinGroups().forEach(page::addExtraGroup);
        }
        page.setParentCode(pageRequest.getParentCode());
        page.setPosition(oldPage.getPosition());
        page.setChildrenCodes(oldPage.getChildrenCodes());

        return page;
    }

    private void valueMetadataFromRequest(PageMetadata metadata, PageRequest request) {
        metadata.setGroup(request.getOwnerGroup());
        metadata.setShowable(request.isDisplayedInMenu());
        metadata.setUseExtraTitles(request.isSeo());
        Optional<Map<String, String>> titles = Optional.ofNullable(request.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.keySet().forEach((lang) -> {
            apsTitles.put(lang, values.get(lang));
        }));
        metadata.setTitles(apsTitles);
        if (metadata.getExtraGroups() != null) {
            List<String> oldGroups = new ArrayList<>(metadata.getExtraGroups());
            oldGroups.forEach(metadata::removeExtraGroup);
        }
        if (request.getJoinGroups() != null) {
            request.getJoinGroups().forEach(metadata::addExtraGroup);
        }
        String charset = request.getCharset();
        metadata.setCharset(StringUtils.isNotBlank(charset) ? charset : null);

        String mimetype = request.getContentType();
        metadata.setMimeType(StringUtils.isNotBlank(mimetype) ? mimetype : null);
    }

    protected IPage loadPage(String pageCode, String status) {
        IPage page = null;
        switch (status) {
            case STATUS_DRAFT:
                page = this.getPageManager().getDraftPage(pageCode);
                break;
            case STATUS_ONLINE:
                page = this.getPageManager().getOnlinePage(pageCode);
                break;
            default:
                break;
        }
        if (status.equals(STATUS_ONLINE) && null == page && null != this.getPageManager().getDraftPage(pageCode)) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(page, "page");
            bindingResult.reject(ERRCODE_PAGE_ONLY_DRAFT, new Object[]{pageCode}, "page.status.draftOnly");
            throw new ValidationGenericException(bindingResult);
        }
        return page;
    }

    protected void validateRequest(PageRequest request) {
        if (this.getPageModelManager().getPageModel(request.getPageModel()) == null) {
            throw new ResourceNotFoundException(ERRCODE_PAGEMODEL_NOT_FOUND, "pageModel", request.getPageModel());
        }
        if (this.getPageManager().getDraftPage(request.getParentCode()) == null) {
            throw new ResourceNotFoundException(ERRCODE_PARENT_NOT_FOUND, "parent", request.getParentCode());
        }
        if (this.getGroupManager().getGroup(request.getOwnerGroup()) == null) {
            throw new ResourceNotFoundException(ERRCODE_GROUP_NOT_FOUND, "group", request.getOwnerGroup());
        }
        Optional.ofNullable(request.getJoinGroups()).ifPresent(groups -> groups.forEach(group -> {
            if (this.getGroupManager().getGroup(group) == null) {
                throw new ResourceNotFoundException(ERRCODE_GROUP_NOT_FOUND, "joingroup", group);
            }
        }));
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getPageManager()).getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PageDto> getGroupUtilizer(String groupName) {
        try {
            List<IPage> pages = ((GroupUtilizer<IPage>) this.getPageManager()).getGroupUtilizers(groupName);
            return this.getDtoBuilder().convert(pages);
        } catch (EntException ex) {
            logger.error("Error loading page references for group {}", groupName, ex);
            throw new RestServerError("Error loading page references for group", ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PageDto> getPageModelUtilizer(String pageModelCode) {
        try {
            List<IPage> pages = ((PageModelUtilizer) this.getPageManager()).getPageModelUtilizers(pageModelCode);
            return this.getDtoBuilder().convert(pages);
        } catch (EntException ex) {
            logger.error("Error loading page references for pagemodel {}", pageModelCode, ex);
            throw new RestServerError("Error loading page references for pagemodel " + pageModelCode, ex);
        }
    }


    @Override
    public PagedMetadata<PageDto> searchPages(PageSearchRequest request, List<String> allowedGroups) {
        try {
            List<IPage> rawPages = this.getPageManager().searchPages(request.getPageCodeToken(),
                    request.getTitle(), allowedGroups);
            List<PageDto> pages = this.getDtoBuilder().convert(rawPages);
            return pageSearchMapper.toPageSearchDto(request, pages);
        } catch (EntException ex) {
            logger.error("Error searching pages with token {}", request.getPageCodeToken(), ex);
            throw new RestServerError("Error searching pages", ex);
        }
    }

    @Override
    public PagedMetadata<?> getPageReferences(String pageCode, String managerName, RestListRequest requestList) {
        IPage page = this.getPageManager().getDraftPage(pageCode);
        if (null == page) {
            logger.warn("no page found with code {}", pageCode);
            throw new ResourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        PageServiceUtilizer<?> utilizer = this.getPageServiceUtilizer(managerName);
        if (null == utilizer) {
            logger.warn("no references found for {}", managerName);
            throw new ResourceNotFoundException(ERRCODE_PAGE_REFERENCES, "reference", managerName);
        }
        List<?> dtoList = utilizer.getPageUtilizer(pageCode);
        List<?> subList = requestList.getSublist(dtoList);
        SearcherDaoPaginatedResult<?> pagedResult = new SearcherDaoPaginatedResult<>(dtoList.size(), subList);
        PagedMetadata<Object> pagedMetadata = new PagedMetadata<>(requestList, pagedResult);
        pagedMetadata.setBody((List<Object>) subList);
        return pagedMetadata;
    }

    @Override
    public PagedMetadata<PageDto> searchOnlineFreePages(RestListRequest request) {
        try {
            List<String> groups = new ArrayList<>();
            groups.add(Group.FREE_GROUP_NAME);
            List<IPage> rawPages = this.getPageManager().searchOnlinePages(null, null, groups);
            List<PageDto> pages = this.getDtoBuilder().convert(rawPages);
            return pageSearchMapper.toPageSearchDto(request, pages);
        } catch (EntException ex) {
            logger.error("Error searching free online pages ", ex);
            throw new RestServerError("Error searching free online pages", ex);
        }
    }

    @Override
    public Integer getComponentUsage(String pageCode) {
        try {
            return this.getComponentUsageDetails(pageCode, new RestListRequest()).getTotalItems();
        } catch (ResourceNotFoundException e) {
            return 0;
        }
    }


    @Override
    public PagedMetadata<ComponentUsageEntity> getComponentUsageDetails(String pageCode, RestListRequest restListRequest) {

        PageDto pageDto = this.getPage(pageCode, IPageService.STATUS_DRAFT);
        List<PageDto> childrenPageDtoList = this.getPages(pageCode);

        List<ComponentUsageEntity> componentUsageEntityList = childrenPageDtoList.stream()
                .map(childPageDto -> new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, childPageDto.getCode(), childPageDto.getStatus()))
                .collect(Collectors.toList());

        if (pageDto.getStatus().equals(IPageService.STATUS_ONLINE)) {
            componentUsageEntityList.add(new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, pageDto.getCode(), pageDto.getStatus()));
        }

        return pagedMetadataMapper.getPagedResult(restListRequest, componentUsageEntityList);
    }

    private PagedMetadata<PageDto> getPagedResult(PageSearchRequest request, List<PageDto> pages) {
        BeanComparator comparator = new BeanComparator(request.getSort());
        if (request.getDirection().equals(FieldSearchFilter.DESC_ORDER)) {
            Collections.sort(pages, comparator.reversed());
        } else {
            Collections.sort(pages, comparator);
        }
        PageSearchDto result = new PageSearchDto(request, pages);
        result.imposeLimits();
        return result;
    }

    private PagedMetadata<PageDto> getPagedResult(RestListRequest request, List<PageDto> pages) {
        PageSearchRequest pageSearchReq = new PageSearchRequest();
        BeanUtils.copyProperties(request, pageSearchReq);

        return getPagedResult(pageSearchReq, pages);
    }


    protected Map<String, Boolean> getReferencesInfo(IPage page) {
        Map<String, Boolean> references = new HashMap<>();
        try {
            String[] defNames = applicationContext.getBeanNamesForType(PageUtilizer.class);
            for (String defName : defNames) {
                Object service = null;
                try {
                    service = applicationContext.getBean(defName);
                } catch (Throwable t) {
                    logger.error("error in hasReferencingObjects", t);
                    service = null;
                }
                if (service != null) {
                    PageUtilizer pageUtilizer = (PageUtilizer) service;
                    List<?> utilizers = pageUtilizer.getPageUtilizers(page.getCode());
                    if (utilizers != null && !utilizers.isEmpty()) {
                        references.put(pageUtilizer.getName(), true);
                    } else {
                        references.put(pageUtilizer.getName(), false);
                    }
                }
            }
        } catch (EntException ex) {
            logger.error("error loading references for page {}", page.getCode(), ex);
            throw new RestServerError("error in getReferencingObjects ", ex);
        }
        return references;
    }

    @SuppressWarnings("rawtypes")
    private PageServiceUtilizer<?> getPageServiceUtilizer(String managerName) {
        Map<String, PageServiceUtilizer> beans = applicationContext.getBeansOfType(PageServiceUtilizer.class);
        PageServiceUtilizer defName = beans.values().stream()
                .filter(service -> service.getManagerName().equals(managerName))
                .findFirst().orElse(null);
        return defName;

    }

    @Override
    public PagesStatusDto getPagesStatus() {
        PagesStatus raw = this.getPageManager().getPagesStatus();
        return new PagesStatusDto(raw);
    }

    @Override
    public List<PageDto> listViewPages() {
        IPage root = pageManager.getOnlineRoot();
        List<IPage> pages = new ArrayList<>();
        addViewPages(root, pages);
        return pages.stream()
                .map(p -> PageDtoBuilder.converToDto(p, pageManager))
                .collect(Collectors.toList());
    }

    private void addViewPages(IPage page, List<IPage> pages) {
        if (null == page) {
            return;
        }
        PageModel pageModel = this.getPageModelManager().getPageModel(page.getModelCode());
        if (page.getGroup().equals(Group.FREE_GROUP_NAME) && PageUtils.isOnlineFreeViewerPage(page, pageModel, null, this.getWidgetTypeManager())) {
            pages.add(page);
        }
        String[] children = page.getChildrenCodes();
        if (null == children) {
            return;
        }
        for (int i = 0; i < children.length; i++) {
            IPage child = pageManager.getOnlinePage(children[i]);
            this.addViewPages(child, pages);
        }
    }

    @Override
    public PageDto clonePage(String pageCode, PageCloneRequest pageCloneRequest, BindingResult bindingResult) {
        try {
            Page page = loadPage(pageCode);
            if (page == null) {
                bindingResult.reject(ERRCODE_PAGE_NOT_FOUND, new String[]{pageCode}, "page.notFound.anyStatus");
                throw new ResourceNotFoundException(bindingResult);
            }
            page.setCode(pageCloneRequest.getNewPageCode());
            page.setTitles(ApsProperties.fromMap(pageCloneRequest.getTitles()));
            page.setParentCode(pageCloneRequest.getParentCode());
            page.setOnline(false);
            pageManager.addPage(page);
            IPage addedPage = pageManager.getDraftPage(page.getCode());
            return dtoBuilder.convert(addedPage);
        } catch (EntException e) {
            logger.error("Error adding page", e);
            throw new RestServerError("error add page", e);
        }
    }


    /**
     * Load the page in online or not.
     *
     * @param pageCode pageCode to be loaded
     * @return page loaded or null if not found.
     */
    private Page loadPage(String pageCode) {
        IPage result = getPageManager().getDraftPage(pageCode);
        if (result == null) {
            result = getPageManager().getOnlinePage(pageCode);
        }
        return (Page) result;
    }
}
