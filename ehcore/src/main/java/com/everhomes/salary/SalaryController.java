// @formatter:off
package com.everhomes.salary;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.salary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileTaskDTO;

@RestController
@RequestMapping("/salary")
public class SalaryController extends ControllerBase {
	
	@Autowired
	private SalaryService salaryService;

	/**
	 * <p>0.查询子公司</p>
	 * <b>URL: /salary/listEnterprises</b>
	 */
	@RequestMapping("listEnterprises")
	@RestReturn(ListEnterprisesResponse.class)
	public RestResponse listEnterprises(ListEnterprisesCommand cmd){
		ListEnterprisesResponse res = this.salaryService.listEnterprises(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>1.列出薪酬选项</p>
	 * <b>URL: /salary/listGroupEntities</b>
	 */
	@RequestMapping("listGroupEntities")
	@RestReturn(ListGroupEntitiesResponse.class)
	public RestResponse listGroupEntities(ListGroupEntitiesCommand cmd ){
		ListGroupEntitiesResponse res = this.salaryService.listGroupEntities(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>2.更新薪酬选项</p>
	 * <b>URL: /salary/updateGroupEntities</b>
	 */
	@RequestMapping("updateGroupEntities")
	@RestReturn(String.class)
	public RestResponse updateGroupEntities(UpdateGroupEntitiesCommand cmd ){
		this.salaryService.updateGroupEntities(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>3.查询子公司的人员</p>
	 * <b>URL: /salary/listSalaryEmployees</b>
	 */
	@RequestMapping("listSalaryEmployees")
	@RestReturn(ListSalaryEmployeesResponse.class)
	public RestResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd ){
		ListSalaryEmployeesResponse resp = this.salaryService.listSalaryEmployees(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <p>4.导出导入工资表模板</p>
	 * <b>URL: /salary/exportEmployeeSalaryTemplate</b>
	 */
	@RequestMapping("exportEmployeeSalaryTemplate")
	public HttpServletResponse exportEmployeeSalaryTemplate(ExportEmployeeSalaryTemplateCommand cmd ,
													 HttpServletResponse response ){
		HttpServletResponse resp = salaryService.exportEmployeeSalaryTemplate(cmd,response);
		return resp;
	}

	/**
	 * <p>5.查询人员的工资字段列表</p>
	 * <b>URL: /salary/getEmployeeEntities</b>
	 */
	@RequestMapping("getEmployeeEntities")
	@RestReturn(GetEmployeeEntitiesResponse.class)
	public RestResponse getEmployeeEntities(GetEmployeeEntitiesCommand cmd ){
		GetEmployeeEntitiesResponse resp = this.salaryService.getEmployeeEntities(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>6.导出工资表</p>
	 * <b>URL: /salary/exportEmployeeSalary</b>
	 */
	@RequestMapping("exportEmployeeSalary")
	@RestReturn(String.class)
	public RestResponse exportEmployeeSalary(ExportEmployeeSalaryTemplateCommand cmd ){
		salaryService.exportEmployeeSalary(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <p>7.导入工资表</p>
	 * <b>URL: /salary/importEmployeeSalary</b>
	 */
	@RequestMapping("importEmployeeSalary")
	@RestReturn(ImportFileTaskDTO.class)
	public RestResponse importEmployeeSalary(@RequestParam(value = "attachment") MultipartFile[] files,
													ExportEmployeeSalaryTemplateCommand cmd ){
		ImportFileTaskDTO dto = salaryService.importEmployeeSalary(cmd,files);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /salary/getImportResult</b>
	 * <p>8.查询导入结果</p>
	 */
	@RequestMapping("getImportResult")
	@RestReturn(value = ImportFileResponse.class)
	public RestResponse getImportResult(GetImportFileResultCommand cmd) {
		ImportFileResponse resp  = salaryService.getImportResult(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /salary/getSalaryGroupStatus</b>
	 * <p>9.进入工资报表页面</p>
	 */
	@RequestMapping("getSalaryGroupStatus")
	@RestReturn(value = GetSalaryGroupStatusResponse.class)
	public RestResponse getSalaryGroupStatus(GetSalaryGroupStatusCommand cmd) {
		GetSalaryGroupStatusResponse resp  = salaryService.getSalaryGroupStatus(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>10.导出工资报表</p>
	 * <b>URL: /salary/exportSalaryReport</b>
	 */
	@RequestMapping("exportSalaryReport")
	@RestReturn(String.class)
	public RestResponse exportEmployeeSalary(ExportSalaryReportCommand cmd ){
		salaryService.exportSalaryReport(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <p>11.归档报表</p>
	 * <b>URL: /salary/fileSalaryGroup</b>
	 */
	@RequestMapping("fileSalaryGroup")
	@RestReturn(FileSalaryGroupResponse.class)
	public RestResponse fileSalaryGroup(FileSalaryGroupCommand cmd ){
		FileSalaryGroupResponse resp = salaryService.fileSalaryGroup(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <p>11.查询异步任务状态</p>
	 * <b>URL: /salary/getSalaryTaskStatus</b>
	 */
	@RequestMapping("getSalaryTaskStatus")
	@RestReturn(GetSalaryTaskStatusResponse.class)
	public RestResponse getSalaryTaskStatus(GetSalaryTaskStatusCommand cmd ){
		GetSalaryTaskStatusResponse resp = salaryService.getSalaryTaskStatus(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>12.新建报表</p>
	 * <b>URL: /salary/newSalaryMonth</b>
	 */
	@RequestMapping("newSalaryMonth")
	@RestReturn(String.class)
	public RestResponse newSalaryMonth(NewSalaryMonthCommand cmd ){
		salaryService.newSalaryMonth(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <p>后台按年份查询每月工资条发放情况</p>
	 * <b>URL: /salary/listYearPayslipSummary</b>
	 */
	@RequestMapping("listYearPayslipSummary")
	@RestReturn(ListYearPayslipSummaryResponse.class)
	public RestResponse listYearPayslipSummary(ListYearPayslipSummaryCommand cmd){
		return new RestResponse(salaryService.listYearPayslipSummary(cmd));
	}	

	/**
	 * <p>上传工资条</p>
	 * <b>URL: /salary/importPayslip</b>
	 */
	@RequestMapping("importPayslip")
	@RestReturn(ImportPayslipResponse.class)
	public RestResponse importPayslip(@RequestParam(value = "attachment") MultipartFile[] files,ImportPayslipCommand cmd){
		return new RestResponse(salaryService.importPayslip(files,cmd));
	}

	/**
	 * <p>发放工资条</p>
	 * <b>URL: /salary/sendPayslip</b>
	 */
	@RequestMapping("sendPayslip")
	@RestReturn(SendPayslipResponse.class)
	public RestResponse sendPayslip(SendPayslipCommand cmd){
		SendPayslipResponse resp = salaryService.sendPayslip(cmd);
		return new RestResponse(resp);
	}

	/**
	 * <p>查看(某月)工资条记录</p>
	 * <b>URL: /salary/listMonthPayslipSummary</b>
	 */
	@RequestMapping("listMonthPayslipSummary")
	@RestReturn(ListMonthPayslipSummaryResponse.class)
	public RestResponse listMonthPayslipSummary(ListMonthPayslipSummaryCommand cmd){
		return new RestResponse(salaryService.listMonthPayslipSummary(cmd));
	}

	/**
	 * <p>查看(某次)工资条发放详情</p>
	 * <b>URL: /salary/listSendPayslipDetails</b>
	 */
	@RequestMapping("listSendPayslipDetails")
	@RestReturn(ListSendPayslipDetailsResponse.class)
	public RestResponse listSendPayslipDetails(ListSendPayslipDetailsCommand cmd){
		return new RestResponse(salaryService.listSendPayslipDetails(cmd));
	}

	/**
	 * <p>重发工资条</p>
	 * <b>URL: /salary/resendPayslip</b>
	 */
	@RequestMapping("resendPayslip")
	@RestReturn(String.class)
	public RestResponse resendPayslip(ResendPayslipCommand cmd){
		salaryService.resendPayslip(cmd);
		return new RestResponse();
	}

	/**
	 * <p>撤销工资条</p>
	 * <b>URL: /salary/revokePayslip</b>
	 */
	@RequestMapping("revokePayslip")
	@RestReturn(String.class)
	public RestResponse revokePayslip(RevokePayslipCommand cmd){
		salaryService.revokePayslip(cmd);
		return new RestResponse();
	}

	/**
	 * <p>删除工资条</p>
	 * <b>URL: /salary/deletePayslip</b>
	 */
	@RequestMapping("deletePayslip")
	@RestReturn(String.class)
	public RestResponse deletePayslip(DeletePayslipCommand cmd){
		salaryService.deletePayslip(cmd);
		return new RestResponse();
	}

	/**
	 * <p>查询工资条列表 </p>
	 * <b>URL: /salary/listUserPayslips</b>
	 */
	@RequestMapping("listUserPayslips")
	@RestReturn(ListUserPayslipsResponse.class)
	public RestResponse listUserPayslips(ListUserPayslipsCommand cmd){
		return new RestResponse(salaryService.listUserPayslips(cmd));
	}

	/**
	 * <p>查询工资条详情</p>
	 * <b>URL: /salary/listPayslipsDetail</b>
	 */
	@RequestMapping("listPayslipsDetail")
	@RestReturn(ListPayslipsDetailResponse.class)
	public RestResponse listPayslipsDetail(ListPayslipsDetailCommand cmd){
		return new RestResponse(salaryService.listPayslipsDetail(cmd));
	}

	/**
	 * <p>确认工资条</p>
	 * <b>URL: /salary/confirmPayslip</b>
	 */
	@RequestMapping("confirmPayslip")
	@RestReturn(String.class)
	public RestResponse confirmPayslip(ConfirmPayslipCommand cmd){
		salaryService.confirmPayslip(cmd);
		return new RestResponse();
	}

}