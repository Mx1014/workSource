// @formatter:off
package com.everhomes.socialSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocialSecurityServiceImpl implements SocialSecurityService {
 

	@Override
	public void addSocialSecurity(AddSocialSecurityCommand cmd) {
	

	}

	@Override
	public ListSocialSecurityCitysResponse listSocialSecurityCitys(ListSocialSecurityCitysCommand cmd) {
	
		return new ListSocialSecurityCitysResponse();
	}

	@Override
	public ListAccumulationFundCitysResponse listAccumulationFundCitys(ListAccumulationFundCitysCommand cmd) {
	
		return new ListAccumulationFundCitysResponse();
	}

	@Override
	public ListFilterItemsResponse listFilterItems(ListFilterItemsCommand cmd) {
	
		return new ListFilterItemsResponse();
	}

	@Override
	public ListSocialSecurityPaymentsResponse listSocialSecurityPayments(ListSocialSecurityPaymentsCommand cmd) {
	
		return new ListSocialSecurityPaymentsResponse();
	}

	@Override
	public ListSocialSecurityEmployeeStatusResponse listSocialSecurityEmployeeStatus(ListSocialSecurityEmployeeStatusCommand cmd) {
	
		return new ListSocialSecurityEmployeeStatusResponse();
	}

	@Override
	public GetSocialSecurityPaymentDetailsResponse getSocialSecurityPaymentDetails(GetSocialSecurityPaymentDetailsCommand cmd) {
	
		return new GetSocialSecurityPaymentDetailsResponse();
	}

	@Override
	public void updateSocialSecurityPayment(UpdateSocialSecurityPaymentCommand cmd) {
	

	}

	@Override
	public void importSocialSecurityPayments(ImportSocialSecurityPaymentsCommand cmd) {
	

	}

	@Override
	public void calculateSocialSecurityReports(CalculateSocialSecurityReportsCommand cmd) {
	

	}

	@Override
	public ListSocialSecurityReportsResponse listSocialSecurityReports(ListSocialSecurityReportsCommand cmd) {
	
		return new ListSocialSecurityReportsResponse();
	}

	@Override
	public void exportSocialSecurityReports(ExportSocialSecurityReportsCommand cmd) {
	

	}

	@Override
	public ListSocialSecurityDepartmentSummarysResponse listSocialSecurityDepartmentSummarys(ListSocialSecurityDepartmentSummarysCommand cmd) {
	
		return new ListSocialSecurityDepartmentSummarysResponse();
	}

	@Override
	public void exportSocialSecurityDepartmentSummarys(ExportSocialSecurityDepartmentSummarysCommand cmd) {
	

	}

	@Override
	public ListSocialSecurityInoutReportsResponse listSocialSecurityInoutReports(ListSocialSecurityInoutReportsCommand cmd) {
	
		return new ListSocialSecurityInoutReportsResponse();
	}

	@Override
	public void exportSocialSecurityInoutReports(ExportSocialSecurityInoutReportsCommand cmd) {
	

	}

	@Override
	public void fileSocialSecurity(FileSocialSecurityCommand cmd) {
	

	}

	@Override
	public ListSocialSecurityHistoryFilesResponse listSocialSecurityHistoryFiles(ListSocialSecurityHistoryFilesCommand cmd) {
	
		return new ListSocialSecurityHistoryFilesResponse();
	}

}