package com.everhomes.rest.openapi.shenzhou;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/28.
 */
public class ZJIndividuals {
    private String communityIdentifier;
    private Long communityId;
    private String userIdentifier;
    private String taxpayerIdentity;
    private String name;
    private String customerCategory;
    private String customerLevel;
    private String gender;
    private String birthday;
    private String registeredResidence;
    private String contactAddress;
    private String offficePhone;
    private String familyPhone;
    private String email;
    private String fax;
    private String mobile;
    private String cardType;
    private String cardNumber;
    private String company;
    private String industry;
    private String job;
    private String maritalStatus;
    private String source;
    private Boolean dealed;
    @ItemType(CommunityAddressDTO.class)
    private List<CommunityAddressDTO> addressList;

    public List<CommunityAddressDTO> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<CommunityAddressDTO> addressList) {
        this.addressList = addressList;
    }

    public Boolean getDealed() {
        return dealed;
    }

    public void setDealed(Boolean dealed) {
        this.dealed = dealed;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityIdentifier() {
        return communityIdentifier;
    }

    public void setCommunityIdentifier(String communityIdentifier) {
        this.communityIdentifier = communityIdentifier;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffficePhone() {
        return offficePhone;
    }

    public void setOffficePhone(String offficePhone) {
        this.offficePhone = offficePhone;
    }

    public String getRegisteredResidence() {
        return registeredResidence;
    }

    public void setRegisteredResidence(String registeredResidence) {
        this.registeredResidence = registeredResidence;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTaxpayerIdentity() {
        return taxpayerIdentity;
    }

    public void setTaxpayerIdentity(String taxpayerIdentity) {
        this.taxpayerIdentity = taxpayerIdentity;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
}
