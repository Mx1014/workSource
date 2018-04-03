// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 月卡申请ID</li>
 * <li>ownerType: 归属的类型，{@link ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>requestorEnterpriseId: 申请人所在公司ID</li>
 * <li>requestorEnterpriseName: requestorEnterpriseName</li>
 * <li>requestorUid: 申请人用户ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>status: 申请状态，{@link ParkingCardRequestStatus}</li>
 * <li>creatorUid: creatorUid</li>
 * <li>createTime: 订单创建时间</li>
 * <li>flowCaseId: flowCaseId</li>
 * <li>identityCard: identityCard</li>
 * <li>attachments: 申请资料 {@link com.everhomes.rest.parking.ParkingAttachmentDTO}</li>
 * </ul>
 */
public class ParkingCarVerificationDTO {

    private Long id;
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private Long requestorEnterpriseId;
    private String requestorEnterpriseName;
    private Long requestorUid;
    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private Byte status;
    private Long creatorUid;
    private Timestamp createTime;
    private Long flowCaseId;
    @ItemType(ParkingAttachmentDTO.class)
    private List<ParkingAttachmentDTO> attachments;
    private String identityCard;

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getRequestorUid() {
        return requestorUid;
    }

    public void setRequestorUid(Long requestorUid) {
        this.requestorUid = requestorUid;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public List<ParkingAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ParkingAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
