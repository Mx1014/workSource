package com.everhomes.techpark.punch;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId：申请人id</li>
 * <li>count：打卡次数</li>
 * </ul>
 */
public class UserPunchStatusCount {
	private Long userId;
	private Integer count;
   
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
