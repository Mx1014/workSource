package com.everhomes.rest.customer;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 客户id</li>
 *     <li>taxName: 报税人</li>
 *     <li>taxNo: 报税人税号</li>
 *     <li>taxAddress: 地址</li>
 *     <li>taxPhone: 联系电话</li>
 *     <li>taxBank: 开户行名称</li>
 *     <li>taxBankNo: 开户行账号</li>
 *     <li>taxPayerTypeId: 报税人类型id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CreateCustomerTaxCommand {
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String taxName;
    private String taxNo;
    private String taxAddress;
    private String taxPhone;
    private String taxBank;
    private String taxBankNo;
    private Long taxPayerTypeId;
    private Long orgId;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getTaxAddress() {
        return taxAddress;
    }

    public void setTaxAddress(String taxAddress) {
        this.taxAddress = taxAddress;
    }

    public String getTaxBank() {
        return taxBank;
    }

    public void setTaxBank(String taxBank) {
        this.taxBank = taxBank;
    }

    public String getTaxBankNo() {
        return taxBankNo;
    }

    public void setTaxBankNo(String taxBankNo) {
        this.taxBankNo = taxBankNo;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public Long getTaxPayerTypeId() {
        return taxPayerTypeId;
    }

    public void setTaxPayerTypeId(Long taxPayerTypeId) {
        this.taxPayerTypeId = taxPayerTypeId;
    }

    public String getTaxPhone() {
        return taxPhone;
    }

    public void setTaxPhone(String taxPhone) {
        this.taxPhone = taxPhone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
