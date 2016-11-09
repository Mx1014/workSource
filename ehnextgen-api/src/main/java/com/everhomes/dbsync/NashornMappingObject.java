package com.everhomes.dbsync;

public class NashornMappingObject implements NashornObject {
	private long id;
	private long createTime;
	private String content;
	private String name;
	
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getJSFunc() {
		return "mappingInit";
	}

}
