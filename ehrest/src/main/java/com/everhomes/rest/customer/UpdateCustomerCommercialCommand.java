package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>enterpriseTypeItemId：企业类型</li>
 *     <li>shareTypeItemId：控股情况</li>
 *     <li>contact：联系人</li>
 *     <li>contactNumber：联系人电话</li>
 *     <li>unifiedSocialCreditCode：统一社会信用代码</li>
 *     <li>businessScope：主营业务</li>
 *     <li>foundationDate：成立日期</li>
 *     <li>taxRegistrationDate：税务登记日期</li>
 *     <li>validityBeginDate：有效开始日期</li>
 *     <li>validityEndDate：有效截止日期</li>
 *     <li>registeredAddr：注册地址</li>
 *     <li>registeredCapital：注册资金</li>
 *     <li>paidupApital：实到资金</li>
 *     <li>propertyType：产权性质</li>
 *     <li>changeDate：变更日期</li>
 *     <li>businessLicenceDate：出营业执照日期</li>
 *     <li>liquidationCommitteeRecoredDate：清算组备案日期</li>
 *     <li>cancelDate：注销日期</li>
 * </ul>
 * Created by ying.xiong on 2017/8/19.
 */
public class UpdateCustomerCommercialCommand {
    private Long id;
    private Byte customerType;
    private Long customerId;
    private Long enterpriseTypeItemId;
    private Long shareTypeItemId;
    private String contact;
    private String contactNumber;
    private String unifiedSocialCreditCode;
    private String businessScope;
    private Long foundationDate;
    private Long taxRegistrationDate;
    private Long validityBeginDate;
    private Long validityEndDate;
    private String registeredAddr;
    private BigDecimal registeredCapital;
    private BigDecimal paidupApital;
    private Long propertyType;
    private Long changeDate;
    private Long businessLicenceDate;
    private Long liquidationCommitteeRecoredDate;
    private Long cancelDate;

    public Long getBusinessLicenceDate() {
        return businessLicenceDate;
    }

    public void setBusinessLicenceDate(Long businessLicenceDate) {
        this.businessLicenceDate = businessLicenceDate;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public Long getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Long cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Long getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Long changeDate) {
        this.changeDate = changeDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Long getEnterpriseTypeItemId() {
        return enterpriseTypeItemId;
    }

    public void setEnterpriseTypeItemId(Long enterpriseTypeItemId) {
        this.enterpriseTypeItemId = enterpriseTypeItemId;
    }

    public Long getFoundationDate() {
        return foundationDate;
    }

    public void setFoundationDate(Long foundationDate) {
        this.foundationDate = foundationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLiquidationCommitteeRecoredDate() {
        return liquidationCommitteeRecoredDate;
    }

    public void setLiquidationCommitteeRecoredDate(Long liquidationCommitteeRecoredDate) {
        this.liquidationCommitteeRecoredDate = liquidationCommitteeRecoredDate;
    }

    public BigDecimal getPaidupApital() {
        return paidupApital;
    }

    public void setPaidupApital(BigDecimal paidupApital) {
        this.paidupApital = paidupApital;
    }

    public Long getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Long propertyType) {
        this.propertyType = propertyType;
    }

    public String getRegisteredAddr() {
        return registeredAddr;
    }

    public void setRegisteredAddr(String registeredAddr) {
        this.registeredAddr = registeredAddr;
    }

    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public Long getShareTypeItemId() {
        return shareTypeItemId;
    }

    public void setShareTypeItemId(Long shareTypeItemId) {
        this.shareTypeItemId = shareTypeItemId;
    }

    public Long getTaxRegistrationDate() {
        return taxRegistrationDate;
    }

    public void setTaxRegistrationDate(Long taxRegistrationDate) {
        this.taxRegistrationDate = taxRegistrationDate;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    public Long getValidityBeginDate() {
        return validityBeginDate;
    }

    public void setValidityBeginDate(Long validityBeginDate) {
        this.validityBeginDate = validityBeginDate;
    }

    public Long getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(Long validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
