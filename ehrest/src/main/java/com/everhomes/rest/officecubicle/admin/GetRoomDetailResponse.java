package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>room:办公室详情 {@link com.everhomes.rest.officecubicle.admin.RoomDTO}</li>
 * </ul>
 */
public class GetRoomDetailResponse {

	private RoomDTO room;

	public RoomDTO getRoom() {
		return room;
	}


	public void setRoom(RoomDTO room) {
		this.room = room;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
