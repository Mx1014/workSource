package com.everhomes.payment.util;

import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.everhomes.payment.PaymentCardServiceImpl;

public class CachePool {
    private static final Logger LOGGER = LoggerFactory.getLogger(CachePool.class);

	private static CachePool instance;//缓存池唯一实例
	  private ConcurrentHashMap<String,Object> cacheItems;//缓存Map
	  private CachePool(){
	    cacheItems = new ConcurrentHashMap<String,Object>();
	  }
	  /**
	   * 得到唯一实例
	   * @return
	   */
	  public synchronized static CachePool getInstance(){
	    if(instance == null){
	      instance = new CachePool();
	    }
	    return instance;
	  }
	  /**
	   * 清除所有Item缓存
	   */
	  public synchronized void clearAllItems(){
	    cacheItems.clear();
	  }
	  /**
	   * 获取缓存实体
	   * @param name
	   * @return
	   */
	  public CacheItem getCacheItem(String name){
	    if(!cacheItems.containsKey(name)){
	      return null;
	    }
	    CacheItem cacheItem = (CacheItem) cacheItems.get(name);
//	    if(cacheItem.isExpired()){
//	      return null;
//	    }
	    return cacheItem;
	  }
	  
	  public Object getObjectValue(String name){
		    if(!cacheItems.containsKey(name)){
		      return null;
		    }
		    CacheItem cacheItem = (CacheItem) cacheItems.get(name);
		    if(cacheItem.isExpired()){
		      return null;
		    }
		    return cacheItem.getEntity();
	  }
	  
	  public String getStringValue(String name){
		    if(!cacheItems.containsKey(name)){
		      return null;
		    }
		    CacheItem cacheItem = (CacheItem) cacheItems.get(name);
		    if(cacheItem.isExpired()){
		      return null;
		    }
		    return (String) cacheItem.getEntity();
	  }
	  /**
	   * 存放缓存信息
	   * @param name
	   * @param obj
	   * @param expires
	   */
	  public void putCacheItem(String name,Object obj,long expires){
	    if(!cacheItems.containsKey(name)){
	      cacheItems.put(name, new CacheItem(obj, expires));
	    }
	    CacheItem cacheItem = (CacheItem) cacheItems.get(name);
	    cacheItem.setCreateTime(new Date());
	    cacheItem.setEntity(obj);
	    cacheItem.setExpireTime(expires);
	  }
//	  public void putCacheItem(String name,Object obj){
//	    putCacheItem(name,obj,-1);
//	  }
	  public void putCacheItem(String name,Object obj){
		    if(!cacheItems.containsKey(name)){
		      cacheItems.put(name, new CacheItem(obj));
		    }
		    CacheItem cacheItem = (CacheItem) cacheItems.get(name);
		    cacheItem.setCreateTime(new Date());
		    cacheItem.setEntity(obj);
	  }
	  /**
	   * 移除缓存数据
	   * @param name
	   */
	  public void removeCacheItem(String name){
	    if(!cacheItems.containsKey(name)){
	      return;
	    }
	    cacheItems.remove(name);
	  }
	  /**
	   * 获取缓存数据的数量
	   * @return
	   */
	  public int getSize(){
	    return cacheItems.size();
	  }
	  
	  public void quartzClearMap(){
		  Enumeration<String> keys =  cacheItems.keys();
		  while(keys.hasMoreElements()){
			  String key = keys.nextElement();
			  CacheItem item = (CacheItem) cacheItems.get(key);
			  if(item.isExpired())
				  cacheItems.remove(key);
		  }
	  }
}
