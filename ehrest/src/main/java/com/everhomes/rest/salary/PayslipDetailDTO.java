package com.everhomes.rest.salary;

import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 
 * <li>payslipDetailId: id</li>
 * <li>userId: 用户id</li>
 * <li>userDetailId: 用户detailId</li>
 * <li>name: 用户姓名</li>
 * <li>userContact: 用户手机</li>
 * <li>payslipContent: 导入内容的list 参考{@link com.everhomes.rest.salary.SalaryPeriodEmployeeEntityDTO}</li>
 * <li>viewedFlag: 已查看0-否 1-是</li>
 * <li>status: 状态0-已发送 1-已撤回  2-已确认</li>
 * <li>creatorUid: 发放者id</li>
 * <li>creatorDetailId: 发放者detail id</li>
 * <li>creatorName: 发放人姓名</li>
 * <li>createTime: 发放时间</li>
 * <li>salaryPeriod: 期数</li>
 * <li>confirmTime: 自动确认时间</li>
 * <li>importResult: 上传结果 0-成功 1-员工不存在 2-手机号为空 </li>
 * </ul>
 */
public class PayslipDetailDTO {
	private Long payslipDetailId;
    private Long userId;
    private Long userDetailId;
	private String salaryPeriod;
    private String name;
    private String userContact;
    private List<SalaryPeriodEmployeeEntityDTO> payslipContent;
    private Byte viewedFlag;
    private Byte status;
    private Long creatorUid;
    private Long creatorDetailId;
    private String creatorName;
    private Long createTime;
    private Long confirmTime;
    private Byte importResult;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserDetailId() {
		return userDetailId;
	}
	public void setUserDetailId(Long userDetailId) {
		this.userDetailId = userDetailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	} 
	public Byte getViewedFlag() {
		return viewedFlag;
	}
	public void setViewedFlag(Byte viewedFlag) {
		this.viewedFlag = viewedFlag;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getPayslipDetailId() {
		return payslipDetailId;
	}
	public void setPayslipDetailId(Long payslipDetailId) {
		this.payslipDetailId = payslipDetailId;
	}
	public Long getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Long confirmTime) {
		this.confirmTime = confirmTime;
	}
	public Byte getImportResult() {
		return importResult;
	}
	public void setImportResult(Byte importResult) {
		this.importResult = importResult;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}

	public List<SalaryPeriodEmployeeEntityDTO> getPayslipContent() {
		return payslipContent;
	}

	public void setPayslipContent(List<SalaryPeriodEmployeeEntityDTO> payslipContent) {
		this.payslipContent = payslipContent;
	}
	public Long getCreatorDetailId() {
		return creatorDetailId;
	}
	public void setCreatorDetailId(Long creatorDetailId) {
		this.creatorDetailId = creatorDetailId;
	}
}
