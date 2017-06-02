// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.ListBuildingByKeywordCommand;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.family.FamilyBillingTransactionDTO;
import com.everhomes.rest.messaging.QuestionMetaObject;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.organization.OrganizationBillingTransactionDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.user.SetCurrentCommunityCommand;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pm")
public class PropertyMgrController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrController.class);

	@Autowired
	PropertyMgrService propertyMgrService;

	@Autowired
	PropertyMgrProvider propertyMgrProvider;
	
	@Autowired
	OrganizationService organizationService;
	
	// @Autowired
	// private PMOwnerSearcher pmOwnerSearcher;

	/**
	 * <b>URL: /pm/findPropertyOrganization</b>
	 * <p>获取物业组织</p>
	 */
	//checked
	@RequestMapping("findPropertyOrganization")
	@RestReturn(value=OrganizationDTO.class)
	public RestResponse findPropertyOrganization(@Valid PropCommunityIdCommand  cmd) {
		OrganizationDTO commandResponse = propertyMgrService.findPropertyOrganization(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/getUserOwningProperties</b>
	 * <p>查询用户加入的物业</p>
	 */
	//checked
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
	//checked
	@RequestMapping("listPMGroupMembers")
	@RestReturn(value=ListPropMemberCommandResponse.class)
	public RestResponse listPropertyMembers(@Valid ListPropMemberCommand cmd) {
		ListPropMemberCommandResponse commandResponse = propertyMgrService.listCommunityPmMembers(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/applyPropertyMember</b>
	 * <p>当前用户申请物业管理员</p>
	 */
	//checked
	@RequestMapping("applyPropertyMember")
	@RestReturn(value= java.lang.String.class)
	public RestResponse applyPropertyMember(@Valid applyPropertyMemberCommand cmd) {

		propertyMgrService.applyPropertyMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/addPropertyMemberByPhone</b>
	 * <p>通过手机添加物业成员</p>
	 */
	//checked
	@RequestMapping("addPMGroupMemberByPhone")
	@RestReturn(value= java.lang.String.class)
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
	/*@RequestMapping("approvePropertyMember")
	@RestReturn(value=String.class)
	public RestResponse approvePropertyMember(@Valid CommunityPropMemberCommand cmd) {
		propertyMgrService.approvePropMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/

	/**
	 * <b>URL: /pm/rejectMember</b>
	 * <p>拒绝物业成员</p>
	 * @return 拒绝的结果
	 */
	/*@RequestMapping("rejectPropertyMember")
	@RestReturn(value=String.class)
	public RestResponse rejectPropertyMember(@Valid CommunityPropMemberCommand cmd) {
		propertyMgrService.rejectPropMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/

	/**
	 * <b>URL: /pm/revokePMGroupMember</b>
	 * <p>删除物业成员</p>
	 * @return 删除的结果
	 */
	/*@RequestMapping("revokePMGroupMember")
	@RestReturn(value=String.class)
	public RestResponse revokePMGroupMember(@Valid DeletePropMemberCommand cmd) {
		propertyMgrService.revokePMGroupMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/

	/**
	 * <b>URL: /pm/listApartmentMappings</b>
	 * <p>列出公寓门牌号映射表（左邻系统和物业自有系统的门牌号映射）</p>
	 */
	//checked
	@RequestMapping("listPMAddressMapping")
	@RestReturn(value=ListPropAddressMappingCommandResponse.class)
	public RestResponse listApartmentMappings(@Valid ListPropAddressMappingCommand cmd) {
		ListPropAddressMappingCommandResponse commandResponse = propertyMgrService.listAddressMappings(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pm/getPMAddressMapping</b>
	 * <p>列出公寓门牌号映射表（左邻系统和物业自有系统的门牌号映射）</p>
	 */
	//checked
	/*@RequestMapping("getPMAddressMapping")
	@RestReturn(value=ListPropAddressMappingCommandResponse.class)
	public RestResponse getPMAddressMapping(@Valid ListPropAddressMappingCommand cmd) {
		ListPropAddressMappingCommandResponse commandResponse = propertyMgrService.listAddressMappings(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/

	/**
	 * <b>URL: /pm/importPMAddressMapping</b>
	 * <p>导入左邻系统和物业自有系统的门牌号映射</p>
	 */
	//checked
	@RequestMapping("importPMAddressMapping")
	@RestReturn(value= java.lang.String.class)
	public RestResponse importPMAddressMapping(@Valid PropCommunityIdCommand cmd) {
		propertyMgrService.importPMAddressMapping(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/updatePMAddressMapping</b>
	 * <p>修改公寓门牌号映射表</p>
	 * @return 修改的结果
	 */
	/*@RequestMapping("updatePMAddressMapping")
	@RestReturn(value=String.class)
	public RestResponse updatePMAddressMapping(@Valid UpdatePropAddressMappingCommand cmd) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/

	/**
	 * <b>URL: /pm/ListPropOwnerCommandResponse</b>
	 * <p>列出业主信息表</p>
	 */
	//checked
	@RequestMapping("listPMPropertyOwnerInfo")
	@RestReturn(value=ListPropOwnerCommandResponse.class)
	public RestResponse listPMPropertyOwnerInfo(@Valid ListPropOwnerCommand cmd) {
		ListPropOwnerCommandResponse commandResponse = propertyMgrService.listPMPropertyOwnerInfo(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/setApartmentStatus</b>
	 * <p>设置公寓状态：自住/出租/空闲/装修/待售/其它</p>
	 * @return 设置的结果
	 */
	//checked
	@RequestMapping("setApartmentStatus")
	@RestReturn(value= java.lang.String.class)
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
	//checked
	@RequestMapping("getApartmentStatistics")
	@RestReturn(value=PropAptStatisticDTO.class)
	public RestResponse getApartmentStatistics(@Valid PropCommunityIdCommand cmd) {
		PropAptStatisticDTO result = propertyMgrService.getApartmentStatistics(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pm/getNewApartmentStatistics</b>
	 * <p>小区公寓统计信息：公寓统计（总数、入住公寓数量、入住用户数量、自住/出租/空闲/装修/待售/其它等数量）</p>
	 */
	//checked
	@RequestMapping("getNewApartmentStatistics")
	@RestReturn(value=PropAptStatisticDTO.class)
	public RestResponse getNewApartmentStatistics(@Valid PropCommunityIdCommand cmd) {
		PropAptStatisticDTO result = propertyMgrService.getNewApartmentStatistics(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/assignPMTopics</b>
	 * <p>把物业维修帖指派给处理人员（可批量指派）</p>
	 * @return 分配的结果
	 */

	/*@RequestMapping("assignPMTopics")
	@RestReturn(value=String.class)
	public RestResponse assignPMTopics(@Valid AssginPmTopicCommand cmd) {
		propertyMgrService.assignPMTopics(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/setPMTopicStatus</b>
	 * <p>设置物业维修帖状态：未处理、处理中、已处理、其它（可批量设置）</p>
	 * @return 设置的结果
	 */

	/*@RequestMapping("setPMTopicStatus")
	@RestReturn(value=String.class)
	public RestResponse setPMTopicStatus(@Valid SetPmTopicStatusCommand cmd) {
		propertyMgrService.setPMTopicStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/getPMTopicStatistics</b>
	 * <p>根据时间和状态获取物业维修帖统计信息：未处理、处理中、已处理、其它</p>
	 */
	//checked
	@RequestMapping("getPMTopicStatistics")
	@RestReturn(value=ListPropTopicStatisticCommandResponse.class)
	public RestResponse getPMTopicStatistics(@Valid ListPropTopicStatisticCommand  cmd) {
		ListPropTopicStatisticCommandResponse result = propertyMgrService.getPMTopicStatistics(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/sendMsgToPMGroup</b>
	 * 发消息给物业组所有成员
	 */
	//checked
	@RequestMapping("sendMsgToPMGroup")
	@RestReturn(value= java.lang.String.class)
	public RestResponse sendMsgToPMGroup(@Valid PropCommunityIdMessageCommand cmd) {
		propertyMgrService.sendMsgToPMGroup(cmd);
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

	/*@RequestMapping("sendNoticeToCommunity")
	@RestReturn(value=String.class)
	public RestResponse sendNoticeToCommunity(@Valid PropCommunityIdCommand cmd)  {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/sendNoticeToFloor</b>
	 * 发通知给整层楼业主（是左邻用户则发消息、不是左邻用户则发短信）
	 * @return
	 */

	/*@RequestMapping("sendNoticeToFloor")
	@RestReturn(value=String.class)
	public RestResponse sendNoticeToFloor(@Valid PropCommunityBuildCommand cmd) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/sendNoticeToFamily</b>
	 * 物业一键推送消息（是左邻用户则发消息、不是左邻用户则发短信）。
	 * 按小区，楼栋，门牌号范围
	 */
	//checked
	@RequestMapping("sendNoticeToFamily")
	@RestReturn(value= java.lang.String.class)
	public RestResponse sendNoticeToFamily(PropCommunityBuildAddessCommand cmd) {
		propertyMgrService.sendNoticeToFamily(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/sendNoticeToPmAdmin</b>
	 * <p>一键推送消息给管理员, 按小区，公司，用户</p>
	 */
	@RequestMapping("sendNoticeToPmAdmin")
	@RestReturn(value= java.lang.String.class)
	public RestResponse sendNoticeToPmAdmin(SendNoticeToPmAdminCommand cmd) {
		propertyMgrService.sendNoticeToPmAdmin(cmd);
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

	/*@RequestMapping(value="importPropertyBills", method = RequestMethod.POST)
	@RestReturn(value=String.class)
	public RestResponse importPropertyBills(@Valid PropCommunityIdCommand cmd,
			@RequestParam(value = "attachment") MultipartFile[] files) {

		propertyMgrService.importPropertyBills(cmd, files);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/listPropertyBill</b>
	 * 查询缴费账单详情
	 */

	/*@RequestMapping("listPropertyBill")
	@RestReturn(value=ListPropBillCommandResponse.class)
	public RestResponse listPropertyBill(
			@Valid ListPropBillCommand cmd) {
		ListPropBillCommandResponse commandResponse = propertyMgrService.listPropertyBill(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/deletePropertyBillByMonth</b>
	 * 删除指定月份的缴费账单
	 * @return 删除的结果
	 */


	/*@RequestMapping("deletePropertyBillByMonth")
	@RestReturn(value=String.class)
	public RestResponse deletePropertyBillByMonth(@Valid PropCommunityBillDateCommand cmd) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/deletePropertyBill</b>
	 * 删除缴费账单（可批量删除）
	 * @return 删除的结果
	 */

	/*@RequestMapping("deletePropertyBill")
	@RestReturn(value=String.class)
	public RestResponse deletePropertyBill(@Valid PropCommunityBillIdCommand cmd) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/sendPropertyBillsByMonth</b>
	 * 批量发指定月份的缴费账单
	 * @return 发送物业缴费通知的结果
	 */

	/*@RequestMapping("sendPropertyBillsByMonth")
	@RestReturn(value=String.class)
	public RestResponse sendPropertyBillsByMonth(@Valid PropCommunityBillDateCommand cmd) {
		propertyMgrService.sendPropertyBillByMonth(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/sendPropertyBillById</b>
	 * 发指定的单个缴费账单
	 * @return 发送物业缴费通知的结果
	 */

	/*@RequestMapping("sendPropertyBillById")
	@RestReturn(value=String.class)
	public RestResponse sendPropertyBillById(@Valid PropCommunityBillIdCommand cmd) {
		propertyMgrService.sendPropertyBillById(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/listInvitedUsers</b>
	 * 查询邀请加入左邻的用户列表
	 */

	/*@RequestMapping("listInvitedUsers")
	@RestReturn(value=ListPropInvitedUserCommandResponse.class)
	public RestResponse listInvitedUsers(@Valid ListPropInvitedUserCommand cmd) {
		ListPropInvitedUserCommandResponse  commandResponse = this.propertyMgrService.listInvitedUsers(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/searchInvitedUsers</b>
	 * 搜索邀请加入左邻的用户
	 */

	/*@RequestMapping("searchInvitedUsers")
	@RestReturn(value=ListPropInvitedUserCommandResponse.class)
	public RestResponse searchInvitedUsers(@Valid ListPropInvitedUserCommand cmd) {
		ListPropInvitedUserCommandResponse  commandResponse = this.propertyMgrService.listInvitedUsers(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/approvePropFamilyMember</b>
	 * <p>批准家庭成员</p>
	 */
	//checked
	@RequestMapping("approvePropFamilyMember")
	@RestReturn(value= java.lang.String.class)
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
	//checked
	@RequestMapping("rejectPropFamilyMember")
	@RestReturn(value= java.lang.String.class)
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
	 */
	//checked
	@RequestMapping("revokePropFamilyMember")
	@RestReturn(value= java.lang.String.class)
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
	//checked
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
	 * <b>URL: /pm/findUserByIndentifier</b>
	 * <p>根据用户token查询用户信息</p>
	 */
	//checked
	@RequestMapping("findUserByIndentifier")
	@RestReturn(value=UserTokenCommandResponse.class)
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
	 */
	//checked
	@RequestMapping("setPropCurrentCommunity")
	@RestReturn(java.lang.String.class)
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
	 */
	//checked
	@RequestMapping("listPropFamilyWaitingMember")
	@RestReturn(value=ListPropFamilyWaitingMemberCommandResponse.class)
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
	 */
	//checked
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
	 * <b>URL: /pm/searchOrganizationOwners</b>
	 * <p>搜索业主列表</p>
	 */
	@RequestMapping("searchOrganizationOwners")
	@RestReturn(value=ListOrganizationOwnersResponse.class)
	public RestResponse searchOrganizationOwners(@Valid SearchOrganizationOwnersCommand cmd) {
		ListOrganizationOwnersResponse results = propertyMgrService.searchOrganizationOwners(cmd);
		RestResponse response = new RestResponse(results);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pm/searchOrganizationOwnersBycondition</b>
	 * <p>搜索业主列表 用于物业报修 任务</p>
	 */
	@RequestMapping("searchOrganizationOwnersBycondition")
	@RestReturn(value=OrganizationOwnerDTO.class, collection=true)
	public RestResponse searchOrganizationOwnersBycondition(@Valid SearchOrganizationOwnersByconditionCommand cmd) {
		List<OrganizationOwnerDTO> results = propertyMgrService.searchOrganizationOwnersBycondition(cmd);
		RestResponse response = new RestResponse(results);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrganizationOwnersByAddress</b>
	 * <p>根据地址信息查询业主列表</p>
	 */
	@RequestMapping("listOrganizationOwnersByAddress")
	@RestReturn(value=OrganizationOwnerDTO.class, collection = true)
	public RestResponse listOrganizationOwnersByAddress(@Valid ListOrganizationOwnersByAddressCommand cmd) {
		List<OrganizationOwnerDTO> dtoList = propertyMgrService.listOrganizationOwnersByAddress(cmd);
		RestResponse response = new RestResponse(dtoList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrganizationOwnerCarsByAddress</b>
	 * <p>根据地址信息查询车辆列表</p>
	 */
	@RequestMapping("listOrganizationOwnerCarsByAddress")
	@RestReturn(value=OrganizationOwnerCarDTO.class, collection = true)
	public RestResponse listOrganizationOwnerCarsByAddress(@Valid ListOrganizationOwnerCarsByAddressCommand cmd) {

		List<OrganizationOwnerCarDTO> dtoList = propertyMgrService.listOrganizationOwnerCarsByAddress(cmd);
		RestResponse response = new RestResponse(dtoList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/getOrganizationOwner</b>
	 * <p>根据id获取业主信息</p>
	 */
	@RequestMapping("getOrganizationOwner")
	@RestReturn(value=OrganizationOwnerDTO.class)
	public RestResponse getOrganizationOwner(@Valid GetOrganizationOwnerCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_LIST);

        OrganizationOwnerDTO dto = propertyMgrService.getOrganizationOwner(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/getOrganizationOwnerCar</b>
	 * <p>根据id获取车辆信息</p>
	 */
	@RequestMapping("getOrganizationOwnerCar")
	@RestReturn(value=OrganizationOwnerCarDTO.class)
	public RestResponse getOrganizationOwnerCar(@Valid GetOrganizationOwnerCarCommand cmd) {
        OrganizationOwnerCarDTO dto = propertyMgrService.getOrganizationOwnerCar(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrganizationOwnersByCar</b>
	 * <p>根据车辆获取业主信息</p>
	 */
	@RequestMapping("listOrganizationOwnersByCar")
	@RestReturn(value=OrganizationOwnerDTO.class, collection = true)
	public RestResponse listOrganizationOwnersByCar(@Valid ListOrganizationOwnersByCarCommand cmd) {
        List<OrganizationOwnerDTO> dtos = propertyMgrService.listOrganizationOwnersByCar(cmd);
		RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /pm/syncOwnerIndex</b>
     * <p>同步业主索引</p>
     */
    @RequestMapping("syncOwnerIndex")
    @RestReturn(value=String.class)
    public RestResponse syncOwnerIndex() {
        propertyMgrService.syncOwnerIndex();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pm/syncOwnerCarIndex</b>
     * <p>同步车辆索引</p>
     */
    @RequestMapping("syncOwnerCarIndex")
    @RestReturn(value=String.class)
    public RestResponse syncOwnerCarIndex() {
        propertyMgrService.syncOwnerCarIndex();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /pm/listPropBillDateStr</b>
	 * <p>查询物业账单的时间列表</p>
	 */

	/*@RequestMapping("listPropBillDateStr")
	@RestReturn(value=String.class, collection=true)
	public RestResponse listPropBillDateStr(@Valid PropCommunityIdCommand cmd) {
		List<String> results = propertyMgrProvider.listPropBillDateStr(cmd.getCommunityId());
		RestResponse response = new RestResponse(results);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/newPmTopic</b>
	 * <p>物业人员发帖</p>
	 * @return 发帖的内容
	 */

	/*@RequestMapping("newPmTopic")
	@RestReturn(value=PostDTO.class)
	public RestResponse newPmTopic(@Valid NewTopicCommand cmd) {
		PostDTO postDto = propertyMgrService.createTopic(cmd);
		RestResponse response = new RestResponse(postDto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/queryPmTopicsByCategory</b>
	 * <p>按指定类型查询的帖子列表（仅查询社区论坛）</p>
	 */

	/*@RequestMapping("queryPmTopicsByCategory")
	@RestReturn(value=ListPropPostCommandResponse.class)
	public RestResponse queryPmTopicsByCategory(QueryPropTopicByCategoryCommand cmd) {
		ListPropPostCommandResponse  cmdResponse = propertyMgrService.queryTopicsByCategory(cmd);
		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/listPmTopics</b>
	 * <p>查询指定论坛的帖子列表（不区分类型查询）</p>
	 */

	/*@RequestMapping("listPmTopics")
	@RestReturn(value=ListPostCommandResponse.class)
	public RestResponse listPmTopics(ListTopicCommand cmd) {
		ListPostCommandResponse cmdResponse = propertyMgrService.listTopics(cmd);

		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/getPmTopic</b>
	 * <p>获取指定论坛里的指定帖子内容</p>
	 */

	/*@RequestMapping("getPmTopic")
	@RestReturn(value=PostDTO.class)
	public RestResponse getPmTopic(GetTopicCommand cmd) {
		PostDTO postDto = propertyMgrService.getTopic(cmd);

		RestResponse response = new RestResponse(postDto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/likePmTopic</b>
	 * <p>对指定论坛里的指定帖子点赞</p>
	 * @return 点赞的结果
	 */

	/*@RequestMapping("likePmTopic")
	@RestReturn(value=String.class)
	public RestResponse likePmTopic(LikeTopicCommand cmd) {
		propertyMgrService.likeTopic(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/cancelLikePmTopic</b>
	 * <p>对指定论坛里的指定帖子取消赞</p>
	 * @return 取消赞的结果
	 */

	/*@RequestMapping("cancelLikePmTopic")
	@RestReturn(value=String.class)
	public RestResponse cancelLikePmTopic(CancelLikeTopicCommand cmd) {
		propertyMgrService.cancelLikeTopic(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/deletePmTopic</b>
	 * <p>删除指定论坛里的指定帖子（需要有删帖权限）</p>
	 * @return 删除结果
	 */

	/*@RequestMapping("deletePmTopic")
	@RestReturn(value=String.class)
	public RestResponse deletePmTopic(DeleteTopicCommand cmd) {

		// ???
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	//    /**

	//     * <b>URL: /pm/forwardTopic</b>
	//     * <p>转发帖子</p>
	//     * @return 转发结果
	//     */
	//    @RequestMapping("forwardTopic")
	//    @RestReturn(value=Long.class)
	//    public RestResponse forwardTopic(@Valid ForwardTopicCommand cmd) {
	//
	//        // ???
	//        RestResponse response = new RestResponse();
	//        response.setErrorCode(ErrorCodes.SUCCESS);
	//        response.setErrorDescription("OK");
	//        return response;
	//    }
	//
	//    /**
	//     * <b>URL: /pm/makeTop</b>
	//     * <p>置顶帖子</p>
	//     * @return 置顶结果
	//     */
	//    @RequestMapping("makeTop")
	//    @RestReturn(value=String.class)
	//    public RestResponse makeTop(@Valid MakeTopCommand cmd) {
	//
	//        // ???
	//        RestResponse response = new RestResponse();
	//        response.setErrorCode(ErrorCodes.SUCCESS);
	//        response.setErrorDescription("OK");
	//        return response;
	//    }
	//
	//    /**
	//     * <b>URL: /pm/search</b>
	//     * <p>按指定条件查询符合条件的帖子列表</p>
	//     */
	//    @RequestMapping("search")
	//    @RestReturn(value=ListPostCommandResponse.class)
	//    public RestResponse search(SearchTopicCommand cmd) {
	//        ListPostCommandResponse cmdResponse = postSearcher.query(cmd);
	//
	//        RestResponse response = new RestResponse();
	//        response.setResponseObject(cmdResponse);
	//        response.setErrorCode(ErrorCodes.SUCCESS);
	//        response.setErrorDescription("OK");
	//        return response;
	//    }
	/**
	 * <b>URL: /pm/listPmTopicComments</b>
	 * <p>获取指定论坛里指定帖子下的评论列表</p>
	 */

	/*@RequestMapping("listPmTopicComments")
	@RestReturn(value=PostDTO.class, collection=true)
	public RestResponse listPmTopicComments(@Valid ListTopicCommentCommand cmd) {
		ListPostCommandResponse cmdResponse = propertyMgrService.listTopicComments(cmd);

		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/newPmComment</b>
	 * <p>创建新评论</p>
	 */

	/*@RequestMapping("newPmComment")
	@RestReturn(value=PostDTO.class)
	public RestResponse newPmComment(@Valid NewCommentCommand cmd) {
		PostDTO postDTO = propertyMgrService.createComment(cmd);

		RestResponse response = new RestResponse(postDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/deletePmComment</b>
	 * <p>删除指定论坛里的指定评论（需要有删评论权限）</p>
	 * @return 删除结果
	 */

	/*@RequestMapping("deletePmComment")
	@RestReturn(value=String.class)
	public RestResponse deletePmComment(DeleteCommonCommentCommand cmd) {

		// ???
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/
	/**
	 * <b>URL: /pm/listPropApartmentsByKeyword</b>
	 * <p>根据小区Id、楼栋号和关键字查询门牌(物业)</p>
	 */
	@RequestMapping("listPropApartmentsByKeyword")
	@RestReturn(value=PropFamilyDTO.class, collection=true)
	public RestResponse listPropApartmentsByKeyword(@Valid ListPropApartmentsByKeywordCommand cmd) {
		List<PropFamilyDTO> results =  propertyMgrService.listPropApartmentsByKeyword(cmd);
		RestResponse response = new RestResponse(results);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/importPmBills
	 * <p>上传缴费账单excel文件
	 * @param cmd
	 * @param files 要上传文件
	 * @return
	 */
	@RequestMapping(value="importPmBills", method = RequestMethod.POST)
	@RestReturn(value= java.lang.String.class)
	public RestResponse importPmBills(@Valid ImportPmBillsCommand cmd,@RequestParam(value = "attachment") MultipartFile[] files) {

		propertyMgrService.importPmBills(cmd.getOrganizationId(), files);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;

	}

	/**
	 * <b>URL: /pm/listPmBillsByConditions
	 * <p>根据条件查询物业缴费单
	 */
	@RequestMapping(value="listPmBillsByConditions",method = RequestMethod.POST)
	@RestReturn(value=PmBillsDTO.class,collection=true)
	public RestResponse listPmBillsByConditions(@Valid ListPmBillsByConditionsCommand cmd) {
		ListPmBillsByConditionsCommandResponse result = propertyMgrService.listPmBillsByConditions(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/deletePmBills
	 * <p>删除物业缴费单
	 */
	@RequestMapping("deletePmBills")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deletePmBills(@Valid DeletePmBillsCommand cmd) {

		propertyMgrService.deletePmBills(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/updatePmBills
	 * <p>更新物业缴费单
	 */
	@RequestMapping("updatePmBills")
	@RestReturn(value= java.lang.String.class)
	public RestResponse updatePmBills(@Valid UpdatePmBillsCommand cmd) {

		propertyMgrService.updatePmBills(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/insertPmBills
	 * <p>新增物业缴费单
	 */
	@RequestMapping("insertPmBills")
	@RestReturn(value= java.lang.String.class)
	public RestResponse insertPmBills(@Valid InsertPmBillsCommand cmd) {

		propertyMgrService.insertPmBills(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/deletePmBill
	 * <p>删除物业缴费单
	 */
	@RequestMapping("deletePmBill")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deletePmBill(@Valid DeletePmBillCommand cmd) {

		propertyMgrService.deletePmBill(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/updatePmBill
	 * <p>更新物业缴费单
	 */
	@RequestMapping("updatePmBill")
	@RestReturn(value= java.lang.String.class)
	public RestResponse updatePmBill(@Valid UpdatePmBillCommand cmd) {

		propertyMgrService.updatePmBill(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/insertPmBill
	 * <p>新增物业缴费单
	 */
	@RequestMapping("insertPmBill")
	@RestReturn(value= java.lang.String.class)
	public RestResponse insertPmBill(@Valid InsertPmBillCommand cmd) {

		propertyMgrService.insertPmBill(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrgBillingTransactionsByConditions
	 * <p>根据条件查询缴费记录
	 */
	@RequestMapping("listOrgBillingTransactionsByConditions")
	@RestReturn(value=OrganizationBillingTransactionDTO.class,collection=true)
	public RestResponse listOrgBillingTransactionsByConditions(@Valid ListOrgBillingTransactionsByConditionsCommand cmd) {

		ListOrgBillingTransactionsByConditionsCommandResponse result = propertyMgrService.listOrgBillingTransactionsByConditions(cmd);

		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /pm/listOweFamilysByConditions
	 * <p>根据条件查询欠费家庭
	 */
	@RequestMapping("listOweFamilysByConditions")
	@RestReturn(value=OweFamilyDTO.class,collection=true)
	public RestResponse listOweFamilysByConditions(@Valid ListOweFamilysByConditionsCommand cmd) {

		ListOweFamilysByConditionsCommandResponse result = propertyMgrService.listOweFamilysByConditions(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	//web-family


	/**
	 * <b>URL: /pm/listBillTxByAddressId
	 * <p>根据家庭Id查询家庭的缴费记录
	 */
	@RequestMapping("listBillTxByAddressId")
	@RestReturn(value=FamilyBillingTransactionDTO.class,collection=true)
	public RestResponse listBillTxByAddressId(@Valid ListBillTxByAddressIdCommand cmd) {

		ListBillTxByAddressIdCommandResponse result = propertyMgrService.listBillTxByAddressId(cmd);

		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /pm/findBillByAddressIdAndTime
	 * <p>根据家庭Id和日期查询家庭的账单
	 */
	@RequestMapping("findBillByAddressIdAndTime")
	@RestReturn(value=PmBillsDTO.class)
	public RestResponse findBillByAddressIdAndTime(@Valid FindBillByAddressIdAndTimeCommand cmd) {

		PmBillsDTO bill = propertyMgrService.findBillByAddressIdAndTime(cmd);
		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/findNewestBillByAddressId
	 * <p>根据家庭Id查询家庭的最新账单
	 */
	@RequestMapping("findNewestBillByAddressId")
	@RestReturn(value=PmBillsDTO.class)
	public RestResponse findNewestBillByAddressId(@Valid FindNewestBillByAddressIdCommand cmd) {

		PmBillsDTO bill = propertyMgrService.findNewestBillByAddressId(cmd);
		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	//app

	/**
	 * <b>URL: /pm/findFamilyBillAndPaysByFamilyIdAndTime
	 * <p>根据家庭id和日期查询家庭某月份的账单和缴费记录
	 */
	@RequestMapping("findFamilyBillAndPaysByFamilyIdAndTime")
	@RestReturn(value=PmBillsDTO.class)
	public RestResponse findFamilyBillAndPaysByFamilyIdAndTime(@Valid FindFamilyBillAndPaysByFamilyIdAndTimeCommand cmd) {

		PmBillsDTO bill = propertyMgrService.findFamilyBillAndPaysByFamilyIdAndTime(cmd);
		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /pm/listFamilyBillsAndPaysByFamilyId
	 * <p>根据家庭id和日期查询家庭的所有账单和缴费记录
	 */
	@RequestMapping("listFamilyBillsAndPaysByFamilyId")
	@RestReturn(ListFamilyBillsAndPaysByFamilyIdCommandResponse.class)
	public RestResponse listFamilyBillsAndPaysByFamilyId(@Valid ListFamilyBillsAndPaysByFamilyIdCommand cmd) {

		ListFamilyBillsAndPaysByFamilyIdCommandResponse result = propertyMgrService.listFamilyBillsAndPaysByFamilyId(cmd);

		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	//线下支付

	/**
	 * <b>URL: /pm/payPmBillByAddressId
	 * <p>缴费
	 */
	@RequestMapping("payPmBillByAddressId")
	@RestReturn(value= java.lang.String.class)
	public RestResponse payPmBillByAddressId(@Valid PayPmBillByAddressIdCommand cmd) {

		propertyMgrService.payPmBillByAddressId(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /pm/getPmPayStatistics
	 * <p>物业缴费统计:年度收入，待收费,欠费户数
	 */
	@RequestMapping("getPmPayStatistics")
	@RestReturn(GetPmPayStatisticsCommandResponse.class)
	public RestResponse getPmPayStatistics (@Valid GetPmPayStatisticsCommand cmd){
		GetPmPayStatisticsCommandResponse result = this.propertyMgrService.getPmPayStatistics(cmd);

		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/sendPmPayMessageByAddressId
	 * <p>给指定欠费家庭发缴费通知
	 */
	@RequestMapping("sendPmPayMessageByAddressId")
	@RestReturn(java.lang.String.class)
	public RestResponse sendPmPayMessageByAddressId (@Valid SendPmPayMessageByAddressIdCommand cmd){
		this.propertyMgrService.sendPmPayMessageByAddressId(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/sendPmPayMessageToAllOweFamilies
	 * <p>给所有欠费家庭发缴费通知
	 */
	@RequestMapping("sendPmPayMessageToAllOweFamilies")
	@RestReturn(java.lang.String.class)
	public RestResponse sendPmPayMessageToAllOweFamilies (@Valid SendPmPayMessageToAllOweFamiliesCommand cmd){
		this.propertyMgrService.sendPmPayMessageToAllOweFamilies(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/getFamilyStatistic
	 * <p>统计家庭的年总应付，年总实付，目前欠款
	 */
	@RequestMapping("getFamilyStatistic")
	@RestReturn(GetFamilyStatisticCommandResponse.class)
	public RestResponse getFamilyStatistic (@Valid GetFamilyStatisticCommand cmd){
		GetFamilyStatisticCommandResponse result = this.propertyMgrService.getFamilyStatistic(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	//线上支付

	/**
	 * <b>URL: /pm/onlinePayPmBill
	 * <p>物业费线上支付
	 */
	@RequestMapping("onlinePayPmBill")
	@RestReturn(value= java.lang.String.class)
	public RestResponse onlinePayPmBill(@Valid OnlinePayPmBillCommand cmd) {
		propertyMgrService.onlinePayPmBill(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	//根据订单号查询订单信息
	/**
	 * <b>URL: /pm/findPmBillByOrderNo
	 * <p>根据订单号查询订单信息
	 */
	@RequestMapping("findPmBillByOrderNo")
	@RestReturn(value=PmBillForOrderNoDTO.class)
	public RestResponse findPmBillByOrderNo(@Valid FindPmBillByOrderNoCommand cmd) {
		PmBillForOrderNoDTO bill = propertyMgrService.findPmBillByOrderNo(cmd);
		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /pm/createPmBillOrder
	 * <p>线上支付下订单
	 */
	@RequestMapping("createPmBillOrder")
	@RestReturn(value=OrganizationOrderDTO.class)
	public RestResponse createPmBillOrder(@Valid CreatePmBillOrderCommand cmd) {
		OrganizationOrderDTO order = propertyMgrService.createPmBillOrder(cmd);
		RestResponse response = new RestResponse(order);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	@RequestMapping("getWebToken")
    @RestReturn(value= java.lang.String.class)
    public RestResponse getWebToken(@Valid FindPmBillByOrderNoCommand cmd){
		java.lang.String str = cmd.getOrderNo();
		java.lang.String encodeStr = WebTokenGenerator.getInstance().toWebToken(str);
        RestResponse response = new RestResponse(encodeStr);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	@RequestMapping("sendNoticeToOrganizationMember")
    @RestReturn(value= java.lang.String.class)
    public RestResponse sendNoticeToOrganizationMember(@Valid PropCommunityBuildAddessCommand cmd){
		propertyMgrService.sendNoticeToOrganizationMember(cmd,UserContext.current().getUser());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	@RequestMapping("createPmBillOrderDemo")
	@RestReturn(value=CommonOrderDTO.class)
	public RestResponse createPmBillOrderDemo(@Valid CreatePmBillOrderCommand cmd) {
		CommonOrderDTO order = propertyMgrService.createPmBillOrderDemo(cmd);
		RestResponse response = new RestResponse(order);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /pm/listFamilyMembersByFamilyId</b>
     * <p>查询家庭的成员列表</p>
     */
    //checked
    /*@RequestMapping("listFamilyMembersByFamilyId")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listFamilyMembersByFamilyId(@Valid ListPropFamilyMemberCommand cmd) {
        List<FamilyMemberDTO> results = propertyMgrService.listFamilyMembersByFamilyId(cmd);
        RestResponse response = new RestResponse(results);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }*/

	/**
	 * <b>URL: /pm/searchPMOwnerInfo</b>
	 * <p>列出业主信息表</p>
	 *//*
	@RequestMapping("searchPMOwnerInfo")
	@RestReturn(value=SearchPMOwnerResponse.class)
	public RestResponse searchPMOwnerInfo(@Valid SearchPMOwnerCommand cmd) {
		SearchPMOwnerResponse commandResponse = pmOwnerSearcher.query(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/

	/**
	 * <b>URL: /pm/createOrganizationOwner</b>
	 * <p>增加新业主</p>
	 */
	@RequestMapping("createOrganizationOwner")
	@RestReturn(value= OrganizationOwnerDTO.class)
	public RestResponse createOrganizationOwner(@Valid CreateOrganizationOwnerCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_CREATE);

        OrganizationOwnerDTO dto = propertyMgrService.createOrganizationOwner(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/addOrganizationOwnerAddress</b>
	 * <p>给业主增加楼栋门牌</p>
	 */
	@RequestMapping("addOrganizationOwnerAddress")
	@RestReturn(value= OrganizationOwnerAddressDTO.class)
	public RestResponse addOrganizationOwnerAddress(@Valid AddOrganizationOwnerAddressCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        OrganizationOwnerAddressDTO dto = propertyMgrService.addOrganizationOwnerAddress(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/addOrganizationOwnerCarUser</b>
	 * <p>给业主增加楼栋门牌</p>
	 */
	@RequestMapping("addOrganizationOwnerCarUser")
	@RestReturn(value= OrganizationOwnerDTO.class)
	public RestResponse addOrganizationOwnerCarUser(@Valid AddOrganizationOwnerCarUserCommand cmd) {
        OrganizationOwnerDTO dto = propertyMgrService.addOrganizationOwnerCarUser(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
     * <b>URL: /pm/updateOrganizationOwner</b>
     * <p>修改业主信息</p>
     */
    @RequestMapping("updateOrganizationOwner")
    @RestReturn(value= OrganizationOwnerDTO.class)
    public RestResponse updateOrganizationOwner(@Valid UpdateOrganizationOwnerCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_UPDATE);

        OrganizationOwnerDTO dto = propertyMgrService.updateOrganizationOwner(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /pm/updateOrganizationOwnerAddressStatus</b>
	 * <p>对业主执行的一些迁入, 迁出</p>
	 */
	@RequestMapping("updateOrganizationOwnerAddressStatus")
	@RestReturn(value= java.lang.String.class)
	public RestResponse updateOrganizationOwnerAddressStatus(@Valid UpdateOrganizationOwnerAddressStatusCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        propertyMgrService.updateOrganizationOwnerAddressStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/deleteOrganizationOwnerAddress</b>
	 * <p>移除业主与地址的之间的关系</p>
	 */
	@RequestMapping("deleteOrganizationOwnerAddress")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deleteOrganizationOwnerAddress(@Valid DeleteOrganizationOwnerAddressCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        propertyMgrService.deleteOrganizationOwnerAddress(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/updateOrganizationOwnerAddressAuthType</b>
	 * <p>解除业主与地址之间的认证关系</p>
	 */
	@RequestMapping("updateOrganizationOwnerAddressAuthType")
	@RestReturn(value= java.lang.String.class)
	public RestResponse updateOrganizationOwnerAddressAuthType(@Valid UpdateOrganizationOwnerAddressAuthTypeCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        propertyMgrService.deleteOrganizationOwnerAddressAuthStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrganizationOwnerBehaviors</b>
	 * <p>查询业主的活动记录</p>
	 */
	@RequestMapping("listOrganizationOwnerBehaviors")
	@RestReturn(value=OrganizationOwnerBehaviorDTO.class, collection = true)
	public RestResponse listOrganizationOwnerBehaviors(@Valid ListOrganizationOwnerBehaviorsCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_LIST);

        List<OrganizationOwnerBehaviorDTO> dtos = propertyMgrService.listOrganizationOwnerBehaviors(cmd);
		RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrganizationOwnerAddresses</b>
	 * <p>查询业主的所关联的楼栋门牌</p>
	 */
	@RequestMapping("listOrganizationOwnerAddresses")
	@RestReturn(value=OrganizationOwnerAddressDTO.class, collection = true)
	public RestResponse listOrganizationOwnerAddresses(@Valid ListOrganizationOwnerAddressesCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_LIST);

        List<OrganizationOwnerAddressDTO> dtos = propertyMgrService.listOrganizationOwnerAddresses(cmd);
        RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrganizationOwnerAttachments</b>
	 * <p>查询业主的所有附件列表</p>
	 */
	@RequestMapping("listOrganizationOwnerAttachments")
	@RestReturn(value=OrganizationOwnerAttachmentDTO.class, collection = true)
	public RestResponse listOrganizationOwnerAttachments(@Valid ListOrganizationOwnerAttachmentsCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_LIST);

        List<OrganizationOwnerAttachmentDTO> dtos = propertyMgrService.listOrganizationOwnerAttachments(cmd);
        RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listOrganizationOwnerTypes</b>
	 * <p>查询业主类型列表</p>
	 */
	@RequestMapping("listOrganizationOwnerTypes")
	@RestReturn(value=OrganizationOwnerTypeDTO.class, collection = true)
	public RestResponse listOrganizationOwnerTypes(@Valid ListOrganizationOwnerTypesCommand cmd) {
        List<OrganizationOwnerTypeDTO> dtos = propertyMgrService.listOrganizationOwnerTypes(cmd);
        RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/listParkingCardCategories</b>
	 * <p>列出停车类型列表</p>
	 */
	@RequestMapping("listParkingCardCategories")
	@RestReturn(value=ParkingCardCategoryDTO.class, collection = true)
	public RestResponse listParkingCardCategories(@Valid ListParkingCardCategoriesCommand cmd) {
        List<ParkingCardCategoryDTO> dtos = propertyMgrService.listParkingCardCategories(cmd);
        RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/uploadOrganizationOwnerAttachment</b>
	 * <p>上传业主附件</p>
	 */
	@RequestMapping("uploadOrganizationOwnerAttachment")
	@RestReturn(value=OrganizationOwnerAttachmentDTO.class)
	public RestResponse uploadOrganizationOwnerAttachment(@Valid UploadOrganizationOwnerAttachmentCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        OrganizationOwnerAttachmentDTO dto = propertyMgrService.uploadOrganizationOwnerAttachment(cmd);
        RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/uploadOrganizationOwnerCarAttachment</b>
	 * <p>上传车辆附件</p>
	 */
	@RequestMapping("uploadOrganizationOwnerCarAttachment")
	@RestReturn(value=OrganizationOwnerCarAttachmentDTO.class)
	public RestResponse uploadOrganizationOwnerCarAttachment(@Valid UploadOrganizationOwnerCarAttachmentCommand cmd) {
		OrganizationOwnerCarAttachmentDTO dto = propertyMgrService.uploadOrganizationOwnerCarAttachment(cmd);
        RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/deleteOrganizationOwnerBehavior</b>
	 * <p>删除业主的活动记录</p>
	 */
	@RequestMapping("deleteOrganizationOwnerBehavior")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deleteOrganizationOwnerBehavior(@Valid DeleteOrganizationOwnerBehaviorCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        propertyMgrService.deleteOrganizationOwnerBehavior(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/deleteOrganizationOwnerAttachment</b>
	 * <p>删除附件</p>
	 */
	@RequestMapping("deleteOrganizationOwnerAttachment")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deleteOrganizationOwnerAttachment(@Valid DeleteOrganizationOwnerAttachmentCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        propertyMgrService.deleteOrganizationOwnerAttachment(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /pm/deleteOrganizationOwnerCarAttachment</b>
	 * <p>删除车辆附件</p>
	 */
	@RequestMapping("deleteOrganizationOwnerCarAttachment")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deleteOrganizationOwnerCarAttachment(@Valid DeleteOrganizationOwnerCarAttachmentCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        propertyMgrService.deleteOrganizationOwnerCarAttachment(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL:/pm/createOrganizationOwnerCar</b>
     * <p>创建车辆记录</p>
     */
    @RequestMapping("createOrganizationOwnerCar")
    @RestReturn(value = OrganizationOwnerCarDTO.class)
    public RestResponse createOrganizationOwnerCar(CreateOrganizationOwnerCarCommand cmd) {
        OrganizationOwnerCarDTO dto = propertyMgrService.createOrganizationOwnerCar(cmd);
        RestResponse resp = new RestResponse(dto);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL:/pm/updateOrganizationOwnerCar</b>
     * <p>修改车辆记录</p>
     */
    @RequestMapping("updateOrganizationOwnerCar")
    @RestReturn(value = OrganizationOwnerCarDTO.class)
    public RestResponse updateOrganizationOwnerCar(UpdateOrganizationOwnerCarCommand cmd) {
        OrganizationOwnerCarDTO dto = propertyMgrService.updateOrganizationOwnerCar(cmd);
        RestResponse resp = new RestResponse(dto);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL:/pm/deleteOrganizationOwnerCar</b>
     * <p>删除车辆</p>
     */
    @RequestMapping("deleteOrganizationOwnerCar")
    @RestReturn(value = java.lang.String.class)
    public RestResponse deleteOrganizationOwnerCar(DeleteOrganizationOwnerCarCommand cmd) {
        propertyMgrService.deleteOrganizationOwnerCar(cmd);
        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL:/pm/setOrganizationOwnerAsCarPrimary</b>
     * <p>标记使用者为首要联系人</p>
     */
    @RequestMapping("setOrganizationOwnerAsCarPrimary")
    @RestReturn(value = java.lang.String.class)
    public RestResponse setOrganizationOwnerAsCarPrimary(SetOrganizationOwnerAsCarPrimaryCommand cmd) {
        propertyMgrService.setOrganizationOwnerAsCarPrimary(cmd);
        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL:/pm/deleteRelationOfOrganizationOwnerAndCar</b>
     * <p>移除车辆与业主的关系</p>
     */
    @RequestMapping("deleteRelationOfOrganizationOwnerAndCar")
    @RestReturn(value = java.lang.String.class)
    public RestResponse deleteRelationOfOrganizationOwnerAndCar(DeleteRelationOfOrganizationOwnerAndCarCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_MANAGE);

        propertyMgrService.deleteRelationOfOrganizationOwnerAndCar(cmd);
        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL:/pm/listOrganizationOwnerCarsByOrgOwner</b>
     * <p>根据业主列出关联车辆</p>
     */
    @RequestMapping("listOrganizationOwnerCarsByOrgOwner")
    @RestReturn(value = OrganizationOwnerCarDTO.class, collection = true)
    public RestResponse listOrganizationOwnerCarsByOrgOwner(ListOrganizationOwnerCarByOrgOwnerCommand cmd) {
        List<OrganizationOwnerCarDTO> dtos = propertyMgrService.listOrganizationOwnerCarsByOrgOwner(cmd);
        RestResponse resp = new RestResponse(dtos);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL:/pm/searchOrganizationOwnerCars</b>
     * <p>车辆搜索</p>
     */
    @RequestMapping("searchOrganizationOwnerCars")
    @RestReturn(ListOrganizationOwnerCarResponse.class)
    public RestResponse searchOrganizationOwnerCars(SearchOrganizationOwnerCarCommand cmd) {
        ListOrganizationOwnerCarResponse responseObject = propertyMgrService.searchOrganizationOwnerCars(cmd);
        RestResponse resp = new RestResponse(responseObject);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL:/pm/listOrganizationOwnerCarAttachments</b>
     * <p>列出车辆关联的附件列表</p>
     */
    @RequestMapping("listOrganizationOwnerCarAttachments")
    @RestReturn(value = OrganizationOwnerCarAttachmentDTO.class, collection = true)
    public RestResponse listOrganizationOwnerCarAttachments(ListOrganizationOwnerCarAttachmentCommand cmd) {
        List<OrganizationOwnerCarAttachmentDTO> dtos = propertyMgrService.listOrganizationOwnerCarAttachments(cmd);
        RestResponse resp = new RestResponse(dtos);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /pm/importOrganizationOwnerCars</b>
     * <p>导入车辆信息</p>
     */
    @RequestMapping(value="importOrganizationOwnerCars", method = RequestMethod.POST)
    @RestReturn(value=String.class)
    public RestResponse importOrganizationOwnerCars(@Valid ImportOrganizationOwnerCarsCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        propertyMgrService.importOrganizationOwnerCars(cmd, files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pm/exportOrganizationOwnerCars</b>
     * <p>导出车辆信息</p>
     */
    @RequestMapping(value="exportOrganizationOwnerCars")
    @RestReturn(value= String.class)
    public RestResponse exportOrganizationOwnerCars(@Valid ExportOrganizationOwnerCarsCommand cmd, HttpServletResponse response) {
        propertyMgrService.exportOrganizationOwnerCars(cmd, response);
        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

	/**
	 * <b>URL: /pm/importOrganizationOwners</b>
	 * <p>导入业主信息</p>
	 */
	@RequestMapping(value="importOrganizationOwners", method = RequestMethod.POST)
	@RestReturn(value=String.class)
	public RestResponse importOrganizationOwners(ImportOrganizationsOwnersCommand cmd, @RequestParam("attachment") MultipartFile[] files) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_IMPORT);

        propertyMgrService.importOrganizationOwners(cmd, files);
        RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
     * <b>URL: /pm/exportOrganizationOwners</b>
     * <p>导出业主信息</p>
     */
    @RequestMapping(value="exportOrganizationOwners")
    @RestReturn(value= java.lang.String.class)
    public RestResponse exportOrganizationOwners(@Valid ExportOrganizationsOwnersCommand cmd, HttpServletResponse response) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_EXPORT);

        propertyMgrService.exportOrganizationOwners(cmd, response);
        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

	/**
	 * <b>URL: /pm/deleteOrganizationOwner</b>
	 * <p>删除业主</p>
	 */
	@RequestMapping("deleteOrganizationOwner")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deleteOrganizationOwner(@Valid DeleteOrganizationOwnerCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_DELETE);

        propertyMgrService.deleteOrganizationOwner(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /pm/listOrganizationOwnerStatisticByGender</b>
     * <p>根据性别来查询报表</p>
     */
    @RequestMapping("listOrganizationOwnerStatisticByGender")
    @RestReturn(value = ListOrganizationOwnerStatisticDTO.class, collection = true)
    public RestResponse listOrganizationOwnerStatisticByGender(ListOrganizationOwnerStatisticCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_STATISTIC);

        List<ListOrganizationOwnerStatisticDTO> dtos = propertyMgrService.listOrganizationOwnerStatisticByGender(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pm/listOrganizationOwnerStatisticByAge</b>
     * <p>根据年龄来查询报表</p>
     */
    @RequestMapping("listOrganizationOwnerStatisticByAge")
    @RestReturn(value = ListOrganizationOwnerStatisticByAgeDTO.class)
    public RestResponse listOrganizationOwnerStatisticByAge(ListOrganizationOwnerStatisticCommand cmd) {
        checkPrivilege(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.CUSTOMER_STATISTIC);

        ListOrganizationOwnerStatisticByAgeDTO dto = propertyMgrService.listOrganizationOwnerStatisticByAge(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /pm/deletePMPropertyOwnerAddress</b>
	 * <p>删除业主地址</p>
	 */
	@RequestMapping("deletePMPropertyOwnerAddress")
	@RestReturn(value= java.lang.String.class)
	public RestResponse deletePMPropertyOwnerAddress(@Valid DeletePropOwnerAddressCommand cmd) {

		propertyMgrService.deletePMPropertyOwnerAddress(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pm/createPMPropertyOwnerAddress</b>
	 * <p>增加业主地址</p>
	 */
	@RequestMapping("createPMPropertyOwnerAddress")
	@RestReturn(value= java.lang.String.class)
	public RestResponse createPMPropertyOwnerAddress(@Valid CreatePropOwnerAddressCommand cmd) {

		propertyMgrService.createPMPropertyOwnerAddress(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * 检查用户对应的具体的权限
     */
    private void checkPrivilege(String ownerType, Long ownerId, Long organizationId, Long privilegeId) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        Long userId = UserContext.current().getUser().getId();
        resolver.checkUserAuthority(userId, ownerType, ownerId, organizationId, privilegeId);
    }

	/**
	 * <b>URL: /pm/getRequestInfo</b>
	 * <p>获取申请的信息</p>
	 */
	@RequestMapping("getRequestInfo")
	@RestReturn(value= GetRequestInfoResponse.class)
	public RestResponse getRequestInfo(GetRequestInfoCommand cmd) {
		RestResponse response = new RestResponse(propertyMgrService.getRequestInfo(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
