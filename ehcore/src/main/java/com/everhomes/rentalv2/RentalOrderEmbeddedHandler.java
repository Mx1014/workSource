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
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.user.IdentifierType;
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
	private RentalCommonServiceImpl rentalCommonService;
	@Autowired
	private Rentalv2AccountProvider rentalv2AccountProvider;

	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		// 
		if(cmd.getPayStatus().toLowerCase().equals("fail")) {

			LOGGER.info(" ----------------- - - - PAY FAIL command is "+cmd.toString());
		}

		//success
		if(cmd.getPayStatus().toLowerCase().equals("success")) {
			this.dbProvider.execute((TransactionStatus status) -> {

//				RentalOrderPayorderMap orderMap= rentalProvider.findRentalBillPaybillMapByOrderNo(cmd.getOrderNo());
//				RentalOrder order = rentalProvider.findRentalBillById(orderMap.getOrderId());

				RentalOrder order = rentalProvider.findRentalBillByOrderNo(String.valueOf(cmd.getOrderNo()));

				order.setPaidMoney(order.getPaidMoney().add(new java.math.BigDecimal(cmd.getPayAmount())));
				order.setVendorType(cmd.getVendorType());
//				orderMap.setVendorType(cmd.getVendorType());
				order.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				order.setPayTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

//				orderMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//				rentalProvider.updateRentalOrderPayorderMap(orderMap);

				if (order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())) {
					//判断支付金额与订单金额是否相同
					if (order.getPayTotalMoney().compareTo(order.getPaidMoney()) == 0) {
						onOrderRecordSuccess(order);
						onOrderSuccess(order);
					} else {
						LOGGER.error("待付款订单:id [" + order.getId() + "]付款金额有问题： 应该付款金额：" + order.getPayTotalMoney() + "实际付款金额：" + order.getPaidMoney());
					}
				} else if (order.getStatus().equals(SiteBillStatus.SUCCESS.getCode())) {
					LOGGER.error("待付款订单:id [" + order.getId() + "] 状态已经是成功预约");
				} else if (order.getStatus().equals(SiteBillStatus.IN_USING.getCode()) || (order.getStatus().equals(SiteBillStatus.OWING_FEE.getCode()))) {//vip停车的欠费和续费
					if (order.getPayTotalMoney().compareTo(order.getPaidMoney()) == 0) {
						if (order.getStatus().equals(SiteBillStatus.OWING_FEE.getCode())) {
							order.setStatus(SiteBillStatus.COMPLETE.getCode());
						}
						else
							rentalService.renewOrderSuccess(order,order.getRentalCount());
						rentalProvider.updateRentalBill(order);
						onOrderRecordSuccess(order);
					}else{
						LOGGER.error("待付款订单:id [" + order.getId() + "]付款金额有问题： 应该付款金额：" + order.getPayTotalMoney() + "实际付款金额：" + order.getPaidMoney());
					}
				} else
					LOGGER.error("待付款订单:id [" + order.getId() + "]状态有问题： 订单状态是：" + order.getStatus());
				return null;
			});
		}
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		// TODO Auto-generated method stub

	}

	private void onOrderRecordSuccess(RentalOrder order){
		Rentalv2OrderRecord record = this.rentalv2AccountProvider.getOrderRecordByOrderNo(Long.valueOf(order.getOrderNo()));
		if (record != null){
			record.setStatus((byte)1);
			this.rentalv2AccountProvider.updateOrderRecord(record);
		}
	}

	public void onOrderSuccess(RentalOrder order){
		if (order.getPayMode().equals(PayMode.ONLINE_PAY.getCode())) {
			//支付成功之后创建工作流
			order.setStatus(SiteBillStatus.SUCCESS.getCode());
			rentalProvider.updateRentalBill(order);
			rentalService.onOrderSuccess(order);
			//发短信
			RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(order.getResourceType());

			handler.sendRentalSuccessSms(order);

		} else {
			rentalProvider.updateRentalBill(order);
			//改变订单状态
			rentalService.changeRentalOrderStatus(order,SiteBillStatus.SUCCESS.getCode(),true);
			//工作流自动进到下一节点
			FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getId(), REFER_TYPE, moduleId);
			FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
			flowCase = tree.getLeafNodes().get(0).getFlowCase();//获取真正正在进行的flowcase
			FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
			stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
			stepDTO.setFlowCaseId(flowCase.getId());
			stepDTO.setFlowMainId(flowCase.getFlowMainId());
			stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
			stepDTO.setFlowVersion(flowCase.getFlowVersion());
			stepDTO.setStepCount(flowCase.getStepCount());
			flowService.processAutoStep(stepDTO);

			//发消息和短信
			//发给发起人
			Map<String, String> map = new HashMap<>();
			map.put("useTime", order.getUseDetail());
			map.put("resourceName", order.getResourceName());
			rentalCommonService.sendMessageCode(order.getRentalUid(), map,
					RentalNotificationTemplateCode.RENTAL_PAY_SUCCESS_CODE);

			String templateScope = SmsTemplateCode.SCOPE;
			String templateLocale = RentalNotificationTemplateCode.locale;
			int templateId = SmsTemplateCode.RENTAL_PAY_SUCCESS_CODE;

			List<Tuple<String, Object>> variables = smsProvider.toTupleList("useTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());

			UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(),
					IdentifierType.MOBILE.getCode());
			if (null == userIdentifier) {
				LOGGER.error("userIdentifier is null...userId = " + order.getCreatorUid());
			} else {
				smsProvider.sendSms(order.getNamespaceId(), userIdentifier.getIdentifierToken(), templateScope,
						templateId, templateLocale, variables);
			}
		}
	}
}
