package com.everhomes.rest.welfare;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 福利Id 有就填没有不填</li>
 * <li>organizationId: 公司id 必填</li>
 * <li>subject:  主题名称 必填</li>
 * <li>content: 祝福语 必填</li>
 * <li>senderName:发放者姓名 必填</li>
 * <li>senderUid:发放者userId 可不填</li>
 * <li>senderDetailId: 发放者detailId 必填</li>
 * <li>senderName: 发放者姓名 必填</li>
 * <li>imgUri: 图片uri 必填</li>
 * <li>imgUrl: 图片url 只读项展示用</li>
 * <li>imgName: 图片名称 只读项展示用</li>
 * <li>imgSize: 图片大小(字节) 只读项展示用</li>
 * <li>status: 状态:0-草稿 1-已发送 只读项</li>
 * <li>sendTime: 发送时间 只读项</li>
 * <li>creatorName: 创建者 只读项</li>
 * <li>creatorUid: 创建者uId 只读项</li>
 * <li>createTime: 创建时间 只读项</li>
 * <li>operatorName: 操作者 只读项</li>
 * <li>operatorUid: 操作者uId 只读项</li>
 * <li>updateTime: 操作时间 只读项</li>
 * <li>receivers: 接收人列表 参考{@link com.everhomes.rest.welfare.WelfareReceiverDTO}</li>
 * <li>coupons: 卡券列表 参考{@link com.everhomes.rest.welfare.WelfareCouponDTO}</li>
 * <li>points: 积分列表 参考{@link com.everhomes.rest.welfare.WelfarePointDTO}</li>
 * </ul>
 */
public class WelfaresDTO {
    private Long id;
    private Long organizationId;
    private String subject;
    private String content;
    private String senderName;
    private Long senderUid;
    private Long senderDetailId;
    private String imgUri;
	private String imgUrl;
	private String imgName;
	private Integer imgSize;
    private Byte status;
    private Long sendTime;
    private String creatorName;
    private Long creatorUid;
    private Long createTime;
    private String operatorName;
    private Long operatorUid;
    private Long updateTime;
    private List<WelfareReceiverDTO> receivers;
    private List<WelfareCouponDTO> coupons;

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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public List<WelfareReceiverDTO> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<WelfareReceiverDTO> receivers) {
		this.receivers = receivers;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public Integer getImgSize() {
		return imgSize;
	}

	public void setImgSize(Integer imgSize) {
		this.imgSize = imgSize;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<WelfareCouponDTO> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<WelfareCouponDTO> coupons) {
		this.coupons = coupons;
	}
}
