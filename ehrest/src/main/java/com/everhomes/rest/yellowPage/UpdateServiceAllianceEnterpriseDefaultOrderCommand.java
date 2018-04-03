// @formatter:off
package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>values : 新顺序集合
 * ServiceAllianceDTO 中赋予id值和defaultOrder即可，其他目前功能不需要赋值。参考 {@link com.everhomes.rest.yellowPage.ServiceAllianceDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class UpdateServiceAllianceEnterpriseDefaultOrderCommand {
	
	@ItemType(ServiceAllianceDTO.class)
	private List<ServiceAllianceDTO> values;
	public List<ServiceAllianceDTO> getValues() {
		return values;
	}
	public void setValues(List<ServiceAllianceDTO> values) {
		this.values = values;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
