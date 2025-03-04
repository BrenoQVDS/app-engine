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
package org.entando.entando.keycloak.services;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;
import java.net.URI;
import java.util.List;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.tenants.TenantManager;
import org.entando.entando.keycloak.services.oidc.OpenIDConnectService;
import org.entando.entando.keycloak.services.oidc.exception.OidcException;
import org.entando.entando.keycloak.services.oidc.model.AuthResponse;
import org.entando.entando.keycloak.services.oidc.model.UserRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {
    @Mock
    private TenantManager tenantManager;
    @Mock
    private KeycloakConfiguration keycloakConfiguration;
    @Mock
    private OpenIDConnectService openIDConnectService;
    @Mock
    private RestTemplate restTemplate;

    private KeycloakService keycloakService;

    @BeforeEach
    public void setUp() {
        keycloakService = new KeycloakService(keycloakConfiguration, openIDConnectService, restTemplate);
    }

    @Test
    void shouldReturnEmptyList() throws Exception {

        Mockito.when(keycloakConfiguration.getAuthUrl()).thenReturn("http://localhost/auth");
        Mockito.when(keycloakConfiguration.getRealm()).thenReturn("my-realm");

        AuthResponse resp = new AuthResponse();
        resp.setAccessToken("access-token-fake");
        Mockito.when(openIDConnectService.authenticateAPI()).thenReturn(resp);
        URI uri = UriComponentsBuilder.fromUriString("http://localhost/auth/admin/realms/my-realm/users").build().toUri();
        Mockito.when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), any(), eq(UserRepresentation[].class))).thenReturn(
                ResponseEntity.ok(null)
        );

        List<UserRepresentation> users = keycloakService.listUsers();
        Assertions.assertTrue(users.isEmpty());

    }



    @Test
    void shouldThrowException() throws Exception {
        Mockito.when(keycloakConfiguration.getAuthUrl()).thenReturn("http://localhost/auth");
        Mockito.when(keycloakConfiguration.getRealm()).thenReturn("my-realm");

        Mockito.when(openIDConnectService.authenticateAPI()).thenThrow(new OidcException(new Exception("test-message")));
        Assertions.assertThrows(RuntimeException.class, () ->keycloakService.listUsers());

        AuthResponse resp = new AuthResponse();
        resp.setAccessToken("access-token-fake");
        Mockito.reset(openIDConnectService);
        Mockito.when(openIDConnectService.authenticateAPI()).thenReturn(resp);
        URI uri = UriComponentsBuilder.fromUriString("http://localhost/auth/admin/realms/my-realm/users").build().toUri();
        Mockito.when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), any(), eq(UserRepresentation[].class))).thenThrow(
                new HttpClientErrorException(HttpStatus.FORBIDDEN)
        );
        Assertions.assertThrows(RestServerError.class, () ->keycloakService.listUsers());

        Mockito.reset(restTemplate);
        Mockito.when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), any(), eq(UserRepresentation[].class))).thenThrow(
                new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        );
        Assertions.assertThrows(HttpClientErrorException.class, () ->keycloakService.listUsers());
    }
}
