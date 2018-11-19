package com.everhomes.rest.address;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;
/**
*<ul>
* <li>namespaceId:域空间id</li>
* <li>addressId:房源编号</li>
* <li>operatorUid: 操作人id</li>
* <li>operatorName: 操作人名称</li>
* <li>operateTime:操作时间</li>
* <li>operateType:操作类型（1：增加，2：删除，3：删除）</li>
* <li>content:日志内容</li>
*</ul>
*/
public class ApartmentEventDTO {
	
	private Integer namespaceId;
	private Long addressId;
	private Long operatorUid;
	private String operatorName;
	private Timestamp operateTime;
	private Byte operateType;
	private String content;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getOperatorUid() {
		return operatorUid;
	}
	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Timestamp getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
	public Byte getOperateType() {
		return operateType;
	}
	public void setOperateType(Byte operateType) {
		this.operateType = operateType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
