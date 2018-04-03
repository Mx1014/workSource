package com.everhomes.contract;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/10.
 */
public interface ContractChargingChangeProvider {
    void createContractChargingChange(ContractChargingChange change);
    void updateContractChargingChange(ContractChargingChange change);
    void deleteContractChargingChange(ContractChargingChange change);
    List<ContractChargingChange> listByContractId(Long contractId);
    ContractChargingChange findById(Long id);
}
