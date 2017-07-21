package com.everhomes.community_approve.admin;

import com.everhomes.community_approve.CommunityApproveService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community_approve.*;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Administrator on 2017/7/18.
 */
@RestDoc(value="Community approve admin controller", site="core")
@RestController
@RequestMapping("/admin/Community_approve")
public class CommunityApproveAdminController extends ControllerBase{

    @Autowired
    private CommunityApproveService communityApproveService;

    /**
     * <b>URL: /admin/Community_approve/createCommunityApprove</b>
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
     * <b>URL: /admin/Community_approve/listCommunityApproves</b>
     * <p> 获取园区审批表单  </p>
     * @return
     */
    @RequestMapping("listCommunityApproves")
    @RestReturn(value= ListCommunityApproveResponse.class)
    public RestResponse listCommunityApproves(@Valid ListCommunityApproveCommand cmd){
        ListCommunityApproveResponse result = communityApproveService.listCommunityApproves(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /admin/Community_approve/updateCommunitityApprove</b>
     * <p> 更新园区审批  </p>
     * @return
     */
    @RequestMapping("updateCommunitityApprove")
    @RestReturn(value=CommunityApproveDTO.class)
    public RestResponse updateCommunitityApprove(@Valid UpdateCommunityApproveCommand cmd){
        CommunityApproveDTO result = communityApproveService.updateCommunityApprove(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/Community_approve/deleteCommunitityApprove</b>
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

    /**
     * <b>URL: /admin/Community_approve/enableCommunitityApproval</b>
         * <p> 启用园区审批 </p>
     * @return
     */
    @RequestMapping("enableCommunitityApproval")
    @RestReturn(value=String.class)
    public RestResponse enableCommunitityApproval(@Valid CommunityApproveIdCommand cmd) {
        communityApproveService.enableCommunityApprove(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /admin/Community_approve/disableCommunitityApproval</b>
     * <p> 不启用园区审批 </p>
     * @return
     */
    @RequestMapping("disableCommunitityApproval")
    @RestReturn(value=String.class)
    public RestResponse disableCommunitityApproval(@Valid CommunityApproveIdCommand cmd) {
        communityApproveService.disableCommunityApprove(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /admin/Community_approve/listCommunityApproveVals</b>
     * <p> 获取申请记录 </p>
     * @return
     */
    @RequestMapping("listCommunityApproveVals")
    @RestReturn(value=String.class)
    public RestResponse listCommunityApproveVals(@Valid ListCommunityApproveValCommand cmd) {
        ListCommunityApproveValResponse result = communityApproveService.listCommunityApproveVals(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /admin/Community_approve/exportApproveVals</b>
     * <p>导出申请记录/p>
     */
    @RequestMapping("exportApproveVals")
    @RestReturn(value=String.class)
    public RestResponse exportApproveVals(@Valid ListCommunityApproveValCommand cmd, HttpServletResponse resp) {
        this.communityApproveService.exportCommunityApproveValWithForm(cmd,resp);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
