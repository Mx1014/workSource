package com.everhomes.decoration;

import com.everhomes.rest.decoration.DecorationIllustrationDTO;
import com.everhomes.rest.decoration.GetIlluStrationCommand;

public interface DecorationService {

    DecorationIllustrationDTO getIllustration(GetIlluStrationCommand cmd);
}
