package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>emailList: 邮箱地址列表</li>
 * <li>messageList: 消息id列表</li>
 * </ul>
 */
public class ArchivesNotificationTarget {

    @ItemType(String.class)
    private List<String> emailList;

    @ItemType(Long.class)
    private List<Long> messageList;

    public ArchivesNotificationTarget() {
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public List<Long> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Long> messageList) {
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
