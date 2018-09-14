// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userIds: 被授权的用户id列表</li>
 * <li>orgIds: 被授权的组织节点id列表</li>
 * <li>companyCommunityIds: 被授权的公司的项目id列表(选则任意楼栋,则传communityId)</li>
 * <li>companyBuildingIds: 被授权的公司的楼栋id列表</li>
 * <li>companyFloorIds: 被授权的公司的楼层id列表 (暂无)</li>
 * <li>companyAddressIds: 被授权的公司的门牌id列表</li>
 * <li>familyCommunityIds: 被授权的家庭的项目id列表(选则任意楼栋,则传communityId)</li>
 * <li>familyBuildingIds: 被授权的家庭的楼栋id列表</li>
 * <li>familyFloorIds: 被授权的家庭的楼层id列表 (暂无)</li>
 * <li>familyAddressIds: 被授权的家庭的门牌id列表</li>
 * <li>doorAuthlist: 门禁(组)/地址列表，必填项{@link com.everhomes.rest.aclink.CreateFormalAuthCommand}</li>
 * </ul>
 */
public class CreateFormalAuthBatchCommand {
	private List<Long> userIds;
	private List<Long> orgIds;
	private List<Long> companyCommunityIds;
	private List<Long> companyBuildingIds;
	private List<Long> companyFloorIds;
	private List<Long> companyAddressIds;
	private List<Long> familyCommunityIds;
	private List<Long> familyBuildingIds;
	private List<Long> familyFloorIds;
	private List<Long> familyAddressIds;
	private List<CreateFormalAuthCommand> doorAuthlist;
	private String keyU;//梯控用
	
	public String getKeyU() {
		return keyU;
	}
	public void setKeyU(String keyU) {
		this.keyU = keyU;
	}
	public List<Long> getCompanyCommunityIds() {
		return companyCommunityIds;
	}
	public void setCompanyCommunityIds(List<Long> companyCommunityIds) {
		this.companyCommunityIds = companyCommunityIds;
	}
	public List<Long> getCompanyBuildingIds() {
		return companyBuildingIds;
	}
	public void setCompanyBuildingIds(List<Long> companyBuildingIds) {
		this.companyBuildingIds = companyBuildingIds;
	}
	public List<Long> getCompanyFloorIds() {
		return companyFloorIds;
	}
	public void setCompanyFloorIds(List<Long> companyFloorIds) {
		this.companyFloorIds = companyFloorIds;
	}
	public List<Long> getFamilyCommunityIds() {
		return familyCommunityIds;
	}
	public void setFamilyCommunityIds(List<Long> familyCommunityIds) {
		this.familyCommunityIds = familyCommunityIds;
	}
	public List<Long> getFamilyBuildingIds() {
		return familyBuildingIds;
	}
	public void setFamilyBuildingIds(List<Long> familyBuildingIds) {
		this.familyBuildingIds = familyBuildingIds;
	}
	public List<Long> getFamilyFloorIds() {
		return familyFloorIds;
	}
	public void setFamilyFloorIds(List<Long> familyFloorIds) {
		this.familyFloorIds = familyFloorIds;
	}
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
    public List<CreateFormalAuthCommand> getDoorAuthlist() {
		return doorAuthlist;
	}
	public void setDoorAuthlist(List<CreateFormalAuthCommand> doorAuthlist) {
		this.doorAuthlist = doorAuthlist;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
