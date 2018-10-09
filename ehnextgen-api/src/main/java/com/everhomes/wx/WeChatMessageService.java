package com.everhomes.wx;


import com.everhomes.rest.wx.MessageItem;

import java.util.HashMap;

public interface WeChatMessageService {

	void sendTemplateMessage(String userOpenId, String templateId, String topColor, HashMap<String, MessageItem> params, String routerUrl);
}
