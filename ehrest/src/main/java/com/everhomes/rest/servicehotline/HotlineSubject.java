package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 
 * <li>title: 展示标题</li> 
 * <li>serviceType: 类型</li> 
 * <li>layoutType: 布局类型 0-普通 1-专属客服试</li> 
 * </ul>
 */
public class HotlineSubject {
	private String title;
	private Byte serviceType;
	private Byte layoutType;
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Byte getServiceType() {
		return serviceType;
	}


	public void setServiceType(Byte serviceType) {
		this.serviceType = serviceType;
	}


	public Byte getLayoutType() {
		return layoutType;
	}


	public void setLayoutType(Byte layoutType) {
		this.layoutType = layoutType;
	} 
    
}
