package com.everhomes.decoration;

import com.everhomes.rest.decoration.DecorationAttachmentDTO;

import java.util.List;

public interface DecorationProvider {


    Long createDecorationSetting(DecorationSetting setting);

    void updateDecorationSetting(DecorationSetting setting);

    DecorationSetting getDecorationSetting(Integer namespaceId,Long communityId,String ownerType,Long ownerId);

    DecorationSetting getDecorationSettingById(Long id);

    void createDecorationAttachment(DecorationAttachment attachment);

    List<DecorationAttachmentDTO> listDecorationAttachmentBySettingId(Long settingId);

    void deleteDecorationBySettingId(Long settingId);

    DecorationRequest getRequestById(Long requestId);

    DecorationWorker getDecorationWorkerById(Long id);

    List<DecorationWorker> listWorkersByRequestId(Long requestId);

    Long createDecorationWorker(DecorationWorker worker);

    void updateDecorationWorker(DecorationWorker worker);

    void deleteDecorationWorkerById(Long id);

}
