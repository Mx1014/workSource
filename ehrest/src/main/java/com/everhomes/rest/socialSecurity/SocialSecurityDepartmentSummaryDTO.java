package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 社保部门汇总表:
 * <li>id :</li>
 * <li>namespaceId :</li>
 * <li>ownerId : 所属企业</li>
 * <li>deptId : user department id</li>
 * <li>deptName : user department name</li>
 * <li>payMonth : yyyymm</li>
 * <li>employeeCount : 人数</li>
 * <li>socialSecuritySum : 社保合计</li>
 * <li>socialSecurityCompanySum : 社保企业合计</li>
 * <li>socialSecurityEmployeeSum : 社保个人合计</li>
 * <li>pensionCompanySum : 养老企业合计</li>
 * <li>pensionEmployeeSum : 养老个人合计</li>
 * <li>medicalCompanySum : 医疗企业合计</li>
 * <li>medicalEmployeeSum : 医疗个人合计</li>
 * <li>injuryCompanySum : 工伤企业合计</li>
 * <li>injuryEmployeeSum : 工伤个人合计</li>
 * <li>unemploymentCompanySum : 失业企业合计</li>
 * <li>unemploymentEmployeeSum : 失业个人合计</li>
 * <li>birthCompanySum : 生育企业合计</li>
 * <li>birthEmployeeSum : 生育个人合计</li>
 * <li>criticalIllnessCompanySum : 大病企业合计</li>
 * <li>criticalIllnessEmployeeSum : 大病个人合计</li>
 * <li>afterSocialSecurityCompanySum : 补缴社保企业合计</li>
 * <li>afterSocialSecurityEmployeeSum : 补缴社保个人合计</li>
 * <li>afterPensionCompanySum : 补缴养老企业合计</li>
 * <li>afterPensionEmployeeSum : 补缴养老个人合计</li>
 * <li>afterMedicalCompanySum : 补缴医疗企业合计</li>
 * <li>afterMedicalEmployeeSum : 补缴医疗个人合计</li>
 * <li>afterInjuryCompanySum : 补缴工伤企业合计</li>
 * <li>afterInjuryEmployeeSum : 补缴工伤个人合计</li>
 * <li>afterUnemploymentCompanySum : 补缴失业企业合计</li>
 * <li>afterUnemploymentEmployeeSum : 补缴失业个人合计</li>
 * <li>afterBirthCompanySum : 补缴生育企业合计</li>
 * <li>afterBirthEmployeeSum : 补缴生育个人合计</li>
 * <li>afterCriticalIllnessCompanySum : 补缴大病企业合计</li>
 * <li>afterCriticalIllnessEmployeeSum : 补缴大病个人合计</li>
 * <li>disabilitySum : 残障金</li>
 * <li>commercialInsurance : 商业保险</li>
 * <li>accumulationFundSum : 公积金合计</li>
 * <li>accumulationFundCompanySum : 公积金企业合计</li>
 * <li>accumulationFundEmployeeSum : 公积金个人合计</li>
 * <li>afterAccumulationFundCompanySum : 补缴公积金企业合计</li>
 * <li>afterAccumulationFundEmployeeSum : 补缴公积金个人合计</li>
 * <li>creatorUid :</li>
 * <li>createTime :</li>
 * <li>fileUid : 归档人</li>
 * <li>fileTime : 归档时</li>
 * </ul>
 */
