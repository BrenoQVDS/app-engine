INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,maingroup, readonlydefaultconfig) VALUES ('breadcrumb','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Breadcrumb</property>
<property key="it">Briciole di pane</property>
</properties>',NULL,NULL,NULL,NULL,1,'free',1);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('breadcrumb','breadcrumb',NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<@wp.currentPage param="code" var="currentViewCode" />

<div class="navbar-position">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">

            <span class="noscreen">
                <@wp.i18n key="ESNB_YOU_ARE_HERE" />
            </span>
            <#assign first=true>
            <@wp.nav spec="current.path" var="currentTarget">
            <#assign currentCode=currentTarget.code>

            <!--            <#if first>
                        <span class="divider">/</span>
                        </#if>-->

            <#if !currentTarget.voidPage>
            <#if (currentCode == currentViewCode)>
            <li class="breadcrumb-item active" aria-current="page">${currentTarget.title}</li>
            <#else>
            <li class="breadcrumb-item">
                <a href="${currentTarget.url}">${currentTarget.title}</a>
            </li>
            </#if>
            <#else>
            ${currentTarget.title}
            </#if>
            <#assign first=false>
            </@wp.nav>
        </ol>
    </nav>
</div>
',NULL,1);

INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_SIGNOUT','en','Logout');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_SIGNOUT','it','Esci');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_PROFILE_CONFIGURATION','en','Profile Configuration');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_PROFILE_CONFIGURATION','it','Configurazione Profilo');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_SIGNIN','en','Log in');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_SIGNIN','it','Entra');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_PASSWORD','en','Password');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_PASSWORD','it','Password');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_USERNAME','en','Username');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_USERNAME','it','Username');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_USER_STATUS_CREDENTIALS_INVALID','en','Invalid credentials');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_USER_STATUS_CREDENTIALS_INVALID','it','Invalid credentials');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_USER_STATUS_EXPIRED','en','User expired');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESLF_USER_STATUS_EXPIRED','it','User expired');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESNB_YOU_ARE_HERE','en','You''re here');
INSERT INTO localstrings (keycode,langcode,stringvalue) VALUES ('ESNB_YOU_ARE_HERE','it','Sei qui');




INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked, readonlydefaultconfig) VALUES ('logo','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Logo</property>
<property key="it">Logo</property>
</properties>',NULL,NULL,NULL,NULL,1,0);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('logo','logo',NULL,'<#assign wp=JspTaglibs["/aps-core"]>

<@wp.info key="systemParam" paramName="applicationBaseURL" var="appUrl" />
<img src="${appUrl}resources/static/img/Entando_light.svg" aria-label="Entando" alt="Logo" role="logo" />','<#assign wp=JspTaglibs["/aps-core"]>

<@wp.info key="systemParam" paramName="applicationBaseURL" var="appUrl" />
<img src="${appUrl}resources/static/img/Entando_light.svg" aria-label="Entando" alt="Entando" role="logo" />',1);


INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,readonlydefaultconfig) VALUES ('language','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Language</property>
<property key="it">Linguaggio</property>
</properties>',NULL,NULL,NULL,NULL,1,0);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('language','language',NULL,'<#assign wp=JspTaglibs["/aps-core"]>

<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />

<@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

<#assign langstrs = [] />
<#list langsVar as curLangVar>
  <#assign langurl><@wp.url lang="${curLangVar.code}" paramRepeat=true /></#assign>
  <#assign langstr = ["{\"code\": \"" + curLangVar.code + "\", \"descr\": \"" + curLangVar.descr + "\", \"url\": \""+ langurl +"\"}"]  />
  <#assign langstrs = langstrs + langstr />
</#list>
<#assign lang_json_string = langstrs?join(", ") />

<choose-language-widget
  currentLang="${currentLangVar}"
  languages="[${lang_json_string?html}]"
></choose-language-widget>','<#assign wp=JspTaglibs["/aps-core"]>

<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />

<@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

<#assign langstrs = [] />
<#list langsVar as curLangVar>
  <#assign langurl><@wp.url lang="${curLangVar.code}" paramRepeat=true /></#assign>
  <#assign langstr = ["{\"code\": \"" + curLangVar.code + "\", \"descr\": \"" + curLangVar.descr + "\", \"url\": \""+ langurl +"\"}"]  />
  <#assign langstrs = langstrs + langstr />
</#list>
<#assign lang_json_string = langstrs?join(", ") />

<choose-language-widget
  currentLang="${currentLangVar}"
  languages="[${lang_json_string?html}]"
></choose-language-widget>',1);

INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,readonlydefaultconfig) VALUES ('keycloak-login','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Login</property>
<property key="it">Login</property>
</properties>',NULL,NULL,NULL,NULL,1,0);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('keycloak-login','keycloak-login',NULL,'<#assign wp=JspTaglibs["/aps-core"]>

<@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

<#assign sessionUser = "" />
<#assign userDisplayName = "" />
<#if (Session.currentUser.username != "guest") >
   <#assign sessionUser = Session.currentUser.username />
   <#if (Session.currentUser.profile??) && (Session.currentUser.profile.displayName??)>
      <#assign userDisplayName = Session.currentUser.profile.displayName />
   <#else>
      <#assign userDisplayName = Session.currentUser />
   </#if>
</#if>

<login-button-widget
   app-url="<@wp.info key="systemParam" paramName="applicationBaseURL" />"
   session-user="${sessionUser}"
   user-display-name="${userDisplayName}"
   page="<@wp.url />"
></login-button-widget>','<#assign wp=JspTaglibs["/aps-core"]>

<@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

<#assign sessionUser = "" />
<#assign userDisplayName = "" />
<#if (Session.currentUser.username != "guest") >
   <#assign sessionUser = Session.currentUser.username />
   <#if (Session.currentUser.profile??) && (Session.currentUser.profile.displayName??)>
      <#assign userDisplayName = Session.currentUser.profile.displayName />
   <#else>
      <#assign userDisplayName = Session.currentUser />
   </#if>
</#if>

<login-button-widget
   app-url="<@wp.info key="systemParam" paramName="applicationBaseURL" />"
   session-user="${sessionUser}"
   user-display-name="${userDisplayName}"
   page="<@wp.url />"
></login-button-widget>',1);

INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked, readonlydefaultconfig) VALUES ('navigation-menu','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Navigation Menu</property>
<property key="it">Menu di Navigazione</property>
</properties>','<config>
	<parameter name="navSpec">Rules for the Page List auto-generation</parameter>
	<action name="navigatorConfig" />
</config>',NULL,NULL,NULL,1,0);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('navigation-menu','navigation-menu',NULL,'<#assign wp=JspTaglibs["/aps-core"]>

<@wp.currentPage param="code" var="currentPageCode" />

<@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

<#assign navstrs = [] />
<@wp.nav var="page">
  <#assign navstr = ["{\"code\": \"" + page.code + "\", \"title\": \"" + page.title + "\", \"level\": \"" + page.level + "\", \"url\": \""+ page.url +"\", \"voidPage\": " + page.voidPage?string("true", "false") + "}"]  />
  <#assign navstrs = navstrs + navstr />
</@wp.nav>
<#assign nav_json_string = navstrs?join(", ") />

<navigation-bar-widget
  current-page="${currentPageCode}"
  nav-items="[${nav_json_string?html}]"
></navigation-bar-widget>','<#assign wp=JspTaglibs["/aps-core"]>

<@wp.currentPage param="code" var="currentPageCode" />

<@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

<#assign navstrs = [] />
<@wp.nav var="page">
  <#assign navstr = ["{\"code\": \"" + page.code + "\", \"title\": \"" + page.title + "\", \"level\": \"" + page.level + "\", \"url\": \""+ page.url +"\", \"voidPage\": " + page.voidPage?string("true", "false") + "}"]  />
  <#assign navstrs = navstrs + navstr />
</@wp.nav>
<#assign nav_json_string = navstrs?join(", ") />

<navigation-bar-widget
  current-page="${currentPageCode}"
  nav-items="[${nav_json_string?html}]"
></navigation-bar-widget>',1);

INSERT INTO widgetcatalog (code,titles,parameters,plugincode,parenttypecode,defaultconfig,locked,maingroup,configui,readonlydefaultconfig) VALUES ('sitemap','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Sitemap</property>
<property key="it">Mappa del Sito</property>
</properties>',NULL,NULL,NULL,NULL,1,'free',NULL,0);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('sitemap','sitemap',NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<@wp.currentPage param="code" var="currentPageCode" />
<@wp.freemarkerTemplateParameter var="currentPageCode" valueName="currentPageCode" />
<link rel="stylesheet" type="text/css" href="<@wp.resourceURL />ootb-widgets/static/css/sitemap.css">

<div class="well well-small sitemap">
<h2>Site Map</h2>

<ul class="nav nav-list">
<@wp.nav spec="code(homepage).subtree(50)" var="page">
   <#if (previousPage?? && previousPage.code??)>
	<#assign previousLevel=previousPage.level>
	<#assign level=page.level>
	<@wp.freemarkerTemplateParameter var="previousLevel" valueName="previousLevel" />
	<@wp.freemarkerTemplateParameter var="level" valueName="level" />
	<@wp.fragment code="sitemap_menu_include" escapeXml=false />
   </#if>
   <@wp.freemarkerTemplateParameter var="previousPage" valueName="page" />
</@wp.nav>
<#if (previousPage??)>
   <#assign previousLevel = previousPage.level>
   <#assign level=0>
   <@wp.freemarkerTemplateParameter var="previousLevel" valueName="previousLevel" />
   <@wp.freemarkerTemplateParameter var="level" valueName="level" />
   <@wp.fragment code="sitemap_menu_include" escapeXml=false />
   <#if (previousLevel != 0)>
	<#list 0..(previousLevel - 1) as ignoreMe>
		</ul></li>
	</#list>
   </#if>
</#if>
</ul>
</div>
<@wp.freemarkerTemplateParameter var="previousPage" valueName="" removeOnEndTag=true />
',1);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('sitemap_menu_include',NULL,NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<#assign liClass="">
<#assign aClass="">
<#if (previousPage.code == currentPageCode)>
  <#assign liClass='' class="active" ''>
</#if>
<#if previousPage.voidPage>
 <#assign liClass='' class="nav-header" ''>
    <#assign aClass='' class="a-void" ''>
</#if>
<#if previousLevel == 0>
  <#assign aClass='' class="root-a" ''>
</#if>
<li ${liClass}>
   <#if (!previousPage.voidPage)>
   	<a href="${previousPage.url}"  ${aClass}>
   <#else>
        <a ${aClass}>
   </#if>
   <!-- [ ${previousLevel} ]-->
<#--  CHECK THIS
${homeIcon}
-->
   ${previousPage.title}
     </a>

   <#if (previousLevel == level)>
	</li>
   </#if>
   <#if (previousLevel < level)>
	<ul class="nav-list">
   </#if>
   <#if (previousLevel > level)>
	<#list 1..(previousLevel - level) as ignoreMe>
		</li></ul>
	</#list>
   </#if>
',1);

INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('entando_ootb_carbon_include',NULL,NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<script src="<@wp.resourceURL />ootb-widgets/static/js/2.ootb.chunk.js"></script>
<script src="<@wp.resourceURL />ootb-widgets/static/js/main.ootb.chunk.js"></script>
<script src="<@wp.resourceURL />ootb-widgets/static/js/runtime-main.ootb.js"></script>
<link href="<@wp.resourceURL />ootb-widgets/static/css/main.ootb.chunk.css" rel="stylesheet">
',1);

INSERT INTO pagemodels (code,descr,frames,plugincode,templategui) VALUES ('1-column','1 Column','<?xml version="1.0" encoding="UTF-8"?>
<frames>
  <frame pos="0">
      <descr>Logo</descr>
      <sketch x1="0" y1="0" x2="2" y2="0" />
      <defaultWidget code="logo" />
  </frame>
  <frame pos="1">
      <descr>Navigation bar</descr>
      <sketch x1="3" y1="0" x2="5" y2="0" />
      <defaultWidget code="navigation-menu" />
  </frame>
  <frame pos="2">
      <descr>Search</descr>
      <sketch x1="6" y1="0" x2="8" y2="0" />
      <defaultWidget code="search_form" />
  </frame>
  <frame pos="3">
      <descr>Login</descr>
      <sketch x1="9" y1="0" x2="11" y2="0" />
      <defaultWidget code="keycloak-login" />
  </frame>
  <frame pos="4">
      <descr>Frame 1</descr>
      <sketch x1="0" y1="1" x2="11" y2="1" />
  </frame>
  <frame pos="5">
      <descr>Frame 2</descr>
      <sketch x1="0" y1="2" x2="11" y2="2" />
  </frame>
  <frame pos="6">
      <descr>Frame 3</descr>
      <sketch x1="0" y1="3" x2="11" y2="3" />
  </frame>
  <frame pos="7">
      <descr>Frame 4</descr>
      <sketch x1="0" y1="4" x2="11" y2="4" />
  </frame>
  <frame pos="8">
      <descr>Footer</descr>
      <sketch x1="0" y1="5" x2="11" y2="5" />
  </frame>
</frames>

',NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="systemParam" paramName="applicationBaseURL" var="appUrl" />

<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>
            <@wp.currentPage param="title" />
        </title>
        <meta name="viewport" content="width=device-width,  user-scalable=no" />
        <link rel="icon" href="${appUrl}favicon.png" type="image/png" />
        <!-- Custom OOTB page template styles -->
        <link rel="stylesheet" href="<@wp.resourceURL />static/css/ootb/page-templates/index.css" rel="stylesheet">
        <!-- Carbon Design System -->
        <@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

        <@wp.outputHeadInfo type="CSS">
            <link rel="stylesheet" type="text/css" href="<@wp.cssURL /><@wp.printHeadInfo />" />
        </@wp.outputHeadInfo>
        </head>
        <body>
          <header-fragment app-url="${appUrl}">
            <template>
                <@wp.show frame=0 />
                <@wp.show frame=1 />
                <@wp.show frame=2 />
                <@wp.show frame=3 />
            </template>
          </header-fragment>
          <div class="bx--grid Homepage__body">
            <div class="bx--row"><@wp.show frame=4 /></div>
            <div class="bx--row"><@wp.show frame=5 /></div>
            <div class="bx--row"><@wp.show frame=6 /></div>
            <div class="bx--row"><@wp.show frame=7 /></div>
          </div>
          <div class="Homepage__footer"><@wp.show frame=8 /></div>
        </body>
</html>
');

INSERT INTO pagemodels (code,descr,frames,plugincode,templategui) VALUES ('1-2x4-1-column','1-2x4-1 Columns','<?xml version="1.0" encoding="UTF-8"?>
<frames>
  <frame pos="0">
      <descr>Logo</descr>
      <sketch x1="0" y1="0" x2="2" y2="0" />
      <defaultWidget code="logo" />
  </frame>
  <frame pos="1">
      <descr>Navigation Menu</descr>
      <sketch x1="3" y1="0" x2="5" y2="0" />
      <defaultWidget code="navigation-menu" />
  </frame>
  <frame pos="2">
      <descr>Search</descr>
      <sketch x1="6" y1="0" x2="8" y2="0" />
      <defaultWidget code="search_form" />
  </frame>
  <frame pos="3">
      <descr>Login</descr>
      <sketch x1="9" y1="0" x2="11" y2="0" />
      <defaultWidget code="keycloak-login" />
  </frame>
  <frame pos="4">
      <descr>Frame 1</descr>
      <sketch x1="0" y1="1" x2="5" y2="1" />
  </frame>
  <frame pos="5">
      <descr>Frame 2</descr>
      <sketch x1="6" y1="1" x2="11" y2="1" />
  </frame>
  <frame pos="6">
      <descr>Frame 3</descr>
      <sketch x1="0" y1="2" x2="5" y2="2" />
  </frame>
  <frame pos="7">
      <descr>Frame 4</descr>
      <sketch x1="6" y1="2" x2="11" y2="2" />
  </frame>
  <frame pos="8">
      <descr>Frame 5</descr>
      <sketch x1="0" y1="3" x2="5" y2="3" />
  </frame>
  <frame pos="9">
      <descr>Frame 6</descr>
      <sketch x1="6" y1="3" x2="11" y2="3" />
  </frame>
  <frame pos="10">
      <descr>Frame 7</descr>
      <sketch x1="0" y1="4" x2="5" y2="4" />
  </frame>
  <frame pos="11">
      <descr>Frame 8</descr>
      <sketch x1="6" y1="4" x2="11" y2="4" />
  </frame>
  <frame pos="12">
      <descr>Footer</descr>
      <sketch x1="0" y1="5" x2="11" y2="5" />
  </frame>
</frames>

',NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="systemParam" paramName="applicationBaseURL" var="appUrl" />

<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>
            <@wp.currentPage param="title" />
        </title>
        <meta name="viewport" content="width=device-width,  user-scalable=no" />
        <link rel="icon" href="${appUrl}favicon.png" type="image/png" />
        <!-- Custom OOTB page template styles -->
        <link rel="stylesheet" href="<@wp.resourceURL />static/css/ootb/page-templates/index.css" rel="stylesheet">
        <!-- Carbon Design System -->
        <@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

        <@wp.outputHeadInfo type="CSS">
            <link rel="stylesheet" type="text/css" href="<@wp.cssURL /><@wp.printHeadInfo />" />
        </@wp.outputHeadInfo>
        </head>
        <body>
          <header-fragment app-url="${appUrl}">
            <template>
                <@wp.show frame=0 />
                <@wp.show frame=1 />
                <@wp.show frame=2 />
                <@wp.show frame=3 />
            </template>
          </header-fragment>
          <div class="bx--grid Homepage__body">
            <div class="bx--row">
              <div class="bx--col-sm-4 bx--col-md-4 bx--no-gutter">
                  <div><@wp.show frame=4 /></div>
                  <div><@wp.show frame=6 /></div>
                  <div><@wp.show frame=8 /></div>
                  <div><@wp.show frame=10 /></div>
              </div>
              <div class="bx--col-sm-4 bx--col-md-4 bx--no-gutter">
                  <div><@wp.show frame=5 /></div>
                  <div><@wp.show frame=7 /></div>
                  <div><@wp.show frame=9 /></div>
                  <div><@wp.show frame=11 /></div>
                </div>
            </div>
          </div>
          <div class="Homepage__footer"><@wp.show frame=12 /></div>
        </body>
</html>
');

INSERT INTO pagemodels (code,descr,frames,plugincode,templategui) VALUES ('1-2-column','1-2 Columns','<?xml version="1.0" encoding="UTF-8"?>
<frames>
  <frame pos="0">
      <descr>Logo</descr>
      <sketch x1="0" y1="0" x2="2" y2="0" />
      <defaultWidget code="logo" />
  </frame>
  <frame pos="1">
      <descr>Navigation Menu</descr>
      <sketch x1="3" y1="0" x2="5" y2="0" />
      <defaultWidget code="navigation-menu" />
  </frame>
  <frame pos="2">
      <descr>Search</descr>
      <sketch x1="6" y1="0" x2="8" y2="0" />
      <defaultWidget code="search_form" />
  </frame>
  <frame pos="3">
      <descr>Login</descr>
      <sketch x1="9" y1="0" x2="11" y2="0" />
      <defaultWidget code="keycloak-login" />
  </frame>
  <frame pos="4">
      <descr>Frame 1</descr>
      <sketch x1="0" y1="1" x2="5" y2="1" />
  </frame>
  <frame pos="5">
      <descr>Frame 2</descr>
      <sketch x1="6" y1="1" x2="11" y2="1" />
  </frame>
  <frame pos="6">
      <descr>Frame 3</descr>
      <sketch x1="0" y1="2" x2="5" y2="2" />
  </frame>
  <frame pos="7">
      <descr>Frame 4</descr>
      <sketch x1="6" y1="2" x2="11" y2="2" />
  </frame>
  <frame pos="8">
      <descr>Frame 5</descr>
      <sketch x1="0" y1="3" x2="5" y2="3" />
  </frame>
  <frame pos="9">
      <descr>Frame 6</descr>
      <sketch x1="6" y1="3" x2="11" y2="3" />
  </frame>
  <frame pos="10">
      <descr>Frame 7</descr>
      <sketch x1="0" y1="4" x2="5" y2="4" />
  </frame>
  <frame pos="11">
      <descr>Frame 8</descr>
      <sketch x1="6" y1="4" x2="11" y2="4" />
  </frame>
  <frame pos="12">
      <descr>Frame 9</descr>
      <sketch x1="0" y1="5" x2="5" y2="5" />
  </frame>
  <frame pos="13">
      <descr>Frame 10</descr>
      <sketch x1="6" y1="5" x2="11" y2="5" />
  </frame>
</frames>

',NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="systemParam" paramName="applicationBaseURL" var="appUrl" />

<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>
            <@wp.currentPage param="title" />
        </title>
        <meta name="viewport" content="width=device-width,  user-scalable=no" />
        <link rel="icon" href="${appUrl}favicon.png" type="image/png" />
        <!-- Custom OOTB page template styles -->
        <link rel="stylesheet" href="<@wp.resourceURL />static/css/ootb/page-templates/index.css" rel="stylesheet">
        <!-- Carbon Design System -->
        <@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

        <@wp.outputHeadInfo type="CSS">
            <link rel="stylesheet" type="text/css" href="<@wp.cssURL /><@wp.printHeadInfo />" />
        </@wp.outputHeadInfo>
        </head>
        <body>
          <header-fragment app-url="${appUrl}">
            <template>
                <@wp.show frame=0 />
                <@wp.show frame=1 />
                <@wp.show frame=2 />
                <@wp.show frame=3 />
            </template>
          </header-fragment>
          <div class="bx--grid Homepage__body">
            <div class="bx--row">
              <div class="bx--col-sm-4 bx--col-md-4 bx--no-gutter">
                  <div><@wp.show frame=4 /></div>
                  <div><@wp.show frame=6 /></div>
                  <div><@wp.show frame=8 /></div>
                  <div><@wp.show frame=10 /></div>
                  <div><@wp.show frame=12/></div>
              </div>
              <div class="bx--col-sm-4 bx--col-md-4 bx--no-gutter">
                  <div><@wp.show frame=5 /></div>
                  <div><@wp.show frame=7 /></div>
                  <div><@wp.show frame=9 /></div>
                  <div><@wp.show frame=11 /></div>
                  <div><@wp.show frame=13 /></div>
                </div>
            </div>
          </div>
        </body>
</html>
');

INSERT INTO pagemodels (code,descr,frames,plugincode,templategui) VALUES ('1-2x2-1-column','1-2x2-1 Columns','<?xml version="1.0" encoding="UTF-8"?>
<frames>
  <frame pos="0">
      <descr>Logo</descr>
      <sketch x1="0" y1="0" x2="2" y2="0" />
      <defaultWidget code="logo" />
  </frame>
  <frame pos="1">
      <descr>Navigation Menu</descr>
      <sketch x1="3" y1="0" x2="5" y2="0" />
      <defaultWidget code="navigation-menu" />
  </frame>
  <frame pos="2">
      <descr>Search</descr>
      <sketch x1="6" y1="0" x2="8" y2="0" />
      <defaultWidget code="search_form" />
  </frame>
  <frame pos="3">
      <descr>Login</descr>
      <sketch x1="9" y1="0" x2="11" y2="0" />
      <defaultWidget code="keycloak-login" />
  </frame>
  <frame pos="4">
      <descr>Frame 1</descr>
      <sketch x1="0" y1="1" x2="5" y2="2" />
  </frame>
  <frame pos="5">
      <descr>Frame 2</descr>
      <sketch x1="6" y1="1" x2="11" y2="1" />
  </frame>
  <frame pos="6">
      <descr>Frame 3</descr>
      <sketch x1="6" y1="2" x2="11" y2="2" />
  </frame>
  <frame pos="7">
      <descr>Frame 4</descr>
      <sketch x1="0" y1="3" x2="5" y2="4" />
  </frame>
  <frame pos="8">
      <descr>Frame 5</descr>
      <sketch x1="6" y1="3" x2="11" y2="3" />
  </frame>
  <frame pos="9">
      <descr>Frame 6</descr>
      <sketch x1="6" y1="4" x2="11" y2="4" />
  </frame>
  <frame pos="10">
      <descr>Footer</descr>
      <sketch x1="0" y1="5" x2="11" y2="5" />
  </frame>
</frames>

',NULL,'<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="systemParam" paramName="applicationBaseURL" var="appUrl" />

<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>
            <@wp.currentPage param="title" />
        </title>
        <meta name="viewport" content="width=device-width,  user-scalable=no" />
        <link rel="icon" href="${appUrl}favicon.png" type="image/png" />
        <!-- Custom OOTB page template styles -->
        <link rel="stylesheet" href="<@wp.resourceURL />static/css/ootb/page-templates/index.css" rel="stylesheet">
        <!-- Carbon Design System -->
        <@wp.fragment code="entando_ootb_carbon_include" escapeXml=false />

        <@wp.outputHeadInfo type="CSS">
            <link rel="stylesheet" type="text/css" href="<@wp.cssURL /><@wp.printHeadInfo />" />
        </@wp.outputHeadInfo>
        </head>
        <body>
          <header-fragment app-url="${appUrl}">
            <template>
                <@wp.show frame=0 />
                <@wp.show frame=1 />
                <@wp.show frame=2 />
                <@wp.show frame=3 />
            </template>
          </header-fragment>
          <div class="bx--grid Homepage__body">
            <div class="bx--row">
              <div class="bx--col-sm-4 bx--col-md-4 bx--no-gutter">
                  <div><@wp.show frame=4 /></div>
                  <div><@wp.show frame=7 /></div>
              </div>
              <div class="bx--col-sm-4 bx--col-md-4 bx--no-gutter">
                  <div><@wp.show frame=5 /></div>
                  <div><@wp.show frame=6 /></div>
                  <div><@wp.show frame=8 /></div>
                  <div><@wp.show frame=9 /></div>
                </div>
            </div>
          </div>
          <div class="Homepage__footer"><@wp.show frame=10 /></div>
        </body>
</html>
');

INSERT INTO pages (code,parentcode,pos) VALUES ('homepage_test','homepage', 3);
INSERT INTO pages (code,parentcode,pos) VALUES ('sitemap','homepage_test',1);
INSERT INTO pages (code,parentcode,pos) VALUES ('search_result','homepage',4);
INSERT INTO pages_metadata_online (code,groupcode,titles,modelcode,showinmenu,extraconfig,updatedat) VALUES ('homepage_test','free','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Sample Homepage</property>
<property key="it">Sample Homepage</property>
</properties>','1-column',1,'<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf8</charset>
  <mimeType>text/html</mimeType>
</config>','2020-07-31 10:22:15');

INSERT INTO pages_metadata_online (code,groupcode,titles,modelcode,showinmenu,extraconfig,updatedat) VALUES ('sitemap','free','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Sitemap</property>
<property key="it">Sitemap</property>
</properties>','1-column',1,'<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
</config>','2020-06-08 08:43:13');
INSERT INTO pages_metadata_online (code,groupcode,titles,modelcode,showinmenu,extraconfig,updatedat) VALUES ('search_result','free','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search Result</property>
<property key="it">Risultati della Ricerca</property>
</properties>','1-column',1,'<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
</config>','2020-06-08 08:43:13');
INSERT INTO pages_metadata_draft (code,groupcode,titles,modelcode,showinmenu,extraconfig,updatedat) VALUES ('homepage_test','free','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Sample Homepage</property>
<property key="it">Sample Homepage</property>
</properties>','1-column',1,'<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf8</charset>
  <mimeType>text/html</mimeType>
</config>','2020-07-31 10:22:15');
INSERT INTO pages_metadata_draft (code,groupcode,titles,modelcode,showinmenu,extraconfig,updatedat) VALUES ('sitemap','free','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Sitemap</property>
<property key="it">Sitemap</property>
</properties>','1-column',1,'<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
</config>','2020-06-08 08:43:13');
INSERT INTO pages_metadata_draft (code,groupcode,titles,modelcode,showinmenu,extraconfig,updatedat) VALUES ('search_result','free','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Search Result</property>
<property key="it">Risultati della Ricerca</property>
</properties>','1-column',1,'<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
</config>','2020-06-08 08:43:13');
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',0,'logo',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',1,'navigation-menu','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(4) + parent + current</property>
</properties>');
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',2,'search_form',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',3,'keycloak-login',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',4,'row_content_viewer_list','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="ownerGroup">free</property>
<property key="joinGroups">()</property>
<property key="contents">[{modelId=10021,contentDescription=Sample - News 1,contentId=NWS3},{modelId=10003,contentDescription=Sample Banner,contentId=BNR2}]</property>
</properties>');

INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('sitemap',0,'logo',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('sitemap',1,'navigation-menu','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(4) + parent + current</property>
</properties>');
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('sitemap',2,'search_form',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('sitemap',3,'keycloak-login',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('sitemap',4,'sitemap',NULL);

INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('search_result',0,'logo',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('search_result',1,'navigation-menu','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(4) + parent + current</property>
</properties>');
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('search_result',2,'search_form',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('search_result',3,'keycloak-login',NULL);
INSERT INTO widgetconfig_draft (pagecode,framepos,widgetcode,config) VALUES ('search_result',4,'search_result',NULL);

INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',0,'logo',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',1,'navigation-menu','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(4) + parent + current</property>
</properties>');
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',2,'search_form',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',3,'keycloak-login',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('homepage_test',4,'row_content_viewer_list','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="ownerGroup">free</property>
<property key="joinGroups">()</property>
<property key="contents">[{modelId=10021,contentDescription=Sample - News 1,contentId=NWS3},{modelId=10003,contentDescription=Sample Banner,contentId=BNR2}]</property>
</properties>');

INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('sitemap',0,'logo',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('sitemap',1,'navigation-menu','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(4) + parent + current</property>
</properties>');
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('sitemap',2,'search_form',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('sitemap',3,'keycloak-login',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('sitemap',4,'sitemap',NULL);

INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('search_result',0,'logo',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('search_result',1,'navigation-menu','<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="navSpec">code(homepage).subtree(4) + parent + current</property>
</properties>');
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('search_result',2,'search_form',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('search_result',3,'keycloak-login',NULL);
INSERT INTO widgetconfig (pagecode,framepos,widgetcode,config) VALUES ('search_result',4,'search_result',NULL);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('CNG_ATTACHMENTS', 'en', 'Attachments');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('CNG_ATTACHMENTS', 'it', 'Allegati');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('CNG_LINKS', 'en', 'Links');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('CNG_LINKS', 'it', 'Link');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('CNG_READ_MORE', 'en', 'View details');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('CNG_READ_MORE', 'it', 'Continua');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_ATTACHMENTS', 'en', 'Attachments');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_ATTACHMENTS', 'it', 'Allegati');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_LINKS', 'en', 'Links');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_LINKS', 'it', 'Link');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_READ_MORE', 'en', 'View details');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('NWS_READ_MORE', 'it', 'Continua');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup, readonlydefaultconfig) VALUES ('NWS_Latest', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">News Latest</property>
<property key="it">Notizie Ultime</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElements">4</property>
<property key="filters">(order=DESC;attributeFilter=true;key=Date)</property>
<property key="title_it">Notizie</property>
<property key="linkDescr_it">Archivio</property>
<property key="pageLink">news</property>
<property key="title_en">News</property>
<property key="contentType">NWS</property>
<property key="modelId">10021</property>
<property key="linkDescr_en">Archive</property>
</properties>', 0, NULL, 0);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup, readonlydefaultconfig) VALUES ('NWS_Archive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">News Archive</property>
<property key="it">Notizie Archivio</property>
</properties>', NULL, NULL, 'content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElemForItem">10</property>
<property key="title_it">Archivio Notizie</property>
<property key="userFilters">(attributeFilter=false;key=fulltext)+(attributeFilter=true;key=Date)</property>
<property key="filters">(order=DESC;attributeFilter=true;key=Date)</property>
<property key="title_en">News Archive</property>
<property key="contentType">NWS</property>
<property key="modelId">10021</property>
</properties>', 0, NULL, 0);

INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('keycloak_auth_with_redirect',NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]><script>  (function () {    const consolePrefix = ''[ENTANDO-KEYCLOAK]'';    const keycloakConfigEndpoint = ''<@wp.info key="systemParam" paramName="applicationBaseURL" />keycloak.json'';    let keycloakConfig;    function dispatchKeycloakEvent(eventType) {      console.info(consolePrefix, ''Dispatching'', eventType, ''custom event'');      return window.dispatchEvent(new CustomEvent(''keycloak'', { detail: { eventType } }));    }    function initKeycloak() {      const keycloak = new Keycloak(keycloakConfig);      keycloak.onReady = function() {        dispatchKeycloakEvent(''onReady'');      }      keycloak.onAuthSuccess = function() {        dispatchKeycloakEvent(''onAuthSuccess'');      }      keycloak.onAuthError = function() {        dispatchKeycloakEvent(''onAuthError'');      }      keycloak.onAuthRefreshSuccess = function() {        dispatchKeycloakEvent(''onAuthRefreshSuccess'');      }      keycloak.onAuthRefreshError = function() {        dispatchKeycloakEvent(''onAuthRefreshError'');      }            keycloak.onAuthLogout = function() {        dispatchKeycloakEvent(''onAuthLogout'');      }            keycloak.onTokenExpired = function() {        dispatchKeycloakEvent(''onTokenExpired'');      }               window.entando = {        ...(window.entando || {}),        keycloak,      };      window.entando.keycloak        .init({ onLoad: ''login-required'', promiseType: ''native'', enableLogging: true })        .catch(function (e) {          console.error(e);          console.error(consolePrefix, ''Failed to initialize Keycloak'');        });    }    function onKeycloakScriptError(e) {      console.error(e);      console.error(consolePrefix, ''Failed to load keycloak.js script'');    }    function addKeycloakScript(keycloakConfig) {      const script = document.createElement(''script'');      script.src = keycloakConfig[''auth-server-url''] + ''/js/keycloak.js'';      script.async = true;      script.addEventListener(''load'', initKeycloak);      script.addEventListener(''error'', onKeycloakScriptError);      document.body.appendChild(script);          }    fetch(keycloakConfigEndpoint)      .then(function (response) {        return response.json();      })      .then(function (config) {        keycloakConfig = config;        if (!keycloakConfig.clientId) {          keycloakConfig.clientId = keycloakConfig.resource;        }        addKeycloakScript(keycloakConfig);      })      .catch(function (e) {        console.error(e);        console.error(consolePrefix, ''Failed to fetch Keycloak configuration'');      });  })();</script>',NULL,0);
INSERT INTO guifragment (code,widgettypecode,plugincode,gui,defaultgui,locked) VALUES ('keycloak_auth',NULL,NULL,'<#assign wp=JspTaglibs["/aps-core"]><script>  (function () {    const consolePrefix = ''[ENTANDO-KEYCLOAK]'';    const keycloakConfigEndpoint = ''<@wp.info key="systemParam" paramName="applicationBaseURL" />keycloak.json'';    let keycloakConfig;    function dispatchKeycloakEvent(eventType) {      console.info(consolePrefix, ''Dispatching'', eventType, ''custom event'');      return window.dispatchEvent(new CustomEvent(''keycloak'', { detail: { eventType } }));    }    function initKeycloak() {      const keycloak = new Keycloak(keycloakConfig);      keycloak.onReady = function() {        dispatchKeycloakEvent(''onReady'');      }      keycloak.onAuthSuccess = function() {        dispatchKeycloakEvent(''onAuthSuccess'');      }      keycloak.onAuthError = function() {        dispatchKeycloakEvent(''onAuthError'');      }      keycloak.onAuthRefreshSuccess = function() {        dispatchKeycloakEvent(''onAuthRefreshSuccess'');      }      keycloak.onAuthRefreshError = function() {        dispatchKeycloakEvent(''onAuthRefreshError'');      }            keycloak.onAuthLogout = function() {        dispatchKeycloakEvent(''onAuthLogout'');      }            keycloak.onTokenExpired = function() {        dispatchKeycloakEvent(''onTokenExpired'');      }               function onKeycloakInitialized(isAuthenticated) {        if (isAuthenticated) {          console.info(consolePrefix, ''Keycloak initialized, user authenticated'');        } else {          console.info(consolePrefix, ''Keycloak initialized, user not authenticated'');        }      };      window.entando = {        ...(window.entando || {}),        keycloak,      };      window.entando.keycloak        .init({ onLoad: ''check-sso'', promiseType: ''native'', enableLogging: true })        .then(onKeycloakInitialized)        .catch(function (e) {          console.error(e);          console.error(consolePrefix, ''Failed to initialize Keycloak'');        });    }    function onKeycloakScriptError(e) {      console.error(e);      console.error(consolePrefix, ''Failed to load keycloak.js script'');    }    function addKeycloakScript(keycloakConfig) {      const script = document.createElement(''script'');      script.src = keycloakConfig[''auth-server-url''] + ''/js/keycloak.js'';      script.async = true;      script.addEventListener(''load'', initKeycloak);      script.addEventListener(''error'', onKeycloakScriptError);      document.body.appendChild(script);          }    fetch(keycloakConfigEndpoint)      .then(function (response) {        return response.json();      })      .then(function (config) {        keycloakConfig = config;        if (!keycloakConfig.clientId) {          keycloakConfig.clientId = keycloakConfig.resource;        }        addKeycloakScript(keycloakConfig);      })      .catch(function (e) {        console.error(e);        console.error(consolePrefix, ''Failed to fetch Keycloak configuration'');      });  })();</script>',NULL,0);
