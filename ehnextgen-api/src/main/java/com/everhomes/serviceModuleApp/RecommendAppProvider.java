// @formatter:off
package com.everhomes.serviceModuleApp;

import java.util.List;


public interface RecommendAppProvider {

	void createRecommendApp(RecommendApp recommendApp);

	void updateRecommendApp(RecommendApp recommendApp);

	RecommendApp findRecommendAppById(Long id);

	List<RecommendApp> listRecommendApps(Byte scopeType, Long scopeId, Long appId);

	void delete(Long id);

	void delete(List<Long> ids);

	void deleteByScope(Byte scopeType, Long scopeId);

	Integer findMaxOrder(Byte scopeType, Long scopeId);
}