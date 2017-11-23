// @formatter:off
package com.everhomes.express;

import java.util.Map;

import com.everhomes.rest.express.*;
import com.everhomes.rest.order.PreOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;

@RestController
@RequestMapping("/express")
public class ExpressController extends ControllerBase {
	
	@Autowired
	private ExpressService expressService;
	
	/**
	 * <p>1.自寄服务地址列表</p>
	 * <b>URL: /express/listServiceAddress</b>
	 */
	@RequestMapping("listServiceAddress")
	@RestReturn(ListServiceAddressResponse.class)
	public RestResponse listServiceAddress(ListServiceAddressCommand cmd){
		return new RestResponse(expressService.listServiceAddress(cmd));
	}

	/**
	 * <p>2.快递公司列表</p>
	 * <b>URL: /express/listExpressCompany</b>
	 */
	@RequestMapping("listExpressCompany")
	@RestReturn(ListExpressCompanyResponse.class)
	public RestResponse listExpressCompany(ListExpressCompanyCommand cmd){
		return new RestResponse(expressService.listExpressCompany(cmd));
	}

	/**
	 * <p>3.快递人员列表</p>
	 * <b>URL: /express/listExpressUser</b>
	 */
	@RequestMapping("listExpressUser")
	@RestReturn(ListExpressUserResponse.class)
	public RestResponse listExpressUser(ListExpressUserCommand cmd){
		return new RestResponse(expressService.listExpressUser(cmd));
	}

	/**
	 * <p>4.添加快递人员</p>
	 * <b>URL: /express/addExpressUser</b>
	 */
	@RequestMapping("addExpressUser")
	@RestReturn(String.class)
	public RestResponse addExpressUser(AddExpressUserCommand cmd){
		RestResponse response = expressService.addExpressUser(cmd);
		return response != null ? response : new RestResponse();
	}

	/**
	 * <p>5.删除快递人员</p>
	 * <b>URL: /express/deleteExpressUser</b>
	 */
	@RequestMapping("deleteExpressUser")
	@RestReturn(String.class)
	public RestResponse deleteExpressUser(DeleteExpressUserCommand cmd){
		expressService.deleteExpressUser(cmd);
		return new RestResponse();
	}

	/**
	 * <p>6.快递订单列表（后台）</p>
	 * <b>URL: /express/listExpressOrder</b>
	 */
	@RequestMapping("listExpressOrder")
	@RestReturn(ListExpressOrderResponse.class)
	public RestResponse listExpressOrder(ListExpressOrderCommand cmd){
		return new RestResponse(expressService.listExpressOrder(cmd));
	}

	/**
	 * <p>7.快递订单详情</p>
	 * <b>URL: /express/getExpressOrderDetail</b>
	 */
	@RequestMapping("getExpressOrderDetail")
	@RestReturn(GetExpressOrderDetailResponse.class)
	public RestResponse getExpressOrderDetail(GetExpressOrderDetailCommand cmd){
		return new RestResponse(expressService.getExpressOrderDetail(cmd));
	}

	/**
	 * <p>8.修改付费总计</p>
	 * <b>URL: /express/updatePaySummary</b>
	 */
	@RequestMapping("updatePaySummary")
	@RestReturn(String.class)
	public RestResponse updatePaySummary(UpdatePaySummaryCommand cmd){
		expressService.updatePaySummary(cmd);
		return new RestResponse();
	}
	
	/**
	 * <p>9.立即支付</p>
	 * <b>URL: /express/payExpressOrder</b>
	 */
	@RequestMapping("payExpressOrder")
	@RestReturn(CommonOrderDTO.class)
	public RestResponse payExpressOrder(PayExpressOrderCommand cmd){
		return new RestResponse(expressService.payExpressOrder(cmd));
	}

	/**
	 * <p>9.1. 立即支付 V2</p>
	 * <b>URL: /express/payExpressOrderV2</b>
	 */
	@RequestMapping("payExpressOrderV2")
	@RestReturn(PreOrderDTO.class)
	public RestResponse payExpressOrderV2(PayExpressOrderCommandV2 cmd){
		return new RestResponse(expressService.payExpressOrderV2(cmd));
	}

	/**
	 * <p>10.出单</p>
	 * <b>URL: /express/printExpressOrder</b>
	 */
	@RequestMapping("printExpressOrder")
	@RestReturn(String.class)
	public RestResponse printExpressOrder(PrintExpressOrderCommand cmd){
		expressService.printExpressOrder(cmd);
		return new RestResponse();
	}

	/**
	 * <p>11.添加地址</p>
	 * <b>URL: /express/createOrUpdateExpressAddress</b>
	 */
	@RequestMapping("createOrUpdateExpressAddress")
	@RestReturn(CreateOrUpdateExpressAddressResponse.class)
	public RestResponse createOrUpdateExpressAddress(CreateOrUpdateExpressAddressCommand cmd){
		return new RestResponse(expressService.createOrUpdateExpressAddress(cmd));
	}

