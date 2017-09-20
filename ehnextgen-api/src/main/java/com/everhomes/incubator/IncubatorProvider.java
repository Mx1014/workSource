package com.everhomes.incubator;

import java.util.List;


public interface IncubatorProvider {
    void createIncubatorApply(IncubatorApply incubatorApply);
    void updateIncubatorApply(IncubatorApply incubatorApply);
    List<IncubatorApply> listIncubatorApplies(Integer namespaceId, Long applyUserId, String keyWord, Byte approveStatus, Byte needReject, Integer pageOffset, Integer pageSize, Byte orderBy);
    List<IncubatorProjectType> listIncubatorProjectType();

    IncubatorApply findIncubatorApplyById(Long id);

    void createAttachment(IncubatorApplyAttachment attachment);

    IncubatorApplyAttachment findByAttachmentId(Long id);

    List<IncubatorApplyAttachment> listAttachmentsByApplyId(Long applyId, Byte type);
}
