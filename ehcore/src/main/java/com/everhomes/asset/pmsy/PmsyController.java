// @formatter:off
package com.everhomes.asset.pmsy;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.pmsy.PmsyProvider;
import com.everhomes.organization.pmsy.PmsyService;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmsy.AddressDTO;
import com.everhomes.rest.pmsy.CreatePmsyBillOrderCommand;
import com.everhomes.rest.pmsy.GetPmsyBills;
import com.everhomes.rest.pmsy.GetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.ListPmsyBillsCommand;
import com.everhomes.rest.pmsy.ListResourceCommand;
import com.everhomes.rest.pmsy.PmsyBillsDTO;
import com.everhomes.rest.pmsy.PmsyBillsResponse;
import com.everhomes.rest.pmsy.PmsyCommunityDTO;
import com.everhomes.rest.pmsy.PmsyPayerDTO;
import com.everhomes.rest.pmsy.SearchBillsOrdersCommand;
import com.everhomes.rest.pmsy.SearchBillsOrdersResponse;
import com.everhomes.rest.pmsy.SetPmsyPropertyCommand;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/pmsy")
public class PmsyController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyController.class);

	@Autowired
	private PmsyService pmsyService;

	@Autowired
	private PmsyProvider pmsyProvider;
	/**
	 * <b>URL: /pmsy/listPmPayers</b>
	 * <p>获取用户填写过的有效缴费用户信息</p>
	 */
	@RequestMapping("listPmPayers")
	@RestReturn(value=PmsyPayerDTO.class,collection=true)
	public RestResponse listPmPayers(/*@Valid ListPmPayerCommand cmd*/) {
		List<PmsyPayerDTO> resultList = pmsyService.listPmPayers();
		RestResponse response = new RestResponse(resultList);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pmsy/listAddresses</b>
	 * <p>获取门牌号(项目)</p>
	 */
	@RequestMapping("listAddresses")
	@RestReturn(value=AddressDTO.class,collection=true)
	public RestResponse listAddresses(@Valid ListResourceCommand cmd) {
		
		List<AddressDTO> resultList = pmsyService.listAddresses(cmd);
		
		RestResponse response = new RestResponse(resultList);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");      
		return response;
	}
	
	/**
	 * <b>URL: /pmsy/listPmBills</b>
	 * <p>根据条件查询物业缴费单</p>
	 */
	@RequestMapping(value="listPmBills",method = RequestMethod.POST)
	@RestReturn(value=PmsyBillsResponse.class)
	public RestResponse listPmBills(@Valid ListPmsyBillsCommand cmd) {
		PmsyBillsResponse r = pmsyService.listPmBills(cmd);
		RestResponse response = new RestResponse(r);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pmsy/getPmsyBills</b>
	 * <p>查询单月物业缴费单</p>
	 */
	@RequestMapping(value="getPmsyBills")
	@RestReturn(value=PmsyBillsDTO.class)
	public RestResponse getPmsyBills(@Valid GetPmsyBills cmd) {
		PmsyBillsDTO monthlyBill = pmsyService.getMonthlyPmBill(cmd);
		RestResponse response = new RestResponse(monthlyBill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pmsy/serachBillingOrders</b>
	 * <p>根据条件搜索缴费记录</p>
	 */
	@RequestMapping("searchBillingOrders")
	@RestReturn(value=SearchBillsOrdersResponse.class)
	public RestResponse searchBillingOrders(@Valid SearchBillsOrdersCommand cmd) {
		SearchBillsOrdersResponse serachBillsOrdersResponse = pmsyService.searchBillingOrders(cmd);
		
		RestResponse response = new RestResponse(serachBillsOrdersResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
     * <b>URL: /pmsy/notifyParkingRechargeOrderPayment</b>
     * <p>支付后，由统一支付调用此接口来通知各厂商支付结果</p>
     */
    @RequestMapping("notifyPmsyOrderPayment")
    @RestReturn(value = String.class)
    public RestResponse notifyPmsyOrderPayment(PayCallbackCommand cmd) {
        
    	pmsyService.notifyPmsyOrderPayment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmsy/createPmBillOrder</b>
     * <p>创建订单</p>
     */
    @RequestMapping("createPmBillOrder")
	@RestReturn(value=CommonOrderDTO.class)
	public RestResponse createPmBillOrder(@Valid CreatePmsyBillOrderCommand cmd) {
		CommonOrderDTO order = pmsyService.createPmBillOrder(cmd);
		RestResponse response = new RestResponse(order);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
    
    /**
     * <b>URL: /pmsy/createPmBillOrderV2</b>
     * <p>创建订单</p>
     */
    @RequestMapping("createPmBillOrderV2")
    @RestReturn(value=PreOrderDTO.class)
    public RestResponse createPmBillOrderV2(@Valid CreatePmsyBillOrderCommand cmd) {
        PreOrderDTO dto = pmsyService.createPmBillOrderV2(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    
    
    /**
     * <b>URL: /pmsy/setPmProperty</b>
     * <p>设置提示信息和手机号</p>
     */
    @RequestMapping("setPmProperty")
	@RestReturn(value=String.class)
	public RestResponse setPmProperty(@Valid SetPmsyPropertyCommand cmd) {
    	
    	pmsyService.setPmProperty(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
    
    /**
     * <b>URL: /pmsy/setPmProperty</b>
     * <p>设置提示信息和手机号</p>
     */
    @RequestMapping("getPmProperty")
	@RestReturn(value=String.class)
	public RestResponse getPmProperty(@Valid GetPmsyPropertyCommand cmd) {
    	PmsyCommunityDTO dto = pmsyService.getPmProperty(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
	 * <b>URL: /pmsy/payNotify</b>
	 * <p>支付模块回调接口，通知支付结果</p>
	 */
	@RequestMapping("payNotify")
	@RestReturn(value=String.class)
	@RequireAuthentication(false)
	public RestResponse payNotify(@Valid OrderPaymentNotificationCommand cmd) {
		pmsyService.payNotify(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
