package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户id - id为0或者null就是未激活</li>
 * <li>detailId: 员工档案id</li>
 * <li>contractName: 员工姓名</li>
 * <li>contactAvatar: 员工头像</li>
 * <li>organizationId: 总公司ID</li>
 * <li>departmentId: 查询日期时所属部门id</li>
 * <li>departmentName: 部门名称</li>
 * <li>punchOrganizationId: 查询日期时所属考勤组</li>
 * <li>statisticsCount: 查询统计项的统计值</li>
 * <li>statisticsUnit: 统计项的统计单位</li>
 * <li>ruleId: 打卡规则id 如果为空就是未设置规则</li>
 * </ul>
 */
public class PunchMemberDTO {
    private Long userId;
    private Long detailId;
    private String contractName;
    private String contactAvatar;
    private Long organizationId;
    private Long departmentId;
    private String departmentName;
    private Long punchOrganizationId;
    private Integer statisticsCount;
    private String statisticsUnit;
    private Long ruleId;
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getPunchOrganizationId() {
        return punchOrganizationId;
    }

    public void setPunchOrganizationId(Long punchOrganizationId) {
        this.punchOrganizationId = punchOrganizationId;
    }

    public Integer getStatisticsCount() {
        return statisticsCount;
    }

    public void setStatisticsCount(Integer statisticsCount) {
        this.statisticsCount = statisticsCount;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

    public String getStatisticsUnit() {
        return statisticsUnit;
    }

    public void setStatisticsUnit(String statisticsUnit) {
        this.statisticsUnit = statisticsUnit;
    }
}
