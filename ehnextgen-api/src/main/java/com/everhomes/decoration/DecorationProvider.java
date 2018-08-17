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

    List<DecorationRequest> queryDecorationRequests(Integer namespaceId,Long communityId,Long startTime,Long endTime,
                                                    String address,Byte status,String keyword,Byte cancelFlag,Integer pageSize,
                                                    ListingLocator locator);

    List<DecorationRequest> queryUserRelateRequests(Integer namespaceId,Long communityId,String phone);

    List<DecorationRequest> queryRequestsByIds(List<Long> ids ,Integer pageSize, ListingLocator locator );

    DecorationWorker getDecorationWorkerById(Long id);

    DecorationWorker queryDecorationWorker(Long requestId,String phone);

    List<DecorationWorker> listWorkersByRequestId(Long requestId, String keyWord, ListingLocator locator, Integer pageSize);

    void createDecorationWorker(DecorationWorker worker);

    void updateDecorationWorker(DecorationWorker worker);

    void deleteDecorationWorkerById(Long id);

    void createDecorationCompany(DecorationCompany company);

    List<DecorationCompany> listDecorationCompanies(Integer namespaceId,String keyword);

    DecorationCompany getDecorationCompanyById(Long id);

    void createDecorationRequest(DecorationRequest request);

    void updateDecorationRequest(DecorationRequest request);

    List<DecorationFee> listDecorationFeeByRequestId(Long requestId);

    void deleteDecorationFeeByRequestId(Long requestId);

    void createDecorationFee(DecorationFee fee);

    void createApprovalVals(DecorationApprovalVal val);

    void deleteApprovalValByRequestId(Long requestId, Long approvalId);

    void updateApprovalVals(DecorationApprovalVal val);

    DecorationApprovalVal getApprovalValById(Long id);

    List<DecorationApprovalVal> listApprovalVals(Long requestId,Long approvalId);

    List<DecorationCompanyChief> listChiefsByCompanyId(Long companyId);

    void createCompanyChief(DecorationCompanyChief chief);

    void updateCompanyChief(DecorationCompanyChief chief);

}
