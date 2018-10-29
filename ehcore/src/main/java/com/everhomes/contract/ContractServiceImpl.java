// @formatter:off
package com.everhomes.contract;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component(ContractService.CONTRACT_PREFIX + "")
public class ContractServiceImpl extends DefaultContractServiceImpl
		implements ApplicationListener<ContextRefreshedEvent> {

<<<<<<< HEAD
}
=======
				BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(contract.getContractNumber(),contract.getId(), assetCategoryId, contract.getNamespaceId());
				contract.setRent(totalAmount);
				contractProvider.updateContract(contract);
			}
		}
	}
	
	
	
	
	
}
>>>>>>> asset7.0
