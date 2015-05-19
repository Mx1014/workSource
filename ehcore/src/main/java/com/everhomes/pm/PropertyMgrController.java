// @formatter:off
package com.everhomes.pm;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.address.ApartmentDTO;
import com.everhomes.address.BuildingDTO;
import com.everhomes.address.ListApartmentByKeywordCommand;
import com.everhomes.address.ListBuildingByKeywordCommand;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.family.FamilyDTO;
import com.everhomes.family.FamilyMemberDTO;
import com.everhomes.family.FindFamilyByAddressIdCommand;
import com.everhomes.family.ListOwningFamilyMembersCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.server.schema.tables.records.EhCommunityPmMembersRecord;
import com.everhomes.user.SetCurrentCommunityCommand;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;

@RestController
@RequestMapping("/pm")
public class PropertyMgrController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrController.class);
    
	@Autowired
	PropertyMgrService propertyMgrService;
	
	@Autowired
	PropertyMgrProvider propertyMgrProvider;
	
	 /**
     * <b>URL: /pm/getUserOwningProperties</b>
     * <p>查询用户加入的物业</p>
     */
    @RequestMapping("getUserOwningProperties")
    @RestReturn(value=ListPropMemberCommandResponse.class)
    public RestResponse getUserOwningProperties() {
        ListPropMemberCommandResponse commandResponse = propertyMgrService.getUserOwningProperties();
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
	/**
	 * <b>URL: /pm/listPMGroupMembers</b>
     * <p>查询物业成员列表</p>
     */
    @RequestMapping("listPMGroupMembers")
    @RestReturn(value=ListPropMemberCommandResponse.class, collection=true)
    public RestResponse listPropertyMembers(@Valid ListPropMemberCommand cmd) {
    	ListPropMemberCommandResponse commandResponse = propertyMgrService.listCommunityPmMembers(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/addPropertyMemberByPhone</b>
     * <p>通过手机添加物业成员</p>
     * @return 添加的结果
     */
    @RequestMapping("addPMGroupMemberByPhone")
    @RestReturn(value=String.class)
    public RestResponse addPropertyMemberByPhone(@Valid CreatePropMemberCommand cmd) {
    	
    	propertyMgrService.createPropMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/approvePropertyMember</b>
     * <p>批准物业成员</p>
     * @return 批准的结果
     */
    @RequestMapping("approvePropertyMember")
    @RestReturn(value=String.class)
    public RestResponse approvePropertyMember(@Valid CommunityPropMemberCommand cmd) {
    	propertyMgrService.approvePropMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/rejectMember</b>
     * <p>拒绝物业成员</p>
     * @return 拒绝的结果
     */
    @RequestMapping("rejectPropertyMember")
    @RestReturn(value=String.class)
    public RestResponse rejectPropertyMember(@Valid CommunityPropMemberCommand cmd) {
    	propertyMgrService.rejectPropMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/revokePMGroupMember</b>
     * <p>删除物业成员</p>
     * @return 删除的结果
     */
    @RequestMapping("revokePMGroupMember")
    @RestReturn(value=String.class)
    public RestResponse revokePMGroupMember(@Valid DeletePropMemberCommand cmd) {
    	propertyMgrService.revokePMGroupMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/listApartmentMappings</b>
     * <p>列出公寓门牌号映射表（左邻系统和物业自有系统的门牌号映射）</p>
     */
    @RequestMapping("listPMAddressMapping")
    @RestReturn(value=ListPropAddressMappingCommandResponse.class, collection=true)
    public RestResponse listApartmentMappings(@Valid ListPropAddressMappingCommand cmd) {
    	ListPropAddressMappingCommandResponse commandResponse = propertyMgrService.ListAddressMappings(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/importPMAddressMapping</b>
     * <p>导入左邻系统和物业自有系统的门牌号映射</p>
     * @return 导入的结果
     */
    @RequestMapping("importPMAddressMapping")
    @RestReturn(value=String.class)
    public RestResponse importPMAddressMapping(@Valid PropCommunityIdCommand cmd) {
    	propertyMgrService.importPMAddressMapping(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/getPMAddressMapping</b>
     * <p>列出公寓门牌号映射表（左邻系统和物业自有系统的门牌号映射）</p>
     */
    @RequestMapping("getPMAddressMapping")
    @RestReturn(value=ListPropAddressMappingCommandResponse.class, collection=true)
    public RestResponse getPMAddressMapping(
    		@Valid ListPropAddressMappingCommand cmd) {
    	ListPropAddressMappingCommandResponse commandResponse = propertyMgrService.ListAddressMappings(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/updatePMAddressMapping</b>
     * <p>修改公寓门牌号映射表</p>
     * @return 修改的结果
     */
    @RequestMapping("updatePMAddressMapping")
    @RestReturn(value=String.class)
    public RestResponse updatePMAddressMapping(
    	@Valid UpdatePropAddressMappingCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/importPMPropertyOwnerInfo</b>
     * @param communityId 小区id
     * @param files 上传的文件
     * @return 上传的结果
     */
    @RequestMapping(value="importPMPropertyOwnerInfo", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse importPMPropertyOwnerInfo(@Valid PropCommunityIdCommand cmd,
    	@RequestParam(value = "attachment") MultipartFile[] files) {
    	propertyMgrService.importPMPropertyOwnerInfo(cmd,files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/ListPropOwnerCommandResponse</b>
     * <p>列出业主信息表</p>
     */
    @RequestMapping("listPMPropertyOwnerInfo")
    @RestReturn(value=ListPropOwnerCommandResponse.class, collection=true)
    public RestResponse listPMPropertyOwnerInfo(@Valid ListPropOwnerCommand cmd) {
    	ListPropOwnerCommandResponse commandResponse = propertyMgrService.listPMPropertyOwnerInfo(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/updatePMPropertyOwnerInfo</b>
     * <p>修改业主信息表</p>
     * @return 修改的结果
     */
    @RequestMapping("updatePMPropertyOwnerInfo")
    @RestReturn(value=String.class)
    public RestResponse updatePMPropertyOwnerInfo(
    	@Valid UpdatePropOwnerCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/deletePMPropertyOwnerInfo</b>
     * @return 删除的结果
     */
    @RequestMapping("deletePMPropertyOwnerInfo")
    @RestReturn(value=String.class)
    public RestResponse deletePMPropertyOwnerInfo(@Valid DeletePropOwnerCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/setApartmentStatus</b>
     * <p>设置公寓状态：自住/出租/空闲/装修/待售/其它</p>
     * @return 设置的结果
     */
    @RequestMapping("setAddressPMStatus")
    @RestReturn(value=String.class)
    public RestResponse setApartmentStatus(@Valid SetPropAddressStatusCommand cmd) {
    	propertyMgrService.setApartmentStatus(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/getApartmentStatistics</b>
     * <p>小区公寓统计信息：公寓统计（总数、入住公寓数量、入住用户数量、自住/出租/空闲/装修/待售/其它等数量）</p>
     */
    @RequestMapping("getApartmentStatistics")
    @RestReturn(value=PropAptStatisticDTO.class)
    public RestResponse getApartmentStatistics(@Valid PropCommunityIdCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/assignPMTopics</b>
     * <p>把物业维修帖指派给处理人员（可批量指派）</p>
     * @return 分配的结果
     */
    @RequestMapping("assignPMTopics")
    @RestReturn(value=String.class)
    public RestResponse assignPMTopics(@Valid AssginPmTopicCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/setPMTopicStatus</b>
     * <p>设置物业维修帖状态：未处理、处理中、已处理、其它（可批量设置）</p>
     * @return 设置的结果
     */
    @RequestMapping("setPMTopicStatus")
    @RestReturn(value=String.class)
    public RestResponse setPMTopicStatus(@Valid SetPmTopicStatusCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/getPMTopicStatistics</b>
     * <p>根据时间和状态获取物业维修帖统计信息：未处理、处理中、已处理、其它</p>
     */
    @RequestMapping("getPMTopicStatistics")
    @RestReturn(value=ListPropTopicStatisticCommandResponse.class, collection=true)
    public RestResponse getPMTopicStatistics(
    	@Valid ListPropTopicStatisticCommand  cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendMsgToPMGroup</b>
	 * 发消息给物业组（含所有成员）
	 */
	@RequestMapping("sendMsgToPMGroup")
	@RestReturn(value=String.class)
    public RestResponse sendMsgToPMGroup(@Valid PropCommunityIdCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendNoticeToCommunity</b>
	 * 发通知给整个小区业主（是左邻用户则发消息、不是左邻用户则发短信）
	 * @return 发通知的结果
	 */
	@RequestMapping("sendNoticeToCommunity")
	@RestReturn(value=String.class)
    public RestResponse sendNoticeToCommunity(@Valid PropCommunityIdCommand cmd)  {
		
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendNoticeToFloor</b>
	 * 发通知给整层楼业主（是左邻用户则发消息、不是左邻用户则发短信）
     * @return 
     */
	@RequestMapping("sendNoticeToFloor")
	@RestReturn(value=String.class)
    public RestResponse sendNoticeToFloor(@Valid PropCommunityBuildCommand cmd) {
		
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendNoticeToFamily</b>
	 * 发通知给指定门牌号（是左邻用户则发消息、不是左邻用户则发短信）
     * @return 发通知的结果
     */
	@RequestMapping("sendNoticeToAddress")
	@RestReturn(value=String.class)
    public RestResponse sendNoticeToFamily(@Valid PropCommunityBuildAddessCommand cmd) {
		
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * <b>URL: /pm/importPropertyBills</b>
     * 上传缴费账单excel文件
     * @param communityId 小区id
     * @param files 要上传文件
     * @return 
     */
    @RequestMapping(value="importPropertyBills", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse importPropertyBills(@Valid PropCommunityIdCommand cmd,
    	@RequestParam(value = "attachment") MultipartFile[] files) {
    	
    	propertyMgrService.importPropertyBills(cmd, files);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/listPropertyBill</b>
	 * 查询缴费账单详情
	 */
    @RequestMapping("listPropertyBill")
    @RestReturn(value=ListPropBillCommandResponse.class, collection=true)
    public RestResponse listPropertyBill(
    	@Valid ListPropBillCommand cmd) {
    	ListPropBillCommandResponse commandResponse = propertyMgrService.listPropertyBill(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	

	/**
	 * <b>URL: /pm/deletePropertyBillByMonth</b>
	 * 删除指定月份的缴费账单
     * @return 删除的结果
     */
    @RequestMapping("deletePropertyBillByMonth")
    @RestReturn(value=String.class)
    public RestResponse deletePropertyBillByMonth(@Valid PropCommunityBillDateStrCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/deletePropertyBill</b>
	 * 删除缴费账单（可批量删除）
     * @return 删除的结果
     */
    @RequestMapping("deletePropertyBill")
    @RestReturn(value=String.class)
    public RestResponse deletePropertyBill(@Valid PropCommunityBillIdCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendPropertyBillsByMonth</b>
	 * 批量发指定月份的缴费账单
     * @return 发送物业缴费通知的结果
     */
    @RequestMapping("sendPropertyBillsByMonth")
    @RestReturn(value=String.class)
    public RestResponse sendPropertyBillsByMonth(@Valid PropCommunityBillDateStrCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendPropertyBill</b>
	 * 发指定的单个缴费账单
     * @return 发送物业缴费通知的结果
     */
    @RequestMapping("sendPropertyBill")
    @RestReturn(value=String.class)
    public RestResponse sendPropertyBill(@Valid PropCommunityBillIdCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/listInvitedUsers</b>
	 * 查询邀请加入左邻的用户列表
	 */
    @RequestMapping("listInvitedUsers")
    @RestReturn(value=ListPropInvitedUserCommandResponse.class, collection=true)
    public RestResponse listInvitedUsers(@Valid ListPropInvitedUserCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/searchInvitedUsers</b>
	 * 搜索邀请加入左邻的用户
	 */
    @RequestMapping("searchInvitedUsers")
    @RestReturn(value=ListPropInvitedUserCommandResponse.class, collection=true)
    public RestResponse searchInvitedUsers(
    	@Valid ListPropInvitedUserCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/approvePropFamilyMember</b>
     * <p>批准家庭成员</p>
     * @return 批准的结果
     */
    @RequestMapping("approvePropFamilyMember")
    @RestReturn(value=String.class)
    public RestResponse approvePropFamilyMember(@Valid CommunityPropFamilyMemberCommand cmd) {
    	propertyMgrService.approvePropFamilyMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/ejectPropFamilyMember</b>
     * <p>拒绝家庭成员</p>
     * @return 拒绝的结果
     */
    @RequestMapping("rejectPropFamilyMember")
    @RestReturn(value=String.class)
    public RestResponse rejectPropFamilyMember(@Valid CommunityPropFamilyMemberCommand cmd) {
    	propertyMgrService.rejectPropFamilyMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/revokePropFamilyMember</b>
     * <p>踢出家庭成员</p>
     * @return 踢出的结果
     */
    @RequestMapping("revokePropFamilyMember")
    @RestReturn(value=String.class)
    public RestResponse revokePropFamilyMember(@Valid CommunityPropFamilyMemberCommand cmd) {
    	propertyMgrService.revokePropFamilyMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/listPropBuildingsByKeyword</b>
     * <p>根据小区Id和关键字查询小区楼栋(物业)</p>
     */
    @RequestMapping("listPropBuildingsByKeyword")
    @RestReturn(value=BuildingDTO.class, collection=true)
    public RestResponse listPropBuildingsByKeyword(@Valid ListBuildingByKeywordCommand cmd) {
        Tuple<Integer, List<BuildingDTO>> results = propertyMgrService.listPropBuildingsByKeyword(cmd);
        RestResponse response = new RestResponse(results.second());
        
        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/listPropAppartmentsByKeyword</b>
     * <p>根据小区Id、楼栋号和关键字查询门牌(物业)</p>
     */
    @RequestMapping("listPropAppartmentsByKeyword")
    @RestReturn(value=PropFamilyDTO.class, collection=true)
    public RestResponse listPropApartmentsByKeyword(@Valid ListApartmentByKeywordCommand cmd) {
        List<PropFamilyDTO> results =  propertyMgrService.listPropApartmentsByKeyword(cmd);
        RestResponse response = new RestResponse(results);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/findUserByIndentifier</b>
     * <p>根据用户token查询用户信息</p>
     */
    @RequestMapping("findUserByIndentifier")
    @RestReturn(value=UserTokenCommandResponse.class, collection=true)
    public RestResponse findUserByIndentifier(@Valid UserTokenCommand cmd) {
        UserTokenCommandResponse commandResponse = propertyMgrService.findUserByIndentifier(cmd);
        RestResponse response = new RestResponse(commandResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/setPropCurrentCommunity</b>
     * <p>设置物业用户当前小区（切换物业）</p>
     * @param communityId 小区ID
     * @return OK
     */
    @RequestMapping("setPropCurrentCommunity")
    @RestReturn(String.class)
    public RestResponse setPropCurrentCommunity(@Valid SetCurrentCommunityCommand cmd) throws Exception {
    	propertyMgrService.setPropCurrentCommunity(cmd);
        RestResponse response = new RestResponse();
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/listPropFamilyWaitingMember</b>
     * <p>查询小区的待审核家庭成员列表</p>
     * @param communityId 小区ID
     */
    @RequestMapping("listPropFamilyWaitingMember")
    @RestReturn(value=ListPropInvitedUserCommandResponse.class, collection=true)
    public RestResponse listPropFamilyWaitingMember(@Valid ListPropFamilyWaitingMemberCommand cmd) throws Exception {
    	ListPropFamilyWaitingMemberCommandResponse commandResponse =  propertyMgrService.listPropFamilyWaitingMember(cmd);
        RestResponse response = new RestResponse(commandResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/findFamilyByAddressId</b>
     * <p>根据addressId查询family</p>
     * @param communityId 小区ID
     */
    @RequestMapping("findFamilyByAddressId")
    @RestReturn(value=PropFamilyDTO.class)
    public RestResponse findFamilyByAddressId(@Valid ListPropCommunityAddressCommand cmd) throws Exception {
    	PropFamilyDTO commandResponse =  propertyMgrService.findFamilyByAddressId(cmd);
        RestResponse response = new RestResponse(commandResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
   
    /**
     * <b>URL: /pm/listFamilyMembersByFamilyId</b>
     * <p>查询家庭的成员列表</p>
     */
    @RequestMapping("listFamilyMembersByFamilyId")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listFamilyMembersByFamilyId(@Valid ListPropFamilyMemberCommand cmd) {
        List<FamilyMemberDTO> results = propertyMgrService.listFamilyMembersByFamilyId(cmd);
        RestResponse response = new RestResponse(results);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/listPropBillDateStr</b>
     * <p>查询物业账单的时间列表</p>
     */
    @RequestMapping("listPropBillDateStr")
    @RestReturn(value=String.class, collection=true)
    public RestResponse listPropBillDateStr(@Valid PropCommunityIdCommand cmd) {
        List<String> results = propertyMgrProvider.listPropBillDateStr(cmd.getCommunityId());
        RestResponse response = new RestResponse(results);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
