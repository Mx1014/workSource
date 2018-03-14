// @formatter:off
package com.everhomes.salary;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.salary.*;
import com.everhomes.rest.uniongroup.RefreshPeriodValsCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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


}