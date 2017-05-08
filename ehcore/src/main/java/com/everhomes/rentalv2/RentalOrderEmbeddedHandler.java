package com.everhomes.rentalv2;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.rentalv2.admin.PayMode;
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
	private FlowButtonProvider flowButtonProvider;

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
						//支付成功之后创建工作流
						order.setStatus(SiteBillStatus.SUCCESS.getCode());
						rentalProvider.updateRentalBill(order);

						if (order.getPayMode().equals(PayMode.ONLINE_PAY.getCode())) {
							rentalService.onOrderSuccess(order);
						}else {
							FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getId(), REFER_TYPE, moduleId);

//							FlowAutoStepDTO dto = new FlowAutoStepDTO();
//							dto.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//							dto.setFlowCaseId(flowcase.getId());
//							dto.setFlowMainId(flowcase.getFlowMainId());
//							dto.setFlowNodeId(flowcase.getCurrentNodeId());
//							dto.setFlowVersion(flowcase.getFlowVersion());
//							dto.setStepCount(flowcase.getStepCount());
//							flowService.processAutoStep(dto);
							//TODO:autoStep 没有消息 暂时改成firebutton
							List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(),
									flowCase.getFlowVersion(), FlowUserType.APPLIER.getCode());

							FlowButton button = null;
							for (FlowButton b: buttons) {
								if (FlowStepType.APPROVE_STEP.getCode().equals(b.getFlowStepType()))
									button = b;
							}

							FlowFireButtonCommand fireButtonCommand = new FlowFireButtonCommand();
							fireButtonCommand.setFlowCaseId(flowCase.getId());
							fireButtonCommand.setButtonId(button.getId());
							flowService.fireButton(fireButtonCommand);
						}

						//发短信
//						UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(), IdentifierType.MOBILE.getCode()) ;
//						if(null == userIdentifier){
//							LOGGER.error("userIdentifier is null...userId = " + order.getCreatorUid());
//						}else{
//							rentalService.sendRentalSuccessSms(order.getNamespaceId(),userIdentifier.getIdentifierToken(), order);
//						}

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
