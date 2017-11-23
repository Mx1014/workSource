<#if entityName?ends_with("s")>
<#assign entityName=entityName?substring(0,entityName?length-1)>
</#if>
<#if entityName?ends_with("ie")>
<#assign entityName=entityName?substring(0,entityName?length-2)+"y">
</#if>
<#if entityName?ends_with("sse")>
<#assign entityName=entityName?substring(0,entityName?length-1)>
</#if>
<#assign entityName=entityName?cap_first>
// @formatter:off
package ${packageName};

import java.util.List;

public interface ${entityName}Provider {

	void create${entityName}(${entityName} ${entityName?uncap_first});

	void update${entityName}(${entityName} ${entityName?uncap_first});

	${entityName} find${entityName}ById(Long id);

	List<${entityName}> list${entityName}();

}