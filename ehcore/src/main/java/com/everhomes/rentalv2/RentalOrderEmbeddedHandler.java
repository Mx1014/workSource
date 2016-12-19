package com.everhomes.rentalv2;

import java.sql.Timestamp;

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

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.RENTAL_ORDER_CODE )
public class RentalOrderEmbeddedHandler implements OrderEmbeddedHandler {

	@Autowired
	private Rentalv2Service rentalService;
	@Autowired
	Rentalv2Provider rentalProvider;
	@Autowired
	private UserProvider userProvider;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RentalOrderEmbeddedHandler.class);
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		// 
				if(cmd.getPayStatus().toLowerCase().equals("fail")) {
					 
					LOGGER.info(" ----------------- - - - PAY FAIL command is "+cmd.toString());
				}
					
				//success
				if(cmd.getPayStatus().toLowerCase().equals("success"))
				{
					RentalOrderPayorderMap orderMap= rentalProvider.findRentalBillPaybillMapByOrderNo(cmd.getOrderNo());
					RentalOrder order = rentalProvider.findRentalBillById(orderMap.getOrderId());
					order.setPaidMoney(order.getPaidMoney().add(new java.math.BigDecimal(cmd.getPayAmount())));
					order.setVendorType(cmd.getVendorType());
					orderMap.setVendorType(cmd.getVendorType());
					order.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));

					orderMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));
//					bill.setOperatorUid(UserContext.current().getUser().getId());
					if(order.getStatus().equals(SiteBillStatus.LOCKED.getCode())){
						order.setStatus(SiteBillStatus.RESERVED.getCode());
					}
					else if(order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())){
						if(order.getPayTotalMoney().compareTo(order.getPaidMoney()) == 0){
							order.setStatus(SiteBillStatus.SUCCESS.getCode());
							rentalService.onBillSuccess(order);
							UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(), IdentifierType.MOBILE.getCode()) ;
							if(null == userIdentifier){
								LOGGER.error("userIdentifier is null...userId = " + order.getCreatorUid());
							}else{
								rentalService.sendRentalSuccessSms(order.getNamespaceId(),userIdentifier.getIdentifierToken(), order); 
							}						}
						else{
							LOGGER.error("待付款订单:id ["+order.getId()+"]付款金额有问题： 应该付款金额："+order.getPayTotalMoney()+"实际付款金额："+order.getPaidMoney());
		 
						}
					}else if(order.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
						LOGGER.error("待付款订单:id ["+order.getId()+"] 状态已经是成功预约");
					}else{
						LOGGER.error("待付款订单:id ["+order.getId()+"]状态有问题： 订单状态是："+order.getStatus());
					}
					rentalProvider.updateRentalBill(order);
					rentalProvider.updateRentalOrderPayorderMap(orderMap); 
				} 
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		// TODO Auto-generated method stub

	}

}
