package com.everhomes.relocation;

import java.util.List;

/**
 * @author sw on 2017/11/21.
 */
public interface RelocationProvider {

    RelocationRequest findRelocationRequestById(Long id);

    void createRelocationRequest(RelocationRequest request);

    void updateRelocationRequest(RelocationRequest request);

    List<RelocationRequest> searchRelocationRequests(Integer namespaceId, String ownerType, Long ownerId, String keyword,
                                                     Long startDate, Long endDate, Byte status, Long orgId, Long pageAnchor, Integer pageSize);

    List<RelocationStatistics> queryRelocationStatistics(Integer namespaceId, String ownerType, Long ownerId,Long startDate, Long endDate);

    void createRelocationRequestItem(RelocationRequestItem item);

    void updateRelocationRequestItemsStatus(Integer namespaceId, Long requestId, Byte status);

    List<RelocationRequestItem> listRelocationRequestItems(Long requestId);

    void createRelocationRequestAttachment(RelocationRequestAttachment attachment);

    List<RelocationRequestAttachment> listRelocationRequestAttachments(String ownerType, Long ownerId);
}
