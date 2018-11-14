//@formatter:off
package com.everhomes.rest.asset.bill;

import java.util.List;

/**
 *<ul>
 * <li>list: 所有合同ID校验是否产生已缴账单的结果集</li>
 *</ul>
 */
public class BatchDeleteBillResponse {
    private List<CheckContractIsProduceBillDTO> list;

	public List<CheckContractIsProduceBillDTO> getList() {
		return list;
	}

	public void setList(List<CheckContractIsProduceBillDTO> list) {
		this.list = list;
	}
	
}
