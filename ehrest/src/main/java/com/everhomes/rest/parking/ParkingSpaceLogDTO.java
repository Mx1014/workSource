package com.everhomes.rest.parking;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>spaceNo: 车位编号</li>
 * <li>lockId: 锁</li>
 * <li>contactPhone: 操作人手机号</li>
 * <li>contactName: 操作人名称</li>
 * <li>contactEnterpriseName: 操作人公司名称</li>
 * <li>operateType: 操作类型 1: 升起车锁, 2: 降下车锁{@link com.everhomes.rest.parking.ParkingSpaceLockOperateType}</li>
 * <li>userType: 用户类型  RESERVE_PERSON(1): 预约人 , PLATE_OWNER(2): 车主{@link com.everhomes.rest.parking.ParkingSpaceLockOperateUserType}</li>
 * <li>operateUid: 操作人id</li>
 * <li>operateTime: 操作时间</li>
 * </ul>
 */
public class ParkingSpaceLogDTO {
    private Long id;
    private String spaceNo;
    private String lockId;
    private String contactPhone;
    private String contactName;
    private String contactEnterpriseName;
    private Byte operateType;
    private Byte userType;
    private Long operateUid;
    private Timestamp operateTime;

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEnterpriseName() {
        return contactEnterpriseName;
    }

    public void setContactEnterpriseName(String contactEnterpriseName) {
        this.contactEnterpriseName = contactEnterpriseName;
    }

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Long getOperateUid() {
        return operateUid;
    }

    public void setOperateUid(Long operateUid) {
        this.operateUid = operateUid;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }
}
