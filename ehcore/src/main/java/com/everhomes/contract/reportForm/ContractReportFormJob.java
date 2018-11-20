package com.everhomes.contract.reportForm;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contract.ContractService;
import com.everhomes.rest.contract.GetTotalContractStaticsCommand;

/**
 * Created by djm  2018/11/17
 */
@Component
public class ContractReportFormJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractReportFormJob.class);

    @Autowired
	private ConfigurationProvider configurationProvider;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("start ContractReportFormJob auto Reading task......");
        ContractService contractService = getContractService(111111);
        GetTotalContractStaticsCommand cmCommand = new GetTotalContractStaticsCommand();
		contractService.generateReportFormStatics(cmCommand);
        LOGGER.info("finish  ContractReportFormJob auto Reading task......");

    }
    
    private ContractService getContractService(Integer namespaceId) {
		String handler = configurationProvider.getValue(namespaceId, "contractService", "");
		return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
	}
}
