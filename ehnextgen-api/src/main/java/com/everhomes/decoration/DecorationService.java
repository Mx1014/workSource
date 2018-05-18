package com.everhomes.decoration;

import com.everhomes.rest.decoration.*;

import java.util.List;

public interface DecorationService {

    DecorationIllustrationDTO getIllustration(GetIlluStrationCommand cmd);

    void setIllustration(UpdateIllustrationCommand cmd);

    DecorationWorkerDTO updateWorker(UpdateWorkerCommand cmd);

    ListWorkersResponse listWorkers(ListWorkersCommand cmd);

    void deleteWorker(DeleteWorkerCommand cmd);

    DecorationIllustrationDTO createRequest(CreateRequestCommand cmd);

}
