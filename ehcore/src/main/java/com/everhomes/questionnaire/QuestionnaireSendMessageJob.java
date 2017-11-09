// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2017/10/18 18:36
 *
 * 每天晚上一点钟，查询问卷即将一天到期的问卷，然后发送消息给目标用户，让这个人填写。
 */
public class QuestionnaireSendMessageJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionnaireSendMessageJob.class);
    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private QuestionnaireAsynSendMessageService questionnaireAsynSendMessageService;

    public QuestionnaireSendMessageJob() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            questionnaireAsynSendMessageService.sendUnAnsweredTargetMessage();
        }
    }
}
