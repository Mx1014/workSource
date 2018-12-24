// @formatter:off
package com.everhomes.aclink;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.aclink.dingxin.DingxinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.aclink.AclinkConnectingCommand;
import com.everhomes.rest.aclink.AclinkDeleteByIdCommand;
import com.everhomes.rest.aclink.AclinkDisconnectedCommand;
import com.everhomes.rest.aclink.AclinkLogCreateCommand;
import com.everhomes.rest.aclink.AclinkLogListResponse;
import com.everhomes.rest.aclink.AclinkMgmtCommand;
import com.everhomes.rest.aclink.AclinkRemoteOpenByHardwareIdCommand;
import com.everhomes.rest.aclink.AclinkRemoteOpenCommand;
import com.everhomes.rest.aclink.AclinkServerDTO;
import com.everhomes.rest.aclink.AclinkSyncTimerCommand;
import com.everhomes.rest.aclink.AclinkUpdateLinglingStoreyCommand;
import com.everhomes.rest.aclink.AclinkUpgradeCommand;
import com.everhomes.rest.aclink.AclinkUpgradeResponse;
import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.CreateDoorAuthByUser;
import com.everhomes.rest.aclink.CreateDoorVisitorCommand;
import com.everhomes.rest.aclink.DoorAccessActivedCommand;
import com.everhomes.rest.aclink.DoorAccessActivingCommand;
import com.everhomes.rest.aclink.DoorAccessCapapilityDTO;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAccessDriverType;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.rest.aclink.GetDoorAccessByHardwareIdCommand;
import com.everhomes.rest.aclink.GetDoorAccessCapapilityCommand;
import com.everhomes.rest.aclink.GetPhoneVisitorCommand;
import com.everhomes.rest.aclink.GetShortMessageCommand;
import com.everhomes.rest.aclink.GetShortMessageResponse;
import com.everhomes.rest.aclink.GetVisitorCommand;
import com.everhomes.rest.aclink.GetVisitorResponse;
import com.everhomes.rest.aclink.ListAesUserKeyByUserResponse;
import com.everhomes.rest.aclink.ListDoorAccessByGroupIdCommand;
import com.everhomes.rest.aclink.ListDoorAccessByGroupIdResponse;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.ListDoorAccessQRKeyResponse;
import com.everhomes.rest.aclink.ListDoorAccessResponse;
import com.everhomes.rest.aclink.ListDoorAuthCommand;
import com.everhomes.rest.aclink.ListDoorAuthResponse;
import com.everhomes.rest.aclink.ListFacialRecognitionKeyByUserCommand;
import com.everhomes.rest.aclink.ListFacialRecognitionKeyByUserResponse;
import com.everhomes.rest.aclink.ListFacialRecognitionPhotoByUserResponse;
import com.everhomes.rest.aclink.ListLocalServerByOrgCommand;
import com.everhomes.rest.aclink.QueryDoorMessageCommand;
import com.everhomes.rest.aclink.QueryDoorMessageResponse;
import com.everhomes.rest.aclink.SetFacialRecognitionPhotoCommand;
import com.everhomes.rest.aclink.ListAdminAesUserKeyCommand;
import com.everhomes.rest.aclink.ListAesUserKeyByUserCommand;
import com.everhomes.rest.aclink.ListLocalServerByOrgResponse;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.SignatureHelper;


@RestDoc(value="Aclink controller", site="core")
@RestController
@RequestMapping("/aclink")
public class AclinkController extends ControllerBase {
    @Autowired
    private DoorAccessService doorAccessService;
    
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    
    @Autowired
    private FaceRecognitionPhotoService faceRecognitionPhotoService;

    @Autowired
    AclinkServerService aclinkServerService;
    
    @Autowired
    AclinkLogService aclinkLogService;

