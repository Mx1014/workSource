// @formatter:off
package com.everhomes.print;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.print.GetPrintLogonUrlCommand;
import com.everhomes.rest.print.GetPrintLogonUrlResponse;
import com.everhomes.rest.print.GetPrintSettingCommand;
import com.everhomes.rest.print.GetPrintSettingResponse;
import com.everhomes.rest.print.GetPrintStatCommand;
import com.everhomes.rest.print.GetPrintStatResponse;
import com.everhomes.rest.print.GetPrintUnpaidOrderCommand;
import com.everhomes.rest.print.GetPrintUnpaidOrderResponse;
import com.everhomes.rest.print.GetPrintUserEmailCommand;
import com.everhomes.rest.print.GetPrintUserEmailResponse;
import com.everhomes.rest.print.InformPrintCommand;
import com.everhomes.rest.print.InformPrintResponse;
import com.everhomes.rest.print.ListPrintJobTypesCommand;
import com.everhomes.rest.print.ListPrintJobTypesResponse;
import com.everhomes.rest.print.ListPrintOrderStatusCommand;
import com.everhomes.rest.print.ListPrintOrderStatusResponse;
import com.everhomes.rest.print.ListPrintOrdersCommand;
import com.everhomes.rest.print.ListPrintOrdersResponse;
import com.everhomes.rest.print.ListPrintRecordsCommand;
import com.everhomes.rest.print.ListPrintRecordsResponse;
import com.everhomes.rest.print.ListPrintUserOrganizationsCommand;
import com.everhomes.rest.print.ListPrintUserOrganizationsResponse;
import com.everhomes.rest.print.ListPrintingJobsCommand;
import com.everhomes.rest.print.ListPrintingJobsResponse;
import com.everhomes.rest.print.LogonPrintCommand;
import com.everhomes.rest.print.PayPrintOrderCommand;
import com.everhomes.rest.print.PrintImmediatelyCommand;
import com.everhomes.rest.print.UnlockPrinterCommand;
import com.everhomes.rest.print.UpdatePrintSettingCommand;
import com.everhomes.rest.print.UpdatePrintUserEmailCommand;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.xml.XMLToJSON;

import sun.misc.BASE64Decoder;

@RestDoc(value="print controller", site="print")
@RestController
@RequestMapping("/siyinprint")
public class SiYinPrintController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiYinPrintController.class);
	
	@Autowired
	private SiyinPrintService siyinPrintService;

	 /**
	  * <b>URL: /siyinprint/getPrintSetting</b>
	  * <p>获取打印设置信息</p>
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
	  * <p>更新打印设置信息</p>
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
	  * <p>获取打印统计</p>
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
	  * <p>查询打印记录</p>
	  */
	 @RequestMapping("listPrintRecords")
	 @RestReturn(value=ListPrintRecordsResponse.class)
	 public RestResponse listPrintRecords(ListPrintRecordsCommand cmd) {
		
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/listPrintJobTypes</b>
	  * <p>获取打印模块类型列表 目前有打印/复印/扫描</p>
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
	  * <p>获取账单状态列表 目前有未支付/支付</p>
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
	  * <p>获取所在用户所在企业</p>
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
	  * <p>更新用户(扫描时)发件邮箱</p>
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
	  * <p>获取用户(扫描时)发件邮箱</p>
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
	  * <p>获取用户登录的URL，/print/informPrint，提供给前端生成二维码</p>
	  */
	 @RequestMapping("getPrintLogonUrl")
	 @RestReturn(value=GetPrintLogonUrlResponse.class)
	 @RequireAuthentication(false)
	 public RestResponse getPrintLogonUrl(GetPrintLogonUrlCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.getPrintLogonUrl(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/logonPrint</b>
	  * <p>web调用，登录打印机，如果手机扫了二维码，那么就会登录成功，否则不会登录成功。</p>
	  */
	 @RequestMapping("logonPrint")
	 @RestReturn(value=String.class)
	 @RequireAuthentication(false)
//	 public RestResponse logonPrint(LogonPrintCommand cmd) {
	public  DeferredResult<RestResponse> logonPrint(LogonPrintCommand cmd) {
		
//	     RestResponse response = new RestResponse(siyinPrintService.logonPrint(cmd));
//	     response.setErrorCode(ErrorCodes.SUCCESS);
//	     response.setErrorDescription("OK");
//	     DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
//	     deferredResult.setResult(response);
//	     return response;
	     return siyinPrintService.logonPrint(cmd);
	 }
	 
	 /**
	  * <b>URL: /siyinprint/informPrint</b>
	  * <p>用户扫描二维码，调用此接口，判断登录，通知web，登录成功或者失败</p>
	  */
	 @RequestMapping("informPrint")
	 @RestReturn(value=InformPrintResponse.class)
	 public RestResponse informPrint(InformPrintCommand cmd) {
		
	     RestResponse response = new RestResponse(siyinPrintService.informPrint(cmd));
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/printImmediately</b>
	  * <p>立即打印,增加任务数量</p>
	  */
	 @RequestMapping("printImmediately")
	 @RestReturn(value=String.class)
	 public RestResponse printImmediately(PrintImmediatelyCommand cmd) {
		 siyinPrintService.printImmediately(cmd);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * <b>URL: /siyinprint/listPrintOrders</b>
	  * <p>查询订单列表</p>
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
	  * <p>查询是否存在未支付订单</p>
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
	  * <p>支付订单</p>
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
	  * <b>URL: /siyinprint/listPrintingJobs</b>
	  * <p>查询正在进行的任务</p>
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
	  * <p>直接解锁打印机</p>
	  */
	 @RequestMapping("unlockPrinter")
	 @RestReturn(value=String.class)
	 public RestResponse unlockPrinter(UnlockPrinterCommand cmd) {
		 siyinPrintService.unlockPrinter(cmd);
	     RestResponse response = new RestResponse();
	     response.setErrorCode(ErrorCodes.SUCCESS);
	     response.setErrorDescription("OK");
	     return response;
	 }
	 
	 /**
	  * 
	  * <b>URL: /siyinprint/jobLogNotification</b>
	  * <p>司印方调用，任务日志处理</p>
	  */
	 @RequestMapping("jobLogNotification")
	 @RestReturn(String.class)
	 @RequireAuthentication(false)
	 public RestResponse jobLogNotification(@RequestParam(value="jobData", required=true) String jobData){
        RestResponse restResponse = new RestResponse();
        try{
            LOGGER.info("siyin params request:{}", jobData);
            BASE64Decoder decoder = new BASE64Decoder();
            jobData = new String(decoder.decodeBuffer(jobData));
            jobData = XMLToJSON.convertStandardJson(jobData);
            LOGGER.info("task log json:{}", jobData);
        }catch (Exception e){

        }

        return restResponse;
	 }
	 
	/**
	 * 
	 * <b>URL: /siyinprint/mfpLogNotification</b>
	 * <p>司印方调用，任务日志处理</p>
	 */
    @RequestMapping("mfpLogNotification")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse mfpLogNotification(@RequestParam(value="jobData", required=true) String jobData){
        RestResponse restResponse = new RestResponse();
        try{
            LOGGER.info("siyin mfpLogNotification request:{}", jobData);
            BASE64Decoder decoder = new BASE64Decoder();
            jobData = new String(decoder.decodeBuffer(jobData));
            jobData = XMLToJSON.convertStandardJson(jobData);
            LOGGER.info("task log json:{}", jobData);
        }catch (Exception e){

        }

        return restResponse;
    }
}
