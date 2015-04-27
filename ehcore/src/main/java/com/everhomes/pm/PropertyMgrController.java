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
    public RestResponse listPropertyMembers() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 通过手机添加物业成员
     */
    @RequestMapping("addPMGroupMemberByPhone")
    @RestReturn(value=Long.class)
    public RestResponse addPropertyMemberByPhone() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 删除物业成员
     */
    @RequestMapping("deletePMGroupMember")
    @RestReturn(value=Long.class)
    public RestResponse deletePropertyMember() {
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
    public RestResponse listApartmentMappings() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 导入左邻系统和物业自有系统的门牌号映射
     */
    @RequestMapping("importPMAddressMapping")
    @RestReturn(value=Long.class)
    public RestResponse importPMAddressMapping() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("getPMAddressMapping")
    @RestReturn(value=Long.class)
    public RestResponse getPMAddressMapping() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("updatePMAddressMapping")
    @RestReturn(value=Long.class)
    public RestResponse updatePMAddressMapping() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("importPMPropertyOwnerInfo")
    @RestReturn(value=Long.class)
    public RestResponse importPMPropertyOwnerInfo() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("getPMPropertyOwnerInfo")
    @RestReturn(value=Long.class)
    public RestResponse getPMPropertyOwnerInfo() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("updatePMPropertyOwnerInfo")
    @RestReturn(value=Long.class)
    public RestResponse updatePMPropertyOwnerInfo() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("deletePMPropertyOwnerInfo")
    @RestReturn(value=Long.class)
    public RestResponse deletePMPropertyOwnerInfo() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 设置公寓状态：自住/出租/空闲/装修/待售/其它
     */
    @RequestMapping("setAddressPMStatus")
    @RestReturn(value=Long.class)
    public RestResponse setApartmentStatus() {
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
    public RestResponse getApartmentStatistics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 把物业维修帖指派给处理人员（可批量指派）
     */
    @RequestMapping("assignPMTopics")
    @RestReturn(value=Long.class)
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
    @RestReturn(value=Long.class)
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
    @RestReturn(value=Long.class)
    public RestResponse sendMsgToPMGroup() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 发通知给整个小区业主（是左邻用户则发消息、不是左邻用户则发短信）
	 */
	@RequestMapping("sendNoticeToCommunity")
    @RestReturn(value=Long.class)
    public RestResponse sendNoticeToCommunity() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 发通知给整层楼业主（是左邻用户则发消息、不是左邻用户则发短信）
	 */
	@RequestMapping("sendNoticeToFloor")
    @RestReturn(value=Long.class)
    public RestResponse sendNoticeToFloor() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 发通知给指定门牌号（是左邻用户则发消息、不是左邻用户则发短信）
	 */
	@RequestMapping("sendNoticeToAddress")
    @RestReturn(value=Long.class)
    public RestResponse sendNoticeToFamily() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * 上传缴费账单excel文件
     */
    @RequestMapping(value="uploadPropertyBills", method = RequestMethod.POST)
    @RestReturn(value=ClientPackageFileDTO.class)
    public RestResponse uploadPropertyBills(@RequestParam(value = "attachment") MultipartFile[] files) {
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
    public RestResponse getPropertyBill() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 列出指定月份的缴费账单
	 */
    @RequestMapping("listPropertyBillByMoth")
    @RestReturn(value=Long.class)
    public RestResponse listPropertyBillByMoth() {
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
    public RestResponse listPropertyBillByTimeRange() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 删除指定月份的缴费账单
	 */
    @RequestMapping("deletePropertyBillByMonth")
    @RestReturn(value=Long.class)
    public RestResponse deletePropertyBillByMonth() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 删除缴费账单（可批量删除）
	 */
    @RequestMapping("deletePropertyBill")
    @RestReturn(value=Long.class)
    public RestResponse deletePropertyBill() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 批量发指定月份的缴费账单
	 */
    @RequestMapping("sendPropertyBillsByMonth")
    @RestReturn(value=Long.class)
    public RestResponse sendPropertyBillsByMonth() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 发指定的单个缴费账单
	 */
    @RequestMapping("sendPropertyBill")
    @RestReturn(value=Long.class)
    public RestResponse sendPropertyBill() {
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
    public RestResponse listInvitedUsers() {
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
    public RestResponse searchInvitedUsers() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
