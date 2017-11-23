// @formatter:off
package com.everhomes.rest.parking;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.flow.FlowButtonDTO;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowNodeLogDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 月卡申请ID</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>requestorEnterpriseId: 申请人所在公司ID</li>
 * <li>requestorUid: 申请人用户ID</li>
 * <li>requestorName: 申请人名称</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerEntperiseName: 车主所在公司名称</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>ranking: 当前排位号</li>
 * <li>status: 申请状态，{@link com.everhomes.rest.parking.ParkingCardRequestStatus}</li>
 * <li>issueFlag: 充值状态， {@link com.everhomes.rest.parking.ParkingCardIssueFlag}</li>
 * <li>issueTime: 领卡时间</li>
 * <li>createTime: 订单创建时间</li>
 * <li>carBrand: 车品牌</li>
 * <li>carSerieName: 车系列</li>
 * <li>carColor: 车颜色</li>
 * <li>attachments: 申请资料</li>
 * </ul>
 */
public class ParkingCardRequestDTO {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private Long requestorEnterpriseId;
    private Long requestorUid;
    private String requestorName;
    private String plateNumber;
    private String plateOwnerEntperiseName;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private Integer ranking;
    private Byte status;

    private Timestamp issueTime;
    private Timestamp createTime;
    
    private Long flowId;
	private Integer flowVersion;
	private Long flowCaseId;
    
    private String carBrand;
    private String carSerieName;
    private String carColor;
    @ItemType(ParkingAttachmentDTO.class)
    private List<ParkingAttachmentDTO> attachments;
    
    private Timestamp auditSucceedTime;
	private Timestamp processSucceedTime;
	private Timestamp openCardTime;
	private Timestamp cancelTime;

	private String cardTypeName;
    private String apartmentName;
    private String invoiceName;

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public ParkingCardRequestDTO() {
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

    public Long getRequestorUid() {
        return requestorUid;
    }

    public void setRequestorUid(Long requestorUid) {
        this.requestorUid = requestorUid;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateOwnerEntperiseName() {
        return plateOwnerEntperiseName;
    }

    public void setPlateOwnerEntperiseName(String plateOwnerEntperiseName) {
        this.plateOwnerEntperiseName = plateOwnerEntperiseName;
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

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Timestamp issueTime) {
        this.issueTime = issueTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarSerieName() {
		return carSerieName;
	}

	public void setCarSerieName(String carSerieName) {
		this.carSerieName = carSerieName;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public List<ParkingAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ParkingAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}

	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Timestamp getAuditSucceedTime() {
		return auditSucceedTime;
	}

	public void setAuditSucceedTime(Timestamp auditSucceedTime) {
		this.auditSucceedTime = auditSucceedTime;
	}

	public Timestamp getProcessSucceedTime() {
		return processSucceedTime;
	}

	public void setProcessSucceedTime(Timestamp processSucceedTime) {
		this.processSucceedTime = processSucceedTime;
	}

	public Timestamp getOpenCardTime() {
		return openCardTime;
	}

	public void setOpenCardTime(Timestamp openCardTime) {
		this.openCardTime = openCardTime;
	}

	public Timestamp getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Timestamp cancelTime) {
		this.cancelTime = cancelTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
