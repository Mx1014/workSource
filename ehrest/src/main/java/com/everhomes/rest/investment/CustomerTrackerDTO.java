package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>customerId: customerId</li>
 *     <li>trackerUid: trackerUid</li>
 *     <li>trackerType: trackerType</li>
 *     <li>sourceType: sourceType</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 *     <li>creatorUid: creatorUid</li>
 *     <li>operatorTime: operatorTime</li>
 *     <li>operatorUid: operatorUid</li>
 * </ul>
 */
public class CustomerTrackerDTO {
    private Long id;
    private Long customerId;
    private Long trackerUid;
    private Byte trackerType;
    private String trackerName;
    private String trackerPhone;
    private Byte customerSource;
    private Byte status;

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

    public Long getTrackerUid() {
        return trackerUid;
    }

    public void setTrackerUid(Long trackerUid) {
        this.trackerUid = trackerUid;
    }

    public Byte getTrackerType() {
        return trackerType;
    }

    public void setTrackerType(Byte trackerType) {
        this.trackerType = trackerType;
    }

    public Byte getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(Byte customerSource) {
        this.customerSource = customerSource;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getTrackerName(){
        return trackerName;
    }

    public void setTrackerName(String trackerName){
        this.trackerName = trackerName;
    }


    public String getTrackerPhone(){
        return trackerPhone;
    }

    public void setTrackerPhone(String trackerPhone){
        this.trackerPhone = trackerPhone;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
