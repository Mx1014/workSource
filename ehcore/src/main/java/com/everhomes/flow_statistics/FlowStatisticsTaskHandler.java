package com.everhomes.flow_statistics;

import com.everhomes.configuration.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author liangming.huang
 * @className FlowStatisticsTaskHandler
 * @description 定时处理工作流效率统计数据
 * @date 2018/10/11
 **/
@Component
public class FlowStatisticsTaskHandler {

    private static Logger LOG = LoggerFactory.getLogger(FlowStatisticsTaskHandler.class);

    @Autowired
    private FlowStatisticsTaskService flowStatisticsTaskService;

    @Autowired
    private ConfigurationProvider configProvider;

    private static final String STATISTICS_TASK_KEY = "statistics.task.key";

    private static final String TRUE_KEY = "1";


    /**
     * 每天的晚上23点59分50秒的时候执行
     */
    @Scheduled(cron = "50 59 23 * * ?")
    public void statisticsTask() {

        LOG.info("flowStatisticsTaskHandler start . today：{}", System.currentTimeMillis());
        //查询是否有配置项
        String statisticsTaskKey = configProvider.getValue(STATISTICS_TASK_KEY, "");

        //使用合部重新统计的处理方式
        if(TRUE_KEY.equals(statisticsTaskKey)){
            flowStatisticsTaskService.allStatistics(null);
        }else{ //默认是使用追加方式处理数据
            flowStatisticsTaskService.appendStatistics(null);
        }
        LOG.info("flowStatisticsTaskHandler end . time:{}", System.currentTimeMillis());
    }
}
