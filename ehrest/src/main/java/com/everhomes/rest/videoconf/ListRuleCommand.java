package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>channelType: 是否多账号 0-单账号 1-多账号</li>
 * <li>confType: 会议类型 0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话</li>
 * <li>pageOffset: 页码，从1开始</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListRuleCommand {
	
	private Byte channelType;
	
	private Byte confType;
	
	private Integer pageOffset;
    
    private Integer pageSize;

	public Byte getChannelType() {
		return channelType;
	}

	public void setChannelType(Byte channelType) {
		this.channelType = channelType;
	}

	public Byte getConfType() {
		return confType;
	}

	public void setConfType(Byte confType) {
		this.confType = confType;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
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
