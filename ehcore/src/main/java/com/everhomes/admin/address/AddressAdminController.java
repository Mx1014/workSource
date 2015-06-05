package com.everhomes.admin.address;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.address.AddressService;
import com.everhomes.address.CorrectAddressCommand;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Address admin controller", site="core")
@RestController
@RequestMapping("/admin/address")
public class AddressAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressAdminController.class);
    
    @Autowired
    private AddressService addressService;
    @Autowired
    private CommunityProvider communityProvider;

    /**
     * <b>URL: /admin/address/correctAddress</b>
     * <p>修正地址</p>
     */
    @RequestMapping("correctAddress")
    @RestReturn(value=String.class)
    public RestResponse correctAddress(@Valid CorrectAddressCommand cmd) {
        this.addressService.correctAddress(cmd);
        RestResponse response = new RestResponse(null);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /admin/address/importCommunityInfos</b>
     * @param files 上传的文件
     * @return 上传的结果
     */
    @RequestMapping(value="importCommunityInfos", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse importCommunityInfos(@RequestParam(value = "attachment") MultipartFile[] files) {
    	addressService.importCommunityInfos(files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/address/importAddressInfos</b>
     * @param files 上传的文件
     * @return 上传的结果
     */
    @RequestMapping(value="importAddressInfos", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse importAddressInfos(@RequestParam(value = "attachment") MultipartFile[] files) {
    	addressService.importAddressInfos(files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
