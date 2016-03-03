package com.everhomes.aclink.admin;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.aclink.AclinkDeleteByIdCommand;
import com.everhomes.aclink.AclinkUserResponse;
import com.everhomes.aclink.AesServerKeyProvider;
import com.everhomes.aclink.AesUserKey;
import com.everhomes.aclink.AesUserKeyDTO;
import com.everhomes.aclink.CreateDoorAuthCommand;
import com.everhomes.aclink.DoorAccess;
import com.everhomes.aclink.DoorAccessAdminUpdateCommand;
import com.everhomes.aclink.DoorAccessProvider;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.aclink.DoorAuthDTO;
import com.everhomes.aclink.ListAclinkUserCommand;
import com.everhomes.aclink.ListAesUserKeyByUserIdCommand;
import com.everhomes.aclink.ListAesUserKeyByUserResponse;
import com.everhomes.aclink.ListDoorAccessByOwnerIdCommand;
import com.everhomes.aclink.ListDoorAccessResponse;
import com.everhomes.aclink.QueryDoorAccessAdminCommand;
import com.everhomes.aclink.QueryDoorMessageResponse;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;
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
     * <b>URL: /admin/aclink/searchDoorAccess</b>
     * <p>获取门禁列表</p>
     * @return 门禁列表
     */
    @RequestMapping("searchDoorAccess")
    @RestReturn(value=ListDoorAccessResponse.class)
    public RestResponse listDoorAccessByOwnerId(@Valid QueryDoorAccessAdminCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.searchDoorAccessByAdmin(cmd));
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
    
    /**
     * 
     * <b>URL: /admin/aclink/createAuth</b>
     * <p>创建授权</p>
     * @return OK 成功
     */
    @RequestMapping("createAuth")
    @RestReturn(value=DoorAuthDTO.class)
    public RestResponse createDoorAuth(@Valid CreateDoorAuthCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.createDoorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }

    /**
     * 
     * <b>URL: /admin/aclink/listAclinkUsers</b>
     * <p>显示用户授权列表</p>
     * @return
     */
    @RequestMapping("listAclinkUsers")
    @RestReturn(value=AclinkUserResponse.class)
    public RestResponse listAclinkUsers(@Valid ListAclinkUserCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listAclinkUsers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /admin/aclink/deleteDoorAccess</b>
     * <p>删除门禁</p>
     * @return
     */
    @RequestMapping("deleteDoorAccess")
    @RestReturn(value=String.class)
    public RestResponse deleteDoorAccess(@Valid AclinkDeleteByIdCommand cmd) {
        doorAccessService.deleteDoorAccess(cmd.getObjId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/deleteDoorAuth</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("deleteDoorAuth")
    @RestReturn(value=String.class)
    public RestResponse deleteDoorAuth(@Valid AclinkDeleteByIdCommand cmd) {
        doorAccessService.deleteDoorAuth(cmd.getObjId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/listUserKey</b>
     * <p>获取用户授权信息</p>
     * @return
     */
    @RequestMapping("listUserKey")
    @RestReturn(value=ListAesUserKeyByUserResponse.class)
    public RestResponse listUserKey(ListAesUserKeyByUserIdCommand cmd) {
        ListAesUserKeyByUserResponse resp = new ListAesUserKeyByUserResponse();
        List<AesUserKey> aesUserKeys = doorAccessService.listAesUserKeyByUserId(cmd.getUserId());
        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(AesUserKey key : aesUserKeys) {
            AesUserKeyDTO dto = ConvertHelper.convert(key, AesUserKeyDTO.class);
            dtos.add(dto);
        }
        
        resp.setAesUserKeys(dtos);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
}
