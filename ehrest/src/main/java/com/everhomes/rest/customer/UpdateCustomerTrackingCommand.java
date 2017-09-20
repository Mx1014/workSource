package com.everhomes.rest.customer;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 客户id</li>
 *     <li>customerName: 客户名称</li>
 *     <li>contactName: 联系人</li>
 *     <li>trackingType: 跟进方式id</li>
 *     <li>trackingUid: 跟进人id</li>
 *     <li>intentionGrade: 意向等级</li>
 *     <li>trackingTime: 跟进时间</li>
 *     <li>content: 跟进内容</li>
 * </ul>
 * Created by shengyue.wang on 2017/9/20.
 */
public class UpdateCustomerTrackingCommand {
	private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String customerName;
    private String contactName;
    private Long trackingType;
    private Long trackingUid;
    private Integer intentionGrade;
    private Long trackingTime;
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

	public Long getTrackingUid() {
		return trackingUid;
	}

	public void setTrackingUid(Long trackingUid) {
		this.trackingUid = trackingUid;
	}

	public Integer getIntentionGrade() {
		return intentionGrade;
	}

	public void setIntentionGrade(Integer intentionGrade) {
		this.intentionGrade = intentionGrade;
	}

	public Long getTrackingTime() {
		return trackingTime;
	}

	public void setTrackingTime(Long trackingTime) {
		this.trackingTime = trackingTime;
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
