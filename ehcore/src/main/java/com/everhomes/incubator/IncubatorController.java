package com.everhomes.incubator;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.incubator.*;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestDoc(value = "Incubator Controller", site = "core")
@RestController
@RequestMapping("/incubator")
public class IncubatorController extends ControllerBase {

    @Autowired
    IncubatorService incubatorService;

	/**
     * <b>URL: /incubator/listIncubatorApply</b>
     * <p>查询入孵申请的记录</p>
     */
    @RequestMapping("listIncubatorApply")
    @RestReturn(value=ListIncubatorApplyResponse.class, collection = true)
    public RestResponse listIncubatorApply(ListIncubatorApplyCommand cmd) {

        ListIncubatorApplyResponse list = incubatorService.listIncubatorApply(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/listMyTeams</b>
     * <p>查询入孵申请的记录</p>
     */
    @RequestMapping("listMyTeams")
    @RestReturn(value=ListIncubatorApplyResponse.class, collection = true)
    public RestResponse listMyTeams() {

        List<IncubatorApplyDTO> dtos = incubatorService.listMyTeams();

        //页面兼容，使用ListIncubatorApplyResponse返回
        ListIncubatorApplyResponse res = new ListIncubatorApplyResponse();
        res.setDtos(dtos);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/exportIncubatorApply</b>
     * <p>导出入孵申请的记录</p>
     */
    @RequestMapping("exportIncubatorApply")
    @RestReturn(value=String.class)
    public RestResponse exportIncubatorApply(ExportIncubatorApplyCommand cmd) {
        incubatorService.exportIncubatorApply(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/listIncubatorProjectType</b>
     * <p>获取入孵申请的项目类型</p>
     */
    @RequestMapping("listIncubatorProjectType")
    @RestReturn(value=ListIncubatorProjectTypeResponse.class, collection = true)
    public RestResponse listIncubatorProjectType() {

        ListIncubatorProjectTypeResponse list = incubatorService.listIncubatorProjectType();

        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/addIncubatorApply</b>
     * <p>入孵申请</p>
     */
    @RequestMapping("addIncubatorApply")
    @RestReturn(value=IncubatorApplyDTO.class)
    public RestResponse addIncubatorApply(AddIncubatorApplyCommand cmd) {

        IncubatorApplyDTO dto = incubatorService.addIncubatorApply(cmd);
        
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/updateIncubatorApply</b>
     * <p>更新入孵申请</p>
     */
    @RequestMapping("updateIncubatorApply")
    @RestReturn(value=IncubatorApplyDTO.class)
    public RestResponse updateIncubatorApply(UpdateIncubatorApplyCommand cmd) {

        IncubatorApplyDTO dto = incubatorService.updateIncubatorApply(cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/cancelIncubatorApply</b>
     * <p>入孵申请</p>
     */
    @RequestMapping("cancelIncubatorApply")
    @RestReturn(value=String.class)
    public RestResponse cancelIncubatorApply(CancelIncubatorApplyCommand cmd) {

        incubatorService.cancelIncubatorApply(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /incubator/approveIncubatorApply</b>
     * <p>审批入孵申请</p>
     */
    @RequestMapping("approveIncubatorApply")
    @RestReturn(value=String.class)
    public RestResponse approveIncubatorApply(ApproveIncubatorApplyCommand cmd) {

        incubatorService.approveIncubatorApply(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/findIncubatorApply</b>
     * <p>获取申请详情</p>
     */
    @RequestMapping("findIncubatorApply")
    @RestReturn(value=IncubatorApplyDTO.class)
    public RestResponse findIncubatorApply(FindIncubatorApplyCommand cmd){
        IncubatorApplyDTO dto = incubatorService.findIncubatorApply(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/findIncubatorAppling</b>
     * <p>获取申请中的一条记录</p>
     */
    @RequestMapping("findIncubatorAppling")
    @RestReturn(value=IncubatorApplyDTO.class)
    public RestResponse findIncubatorAppling(FindIncubatorApplingCommand cmd){
        IncubatorApplyDTO dto = incubatorService.findIncubatorAppling(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
