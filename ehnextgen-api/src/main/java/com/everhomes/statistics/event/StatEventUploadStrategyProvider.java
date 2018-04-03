// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventUploadStrategyProvider {

	void createStatEventUploadStrategy(StatEventUploadStrategy statEventUploadStrategy);

	void updateStatEventUploadStrategy(StatEventUploadStrategy statEventUploadStrategy);

	StatEventUploadStrategy findStatEventUploadStrategyById(Long id);

    List<StatEventUploadStrategy> listUploadStrategyByOwner(String ownerType, Long ownerId);

    List<StatEventUploadStrategy> listUploadStrategyByNamespace(Integer namespaceId);

}