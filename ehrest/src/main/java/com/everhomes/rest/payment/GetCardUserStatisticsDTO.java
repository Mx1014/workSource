package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>totality: 园区用户总人数</li>
 * <li>cardUserCount: 开卡人数</li>
 * <li>normalUserCount: 未开卡人数</li>
 * </ul>
 */
public class GetCardUserStatisticsDTO {
	private Integer totalCount;
	private Integer cardUserCount;
	private Integer normalUserCount;
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCardUserCount() {
		return cardUserCount;
	}

	public void setCardUserCount(Integer cardUserCount) {
		this.cardUserCount = cardUserCount;
	}

	public Integer getNormalUserCount() {
		return normalUserCount;
	}

	public void setNormalUserCount(Integer normalUserCount) {
		this.normalUserCount = normalUserCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
