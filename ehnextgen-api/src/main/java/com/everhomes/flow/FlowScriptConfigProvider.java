package com.everhomes.flow;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.FlowScriptType;

import java.util.List;

public interface FlowScriptConfigProvider {

    Long createFlowScriptConfig(FlowScriptConfig obj);

    List<FlowScriptConfig> queryFlowScriptConfig(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

    void deleteByOwner(String ownerType, Long ownerId);

    List<FlowScriptConfig> listByOwner(String ownerType, Long ownerId);

    String getConfig(String ownerType, Long ownerId, String fieldName);

    void createFlowScriptConfigs(List<FlowScriptConfig> scriptConfigs);

    List<FlowScriptConfig> listByFlow(Long flowMainId, Integer flowVersion);

    List<FlowScriptConfig> listByModule(Long moduleId, FlowScriptType scriptType);
}
