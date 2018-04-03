package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>limitCount: 图片张数限制</li>
 * <li>limitPerSize: 每个图片的大小</li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormImageDTO {
	private Integer limitCount;
	private Integer limitPerSize;
	
	public Integer getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}
	public Integer getLimitPerSize() {
		return limitPerSize;
	}
	public void setLimitPerSize(Integer limitPerSize) {
		this.limitPerSize = limitPerSize;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
