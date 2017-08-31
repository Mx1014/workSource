package com.everhomes.customer;

import com.everhomes.rest.customer.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/15.
 */
public interface CustomerService {
    void createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd);
    void updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd);
    void deleteEnterpriseCustomer(DeleteEnterpriseCustomerCommand cmd);
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

    EnterpriseCustomerStatisticsDTO listEnterpriseCustomerStatistics(ListEnterpriseCustomerStatisticsCommand cmd);
    CustomerIndustryStatisticsResponse listCustomerIndustryStatistics(ListEnterpriseCustomerStatisticsCommand cmd);
    CustomerIntellectualPropertyStatisticsResponse listCustomerIntellectualPropertyStatistics(ListEnterpriseCustomerStatisticsCommand cmd);
    CustomerTalentStatisticsResponse listCustomerTalentStatistics(ListEnterpriseCustomerStatisticsCommand cmd);
    CustomerProjectStatisticsResponse listCustomerProjectStatistics(ListEnterpriseCustomerStatisticsCommand cmd);
    CustomerSourceStatisticsResponse listCustomerSourceStatistics(ListEnterpriseCustomerStatisticsCommand cmd);

    void syncEnterpriseCustomers(SyncCustomersCommand cmd);
    void syncIndividualCustomers(SyncCustomersCommand cmd);
}
