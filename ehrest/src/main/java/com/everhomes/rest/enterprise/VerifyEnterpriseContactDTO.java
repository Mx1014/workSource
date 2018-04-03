package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId: 自认证通过的enterpriseId</li> 
 * <li>userId: 自认证通过的userId</li> 
 * <li>endTime: 截止时间-系统时间在此之前才通过</li> 
 * </ul>
 * @author wh
 *
 */
public class VerifyEnterpriseContactDTO {

    private Long enterpriseId;
    
    private Long userId;
    
    private Long endTime;
    
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}
