package com.everhomes.rest.acl;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 当前机构id</li>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型, 参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司, 如果是左邻后台传0即可</li>
 * <li>targetId：对象id</li>
 * <li>targetType：对象类型，EhUsers, EhOrganizations,{@link com.everhomes.rest.common.EntityType}</li>
 * <li>allFlag：是否全部业务模块 1：是 0：否，{@link com.everhomes.rest.common.AllFlagType}</li>
 * <li>moduleIds:  业务模块id集合</li>
 * <li>module_app_id: 应用id</li>
 * <li>module_app_control_type: 应用控制类型</>
 * <li>all_community_control_flag: 全部园区标识</li>
 * <li>community_control_json: 园区范围详情</li>
 * <li>all_org_control_flag: 全公司范围标识</li>
 * <li>org_control_json: 组织架构范围详情</li>
 * </ul>
 */
public class CreateServiceModuleAdministratorsCommand {

    @NotNull
    private Long organizationId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long ownerId;

    private Long targetId;

    private String targetType;

    private Byte allFlag;

    @NotNull
    @ItemType(Long.class)
    private List<Long> moduleIds;

    private Long module_app_id;

    private String module_app_control_type;

    private Byte all_community_control_flag;

    private String community_control_json;

    private Byte all_org_control_flag;

    private String org_control_json;

    private Byte all_unlimit_control_flag;

    @ItemType(Long.class)
    private List<ModuleAppTarget> community_target;

    @ItemType(Long.class)
    private List<ModuleAppTarget> org_target;

    @ItemType(Long.class)
    private List<ModuleAppTarget> unlimit_target;


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

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
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


    public Long getModule_app_id() {
        return module_app_id;
    }

    public void setModule_app_id(Long module_app_id) {
        this.module_app_id = module_app_id;
    }

    public Byte getAll_community_control_flag() {
        return all_community_control_flag;
    }

    public void setAll_community_control_flag(Byte all_community_control_flag) {
        this.all_community_control_flag = all_community_control_flag;
    }

    public String getCommunity_control_json() {
        return community_control_json;
    }

    public void setCommunity_control_json(String community_control_json) {
        this.community_control_json = community_control_json;
    }

    public Byte getAll_org_control_flag() {
        return all_org_control_flag;
    }

    public void setAll_org_control_flag(Byte all_org_control_flag) {
        this.all_org_control_flag = all_org_control_flag;
    }

    public String getOrg_control_json() {
        return org_control_json;
    }

    public void setOrg_control_json(String org_control_json) {
        this.org_control_json = org_control_json;
    }

    public String getModule_app_control_type() {
        return module_app_control_type;
    }

    public void setModule_app_control_type(String module_app_control_type) {
        this.module_app_control_type = module_app_control_type;
    }

    public List<ModuleAppTarget> getCommunity_target() {
        return community_target;
    }

    public void setCommunity_target(List<ModuleAppTarget> community_target) {
        this.community_target = community_target;
    }

    public List<ModuleAppTarget> getOrg_target() {
        return org_target;
    }

    public void setOrg_target(List<ModuleAppTarget> org_target) {
        this.org_target = org_target;
    }

    public List<ModuleAppTarget> getUnlimit_target() {
        return unlimit_target;
    }

    public void setUnlimit_target(List<ModuleAppTarget> unlimit_target) {
        this.unlimit_target = unlimit_target;
    }

    public Byte getAll_unlimit_control_flag() {
        return all_unlimit_control_flag;
    }

    public void setAll_unlimit_control_flag(Byte all_unlimit_control_flag) {
        this.all_unlimit_control_flag = all_unlimit_control_flag;
    }
}
