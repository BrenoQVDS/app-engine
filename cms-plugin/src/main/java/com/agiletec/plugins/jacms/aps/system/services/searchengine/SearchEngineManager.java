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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingObserver;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.entando.entando.ent.util.EntLogging.EntLogger;

/**
 * Servizio detentore delle operazioni di indicizzazione di oggetti ricercabili
 * tramite motore di ricerca.
 *
 * @author M.Diana - E.Santoboni
 */
public class SearchEngineManager extends AbstractService
        implements ICmsSearchEngineManager, PublicContentChangedObserver, EntityTypesChangingObserver {

    private static final EntLogger logger = EntLogFactory.getSanitizedLogger(SearchEngineManager.class);

    private ISearchEngineDAOFactory factory;

    private IIndexerDAO indexerDao;
    private ISearcherDAO searcherDao;

    private int status;
    private LastReloadInfo lastReloadInfo;
    private ConcurrentLinkedDeque<PublicContentChangedEvent> publicContentChangedEventQueue = new ConcurrentLinkedDeque<>();

    public static final String RELOAD_THREAD_NAME_PREFIX = "RELOAD_INDEX_";

    private String newTempSubDirectory;

    private IContentManager contentManager;

    @Override
    public void init() throws Exception {
        this.setIndexerDao(this.getFactory().getIndexer());
        this.setSearcherDao(this.getFactory().getSearcher());
        logger.info("** Default Search Engine active **");
    }

    @Override
    public void refresh() throws Throwable {
        this.release();
        this.lastReloadInfo = null;
        this.setStatus(STATUS_READY);
        this.getFactory().init();
        this.init();
    }

    @Override
    public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
        if (this.getStatus() == STATUS_RELOADING_INDEXES_IN_PROGRESS) {
            this.publicContentChangedEventQueue.add(event);
        } else {
            this.manageEvent(event);
        }
    }

    private void manageEvent(PublicContentChangedEvent event) {
        Content content = event.getContent();
        try {
            switch (event.getOperationCode()) {
                case PublicContentChangedEvent.INSERT_OPERATION_CODE:
                    this.addEntityToIndex(content);
                    break;
                case PublicContentChangedEvent.REMOVE_OPERATION_CODE:
                    this.deleteIndexedEntity(content.getId());
                    break;
                case PublicContentChangedEvent.UPDATE_OPERATION_CODE:
                    this.updateIndexedEntity(content);
                    break;
            }
        } catch (Throwable t) {
            logger.error("Error on event notification", t);
        }
    }

    public void sellOfQueueEvents() {
        while (this.publicContentChangedEventQueue.peekLast() != null) {
            PublicContentChangedEvent event = this.publicContentChangedEventQueue.pollLast();
            this.manageEvent(event);
        }
    }

    @Override
    public Thread startReloadContentsReferences() throws EntException {
        String newTempSubDir = "indexdir" + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
        return this.startReloadContentsReferences(newTempSubDir);
    }

    @Override
    public Thread startReloadContentsReferences(String subDirectory) throws EntException {
        IndexLoaderThread loaderThread = null;
        if (this.getStatus() == STATUS_READY || this.getStatus() == STATUS_NEED_TO_RELOAD_INDEXES) {
            try {
                this.newTempSubDirectory = subDirectory;
                IIndexerDAO newIndexer = this.getFactory().getIndexer(newTempSubDirectory);
                loaderThread = new IndexLoaderThread(this, this.getContentManager(), newIndexer);
                String threadName = RELOAD_THREAD_NAME_PREFIX + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
                loaderThread.setName(threadName);
                this.setStatus(STATUS_RELOADING_INDEXES_IN_PROGRESS);
                loaderThread.start();
                logger.info("Reload Contents References job started");
            } catch (Throwable t) {
                throw new EntException("Error reloading Contents References", t);
            }
            Map<String, String> properties = new HashMap<>();
            properties.put("eventType", "reload");
            ApsEvent event = new ApsEvent(JacmsSystemConstants.SEARCH_ENGINE_EVENT_CHANNEL, properties) {
                @Override
                public void notify(IManager im) {
                    return;
                }
                @Override
                public Class<?> getObserverInterface() {
                    return null;
                }
            };
            this.notifyEvent(event);
        } else {
            logger.info("Reload Contents References job suspended: current status: {}", this.getStatus());
        }
        return loaderThread;
    }

    @Override
    public void updateFromEntityTypesChanging(EntityTypesChangingEvent event) {
        if (((IManager) this.getContentManager()).getName().equals(event.getEntityManagerName())) {
            if (this.getStatus() == STATUS_NEED_TO_RELOAD_INDEXES) {
                return;
            }
            boolean needToReload = false;
            if (event.getOperationCode() == EntityTypesChangingEvent.INSERT_OPERATION_CODE) {
                return;
            } else if (event.getOperationCode() == EntityTypesChangingEvent.REMOVE_OPERATION_CODE) {
                needToReload = true;
            } else if (event.getOperationCode() == EntityTypesChangingEvent.UPDATE_OPERATION_CODE) {
                needToReload = this.verifyReloadingNeeded(event.getOldEntityType(), event.getNewEntityType());
            }
            if (needToReload == true) {
                this.setStatus(STATUS_NEED_TO_RELOAD_INDEXES);
            }
        }
    }

    protected boolean verifyReloadingNeeded(IApsEntity oldEntityType, IApsEntity newEntityType) {
        List<AttributeInterface> attributes = oldEntityType.getAttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface oldAttribute = attributes.get(i);
            AttributeInterface newAttribute = (AttributeInterface) newEntityType.getAttribute(oldAttribute.getName());
            boolean isOldAttributeIndexeable = (oldAttribute.getIndexingType() != null && oldAttribute.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
            boolean isNewAttributeIndexeable = (newAttribute != null && newAttribute.getIndexingType() != null && newAttribute.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
            if ((isOldAttributeIndexeable && !isNewAttributeIndexeable) || (!isOldAttributeIndexeable && isNewAttributeIndexeable)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addEntityToIndex(IApsEntity entity) throws EntException {
        try {
            this.getIndexerDao().add(entity);
        } catch (EntException e) {
            logger.error("Error saving content to index", e);
            throw e;
        }
    }

    @Override
    public void deleteIndexedEntity(String entityId) throws EntException {
        try {
            this.getIndexerDao().delete(IIndexerDAO.CONTENT_ID_FIELD_NAME, entityId);
        } catch (EntException e) {
            logger.error("Error deleting content {} from index", entityId, e);
            throw e;
        }
    }

    public void notifyEndingIndexLoading(LastReloadInfo info, IIndexerDAO newIndexerDAO) {
        try {
            if (info.getResult() == LastReloadInfo.ID_SUCCESS_RESULT) {
                ISearcherDAO newSearcherDAO = this.getFactory().getSearcher(this.newTempSubDirectory);
                this.getFactory().updateSubDir(newTempSubDirectory);
                this.setIndexerDao(newIndexerDAO);
                this.setSearcherDao(newSearcherDAO);
                this.lastReloadInfo = info;
            } else if (null != this.newTempSubDirectory) {
                this.getFactory().deleteSubDirectory(this.newTempSubDirectory);
            }
        } catch (Throwable t) {
            logger.error("error updating LastReloadInfo", t);
        } finally {
            if (this.getStatus() != STATUS_NEED_TO_RELOAD_INDEXES) {
                this.setStatus(STATUS_READY);
            }
            this.newTempSubDirectory = null;
        }
    }

    @Override
    public LastReloadInfo getLastReloadInfo() {
        return this.lastReloadInfo;
    }

    @Override
    public List<String> searchId(String sectionCode, String langCode,
            String word, Collection<String> allowedGroups) throws EntException {
        return this.searchEntityId(langCode, word, allowedGroups);
    }

    @Override
    public List<String> searchEntityId(String langCode, String word,
            Collection<String> allowedGroups) throws EntException {
        SearchEngineFilter[] filters = new SearchEngineFilter[0];
        if (StringUtils.isNotEmpty(langCode) && StringUtils.isNotEmpty(word)) {
            SearchEngineFilter filter = new SearchEngineFilter(langCode, word);
            filter.setFullTextSearch(true);
            filters = ArrayUtils.add(filters, filter);
        }
        return this.searchEntityId(filters, null, allowedGroups);
    }

    public List<String> searchEntityId(SearchEngineFilter[] filters, Collection<ITreeNode> categories, Collection<String> allowedGroups) throws EntException {
        List<String> contentsId = null;
        try {
            contentsId = this.getSearcherDao().searchContentsId(filters, this.extractCategoryFilters(categories), allowedGroups);
        } catch (Throwable t) {
            logger.error("Error searching content id list. ", t);
            throw new EntException("Error searching content id list", t);
        }
        return contentsId;
    }
    
    @Override
    public FacetedContentsResult searchFacetedEntities(SearchEngineFilter[] filters, Collection<ITreeNode> categories, Collection<String> allowedGroups) throws EntException {
        FacetedContentsResult result = null;
        try {
            result = this.getSearcherDao().searchFacetedContents(filters, this.extractCategoryFilters(categories), allowedGroups);
        } catch (Throwable t) {
            logger.error("Error searching faceted contents", t);
            throw new EntException("Error searching faceted contents", t);
        }
        return result;
    }
    
    private SearchEngineFilter[] extractCategoryFilters(Collection<ITreeNode> categories) {
        SearchEngineFilter[] categoryFilterArray = null;
        if (null != categories) {
            List<SearchEngineFilter> categoryFilters = categories.stream().filter(c -> c != null).map(c -> new SearchEngineFilter("category", false, c.getCode())).collect(Collectors.toList());
            categoryFilterArray = categoryFilters.toArray(new SearchEngineFilter[categoryFilters.size()]);
        }
        return categoryFilterArray;
    }
    
    @Override
    public FacetedContentsResult searchFacetedEntities(SearchEngineFilter[] filters, SearchEngineFilter[] categories, Collection<String> allowedGroups) throws EntException {
        FacetedContentsResult result = null;
        try {
            result = this.getSearcherDao().searchFacetedContents(filters, categories, allowedGroups);
        } catch (Throwable t) {
            logger.error("Error searching faceted contents", t);
            throw new EntException("Error searching faceted contents", t);
        }
        return result;
    }

    @Override
    public List<String> loadContentsId(SearchEngineFilter[] filters, SearchEngineFilter[] facetNodeCodes, List<String> allowedGroups) throws EntException {
        List<String> contentsId = null;
        try {
            contentsId = this.getSearcherDao().searchContentsId(filters, facetNodeCodes, allowedGroups);
        } catch (Throwable t) {
            logger.error("Error searching contents", t);
            throw new EntException("Error searching contents", t);
        }
        return contentsId;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    protected void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void updateIndexedEntity(IApsEntity entity) throws EntException {
        try {
            this.deleteIndexedEntity(entity.getId());
            this.addEntityToIndex(entity);
        } catch (EntException e) {
            logger.error("Error updating content", e);
            throw e;
        }
    }

    private void checkUpgradeDao() {
        try {
            if (!this.getFactory().checkCurrentSubfolder()) {
                logger.warn("Upgrading Search Engine DAOs");
                this.getFactory().init();
                this.init();
            }
        } catch (Exception e) {
            logger.error("Error upgrading DAOs", e);
        }
    }

    protected IIndexerDAO getIndexerDao() {
        this.checkUpgradeDao();
        return indexerDao;
    }

    protected void setIndexerDao(IIndexerDAO indexerDao) {
        this.indexerDao = indexerDao;
    }

    protected ISearcherDAO getSearcherDao() {
        this.checkUpgradeDao();
        return searcherDao;
    }

    protected void setSearcherDao(ISearcherDAO searcherDao) {
        this.searcherDao = searcherDao;
    }

    protected ISearchEngineDAOFactory getFactory() {
        return factory;
    }

    public void setFactory(ISearchEngineDAOFactory factory) {
        this.factory = factory;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

}
