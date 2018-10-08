package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>customerId: customerId</li>
 *     <li>name: name</li>
 *     <li>phoneNumber: phoneNumber</li>
 *     <li>email: email</li>
 *     <li>position: position</li>
 *     <li>address: address</li>
 *     <li>contactType: contactType</li>
 *     <li>sourceType: sourceType</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 *     <li>creatorUid: creatorUid</li>
 *     <li>operatorTime: operatorTime</li>
 *     <li>operatorUid: operatorUid</li>
 * </ul>
 */
public class CustomerContactDTO {


    private Long id;
    private Long customerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String position;
    private String address;
    private Byte contactType;
    private Byte customerSource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Byte getContactType() {
        return contactType;
    }

    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    public Byte getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(Byte customerSource) {
        this.customerSource = customerSource;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
