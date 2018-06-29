// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.GetImportFileResultCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.socialSecurity.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
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


	void exportImportFileFailResults(GetImportFileResultCommand cmd, HttpServletResponse httpResponse);

	ImportFileTaskDTO importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd, MultipartFile file);

	CalculateSocialSecurityReportsResponse calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd);


	BigDecimal calculateAmount(BigDecimal radix, Integer ratio);

	ListSocialSecurityReportsResponse listSocialSecurityReports(ListSocialSecurityReportsCommand cmd);


	void exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd);


	ListSocialSecurityDepartmentSummarysResponse listSocialSecurityDepartmentSummarys(ListSocialSecurityDepartmentSummarysCommand cmd);


	OutputStream getSocialSecurityDepartmentSummarysOutputStream(Long ownerId, String payMonth);

	void exportSocialSecurityDepartmentSummarys(ExportSocialSecurityDepartmentSummarysCommand cmd);


	ListSocialSecurityInoutReportsResponse listSocialSecurityInoutReports(ListSocialSecurityInoutReportsCommand cmd);


	OutputStream getSocialSecurityInoutReportsOutputStream(Long ownerId, String payMonth);

	void exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd);


	FileSocialSecurityResponse fileSocialSecurity(FileSocialSecurityCommand cmd);


	ListSocialSecurityHistoryFilesResponse listSocialSecurityHistoryFiles(ListSocialSecurityHistoryFilesCommand cmd);


	ListUserInoutHistoryResponse listUserInoutHistory(ListUserInoutHistoryCommand cmd);


	ListSocialSecurityHouseholdTypesResponse listSocialSecurityHouseholdTypes(ListSocialSecurityHouseholdTypesCommand cmd);


	ListAccumulationFundHouseholdTypesResponse listAccumulationFundHouseholdTypes(ListAccumulationFundHouseholdTypesCommand cmd);


	GetSocialSecurityReportStatusResponse getSocialSecurityReportStatus(GetSocialSecurityReportStatusCommand cmd);

	GetSocialSecurityReportsHeadResponse getSocialSecurityReportsHead(GetSocialSecurityReportsHeadCommand cmd);

	String findNameByOwnerAndUser(Long ownerId, Long creatorUid);

	List<SocialSecurityEmployeeDTO> listSocialSecurityEmployees(ListSocialSecurityPaymentsCommand cmd);

	java.sql.Date getTheFirstDate(String m);

	java.sql.Date getTheLastDate(String m);

	SocialSecurityEmployeeDTO getSocialSecurityEmployeeInfo(Long detailId);

//    List<Long> listSocialSecurityEmployeeDetailIdsByPayMonth(Long ownerId, String payMonth);

	ImportFileResponse getImportSocialSecurityPaymentsResult(
			GetImportSocialSecurityPaymentsResultCommand cmd);

	OutputStream getSocialSecurityReportsOutputStream(Long ownerId, String payMonth);

	public void increseSocialSecurity(IncreseSocialSecurityCommand cmd);


	public void decreseSocialSecurity(DecreseSocialSecurityCommand cmd);

}