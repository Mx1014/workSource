package com.everhomes.aclink.admin;

import com.everhomes.aclink.*;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestDoc(value="Aclink Admin controller", site="core")
@RestController
@RequestMapping("/admin/aclink")
public class AclinkAdminController extends ControllerBase {
    @Autowired
    private DoorAccessService doorAccessService;
    
    @Autowired
    private AesServerKeyProvider aesServerKeyProvider;
    
    @Autowired
    private AclinkLogProvider aclinkLogProvider;
    
    @Autowired
    private DoorAuthProvider doorAuthProvider;
    
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
//        cmd.setIsOpenAuth((byte)0);
        RestResponse response = new RestResponse(doorAccessService.listAclinkUsers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/exportAclinkUsersXls</b>
     * <p>导出門禁授權用戶列表</p>
     */
    @RequestMapping("exportAclinkUsersXls")
    @RestReturn(value=String.class)
    public RestResponse exportAclinkUsersXls(@Valid ListAclinkUserCommand cmd, HttpServletResponse httpResponse) {
        doorAccessService.exportAclinkUsersXls(cmd, httpResponse);
        RestResponse response = new RestResponse();
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
     *
     * <b>URL: /admin/aclink/createAllAuthList</b>
     * <p>全部查询结果授权</p>
     * @return OK 成功
     */
    @RequestMapping("createAllAuthList")
    @RestReturn(value=ListDoorAuthResponse.class)
    public RestResponse createAllAuthList(@Valid AclinkCreateAllDoorAuthListCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.createAllDoorAuthList(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /admin/aclink/checkAllAuthList</b>
     * <p>校验能不能全部授权</p>
     * @return
     */
    @RequestMapping("checkAllAuthList")
    @RestReturn(value=String.class)
    public RestResponse checkAllAuthList() {
        RestResponse response = new RestResponse(doorAccessService.checkAllDoorAuthList());
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
     * <b>URL: /admin/aclink/exportVisitorDoorAuthByAdmin</b>
     * <p>导出访客授权记录</p>
     */
    @RequestMapping("exportVisitorDoorAuthByAdmin")
    @RestReturn(value=String.class)
    public RestResponse exportVisitorDoorAuthByAdmin(@Valid ExportDoorAuthCommand cmd, HttpServletResponse httpResponse) {
        doorAccessService.exportVisitorDoorAuth(cmd, httpResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listDoorAuthLogs</b>
     * <p>授权记录</p>
     * @return
     */
    @RequestMapping("listDoorAuthLogs")
    @RestReturn(value=ListDoorAuthLogResponse.class)
    public RestResponse listDoorAuthLogs(@Valid ListDoorAuthLogCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorAuthLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/qryDoorAuthStatistics</b>
     * <p>授权用户统计</p>
     * @return
     */
    @RequestMapping("qryDoorAuthStatistics")
    @RestReturn(value=DoorAuthStatisticsDTO.class)
    public RestResponse qryDoorAuthStatistics(QryDoorAuthStatisticsCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.qryDoorAuthStatistics(cmd));
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
    
    /**
     * <b>URL: /admin/aclink/queryLogs</b>
     * <p>获取门禁列表</p>
     * @return 门禁列表
     */
    @RequestMapping("queryLogs")
    @RestReturn(value=AclinkQueryLogResponse.class)
    public RestResponse queryLogs(@Valid AclinkQueryLogCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.queryLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/deleteLogById</b>
     * <p>获取门禁列表</p>
     * @return 门禁列表
     */
    @RequestMapping("deleteLogById")
    @RestReturn(value=String.class)
    public RestResponse deleteLogById(@Valid AclinkLogDeleteCommand cmd) {
        AclinkLog obj = aclinkLogProvider.getAclinkLogById(cmd.getId());
        if(obj != null) {
            aclinkLogProvider.deleteAclinkLog(obj);
        }
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
    
    /**
     * <b>URL: /admin/aclink/authVisitorStatistic</b>
     * <p>访客统计</p>
     * @return 门禁列表
     */
    @RequestMapping("authVisitorStatistic")
    @RestReturn(value=AuthVisitorStasticResponse.class)
    public RestResponse authVisitorStatistic(@Valid AuthVisitorStatisticCommand cmd) {
        if(cmd.getStart() == null) {
            cmd.setStart(DateHelper.parseDataString(cmd.getStartStr(), "yyyy-MM-dd").getTime());
        }
        if(cmd.getEnd() == null) {
            cmd.setEnd(DateHelper.parseDataString(cmd.getEndStr(), "yyyy-MM-dd").getTime());
        }
        AuthVisitorStasticResponse obj = doorAuthProvider.authVistorStatistic(cmd);
        RestResponse response = new RestResponse(obj);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }       
    
    /**
     * <b>URL: /admin/aclink/createQRUserPermission</b>
     * <p>创建保安二维码授权</p>
     * @return 授权详情
     */
    @RequestMapping("createQRUserPermission")
    @RestReturn(value=DoorUserPermissionDTO.class)
    public RestResponse createQRUserPermission(@Valid CreateQRUserPermissionCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.createQRUserPermission(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }   
    
    /**
     * <b>URL: /admin/aclink/deleteQRUserPermission</b>
     * <p>创建保安二维码授权</p>
     * @return 授权详情
     */
    @RequestMapping("deleteQRUserPermission")
    @RestReturn(value=DoorUserPermissionDTO.class)
    public RestResponse deleteQRUserPermission(@Valid DeleteQRUserPermissionCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.deleteQRUserPermission(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }   
    
    /**
     * <b>URL: /admin/aclink/listQRUserPermission</b>
     * <p>创建保安二维码授权</p>
     * @return 授权详情
     */
    @RequestMapping("listQRUserPermission")
    @RestReturn(value=ListQRUserPermissionResponse.class)
    public RestResponse listQRUserPermission(@Valid ListQRUserPermissionCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listQRUserPermissions(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/getServerKey</b>
     * <p>测试专用</p>
     * @return 
     */
    @RequestMapping("getServerKey")
    @RestReturn(value=AclinkGetServerKeyResponse.class)
    public RestResponse getServerKey(@Valid AclinkGetServerKeyCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.getServerKey(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createAuthLevel</b>
     * <p>创建不同层级的授权</p>
     * @return OK 成功
     */
    @RequestMapping("createAuthLevel")
    @RestReturn(value=DoorAuthLevelDTO.class)
    public RestResponse createDoorAuthLevel(@Valid CreateDoorAuthLevelCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.createDoorAuthLevel(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/listDoorAuthLevel</b>
     * <p>显示门禁下的公司授权</p>
     * @return OK 成功
     */
    @RequestMapping("listDoorAuthLevel")
    @RestReturn(value=ListDoorAuthLevelResponse.class)
    public RestResponse getDoorAuthLevel(@Valid ListDoorAuthLevelCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorAuthLevel(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/deleteDoorAuthLevel</b>
     * <p>删除某个公司的授权</p>
     * @return OK 成功
     */
    @RequestMapping("deleteDoorAuthLevel")
    @RestReturn(value=String.class)
    public RestResponse getDoorAuthLevel(@Valid DeleteDoorAuthLevelCommand cmd) {
        doorAccessService.deleteDoorAuthLevel(cmd.getId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }


}