public class SocialSecurityDepartmentSummaryDTO {
	private Long id;
	private Integer namespaceId;
	private Long ownerId;
	private Long deptId;
	private String deptName;
	private String payMonth;
	private Integer employeeCount;
	private BigDecimal socialSecuritySum;
	private BigDecimal socialSecurityCompanySum;
	private BigDecimal socialSecurityEmployeeSum;
	private BigDecimal pensionCompanySum;
	private BigDecimal pensionEmployeeSum;
	private BigDecimal medicalCompanySum;
	private BigDecimal medicalEmployeeSum;
	private BigDecimal injuryCompanySum;
	private BigDecimal injuryEmployeeSum;
	private BigDecimal unemploymentCompanySum;
	private BigDecimal unemploymentEmployeeSum;
	private BigDecimal birthCompanySum;
	private BigDecimal birthEmployeeSum;
	private BigDecimal criticalIllnessCompanySum;
	private BigDecimal criticalIllnessEmployeeSum;
	private BigDecimal afterSocialSecurityCompanySum;
	private BigDecimal afterSocialSecurityEmployeeSum;
	private BigDecimal afterPensionCompanySum;
	private BigDecimal afterPensionEmployeeSum;
	private BigDecimal afterMedicalCompanySum;
	private BigDecimal afterMedicalEmployeeSum;
	private BigDecimal afterInjuryCompanySum;
	private BigDecimal afterInjuryEmployeeSum;
	private BigDecimal afterUnemploymentCompanySum;
	private BigDecimal afterUnemploymentEmployeeSum;
	private BigDecimal afterBirthCompanySum;
	private BigDecimal afterBirthEmployeeSum;
	private BigDecimal afterCriticalIllnessCompanySum;
	private BigDecimal afterCriticalIllnessEmployeeSum;
	private BigDecimal disabilitySum;
	private BigDecimal commercialInsurance;
	private BigDecimal accumulationFundSum;
	private BigDecimal accumulationFundCompanySum;
	private BigDecimal accumulationFundEmployeeSum;
	private BigDecimal afterAccumulationFundCompanySum;
	private BigDecimal afterAccumulationFundEmployeeSum;
	private Long creatorUid;
	private Long createTime;
	private Long fileUid;
	private Long fileTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public Integer getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(Integer employeeCount) {
		this.employeeCount = employeeCount;
	}

	public BigDecimal getSocialSecuritySum() {
		return socialSecuritySum;
	}

	public void setSocialSecuritySum(BigDecimal socialSecuritySum) {
		this.socialSecuritySum = socialSecuritySum;
	}

	public BigDecimal getSocialSecurityCompanySum() {
		return socialSecurityCompanySum;
	}

	public void setSocialSecurityCompanySum(BigDecimal socialSecurityCompanySum) {
		this.socialSecurityCompanySum = socialSecurityCompanySum;
	}

	public BigDecimal getSocialSecurityEmployeeSum() {
		return socialSecurityEmployeeSum;
	}

	public void setSocialSecurityEmployeeSum(BigDecimal socialSecurityEmployeeSum) {
		this.socialSecurityEmployeeSum = socialSecurityEmployeeSum;
	}

	public BigDecimal getPensionCompanySum() {
		return pensionCompanySum;
	}

	public void setPensionCompanySum(BigDecimal pensionCompanySum) {
		this.pensionCompanySum = pensionCompanySum;
	}

	public BigDecimal getPensionEmployeeSum() {
		return pensionEmployeeSum;
	}

	public void setPensionEmployeeSum(BigDecimal pensionEmployeeSum) {
		this.pensionEmployeeSum = pensionEmployeeSum;
	}

	public BigDecimal getMedicalCompanySum() {
		return medicalCompanySum;
	}

	public void setMedicalCompanySum(BigDecimal medicalCompanySum) {
		this.medicalCompanySum = medicalCompanySum;
	}

	public BigDecimal getMedicalEmployeeSum() {
		return medicalEmployeeSum;
	}

	public void setMedicalEmployeeSum(BigDecimal medicalEmployeeSum) {
		this.medicalEmployeeSum = medicalEmployeeSum;
	}

	public BigDecimal getInjuryCompanySum() {
		return injuryCompanySum;
	}

	public void setInjuryCompanySum(BigDecimal injuryCompanySum) {
		this.injuryCompanySum = injuryCompanySum;
	}

	public BigDecimal getInjuryEmployeeSum() {
		return injuryEmployeeSum;
	}

	public void setInjuryEmployeeSum(BigDecimal injuryEmployeeSum) {
		this.injuryEmployeeSum = injuryEmployeeSum;
	}

	public BigDecimal getUnemploymentCompanySum() {
		return unemploymentCompanySum;
	}

