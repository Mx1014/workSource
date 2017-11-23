// @formatter:off
package com.everhomes.rest.print;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>creatorUid : 发起人id</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintUserOrganizationsCommand {
	private String ownerType;
	private Long ownerId;
	@NotNull
	private Long creatorUid;
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return StringHelper.toJsonString(this);
	}
}
