package com.everhomes.payment.util;

import java.util.Date;

public class CacheItem {
	
	private Date createTime = new Date();//创建缓存的时间
	private long expireTime = 10 * 60 * 1000;//缓存期满的时间 默认为十分钟
	private Object entity;//缓存的实体
	  
	public CacheItem(Object obj,long expires){
		this.entity = obj;
		this.expireTime = expires;
	}
	
	public CacheItem(Object obj){
		this.entity = obj;
	}
	  
	public boolean isExpired(){
	    return (expireTime != -1 && new Date().getTime()-createTime.getTime() > expireTime);
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}
	
}
