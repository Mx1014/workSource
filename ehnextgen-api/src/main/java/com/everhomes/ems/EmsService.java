package com.everhomes.ems;

import com.everhomes.rest.ems.TrackBillResponse;

public interface EmsService {

	String getBillNo(String businessType);

	void updatePrintInfo();

	TrackBillResponse trackBill(String billno);

}
