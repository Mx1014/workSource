package com.everhomes.investment;

import com.everhomes.rest.customer.ExportEnterpriseCustomerCommand;
import com.everhomes.rest.customer.ImportEnterpriseCustomerDataCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface InvitedCustomerService {


    InvitedCustomerDTO createInvitedCustomer(CreateInvitedCustomerCommand cmd);

    void updateInvestment(CreateInvitedCustomerCommand cmd);

    void deleteInvestment(DeleteInvitedCustomerCommand cmd);

    SearchInvestmentResponse listInvestment(SearchEnterpriseCustomerCommand cmd);

    InvitedCustomerDTO viewInvestmentDetail(ViewInvestmentDetailCommand cmd);

    CustomerRequirementDTO getCustomerRequirementDTOByCustomerId(Long customerId);

    void giveUpInvitedCustomer(ViewInvestmentDetailCommand cmd) ;

    void syncTrackerData();

    void exportEnterpriseCustomerTemplate(ListFieldGroupCommand cmd, HttpServletResponse response);

    List<Long> changeInvestmentToCustomer(ChangeInvestmentToCustomerCommand cmd);
    
    InvitedCustomerDTO createInvitedCustomerWithoutAuth(CreateInvitedCustomerCommand cmd);

    DynamicImportResponse importEnterpriseCustomer(ImportFieldExcelCommand cmd, MultipartFile mfile);

    void exportContractListByContractList(ExportEnterpriseCustomerCommand cmd);

    void changeCustomerAptitude(SearchEnterpriseCustomerCommand cmd);

    String signCustomerDataToThird(SignCustomerDataToThirdCommand cmd);

}
