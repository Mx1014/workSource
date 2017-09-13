package com.everhomes.incubator;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.incubator.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestDoc(value = "Incubator Controller", site = "core")
@RestController
@RequestMapping("/incubator")
public class IncubatorController extends ControllerBase {


	/**
     * <b>URL: /incubator/listIncubator</b>
     * <p>查询入孵申请的记录</p>
     */
    @RequestMapping("listIncubator")
    @RestReturn(value=IncubatorDTO.class, collection = true)
    public RestResponse listIncubator(ListIncubatorCommand cmd) {
    	
    	List<IncubatorDTO> dtos = null;
        
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/listIncubatorProject</b>
     * <p>获取入孵申请的项目类型</p>
     */
    @RequestMapping("listIncubatorProject")
    @RestReturn(value=IncubatorProjectTypeDTO.class, collection = true)
    public RestResponse listIncubatorProject() {

        List<IncubatorProjectTypeDTO> dtos = null;

        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /incubator/addIncubator</b>
     * <p>入孵申请</p>
     */
    @RequestMapping("addIncubator")
    @RestReturn(value=IncubatorDTO.class)
    public RestResponse addIncubator(AddIncubatorCommand cmd) {

        IncubatorDTO dto = null;
        
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /incubator/approveIncubator</b>
     * <p>审批入孵申请</p>
     */
    @RequestMapping("approveIncubator")
    @RestReturn(value=String.class)
    public RestResponse approveIncubator(ApproveIncubatorCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
