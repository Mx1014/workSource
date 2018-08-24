package com.everhomes.flow;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface FlowServiceMappingProvider {

    void createFlowServiceMapping(FlowServiceMapping serviceMapping);

    void updateFlowServiceMapping(FlowServiceMapping serviceMapping);

    List<FlowServiceMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

    FlowServiceMapping findById(Long id);

    FlowServiceMapping findConfigMapping(Integer namespaceId, String projectType,
                                         Long projectId, String moduleType, Long moduleId, String ownerType, Long ownerId);

    List<FlowServiceMapping> listFlowServiceMapping(Integer namespaceId, String projectType,
                                                    Long projectId, String moduleType, Long moduleId, String ownerType, Long ownerId);

    void deleteFlowServiceMappingByFlowMainId(Long flowMainId);

    List<FlowServiceMapping> listFlowServiceMappingByFlowMainId(Long flowMainId, Integer flowVersion);
}
