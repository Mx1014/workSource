package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul>预订的actiondata
 * <li>launchPadItemId: 广场图标的id</li>
 * <li>pageType: 预定展示0代表默认页面DefaultType, 1代表定制页面CustomType</li>
 * </ul>
 */
public class RentalActionData implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

	private Long launchPadItemId;
	private Byte pageType;
	
	
	
    public Long getLaunchPadItemId() {
		return launchPadItemId;
	}



	public void setLaunchPadItemId(Long launchPadItemId) {
		this.launchPadItemId = launchPadItemId;
	}



	public Byte getPageType() {
		return pageType;
	}



	public void setPageType(Byte pageType) {
		this.pageType = pageType;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
