//@formatter:off
package com.everhomes.pmkexing;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pmkexing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Property manage KeXing bill
 * Created by xq.tian on 2016/12/26.
 */
@RestDoc(value = "Property manage pmkexing bill")
@RequestMapping("/pmkexing")
@RestController
public class PmKeXingBillController extends ControllerBase {

    @Autowired
    private PmKeXingBillService keXingBillService;

    /**
     * <p>获取用户为管理员的公司列表</p>
     * <b>URL: /pmkexing/listOrganizationsByPmAdmin</b>
     */
    @RequestMapping("listOrganizationsByPmAdmin")
    @RestReturn(value = ListOrganizationsByPmAdminDTO.class, collection = true)
    public RestResponse listOrganizationsByPmAdmin() {
        return response(keXingBillService.listOrganizationsByPmAdmin());
    }

    /**
     * <p>获取公司账单列表</p>
     * <b>URL: /pmkexing/listPmKeXingBills</b>
     */
    @RequestMapping("listPmKeXingBills")
    @RestReturn(ListPmKeXingBillsResponse.class)
    public RestResponse listPmKeXingBills(ListPmKeXingBillsCommand cmd) {
        return response(keXingBillService.listPmKeXingBills(cmd));
        // return response(keXingBillService.listPmKeXingBillsByMultiRequest(cmd));
    }

    /**
     * <p>获取公司物业账单统计信息</p>
     * <b>URL: /pmkexing/getPmKeXingBillStat</b>
     */
    @RequestMapping("getPmKeXingBillStat")
    @RestReturn(PmKeXingBillStatDTO.class)
    public RestResponse getPmKeXingBillStat(GetPmKeXingBillStatCommand cmd) {
        return response(keXingBillService.getPmKeXingBillStat(cmd));
    }

    /**
     * <p>获取账单详情</p>
     * <b>URL: /pmkexing/getPmKeXingBill</b>
     */
    @RequestMapping("getPmKeXingBill")
    @RestReturn(PmKeXingBillDTO.class)
    public RestResponse getPmKeXingBill(GetPmKeXingBillCommand cmd) {
        return response(keXingBillService.getPmKeXingBill(cmd));
    }

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}
