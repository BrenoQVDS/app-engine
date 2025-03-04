/*
 * Copyright 2021-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsolr.web.content;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import java.util.Arrays;
import java.util.List;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.plugins.jpsolr.aps.system.content.IAdvContentFacetManager;
import org.entando.entando.plugins.jpsolr.aps.system.solr.model.SolrFacetedContentsResult;
import org.entando.entando.plugins.jpsolr.conditions.SolrActive;
import org.entando.entando.plugins.jpsolr.web.content.model.AdvRestContentListRequest;
import org.entando.entando.plugins.jpsolr.web.content.model.SolrContentPagedMetadata;
import org.entando.entando.plugins.jpsolr.web.content.model.SolrFacetedPagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author E.Santoboni
 */
@SolrActive(true)
@RestController
@RequestMapping(value = "/plugins/advcontentsearch")
public class AdvContentSearchController {

    private static final Logger logger = LoggerFactory.getLogger(AdvContentSearchController.class);

    private final IAdvContentFacetManager advContentFacetManager;

    @Autowired
    public AdvContentSearchController(IAdvContentFacetManager advContentFacetManager) {
        this.advContentFacetManager = advContentFacetManager;
    }

    protected AbstractPaginationValidator getPaginationValidator() {
        return new AbstractPaginationValidator() {
            @Override
            public boolean supports(Class<?> type) {
                return true;
            }

            @Override
            public void validate(Object o, Errors errors) {
                //nothing to do
            }

            @Override
            protected String getDefaultSortProperty() {
                return IContentManager.CONTENT_CREATION_DATE_FILTER_KEY;
            }

            @Override
            public boolean isValidField(String fieldName, Class<?> type) {
                if (fieldName.contains(".")) {
                    return true;
                } else {
                    return Arrays.asList(IContentManager.METADATA_FILTER_KEYS).contains(fieldName);
                }
            }
        };
    }

    @GetMapping(value = "/contents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<String>> getContents(AdvRestContentListRequest requestList,
            @RequestAttribute(value = "user", required = false) UserDetails currentUser) {
        logger.debug("getting contents with request {}", requestList);
        this.getPaginationValidator().validateRestListRequest(requestList, String.class);
        SolrFacetedContentsResult facetedResult = this.advContentFacetManager
                .getFacetedContents(requestList, currentUser);
        List<String> result = facetedResult.getContentsId();
        SolrContentPagedMetadata<String> pagedMetadata = new SolrContentPagedMetadata<>(requestList, facetedResult.getTotalSize());
        pagedMetadata.setBody(result);
        boolean isGuest = (null == currentUser || currentUser.getUsername()
                .equalsIgnoreCase(SystemConstants.GUEST_USER_NAME));
        pagedMetadata.getAdditionalParams().put("guestUser", String.valueOf(isGuest));
        return new ResponseEntity<>(new PagedRestResponse<>(pagedMetadata), HttpStatus.OK);
    }

    @GetMapping(value = "/facetedcontents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<FacetedContentsResult, SolrFacetedPagedMetadata>> getFacetedContents(
            AdvRestContentListRequest requestList,
            @RequestAttribute(value = "user", required = false) UserDetails currentUser) {
        logger.debug("getting contents with request {}", requestList);
        this.getPaginationValidator().validateRestListRequest(requestList, String.class);
        SolrFacetedContentsResult result = this.advContentFacetManager
                .getFacetedContents(requestList, currentUser);
        boolean isGuest = (null == currentUser || currentUser.getUsername()
                .equalsIgnoreCase(SystemConstants.GUEST_USER_NAME));
        requestList.setGuestUser(isGuest);
        SolrFacetedPagedMetadata pagedMetadata = new SolrFacetedPagedMetadata(requestList, result.getTotalSize());
        pagedMetadata.setBody(result);
        pagedMetadata.getAdditionalParams().put("guestUser", String.valueOf(isGuest));
        return new ResponseEntity<>(new RestResponse<>(result, pagedMetadata), HttpStatus.OK);
    }

}