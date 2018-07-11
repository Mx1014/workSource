//@formatter:off
package com.everhomes.organization.pm;

import com.everhomes.address.AddressProvider;
import com.everhomes.rest.address.AddressLivingStatus;
import com.everhomes.rest.organization.pm.ReservationStatus;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * Created by Wentian Wang on 2018/6/13.
 */
@Component
public class ReservationJob extends QuartzJobBean {
    @Autowired
    private AddressProvider addressProvider;
    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Long addressId = (Long)jobDataMap.get("addressId");
        Long reservationId = (Long)jobDataMap.get("reservationId");
        Byte reservationPreviousLivingStatusById = propertyMgrProvider.getReservationPreviousLivingStatusById(reservationId);
        // todo realease address and inactive reservation
        addressProvider.changeAddressLivingStatus(addressId, reservationPreviousLivingStatusById);
        propertyMgrProvider.changeReservationStatus(reservationId, ReservationStatus.INACTIVE.getCode());
    }
}
