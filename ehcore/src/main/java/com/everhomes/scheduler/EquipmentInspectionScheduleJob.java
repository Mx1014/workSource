package com.everhomes.scheduler;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.equipment.EquipmentInspectionPlans;
import com.everhomes.equipment.EquipmentProvider;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.equipment.EquipmentStandardMap;
import com.everhomes.mail.MailHandler;
import com.everhomes.repeat.RepeatService;
import com.everhomes.util.DateHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.List;

@Component
@Scope("prototype")
public class EquipmentInspectionScheduleJob extends QuartzJobBean {
	
private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentInspectionScheduleJob.class);

	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private RepeatService repeatService;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private ScheduleProvider scheduleProvider;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("EquipmentInspectionScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
		}

		//双机判断
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			//为防止时间长了的话可能会有内存溢出的可能，把每天过期的定时任务清理一下
			//scheduleProvider.unscheduleJob("EquipmentInspectionNotify ");
			closeDelayTasks();
			//createTask();
			createTaskByPlan();
		}

	}

	private void createTaskByPlan() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("EquipmentInspectionScheduleJob:createTaskByPlan.....");
		}

		List<EquipmentInspectionPlans> plans = equipmentProvider.listQualifiedEquipmentInspectionPlans();
		if (plans != null && plans.size() > 0) {
			LOGGER.info("createTaskByPlan.....plan size = {}"+plans.size());
			for (EquipmentInspectionPlans plan : plans) {
				try {
					if (checkPlanRepeat(plan)) {
                        LOGGER.info("EquipmentInspectionScheduleJob: createEquipmentTaskByPlan.");
                        equipmentService.createEquipmentTaskByPlan(plan);
                        plan.setLastCreateTasktime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        equipmentProvider.updateEquipmentInspectionPlan(plan);
                    }
				} catch (Exception e) {
                    LOGGER.error("Equipment schdule erro cause by {},planId={}", e, plan.getId());
					sendErrorMessage(e,plan.getId());
					e.printStackTrace();
				}
			}
			plans.clear();
		}
	}

	private boolean checkPlanRepeat(EquipmentInspectionPlans plan) {
		boolean isRepeat = repeatService.isRepeatSettingActive(plan.getRepeatSettingId());
		LOGGER.info("checkPlanRepeat: plans  id = " + plan.getId()
				+ "repeat setting id = "+ plan.getRepeatSettingId() + "is repeat setting active: " + isRepeat);
		return isRepeat;
	}

	private boolean checkTaskAlreadyCreated(EquipmentStandardMap equipmentStandardMap) {
		//只有一台指定的服务器会生成任务 所以不需要再做判断了
//		if(equipmentStandardMap.getLastCreateTaskTime() != null) {
//			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
//			Long hours = (now.getTime() - equipmentStandardMap.getLastCreateTaskTime().getTime())/(1000*60*60);
//
//			LOGGER.info("checkTaskAlreadyCreated: current time = " + now + ", equipmentStandardMap id: " +equipmentStandardMap.getId()
//					+ ", LastCreateTaskTime  = "+ equipmentStandardMap.getLastCreateTaskTime() + ", interval hours: " + hours);
//			// 2小时之内已生成过任务则不再生成
//			if(hours < 2L) {
//				return false;
//			}
//		}
		return true;
	}
	private void closeDelayTasks() {
		LOGGER.info("EquipmentInspectionScheduleJob: close delay tasks.");
		equipmentProvider.closeDelayTasks();
		
		LOGGER.info("EquipmentInspectionScheduleJob: close expired review tasks.");
		equipmentProvider.closeExpiredReviewTasks();
	}

	private void sendErrorMessage(Exception e, Long planId) {
		String targetEmailAddress = "rui.jia@zuolin.com";
		String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
		MailHandler handler = PlatformContext.getComponent(handlerName);
		String account = configurationProvider.getValue(0, "mail.smtp.account", "zuolin@zuolin.com");
		if ("core.zuolin.com".equals(configurationProvider.getValue(0,"home.url", ""))) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintStream stream = new PrintStream(out)) {
				e.printStackTrace(stream);
				String message = out.toString("UTF-8");
				handler.sendMail(0, account, targetEmailAddress, "Equipment Task Schedule Error,planId:", planId.toString());
				// out.reset();
				e.getCause().printStackTrace(stream);
				message = out.toString("UTF-8");
				handler.sendMail(0, account, targetEmailAddress, "Equipment Task Schedule Error.Cause By", message);
			} catch (Exception ignored) {
				//
			}
		}
	}
}
