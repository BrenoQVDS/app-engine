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
package org.entando.entando.aps.system.services.guifragment;

import com.agiletec.aps.system.common.AbstractParameterizableService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import org.entando.entando.ent.exception.EntException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.guifragment.event.GuiFragmentChangedEvent;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author E.Santoboni
 */
public class GuiFragmentManager extends AbstractParameterizableService implements IGuiFragmentManager, GuiFragmentUtilizer {

    private static final EntLogger logger = EntLogFactory.getSanitizedLogger(GuiFragmentManager.class);

    private static final String UNIQUE_BY_TYPE_CACHE_GROUP = "GuiFragment_uniqueByWidgetTypeGroup";
    
    private static final String UNIQUE_BY_TYPE_CACHE_PREFIX = "GuiFragment_uniqueByWidgetType_";

    private static final String CODES_BY_TYPE_CACHE_GROUP = "GuiFragment_codesByWidgetTypeGroup";
    
    private static final String CODES_BY_TYPE_CACHE_PREXIX = "GuiFragment_codesByWidgetType_";

    private transient IGuiFragmentDAO guiFragmentDAO;

    private transient ICacheInfoManager cacheInfoManager;

    @Autowired
    @Qualifier(value = "GuiFragmentManagerParameterNames")
    public transient List<String> parameterNames;

    @Override
    public void init() throws Exception {
        logger.debug("{} ready.", this.getClass().getName());
    }

    @Override
    @Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#code)")
    public GuiFragment getGuiFragment(String code) throws EntException {
        GuiFragment guiFragment = null;
        try {
            guiFragment = this.getGuiFragmentDAO().loadGuiFragment(code);
        } catch (Throwable t) {
            logger.error("Error loading guiFragment with code '{}'", code, t);
            throw new EntException("Error loading guiFragment with code: " + code, t);
        }
        return guiFragment;
    }

    @Override
    public SearcherDaoPaginatedResult<GuiFragment> getGuiFragments(List<FieldSearchFilter> filters) throws EntException {
        SearcherDaoPaginatedResult<GuiFragment> pagedResult = null;
        try {
            List<GuiFragment> fragments = new ArrayList<>();
            FieldSearchFilter[] filtersArray = filters.toArray(new FieldSearchFilter[filters.size()]);
            int count = this.getGuiFragmentDAO().countGuiFragments(filtersArray);
            List<String> codes = this.searchGuiFragments(filtersArray);
            for (String code : codes) {
                fragments.add(this.getGuiFragment(code));
            }
            pagedResult = new SearcherDaoPaginatedResult<>(count, fragments);
        } catch (Throwable t) {
            logger.error("Error searching GuiFragments", t);
            throw new EntException("Error searching GuiFragments", t);
        }
        return pagedResult;
    }

    @Override
    public List<String> getGuiFragments() throws EntException {
        return this.searchGuiFragments(null);
    }

    @Override
    public List<String> searchGuiFragments(FieldSearchFilter[] filters) throws EntException {
        List<String> guiFragments = null;
        try {
            FieldSearchFilter filter = new FieldSearchFilter("code");
            filter.setOrder(FieldSearchFilter.Order.ASC);
            filters = this.addFilter(filters, filter);
            guiFragments = this.getGuiFragmentDAO().searchGuiFragments(filters);
        } catch (Throwable t) {
            logger.error("Error searching GuiFragments", t);
            throw new EntException("Error searching GuiFragments", t);
        }
        return guiFragments;
    }
    
    protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
        if (null == filters) {
            return new FieldSearchFilter[]{filterToAdd};
        }
        return ArrayUtils.add(filters, filterToAdd);
    }
    
    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#guiFragment.code)")
    public void addGuiFragment(GuiFragment guiFragment) throws EntException {
        try {
            this.getGuiFragmentDAO().insertGuiFragment(guiFragment);
            this.notifyGuiFragmentChangedEvent(guiFragment, GuiFragmentChangedEvent.INSERT_OPERATION_CODE);
            this.evictGroups();
        } catch (Throwable t) {
            logger.error("Error adding GuiFragment", t);
            throw new EntException("Error adding GuiFragment", t);
        }
    }

    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#guiFragment.code)")
    public void updateGuiFragment(GuiFragment guiFragment) throws EntException {
        try {
            this.getGuiFragmentDAO().updateGuiFragment(guiFragment);
            this.notifyGuiFragmentChangedEvent(guiFragment, GuiFragmentChangedEvent.UPDATE_OPERATION_CODE);
            this.evictGroups();
        } catch (Throwable t) {
            logger.error("Error updating GuiFragment", t);
            throw new EntException("Error updating GuiFragment " + guiFragment, t);
        }
    }

    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#code)")
    public void deleteGuiFragment(String code) throws EntException {
        try {
            GuiFragment guiFragment = this.getGuiFragment(code);
            this.getGuiFragmentDAO().removeGuiFragment(code);
            this.notifyGuiFragmentChangedEvent(guiFragment, GuiFragmentChangedEvent.REMOVE_OPERATION_CODE);
            this.evictGroups();
        } catch (Throwable t) {
            logger.error("Error deleting GuiFragment with code {}", code, t);
            throw new EntException("Error deleting GuiFragment with code:" + code, t);
        }
    }

    private void evictGroups() {
        this.getCacheInfoManager().flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, UNIQUE_BY_TYPE_CACHE_GROUP);
        this.getCacheInfoManager().flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, CODES_BY_TYPE_CACHE_GROUP);
    }

    private void notifyGuiFragmentChangedEvent(GuiFragment guiFragment, int operationCode) {
        GuiFragmentChangedEvent event = new GuiFragmentChangedEvent();
        event.setGuiFragment(guiFragment);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    @Override
    @Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            key = "'GuiFragment_uniqueByWidgetType_'.concat(#widgetTypeCode)", condition = "null != #result")
    public GuiFragment getUniqueGuiFragmentByWidgetType(String widgetTypeCode) throws EntException {
        GuiFragment guiFragment = null;
        try {
            List<String> fragmentCodes = this.getGuiFragmentCodesByWidgetType(widgetTypeCode);
            if (null != fragmentCodes && !fragmentCodes.isEmpty()) {
                if (fragmentCodes.size() > 1) {
                    logger.warn("There are more then one fragment joined with widget '{}'", widgetTypeCode);
                }
                guiFragment = this.getGuiFragment(fragmentCodes.get(0));
                String cacheKey = UNIQUE_BY_TYPE_CACHE_PREFIX + widgetTypeCode;
                this.getCacheInfoManager().putInGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, cacheKey, new String[]{UNIQUE_BY_TYPE_CACHE_GROUP});
            }
        } catch (Throwable t) {
            logger.error("Error loading guiFragment by widget '{}'", widgetTypeCode, t);
            throw new EntException("Error loading guiFragment by widget " + widgetTypeCode, t);
        }
        return guiFragment;
    }

    @Override
    @Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_codesByWidgetType_'.concat(#widgetTypeCode)")
    public List<String> getGuiFragmentCodesByWidgetType(String widgetTypeCode) throws EntException {
        List<String> codes = null;
        try {
            FieldSearchFilter filter = new FieldSearchFilter("widgettypecode", widgetTypeCode, false);
            filter.setOrder(FieldSearchFilter.Order.ASC);
            FieldSearchFilter[] filters = {filter};
            codes = this.searchGuiFragments(filters);
            String cacheKey = CODES_BY_TYPE_CACHE_PREXIX + widgetTypeCode;
            this.getCacheInfoManager().putInGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, cacheKey, new String[]{CODES_BY_TYPE_CACHE_GROUP});
        } catch (Throwable t) {
            logger.error("Error loading fragments code by widget '{}'", widgetTypeCode, t);
            throw new EntException("Error loading fragment codes by widget " + widgetTypeCode, t);
        }
        return codes;
    }

    @Override
    public List getGuiFragmentUtilizers(String guiFragmentCode) throws EntException {
        List<GuiFragment> utilizers = new ArrayList<>();
        try {
            String strToSearch = "code=\"" + guiFragmentCode + "\"";
            Set<String> results = new HashSet<>();
            results.addAll(this.searchFragments(strToSearch, "gui"));
            results.addAll(this.searchFragments(strToSearch, "defaultgui"));
            if (!results.isEmpty()) {
                Pattern patternTag = Pattern.compile("<@wp\\.fragment.*code=\"" + guiFragmentCode + "\".*/>", Pattern.MULTILINE);
                Pattern patternFreem = Pattern.compile("<#include.*\"" + guiFragmentCode + "\".*>", Pattern.MULTILINE);
                Iterator<String> it = results.iterator();
                while (it.hasNext()) {
                    String fcode = it.next();
                    GuiFragment fragment = this.getGuiFragment(fcode);
                    if (this.scanTemplate(patternTag, fragment.getGui()) 
                            || this.scanTemplate(patternTag, fragment.getDefaultGui())
                            || this.scanTemplate(patternFreem, fragment.getGui())
                            || this.scanTemplate(patternFreem, fragment.getDefaultGui())) {
                        utilizers.add(fragment);
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Error extracting utilizers", t);
            throw new EntException("Error extracting utilizers", t);
        }
        return utilizers;
    }

    protected boolean scanTemplate(Pattern pattern, String template) {
        boolean check = false;
        if (StringUtils.isNotBlank(template)) {
            Matcher matcher = pattern.matcher(template);
            if (matcher.find()) {
                check = true;
            }
        }
        return check;
    }

    protected Set<String> searchFragments(String strToSearch, String column) throws EntException {
        FieldSearchFilter<String> filterCode = new FieldSearchFilter<>(column, strToSearch, true);
        FieldSearchFilter<String> filterTag = new FieldSearchFilter<>(column, "<@wp.fragment", true);
        FieldSearchFilter[] filters1 = new FieldSearchFilter[]{filterCode, filterTag};
        List<String> result1 = this.searchGuiFragments(filters1);
        FieldSearchFilter<String> filterFreem = new FieldSearchFilter<>(column, "<#include", true);
        FieldSearchFilter[] filters2 = new FieldSearchFilter[]{filterCode, filterFreem};
        List<String> result2 = this.searchGuiFragments(filters2);
        Set<String> result = new HashSet<>();
        result.addAll(result1);
        result.addAll(result2);
        return result;
    }

    @Override
    @Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_pluginCodes'")
    public List<String> loadGuiFragmentPluginCodes() throws EntException {
        List<String> codes = null;
        try {
            codes = this.getGuiFragmentDAO().loadGuiFragmentPluginCodes();
        } catch (Throwable t) {
            logger.error("Error loading guiFragment plugin codes", t);
            throw new EntException("Error loading guiFragment plugin codes", t);
        }
        return codes;
    }

    @Override
    protected List<String> getParameterNames() {
        return parameterNames;
    }

    public void setGuiFragmentDAO(IGuiFragmentDAO guiFragmentDAO) {
        this.guiFragmentDAO = guiFragmentDAO;
    }
    protected IGuiFragmentDAO getGuiFragmentDAO() {
        return guiFragmentDAO;
    }

    protected ICacheInfoManager getCacheInfoManager() {
        return this.cacheInfoManager;
    }
    @Autowired
    public void setCacheInfoManager(ICacheInfoManager cacheInfoManager) {
        this.cacheInfoManager = cacheInfoManager;
    }

}
