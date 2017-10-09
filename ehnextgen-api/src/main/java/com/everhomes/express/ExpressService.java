// @formatter:off
package com.everhomes.express;

import java.util.Map;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.express.*;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderDTO;

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

	public PreOrderDTO payExpressOrderV2(PayExpressOrderCommandV2 cmd);

}