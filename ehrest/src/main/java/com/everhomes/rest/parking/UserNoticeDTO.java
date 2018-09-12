// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>contact: 联系电话</li>
 * <li>summary:用户须知</li>
 * </ul>
 */
public class UserNoticeDTO {
    private String contact;
	private String summary;
    

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public UserNoticeDTO() {
    }


    public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
