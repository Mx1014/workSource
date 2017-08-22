package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remindTime: 提醒时间</li>
 * <li>remindEmails: 邮箱</li>
 * </ul>
 */
public class RemindArchivesEmployeeCommand {

    private String remindTime;

    private String remindEmails;

    public RemindArchivesEmployeeCommand() {
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getRemindEmails() {
        return remindEmails;
    }

    public void setRemindEmails(String remindEmails) {
        this.remindEmails = remindEmails;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
