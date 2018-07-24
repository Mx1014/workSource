package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flag: 0-不需要提醒 1-需要 参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>title: 提醒标题</li>
 * <li>content: 提醒正文</li>
 * </ul>
 */
public class GeneralFormReminderDTO {

    private Byte flag;

    private String title;

    private String content;

    public GeneralFormReminderDTO() {
    }

    public GeneralFormReminderDTO(Byte flag) {
        this.flag = flag;
    }

    public Byte getFlag() {
        return flag;
    }

    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
