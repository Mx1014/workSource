package com.everhomes.rentalv2.order_action;

import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalMessageHandler;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.Rentalv2Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;  
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.everhomes.rest.rentalv2.SiteBillStatus;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelUnsuccessRentalOrderAction implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CancelUnsuccessRentalOrderAction.class);

	private Long rentalBillId;
	@Autowired
	private Rentalv2Provider rentalProvider;
	@Autowired
	private RentalCommonServiceImpl rentalCommonService;

	public CancelUnsuccessRentalOrderAction(final String id) { 
		this.rentalBillId =  Long.valueOf(id) ;
		
	}

	@Override
	public void run() {
		// 如果还没成功付全款，则取消订单
		//TODO：加锁
		RentalOrder rentalBill = rentalProvider.findRentalBillById(Long.valueOf(rentalBillId));
		if(null == rentalBill)
			return ;
		if (!rentalBill.getStatus().equals(SiteBillStatus.SUCCESS.getCode()) ) {
			rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
			rentalProvider.updateRentalBill(rentalBill);
			//发消息
			RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(rentalBill.getResourceType());

			handler.sendOrderOverTimeMessage(rentalBill);
		}
	}

}
