//@formatter:off
package com.everhomes.supplier;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.supplier.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private SupplierServiceImpl supplierService;

    /**
     * <b>URL: /supplier/createOrUpdateOneSupplier</b>
     * <p>创建或者更新一个供应商</p>
     */
    @RequestMapping(value = "createOrUpdateOneSupplier",method = RequestMethod.POST)
    @RestReturn(value = String.class)
    private RestResponse createOrUpdateOneSupplier(CreateOrUpdateOneSupplierCommand cmd, @RequestParam(value = "attachment") MultipartFile file){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/deleteOneSupplier</b>
     * <p>删除一个供应商</p>
     */
    @RequestMapping(value = "deleteOneSupplier",method = RequestMethod.POST)
    @RestReturn(value = String.class)
    private RestResponse deleteOneSupplier(DeleteOneSupplierCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/listSuppliers</b>
     * <p>展示供应商的列表</p>
     */
    @RequestMapping(value = "listSuppliers")
    @RestReturn(value = ListSuppliersResponse.class)
    private RestResponse listSuppliers(ListSuppliersCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/findSupplierDetail</b>
     * <p>获得一个供应商的信息</p>
     */
    @RequestMapping(value = "findSupplierDetail")
    @RestReturn(value = FindSupplierDetailDTO.class)
    private RestResponse findSupplierDetail(FindSupplierDetailCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /supplier/searchSuppliers</b>
     * <p>模糊搜索供应商</p>
     */
    @RequestMapping(value = "searchSuppliers")
    @RestReturn(value = SearchSuppliersDTO.class,collection = true)
    private RestResponse searchSuppliers(SearchSuppliersCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

}
