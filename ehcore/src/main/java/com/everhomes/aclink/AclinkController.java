package com.everhomes.aclink;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.aclink.lingling.AclinkLinglingConstant;
import com.everhomes.aclink.lingling.AclinkLinglingDevice;
import com.everhomes.aclink.lingling.AclinkLinglingMakeSdkKey;
import com.everhomes.aclink.lingling.AclinkLinglingService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.AclinkConnectingCommand;
import com.everhomes.rest.aclink.AclinkDeleteByIdCommand;
import com.everhomes.rest.aclink.AclinkDisconnectedCommand;
import com.everhomes.rest.aclink.AclinkMgmtCommand;
import com.everhomes.rest.aclink.AclinkUpgradeCommand;
import com.everhomes.rest.aclink.AclinkUpgradeResponse;
import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.CreateDoorAuthByUser;
import com.everhomes.rest.aclink.CreateLinglingVisitorCommand;
import com.everhomes.rest.aclink.DoorAccessActivedCommand;
import com.everhomes.rest.aclink.DoorAccessActivingCommand;
import com.everhomes.rest.aclink.DoorAccessCapapilityDTO;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.rest.aclink.GetDoorAccessByHardwareIdCommand;
import com.everhomes.rest.aclink.GetDoorAccessCapapilityCommand;
import com.everhomes.rest.aclink.GetVisitorCommand;
import com.everhomes.rest.aclink.GetVisitorResponse;
import com.everhomes.rest.aclink.ListAesUserKeyByUserResponse;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.ListDoorAccessQRKeyResponse;
import com.everhomes.rest.aclink.ListDoorAccessResponse;
import com.everhomes.rest.aclink.ListDoorAuthCommand;
import com.everhomes.rest.aclink.ListDoorAuthResponse;
import com.everhomes.rest.aclink.QueryDoorMessageCommand;
import com.everhomes.rest.aclink.QueryDoorMessageResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;

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
    
    @Autowired
    private AclinkLinglingService aclinkLinglingService;
    
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
    
    /**
     * 
     * <b>URL: /aclink/getDoorAccessCapapilityCommand</b>
     * <p>创建 Lingling 门禁设备 </p>
     * @return
     */
    @RequestMapping("getDoorAccessCapapility")
    @RestReturn(value=DoorAccessCapapilityDTO.class)
    public RestResponse getDoorAccessCapapility(@Valid GetDoorAccessCapapilityCommand cmd) {
        RestResponse response = new RestResponse();
      
//        AclinkLinglingMakeSdkKey sdkKey = new AclinkLinglingMakeSdkKey();
//        sdkKey.setDeviceIds(new ArrayList<Long>(){{add(1008l);}});
//        aclinkLinglingService.makeSdkKey(sdkKey);
        
//        AclinkLinglingDevice device = new AclinkLinglingDevice();
//        device.setDeviceCode("920F41B7F75C0");
//        device.setDeviceName("test");
//        aclinkLinglingService.createDevice(device);
        
        response.setResponseObject(doorAccessService.getDoorAccessCapapility(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/listDoorAccessQRKey</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    @RequestMapping("listDoorAccessQRKey")
    @RestReturn(value=ListDoorAccessQRKeyResponse.class)
    public RestResponse listDoorAccessQRKey() {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.listDoorAccessQRKey());
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/getVisitor</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    @RequestMapping("getVisitor")
    @RequireAuthentication(false)
    @RestReturn(value=GetVisitorResponse.class)
    public RestResponse getDoorVisitorAuthByUuid(GetVisitorCommand cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.getVisitor(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/v</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    @RequestMapping("v")
    @RequireAuthentication(false)
    public Object doorVisitor(GetVisitorCommand cmd) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI("/mobile/static/qr_access/qrCode.html?id=" + cmd.getId()));
        } catch (URISyntaxException e) {
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
    
    /**
     * 
     * <b>URL: /aclink/listDoorAccessQRKey</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    @RequestMapping("wifiMgmt")
    @RestReturn(value=DoorMessage.class)
    public RestResponse getDoorVisitorAuthByUuid(AclinkMgmtCommand cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.queryWifiMgmtMessage(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/listDoorAccessGroup</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    @RequestMapping("listDoorAccessGroup")
    @RestReturn(value=ListDoorAccessResponse.class)
    public RestResponse listDoorAccessGroup(@Valid ListDoorAccessGroupCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorAccessGroup(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
}
