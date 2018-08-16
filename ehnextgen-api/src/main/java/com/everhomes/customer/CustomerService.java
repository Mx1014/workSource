package com.everhomes.customer;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.customer.AllotEnterpriseCustomerCommand;
import com.everhomes.rest.customer.CreateCustomerAccountCommand;
import com.everhomes.rest.customer.CreateCustomerApplyProjectCommand;
import com.everhomes.rest.customer.CreateCustomerCertificateCommand;
import com.everhomes.rest.customer.CreateCustomerCommercialCommand;
import com.everhomes.rest.customer.CreateCustomerDepartureInfoCommand;
import com.everhomes.rest.customer.CreateCustomerEconomicIndicatorCommand;
import com.everhomes.rest.customer.CreateCustomerEntryInfoCommand;
import com.everhomes.rest.customer.CreateCustomerInvestmentCommand;
import com.everhomes.rest.customer.CreateCustomerPatentCommand;
import com.everhomes.rest.customer.CreateCustomerTalentCommand;
import com.everhomes.rest.customer.CreateCustomerTaxCommand;
import com.everhomes.rest.customer.CreateCustomerTrackingCommand;
import com.everhomes.rest.customer.CreateCustomerTrackingPlanCommand;
import com.everhomes.rest.customer.CreateCustomerTrademarkCommand;
import com.everhomes.rest.customer.CreateEnterpriseCustomerCommand;
import com.everhomes.rest.customer.CustomerAccountDTO;
import com.everhomes.rest.customer.CustomerApplyProjectDTO;
import com.everhomes.rest.customer.CustomerCertificateDTO;
import com.everhomes.rest.customer.CustomerCommercialDTO;
import com.everhomes.rest.customer.CustomerConfigurationCommand;
import com.everhomes.rest.customer.CustomerConfigurationDTO;
import com.everhomes.rest.customer.CustomerDepartureInfoDTO;
import com.everhomes.rest.customer.CustomerEconomicIndicatorDTO;
import com.everhomes.rest.customer.CustomerEntryInfoDTO;
import com.everhomes.rest.customer.CustomerEventDTO;
import com.everhomes.rest.customer.CustomerExpandItemDTO;
import com.everhomes.rest.customer.CustomerIndustryStatisticsResponse;
import com.everhomes.rest.customer.CustomerIntellectualPropertyStatisticsResponse;
import com.everhomes.rest.customer.CustomerInvestmentDTO;
import com.everhomes.rest.customer.CustomerPatentDTO;
import com.everhomes.rest.customer.CustomerPotentialResponse;
import com.everhomes.rest.customer.CustomerProjectStatisticsResponse;
import com.everhomes.rest.customer.CustomerSourceStatisticsResponse;
import com.everhomes.rest.customer.CustomerTalentDTO;
import com.everhomes.rest.customer.CustomerTalentStatisticsResponse;
import com.everhomes.rest.customer.CustomerTaxDTO;
import com.everhomes.rest.customer.CustomerTrackingDTO;
import com.everhomes.rest.customer.CustomerTrackingPlanDTO;
import com.everhomes.rest.customer.CustomerTrademarkDTO;
import com.everhomes.rest.customer.DeleteCustomerAccountCommand;
import com.everhomes.rest.customer.DeleteCustomerApplyProjectCommand;
import com.everhomes.rest.customer.DeleteCustomerCertificateCommand;
import com.everhomes.rest.customer.DeleteCustomerCommercialCommand;
import com.everhomes.rest.customer.DeleteCustomerDepartureInfoCommand;
import com.everhomes.rest.customer.DeleteCustomerEconomicIndicatorCommand;
import com.everhomes.rest.customer.DeleteCustomerEntryInfoCommand;
import com.everhomes.rest.customer.DeleteCustomerInvestmentCommand;
import com.everhomes.rest.customer.DeleteCustomerPatentCommand;
import com.everhomes.rest.customer.DeleteCustomerTalentCommand;
import com.everhomes.rest.customer.DeleteCustomerTaxCommand;
import com.everhomes.rest.customer.DeleteCustomerTrackingCommand;
import com.everhomes.rest.customer.DeleteCustomerTrackingPlanCommand;
import com.everhomes.rest.customer.DeleteCustomerTrademarkCommand;
import com.everhomes.rest.customer.DeleteEnterpriseCustomerCommand;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.EnterpriseCustomerStatisticsDTO;
import com.everhomes.rest.customer.ExportEnterpriseCustomerCommand;
import com.everhomes.rest.customer.GetCustomerAccountCommand;
import com.everhomes.rest.customer.GetCustomerApplyProjectCommand;
import com.everhomes.rest.customer.GetCustomerCertificateCommand;
import com.everhomes.rest.customer.GetCustomerCommercialCommand;
import com.everhomes.rest.customer.GetCustomerDepartureInfoCommand;
import com.everhomes.rest.customer.GetCustomerEconomicIndicatorCommand;
import com.everhomes.rest.customer.GetCustomerEntryInfoCommand;
import com.everhomes.rest.customer.GetCustomerInvestmentCommand;
import com.everhomes.rest.customer.GetCustomerPatentCommand;
import com.everhomes.rest.customer.GetCustomerTalentCommand;
import com.everhomes.rest.customer.GetCustomerTaxCommand;
import com.everhomes.rest.customer.GetCustomerTrackingCommand;
import com.everhomes.rest.customer.GetCustomerTrackingPlanCommand;
import com.everhomes.rest.customer.GetCustomerTrademarkCommand;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;
import com.everhomes.rest.customer.GiveUpEnterpriseCustomerCommand;
import com.everhomes.rest.customer.ImportEnterpriseCustomerDataCommand;
import com.everhomes.rest.customer.ListCommunitySyncResultCommand;
import com.everhomes.rest.customer.ListCommunitySyncResultResponse;
import com.everhomes.rest.customer.ListCustomerAccountsCommand;
import com.everhomes.rest.customer.ListCustomerAnnualDetailsCommand;
import com.everhomes.rest.customer.ListCustomerAnnualDetailsResponse;
import com.everhomes.rest.customer.ListCustomerAnnualStatisticsCommand;
import com.everhomes.rest.customer.ListCustomerAnnualStatisticsResponse;
import com.everhomes.rest.customer.ListCustomerApplyProjectsCommand;
import com.everhomes.rest.customer.ListCustomerCertificatesCommand;
import com.everhomes.rest.customer.ListCustomerCommercialsCommand;
import com.everhomes.rest.customer.ListCustomerDepartureInfosCommand;
import com.everhomes.rest.customer.ListCustomerEconomicIndicatorsCommand;
import com.everhomes.rest.customer.ListCustomerEntryInfosCommand;
import com.everhomes.rest.customer.ListCustomerEventsCommand;
import com.everhomes.rest.customer.ListCustomerInvestmentsCommand;
import com.everhomes.rest.customer.ListCustomerPatentsCommand;
import com.everhomes.rest.customer.ListCustomerRentalBillsCommand;
import com.everhomes.rest.customer.ListCustomerSeviceAllianceAppRecordsCommand;
import com.everhomes.rest.customer.ListCustomerTalentsCommand;
import com.everhomes.rest.customer.ListCustomerTaxesCommand;
import com.everhomes.rest.customer.ListCustomerTrackingPlansByDateCommand;
import com.everhomes.rest.customer.ListCustomerTrackingPlansCommand;
import com.everhomes.rest.customer.ListCustomerTrackingsCommand;
import com.everhomes.rest.customer.ListCustomerTrademarksCommand;
import com.everhomes.rest.customer.ListEnterpriseCustomerStatisticsCommand;
import com.everhomes.rest.customer.ListNearbyEnterpriseCustomersCommand;
import com.everhomes.rest.customer.ListNearbyEnterpriseCustomersCommandResponse;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerResponse;
import com.everhomes.rest.customer.SyncCustomerDataCommand;
import com.everhomes.rest.customer.SyncCustomersCommand;
import com.everhomes.rest.customer.SyncResultViewedCommand;
import com.everhomes.rest.customer.UpdateCustomerAccountCommand;
import com.everhomes.rest.customer.UpdateCustomerApplyProjectCommand;
import com.everhomes.rest.customer.UpdateCustomerCertificateCommand;
import com.everhomes.rest.customer.UpdateCustomerCommercialCommand;
import com.everhomes.rest.customer.UpdateCustomerDepartureInfoCommand;
import com.everhomes.rest.customer.UpdateCustomerEconomicIndicatorCommand;
import com.everhomes.rest.customer.UpdateCustomerEntryInfoCommand;
import com.everhomes.rest.customer.UpdateCustomerInvestmentCommand;
import com.everhomes.rest.customer.UpdateCustomerPatentCommand;
import com.everhomes.rest.customer.UpdateCustomerTalentCommand;
import com.everhomes.rest.customer.UpdateCustomerTaxCommand;
import com.everhomes.rest.customer.UpdateCustomerTrackingCommand;
import com.everhomes.rest.customer.UpdateCustomerTrackingPlanCommand;
import com.everhomes.rest.customer.UpdateCustomerTrademarkCommand;
import com.everhomes.rest.customer.UpdateEnterpriseCustomerCommand;
import com.everhomes.rest.energy.ListCommnutyRelatedMembersCommand;
import com.everhomes.rest.enterprise.DeleteEnterpriseCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.rentalv2.ListRentalBillsCommandResponse;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/15.
 */
