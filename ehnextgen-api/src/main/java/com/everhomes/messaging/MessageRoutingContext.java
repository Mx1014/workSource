package com.everhomes.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRoutingContext {
    private Map<String, Long> sendUsers;
    //当前是否运行在异步环境下
    private boolean async;
    private List<Long> includeUsers;
    private List<Long> excludeUsers;
    private Map<Long, Long> excludeMap;

    public MessageRoutingContext() {
        sendUsers = new HashMap<String, Long>();
        async = false;
        includeUsers = null;
        excludeUsers = null;
        excludeMap = new HashMap<Long, Long>();
    }
    
    public Map<String, Long> getSendUsers() {
        return sendUsers;
    }
    
    /**
     * 确认是否发消息
     * @param userId
     * @return false 表示已经发消息，不在往此用户发消息
     */
    public Boolean checkAndAdd(Long userId, Integer loginId) {
        Long ex = null;
        if(excludeUsers != null) {
            ex = excludeMap.get(userId);
        }
        if(ex != null) {
            //ignore this user.
            return false;
        }
        
        String key = String.format("%d:%d", userId, loginId);
        Long l = sendUsers.get(key);
        if(null == l) {
            sendUsers.put(key, new Long(1));
            return true;
        } else {
            l += 1;
            sendUsers.put(key, l);
            return false;
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
            excludeMap.put(excludeUsers.get(i), 1l);
        }
    }
   
    
}
