package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>organizationId: 公司 id</li>
 * <li>remindDay: 提醒日, 参考{@link com.everhomes.rest.archives.WeekDaysList}</li>
 * <li>remindTime: 提醒时间点</li>
 * <li>detailIds: 成员 detailId(List)</li>
 * <li>mailFlag: 邮件提醒 0-否,1-是</li>
 * <li>messageFlag: 消息提醒 0-否,1-是</li>
 * </ul>
 */
public class ArchivesNotificationCommand {

    private Integer namespaceId;

    private Long organizationId;

    private Integer remindDay;

    private Integer remindTime;

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Byte mailFlag;

    private Byte messageFlag;

    public ArchivesNotificationCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getRemindDay() {
        return remindDay;
    }

    public void setRemindDay(Integer remindDay) {
        this.remindDay = remindDay;
    }

    public Integer getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Integer remindTime) {
        this.remindTime = remindTime;
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
