package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: 跟进信息id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 客户id</li>
 *     <li>customerName: 客户名称</li>
 *     <li>contactName: 联系人</li>
 *     <li>trackingType: 跟进方式id</li>
 *     <li>trackingTypeName: 跟进方式名称</li>
 *     <li>trackingUid: 跟进人id</li>
 *     <li>trackingUidName: 跟进人姓名</li>
 *     <li>intentionGrade: 意向等级</li>
 *     <li>trackingTime: 跟进时间</li>
 *     <li>content: 跟进内容</li>
 *     <li>contentImgUriList: 跟进内容图片uri</li>
 *     <li>visitTimeLength: 时长</li>
 *     <li>visitPersonUid: 访问人uid</li>
 *     <li>visitPersonName: 访问人姓名</li>
 *     <li>contactPhone: 联系方式</li>
 * </ul>
 * Created by shengyue.wang on 2017/9/20.
 */
public class CustomerTrackingDTO {
    private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String customerName;
    private String contactName;
    private Long trackingType;
    private String trackingTypeName;
    private Long trackingUid;
    private String trackingUidName;
    private Integer intentionGrade;
    private String intentionGradeName;
    private Timestamp trackingTime;
    private String content;
	private BigDecimal visitTimeLength;
	private  Long visitPersonUid;
	private  String visitPersonName;
	private String contactPhone;
	private Byte investmentType;
    
    @ItemType(String.class)
    private List<String>  contentImgUriList;
    
    @ItemType(String.class)
    private List<String> contentImgUrlList;

	public String getIntentionGradeName() {
		return intentionGradeName;
	}

	public void setIntentionGradeName(String intentionGradeName) {
		this.intentionGradeName = intentionGradeName;
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

	public String getTrackingUidName() {
		return trackingUidName;
	}

	public void setTrackingUidName(String trackingUidName) {
		this.trackingUidName = trackingUidName;
	}

	
	public List<String> getContentImgUriList() {
		return contentImgUriList;
	}

	public void setContentImgUriList(List<String> contentImgUriList) {
		this.contentImgUriList = contentImgUriList;
	}

	public List<String> getContentImgUrlList() {
		return contentImgUrlList;
	}

	public void setContentImgUrlList(List<String> contentImgUrlList) {
		this.contentImgUrlList = contentImgUrlList;
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

	public Byte getInvestmentType() {
		return investmentType;
	}

	public void setInvestmentType(Byte investmentType) {
		this.investmentType = investmentType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
