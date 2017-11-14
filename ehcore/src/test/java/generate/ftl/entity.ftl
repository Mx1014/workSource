<#if entityName?ends_with("s")>
<#assign MyEntityName=entityName?substring(0,entityName?length-1)>
<#else>
<#assign MyEntityName=entityName>
</#if>
<#if MyEntityName?ends_with("ie")>
<#assign MyEntityName=MyEntityName?substring(0,MyEntityName?length-2)+"y">
</#if>
<#if MyEntityName?ends_with("sse")>
<#assign MyEntityName=MyEntityName?substring(0,MyEntityName?length-1)>
</#if>
<#assign MyEntityName=MyEntityName?cap_first>
<#assign entityName=entityName?cap_first>
// @formatter:off
package ${packageName};

import com.everhomes.server.schema.tables.pojos.Eh${entityName};
import com.everhomes.util.StringHelper;

public class ${MyEntityName} extends Eh${entityName} {
	
	private static final long serialVersionUID = ${serialNumber}L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}