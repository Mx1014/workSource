// @formatter:off
package com.everhomes.express;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.express.AddExpressUserCommand;
import com.everhomes.rest.express.DeleteExpressUserCommand;
import com.everhomes.rest.express.GetExpressOrderDetailCommand;
import com.everhomes.rest.express.GetExpressOrderDetailResponse;
import com.everhomes.rest.express.ListExpressCompanyCommand;
import com.everhomes.rest.express.ListExpressCompanyResponse;
import com.everhomes.rest.express.ListExpressOrderCommand;
import com.everhomes.rest.express.ListExpressOrderResponse;
import com.everhomes.rest.express.ListExpressUserCommand;
import com.everhomes.rest.express.ListExpressUserResponse;
import com.everhomes.rest.express.ListServiceAddressCommand;
import com.everhomes.rest.express.ListServiceAddressResponse;
import com.everhomes.rest.express.PrintExpressOrderCommand;
import com.everhomes.rest.express.UpdatePaySummaryCommand;

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
		expressService.addExpressUser(cmd);
		return new RestResponse();
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
	 * <p>6.快递订单列表</p>
	 * <b>URL: /express/listExpressOrder</b>
	 */
	@RequestMapping("listExpressOrder")
	@RestReturn(ListExpressOrderResponse.class)
	public RestResponse listExpressOrder(ListExpressOrderCommand cmd){
		return new RestResponse(expressService.listExpressOrder(cmd));
	}

	/**
	 * <p>7.快递订单列表</p>
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
	 * <p>9.出单</p>
	 * <b>URL: /express/printExpressOrder</b>
	 */
	@RequestMapping("printExpressOrder")
	@RestReturn(String.class)
	public RestResponse printExpressOrder(PrintExpressOrderCommand cmd){
		expressService.printExpressOrder(cmd);
		return new RestResponse();
	}

}