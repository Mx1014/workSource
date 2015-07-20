package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;


/**
 * <ul>
 * 	<li>insertList : 待插入账单列表</li>
 *	<li>updateList : 待更新账单列表</li>
 *	<li>deleteList : 待删除账单列表</li>
 *	<li>communityId : 小区id</li>
 *</ul>
 *
 */
public class UpdatePmBillsCommand {
	
	private Long communityId;
	

	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> insertList;
	
	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> updateList;
	
	@ItemType(PmBillsDTO.class)
	private List<PmBillsDTO> deleteList;
	
	public Long getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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
