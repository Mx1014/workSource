package com.everhomes.decoration;

import com.everhomes.rest.decoration.*;
import com.everhomes.rest.flow.FlowCaseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DecorationService {

    DecorationIllustrationDTO getIllustration(GetIlluStrationCommand cmd);

    DecorationIllustrationDTO getRefundInfo(RequestIdCommand cmd);

    GetDecorationFeeResponse getFeeInfo(RequestIdCommand cmd);

    void setIllustration(UpdateIllustrationCommand cmd);

    void setApplySetting(UpdateApplySettingCommand cmd);

    DecorationWorkerDTO updateWorker(UpdateWorkerCommand cmd);

    ListWorkersResponse listWorkers(ListWorkersCommand cmd);

    void deleteWorker(DeleteWorkerCommand cmd);

    DecorationRequestDTO createRequest(CreateRequestCommand cmd);

    SearchRequestResponse searchRequest(SearchRequestsCommand cmd);

    SearchRequestResponse searchUserRelateRequest(ListUserRequestsCommand cmd);

    DecorationRequestDTO getRequestDetail(GetDecorationDetailCommand cmd);

    String convertAddress(String address);

    List<FlowCaseEntity> getFormEntitiesByApprovalVal(DecorationApprovalVal val);

    void cancelRequest(RequestIdCommand cmd);

    void modifyFee(ModifyFeeCommand cmd);

    void modifyRefoundAmount(ModifyRefoundAmountCommand cmd);

    void confirmRefound(RequestIdCommand cmd);

    void confirmFee(RequestIdCommand cmd);

    Long postApprovalForm(PostApprovalFormCommand cmd);

    void DecorationApplySuccess(Long requestId);

    void FileApprovalSuccess(Long requestId);

    void DecorationCheckSuccess(Long requestId);

    DecorationFlowCaseDTO completeDecoration(RequestIdCommand cmd);

    List<DecorationCompanyDTO> listDecorationCompanies (ListDecorationCompaniesCommand cmd);

    QrDetailDTO getQrDetail(GetLicenseCommand cmd);

    DecorationLicenseDTO getLicense(GetLicenseCommand cmd);

    GetUserMemberGroupResponse getUserMemberGroup(GetUserMemberGroupCommand cmd);

    void exportRequests(SearchRequestsCommand cmd, HttpServletResponse response);

    void exportWorkers(ListWorkersCommand cmd, HttpServletResponse response);

    DecorationFlowCaseDTO getApprovalVals(getApprovalValsCommand cmd);

}
