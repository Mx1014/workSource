package com.everhomes.rest.openapi;

import java.util.List;

import com.everhomes.util.StringHelper;

public class GetOrgCheckInDataResponse {
	private List<CheckInDataDTO> checkinDatas;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<CheckInDataDTO> getCheckinDatas() {
		return checkinDatas;
	}

	public void setCheckinDatas(List<CheckInDataDTO> checkinDatas) {
		this.checkinDatas = checkinDatas;
	}
}