	public void setUnemploymentCompanySum(BigDecimal unemploymentCompanySum) {
		this.unemploymentCompanySum = unemploymentCompanySum;
	}

	public BigDecimal getUnemploymentEmployeeSum() {
		return unemploymentEmployeeSum;
	}

	public void setUnemploymentEmployeeSum(BigDecimal unemploymentEmployeeSum) {
		this.unemploymentEmployeeSum = unemploymentEmployeeSum;
	}

	public BigDecimal getBirthCompanySum() {
		return birthCompanySum;
	}

	public void setBirthCompanySum(BigDecimal birthCompanySum) {
		this.birthCompanySum = birthCompanySum;
	}

	public BigDecimal getBirthEmployeeSum() {
		return birthEmployeeSum;
	}

	public void setBirthEmployeeSum(BigDecimal birthEmployeeSum) {
		this.birthEmployeeSum = birthEmployeeSum;
	}

	public BigDecimal getCriticalIllnessCompanySum() {
		return criticalIllnessCompanySum;
	}

	public void setCriticalIllnessCompanySum(BigDecimal criticalIllnessCompanySum) {
		this.criticalIllnessCompanySum = criticalIllnessCompanySum;
	}

	public BigDecimal getCriticalIllnessEmployeeSum() {
		return criticalIllnessEmployeeSum;
	}

	public void setCriticalIllnessEmployeeSum(BigDecimal criticalIllnessEmployeeSum) {
		this.criticalIllnessEmployeeSum = criticalIllnessEmployeeSum;
	}

	public BigDecimal getAfterSocialSecurityCompanySum() {
		return afterSocialSecurityCompanySum;
	}

	public void setAfterSocialSecurityCompanySum(BigDecimal afterSocialSecurityCompanySum) {
		this.afterSocialSecurityCompanySum = afterSocialSecurityCompanySum;
	}

	public BigDecimal getAfterSocialSecurityEmployeeSum() {
		return afterSocialSecurityEmployeeSum;
	}

	public void setAfterSocialSecurityEmployeeSum(BigDecimal afterSocialSecurityEmployeeSum) {
		this.afterSocialSecurityEmployeeSum = afterSocialSecurityEmployeeSum;
	}

	public BigDecimal getAfterPensionCompanySum() {
		return afterPensionCompanySum;
	}

	public void setAfterPensionCompanySum(BigDecimal afterPensionCompanySum) {
		this.afterPensionCompanySum = afterPensionCompanySum;
	}

	public BigDecimal getAfterPensionEmployeeSum() {
		return afterPensionEmployeeSum;
	}

	public void setAfterPensionEmployeeSum(BigDecimal afterPensionEmployeeSum) {
		this.afterPensionEmployeeSum = afterPensionEmployeeSum;
	}

	public BigDecimal getAfterMedicalCompanySum() {
		return afterMedicalCompanySum;
	}

	public void setAfterMedicalCompanySum(BigDecimal afterMedicalCompanySum) {
		this.afterMedicalCompanySum = afterMedicalCompanySum;
	}

	public BigDecimal getAfterMedicalEmployeeSum() {
		return afterMedicalEmployeeSum;
	}

	public void setAfterMedicalEmployeeSum(BigDecimal afterMedicalEmployeeSum) {
		this.afterMedicalEmployeeSum = afterMedicalEmployeeSum;
	}

	public BigDecimal getAfterInjuryCompanySum() {
		return afterInjuryCompanySum;
	}

	public void setAfterInjuryCompanySum(BigDecimal afterInjuryCompanySum) {
		this.afterInjuryCompanySum = afterInjuryCompanySum;
	}

	public BigDecimal getAfterInjuryEmployeeSum() {
		return afterInjuryEmployeeSum;
	}

	public void setAfterInjuryEmployeeSum(BigDecimal afterInjuryEmployeeSum) {
		this.afterInjuryEmployeeSum = afterInjuryEmployeeSum;
	}

