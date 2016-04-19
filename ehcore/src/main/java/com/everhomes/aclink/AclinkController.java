package com.everhomes.aclink;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.AclinkConnectingCommand;
import com.everhomes.rest.aclink.AclinkDeleteByIdCommand;
import com.everhomes.rest.aclink.AclinkDisconnectedCommand;
import com.everhomes.rest.aclink.AclinkUpgradeCommand;
import com.everhomes.rest.aclink.AclinkUpgradeResponse;
import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.CreateDoorAuthByUser;
import com.everhomes.rest.aclink.DoorAccessActivedCommand;
import com.everhomes.rest.aclink.DoorAccessActivingCommand;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.rest.aclink.GetDoorAccessByHardwareIdCommand;
import com.everhomes.rest.aclink.ListAesUserKeyByUserResponse;
import com.everhomes.rest.aclink.ListDoorAccessResponse;
import com.everhomes.rest.aclink.ListDoorAuthCommand;
import com.everhomes.rest.aclink.ListDoorAuthResponse;
import com.everhomes.rest.aclink.QueryDoorMessageCommand;
import com.everhomes.rest.aclink.QueryDoorMessageResponse;
import com.everhomes.util.ConvertHelper;

@RestDoc(value="Aclink controller", site="core")
@RestController
@RequestMapping("/aclink")
public class AclinkController extends ControllerBase {
    @Autowired
    private DoorAccessService doorAccessService;
    
    @Autowired
    private AesServerKeyProvider aesServerKeyProvider;
    
    @Autowired
    private DoorAccessProvider doorAccessProvider;
    
    /**
     * <b>URL: /aclink/activing</b>
     * <p>激活门禁</p>
     * @return 激活门禁消息
     */
    @RequestMapping("activing")
    @RestReturn(value=DoorMessage.class)
    public RestResponse create(@Valid DoorAccessActivingCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.activatingDoorAccess(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/active</b>
     * <p>激活门禁</p>
     * @return 激活门禁消息
     */
    @RequestMapping("active")
    @RestReturn(value=QueryDoorMessageResponse.class)
    public RestResponse active(@Valid DoorAccessActivedCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.activateDoorAccess(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/queryMessages</b>
     * <p>激活门禁</p>
     * @return 激活门禁消息
     */
    @RequestMapping("queryMessages")
    @RestReturn(value=QueryDoorMessageResponse.class)
    public RestResponse queryMessages(@Valid QueryDoorMessageCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.queryDoorMessageByDoorId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/listAesUserKey</b>
     * <p>获取用户授权信息</p>
     * @return
     */
    @RequestMapping("listAesUserKey")
    @RestReturn(value=ListAesUserKeyByUserResponse.class)
    public RestResponse listAesUserKey() {
        RestResponse response = new RestResponse(doorAccessService.listAesUserKeyByUser());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    @RequestMapping("listAdminAesUserKey")
    @RestReturn(value=ListAesUserKeyByUserResponse.class)
    public RestResponse listAdminAesUserKey() {
        RestResponse response = new RestResponse(doorAccessService.listAdminAesUserKeyByUserAuth());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/createAuth</b>
     * <p>给其它用户授权</p>
     * @return OK 成功
     */
    @RequestMapping("createAuth")
    @RestReturn(value=DoorAuthDTO.class)
    public RestResponse createDoorAuth(@Valid CreateDoorAuthByUser cmd) {
        RestResponse response = new RestResponse(doorAccessService.createDoorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /aclink/listAuthHistory</b>
     * <p>获取授权历史信息</p>
     * @return 授权历史列表
     */
    @RequestMapping("listAuthHistory")
    @RestReturn(value=ListDoorAuthResponse.class)
    public RestResponse listAuthHistory(@Valid ListDoorAuthCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.queryDoorAuthByApproveId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }    
    
    /**
     * 
     * <b>URL: /aclink/getDoorAccessByHardwareId</b>
     * <p>获取门禁信息</p>
     * @return 获取门禁信息
     */
    @RequestMapping("getDoorAccessByHardwareId")
    @RestReturn(value=ListDoorAccessResponse.class)
    public RestResponse listAuthHistory(@Valid GetDoorAccessByHardwareIdCommand cmd) {
        ListDoorAccessResponse resp = new ListDoorAccessResponse();
        RestResponse response = new RestResponse(resp);
        List<DoorAccessDTO> dtos = new ArrayList<DoorAccessDTO>();
        resp.setDoors(dtos);
        
        for(String hardwareId : cmd.getHardwareIds()) {
            DoorAccessDTO doorAccess = doorAccessService.getDoorAccessDetail(hardwareId);

            if(doorAccess != null) {
                dtos.add(doorAccess);
            }            
        }

        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /aclink/deleteDoorAuth</b>
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
     * 
     * <b>URL: /aclink/connecting</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("connecting")
    @RestReturn(value=DoorAccessDTO.class)
    public RestResponse connecting(@Valid AclinkConnectingCommand cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.onDoorAccessConnecting(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/disConnected</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("disConnected")
    @RestReturn(value=String.class)
    public RestResponse disConnected(@Valid AclinkDisconnectedCommand cmd) {
        doorAccessService.onDoorAcessDisconnected(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/syncDoorMessages</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("syncWebsocketMessages")
    @RestReturn(value=AclinkWebSocketMessage.class)
    public RestResponse syncWebsocketMessages(@Valid AclinkWebSocketMessage cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.syncWebSocketMessages(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/upgrateAuth</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("upgrateAuth")
    @RestReturn(value=AclinkUpgradeResponse.class)
    public RestResponse upgrateAuth(@Valid AclinkUpgradeCommand cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.upgradeFirmware(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/upgrateAuth</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("upgrateVerify")
    @RestReturn(value=String.class)
    public RestResponse upgrateVerify(@Valid AclinkUpgradeCommand cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.upgradeVerify(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
