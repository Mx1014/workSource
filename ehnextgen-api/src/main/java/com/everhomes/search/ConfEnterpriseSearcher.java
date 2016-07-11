package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.videoconf.ListEnterpriseWithVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListEnterpriseWithVideoConfAccountResponse;
import com.everhomes.videoconf.ConfEnterprises;

public interface ConfEnterpriseSearcher {

	void deleteById(Long id);
    void bulkUpdate(List<ConfEnterprises> enterprises);
    void feedDoc(ConfEnterprises enterprise);
    void syncFromDb();
    ListEnterpriseWithVideoConfAccountResponse query(ListEnterpriseWithVideoConfAccountCommand cmd);
}
