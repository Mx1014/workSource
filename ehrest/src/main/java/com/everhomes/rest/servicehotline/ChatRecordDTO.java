package com.everhomes.rest.servicehotline;

import java.sql.Timestamp;

/**
 * <ul>
 * 返回值
 * <li>senderName: 发送人名字：客服名/用户名</li>
 * <li>message: 消息内容</li>
 * <li>messageType: 消息类型 {@link com.everhomes.rest.servicehotline.ChatMessageType}</li>
 * <li>sendTime: 发送人名字：客服名/用户名</li>
 * <li>isServicer: 是否客服 0-否  1-是  {@link com.everhomes.rest.servicehotline.NormalFlag} </li>
 * </ul>
 */
public class ChatRecordDTO {
	
	private String senderName;
	private String message;
	private Byte messageType;
	private Timestamp sendTime;
	private Byte isServicer;
	
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Byte getMessageType() {
		return messageType;
	}
	public void setMessageType(Byte messageType) {
		this.messageType = messageType;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public Byte getIsServicer() {
		return isServicer;
	}
	public void setIsServicer(Byte isServicer) {
		this.isServicer = isServicer;
	}
}
