// @formatter:off
package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.util.StringHelper;

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

    private Byte community_control_flag;

    private Byte org_control_flag;

    private Byte unlimit_control_flag;

    @ItemType(ServiceModuleAppDTO.class)
    private List<ServiceModuleAppDTO> community_apps;

    @ItemType(ServiceModuleAppDTO.class)
    private List<ServiceModuleAppDTO> org_apps;

    @ItemType(ServiceModuleAppDTO.class)
    private List<ServiceModuleAppDTO> unlimit_apps;


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

    public Byte getCommunity_control_flag() {
        return community_control_flag;
    }

    public void setCommunity_control_flag(Byte community_control_flag) {
        this.community_control_flag = community_control_flag;
    }

    public Byte getOrg_control_flag() {
        return org_control_flag;
    }

    public void setOrg_control_flag(Byte org_control_flag) {
        this.org_control_flag = org_control_flag;
    }

    public Byte getUnlimit_control_flag() {
        return unlimit_control_flag;
    }

    public void setUnlimit_control_flag(Byte unlimit_control_flag) {
        this.unlimit_control_flag = unlimit_control_flag;
    }

    public List<ServiceModuleAppDTO> getCommunity_apps() {
        return community_apps;
    }

    public void setCommunity_apps(List<ServiceModuleAppDTO> community_apps) {
        this.community_apps = community_apps;
    }

    public List<ServiceModuleAppDTO> getOrg_apps() {
        return org_apps;
    }

    public void setOrg_apps(List<ServiceModuleAppDTO> org_apps) {
        this.org_apps = org_apps;
    }

    public List<ServiceModuleAppDTO> getUnlimit_apps() {
        return unlimit_apps;
    }

    public void setUnlimit_apps(List<ServiceModuleAppDTO> unlimit_apps) {
        this.unlimit_apps = unlimit_apps;
    }
}
