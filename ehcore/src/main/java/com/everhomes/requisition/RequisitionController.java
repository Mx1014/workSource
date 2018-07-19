//@formatter:off
package com.everhomes.requisition;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.requisition.*;
import com.everhomes.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        requisitionService.createRequisition(cmd);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     *
     * @param cmd
     * @return
     */
    @RequestMapping("saveRequisition")
    @RestReturn(value = String.class)
    public RestResponse saveRequisition(PostGeneralFormValCommand cmd){
        //requisitionService.createRequisition(cmd);
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
        ListRequisitionsResponse response = requisitionService.listRequisitions(cmd);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /requisition/listRequisitionsForSecondParty</b>
     * <p>展示请示单列表</p>
     */
    @RequestMapping("listRequisitionsForSecondParty")
    @RestReturn(value = ListRequisitionsResponse.class)
    public RestResponse listRequisitionsForSecondParty(ListRequisitionsCommand cmd){
        ListRequisitionsResponse response = requisitionService.listRequisitionsForSecondParty(cmd);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /requisition/getRequisitionDetail</b>
     * <p>查看请示单</p>
     */
    @RequestMapping("getRequisitionDetail")
    @RestReturn(value = GetRequisitionDetailResponse.class)
    public RestResponse getRequisitionDetail(GetRequisitionDetailCommand cmd){
        GetRequisitionDetailResponse response = requisitionService.getRequisitionDetail(cmd);
        RestResponse restResponse = new RestResponse(response);
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
        List<ListRequisitionTypesDTO> list = requisitionService.listRequisitionTypes(cmd);
        RestResponse restResponse = new RestResponse(list);
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
}
