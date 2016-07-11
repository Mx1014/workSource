package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.videoconf.ListEnterpriseVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListEnterpriseVideoConfAccountResponse;
import com.everhomes.videoconf.ConfAccounts;
public interface ConfAccountSearcher {

	void deleteById(Long id);
	void bulkUpdate(List<ConfAccounts> accounts);
    void feedDoc(ConfAccounts account);
    void syncFromDb();
    ListEnterpriseVideoConfAccountResponse query(ListEnterpriseVideoConfAccountCommand cmd);

}