	public BigDecimal getAfterUnemploymentCompanySum() {
		return afterUnemploymentCompanySum;
	}

	public void setAfterUnemploymentCompanySum(BigDecimal afterUnemploymentCompanySum) {
		this.afterUnemploymentCompanySum = afterUnemploymentCompanySum;
	}

	public BigDecimal getAfterUnemploymentEmployeeSum() {
		return afterUnemploymentEmployeeSum;
	}

	public void setAfterUnemploymentEmployeeSum(BigDecimal afterUnemploymentEmployeeSum) {
		this.afterUnemploymentEmployeeSum = afterUnemploymentEmployeeSum;
	}

	public BigDecimal getAfterBirthCompanySum() {
		return afterBirthCompanySum;
	}

	public void setAfterBirthCompanySum(BigDecimal afterBirthCompanySum) {
		this.afterBirthCompanySum = afterBirthCompanySum;
	}

	public BigDecimal getAfterBirthEmployeeSum() {
		return afterBirthEmployeeSum;
	}

	public void setAfterBirthEmployeeSum(BigDecimal afterBirthEmployeeSum) {
		this.afterBirthEmployeeSum = afterBirthEmployeeSum;
	}

	public BigDecimal getAfterCriticalIllnessCompanySum() {
		return afterCriticalIllnessCompanySum;
	}

	public void setAfterCriticalIllnessCompanySum(BigDecimal afterCriticalIllnessCompanySum) {
		this.afterCriticalIllnessCompanySum = afterCriticalIllnessCompanySum;
	}

	public BigDecimal getAfterCriticalIllnessEmployeeSum() {
		return afterCriticalIllnessEmployeeSum;
	}

	public void setAfterCriticalIllnessEmployeeSum(BigDecimal afterCriticalIllnessEmployeeSum) {
		this.afterCriticalIllnessEmployeeSum = afterCriticalIllnessEmployeeSum;
	}

	public BigDecimal getDisabilitySum() {
		return disabilitySum;
	}

	public void setDisabilitySum(BigDecimal disabilitySum) {
		this.disabilitySum = disabilitySum;
	}

	public BigDecimal getCommercialInsurance() {
		return commercialInsurance;
	}

	public void setCommercialInsurance(BigDecimal commercialInsurance) {
		this.commercialInsurance = commercialInsurance;
	}

	public BigDecimal getAccumulationFundSum() {
		return accumulationFundSum;
	}

	public void setAccumulationFundSum(BigDecimal accumulationFundSum) {
		this.accumulationFundSum = accumulationFundSum;
	}

	public BigDecimal getAccumulationFundCompanySum() {
		return accumulationFundCompanySum;
	}

	public void setAccumulationFundCompanySum(BigDecimal accumulationFundCompanySum) {
		this.accumulationFundCompanySum = accumulationFundCompanySum;
	}

	public BigDecimal getAccumulationFundEmployeeSum() {
		return accumulationFundEmployeeSum;
	}

	public void setAccumulationFundEmployeeSum(BigDecimal accumulationFundEmployeeSum) {
		this.accumulationFundEmployeeSum = accumulationFundEmployeeSum;
	}

	public BigDecimal getAfterAccumulationFundCompanySum() {
		return afterAccumulationFundCompanySum;
	}

	public void setAfterAccumulationFundCompanySum(BigDecimal afterAccumulationFundCompanySum) {
		this.afterAccumulationFundCompanySum = afterAccumulationFundCompanySum;
	}

	public BigDecimal getAfterAccumulationFundEmployeeSum() {
		return afterAccumulationFundEmployeeSum;
	}

	public void setAfterAccumulationFundEmployeeSum(BigDecimal afterAccumulationFundEmployeeSum) {
		this.afterAccumulationFundEmployeeSum = afterAccumulationFundEmployeeSum;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getFileUid() {
		return fileUid;
	}

	public void setFileUid(Long fileUid) {
		this.fileUid = fileUid;
	}

	public Long getFileTime() {
		return fileTime;
	}

	public void setFileTime(Long fileTime) {
		this.fileTime = fileTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
