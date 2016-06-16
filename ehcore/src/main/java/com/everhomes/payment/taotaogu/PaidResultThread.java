package com.everhomes.payment.taotaogu;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.payment.PaymentCardProvider;
import com.everhomes.rest.payment.GetCardPaidResultDTO;

public class PaidResultThread implements Runnable{

	private GetCardPaidResultDTO dto;
	
	public PaidResultThread(GetCardPaidResultDTO dto){
		this.dto = dto;
	}
	
	@Override
	public void run() {
		PaymentCardProvider paymentCardProvider = PlatformContext.getComponent("");
		synchronized (dto) {
			
		}
	}

}
