package com.everhomes.general_form;

import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.ListGeneralFormValResponse;
import com.everhomes.rest.general_approval.SearchFormValsCommand;

import java.util.List;

public interface GeneralFormSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<GeneralFormVal> generalFormVal);
    void feedDoc(GeneralFormVal generalFormVal);
    void syncFromDb();
    ListGeneralFormValResponse queryContracts(SearchFormValsCommand cmd);
}
