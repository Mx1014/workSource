package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * capacities:会议容量类型，参考com.everhomes.rest.videoconf.ConfCapacityDTO
 *
 */
public class ListConfCapacityResponse {
	
	private List<ConfCapacityDTO> capacities;
	
	public List<ConfCapacityDTO> getCapacities() {
		return capacities;
	}

	public void setCapacities(List<ConfCapacityDTO> capacities) {
		this.capacities = capacities;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
