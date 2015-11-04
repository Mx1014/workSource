package com.everhomes.techpark.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelRentalBillAction implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CancelRentalBillAction.class);
	private final RentalBill rentalBill;
	@Autowired
	RentalProvider rentalProvider;

	public CancelRentalBillAction(final String id) {
		this.rentalBill = rentalProvider.findRentalBillById(Long.valueOf(id));
	}

	@Override
	public void run() {
		// 取消订单
		if (rentalBill.getStatus().equals(SiteBillStatus.LOCKED.getCode())||rentalBill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())) {
			rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
			rentalProvider.updateRentalBill(rentalBill);
		}
	}

}
