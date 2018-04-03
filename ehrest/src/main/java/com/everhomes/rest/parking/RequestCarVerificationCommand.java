package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerType : 目前是community 参考 {@link ParkingOwnerType}</li>
 * <li>ownerId : 目前是小区/园区ID</li>
 * <li>parkingLotId : 停车场id</li>
 * <li>requestorEnterpriseId: 申请人公司id</li>
 * <li>requestorEnterpriseName: 申请人公司名称</li>
 * <li>plateNumber: 车牌</li>
 * <li>plateOwnerName: 车主姓名</li>
 * <li>plateOwnerPhone: 车数手机号</li>
 * <li>requestType: 申请类型 {@link ParkingCarVerificationType}</li>
 * <li>attachments: attachments {@link com.everhomes.rest.parking.AttachmentDescriptor}</li>
 * </ul>
 */
public class RequestCarVerificationCommand {
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private Long requestorEnterpriseId;
    private String requestorEnterpriseName;
    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private Byte requestType;

    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

    public Byte getRequestType() {
        return requestType;
    }

    public void setRequestType(Byte requestType) {
        this.requestType = requestType;
    }

    public List<AttachmentDescriptor> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDescriptor> attachments) {
        this.attachments = attachments;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getRequestorEnterpriseId() {
        return requestorEnterpriseId;
    }

    public void setRequestorEnterpriseId(Long requestorEnterpriseId) {
        this.requestorEnterpriseId = requestorEnterpriseId;
    }

    public String getRequestorEnterpriseName() {
        return requestorEnterpriseName;
    }

    public void setRequestorEnterpriseName(String requestorEnterpriseName) {
        this.requestorEnterpriseName = requestorEnterpriseName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateOwnerName() {
        return plateOwnerName;
    }

    public void setPlateOwnerName(String plateOwnerName) {
        this.plateOwnerName = plateOwnerName;
    }

    public String getPlateOwnerPhone() {
        return plateOwnerPhone;
    }

    public void setPlateOwnerPhone(String plateOwnerPhone) {
        this.plateOwnerPhone = plateOwnerPhone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
