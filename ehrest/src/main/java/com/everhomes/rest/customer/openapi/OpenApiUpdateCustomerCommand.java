package com.everhomes.rest.customer.openapi;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

public class OpenApiUpdateCustomerCommand {

    private Long enterpriseId;
    private Integer namespaceId;
    private Long communityId;
    private String companyName;
    private String contactName;
    @ItemType(AddressDTO.class)
    private List<AddressDTO> addresses;
    private String corpBusinessLicense;
    private Long corpEntryDate;
    private String corpDescription;
    private String remark;
    private String hotline;
    private Long foundingTime;
    private String unifiedSocialCreditCode;
    //以下参数为openapi所需
    private String appKey;
    private String signature;
    private Long timestamp;
    private Integer nonce;
    private String crypto;


    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public String getCorpBusinessLicense() {
        return corpBusinessLicense;
    }

    public void setCorpBusinessLicense(String corpBusinessLicense) {
        this.corpBusinessLicense = corpBusinessLicense;
    }

    public Long getCorpEntryDate() {
        return corpEntryDate;
    }

    public void setCorpEntryDate(Long corpEntryDate) {
        this.corpEntryDate = corpEntryDate;
    }

    public String getCorpDescription() {
        return corpDescription;
    }

    public void setCorpDescription(String corpDescription) {
        this.corpDescription = corpDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public Long getFoundingTime() {
        return foundingTime;
    }

    public void setFoundingTime(Long foundingTime) {
        this.foundingTime = foundingTime;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getCrypto() {
        return crypto;
    }

    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
