package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>社保/公积金 具体项:
 * <li>payItem: 缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗</li>
 * <li>companyRadix: 企业基数</li>
 * <li>companyRatio: 企业比例</li>
 * <li>companyRadixMin: 企业基数最小值</li>
 * <li>companyRadixMax: 企业基数最大值(最小等于最大的时候不可更改)</li>
 * <li>companyRatioMin: 企业比例最小值</li>
 * <li>companyRatioMax: 企业比例最大值(最小等于最大的时候不可更改)</li>
 * <li>employeeRadix: 个人基数</li>
 * <li>employeeRatio: 个人比例</li>
 * <li>employeeRadixMin: 个人基数最小值</li>
 * <li>employeeRadixMax: 个人基数最大值(最小等于最大的时候不可更改)</li>
 * <li>employeeRatioMin: 个人比例最小值</li>
 * <li>employeeRatioMax: 个人比例最大值(最小等于最大的时候不可更改)</li>
 * </ul>
 */
public class SocialSecurityItemDTO {

	private String payItem; 
	private BigDecimal companyRadix;
	private Integer companyRatio;
	private BigDecimal employeeRadix;
	private Integer employeeRatio;
    private BigDecimal companyRadixMin;
    private BigDecimal companyRadixMax;
    private Integer companyRatioMin;
    private Integer companyRatioMax;
    private BigDecimal employeeRadixMin;
    private BigDecimal employeeRadixMax;
    private Integer employeeRatioMin;
    private Integer employeeRatioMax;
	private Byte editableFlag;
	private Byte isDefault;
	private Byte isNew;
	private Byte isWork;
	public String getPayItem() {
		return payItem;
	}
	public void setPayItem(String payItem) {
		this.payItem = payItem;
	}
	public BigDecimal getCompanyRadix() {
		return companyRadix;
	}
	public void setCompanyRadix(BigDecimal companyRadix) {
		this.companyRadix = companyRadix;
	}
	public Integer getCompanyRatio() {
		return companyRatio;
	}
	public void setCompanyRatio(Integer companyRatio) {
		this.companyRatio = companyRatio;
	}
	public BigDecimal getEmployeeRadix() {
		return employeeRadix;
	}
	public void setEmployeeRadix(BigDecimal employeeRadix) {
		this.employeeRadix = employeeRadix;
	}
	public Integer getEmployeeRatio() {
		return employeeRatio;
	}
	public void setEmployeeRatio(Integer employeeRatio) {
		this.employeeRatio = employeeRatio;
	}
	public BigDecimal getCompanyRadixMin() {
		return companyRadixMin;
	}
	public void setCompanyRadixMin(BigDecimal companyRadixMin) {
		this.companyRadixMin = companyRadixMin;
	}
	public BigDecimal getCompanyRadixMax() {
		return companyRadixMax;
	}
	public void setCompanyRadixMax(BigDecimal companyRadixMax) {
		this.companyRadixMax = companyRadixMax;
	}
	public Integer getCompanyRatioMin() {
		return companyRatioMin;
	}
	public void setCompanyRatioMin(Integer companyRatioMin) {
		this.companyRatioMin = companyRatioMin;
	}
	public Integer getCompanyRatioMax() {
		return companyRatioMax;
	}
	public void setCompanyRatioMax(Integer companyRatioMax) {
		this.companyRatioMax = companyRatioMax;
	}
	public BigDecimal getEmployeeRadixMin() {
		return employeeRadixMin;
	}
	public void setEmployeeRadixMin(BigDecimal employeeRadixMin) {
		this.employeeRadixMin = employeeRadixMin;
	}
	public BigDecimal getEmployeeRadixMax() {
		return employeeRadixMax;
	}
	public void setEmployeeRadixMax(BigDecimal employeeRadixMax) {
		this.employeeRadixMax = employeeRadixMax;
	}
	public Integer getEmployeeRatioMin() {
		return employeeRatioMin;
	}
	public void setEmployeeRatioMin(Integer employeeRatioMin) {
		this.employeeRatioMin = employeeRatioMin;
	}
	public Integer getEmployeeRatioMax() {
		return employeeRatioMax;
	}
	public void setEmployeeRatioMax(Integer employeeRatioMax) {
		this.employeeRatioMax = employeeRatioMax;
	}
	public Byte getEditableFlag() {
		return editableFlag;
	}
	public void setEditableFlag(Byte editableFlag) {
		this.editableFlag = editableFlag;
	}
	public Byte getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}
	public Byte getIsNew() {
		return isNew;
	}
	public void setIsNew(Byte isNew) {
		this.isNew = isNew;
	}
	public Byte getIsWork() {
		return isWork;
	}
	public void setIsWork(Byte isWork) {
		this.isWork = isWork;
	}
	

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}	
}
