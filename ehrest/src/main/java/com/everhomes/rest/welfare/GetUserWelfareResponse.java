// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>返回值:
 * <li>id: 福利Id</li>
 * <li>ownerType: 所属类型,填organization</li>
 * <li>ownerId: 公司id</li>
 * <li>subject:  主题名称</li>
 * <li>content: 祝福语</li>
 * <li>senderName:发放者姓名</li>
 * <li>senderUid:发放者userId</li>
 * <li>senderDetailId: 发放者detailId</li>
 * <li>senderName: 发放者姓名</li>
 * <li>senderAvatarUrl: 发放者头像</li>
 * <li>imgUri: 图片uri</li>
 * <li>imgUrl: 图片url</li>
 * <li>sendTime: 发送时间</li>
 * <li>coupons: 卡券列表 参考{@link com.everhomes.rest.welfare.WelfareCouponDTO}</li>
 * <li>points: 积分列表( 5.11.0暂时不上等积分系统搞好了这里要修改 )参考{@link com.everhomes.rest.welfare.WelfarePointDTO}</li>
 * </ul>
 */
public class GetUserWelfareResponse {

    private Long id;
    private String ownerType;
    private Long ownerId;
    private String subject;
    private String content;
    private String senderName;
    private Long senderUid;
    private Long senderDetailId;
	private String senderAvatarUrl;
    private String imgUri;
    private String imgUrl;
    private Long sendTime;
    private List<WelfareCouponDTO> coupons;


	public GetUserWelfareResponse() {

	}
 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}


	public Long getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getSenderName() {
		return senderName;
	}


	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}


	public Long getSenderUid() {
		return senderUid;
	}


	public void setSenderUid(Long senderUid) {
		this.senderUid = senderUid;
	}


	public Long getSenderDetailId() {
		return senderDetailId;
	}


	public void setSenderDetailId(Long senderDetailId) {
		this.senderDetailId = senderDetailId;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getSendTime() {
		return sendTime;
	}


	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public List<WelfareCouponDTO> getCoupons() {
		return coupons;
	}


	public void setCoupons(List<WelfareCouponDTO> coupons) {
		this.coupons = coupons;
	}

	public String getSenderAvatarUrl() {
		return senderAvatarUrl;
	}

	public void setSenderAvatarUrl(String senderAvatarUrl) {
		this.senderAvatarUrl = senderAvatarUrl;
	}
}
