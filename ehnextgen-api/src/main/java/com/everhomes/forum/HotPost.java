package com.everhomes.forum;

public class HotPost {

	private Long postId;
	
	private Long timeStamp;
	
	private Byte modifyType;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Byte getModifyType() {
		return modifyType;
	}

	public void setModifyType(Byte modifyType) {
		this.modifyType = modifyType;
	}
	
	
}
