package com.everhomes.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRoutingContext {
    private Map<Long, Long> sendUsers;
    //当前是否运行在异步环境下
    private boolean async;
    private List<Long> includeUsers;
    private List<Long> excludeUsers;

    public MessageRoutingContext() {
        sendUsers = new HashMap<Long, Long>();
        async = false;
        includeUsers = null;
        excludeUsers = null;
    }
    
    public Map<Long, Long> getSendUsers() {
        return sendUsers;
    }
    
    /**
     * 确认是否发消息
     * @param userId
     * @return true 表示已经发消息，不在往此用户不发消息
     */
    public Boolean checkAndAdd(Long userId) {
        Long l = sendUsers.get(userId);
        if(null == l) {
            sendUsers.put(userId, new Long(1));
            return false;
        } else {
            l += 1;
            sendUsers.put(userId, l);
            return true;
            }
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public List<Long> getIncludeUsers() {
        return includeUsers;
    }

    public void setIncludeUsers(List<Long> includeUsers) {
        this.includeUsers = includeUsers;
    }

    public List<Long> getExcludeUsers() {
        return excludeUsers;
    }

    public void setExcludeUsers(List<Long> excludeUsers) {
        this.excludeUsers = excludeUsers;
        for(int i = 0; i < excludeUsers.size(); i++) {
            this.checkAndAdd(excludeUsers.get(i));
        }
    }
   
    
}
