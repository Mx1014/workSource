package com.everhomes.investment;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.customer.ExportEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.investment.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestDoc(value="investment enterprise controller", site="core")
@RestController
@RequestMapping("/invitedCustomer")
public class InvitedCustomerController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitedCustomerController.class);


    //investment_promotion

    @Autowired
    private InvitedCustomerService invitedCustomerService;

    /**
     * <b>URL: /invitedCustomer/createInvestment</b>
     * <p> 新建招商客户 </p>
     */
    @RequestMapping("createInvestment")
    @RestReturn(value=Long.class)
    public RestResponse createInvestment(CreateInvitedCustomerCommand cmd) {
        invitedCustomerService.createInvitedCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /invitedCustomer/updateInvestment</b>
     * <p> 修改招商客户 </p>
     */
    @RequestMapping("updateInvestment")
    @RestReturn(value=Long.class)
    public RestResponse updateInvestment(CreateInvitedCustomerCommand cmd) {
        invitedCustomerService.updateInvestment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /invitedCustomer/listInvestment</b>
     * <p> 列出招商客户 </p>
     */
    @RequestMapping("listInvestment")
    @RestReturn(value=Long.class)
    public RestResponse listInvestment(SearchEnterpriseCustomerCommand cmd) {
        SearchInvestmentResponse searchInvestmentResponse =  invitedCustomerService.listInvestment(cmd);
        RestResponse response = new RestResponse(searchInvestmentResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/deleteInvestment</b>
     * <p> 删除招商客户 </p>
     */
    @RequestMapping("deleteInvestment")
    @RestReturn(value=Long.class)
    public RestResponse deleteInvestment(DeleteInvitedCustomerCommand cmd) {
        RestResponse response = new RestResponse();
        // cmd.getId() cmd.getNameSpaceId()
        invitedCustomerService.deleteInvestment(cmd);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/viewInvitedCustomerDetail</b>
     * <p> 查看招商客户 </p>
     */
    @RequestMapping("viewInvitedCustomerDetail")
    @RestReturn(value=InvitedCustomerDTO.class)
    public RestResponse viewInvitedCustomerDetail(ViewInvestmentDetailCommand cmd) {
        // cmd.getId() cmd.getNameSpaceId()
        InvitedCustomerDTO dto = invitedCustomerService.viewInvestmentDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invitedCustomer/changeInvestmentToCustomer</b>
     * <p> 转换招商客户为已成交客户 </p>
     */
    @RequestMapping("changeInvestmentToCustomer")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse changeInvestmentToCustomer(ChangeInvestmentToCustomerCommand cmd) {
        List<Long> customerIds = invitedCustomerService.changeInvestmentToCustomer(cmd);
        RestResponse response = new RestResponse(customerIds);
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invitedCustomer/changeApplyToInvestment</b>
     * <p> 转换申请客户为招商客户 </p>
     */
    @RequestMapping("changeApplyToInvestment")
    @RestReturn(value=String.class)
    public RestResponse changeApplyToInvestment(ChangeApplyToInvestmentCommand cmd) {

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invitedCustomer/importInvestmentEnterpriseData</b>
     * <p> 导入招商客户 </p>
     */
    @RequestMapping("importInvestmentEnterpriseData")
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse importInvestmentEnterpriseData(ImportFieldExcelCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }

        RestResponse response = new RestResponse(invitedCustomerService.importEnterpriseCustomer(cmd, files[0]));
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invitedCustomer/exportEnterpriseCustomerTemplate</b>
     * <p>导出企业客户excel模板</p>
     */
    @RequestMapping("exportEnterpriseCustomerTemplate")
    public void exportEnterpriseCustomerTemplate(ListFieldGroupCommand cmd, HttpServletResponse response) {
        invitedCustomerService.exportEnterpriseCustomerTemplate(cmd, response);
    }


    /**
     * <b>URL: /invitedCustomer/exportInvitedCustomer</b>
     * <p>导出企业客户excel</p>
     */
    @RequestMapping("exportInvitedCustomer")
    @RestReturn(value = String.class)
    public RestResponse exportInvitedCustomer(@Valid ExportEnterpriseCustomerCommand cmd) {
        invitedCustomerService.exportContractListByContractList(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/giveUpInvitedCustomer</b>
     * <p> 放弃招商客户 </p>
     */
    @RequestMapping("giveUpInvitedCustomer")
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse giveUpInvitedCustomer(@Valid ViewInvestmentDetailCommand cmd) {
        invitedCustomerService.giveUpInvitedCustomer(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes. SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invitedCustomer/changeCustomerAptitude</b>
     * <p>一键转为资质客户</p>
     */
    @RequestMapping("changeCustomerAptitude")
    @RestReturn(value = String.class)
    public RestResponse changeCustomerAptitude(@Valid SearchEnterpriseCustomerCommand cmd) {
        invitedCustomerService.changeCustomerAptitude(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/signCustomerDataToThird</b>
     * <p>传送瑞安客户</p>
     */
    @RequestMapping("signCustomerDataToThird")
    @RestReturn(value = String.class)
    public RestResponse signCustomerDataToThird(@Valid SignCustomerDataToThirdCommand cmd) {
        String url = invitedCustomerService.signCustomerDataToThird(cmd);
        RestResponse response = new RestResponse(url);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    //========================统计相关接口========================
    /**
     *
     * <b>URL: /invitedCustomer/getAllCommunityCustomerStatisticsDaily</b>
     * <p>统计该管理公司每日的客户信息</p>
     */
    @RequestMapping("getAllCommunityCustomerStatisticsDaily")
    @RestReturn(value = String.class)
    public RestResponse getAllCommunityCustomerStatisticsDaily(GetAllCommunityCustomerStatisticsDailyCommand cmd) {
        //String url = invitedCustomerService.signCustomerDataToThird(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/getAllCommunityCustomerStatisticsMonthly</b>
     * <p>统计该管理公司每月的客户信息</p>
     */
    @RequestMapping("getAllCommunityCustomerStatisticsMonthly")
    @RestReturn(value = String.class)
    public RestResponse getAllCommunityCustomerStatisticsMonthly(GetAllCommunityCustomerStatisticsMonthlyCommand cmd) {
        //String url = invitedCustomerService.signCustomerDataToThird(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/getCustomerStatisticsDaily</b>
     * <p>统计该管理公司指定园区下每月的客户数据</p>
     */
    @RequestMapping("getCustomerStatisticsDaily")
    @RestReturn(value = String.class)
    public RestResponse getCustomerStatisticsDaily(GetCustomerStatisticsDailyCommand cmd) {
        //String url = invitedCustomerService.signCustomerDataToThird(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/getCustomerStatisticsMonthly</b>
     * <p>统计该管理公司指定园区下每月的客户数据</p>
     */
    @RequestMapping("getCustomerStatisticsMonthly")
    @RestReturn(value = String.class)
    public RestResponse getCustomerStatisticsMonthly(GetCustomerStatisticsMonthlyCommand cmd) {
        //String url = invitedCustomerService.signCustomerDataToThird(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/getCustomerStatisticsNow</b>
     * <p>统计该管理公司当前的客户数据</p>
     */
    @RequestMapping("getCustomerStatisticsNow")
    @RestReturn(value = String.class)
    public RestResponse getCustomerStatisticsNow(GetCustomerStatisticsNowCommand cmd) {
        //String url = invitedCustomerService.signCustomerDataToThird(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }



    /**
     *
     * <b>URL: /invitedCustomer/initCustomerStatusToDB</b>
     * <p>将现在系统中所有客户的状态初始化进数据库</p>
     */
    @RequestMapping("initCustomerStatusToDB")
    @RestReturn(value = String.class)
    public RestResponse initCustomerStatusToDB() {
        invitedCustomerService.initCustomerStatusToDB();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/testCustomerStatistic</b>
     * <p>进行某天的某种统计</p>
     */
    @RequestMapping("testCustomerStatistic")
    @RestReturn(value = String.class)
    public RestResponse testCustomerStatistic(TestCreateCustomerStatisticCommand cmd) {
        invitedCustomerService.testCustomerStatistic(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticDaily</b>
     * <p>获取某天的数据，分园区获取</p>
     */
    @RequestMapping("queryCustomerStatisticDaily")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticDaily(GetCustomerStatisticsDailyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.getCustomerStatisticsDaily(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticDailyTotal</b>
     * <p>获取某天的数据，分管理公司获取</p>
     */
    @RequestMapping("queryCustomerStatisticDailyTotal")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticDailyTotal(GetCustomerStatisticsDailyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.getCustomerStatisticsDailyTotal(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticMonthly</b>
     * <p>获取某月的数据</p>
     */
    @RequestMapping("queryCustomerStatisticMonthly")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticMonthly(GetCustomerStatisticsMonthlyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.getCustomerStatisticsMonthly(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticMonthlyTotal</b>
     * <p>获取某月的数据，分管理公司获取</p>
     */
    @RequestMapping("queryCustomerStatisticMonthlyTotal")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticMonthlyTotal(GetCustomerStatisticsMonthlyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.getCustomerStatisticsMonthlyTotal(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticMonthlyNow</b>
     * <p>获取某月的数据</p>
     */
    @RequestMapping("queryCustomerStatisticMonthlyNow")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticMonthlyNow(GetCustomerStatisticsMonthlyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.queryCustomerStatisticMonthlyNow(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticMonthlyTotalNow</b>
     * <p>获取某月的数据，分管理公司获取</p>
     */
    @RequestMapping("queryCustomerStatisticMonthlyTotalNow")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticMonthlyTotalNow(GetCustomerStatisticsMonthlyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.queryCustomerStatisticMonthlyTotalNow(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticDailyNow</b>
     * <p>获取某月的数据</p>
     */
    @RequestMapping("queryCustomerStatisticDailyNow")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticDailyNow(GetCustomerStatisticsDailyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.queryCustomerStatisticDailyNow(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     *
     * <b>URL: /invitedCustomer/queryCustomerStatisticDailyTotalNow</b>
     * <p>获取某月的数据，分管理公司获取</p>
     */
    @RequestMapping("queryCustomerStatisticDailyTotalNow")
    @RestReturn(value = GetCustomerStatisticResponse.class)
    public RestResponse queryCustomerStatisticDailyTotalNow(GetCustomerStatisticsDailyCommand cmd) {
        GetCustomerStatisticResponse dto = invitedCustomerService.queryCustomerStatisticDailyTotalNow(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}
