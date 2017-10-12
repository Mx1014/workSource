package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>remindDay: 提醒日, 参考{@link com.everhomes.rest.archives.WeekDaysList}</li>
 * <li>remindTime: 提醒时间点</li>
 * <li>remindEmails: 邮箱(List)</li>
 * </ul>
 */
public class RemindArchivesEmployeeCommand {

    private Long organizationId;

    private String remindDay;

    private String remindTime;

    @ItemType(String.class)
    private List<String> remindEmails;

    public RemindArchivesEmployeeCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getRemindDay() {
        return remindDay;
    }

    public void setRemindDay(String remindDay) {
        this.remindDay = remindDay;
    }

    public List<String> getRemindEmails() {
        return remindEmails;
    }

    public void setRemindEmails(List<String> remindEmails) {
        this.remindEmails = remindEmails;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
