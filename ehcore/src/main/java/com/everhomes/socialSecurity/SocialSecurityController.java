// @formatter:off
package com.everhomes.socialSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.socialSecurity.AddSocialSecurityCommand;
import com.everhomes.rest.socialSecurity.CalculateSocialSecurityReportsCommand;
import com.everhomes.rest.socialSecurity.ExportSocialSecurityDepartmentSummarysCommand;
import com.everhomes.rest.socialSecurity.ExportSocialSecurityInoutReportsCommand;
import com.everhomes.rest.socialSecurity.ExportSocialSecurityReportsCommand;
import com.everhomes.rest.socialSecurity.FileSocialSecurityCommand;
import com.everhomes.rest.socialSecurity.GetSocialSecurityPaymentDetailsCommand;
import com.everhomes.rest.socialSecurity.GetSocialSecurityPaymentDetailsResponse;
import com.everhomes.rest.socialSecurity.GetSocialSecurityReportStatusCommand;
import com.everhomes.rest.socialSecurity.GetSocialSecurityReportStatusResponse;
import com.everhomes.rest.socialSecurity.ImportSocialSecurityPaymentsCommand;
import com.everhomes.rest.socialSecurity.ListAccumulationFundCitiesCommand;
import com.everhomes.rest.socialSecurity.ListAccumulationFundCitiesResponse;
import com.everhomes.rest.socialSecurity.ListAccumulationFundHouseholdTypesCommand;
import com.everhomes.rest.socialSecurity.ListAccumulationFundHouseholdTypesResponse;
import com.everhomes.rest.socialSecurity.ListFilterItemsCommand;
import com.everhomes.rest.socialSecurity.ListFilterItemsResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityCitiesCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityCitiesResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityDepartmentSummarysCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityDepartmentSummarysResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityEmployeeStatusCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityEmployeeStatusResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityHistoryFilesCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityHistoryFilesResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityHouseholdTypesCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityHouseholdTypesResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityInoutReportsCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityInoutReportsResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityPaymentsCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityPaymentsResponse;
import com.everhomes.rest.socialSecurity.ListSocialSecurityReportsCommand;
import com.everhomes.rest.socialSecurity.ListSocialSecurityReportsResponse;
import com.everhomes.rest.socialSecurity.ListUserInoutHistoryCommand;
import com.everhomes.rest.socialSecurity.ListUserInoutHistoryResponse;
import com.everhomes.rest.socialSecurity.UpdateSocialSecurityPaymentCommand;

@RestController
@RequestMapping("/socialSecurity")
public class SocialSecurityController extends ControllerBase {
	
	@Autowired
	private SocialSecurityService socialSecurityService;
	 

	/**
	 * <p>1.新建报表</p>
	 * <b>URL: /socialSecurity/addSocialSecurity</b>
	 */
	@RequestMapping("addSocialSecurity")
	@RestReturn(String.class)
	public RestResponse addSocialSecurity(AddSocialSecurityCommand cmd){
		socialSecurityService.addSocialSecurity(cmd);
		return new RestResponse();
	}

