//@formatter:off
package com.everhomes.requisition;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.Requisition.*;
import com.everhomes.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Wentian Wang on 2018/1/20.
 */
@RestController
@RequestMapping("/requisition")
public class RequisitionController extends ControllerBase {
    @Autowired
    private RequisitionService requisitionService;

    /**
     * <b>URL: /requisition/createRequisition</b>
     * <p>新增一个请示单</p>
     */
    @RequestMapping("createRequisition")
    @RestReturn(value = String.class)
    public RestResponse createRequisition(CreateRequisitionCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /requisition/listRequisitions</b>
     * <p>展示请示单列表</p>
     */
    @RequestMapping("listRequisitions")
    @RestReturn(value = ListRequisitionsResponse.class)
    public RestResponse listRequisitions(ListRequisitionsCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /requisition/showRequisitions</b>
     * <p>查看请示单</p>
     */
    @RequestMapping("showRequisitions")
    @RestReturn(value = ShowRequisitionsResponse.class)
    public RestResponse showRequisitions(ShowRequisitionsCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /requisition/listRequisitionTypes</b>
     * <p>获取请示类型</p>
     */
    @RequestMapping("listRequisitionTypes")
    @RestReturn(value = ListRequisitionTypesDTO.class,collection = true)
    public RestResponse listRequisitionTypes(ListRequisitionTypesCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
}
