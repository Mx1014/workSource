package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Ryan on 2017/5/18.
 * <ul>
 * <li>id: 表增序号</li>
 * <li>memerId: 员工编号</li>
 * <li>contactName：成员名称</li>
 * <li>gender: 成员性别，0：保密 1：男性 2：女性</li>
 * <li>departments：部门列表</li>
 * <li>jobPositions: 岗位</li>
 * <li>jobLevels: 职级</li>
 * <li>organizationId: 小区id</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * <li>employeeStatus：员工状态, 0: 试用 1: 在职 2: 离职</li>
 * <li>employmentTime：转正时间</li>
 * <li>contractEndTime: 合同时间</li>
 * <li>profileIntegrity: 档案完整度,0-100%</li>
 * <li>avatar: 成员头像</li>
 * <li>visibleFlag: 成员隐藏性, 0: 显示 1: 隐藏</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>groups：群组列表</li>
 * </ul>
 */
public class OrganizationMemberV2DTO {

    @NotNull
    private Long id;

    @NotNull
    private Long memberId;

    private String contactName;

    private Byte gender;

    // 群组需修改
    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobLevels;

    @NotNull
    private Long organizationId;

    private String organizationName;

    private Byte contactType;

    private String contactToken;

    @NotNull
    private Byte employeeStatus;

    @NotNull
    private String employmentTime;

    private String contractEndTime;

    private Integer profileIntegrity;

    private String avatar;

    private Byte visibleFlag;

    /*
    @NotNull
    private String contactDescription;
    private String initial;
    private String fullPinyin;
    private String fullInitial;
    private String memberGroup;
    private Long groupId;
    private String groupName;
    private String nickName;
    private Long creatorUid;
    private String groupType;
    roles：角色列表
    private String groupPath;
    */

    private Long targetId;

    private String targetType;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> groups;

    public OrganizationMemberV2DTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public List<OrganizationDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<OrganizationDTO> departments) {
        this.departments = departments;
    }

    public List<OrganizationDTO> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<OrganizationDTO> jobPositions) {
        this.jobPositions = jobPositions;
    }

    public List<OrganizationDTO> getJobLevels() {
        return jobLevels;
    }

    public void setJobLevels(List<OrganizationDTO> jobLevels) {
        this.jobLevels = jobLevels;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Byte getContactType() {
        return contactType;
    }

    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime = employmentTime;
    }

    public String getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(String contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public Integer getProfileIntegrity() {
        return profileIntegrity;
    }

    public void setProfileIntegrity(Integer profileIntegrity) {
        this.profileIntegrity = profileIntegrity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
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

    public List<OrganizationDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<OrganizationDTO> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
