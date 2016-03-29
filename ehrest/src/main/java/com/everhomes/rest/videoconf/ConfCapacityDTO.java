package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>capacity: 会议容量</li>
 *  <li>capacityId: 会议容量对应code号</li>
 * </ul>
 *
 */
public class ConfCapacityDTO {
	
	private String capacity;
	
	private int capacityId;

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public int getCapacityId() {
		return capacityId;
	}

	public void setCapacityId(int capacityId) {
		this.capacityId = capacityId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
