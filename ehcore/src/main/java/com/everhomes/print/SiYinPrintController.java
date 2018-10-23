// @formatter:off
package com.everhomes.print;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.print.*;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.asset.AssetService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.util.RequireAuthentication;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

@RestDoc(value="print controller", site="print")
@RestController
@RequestMapping("/siyinprint")
public class SiYinPrintController extends ControllerBase {
	@Autowired
	private SiyinPrintService siyinPrintService;
	
	
	 /**
	  * <b>URL: /siyinprint/getPrintSetting</b>
	  * <p>1.获取打印设置信息(后台管理)</p>
	  */
	 @RequestMapping("getPrintSetting")
	 @RestReturn(value=GetPrintSettingResponse.class)
	 public RestResponse getPrintSetting(GetPrintSettingCommand cmd) {
	     RestResponse response = new RestResponse(siyinPrintService.getPrintSetting(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/updatePrintSetting</b>
	  * <p>2.更新打印设置信息(后台管理)</p>
	  */
	 @RequestMapping("updatePrintSetting")
	 @RestReturn(value=String.class)
	 public RestResponse updatePrintSetting(UpdatePrintSettingCommand cmd) {
		 siyinPrintService.updatePrintSetting(cmd);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 
	 /**
	  * <b>URL: /siyinprint/getPrintStat</b>
	  * <p>3.获取打印统计(后台管理)</p>
	  */
	 @RequestMapping("getPrintStat")
	 @RestReturn(value=GetPrintStatResponse.class)
	 public RestResponse getPrintStat(GetPrintStatCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.getPrintStat(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/listPrintRecords</b>
	  * <p>4.查询打印记录(后台管理)</p>
	  */
	 @RequestMapping("listPrintRecords")
	 @RestReturn(value=ListPrintRecordsResponse.class)
	 public RestResponse listPrintRecords(ListPrintRecordsCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.listPrintRecords(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/listPrintJobTypes</b>
	  * <p>5.获取打印模块类型列表 目前有打印/复印/扫描(后台管理)</p>
	  */
	 @RequestMapping("listPrintJobTypes")
	 @RestReturn(value=ListPrintJobTypesResponse.class)
	 public RestResponse listPrintJobTypes(ListPrintJobTypesCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.listPrintJobTypes(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/listPrintOrderStatus</b>
	  * <p>6.获取账单状态列表 目前有未支付/支付(后台管理)</p>
	  */
	 @RequestMapping("listPrintOrderStatus")
	 @RestReturn(value=ListPrintOrderStatusResponse.class)
	 public RestResponse listPrintOrderStatus(ListPrintOrderStatusCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.listPrintOrderStatus(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/listPrintUserOrganizations</b>
	  * <p>7.获取所在用户所在企业</p>
	  */
	 @RequestMapping("listPrintUserOrganizations")
	 @RestReturn(value=ListPrintUserOrganizationsResponse.class)
	 public RestResponse listPrintUserOrganizations(@Valid ListPrintUserOrganizationsCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.listPrintUserOrganizations(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/updatePrintUserEmail</b>
	  * <p>8.更新用户(扫描时)发件邮箱</p>
	  */
	 @RequestMapping("updatePrintUserEmail")
	 @RestReturn(value=String.class)
	 public RestResponse updatePrintUserEmail(UpdatePrintUserEmailCommand cmd) {
		 siyinPrintService.updatePrintUserEmail(cmd);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/getPrintUserEmail</b>
	  * <p>9.获取用户(扫描时)发件邮箱</p>
	  */
	 @RequestMapping("getPrintUserEmail")
	 @RestReturn(value=GetPrintUserEmailResponse.class)
	 public RestResponse getPrintUserEmail(GetPrintUserEmailCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.getPrintUserEmail(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/getPrintLogonUrl</b>
	  * <p>10.获取用户登录的URL，/print/informPrint，提供给前端生成二维码</p>
	  */
	 @RequestMapping("getPrintLogonUrl")
	 @RestReturn(value=GetPrintLogonUrlResponse.class)
	 @RequireAuthentication(false)
	 @Deprecated //使用司印二维码定制。，此接口废弃
	 public RestResponse getPrintLogonUrl(GetPrintLogonUrlCommand cmd) {
	     RestResponse response = new RestResponse(siyinPrintService.getPrintLogonUrl(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/logonPrint</b>
	  * <p>11.web调用，登录打印机，如果手机扫了二维码，那么就会登录成功，否则不会登录成功。</p>
	  */
	 @RequestMapping("logonPrint")
	 @RestReturn(value=String.class)
	 @RequireAuthentication(false)
	 @Deprecated //使用司印二维码定制。，此接口废弃
	 public  DeferredResult<RestResponse> logonPrint(@RequestParam(value="identifierToken", required=true)String identifierToken) {
//	 public RestResponse logonPrint(LogonPrintCommand cmd) {
		
//	     RestResponse response = new RestResponse(siyinPrintService.logonPrint(cmd));
//	     response.setErrorCode(ErrorCodes.SUCCESS);
//	     response.setErrorDescription("OK");
//	     DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
//	     deferredResult.setResult(response);
//	     return response;
	     return siyinPrintService.logonPrint(identifierToken);
	 }
	 
	 /**
	  * <b>URL: /siyinprint/informPrint</b>
	  * <p>12.用户扫描二维码，调用此接口，判断登录，通知web，登录成功或者失败</p>
	  */
	 @RequestMapping("informPrint")
	 @RestReturn(value=InformPrintResponse.class)
	 @Deprecated //使用司印二维码定制。，此接口废弃
	 public RestResponse informPrint(InformPrintCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.informPrint(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/printImmediately</b>
	  * <p>13.立即打印,增加任务数量,废弃</p>
	  */
	 @RequestMapping("printImmediately")
	 @RestReturn(value=String.class)
	 @Deprecated
	 public RestResponse printImmediately(PrintImmediatelyCommand cmd) {
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/listPrintOrders</b>
	  * <p>14.查询订单列表(app)</p>
	  */
	 @RequestMapping("listPrintOrders")
	 @RestReturn(value=ListPrintOrdersResponse.class)
	 public RestResponse listPrintOrders(ListPrintOrdersCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.listPrintOrders(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 
	 /**
	  * <b>URL: /siyinprint/getPrintUnpaidOrder</b>
	  * <p>15.查询是否存在未支付订单</p>
	  */
	 @RequestMapping("getPrintUnpaidOrder")
	 @RestReturn(value=GetPrintUnpaidOrderResponse.class)
	 public RestResponse getPrintUnpaidOrder(GetPrintUnpaidOrderCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.getPrintUnpaidOrder(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/payPrintOrder</b>
	  * <p>16.支付订单</p>
	  */
	 @RequestMapping("payPrintOrder")
	 @RestReturn(value=CommonOrderDTO.class)
	 public RestResponse payPrintOrder(PayPrintOrderCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.payPrintOrder(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }

	/**
	 * <b>URL: /siyinprint/payPrintOrderV2</b>
	 * <p>17.新支付订单</p>
	 */
	@RequestMapping("payPrintOrderV2")
	@RestReturn(value=PreOrderDTO.class)
	public RestResponse payPrintOrderV2(PayPrintOrderCommandV2 cmd) {

		RestResponse response = new RestResponse(siyinPrintService.payPrintOrderV2(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /siyinprint/notifySiyinprintOrderPaymentV2</b>
	 * <p>17.1.支付回调订单</p>
	 */
	@RequestMapping("notifySiyinprintOrderPaymentV2")
	@RestReturn(value=String.class)
	@RequireAuthentication(false)
	public RestResponse notifySiyinprintOrderPaymentV2(MerchantPaymentNotificationCommand cmd) {
		siyinPrintService.notifySiyinprintOrderPaymentV2(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	 
	 /**
	  * <b>URL: /siyinprint/listPrintingJobs</b>
	  * <p>18.查询正在进行的任务</p>
	  */
	 @RequestMapping("listPrintingJobs")
	 @RestReturn(value=ListPrintingJobsResponse.class)
	 public RestResponse listPrintingJobs(ListPrintingJobsCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.listPrintingJobs(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/unlockPrinter</b>
	  * <p>19.直接解锁打印机或扫描驱动后调用的.请先调用 /siyinprint/getPrintUnpaidOrder 接口检查未支付订单</p>
	  */
	 @RequestMapping("unlockPrinter")
	 @RestReturn(value=UnlockPrinterResponse.class)
	 public RestResponse unlockPrinter(UnlockPrinterCommand cmd) {
		 UnlockPrinterResponse r = siyinPrintService.unlockPrinter(cmd);
	     RestResponse response = new RestResponse(r);
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * 
	  * <b>URL: /siyinprint/jobLogNotification</b>
	  * <p>20.司印方调用，任务日志处理</p>
	  */
	 @RequestMapping("jobLogNotification")
	 @RestReturn(String.class)
	 @RequireAuthentication(false)
	 public RestResponse jobLogNotification(@RequestParam(value="jobData", required=true) String jobData){
		siyinPrintService.jobLogNotification(jobData);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
	    response.setErrorDescription("OK");
        return response;
	 }
	 
	/**
	 * 
	 * <b>URL: /siyinprint/mfpLogNotification</b>
	 * <p>21.司印方调用，任务日志处理</p>
	 */
    @RequestMapping("mfpLogNotification")
    @RequireAuthentication(false)
    public void mfpLogNotification(@RequestParam(value="jobData", required=true) String jobData, HttpServletResponse response){
    	siyinPrintService.mfpLogNotification(jobData,response);
    }
    
    /**
	  * <b>URL: /siyinprint/listQueueJobs</b>
	  * <p>22.查询待打印的任务</p>
	  */
	 @RequestMapping("listQueueJobs")
	 @RestReturn(value=ListQueueJobsResponse.class)
	 public RestResponse listQueueJobs(ListQueueJobsCommand cmd) {
	     RestResponse response = new RestResponse(siyinPrintService.listQueueJobs(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/releaseQueueJobs</b>
	  * <p>23.放行指定的待打印任务</p>
	  */
	 @RequestMapping("releaseQueueJobs")
	 @RestReturn(value=String.class)
	 public RestResponse releaseQueueJobs(ReleaseQueueJobsCommand cmd) {
	     siyinPrintService.releaseQueueJobs(cmd);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/deleteQueueJobs</b>
	  * <p>24.删除指定的待打印任务</p>
	  */
	 @RequestMapping("deleteQueueJobs")
	 @RestReturn(value=String.class)
	 public RestResponse deleteQueueJobs(DeleteQueueJobsCommand cmd) {
	     siyinPrintService.deleteQueueJobs(cmd);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/getPrintQrcode</b>
	  * <p>25.提供给司印的二维码</p>
	  */
	 @RequestMapping("getPrintQrcode")
	 @RestReturn(value=String.class)
	 @RequireAuthentication(false)
	 public RestResponse getPrintQrcode(HttpServletRequest req,HttpServletResponse rps) {
	     siyinPrintService.getPrintQrcode(req,rps);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }

	/**
	 * <b>URL: /siyinprint/listPayeeAccount </b>
	 * <p>获取收款方账号</p>
	 */
	@RequestMapping("listPayeeAccount")
	@RestReturn(value=ListBizPayeeAccountDTO.class,collection = true)
	public RestResponse listPayeeAccount(ListPayeeAccountCommand cmd) {

		RestResponse response = new RestResponse(siyinPrintService.listPayeeAccount(cmd));
		response.setErrorDescription("OK");
		response.setErrorCode(ErrorCodes.SUCCESS);
		return response;
	}

	/**
	 * <b>URL: /siyinprint/createOrUpdateBusinessPayeeAccount </b>
	 * <p>关联收款方账号到具体业务</p>
	 */
	@RequestMapping("createOrUpdateBusinessPayeeAccount")
	@RestReturn(value=String.class)
	public RestResponse createOrUpdateBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd) {

        siyinPrintService.createOrUpdateBusinessPayeeAccount(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /siyinprint/getBusinessPayeeAccount </b>
	 * <p>获取已关联收款账号的业务</p>
	 */
	@RequestMapping("getBusinessPayeeAccount")
	@RestReturn(value=BusinessPayeeAccountDTO.class)
	public RestResponse getBusinessPayeeAccount(ListBusinessPayeeAccountCommand cmd) {

		RestResponse response = new RestResponse(siyinPrintService.getBusinessPayeeAccount(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /siyinprint/initPayeeAccount </b>
	 * <p>将老的账户初始化到账号表</p>
	 */
	@RequestMapping("initPayeeAccount")
	@RestReturn(value=String.class)
	public RestResponse initPayeeAccount(@RequestParam("attachment") MultipartFile[] files) {
		siyinPrintService.initPayeeAccount(files);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 *
	 * <b>URL: /siyinprint/mfpLogNotificationV2</b>
	 * <p>21.司印方调用，任务日志处理</p>
	 */
	@RequestMapping("mfpLogNotificationV2")
	@RequireAuthentication(false)
	public void mfpLogNotificationV2(MfpLogNotificationV2Command cmd, HttpServletResponse response){
		siyinPrintService.mfpLogNotificationV2(cmd,response);
	}
	
	/**
	 * <b>URL: /siyinprint/payPrintGeneralOrder</b>
	 * <p>22.统一订单支付</p>
	 */
	@RequestMapping("payPrintGeneralOrder")
	@RestReturn(value=PayPrintGeneralOrderResponse.class)
	public RestResponse payPrintGeneralOrder(PayPrintGeneralOrderCommand cmd) {
		RestResponse response = new RestResponse(siyinPrintService.payPrintGeneralOrder(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	 /**
	  * <b>URL: /siyinprint/getPrintOrders</b>
	  * <p>23.根据orderNo查询订单</p>
	  */
	 @RequestMapping("getPrintOrders")
	 @RestReturn(value=GetPrintOrdersResponse.class)
	 public RestResponse getPrintOrders(GetPrintOrdersCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.getPrintOrders(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
}
