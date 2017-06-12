package com.everhomes.rentalv2;

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

	private   Long  rentalBillId;
	@Autowired
	private Rentalv2Provider rentalProvider;
	
	public CancelUnsuccessRentalOrderAction(final String id) { 
		this.rentalBillId =  Long.valueOf(id) ;
		
	}

	@Override
	public void run() {
		// 如果还没成功付全款，则取消订单
		//TODO：加锁
		RentalOrder rentalBill = rentalProvider.findRentalBillById(Long.valueOf(rentalBillId));
		if(null==rentalBill)
			return ;
		if (!rentalBill.getStatus().equals(SiteBillStatus.SUCCESS.getCode()) ) {
			rentalBill.setStatus(SiteBillStatus.FAIL.getCode());
//			rentalProvider.deleteRentalBillById(rentalBill.getId());
			rentalProvider.updateRentalBill(rentalBill);
//			RentalResource site = this.rentalProvider.getRentalSiteById(rentalBill.getRentalResourceId());
//			RentalRule rule = this.rentalProvider.getRentalRule(site.getOwnerId(), site.getOwnerType(), site.getSiteType());
//			StringBuffer sb = new StringBuffer();
//			sb.append("您预定的："); 
//			sb.append(site.getResourceName());
//			sb.append("(时间:");
//			if (site.getRentalType().equals(RentalType.HOUR)){
//				SimpleDateFormat  datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				sb.append(datetimeSF.format(rentalBill.getStartTime()));
//			}else{
//				SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
//				sb.append(dateSF.format(rentalBill.getRentalDate()));
//			}
//			sb.append(")");
//			sb.append("由于超期被取消了 > <,请重新预订");
//			sendMessageToUser(rentalBill.getRentalUid(),sb.toString());
		}
		
		
	}

}
