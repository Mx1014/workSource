package com.everhomes.rentalv2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.db.DbProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.RefundEmbeddedHandler;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rentalv2.RentalRefundOrder;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.RefundCallbackCommand;
import com.everhomes.rest.rentalv2.SiteBillStatus;

@Component(RefundEmbeddedHandler.REFUND_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.RENTAL_ORDER_CODE )
public class RentalRefundEmbeddedHandler implements RefundEmbeddedHandler {

	@Autowired
	private Rentalv2Service rentalService;
	@Autowired
	private Rentalv2Provider rentalProvider;
	@Autowired
	private RentalCommonServiceImpl rentalCommonService;
	@Autowired
	private DbProvider dbProvider;
	@Override
	public void paySuccess(RefundCallbackCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalRefundOrder rentalRefundOrder = this.rentalProvider.getRentalRefundOrderByRefundNo(cmd.getRefundOrderNo());
			RentalOrder bill = this.rentalProvider.findRentalBillById(rentalRefundOrder.getOrderId());
			rentalRefundOrder.setStatus(SiteBillStatus.REFUNDED.getCode());
			bill.setStatus(SiteBillStatus.REFUNDED.getCode());
			rentalProvider.updateRentalBill(bill); 
			rentalProvider.updateRentalRefundOrder(rentalRefundOrder);
			RentalMessageHandler messageHandler = rentalCommonService.getRentalMessageHandler(bill.getResourceType());
			messageHandler.refundOrderSuccessSendMessage(bill);
			return null;
		});
	}

//	@Override
//	public void payFail(RefundCallbackCommand cmd) {
//		// TODO Auto-generated method stub
//
//	}

}
