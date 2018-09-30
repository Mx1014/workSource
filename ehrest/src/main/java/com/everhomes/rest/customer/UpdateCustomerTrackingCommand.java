package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

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
 *     <li>contentImgUri: 跟进内容图片uri,如果有多个,用英文逗号分隔</li>
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
    private String contentImgUri;
	private Long communityId;

	private BigDecimal visitTimeLength;
	private  Long visitPersonUid;
	private  String visitPersonName;
	private String contactPhone;

	private Long orgId;

	private Byte customerSource;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

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
	
	public String getContentImgUri() {
		return contentImgUri;
	}

	public void setContentImgUri(String contentImgUri) {
		this.contentImgUri = contentImgUri;
	}

	public BigDecimal getVisitTimeLength() {
		return visitTimeLength;
	}

	public void setVisitTimeLength(BigDecimal visitTimeLength) {
		this.visitTimeLength = visitTimeLength;
	}

	public Long getVisitPersonUid() {
		return visitPersonUid;
	}

	public void setVisitPersonUid(Long visitPersonUid) {
		this.visitPersonUid = visitPersonUid;
	}

	public String getVisitPersonName() {
		return visitPersonName;
	}

	public void setVisitPersonName(String visitPersonName) {
		this.visitPersonName = visitPersonName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Byte getCustomerSource() {
		return customerSource;
	}

	public void setCustomerSource(Byte customerSource) {
		this.customerSource = customerSource;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
