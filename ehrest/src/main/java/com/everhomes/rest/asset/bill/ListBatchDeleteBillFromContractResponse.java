//@formatter:off
package com.everhomes.rest.asset.bill;

import java.util.List;

/**
 *<ul>
 * <li>list: 批量删除合同产生的相关账单、费项明细数据的结果</li>
 *</ul>
 */
public class ListBatchDeleteBillFromContractResponse {
    private List<BatchDeleteBillFromContractDTO> list;

	public List<BatchDeleteBillFromContractDTO> getList() {
		return list;
	}

	public void setList(List<BatchDeleteBillFromContractDTO> list) {
		this.list = list;
	}
	
}
