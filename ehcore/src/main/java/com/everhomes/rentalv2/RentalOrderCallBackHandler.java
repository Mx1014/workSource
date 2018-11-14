// @formatter:off
package com.everhomes.rentalv2;

import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.rentalv2.admin.PayMode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX + OrderType.RENTAL_ORDER_CODE )
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
	@Autowired
	private RentalCommonServiceImpl rentalCommonService;
	@Override
	public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {

			this.dbProvider.execute((TransactionStatus status) -> {

//				RentalOrderPayorderMap orderMap= rentalProvider.findRentalBillPaybillMapByOrderNo(String.valueOf(cmd.getOrderId()));
//				RentalOrder order = rentalProvider.findRentalBillById(orderMap.getOrderId());

				RentalOrder order = rentalProvider.findRentalBillByOrderNo(String.valueOf(cmd.getOrderId()));

				BigDecimal payAmount = changePayAmount(cmd.getAmount());

				order.setPaidMoney(order.getPaidMoney().add(payAmount));
				order.setPayTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

				PaymentType paymentType = PaymentType.fromCode(cmd.getPaymentType());
				if (null != paymentType) {
					if (paymentType.name().toUpperCase().startsWith("WECHAT")) {
						order.setVendorType(VendorType.WEI_XIN.getCode());
//						orderMap.setVendorType(VendorType.WEI_XIN.getCode());
					}else {
						order.setVendorType(VendorType.ZHI_FU_BAO.getCode());
//						orderMap.setVendorType(VendorType.ZHI_FU_BAO.getCode());
					}
				}

				order.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

//				orderMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//				rentalProvider.updateRentalOrderPayorderMap(orderMap);

				if(order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())||
						order.getStatus().equals(SiteBillStatus.APPROVING.getCode())){
					//判断支付金额与订单金额是否相同
					if (order.getPayTotalMoney().compareTo(order.getPaidMoney()) == 0) {
						onOrderSuccess(order);
					}else {
						LOGGER.error("待付款订单:id ["+order.getId()+"]付款金额有问题： 应该付款金额："+order.getPayTotalMoney()+"实际付款金额："+order.getPaidMoney());
					}
				}else if(order.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
					LOGGER.error("待付款订单:id ["+order.getId()+"] 状态已经是成功预约");
				}else if (order.getStatus().equals(SiteBillStatus.IN_USING.getCode()) || (order.getStatus().equals(SiteBillStatus.OWING_FEE.getCode()))) {//vip停车的欠费和续费
					if (order.getPayTotalMoney().compareTo(order.getPaidMoney()) == 0) {
						if (order.getStatus().equals(SiteBillStatus.OWING_FEE.getCode())){
							order.setStatus(SiteBillStatus.COMPLETE.getCode());
						}
						else
							rentalService.renewOrderSuccess(order,order.getRentalCount());
						rentalProvider.updateRentalBill(order);
					}else{
						LOGGER.error("待付款订单:id [" + order.getId() + "]付款金额有问题： 应该付款金额：" + order.getPayTotalMoney() + "实际付款金额：" + order.getPaidMoney());
					}
				} else{
					LOGGER.error("待付款订单:id ["+order.getId()+"]状态有问题： 订单状态是："+order.getStatus());
				}

				return null;
			});

	}

	public void onOrderSuccess(RentalOrder order){
		if (order.getPayMode().equals(PayMode.ONLINE_PAY.getCode())) {
			//支付成功之后创建工作流
			order.setStatus(SiteBillStatus.SUCCESS.getCode());
			rentalProvider.updateRentalBill(order);
			rentalService.onOrderSuccess(order);

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
		}
	}

	private BigDecimal changePayAmount(Long amount){

		if(amount == null){
			return new BigDecimal(0);
		}
		return  new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
	}

	@Override
	public void payFail(SrvOrderPaymentNotificationCommand cmd) {
		//TODO: 失败
		LOGGER.error("Parking pay failed, cmd={}", cmd);
	}

	@Override
	public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {
//		RentalOrderPayorderMap orderMap= rentalProvider.findRentalBillPaybillMapByOrderNo(String.valueOf(cmd.getOrderId()));
//		RentalOrder order = rentalProvider.findRentalBillById(orderMap.getOrderId());
//		order.setStatus(SiteBillStatus.REFUNDED.getCode());
//		rentalProvider.updateRentalBill(order);
	}

	@Override
	public void refundFail(SrvOrderPaymentNotificationCommand cmd) {
		//when you refund, i can do nothing.
	}

}
