package com.everhomes.contract;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/16.
 */
public interface ContractAttachmentProvider {
    void createContractAttachment(ContractAttachment attachment);
    void deleteContractAttachment(ContractAttachment attachment);
    List<ContractAttachment> listByContractId(Long contractId);
    ContractAttachment findById(Long id);
}
