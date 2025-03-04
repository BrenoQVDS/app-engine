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
package com.agiletec.apsadmin.portal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.apsadmin.portal.rs.model.PageResponse;

import com.agiletec.aps.system.SystemConstants;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import org.entando.entando.ent.exception.EntException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author E.Santoboni
 */
class TestPageAction extends ApsAdminBaseTestCase {

    @Test
    void shouldReturnErrorIfModelIsEmptyString() throws Throwable {
        String selectedPageCode = "homepage";
        this.setUserOnSession("admin");
        this.initAction("/do/Page", "saveConfigure");
        this.addParameter("selectedNode", selectedPageCode);
        this.addParameter("pageCode", "page_code");
        this.addParameter("model", ""); // this is an error
        this.addParameter("group", "aaa");
        this.addParameter("langit", "Pagina Test 1");
        this.addParameter("langen", "Test Page 1");

        String result = this.executeAction();

        Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
        assertEquals(1, fieldErrors.size());
        assertTrue(fieldErrors.containsKey("model"));

        assertEquals(Action.INPUT, result);
    }

    @Test
	void testNewPage_1() throws Throwable {
        String selectedPageCode = "homepage";
        String result = this.executeNewPage(selectedPageCode, "admin");
        assertEquals(Action.SUCCESS, result);
        PageAction action = (PageAction) this.getAction();
        assertFalse(action.isGroupSelectLock());
        assertEquals(Group.FREE_GROUP_NAME, action.getGroup());

        result = this.executeNewPage("coach_page", "admin");
        assertEquals(Action.SUCCESS, result);
        action = (PageAction) this.getAction();
        assertFalse(action.isGroupSelectLock());
        assertEquals("coach", action.getGroup());

        result = this.executeNewPage("customers_page", "admin");
        assertEquals(Action.SUCCESS, result);
        action = (PageAction) this.getAction();
        assertFalse(action.isGroupSelectLock());
        assertEquals("customers", action.getGroup());

        result = this.executeNewPage("customer_subpage_2", "admin");
        assertEquals(Action.SUCCESS, result);
        action = (PageAction) this.getAction();
        assertFalse(action.isGroupSelectLock());
        assertEquals("customers", action.getGroup());

        result = this.executeNewPage(selectedPageCode, null);
        assertEquals("apslogin", result);
    }

    @Test
	void testNewPage_2() throws Throwable {
        String result = this.executeNewPage("homepage", "pageManagerCustomers");
        assertEquals("pageTree", result);

        result = this.executeNewPage("coach_page", "pageManagerCustomers");
        assertEquals("pageTree", result);

        result = this.executeNewPage("customers_page", "pageManagerCustomers");
        assertEquals(Action.SUCCESS, result);
        PageAction action = (PageAction) this.getAction();
        assertFalse(action.isGroupSelectLock());
        assertEquals("customers", action.getGroup());

        result = this.executeNewPage("homepage", null);
        assertEquals("apslogin", result);
    }

    @Test
	void testEditPageForAdminUser() throws Throwable {
        String selectedPageCode = "pagina_1";
        String result = this.executeActionOnPage(selectedPageCode, "admin", "edit");
        assertEquals(Action.SUCCESS, result);
        IPage page = this._pageManager.getDraftPage(selectedPageCode);
        PageAction action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
        assertEquals(page.getCode(), action.getPageCode());
        assertEquals(page.getParentCode(), action.getParentPageCode());
        assertEquals(page.getModelCode(), action.getModel());
        assertEquals(page.getGroup(), action.getGroup());
        assertEquals(page.isShowable(), action.isShowable());
        assertEquals("Pagina 1", action.getTitles().getProperty("it"));
        assertEquals("Page 1", action.getTitles().getProperty("en"));
    }

    @Test
	void testEdit_1() throws Throwable {
        String result = this.executeActionOnPage("pagina_1", "admin", "edit");
        assertEquals(Action.SUCCESS, result);
        PageAction action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
        assertFalse(action.isGroupSelectLock());

        result = this.executeActionOnPage("customers_page", "admin", "edit");
        assertEquals(Action.SUCCESS, result);
        action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
        assertEquals("customers", action.getGroup());
        assertFalse(action.isGroupSelectLock());

        result = this.executeActionOnPage("customer_subpage_1", "admin", "edit");
        assertEquals(Action.SUCCESS, result);
        action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
        assertEquals("customers", action.getGroup());
        assertTrue(action.isGroupSelectLock());
    }

