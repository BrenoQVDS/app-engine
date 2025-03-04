/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.cache;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Manager of the System Cache
 *
 * @author E.Santoboni
 */
public class CacheInfoManager extends AbstractService implements ICacheInfoManager, PageChangedObserver {

    private static final EntLogger logger = EntLogFactory.getSanitizedLogger(CacheInfoManager.class);

    private CacheManager springCacheManager;

    @Override
    public void init() throws Exception {
        logger.debug("{} (cache info service initialized) ready", this.getClass().getName());
    }

    @Override
    public void setExpirationTime(String targetCache, String key, int expiresInMinute) {
        Date expirationTime = DateUtils.addMinutes(new Date(), expiresInMinute);
        this.setExpirationTime(targetCache, key, expirationTime);
    }
    
    @Override
    public void setExpirationTime(String targetCache, String key, long expiresInSeconds) {
        Date expirationTime = DateUtils.addSeconds(new Date(), (int) expiresInSeconds);
        this.setExpirationTime(targetCache, key, expirationTime);
    }

    @Override
    public void setExpirationTime(String targetCache, String key, Date expirationTime) {
        Cache cache = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        Map<String, Date> expirationTimes = this.get(cache, EXPIRATIONS_CACHE_NAME_PREFIX + targetCache, Map.class);
        if (null == expirationTimes) {
            expirationTimes = new HashMap<String, Date>();
        }
        expirationTimes.put(key, expirationTime);
        cache.put(EXPIRATIONS_CACHE_NAME_PREFIX + targetCache, expirationTimes);
    }

    @Override
    public void updateFromPageChanged(PageChangedEvent event) {
        IPage page = event.getPage();
        if (null != page) {
            String pageCacheGroupName = SystemConstants.PAGES_CACHE_GROUP_PREFIX + page.getCode();
            this.flushGroup(DEFAULT_CACHE_NAME, pageCacheGroupName);
        }
    }

    @Override
    protected void release() {
        super.release();
        this.destroy();
    }

    @Override
    public void destroy() {
        this.flushAll(CACHE_INFO_MANAGER_CACHE_NAME);
        this.flushAll(DEFAULT_CACHE_NAME);
        super.destroy();
    }

    public void flushAll() {
        Collection<String> cacheNames = this.getSpringCacheManager().getCacheNames();
        Iterator<String> iter = cacheNames.iterator();
        while (iter.hasNext()) {
            String cacheName = iter.next();
            this.flushAll(cacheName);
        }
    }

    public void flushAll(String cacheName) {
        Cache cacheOfGroup = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        cacheOfGroup.evict(GROUP_CACHE_NAME_PREFIX + cacheName);
        Cache cache = this.getCache(cacheName);
        cache.clear();
    }

    @Override
    public void flushEntry(String targetCache, String key) {
        this.getCache(targetCache).evict(key);
    }

    /**
     * Put an object on the given cache.
     *
     * @param targetCache The cache name
     * @param key The key
     * @param obj The object to put into cache.
     */
    @Override
    public void putInCache(String targetCache, String key, Object obj) {
        Cache cache = this.getCache(targetCache);
        cache.put(key, obj);
    }
    @Override
    public void putInCache(String targetCache, String key, Object obj, String[] groups) {
        Cache cache = this.getCache(targetCache);
        cache.put(key, obj);
        this.accessOnGroupMapping(targetCache, 1, groups, key);
    }

    @Override
    public void putInGroup(String targetCache, String key, String[] groups) {
        this.accessOnGroupMapping(targetCache, 1, groups, key);
    }

    @Override
    public void flushGroup(String targetCache, String group) {
        String[] groups = {group};
        this.accessOnGroupMapping(targetCache, -1, groups, null);
    }

