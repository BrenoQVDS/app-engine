<#assign wp=JspTaglibs["/aps-core"]>

<#if ((userUnauthorized?? && userUnauthorized == true) || (RequestParameters.userUnauthorized?? && RequestParameters.userUnauthorized?lower_case?matches("true"))) >
<h1 class="alert-heading"><@wp.i18n key="USER_UNAUTHORIZED" escapeXml=false /></h1>
<#else>
<#assign currentPageCode><@wp.currentPage param="code" /></#assign>
<#if (currentPageCode == 'notfound')>
<div class="alert alert-error alert-block">
	<h1 class="alert-heading"><@wp.i18n key="PAGE_NOT_FOUND" escapeXml=false /></h1>
</div>
</#if>
<#if (currentPageCode == 'errorpage')>
<div class="alert alert-error alert-block">
	<h1 class="alert-heading"><@wp.i18n key="GENERIC_ERROR" escapeXml=false /></h1>
</div>
</#if>
</#if>