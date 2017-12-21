// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.rest.socialSecurity.*;

public interface SocialSecurityService {

 
	public void addSocialSecurity(AddSocialSecurityCommand cmd);


	public ListSocialSecurityCitiesResponse listSocialSecurityCities(ListSocialSecurityCitiesCommand cmd);


	public ListAccumulationFundCitiesResponse listAccumulationFundCities(ListAccumulationFundCitiesCommand cmd);


	public ListFilterItemsResponse listFilterItems(ListFilterItemsCommand cmd);


	public ListSocialSecurityPaymentsResponse listSocialSecurityPayments(ListSocialSecurityPaymentsCommand cmd);


	public ListSocialSecurityEmployeeStatusResponse listSocialSecurityEmployeeStatus(ListSocialSecurityEmployeeStatusCommand cmd);


	SocialSecurityPaymentDetailDTO getSocialSecurityRule(GetSocialSecurityRuleCommand cmd);

	public GetSocialSecurityPaymentDetailsResponse getSocialSecurityPaymentDetails(GetSocialSecurityPaymentDetailsCommand cmd);


	public void updateSocialSecurityPayment(UpdateSocialSecurityPaymentCommand cmd);


	public void importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd);


	public void calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd);


	public ListSocialSecurityReportsResponse listSocialSecurityReports(ListSocialSecurityReportsCommand cmd);


	public void exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd);


	public ListSocialSecurityDepartmentSummarysResponse listSocialSecurityDepartmentSummarys(ListSocialSecurityDepartmentSummarysCommand cmd);


	public void exportSocialSecurityDepartmentSummarys(ExportSocialSecurityDepartmentSummarysCommand cmd);


	public ListSocialSecurityInoutReportsResponse listSocialSecurityInoutReports(ListSocialSecurityInoutReportsCommand cmd);


	public void exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd);


	public void fileSocialSecurity(FileSocialSecurityCommand cmd);


	public ListSocialSecurityHistoryFilesResponse listSocialSecurityHistoryFiles(ListSocialSecurityHistoryFilesCommand cmd);


	public ListUserInoutHistoryResponse listUserInoutHistory(ListUserInoutHistoryCommand cmd);


	public ListSocialSecurityHouseholdTypesResponse listSocialSecurityHouseholdTypes(ListSocialSecurityHouseholdTypesCommand cmd);


	public ListAccumulationFundHouseholdTypesResponse listAccumulationFundHouseholdTypes(ListAccumulationFundHouseholdTypesCommand cmd);


	public GetSocialSecurityReportStatusResponse getSocialSecurityReportStatus(GetSocialSecurityReportStatusCommand cmd);

}