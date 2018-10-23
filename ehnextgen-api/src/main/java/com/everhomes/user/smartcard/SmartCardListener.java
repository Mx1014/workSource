package com.everhomes.user.smartcard;

import java.util.List;

import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.rest.user.SmartCardHandler;

public interface SmartCardListener {
	/**
	 * 模块初始化
	 * @return 返回模块信息
	 */
	SmartCardModuleInfo initModule();
	
	/**
	 * 创建主页的一卡通二维码
	 * @return
	 */
	public List<SmartCardHandler> generateSmartCards(SmartCardProcessorContext ctx);
	
	/**
	 * 每个单独独立的二维码
	 * @return
	 */
	public List<SmartCardHandler> generateStandaloneCards(SmartCardProcessorContext ctx);
}
