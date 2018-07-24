package com.everhomes.rentalv2.job;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.vip_parking.DingDingParkingLockHandler;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rentalv2.Rentalv2Service;
import com.everhomes.rest.parking.ParkingSpaceDTO;
import com.everhomes.rest.parking.ParkingSpaceLockStatus;
import com.everhomes.rest.rentalv2.CompleteRentalOrderCommand;
import com.everhomes.rest.rentalv2.VipParkingUseInfoDTO;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RentalAutoCompleteJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalAutoCompleteJob.class);

    @Autowired
    private Rentalv2Service rentalv2Service;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private DingDingParkingLockHandler dingDingParkingLockHandler;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try{
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();
            Long orderId = jobMap.getLong("orderId");
            RentalOrder bill = rentalv2Provider.findRentalBillById(orderId);
            VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(bill.getCustomObject(), VipParkingUseInfoDTO.class);
            ParkingSpaceDTO spaceDTO = dingDingParkingLockHandler.getParkingSpaceLock(parkingInfo.getLockId());
            if (null != spaceDTO && spaceDTO.getLockStatus().equals(ParkingSpaceLockStatus.UP.getCode())) {//车锁升起 自动结束
                CompleteRentalOrderCommand cmd = new CompleteRentalOrderCommand();
                cmd.setRentalBillId(bill.getId());
                rentalv2Service.completeRentalOrder(cmd);
            }
        }catch (Exception e) {
            LOGGER.error("RentalAutoCompleteJob error", e);
        }
    }
}
