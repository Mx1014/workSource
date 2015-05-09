// @formatter:off
package com.everhomes.pm;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.server.schema.tables.records.EhCommunityPmMembersRecord;
import com.everhomes.util.ConvertHelper;

@RestController
@RequestMapping("/pm")
public class PropertyMgrController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrController.class);
    
	@Autowired
	PropertyMgrService propertyMgrService;
	
	@Autowired
	PropertyMgrProvider propertyMgrProvider;
	
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
     * @param communityId 小区id
     * @param userType 用户类型
     * @param userToken 用户标识
     * @return 
     */
    @RequestMapping("addPMGroupMemberByPhone")
    @RestReturn(value=String.class)
    public RestResponse addPropertyMemberByPhone(@Valid CreatePropMemberCommand cmd) {
    	CommunityPmMember communityPmMember = ConvertHelper.convert(cmd, CommunityPmMember.class);
    	propertyMgrProvider.createPropMember(communityPmMember);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/deletePMGroupMember</b>
     * <p>删除物业成员</p>
     * @param communityId 小区id
     * @param memberId 成员id
     * @return 
     */
    @RequestMapping("deletePMGroupMember")
    @RestReturn(value=String.class)
    public RestResponse deletePropertyMember(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "memberId", required = true) Long memberId) {
    	propertyMgrProvider.deletePropMember(memberId);
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
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/importPMAddressMapping</b>
     * <p>导入左邻系统和物业自有系统的门牌号映射</p>
     */
    @RequestMapping("importPMAddressMapping")
    @RestReturn(value=String.class)
    public RestResponse importPMAddressMapping(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
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
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/updatePMAddressMapping</b>
     * <p>修改公寓门牌号映射表</p>
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
     * @return 
     */
    @RequestMapping(value="importPMPropertyOwnerInfo", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse importPMPropertyOwnerInfo(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "attachment") MultipartFile[] files) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/getPMPropertyOwnerInfo</b>
     * <p>列出业主信息表</p>
     */
    @RequestMapping("getPMPropertyOwnerInfo")
    @RestReturn(value=ListPropOwnerCommandResponse.class, collection=true)
    public RestResponse getPMPropertyOwnerInfo(@Valid ListPropOwnerCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/updatePMPropertyOwnerInfo</b>
     * <p>修改业主信息表</p>
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
     * @param communityId 小区id
     * @param ownerId 业主id
     * @return 
     */
    @RequestMapping("deletePMPropertyOwnerInfo")
    @RestReturn(value=String.class)
    public RestResponse deletePMPropertyOwnerInfo(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "ownerId", required = true) Long ownerId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/setApartmentStatus</b>
     * <p>设置公寓状态：自住/出租/空闲/装修/待售/其它</p>
     * @param addressId 地址id
     * @param status 地址状态
     * @return 
     */
    @RequestMapping("setAddressPMStatus")
    @RestReturn(value=String.class)
    public RestResponse setApartmentStatus(
		@RequestParam(value = "addressId", required = true) Long addressId,
		@RequestParam(value = "status", required = true) Integer status) {
    	
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
    public RestResponse getApartmentStatistics(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pm/assignPMTopics</b>
     * <p>把物业维修帖指派给处理人员（可批量指派）</p>
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
    public RestResponse sendMsgToPMGroup(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendNoticeToCommunity</b>
	 * 发通知给整个小区业主（是左邻用户则发消息、不是左邻用户则发短信）
	 */
	@RequestMapping("sendNoticeToCommunity")
	@RestReturn(value=String.class)
    public RestResponse sendNoticeToCommunity(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
		
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendNoticeToFloor</b>
	 * 发通知给整层楼业主（是左邻用户则发消息、不是左邻用户则发短信）
	 * @param communityId 小区id
     * @param buildingName 楼栋名称
     * @return 
     */
	@RequestMapping("sendNoticeToFloor")
	@RestReturn(value=String.class)
    public RestResponse sendNoticeToFloor(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "buildingName", required = true) String buildingName) {
		
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendNoticeToFamily</b>
	 * 发通知给指定门牌号（是左邻用户则发消息、不是左邻用户则发短信）
	 * @param communityId 小区id
     * @param buildingName 楼栋名称
     * @param apartmentName 公寓名称
     * @return 
     */
	@RequestMapping("sendNoticeToAddress")
	@RestReturn(value=String.class)
    public RestResponse sendNoticeToFamily(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "buildingName", required = true) String buildingName,
    	@RequestParam(value = "apartmentName", required = true) String apartmentName) {
		
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * <b>URL: /pm/uploadPropertyBills</b>
     * 上传缴费账单excel文件
     * @param communityId 小区id
     * @param files 要上传文件
     * @return 
     */
    @RequestMapping(value="uploadPropertyBills", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse uploadPropertyBills(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "attachment") MultipartFile[] files) {
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/getPropertyBill</b>
	 * 查询缴费账单详情
	 */
    @RequestMapping("getPropertyBill")
    @RestReturn(value=ListPropBillCommandResponse.class, collection=true)
    public RestResponse getPropertyBill(
    	@Valid ListPropBillCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/listPropertyBillByMonth</b>
	 * 列出指定月份的缴费账单
	 */
    @RequestMapping("listPropertyBillByMonth")
    @RestReturn(value=ListPropBillCommandResponse.class, collection=true)
    public RestResponse listPropertyBillByMonth(@Valid ListPropBillCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/listPropertyBillByTimeRange</b>
	 * 列出指定时间范围的缴费账单
	 */
    @RequestMapping("listPropertyBillByTimeRange")
    @RestReturn(value=ListPropBillCommandResponse.class, collection=true)
    public RestResponse listPropertyBillByTimeRange(
    	@Valid ListPropBillCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/deletePropertyBillByMonth</b>
	 * 删除指定月份的缴费账单
	 * @param communityId 小区id
     * @param dateStr 账单月份
     * @return 
     */
    @RequestMapping("deletePropertyBillByMonth")
    @RestReturn(value=String.class)
    public RestResponse deletePropertyBillByMonth(
		@RequestParam(value = "communityId", required = true) Long communityId,
		@RequestParam(value = "dateStr", required = true) String dateStr) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/deletePropertyBill</b>
	 * 删除缴费账单（可批量删除）
	 * @param communityId 小区id
     * @param billId 账单id
     * @return 
     */
    @RequestMapping("deletePropertyBill")
    @RestReturn(value=String.class)
    public RestResponse deletePropertyBill(
		@RequestParam(value = "communityId", required = true) Long communityId,
		@RequestParam(value = "billId", required = true) Long billId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendPropertyBillsByMonth</b>
	 * 批量发指定月份的缴费账单
	 * @param communityId 小区id
     * @param dateStr 账单月份
     * @return 
     */
    @RequestMapping("sendPropertyBillsByMonth")
    @RestReturn(value=String.class)
    public RestResponse sendPropertyBillsByMonth(
		@RequestParam(value = "communityId", required = true) Long communityId,
		@RequestParam(value = "dateStr", required = true) String dateStr) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * <b>URL: /pm/sendPropertyBill</b>
	 * 发指定的单个缴费账单
	 * @param communityId 小区id
     * @param billId 账单id
     * @return 
     */
    @RequestMapping("sendPropertyBill")
    @RestReturn(value=String.class)
    public RestResponse sendPropertyBill(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "billId", required = true) Long billId) {
    	
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
}
