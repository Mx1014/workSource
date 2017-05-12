// @formatter:off
package com.everhomes.activity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityRosterPayFlag;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.ACTIVITY_SIGNUP_ORDER_CODE )
public class ActivitySignupOrderEmbeddedHandler implements OrderEmbeddedHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitySignupOrderEmbeddedHandler.class);

    @Autowired
    private ActivityProivider activityProvider;
    
    @Autowired
    private ActivityService activityService;
    
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		ActivityRoster roster = activityProvider.findRosterByOrderNo(Long.valueOf(cmd.getOrderNo()));
		if(roster == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
					"no roster.");
		}
		Activity activity = activityProvider.findActivityById(roster.getActivityId());
		if(activity == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
					"no activity.");
		}
		//检验支付结果和应价格是否相等
		checkPayAmount(cmd.getPayAccount(), activity.getChargePrice());
		//支付宝回调时，可能会同时回调多次，
		this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY_ROSTER.getCode()).enter(()-> {
			roster.setPayFlag(ActivityRosterPayFlag.PAY.getCode());
			roster.setPayTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			roster.setPayAmount(Double.valueOf(cmd.getPayAccount()));
			activityProvider.updateRoster(roster);
			return null;
		});
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillFail");
		ActivityRoster roster = activityProvider.findRosterById(Long.valueOf(cmd.getOrderNo()));
		if(roster == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
					"no roster.");
		}
		ActivityCancelSignupCommand cancelCmd = new ActivityCancelSignupCommand();
		cancelCmd.setActivityId(roster.getActivityId());
		activityService.cancelSignup(cancelCmd);
	}
	
	
	
	private void checkPayAmount(String payAmount, Double chargePrice) {
		if(StringUtils.isBlank(payAmount) || chargePrice.doubleValue() != Double.valueOf(payAmount).doubleValue()){
			LOGGER.error("payAmount and chargePrice is not equal.");
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_PAYAMOUNT_ERROR,
					"payAmount and chargePrice is not equal.");
		}
	}
}
