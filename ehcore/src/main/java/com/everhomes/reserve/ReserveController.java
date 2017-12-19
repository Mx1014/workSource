// @formatter:off
package com.everhomes.reserve;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.parking.ParkingService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.reserve.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestDoc(value="Reserve controller", site="reserve")
@RestController
@RequestMapping("/reserve")
public class ReserveController extends ControllerBase {


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
     * <b>URL: /reserve/getReserveOrderStrategy</b>
     * <p>获取预订订单策略</p>
     */
    @RequestMapping("getReserveOrderStrategy")
    @RestReturn(value=ReserveOrderStrategyDTO.class)
    public RestResponse getReserveOrderStrategy(GetReserveOrderStrategyCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/setReserveOrderStrategy</b>
     * <p>设置预订订单策略</p>
     */
    @RequestMapping("setReserveOrderStrategy")
    @RestReturn(value=ReserveOrderStrategyDTO.class)
    public RestResponse setReserveOrderStrategy(SetReserveOrderStrategyCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/getReserveDiscountStrategy</b>
     * <p>获取内部优惠策略</p>
     */
    @RequestMapping("getReserveDiscountStrategy")
    @RestReturn(value=ReserveDiscountStrategyDTO.class, collection = true)
    public RestResponse getReserveDiscountStrategy(GetReserveDiscountStrategyCommand cmd) {
        //TODO:
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /reserve/setReserveDiscountStrategy</b>
     * <p>设置内部优惠策略</p>
     */
    @RequestMapping("setReserveDiscountStrategy")
    @RestReturn(value=String.class)
    public RestResponse setReserveDiscountStrategy(SetReserveDiscountStrategyCommand cmd) {
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
