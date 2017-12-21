package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.module.ControlTarget;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 当前机构id</li>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型, 参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司, 如果是左邻后台传0即可</li>
 * <li>targetIds：对象id列表</li>
 * <li>targetType：对象类型，EhUsers, EhOrganizations,{@link com.everhomes.rest.common.EntityType}</li>
 * <li>allFlag：是否全部业务模块 1：是 0：否，{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>moduleIds:  业务模块id集合</li>
 * <li>allCommunityControlFlag: 受community控制的应用的全部标识</li>
 * <li>communityControlOption: 园区的控制标识</li>
 * <li>communityControlIds: 园区ids</>
 * <li>communityTarget: 园区控制范围下的个应用目标,参考{@link ModuleAppTarget}</li>
 * <li>allOrgControlFlag: 受OA控制的应用的全部标识</li>
 * <li>orgControlOption: OA的控制标识</li>
 * <li>orgControlDetails: 具体的OA控制参数，参考{@link ControlTarget}</li>
 * <li>orgTarget: 受OA控制的应用目标，参考{@link ModuleAppTarget}</li>
 * <li>allUnlimitControlFlag: 不受限制的应用的全部标识</li>
 * <li>unlimitTarget: 不受限制的应用的目标，参考{@link ModuleAppTarget}</li>
 * </ul>
 */
public class ResetServiceModuleAdministratorsCommand {

    @NotNull
    private Long organizationId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long ownerId;

    @NotNull
    @ItemType(Long.class)
    private List<Long> targetIds;

    private String targetType;

    private Byte allFlag;

    @NotNull
    @ItemType(Long.class)
    private List<Long> moduleIds;

    //
    private Byte allCommunityControlFlag;

    private Byte communityControlOption;

    @ItemType(Long.class)
    private List<Long> communityControlIds;

    @ItemType(Long.class)
    private List<ModuleAppTarget> communityTarget;

    //
    private Byte allOrgControlFlag;

    private Byte orgControlOption;

    @ItemType(ControlTarget.class)
    private List<ControlTarget> orgControlDetails;

    @ItemType(Long.class)
    private List<ModuleAppTarget> orgTarget;

    //
    private Byte allUnlimitControlFlag;

    @ItemType(Long.class)
    private List<ModuleAppTarget> unlimitTarget;


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Byte getAllFlag() {
        return allFlag;
    }

    public void setAllFlag(Byte allFlag) {
        this.allFlag = allFlag;
    }

    public List<Long> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<Long> moduleIds) {
        this.moduleIds = moduleIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Byte getAllCommunityControlFlag() {
        return allCommunityControlFlag;
    }

    public void setAllCommunityControlFlag(Byte allCommunityControlFlag) {
        this.allCommunityControlFlag = allCommunityControlFlag;
    }

    public List<Long> getCommunityControlIds() {
        return communityControlIds;
    }

    public void setCommunityControlIds(List<Long> communityControlIds) {
        this.communityControlIds = communityControlIds;
    }

    public List<ModuleAppTarget> getCommunityTarget() {
        return communityTarget;
    }

    public void setCommunityTarget(List<ModuleAppTarget> communityTarget) {
        this.communityTarget = communityTarget;
    }

    public Byte getAllOrgControlFlag() {
        return allOrgControlFlag;
    }

    public void setAllOrgControlFlag(Byte allOrgControlFlag) {
        this.allOrgControlFlag = allOrgControlFlag;
    }

    public List<ControlTarget> getOrgControlDetails() {
        return orgControlDetails;
    }

    public void setOrgControlDetails(List<ControlTarget> orgControlDetails) {
        this.orgControlDetails = orgControlDetails;
    }

    public List<ModuleAppTarget> getOrgTarget() {
        return orgTarget;
    }

    public void setOrgTarget(List<ModuleAppTarget> orgTarget) {
        this.orgTarget = orgTarget;
    }

    public Byte getAllUnlimitControlFlag() {
        return allUnlimitControlFlag;
    }

    public void setAllUnlimitControlFlag(Byte allUnlimitControlFlag) {
        this.allUnlimitControlFlag = allUnlimitControlFlag;
    }

    public List<ModuleAppTarget> getUnlimitTarget() {
        return unlimitTarget;
    }

    public void setUnlimitTarget(List<ModuleAppTarget> unlimitTarget) {
        this.unlimitTarget = unlimitTarget;
    }

    public List<Long> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<Long> targetIds) {
        this.targetIds = targetIds;
    }

    public Byte getCommunityControlOption() {
        return communityControlOption;
    }

    public void setCommunityControlOption(Byte communityControlOption) {
        this.communityControlOption = communityControlOption;
    }

    public Byte getOrgControlOption() {
        return orgControlOption;
    }

    public void setOrgControlOption(Byte orgControlOption) {
        this.orgControlOption = orgControlOption;
    }
}