    protected synchronized void accessOnGroupMapping(String targetCache, int operationId, String[] groups, String key) {
        Cache cache = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        Map<String, List<String>> objectsByGroup = this.get(cache, GROUP_CACHE_NAME_PREFIX + targetCache, Map.class);
        if (objectsByGroup != null) {
            objectsByGroup = new HashMap<>(objectsByGroup);
        }
        boolean updateMapInCache = false;
        if (operationId > 0) {
            //add
            if (null == objectsByGroup) {
                objectsByGroup = new HashMap<>();
            }
            for (String group : groups) {
                List<String> objectKeys = objectsByGroup.get(group);
                if (null == objectKeys) {
                    objectKeys = new ArrayList<>();
                    objectsByGroup.put(group, objectKeys);
                }
                if (!objectKeys.contains(key)) {
                    objectKeys.add(key);
                    updateMapInCache = true;
                }
            }
        } else {
            //remove
            if (null == objectsByGroup) {
                return;
            }
            for (String group : groups) {
                List<String> objectKeys = objectsByGroup.get(group);
                if (null != objectKeys) {
                    for (String extractedKey : objectKeys) {
                        this.flushEntry(targetCache, extractedKey);
                    }
                    objectsByGroup.remove(group);
                    updateMapInCache = true;
                }
            }
        }
        if (updateMapInCache) {
            cache.put(GROUP_CACHE_NAME_PREFIX + targetCache, objectsByGroup);
        }
    }

    protected Collection<Cache> getCaches() {
        Collection<Cache> caches = new ArrayList<Cache>();
        Iterator<String> iter = this.getSpringCacheManager().getCacheNames().iterator();
        while (iter.hasNext()) {
            String cacheName = iter.next();
            caches.add(this.getSpringCacheManager().getCache(cacheName));
        }
        return caches;
    }

    protected boolean isNotExpired(String targetCache, String key) {
        return !isExpired(targetCache, key);
    }

    @Override
    public boolean isExpired(String targetCache, String key) {
        if (StringUtils.isBlank(targetCache)) {
            targetCache = DEFAULT_CACHE_NAME;
        }
        Map<String, Date> expirationTimes = this.get(EXPIRATIONS_CACHE_NAME_PREFIX + targetCache, Map.class);
        if (null == expirationTimes) {
            return false;
        }
        Date expirationTime = expirationTimes.get(key);
        if (null == expirationTime) {
            return false;
        }
        if (expirationTime.before(new Date())) {
            logger.debug("Key {} of cache {} is expired", key, targetCache);
            Map<String, Date> newExpirationTimes = new HashMap<>(expirationTimes);
            newExpirationTimes.remove(key);
            this.putInCache(CACHE_INFO_MANAGER_CACHE_NAME, EXPIRATIONS_CACHE_NAME_PREFIX + targetCache, newExpirationTimes);
            return true;
        } else {
            return false;
        }
    }

    protected Cache getCache(String cacheName) {
        if (StringUtils.isBlank(cacheName)) {
            cacheName = DEFAULT_CACHE_NAME;
        }
        return this.getSpringCacheManager().getCache(cacheName);
    }
    
    @Override
    public Object getFromCache(String targetCache, String key) {
        return this.getFromCache(targetCache, key, Object.class);
    }

    @Override
    public <T> T getFromCache(String targetCache, String key, Class<T> requiredType) {
        if (isExpired(targetCache, key)) {
            this.flushEntry(targetCache, key);
            return null;
        }
        Cache cache = this.getCache(targetCache);
        return this.get(cache, key, requiredType);
    }

    protected <T> T get(String name, Class<T> requiredType) {
        Cache cache = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        return this.get(cache, name, requiredType);
    }

    protected <T> T get(Cache cache, String name, Class<T> requiredType) {
        Object value = cache.get(name);
        if (value instanceof Cache.ValueWrapper) {
            value = ((Cache.ValueWrapper) value).get();
        }
        return (T) value;
    }

    protected CacheManager getSpringCacheManager() {
        return springCacheManager;
    }
    @Autowired
    public void setSpringCacheManager(CacheManager springCacheManager) {
        this.springCacheManager = springCacheManager;
    }

    protected String getCacheName() {
        return CACHE_INFO_MANAGER_CACHE_NAME;
    }

}
