package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <p>用户须知详情信息</p>
 * <ul>
 * <li>parkingLotId : 停车场ID</li>
 * <li>contect : 联系电话</li>
 * <li>summary : 须知详情</li>
 * </ul>
 */
public class UpdateUserNoticeCommand {
    private Long parkingLotId;
    private String contact;
    private String summary;


    public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
