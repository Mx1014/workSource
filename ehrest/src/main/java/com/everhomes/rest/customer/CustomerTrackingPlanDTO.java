package com.everhomes.rest.customer;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 计划信息id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 客户id</li>
 *     <li>customerName: 客户名称</li>
 *     <li>contactName: 联系人</li>
 *     <li>trackingType: 跟进方式id</li>
 *     <li>trackingTypeName: 跟进方式名称</li>
 *     <li>trackingTime: 跟进时间</li>
 *     <li>notifyTime: 提醒时间</li>
 *     <li>title: 标题</li>
 *     <li>content: 内容</li>
 * </ul>
 */
public class CustomerTrackingPlanDTO {
    private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String customerName;
    private String contactName;
    private Long trackingType;
    private String trackingTypeName;
    private Timestamp trackingTime;
    private Timestamp notifyTime;
    private String title;
    private String content;
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Byte customerType) {
		this.customerType = customerType;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getTrackingType() {
		return trackingType;
	}

	public void setTrackingType(Long trackingType) {
		this.trackingType = trackingType;
	}


	public Timestamp getTrackingTime() {
		return trackingTime;
	}

	public void setTrackingTime(Timestamp trackingTime) {
		this.trackingTime = trackingTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTrackingTypeName() {
		return trackingTypeName;
	}

	public void setTrackingTypeName(String trackingTypeName) {
		this.trackingTypeName = trackingTypeName;
	}

	public Timestamp getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(Timestamp notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
