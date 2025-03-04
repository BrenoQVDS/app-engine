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
package org.entando.entando.web.health;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.health.IHealthService;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/health")
public class HealthController {

    private final IHealthService healthService;
    private boolean isExtendedCheck;

    @Autowired
    public HealthController(IHealthService healthService, @Value("${entando.app.engine.health.check.type:standard}") String checkDb) {
        this.healthService = healthService;
        this.isExtendedCheck = StringUtils.equalsIgnoreCase(checkDb, "extended");
    }

    @GetMapping
    public ResponseEntity<Void> isHealthy() {
        if(isExtendedCheck) {
            log.debug("health check with db");
            return new ResponseEntity<>(this.healthService.isHealthy() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            log.debug("health check standard");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}