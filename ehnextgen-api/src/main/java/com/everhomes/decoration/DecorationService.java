package com.everhomes.decoration;

import com.everhomes.rest.decoration.*;

public interface DecorationService {

    DecorationIllustrationDTO getIllustration(GetIlluStrationCommand cmd);

    void setIllustration(UpdateIllustrationCommand cmd);

    DecorationWorkerDTO updateWorker(UpdateWorkerCommand cmd);

}
