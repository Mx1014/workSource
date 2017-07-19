package com.everhomes.community_approve;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community_approve.*;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Administrator on 2017/7/18.
 */
@RestDoc(value="General approval admin controller", site="core")
@RestController
@RequestMapping("/Community_approve")
public class CommunityApproveController  extends ControllerBase{

    /**
     * <b>URL: /Community_approve/createCommunityApprove</b>
     * <p> 创建园区审批  </p>
     * @return
     */
    @RequestMapping("createCommunityApprove")
    @RestReturn(value=CommunityApproveDTO.class)
    public RestResponse createApprovalForm(@Valid CreateCommunityApproveCommand cmd) {

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /Community_approve/listCommunityApproves</b>
     * <p> 获取园区审批表单  </p>
     * @return
     */
    @RequestMapping("listCommunityApproves")
    @RestReturn(value= ListCommunityApproveResponse.class)
    public RestResponse listCommunityApproves(@Valid ListCommunityApproveCommand cmd){

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /Community_approve/updateCommunitityApprove</b>
     * <p> 更新园区审批  </p>
     * @return
     */
    @RequestMapping("updateCommunitityApprove")
    @RestReturn(value=CommunityApproveDTO.class)
    public RestResponse updateCommunitityApprove(@Valid UpdateCommunityApproveCommand cmd){

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /Community_approve/deleteCommunitityApprove</b>
     * <p> 删除园区审批  </p>
     * @return
     */
    @RequestMapping("deleteCommunitityApprove")
    @RestReturn(value=String.class)
    public RestResponse deleteCommunitityApprove(@Valid CommunityApproveIdCommand cmd){

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

}
