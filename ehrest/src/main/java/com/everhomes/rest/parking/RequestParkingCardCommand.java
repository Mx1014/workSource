// @formatter:off
package com.everhomes.rest.parking;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>申请月卡
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>requestorEnterpriseId: 申请人所在公司ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerEntperiseName: 车主所在公司名称</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * </ul>
 */
public class RequestParkingCardCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
    private Long requestorEnterpriseId;
    @NotNull
    private String plateNumber;
    private String plateOwnerEntperiseName;
    private String plateOwnerName;
    private String plateOwnerPhone;
    
    private String carBrand;
    private String carColor;
    private String carSerieName;
    private Long carSerieId;

    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

    private String cardTypeId;
    private Long addressId;
    private Long invoiceType;

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Long invoiceType) {
        this.invoiceType = invoiceType;
    }

    public RequestParkingCardCommand() {
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

    public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getCarSerieName() {
		return carSerieName;
	}

	public void setCarSerieName(String carSerieName) {
		this.carSerieName = carSerieName;
	}

	public Long getCarSerieId() {
		return carSerieId;
	}

	public void setCarSerieId(Long carSerieId) {
		this.carSerieId = carSerieId;
	}

	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
