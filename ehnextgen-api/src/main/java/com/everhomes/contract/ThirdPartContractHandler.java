package com.everhomes.contract;

import com.everhomes.rest.contract.*;

import java.util.List;

/**
 * Created by ying.xiong on 2017/11/22.
 */
public interface ThirdPartContractHandler {
    String CONTRACT_PREFIX = "contract-";
    ListContractsResponse listContracts(ListContractsCommand cmd);
    List<ContractDTO> listIndividualCustomerContracts(ListIndividualCustomerContractsCommand cmd);
    List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd);
    ContractDetailDTO findContract(FindContractCommand cmd);
}
