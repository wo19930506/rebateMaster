<#macro page page>
<#assign pageCnd = "pageNo=" + "&pageSize=" + page.pageSize />
<#if location?contains("?")>
	<#assign location = location + "&" + pageCnd />
<#else>
	<#assign location = location + "?" + pageCnd />
</#if>
<ul class="page">
	<li>每页显示</li>
	<li><a style="width:23px;" id="page_btm" class="but_select" href="javascript:void(0)">20</a></li>
	<#if page.pageNo == 1><li>首页</li><#else><li><a href="${location?replace('pageNo=', 'pageNo=1')}">首页</a></li></#if>
	<#if page.hasPrePage><li><a href="${location?replace('pageNo=', 'pageNo=' + (page.pageNo?number - 1))}">上页</a></li><#else><li>上页</li></#if>
	<#if page.hasNextPage><li><a href="${location?replace('pageNo=', 'pageNo=' + (page.pageNo?number + 1))}">下页</a></li><#else><li>下页</li></#if>
	<#if page.totalPages == 0 || page.pageNo == page.totalPages><li>末页</li><#else><li><a href="${location?replace('pageNo=', 'pageNo=' + page.totalPages)}">末页</a></li></#if>
	<li>共${page.totalPages}页</li>
	<li>转到</li>
	<li><input type="text" name="pageNo" value="${page.pageNo}" /></li>
	<li class="go"><a href="javascript:void(0)">GO</a></li>
</ul>
<ul class="btn"></ul>
</#macro>