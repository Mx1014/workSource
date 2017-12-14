package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
*<li>  userName;               姓名             </li>         
*<li>  entryDate;              入职日期           </li>         
*<li>  outWorkDate;            离职日期           </li>         
*<li> contactToken;            手机号          </li>         
*<li> idNumber;                身份证号        </li>         
*<li> degree;                  学历            </li>         
*<li> salaryCardBank;          开户行          </li>         
*<li> salaryCardNumber;        工资卡号        </li>         
*<li> deptName;                部门            </li>         
*<li> socialSecurityNumber;    社保号          </li>         
*<li> providentFundNumber;     公积金号        </li>         
*<li> householdType;           户籍类型        </li>         
*<li>  socialSecurityCityId;   参保城市id         </li>         
*<li> socialSecurityCityName;  参保城市        </li>         
*<li> payMonth;                社保月份        </li>         
*<li> socialSecurityRadix;     社保基数       </li>         
*<li> socialSecurityIncrease;  社保增         </li>         
*<li> socialSecurityDecrease;  社保减         </li>         
*<li> socialSecurityAfter;     社保补缴       </li>         
*<li>  accumulationFundCityId; 公积金城市id    </li>         
*<li>  accumulationFundCityName公积金城市  ;    </li>         
*<li> accumulationFundRadix;   公积金基数     </li>         
*<li> accumulationFundIncrease;公积金增       </li>         
*<li> accumulationFundDecrease;公积金减       </li>         
*<li> accumulationFundAfter;   公积金补缴     </li>         
*<li> fileUid;                 归档人           </li>         
*<li> fileTime;                归档时间        </li>  
 * </ul>
 */
public class SocialSecurityInoutReportDTO {
    private Long id;
    private Integer namespaceId;
    private Long organizationId;
    private Long userId;
    private Long detailId;
    private String userName;
    private Long entryDate;
    private Long outWorkDate;
    private String contactToken;
    private String idNumber;
    private String degree;
    private String salaryCardBank;
    private String salaryCardNumber;
    private String deptName;
    private String socialSecurityNumber;
    private String providentFundNumber;
    private String householdType;
    private Long socialSecurityCityId;
    private String socialSecurityCityName;
    private String payMonth;
    private BigDecimal socialSecurityRadix;
    private BigDecimal socialSecurityIncrease;
    private BigDecimal socialSecurityDecrease;
    private BigDecimal socialSecurityAfter;
    private Long accumulationFundCityId;
    private String accumulationFundCityName;
    private BigDecimal accumulationFundRadix;
    private BigDecimal accumulationFundIncrease;
    private BigDecimal accumulationFundDecrease;
    private BigDecimal accumulationFundAfter;
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
	public Long getOutWorkDate() {
		return outWorkDate;
	}
	public void setOutWorkDate(Long outWorkDate) {
		this.outWorkDate = outWorkDate;
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
	public BigDecimal getSocialSecurityIncrease() {
		return socialSecurityIncrease;
	}
	public void setSocialSecurityIncrease(BigDecimal socialSecurityIncrease) {
		this.socialSecurityIncrease = socialSecurityIncrease;
	}
	public BigDecimal getSocialSecurityDecrease() {
		return socialSecurityDecrease;
	}
	public void setSocialSecurityDecrease(BigDecimal socialSecurityDecrease) {
		this.socialSecurityDecrease = socialSecurityDecrease;
	}
	public BigDecimal getSocialSecurityAfter() {
		return socialSecurityAfter;
	}
	public void setSocialSecurityAfter(BigDecimal socialSecurityAfter) {
		this.socialSecurityAfter = socialSecurityAfter;
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
	public BigDecimal getAccumulationFundIncrease() {
		return accumulationFundIncrease;
	}
	public void setAccumulationFundIncrease(BigDecimal accumulationFundIncrease) {
		this.accumulationFundIncrease = accumulationFundIncrease;
	}
	public BigDecimal getAccumulationFundDecrease() {
		return accumulationFundDecrease;
	}
	public void setAccumulationFundDecrease(BigDecimal accumulationFundDecrease) {
		this.accumulationFundDecrease = accumulationFundDecrease;
	}
	public BigDecimal getAccumulationFundAfter() {
		return accumulationFundAfter;
	}
	public void setAccumulationFundAfter(BigDecimal accumulationFundAfter) {
		this.accumulationFundAfter = accumulationFundAfter;
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
