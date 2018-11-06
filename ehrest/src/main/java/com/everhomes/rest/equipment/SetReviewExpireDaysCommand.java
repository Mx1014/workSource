package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 设置id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 项目id</li>
 * <li>reviewExpiredDays: 审批过期时间</li>
 * <li>status: 状态 参考{@link com.everhomes.rest.pmNotify.PmNotifyConfigurationStatus}</li>
 * </ul>
 **/
public class SetReviewExpireDaysCommand {

    private Long id;

    private Integer namespaceId;

    private Long communityId;

    private Byte status;

    private Integer reviewExpiredDays;

    private Long targetId;

    private String targetType;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getReviewExpiredDays() {
        return reviewExpiredDays;
    }

    public void setReviewExpiredDays(Integer reviewExpiredDays) {
        this.reviewExpiredDays = reviewExpiredDays;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
