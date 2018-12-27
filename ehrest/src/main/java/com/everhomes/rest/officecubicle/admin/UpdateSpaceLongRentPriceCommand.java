package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.ChargeUserDTO;
import com.everhomes.rest.officecubicle.OfficeAttachmentDTO;
import com.everhomes.rest.officecubicle.OfficeCategoryDTO;
import com.everhomes.rest.officecubicle.OfficeRangeDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId :namespaceId;</li>
 * <li>ownerType : community 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 *<li> id: id</li>
 * <li>longRentPrice:长租工位价格</li>
 * </ul>
 */
public class UpdateSpaceLongRentPriceCommand { 
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long spaceId;
	private BigDecimal longRentPrice;

	
	
	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}


	public BigDecimal getLongRentPrice() {
		return longRentPrice;
	}

	public void setLongRentPrice(BigDecimal longRentPrice) {
		this.longRentPrice = longRentPrice;
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
 
}
