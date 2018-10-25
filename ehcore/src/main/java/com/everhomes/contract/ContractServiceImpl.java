// @formatter:off
package com.everhomes.contract;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component(ContractService.CONTRACT_PREFIX + "")
public class ContractServiceImpl extends DefaultContractServiceImpl
		implements ApplicationListener<ContextRefreshedEvent> {

}
