/*
 * Copyright 2022-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.tenants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author E.Santoboni
 */
public class TenantConfig implements Serializable {

    public static final String TENANT_CODE_PROPERTY = "tenantCode";
    public static final String KC_ENABLED_PROPERTY = "kcEnabled";
    public static final String KC_AUTH_URL_PROPERTY = "kcAuthUrl";
    public static final String KC_REALM_PROPERTY = "kcRealm";
    public static final String KC_CLIENT_ID_PROPERTY = "kcClientId";
    public static final String KC_CLIENT_SECRET_PROPERTY = "kcClientSecret";
    public static final String KC_PUBLIC_CLIENT_PROPERTY = "kcPublicClientId";
    public static final String KC_SECURE_URIS_PROPERTY = "kcSecureUris";
    public static final String KC_DEFAULT_AUTH_PROPERTY = "kcDefaultAuthorizations";
    public static final String DB_DRIVER_CLASS_NAME_PROPERTY = "dbDriverClassName";
    public static final String DB_URL_PROPERTY = "dbUrl";
    public static final String DB_USERNAME_PROPERTY = "dbUsername";
    public static final String DB_PASSWORD_PROPERTY = "dbPassword";
    public static final String DOMAIN_PREFIX_PROPERTY = "domainPrefix";
    public static final String DB_MAX_TOTAL_PROPERTY = "dbMaxTotal";
    public static final String DB_MAX_IDLE_PROPERTY = "dbMaxIdle";
    public static final String DB_MAX_WAIT_MS_PROPERTY = "dbMaxWaitMillis";
    public static final String DB_INITIAL_SIZE_PROPERTY = "dbInitialSize";
    private Map<String, String> configs;

    public TenantConfig(Map<String,String> c) {
        configs = c;
    }

    public TenantConfig(TenantConfig t) {
        configs = t.getAll();
    }

/*
    @Override
    public TenantConfig clone() {
        TenantConfig clone = new TenantConfig();
        clone.putAll(configs);
        return clone;
    }
*/

    protected Map<String, String> getAll(){
        return configs;
    }

    protected void putAll(Map<String,String> map){
        configs = map;
    }


    public String getTenantCode() {
        return configs.get(TENANT_CODE_PROPERTY);
    }

    public boolean isKcEnabled() {
        String enabled = configs.get(KC_ENABLED_PROPERTY);
        return BooleanUtils.toBoolean(enabled);
    }

    public String getKcAuthUrl() {
        return configs.get(KC_AUTH_URL_PROPERTY);
    }

    public String getKcRealm() {
        return configs.get(KC_REALM_PROPERTY);
    }

    public String getKcClientId() {
        return configs.get(KC_CLIENT_ID_PROPERTY);
    }

    public String getKcClientSecret() {
        return configs.get(KC_CLIENT_SECRET_PROPERTY);
    }

    public String getKcPublicClientId() {
        return configs.get(KC_PUBLIC_CLIENT_PROPERTY);
    }

    public String getKcSecureUris() {
        return configs.get(KC_SECURE_URIS_PROPERTY);
    }

    public String getKcDefaultAuthorizations() {
        return configs.get(KC_DEFAULT_AUTH_PROPERTY);
    }

    public String getDbDriverClassName() {
        return configs.get(DB_DRIVER_CLASS_NAME_PROPERTY);
    }

    public String getDbUrl() {
        return configs.get(DB_URL_PROPERTY);
    }

    public String getDbUsername() {
        return configs.get(DB_USERNAME_PROPERTY);
    }

    public String getDbPassword() {
        return configs.get(DB_PASSWORD_PROPERTY);
    }

    public String getDomainPrefix() {
        return configs.get(DOMAIN_PREFIX_PROPERTY);
    }

    public Optional<String> getProperty(String name) {
        return Optional.ofNullable(configs.get(name));
    }

    public int getMaxTotal() {
        return getDbConnectionParam(TenantConfig.DB_MAX_TOTAL_PROPERTY, ITenantManager.DEFAULT_DB_MAX_TOTAL);
    }

    public int getMaxIdle() {
        return getDbConnectionParam(TenantConfig.DB_MAX_IDLE_PROPERTY, ITenantManager.DEFAULT_DB_MAX_IDLE);
    }

    public int getMaxWaitMillis() {
        return getDbConnectionParam(TenantConfig.DB_MAX_WAIT_MS_PROPERTY, ITenantManager.DEFAULT_DB_MAX_WAIT_MS);
    }

    public int getInitialSize() {
        return getDbConnectionParam(TenantConfig.DB_INITIAL_SIZE_PROPERTY, ITenantManager.DEFAULT_DB_INITIAL_SIZE);
    }

    private int getDbConnectionParam(String paramName, int defaultValue) {
        return getProperty(paramName)
                .filter(StringUtils::isNotBlank)
                .map(i -> NumberUtils.toInt(i, defaultValue))
                .orElse(defaultValue);
    }

}