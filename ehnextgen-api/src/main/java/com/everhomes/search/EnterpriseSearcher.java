package com.everhomes.search;

import java.util.List;

import com.everhomes.enterprise.Enterprise;
import com.everhomes.rest.enterprise.SearchEnterpriseCommand;
import com.everhomes.rest.enterprise.SetCurrentEnterpriseCommand;
import com.everhomes.rest.search.GroupQueryResult;

public interface EnterpriseSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Enterprise> enterprise);
    void feedDoc(Enterprise enterprise);
    void syncFromDb();
    GroupQueryResult query(SearchEnterpriseCommand cmd); 
}
