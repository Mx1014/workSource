package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.rest.yellowPage.AllianceCommonCommand;

public interface AllianceOperateServiceProvider {

	void createOperateService(AllianceOperateService createItem);

	void updateOperateService(AllianceOperateService updateItem);

	void deleteOperateService(Long itemId);

	AllianceOperateService getOperateService(Long itemId);

	void updateOperateServiceOrder(Long itemId, Long defaultOrderId);

	List<AllianceOperateService> listOperateServices(AllianceCommonCommand cmd);

	void deleteOperateServices(AllianceCommonCommand cmd);

}
