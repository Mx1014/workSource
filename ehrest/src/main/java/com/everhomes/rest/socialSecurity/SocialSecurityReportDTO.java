package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 返回值:
 * 
 * <li>userName:姓名</li>
 * <li>entryDate:入职日期</li>
 * <li>contactToken:手机号</li>
 * <li>idNumber:身份证号</li>
 * <li>degree:学历</li>
 * <li>salaryCardBank:开户行</li>
 * <li>salaryCardNumber:工资卡号</li>
 * <li>deptName:部门</li>
 * <li>socialSecurityNumber:社保号</li>
 * <li>providentFundNumber:公积金号</li>
 * <li>outWorkDate:离职日期</li>
 * <li>householdType:户籍类型</li>
 * <li>socialSecurityCityId:参保城市id</li>
 * <li>socialSecurityCityName:参保城市</li>
 * <li>payMonth:社保月份</li>
 * <li>socialSecurityRadix:社保基数</li>
 * <li>socialSecuritySum:社保合计</li>
 * <li>socialSecurityCompanySum:社保企业合计</li>
 * <li>socialSecurityEmployeeSum:社保个人合计</li>
 * <li>accumulationFundCityId:公积金城市id</li>
 * <li>accumulationFundCityName:公积金城市</li>
 * <li>accumulationFundRadix:公积金基数</li>
 * <li>accumulationFundCompanyRadix:公积金企业基数</li>
 * <li>accumulationFundCompanyRatio:公积金企业比例万分之eq:100=1%:1=0.01%</li>
 * <li>accumulationFundEmployeeRadix:公积金个人基数</li>
 * <li>accumulationFundEmployeeRatio:公积金个人比例万分之eq:100=1%:1=0.01%</li>
 * <li>accumulationFundSum:公积金合计</li>
 * <li>accumulationFundCompanySum:公积金企业合计</li>
 * <li>accumulationFundEmployeeSum:公积金个人合计</li>
 * <li>pensionCompanyRadix:养老保险企业基数</li>
 * <li>pensionCompanyRatio:养老保险企业比例万分之eq:100=1%:1=0.01%</li>
 * <li>pensionEmployeeRadix:养老保险个人基数</li>
 * <li>pensionEmployeeRatio:养老保险个人比例万分之eq:100=1%:1=0.01%</li>
 * <li>pensionCompanySum:养老保险企业合计</li>
 * <li>pensionEmployeeSum:养老保险个人合计</li>
 * <li>medicalCompanyRadix:医疗保险企业基数</li>
 * <li>medicalCompanyRatio:医疗保险企业比例万分之eq:100=1%:1=0.01%</li>
 * <li>medicalEmployeeRadix:医疗保险个人基数</li>
 * <li>medicalEmployeeRatio:医疗保险个人比例万分之eq:100=1%:1=0.01%</li>
 * <li>medicalCompanySum:医疗保险企业合计</li>
 * <li>medicalEmployeeSum:医疗保险个人合计</li>
 * <li>injuryCompanyRadix:工伤保险企业基数</li>
 * <li>injuryCompanyRatio:工伤保险企业比例万分之eq:100=1%:1=0.01%</li>
 * <li>injuryEmployeeRadix:工伤保险个人基数</li>
 * <li>injuryEmployeeRatio:工伤保险个人比例万分之eq:100=1%:1=0.01%</li>
 * <li>injuryCompanySum:工伤保险企业合计</li>
 * <li>injuryEmployeeSum:工伤保险个人合计</li>
 * <li>unemploymentCompanyRadix:失业保险企业基数</li>
 * <li>unemploymentCompanyRatio:失业保险企业比例万分之eq:100=1%:1=0.01%</li>
 * <li>unemploymentEmployeeRadix:失业保险个人基数</li>
 * <li>unemploymentEmployeeRatio:失业保险个人比例万分之eq:100=1%:1=0.01%</li>
 * <li>unemploymentCompanySum:失业保险企业合计</li>
 * <li>unemploymentEmployeeSum:失业保险个人合计</li>
 * <li>birthCompanyRadix:生育保险企业基数</li>
 * <li>birthCompanyRatio:生育保险企业比例万分之eq:100=1%:1=0.01%</li>
 * <li>birthEmployeeRadix:生育保险个人基数</li>
 * <li>birthEmployeeRatio:生育保险个人比例万分之eq:100=1%:1=0.01%</li>
 * <li>birthCompanySum:生育保险企业合计</li>
 * <li>birthEmployeeSum:生育保险个人合计</li>
 * <li>criticalIllnessCompanyRadix:大病保险企业基数</li>
 * <li>criticalIllnessCompanyRatio:大病保险企业比例万分之eq:100=1%:1=0.01%</li>
 * <li>criticalIllnessEmployeeRadix:大病保险个人基数</li>
 * <li>criticalIllnessEmployeeRatio:大病保险个人比例万分之eq:100=1%:1=0.01%</li>
 * <li>criticalIllnessCompanySum:大病保险企业合计</li>
 * <li>criticalIllnessEmployeeSum:大病保险个人合计</li>
 * <li>afterSocialSecurityCompanySum:补缴社保企业合计</li>
 * <li>afterSocialSecurityEmployeeSum:补缴社保个人合计</li>
 * <li>afterPensionCompanySum:补缴养老企业合计</li>
 * <li>afterPensionEmployeeSum:补缴养老个人合计</li>
 * <li>afterMedicalCompanySum:补缴医疗企业合计</li>
 * <li>afterMedicalEmployeeSum:补缴医疗个人合计</li>
 * <li>afterInjuryCompanySum:补缴工伤企业合计</li>
 * <li>afterInjuryEmployeeSum:补缴工伤个人合计</li>
 * <li>afterUnemploymentCompanySum:补缴失业企业合计</li>
 * <li>afterUnemploymentEmployeeSum:补缴失业个人合计</li>
 * <li>afterBirthCompanySum:补缴生育企业合计</li>
 * <li>afterBirthEmployeeSum:补缴生育个人合计</li>
 * <li>afterCriticalIllnessCompanySum:补缴大病企业合计</li>
 * <li>afterCriticalIllnessEmployeeSum:补缴大病个人合计</li>
 * <li>disabilitySum:残障金</li>
 * <li>commercialInsurance:商业保险</li>
 * <li>fileUid:归档人</li>
 * <li>fileTime:归档时间</li>
 * </ul>
 */
