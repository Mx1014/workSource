package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定的空间
  * <li>namespaceId : namespaceId</li>
 * <li>ownerType : community 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 *<li> spaceid: id	</li>
 *<li> spaceName: 工位空间名称	</li>
*<li> address : 地址	</li>
*<li>contactPhone  : 咨询电话	</li>
*<li> coverUri : 封面图片uri</li>
*<li>refundTip:退款提示</li>
*<li>pirce</li>
*<li>holidayOpenFlag:0不开放，1开放</li>
*<li>holidayType：1双休，2法定节假日</li>
* </ul>
 */
public class OfficeCubicleDTO {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long spaceId;
	private String spaceName;
	private BigDecimal price;
	private String refundTip;
	private String address;
	private String contactPhone;
	private String coverUri;
	private Byte holidayOpenFlag;
	private Byte holidayType;
	private Byte rentFlag;
	private Byte assciateFlag;
	private Long assciateStation;
	private String description;
	private Byte stationType;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getRefundTip() {
		return refundTip;
	}

	public void setRefundTip(String refundTip) {
		this.refundTip = refundTip;
	}

	public Byte getHolidayOpenFlag() {
		return holidayOpenFlag;
	}

	public void setHolidayOpenFlag(Byte holidayOpenFlag) {
		this.holidayOpenFlag = holidayOpenFlag;
	}

	public Byte getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(Byte holidayType) {
		this.holidayType = holidayType;
	}

	public Byte getRentFlag() {
		return rentFlag;
	}

	public void setRentFlag(Byte rentFlag) {
		this.rentFlag = rentFlag;
	}

	public Byte getAssciateFlag() {
		return assciateFlag;
	}

	public void setAssciateFlag(Byte assciateFlag) {
		this.assciateFlag = assciateFlag;
	}

	public Long getAssciateStation() {
		return assciateStation;
	}

	public void setAssciateStation(Long assciateStation) {
		this.assciateStation = assciateStation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getStationType() {
		return stationType;
	}

	public void setStationType(Byte stationType) {
		this.stationType = stationType;
	}
	
	
}
