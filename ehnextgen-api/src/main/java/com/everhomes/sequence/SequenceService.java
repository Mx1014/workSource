package com.everhomes.sequence;

import com.everhomes.rest.admin.GetSequenceCommand;
import com.everhomes.rest.admin.GetSequenceDTO;

public interface SequenceService {
    void syncSequence();
    GetSequenceDTO getSequence(GetSequenceCommand cmd);
}
