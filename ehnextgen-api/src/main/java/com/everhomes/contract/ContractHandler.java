package com.everhomes.contract;

import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;

/**
 * Created by ying.xiong on 2017/8/14.
 */
public interface ContractHandler {
    String CONTRACT_PREFIX = "contract-";
    ListContractsResponse listContracts(ListContractsCommand cmd);
}
