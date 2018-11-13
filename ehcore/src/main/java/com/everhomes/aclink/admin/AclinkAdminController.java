// @formatter:off
package com.everhomes.aclink.admin;

import com.everhomes.aclink.*;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RequireAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
    
    @Autowired
    AclinkServerService aclinkServerService;
    
    @Autowired
    AclinkCameraService aclinkCameraService;
    
    @Autowired
    AclinkIpadService aclinkIpadService;
    
    @Autowired
    private FaceRecognitionPhotoService faceRecognitionPhotoService;
    
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
     * <b>URL: /admin/aclink/listDoorAccess</b>
     * <p>获取门禁列表 lite版</p>
     * @return 门禁列表
     */
    @RequestMapping("listDoorAccess")
    @RestReturn(value=ListDoorAccessLiteResponse.class)
    public RestResponse listDoorAccessByOwnerIdLite(@Valid QueryDoorAccessAdminCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorAccessByOwnerIdLite(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/getDoorAccessById</b>
     * <p>根据id获取单个门禁详情</p>
     * @return 门禁列表
     */
    @RequestMapping("getDoorAccessById")
    @RestReturn(value=DoorAccessDTO.class)
    public RestResponse getDoorAccessById(@Valid GetDoorAccessByIdCommand cmd) {
    	RestResponse response = new RestResponse();
        
        response.setResponseObject(doorAccessService.getDoorAccessById(cmd));
        
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
     * <b>URL: /admin/aclink/listFormalAuth</b>
     * <p>正式授权列表,能远程开门必须要有常规开门的权限</p>
     * @return
     */
    @RequestMapping("listFormalAuth")
    @RestReturn(value=ListFormalAuthResponse.class)
    public RestResponse listFormalAuth(@Valid ListFormalAuthCommand cmd) {
    	//TODO 
        RestResponse response = new RestResponse(doorAccessService.listFormalAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK"); 
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/updateAuthBatch</b>
     * <p>批量更新(正式)授权</p>
     * @return
     */
    @RequestMapping("updateAuthBatch")
    @RestReturn(value=String.class)
    public RestResponse updateAuthBatch(@Valid UpdateAuthBatchCommand cmd) {
//        cmd.setIsOpenAuth((byte)0);
    	//TODO 
    	doorAccessService.updateAuthBatch(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createFormalAuthBatch</b>
     * <p>批量创建(正式)授权</p>
     * @return
     */
    @RequestMapping("createFormalAuthBatch")
    @RestReturn(value=String.class)
    public RestResponse createAuthBatch(@Valid CreateFormalAuthBatchCommand cmd) {
    	doorAccessService.createFormalAuthBatch(cmd);
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
     * <b>URL: /admin/aclink/createAclinkFirmware</b>
     * <p>新增版本包</p>
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
    //门禁v3.0.2 add by liqingyan
    /**
     * <b>URL: /admin/aclink/createTempAuth</b>
     * <p>创建临时授权</p>
     * @return OK 成功
     */
    @RequestMapping("createTempAuth")
    @RestReturn(value=String.class)
    public RestResponse createTempAuth (@Valid CreateTempAuthCommand cmd){
        RestResponse response = new RestResponse();
        doorAccessService.createTempAuth(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/aclink/listTempAuth</b>
     * <p>获取门禁授权列表</p>
     * @return 门禁列表
     */
    @RequestMapping("listTempAuth")
    @RestReturn(value=ListDoorAuthResponse.class)
    public RestResponse listTempAuth(@Valid SearchDoorAuthCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listTempAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/createTempAuthCustomField</b>
     * <p>创建临时授权自定义字段</p>
     * @return OK 成功
     */
    @RequestMapping("createTempAuthCustomField")
    @RestReturn(value=String.class)
    public RestResponse createTempAuthCustomField (@Valid CreateTempAuthCustomFieldCommand cmd){
        RestResponse response = new RestResponse();
        doorAccessService.createTempAuthCustomField(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listSelectDoorsAndGroups</b>
     * <p>列出可选门禁（组）</p>
     * @return OK 成功
     */
    @RequestMapping("listSelectDoorsAndGroups")
    @RestReturn(value=ListSelectDoorsAndGroupsResponse.class)
    public RestResponse listSelectDoorsAndGroups (@Valid ListSelectDoorsAndGroupsCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.listSelectDoorsAndGroups(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/createTempAuthPriority</b>
     * <p>创建临时授权优先门禁</p>
     * @return OK 成功
     */
    @RequestMapping("createTempAuthPriority")
    @RestReturn(value=String.class)
    public RestResponse createTempAuthPriority (@Valid CreateTempAuthPriorityCommand cmd){
        RestResponse response = new RestResponse();
        doorAccessService.createTempAuthPriority(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/deleteTempAuthPriority</b>
     * <p>删除临时授权优先门禁</p>
     * @return OK 成功
     */
    @RequestMapping("deleteTempAuthPriority")
    @RestReturn(value=AclinkFormValuesDTO.class)
    public RestResponse deleteTempAuthPriority (@Valid DeleteTempAuthPriorityCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.deleteTempAuthPriority(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listTempAuthPriority</b>
     * <p>列出临时授权优先门禁</p>
     * @return OK 成功
     */
    @RequestMapping("listTempAuthPriority")
    @RestReturn(value=ListTempAuthPriorityResponse.class)
    public RestResponse listTempAuthPriority (@Valid ListTempAuthPriorityCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.listTempAuthPriority(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listTempAuthDefaultRule</b>
     * <p>列出临时授权默认规则</p>
     * @return OK 成功
     */
    @RequestMapping("listTempAuthDefaultRule")
    @RestReturn(value=ListTempAuthDefaultRuleResponse.class)
    public RestResponse listTempAuthDefaultRule (@Valid ListTempAuthDefaultRuleCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.listTempAuthDefaultRule(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/createTempAuthDefaultRule</b>
     * <p>创建临时授权默认规则</p>
     * @return OK 成功
     */
    @RequestMapping("createTempAuthDefaultRule")
    @RestReturn(value=String.class)
    public RestResponse createTempAuthDefaultRule (@Valid CreateTempAuthDefaultRuleCommand cmd){
        RestResponse response = new RestResponse();
        doorAccessService.createTempAuthDefaultRule(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listTempAuthCustomField</b>
     * <p>查询临时授权自定义字段</p>
     * @return OK 成功
     */
    @RequestMapping("listTempAuthCustomField")
    @RestReturn(value=ListTempAuthCustomFieldResponse.class)
    public RestResponse listTempAuthCustomField (@Valid ListTempAuthCustomFieldCommand cmd){
        RestResponse response = new RestResponse();
        response.setResponseObject(doorAccessService.listTempAuthCustomField(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/changeTempAuthCustomField</b>
     * <p>修改临时授权自定义字段</p>
     * @return OK 成功
     */
    @RequestMapping("changeTempAuthCustomField")
    @RestReturn(value=String.class)
    public RestResponse listTempAuthCustomField (@Valid ChangeTempAuthCustomFieldCommand cmd){
        RestResponse response = new RestResponse();
        doorAccessService.changeTempAuthCustomField(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * 
     * <b>URL: /admin/aclink/listDoorAccessGroup</b>
     * <p>列出所有门禁列表</p>
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
     * <b>URL: /admin/aclink/listAccessGroupRel</b>
     * <p>获取门禁组-门禁关系</p>
     * @return 门禁列表
     */
    @RequestMapping("listAccessGroupRel")
    @RestReturn(value=ListAccessGroupRelResponse.class)
    public RestResponse listDoorGroup(@Valid ListDoorAccessGroupCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorGroupRel(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/updateDoorGroup</b>
     * <p>更新门禁组</p>
     * @return 门禁列表
     */
    @RequestMapping("updateDoorGroup")
    @RestReturn(value=String.class)
    public RestResponse updateDoorGroup(@Valid UpdateDoorAccessGroupCommand cmd) {
        doorAccessService.updateDoorGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/createDoorGroup</b>
     * <p>新增门禁组</p>
     * @return 门禁列表
     */
    @RequestMapping("createDoorGroup")
    @RestReturn(value=String.class)
    public RestResponse createDoorGroup(@Valid CreateDoorAccessGroupCommand cmd) {
        doorAccessService.createDoorGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listDoorGroupNew</b>
     * <p>列出门禁组</p>
     * @return 门禁列表
     */
    @RequestMapping("listDoorGroupNew")
    @RestReturn(value=ListDoorGroupResponse.class)
    public RestResponse listDoorGroupNew(@Valid ListDoorGroupCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorGroupNew(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/deleteDoorGroupRel</b>
     * <p>删除门禁组-门禁关系</p>
     * @return 门禁列表
     */
    @RequestMapping("deleteDoorGroupRel")
    @RestReturn(value=String.class)
    public RestResponse deleteDoorGroupRel(@Valid DeleteDoorGroupRelCommand cmd) {
        doorAccessService.deleteDoorGroupRel(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listSelectDoors</b>
     * <p>列出可选门禁</p>
     * @return 门禁列表
     */
    @RequestMapping("listSelectDoors")
    @RestReturn(value=ListSelectDoorsResponse.class)
    public RestResponse listSelectDoors(@Valid ListSelectDoorsCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listSelectDoors(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/getTempAuthSettings</b>
     * <p>获取门禁组-门禁关系</p>
     * @return 门禁列表
     */
    @RequestMapping("getTempAuthSettings")
    @RestReturn(value=GetTempAuthSettingsResponse.class)
    public RestResponse getTempAuthSettings(@Valid GetTempAuthSettingsCommand cmd) {
//    	Map<String, String> resMap = new HashMap<>();
//    	resMap.put("TESTKEY", "testvalue");
    	GetTempAuthSettingsResponse rsp = new GetTempAuthSettingsResponse();
//    	rsp.setConfigs(resMap);
        RestResponse response = new RestResponse(rsp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/setTempAuthSettings</b>
     * <p>获取门禁组-门禁关系</p>
     * @return 门禁列表
     */
    @RequestMapping("setTempAuthSettings")
    @RestReturn(value=String.class)
    public RestResponse setTempAuthSettings(@Valid SetTempAuthSettingsCommand cmd) {
//    	Map<String, String> resMap = new HashMap<>();
//    	resMap.put("TESTKEY", "testvalue");
//    	rsp.setConfigs(resMap);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /admin/aclink/updateAccessType</b>
     * <p>门禁类型转变 </p>
     * @return
     */
    @RequestMapping("updateAccessType")
    @RestReturn(value=String.class)
    public RestResponse updateAccessType(UpdateAccessTypeCommand cmd) {
    	RestResponse response = new RestResponse();
    	doorAccessService.updateAccessType(cmd.getDoorId(), cmd.getDoorType());
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
    public RestResponse getCurrentFirmware(@Valid GetCurrentFirmwareCommand cmd) {
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
     * <p>获取门禁授权列表</p>
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
//add by liqingyan
    /**
     * <b>URL: /admin/aclink/exportAclinkLogsXls</b>
     * <p>导出门禁日志列表</p>
     */
    @RequestMapping("exportAclinkLogsXls")
    @RestReturn(value=String.class)
    public RestResponse exportAclinkLogsXls(@Valid AclinkQueryLogCommand cmd, HttpServletResponse httpResponse) {
        doorAccessService.exportAclinkLogsXls(cmd, httpResponse);
        RestResponse response = new RestResponse();
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
    	//---by liuyilin 20180417  查不到当天的数据,前端传过来的时分秒默认是当前时间,startTime改为0时0分,endTime改为第二天0点. by liuyilin 20180417
    	if(cmd.getStart() == null) {
            cmd.setStart(DateHelper.parseDataString(cmd.getStartStr(), "yyyy-MM-dd").getTime());
        }else{
        	
        	cmd.setStart(DateHelper.parseDataString(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT+:08:00"), cmd.getStart()),"yyyy-MM-dd").getTime());
        }
        if(cmd.getEnd() == null) {
            cmd.setEnd(DateHelper.parseDataString(cmd.getEndStr(), "yyyy-MM-dd").getTime() + 24*60*60*1000);
        }else{
        	cmd.setEnd(DateHelper.parseDataString(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT+:08:00"), cmd.getEnd()),"yyyy-MM-dd").getTime() + 24*60*60*1000);
        }
        //---
        AuthVisitorStasticResponse obj = doorAuthProvider.authVistorStatistic(cmd);
        RestResponse response = new RestResponse(obj);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/doorStatisticEh</b>
     * <p>左邻后台门禁统计</p>
     * @return 门禁列表
     */
    @RequestMapping("doorStatisticEh")
    @RestReturn(value=DoorStatisticEhResponse.class)
    public RestResponse doorStatisticEh(@Valid DoorStatisticEhCommand cmd) {
        DoorStatisticEhResponse obj = doorAccessService.doorStatisticEh(cmd);
        RestResponse response = new RestResponse(obj);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//add by liqingyan
    /**
     * <b>URL: /admin/aclink/doorStatistic</b>
     * <p>门禁综合统计</p>
     * @return 门禁列表
     */
    @RequestMapping("doorStatistic")
    @RestReturn(value=DoorStatisticResponse.class)
    public RestResponse doorStatistic(@Valid DoorStatisticCommand cmd) {
        DoorStatisticResponse obj = doorAccessService.doorStatistic(cmd);
        RestResponse response = new RestResponse(obj);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/aclink/listDoorAccessEh</b>
     * <p>列出门禁列表（左邻后台）</p>
     * @return 门禁列表
     */
    @RequestMapping("listDoorAccessEh")
    @RestReturn(value=ListDoorAccessEhResponse.class)
    public RestResponse listDoorAccessEh(@Valid ListDoorAccessEhCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.listDoorAccessEh(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/deleteDoorAccessEh</b>
     * <p>删除门禁设备(左邻后台)</p>
     * @return OK 成功
     */
    @RequestMapping("deleteDoorAccessEh")
    @RestReturn(value=String.class)
    public RestResponse deleteDoorAccessEh(@Valid AclinkDeleteByIdCommand cmd) {
        doorAccessService.deleteDoorAccessEh(cmd.getId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/changeDoorName</b>
     * <p>修改门禁名称(左邻后台)</p>
     * @return OK 成功
     */
    @RequestMapping("changeDoorName")
    @RestReturn(value=String.class)
    public RestResponse changeDoorName(@Valid ChangeDoorNameCommand cmd){
        doorAccessService.changeDoorName(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/addDoorManagement</b>
     * <p>添加门禁管理授权企业(左邻后台)</p>
     * @return OK 成功
     */
    @RequestMapping("addDoorManagement")
    @RestReturn(value=String.class)
    public RestResponse addDoorManagement (@Valid AddDoorManagementCommand cmd){
        doorAccessService.addDoorManagement(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listDoorManagement</b>
     * <p>列出门禁管理授权企业(左邻后台)</p>
     * @return OK 成功
     */
    @RequestMapping("listDoorManagement")
    @RestReturn(value=ListDoorManagementResponse.class)
    public RestResponse listDoorManagement (@Valid ListDoorManagementCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.listDoorManagement(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/deleteDoorManagement</b>
     * <p>删除门禁管理授权企业(左邻后台)</p>
     * @return OK 成功
     */
    @RequestMapping("deleteDoorManagement")
    @RestReturn(value=String.class)
    public RestResponse deleteDoorManagement (@Valid DeleteDoorManagementCommand cmd){
        doorAccessService.deleteDoorManagement(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/listDoorType</b>
     * <p>列出设备类型列表（左邻后台）</p>
     * @return 门禁列表
     */
    @RequestMapping("listDoorType")
    @RestReturn(value=ListDoorTypeResponse.class)
    public RestResponse listDoorType  (@Valid ListDoorTypeCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.listDoorType(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/changeUpdateFirmware</b>
     * <p>修改默认升级固件（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("changeUpdateFirmware")
    @RestReturn(value=AclinkDeviceDTO.class)
    public RestResponse changeUpdateFirmware (@Valid ChangeUpdateFirmwareCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.changeUpdateFirmware(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/listFirmware</b>
     * <p>列出固件（左邻后台）</p>
     * @return 固件列表
     */
    @RequestMapping("listFirmware")
    @RestReturn(value=ListFirmwareResponse.class)
    public RestResponse listFirmware (@Valid ListFirmwareCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.listFirmware(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/addFirmware</b>
     * <p>新建固件（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("addFirmware")
    @RestReturn(value=FirmwareNewDTO.class)
    public RestResponse addFirmware (@Valid AddFirmwareCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.addFirmware(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/deleteFirmware</b>
     * <p>删除固件（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("deleteFirmware")
    @RestReturn(value=FirmwareNewDTO.class)
    public RestResponse deleteFirmware (@Valid DeleteFirmwareCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.deleteFirmware(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/listFirmwarePackage</b>
     * <p>列出固件包（左邻后台）</p>
     * @return 固件包列表
     */
    @RequestMapping("listFirmwarePackage")
    @RestReturn(value=ListFirmwarePackageResponse.class)
    public RestResponse listFirmwarePackage (@Valid ListFirmwarePackageCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.listFirmwarePackage(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/deleteFirmwarePackage</b>
     * <p>删除固件程序（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("deleteFirmwarePackage")
    @RestReturn(value=FirmwarePackageDTO.class)
    public RestResponse deleteFirmwarePackage (@Valid DeleteFirmwarePackageCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.deleteFirmwarePackage(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/uploadFirmwarePackage</b>
     * <p>上传固件程序（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("uploadFirmwarePackage")
    @RestReturn(value=FirmwarePackageDTO.class)
    public RestResponse uploadWifi(@Valid UploadFirmwarePackageCommand cmd){
        RestResponse response = new RestResponse(doorAccessService.uploadFirmwarePackage(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/downloadBluetooth</b>
     * <p>下载蓝牙程序（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("downloadBluetooth")
    @RestReturn(value=String.class)
    public RestResponse downloadBluetooth(@Valid DownloadBluetoothCommand cmd){
        doorAccessService.downloadBluetooth(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/downloadWifi</b>
     * <p>下载wifi程序（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("downloadWifi")
    @RestReturn(value=String.class)
    public RestResponse downloadWifi(@Valid DownloadBluetoothCommand cmd){
        doorAccessService.downloadWifi(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/aclink/deleteBluetooth</b>
     * <p>删除蓝牙程序（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("deleteBluetooth")
    @RestReturn(value=String.class)
    public RestResponse deleteBluetooth (@Valid DeleteBluetoothCommand cmd){
        doorAccessService.deleteBluetooth(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//add by liqingyan
    /**
     * <b>URL: /admin/aclink/doorStatisticByTime</b>
     * <p>门禁时间统计</p>
     * @return 门禁列表
     */
    @RequestMapping("doorStatisticByTime")
    @RestReturn(value=DoorStatisticByTimeResponse.class)
    public RestResponse doorStatisticByTime(@Valid DoorStatisticByTimeCommand cmd) {
        DoorStatisticByTimeResponse obj = doorAccessService.doorStatisticByTime(cmd);
        RestResponse response = new RestResponse(obj);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/deleteWifi</b>
     * <p>删除Wifi程序（左邻后台）</p>
     * @return OK 成功
     */
    @RequestMapping("deleteWifi")
    @RestReturn(value=String.class)
    public RestResponse deleteWifi (@Valid DeleteBluetoothCommand cmd){
        doorAccessService.deleteWifi(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/tempStatisticByTime</b>
     * <p>临时授权时间统计</p>
     * @return 门禁列表
     */
    @RequestMapping("tempStatisticByTime")
    @RestReturn(value=TempStatisticByTimeResponse.class)
    public RestResponse TempStatisticByTime(@Valid TempStatisticByTimeCommand cmd) {
        TempStatisticByTimeResponse obj = doorAccessService.tempStatisticByTime(cmd);
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
    
    /**
     * <b>URL: /admin/aclink/generateMarchUUID</b>
     * <p>生成唯一配对码  门禁3.0已经不用该接口</p>
     * @return
     */
    @RequestMapping("generateMarchUUID")
    @RestReturn(value=CreateMarchUUIDResponse.class)
    public RestResponse createMarchUUID(CreateMarchUUIDCommand cmd){
    	RestResponse response = new RestResponse(aclinkServerService.generateUUID(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/listLocalServers</b>
     * <p>获取本地服务器列表 </p>
     * @return
     */
    @RequestMapping("listLocalServers")
    @RestReturn(value=ListAclinkServersResponse.class)
    public RestResponse listLocalServers(AclinkListLocalServersCommand cmd){
    	RestResponse response = new RestResponse(aclinkServerService.listLocalServers(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/queryDoorAccessByServer</b>
     * <p>根据本地服务器id查询门禁列表 </p>
     * @return
     */
    @RequestMapping("queryDoorAccessByServer")
    @RestReturn(value=QueryDoorAccessByServerResponse.class)
    public RestResponse getDoorAccessByServer(QueryDoorAccessByServerCommand cmd){
    	RestResponse response = new RestResponse(doorAccessService.listDoorAccessByServerId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/queryServerRelations</b>
     * <p>根据本地服务器及门禁查询关联设备</p>
     * @return
     */
    @RequestMapping("queryServerRelations")
    @RestReturn(value=QueryServerRelationsResponse.class)
    public RestResponse getDevicesByServer(QueryServerRelationsCommand cmd){
    	RestResponse response = new RestResponse(aclinkServerService.queryServerRelations(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/createLocalServers</b>
     * <p>新增内网服务器</p>
     */
    @RequestMapping("createLocalServers")
    @RestReturn(value=String.class)
    public RestResponse addLocalServers(CreateLocalServersCommand cmd){
    	aclinkServerService.createLocalServer(cmd);
    	//TODO 异常处理   名称是否可重复？
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/updateLocalServers</b>
     * <p>修改内网服务器</p>
     */
    @RequestMapping("updateLocalServers")
    @RestReturn(value=String.class)
    public RestResponse updateLocalServers(UpdateLocalServersCommand cmd){
    	//名称是否可重复？
    	aclinkServerService.updateLocalServer(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/deleteLocalServers</b>
     * <p>删除内网服务器</p>
     */
    @RequestMapping("deleteLocalServers")
    @RestReturn(value=String.class)
    public RestResponse deleteLocalServers(DeleteLocalServerCommand cmd){
    	aclinkServerService.deleteLocalServer(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/updateCameraIpadBatch</b>
     * <p>批量更新摄像头及ipad关联关系 </p>
     * @return
     */
    @RequestMapping("updateCameraIpadBatch")
    @RestReturn(value=String.class)
    public RestResponse updateCameraIpadBatch(UpdateCameraIpadBatchCommand cmd){
    	aclinkServerService.updateCameraIpadBatch(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /admin/aclink/listLocalCameras</b>
     * <p>获取内网摄像头列表 </p>
     * @return
     */
    @RequestMapping("listLocalCameras")
    @RestReturn(value=ListLocalCamerasResponse.class)
    public RestResponse listLocalCameras(ListLocalCamerasCommand cmd){
    	CrossShardListingLocator locator = new CrossShardListingLocator();
    	locator.setAnchor(cmd.getPageAnchor());
    	RestResponse response = new RestResponse(aclinkCameraService.listLocalCameras(ConvertHelper.convert(cmd, ListLocalCamerasCommand.class)));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/queryLocalCameras</b>
     * <p>查找内网摄像头列表 </p>
     * @return
     */
    @RequestMapping("queryLocalCameras")
    @RestReturn(value=ListAclinkServersResponse.class)
    public RestResponse queryLocalCameras(QueryAclinkCamerasCommand cmd){
    	CrossShardListingLocator locator = new CrossShardListingLocator();
    	locator.setAnchor(cmd.getPageAnchor());
    	RestResponse response = new RestResponse(aclinkCameraService.listLocalCameras(ConvertHelper.convert(cmd, ListLocalCamerasCommand.class)));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/createLocalCameras</b>
     * <p>新增内网摄像头</p>
     */
    @RequestMapping("createLocalCameras")
    @RestReturn(value=String.class)
    public RestResponse addLocalCameras(CreateLocalCamerasCommand cmd){
    	aclinkCameraService.createLocalCamera(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/updateLocalCameras</b>
     * <p>修改内网摄像头</p>
     */
    @RequestMapping("updateLocalCameras")
    @RestReturn(value=String.class)
    public RestResponse updateLocalCameras(UpdateLocalCamerasCommand cmd){
    	aclinkCameraService.updateLocalCamera(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/deleteLocalCameras</b>
     * <p>删除内网摄像头</p>
     */
    @RequestMapping("deleteLocalCameras")
    @RestReturn(value=String.class)
    public RestResponse deleteLocalCameras(DeleteLocalCamerasCommand cmd){
    	aclinkCameraService.deleteLocalCameras(cmd.getId());
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/listLocalIpad</b>
     * <p>获取内网ipad列表 </p>
     * @return
     */
    @RequestMapping("listLocalIpad")
    @RestReturn(value=ListLocalIpadResponse.class)
    public RestResponse listLocalIpad(ListLocalIpadCommand cmd){
    	CrossShardListingLocator locator = new CrossShardListingLocator();
    	locator.setAnchor(cmd.getPageAnchor());
    	RestResponse response = new RestResponse(aclinkIpadService.listLocalIpads(locator,cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/queryLocalIpad</b>
     * <p>查找内网ipad列表 </p>
     * @return
     */
    @RequestMapping("queryLocalIpad")
    @RestReturn(value=ListLocalIpadResponse.class)
    public RestResponse queryLocalIpads(QueryAclinkIpadCommand cmd){
    	CrossShardListingLocator locator = new CrossShardListingLocator();
    	locator.setAnchor(cmd.getPageAnchor());
    	RestResponse response = new RestResponse(aclinkIpadService.listLocalIpads(locator,ConvertHelper.convert(cmd, ListLocalIpadCommand.class)));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/createLocalIpad</b>
     * <p>新增内网ipad</p>
     */
    @RequestMapping("createLocalIpad")
    @RestReturn(value=CreateLocalIpadResponse.class)
    public RestResponse addLocalIpad(CreateLocalIpadCommand cmd){
    	CreateMarchUUIDCommand cmd1 = new CreateMarchUUIDCommand();
    	cmd1.setUuidType((byte) 1);
    	cmd.setUuid(aclinkServerService.generateUUID(cmd1));
    	RestResponse response = new RestResponse(aclinkIpadService.createLocalIpad(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/updateLocalIpad</b>
     * <p>修改内网ipad</p>
     */
    @RequestMapping("updateLocalIpad")
    @RestReturn(value=String.class)
    public RestResponse updateLocalIpad(UpdateLocalIpadCommand cmd){
    	aclinkIpadService.updateLocalIpad(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/setIpadLogo</b>
     * <p>删除内网ipad</p>
     */
    //TODO
//    @RequestMapping("setIpadLogo")
//    @RestReturn(value=String.class)
//    public RestResponse setIpadLogo(SetIpadLogoCommand cmd){
//    	aclinkIpadService.deleteLocalIpad(cmd.getId());
//    	RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
    
    /**
     * <b>URL: /admin/aclink/deleteLocalIpad</b>
     * <p>删除内网ipad</p>
     */
    @RequestMapping("deleteLocalIpad")
    @RestReturn(value=String.class)
    public RestResponse deleteLocalIpad(DeleteLocalIpadCommand cmd){
    	aclinkIpadService.deleteLocalIpad(cmd.getId());
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/syncLocalServer</b>
     * <p>从云同步内网服务器</p>
     */
    @RequestMapping("syncLocalServer")
    @RestReturn(value=SyncLocalServerResponse.class)
    public RestResponse syncLocalServer(SyncLocalServerCommand cmd){
    	aclinkServerService.syncLocalServer(cmd.getId());
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/aclink/pairLocalServer</b>
     * <p> 内网服务器配对</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("pairLocalServer")
    @RestReturn(value=PairLocalServerResponse.class)
    public RestResponse pairLocalServer(PairLocalServerCommand cmd){
    	RestResponse response = new RestResponse(aclinkServerService.pairLocalServer(cmd.getUuid(), cmd.getIpAddress(), cmd.getVersion()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/aclink/updateServerSyncTime</b>
     * <p> 内网服务器同步成功后更新同步时间</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("updateServerSyncTime")
    @RestReturn(value=String.class)
    public RestResponse updateServerSyncTime(UpdateServerSyncTimeCommand cmd){
    	aclinkServerService.updateServerSyncTime(cmd.getServerId());
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/syncLocalPhotoByUserId</b>
     * <p>通知内网服务器拉取人脸识别照片 </p>
     * @return
     */
    @RequestMapping("syncLocalPhotoByUserId")
    @RestReturn(value=String.class)
    public RestResponse listFacialRecognitionPhotoByUser(SyncLocalPhotoByUserIdCommand cmd){
    	//同步单个用户,根据ownerId查内网服务器uuid,通过borderServer通知内网服务器,由内网服务器根据userId拉取数据
    	RestResponse response = new RestResponse();
    	faceRecognitionPhotoService.syncLocalPhotoByUserId(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/syncLocalUserData</b>
     * <p>同步人脸识别照片及授权(注册用户) </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("syncLocalUserData")
    @RestReturn(value=SyncLocalUserDataResponse.class)
    public RestResponse syncLocalUserData(SyncLocalUserDataCommand cmd){
    	//同步单个用户,根据ownerId查内网服务器uuid,通过borderServer通知内网服务器,由内网服务器根据userId拉取数据
    	RestResponse response = new RestResponse(faceRecognitionPhotoService.syncLocalUserData(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /admin/aclink/syncLocalVistorData</b>
     * <p>同步人脸识别照片及授权(访客) </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("syncLocalVistorData")
    @RestReturn(value=SyncLocalVistorDataResponse.class)
    public RestResponse syncLocalVistorData(SyncLocalVistorDataCommand cmd){
    	//同步访客信息
    	RestResponse response = new RestResponse(faceRecognitionPhotoService.syncLocalVistorData(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/updateUserSyncTime</b>
     * <p>更新用户同步时间 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("updateUserSyncTime")
    @RestReturn(value=String.class)
    public RestResponse updateUserSyncTime(UpdateUserSyncTimeCommand cmd){
    	faceRecognitionPhotoService.updateUserSyncTime(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/updateVistorSyncTime</b>
     * <p>更新访客同步时间 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("updateVistorSyncTime")
    @RestReturn(value=String.class)
    public RestResponse updateVistorSyncTime(UpdateVistorSyncTimeCommand cmd){
    	faceRecognitionPhotoService.updateVistorSyncTimes(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/updateVistorSyncState</b>
     * <p>更新访客同步时间 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("updateVistorSyncState")
    @RestReturn(value=String.class)
    public RestResponse updateVistorSyncState(UpdateVistorSyncTimeCommand cmd){
    	faceRecognitionPhotoService.invalidVistorSyncState(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
//createLocalVisitorAuth Test
    /**
     * 
     * <b>URL: /admin/aclink/createLocalVisitorAuth</b>
     * <p>更新用户同步时间 </p>
     * @return
     */
    @RequestMapping("createLocalVisitorAuth")
    @RestReturn(value=String.class)
    public RestResponse createLocalVisitorAuth(CreateLocalVistorCommand cmd){
    	RestResponse response = new RestResponse();
    	doorAccessService.createLocalVisitorAuth(cmd);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /admin/aclink/createVisitorBatch</b>
     * <p>批量授权访客 </p>
     * @return
     */
    @RequestMapping("createVisitorBatch")
    @RestReturn(value=String.class)
    public RestResponse createVisitorBatch(CreateVisitorBatchCommand cmd){
    	RestResponse response = new RestResponse();
    	doorAccessService.createVisitorBatch(cmd);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    //20180914 add by liqingyan
    /**
     * <b>URL: /admin/aclink/checkMobilePrivilege</b>
     * <p>移动端管理权限检验</p>
     * @return
     */
    @RequestMapping("checkMobilePrivilege")
    @RestReturn(value=CheckMobilePrivilegeResponse.class)
    public RestResponse checkMobilePrivilege(@Valid CheckMobilePrivilegeCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.checkMobilePrivilege(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
}
