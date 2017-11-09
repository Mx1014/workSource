	/**
	 * <p>${methodDesc}</p>
	 * <b>URL: ${controllerMapping}/${methodName}</b>
	 */
	@RequestMapping("${methodName}")
	@RestReturn(<#if hasResponse??>${methodName?cap_first}Response<#else>String</#if>.class)
	public RestResponse ${methodName}(<#if hasCommand??>${methodName?cap_first}Command cmd</#if>){
<#if hasResponse??>
		return new RestResponse(${serviceName?uncap_first}.${methodName}(<#if hasCommand??>cmd</#if>));
<#else>
		${serviceName?uncap_first}.${methodName}(<#if hasCommand??>cmd</#if>);
		return new RestResponse();
</#if>
	}