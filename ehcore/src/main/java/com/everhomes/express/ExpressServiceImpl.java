// @formatter:off
package com.everhomes.express;

import org.springframework.stereotype.Component;

import com.everhomes.rest.express.AddExpressUserCommand;
import com.everhomes.rest.express.CancelExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderResponse;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressCommand;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressResponse;
import com.everhomes.rest.express.DeleteExpressAddressCommand;
import com.everhomes.rest.express.DeleteExpressUserCommand;
import com.everhomes.rest.express.GetExpressLogisticsDetailCommand;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.rest.express.GetExpressOrderDetailCommand;
import com.everhomes.rest.express.GetExpressOrderDetailResponse;
import com.everhomes.rest.express.ListExpressAddressCommand;
import com.everhomes.rest.express.ListExpressAddressResponse;
import com.everhomes.rest.express.ListExpressCompanyCommand;
import com.everhomes.rest.express.ListExpressCompanyResponse;
import com.everhomes.rest.express.ListExpressOrderCommand;
import com.everhomes.rest.express.ListExpressOrderResponse;
import com.everhomes.rest.express.ListExpressQueryHistoryResponse;
import com.everhomes.rest.express.ListExpressUserCommand;
import com.everhomes.rest.express.ListExpressUserResponse;
import com.everhomes.rest.express.ListPersonalExpressOrderCommand;
import com.everhomes.rest.express.ListPersonalExpressOrderResponse;
import com.everhomes.rest.express.ListServiceAddressCommand;
import com.everhomes.rest.express.ListServiceAddressResponse;
import com.everhomes.rest.express.PrintExpressOrderCommand;
import com.everhomes.rest.express.UpdatePaySummaryCommand;

@Component
public class ExpressServiceImpl implements ExpressService {

	@Override
	public ListServiceAddressResponse listServiceAddress(ListServiceAddressCommand cmd) {
	
		return new ListServiceAddressResponse();
	}

	@Override
	public ListExpressCompanyResponse listExpressCompany(ListExpressCompanyCommand cmd) {
	
		return new ListExpressCompanyResponse();
	}

	@Override
	public ListExpressUserResponse listExpressUser(ListExpressUserCommand cmd) {
	
		return new ListExpressUserResponse();
	}

	@Override
	public void addExpressUser(AddExpressUserCommand cmd) {
	

	}

	@Override
	public void deleteExpressUser(DeleteExpressUserCommand cmd) {
	

	}

	@Override
	public ListExpressOrderResponse listExpressOrder(ListExpressOrderCommand cmd) {
	
		return new ListExpressOrderResponse();
	}

	@Override
	public GetExpressOrderDetailResponse getExpressOrderDetail(GetExpressOrderDetailCommand cmd) {
	
		return new GetExpressOrderDetailResponse();
	}

	@Override
	public void updatePaySummary(UpdatePaySummaryCommand cmd) {
	

	}

	@Override
	public void printExpressOrder(PrintExpressOrderCommand cmd) {
	

	}

	@Override
	public CreateOrUpdateExpressAddressResponse createOrUpdateExpressAddress(CreateOrUpdateExpressAddressCommand cmd) {
	
		return new CreateOrUpdateExpressAddressResponse();
	}

	@Override
	public void deleteExpressAddress(DeleteExpressAddressCommand cmd) {
	

	}

	@Override
	public ListExpressAddressResponse listExpressAddress(ListExpressAddressCommand cmd) {
	
		return new ListExpressAddressResponse();
	}

	@Override
	public CreateExpressOrderResponse createExpressOrder(CreateExpressOrderCommand cmd) {
	
		return new CreateExpressOrderResponse();
	}

	@Override
	public ListPersonalExpressOrderResponse listPersonalExpressOrder(ListPersonalExpressOrderCommand cmd) {
	
		return new ListPersonalExpressOrderResponse();
	}

	@Override
	public void cancelExpressOrder(CancelExpressOrderCommand cmd) {
	

	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(GetExpressLogisticsDetailCommand cmd) {
	
		return new GetExpressLogisticsDetailResponse();
	}

	@Override
	public ListExpressQueryHistoryResponse listExpressQueryHistory() {
	
		return new ListExpressQueryHistoryResponse();
	}

	@Override
	public void clearExpressQueryHistory() {
	

	}

}