    @Test
	void testEdit_2() throws Throwable {
        String result = this.executeActionOnPage("customers_page", "pageManagerCustomers", "edit");
        assertEquals(Action.SUCCESS, result);
        PageAction action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
        assertEquals("customers", action.getGroup());
        assertTrue(action.isGroupSelectLock());

        result = this.executeActionOnPage("customer_subpage_1", "pageManagerCustomers", "edit");
        assertEquals(Action.SUCCESS, result);
        action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
        assertEquals("customers", action.getGroup());
        assertTrue(action.isGroupSelectLock());
    }

    @Test
	void testEditForCoachUser() throws Throwable {
        String selectedPageCode = this._pageManager.getDraftRoot().getCode();
        String result = this.executeActionOnPage(selectedPageCode, "pageManagerCoach", "edit");
        assertEquals("pageTree", result);

        IPage customers_page = this._pageManager.getDraftPage("customers_page"); // PAGINA
        result = this.executeActionOnPage(customers_page.getCode(), "pageManagerCustomers", "edit");
        assertEquals(Action.SUCCESS, result);

        PageAction action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
        assertEquals(customers_page.getCode(), action.getPageCode());
        assertEquals(customers_page.getModelCode(), action.getModel());
        assertTrue(action.isShowable());
        assertTrue(action.isGroupSelectLock());
        PageModel pageModel = this._pageModelManager.getPageModel(customers_page.getMetadata().getModelCode());
        Widget widget = customers_page.getWidgets()[pageModel.getMainFrame()];
        if (null != widget) {
            assertEquals("content_viewer", widget.getTypeCode());
            assertTrue(null != widget.getConfig() && !widget.getConfig().isEmpty());
        }
    }

