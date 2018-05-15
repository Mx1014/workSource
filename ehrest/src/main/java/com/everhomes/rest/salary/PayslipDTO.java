package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>payslipId: id</li>
 * <li>salaryPeriod: 期数YYYYMM</li>
 * <li>name: 工资表名称</li>
 * <li>sendCount: 发放人数</li>
 * <li>viewCount: 查看人数</li>
 * <li>confirmCount: 确认人数</li>
 * <li>revokeCount: 撤回人数</li>
 * <li>creatorUid: 发放者id</li>
 * <li>creatorName: 发放人姓名</li>
 * <li>createTime: 发放时间</li>
 * </ul>
 */
public class PayslipDTO implements Comparable{

	private Long payslipId;
    private String salaryPeriod;
    private String name;
    private Integer sendCount;
    private Integer viewCount;
    private Integer confirmCount;
    private Integer revokeCount;
    private Long creatorUid;
    private String creatorName;
    private Long createTime;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getConfirmCount() {
		return confirmCount;
	}

	public void setConfirmCount(Integer confirmCount) {
		this.confirmCount = confirmCount;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPayslipId() {
		return payslipId;
	}

	public void setPayslipId(Long payslipId) {
		this.payslipId = payslipId;
	}

	public Integer getRevokeCount() {
		return revokeCount;
	}

	public void setRevokeCount(Integer revokeCount) {
		this.revokeCount = revokeCount;
	}

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	@Override
	public int compareTo(Object o) {
		try {
			PayslipDTO dto = (PayslipDTO) o;
				return this.getCreateTime() < dto.getCreateTime() ? 1 : -1;
		} catch (Exception e) {
			//pass
		}
		return 0;
	}
}
