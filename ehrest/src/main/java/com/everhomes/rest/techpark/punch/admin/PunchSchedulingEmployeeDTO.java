package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>userId: userId</li>
 * <li>contactName：联系人姓名</li> 
 * <li>daySchedulings: 排班列表字符串比如["早班","","晚班"]代表1号早班,2号不上班,3号晚班</li> 
 * </ul>
 */
public class PunchSchedulingEmployeeDTO {
	private Long userId;
	private String contactName;

	@ItemType(String.class)
	private List<String> daySchedulings;

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