	/**
	 * <p>12.删除地址</p>
	 * <b>URL: /express/deleteExpressAddress</b>
	 */
	@RequestMapping("deleteExpressAddress")
	@RestReturn(String.class)
	public RestResponse deleteExpressAddress(DeleteExpressAddressCommand cmd){
		expressService.deleteExpressAddress(cmd);
		return new RestResponse();
	}

	/**
	 * <p>13.地址列表</p>
	 * <b>URL: /express/listExpressAddress</b>
	 */
	@RequestMapping("listExpressAddress")
	@RestReturn(ListExpressAddressResponse.class)
	public RestResponse listExpressAddress(ListExpressAddressCommand cmd){
		return new RestResponse(expressService.listExpressAddress(cmd));
	}

	/**
	 * <p>14.寄快递</p>
	 * <b>URL: /express/createExpressOrder</b>
	 */
	@RequestMapping("createExpressOrder")
	@RestReturn(CreateExpressOrderResponse.class)
	public RestResponse createExpressOrder(CreateExpressOrderCommand cmd){
		return new RestResponse(expressService.createExpressOrder(cmd));
	}

	/**
	 * <p>15.快递订单列表（个人）</p>
	 * <b>URL: /express/listPersonalExpressOrder</b>
	 */
	@RequestMapping("listPersonalExpressOrder")
	@RestReturn(ListPersonalExpressOrderResponse.class)
	public RestResponse listPersonalExpressOrder(ListPersonalExpressOrderCommand cmd){
		return new RestResponse(expressService.listPersonalExpressOrder(cmd));
	}

	/**
	 * <p>16.取消订单</p>
	 * <b>URL: /express/cancelExpressOrder</b>
	 */
	@RequestMapping("cancelExpressOrder")
	@RestReturn(String.class)
	public RestResponse cancelExpressOrder(CancelExpressOrderCommand cmd){
		expressService.cancelExpressOrder(cmd);
		return new RestResponse();
	}

	/**
	 * <p>17.查看物流详情</p>
	 * <b>URL: /express/getExpressLogisticsDetail</b>
	 */
	@RequestMapping("getExpressLogisticsDetail")
	@RestReturn(GetExpressLogisticsDetailResponse.class)
	public RestResponse getExpressLogisticsDetail(GetExpressLogisticsDetailCommand cmd){
		return new RestResponse(expressService.getExpressLogisticsDetail(cmd));
	}

	/**
	 * <p>18.查询快递历史列表</p>
	 * <b>URL: /express/listExpressQueryHistory</b>
	 */
	@RequestMapping("listExpressQueryHistory")
	@RestReturn(ListExpressQueryHistoryResponse.class)
	public RestResponse listExpressQueryHistory(@RequestParam("pageSize") Integer pageSize){
		return new RestResponse(expressService.listExpressQueryHistory(pageSize));
	}

	/**
	 * <p>19.清空快递查询历史</p>
	 * <b>URL: /express/clearExpressQueryHistory</b>
	 */
	@RequestMapping("clearExpressQueryHistory")
	@RestReturn(String.class)
	public RestResponse clearExpressQueryHistory(){
		expressService.clearExpressQueryHistory();
		return new RestResponse();
	}
	
	/**
	 * <p>20.查询参数设置tab的显示标签(后台)</p>
	 * <b>URL: /express/getExpressParamSetting</b>
	 */
	@RequestMapping("getExpressParamSetting")
	@RestReturn(GetExpressParamSettingResponse.class)
	public RestResponse getExpressParamSetting(){
		return new RestResponse(expressService.getExpressParamSetting());
	}

	/**
	 * <p>21.查询业务说明</p>
	 * <b>URL: /express/getExpressBusinessNote</b>
	 */
	@RequestMapping("getExpressBusinessNote")
	@RestReturn(GetExpressBusinessNoteResponse.class)
	public RestResponse getExpressBusinessNote(GetExpressBusinessNoteCommand cmd){
		return new RestResponse(expressService.getExpressBusinessNote(cmd));
	}
	
	/**
	 * <p>22.更新 业务说明</p>
	 * <b>URL: /express/updateExpressBusinessNote</b>
	 */
	@RequestMapping("updateExpressBusinessNote")
	@RestReturn(String.class)
	public RestResponse updateExpressBusinessNote(UpdateExpressBusinessNoteCommand cmd){
		expressService.updateExpressBusinessNote(cmd);
		return new RestResponse();
	}
	
