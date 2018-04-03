package com.everhomes.contract;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/16.
 */
public interface ContractChargingItemProvider {
    void createContractChargingItem(ContractChargingItem item);
    void updateContractChargingItem(ContractChargingItem item);
    void deleteContractChargingItem(ContractChargingItem item);
    List<ContractChargingItem> listByContractId(Long contractId);
    ContractChargingItem findById(Long id);
}
