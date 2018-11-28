// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 */
public class RoomDTO {
    private Long roomId;
    private String roomName;
    private BigDecimal price;
    private String coverUri;
    private String description;
    private Byte rentFlag;
    private List<AssociateStationDTO> associateStation;
    
    public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}


	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
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



	public Byte getRentFlag() {
		return rentFlag;
	}

	public void setRentFlag(Byte rentFlag) {
		this.rentFlag = rentFlag;
	}

	public List<AssociateStationDTO> getAssociateStation() {
		return associateStation;
	}

	public void setAssociateStation(List<AssociateStationDTO> associateStation) {
		this.associateStation = associateStation;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
