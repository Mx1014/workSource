package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>namespaceId: 域空间</li>
 * <li>statDate: 统计日</li>
 * </ul>
 */
public class AddAnyDayActiveCommand {
	
	private Integer namespaceId;
	
	private String statDate;
 
	 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public Integer getNamespaceId() {
		return namespaceId;
	}



	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}



	public String getStatDate() {
		return statDate;
	}



	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
 
}
