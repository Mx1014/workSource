package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId: 企业id</li>
 * <li>keyword: 关键字 企业名称 联系人姓名或手机号</li>
 * <li>startTime: 订单时间 查询开始时间</li>
 * <li>endTime: 订单时间 查询结束时间</li>
 * <li>confCapacity: 账号类型  0-25方 1-100方 2-6方 3-50方 </li>
 * <li>confType: 是否支持电话 0-仅视频 1-支持电话 </li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListVideoConfAccountOrderCommand {
	
	private Long enterpriseId;
	
	private String keyword;
	
	private Long startTime;
	
	private Long endTime;
	
	private Byte confCapacity;
	
	private Byte confType;
	
	private Long pageAnchor;
	
    private Integer pageSize;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Byte getConfCapacity() {
		return confCapacity;
	}

	public void setConfCapacity(Byte confCapacity) {
		this.confCapacity = confCapacity;
	}

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
