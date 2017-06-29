package com.everhomes.rest.salary;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
/**
 * 
 * <ul>参数:
 * <li>id: salaryEmployeeId</li>
 * <li>userId: 用户 id</li>
 * <li>employeeNo: 员工编号(可为空)</li>
 * <li>contactName: 员工姓名</li>
 * <li>departments: 员工部门 </li>
 * <li>jobPositions: 员工岗位 </li>
 * <li>salaryGroupId: 批次id</li>
 * <li>salaryGroupName: 批次名称</li>
 * <li>salaryPeriod: 所属类型:Organization</li>
 * <li>paidMoney: 实发工资</li>
 * <li>checkFlag: 核算标准0-未核算 1-核算</li>
 * <li>periodEmployeeEntitys: 批次档期的字段列表 参考{@link com.everhomes.rest.salary.SalaryPeriodEmployeeEntityDTO}</li>
 * </ul>
 */
public class SalaryPeriodEmployeeDTO {

	@ItemType(SalaryPeriodEmployeeEntityDTO.class)
	private List<SalaryPeriodEmployeeEntityDTO> periodEmployeeEntitys;
	private Long id ;

    private String employeeNo;

    private String contactName;

    private String departments;

    private String jobPositions;

    private Long salaryGroupId;

    private String salaryGroupName;
 
    private BigDecimal paidMoney;
    private String salaryPeriod;
    private Long userId;
	private Byte checkFlag;
	public List<SalaryPeriodEmployeeEntityDTO> getPeriodEmployeeEntitys() {
		return periodEmployeeEntitys;
	}
	public void setPeriodEmployeeEntitys(List<SalaryPeriodEmployeeEntityDTO> periodEmployeeEntitys) {
		this.periodEmployeeEntitys = periodEmployeeEntitys;
	}
	public String getSalaryPeriod() {
		return salaryPeriod;
	}
	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Byte getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(Byte checkFlag) {
		this.checkFlag = checkFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Long getSalaryGroupId() {
		return salaryGroupId;
	}
	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}
	public String getSalaryGroupName() {
		return salaryGroupName;
	}
	public void setSalaryGroupName(String salaryGroupName) {
		this.salaryGroupName = salaryGroupName;
	}
	public BigDecimal getPaidMoney() {
		return paidMoney;
	}
	public void setPaidMoney(BigDecimal paidMoney) {
		this.paidMoney = paidMoney;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getJobPositions() {
		return jobPositions;
	}

	public void setJobPositions(String jobPositions) {
		this.jobPositions = jobPositions;
	}
}
