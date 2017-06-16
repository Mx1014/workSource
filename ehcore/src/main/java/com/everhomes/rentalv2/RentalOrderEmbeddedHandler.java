package com.everhomes.rentalv2;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.rentalv2.admin.PayMode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.sms.SmsProvider;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.rentalv2.OnlinePayCallbackCommandResponse;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import org.springframework.transaction.TransactionStatus;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.RENTAL_ORDER_CODE )
public class RentalOrderEmbeddedHandler implements OrderEmbeddedHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalOrderEmbeddedHandler.class);

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
	@Autowired
	private Rentalv2Service rentalv2Service;

	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		// 
		if(cmd.getPayStatus().toLowerCase().equals("fail")) {

			LOGGER.info(" ----------------- - - - PAY FAIL command is "+cmd.toString());
		}

		//success
		if(cmd.getPayStatus().toLowerCase().equals("success")) {
			this.dbProvider.execute((TransactionStatus status) -> {

				RentalOrderPayorderMap orderMap= rentalProvider.findRentalBillPaybillMapByOrderNo(cmd.getOrderNo());
				RentalOrder order = rentalProvider.findRentalBillById(orderMap.getOrderId());
				order.setPaidMoney(order.getPaidMoney().add(new java.math.BigDecimal(cmd.getPayAmount())));
				order.setVendorType(cmd.getVendorType());
				orderMap.setVendorType(cmd.getVendorType());
				order.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

				orderMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				rentalProvider.updateRentalOrderPayorderMap(orderMap);

				if(order.getStatus().equals(SiteBillStatus.LOCKED.getCode())){
					order.setStatus(SiteBillStatus.RESERVED.getCode());
				}else if(order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())){
					//判断支付金额与订单金额是否相同
					if (order.getPaidMoney().compareTo(new BigDecimal(cmd.getPayAmount())) == 0
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
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		// TODO Auto-generated method stub

	}

}
