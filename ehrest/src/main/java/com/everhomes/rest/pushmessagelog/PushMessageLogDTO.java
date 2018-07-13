package com.everhomes.rest.pushmessagelog;

import java.util.Date;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>id: 主键</li>
* <li>namespaceId: 域空间ID</li>
* <li>content: 推送内容</li>
* <li>pushType: 推送方式{@link com.everhomes.rest.pushmessagelog.PushMessageTypeCode}</li>
* <li>receiverType: 推送对象的类型{@link com.everhomes.rest.pushmessagelog.ReceiverTypeCode}</li>
* <li>receivers: 消息推送对象(推送对象的类型为所有人时，该值为空或长度为0，按项目时，存的是项目名，按手机号时，存的时手机号)</li>
* <li>createTime: 推送创建时间,格式 yyyy-MM-dd HH:mm</li>
* <li>pushStatus: 推送状态{@link com.everhomes.rest.pushmessagelog.PushStatusCode}</li>
* <li>operator: 操作人</li>
* </ul>
*/
public class PushMessageLogDTO {
	
	private Long id ;
	private Long namespaceId ;
	private String content ; 
	private Integer pushType ;
	private Integer receiverType ;
	private List<String> receivers ;
	private Date createTime ;
	private Integer pushStatus ;	
    private String operator ;
 
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Long namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public Integer getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(Integer receiverType) {
		this.receiverType = receiverType;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
