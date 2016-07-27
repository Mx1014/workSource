package com.everhomes.statistics.transaction;

import java.util.Calendar;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StatTransactionScheduleJob extends QuartzJobBean{

	private static final Logger LOGGER = LoggerFactory.getLogger(StatTransactionScheduleJob.class);
	
	public static final String SCHEDELE_NAME = "stat-transaction-";
	
	public static String CRON_EXPRESSION = "0 0 1 * * ?";
	
	@Autowired
	private StatTransactionService statTransactionService;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		LOGGER.debug("start schedele job, excute task date = {}", calendar.getTime());
		
		Long startDate = calendar.getTimeInMillis();
		Long endDate = calendar.getTimeInMillis();
		//执行任务
		List<StatTaskLog> statTaskLogs =  statTransactionService.excuteSettlementTask(startDate, endDate);
		
		LOGGER.debug("schedele job result: {}", statTaskLogs);
	}
}