    @Test
	void testSearchPage() throws Throwable {
        String result = null;
        PageFinderAction action = null;
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/Page", "search");
            this.addParameter("pageCodeToken", "aGIna_");
            result = this.executeAction();
            assertNotNull(result);
            action = (PageFinderAction) this.getAction();
            assertNotNull(action);
            assertNotNull(action.getPagesFound());
            assertEquals(5, action.getPagesFound().size());
            // test unlikey events - empty search string
            this.initAction("/do/Page", "search");
            this.addParameter("pageCodeToken", "");
            result = this.executeAction();
            assertNotNull(result);
            action = (PageFinderAction) this.getAction();
            assertNotNull(action);
            assertNotNull(action.searchPages());
            assertEquals(17, action.searchPages().size());
            // test unlikey events - null
            this.initAction("/do/Page", "search");
            result = this.executeAction();
            assertNotNull(result);
            action = (PageFinderAction) this.getAction();
            assertNotNull(action);
            List<IPage> pages = action.searchPages();
            assertNotNull(pages);
            assertEquals(17, pages.size());
            for (int i = 0; i < pages.size(); i++) {
                IPage page = pages.get(i);
                assertNotNull(action.getFullTitle(page.getCode(), action.getCurrentLang().getCode()));
                assertEquals("undefined", action.getFullTitle(page.getCode()+"xxx", action.getCurrentLang().getCode()));
            }
        } catch (Throwable t) {
            throw t;
        }
    }

    @Test
	void testDetailPageForAdmin() throws Throwable {
        String selectedPageCode = "contentview"; // PAGINA PREDISPOSTA PER LA
        // PUBBLICAZIONE VOLANTE
        String result = this.executeActionOnPage(selectedPageCode, "admin", "detail");
        assertEquals(Action.SUCCESS, result);

        IPage page = this._pageManager.getDraftPage(selectedPageCode);
        PageAction action = (PageAction) this.getAction();
        IPage pageToShow = action.getPageToShow();

        assertEquals(page.getCode(), pageToShow.getCode());
        assertEquals(page.getParentCode(), pageToShow.getParentCode());
        assertEquals(page.getModelCode(), pageToShow.getModelCode());
        assertEquals(page.getGroup(), pageToShow.getGroup());
        assertEquals(page.isShowable(), pageToShow.isShowable());
        assertEquals("Publicazione Contenuto", pageToShow.getTitles().getProperty("it"));
        assertEquals("Content Publishing", pageToShow.getTitles().getProperty("en"));
        PageModel pageModel = this._pageModelManager.getPageModel(page.getMetadata().getModelCode());
        Widget widget = page.getWidgets()[pageModel.getMainFrame()];
        if (null != widget) {
            assertEquals("formAction", widget.getTypeCode());
            assertTrue(null != widget.getConfig() && !widget.getConfig().isEmpty());
        }
    }

    @Test
	void testDetailForCoachUser() throws Throwable {
        String selectedPageCode = this._pageManager.getOnlineRoot().getCode();
        String result = this.executeActionOnPage(selectedPageCode, "pageManagerCoach", "detail");
        assertEquals("pageTree", result);

        IPage customers_page = this._pageManager.getDraftPage("customers_page");
        result = this.executeActionOnPage(customers_page.getCode(), "pageManagerCustomers", "detail");
        assertEquals(Action.SUCCESS, result);

        PageAction action = (PageAction) this.getAction();
        IPage pageToShow = action.getPageToShow();
        assertEquals(customers_page.getCode(), pageToShow.getCode());
        assertEquals(customers_page.getModelCode(), pageToShow.getModelCode());
    }

    private String executeActionOnPage(String selectedPageCode, String username, String actionName) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/Page", actionName);
        this.addParameter("selectedNode", selectedPageCode);
        String result = this.executeAction();
        return result;
    }

    @Test
	void testAddForAdminUser() throws Throwable {
        IPage root = this._pageManager.getDraftRoot();
        String result = this.executeNewPage(root.getCode(), "admin");
        assertEquals(Action.SUCCESS, result);

        PageAction action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
        assertEquals(root.getCode(), action.getParentPageCode());
        assertEquals(root.getGroup(), action.getGroup());
        assertFalse(action.isGroupSelectLock());
        assertTrue(action.isShowable());
        assertTrue(action.isDefaultShowlet());
    }

    @Test
	void testNewPageByCustomerUser() throws Throwable {
        IPage root = this._pageManager.getDraftRoot();
        String result = this.executeNewPage(root.getCode(), "pageManagerCustomers");
        assertEquals("pageTree", result);

        result = this.executeNewPage(root.getCode(), "pageManagerCustomers");
        assertEquals("pageTree", result);

        IPage coach_page = this._pageManager.getDraftPage("coach_page");
        result = this.executeNewPage(coach_page.getCode(), "pageManagerCustomers");
        assertEquals("pageTree", result);

        IPage customers_page = this._pageManager.getDraftPage("customers_page");
        result = this.executeNewPage(customers_page.getCode(), "pageManagerCustomers");
        assertEquals(Action.SUCCESS, result);

        PageAction action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
        assertEquals(customers_page.getCode(), action.getParentPageCode());
        assertEquals(customers_page.getGroup(), action.getGroup());
        assertFalse(action.isGroupSelectLock());
        assertTrue(action.isShowable());
        assertTrue(action.isDefaultShowlet());
    }

    @Test
	void testNewPageByCoachUser() throws Throwable {
        IPage root = this._pageManager.getDraftRoot();
        String result = this.executeNewPage(root.getCode(), "pageManagerCoach");
        assertEquals("pageTree", result);

        IPage coach_page = this._pageManager.getDraftPage("coach_page");
        result = this.executeNewPage(coach_page.getCode(), "pageManagerCoach");
        assertEquals(Action.SUCCESS, result);

        PageAction action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
        assertEquals(coach_page.getCode(), action.getParentPageCode());
        assertEquals(coach_page.getGroup(), action.getGroup());
        assertFalse(action.isGroupSelectLock());
        assertTrue(action.isShowable());
        assertTrue(action.isDefaultShowlet());

        IPage customers_page = this._pageManager.getDraftPage("customers_page");
        result = this.executeNewPage(customers_page.getCode(), "pageManagerCoach");
        assertEquals(Action.SUCCESS, result);

        action = (PageAction) this.getAction();
        assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
        assertEquals(customers_page.getCode(), action.getParentPageCode());
        assertEquals(customers_page.getGroup(), action.getGroup());
        assertFalse(action.isGroupSelectLock());
        assertTrue(action.isShowable());
        assertTrue(action.isDefaultShowlet());
    }

    private String executeNewPage(String selectedPageCode, String username) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/Page", "new");
        this.addParameter("selectedNode", selectedPageCode);
        String result = this.executeAction();
        return result;
    }

    @Test
	void testCloneForAdminUser() throws Throwable {
        IPage copied = this._pageManager.getDraftPage("service");
        String result = this.executeCopyPage(copied, "admin");
        assertEquals(Action.SUCCESS, result);

        PageAction action = (PageAction) this.getAction();
        assertEquals(action.getStrutsAction(), ApsAdminSystemConstants.PASTE);
        assertEquals(copied.getCode(), action.getCopyPageCode());
        assertEquals(copied.getCode(), action.getParentPageCode());
        assertEquals(copied.getModelCode(), action.getModel());
        assertEquals(copied.getGroup(), action.getGroup());
        assertEquals(copied.isShowable(), action.isShowable());
        assertNull(action.getTitles().getProperty("it"));
        assertNull(action.getTitles().getProperty("en"));
    }

    @Test
	void testCloneForCoachUser() throws Throwable {
        IPage copied = this._pageManager.getDraftPage("coach_page");
        String result = this.executeCopyPage(copied, "admin");
        assertEquals(Action.SUCCESS, result);

        PageAction action = (PageAction) this.getAction();
        assertEquals(action.getStrutsAction(), ApsAdminSystemConstants.PASTE);
        assertEquals(copied.getCode(), action.getCopyPageCode());

        assertEquals(copied.getCode(), action.getParentPageCode());
        assertEquals(copied.getModelCode(), action.getModel());
        assertEquals(copied.getGroup(), action.getGroup());
        assertEquals(copied.isShowable(), action.isShowable());
        assertNull(action.getTitles().getProperty("it"));
        assertNull(action.getTitles().getProperty("en"));
    }

    private String executeCopyPage(IPage copiedPage, String userName) throws Throwable {
        this.setUserOnSession(userName);
        this.initAction("/do/Page", "copy");
        this.addParameter("btnCopy", "copy");
        this.addParameter("selectedNode", copiedPage.getCode());
        String result = this.executeAction();
        return result;
    }

    @Test
	void testValidateSavePage() throws Throwable {
        String pageCode = "pagina_test";
        String longPageCode = "very_long_page_code__very_long_page_code";
        assertNull(this._pageManager.getDraftPage(pageCode));
        assertNull(this._pageManager.getDraftPage(longPageCode));
        try {
            IPage root = this._pageManager.getDraftRoot();
            Map<String, String> params = new HashMap<String, String>();
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
            String result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
            assertEquals(6, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("pageCode"));
            assertTrue(fieldErrors.containsKey("parentPageCode"));
            assertTrue(fieldErrors.containsKey("model"));
            assertTrue(fieldErrors.containsKey("group"));
            assertTrue(fieldErrors.containsKey("langit"));
            assertTrue(fieldErrors.containsKey("langen"));

            params.put("parentPageCode", root.getCode());
            result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(5, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("pageCode"));
            assertTrue(fieldErrors.containsKey("model"));
            assertTrue(fieldErrors.containsKey("group"));
            assertTrue(fieldErrors.containsKey("langit"));
            assertTrue(fieldErrors.containsKey("langen"));

            params.put("langit", "Pagina Test");
            params.put("model", "home");
            result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(3, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("pageCode"));
            assertTrue(fieldErrors.containsKey("group"));
            assertTrue(fieldErrors.containsKey("langen"));

            assertNotNull(this._pageManager.getDraftPage("pagina_1"));
            params.put("langen", "Test Page");
            params.put("group", Group.FREE_GROUP_NAME);
            params.put("pageCode", "pagina_1");// page already present
            result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(1, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("pageCode"));

            params.put("pageCode", longPageCode);
            result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(1, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("pageCode"));
            
            params.put("langen", String.join("", Collections.nCopies(71, "x"))); // very long title
            result = this.executeSave(params, "admin");
            assertEquals(Action.INPUT, result);
            fieldErrors = this.getAction().getFieldErrors();
            assertEquals(2, fieldErrors.size());
            assertTrue(fieldErrors.containsKey("pageCode"));
            assertTrue(fieldErrors.containsKey("langen"));
        } catch (Throwable t) {
            this._pageManager.deletePage(pageCode);
            this._pageManager.deletePage(longPageCode);
            throw t;
        }
    }

    @Test
	void testSavePage_Draft_1() throws Throwable {
        String pageCode = "pagina_test_1";
        assertNull(this._pageManager.getDraftPage(pageCode));
        try {
            IPage root = this._pageManager.getDraftRoot();
            Map<String, String> params = new HashMap<String, String>();
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
            params.put("parentPageCode", root.getCode());
            params.put("langit", "Pagina Test 1");
            params.put("langen", "Test Page 1");
            params.put("model", "home");
            params.put("group", Group.FREE_GROUP_NAME);
            params.put("pageCode", pageCode);
            String result = this.executeSave(params, "admin");
            assertEquals(Action.SUCCESS, result);
            IPage addedPage = this._pageManager.getDraftPage(pageCode);
            assertNotNull(addedPage);
            assertEquals("Pagina Test 1", addedPage.getMetadata().getTitles().getProperty("it"));
        } catch (Throwable t) {
            throw t;
        } finally {
            this._pageManager.deletePage(pageCode);
        }
    }
    
    @Test
	void testSavePage_Draft_2() throws Throwable {
        String pageCode = "pagina_test_2";
        assertNull(this._pageManager.getDraftPage(pageCode));
        try {
            IPage root = this._pageManager.getDraftRoot();
            Map<String, String> params = new HashMap<String, String>();
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
            params.put("parentPageCode", root.getCode());
            params.put("langit", "Pagina Test 2");
            params.put("langen", "Test Page 2");
            params.put("model", "internal");
            params.put("group", Group.FREE_GROUP_NAME);
            params.put("pageCode", pageCode);
            String result = this.executeSave(params, "admin");
            assertEquals(Action.SUCCESS, result);
            IPage addedPage = this._pageManager.getDraftPage(pageCode);
            assertNotNull(addedPage);
            this.executeSetDefaultWidgets(params, "admin");
            addedPage = this._pageManager.getDraftPage(pageCode);
            assertNotNull(addedPage);
            assertEquals("Pagina Test 2", addedPage.getMetadata().getTitles().getProperty("it"));
            Widget[] showlets = addedPage.getWidgets();
            PageModel pageModel = this._pageModelManager.getPageModel(addedPage.getMetadata().getModelCode());
            assertEquals(pageModel.getFrames().length, showlets.length);
            for (int i = 0; i < showlets.length; i++) {
                Widget widget = showlets[i];
                if (i == 3) {
                    assertNotNull(widget);
                    WidgetType type = this._widgetTypeManager.getWidgetType(widget.getTypeCode());
                    assertEquals("leftmenu", type.getCode());
                    assertEquals(1, type.getTypeParameters().size());
                    assertNull(type.getConfig());
                    ApsProperties config = widget.getConfig();
                    assertEquals(1, config.size());
                    assertEquals("code(homepage).subtree(1)", config.getProperty("navSpec"));
                } else {
                    assertNull(widget);
                }
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            this._pageManager.deletePage(pageCode);
        }
    }
    
    @Test
	void testSavePage_3() throws Throwable {
        String pageCode = "pagina_test_3";
        assertNull(this._pageManager.getDraftPage(pageCode));
        try {
            IPage root = this._pageManager.getDraftRoot();
            Map<String, String> params = new HashMap<>();
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
            params.put("parentPageCode", root.getCode());
            params.put("langit", "Pagina Test 3");
            params.put("langen", "Test Page 3");
            params.put("model", "home");
            params.put("group", Group.FREE_GROUP_NAME);
            params.put("pageCode", pageCode);
            String result = this.executeSave(params, "admin");
            assertEquals(Action.SUCCESS, result);
            IPage addedPage = this._pageManager.getDraftPage(pageCode);
            assertNotNull(addedPage);
            assertEquals("Pagina Test 3", addedPage.getMetadata().getTitles().getProperty("it"));
            assertEquals("Test Page 3", addedPage.getMetadata().getTitles().getProperty("en"));
            
            Date lastModified = addedPage.getMetadata().getUpdatedAt();
            assertNotNull(lastModified);
            synchronized (this) {
                wait(1000);
            }
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
            params.put("langen", "Test Page 3 Modified");
            result = this.executeSave(params, "admin");
            assertEquals(Action.SUCCESS, result);
            IPage modifiedPage = this._pageManager.getDraftPage(pageCode);
            assertNotNull(modifiedPage);
            
            assertEquals("Test Page 3 Modified", modifiedPage.getMetadata().getTitles().getProperty("en"));
            assertTrue(modifiedPage.getMetadata().getUpdatedAt().after(lastModified));
        } catch (Throwable t) {
            throw t;
        } finally {
            this._pageManager.deletePage(pageCode);
        }
    }

    @Test
	void testSavePage_Clone() throws Throwable {
        String copiedPageCode = "pagina_2";
        IPage copiedPage = this._pageManager.getDraftPage(copiedPageCode);
        assertNotNull(copiedPage);
        String pageCode = "pagina_test";
        assertNull(this._pageManager.getDraftPage(pageCode));
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.PASTE));
            params.put("parentPageCode", "pagina_1");
            params.put("copyPageCode", copiedPageCode);
            params.put("langit", "Pagina Test 1");
            params.put("langen", "Test Page 1");
            params.put("model", "aaaa");
            params.put("group", "aaaa");
            params.put("pageCode", pageCode);
            String result = this.executeSave(params, "admin");
            assertEquals(Action.SUCCESS, result);
            IPage addedPage = this._pageManager.getDraftPage(pageCode);
            assertNotNull(addedPage);
            IPage addedPublicPage = this._pageManager.getOnlinePage(pageCode);
            assertNull(addedPublicPage);
            assertEquals("Pagina Test 1", addedPage.getMetadata().getTitles().getProperty("it"));
            assertEquals("Test Page 1", addedPage.getMetadata().getTitles().getProperty("en"));
            PageMetadata addedMetadata = addedPage.getMetadata();
            assertNotNull(addedMetadata);
            PageMetadata expectedMetadata = copiedPage.getMetadata();
            addedMetadata.setTitles(expectedMetadata.getTitles());
            PageTestUtil.comparePageMetadata(expectedMetadata, addedMetadata, null);
        } catch (Throwable t) {
            throw t;
        } finally {
            this._pageManager.deletePage(pageCode);
        }
    }
    
    private String executeSave(Map<String, String> params, String username) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/Page", "save");
        this.addParameters(params);
        String result = this.executeAction();
        return result;
    }

    private String executeSetDefaultWidgets(Map<String, String> params, String username) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/Page", "setDefaultWidgets");
        this.addParameters(params);
        String result = this.executeAction();
        return result;
    }
    
    @Test
	void testTrashPage() throws Throwable {
        String result = this.executeTrashPage(this._pageManager.getDraftRoot().getCode(), "admin");
        assertEquals("pageTree", result);
        ActionSupport action = this.getAction();
        assertEquals(1, action.getActionErrors().size());

        result = this.executeTrashPage("pagina_1", "admin");
        assertEquals("pageTree", result);
        action = this.getAction();
        assertEquals(1, action.getActionErrors().size());

        result = this.executeTrashPage("pagina_12", "admin");
        assertEquals("pageTree", result);
        action = this.getAction();
        assertEquals(1, action.getActionErrors().size());

        try {
            this._pageManager.setPageOffline("pagina_12");
            result = this.executeTrashPage("pagina_12", "admin");
            assertEquals(Action.SUCCESS, result);
            action = this.getAction();
            assertEquals(0, action.getActionErrors().size());
        } catch (Exception e) {
            throw e;
        } finally {
            this._pageManager.setPageOnline("pagina_12");
        }
    }

    private String executeTrashPage(String selectedPageCode, String username) throws Throwable {
        this.setUserOnSession(username);
        this.initAction("/do/Page", "trash");
        this.addParameter("selectedNode", selectedPageCode);
        String result = this.executeAction();
        return result;
    }

    @Test
	void testGetBreadCrumbs() throws Throwable {
        IPage customers_page = this._pageManager.getDraftPage("customers_page");
        String result = this.executeActionOnPage(customers_page.getCode(), "pageManagerCustomers", "edit");
        assertEquals(Action.SUCCESS, result);
        PageAction action = (PageAction) this.getAction();
        List<IPage> breadCrumbs = action.getBreadCrumbsTargets("pagina_11");
        assertEquals(3, breadCrumbs.size());
        assertEquals("homepage", breadCrumbs.get(0).getCode());
        assertEquals("pagina_1", breadCrumbs.get(1).getCode());
        assertEquals("pagina_11", breadCrumbs.get(2).getCode());
        breadCrumbs = action.getBreadCrumbsTargets("homepage");
        assertEquals(1, breadCrumbs.size());
        assertEquals("homepage", breadCrumbs.get(0).getCode());
        breadCrumbs = action.getBreadCrumbsTargets("wrongCode");
        assertNull(breadCrumbs);
    }

    @Test
	void testGetBreadCrumbsTargets() throws Throwable {
        IPage customers_page = this._pageManager.getDraftPage("customers_page");
        String result = this.executeActionOnPage(customers_page.getCode(), "pageManagerCustomers", "edit");
        assertEquals(Action.SUCCESS, result);
        PageAction action = (PageAction) this.getAction();
        List<IPage> targets = action.getBreadCrumbsTargets("pagina_11");
        assertNotNull(targets);
        assertEquals(3, targets.size());
        assertEquals("pagina_11", targets.get(2).getCode());
        assertEquals("homepage", targets.get(0).getCode());

        targets = action.getBreadCrumbsTargets("homepage");
        assertNotNull(targets);
        assertEquals(1, targets.size());
        assertEquals("homepage", targets.get(0).getCode());

        targets = action.getBreadCrumbsTargets("wrongCode");
        assertNull(targets);
    }

    @Test
	void testSetOffline_Wrong() throws Throwable {
        this.checkActionOnPage("checkSetOffline", "homepage", "pageManagerCustomers", "pageTree", "error.page.userNotAllowed");
        this.checkActionOnPage("doSetOffline", "homepage", "pageManagerCustomers", "pageTree", "error.page.userNotAllowed");
        this.checkActionOnPage("setOffline", "/do/rs/Page", "homepage", "pageManagerCustomers", "pageTree", "error.page.userNotAllowed");
        this.checkPageResponse("homepage", "error.page.userNotAllowed");

        this.checkActionOnPage("checkSetOffline", "homepage", "admin", "pageTree", "error.page.offlineHome.notAllowed");
        this.checkActionOnPage("doSetOffline", "homepage", "admin", "pageTree", "error.page.offlineHome.notAllowed");
        this.checkActionOnPage("setOffline", "/do/rs/Page", "homepage", "admin", "pageTree", "error.page.offlineHome.notAllowed");
        this.checkPageResponse("homepage", "error.page.offlineHome.notAllowed");

        this.checkActionOnPage("checkSetOffline", "service", "admin", "pageTree", "error.page.offline.notAllowed");
        this.checkActionOnPage("doSetOffline", "service", "admin", "pageTree", "error.page.offline.notAllowed");
        this.checkActionOnPage("setOffline", "/do/rs/Page", "service", "admin", "pageTree", "error.page.offline.notAllowed");
        this.checkPageResponse("service", "error.page.offline.notAllowed");
    }

    @Test
	void testSetOnlineOffline() throws Throwable {
        String pageCode = "temp";
        try {
            this.addPage(pageCode);
            IPage page = this._pageManager.getDraftPage(pageCode);
            assertFalse(page.isOnline());
            assertFalse(page.isChanged());

            this.checkActionOnPage("checkSetOffline", pageCode, "admin", Action.SUCCESS, null);
            page = this._pageManager.getDraftPage(pageCode);
            assertFalse(page.isOnline());
            assertFalse(page.isChanged());

            this.checkActionOnPage("doSetOnline", pageCode, "admin", Action.SUCCESS, null);
            page = _pageManager.getDraftPage(pageCode);
            assertTrue(page.isOnline());
            assertFalse(page.isChanged());

            this.checkActionOnPage("doSetOffline", pageCode, "admin", Action.SUCCESS, null);
            page = _pageManager.getDraftPage(pageCode);
            assertFalse(page.isOnline());
            assertFalse(page.isChanged());
        } catch (Throwable t) {
            throw t;
        } finally {
            this._pageManager.deletePage(pageCode);
        }
    }

    @Test
	void testSetOnlineOfflineJson() throws Throwable {
        String pageCode = "temp";
        try {
            this.addPage(pageCode);
            IPage page = _pageManager.getDraftPage(pageCode);
            assertFalse(page.isOnline());
            assertFalse(page.isChanged());

            this.checkActionOnPage("setOffline", "/do/rs/Page", pageCode, "admin", Action.SUCCESS, null);
            this.checkPageResponse(pageCode, null);
            page = _pageManager.getDraftPage(pageCode);
            assertFalse(page.isOnline());
            assertFalse(page.isChanged());

            this.checkActionOnPage("setOnline", "/do/rs/Page", pageCode, "admin", Action.SUCCESS, null);
            this.checkPageResponse(pageCode, null);
            page = _pageManager.getDraftPage(pageCode);
            assertTrue(page.isOnline());
            assertFalse(page.isChanged());

            this.checkActionOnPage("setOffline", "/do/rs/Page", pageCode, "admin", Action.SUCCESS, null);
            this.checkPageResponse(pageCode, null);
            page = _pageManager.getDraftPage(pageCode);
            assertFalse(page.isOnline());
            assertFalse(page.isChanged());
        } catch (Throwable t) {
            throw t;
        } finally {
            this._pageManager.deletePage(pageCode);
        }
    }

    @Test
	void testJoinRemoveExtraGroup() throws Throwable {
        this.setUserOnSession("admin");

        // Add 2 groups
        String[] groupsToAdd = new String[]{"group1", "group2"};
        this.initAction("/do/Page", "joinExtraGroup");
        this.addParameter("extraGroupNameToAdd", groupsToAdd);
        assertEquals(Action.SUCCESS, this.executeAction());
        Set<String> extraGroups = ((PageAction) this.getAction()).getExtraGroups();
        assertEquals(2, extraGroups.size());
        assertTrue(extraGroups.containsAll(Arrays.asList(groupsToAdd)));

        // Remove 1 group
        this.initAction("/do/Page", "removeExtraGroup");
        this.addParameter("extraGroups", groupsToAdd);
        this.addParameter("extraGroupNameToRemove", "group1");
        assertEquals(Action.SUCCESS, this.executeAction());
        extraGroups = ((PageAction) this.getAction()).getExtraGroups();
        assertEquals(1, extraGroups.size());
        assertTrue(extraGroups.contains("group2"));
    }
    
    private void addPage(String pageCode) throws EntException {
        IPage parentPage = _pageManager.getOnlinePage("service");
        PageModel pageModel = this._pageModelManager.getPageModel(parentPage.getMetadata().getModelCode());
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel, true, "pagina temporanea", null, null, false, null, null);
        ApsProperties properties = new ApsProperties();
		properties.setProperty("actionPath", "/path");
        Widget widgetToAdd = PageTestUtil.createWidget("formAction", properties);
        Widget[] widgets = {widgetToAdd};
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage.getCode(), "free", pageModel, metadata, widgets);
        this._pageManager.addPage(pageToAdd);
    }

    private void checkActionOnPage(String actionName, String namespace, String pageCode, String username, String expectedResult,
            String errorLabel) throws Throwable {
        this.setUserOnSession(username);
        this.initAction(namespace, actionName);
        this.addParameter("pageCode", pageCode);
        String actualResult = this.executeAction();
        assertEquals(expectedResult, actualResult);
        assertEquals(0, this.getAction().getFieldErrors().size());
        if (errorLabel != null) {
            String[] errors = this.getAction().getActionErrors().toArray(new String[0]);
            assertEquals(1, errors.length);
            assertEquals(this.getAction().getText(errorLabel), errors[0]);
        } else {
            assertEquals(0, this.getAction().getActionErrors().size());
        }
    }

    private void checkPageResponse(String pageCode, String errorLabel, String... expectedFieldErrors) {
        PageResponse response = ((PageAction) this.getAction()).getPageResponse();
        if (pageCode == null) {
            assertNull(response.getPage());
        } else {
            assertEquals(pageCode, response.getPage().getCode());
        }
        if (errorLabel != null) {
            String[] errors = response.getActionErrors().toArray(new String[0]);
            assertEquals(1, errors.length);
            assertEquals(this.getAction().getText(errorLabel), errors[0]);
        } else {
            assertEquals(0, response.getActionErrors().size());
        }
        if (expectedFieldErrors != null && expectedFieldErrors.length > 0) {
            assertEquals(expectedFieldErrors.length, response.getFieldErrors().size());
            List<String> expectedFieldErrorsList = Arrays.asList(expectedFieldErrors);
            expectedFieldErrorsList.removeAll(response.getFieldErrors().keySet());
            assertEquals(0, expectedFieldErrors.length);
        } else {
            assertEquals(0, response.getFieldErrors().size());
        }
    }

    private void checkActionOnPage(String actionName, String pageCode, String username, String expectedResult, String errorLabel)
            throws Throwable {
        this.checkActionOnPage(actionName, "/do/Page", pageCode, username, expectedResult, errorLabel);
    }

    @BeforeEach
    private void init() throws Exception {
        try {
            this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
            this._pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
            this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IPageManager _pageManager = null;
    private IPageModelManager _pageModelManager = null;
    private IWidgetTypeManager _widgetTypeManager;

}
