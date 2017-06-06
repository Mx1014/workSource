package com.everhomes.videoconf;

import java.util.List;
import java.util.Set;

import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.videoconf.AddSourceVideoConfAccountCommand;
import com.everhomes.rest.videoconf.AssignVideoConfAccountCommand;
import com.everhomes.rest.videoconf.CancelVideoConfCommand;
import com.everhomes.rest.videoconf.CheckVideoConfTrialAccountCommand;
import com.everhomes.rest.videoconf.CheckVideoConfTrialAccountResponse;
import com.everhomes.rest.videoconf.ConfAccountOrderDTO;
import com.everhomes.rest.videoconf.CreateAccountOwnerCommand;
import com.everhomes.rest.videoconf.CreateConfAccountOrderCommand;
import com.everhomes.rest.videoconf.CreateConfAccountOrderOnlineCommand;
import com.everhomes.rest.videoconf.CreateInvoiceCommand;
import com.everhomes.rest.videoconf.CreateVideoConfInvitationCommand;
import com.everhomes.rest.videoconf.DeleteConfEnterpriseCommand;
import com.everhomes.rest.videoconf.DeleteReservationConfCommand;
import com.everhomes.rest.videoconf.DeleteSourceVideoConfAccountCommand;
import com.everhomes.rest.videoconf.DeleteVideoConfAccountCommand;
import com.everhomes.rest.videoconf.DeleteWarningContactorCommand;
import com.everhomes.rest.videoconf.EnterpriseLockStatusCommand;
import com.everhomes.rest.videoconf.GetVideoConfHelpUrlResponse;
import com.everhomes.rest.videoconf.GetVideoConfTrialAccountCommand;
import com.everhomes.rest.videoconf.UpdateConfAccountPeriodCommand;
import com.everhomes.rest.videoconf.ExtendedSourceAccountPeriodCommand;
import com.everhomes.rest.videoconf.ExtendedVideoConfAccountPeriodCommand;
import com.everhomes.rest.videoconf.GetNamespaceIdListCommand;
import com.everhomes.rest.videoconf.GetNamespaceListResponse;
import com.everhomes.rest.videoconf.InvoiceDTO;
import com.everhomes.rest.videoconf.JoinVideoConfCommand;
import com.everhomes.rest.videoconf.JoinVideoConfResponse;
import com.everhomes.rest.videoconf.ListConfCategoryCommand;
import com.everhomes.rest.videoconf.ListConfCategoryResponse;
import com.everhomes.rest.videoconf.ListConfOrderAccountResponse;
import com.everhomes.rest.videoconf.ListEnterpriseVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListEnterpriseVideoConfAccountResponse;
import com.everhomes.rest.videoconf.ListEnterpriseWithVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListEnterpriseWithVideoConfAccountResponse;
import com.everhomes.rest.videoconf.ListInvoiceByOrderIdCommand;
import com.everhomes.rest.videoconf.ListOrderByAccountCommand;
import com.everhomes.rest.videoconf.ListOrderByAccountResponse;
import com.everhomes.rest.videoconf.ListReservationConfCommand;
import com.everhomes.rest.videoconf.ListReservationConfResponse;
import com.everhomes.rest.videoconf.ListConfAccountSaleRuleCommand;
import com.everhomes.rest.videoconf.ListSourceVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListSourceVideoConfAccountResponse;
import com.everhomes.rest.videoconf.ListUnassignAccountsByOrderCommand;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeCommand;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeResponse;
import com.everhomes.rest.videoconf.ListVideoConfAccountByOrderIdCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountConfRecordCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountConfRecordResponse;
import com.everhomes.rest.videoconf.ListVideoConfAccountOrderCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountOrderResponse;
import com.everhomes.rest.videoconf.ListVideoConfAccountRuleResponse;
import com.everhomes.rest.videoconf.ListWarningContactorResponse;
import com.everhomes.rest.videoconf.OfflinePayBillCommand;
import com.everhomes.rest.videoconf.ReserveVideoConfCommand;
import com.everhomes.rest.videoconf.SetWarningContactorCommand;
import com.everhomes.rest.videoconf.SourceVideoConfAccountStatistics;
import com.everhomes.rest.videoconf.StartVideoConfCommand;
import com.everhomes.rest.videoconf.StartVideoConfResponse;
import com.everhomes.rest.videoconf.UnassignAccountResponse;
import com.everhomes.rest.videoconf.UpdateAccountOrderCommand;
import com.everhomes.rest.videoconf.UpdateConfAccountCategoriesCommand;
import com.everhomes.rest.videoconf.UpdateContactorCommand;
import com.everhomes.rest.videoconf.UpdateInvoiceCommand;
import com.everhomes.rest.videoconf.UpdateVideoConfAccountCommand;
import com.everhomes.rest.videoconf.UserAccountDTO;
import com.everhomes.rest.videoconf.VerifyPurchaseAuthorityCommand;
import com.everhomes.rest.videoconf.VerifyPurchaseAuthorityResponse;
import com.everhomes.rest.videoconf.VerifyVideoConfAccountCommand;
import com.everhomes.rest.videoconf.VideoConfInvitationResponse;


public interface VideoConfService {

	List<String> getConfCapacity();
	List<String> getAccountType();
	List<String> getConfType();
	
	VideoConfInvitationResponse createVideoConfInvitation(CreateVideoConfInvitationCommand cmd);
	void createInvoice(CreateInvoiceCommand cmd);
	
	void updateConfAccountCategories(UpdateConfAccountCategoriesCommand cmd);
	ListVideoConfAccountRuleResponse listConfAccountCategories(ListConfAccountSaleRuleCommand cmd);
	
