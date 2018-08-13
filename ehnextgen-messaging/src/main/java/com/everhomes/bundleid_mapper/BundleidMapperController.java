package com.everhomes.bundleid_mapper;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.bundleid_mapper.BundleidMapperCommand;


/**
 * bundleid 映射信息 Controller
 * @author huanglm 20180607
 *
 */
@RestDoc(value="BundleidMapper controller", site="messaging")
@RestController
@RequestMapping("/bundleidMapper")
public class BundleidMapperController extends ControllerBase {
    
    @Autowired
    BundleidMapperService bundleidMapperService;


    /**
     * <b>URL: /bundleidMapper/createBundleidMapper</b>
     * <p>创建开发者账号信息</p>
     * @param cmd
     * @return
     */
    @RequestMapping("createBundleidMapper")
    @RestReturn(value=String.class)
    public RestResponse createBundleidMapper(@Valid BundleidMapperCommand cmd ) {
    	
    	RestResponse response = new RestResponse();
    	              
        	BundleidMapper bo = new BundleidMapper();
            bo.setNamespaceId(cmd.getNamespaceId());
            bo.setIdentify(cmd.getIdentify());
            bo.setBundleId(cmd.getBundleId());
            
            bundleidMapperService.createBundleidMapper(bo);
                
            response.setErrorCode(ErrorCodes.SUCCESS);
            response.setErrorDescription("OK");
           
        return response;
    }
    	
}
