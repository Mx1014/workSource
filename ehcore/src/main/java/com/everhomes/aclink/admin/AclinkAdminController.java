package com.everhomes.aclink.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.aclink.AesServerKeyProvider;
import com.everhomes.aclink.CreateDoorAuthCommand;
import com.everhomes.aclink.DoorAccess;
import com.everhomes.aclink.DoorAccessAdminUpdateCommand;
import com.everhomes.aclink.DoorAccessProvider;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.aclink.DoorAuthDTO;
import com.everhomes.aclink.ListDoorAccessByOwnerIdCommand;
import com.everhomes.aclink.ListDoorAccessResponse;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RuntimeErrorException;

@RestDoc(value="Aclink Admin controller", site="core")
@RestController
@RequestMapping("/admin/aclink")
public class AclinkAdminController extends ControllerBase {
    @Autowired
    private DoorAccessService doorAccessService;
    
    @Autowired
    private AesServerKeyProvider aesServerKeyProvider;
    
    @Autowired
    DoorAccessProvider doorAccessProvider;
    
    /**
     * <b>URL: /admin/aclink/listDoorAccessByOwnerId</b>
     * <p>获取门禁列表</p>
     * @return 门禁列表
     */
    @RequestMapping("listDoorAccessByOwnerId")
    @RestReturn(value=ListDoorAccessResponse.class)
    public RestResponse listDoorAccessByOwnerId(@Valid ListDoorAccessByOwnerIdCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorAccessByOwnerId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/updateDoorAccess</b>
     * <p>更新门禁信息</p>
     * @return OK 成功
     */
    @RequestMapping("updateDoorAccess")
    @RestReturn(value=String.class)
    public RestResponse updateDoorAccess(@Valid DoorAccessAdminUpdateCommand cmd) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "DoorAccess Id not found. ");
        }
        
        if(cmd.getName() != null) {
            doorAccess.setName(cmd.getName());    
        }
        if(cmd.getAddress() != null) {
            doorAccess.setAddress(cmd.getAddress());
        }
        if(cmd.getDescription() != null) {
            doorAccess.setDescription(cmd.getDescription());
        }
        
        doorAccess.setLatitude(cmd.getLatitude());
        doorAccess.setLongitude(cmd.getLongitude());
        doorAccessProvider.updateDoorAccess(doorAccess);
                
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("updateDoorAccess")
    @RestReturn(value=DoorAuthDTO.class)
    public RestResponse createDoorAuth(@Valid CreateDoorAuthCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.createDoorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
}
