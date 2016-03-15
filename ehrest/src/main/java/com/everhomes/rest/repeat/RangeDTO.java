package com.everhomes.rest.repeat;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class RangeDTO {
	
	@ItemType(TimeRangeDTO.class)
	private List<TimeRangeDTO> ranges;

	public List<TimeRangeDTO> getRanges() {
		return ranges;
	}

	public void setRanges(List<TimeRangeDTO> ranges) {
		this.ranges = ranges;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
