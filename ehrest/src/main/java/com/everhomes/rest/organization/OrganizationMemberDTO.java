// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>organizationId: 小区id</li>
 *     <li>organizationName: organizationName</li>
 *     <li>targetType: 成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
 *     <li>targetId: 注册用户对应的userId，未注册填0</li>
 *     <li>memberGroup: 组织角色类型 参考{@link com.everhomes.rest.organization.pm.PmMemberGroup}</li>
 *     <li>contactName: 成员名称</li>
 *     <li>contactType: 成员类型：{@link com.everhomes.rest.user.IdentifierType}</li>
 *     <li>contactToken: 成员标识</li>
 *     <li>contactDescription: 描述</li>
 *     <li>status: 状态</li>
 *     <li>initial: 首字母</li>
 *     <li>fullPinyin: fullPinyin</li>
 *     <li>fullInitial: fullInitial</li>
 *     <li>roles: 角色列表 {@link com.everhomes.rest.acl.admin.RoleDTO}</li>
 *     <li>groupId: groupId</li>
 *     <li>groupName: groupName</li>
 *     <li>nickName: nickName</li>
 *     <li>avatar: avatar</li>
 *     <li>creatorUid: creatorUid</li>
 *     <li>employeeNo: 工号</li>
 *     <li>gender: gender</li>
 *     <li>visibleFlag: visibleFlag</li>
 *     <li>groups: 群组列表 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 *     <li>departments: 部门列表 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 *     <li>executiveFlag: 是否高管 1-是 0-否</li>
 *     <li>position: 职位</li>
 *     <li>idNumber: 身份证号码</li>
 *     <li>jobPositions: jobPositions {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 *     <li>jobLevels: jobLevels {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 *     <li>proccesingTaskCount: 执行任务数量</li>
 *     <li>groupType: groupType</li>
 *     <li>groupPath: groupPath</li>
 *     <li>createTime: 创建时间</li>
 *     <li>approveTime: 审核时间</li>
 *     <li>operatorName: 审核人</li>
 *     <li>operatorPhone: 审核人电话</li>
 * </ul>
 */
public class OrganizationMemberDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long organizationId;
    private String organizationName;
    private String targetType;
    @NotNull
    private Long targetId;

    private String memberGroup;
    private String contactName;
    private Byte contactType;
    private String contactToken;
    private String contactDescription;
    private Byte status;
    private String initial;
    private String fullPinyin;
    private String fullInitial;

    @ItemType(RoleDTO.class)
    private List<RoleDTO> roles;

    private Long groupId;

    private String groupName;

    private String nickName;
    private String avatar;

    private Long creatorUid;

    private Long employeeNo;
    private Byte gender;

    private Byte visibleFlag;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> groups;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    private Byte executiveFlag;
    private String position;
    private String idNumber;
    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobLevels;

    private Integer proccesingTaskCount;

    private String groupType;

    private String groupPath;

    private Timestamp createTime;
    private Long approveTime;
    private String operatorName;
    private String operatorPhone;

    public OrganizationMemberDTO() {
    }

    public Byte getExecutiveFlag() {
        return executiveFlag;
    }

    public void setExecutiveFlag(Byte executiveFlag) {
        this.executiveFlag = executiveFlag;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getMemberGroup() {
        return memberGroup;
    }

    public void setMemberGroup(String memberGroup) {
        this.memberGroup = memberGroup;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public String getContactDescription() {
        return contactDescription;
    }

    public void setContactDescription(String contactDescription) {
        this.contactDescription = contactDescription;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Long approveTime) {
        this.approveTime = approveTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(Long employeeNo) {
        this.employeeNo = employeeNo;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }


    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }


    public String getFullPinyin() {
        return fullPinyin;
    }

    public void setFullPinyin(String fullPinyin) {
        this.fullPinyin = fullPinyin;
    }

    public String getFullInitial() {
        return fullInitial;
    }

    public void setFullInitial(String fullInitial) {
        this.fullInitial = fullInitial;
    }


    public List<OrganizationDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<OrganizationDTO> groups) {
        this.groups = groups;
    }


    public List<OrganizationDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<OrganizationDTO> departments) {
        this.departments = departments;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getProccesingTaskCount() {
        return proccesingTaskCount;
    }

    public void setProccesingTaskCount(Integer proccesingTaskCount) {
        this.proccesingTaskCount = proccesingTaskCount;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }
}
