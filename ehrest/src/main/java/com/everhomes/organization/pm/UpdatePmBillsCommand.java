package com.everhomes.organization.pm;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;


/**
 * <ul>
 * 	<li>insertList : 待插入账单列表</li>
 *	<li>updateList : 待更新账单列表</li>
 *	<li>deleteList : 待删除账单列表</li>
 *	<li>organizationId : 组织id</li>
 *</ul>
 *
 */
public class UpdatePmBillsCommand {
	
	@NotNull
	private Long organizationId;

	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> insertList;
	
	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> updateList;
	
	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> deleteList;
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<PmBillsDTO> getInsertList() {
		return insertList;
	}

	public void setInsertList(List<PmBillsDTO> insertList) {
		this.insertList = insertList;
	}

	public List<PmBillsDTO> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<PmBillsDTO> updateList) {
		this.updateList = updateList;
	}

	public List<PmBillsDTO> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<PmBillsDTO> deleteList) {
		this.deleteList = deleteList;
	}
	
	

}
