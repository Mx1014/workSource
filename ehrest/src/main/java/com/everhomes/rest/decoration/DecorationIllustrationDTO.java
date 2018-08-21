package com.everhomes.rest.decoration;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>id</li>
 * <li>communityId</li>
 * <li>content：说明</li>
 * <li>phone：联系电话</li>
 * <li>address：资料提交地址</li>
 * <li>ownerType：装修说明类型  参考{@link com.everhomes.rest.decoration.IllustrationType}</li>
 * <li>ownerId：审批id</li>
 * <li>attachments：附件列表</li>
 * <li>refundAmount：退款金额</li>
 * <li>latitude longitude：经纬度</li>
 * </ul>
 */
public class DecorationIllustrationDTO {

    private Long id;
    private Long communityId;
    private String content;
    private String phone;
    private String address;
    private Double longitude;
    private Double latitude;
    private String ownerType;
    private Long ownerId;
    private List<DecorationAttachmentDTO> attachments;
    private BigDecimal refundAmount;
    private String refoundComment;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<DecorationAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<DecorationAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getRefoundComment() {
        return refoundComment;
    }

    public void setRefoundComment(String refoundComment) {
        this.refoundComment = refoundComment;
    }
}
