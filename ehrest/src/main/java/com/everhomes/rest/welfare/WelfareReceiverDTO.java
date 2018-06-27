package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 
 * <li>id: id 有就填没有不填 </li>
 * <li>receiverName: 接收者姓名 必填 </li>
 * <li>receiverUid: 接收者uId 可不填</li>
 * <li>receiverDetailId: 接收者detail id 必填</li>
 * </ul>
 */
public class WelfareReceiverDTO {
	private Long id;
    private Long receiverUid;
    private String receiverName;
    private Long receiverDetailId;
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getReceiverUid() {
		return receiverUid;
	}

	public void setReceiverUid(Long receiverUid) {
		this.receiverUid = receiverUid;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Long getReceiverDetailId() {
		return receiverDetailId;
	}

	public void setReceiverDetailId(Long receiverDetailId) {
		this.receiverDetailId = receiverDetailId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
