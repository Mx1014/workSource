// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userIds: 被授权的用户id列表</li>
 * <li>orgIds: 被授权的组织节点id列表</li>
 * <li>companyAddressIds: 被授权的公司的楼栋门牌id列表</li>
 * <li>familyAddressIds: 被授权的家庭的楼栋门牌id列表</li>
 * <li>doorIds: 门禁 ID列表，必填项</li>
 * <li>doorLocationIds:门禁位置id列表,每项由communityId,buildingId和楼层组成,用下划线隔开</li>
 * <li>rightRemote: 远程开门权限 0无 1有 null无</li>
 * <li>rightVisitor: 访客授权权限 0无 1有 null无</li>
 * </ul>
 */
public class CreateFormalAuthBatchCommand {
	private List<Long> userIds;
	private List<Long> orgIds;
	private List<Long> companyAddressIds;
	private List<Long> familyAddressIds;
	private List<Long> doorIds;
	private List<String> doorLocationIds;
	private Byte rightRemote;
	private Byte rightVisitor;
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	public List<Long> getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}
	public List<Long> getCompanyAddressIds() {
		return companyAddressIds;
	}
	public void setCompanyAddressIds(List<Long> companyAddressIds) {
		this.companyAddressIds = companyAddressIds;
	}
	public List<Long> getFamilyAddressIds() {
		return familyAddressIds;
	}
	public void setFamilyAddressIds(List<Long> familyAddressIds) {
		this.familyAddressIds = familyAddressIds;
	}
	public List<Long> getDoorIds() {
		return doorIds;
	}
	public void setDoorIds(List<Long> doorIds) {
		this.doorIds = doorIds;
	}
	public List<String> getDoorLocationIds() {
		return doorLocationIds;
	}
	public void setDoorLocationIds(List<String> doorLocationIds) {
		this.doorLocationIds = doorLocationIds;
	}
	public Byte getRightRemote() {
		return rightRemote;
	}
	public void setRightRemote(Byte rightRemote) {
		this.rightRemote = rightRemote;
	}
	public Byte getRightVisitor() {
		return rightVisitor;
	}
	public void setRightVisitor(Byte rightVisitor) {
		this.rightVisitor = rightVisitor;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
