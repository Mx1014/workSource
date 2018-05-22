package com.everhomes.decoration;

import com.everhomes.rest.decoration.*;

import java.util.List;

public interface DecorationService {

    DecorationIllustrationDTO getIllustration(GetIlluStrationCommand cmd);

    DecorationIllustrationDTO getRefundInfo(RequestIdCommand cmd);

    GetDecorationFeeResponse getFeeInfo(RequestIdCommand cmd);

    void setIllustration(UpdateIllustrationCommand cmd);

    DecorationWorkerDTO updateWorker(UpdateWorkerCommand cmd);

    ListWorkersResponse listWorkers(ListWorkersCommand cmd);

    void deleteWorker(DeleteWorkerCommand cmd);

    DecorationRequestDTO createRequest(CreateRequestCommand cmd);

    void cancelRequest(RequestIdCommand cmd);

    void modifyFee(ModifyFeeCommand cmd);

    void modifyRefoundAmount(ModifyRefoundAmountCommand cmd);

    void confirmRefound(RequestIdCommand cmd);

    void confirmFee(RequestIdCommand cmd);

    void postApprovalForm(PostApprovalFormCommand cmd);

}
