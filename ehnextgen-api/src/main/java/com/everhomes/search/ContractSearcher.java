package com.everhomes.search;

import com.everhomes.openapi.Contract;
import com.everhomes.rest.contract.ListContractsResponse;
import com.everhomes.rest.contract.OpenapiListContractsCommand;
import com.everhomes.rest.contract.SearchContractCommand;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/17.
 */
public interface ContractSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Contract> contracts);
    void feedDoc(Contract contract);
    void syncFromDb();
    ListContractsResponse queryContracts(SearchContractCommand cmd);
    ListContractsResponse openapiListContracts(OpenapiListContractsCommand cmd);
}
