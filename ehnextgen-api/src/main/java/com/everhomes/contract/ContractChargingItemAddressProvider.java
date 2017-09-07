package com.everhomes.contract;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/16.
 */
public interface ContractChargingItemAddressProvider {
    void createContractChargingItemAddress(ContractChargingItemAddress address);
    void updateContractChargingItemAddress(ContractChargingItemAddress address);
    void deleteContractChargingItemAddress(ContractChargingItemAddress address);
    ContractChargingItemAddress findById(Long id);
    List<ContractChargingItemAddress> findByItemId(Long itemId);
}
