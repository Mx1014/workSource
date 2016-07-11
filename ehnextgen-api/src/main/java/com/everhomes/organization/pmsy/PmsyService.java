package com.everhomes.organization.pmsy;

import java.util.List;

import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.pmsy.AddressDTO;
import com.everhomes.rest.pmsy.CreatePmsyBillOrderCommand;
import com.everhomes.rest.pmsy.GetPmsyBills;
import com.everhomes.rest.pmsy.GetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.ListPmsyBillsCommand;
import com.everhomes.rest.pmsy.ListResourceCommand;
import com.everhomes.rest.pmsy.PmsyBillsDTO;
import com.everhomes.rest.pmsy.PmsyCommunityDTO;
import com.everhomes.rest.pmsy.PmsyPayerDTO;
import com.everhomes.rest.pmsy.PmsyBillsResponse;
import com.everhomes.rest.pmsy.SearchBillsOrdersResponse;
import com.everhomes.rest.pmsy.SetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.SearchBillsOrdersCommand;

public interface PmsyService {
	List<AddressDTO> listAddresses(ListResourceCommand cmd);
	
	PmsyBillsResponse listPmBills(ListPmsyBillsCommand cmd);
	
	PmsyBillsDTO getMonthlyPmBill(GetPmsyBills cmd);
	
	SearchBillsOrdersResponse searchBillingOrders(SearchBillsOrdersCommand cmd);
	
	void notifyPmsyOrderPayment(PayCallbackCommand cmd);
	
	List<PmsyPayerDTO> listPmPayers();
	
	void setPmProperty(SetPmsyPropertyCommand cmd);
	
	CommonOrderDTO createPmBillOrder(CreatePmsyBillOrderCommand cmd);
	
	PmsyCommunityDTO getPmProperty(GetPmsyPropertyCommand cmd);
}