	/**
	 * <p>23.查询热线</p>
	 * <b>URL: /express/listExpressHotlines</b>
	 */
	@RequestMapping("listExpressHotlines")
	@RestReturn(ListExpressHotlinesResponse.class)
	public RestResponse listExpressHotlines(ListExpressHotlinesCommand cmd){
		return new RestResponse(expressService.listExpressHotlines(cmd));
	}
	
	/**
	 * <p>24.更新热线是否在app端显示标志</p>
	 * <b>URL: /express/updateExpressHotlineFlag</b>
	 */
	@RequestMapping("updateExpressHotlineFlag")
	@RestReturn(String.class)
	public RestResponse updateExpressHotlineFlag(UpdateExpressHotlineFlagCommand cmd){
		expressService.updateExpressHotlineFlag(cmd);
		return new RestResponse();
	}
	
	/**
	 * <p>25.创建or更新热线</p>
	 * <b>URL: /express/createOrUpdateExpressHotline</b>
	 */
	@RequestMapping("createOrUpdateExpressHotline")
	@RestReturn(CreateOrUpdateExpressHotlineResponse.class)
	public RestResponse createOrUpdateExpressHotline(CreateOrUpdateExpressHotlineCommand cmd){
		return new RestResponse(expressService.createOrUpdateExpressHotline(cmd));
	}
	
	/**
	 * <p>26.删除热线</p>
	 * <b>URL: /express/deleteExpressHotline</b>
	 */
	@RequestMapping("deleteExpressHotline")
	@RestReturn(String.class)
	public RestResponse deleteExpressHotline(DeleteExpressHotlineCommand cmd){
		expressService.deleteExpressHotline(cmd);
		return new RestResponse();
	}
	
	/**
	 * <p>27.查询寄件类型列表(后台，app)</p>
	 * <b>URL: /express/listExpressSendTypes</b>
	 */
	@RequestMapping("listExpressSendTypes")
	@RestReturn(ListExpressSendTypesResponse.class)
	public RestResponse listExpressSendTypes(ListExpressSendTypesCommand cmd){
		return new RestResponse(expressService.listExpressSendTypes(cmd));
	}
	
	/**
	 * <p>28.查询 热线和业务说明在app端是否显示(app)</p>
	 * <b>URL: /express/getExpressHotlineAndBusinessNoteFlag</b>
	 */
	@RequestMapping("getExpressHotlineAndBusinessNoteFlag")
	@RestReturn(GetExpressHotlineAndBusinessNoteFlagResponse.class)
	public RestResponse getExpressHotlineAndBusinessNoteFlag(GetExpressHotlineAndBusinessNoteFlagCommand cmd){
		return new RestResponse(expressService.getExpressHotlineAndBusinessNoteFlag(cmd));
	}
	
	/**
	 * <p>29.查询寄件方式列表 （app）</p>
	 * <b>URL: /express/listExpressSendModes</b>
	 */
	@RequestMapping("listExpressSendModes")
	@RestReturn(ListExpressSendModesResponse.class)
	public RestResponse listExpressSendModes(ListExpressSendModesCommand cmd){
		return new RestResponse(expressService.listExpressSendModes(cmd));
	}
	
	/**
	 * <p>30.查询封装类型列表 （app）</p>
	 * <b>URL: /express/listExpressPackageTypes</b>
	 */
	@RequestMapping("listExpressPackageTypes")
	@RestReturn(ListExpressPackageTypesResponse.class)
	public RestResponse listExpressPackageTypes(ListExpressPackageTypesCommand cmd){
		return new RestResponse(expressService.listExpressPackageTypes(cmd));
	}
	
	/**
	 * <p>31.查询封装文案 （app）</p>
	 * <b>URL: /express/getExpressInsuredDocuments</b>
	 */
	@RequestMapping("getExpressInsuredDocuments")
	@RestReturn(GetExpressInsuredDocumentsResponse.class)
	public RestResponse getExpressInsuredDocuments(GetExpressInsuredDocumentsCommand cmd){
		return new RestResponse(expressService.getExpressInsuredDocuments(cmd));
	}
	

	/**
	 * <p>32.订单状态列表</p>
	 * <b>URL: /express/listExpressOrderStatus</b>
	 */
	@RequestMapping("listExpressOrderStatus")
	@RestReturn(ListExpressOrderStatusResponse.class)
	public RestResponse listExpressOrderStatus(){
		return new RestResponse(expressService.listExpressOrderStatus());
	}
	
	/**
	 * <p>33.国贸支付，直接调用我方payserver预支付</p>
	 * <b>URL: /express/prePayExpressOrder</b>
	 */
	@RequestMapping("prePayExpressOrder")
	@RestReturn(Map.class)
	public RestResponse prePayExpressOrder(PrePayExpressOrderCommand cmd){
		return new RestResponse(expressService.prePayExpressOrder(cmd));
	}
	
}