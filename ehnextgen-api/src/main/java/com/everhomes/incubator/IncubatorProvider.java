package com.everhomes.incubator;

import java.util.List;


public interface IncubatorProvider {
    void createIncubatorApply(IncubatorApply incubatorApply);
    void updateIncubatorApply(IncubatorApply incubatorApply);
    List<IncubatorApply> listIncubatorApplies(Integer namespaceId, Long applyUserId, String keyWord, Byte approveStatus, Byte needReject, Integer pageOffset, Integer pageSize, Byte orderBy, Byte applyType, Long startTime, Long endTime);

    List<IncubatorApply> listIncubatorAppliesByRootId(Long rootId);

    List<Long> listRootIdByUserId(Long userId);

    IncubatorApply findLatestValidByRootId(Long rootId);

    List<IncubatorApply> listIncubatorAppling(Long applyUserId, Long rootId);

    List<IncubatorProjectType> listIncubatorProjectType();

    IncubatorApply findIncubatorApplyById(Long id);

    void deleteIncubatorApplyById(Long id);

    void createAttachment(IncubatorApplyAttachment attachment);

    IncubatorApplyAttachment findByAttachmentId(Long id);

    List<IncubatorApplyAttachment> listAttachmentsByApplyId(Long applyId, Byte type);

    IncubatorApply findSameApply(IncubatorApply incubatorApply);
}
