<#if isImpl??>	@Override</#if>
	public <#if hasResponse??>${method.name?cap_first}Response<#else>void</#if> ${method.name}(<#if hasCommand??>${method.name?cap_first}Command cmd</#if>)<#if isImpl??> {
	
<#if hasResponse??>		return new ${method.name?cap_first}Response();</#if>
	}<#else>;</#if>