package com.everhomes.contract;

import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.PeriodUnit;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.ContractSearcher;
import com.everhomes.util.DateHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/8/26.
 */
@Component
@Scope("prototype")
public class ContractScheduleJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractScheduleJob.class);

    public static final String SCHEDELE_NAME = "contract-";

    public static String CRON_EXPRESSION = "0 0 2 * * ?";

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ContractSearcher contractSearcher;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        Map<Long, List<Contract>> contracts = contractProvider.listContractGroupByCommunity();
        if(contracts != null && contracts.size() > 0) {
            contracts.forEach((communityId, contractList) -> {
                ContractParam param = contractProvider.findContractParamByCommunityId(communityId);
                if(param != null) {
                    if(contractList != null) {
                        contractList.forEach(contract -> {
                            //当前时间在合同到期时间之后 直接过期
                            if(now.after(contract.getContractEndDate())) {
                                contract.setStatus(ContractStatus.EXPIRED.getCode());
                                contractProvider.updateContract(contract);
                                contractSearcher.feedDoc(contract);
                            } else {
                                if(ContractStatus.ACTIVE.equals(ContractStatus.fromStatus(contract.getStatus()))) {
                                    //正常合同转即将过期
                                    compare(contract.getContractEndDate(), param.getExpiringUnit());
                                } else if(ContractStatus.APPROVE_QUALITIED.equals(ContractStatus.fromStatus(contract.getStatus()))) {
                                    //审批通过没有转为正常合同 过期
                                    compare(contract.getContractEndDate(), param.getExpiredUnit());
                                }

                            }

                        });
                    }
                }
            });
        }

    }

    private int compare(Timestamp date, Byte compareUnit) {
        return -1;
    }

    private int compareDate(Timestamp date, Timestamp compared) {

        return -1;
    }

    private Timestamp addPeriod(Timestamp startTime, int period, Byte unit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        if(PeriodUnit.MINUTE.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.MINUTE, period);
        }
        else if(PeriodUnit.HOUR.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.HOUR, period);
        }
        else if(PeriodUnit.DAY.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.DATE, period);
        }
        else if(PeriodUnit.MONTH.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.MONTH, period);
        }
        else if(PeriodUnit.YEAR.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.YEAR, period);
        }
        Timestamp time = new Timestamp(calendar.getTimeInMillis());

        return time;
    }

}
