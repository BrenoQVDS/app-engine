/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.widgettype;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.ServletContext;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.services.assertionhelper.WidgetAssertionHelper;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.mockhelper.WidgetMockHelper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDto;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDtoBuilder;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.web.common.assembler.PagedMetadataMapper;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.FileSystemUtils;

import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;

@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {

    private static final String WIDGET_1_CODE = "widget1";
    private static final String WIDGET_2_CODE = "widget2";
    private static final String CUSTOM_ELEMENT_1 = "my-custom-element-1";
    private static final String CUSTOM_ELEMENT_2 = "my-custom-element-2";
    private static final List<String> RESOURCES_1 = Arrays.asList("/relative/path/to/script.js", "/relative/path/to/otherScript.js");
    private static final List<String> RESOURCES_2 = Arrays.asList("/relative/path/to/script2.js", "/relative/path/to/otherScript2.js");
    private static final String BUNDLE_1 = "bundle1";
    private static final String BUNDLE_2 = "bundle2";
    private static final String CUSTOM_ELEMENT_KEY = "customElement";
    private static final String RESOURCES_KEY = "resources";

    @Mock
    private IPageManager pageManager;

    @Mock
    private IComponentManager componentManager;

    @Mock
    private WidgetTypeManager widgetManager;

    @Mock
    private IGroupManager groupManager;

    @Mock
    private IGuiFragmentManager guiFragmentManager;
    
    @Mock
    private IPageModelManager pageModelManager;

    @Mock
    private PagedMetadataMapper pagedMetadataMapper;

    @Mock
    private ServletContext srvCtx;

    @InjectMocks
    private WidgetService widgetService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {

        Mockito.lenient().when(pageManager.getOnlineWidgetUtilizers(WIDGET_1_CODE)).thenReturn(ImmutableList.of(new Page()));
        Mockito.lenient().when(pageManager.getDraftWidgetUtilizers(WIDGET_1_CODE)).thenReturn(ImmutableList.of(new Page()));

        WidgetDtoBuilder dtoBuilder = new WidgetDtoBuilder();
        dtoBuilder.setPageManager(pageManager);
        dtoBuilder.setComponentManager(componentManager);
        dtoBuilder.setStockWidgetCodes("");
        widgetService.setDtoBuilder(dtoBuilder);

        Mockito.lenient().when(widgetManager.getWidgetTypes()).thenReturn(ImmutableList.of(getWidget1(), getWidget2()));
        PageModel pageModel = Mockito.mock(PageModel.class);
        Mockito.lenient().when(pageModel.getFrames()).thenReturn(new String[10]);
        Mockito.lenient().when(pageModelManager.getPageModel(Mockito.anyString())).thenReturn(pageModel);
    }

    @Test
    void shouldReturnAll() {
        PagedMetadata<WidgetDto> result = widgetService.getWidgets(new RestListRequest());

        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getBundleId()).isEqualTo(BUNDLE_1);
        assertThat(result.getBody().get(0).getConfigUi().get(CUSTOM_ELEMENT_KEY)).isEqualTo(CUSTOM_ELEMENT_1);
        assertThat(result.getBody().get(0).getConfigUi().get(RESOURCES_KEY)).isEqualTo(RESOURCES_1);
        assertThat(result.getBody().get(1).getBundleId()).isEqualTo(BUNDLE_2);
        assertThat(result.getBody().get(1).getConfigUi().get(CUSTOM_ELEMENT_KEY)).isEqualTo(CUSTOM_ELEMENT_2);
        assertThat(result.getBody().get(1).getConfigUi().get(RESOURCES_KEY)).isEqualTo(RESOURCES_2);
    }

    @Test
    void shouldFilterByCode() {

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("code");
        filter.setValue(WIDGET_1_CODE);
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    void shouldFilterByUsed() {
        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("used");
        filter.setValue("1");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    void shouldFilterByTypology() {

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("typology");
        filter.setValue("custom");
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    void shouldFilterByGroup() {

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("group");
        filter.setValue("group2");
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
    }

    @Test
    void shouldSortByCode() {
        RestListRequest requestList = new RestListRequest();

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_1_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_2_CODE);
    }

    @Test
    void shouldSortByUsed() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("used");

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    void shouldSortByTypology() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("typology");
        requestList.setDirection("DESC");

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    void shouldSortByGroup() {
        RestListRequest requestList = new RestListRequest();
        requestList.setSort("typology");
        requestList.setDirection("DESC");

        PagedMetadata<WidgetDto> result = widgetService.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody().get(0).getCode()).isEqualTo(WIDGET_2_CODE);
        assertThat(result.getBody().get(1).getCode()).isEqualTo(WIDGET_1_CODE);
    }

    @Test
    void shouldAddNewWidget() throws Exception {
        // Given
        String expectedCustomUi = "<#assign wp=JspTaglibs[ \"/aps-core\"]>\n"
                + "<script nonce=\"<@wp.cspNonce />\">my_js_script</script>";
        WidgetRequest widgetRequest = getWidgetRequest1();
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));

        // When
        widgetService.addWidget(widgetRequest);

        // Then
        ArgumentCaptor<WidgetType> argumentCaptor = ArgumentCaptor.forClass(WidgetType.class);
        verify(widgetManager).addWidgetType(argumentCaptor.capture());
        WidgetType argument = argumentCaptor.getValue();
        assertThat(argument.getCode()).isEqualTo(widgetRequest.getCode());
        assertThat(argument.getConfigUi()).isEqualTo(objectMapper.writeValueAsString(widgetRequest.getConfigUi()));
        assertThat(argument.getBundleId()).isEqualTo(widgetRequest.getBundleId());

        ArgumentCaptor<GuiFragment> fragmentCaptor = ArgumentCaptor.forClass(GuiFragment.class);
        verify(guiFragmentManager).addGuiFragment(fragmentCaptor.capture());

        assertThat(fragmentCaptor.getValue().getGui()).isEqualTo(expectedCustomUi);
    }

    @Test
    void shouldUpdateWidget() throws Exception {
        // Given
        String expectedCustomUi = "<#assign wp=JspTaglibs[ \"/aps-core\"]>\n"
                + "<script nonce=\"<@wp.cspNonce />\">my_js_script</script>";
        WidgetRequest widgetRequest = getWidgetUpdateRequest1();
        when(widgetManager.getWidgetType(eq(WIDGET_1_CODE))).thenReturn(getWidget1());
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));
        GuiFragment mockedFragment = mock(GuiFragment.class);
        when(mockedFragment.getGui()).thenReturn(expectedCustomUi);
        when(guiFragmentManager.getUniqueGuiFragmentByWidgetType(any())).thenReturn(mockedFragment);

        // When
        widgetService.updateWidget(WIDGET_1_CODE, widgetRequest);

        // Then
        ArgumentCaptor<String> configUiCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bundleIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(widgetManager).updateWidgetType(anyString(), any(), any(), anyString(), configUiCaptor.capture(),
                bundleIdCaptor.capture(), anyBoolean(), anyString(), anyString());
        assertThat(configUiCaptor.getValue()).isEqualTo(objectMapper.writeValueAsString(widgetRequest.getConfigUi()));
        assertThat(bundleIdCaptor.getValue()).isEqualTo(widgetRequest.getBundleId());

        ArgumentCaptor<GuiFragment> fragmentCaptor = ArgumentCaptor.forClass(GuiFragment.class);
        verify(guiFragmentManager).updateGuiFragment(fragmentCaptor.capture());

        assertThat(fragmentCaptor.getValue().getGui()).isEqualTo(expectedCustomUi);
    }

    @Test
    void shouldNotUpdateWidgetCustomUiNonce() throws Exception {
        // Given
        String expectedCustomUi = "<#assign wp=JspTaglibs[ \"/aps-core\"]>\n<script nonce=\"<@wp.cspNonce />\">my_js_script</script>";
        WidgetRequest widgetRequest = getWidgetUpdateRequest1();
        widgetRequest.setCustomUi(expectedCustomUi);
        when(widgetManager.getWidgetType(eq(WIDGET_1_CODE))).thenReturn(getWidget1());
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));
        GuiFragment mockedFragment = mock(GuiFragment.class);
        when(mockedFragment.getGui()).thenReturn(expectedCustomUi);
        when(guiFragmentManager.getUniqueGuiFragmentByWidgetType(any())).thenReturn(mockedFragment);

        // When
        widgetService.updateWidget(WIDGET_1_CODE, widgetRequest);

        // Then
        ArgumentCaptor<String> configUiCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bundleIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(widgetManager).updateWidgetType(anyString(), any(), any(), anyString(), configUiCaptor.capture(),
                bundleIdCaptor.capture(), anyBoolean(), anyString(), anyString());
        assertThat(configUiCaptor.getValue()).isEqualTo(objectMapper.writeValueAsString(widgetRequest.getConfigUi()));
        assertThat(bundleIdCaptor.getValue()).isEqualTo(widgetRequest.getBundleId());

        ArgumentCaptor<GuiFragment> fragmentCaptor = ArgumentCaptor.forClass(GuiFragment.class);
        verify(guiFragmentManager).updateGuiFragment(fragmentCaptor.capture());

        assertThat(fragmentCaptor.getValue().getGui()).isEqualTo(expectedCustomUi);
    }

    private WidgetType getWidget1() throws JsonProcessingException {
        WidgetType widgetType = new WidgetType();
        widgetType.setCode(WIDGET_1_CODE);
        widgetType.setMainGroup("group1");
        widgetType.setLocked(true);
        widgetType.setBundleId(BUNDLE_1);
        widgetType.setReadonlyPageWidgetConfig(true);
        widgetType.setConfigUi(objectMapper.writeValueAsString(
                ImmutableMap.of(CUSTOM_ELEMENT_KEY, CUSTOM_ELEMENT_1, RESOURCES_KEY, RESOURCES_1)));
        return widgetType;
    }

    private WidgetType getWidget2() throws JsonProcessingException {
        WidgetType widgetType = new WidgetType();
        widgetType.setCode(WIDGET_2_CODE);
        widgetType.setMainGroup("group2");
        widgetType.setParentType(getWidget1());
        widgetType.setBundleId(BUNDLE_2);
        widgetType.setReadonlyPageWidgetConfig(true);
        widgetType.setConfigUi(objectMapper.writeValueAsString(
                ImmutableMap.of(CUSTOM_ELEMENT_KEY, CUSTOM_ELEMENT_2, RESOURCES_KEY, RESOURCES_2)));
        return widgetType;
    }

    private WidgetRequest getWidgetRequest1() {
        WidgetRequest widgetRequest = new WidgetRequest();
        widgetRequest.setCode(WIDGET_1_CODE);
        widgetRequest.setTitles(ImmutableMap.of("it", "Mio Titolo", "en", "My Title"));
        widgetRequest.setCustomUi("<script>my_js_script</script>");
        widgetRequest.setGroup("group");
        widgetRequest.setReadonlyPageWidgetConfig(true);
        widgetRequest.setConfigUi(ImmutableMap.of(CUSTOM_ELEMENT_KEY, CUSTOM_ELEMENT_1, RESOURCES_KEY, RESOURCES_1));
        widgetRequest.setBundleId(BUNDLE_1);
        widgetRequest.setWidgetCategory("test");
        widgetRequest.setIcon("test");
        return widgetRequest;
    }

    private WidgetRequest getWidgetUpdateRequest1() {
        WidgetRequest widgetRequest = new WidgetRequest();
        widgetRequest.setTitles(ImmutableMap.of("it", "Mio Titolo", "en", "My Title"));
        widgetRequest.setCustomUi("<script>my_js_script</script>");
        widgetRequest.setGroup("group");
        widgetRequest.setReadonlyPageWidgetConfig(true);
        widgetRequest.setConfigUi(ImmutableMap.of(CUSTOM_ELEMENT_KEY, CUSTOM_ELEMENT_1, RESOURCES_KEY, RESOURCES_1));
        widgetRequest.setBundleId(BUNDLE_1);
        widgetRequest.setWidgetCategory("test");
        widgetRequest.setIcon("test");
        return widgetRequest;
    }

    @Test
    void getWidgetUsageForNonExistingCodeShouldReturnZero() {

        int componentUsage = widgetService.getComponentUsage("non_existing");
        assertEquals(0, componentUsage);
    }



    @Test
    void getWidgetUsageDetails() throws Exception {

        this.mockPagedMetadata(Arrays.asList(PageMockHelper.PAGE_CODE), 1, 1, 100, 2);

        PagedMetadata<ComponentUsageEntity> usageDetails = widgetService.getComponentUsageDetails(WidgetMockHelper.WIDGET_1_CODE, new PageSearchRequest(WidgetMockHelper.WIDGET_1_CODE));

        WidgetAssertionHelper.assertUsageDetails(usageDetails);
    }


    @Test
    void getWidgetUsageDetailsWithPagination() throws Exception {

        int pageSize = 3;

        // creates paged data
        List<Integer> pageNumList = Arrays.asList(1, 2);
        Map<Integer, List<ComponentUsageEntity>> usageEntityMap = new HashMap<>();
        usageEntityMap.put(pageNumList.get(0), Arrays.asList(
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_MISSION_CODE, IPageService.STATUS_ONLINE),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_CODE, IPageService.STATUS_ONLINE),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_MISSION_CODE, IPageService.STATUS_DRAFT)));
        usageEntityMap.put(pageNumList.get(1), Arrays.asList(
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, PageMockHelper.PAGE_CODE, IPageService.STATUS_DRAFT)));

        PageSearchRequest pageSearchRequest = new PageSearchRequest(WidgetMockHelper.WIDGET_1_CODE);
        pageSearchRequest.setPageSize(pageSize);

        // does assertions
        IntStream.range(0, pageNumList.size())
                .forEach(i -> {

                    mockPagedMetadata(Arrays.asList(PageMockHelper.PAGE_MISSION_CODE, PageMockHelper.PAGE_CODE), pageNumList.get(i), pageNumList.get(pageNumList.size()-1), pageSize, 4);

                    pageSearchRequest.setPage(pageNumList.get(i));

                    PagedMetadata<ComponentUsageEntity> pageUsageDetails = widgetService.getComponentUsageDetails(WIDGET_1_CODE, pageSearchRequest);

                    WidgetAssertionHelper.assertUsageDetails(pageUsageDetails,
                            usageEntityMap.get(i+1),
                            4,
                            pageNumList.get(i));
                });
    }


    @Test
    void getWidgetUsageDetailsWithInvalidCodeShouldThrowResourceNotFoundException() throws Exception {

        Page page1 = PageMockHelper.mockTestPage(PageMockHelper.PAGE_MISSION_CODE, WidgetMockHelper.WIDGET_1_CODE);
        Page page2 = PageMockHelper.mockTestPage(PageMockHelper.PAGE_CODE, WidgetMockHelper.WIDGET_1_CODE);

        List mockedPageList = Arrays.asList(page1, page2);

        mockPagedMetadata(Arrays.asList(PageMockHelper.PAGE_MISSION_CODE, PageMockHelper.PAGE_CODE), 1, 1, 100, 4);

        PageSearchRequest pageSearchRequest = new PageSearchRequest(PageMockHelper.PAGE_CODE);

        Arrays.stream(new String[]{ "not existing", null, ""})
                .forEach(code -> {
                    try {
                        widgetService.getComponentUsageDetails(code, pageSearchRequest);
                        fail("NoSuchElementException NOT thrown with code " + code);
                    } catch (Exception e) {
                        assertTrue(e instanceof NoSuchElementException || e instanceof NullPointerException);
                    }
                });
    }

    @Test
    void shouldNotThrowExceptionIfWidgetItsBroken() throws Exception {

        WidgetService service = new WidgetService();
        service.setWidgetManager(widgetManager);

        WidgetDtoBuilder dtoBuilder = new WidgetDtoBuilder();
        dtoBuilder.setPageManager(pageManager);
        dtoBuilder.setComponentManager(componentManager);
        dtoBuilder.setStockWidgetCodes("");
        service.setDtoBuilder(dtoBuilder);

        Mockito.lenient().when(pageManager.getOnlineWidgetUtilizers(WIDGET_1_CODE)).thenThrow(new EntException("Failure"));
        Mockito.lenient().when(pageManager.getDraftWidgetUtilizers(WIDGET_1_CODE)).thenThrow(new EntException("Failure"));
        Mockito.lenient().when(widgetManager.getWidgetTypes()).thenReturn(ImmutableList.of(getWidget1(), getWidget2()));

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("used");
        filter.setValue("1");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        requestList.addFilter(filter);

        PagedMetadata<WidgetDto> result = service.getWidgets(requestList);
        assertThat(result.getBody()).hasSize(0);
    }

    @Test
    void updateWidgetWithDefaultUiAndNoCustomUi() throws Exception {

        WidgetRequest widgetRequest = getWidgetUpdateRequest1();
        widgetRequest.setCustomUi(null);

        WidgetType type = getWidget1();
        type.setLocked(false);

        when(widgetManager.getWidgetType(WIDGET_1_CODE)).thenReturn(type);
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));
        GuiFragment mockedFragment = mock(GuiFragment.class);
        when(mockedFragment.getDefaultGui()).thenReturn("default-gui");
        when(guiFragmentManager.getUniqueGuiFragmentByWidgetType(any())).thenReturn(mockedFragment);

        widgetService.updateWidget(WIDGET_1_CODE, widgetRequest);

        verify(widgetManager).updateWidgetType(eq(WIDGET_1_CODE), any(), any(), anyString(), anyString(),
                anyString(), anyBoolean(), anyString(), anyString());
        verify(guiFragmentManager).updateGuiFragment(argThat(fragment -> fragment.getGui() == null));
    }

    @Test
    void updateWidgetWithExistingJspAndNoCustomUi() throws Exception {

        String jspRelativePath = WidgetType.getJspPath(WIDGET_1_CODE, null).substring(1);
        Path tmpDir = Files.createTempDirectory(null);
        Path jspPath = tmpDir.resolve(jspRelativePath);
        jspPath.getParent().toFile().mkdirs();
        jspPath.toFile().createNewFile();
        Mockito.when(srvCtx.getRealPath("/")).thenReturn(tmpDir.toAbsolutePath().toString());

        try {
            WidgetRequest widgetRequest = getWidgetUpdateRequest1();
            widgetRequest.setCustomUi(null);

            WidgetType type = getWidget1();
            type.setLocked(false);

            when(widgetManager.getWidgetType(WIDGET_1_CODE)).thenReturn(type);
            when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));

            widgetService.updateWidget(WIDGET_1_CODE, widgetRequest);

            verify(widgetManager).updateWidgetType(eq(WIDGET_1_CODE), any(), any(), anyString(), anyString(),
                    anyString(), anyBoolean(), anyString(), anyString());
            verify(guiFragmentManager, never()).updateGuiFragment(any());
        } finally {
            FileSystemUtils.deleteRecursively(tmpDir.toFile());
        }
    }

    @Test
    void updateWidgetWithoutDefaultUiAndCustomUi() throws Exception {

        WidgetRequest widgetRequest = getWidgetUpdateRequest1();
        widgetRequest.setCustomUi(null);

        WidgetType type = getWidget1();
        type.setLocked(false);

        when(widgetManager.getWidgetType(WIDGET_1_CODE)).thenReturn(type);
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));
        GuiFragment mockedFragment = mock(GuiFragment.class);
        when(guiFragmentManager.getUniqueGuiFragmentByWidgetType(any())).thenReturn(mockedFragment);

        ValidationGenericException ex = assertThrows(ValidationGenericException.class,
                () -> widgetService.updateWidget(WIDGET_1_CODE, widgetRequest));

        assertEquals(1, ex.getBindingResult().getAllErrors().size());
        assertEquals("widgettype.customUi.notBlank", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @Test
    void updateWidgetWithoutGuiFragmentAndCustomUi() throws Exception {

        WidgetRequest widgetRequest = getWidgetUpdateRequest1();
        widgetRequest.setCustomUi(null);

        WidgetType type = getWidget1();
        type.setLocked(false);

        when(widgetManager.getWidgetType(WIDGET_1_CODE)).thenReturn(type);
        when(groupManager.getGroup(widgetRequest.getGroup())).thenReturn(mock(Group.class));

        ValidationGenericException ex = assertThrows(ValidationGenericException.class,
                () -> widgetService.updateWidget(WIDGET_1_CODE, widgetRequest));

        assertEquals(1, ex.getBindingResult().getAllErrors().size());
        assertEquals("widgettype.customUi.notBlank", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * init mock for a multipaged request
     */
    private void mockPagedMetadata(List<String> utilizers, int currPage, int lastPage, int pageSize, int totalSize) {

        try {

            List<IPage> onlinePageList = utilizers.stream().map(u -> PageMockHelper.mockTestPage(u, WidgetMockHelper.WIDGET_1_CODE)).collect(Collectors.toList());
            List<IPage> draftPageList = utilizers.stream().map(u -> PageMockHelper.mockTestPage(u, WidgetMockHelper.WIDGET_1_CODE)).collect(Collectors.toList());

            when(pageManager.getOnlineWidgetUtilizers(anyString())).thenReturn(onlinePageList);
            when(pageManager.getDraftWidgetUtilizers(anyString())).thenReturn(draftPageList);
            when(widgetManager.getWidgetType(anyString())).thenReturn(WidgetMockHelper.mockWidgetType());

            RestListRequest restListRequest = new RestListRequest();
            restListRequest.setPageSize(pageSize);
            restListRequest.setPage(currPage);

            List<ComponentUsageEntity> componentUsageEntityList = onlinePageList.stream()
                    .map(child -> new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, child.getCode(), IPageService.STATUS_ONLINE))
                    .collect(Collectors.toList());
            componentUsageEntityList.addAll(draftPageList.stream()
                    .map(child -> new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, child.getCode(), IPageService.STATUS_DRAFT))
                    .collect(Collectors.toList()));

            PagedMetadata pagedMetadata = new PagedMetadata(restListRequest, componentUsageEntityList, totalSize);
            pagedMetadata.setPageSize(pageSize);
            pagedMetadata.setPage(currPage);
            pagedMetadata.imposeLimits();
            Mockito.lenient().when(pagedMetadataMapper.getPagedResult(any(), any())).thenReturn(pagedMetadata);
        } catch (Exception e) {
            Assertions.fail("Mock Exception");
        }
    }
}