public interface CustomerService {
    EnterpriseCustomerDTO createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd);

    EnterpriseCustomerDTO updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd);

    void deleteEnterpriseCustomer(DeleteEnterpriseCustomerCommand cmd, Boolean checkAuth);

    SearchEnterpriseCustomerResponse searchEnterpriseCustomer(SearchEnterpriseCustomerCommand cmd);

    ImportFileTaskDTO importEnterpriseCustomer(ImportEnterpriseCustomerDataCommand cmd, MultipartFile mfile, Long userId);

    EnterpriseCustomerDTO getEnterpriseCustomer(GetEnterpriseCustomerCommand cmd);

    void createCustomerTalent(CreateCustomerTalentCommand cmd);

    void updateCustomerTalent(UpdateCustomerTalentCommand cmd);

    void deleteCustomerTalent(DeleteCustomerTalentCommand cmd);

    CustomerTalentDTO getCustomerTalent(GetCustomerTalentCommand cmd);

    List<CustomerTalentDTO> listCustomerTalents(ListCustomerTalentsCommand cmd);

    void createCustomerTrademark(CreateCustomerTrademarkCommand cmd);

    void updateCustomerTrademark(UpdateCustomerTrademarkCommand cmd);

    void deleteCustomerTrademark(DeleteCustomerTrademarkCommand cmd);

    CustomerTrademarkDTO getCustomerTrademark(GetCustomerTrademarkCommand cmd);

    List<CustomerTrademarkDTO> listCustomerTrademarks(ListCustomerTrademarksCommand cmd);

    void createCustomerPatent(CreateCustomerPatentCommand cmd);

    void updateCustomerPatent(UpdateCustomerPatentCommand cmd);

    void deleteCustomerPatent(DeleteCustomerPatentCommand cmd);

    CustomerPatentDTO getCustomerPatent(GetCustomerPatentCommand cmd);

    List<CustomerPatentDTO> listCustomerPatents(ListCustomerPatentsCommand cmd);

    void createCustomerCertificate(CreateCustomerCertificateCommand cmd);

    void updateCustomerCertificate(UpdateCustomerCertificateCommand cmd);

    void deleteCustomerCertificate(DeleteCustomerCertificateCommand cmd);

    CustomerCertificateDTO getCustomerCertificate(GetCustomerCertificateCommand cmd);

    List<CustomerCertificateDTO> listCustomerCertificates(ListCustomerCertificatesCommand cmd);

    void createCustomerApplyProject(CreateCustomerApplyProjectCommand cmd);

    void updateCustomerApplyProject(UpdateCustomerApplyProjectCommand cmd);

    void deleteCustomerApplyProject(DeleteCustomerApplyProjectCommand cmd);

    CustomerApplyProjectDTO getCustomerApplyProject(GetCustomerApplyProjectCommand cmd);

    List<CustomerApplyProjectDTO> listCustomerApplyProjects(ListCustomerApplyProjectsCommand cmd);

    void createCustomerCommercial(CreateCustomerCommercialCommand cmd);

    void updateCustomerCommercial(UpdateCustomerCommercialCommand cmd);

    void deleteCustomerCommercial(DeleteCustomerCommercialCommand cmd);

    CustomerCommercialDTO getCustomerCommercial(GetCustomerCommercialCommand cmd);

    List<CustomerCommercialDTO> listCustomerCommercials(ListCustomerCommercialsCommand cmd);

    void createCustomerInvestment(CreateCustomerInvestmentCommand cmd);

    void updateCustomerInvestment(UpdateCustomerInvestmentCommand cmd);

    void deleteCustomerInvestment(DeleteCustomerInvestmentCommand cmd);

    CustomerInvestmentDTO getCustomerInvestment(GetCustomerInvestmentCommand cmd);

    List<CustomerInvestmentDTO> listCustomerInvestments(ListCustomerInvestmentsCommand cmd);


    void createCustomerEconomicIndicator(CreateCustomerEconomicIndicatorCommand cmd);

    void updateCustomerEconomicIndicator(UpdateCustomerEconomicIndicatorCommand cmd);

    void deleteCustomerEconomicIndicator(DeleteCustomerEconomicIndicatorCommand cmd);

    CustomerEconomicIndicatorDTO getCustomerEconomicIndicator(GetCustomerEconomicIndicatorCommand cmd);

    List<CustomerEconomicIndicatorDTO> listCustomerEconomicIndicators(ListCustomerEconomicIndicatorsCommand cmd);

    void createCustomerEntryInfo(CreateCustomerEntryInfoCommand cmd);

    void updateCustomerEntryInfo(UpdateCustomerEntryInfoCommand cmd);

    void deleteCustomerEntryInfo(DeleteCustomerEntryInfoCommand cmd);

    CustomerEntryInfoDTO getCustomerEntryInfo(GetCustomerEntryInfoCommand cmd);

    List<CustomerEntryInfoDTO> listCustomerEntryInfos(ListCustomerEntryInfosCommand cmd);

    List<CustomerEntryInfoDTO> listCustomerEntryInfosWithoutAuth(ListCustomerEntryInfosCommand cmd);

    void createCustomerDepartureInfo(CreateCustomerDepartureInfoCommand cmd);

    void updateCustomerDepartureInfo(UpdateCustomerDepartureInfoCommand cmd);

    void deleteCustomerDepartureInfo(DeleteCustomerDepartureInfoCommand cmd);

    CustomerDepartureInfoDTO getCustomerDepartureInfo(GetCustomerDepartureInfoCommand cmd);

    List<CustomerDepartureInfoDTO> listCustomerDepartureInfos(ListCustomerDepartureInfosCommand cmd);

    EnterpriseCustomerStatisticsDTO listEnterpriseCustomerStatistics(ListEnterpriseCustomerStatisticsCommand cmd);

    CustomerIndustryStatisticsResponse listCustomerIndustryStatistics(ListEnterpriseCustomerStatisticsCommand cmd);

    CustomerIntellectualPropertyStatisticsResponse listCustomerIntellectualPropertyStatistics(ListEnterpriseCustomerStatisticsCommand cmd);

    CustomerTalentStatisticsResponse listCustomerTalentStatistics(ListEnterpriseCustomerStatisticsCommand cmd);

    CustomerProjectStatisticsResponse listCustomerProjectStatistics(ListEnterpriseCustomerStatisticsCommand cmd);

    CustomerSourceStatisticsResponse listCustomerSourceStatistics(ListEnterpriseCustomerStatisticsCommand cmd);

    ListCustomerAnnualStatisticsResponse listCustomerAnnualStatistics(ListCustomerAnnualStatisticsCommand cmd);

    ListCustomerAnnualDetailsResponse listCustomerAnnualDetails(ListCustomerAnnualDetailsCommand cmd);

    String syncEnterpriseCustomers(SyncCustomersCommand cmd, Boolean authFlag);

    String syncIndividualCustomers(SyncCustomersCommand cmd);


    List<CustomerTrackingDTO> listCustomerTrackings(ListCustomerTrackingsCommand cmd);

    CustomerTrackingDTO getCustomerTracking(GetCustomerTrackingCommand cmd);

    CustomerTrackingDTO updateCustomerTracking(UpdateCustomerTrackingCommand cmd);

    void deleteCustomerTracking(DeleteCustomerTrackingCommand cmd);

    void createCustomerTracking(CreateCustomerTrackingCommand cmd);

    List<CustomerTrackingPlanDTO> listCustomerTrackingPlans(ListCustomerTrackingPlansCommand cmd);

    CustomerTrackingPlanDTO getCustomerTrackingPlan(GetCustomerTrackingPlanCommand cmd);

    CustomerTrackingPlanDTO updateCustomerTrackingPlan(UpdateCustomerTrackingPlanCommand cmd);

    void deleteCustomerTrackingPlan(DeleteCustomerTrackingPlanCommand cmd);

    void createCustomerTrackingPlan(CreateCustomerTrackingPlanCommand cmd);

    List<CustomerEventDTO> listCustomerEvents(ListCustomerEventsCommand cmd);

    void allotEnterpriseCustomer(AllotEnterpriseCustomerCommand cmd);

    void giveUpEnterpriseCustomer(GiveUpEnterpriseCustomerCommand cmd);

    ListNearbyEnterpriseCustomersCommandResponse listNearbyEnterpriseCustomers(ListNearbyEnterpriseCustomersCommand cmd);

    void trackingPlanWarningSchedule();

    void processTrackingPlanNotify(CustomerTrackingPlan plan);

    List<List<CustomerTrackingPlanDTO>> listCustomerTrackingPlansByDate(ListCustomerTrackingPlansByDateCommand cmd);

    void createCustomerTax(CreateCustomerTaxCommand cmd);

    void updateCustomerTax(UpdateCustomerTaxCommand cmd);

    void deleteCustomerTax(DeleteCustomerTaxCommand cmd);

    CustomerTaxDTO getCustomerTax(GetCustomerTaxCommand cmd);

    List<CustomerTaxDTO> listCustomerTaxes(ListCustomerTaxesCommand cmd);

    void createCustomerAccount(CreateCustomerAccountCommand cmd);

    void updateCustomerAccount(UpdateCustomerAccountCommand cmd);

    void deleteCustomerAccount(DeleteCustomerAccountCommand cmd);

    CustomerAccountDTO getCustomerAccount(GetCustomerAccountCommand cmd);

    List<CustomerAccountDTO> listCustomerAccounts(ListCustomerAccountsCommand cmd);

    void checkCustomerAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId);

    SearchEnterpriseCustomerResponse queryEnterpriseCustomers(SearchEnterpriseCustomerCommand cmd);

    ListCommunitySyncResultResponse listCommunitySyncResult(ListCommunitySyncResultCommand cmd);

    void exportEnterpriseCustomer(ExportEnterpriseCustomerCommand cmd, HttpServletResponse response);

    void exportEnterpriseCustomerTemplate(ListFieldGroupCommand cmd, HttpServletResponse response);

    void saveCustomerEvent(int i, EnterpriseCustomer customer, EnterpriseCustomer exist,Byte deviceType);

    void updateOrganizationAddress(Long orgId, Long buildingId, Long addressId);

    OrganizationDTO createOrganization(EnterpriseCustomer customer);

    void createCustomerTalentFromOrgMember(Long orgId, OrganizationMember member);
    List<OrganizationMemberDTO> listCommunityUserRelatedTrackUsers(ListCommunitySyncResultCommand cmd);

    SearchRequestInfoResponse listCustomerSeviceAllianceAppRecords(ListCustomerSeviceAllianceAppRecordsCommand cmd);
    List<OrganizationMemberDTO> listCommunityRelatedMembers(ListCommnutyRelatedMembersCommand cmd);

    ListRentalBillsCommandResponse listCustomerRentalBills(ListCustomerRentalBillsCommand cmd);

    String syncResultViewed(SyncResultViewedCommand cmd);
    Byte checkCustomerCurrentUserAdmin(ListCommnutyRelatedMembersCommand cmd);

    void createOrganizationAdmin(CreateOrganizationAdminCommand cmd);

    void deleteOrganizationAdmin(DeleteOrganizationAdminCommand cmd);

    List<OrganizationContactDTO> listOrganizationAdmin(ListServiceModuleAdministratorsCommand cmd);

    void syncOrganizationToCustomer(SyncCustomerDataCommand cmd);

    HttpServletResponse exportCustomerDetails(ListEnterpriseCustomerStatisticsCommand cmd, HttpServletResponse httpResponse);

    EnterpriseCustomerDTO getCustomerBasicInfoByOrgId(GetEnterpriseCustomerCommand cmd);

    void deletePotentialCustomer(DeleteEnterpriseCommand cmd);

    CustomerPotentialResponse listPotentialCustomers(DeleteEnterpriseCommand cmd);

    void setSyncPotentialCustomer(CustomerConfigurationCommand cmd);

    List<CustomerConfigurationDTO> listSyncPotentialCustomer(CustomerConfigurationCommand cmd);

    List<CustomerExpandItemDTO> listExpandItems(CustomerConfigurationCommand cmd);

    List<CustomerTalentDTO> listPotentialTalent(DeleteEnterpriseCommand cmd);

    void updatePotentialCustomer(DeleteEnterpriseCommand cmd);

    SearchEnterpriseCustomerResponse listSyncErrorCustomer(SearchEnterpriseCustomerCommand cmd);
}
