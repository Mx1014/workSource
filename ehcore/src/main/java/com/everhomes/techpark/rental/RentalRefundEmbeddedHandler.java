package com.everhomes.techpark.rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.db.DbProvider;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.RefundEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.RefundCallbackCommand;
import com.everhomes.rest.techpark.rental.SiteBillStatus;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.RENTAL_REFUND_CODE )
public class RentalRefundEmbeddedHandler implements RefundEmbeddedHandler {

	@Autowired
	private RentalProvider rentalProvider;

	@Autowired
	private DbProvider dbProvider;
	@Override
	public void paySuccess(RefundCallbackCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalRefundOrder rentalRefundOrder = this.rentalProvider.getRentalRefundOrderByRefoundNo(cmd.getRefundOrderNo());
			RentalBill bill = this.rentalProvider.findRentalBillById(rentalRefundOrder.getRentalBillId());
			rentalRefundOrder.setStatus(SiteBillStatus.REFUNDED.getCode());
			bill.setStatus(SiteBillStatus.REFUNDED.getCode());
			rentalProvider.updateRentalBill(bill); 
			rentalProvider.updateRentalRefundOrder(rentalRefundOrder);
			return null;
		});
	}

//	@Override
//	public void payFail(RefundCallbackCommand cmd) {
//		// TODO Auto-generated method stub
//
//	}

}
