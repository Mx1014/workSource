package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul><li>meetingRoomName: 会议室名称</li>
 * <li>subject: 会议主题</li>
 * <li>content: 会议描述</li>
 * <li>markLabel: 状态标签</li>
 * <li>markColor: 状态标签色值</li></ul>
 */
public class MeetingDashboardDTO {
    private Long id;
    private Long meetingRoomId;
    private String meetingRoomName;
    private String subject;
    private String content;
    private String markLabel;
    private String markColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getMeetingRoomName() {
        return meetingRoomName;
    }

    public void setMeetingRoomName(String meetingRoomName) {
        this.meetingRoomName = meetingRoomName;
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

    public String getMarkLabel() {
        return markLabel;
    }

    public void setMarkLabel(String markLabel) {
        this.markLabel = markLabel;
    }

    public String getMarkColor() {
        return markColor;
    }

    public void setMarkColor(String markColor) {
        this.markColor = markColor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
