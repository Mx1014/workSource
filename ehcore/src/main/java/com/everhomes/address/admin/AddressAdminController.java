package com.everhomes.address.admin;

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
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.CorrectAddressCommand;
import com.everhomes.rest.address.admin.CorrectAddressAdminCommand;
import com.everhomes.rest.address.admin.ImportAddressCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

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
    public RestResponse correctAddress(@Valid CorrectAddressAdminCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	addressService.importAddressInfos(files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
//    
//    /**
//     * <b>URL: /admin/address/importParkAddressData</b>
//     * @param files 上传的文件
//     * @return 上传的结果
//     */
//    @RequestMapping(value="importParkAddressData", method = RequestMethod.POST)
//    @RestReturn(value=String.class)
//    public RestResponse importParkAddressData(@Valid ImportAddressCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
//        
//    	addressService.importParkAddressData(cmd, files);
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
    
    /**
     * <b>URL: /admin/address/importParkAddressData</b>
     * @param files 上传的文件
     * @return 上传的结果
     */
    @RequestMapping(value="importParkAddressData", method = RequestMethod.POST)
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse importParkAddressData(@Valid ImportAddressCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
    	RestResponse response = new RestResponse(addressService.importParkAddressData(cmd, files[0]));
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
     * <b>URL: /admin/address/importAddressInfos</b>
     * @param files 上传的文件
     * @return 上传的结果
     */
    @RequestMapping(value="importAddressData", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse importAddressData(@RequestParam(value = "attachment") MultipartFile[] files) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	addressService.importAddressData(files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    

}