public class SocialSecurityReportDTO {
	private Long id;
	private Integer namespaceId;
	private Long organizationId;
	private Long userId;
	private Long detailId;
	private String userName;
	private Long entryDate;
	private String contactToken;
	private String idNumber;
	private String degree;
	private String salaryCardBank;
	private String salaryCardNumber;
	private String deptName;
	private String socialSecurityNumber;
	private String providentFundNumber;
	private Long outWorkDate;
	private String householdType;
	private Long socialSecurityCityId;
	private String socialSecurityCityName;
	private String payMonth;
	private BigDecimal socialSecurityRadix;
	private BigDecimal socialSecuritySum;
	private BigDecimal socialSecurityCompanySum;
	private BigDecimal socialSecurityEmployeeSum;
	private Long accumulationFundCityId;
	private String accumulationFundCityName;
	private BigDecimal accumulationFundRadix;
	private BigDecimal accumulationFundCompanyRadix;
	private Integer accumulationFundCompanyRatio;
	private BigDecimal accumulationFundEmployeeRadix;
	private Integer accumulationFundEmployeeRatio;
	private BigDecimal accumulationFundSum;
	private BigDecimal accumulationFundCompanySum;
	private BigDecimal accumulationFundEmployeeSum;
	private BigDecimal pensionCompanyRadix;
	private Integer pensionCompanyRatio;
	private BigDecimal pensionEmployeeRadix;
	private Integer pensionEmployeeRatio;
	private BigDecimal pensionCompanySum;
	private BigDecimal pensionEmployeeSum;
	private BigDecimal medicalCompanyRadix;
	private Integer medicalCompanyRatio;
	private BigDecimal medicalEmployeeRadix;
	private Integer medicalEmployeeRatio;
	private BigDecimal medicalCompanySum;
	private BigDecimal medicalEmployeeSum;
	private BigDecimal injuryCompanyRadix;
	private Integer injuryCompanyRatio;
	private BigDecimal injuryEmployeeRadix;
	private Integer injuryEmployeeRatio;
	private BigDecimal injuryCompanySum;
	private BigDecimal injuryEmployeeSum;
	private BigDecimal unemploymentCompanyRadix;
	private Integer unemploymentCompanyRatio;
	private BigDecimal unemploymentEmployeeRadix;
	private Integer unemploymentEmployeeRatio;
	private BigDecimal unemploymentCompanySum;
	private BigDecimal unemploymentEmployeeSum;
	private BigDecimal birthCompanyRadix;
	private Integer birthCompanyRatio;
	private BigDecimal birthEmployeeRadix;
	private Integer birthEmployeeRatio;
	private BigDecimal birthCompanySum;
	private BigDecimal birthEmployeeSum;
	private BigDecimal criticalIllnessCompanyRadix;
	private Integer criticalIllnessCompanyRatio;
	private BigDecimal criticalIllnessEmployeeRadix;
	private Integer criticalIllnessEmployeeRatio;
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
	private Long fileUid;
	private Long fileTime;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Long entryDate) {
		this.entryDate = entryDate;
	}

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getSalaryCardBank() {
		return salaryCardBank;
	}

	public void setSalaryCardBank(String salaryCardBank) {
		this.salaryCardBank = salaryCardBank;
	}

	public String getSalaryCardNumber() {
		return salaryCardNumber;
	}

	public void setSalaryCardNumber(String salaryCardNumber) {
		this.salaryCardNumber = salaryCardNumber;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getProvidentFundNumber() {
		return providentFundNumber;
	}

	public void setProvidentFundNumber(String providentFundNumber) {
		this.providentFundNumber = providentFundNumber;
	}

	public Long getOutWorkDate() {
		return outWorkDate;
	}

	public void setOutWorkDate(Long outWorkDate) {
		this.outWorkDate = outWorkDate;
	}

	public String getHouseholdType() {
		return householdType;
	}

	public void setHouseholdType(String householdType) {
		this.householdType = householdType;
	}

	public Long getSocialSecurityCityId() {
		return socialSecurityCityId;
	}

	public void setSocialSecurityCityId(Long socialSecurityCityId) {
		this.socialSecurityCityId = socialSecurityCityId;
	}

	public String getSocialSecurityCityName() {
		return socialSecurityCityName;
	}

	public void setSocialSecurityCityName(String socialSecurityCityName) {
		this.socialSecurityCityName = socialSecurityCityName;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public BigDecimal getSocialSecurityRadix() {
		return socialSecurityRadix;
	}

	public void setSocialSecurityRadix(BigDecimal socialSecurityRadix) {
		this.socialSecurityRadix = socialSecurityRadix;
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

	public Long getAccumulationFundCityId() {
		return accumulationFundCityId;
	}

	public void setAccumulationFundCityId(Long accumulationFundCityId) {
		this.accumulationFundCityId = accumulationFundCityId;
	}

	public String getAccumulationFundCityName() {
		return accumulationFundCityName;
	}

	public void setAccumulationFundCityName(String accumulationFundCityName) {
		this.accumulationFundCityName = accumulationFundCityName;
	}

	public BigDecimal getAccumulationFundRadix() {
		return accumulationFundRadix;
	}

	public void setAccumulationFundRadix(BigDecimal accumulationFundRadix) {
		this.accumulationFundRadix = accumulationFundRadix;
	}

	public BigDecimal getAccumulationFundCompanyRadix() {
		return accumulationFundCompanyRadix;
	}

	public void setAccumulationFundCompanyRadix(BigDecimal accumulationFundCompanyRadix) {
		this.accumulationFundCompanyRadix = accumulationFundCompanyRadix;
	}

	public Integer getAccumulationFundCompanyRatio() {
		return accumulationFundCompanyRatio;
	}

	public void setAccumulationFundCompanyRatio(Integer accumulationFundCompanyRatio) {
		this.accumulationFundCompanyRatio = accumulationFundCompanyRatio;
	}

	public BigDecimal getAccumulationFundEmployeeRadix() {
		return accumulationFundEmployeeRadix;
	}

	public void setAccumulationFundEmployeeRadix(BigDecimal accumulationFundEmployeeRadix) {
		this.accumulationFundEmployeeRadix = accumulationFundEmployeeRadix;
	}

	public Integer getAccumulationFundEmployeeRatio() {
		return accumulationFundEmployeeRatio;
	}

	public void setAccumulationFundEmployeeRatio(Integer accumulationFundEmployeeRatio) {
		this.accumulationFundEmployeeRatio = accumulationFundEmployeeRatio;
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

	public BigDecimal getPensionCompanyRadix() {
		return pensionCompanyRadix;
	}

	public void setPensionCompanyRadix(BigDecimal pensionCompanyRadix) {
		this.pensionCompanyRadix = pensionCompanyRadix;
	}

	public Integer getPensionCompanyRatio() {
		return pensionCompanyRatio;
	}

	public void setPensionCompanyRatio(Integer pensionCompanyRatio) {
		this.pensionCompanyRatio = pensionCompanyRatio;
	}

	public BigDecimal getPensionEmployeeRadix() {
		return pensionEmployeeRadix;
	}

	public void setPensionEmployeeRadix(BigDecimal pensionEmployeeRadix) {
		this.pensionEmployeeRadix = pensionEmployeeRadix;
	}

	public Integer getPensionEmployeeRatio() {
		return pensionEmployeeRatio;
	}

	public void setPensionEmployeeRatio(Integer pensionEmployeeRatio) {
		this.pensionEmployeeRatio = pensionEmployeeRatio;
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

	public BigDecimal getMedicalCompanyRadix() {
		return medicalCompanyRadix;
	}

	public void setMedicalCompanyRadix(BigDecimal medicalCompanyRadix) {
		this.medicalCompanyRadix = medicalCompanyRadix;
	}

	public Integer getMedicalCompanyRatio() {
		return medicalCompanyRatio;
	}

	public void setMedicalCompanyRatio(Integer medicalCompanyRatio) {
		this.medicalCompanyRatio = medicalCompanyRatio;
	}

	public BigDecimal getMedicalEmployeeRadix() {
		return medicalEmployeeRadix;
	}

	public void setMedicalEmployeeRadix(BigDecimal medicalEmployeeRadix) {
		this.medicalEmployeeRadix = medicalEmployeeRadix;
	}

	public Integer getMedicalEmployeeRatio() {
		return medicalEmployeeRatio;
	}

	public void setMedicalEmployeeRatio(Integer medicalEmployeeRatio) {
		this.medicalEmployeeRatio = medicalEmployeeRatio;
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

	public BigDecimal getInjuryCompanyRadix() {
		return injuryCompanyRadix;
	}

	public void setInjuryCompanyRadix(BigDecimal injuryCompanyRadix) {
		this.injuryCompanyRadix = injuryCompanyRadix;
	}

	public Integer getInjuryCompanyRatio() {
		return injuryCompanyRatio;
	}

	public void setInjuryCompanyRatio(Integer injuryCompanyRatio) {
		this.injuryCompanyRatio = injuryCompanyRatio;
	}

	public BigDecimal getInjuryEmployeeRadix() {
		return injuryEmployeeRadix;
	}

	public void setInjuryEmployeeRadix(BigDecimal injuryEmployeeRadix) {
		this.injuryEmployeeRadix = injuryEmployeeRadix;
	}

	public Integer getInjuryEmployeeRatio() {
		return injuryEmployeeRatio;
	}

	public void setInjuryEmployeeRatio(Integer injuryEmployeeRatio) {
		this.injuryEmployeeRatio = injuryEmployeeRatio;
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

	public BigDecimal getUnemploymentCompanyRadix() {
		return unemploymentCompanyRadix;
	}

	public void setUnemploymentCompanyRadix(BigDecimal unemploymentCompanyRadix) {
		this.unemploymentCompanyRadix = unemploymentCompanyRadix;
	}

	public Integer getUnemploymentCompanyRatio() {
		return unemploymentCompanyRatio;
	}

	public void setUnemploymentCompanyRatio(Integer unemploymentCompanyRatio) {
		this.unemploymentCompanyRatio = unemploymentCompanyRatio;
	}

	public BigDecimal getUnemploymentEmployeeRadix() {
		return unemploymentEmployeeRadix;
	}

	public void setUnemploymentEmployeeRadix(BigDecimal unemploymentEmployeeRadix) {
		this.unemploymentEmployeeRadix = unemploymentEmployeeRadix;
	}

	public Integer getUnemploymentEmployeeRatio() {
		return unemploymentEmployeeRatio;
	}

	public void setUnemploymentEmployeeRatio(Integer unemploymentEmployeeRatio) {
		this.unemploymentEmployeeRatio = unemploymentEmployeeRatio;
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

	public BigDecimal getBirthCompanyRadix() {
		return birthCompanyRadix;
	}

	public void setBirthCompanyRadix(BigDecimal birthCompanyRadix) {
		this.birthCompanyRadix = birthCompanyRadix;
	}

	public Integer getBirthCompanyRatio() {
		return birthCompanyRatio;
	}

	public void setBirthCompanyRatio(Integer birthCompanyRatio) {
		this.birthCompanyRatio = birthCompanyRatio;
	}

	public BigDecimal getBirthEmployeeRadix() {
		return birthEmployeeRadix;
	}

	public void setBirthEmployeeRadix(BigDecimal birthEmployeeRadix) {
		this.birthEmployeeRadix = birthEmployeeRadix;
	}

	public Integer getBirthEmployeeRatio() {
		return birthEmployeeRatio;
	}

	public void setBirthEmployeeRatio(Integer birthEmployeeRatio) {
		this.birthEmployeeRatio = birthEmployeeRatio;
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

	public BigDecimal getCriticalIllnessCompanyRadix() {
		return criticalIllnessCompanyRadix;
	}

	public void setCriticalIllnessCompanyRadix(BigDecimal criticalIllnessCompanyRadix) {
		this.criticalIllnessCompanyRadix = criticalIllnessCompanyRadix;
	}

	public Integer getCriticalIllnessCompanyRatio() {
		return criticalIllnessCompanyRatio;
	}

	public void setCriticalIllnessCompanyRatio(Integer criticalIllnessCompanyRatio) {
		this.criticalIllnessCompanyRatio = criticalIllnessCompanyRatio;
	}

	public BigDecimal getCriticalIllnessEmployeeRadix() {
		return criticalIllnessEmployeeRadix;
	}

	public void setCriticalIllnessEmployeeRadix(BigDecimal criticalIllnessEmployeeRadix) {
		this.criticalIllnessEmployeeRadix = criticalIllnessEmployeeRadix;
	}

	public Integer getCriticalIllnessEmployeeRatio() {
		return criticalIllnessEmployeeRatio;
	}

	public void setCriticalIllnessEmployeeRatio(Integer criticalIllnessEmployeeRatio) {
		this.criticalIllnessEmployeeRatio = criticalIllnessEmployeeRatio;
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

}
