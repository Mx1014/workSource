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
	private Long totalCount;
	private Long cardUserCount;
	private Long normalUserCount;
	
	
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public Long getCardUserCount() {
		return cardUserCount;
	}
	public void setCardUserCount(Long cardUserCount) {
		this.cardUserCount = cardUserCount;
	}
	public Long getNormalUserCount() {
		return normalUserCount;
	}
	public void setNormalUserCount(Long normalUserCount) {
		this.normalUserCount = normalUserCount;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
