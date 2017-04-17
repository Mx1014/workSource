package com.everhomes.rest.videoconf;

public interface VideoconfNotificationTemplateCode {

	static final String SCOPE = "videoconf.notification";
	    
    static final int VIDEOCONF_MSG_SUBJECT = 1; // 信息主题
    static final int VIDEOCONF_CONFTIME = 2; // 会议时间
    static final int VIDEOCONF_CONFDESCRIBTION = 3; // 会议描述

    static final int VIDEOCONF_JOINURL_TEMPLATE = 4; // 加入会议协议格式

}
