package com.everhomes.customer;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.activity.ListSignupInfoByOrganizationIdResponse;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.energy.ListCommnutyRelatedMembersCommand;
import com.everhomes.rest.enterprise.DeleteEnterpriseCommand;
import com.everhomes.rest.enterprise.UpdateSuperAdminCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.rentalv2.ListRentalBillsCommandResponse;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
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

    void exportContractListByContractList(ExportEnterpriseCustomerCommand cmd);

    OutputStream exportEnterpriseCustomer(ExportEnterpriseCustomerCommand cmd);

    OutputStream exportEnterpriseCustomerWihtOutPrivilege(ExportEnterpriseCustomerCommand cmd);

    void changeCustomerAptitude(SearchEnterpriseCustomerCommand cmd);

    Boolean checkCustomerAdmin(Long ownerId, String ownerType, Integer namespaceId);

    EnterpriseCustomerDTO createEnterpriseCustomerOutAuth(CreateEnterpriseCustomerCommand cmd);

    ListSignupInfoByOrganizationIdResponse listCustomerApartmentActivity(ListCustomerApartmentActivityCommand cmd);

    void updateSuperAdmin(createSuperAdminCommand cmd);

    void transNewAdmin(TransNewAdminCommand cmd);

}
