package com.everhomes.rest.notice;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.Date;
import java.util.List;

/**
 * <p>企业公告详情信息</p>
 * <ul>
 * <li>id : 公告主键</li>
 * <li>title : 公告标题</li>
 * <li>summary : 内容摘要：用户输入或者从正文截取不超过280字节</li>
 * <li>contentType : 正文内容类型，类型定义请查看{@link EnterpriseNoticeContentType}</li>
 * <li>content : 公告正文</li>
 * <li>publisher :公告发布者，注意不是登录人，创建公告时指定的签名</li>
 * <li>secretFlag : 0-公开，1-保密；类型定义请查看{@link EnterpriseNoticeSecretFlag}</li>
 * <li>status : 状态 : 0-已删除, 1-草稿, 2-已发送, 3-已撤销；类型定义请查考{@link EnterpriseNoticeStatus}</li>
 * <li>creatorUid : 创建者</li>
 * <li>createTime : 创建时间</li>
 * <li>updateUid : 更新者</li>
 * <li>updateTime : 更新时间</li>
 * <li>operatorName : 操作人姓名</li>
 * <li>webShareUrl: 公告分享的链接</li>
 * <li>ownerId: 公司ID</li>
 * <li>stickFlag: 置顶状态 1置顶 0非置顶</li>
 * <li>receivers : 公告的发送范围：发送给部门或者员工，参考{@link EnterpriseNoticeReceiverDTO}</li>
 * <li>attachments : 已上传的附件，复制公告的时候会使用到，参考{@link EnterpriseNoticeAttachmentDTO }</li>
 * </ul>
 */
public class EnterpriseNoticeDTO {
    private Long id;
    private String title;
    private String summary;
    private String contentType;
    private String content;
    private String publisher;
    private Byte secretFlag;
    private Byte status;
    private Long ownerId;
    private Long creatorUid;
    private Date createTime;
    private Long updateUid;
    private Date updateTime;
    private String operatorName;
    private String webShareUrl;
    private Byte stickFlag;
    @ItemType(value = EnterpriseNoticeReceiverDTO.class)
    private List<EnterpriseNoticeReceiverDTO> receivers;
    @ItemType(value = EnterpriseNoticeAttachmentDTO.class)
    private List<EnterpriseNoticeAttachmentDTO> attachments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Byte getSecretFlag() {
        return secretFlag;
    }

    public void setSecretFlag(Byte secretFlag) {
        this.secretFlag = secretFlag;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<EnterpriseNoticeReceiverDTO> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<EnterpriseNoticeReceiverDTO> receivers) {
        this.receivers = receivers;
    }

    public List<EnterpriseNoticeAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EnterpriseNoticeAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getWebShareUrl() {
        return webShareUrl;
    }

    public void setWebShareUrl(String webShareUrl) {
        this.webShareUrl = webShareUrl;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getStickFlag() {
		return stickFlag;
	}

	public void setStickFlag(Byte stickFlag) {
		this.stickFlag = stickFlag;
	}
}
