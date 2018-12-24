package com.everhomes.openapi;

import java.util.Map;

/**
 * Created by momoubin  2018/12/19 16 :41
 */

public interface OpenMessageService {
    public void sendSystemMessageToUser(Long userId, String content, Map<String, String> meta);
}
