// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 */
public class StationDTO {
    private Long stationId;
    private String stationName;
    private BigDecimal price;
    private Byte rentFlag;
    private String coverUri;
    private String description;
    private Long associateRoom;


	public Long getStationId() {
		return stationId;
	}



	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}




	public String getStationName() {
		return stationName;
	}



	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public Byte getRentFlag() {
		return rentFlag;
	}



	public void setRentFlag(Byte rentFlag) {
		this.rentFlag = rentFlag;
	}



	public String getCoverUri() {
		return coverUri;
	}


	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAssociateRoom() {
		return associateRoom;
	}

	public void setAssociateRoom(Long associateRoom) {
		this.associateRoom = associateRoom;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
