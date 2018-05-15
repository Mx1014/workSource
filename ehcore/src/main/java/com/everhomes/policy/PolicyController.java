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

import java.util.List;

@RestDoc(value="Policy controller", site="policy")
@RestController
@RequestMapping("/policy")
public class PolicyController extends ControllerBase {

    @Autowired
    private PolicyService policyService;
    @Autowired
    private PolicyRecordService policyRecordService;

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
    public RestResponse deletePolicy(DeletePolicyCommand cmd) {
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
    @RestReturn(value=PolicyDTO.class, collection=true)
    public RestResponse listPoliciesByTitle(listPoliciesCommand cmd) {
        List<PolicyDTO> dtos = policyService.listPoliciesByTitle(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/searchPolicies</b>
     * <p>根据关键字获取政策</p>
     */
    @RequestMapping("searchPolicies")
    @RestReturn(value=PolicyDTO.class, collection=true)
    public RestResponse searchPolicies(GetPolicyCommand cmd) {
        List<PolicyDTO> dtos = policyService.searchPolicies(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /policy/searchPolicyRecords</b>
     * <p>查询政策计算器记录</p>
     */
    @RequestMapping("searchPolicyRecords")
    @RestReturn(value=PolicyDTO.class, collection=true)
    public RestResponse searchPolicyRecords(GetPolicyRecordCommand cmd) {
        List<PolicyRecordDTO> dtos = policyRecordService.searchPolicyRecords(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
