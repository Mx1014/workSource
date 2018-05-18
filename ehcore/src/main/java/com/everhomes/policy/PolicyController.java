package com.everhomes.policy;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.policy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestDoc(value="Policy controller", site="policy")
@RestController
@RequestMapping("/policy")
public class PolicyController extends ControllerBase {

    @Autowired
    private PolicyService policyService;
    @Autowired
    private PolicyRecordService policyRecordService;
    @Autowired
    private PolicyAgentRuleService policyAgentRuleService;

    /**
     * <b>URL: /policy/createPolicy</b>
     * <p>创建政策</p>
     */
    @RequestMapping("createPolicy")
    @RestReturn(value=PolicyDTO.class)
    public RestResponse createPolicy(CreatePolicyCommand cmd) {
        PolicyDTO dto = policyService.createPolicy(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/deletePolicy</b>
     * <p>删除政策</p>
     */
    @RequestMapping("deletePolicy")
    @RestReturn(value=String.class)
    public RestResponse deletePolicy(GetPolicyByIdCommand cmd) {
        policyService.deletePolicy(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/updatePolicy</b>
     * <p>修改政策</p>
     */
    @RequestMapping("updatePolicy")
    @RestReturn(value=PolicyDTO.class)
    public RestResponse updatePolicy(UpdatePolicyCommand cmd) {
        PolicyDTO dto = policyService.updatePolicy(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/listPolicies</b>
     * <p>根据标题获取政策</p>
     */
    @RequestMapping("listPolicies")
    @RestReturn(value=PolicyResponse.class)
    public RestResponse listPoliciesByTitle(listPoliciesCommand cmd) {
        RestResponse response = new RestResponse();
        response.setResponseObject(policyService.listPoliciesByTitle(cmd));
        return response;
    }

    /**
     * <b>URL: /policy/searchPolicyById</b>
     * <p>根据id获取政策</p>
     */
    @RequestMapping("searchPolicyById")
    @RestReturn(value=PolicyDTO.class)
    public RestResponse searchPolicyById(GetPolicyByIdCommand cmd) {
        PolicyDTO dto = policyService.searchPolicyById(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/searchPolicies</b>
     * <p>根据关键字获取政策</p>
     */
    @RequestMapping("searchPolicies")
    @RestReturn(value=PolicyResponse.class)
    public RestResponse searchPolicies(GetPolicyCommand cmd) {
        RestResponse response = new RestResponse();
        response.setResponseObject(policyService.searchPolicies(cmd));
        return response;
    }

    /**
     * <b>URL: /policy/searchPolicyRecords</b>
     * <p>查询政策计算器记录</p>
     */
    @RequestMapping("searchPolicyRecords")
    @RestReturn(value=PolicyRecordResponse.class)
    public RestResponse searchPolicyRecords(GetPolicyRecordCommand cmd) {
        RestResponse response = new RestResponse();
        response.setResponseObject(policyRecordService.searchPolicyRecords(cmd));
        return response;
    }

    /**
     * <b>URL: /policy/searchPolicyRecordsById</b>
     * <p>根据Id查询政策计算器记录</p>
     */
    @RequestMapping("searchPolicyRecordsById")
    @RestReturn(value=PolicyDTO.class)
    public RestResponse searchPolicyRecordsById(GetPolicyByIdCommand cmd) {
        PolicyRecordDTO dto = policyRecordService.searchPolicyRecordById(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/exportPolicyRecords</b>
     * <p>导出查询记录列表</p>
     */
    @RequestMapping("exportPolicyRecords")
    public void exportPolicyRecords(GetPolicyRecordCommand cmd, HttpServletResponse resp) {
        policyRecordService.exportPolicyRecords(cmd, resp);
    }

    /**
     * <b>URL: /policy/admin/setPolicyAgent</b>
     * <p>设置政策代办</p>
     */
    @RequestMapping("admin/setPolicyAgent")
    @RestReturn(value=PolicyAgentRuleDTO.class)
    public RestResponse setPolicyAgent(SetPolicyAgentRuleCommand cmd) {
        PolicyAgentRuleDTO dto = policyAgentRuleService.setPolicyAgentRule(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/admin/searchPolicyAgentRule</b>
     * <p>查询政策代办</p>
     */
    @RequestMapping("admin/searchPolicyAgentRule")
    @RestReturn(value=PolicyAgentRuleDTO.class)
    public RestResponse searchPolicyAgentRule(GetPolicyAgentRuleCommand cmd) {
        PolicyAgentRuleDTO dto = policyAgentRuleService.searchPolicyAgentRuleById(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
