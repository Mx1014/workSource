//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

/**
 * @author created by yangcx
 * @date 2018年5月23日----下午5:56:48
 */
/**
 *<ul>
 * <li>listGeneralBillsDTOs: 统一账单记账之后的返回值</li>
 *</ul>
 */
public class ListGeneralBillsResponse {
	private List<ListGeneralBillsDTO> listGeneralBillsDTOs;

	public List<ListGeneralBillsDTO> getListGeneralBillsDTOs() {
		return listGeneralBillsDTOs;
	}

	public void setListGeneralBillsDTOs(List<ListGeneralBillsDTO> listGeneralBillsDTOs) {
		this.listGeneralBillsDTOs = listGeneralBillsDTOs;
	}

}
