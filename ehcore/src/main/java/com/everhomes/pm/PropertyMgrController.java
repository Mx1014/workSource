// @formatter:off
package com.everhomes.pm;

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
import com.everhomes.pkg.ClientPackageFileDTO;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/pm")
public class PropertyMgrController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrController.class);
    
	@Autowired
	PropertyMgrService propertyMgrService;
	
	/**
     * 查询物业成员列表
     */
    @RequestMapping("listPMGroupMembers")
    @RestReturn(value=Long.class)
    public RestResponse listPropertyMembers( 
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 通过手机添加物业成员
     */
    @RequestMapping("addPMGroupMemberByPhone")
    @RestReturn(value=String.class)
    public RestResponse addPropertyMemberByPhone(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "userType", required = true) Integer userType,
    	@RequestParam(value = "userToken", required = true) String userToken) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 删除物业成员
     */
    @RequestMapping("deletePMGroupMember")
    @RestReturn(value=String.class)
    public RestResponse deletePropertyMember(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "memberId", required = true) Long memberId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 列出公寓门牌号映射表（左邻系统和物业自有系统的门牌号映射）
     */
    @RequestMapping("listPMAddressMapping")
    @RestReturn(value=Long.class)
    public RestResponse listApartmentMappings(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 导入左邻系统和物业自有系统的门牌号映射
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
     * 列出公寓门牌号映射表（左邻系统和物业自有系统的门牌号映射）
     */
    @RequestMapping("getPMAddressMapping")
    @RestReturn(value=Long.class)
    public RestResponse getPMAddressMapping(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 修改公寓门牌号映射表
     */
    @RequestMapping("updatePMAddressMapping")
    @RestReturn(value=String.class)
    public RestResponse updatePMAddressMapping(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "updatePropAddressMappingCommand", required = true) UpdatePropAddressMappingCommand updatePropAddressMappingCommand) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 导入业主信息表
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
     * 列出业主信息表
     */
    @RequestMapping("getPMPropertyOwnerInfo")
    @RestReturn(value=Long.class)
    public RestResponse getPMPropertyOwnerInfo(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 修改业主信息表
     */
    @RequestMapping("updatePMPropertyOwnerInfo")
    @RestReturn(value=String.class)
    public RestResponse updatePMPropertyOwnerInfo(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "updatePropOwnerCommand", required = true) UpdatePropOwnerCommand updatePropOwnerCommand) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 删除业主信息
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
     * 设置公寓状态：自住/出租/空闲/装修/待售/其它
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
     * 小区公寓统计信息：公寓统计（总数、入住公寓数量、入住用户数量、自住/出租/空闲/装修/待售/其它等数量）
     */
    @RequestMapping("getApartmentStatistics")
    @RestReturn(value=Long.class)
    public RestResponse getApartmentStatistics(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 把物业维修帖指派给处理人员（可批量指派）
     */
    @RequestMapping("assignPMTopics")
    @RestReturn(value=String.class)
    public RestResponse assignPMTopics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 设置物业维修帖状态：未处理、处理中、已处理、其它（可批量设置）
     */
    @RequestMapping("setPMTopicStatus")
    @RestReturn(value=String.class)
    public RestResponse setPMTopicStatus() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 根据时间和状态获取物业维修帖统计信息：未处理、处理中、已处理、其它
     */
    @RequestMapping("getPMTopicStatistics")
    @RestReturn(value=Long.class)
    public RestResponse getPMTopicStatistics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
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
	 * 发通知给整层楼业主（是左邻用户则发消息、不是左邻用户则发短信）
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
	 * 发通知给指定门牌号（是左邻用户则发消息、不是左邻用户则发短信）
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
     * 上传缴费账单excel文件
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
	 * 查询缴费账单详情
	 */
    @RequestMapping("getPropertyBill")
    @RestReturn(value=Long.class)
    public RestResponse getPropertyBill(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 列出指定月份的缴费账单
	 */
    @RequestMapping("listPropertyBillByMonth")
    @RestReturn(value=Long.class)
    public RestResponse listPropertyBillByMonth(
    	@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "dateStr", required = true) String dateStr) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 列出指定时间范围的缴费账单
	 */
    @RequestMapping("listPropertyBillByTimeRange")
    @RestReturn(value=Long.class)
    public RestResponse listPropertyBillByTimeRange(
		@RequestParam(value = "communityId", required = true) Long communityId,
		@RequestParam(value = "startMonth", required = true) Long startMonth,
		@RequestParam(value = "endMonth", required = true) Long endMonth) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 删除指定月份的缴费账单
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
	 * 删除缴费账单（可批量删除）
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
	 * 批量发指定月份的缴费账单
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
	 * 发指定的单个缴费账单
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
	 * 查询邀请加入左邻的用户列表
	 */
    @RequestMapping("listInvitedUsers")
    @RestReturn(value=Long.class)
    public RestResponse listInvitedUsers(
    	@RequestParam(value = "communityId", required = true) Long communityId) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 搜索邀请加入左邻的用户
	 */
    @RequestMapping("searchInvitedUsers")
    @RestReturn(value=Long.class)
    public RestResponse searchInvitedUsers(
		@RequestParam(value = "communityId", required = true) Long communityId,
    	@RequestParam(value = "userType", required = true) Integer userType,
    	@RequestParam(value = "userToken", required = true) String userToken) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