    @Autowired
    DingxinService dingxinService;

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
     * <p>激活门禁,在activing之后调用</p>
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
     * <p>获取用户授权信息,蓝牙钥匙,查永久和临时权限,3.0以后不使用</p>
     * @return
     */
    @RequestMapping("listAesUserKey")
    @RestReturn(value=ListAesUserKeyByUserResponse.class)
    public RestResponse listAesUserKey(ListAesUserKeyByUserCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listAesUserKeyByUser(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/listAdminAesUserKey</b>
     * <p>我的钥匙,只查永久权限,3.0以后不使用</p>
     * @return
     */
    @RequestMapping("listAdminAesUserKey")
    @RestReturn(value=ListAesUserKeyByUserResponse.class)
    public RestResponse listAdminAesUserKey(@Valid ListAdminAesUserKeyCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listAdminAesUserKeyByUserAuth(cmd));
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
        if(cmd.getAuthMethod() == null) {
            cmd.setAuthMethod(DoorAuthMethodType.MOBILE.getCode());    
        }
        
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
        Long role = 0l;
        
        if(cmd.getOrganizationId() != null) {
       
            try {
                userPrivilegeMgr.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.MODULE_ACLINK_MANAGER);
                //rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), PrivilegeConstants.AclinkInnerManager);
                role = 1l;
            } catch(Exception e) {}
            
        }
        
        resp.setRole(role);
        
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
     * <b>URL: /aclink/updateAuthBatch</b>
     * <p>删除授权</p>
     * @return
     */
    @RequestMapping("updateAuthBatch")
    @RestReturn(value=String.class)
    public RestResponse updateAuthBatch(@Valid UpdateAuthBatchCommand cmd) {
        doorAccessService.updateAuthBatch(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /aclink/connecting</b>
     * <p>建立门禁websocket链接</p>
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
     * <b>URL: /aclink/serverConnecting</b>
     * <p>建立内网websocket链接</p>
     * @return
     */
    @RequestMapping("serverConnecting")
    @RestReturn(value=AclinkServerDTO.class)
    public RestResponse serverConnecting(@Valid AclinkConnectingCommand cmd) {
        RestResponse response = new RestResponse();

        response.setResponseObject(aclinkServerService.onServerConnecting(cmd));

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /aclink/disConnected</b>
     * <p>断开门禁websocket链接</p>
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
     * <b>URL: /aclink/serverDisconnected</b>
     * <p>断开人脸识别服务器的websocket链接</p>
     * @return
     */
    @RequestMapping("serverDisconnected")
    @RestReturn(value=AclinkServerDTO.class)
    public RestResponse serverDisconnected(@Valid AclinkDisconnectedCommand cmd) {
        RestResponse response = new RestResponse();
        response.setResponseObject(aclinkServerService.onServerDisconnecting(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/syncWebsocketMessages</b>
     * <p>消息同步</p>
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
     * <p>列出所有二维码门禁列表 ,查永久和临时授权,3.0以后不使用</p>
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
     * <b>URL: /aclink/listBusAccessQRKey</b>
     * <p>列出所有巴士二维码列表 </p>
     * @return
     */
    @RequestMapping("listBusAccessQRKey")
    @RestReturn(value=ListDoorAccessQRKeyResponse.class)
    public RestResponse listBusAccessQRKey() {
    	RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.listBusAccessQRKey());
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/listDoorAccessWebQRKey</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    @RequestMapping("listDoorAccessWebQRKey")
    @RestReturn(value=ListDoorAccessQRKeyResponse.class)
    public RestResponse listDoorAccessWebQRKey() {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.listDoorAccessQRKeyAndGenerateQR(null, true));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * 
     * <b>URL: /aclink/getVisitor</b>
     * <p> 设备访客二维码 </p>
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
     * <b>URL: /aclink/getVisitorPhone</b>
     * <p> 保安认证的访客二维码 </p>
     * @return
     */
    @RequestMapping("getVisitorPhone")
    @RequireAuthentication(false)
    @RestReturn(value=GetVisitorResponse.class)
    public RestResponse getDoorVisitorAuthPhoneByUuid(GetVisitorCommand cmd) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.getVisitorPhone(cmd));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/checkVisitor</b>
     * <p> 保安认证 </p>
     * @return
     */
    @RequestMapping("checkVisitor")
    @RestReturn(value=GetVisitorResponse.class)
    public RestResponse doorCheckVisitor(GetPhoneVisitorCommand cmd) {
        RestResponse response = new RestResponse();
        
        GetVisitorCommand cmd2 = new GetVisitorCommand();
        cmd2.setId(cmd.getPhvid());
        cmd2.setNamespaceId(cmd.getNamespaceId());
        response.setResponseObject(doorAccessService.checkVisitor(cmd2));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /aclink/v</b>
     * <p> 访客二维码信息 </p>
     * @return
     */
    @RequestMapping("v")
    @RequireAuthentication(false)
    public Object doorVisitor(GetVisitorCommand cmd) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            //https://core.zuolin.com/evh/aclink/v?id=10ae5-15016
            //https://core.zuolin.com/mobile/static/qr_access/qrCode.html?id=10ae5-15016
            //getVisitor
            DoorAuth auth = doorAccessService.getLinglingDoorAuthByUuid(cmd.getId());
//            if(auth == null) {
//                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "DoorAuth error");
//            }
            if(auth != null && auth.getDriver().equals(DoorAccessDriverType.PHONE_VISIT.getCode())) {
                httpHeaders.setLocation(new URI("/mobile/static/qr_access/qrPhoneCode.html?id=" + cmd.getId()));
                
            } else {
                //if(auth.getDriver().equals(DoorAccessDriverType.ZUOLIN.getCode()))
                httpHeaders.setLocation(new URI("/mobile/static/qr_access/qrCode.html?id=" + cmd.getId()));    
            }
            
        } catch (URISyntaxException e) {
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
    
    /**
     * 
     * <b>URL: /aclink/v</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    @RequestMapping("phv")
    @RequireAuthentication(false)
    public Object doorPhoneVisitor(GetPhoneVisitorCommand cmd, HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String originUrl = "/mobile/static/qr_access/qrAdminCode.html?";
        Map<String, String[]> maps = request.getParameterMap();
        int i = 0;
        for(Entry<String, String[]> m : maps.entrySet()) {
         
            String[] mv = m.getValue();
            String vv = "";
            if(mv.length > 0) {
                vv = mv[0];
            }
            
            if(i == 0) {
                originUrl += m.getKey() + "=" + URLEncoder.encode(vv);
            } else {
                originUrl += "&" + m.getKey() + "=" + URLEncoder.encode(vv);
            }
            i++;
        }
        
        try {
                httpHeaders.setLocation(new URI(originUrl));
        } catch (URISyntaxException e) {
        }
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
    
    /**
     * 
     * <b>URL: /aclink/wifiMgmt</b>
     * <p> wifi 配置命令 </p>
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
    
//    /**
//     * 
//     */
//    @RequestMapping("aclinkMessageTest")
//    @RestReturn(value=ListDoorAccessResponse.class)
//    public RestResponse aclinkMessageTest(@Valid AclinkMessageTestCommand cmd) {
////        doorAccessService.sendMessageToUser(cmd.getUid(), cmd.getDoorId(), cmd.getDoorType());
//        doorAccessService.test();
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;        
//    }
    
    /**
     * 
     * <b>URL: /aclink/remoteOpen</b>
     * <p>远程开门</p>
     * @return
     */
    @RequestMapping("remoteOpen")
    @RestReturn(value=String.class)
    public RestResponse remoteOpen(@Valid AclinkRemoteOpenCommand cmd) {
        doorAccessService.remoteOpenDoor(cmd.getAuthId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /aclink/excuteMessage</b>
     * <p>处理websocket发送的请求</p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("excuteMessage")
    @RestReturn(value=String.class)
    public RestResponse excuteMessage(@Valid AclinkWebSocketMessage cmd) {
        doorAccessService.excuteMessage(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /aclink/remoteOpenByHardwareId</b>
     * <p>远程开门</p>
     * @return
     */
    @RequestMapping("remoteOpenByHardwareId")
    @RestReturn(value=String.class)
    public RestResponse remoteOpen(@Valid AclinkRemoteOpenByHardwareIdCommand cmd) {
        doorAccessService.remoteOpenDoor(cmd.getHardwareId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /aclink/updateAndQueryQR</b>
     * <p>删除一个组或者单独一个门禁设备</p>
     * @return
     */
    @RequestMapping("updateAndQueryQR")
    @RestReturn(value=ListDoorAccessQRKeyResponse.class)
    public RestResponse updateAndQueryQR(@Valid AclinkUpdateLinglingStoreyCommand cmd) {
        doorAccessService.updateAndQueryQR(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /aclink/createDoorVistor</b>
     * <p>给令令访客授权</p>
     * @return OK 成功
     */
    @RequestMapping("createDoorVistor")
    @RestReturn(value=DoorAuthDTO.class)
    public RestResponse createLingingVistor(@Valid CreateDoorVisitorCommand cmd) {
        if(cmd.getAuthMethod() == null) {
            cmd.setAuthMethod(DoorAuthMethodType.MOBILE.getCode());    
        }
        
        RestResponse response = new RestResponse(doorAccessService.createDoorVisitorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * <b>URL: /aclink/createAclinkLog</b>
     * <p>app回传开门日志</p>
     * @return 门禁列表
     */
    @RequestMapping("createAclinkLog")
    @RestReturn(value=AclinkLogListResponse.class)
    public RestResponse createAclinkLog(@Valid AclinkLogCreateCommand cmd) {
        RestResponse response = new RestResponse(aclinkLogService.createAclinkLog(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /aclink/createAclinkLogByServer</b>
     * <p>内网服务器回传开门日志</p>
     * @return 门禁列表
     */
    @RequireAuthentication(false)
    @RequestMapping("createAclinkLogByServer")
    @RestReturn(value=AclinkLogListResponse.class)
    public RestResponse createAclinkLogByServer(@Valid AclinkLogCreateCommand cmd) {
    	//TODO RequireAuthentication
        RestResponse response = new RestResponse(aclinkLogService.createAclinkLogByLocalServer(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /aclink/recordFaceRecognitionResult</b>
     * <p>内网服务器回传识别结果</p>
     * @return 门禁列表
     */
    @RequireAuthentication(false)
    @RequestMapping("recordFaceRecognitionResult")
    @RestReturn(value=String.class)
    public RestResponse recordFaceRecognitionResult(@Valid AclinkLogCreateCommand cmd) {
    	//TODO RequireAuthentication
        RestResponse response = new RestResponse();
        aclinkLogService.recordFaceRecognitionResult(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /aclink/notifyPhotoSyncResult</b>
     * <p>内网服务器回传识别结果</p>
     * @return 门禁列表
     */
    @RequireAuthentication(false)
    @RequestMapping("notifyPhotoSyncResult")
    @RestReturn(value=String.class)
    public RestResponse notifyPhotoSyncResult(@Valid NotifyPhotoSyncResultCommand cmd) {
    	//TODO RequireAuthentication
        RestResponse response = new RestResponse();
        doorAccessService.notifyPhotoSyncResult(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /aclink/syncTimer</b>
     * <p>同步门禁时间</p>
     * @return 同步门禁时间
     */
    @RequestMapping("syncTimer")
    @RestReturn(value=QueryDoorMessageResponse.class)
    public RestResponse syncTimer(@Valid AclinkSyncTimerCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.syncTimerMessage(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /aclink/aliqr</b>
     * <p>alitest 001</p>
     * @return 
     */
    @SuppressDiscover
    @RequireAuthentication(false)
    @RequestMapping("aliqr")
    public void aliQR(HttpServletRequest request, HttpServletResponse response) {
        doorAccessService.doAlipayRedirect(request, response);
    }
    
    /**
     * <b>URL: /aclink/getAliQR</b>
     * <p>getAliQR from alipay</p>
     * @return 
     */
    @RequireAuthentication(false)
    @RequestMapping("getAliQR")
    public RestResponse getAliQR(HttpServletRequest request) {
        RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.getAlipayQR(request));
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /aclink/faceTest</b>
     * <p>alitest 002</p>
     * @return 
     */
    @SuppressDiscover
    @RequireAuthentication(false)
    @RequestMapping("faceTest")
    public String faceTest(HttpServletRequest request) {
        //doorAccessService.test();
        return SignatureHelper.generateSecretKey();
    }
    
    /**
     * 
     * <b>URL: /aclink/v</b>
     * <p>列出所有二维码门禁列表 </p>
     * @return
     */
    /*@RequestMapping("doorTest3")
    @RequireAuthentication(false)
    public Object doorTest3(HttpServletRequest request) {
        doorAccessService.sendXiaomiMessage();
        Map<String,Long> m = new HashMap<String,Long>();
        m.put("result", 0l);
        return m;
    }*/


    /**
     *
     * <b>URL: /aclink/listDoorAccessByUser</b>
     * <p>列出用户授权梯控列表 </p>
     * @return
     */
    @RequestMapping("listDoorAccessByUser")
    @RestReturn(value=DoorAccessGroupResp.class)
    public RestResponse listDoorAccessByUser(ListDoorAccessByUserCommand cmd) {
        RestResponse response = new RestResponse();

        response.setResponseObject(doorAccessService.listDoorAccessByUser(cmd));

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     *
     * <b>URL: /aclink/setFacialRecognitionPhoto</b>
     * <p>人脸识别照片上传 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("setFacialRecognitionPhoto")
    @RestReturn(value=String.class)
    public RestResponse setFacialRecognitionPhoto(SetFacialRecognitionPhotoCommand cmd){
    	RestResponse response = new RestResponse();
    	faceRecognitionPhotoService.setFacialRecognitionPhoto(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
    *
    * <b>URL: /aclink/getPhotoSyncResult</b>
    * <p>人脸识别照片上传 </p>
    * @return
    */
   @RequireAuthentication(false)
   @RequestMapping("getPhotoSyncResult")
   @RestReturn(value=GetPhotoSyncResultResponse.class)
   public RestResponse getPhotoSyncResult(){
		RestResponse response = new RestResponse(faceRecognitionPhotoService.getPhotoSyncResult());
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
   }
    
    /**
    *
    * <b>URL: /aclink/deletePhotoByIds</b>
    * <p>删除人脸识别照片 </p>
    * @return
    */
   @RequestMapping("deletePhotoByIds")
   @RestReturn(value=String.class)
   public RestResponse deletePhotoByIds(DeletePhotoByIdCommand cmd){
		RestResponse response = new RestResponse();
		faceRecognitionPhotoService.invalidPhotoByIds(cmd);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
   }

    /**
     *
     * <b>URL: /aclink/listFacialRecognitionPhotoByUser</b>
     * <p>显示人脸识别照片 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("listFacialRecognitionPhotoByUser")
    @RestReturn(value=ListFacialRecognitionPhotoByUserResponse.class)
    public RestResponse listFacialRecognitionPhotoByUser(){
    	RestResponse response = new RestResponse(faceRecognitionPhotoService.listFacialRecognitionPhotoByUser());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /aclink/listDoorAccessByGroupId</b>
     * <p>获取组内门禁 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("listDoorAccessByGroupId")
    @RestReturn(value=ListDoorAccessByGroupIdResponse.class)
    public RestResponse listDoorAccessByGroupId(ListDoorAccessByGroupIdCommand cmd){
    	RestResponse response = new RestResponse(doorAccessService.listDoorAccessByGroupId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /aclink/listFacialRecognitionKeyByUser</b>
     * <p>获取人脸开门钥匙 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("listFacialRecognitionKeyByUser")
    @RestReturn(value=ListFacialRecognitionKeyByUserResponse.class)
    public RestResponse listFacialRecognitionKeyByUser(ListFacialRecognitionKeyByUserCommand cmd){
    	RestResponse response = new RestResponse(doorAccessService.listFacialAesUserKeyByUser(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /aclink/getShortMessages</b>
     * <p>来访事由</p>
     */
    @RequestMapping("getShortMessages")
    @RestReturn(value=GetShortMessageResponse.class)
    public RestResponse getShortMessages(@Valid GetShortMessageCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.getShortMessages(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /aclink/listLocalServerByOrg</b>
     * <p> 内网服务器列表 </p>
     * @return
     */
    @RequestMapping("listLocalServerByOrg")
    @RestReturn(value=ListLocalServerByOrgResponse.class)
    public RestResponse listLocalServerByUser(ListLocalServerByOrgCommand cmd) {
        RestResponse response = new RestResponse();

        response.setResponseObject(aclinkServerService.listLocalServerByOrg(cmd));

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /aclink/listUserAuth</b>
     * <p>获取钥匙信息 门禁3.0</p>
     * @return
     */
    @RequestMapping("listUserAuth")
    @RestReturn(value=ListUserAuthResponse.class)
    public RestResponse listUserKeys(ListAesUserKeyByUserCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listUserKeys(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/getUserKeyInfo</b>
     * <p>获取钥匙信息 门禁3.0</p>
     * @return
     */
    @RequestMapping("getUserKeyInfo")
    @RestReturn(value= GetUserKeyInfoResponse.class)
    public RestResponse getUserKeyInfo(GetUserKeyInfoCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.getUserKeyInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
    *
    * <b>URL: /aclink/listZLDoorAccess</b>
    * <p> 获取门禁mac地址列表 </p>
    * @return
    */
   @RequestMapping("listZLDoorAccess")
   @RestReturn(value=ListZLDoorAccessResponse.class)
   @RequireAuthentication(value = true)
   public RestResponse listZLDoorAccess() {
       RestResponse response = new RestResponse();

       response.setResponseObject(doorAccessService.listDoorAccessMacByApp());

       response.setErrorCode(ErrorCodes.SUCCESS);
       response.setErrorDescription("OK");
       return response;
   }
   
   /**
   *
   * <b>URL: /aclink/getZLAesUserKey</b>
   * <p> 第三方申请门禁钥匙 </p>
   * @return
   */
  @RequestMapping("getZLAesUserKey")
  @RestReturn(value=GetZLAesUserKeyResponse.class)
  @RequireAuthentication(value = true)
  public RestResponse getZLAesUserKey(GetZLAesUserKeyCommand cmd) {
      RestResponse response = new RestResponse();

      response.setResponseObject(doorAccessService.getAppAesUserKey(cmd));

      response.setErrorCode(ErrorCodes.SUCCESS);
      response.setErrorDescription("OK");
      return response;
  }
  
  /**
  *
  * <b>URL: /aclink/createZLVisitorQRKey</b>
  * <p> 第三方申请访客二维码 </p>
  * @return
  */
 @RequestMapping("createZLVisitorQRKey")
 @RestReturn(value=CreateZLVisitorQRKeyResponse.class)
 @RequireAuthentication(value = true)
 public RestResponse createZLVisitorQRKey(CreateZLVisitorQRKeyCommand cmd) {
     RestResponse response = new RestResponse();

     response.setResponseObject(doorAccessService.createZLVisitorQRKey(cmd));

     response.setErrorCode(ErrorCodes.SUCCESS);
     response.setErrorDescription("OK");
     return response;
 }
    /**
     * <b>URL: /aclink/queryServiceHotline</b>
     * <p>查询服务热线</p>
     * @return
     */
    @RequestMapping("queryServiceHotline")
    @RestReturn(value = QueryServiceHotlineResponse.class)
    public RestResponse queryServiceHotline(@Valid QueryServiceHotlineCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.queryServiceHotline(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /aclink/verifyDoorAuth</b>
     * <p> (鼎芯)判断是否有开门权限</p>
     * @return
     */
    @RequestMapping("verifyDoorAuth")
    @RestReturn(value=String.class)
    @RequireAuthentication(value = false)
    public RestResponse verifyOpenAuth(VerifyDoorAuthCommand cmd) {
        RestResponse response = new RestResponse();
        response.setResponseObject(dingxinService.verifyDoorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
