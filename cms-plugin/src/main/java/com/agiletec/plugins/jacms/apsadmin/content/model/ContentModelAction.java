/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.apsadmin.content.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import org.entando.entando.ent.exception.EntException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary.ContentModelDictionary;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;

/**
 * Classe action delegata alle operazioni sugli oggetti modelli di contenuti.
 *
 * @author E.Santoboni
 */
public class ContentModelAction extends BaseAction implements IContentModelAction {

    private static final EntLogger _logger = EntLogFactory.getSanitizedLogger(ContentModelAction.class);

    @Override
    public void validate() {
        super.validate();
        if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
            this.checkModelId();
        }
    }

    private void checkModelId() {
        if (null == this.getModelId()) {
            return;
        }
        ContentModel dummyModel = this.getContentModelManager().getContentModel(this.getModelId());
        if (dummyModel != null) {
            this.addFieldError("modelId", this.getText("error.contentModel.modelId.alreadyPresent"));
        }
        SmallContentType utilizer = this.getContentModelManager().getDefaultUtilizer(this.getModelId());
        if (null != utilizer && !utilizer.getCode().equals(this.getContentType())) {
            String[] args = {this.getModelId().toString(), utilizer.getDescr()};
            this.addFieldError("modelId", this.getText("error.contentModel.modelId.wrongUtilizer", args));
        }
    }

    @Override
    public String newModel() {
        this.setStrutsAction(ApsAdminSystemConstants.ADD);
        return SUCCESS;
    }

    @Override
    public String edit() {
        this.setStrutsAction(ApsAdminSystemConstants.EDIT);
        try {
            long modelId = this.getModelId().longValue();
            ContentModel model = this.getContentModelManager().getContentModel(modelId);
            this.setFormValues(model);
        } catch (Throwable t) {
            _logger.error("error in edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String save() {
        try {
            ContentModel model = this.getBeanFromForm();
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
                this.getContentModelManager().addContentModel(model);
            } else {
                this.getContentModelManager().updateContentModel(model);
            }
        } catch (Throwable t) {
            _logger.error("error in save", t);
            //ApsSystemUtils.logThrowable(t, this, "save");
            return FAILURE;
        }
        return SUCCESS;
    }

    private String checkDelete() {
        String check = null;
        long modelId = this.getModelId().longValue();

        List<ContentModelReference> references = this.getContentModelManager().getContentModelReferences(modelId,false);
        if (!references.isEmpty()) {
            // sort by page code, status (draft first) and then widget position
            Collections.sort(references, (ref1, ref2) -> {
                int pageCompare = ref1.getPageCode().compareTo(ref2.getPageCode());
                if (pageCompare == 0) {
                    int statusCompare = Boolean.compare(ref1.isOnline(), ref2.isOnline());
                    if (statusCompare == 0) {
                        return Integer.compare(ref1.getWidgetPosition(), ref2.getWidgetPosition());
                    }
                    return statusCompare;
                }
                return pageCompare;
            });

            // build page maps (used for displaying information on the table inside JSP)
            this.onlineReferencedPages = new HashMap<>();
            this.draftReferencedPages = new HashMap<>();
            for (ContentModelReference reference : references) {
                String pageCode = reference.getPageCode();
                if (reference.isOnline()) {
                    this.onlineReferencedPages.computeIfAbsent(pageCode, code -> this.pageManager.getOnlinePage(code));
                } else {
                    this.draftReferencedPages.computeIfAbsent(pageCode, code -> this.pageManager.getDraftPage(code));
                }
            }

            this.setContentModelReferences(references);
            check = "references";
        }

        return check;
    }

    @Override
    public String trash() {
        try {
            String check = this.checkDelete();
            if (null != check) {
                return check;
            }
            long modelId = this.getModelId().longValue();
            ContentModel model = this.getContentModelManager().getContentModel(modelId);
            if (null != model) {
                this.setDescription(model.getDescription());
                this.setContentType(model.getContentType());
            } else {
                return "noModel";
            }
        } catch (Throwable t) {
            _logger.error("error in trash", t);
            //ApsSystemUtils.logThrowable(t, this, "trash");
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public String delete() {
        try {
            String check = this.checkDelete();
            if (null != check) {
                return check;
            }
            long modelId = this.getModelId().longValue();
            ContentModel model = this.getContentModelManager().getContentModel(modelId);
            this.getContentModelManager().removeContentModel(model);
        } catch (Throwable t) {
            _logger.error("error in delete", t);
            //ApsSystemUtils.logThrowable(t, this, "delete");
            return FAILURE;
        }
        return SUCCESS;
    }

    public List<SmallContentType> getSmallContentTypes() {
        return this.getContentManager().getSmallContentTypes();
    }

    public Map<String, SmallContentType> getSmallContentTypesMap() {
        return this.getContentManager().getSmallContentTypesMap();
    }

    public SmallContentType getSmallContentType(String typeCode) {
        return this.getContentManager().getSmallContentTypesMap().get(typeCode);
    }

    public ContentModel getContentModel(long modelId) {
        return this.getContentModelManager().getContentModel(modelId);
    }

    private ContentModel getBeanFromForm() {
        ContentModel contentModel = new ContentModel();
        contentModel.setId(this.getModelId());
        contentModel.setContentShape(this.getContentShape());
        contentModel.setContentType(this.getContentType());
        contentModel.setDescription(this.getDescription());
        if (null != this.getStylesheet() && this.getStylesheet().trim().length() > 0) {
            contentModel.setStylesheet(this.getStylesheet());
        }
        if (getStrutsAction() == ApsAdminSystemConstants.EDIT) {
            contentModel.setId(new Long(this.getModelId()).longValue());
        }
        return contentModel;
    }

    private void setFormValues(ContentModel model) {
        this.setModelId(new Integer(String.valueOf(model.getId())));
        this.setDescription(model.getDescription());
        this.setContentType(model.getContentType());
        this.setContentShape(model.getContentShape());
        this.setStylesheet(model.getStylesheet());
    }

    /**
     * Restituisce il contenuto vo in base all'identificativo. Metodo a servizio
     * dell'interfaccia di visualizzazione contenuti in lista.
     *
     * @param contentId L'identificativo del contenuto.
     * @return Il contenuto vo cercato.
     */
    public ContentRecordVO getContentVo(String contentId) {
        ContentRecordVO contentVo = null;
        try {
            contentVo = this.getContentManager().loadContentVO(contentId);
        } catch (Throwable t) {
            _logger.error("error loading getContentVo for {}", contentId, t);
            //ApsSystemUtils.logThrowable(t, this, "getContentVo");
            throw new RuntimeException("Errore in caricamento contenuto vo", t);
        }
        return contentVo;
    }

    /* Used by JSP page */
    public IPage getPage(ContentModelReference reference) {
        if (reference.isOnline()) {
            return onlineReferencedPages.get(reference.getPageCode());
        } else {
            return draftReferencedPages.get(reference.getPageCode());
        }
    }

    /* Used by JSP page */
    public Widget getWidget(ContentModelReference reference) {
        return getPage(reference).getWidgets()[reference.getWidgetPosition()];
    }

    /* Used by JSP page */
    public String getWidgetTitle(ContentModelReference reference, String langCode) {
        Widget widget = this.getWidget(reference);
        if (null == widget) {
            return null;
        }
        WidgetType type = this.getWidgetTypeManager().getWidgetType(widget.getTypeCode());
        if (null == type) {
            return null;
        }
        return type.getTitles().getProperty(langCode);
    }

    public Content getContentPrototype() {
        if (null == this.getContentType()) {
            return null;
        }
        return (Content) this.getContentManager().getEntityPrototype(this.getContentType());
    }

    public List<String> getAllowedAttributeMethods(Content prototype, String attributeName) {
        List<String> methods = new ArrayList<>();
        try {
            AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(attributeName);
            if (null == attribute) {
                throw new EntException("Null Attribute '" + attributeName + "' for Content Type '"
                        + prototype.getTypeCode() + "' - '" + prototype.getTypeDescr());
            }
            List<String> attributeMethods = ContentModelDictionary.getAllowedAttributeMethods(attribute, this.getAllowedPublicAttributeMethods());
            if (null != attributeMethods) {
                methods.addAll(attributeMethods);
            }
        } catch (Throwable t) {
            _logger.error("error in getAllowedAttributeMethods", t);
        }
        return methods;
    }
    
    public int getStrutsAction() {
        return strutsAction;
    }
    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    public Integer getModelId() {
        return modelId;
    }
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentShape() {
        return contentShape;
    }
    public void setContentShape(String contentShape) {
        this.contentShape = contentShape;
    }

    public String getStylesheet() {
        return stylesheet;
    }
    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }

    public List<String> getAllowedPublicContentMethods() {
        return allowedPublicContentMethods;
    }
    public void setAllowedPublicContentMethods(List<String> allowedPublicContentMethods) {
        this.allowedPublicContentMethods = allowedPublicContentMethods;
    }

    public Properties getAllowedPublicAttributeMethods() {
        return allowedPublicAttributeMethods;
    }
    public void setAllowedPublicAttributeMethods(Properties allowedPublicAttributeMethods) {
        this.allowedPublicAttributeMethods = allowedPublicAttributeMethods;
    }

    public List<ContentModelReference> getContentModelReferences() {
        return contentModelReferences;
    }
    protected void setContentModelReferences(List<ContentModelReference> contentModelReferences) {
        this.contentModelReferences = contentModelReferences;
    }

    protected IContentModelManager getContentModelManager() {
        return contentModelManager;
    }
    public void setContentModelManager(IContentModelManager contentModelManager) {
        this.contentModelManager = contentModelManager;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public IPageManager getPageManager() {
        return pageManager;
    }
    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }
    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

    private int strutsAction;
    private Integer modelId;
    private String contentType;
    private String description;
    private String contentShape;
    private String stylesheet;

    private List<String> allowedPublicContentMethods;
    private Properties allowedPublicAttributeMethods;

    private List<ContentModelReference> contentModelReferences;
    private Map<String, IPage> draftReferencedPages;
    private Map<String, IPage> onlineReferencedPages;

    private transient IContentModelManager contentModelManager;
    private IContentManager contentManager;
    private transient IPageManager pageManager;
    private transient IWidgetTypeManager widgetTypeManager;

}
