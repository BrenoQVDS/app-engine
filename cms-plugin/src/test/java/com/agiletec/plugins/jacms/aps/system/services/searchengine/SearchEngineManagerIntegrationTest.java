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

import static com.agiletec.aps.BaseTestCase.createRequestContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter.Order;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.common.tree.ITreeNode;
import org.entando.entando.ent.exception.EntException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.AttachAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import javax.servlet.ServletContext;
import org.entando.entando.aps.system.services.searchengine.FacetedContentsResult;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter.TextSearchOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;

/**
 * Test del servizio detentore delle operazioni sul motore di ricerca.
 *
 * @author E.Santoboni
 */
class SearchEngineManagerIntegrationTest extends BaseTestCase {

    private final String ROLE_FOR_TEST = "jacmstest:date";

    private IContentManager contentManager = null;
    private IResourceManager resourceManager = null;
    private ICmsSearchEngineManager searchEngineManager = null;
    private ICategoryManager categoryManager;
    
    @BeforeAll
    public static void setUp() throws Exception {
        try {
            ServletContext srvCtx = new MockServletContext("", new FileSystemResourceLoader());
            ApplicationContext applicationContext = (new CustomConfigTestUtils()).createApplicationContext(srvCtx);
            setApplicationContext(applicationContext);
            RequestContext reqCtx = createRequestContext(applicationContext, srvCtx);
            setRequestContext(reqCtx);
            setUserOnSession("guest");
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Test
    void testSearchAllContents() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            Set<String> allowedGroup = new HashSet<>();
            SearchEngineFilter[] filters = {};
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            List<String> freeContentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertNotNull(freeContentsId);
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            List<String> allContentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertNotNull(allContentsId);
            assertTrue(allContentsId.size() > freeContentsId.size());
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_1() throws Throwable {
        try {
            Content content_1 = this.createContent_1();
            this.searchEngineManager.deleteIndexedEntity(content_1.getId());
            this.searchEngineManager.addEntityToIndex(content_1);

            Content content_2 = this.createContent_2();
            this.searchEngineManager.deleteIndexedEntity(content_2.getId());
            this.searchEngineManager.addEntityToIndex(content_2);

            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "San meravigliosa", null);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains(content_1.getId()));
            contentsId = this.searchEngineManager.searchEntityId("en", "Petersburg wonderful", null);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains(content_1.getId()));
            contentsId = this.searchEngineManager.searchEntityId("en", "meravigliosa", null);
            assertNotNull(contentsId);
            assertFalse(contentsId.contains(content_1.getId()));
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_2() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();

            Set<String> allowedGroup = new HashSet<>();
            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "Corpo coach", allowedGroup);
            assertNotNull(contentsId);
            assertFalse(contentsId.contains("ART104"));

            allowedGroup.add("coach");
            contentsId = this.searchEngineManager.searchEntityId("it", "testo coach", allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains("ART104"));//coach content

            contentsId = this.searchEngineManager.searchEntityId("it", "Titolo Evento 4", allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains("EVN194"));//free content

