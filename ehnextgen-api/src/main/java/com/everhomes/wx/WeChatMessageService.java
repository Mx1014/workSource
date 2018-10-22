package com.everhomes.wx;


import com.everhomes.rest.wx.MessageItem;

import java.util.HashMap;
import java.util.List;

public interface WeChatMessageService {

	void sendTemplateMessage(List<Long> userIds, String title, String message, String routerUrl);

	void sendTemplateMessage(String userOpenId, String templateId, String topColor, HashMap<String, MessageItem> params, String routerUrl);
}
