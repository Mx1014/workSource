//@formatter:off
package com.everhomes.requisition;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.Requisition.CreateRequisitionCommand;
import com.everhomes.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Wentian Wang on 2018/1/20.
 */
@RestController
@RequestMapping("/requisition")
public class RequisitionController extends ControllerBase {
    @Autowired
    private RequisitionService requisitionService;

    /**
     * <b>URL: </b>
     * <p></p>
     */
    @RequestMapping("")
    @RestReturn(value = String.class)
    public RestResponse xxx(){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /requisition/createRequisition</b>
     * <p>新增一个请示单</p>     not finished yet
     */
    @RequestMapping("createRequisition")
    @RestReturn(value = String.class)
    public RestResponse createRequisition(CreateRequisitionCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
}
