package com.everhomes.incubator;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.incubator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * <b>URL: /incubator/listIncubatorProject</b>
     * <p>获取入孵申请的项目类型</p>
     */
    @RequestMapping("listIncubatorProject")
    @RestReturn(value=ListIncubatorProjectTypeResponse.class, collection = true)
    public RestResponse listIncubatorProject() {

        ListIncubatorProjectTypeResponse list = incubatorService.listIncubatorProject();

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
    public RestResponse findIncubatorApplyById(FindIncubatorApplyCommand cmd){
        IncubatorApplyDTO dto = incubatorService.findIncubatorApplyById(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
