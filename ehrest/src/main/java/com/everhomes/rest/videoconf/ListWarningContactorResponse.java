package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页页码，如果没有则后面没有数据</li>
 * <li>contactors: contactor信息，参考{@link com.everhomes.rest.videoconf.WarningContactorDTO}</li>
 * </ul>
 */
public class ListWarningContactorResponse {
	
	@ItemType(WarningContactorDTO.class)
	private List<WarningContactorDTO> contactors;
	
	private Integer nextPageOffset;

	public List<WarningContactorDTO> getContactors() {
		return contactors;
	}

	public void setContactors(List<WarningContactorDTO> contactors) {
		this.contactors = contactors;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
