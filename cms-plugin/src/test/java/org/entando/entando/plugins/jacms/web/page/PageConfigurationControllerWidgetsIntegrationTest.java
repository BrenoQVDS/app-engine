package org.entando.entando.plugins.jacms.web.page;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

class PageConfigurationControllerWidgetsIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;
    @Autowired
    private IPageModelManager pageModelManager;

    @Test
    void testConfigureListViewer() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode);
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

            String payloadWithInvalidContentType = "{\n"
                    + "  \"code\": \"content_viewer_list\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentType\": \"LOL\",\n"
                    + "      \"maxElements\": \"15\"\n"
                    + "  }\n"
                    + "}";

            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_ONLINE)
                            .content(payloadWithInvalidContentType)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isConflict());

            //-------------------
            String payloadWithInvalidModelId = "{\n"
                    + "  \"code\": \"content_viewer_list\",\n"
                    + "  \"config\": {\n"
                    + " \"contentType\": \"ART\",\n"
                    + " \"modelId\": \"9999999999\",\n"
                    + " \"maxElements\": \"15\"\n"
                    + "  }\n"
                    + "}";

            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_ONLINE)
                            .content(payloadWithInvalidModelId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isConflict());

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testContentViewer() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            String payloadWithInvalidContentId = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"ART1120000000\",\n"
                    + "      \"modelId\": \"default\"\n"
                    + "  }\n"
                    + "}";

            result = this.executePutPageConfig(payloadWithInvalidContentId,
                    pageCode, "0", accessToken, status().isConflict());
            result.andExpect(jsonPath("$.errors", hasSize(1)));

            result = this.executePutPageConfig(payloadWithInvalidContentId,
                    pageCode, "45", accessToken, status().isNotFound());
            result.andExpect(jsonPath("$.errors", hasSize(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("2")));
            result = this.executePutPageConfig(payloadWithInvalidContentId,
                    pageCode, "xxx", accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.errors", hasSize(1)));
            result.andExpect(jsonPath("$.errors[0].code", is("40")));
            result.andDo(resultPrint());

            String validContentIdWithIncompatibleGroup = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"ART112\",\n"
                    + "      \"modelId\": \"default\"\n"
                    + "  }\n"
                    + "}";
            result = this.executePutPageConfig(validContentIdWithIncompatibleGroup,
                    pageCode, "0", accessToken, status().isConflict());
            result.andExpect(jsonPath("$.errors", hasSize(1)));
            result.andDo(resultPrint());

            String validContent_1 = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"ART1\",\n"
                    + "      \"modelId\": \"default\"\n"
                    + "  }\n"
                    + "}";
            result = this.executePutPageConfig(validContent_1,
                    pageCode, "0", accessToken, status().isOk());
            result.andExpect(jsonPath("$.errors", hasSize(0)));
            result.andExpect(jsonPath("$.payload.code", is("content_viewer")));
            result.andExpect(jsonPath("$.payload.config.size()", is(2)));
            result.andDo(resultPrint());

            String validContent_2 = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"ART1\"\n"
                    + "  }\n"
                    + "}";
            result = this.executePutPageConfig(validContent_2,
                    pageCode, "0", accessToken, status().isOk());
            result.andExpect(jsonPath("$.errors", hasSize(0)));
            result.andExpect(jsonPath("$.payload.code", is("content_viewer")));
            result.andExpect(jsonPath("$.payload.config.size()", is(1)));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testConfigurationRestore() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            //checking page draft config
            ResultActions result = this.executeGetPageConfig(pageCode, IPageService.STATUS_DRAFT, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.widgets", hasSize(4)));
            result.andExpect(jsonPath("$.payload.widgets[0].config.contentId", is("ART11")));

            //publishing page
            String putPageOnlinePayload = "{\"status\": \"published\"}";
            result = mockMvc.perform(
                    put("/pages/{pageCode}/status", pageCode)
                    .content(putPageOnlinePayload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            //checking page online config
            result = this.executeGetPageConfig(pageCode, IPageService.STATUS_ONLINE, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.widgets", hasSize(4)));
            result.andExpect(jsonPath("$.payload.widgets[0].config.contentId", is("ART11")));

            //changing configuration
            String payloadWithValidContentId = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"ART187\",\n"
                    + "      \"modelId\": \"default\"\n"
                    + "  }\n"
                    + "}";
            result = this.executePutPageConfig(payloadWithValidContentId,
                    pageCode, "0", accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.config.contentId", is("ART187")));

            //checking page draft config
            result = this.executeGetPageConfig(pageCode, IPageService.STATUS_DRAFT, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.widgets", hasSize(4)));
            result.andExpect(jsonPath("$.payload.widgets[0].config.contentId", is("ART187")));

            //restoring page configuration
            result = mockMvc
                    .perform(put("/pages/{pageCode}/configuration/restore", new Object[]{pageCode})
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            //checking page draft config
            result = this.executeGetPageConfig(pageCode, IPageService.STATUS_DRAFT, accessToken, status().isOk());
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.widgets", hasSize(4)));
            result.andExpect(jsonPath("$.payload.widgets[0].config.contentId", is("ART11")));

        } finally {
            this.pageManager.setPageOffline(pageCode);
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    void testConfigurationDelete() throws Exception {
        String pageCode = "draft_page_x";
        try {
            Page mockPage = createPage(pageCode);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                    .withAuthorization(Group.FREE_GROUP_NAME, "managePages", Permission.MANAGE_PAGES)
                    .build();
            String accessToken = mockOAuthInterceptor(user);

            //checking page draft config
            ResultActions result = this.executeGetPageConfig(pageCode, IPageService.STATUS_DRAFT, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.widgets", hasSize(4)));
            result.andExpect(jsonPath("$.payload.widgets[0].config.contentId", is("ART11")));

            result = this.executeDeletePageConfig(pageCode, 0, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.code", is("0")));
            result.andExpect(jsonPath("$.metaData.status", is(IPageService.STATUS_DRAFT)));

            result = this.executeDeletePageConfig(pageCode, 0, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.code", is("0")));
            result.andExpect(jsonPath("$.metaData.status", is(IPageService.STATUS_DRAFT)));

            result = this.executeDeletePageConfig(pageCode, 3, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.code", is("3")));
            result.andExpect(jsonPath("$.metaData.status", is(IPageService.STATUS_DRAFT)));

            result = this.executeDeletePageConfig(pageCode, 4, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.code", is("4")));
            result.andExpect(jsonPath("$.metaData.status", is(IPageService.STATUS_DRAFT)));

            result = this.executeDeletePageConfig(pageCode, 80, accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.code", is("80")));
            result.andExpect(jsonPath("$.metaData.status", is(IPageService.STATUS_DRAFT)));

        } finally {
            this.pageManager.setPageOffline(pageCode);
            this.pageManager.deletePage(pageCode);
        }
    }

    private ResultActions executeGetPageConfig(String pageCode, String pageStatus, String accessToken, ResultMatcher rm) throws Exception {
        ResultActions result = mockMvc
                .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                        .param("status", pageStatus)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(rm);
        return result;
    }

    private ResultActions executePutPageConfig(String payload, String pageCode,
            String pos, String accessToken, ResultMatcher rm) throws Exception {
        ResultActions result = mockMvc
                .perform(put("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, pos})
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(resultPrint());
        result.andExpect(rm);
        return result;
    }

    private ResultActions executeDeletePageConfig(String pageCode,
            Integer pos, String accessToken, ResultMatcher rm) throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/pages/{pageCode}/widgets/{frameId}", new Object[]{pageCode, pos})
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        String response = result.andReturn().getResponse().getContentAsString();
        result.andDo(resultPrint());
        result.andExpect(rm);
        return result;
    }

    protected Page createPage(String pageCode) {
        IPage parentPage = pageManager.getDraftPage("service");
        PageModel pageModel = this.pageModelManager.getPageModel(parentPage.getMetadata().getModelCode());
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel, true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = new ApsProperties();
        config.put("contentId", "ART11");
        config.put("temp", "tempValue");
        Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config);
        Widget[] widgets = new Widget[pageModel.getFrames().length];
        widgets[0] = widgetToAdd;
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage.getCode(), "free", pageModel, metadata, widgets);
        return pageToAdd;
    }

}
