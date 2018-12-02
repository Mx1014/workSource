package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeAttachmentDTO;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>station:工位详情{@link com.everhomes.rest.officecubicle.admin.StationDTO}</li>
 * <li>room:办公室详情{@link com.everhomes.rest.officecubicle.admin.RoomDTO}</li>
 * <li>minStationPrice:开放式工位最小价格</li>
 * <li>stationAttachments:开放式工位图片</li>
 * <li>stationNums:开放式工位数量</li>
 * </ul>
 */
public class GetCubicleForAppResponse {

	private List<RoomDTO> room;
	private BigDecimal minStationPrice;
    @ItemType(OfficeAttachmentDTO.class)
	private List<OfficeAttachmentDTO> stationAttachments;
	private Integer stationNums;



	public List<OfficeAttachmentDTO> getStationAttachments() {
		return stationAttachments;
	}





	public void setStationAttachments(List<OfficeAttachmentDTO> stationAttachments) {
		this.stationAttachments = stationAttachments;
	}





	public Integer getStationNums() {
		return stationNums;
	}





	public void setStationNums(Integer stationNums) {
		this.stationNums = stationNums;
	}





	public BigDecimal getMinStationPrice() {
		return minStationPrice;
	}





	public void setMinStationPrice(BigDecimal minStationPrice) {
		this.minStationPrice = minStationPrice;
	}





	public List<RoomDTO> getRoom() {
		return room;
	}





	public void setRoom(List<RoomDTO> room) {
		this.room = room;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
