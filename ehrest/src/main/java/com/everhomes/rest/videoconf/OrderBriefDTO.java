package com.everhomes.rest.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：订单id</li>
 * <li>createTime：下单时间</li>
 * <li>period：包月月份</li>
 * <li>confType：会议类型 0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话</li>
 * </ul>
 */
public class OrderBriefDTO {
	
	private Long id;
	
	private Timestamp createTime;
	
	private Integer period;
	
	private Byte confType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