	void setWarningContactor(SetWarningContactorCommand cmd);
	void deleteWarningContactor(DeleteWarningContactorCommand cmd);
	ListWarningContactorResponse listWarningContactor(ListConfAccountSaleRuleCommand cmd);
	
	void addSourceVideoConfAccount(AddSourceVideoConfAccountCommand cmd);
	void extendedSourceAccountPeriod(ExtendedSourceAccountPeriodCommand cmd);
	ListSourceVideoConfAccountResponse listSourceVideoConfAccount(ListSourceVideoConfAccountCommand cmd);
	
//	ListEnterpriseWithVideoConfAccountResponse listEnterpriseWithVideoConfAccount(ListEnterpriseWithVideoConfAccountCommand cmd);

	void updateContactor(UpdateContactorCommand cmd);
	void setEnterpriseLockStatus(EnterpriseLockStatusCommand cmd);
//	ListVideoConfAccountResponse listVideoConfAccount(ListEnterpriseWithVideoConfAccountCommand cmd);
	ListVideoConfAccountConfRecordResponse listVideoConfAccountConfRecord(ListVideoConfAccountConfRecordCommand cmd);
//	
	void updateVideoConfAccount(UpdateVideoConfAccountCommand cmd);
	void deleteVideoConfAccount(DeleteVideoConfAccountCommand cmd);
	void extendedVideoConfAccountPeriod(ExtendedVideoConfAccountPeriodCommand cmd);
	ListVideoConfAccountOrderResponse listConfOrder(ListVideoConfAccountOrderCommand cmd);
	ListOrderByAccountResponse listOrderByAccount(ListOrderByAccountCommand cmd);
//	
//	ConfOrderDTO listVideoConfAccountOrderInfoByOrderId(ListVideoConfAccountOrderInfoByOrderIdCommand cmd);
	ListConfOrderAccountResponse listVideoConfAccountByOrderId(ListVideoConfAccountByOrderIdCommand cmd);
//	
	InvoiceDTO listInvoiceByOrderId(ListInvoiceByOrderIdCommand cmd);
	void updateVideoConfAccountOrderInfo(UpdateAccountOrderCommand cmd);
	Long createConfAccountOrder(CreateConfAccountOrderCommand cmd);
//	
//	List<VideoConfAccountStatisticsDTO> getVideoConfAccountStatistics(GetVideoConfAccountStatisticsCommand cmd);
	List<SourceVideoConfAccountStatistics> getSourceVideoConfAccountStatistics();
//	ListEnterpriseVideoConfAccountResponse listVideoConfAccountByEnterpriseId(ListEnterpriseVideoConfAccountCommand cmd);
	void assignVideoConfAccount(AssignVideoConfAccountCommand cmd);
	void createAccountOwner(CreateAccountOwnerCommand cmd);
//	String applyVideoConfAccount(ApplyVideoConfAccountCommand cmd);
//	ListUsersWithoutVideoConfPrivilegeResponse listUsersWithoutVideoConfPrivilege(ListUsersWithoutVideoConfPrivilegeCommand cmd);
	
	UserAccountDTO verifyVideoConfAccount(VerifyVideoConfAccountCommand cmd);
	
	void cancelVideoConf(CancelVideoConfCommand cmd);
	
	StartVideoConfResponse startVideoConf(StartVideoConfCommand cmd);
	JoinVideoConfResponse joinVideoConf(JoinVideoConfCommand cmd);
	
	void reserveVideoConf(ReserveVideoConfCommand cmd);
	void deleteReservationConf(DeleteReservationConfCommand cmd);
	
	ListReservationConfResponse listReservationConf(ListReservationConfCommand cmd);
	
	Set<Long> listOrdersWithUnassignAccount(ListUsersWithoutVideoConfPrivilegeCommand cmd);
	UnassignAccountResponse listUnassignAccountsByOrder(ListUnassignAccountsByOrderCommand cmd);
	
	void confPaymentCallBack(OnlinePayBillCommand cmd);
	void offlinePayBill(OfflinePayBillCommand cmd);
	
	Double getEarlyWarningLine(Byte warningLineType);
	
	void invalidAccount();
	void invalidConf();
	
	List<GetNamespaceListResponse> getRegisterNamespaceIdList(GetNamespaceIdListCommand cmd);
	List<GetNamespaceListResponse> getConferenceNamespaceIdList(GetNamespaceIdListCommand cmd);

	void deleteSourceVideoConfAccount(DeleteSourceVideoConfAccountCommand cmd);
	
	InvoiceDTO updateInvoice(UpdateInvoiceCommand cmd);
	ConfAccountOrderDTO updateConfAccountPeriod(UpdateConfAccountPeriodCommand cmd);
	ListConfCategoryResponse listConfCategory(ListConfCategoryCommand cmd);
	ConfAccountOrderDTO createConfAccountOrderOnline(CreateConfAccountOrderOnlineCommand cmd);
	
	VerifyPurchaseAuthorityResponse verifyPurchaseAuthority(VerifyPurchaseAuthorityCommand cmd);
	
	void deleteConfEnterprise(DeleteConfEnterpriseCommand cmd);
	CheckVideoConfTrialAccountResponse checkVideoConfTrialAccount(
			CheckVideoConfTrialAccountCommand cmd);
	void getVideoTrialConfAccount(GetVideoConfTrialAccountCommand cmd);
	void testSendPhoneMsg(String phoneNum, int templateId, int namespaceId);
	GetVideoConfHelpUrlResponse getVideoConfHelpUrl();
}
