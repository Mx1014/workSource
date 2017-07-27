package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>userId: userId</li>
 * <li>contactName：联系人姓名</li> 
 * <li>schedulings: 排班列表{@link com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO}</li> 
 * </ul>
 */
public class PunchSchedulingEmployeeDTO {
	private Long userId;
	private String contactName;

	@ItemType(PunchSchedulingDTO.class)
	private List<PunchSchedulingDTO> schedulings;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public List<PunchSchedulingDTO> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<PunchSchedulingDTO> schedulings) {
		this.schedulings = schedulings;
	}
	
}
