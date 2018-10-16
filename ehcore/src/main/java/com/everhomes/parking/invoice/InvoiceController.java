// @formatter:off
package com.everhomes.parking.invoice;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.invoice.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
/**
 * 对接发票的，上下文为invoice
 */
@RestDoc(value="invoice controller", site="parking invoice")
@RestController
@RequestMapping("/invoice")
public class InvoiceController extends ControllerBase {
    
    @Autowired
    private InvoiceService invoiceService;

    /**
     * <b>URL: /invoice/listNotInvoicedOrders</b>
     * <p>查询指定人员未开发票列表</p>
     */
    @RequestMapping("listNotInvoicedOrders")
    @RestReturn(value=ListNotInvoicedOrdersResponse.class)
    public RestResponse listNotInvoicedOrders(ListNotInvoicedOrdersCommand cmd) {

        ListNotInvoicedOrdersResponse notInvoicedOrdersResponse = invoiceService.listNotInvoicedOrders(cmd);
        RestResponse response = new RestResponse(notInvoicedOrdersResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /invoice/notifyOrderInvoiced</b>
     * <p>通知发票开票成功</p>
     */
    @RequestMapping("notifyOrderInvoiced")
    @RestReturn(value=String.class)
    public RestResponse notifyOrderInvoiced(NotifyOrderInvoicedCommand cmd) {

    	invoiceService.notifyOrderInvoiced(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invoice/getPayeeIdByOrderNo</b>
     * <p>根据业务编号查找收款方id</p>
     */
    @RequestMapping("getPayeeIdByOrderNo")
    @RestReturn(value=GetPayeeIdByOrderNoResponse.class)
    public RestResponse getPayeeIdByOrderNo(GetPayeeIdByOrderNoCommand cmd) {

        GetPayeeIdByOrderNoResponse baseResponse = invoiceService.getPayeeIdByOrderNo(cmd);
        RestResponse response = new RestResponse(baseResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /invoice/listAllParkingLots</b>
     * <p>查询所有的停车场列表</p>
     */
    @RequestMapping("listAllParkingLots")
    @RestReturn(value=ParkingLotDTO.class, collection=true)
    public RestResponse listAllParkingLots(ListAllParkingLotsCommand cmd) {

        List<ParkingLotDTO> parkingLotList = invoiceService.listAllParkingLots(cmd);
        RestResponse response = new RestResponse(parkingLotList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /invoice/parkingRechargeOrdersByOrderNo</b>
     * <p>查询指定订单发票</p>
     */
    @RequestMapping("parkingRechargeOrdersByOrderNo")
    @RestReturn(value=ParkingRechargeOrderDTO.class)
    public RestResponse parkingRechargeOrdersByOrderNo(ParkingRechargeOrdersByOrderNoCommand cmd) {

    	ParkingRechargeOrderDTO parkingRechargeOrder = invoiceService.parkingRechargeOrdersByOrderNo(cmd.getOrderNo());
        RestResponse response = new RestResponse(parkingRechargeOrder);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
}
