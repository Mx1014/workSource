// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>memberId：员工编号</li>
 * <li>contactName：成员名称</li>
 * <li>organizationId: 小区id</li>
 * <li>gender：性别，0：保密 1：男性 2：女性</li>
 * <li>departments：部门列表</li>
 * <li>jobPositions: 岗位</li>
 * <li>jobLevels: 职级</li>
 * <li>employeeStatus：员工状态, 0: 试用 1: 在职 2: 离职</li>
 * <li>checkInTime：入职时间</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：联系号码</li>
 * <li>avatar: 成员头像</li>
 * </ul>
 */
public class OrganizationMemberBasicDTO {
    @NotNull
    private Long memberId;

    private String contactName;

    private Byte gender;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobLevels;

    private Byte employeeStatus;

    private String checkInTime;

    private Byte contactType;
    private String contactToken;

    private String avatar;

    /*
    @NotNull
    private Long   organizationId;
    private String   organizationName;
    private String targetType;
    @NotNull
    private Long   targetId;
    private String memberGroup;
    private String contactDescription;
    private Byte   status;private String initial;
    private String fullPinyin;
    private String fullInitial;
    @ItemType(RoleDTO.class)
    private List<RoleDTO> roles;
    private Long groupId;
    private String groupName;
    private String   nickName;
    private Long creatorUid;
    @ItemType(OrganizationDTO.class)
	private List<OrganizationDTO> groups;
	private Integer proccesingTaskCount;
    private String groupType;
    private String groupPath;
    private String position;
    private String idNumber;
    targetType：成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
    targetId：注册用户对应的userId，未注册填0</li>
    memberGroup：组织角色类型 参考{@link com.everhomes.rest.organization.pm.PmMemberGroup}</li>
    contactDescription：描述</li>
    status：状态</li>
    roles：角色列表</li>
    groups：群组列表</li>
    employeeNo：工号</li>
    initial：首字母</li>
    proccesingTaskCount：执行任务数量</li>
    executiveFlag：是否高管 1-是 0-否</li>
    position：职位</li>
    idNumber：身份证号码</li>
    */
    public OrganizationMemberBasicDTO() {
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

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
