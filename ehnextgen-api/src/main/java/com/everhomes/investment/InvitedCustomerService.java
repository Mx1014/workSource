package com.everhomes.investment;

import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.rest.customer.ExportEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
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

    //========================统计相关代码========================
    List<CustomerStatisticsDTO> getAllCommunityCustomerStatisticsDaily(GetAllCommunityCustomerStatisticsDailyCommand cmd);

    List<CustomerStatisticsDTO> getAllCommunityCustomerStatisticsMonthly(GetAllCommunityCustomerStatisticsMonthlyCommand cmd);

    GetCustomerStatisticResponse getCustomerStatisticsDaily(GetCustomerStatisticsDailyCommand cmd);

    GetCustomerStatisticResponse getCustomerStatisticsDailyTotal(GetCustomerStatisticsDailyCommand cmd);

    GetCustomerStatisticResponse getCustomerStatisticsMonthly(GetCustomerStatisticsMonthlyCommand cmd);

    GetCustomerStatisticResponse getCustomerStatisticsMonthlyTotal(GetCustomerStatisticsMonthlyCommand cmd);

    GetCustomerStatisticResponse getCustomerStatisticsNow(GetCustomerStatisticsNowCommand cmd);

    void initCustomerStatusToDB();

    void recordCustomerLevelChange(Long oldLevelItemId, Long newLevelItemId, Integer namespaceId, Long communityId, Long customerId, Timestamp changeDate);

    void changeCustomerLevel(EnterpriseCustomer customer, Long levelItemId);

    void changeCustomerLevelByCustomerId(Long customerId, Long levelItemId);

    List<StatisticDataDTO> startCustomerStatisticTotal(StatisticTime time);

    List<StatisticDataDTO> startCustomerStatistic(StatisticTime time);

    StatisticTime getBeforeForStatistic(Date date, int type);

    void statisticCustomerAll(Date date);

    void statisticCustomerDailyTotal(Date date);

    void statisticCustomerDaily(Date date);

    void statisticCustomerMonthlyTotal(Date date);

    void statisticCustomerMonthly(Date date);

    void testCustomerStatistic(TestCreateCustomerStatisticCommand cmd);

    java.sql.Date getDateByTimestamp(Timestamp time);

}
