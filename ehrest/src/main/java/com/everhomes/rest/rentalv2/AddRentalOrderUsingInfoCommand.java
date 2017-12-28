package com.everhomes.rest.rentalv2;


import javax.validation.constraints.NotNull;

/**
 * @author sw on 2017/12/28.
 */
public class AddRentalOrderUsingInfoCommand {

    @NotNull
    private Long rentalBillId;
    private Byte rentalType;

    private String customObject;

    private String clientAppName;
    private Integer paymentType;

    private Long userEnterpriseId;
    private String userEnterpriseName;
    private String userPhone;
    private String userName;
    private Long addressId;

    public Long getRentalBillId() {
        return rentalBillId;
    }

    public void setRentalBillId(Long rentalBillId) {
        this.rentalBillId = rentalBillId;
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public String getCustomObject() {
        return customObject;
    }

    public void setCustomObject(String customObject) {
        this.customObject = customObject;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Long getUserEnterpriseId() {
        return userEnterpriseId;
    }

    public void setUserEnterpriseId(Long userEnterpriseId) {
        this.userEnterpriseId = userEnterpriseId;
    }

    public String getUserEnterpriseName() {
        return userEnterpriseName;
    }

    public void setUserEnterpriseName(String userEnterpriseName) {
        this.userEnterpriseName = userEnterpriseName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
