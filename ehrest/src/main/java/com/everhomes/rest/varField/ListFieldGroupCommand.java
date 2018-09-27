package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>moduleName: 模块名</li>
 *     <li>communityId: 项目id</li>
 *     <li>orgId: 公司id</li>
 *     <li>ownerType: 公司type</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ListFieldGroupCommand {

    private String moduleName;

    private Integer namespaceId;

    private Long communityId;

    private Long orgId;

    private String ownerType;

    // 暂时增加用于物业巡检使用
    private String  equipmentCategoryName;

    private Long inspectionCategoryId;

    //客户有根据权限区分动态excel的cell
    private Boolean isAdmin;

	private Long categoryId;

    private Long ownerId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getEquipmentCategoryName() {
        return equipmentCategoryName;
    }

    public void setEquipmentCategoryName(String equipmentCategoryName) {
        this.equipmentCategoryName = equipmentCategoryName;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }


    public Long getInspectionCategoryId() {
        return inspectionCategoryId;
    }

    public void setInspectionCategoryId(Long inspectionCategoryId) {
        this.inspectionCategoryId = inspectionCategoryId;
    }


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
