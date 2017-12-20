// @formatter:off
package com.everhomes.reserve;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.reserve.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestDoc(value="Reserve controller", site="reserve")
@RestController
@RequestMapping("/reserve")
public class ReserveController extends ControllerBase {

    /**
     * <b>URL: /reserve/listReserveResource</b>
     * <p>获取资源列表</p>
     */
    @RequestMapping("listReserveResource")
    @RestReturn(value=ReserveResourceDTO.class, collection = true)
    public RestResponse listReserveResource(ListReserveResourceCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/getReserveOpenDayAndPrice</b>
     * <p>获取预订开放时间和价格</p>
     */
    @RequestMapping("getReserveOpenDayAndPrice")
    @RestReturn(value=ReserveOpenDayAndPriceDTO.class)
    public RestResponse getReserveOpenDayAndPrice(GetReserveOpenDayAndPriceCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/setReserveOpenDayAndPrice</b>
     * <p>设置预订开放时间和价格</p>
     */
    @RequestMapping("setReserveOpenDayAndPrice")
    @RestReturn(value=ReserveOpenDayAndPriceDTO.class)
    public RestResponse setReserveOpenDayAndPrice(SetReserveOpenDayAndPriceCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/getReserveOrderHandleRule</b>
     * <p>获取预订订单退款/超时收费规则</p>
     */
    @RequestMapping("getReserveOrderHandleRule")
    @RestReturn(value=ReserveOrderHandleRuleDTO.class)
    public RestResponse getReserveOrderHandleRule(GetReserveOrderHandleRuleCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/setReserveOrderHandleRule</b>
     * <p>设置预订订单退款/超时收费规则</p>
     */
    @RequestMapping("setReserveOrderHandleRule")
    @RestReturn(value=ReserveOrderHandleRuleDTO.class)
    public RestResponse setReserveOrderHandleRule(SetReserveOrderHandleRuleCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/getReserveDiscountRule</b>
     * <p>获取内部优惠规则</p>
     */
    @RequestMapping("getReserveDiscountRule")
    @RestReturn(value=ReserveDiscountRuleDTO.class, collection = true)
    public RestResponse getReserveDiscountRule(GetReserveDiscountRuleCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/setReserveDiscountRule</b>
     * <p>设置内部优惠规则</p>
     */
    @RequestMapping("setReserveDiscountRule")
    @RestReturn(value=String.class)
    public RestResponse setReserveDiscountRule(SetReserveDiscountRuleCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/searchReserveOrders</b>
     * <p>搜索预订订单</p>
     */
    @RequestMapping("searchReserveOrders")
    @RestReturn(value=SearchReserveOrdersResponse.class)
    public RestResponse searchReserveOrders(SearchReserveOrdersCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/exportReserveOrders</b>
     * <p>导出预订订单</p>
     */
    @RequestMapping("exportReserveOrders")
    public void exportReserveOrders(SearchReserveOrdersCommand cmd, HttpServletResponse response) {
        //TODO:

    }

    /**
     * <b>URL: /reserve/getReserveOrderById</b>
     * <p>根据id查询预订订单</p>
     */
    @RequestMapping("getReserveOrderById")
    @RestReturn(value=ReserveOrderDTO.class)
    public RestResponse getReserveOrderById(GetReserveOrderByIdCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/getReserveOrderDetailById</b>
     * <p>根据id查询预订订单</p>
     */
    @RequestMapping("getReserveOrderDetailById")
    @RestReturn(value=ReserveOrderDTO.class)
    public RestResponse getReserveOrderDetailById(GetReserveOrderByIdCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/addReserveOrder</b>
     * <p>新增预订订单</p>
     */
    @RequestMapping("addReserveOrder")
    @RestReturn(value=ReserveOrderDTO.class)
    public RestResponse addReserveOrder(AddReserveOrderCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/getReserveResourceCells</b>
     * <p>获取资源预订表格</p>
     */
    @RequestMapping("getReserveResourceCells")
    @RestReturn(value=ReserveResourceCellsResponse.class)
    public RestResponse getReserveResourceCells(GetReserveResourceCellsCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/cancelReserveOrder</b>
     * <p>取消预订订单</p>
     */
    @RequestMapping("cancelReserveOrder")
    @RestReturn(value=ReserveOrderDTO.class)
    public RestResponse cancelReserveOrder(CancelReserveOrderCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/payForReserveOrder</b>
     * <p>付款预订订单</p>
     */
    @RequestMapping("payForReserveOrder")
    @RestReturn(value=ReserveOrderDTO.class)
    public RestResponse payForReserveOrder(PayForReserveOrderCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/completeReserveOrder</b>
     * <p>完成订单</p>
     */
    @RequestMapping("completeReserveOrder")
    @RestReturn(value=ReserveOrderDTO.class)
    public RestResponse completeReserveOrder(CompleteReserveOrderCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/delayReserveOrder</b>
     * <p>延长订单</p>
     */
    @RequestMapping("delayReserveOrder")
    @RestReturn(value=ReserveOrderDTO.class)
    public RestResponse delayReserveOrder(DelayReserveOrderCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
