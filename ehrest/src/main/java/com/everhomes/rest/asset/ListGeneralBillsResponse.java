//@formatter:off
package com.everhomes.rest.asset;

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
	private ListGeneralBillsDTO listGeneralBillsDTO;

	public ListGeneralBillsDTO getListGeneralBillsDTO() {
		return listGeneralBillsDTO;
	}

	public void setListGeneralBillsDTO(ListGeneralBillsDTO listGeneralBillsDTO) {
		this.listGeneralBillsDTO = listGeneralBillsDTO;
	}

}
