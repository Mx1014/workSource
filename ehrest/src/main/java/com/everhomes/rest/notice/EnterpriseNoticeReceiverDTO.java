package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>noticeId : 公告ID</li>
 * <li>name : 员工或者部门名称</li>
 * <li>receiverId : 公告接收人员或部门ID</li>
 * <li>receiverType : 公告接收ID类型，请参考{@link com.everhomes.rest.notice.EnterpriseNoticeReceiverType}</li>
 * </ul>
 */
public class EnterpriseNoticeReceiverDTO {
    private Long noticeId;
    private String name;
    private Long receiverId;
    private String receiverType;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
