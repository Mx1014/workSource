package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>notifyDay: 提醒日, 参考{@link com.everhomes.rest.archives.WeekDaysList}</li>
 * <li>notifyTime: 提醒时间点</li>
 * <li>targets: 成员列表, 参考 {@link com.everhomes.rest.archives.ArchivesNotificationTarget}</li>
 * <li>mailFlag: 邮件提醒 0-否,1-是</li>
 * <li>messageFlag: 消息提醒 0-否,1-是</li>
 * </ul>
 */
public class ArchivesNotificationDTO {

    private Long organizationId;

    private Integer notifyDay;

    private Integer notifyTime;

    @ItemType(ArchivesNotificationTarget.class)
    private List<ArchivesNotificationTarget> targets;

    private Byte mailFlag;

    private Byte messageFlag;

    public ArchivesNotificationDTO() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getNotifyDay() {
        return notifyDay;
    }

    public void setNotifyDay(Integer notifyDay) {
        this.notifyDay = notifyDay;
    }

    public Integer getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Integer notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Byte getMailFlag() {
        return mailFlag;
    }

    public void setMailFlag(Byte mailFlag) {
        this.mailFlag = mailFlag;
    }

    public Byte getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(Byte messageFlag) {
        this.messageFlag = messageFlag;
    }

    public List<ArchivesNotificationTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<ArchivesNotificationTarget> targets) {
        this.targets = targets;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
