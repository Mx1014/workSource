// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>stationId:工位id</li>
 * <li>stationName:工位名称</li>
 * <li>price:价格</li>
 * <li>coverUri:办公室封面图</li>
 * <li>description:描述</li>
 * <li>rentFlag:是否开启预定，1开启，0不开启</li>
 * <li>associateRoomId:关联办公室id</li>
 * <li>associateRoomName:关联办公室名称</li>
 * <li>coverUrl:图片url</li>
 * </ul>
 */
public class StationDTO {
    private Long stationId;
    private String stationName;
    private BigDecimal price;
    private Byte rentFlag;
    private String coverUri;
    private String description;
    private Long associateRoomId;
    private String associateRoomName;
    private String coverUrl;
    private Byte status;

	public Byte getStatus() {
		return status;
	}



	public void setStatus(Byte status) {
		this.status = status;
	}



	public String getCoverUrl() {
		return coverUrl;
	}



	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}



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



	public Long getAssociateRoomId() {
		return associateRoomId;
	}



	public void setAssociateRoomId(Long associateRoomId) {
		this.associateRoomId = associateRoomId;
	}



	public String getAssociateRoomName() {
		return associateRoomName;
	}



	public void setAssociateRoomName(String associateRoomName) {
		this.associateRoomName = associateRoomName;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
