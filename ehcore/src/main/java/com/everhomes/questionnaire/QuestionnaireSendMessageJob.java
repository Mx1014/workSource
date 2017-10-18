// @formatter:off
package com.everhomes.questionnaire;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2017/10/18 18:36
 */
public class QuestionnaireSendMessageJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionnaireSendMessageJob.class);

    public QuestionnaireSendMessageJob() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
