// @formatter:off
package ${targetRestPackage};
<#if hasDecimal??>

import java.math.BigDecimal;

</#if>
<#if hasList??>

import java.util.List;

</#if>
<#if hasList??>import com.everhomes.discover.ItemType;</#if>
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul><#if isParam??>参数<#else>返回值</#if>:
<#list fields as f>
 * <li>${f.name}: ${f.desc}</li>
</#list>
 * </ul>
 */
public class ${modelName} {
<#list fields as f>

<#if f.type?starts_with("List<")>
	@ItemType(${f.type?substring(f.type?index_of("<")+1, f.type?index_of(">"))}.class)
</#if>
	private ${f.type} ${f.name};
</#list>

	public ${modelName}() {

	}

	public ${modelName}(<#list fields as param>${param.type} ${param.name}<#sep>, </#sep></#list>) {
		super();
<#list fields as f>
		this.${f.name} = ${f.name};
</#list>
	}
<#list fields as f>

	public ${f.type} get${f.name?cap_first}() {
		return ${f.name};
	}

	public void set${f.name?cap_first}(${f.type} ${f.name}) {
		this.${f.name} = ${f.name};
	}
</#list>

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
