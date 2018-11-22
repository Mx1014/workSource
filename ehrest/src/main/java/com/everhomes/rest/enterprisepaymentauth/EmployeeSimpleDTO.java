package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>detailId: 用户detailId</li>
 * <li>contactName: 姓名</li>
 * </ul>
 */
public class EmployeeSimpleDTO {
    private Long userId;
    private Long detailId;
    private String contactName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }
 

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
}
