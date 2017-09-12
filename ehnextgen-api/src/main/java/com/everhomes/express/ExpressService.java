// @formatter:off
package com.everhomes.express;

import java.util.Map;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.express.AddExpressUserCommand;
import com.everhomes.rest.express.CancelExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderResponse;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressCommand;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressResponse;
import com.everhomes.rest.express.CreateOrUpdateExpressHotlineCommand;
import com.everhomes.rest.express.CreateOrUpdateExpressHotlineResponse;
import com.everhomes.rest.express.DeleteExpressAddressCommand;
import com.everhomes.rest.express.DeleteExpressHotlineCommand;
import com.everhomes.rest.express.DeleteExpressUserCommand;
import com.everhomes.rest.express.GetExpressBusinessNoteCommand;
import com.everhomes.rest.express.GetExpressBusinessNoteResponse;
import com.everhomes.rest.express.GetExpressHotlineAndBusinessNoteFlagCommand;
import com.everhomes.rest.express.GetExpressHotlineAndBusinessNoteFlagResponse;
import com.everhomes.rest.express.GetExpressInsuredDocumentsCommand;
import com.everhomes.rest.express.GetExpressInsuredDocumentsResponse;
import com.everhomes.rest.express.GetExpressLogisticsDetailCommand;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.rest.express.GetExpressOrderDetailCommand;
import com.everhomes.rest.express.GetExpressOrderDetailResponse;
import com.everhomes.rest.express.GetExpressParamSettingResponse;
import com.everhomes.rest.express.ListExpressAddressCommand;
import com.everhomes.rest.express.ListExpressAddressResponse;
import com.everhomes.rest.express.ListExpressCompanyCommand;
import com.everhomes.rest.express.ListExpressCompanyResponse;
import com.everhomes.rest.express.ListExpressHotlinesCommand;
import com.everhomes.rest.express.ListExpressHotlinesResponse;
import com.everhomes.rest.express.ListExpressOrderCommand;
import com.everhomes.rest.express.ListExpressOrderResponse;
import com.everhomes.rest.express.ListExpressOrderStatusResponse;
import com.everhomes.rest.express.ListExpressPackageTypesCommand;
import com.everhomes.rest.express.ListExpressPackageTypesResponse;
import com.everhomes.rest.express.ListExpressQueryHistoryResponse;
import com.everhomes.rest.express.ListExpressSendModesCommand;
import com.everhomes.rest.express.ListExpressSendModesResponse;
import com.everhomes.rest.express.ListExpressSendTypesCommand;
import com.everhomes.rest.express.ListExpressSendTypesResponse;
import com.everhomes.rest.express.ListExpressUserCommand;
import com.everhomes.rest.express.ListExpressUserResponse;
import com.everhomes.rest.express.ListPersonalExpressOrderCommand;
import com.everhomes.rest.express.ListPersonalExpressOrderResponse;
import com.everhomes.rest.express.ListServiceAddressCommand;
import com.everhomes.rest.express.ListServiceAddressResponse;
import com.everhomes.rest.express.PayExpressOrderCommand;
import com.everhomes.rest.express.PrePayExpressOrderCommand;
import com.everhomes.rest.express.PrintExpressOrderCommand;
import com.everhomes.rest.express.UpdateExpressBusinessNoteCommand;
import com.everhomes.rest.express.UpdateExpressHotlineFlagCommand;
import com.everhomes.rest.express.UpdatePaySummaryCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;

public interface ExpressService {


	public ListServiceAddressResponse listServiceAddress(ListServiceAddressCommand cmd);


	public ListExpressCompanyResponse listExpressCompany(ListExpressCompanyCommand cmd);


	public ListExpressUserResponse listExpressUser(ListExpressUserCommand cmd);


	public RestResponse addExpressUser(AddExpressUserCommand cmd);


	public void deleteExpressUser(DeleteExpressUserCommand cmd);


	public ListExpressOrderResponse listExpressOrder(ListExpressOrderCommand cmd);


	public GetExpressOrderDetailResponse getExpressOrderDetail(GetExpressOrderDetailCommand cmd);


	public void updatePaySummary(UpdatePaySummaryCommand cmd);


	public void printExpressOrder(PrintExpressOrderCommand cmd);


	public CreateOrUpdateExpressAddressResponse createOrUpdateExpressAddress(CreateOrUpdateExpressAddressCommand cmd);


	public void deleteExpressAddress(DeleteExpressAddressCommand cmd);


	public ListExpressAddressResponse listExpressAddress(ListExpressAddressCommand cmd);

	public CreateExpressOrderResponse createExpressOrder(CreateExpressOrderCommand cmd);


	public ListPersonalExpressOrderResponse listPersonalExpressOrder(ListPersonalExpressOrderCommand cmd);


	public void cancelExpressOrder(CancelExpressOrderCommand cmd);


	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(GetExpressLogisticsDetailCommand cmd);

	
	public ListExpressQueryHistoryResponse listExpressQueryHistory(Integer pageSize);


	public void clearExpressQueryHistory();


	public CommonOrderDTO payExpressOrder(PayExpressOrderCommand cmd);


	public void paySuccess(PayCallbackCommand cmd);


	public void payFail(PayCallbackCommand cmd);


	String getUrl(String uri);
	
	GetExpressParamSettingResponse getExpressParamSetting();

	GetExpressBusinessNoteResponse getExpressBusinessNote(GetExpressBusinessNoteCommand cmd);

	void updateExpressBusinessNote(UpdateExpressBusinessNoteCommand cmd);

	ListExpressHotlinesResponse listExpressHotlines(ListExpressHotlinesCommand cmd);

	void updateExpressHotlineFlag(UpdateExpressHotlineFlagCommand cmd);

	CreateOrUpdateExpressHotlineResponse createOrUpdateExpressHotline(CreateOrUpdateExpressHotlineCommand cmd);

	void deleteExpressHotline(DeleteExpressHotlineCommand cmd);

	ListExpressSendTypesResponse listExpressSendTypes(ListExpressSendTypesCommand cmd);

	GetExpressHotlineAndBusinessNoteFlagResponse getExpressHotlineAndBusinessNoteFlag(GetExpressHotlineAndBusinessNoteFlagCommand cmd);

	ListExpressSendModesResponse listExpressSendModes(ListExpressSendModesCommand cmd);

	ListExpressPackageTypesResponse listExpressPackageTypes(ListExpressPackageTypesCommand cmd);

	GetExpressInsuredDocumentsResponse getExpressInsuredDocuments(GetExpressInsuredDocumentsCommand cmd);

	ListExpressOrderStatusResponse listExpressOrderStatus();

	public Map<String,String> prePayExpressOrder(PrePayExpressOrderCommand cmd);

}