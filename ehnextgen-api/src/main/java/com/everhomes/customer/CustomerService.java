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
    ImportFileTaskDTO importEnterpriseCustomerData(ImportEnterpriseCustomerDataCommand cmd, MultipartFile mfile, Long userId);
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

//    void createCustomerTalent(CreateCustomerTalentCommand cmd);
//    void updateCustomerTalent(UpdateCustomerTalentCommand cmd);
//    void deleteCustomerTalent(DeleteCustomerTalentCommand cmd);
//    CustomerTalentDTO getCustomerTalent(GetCustomerTalentCommand cmd);
//    List<CustomerTalentDTO> listCustomerTalents(ListCustomerTalentsCommand cmd);
}
