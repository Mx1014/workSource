package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>identifierToken:token</li>
 * <li>content:消息文本</li>
 * <li>msgType:消息类型：1，只发应用内消息；2，只推送；3，同时推送兼发应用内消息</li>
 * <li>namespaceId:用户域id</li>
 * </ul>
 * 
 * @author moubinmo
 *
 */

public class ThirdPartPushMessageCommand {
	private String identifierToken;
	private String content;
	private Integer msgType;
	private Integer namespaceId;
	
	
	public String getIdentifierToken() {
		return identifierToken;
	}
	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
    public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
