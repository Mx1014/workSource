// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>roomId:办公室id</li>
 * <li>roomName:办公室名称</li>
 * <li>price:价格</li>
 * <li>coverUri:办公室封面图</li>
 * <li>description:描述</li>
 * <li>rentFlag:是否开启预定，1开启，0不开启</li>
 * <li>associateStation:关联工位id{@link com.everhomes.rest.officecubicle.admin.AssociateStationDTO}</li>
 * <li>rentDate:可预定日期，如改办公室可预定则为空</li>
 * <li>coverUrl:封面图url</li>
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
    private Long rentDate;
    private String coverUrl;
    
    public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Long getRentDate() {
		return rentDate;
	}

	public void setRentDate(Long rentDate) {
		this.rentDate = rentDate;
	}

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
