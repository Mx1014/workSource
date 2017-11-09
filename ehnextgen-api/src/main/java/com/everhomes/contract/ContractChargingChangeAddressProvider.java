package com.everhomes.contract;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/10.
 */
public interface ContractChargingChangeAddressProvider {
    void createContractChargingChangeAddress(ContractChargingChangeAddress address);
    void updateContractChargingChangeAddress(ContractChargingChangeAddress address);
    void deleteContractChargingChangeAddress(ContractChargingChangeAddress address);
    ContractChargingChangeAddress findById(Long id);
    List<ContractChargingChangeAddress> findByChangeId(Long changeId);
}
