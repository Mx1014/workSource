// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.OrganizationMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>contactName：成员名称</li>
 * <li>contactToken：联系信息</li>
 * <li>gender：性别</li>
 * <li>employeeNo：工号</li>
 * <li>contactDescription：描述</li>
 * <li>employeeNo: 工号</li>
 * <li>employeeType: 员工类型：0，全职 1，兼职 2，实习 3，劳动派遣 参考{@link com.everhomes.rest.organization.EmployeeType}</li>
 * <li>employeeStatus: 员工状态, 0: 试用 1: 在职 2: 离职 参考{@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * <li>employmentTime: 转正时间</li>
 * <li>checkInTime: 入职时间</li>
 * <li>departmentIds：添加到多部门</li>
 * <li>jobPositionIds：添加到多群组</li>
 * </ul>
 */
public class AddOrganizationPersonnelCommand {
	
    @NotNull
    private Long   organizationId;
   
	private String targetType;
	
	private Long   targetId;
	
	private String contactName;
	
	private String contactToken;
	
	private Byte gender;

	private String employeeNo;

	private Byte employeeType;

	private Byte employeeStatus;

	private String employmentTime;

	private String checkInTime;
	
	@ItemType(Long.class)
	private List<Long> departmentIds;

	@ItemType(Long.class)
	private List<Long> groupIds;

	@ItemType(Long.class)
	private List<Long> jobPositionIds;
	
	@ItemType(Long.class)
	private List<Long> jobLevelIds;

	public AddOrganizationPersonnelCommand() {
	}

	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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

	public String getContactName() {
		return contactName;
	}


	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


	public String getContactToken() {
		return contactToken;
	}


	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}


	public Byte getGender() {
		return gender;
	}


	public void setGender(Byte gender) {
		this.gender = gender;
	}


	public String getEmployeeNo() {
		return employeeNo;
	}


	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public List<Long> getJobPositionIds() {
		return jobPositionIds;
	}

	public void setJobPositionIds(List<Long> jobPositionIds) {
		this.jobPositionIds = jobPositionIds;
	}

	public List<Long> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public List<Long> getJobLevelIds() {
		return jobLevelIds;
	}

	public void setJobLevelIds(List<Long> jobLevelIds) {
		this.jobLevelIds = jobLevelIds;
	}

	public Byte getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(Byte employeeType) {
		this.employeeType = employeeType;
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

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
