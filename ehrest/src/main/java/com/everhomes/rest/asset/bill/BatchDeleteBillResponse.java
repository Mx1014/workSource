//@formatter:off
package com.everhomes.rest.asset.bill;

import java.util.List;

/**
 *<ul>
 * <li>selectCount：共选中条数</li>
 * <li>deleteSuccess: 删除成功数量</li>
 * <li>deleteFail: 删除失败数量</li>
 * <li>batchDeleteBillDTOList: 删除失败的信息提示列表</li>
 *</ul>
 */
public class BatchDeleteBillResponse {
	private Integer selectCount;
	private Integer deleteSuccess;
	private Integer deleteFail;
    private List<BatchDeleteBillDTO> batchDeleteBillDTOList;
    
	public Integer getSelectCount() {
		return selectCount;
	}
	public void setSelectCount(Integer selectCount) {
		this.selectCount = selectCount;
	}
	public Integer getDeleteSuccess() {
		return deleteSuccess;
	}
	public void setDeleteSuccess(Integer deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}
	public Integer getDeleteFail() {
		return deleteFail;
	}
	public void setDeleteFail(Integer deleteFail) {
		this.deleteFail = deleteFail;
	}
	public List<BatchDeleteBillDTO> getBatchDeleteBillDTOList() {
		return batchDeleteBillDTOList;
	}
	public void setBatchDeleteBillDTOList(List<BatchDeleteBillDTO> batchDeleteBillDTOList) {
		this.batchDeleteBillDTOList = batchDeleteBillDTOList;
	}
	
}
