package com.everhomes.flow;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface FlowScriptConfigProvider {

    Long createFlowScriptConfig(FlowScriptConfig obj);

    List<FlowScriptConfig> queryFlowScriptConfig(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

    void deleteByOwner(String ownerType, Long ownerId);

    List<FlowScriptConfig> listByOwner(String ownerType, Long ownerId);

    String getConfig(Long flowMainId, Integer flowVersion, String fieldName);

    void createFlowScriptConfigs(List<FlowScriptConfig> scriptConfigs);

    List<FlowScriptConfig> listByFlow(Long flowMainId, Integer flowVersion);
}
