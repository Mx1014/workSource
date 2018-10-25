package com.everhomes.user.smartcard;

import java.util.List;

import com.everhomes.rest.user.SmartCardHandler;

public interface SmartCardModuleManager {

	SmartCardModuleInfo getModule(Long moduleId);

	List<SmartCardModuleInfo> getModules();
	
	/**
	 * 创建主页的一卡通二维码
	 * @return
	 */
	List<SmartCardHandler> generateSmartCards(SmartCardProcessorContext ctx);
	
	/**
	 * 每个单独独立的二维码
	 * @return
	 */
	List<SmartCardHandler> generateStandaloneCards(SmartCardProcessorContext ctx);

}
