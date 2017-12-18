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
import com.everhomes.rest.reserve.GetReserveOpenDayAndPriceCommand;
import com.everhomes.rest.reserve.ReserveOpenDayAndPriceDTO;
import com.everhomes.rest.reserve.SetReserveOpenDayAndPriceCommand;
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

}
