package com.everhomes.decoration;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.decoration.DecorationAttachmentDTO;

import java.util.List;

public interface DecorationProvider {

    void createDecorationSetting(DecorationSetting setting);

    void updateDecorationSetting(DecorationSetting setting);

    DecorationSetting getDecorationSetting(Integer namespaceId,Long communityId,String ownerType,Long ownerId);

    DecorationSetting getDecorationSettingById(Long id);

    void createDecorationAttachment(DecorationAttachment attachment);

    List<DecorationAttachmentDTO> listDecorationAttachmentBySettingId(Long settingId);

    void deleteDecorationBySettingId(Long settingId);

    DecorationRequest getRequestById(Long requestId);

    DecorationWorker getDecorationWorkerById(Long id);

    List<DecorationWorker> listWorkersByRequestId(Long requestId, String keyWord, ListingLocator locator, Integer pageSize);

    void createDecorationWorker(DecorationWorker worker);

    void updateDecorationWorker(DecorationWorker worker);

    void deleteDecorationWorkerById(Long id);

    DecorationCompany getDecorationCompanyById(Long id);

    void createDecorationRequest(DecorationRequest request);

    void updateDecorationRequest(DecorationRequest request);

    List<DecorationFee> listDecorationFeeByRequestId(Long requestId);

    void deleteDecorationFeeByRequestId(Long requestId);

    void createDecorationFee(DecorationFee fee);
}
