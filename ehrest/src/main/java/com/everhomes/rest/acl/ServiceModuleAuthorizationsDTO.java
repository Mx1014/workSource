// @formatter:off
package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.module.ControlTarget;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>targetId：对象id</li>
 * <li>targetType：对象类型，Eh_Users, Eh_Organizations,{@link com.everhomes.rest.common.EntityType}</li>
 * <li>contactType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>targetName：对象名称</li>
 * <li>identifierType：账号类型</li>
 * <li>identifierToken：手机号码</li>
 * <li>allFlag：是否全部业务模块 1：是 0：否</li>
 * <li>modules：模块列表，{@link com.everhomes.rest.acl.ServiceModuleDTO}</li>
 * <li>projects：项目列表，{@link com.everhomes.rest.acl.ProjectDTO}</li>
 * <li>community_control_flag: 受项目控制的全部标识</li>
 * <li>org_control_flag：受OA控制的全部标识</li>
 * <li>unlimit_control_flag： 不受范围控制的全部标识</li>
 * <li>community_apps: 项目控制的应用</li>
 * <li>org_apps：OA控制的应用</li>
 * <li>unlimit_apps：不受范围控制的应用</li>
 * </ul>
 */
public class ServiceModuleAuthorizationsDTO {

    private String ownerType;

    private Long ownerId;

    private Long targetId;

    private String targetType;

    private String targetName;

    private String nickName;

    private Byte identifierType;

    private String identifierToken;

    private Byte allFlag;

    @ItemType(ServiceModuleDTO.class)
    private List<ServiceModuleDTO> modules;

    private Byte communityControlFlag;

    private Byte orgControlFlag;

    private Byte unlimitControlFlag;

    @NotNull
    @ItemType(Long.class)
    private List<Long> communityControlIds;


    @ItemType(ServiceModuleAppDTO.class)
    private List<ServiceModuleAppDTO> communityApps;

    @ItemType(ControlTarget.class)
    private List<ControlTarget> orgControlDetails;

    @ItemType(ServiceModuleAppDTO.class)
    private List<ServiceModuleAppDTO> orgApps;

    @ItemType(ServiceModuleAppDTO.class)
    private List<ServiceModuleAppDTO> unlimitApps;


    @ItemType(ProjectDTO.class)
    private List<ProjectDTO> projects;

    public ServiceModuleAuthorizationsDTO() {
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

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Byte getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(Byte identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public List<ServiceModuleDTO> getModules() {
        return modules;
    }

    public void setModules(List<ServiceModuleDTO> modules) {
        this.modules = modules;
    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDTO> projects) {
        this.projects = projects;
    }

    public Byte getAllFlag() {
        return allFlag;
    }

    public void setAllFlag(Byte allFlag) {
        this.allFlag = allFlag;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Byte getCommunityControlFlag() {
        return communityControlFlag;
    }

    public void setCommunityControlFlag(Byte communityControlFlag) {
        this.communityControlFlag = communityControlFlag;
    }


    public Byte getUnlimitControlFlag() {
        return unlimitControlFlag;
    }

    public void setUnlimitControlFlag(Byte unlimitControlFlag) {
        this.unlimitControlFlag = unlimitControlFlag;
    }

    public List<ServiceModuleAppDTO> getCommunityApps() {
        return communityApps;
    }

    public void setCommunityApps(List<ServiceModuleAppDTO> communityApps) {
        this.communityApps = communityApps;
    }

    public List<ServiceModuleAppDTO> getOrgApps() {
        return orgApps;
    }

    public void setOrgApps(List<ServiceModuleAppDTO> orgApps) {
        this.orgApps = orgApps;
    }

    public List<ServiceModuleAppDTO> getUnlimitApps() {
        return unlimitApps;
    }

    public void setUnlimitApps(List<ServiceModuleAppDTO> unlimitApps) {
        this.unlimitApps = unlimitApps;
    }

    public Byte getOrgControlFlag() {
        return orgControlFlag;
    }

    public void setOrgControlFlag(Byte orgControlFlag) {
        this.orgControlFlag = orgControlFlag;
    }

    public List<Long> getCommunityControlIds() {
        return communityControlIds;
    }

    public void setCommunityControlIds(List<Long> communityControlIds) {
        this.communityControlIds = communityControlIds;
    }

    public List<ControlTarget> getOrgControlDetails() {
        return orgControlDetails;
    }

    public void setOrgControlDetails(List<ControlTarget> orgControlDetails) {
        this.orgControlDetails = orgControlDetails;
    }
}