	/**
	 * <p>2.查询社保的城市列表</p>
	 * <b>URL: /socialSecurity/listSocialSecurityCities</b>
	 */
	@RequestMapping("listSocialSecurityCitys")
	@RestReturn(ListSocialSecurityCitiesResponse.class)
	public RestResponse listSocialSecurityCitys(ListSocialSecurityCitiesCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityCities(cmd));
	}

	/**
	 * <p>3.查询公积金的城市列表</p>
	 * <b>URL: /socialSecurity/listAccumulationFundCities</b>
	 */
	@RequestMapping("listAccumulationFundCitys")
	@RestReturn(ListAccumulationFundCitiesResponse.class)
	public RestResponse listAccumulationFundCitys(ListAccumulationFundCitiesCommand cmd){
		return new RestResponse(socialSecurityService.listAccumulationFundCities(cmd));
	}

	/**
	 * <p>4.查询筛选项</p>
	 * <b>URL: /socialSecurity/listFilterItems</b>
	 */
	@RequestMapping("listFilterItems")
	@RestReturn(ListFilterItemsResponse.class)
	public RestResponse listFilterItems(ListFilterItemsCommand cmd){
		return new RestResponse(socialSecurityService.listFilterItems(cmd));
	}

	/**
	 * <p>5.查询一个公司当前月社保缴费信息</p>
	 * <b>URL: /socialSecurity/listSocialSecurityPayments</b>
	 */
	@RequestMapping("listSocialSecurityPayments")
	@RestReturn(ListSocialSecurityPaymentsResponse.class)
	public RestResponse listSocialSecurityPayments(ListSocialSecurityPaymentsCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityPayments(cmd));
	}

	/**
	 * <p>6.查询一个公司当前月社保在缴/增减员/入离职 人数</p>
	 * <b>URL: /socialSecurity/listSocialSecurityEmployeeStatus</b>
	 */
	@RequestMapping("listSocialSecurityEmployeeStatus")
	@RestReturn(ListSocialSecurityEmployeeStatusResponse.class)
	public RestResponse listSocialSecurityEmployeeStatus(ListSocialSecurityEmployeeStatusCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityEmployeeStatus(cmd));
	}

	/**
	 * <p>7.查看某个人的社保具体项目</p>
	 * <b>URL: /socialSecurity/getSocialSecurityPaymentDetails</b>
	 */
	@RequestMapping("getSocialSecurityPaymentDetails")
	@RestReturn(GetSocialSecurityPaymentDetailsResponse.class)
	public RestResponse getSocialSecurityPaymentDetails(GetSocialSecurityPaymentDetailsCommand cmd){
		return new RestResponse(socialSecurityService.getSocialSecurityPaymentDetails(cmd));
	}

	/**
	 * <p>8.更新某个人的社保具体项目设置</p>
	 * <b>URL: /socialSecurity/updateSocialSecurityPayment</b>
	 */
	@RequestMapping("updateSocialSecurityPayment")
	@RestReturn(String.class)
	public RestResponse updateSocialSecurityPayment(UpdateSocialSecurityPaymentCommand cmd){
		socialSecurityService.updateSocialSecurityPayment(cmd);
		return new RestResponse();
	}

	/**
	 * <p>9.批量导入修改社保具体项目设置</p>
	 * <b>URL: /socialSecurity/importSocialSecurityPayments</b>
	 */
	@RequestMapping("importSocialSecurityPayments")
	@RestReturn(String.class)
	public RestResponse importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd){
		socialSecurityService.importSocialSecurityPayments(cmd);
		return new RestResponse();
	}

	/**
	 * <p>10.汇总计算报表接口</p>
	 * <b>URL: /socialSecurity/calculateSocialSecurityReports</b>
	 */
	@RequestMapping("calculateSocialSecurityReports")
	@RestReturn(String.class)
	public RestResponse calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd){
		socialSecurityService.calculateSocialSecurityReports(cmd);
		return new RestResponse();
	}

	/**
	 * <p>11.查询社保报表</p>
	 * <b>URL: /socialSecurity/listSocialSecurityReports</b>
	 */
	@RequestMapping("listSocialSecurityReports")
	@RestReturn(ListSocialSecurityReportsResponse.class)
	public RestResponse listSocialSecurityReports(ListSocialSecurityReportsCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityReports(cmd));
	}

	/**
	 * <p>12.导出社保报表</p>
	 * <b>URL: /socialSecurity/exportSocialSecurityReports</b>
	 */
	@RequestMapping("exportSocialSecurityReports")
	@RestReturn(String.class)
	public RestResponse exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd){
		socialSecurityService.exportSocialSecurityReports(cmd);
		return new RestResponse();
	}

	/**
	 * <p>13.查询部门汇总</p>
	 * <b>URL: /socialSecurity/listSocialSecurityDepartmentSummarys</b>
	 */
	@RequestMapping("listSocialSecurityDepartmentSummarys")
	@RestReturn(ListSocialSecurityDepartmentSummarysResponse.class)
	public RestResponse listSocialSecurityDepartmentSummarys(ListSocialSecurityDepartmentSummarysCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityDepartmentSummarys(cmd));
	}

	/**
	 * <p>14.导出部门汇总</p>
	 * <b>URL: /socialSecurity/exportSocialSecurityDepartmentSummarys</b>
	 */
	@RequestMapping("exportSocialSecurityDepartmentSummarys")
	@RestReturn(String.class)
	public RestResponse exportSocialSecurityDepartmentSummarys(ExportSocialSecurityDepartmentSummarysCommand cmd){
		socialSecurityService.exportSocialSecurityDepartmentSummarys(cmd);
		return new RestResponse();
	}

	/**
	 * <p>15.查询社保增减表</p>
	 * <b>URL: /socialSecurity/listSocialSecurityInoutReports</b>
	 */
	@RequestMapping("listSocialSecurityInoutReports")
	@RestReturn(ListSocialSecurityInoutReportsResponse.class)
	public RestResponse listSocialSecurityInoutReports(ListSocialSecurityInoutReportsCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityInoutReports(cmd));
	}

	/**
	 * <p>16.导出社保增减表</p>
	 * <b>URL: /socialSecurity/exportSocialSecurityInoutReports</b>
	 */
	@RequestMapping("exportSocialSecurityInoutReports")
	@RestReturn(String.class)
	public RestResponse exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd){
		socialSecurityService.exportSocialSecurityInoutReports(cmd);
		return new RestResponse();
	}

	/**
	 * <p>17.归档报表</p>
	 * <b>URL: /socialSecurity/fileSocialSecurity</b>
	 */
	@RequestMapping("fileSocialSecurity")
	@RestReturn(String.class)
	public RestResponse fileSocialSecurity(FileSocialSecurityCommand cmd){
		socialSecurityService.fileSocialSecurity(cmd);
		return new RestResponse();
	}

	/**
	 * <p>18.查看历史归档</p>
	 * <b>URL: /socialSecurity/listSocialSecurityHistoryFiles</b>
	 */
	@RequestMapping("listSocialSecurityHistoryFiles")
	@RestReturn(ListSocialSecurityHistoryFilesResponse.class)
	public RestResponse listSocialSecurityHistoryFiles(ListSocialSecurityHistoryFilesCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityHistoryFiles(cmd));
	}

	/**
	 * <p>19.查询查询增减员记录项</p>
	 * <b>URL: /socialSecurity/listUserInoutHistory</b>
	 */
	@RequestMapping("listUserInoutHistory")
	@RestReturn(ListUserInoutHistoryResponse.class)
	public RestResponse listUserInoutHistory(ListUserInoutHistoryCommand cmd){
		return new RestResponse(socialSecurityService.listUserInoutHistory(cmd));
	}

	/**
	 * <p>20.查询社保户籍档次</p>
	 * <b>URL: /socialSecurity/listSocialSecurityHouseholdTypes</b>
	 */
	@RequestMapping("listSocialSecurityHouseholdTypes")
	@RestReturn(ListSocialSecurityHouseholdTypesResponse.class)
	public RestResponse listSocialSecurityHouseholdTypes(ListSocialSecurityHouseholdTypesCommand cmd){
		return new RestResponse(socialSecurityService.listSocialSecurityHouseholdTypes(cmd));
	}

	/**
	 * <p>21.查询公积金户籍档次</p>
	 * <b>URL: /socialSecurity/listAccumulationFundHouseholdTypes</b>
	 */
	@RequestMapping("listAccumulationFundHouseholdTypes")
	@RestReturn(ListAccumulationFundHouseholdTypesResponse.class)
	public RestResponse listAccumulationFundHouseholdTypes(ListAccumulationFundHouseholdTypesCommand cmd){
		return new RestResponse(socialSecurityService.listAccumulationFundHouseholdTypes(cmd));
	}

	/**
	 * <p>22.得到汇总计算结果</p>
	 * <b>URL: /socialSecurity/getSocialSecurityReportStatus</b>
	 */
	@RequestMapping("getSocialSecurityReportStatus")
	@RestReturn(GetSocialSecurityReportStatusResponse.class)
	public RestResponse getSocialSecurityReportStatus(GetSocialSecurityReportStatusCommand cmd){
		return new RestResponse(socialSecurityService.getSocialSecurityReportStatus(cmd));
	}

}