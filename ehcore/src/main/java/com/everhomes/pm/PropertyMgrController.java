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
	 * 发消息给物业组（含所有成员）
	 */
	@RequestMapping("sendMsgToPropertyGroup")
    @RestReturn(value=Long.class)
    public RestResponse sendMsgToPropertyGroup() {
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
	@RequestMapping("sendNoticeToFamily")
    @RestReturn(value=Long.class)
    public RestResponse sendNoticeToFamily() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 发社区公告（只有物业人员可以发）
	 */
    @RequestMapping("newBulletinTopic")
    @RestReturn(value=Long.class)
    public RestResponse newCommunityBulletinTopic() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 删除社区公告帖（可批量删除）
	 */
    @RequestMapping("deleteBulletinTopic")
    @RestReturn(value=Long.class)
    public RestResponse deleteCommunityBulletinTopic() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 设置物业维修帖状态：未处理、处理中、已处理、其它（可批量设置）
	 */
    @RequestMapping("setRepairTopicStatus")
    @RestReturn(value=Long.class)
    public RestResponse setCommunityRepairTopicStatus() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 把物业维修帖指派给处理人员（可批量指派）
	 */
    @RequestMapping("assignRepairTopics")
    @RestReturn(value=Long.class)
    public RestResponse assignCommunityRepairTopics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 根据时间和状态获取物业维修帖统计信息：未处理、处理中、已处理、其它
	 */
    @RequestMapping("getRepairTopicStatistics")
    @RestReturn(value=Long.class)
    public RestResponse getCommunityRepairTopicStatistics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
	/**
	 * 设置物业求助帖状态：未处理、处理中、已处理、其它（可批量设置）
	 */
    @RequestMapping("setHelpdeskTopicStatus")
    @RestReturn(value=Long.class)
    public RestResponse setCommunityHelpdeskTopicStatus() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 把物业求助帖指派给处理人员（可批量指派）
	 */
    @RequestMapping("assignHelpdeskTopics")
    @RestReturn(value=Long.class)
    public RestResponse assignCommunityHelpdeskTopics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 根据时间和状态获取物业求助帖统计信息：未处理、处理中、已处理、其它
	 */
    @RequestMapping("getHelpdeskTopicStatistics")
    @RestReturn(value=Long.class)
    public RestResponse getCommunityHelpdeskTopicStatistics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
	/**
	 * 设置物业建议帖状态：未处理、处理中、已处理、其它（可批量设置）
	 */
    @RequestMapping("setCommunitySuggestionTopicStatus")
    @RestReturn(value=Long.class)
    public RestResponse setCommunitySuggestionTopicStatus() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 把物业建议帖指派给处理人员（可批量指派）
	 */
    @RequestMapping("assignCommunitySuggestionTopics")
    @RestReturn(value=Long.class)
    public RestResponse assignCommunitySuggestionTopics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 根据时间和状态获取物业建议帖统计信息：未处理、处理中、已处理、其它
	 */
    @RequestMapping("getCommunitySuggestionTopicStatistics")
    @RestReturn(value=Long.class)
    public RestResponse getCommunitySuggestionTopicStatistics() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 列出公寓门牌号映射表（左邻系统和物业自有系统的门牌号映射）
	 */
    @RequestMapping("listApartmentMappings")
    @RestReturn(value=Long.class)
    public RestResponse listApartmentMappings() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 修改单个公寓门牌号映射（左邻系统和物业自有系统的门牌号映射）
	 */
    @RequestMapping("modifyApartmentMapping")
    @RestReturn(value=Long.class)
    public RestResponse modifyApartmentMapping() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 根据表达式修改公寓门牌号映射（左邻系统和物业自有系统的门牌号映射）
	 */
    @RequestMapping("modifyApartmentMappingByExpress")
    @RestReturn(value=Long.class)
    public RestResponse modifyApartmentMappingByExpress() {
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
	 * 列出小区所有楼栋号
	 */
    @RequestMapping("listCommunityBuildings")
    @RestReturn(value=Long.class)
    public RestResponse listCommunityBuildings() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 列出小区指定楼栋里的公寓列表
	 */
    @RequestMapping("listApartmentsByBuilding")
    @RestReturn(value=Long.class)
    public RestResponse listApartmentsByBuilding() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 设置公寓状态：自住/出租/空闲/装修/待售/其它
	 */
    @RequestMapping("setApartmentStatus")
    @RestReturn(value=Long.class)
    public RestResponse setApartmentStatus() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * 上传业主信息excel文件（用户名、手机号、门牌号）
     */
    @RequestMapping(value="uploadPropertyOwnerInfos", method = RequestMethod.POST)
    @RestReturn(value=ClientPackageFileDTO.class)
    public RestResponse uploadPropertyOwnerInfos(@RequestParam(value = "attachment") MultipartFile[] files) {
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 编辑业主信息
	 */
    @RequestMapping("modifyPropertyOwnerInfo")
    @RestReturn(value=Long.class)
    public RestResponse modifyPropertyOwnerInfo() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
	 * 删除业主信息（可批量删除）
	 */
    @RequestMapping("deletePropertyOwnerInfo")
    @RestReturn(value=Long.class)
    public RestResponse deletePropertyOwnerInfo() {
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
	 * 查询物业成员列表
	 */
    @RequestMapping("listPropertyMembers")
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
    @RequestMapping("addPropertyMemberByPhone")
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
    @RequestMapping("deletePropertyMember")
    @RestReturn(value=Long.class)
    public RestResponse deletePropertyMember() {
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
