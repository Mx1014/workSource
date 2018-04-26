package com.everhomes.contract;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/2.
 */
public interface ContractPaymentPlanProvider {
    void createContractPaymentPlan(ContractPaymentPlan plan);
    void deleteContractPaymentPlan(ContractPaymentPlan plan);
    List<ContractPaymentPlan> listByContractId(Long contractId);
    ContractPaymentPlan findById(Long id);
}
