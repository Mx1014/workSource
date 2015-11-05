package com.everhomes.techpark.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelUnsuccessRentalBillAction implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CancelUnsuccessRentalBillAction.class);
	private final RentalBill rentalBill;
	@Autowired
	RentalProvider rentalProvider;

	public CancelUnsuccessRentalBillAction(final String id) {
		this.rentalBill = rentalProvider.findRentalBillById(Long.valueOf(id));
	}

	@Override
	public void run() {
		// 如果还没成功付全款，则取消订单
		//TODO：加锁
		if (!rentalBill.getStatus().equals(SiteBillStatus.SUCCESS.getCode()) ) {
			rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
			rentalProvider.updateRentalBill(rentalBill);
		}
	}

}
