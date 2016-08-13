package com.everhomes.aclink.admin;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.aclink.AesServerKeyProvider;
import com.everhomes.aclink.AesUserKey;
import com.everhomes.aclink.DoorAccess;
import com.everhomes.aclink.DoorAccessProvider;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.aclink.DoorAuthMethodType;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.AclinkCreateDoorAuthListCommand;
import com.everhomes.rest.aclink.AclinkDeleteByIdCommand;
import com.everhomes.rest.aclink.AclinkFirmwareDTO;
import com.everhomes.rest.aclink.AclinkUserResponse;
import com.everhomes.rest.aclink.AesUserKeyDTO;
import com.everhomes.rest.aclink.CreateAclinkFirmwareCommand;
import com.everhomes.rest.aclink.CreateDoorAccessGroup;
import com.everhomes.rest.aclink.CreateDoorAccessLingLing;
import com.everhomes.rest.aclink.CreateDoorAuthCommand;
import com.everhomes.rest.aclink.CreateDoorVisitorCommand;
import com.everhomes.rest.aclink.CreateLinglingVisitorCommand;
import com.everhomes.rest.aclink.DeleteDoorAccessById;
import com.everhomes.rest.aclink.DoorAccessAdminUpdateCommand;
import com.everhomes.rest.aclink.DoorAccessCapapilityDTO;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.aclink.GetCurrentFirmwareCommand;
import com.everhomes.rest.aclink.GetDoorAccessCapapilityCommand;
import com.everhomes.rest.aclink.GetShortMessageCommand;
import com.everhomes.rest.aclink.GetShortMessageResponse;
import com.everhomes.rest.aclink.ListAclinkUserCommand;
import com.everhomes.rest.aclink.ListAesUserKeyByUserIdCommand;
import com.everhomes.rest.aclink.ListAesUserKeyByUserResponse;
import com.everhomes.rest.aclink.ListDoorAccessByOwnerIdCommand;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.ListDoorAccessResponse;
import com.everhomes.rest.aclink.ListDoorAuthResponse;
import com.everhomes.rest.aclink.QueryDoorAccessAdminCommand;
import com.everhomes.rest.aclink.QueryDoorMessageResponse;
import com.everhomes.rest.aclink.SearchDoorAuthCommand;
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
        doorAccessService.updateDoorAccess(cmd);
                
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
        cmd.setIsOpenAuth((byte)0);
        RestResponse response = new RestResponse(doorAccessService.listAclinkUsers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /admin/aclink/deleteDoorAccess</b>
     * <p>删除一个组或者单独一个门禁设备</p>
     * @return
     */
    @RequestMapping("deleteDoorAccess")
    @RestReturn(value=String.class)
    public RestResponse deleteDoorAccess(@Valid AclinkDeleteByIdCommand cmd) {
        doorAccessService.deleteDoorAccess(cmd.getId());
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
        doorAccessService.deleteDoorAuth(cmd.getId());
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
    
    /**
     * <b>URL: /admin/aclink/searchDoorAuth</b>
     * <p>获取门禁列表</p>
     * @return 门禁列表
     */
    @RequestMapping("searchDoorAuth")
    @RestReturn(value=ListDoorAuthResponse.class)
    public RestResponse searchDoorAuthByAdmin(@Valid SearchDoorAuthCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.searchDoorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/syncDoorMessages</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("createAclinkFirmware")
    @RestReturn(value=AclinkFirmwareDTO.class)
    public RestResponse createAlinkFireware(@Valid CreateAclinkFirmwareCommand cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.createAclinkFirmware(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createDoorAccessGroup</b>
     * <p>创建门禁分组</p>
     * @return
     */
    @RequestMapping("createDoorAccessGroup")
    @RestReturn(value=DoorAccessDTO.class)
    public RestResponse createDoorAccessGroup(@Valid CreateDoorAccessGroup cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.createDoorAccessGroup(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createDoorAccessLingLing</b>
     * <p>创建 Lingling 门禁设备 </p>
     * @return
     */
    @RequestMapping("createDoorAccessLingLing")
    @RestReturn(value=DoorAccessDTO.class)
    public RestResponse createDoorAccessLingLing(@Valid CreateDoorAccessLingLing cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.createDoorAccessLingLing(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createLingingVistor</b>
     * <p>给令令访客授权</p>
     * @return OK 成功
     */
    @RequestMapping("createLingingVistor")
    @RestReturn(value=DoorAuthDTO.class)
    public RestResponse createLingingVistor(@Valid CreateDoorVisitorCommand cmd) {
        if(cmd.getAuthMethod() == null) {
            cmd.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());    
        }
        
        RestResponse response = new RestResponse(doorAccessService.createDoorVisitorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createLingingVistor</b>
     * <p>给令令访客授权</p>
     * @return OK 成功
     */
    @RequestMapping("listDoorAccessGroup")
    @RestReturn(value=ListDoorAccessResponse.class)
    public RestResponse listDoorAccessGroup(@Valid ListDoorAccessGroupCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorAccessGroup(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }

    /**
     * 
     * <b>URL: /admin/aclink/getCurrentFirmware</b>
     * <p>获取当前版本</p>
     * @return OK 成功
     */
    @RequestMapping("getCurrentFirmware")
    @RestReturn(value=ListDoorAccessResponse.class)
    public RestResponse listDoorAccessGroup(@Valid GetCurrentFirmwareCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.getCurrentFirmware(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createAuthList</b>
     * <p>创建授权</p>
     * @return OK 成功
     */
    @RequestMapping("createAuthList")
    @RestReturn(value=ListDoorAuthResponse.class)
    public RestResponse createDoorAuthList(@Valid AclinkCreateDoorAuthListCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.createDoorAuthList(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * <b>URL: /admin/aclink/searchVisitorDoorAuth</b>
     * <p>获取门禁列表</p>
     * @return 门禁列表
     */
    @RequestMapping("searchVisitorDoorAuth")
    @RestReturn(value=ListDoorAuthResponse.class)
    public RestResponse searchVisitorDoorAuthByAdmin(@Valid SearchDoorAuthCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.searchVisitorDoorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/getShortMessages</b>
     * <p>获取门禁列表</p>
     * @return 门禁列表
     */
    @RequestMapping("getShortMessages")
    @RestReturn(value=GetShortMessageResponse.class)
    public RestResponse getShortMessages(@Valid GetShortMessageCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.getShortMessages(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