            Set<String> allowedGroup2 = new HashSet<>();
            allowedGroup2.add(Group.ADMINS_GROUP_NAME);
            contentsId = this.searchEngineManager.searchEntityId("it", "testo coach", allowedGroup2);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains("ART104"));//coach content

        } catch (Throwable t) {
            throw t;
        }
    }

    @Test
    void testSearchContentsId_3() throws Throwable {
        try {
            Content content_1 = this.createContent_1();
            content_1.setMainGroup(Group.ADMINS_GROUP_NAME);
            this.searchEngineManager.deleteIndexedEntity(content_1.getId());
            this.searchEngineManager.addEntityToIndex(content_1);

            Content content_2 = this.createContent_2();
            this.searchEngineManager.deleteIndexedEntity(content_2.getId());
            this.searchEngineManager.addEntityToIndex(content_2);

            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
            assertNotNull(contentsId);
            assertFalse(contentsId.contains(content_1.getId()));
            allowedGroup.add("secondaryGroup");
            contentsId = this.searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.contains(content_1.getId()));
        } catch (Throwable t) {
            throw t;
        }
    }

    @Test
    void testSearchContentsId_4() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            SearchEngineFilter filterByType = new SearchEngineFilter(IIndexerDAO.CONTENT_TYPE_FIELD_NAME, "ART");
            SearchEngineFilter[] filters = {filterByType};
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertNotNull(contentsId);
            String[] expected1 = {"ART180", "ART1", "ART187", "ART121"};
            this.verify(contentsId, expected1);
            Category cat1 = this.categoryManager.getCategory("cat1");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(cat1);
            contentsId = sem.searchEntityId(filters, categories, allowedGroup);
            assertNotNull(contentsId);
            String[] expected2 = {"ART180"};
            this.verify(contentsId, expected2);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_5() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Category general_cat2 = this.categoryManager.getCategory("general_cat2");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(general_cat2);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = sem.searchEntityId(null, categories, allowedGroup);
            assertNotNull(contentsId);
            assertFalse(contentsId.isEmpty());
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            contentsId = sem.searchEntityId(null, categories, allowedGroup);
            String[] expected1 = {"ART111", "ART120", "EVN25"};
            this.verify(contentsId, expected1);
            Category general_cat1 = this.categoryManager.getCategory("general_cat1");
            categories.add(general_cat1);
            contentsId = sem.searchEntityId(null, categories, allowedGroup);
            assertNotNull(contentsId);
            String[] expected2 = {"ART111", "EVN25"};
            this.verify(contentsId, expected2);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_5_allowdValues() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            SearchEngineFilter filterGenCat1 = new SearchEngineFilter("category", "general_cat1");
            SearchEngineFilter[] catFilter1 = {filterGenCat1};
            List<String> contentsId1 = sem.loadContentsId(null, catFilter1, allowedGroup);
            assertNotNull(contentsId1);
            assertFalse(contentsId1.isEmpty());
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            contentsId1 = sem.loadContentsId(null, catFilter1, allowedGroup);
            String[] expected1 = {"ART111", "ART102", "EVN23", "EVN25"};
            this.verify(contentsId1, expected1);
            SearchEngineFilter filterCategories = SearchEngineFilter.createAllowedValuesFilter("categories", false, Arrays.asList(new String[]{"general_cat1", "general_cat2"}), TextSearchOption.EXACT);
            SearchEngineFilter[] catFilter2 = {filterCategories};
            List<String> contentsId2 = sem.loadContentsId(null, catFilter2, allowedGroup);
            assertNotNull(contentsId2);
            String[] expected2 = {"ART111", "EVN25", "ART120", "ART102", "EVN23"};
            this.verify(contentsId2, expected2);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_6() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Category general = this.categoryManager.getCategory("general");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(general);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            List<String> contentsId = sem.searchEntityId(null, categories, allowedGroup);
            assertNotNull(contentsId);
            String[] expected1 = {"ART122", "ART102", "ART111", "ART120", "EVN23", "EVN25"};
            this.verify(contentsId, expected1);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_7() throws Throwable {
        try {
            Content content_1 = this.createContent_1();
            this.searchEngineManager.deleteIndexedEntity(content_1.getId());
            this.searchEngineManager.addEntityToIndex(content_1);

            Content content_2 = this.createContent_2();
            this.searchEngineManager.deleteIndexedEntity(content_2.getId());
            this.searchEngineManager.addEntityToIndex(content_2);

            Content content_3 = this.createContent_3();
            this.searchEngineManager.deleteIndexedEntity(content_3.getId());
            this.searchEngineManager.addEntityToIndex(content_3);

            //San Pietroburgo è una città meravigliosa W3C-WAI
            //100
            //Il turismo ha incrementato più del 20 per cento nel 2011-2013, quando la Croazia ha aderito all'Unione europea. Consegienda di questo aumento è una serie di modernizzazione di alloggi di recente costruzione, tra cui circa tre dozzine di ostelli.
            //101
            //La vita è una cosa meravigliosa
            //103
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;

            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            SearchEngineFilter filter1 = new SearchEngineFilter("it", "San meravigliosa", SearchEngineFilter.TextSearchOption.ALL_WORDS);
            filter1.setFullTextSearch(true);
            SearchEngineFilter[] filters1 = {filter1};
            List<String> contentsId = sem.searchEntityId(filters1, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains(content_1.getId()));

            SearchEngineFilter filter1_2 = new SearchEngineFilter("en", "San meravigliosa", SearchEngineFilter.TextSearchOption.ALL_WORDS);
            filter1_2.setFullTextSearch(true);
            SearchEngineFilter[] filters1_2 = {filter1_2};
            contentsId = sem.searchEntityId(filters1_2, null, allowedGroup);
            assertNotNull(contentsId);
            assertTrue(contentsId.isEmpty());

            SearchEngineFilter filter2 = new SearchEngineFilter("it", "San meravigliosa", SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD);
            filter2.setFullTextSearch(true);
            SearchEngineFilter[] filters2 = {filter2};
            contentsId = sem.searchEntityId(filters2, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(2, contentsId.size());
            assertTrue(contentsId.contains(content_1.getId()));
            assertTrue(contentsId.contains(content_3.getId()));

            SearchEngineFilter filter3 = new SearchEngineFilter("it", "San meravigliosa", SearchEngineFilter.TextSearchOption.EXACT);
            filter3.setFullTextSearch(true);
            SearchEngineFilter[] filters3 = {filter3};
            contentsId = sem.searchEntityId(filters3, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(0, contentsId.size());

            SearchEngineFilter filter4 = new SearchEngineFilter("it", "una cosa meravigliosa", SearchEngineFilter.TextSearchOption.EXACT);
            filter4.setFullTextSearch(true);
            SearchEngineFilter[] filters4 = {filter4};
            contentsId = sem.searchEntityId(filters4, null, allowedGroup);
            assertNotNull(contentsId);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains(content_3.getId()));
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_7_like() throws Throwable {
        try {
            this.createContentsForTest();
            //San Pietroburgo è una città meravigliosa W3C-WAI
            //Il turismo ha incrementato più del 20 per cento nel 2011-2013, quando la Croazia ha aderito all'Unione europea. Consegienda di questo aumento è una serie di modernizzazione di alloggi di recente costruzione, tra cui circa tre dozzine di ostelli.
            //La vita è una cosa meravigliosa
            
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;

            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            SearchEngineFilter filter1 = new SearchEngineFilter("Articolo", true, "San viglios", SearchEngineFilter.TextSearchOption.ALL_WORDS);
            filter1.setLangCode("it");
            filter1.setLikeOption(true);
            SearchEngineFilter[] filters1 = {filter1};
            List<String> contentsId = sem.searchEntityId(filters1, null, allowedGroup);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains("101"));

            SearchEngineFilter filter2 = new SearchEngineFilter("Articolo", true, "San ravigl", SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD);
            filter2.setLangCode("it");
            filter2.setLikeOption(true);
            SearchEngineFilter[] filters2 = {filter2};
            contentsId = sem.searchEntityId(filters2, null, allowedGroup);
            assertEquals(2, contentsId.size());
            assertTrue(contentsId.contains("101"));
            assertTrue(contentsId.contains("103"));
            
            SearchEngineFilter filter3 = new SearchEngineFilter("Articolo", true, "meravig*");
            filter3.setLangCode("it");
            SearchEngineFilter[] filters3 = {filter3};
            contentsId = sem.searchEntityId(filters3, null, allowedGroup);
            assertEquals(2, contentsId.size());
            assertTrue(contentsId.contains("101"));
            assertTrue(contentsId.contains("103"));
            
            SearchEngineFilter filter4 = new SearchEngineFilter("en", "Accompany*");
            filter4.setFullTextSearch(true);
            SearchEngineFilter[] filters4 = {filter4};
            contentsId = sem.searchEntityId(filters4, null, allowedGroup);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains("102"));
            
            SearchEngineFilter filter5 = new SearchEngineFilter("en", "Accompany");
            filter5.setFullTextSearch(true);
            SearchEngineFilter[] filters5 = {filter5};
            contentsId = sem.searchEntityId(filters5, null, allowedGroup);
            assertTrue(contentsId.isEmpty());
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_8() throws Throwable {
        SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
        List<String> allowedGroup = new ArrayList<>();
        allowedGroup.add(Group.ADMINS_GROUP_NAME);
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineFilter filterByType
                    = SearchEngineFilter.createAllowedValuesFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, Arrays.asList("ART", "EVN"), TextSearchOption.EXACT);
            SearchEngineFilter[] filters1 = {filterByType};
            List<String> contentsId_1 = sem.searchEntityId(filters1, null, allowedGroup);
            List<String> expectedContentsId_1 = Arrays.asList("ART1", "ART180", "ART187", "ART121", 
                "ART122", "ART104", "ART102", "ART111", "ART120", "ART112", 
                "EVN25", "EVN41", "EVN103", "EVN193", "EVN20", 
                "EVN194", "EVN191", "EVN21", "EVN24", "EVN23", "EVN192");
            assertEquals(expectedContentsId_1.size(), contentsId_1.size());
            for (int i = 0; i < expectedContentsId_1.size(); i++) {
                assertTrue(expectedContentsId_1.contains(contentsId_1.get(i)));
            }
            
            SearchEngineFilter filterByRole = new SearchEngineFilter("jacms:title", true);
            filterByRole.setLangCode("it");
            SearchEngineFilter[] filters2 = {filterByType, filterByRole};
            List<String> contentsId_2 = sem.searchEntityId(filters2, null, allowedGroup);
            List<String> expectedContentsId_2 = Arrays.asList("ART1", "ART121", 
                    "ART122", "ART104", "ART102", "ART111", "ART120", "ART112", 
                    "EVN25", "EVN41", "EVN103", "EVN193", "EVN20", 
                    "EVN194", "EVN191", "EVN21", "EVN24", "EVN23", "EVN192");
            assertEquals(expectedContentsId_2.size(), contentsId_2.size());
            for (int i = 0; i < expectedContentsId_2.size(); i++) {
                assertTrue(expectedContentsId_2.contains(contentsId_2.get(i)));
            }
            
            filterByRole.setOrder(Order.DESC);
            List<String> contentsId_3 = sem.searchEntityId(filters2, null, allowedGroup);
            String[] expectedContentsId_3 = {"EVN194", "ART122", "ART121", "ART120", 
                "ART112", "ART111", "ART102", "ART104", "EVN103", "EVN193", "EVN192", "EVN191", "EVN25", 
                "EVN41", "EVN20", "EVN21", "ART1", "EVN23", "EVN24"};
            Assertions.assertEquals(expectedContentsId_3.length, contentsId_3.size());
            for (int i = 0; i < expectedContentsId_3.length; i++) {
                assertEquals(expectedContentsId_3[i], contentsId_3.get(i));
            }
            
            filterByRole.setOrder(Order.ASC);
            List<String> contentsId_4 = sem.searchEntityId(filters2, null, allowedGroup);
            for (int i = 0; i < expectedContentsId_3.length; i++) {
                assertEquals(expectedContentsId_3[i], contentsId_4.get(contentsId_3.size() - i - 1));
            }
            
            filterByRole.setLangCode("en");
            String[] expectedContentsId_5_en = {"EVN41", "EVN24", "EVN103", "EVN23", "EVN21", 
                "ART1", "EVN194", "EVN192", "EVN191", "EVN193", "ART120", "ART121", 
                "ART104", "ART102", "ART111", "ART112", "ART122", "EVN25", "EVN20"};
            List<String> contentsId_5 = sem.searchEntityId(filters2, null, allowedGroup);
            for (int i = 0; i < expectedContentsId_5_en.length; i++) {
                assertEquals(expectedContentsId_5_en[i], contentsId_5.get(i));
            }
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchContentsId_9() throws Throwable {
        SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
        List<String> allowedGroup = new ArrayList<>();
        allowedGroup.add(Group.ADMINS_GROUP_NAME);
        List<String> ids = new ArrayList<>();
        Content artType = this.contentManager.createContentType("ART");
        try {
            TextAttribute newTextAttribute = (TextAttribute) this.contentManager.getEntityAttributePrototypes().get("Text");
            newTextAttribute.setName("Test");
            newTextAttribute.getValidationRules().setRequired(true);
            newTextAttribute.setSearchable(true);
            newTextAttribute.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
            artType.addAttribute(newTextAttribute);
            ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(artType);
            
            char[] characters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            for (int i = 0; i < characters.length; i++) {
                String c = String.valueOf(characters[i]);
                Content content = this.contentManager.loadContent("ART104", true);
                content.setId(null);
                TextAttribute textAttribute = (TextAttribute) content.getAttribute("Test");
                textAttribute.setText((c + c).toUpperCase(), "it"); // note: search by single term
                textAttribute.setText((c + c).toUpperCase(), "en"); // note: search by single term
                this.contentManager.insertOnLineContent(content);
                ids.add(content.getId());
            }
            synchronized (this) {
                this.wait(3000);
            }
            super.waitNotifyingThread();
            
            SearchEngineFilter filterByType = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART");
            SearchEngineFilter filter = new SearchEngineFilter("Test", true);
            filter.setLangCode("it");
            filter.setOrder(Order.ASC);
            SearchEngineFilter[] filters = {filterByType, filter};
            List<String> contentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertEquals(contentsId.size(), ids.size());
            for (int i = 0; i < contentsId.size(); i++) {
                assertEquals(ids.get(i), contentsId.get(i));
            }
            this.executeTestByStringRange(allowedGroup, "D", "J", ids, 3, 7);
            this.executeTestByStringRange(allowedGroup, null, "O", ids, 0, 15);
            this.executeTestByStringRange(allowedGroup, "I", null, ids, 8, 18);
            
            SearchEngineFilter filterAllowedValues = SearchEngineFilter.createAllowedValuesFilter("Test", true, Arrays.asList(new String[]{"AA", "FF", "SS", "LL"}), TextSearchOption.ALL_WORDS);
            SearchEngineFilter[] filters_2 = {filterByType, filterAllowedValues};
            List<String> contentsId_2 = sem.searchEntityId(filters_2, null, allowedGroup);
            String[] expected_2 = {ids.get(0), ids.get(5), ids.get(11), ids.get(18)};
            this.verify(contentsId_2, expected_2);
        } catch (Throwable t) {
            throw t;
        } finally {
            artType.getAttributeMap().remove("Test");
            artType.getAttributeList().removeIf(a -> a.getName().equals("Test"));
            ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(artType);
            Content newArtType = this.contentManager.createContentType("ART");
            assertNull(newArtType.getAttribute("Test"));
            for (int i = 0; i < ids.size(); i++) {
                String newId = ids.get(i);
                Content newContent = this.contentManager.loadContent(newId, false);
                this.contentManager.removeOnLineContent(newContent);
                this.contentManager.deleteContent(newContent);
            }
        }
    }
    
    private void executeTestByStringRange(List<String> allowedGroup,
            String start, String end, List<String> total, int startIndex, int expectedSize) throws Exception {
        SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
        SearchEngineFilter filterByType = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART");
        SearchEngineFilter filter = SearchEngineFilter.createRangeFilter("Test", true, start, end);
        filter.setLangCode("it");
        filter.setOrder(Order.ASC);
        SearchEngineFilter[] filters = {filterByType, filter};
        List<String> contentsId_1 = sem.searchEntityId(filters, null, allowedGroup);
        assertEquals(expectedSize, contentsId_1.size());
        for (int i = 0; i < contentsId_1.size(); i++) {
            assertEquals(total.get(i + startIndex), contentsId_1.get(i));
        }
        filter.setLangCode("en");
        filter.setOrder(Order.DESC);
        List<String> contentsId_2_en = sem.searchEntityId(filters, null, allowedGroup);
        assertEquals(contentsId_1.size(), contentsId_2_en.size());
        for (int i = 0; i < contentsId_2_en.size(); i++) {
            assertEquals(contentsId_1.get(i), contentsId_2_en.get(contentsId_2_en.size() - i - 1));
        }
    }
    
    @Test
    void testSearchContentsId_10() throws Throwable {
        SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
        List<String> allowedGroup = new ArrayList<>();
        allowedGroup.add(Group.ADMINS_GROUP_NAME);
        List<String> ids = new ArrayList<>();
        Content artType = this.contentManager.createContentType("ART");
        try {
            NumberAttribute newNumberAttribute = (NumberAttribute) this.contentManager.getEntityAttributePrototypes().get("Number");
            newNumberAttribute.setName("TestNum");
            newNumberAttribute.getValidationRules().setRequired(true);
            newNumberAttribute.setSearchable(true);
            newNumberAttribute.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
            artType.addAttribute(newNumberAttribute);
            ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(artType);
            
            for (int i = 0; i < 30; i++) {
                Content content = this.contentManager.loadContent("ART104", true);
                content.setId(null);
                NumberAttribute textAttribute = (NumberAttribute) content.getAttribute("TestNum");
                textAttribute.setValue(new BigDecimal(i+1));
                this.contentManager.insertOnLineContent(content);
                ids.add(content.getId());
            }
            synchronized (this) {
                this.wait(3000);
            }
            super.waitNotifyingThread();
            
            SearchEngineFilter filterByType = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART");
            NumericSearchEngineFilter filter = new NumericSearchEngineFilter("TestNum", true);
            filter.setOrder(Order.ASC);
            SearchEngineFilter[] filters = {filterByType, filter};
            List<String> contentsId = sem.searchEntityId(filters, null, allowedGroup);
            assertEquals(contentsId.size(), ids.size());
            for (int i = 0; i < contentsId.size(); i++) {
                assertEquals(ids.get(i), contentsId.get(i));
            }
            
            this.executeTestByNumberRange(allowedGroup, 7, 13, ids, 6, 7);
            this.executeTestByNumberRange(allowedGroup, null, 12, ids, 0, 12);
            this.executeTestByNumberRange(allowedGroup, 11, null, ids, 10, 20);
            
            NumericSearchEngineFilter filterForValue = new NumericSearchEngineFilter("TestNum", true, 5);
            SearchEngineFilter[] filters_2 = {filterByType, filterForValue};
            List<String> contentsId_2 = sem.searchEntityId(filters_2, null, allowedGroup);
            assertEquals(1, contentsId_2.size());
            assertEquals(ids.get(4), contentsId_2.get(0));
            
            NumericSearchEngineFilter filterAllowedValues = NumericSearchEngineFilter.createAllowedValuesFilter("TestNum", true, Arrays.asList(new Number[]{27, 12, 1, 89}));
            SearchEngineFilter[] filters_3 = {filterByType, filterAllowedValues};
            List<String> contentsId_3 = sem.searchEntityId(filters_3, null, allowedGroup);
            String[] expected_3 = {ids.get(26), ids.get(11), ids.get(0)};
            this.verify(contentsId_3, expected_3);
        } catch (Throwable t) {
            throw t;
        } finally {
            artType.getAttributeMap().remove("TestNum");
            artType.getAttributeList().removeIf(a -> a.getName().equals("TestNum"));
            ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(artType);
            Content newArtType = this.contentManager.createContentType("ART");
            assertNull(newArtType.getAttribute("TestNum"));
            for (int i = 0; i < ids.size(); i++) {
                String newId = ids.get(i);
                Content newContent = this.contentManager.loadContent(newId, false);
                this.contentManager.removeOnLineContent(newContent);
                this.contentManager.deleteContent(newContent);
            }
        }
    }
    
    private void executeTestByNumberRange(List<String> allowedGroup,
            Number start, Number end, List<String> total, int startIndex, int expectedSize) throws Exception {
        SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
        SearchEngineFilter filterByType = new SearchEngineFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART");
        NumericSearchEngineFilter filter = NumericSearchEngineFilter.createRangeFilter("TestNum", true, start, end);
        filter.setLangCode("it");
        filter.setOrder(Order.ASC);
        SearchEngineFilter[] filters = {filterByType, filter};
        List<String> contentsId_1 = sem.searchEntityId(filters, null, allowedGroup);
        assertEquals(expectedSize, contentsId_1.size());
        for (int i = 0; i < contentsId_1.size(); i++) {
            assertEquals(total.get(i + startIndex), contentsId_1.get(i));
        }
        filter.setLangCode("en");
        filter.setOrder(Order.DESC);
        List<String> contentsId_2_en = sem.searchEntityId(filters, null, allowedGroup);
        assertEquals(contentsId_1.size(), contentsId_2_en.size());
        for (int i = 0; i < contentsId_2_en.size(); i++) {
            assertEquals(contentsId_1.get(i), contentsId_2_en.get(contentsId_2_en.size() - i - 1));
        }
    }
    
    @Test
    void testSearchContentsId_11() throws Throwable {
        SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
        List<String> allowedGroup = new ArrayList<>();
        allowedGroup.add(Group.ADMINS_GROUP_NAME);
        String newId = null;
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineFilter filterByType
                    = SearchEngineFilter.createAllowedValuesFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, Arrays.asList("ART", "EVN"), TextSearchOption.EXACT);
            SearchEngineFilter filterByRole = new SearchEngineFilter(ROLE_FOR_TEST, true);
            filterByRole.setOrder(Order.DESC);
            SearchEngineFilter[] filters1 = {filterByType, filterByRole};
            List<String> contentsId_1 = sem.searchEntityId(filters1, null, allowedGroup);
            String[] expectedContentsId_1 = {"EVN194", "EVN193", "ART121", "ART120", "EVN24", "EVN23",
                "EVN41", "EVN25", "ART104", "ART111", "EVN20", "ART112", "EVN21", "ART1", "EVN103", "EVN192", "EVN191"};
            Assertions.assertEquals(expectedContentsId_1.length, contentsId_1.size());
            for (int i = 0; i < expectedContentsId_1.length; i++) {
                Assertions.assertEquals(expectedContentsId_1[i], contentsId_1.get(i));
            }
            
            String[] langs = {"it", "en"};
            for (int j = 0; j < langs.length; j++) {
                String lang = langs[j];
                filterByRole.setLangCode(lang);
                List<String> contentsId_lang = sem.searchEntityId(filters1, null, allowedGroup);
                for (int i = 0; i < expectedContentsId_1.length; i++) {
                    Assertions.assertEquals(expectedContentsId_1[i], contentsId_lang.get(i));
                }
            }
            
            filterByRole.setOrder(Order.ASC);
            List<String> contentsId_2 = sem.searchEntityId(filters1, null, allowedGroup);
            for (int i = 0; i < expectedContentsId_1.length; i++) {
                Assertions.assertEquals(expectedContentsId_1[i], contentsId_2.get(contentsId_2.size() - i - 1));
            }
            
            Content newContent = this.contentManager.loadContent("EVN194", true);
            newContent.setId(null);
            DateAttribute dateAttribute = (DateAttribute) newContent.getAttributeByRole(ROLE_FOR_TEST);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateAttribute.getDate());
            cal.add(Calendar.YEAR, 1);
            dateAttribute.setDate(cal.getTime());
            this.contentManager.insertOnLineContent(newContent);
            synchronized (this) {
                this.wait(1000);
            }
            super.waitNotifyingThread();
            newId = newContent.getId();
            filterByRole.setOrder(Order.DESC);
            List<String> contentsId_3 = sem.searchEntityId(filters1, null, allowedGroup);
            Assertions.assertEquals(expectedContentsId_1.length + 1, contentsId_3.size());
            for (int i = 0; i < contentsId_3.size(); i++) {
                if (i == 0) {
                    assertEquals(newId, contentsId_3.get(i));
                } else {
                    assertEquals(expectedContentsId_1[i - 1], contentsId_3.get(i));
                }
            }
            
            Calendar start = Calendar.getInstance();
            start.setTime(DateConverter.parseDate("10/06/2000", "dd/MM/yyyy"));
            Calendar end = Calendar.getInstance();
            end.setTime(DateConverter.parseDate("01/02/2008", "dd/MM/yyyy"));
            SearchEngineFilter filterByRange = SearchEngineFilter.createRangeFilter(ROLE_FOR_TEST, true, start.getTime(), end.getTime());
            filterByRange.setOrder(Order.DESC);
            SearchEngineFilter[] filters2 = {filterByRange};
            List<String> contentsId_4 = sem.searchEntityId(filters2, null, allowedGroup);
            String[] expectedContentsId_2 = {"EVN41", "EVN25", "ART104", "ART111", "EVN20", "ART112", "EVN21", "ART1"};
            Assertions.assertEquals(expectedContentsId_2.length, contentsId_4.size());
            for (int i = 0; i < contentsId_4.size(); i++) {
                Assertions.assertEquals(expectedContentsId_2[i], contentsId_4.get(i));
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            if (null != newId) {
                Content newContent = this.contentManager.loadContent(newId, false);
                this.contentManager.removeOnLineContent(newContent);
                this.contentManager.deleteContent(newContent);
            }
        }
    }
    
    @Test
    void testFacetedAllContents() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Set<String> allowedGroup = new HashSet<>();
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            SearchEngineFilter[] filters = {};
            SearchEngineFilter[] categories = {};
            FacetedContentsResult result = sem.searchFacetedEntities(filters, categories, allowedGroup);
            assertNotNull(result);
            assertNotNull(result.getContentsId());
            assertNotNull(result.getOccurrences());
            assertTrue(result.getContentsId().size() > 0);
            assertTrue(result.getOccurrences().size() > 0);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    @Test
    void testSearchFacetedContents_1() throws Throwable {
        try {
            Thread thread = this.searchEngineManager.startReloadContentsReferences();
            thread.join();
            SearchEngineManager sem = (SearchEngineManager) this.searchEngineManager;
            Category general = this.categoryManager.getCategory("general");
            List<ITreeNode> categories = new ArrayList<>();
            categories.add(general);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            allowedGroup.add(Group.ADMINS_GROUP_NAME);
            SearchEngineFilter[] filters = {};
            FacetedContentsResult result = sem.searchFacetedEntities(filters, categories, allowedGroup);
            assertNotNull(result);
            String[] expected1 = {"ART122", "ART102", "ART111", "ART120", "EVN23", "EVN25"};
            this.verify(result.getContentsId(), expected1);
            assertEquals(4, result.getOccurrences().size());
        } catch (Throwable t) {
            throw t;
        }
    }
    
    private void verify(List<String> contentsId, String[] array) {
        assertEquals(array.length, contentsId.size());
        for (int i = 0; i < array.length; i++) {
            assertTrue(contentsId.contains(array[i]));
        }
    }
    
    private void createContentsForTest() throws EntException {
        Content content_1 = this.createContent_1();
        this.searchEngineManager.deleteIndexedEntity(content_1.getId());
        this.searchEngineManager.addEntityToIndex(content_1);
        Content content_2 = this.createContent_2();
        this.searchEngineManager.deleteIndexedEntity(content_2.getId());
        this.searchEngineManager.addEntityToIndex(content_2);
        Content content_3 = this.createContent_3();
        this.searchEngineManager.deleteIndexedEntity(content_3.getId());
        this.searchEngineManager.addEntityToIndex(content_3);
    }

    private Content createContent_1() {
        Content content = new Content();
        content.setId("101");
        content.setDescription("Description content 1");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("secondaryGroup");
        content.setTypeCode("ART");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("San Pietroburgo è una città meravigliosa W3C-WAI", "it");
        text.setText("St. Petersburg is a wonderful city", "en");
        content.addAttribute(text);
        Category category1 = this.categoryManager.getCategory("resCat2");
        Category category2 = this.categoryManager.getCategory("general_cat3");
        content.addCategory(category1);
        content.addCategory(category2);
        return content;
    }

    private Content createContent_2() {
        Content content = new Content();
        content.setId("102");
        content.setDescription("Description content 2");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("thirdGroup");
        content.setTypeCode("ART");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("Il turismo ha incrementato più del 20 per cento nel 2011-2013, quando la Croazia ha aderito all'Unione europea. Consegienda di questo aumento è una serie di modernizzazione di alloggi di recente costruzione, tra cui circa tre dozzine di ostelli.", "it");
        text.setText("Tourism had shot up more than 20 percent from 2011 to 2013, when Croatia joined the European Union. Accompanying that rise is a raft of modernized and recently built lodgings, including some three dozen hostels.", "en");
        content.addAttribute(text);
        Category category1 = this.categoryManager.getCategory("resCat1");
        Category category2 = this.categoryManager.getCategory("general_cat2");
        content.addCategory(category1);
        content.addCategory(category2);
        return content;
    }

    private Content createContent_3() {
        Content content = new Content();
        content.setId("103");
        content.setDescription("Description content 3");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.setTypeCode("ART");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("La vita è una cosa meravigliosa", "it");
        text.setText("Life is a wonderful thing", "en");
        content.addAttribute(text);
        Category category = this.categoryManager.getCategory("general_cat1");
        content.addCategory(category);
        return content;
    }
    
    @Test
    void testSearchContentByResource() throws Exception {
        Content contentForTest = this.contentManager.loadContent("ALL4", true);
        try {
            contentForTest.setId(null);
            AttachAttribute attachAttribute = (AttachAttribute) contentForTest.getAttribute("Attach");
            ResourceInterface resource = this.resourceManager.loadResource("6");
            assertNotNull(resource);
            attachAttribute.setResource(resource, "it");
            this.contentManager.insertOnLineContent(contentForTest);
            assertNotNull(contentForTest.getId());
            super.waitNotifyingThread();
            super.waitThreads(ICmsSearchEngineManager.RELOAD_THREAD_NAME_PREFIX);
            List<String> allowedGroup = new ArrayList<>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            List<String> contentsId = this.searchEngineManager.searchEntityId("it", "accelerated development", allowedGroup);
            assertNotNull(contentsId);
            assertEquals(1, contentsId.size());
            assertTrue(contentsId.contains(contentForTest.getId()));
        } catch (Exception e) {
            throw e;
        } finally {
            Content contentToDelete = this.contentManager.loadContent(contentForTest.getId(), false);
            if (null != contentToDelete) {
                this.contentManager.removeOnLineContent(contentToDelete);
                this.contentManager.deleteContent(contentToDelete);
            }
        }
    }
    
    @BeforeEach
    private void init() throws Exception {
        try {
            this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
            this.resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
            this.searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
            this.categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
            
            AttributeRole role = this.contentManager.getAttributeRole(ROLE_FOR_TEST);
            assertNotNull(role);
            Content artType = this.contentManager.createContentType("ART");
            DateAttribute dateAttrArt = (DateAttribute) artType.getAttribute("Data");
            if (null == dateAttrArt.getRoles() || !Arrays.asList(dateAttrArt.getRoles()).contains(ROLE_FOR_TEST)) {
                dateAttrArt.setRoles(new String[]{ROLE_FOR_TEST});
                ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(artType);
            }
            Content evnType = this.contentManager.createContentType("EVN");
            DateAttribute dateAttrEnv = (DateAttribute) evnType.getAttribute("DataInizio");
            if (null == dateAttrEnv.getRoles() || !Arrays.asList(dateAttrEnv.getRoles()).contains(ROLE_FOR_TEST)) {
                dateAttrEnv.setRoles(new String[]{ROLE_FOR_TEST});
                ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(evnType);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    @AfterEach
    private void release() throws Exception {
        this.waitThreads(ICmsSearchEngineManager.RELOAD_THREAD_NAME_PREFIX);
        try {
            Content artType = this.contentManager.createContentType("ART");
            DateAttribute dateAttrArt = (DateAttribute) artType.getAttribute("Data");
            dateAttrArt.setRoles(new String[0]);
            ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(artType);
            Content evnType = this.contentManager.createContentType("EVN");
            DateAttribute dateAttrEnv = (DateAttribute) evnType.getAttribute("DataInizio");
            dateAttrEnv.setRoles(new String[0]);
            ((IEntityTypesConfigurer) this.contentManager).updateEntityPrototype(evnType);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
