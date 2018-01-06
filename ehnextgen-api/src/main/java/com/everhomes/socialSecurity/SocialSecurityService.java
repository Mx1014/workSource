// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.socialSecurity.*;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SocialSecurityService {

 
	void addSocialSecurity(AddSocialSecurityCommand cmd);

	void newSocialSecurityEmployee(Long detailId, String inMonth);

	ListSocialSecurityCitiesResponse listSocialSecurityCities(ListSocialSecurityCitiesCommand cmd);


	ListAccumulationFundCitiesResponse listAccumulationFundCities(ListAccumulationFundCitiesCommand cmd);


	ListFilterItemsResponse listFilterItems(ListFilterItemsCommand cmd);


	ListSocialSecurityPaymentsResponse listSocialSecurityPayments(ListSocialSecurityPaymentsCommand cmd);


	ListSocialSecurityEmployeeStatusResponse listSocialSecurityEmployeeStatus(ListSocialSecurityEmployeeStatusCommand cmd);


	SocialSecurityPaymentDetailDTO getSocialSecurityRule(GetSocialSecurityRuleCommand cmd);

	GetSocialSecurityPaymentDetailsResponse getSocialSecurityPaymentDetails(GetSocialSecurityPaymentDetailsCommand cmd);


	void updateSocialSecurityPayment(UpdateSocialSecurityPaymentCommand cmd);


	ImportFileTaskDTO importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd, MultipartFile file);

	CalculateSocialSecurityReportsResponse calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd);


	BigDecimal calculateAmount(BigDecimal radix, Integer ratio);

	ListSocialSecurityReportsResponse listSocialSecurityReports(ListSocialSecurityReportsCommand cmd);


	void exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd);


	ListSocialSecurityDepartmentSummarysResponse listSocialSecurityDepartmentSummarys(ListSocialSecurityDepartmentSummarysCommand cmd);


	void exportSocialSecurityDepartmentSummarys(ExportSocialSecurityDepartmentSummarysCommand cmd);


	ListSocialSecurityInoutReportsResponse listSocialSecurityInoutReports(ListSocialSecurityInoutReportsCommand cmd);


	void exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd);


	FileSocialSecurityResponse fileSocialSecurity(FileSocialSecurityCommand cmd);


	ListSocialSecurityHistoryFilesResponse listSocialSecurityHistoryFiles(ListSocialSecurityHistoryFilesCommand cmd);


	ListUserInoutHistoryResponse listUserInoutHistory(ListUserInoutHistoryCommand cmd);


	ListSocialSecurityHouseholdTypesResponse listSocialSecurityHouseholdTypes(ListSocialSecurityHouseholdTypesCommand cmd);


	ListAccumulationFundHouseholdTypesResponse listAccumulationFundHouseholdTypes(ListAccumulationFundHouseholdTypesCommand cmd);


	GetSocialSecurityReportStatusResponse getSocialSecurityReportStatus(GetSocialSecurityReportStatusCommand cmd);

	GetSocialSecurityReportsHeadResponse getSocialSecurityReportsHead(GetSocialSecurityReportsHeadCommand cmd);

	SocialSecurityInoutTimeDTO addSocialSecurityInOutTime(AddSocialSecurityInOutTimeCommand cmd);

	SocialSecurityEmployeeDTO getSocialSecurityEmployeeInfo(Long detailId);

    List<Long> listSocialSecurityEmployeeDetailIdsByPayMonth(Long ownerId, String payMonth);

	ImportFileResponse<Map<String, String>> getImportSocialSecurityPaymentsResult(
			GetImportSocialSecurityPaymentsResultCommand cmd);
}