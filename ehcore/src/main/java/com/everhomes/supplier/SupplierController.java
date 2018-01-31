//@formatter:off
package com.everhomes.supplier;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.supplier.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Wentian Wang on 2018/1/9.
 */

@RestController
@RequestMapping("/supplier")
public class SupplierController extends ControllerBase {
    @Autowired
    private SupplierService supplierService;

    /**
     * <b>URL: /supplier/createOrUpdateOneSupplier</b>
     * <p>创建或者更新一个供应商</p>
     */
    @RequestMapping("createOrUpdateOneSupplier")
    @RestReturn(value = String.class)
    private RestResponse createOrUpdateOneSupplier(CreateOrUpdateOneSupplierCommand cmd){
        supplierService.createOrUpdateOneSupplier(cmd);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/deleteSupplier</b>
     * <p>删除一个供应商</p>
     */
    @RequestMapping("deleteSupplier")
    @RestReturn(value = String.class)
    private RestResponse deleteSupplier(DeleteOneSupplierCommand cmd){
        supplierService.deleteSupplier(cmd);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/listSuppliers</b>
     * <p>展示供应商的列表</p>
     */
    @RequestMapping("listSuppliers")
    @RestReturn(value = ListSuppliersResponse.class)
    private RestResponse listSuppliers(ListSuppliersCommand cmd){
        ListSuppliersResponse response = supplierService.listSuppliers(cmd);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/getSupplierDetail</b>
     * <p>获得一个供应商的信息</p>
     */
    @RequestMapping("getSupplierDetail")
    @RestReturn(value = GetSupplierDetailDTO.class)
    private RestResponse getSupplierDetail(GetSupplierDetailCommand cmd){
        GetSupplierDetailDTO dto = supplierService.getSupplierDetail(cmd);
        RestResponse restResponse = new RestResponse(dto);
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/searchSuppliers</b>
     * <p>模糊搜索供应商</p>
     */
    @RequestMapping("searchSuppliers")
    @RestReturn(value = SearchSuppliersDTO.class,collection = true)
    private RestResponse searchSuppliers(SearchSuppliersCommand cmd){
//        supplierService.searchSuppliers(cmd.getNameKeyword());
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

}
