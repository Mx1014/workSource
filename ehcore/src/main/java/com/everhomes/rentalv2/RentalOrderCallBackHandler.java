// @formatter:off
package com.everhomes.rentalv2;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.order.PayService;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.rentalv2.admin.PayMode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.ACTIVITY_SIGNUP_ORDER_CODE )
public class RentalOrderCallBackHandler implements PaymentCallBackHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalOrderCallBackHandler.class);

	private static final String REFER_TYPE= FlowReferType.RENTAL.getCode();
	public static final Long moduleId = 40400L;

	@Autowired
	private Rentalv2Service rentalService;
	@Autowired
	private Rentalv2Provider rentalProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowCaseProvider flowCaseProvider;
	@Autowired
	private SmsProvider smsProvider;
	@Override
	public void paySuccess(OrderPaymentNotificationCommand cmd) {

			this.dbProvider.execute((TransactionStatus status) -> {

				RentalOrderPayorderMap orderMap= rentalProvider.findRentalBillPaybillMapByOrderNo(String.valueOf(cmd.getOrderId()));
				RentalOrder order = rentalProvider.findRentalBillById(orderMap.getOrderId());
				order.setPaidMoney(order.getPaidMoney().add(new java.math.BigDecimal(cmd.getAmount())));
//				order.setVendorType(cmd.getVendorType());
//				orderMap.setVendorType(cmd.getVendorType());
				order.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

				orderMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				rentalProvider.updateRentalOrderPayorderMap(orderMap);

				if(order.getStatus().equals(SiteBillStatus.LOCKED.getCode())){
					order.setStatus(SiteBillStatus.RESERVED.getCode());
				}else if(order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())){
					//判断支付金额与订单金额是否相同
					if (order.getPaidMoney().compareTo(new BigDecimal(cmd.getAmount())) == 0
							&& order.getPayTotalMoney().compareTo(order.getPaidMoney()) == 0) {

						if (order.getPayMode().equals(PayMode.ONLINE_PAY.getCode())) {
							//支付成功之后创建工作流
							order.setStatus(SiteBillStatus.SUCCESS.getCode());
							rentalProvider.updateRentalBill(order);
							rentalService.onOrderSuccess(order);
							//发短信
							UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(), IdentifierType.MOBILE.getCode()) ;
							if(null == userIdentifier){
								LOGGER.error("userIdentifier is null...userId = " + order.getCreatorUid());
							}else{
								rentalService.sendRentalSuccessSms(order.getNamespaceId(),userIdentifier.getIdentifierToken(), order);
							}
						}else {

//							rentalv2Service.changeRentalOrderStatus(order, SiteBillStatus.SUCCESS.getCode(), true);

							FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getId(), REFER_TYPE, moduleId);

							FlowAutoStepDTO dto = new FlowAutoStepDTO();
							dto.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
							dto.setFlowCaseId(flowCase.getId());
							dto.setFlowMainId(flowCase.getFlowMainId());
							dto.setFlowNodeId(flowCase.getCurrentNodeId());
							dto.setFlowVersion(flowCase.getFlowVersion());
							dto.setStepCount(flowCase.getStepCount());
							flowService.processAutoStep(dto);

							//发消息和短信
							//发给发起人
							Map<String, String> map = new HashMap<>();
							map.put("useTime", order.getUseDetail());
							map.put("resourceName", order.getResourceName());
							rentalService.sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
									RentalNotificationTemplateCode.RENTAL_PAY_SUCCESS_CODE);

							String templateScope = SmsTemplateCode.SCOPE;
							String templateLocale = RentalNotificationTemplateCode.locale;
							int templateId = SmsTemplateCode.RENTAL_PAY_SUCCESS_CODE;

							List<Tuple<String, Object>> variables = smsProvider.toTupleList("useTime", order.getUseDetail());
							smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());

							UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(),
									IdentifierType.MOBILE.getCode()) ;
							if(null == userIdentifier){
								LOGGER.error("userIdentifier is null...userId = " + order.getCreatorUid());
							}else{
								smsProvider.sendSms(order.getNamespaceId(), userIdentifier.getIdentifierToken(), templateScope,
										templateId, templateLocale, variables);
							}
						}
					}else {
						LOGGER.error("待付款订单:id ["+order.getId()+"]付款金额有问题： 应该付款金额："+order.getPayTotalMoney()+"实际付款金额："+order.getPaidMoney());
					}
				}else if(order.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
					LOGGER.error("待付款订单:id ["+order.getId()+"] 状态已经是成功预约");
				}else{
					LOGGER.error("待付款订单:id ["+order.getId()+"]状态有问题： 订单状态是："+order.getStatus());
				}

				return null;
			});

	}

	@Override
	public void payFail(OrderPaymentNotificationCommand cmd) {
		//TODO: 失败
		LOGGER.error("Parking pay failed, cmd={}", cmd);
	}

	@Override
	public void refundSuccess(OrderPaymentNotificationCommand cmd) {

	}

	@Override
	public void refundFail(OrderPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.
	}

}
