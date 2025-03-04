package org.entando.entando.plugins.jacms.web.page;

import org.entando.entando.ent.exception.EntException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.PagesStatusDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PageConfigurationControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void testPageConfiguration() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                .build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/pages/{pageCode}/configuration", "homepage")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.payload.online", is(true)));

        super.testCors("/pages/homepage/configuration");
    }

    /**
     * Given: a page only in draft When: the user request the configuration for
     * status draft Then the result is ok
     *
     * Given: a page only in draft When: the user request the configuration for
     * status published Then an error with status code 400 is raised
     *
     * @throws Exception
     */
    @Test
    void testGetPageConfigurationOnLineNotFound() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_ONLINE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("3")));
        } finally {
            this.pageManager.deletePage(pageCode);
        }

    }

    @Test
    void testPutPageConfiguration() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            String payloadWithOtherModelId = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + " \"modelId\": \"list\",\n"
                    + " \"contentId\": \"EVN24\"\n"
                    + "  }\n"
                    + "}";
            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payloadWithOtherModelId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            
            result
                .andExpect(jsonPath("$.errors", hasSize(0)))
                .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                .andExpect(jsonPath("$.payload.config.modelId", is("list")))
                .andExpect(jsonPath("$.payload.config.contentId", is("EVN24")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testPutPageConfigurationFilterFormat() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            String payload = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"EVN24\",\n"
                    + "      \"modelId\": \"default\",\n"
                    + "      \"userFilters\": [\n"
                    + "         {\"attributeFilter\": false, \"key\": \"fulltext\"},\n"
                    + "         {\"attributeFilter\": false, \"key\": \"category\"}\n"
                    + "      ]\n"
                    + "  }\n"
                    + "}";

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result
                .andDo(resultPrint())
                .andExpect(jsonPath("$.errors", hasSize(0)))
                .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                .andExpect(jsonPath("$.payload.config.size()", is(3)))
                .andExpect(jsonPath("$.payload.config.userFilters.size()", is(2)));

            IPage updatedPage = pageManager.getDraftPage(pageCode);
            Widget widget = updatedPage.getWidgets()[0];
            assertThat(widget.getConfig().get("userFilters"),
                    equalTo("(attributeFilter=false;key=fulltext)+(attributeFilter=false;key=category)"));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testPutPageConfigurationCategoriesFormat() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            String payload = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"EVN24\",\n"
                    + "      \"modelId\": \"default\",\n"
                    + "      \"categories\": [\"resCat1\", \"resCat2\"]\n"
                    + "  }\n"
                    + "}";

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result
                    .andDo(resultPrint())
                    .andExpect(jsonPath("$.errors", hasSize(0)))
                    .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                    .andExpect(jsonPath("$.payload.config.size()", is(3)))
                    .andExpect(jsonPath("$.payload.config.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.config.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.config.categories[1]", is("resCat2")));

            IPage updatedPage = pageManager.getDraftPage(pageCode);
            Widget widget = updatedPage.getWidgets()[0];

            assertThat(widget.getConfig().get("categories"),
                    equalTo("resCat1,resCat2"));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testPutPageConfigurationContentsFormat() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String payload = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"EVN24\",\n"
                    + "      \"modelId\": \"default\",\n"
                    + "      \"contents\": [\n"
                    + "         {\"contentId\": \"ABC1\", \"contentDescription\": \"My Content 1\"},\n"
                    + "         {\"contentId\": \"ABC2\", \"contentDescription\": \"My Content 2\"}\n"
                    + "      ]\n"
                    + "  }\n"
                    + "}";

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result
                    .andDo(resultPrint())
                    .andExpect(jsonPath("$.errors", hasSize(0)))
                    .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                    .andExpect(jsonPath("$.payload.config.size()", is(3)))
                    .andExpect(jsonPath("$.payload.config.contents.size()", is(2)))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentId", is("ABC1")))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentDescription", is("My Content 1")))
                    .andExpect(jsonPath("$.payload.config.contents[1].contentId", is("ABC2")))
                    .andExpect(jsonPath("$.payload.config.contents[1].contentDescription", is("My Content 2")));

            IPage updatedPage = pageManager.getDraftPage(pageCode);
            Widget widget = updatedPage.getWidgets()[0];

            assertThat(widget.getConfig().get("contents"),
                    equalTo("[{contentId=ABC1,contentDescription=My Content 1},{contentId=ABC2,contentDescription=My Content 2}]"));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testPutPageConfigurationInvalidFrame() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "XXX"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            String getResult = result.andReturn().getResponse().getContentAsString();
            result.andExpect(status().isBadRequest());

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testPutPageConfigurationWrongFrame() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "500"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.errors[0].code", is("2")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testPutPageDescription() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);//, "row_content_viewer_list");
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            InputStream isJsonPostValid = this.getClass().getResourceAsStream("1_PUT_page_description_row_content_viewer_list.json");
            String jsonPostValid = FileTextReader.getText(isJsonPostValid);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk())
                    .andDo(resultPrint())
                    .andExpect(jsonPath("$.errors", hasSize(0)))
                    .andExpect(jsonPath("$.payload.code", is("row_content_viewer_list")))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentId", is("EVN21")))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentDescription", is("content EVN21 description")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testPutDynamicProperties() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);//, "row_content_viewer_list");
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            InputStream isJsonPostValid = this.getClass().getResourceAsStream("1_PUT_dynamic_properties.json");
            String jsonPostValid = FileTextReader.getText(isJsonPostValid);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk())
                    .andDo(resultPrint())
                    .andExpect(jsonPath("$.errors", hasSize(0)))
                    .andExpect(jsonPath("$.payload.code", is("row_content_viewer_list")))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentId", is("EVN21")))
                    .andExpect(jsonPath("$.payload.config.contents[0].modelId", is("list")))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentDescription", is("Description for dynamic properties")))
                    .andExpect(jsonPath("$.payload.config.maxElemForItem", is("4")))
                    .andExpect(jsonPath("$.payload.config.title_en", is("eng title")))
                    .andExpect(jsonPath("$.payload.config.title_it", is("it title")))
                    .andExpect(jsonPath("$.payload.config.pageLink", is("errorpage")))
                    .andExpect(jsonPath("$.payload.config.linkDescr_en", is("en ltext")))
                    .andExpect(jsonPath("$.payload.config.linkDescr_it", is("it ltext")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testGetWithMultipleFrames() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);//, "row_content_viewer_list");
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            InputStream isJsonPostValid = this.getClass().getResourceAsStream("1_PUT_dynamic_properties.json");
            String jsonPostValid = FileTextReader.getText(isJsonPostValid);

            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 2})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk())
                    .andDo(resultPrint());

            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(jsonPostValid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk())
                    .andDo(resultPrint());

            result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk())
                    .andDo(resultPrint())
                    .andExpect(jsonPath("$.payload[0].code", is("row_content_viewer_list")))
                    .andExpect(jsonPath("$.payload[0].config.contents[0].contentId", is("EVN21")))
                    .andExpect(jsonPath("$.payload[0].config.contents[0].modelId", is("list")))
                    .andExpect(jsonPath("$.payload[0].config.contents[0].contentDescription", is("Description for dynamic properties")))
                    .andExpect(jsonPath("$.payload[0].config.maxElemForItem", is("4")))
                    .andExpect(jsonPath("$.payload[0].config.title_en", is("eng title")))
                    .andExpect(jsonPath("$.payload[0].config.title_it", is("it title")))
                    .andExpect(jsonPath("$.payload[0].config.pageLink", is("errorpage")))
                    .andExpect(jsonPath("$.payload[0].config.linkDescr_en", is("en ltext")))
                    .andExpect(jsonPath("$.payload[0].config.linkDescr_it", is("it ltext")))
                    .andExpect(jsonPath("$.payload[1]", isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[2].code", is("row_content_viewer_list")))
                    .andExpect(jsonPath("$.payload[2].config.contents[0].contentId", is("EVN21")))
                    .andExpect(jsonPath("$.payload[2].config.contents[0].modelId", is("list")))
                    .andExpect(jsonPath("$.payload[2].config.contents[0].contentDescription", is("Description for dynamic properties")))
                    .andExpect(jsonPath("$.payload[2].config.maxElemForItem", is("4")))
                    .andExpect(jsonPath("$.payload[2].config.title_en", is("eng title")))
                    .andExpect(jsonPath("$.payload[2].config.title_it", is("it title")))
                    .andExpect(jsonPath("$.payload[2].config.pageLink", is("errorpage")))
                    .andExpect(jsonPath("$.payload[2].config.linkDescr_en", is("en ltext")))
                    .andExpect(jsonPath("$.payload[2].config.linkDescr_it", is("it ltext")))
                    .andExpect(jsonPath("$.payload[3]", isEmptyOrNullString()));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testDeletePageConfigurationWithInvalidFrameId() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(delete("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "XXX"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("40")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testDeletePageConfigurationWithWrongFrameId() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(delete("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 9999})
                            .param("status", IPageService.STATUS_DRAFT)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testGetPageWidgetConfiguration() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 999})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.errors[0].code", is("2")));

            result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "ASD"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("40")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }


    @Test
    void testRestoreWithPageChangedShouldUpdatePageStatus() throws EntException {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES,
                        Permission.ENTER_BACKEND)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        String pageCode = "draft_page_1005";
        String widgetCode = "login_form";

        try {
            // create a page and set it as online
            PageModel pageModel = this.pageModelManager.getPageModel("internal");
            Page mockPage = createPage(pageCode, pageModel);
            this.pageManager.addPage(mockPage);
            this.pageManager.setPageOnline(pageCode);
            PagesStatusDto pageStatusBeforeSettingWidget = getPageStatus(accessToken);

            assertTrue(this.pageManager.getDraftPage(pageCode).isOnline());
            assertFalse(this.pageManager.getDraftPage(pageCode).isChanged());

            // add a widget to the page
            WidgetConfigurationRequest widgetRequest = new WidgetConfigurationRequest();
            widgetRequest.setCode(widgetCode);
            widgetRequest.setConfig(new HashMap<>());

            mockMvc.perform(put("/pages/{pageCode}/widgets/{frameId}", pageCode, 0)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(widgetRequest)))
                    .andExpect(status().isOk());
            PagesStatusDto pageStatusBeforeRestore = getPageStatus(accessToken);

            assertTrue(this.pageManager.getDraftPage(pageCode).isOnline());
            assertTrue(this.pageManager.getDraftPage(pageCode).isChanged());
            assertEquals(pageStatusBeforeSettingWidget.getDraft() + 1, pageStatusBeforeRestore.getDraft());
            assertEquals(pageStatusBeforeSettingWidget.getPublished() - 1, pageStatusBeforeRestore.getPublished());
            assertEquals(pageStatusBeforeSettingWidget.getUnpublished(), pageStatusBeforeRestore.getUnpublished());

            // restore the page
            mockMvc.perform(put("/pages/{code}/configuration/restore", pageCode)
                    .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk());
            PagesStatusDto pageStatusAfterRestore = getPageStatus(accessToken);

            assertTrue(this.pageManager.getDraftPage(pageCode).isOnline());
            assertFalse(this.pageManager.getDraftPage(pageCode).isChanged());

            assertEquals(pageStatusBeforeRestore.getDraft() - 1, pageStatusAfterRestore.getDraft());
            assertEquals(pageStatusBeforeRestore.getPublished() + 1, pageStatusAfterRestore.getPublished());
            assertEquals(pageStatusBeforeRestore.getUnpublished(), pageStatusAfterRestore.getUnpublished());

        } catch (Exception e) {
            Assertions.fail(e);
        } finally {
            pageManager.deletePage(pageCode);
        }
    }


    @Test
    void testRestoreWithPageNotChangedShouldNOTUpdatePageStatus() throws EntException {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES,
                        Permission.ENTER_BACKEND)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        String pageCode = "draft_page_1005";
        String widgetCode = "login_form";

        try {
            // create a page and set it as online
            PageModel pageModel = this.pageModelManager.getPageModel("internal");
            Page mockPage = createPage(pageCode, pageModel);
            this.pageManager.addPage(mockPage);
            this.pageManager.setPageOnline(pageCode);
            PagesStatusDto pageStatusBeforeRestore = getPageStatus(accessToken);

            assertTrue(this.pageManager.getDraftPage(pageCode).isOnline());
            assertFalse(this.pageManager.getDraftPage(pageCode).isChanged());

            // restore the page that is not modified => should not update pagestatus
            mockMvc.perform(put("/pages/{code}/configuration/restore", pageCode)
                    .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk());
            PagesStatusDto pageStatusAfterRestore = getPageStatus(accessToken);

            assertTrue(this.pageManager.getDraftPage(pageCode).isOnline());
            assertFalse(this.pageManager.getDraftPage(pageCode).isChanged());

            assertEquals(pageStatusBeforeRestore.getDraft(), pageStatusAfterRestore.getDraft());
            assertEquals(pageStatusBeforeRestore.getPublished(), pageStatusAfterRestore.getPublished());
            assertEquals(pageStatusBeforeRestore.getUnpublished(), pageStatusAfterRestore.getUnpublished());

        } catch (Exception e) {
            Assertions.fail(e);
        } finally {
            pageManager.deletePage(pageCode);
        }
    }


    private PagesStatusDto getPageStatus(String accessToken) throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/dashboard/pageStatus")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(resultPrint());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString().replace("{\"payload\":", "").replace(",\"metaData\":{},\"errors\":[]}", "");

        return mapper.readValue(contentAsString, PagesStatusDto.class);
    }

    /**
     * creates a page without configured frames than applies the default widgets
     */
    @Test
    void testApplyDefautWidgets() throws Exception {
        String pageCode = "draft_page_100";
        try {
            PageModel pageModel = this.pageModelManager.getPageModel("internal");
            Page mockPage = createPage(pageCode, pageModel);

            mockPage.setWidgets(new Widget[mockPage.getWidgets().length]);

            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));

            Widget[] defaultWidgetConfiguration = pageModel.getDefaultWidget();
            
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.widgets", Matchers.hasSize(pageModel.getConfiguration().length)));
            for (int i = 0; i < pageModel.getConfiguration().length; i++) {
                String path = String.format("$.payload.widgets[%d]", i);
                result.andExpect(jsonPath(path, is(nullValue())));
            }

            result = mockMvc
                    .perform(put("/pages/{pageCode}/configuration/defaultWidgets", new Object[]{pageCode})
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            for (int i = 0; i < pageModel.getConfiguration().length; i++) {
                String path = String.format("$.payload.widgets[%d]", i);

                if (null != defaultWidgetConfiguration[i]) {
                    WidgetConfigurationDto exp = new WidgetConfigurationDto(defaultWidgetConfiguration[i].getTypeCode(), defaultWidgetConfiguration[i].getConfig());
                    Map actual = mapper.convertValue(exp, Map.class); //jsonPath workaround
                    result.andExpect(jsonPath(path, is(actual)));
                } else {
                    result.andExpect(jsonPath(path, is(nullValue())));
                }
            }

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    protected Page createPage(String pageCode, PageModel pageModel) {
        IPage parentPage = pageManager.getDraftPage("service");
        if (null == pageModel) {
            pageModel = this.pageModelManager.getPageModel(parentPage.getMetadata().getModelCode());
        }
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel, true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = new ApsProperties();//PageTestUtil.createProperties("modelId", "default", "contentId", "EVN24");
        config.put("contentId", "EVN24");
        config.put("modelId", "default");
        Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config);
        Widget[] widgets = new Widget[pageModel.getFrames().length];
        widgets[0] = widgetToAdd;
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage.getCode(), "free", pageModel, metadata, widgets);
        return pageToAdd;
    }

}
