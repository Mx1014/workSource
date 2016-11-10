package com.everhomes.dbsync;

public class NashornMappingObject implements NashornObject {
	private long id;
	private long createTime;
	private String name;
	private String appName;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

    public long getCreateTime() {
		return createTime;
	}

	@Override
    public long getTimeout() {
        return (10*1000l); //TODO hard code here
    }
    
	@Override
	public void onError(Exception ex) {
	}

	@Override
	public void onComplete() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String getJSFunc() {
		return "mappingInit";
	}

}
