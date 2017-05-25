// @formatter:off
package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>values : 交换的集合，按照values的顺序依次交换，最后一项和第一项交换；
 * ServiceAllianceDTO 中赋予id值和defaultOrder即可，其他目前功能不需要赋值。参考 {@link com.everhomes.rest.yellowPage.ServiceAllianceDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class UpdateServiceAllianceEnterpriseDefaultOrderCommand {
	
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
