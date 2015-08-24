package com.everhomes.organization;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.forum.CancelLikeTopicCommand;
import com.everhomes.forum.DeleteCommentCommand;
import com.everhomes.forum.DeleteTopicCommand;
import com.everhomes.forum.GetTopicCommand;
import com.everhomes.forum.LikeTopicCommand;
import com.everhomes.forum.ListPostCommandResponse;
import com.everhomes.forum.ListTopicCommand;
import com.everhomes.forum.ListTopicCommentCommand;
import com.everhomes.forum.NewCommentCommand;
import com.everhomes.forum.NewTopicCommand;
import com.everhomes.forum.PostDTO;
import com.everhomes.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;

@RestController
@RequestMapping("/org")
public class OrganizationController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

	@Autowired
	private OrganizationService organizationService;

	/**
	 * <b>URL: /org/getUserOwningOrganizations</b>
	 * <p>查询用户加入的政府机构</p>
	 */
	//no use
	@RequestMapping("getUserOwningOrganizations")
	@RestReturn(value=ListOrganizationMemberCommandResponse.class)
	public RestResponse getUserOwningOrganizations() {
		ListOrganizationMemberCommandResponse commandResponse = organizationService.getUserOwningOrganizations();
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/listOrgMembers</b>
	 * <p>查询政府机构成员列表</p>
	 */
	@RequestMapping("listOrgMembers")
	@RestReturn(value=ListOrganizationMemberCommandResponse.class)
	public RestResponse listOrgMembers(@Valid ListOrganizationMemberCommand cmd) {
		ListOrganizationMemberCommandResponse commandResponse = organizationService.listOrgMembers(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/applyOrganizationMember</b>
	 * <p>申请成为组织管理员:注册用户申请自己小区的组织管理员</p>
	 */
	@RequestMapping("applyOrganizationMember")
	@RestReturn(value=String.class)
	public RestResponse applyOrganizationMember(@Valid ApplyOrganizationMemberCommand cmd) {

		organizationService.applyOrganizationMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /org/addOrgMemberByPhone</b>
	 * <p>申请成为组织管理员，直接在orgMember表新增记录</p>
	 */
	@RequestMapping("addOrgMemberByPhone")
	@RestReturn(value=String.class)
	public RestResponse addOrgMemberByPhone(@Valid CreateOrganizationMemberCommand cmd) {
		organizationService.createOrganizationMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/deleteOrganizationMember</b>
	 * <p>删除组织成员：组织成员表的主键来删</p>
	 */
	@RequestMapping("deleteOrganizationMember")
	@RestReturn(value=String.class)
	public RestResponse deleteOrganizationMember(@Valid DeleteOrganizationIdCommand cmd) {
		organizationService.deleteOrganizationMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/createOrganizationCommunity</b>
	 * <p>添加组织管辖的小区，物业和业委不能自己添加</p>
	 */
	@RequestMapping("createOrganizationCommunity")
	@RestReturn(value=String.class)
	public RestResponse createOrganizationCommunity(@Valid CreateOrganizationCommunityCommand cmd) {
		organizationService.createOrganizationCommunity(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /org/deleteOrganizationCommunity</b>
	 * <p>删除组织管辖的小区</p>
	 * @return 
	 */
	@RequestMapping("deleteOrganizationCommunity")
	@RestReturn(value=String.class)
	public RestResponse deleteOrganizationCommunity(@Valid DeleteOrganizationCommunityCommand cmd) {
		organizationService.deleteOrganizationCommunity(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/listOrganizationCommunities</b>
	 * <p>列出某个组织的信息,已管辖小区划分</p>
	 */
	@RequestMapping("listOrganizationCommunities")
	@RestReturn(value=ListOrganizationCommunityCommandResponse.class)
	public RestResponse listOrganizationCommunities(@Valid ListOrganizationCommunityCommand cmd) {
		ListOrganizationCommunityCommandResponse commandResponse = organizationService.listOrganizationCommunities(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/findUserByIndentifier</b>
	 * <p>根据用户token查询用户信息</p>
	 */
	@RequestMapping("findUserByIndentifier")
	@RestReturn(value=UserTokenCommandResponse.class)
	public RestResponse findUserByIndentifier(@Valid UserTokenCommand cmd) {
		UserTokenCommandResponse commandResponse = organizationService.findUserByIndentifier(cmd);
		RestResponse response = new RestResponse(commandResponse);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /org/newOrgTopic</b>
	 * <p>政府人员发帖</p>
	 */
	@RequestMapping("newOrgTopic")
	@RestReturn(value=PostDTO.class)
	public RestResponse newOrgTopic(@Valid NewTopicCommand cmd) {
		PostDTO postDto = organizationService.createTopic(cmd);
		RestResponse response = new RestResponse(postDto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/queryOrgTopicsByCategory</b>
	 * <p>查询的组织可看的帖子列表（仅查询社区论坛），如果不提供类型则查询全部，若指定类型则查询指定类型的</p>
	 */
	@RequestMapping("queryOrgTopicsByCategory")
	@RestReturn(value=ListPostCommandResponse.class)
	public RestResponse queryOrgTopicsByCategory(QueryOrganizationTopicCommand cmd) {
		ListPostCommandResponse  cmdResponse = organizationService.queryTopicsByCategory(cmd);
		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/listOrgTopics</b>
	 * <p>查询指定论坛的帖子列表（不区分类型查询）</p>
	 */
	@RequestMapping("listOrgTopics")
	@RestReturn(value=ListPostCommandResponse.class)
	public RestResponse listOrgTopics(ListTopicCommand cmd) {
		ListPostCommandResponse cmdResponse = organizationService.listTopics(cmd);

		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/deleteOrgTopic</b>
	 * <p>删除指定论坛里的指定帖子（需要有删帖权限）</p>
	 */
	@RequestMapping("deleteOrgTopic")
	@RestReturn(value=String.class)
	public RestResponse deleteOrgTopic(DeleteTopicCommand cmd) {

		// ???
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/deleteOrgComment</b>
	 * <p>删除指定论坛里的指定评论（需要有删评论权限）</p>
	 */
	@RequestMapping("deleteOrgComment")
	@RestReturn(value=String.class)
	public RestResponse deleteOrgComment(DeleteCommentCommand cmd) {

		// ???
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}    

	//行政热线
	/**
	 * <b>URL: /org/createOrgContact</b>
	 * <p>添加组织联系电话</p>
	 * @return 
	 */
	@RequestMapping("createOrgContact")
	@RestReturn(value=String.class)
	public RestResponse createOrgContact(@Valid CreateOrganizationContactCommand cmd) {
		organizationService.createOrgContact(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/updateOrgContact</b>
	 * <p>修改组织联系电话</p>
	 * @return 
	 */
	@RequestMapping("updateOrgContact")
	@RestReturn(value=String.class)
	public RestResponse updateOrgContact(@Valid UpdateOrganizationContactCommand cmd) {
		organizationService.updateOrgContact(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/deleteOrgContact</b>
	 * <p>删除组织联系电话</p>
	 * @return 
	 */
	@RequestMapping("deleteOrgContact")
	@RestReturn(value=String.class)
	public RestResponse deleteOrgContact(@Valid DeleteOrganizationIdCommand cmd) {
		organizationService.deleteOrgContact(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/listOrgContact</b>
	 * <p>查询组织联系电话列表</p>
	 */
	@RequestMapping("listOrgContact")
	@RestReturn(value=ListOrganizationContactCommandResponse.class)
	public RestResponse listOrgContact(@Valid ListOrganizationContactCommand cmd) {
		ListOrganizationContactCommandResponse commandResponse = organizationService.listOrgContact(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/sendOrgMessage</b>
	 * <p>发通知组织管辖的小区用户</p>
	 */
	@RequestMapping("sendOrgMessage")
	@RestReturn(value=String.class)
	public RestResponse sendOrgMessage(SendOrganizationMessageCommand cmd) {
		organizationService.sendOrgMessage(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/getCurrentOrganization</b>
	 * <p>获取用户当前的组织</p>
	 */
	@RequestMapping("getCurrentOrganization")
	@RestReturn(OrganizationDTO.class)
	public RestResponse getCurrentOrganization() throws Exception {
		OrganizationDTO  organization =  organizationService.getUserCurrentOrganization();
		RestResponse response = new RestResponse(organization);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/setCurrentOrganization</b>
	 * <p>设置用户当前组织</p>
	 */
	@RequestMapping("setCurrentOrganization")
	@RestReturn(String.class)
	public RestResponse setCurrentOrganization(@Valid SetCurrentOrganizationCommand cmd) throws Exception {
		organizationService.setCurrentOrganization(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /org/listUserRelatedOrganizations</b>
	 * <p>查询当前用户加入的组织</p>
	 * @param 
	 * @return 
	 */
	@RequestMapping("listUserRelatedOrganizations")
	@RestReturn(value=OrganizationSimpleDTO.class,collection=true)
	public RestResponse listUserRelatedOrganizations(@Valid ListUserRelatedOrganizationsCommand cmd) throws Exception {

		List<OrganizationSimpleDTO> list = organizationService.listUserRelateOrgs(cmd);
		RestResponse response = new RestResponse(list);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/getOrganizationDetails</b>
	 * <p>根据小区Id和组织类型查询对应的组织</p>
	 */
	@RequestMapping("getOrganizationDetails")
	@RestReturn(OrganizationDTO.class)
	public RestResponse getOrganizationDetails(@Valid GetOrgDetailCommand cmd) throws Exception {

		OrganizationDTO org = organizationService.getOrganizationByComunityidAndOrgType(cmd);
		RestResponse response = new RestResponse(org);

		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /org/rejectOrganization</b>
	 */
	@RequestMapping("rejectOrganization")
	@RestReturn(String.class)
	public RestResponse rejectOrganization(@Valid RejectOrganizationCommand cmd) throws Exception {

		int status = organizationService.rejectOrganization(cmd);
		
		RestResponse response = new RestResponse(status);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /org/userExitOrganization</b>
	 * @return 
	 */
	@RequestMapping("userExitOrganization")
	@RestReturn(String.class)
	public RestResponse userExitOrganization(@Valid UserExitOrganizationCommand cmd) throws Exception {

		int status = organizationService.userExitOrganization(cmd);
		RestResponse response = new RestResponse(status);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//move from pm to organization
	
	//1. 根据topicId获取贴详情
	//pm 也有 ： getPmTopic
	/**
	 * <b>URL: /org/getOrgTopic</b>
	 * <p>获取指定论坛里的指定帖子内容</p>
	 */
	@RequestMapping("getOrgTopic")
	@RestReturn(value=PostDTO.class)
	public RestResponse getOrgTopic(GetTopicCommand cmd) {
		PostDTO postDto = organizationService.getTopic(cmd);

		RestResponse response = new RestResponse(postDto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//2. 获取贴评论列表
	//pm listPmTopicComments
	/**
	 * <b>URL: /org/listOrgTopicComments</b>
	 * <p>获取指定论坛里指定帖子下的评论列表</p>
	 */
	@RequestMapping("listOrgTopicComments")
	@RestReturn(value=ListPostCommandResponse.class)
	public RestResponse listOrgTopicComments(@Valid ListTopicCommentCommand cmd) {
		ListPostCommandResponse cmdResponse = organizationService.listTopicComments(cmd);
		
		RestResponse response = new RestResponse(cmdResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//3. 发表评论
	//pm newPmComment
	/**
	 * <b>URL: /org/newOrgComment</b>
	 * <p>创建新评论</p>
	 */
	@RequestMapping("newOrgComment")
	@RestReturn(value=PostDTO.class)
	public RestResponse newOrgComment(@Valid NewCommentCommand cmd) {
		PostDTO postDTO = organizationService.createComment(cmd);

		RestResponse response = new RestResponse(postDTO);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	 
	//4. 赞
	//likeOrgTopic is exist in organizationController,PropertyMgrController is likePmTopic and it same to likeOrgTopic of its comments.
	/**
	 * <b>URL: /org/likeOrgTopic</b>
	 * <p>对指定论坛里的指定帖子点赞</p>
	 * @return 点赞的结果
	 */
	@RequestMapping("likeOrgTopic")
	@RestReturn(value=String.class)
	public RestResponse likeOrgTopic(LikeTopicCommand cmd) {
		organizationService.likeTopic(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	//5. 取消赞
	//cancelLikeOrgTopic is exist in organizationController.it is also in PropertyMgrController,name cancelLikePmTopic.
	/**
	 * <b>URL: /org/cancelLikeOrgTopic</b>
	 * <p>对指定论坛里的指定帖子取消赞</p>
	 * @return 取消赞的结果
	 */
	@RequestMapping("cancelLikeOrgTopic")
	@RestReturn(value=String.class)
	public RestResponse cancelLikeOrgTopic(CancelLikeTopicCommand cmd) {
		organizationService.cancelLikeTopic(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//6. 拉报修贴列表
	/**
	 * <b>URL: /org/listTopicsByType</b>
	 * <p>查询指定帖子类型的帖子列表</p>
	 */
	@RequestMapping("listTopicsByType")
	@RestReturn(value=ListTopicsByTypeCommandResponse.class)
	public RestResponse listTopicsByType(ListTopicsByTypeCommand cmd) {
		ListTopicsByTypeCommandResponse result= organizationService.listTopicsByType(cmd);//forumService.getTopic(cmd)
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	//7. 报修贴分配人员
	/**
	 * <b>URL: /org/assignOrgTopic</b>
	 * <p>指派帖子给相关人员处理</p>
	 */
	@RequestMapping("assignOrgTopic")
	@RestReturn(value=String.class)
	public RestResponse assignOrgTopic(@Valid AssginOrgTopicCommand cmd) {
		organizationService.assignOrgTopic(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//8. 报修贴修改状态
	/**
	 * <b>URL: /org/setOrgTopicStatus</b>
	 * <p>设置帖状态：未处理、处理中、已处理、其它</p>
	 */
	@RequestMapping("setOrgTopicStatus")
	@RestReturn(value=String.class)
	public RestResponse setOrgTopicStatus(@Valid SetOrgTopicStatusCommand cmd) {
		organizationService.setOrgTopicStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//9. 人员管理通过管理员
	/**
	 * <b>URL: /org/approveOrganizationMember</b>
	 * <p>批准成员成为组织管理员</p>
	 */
	@RequestMapping("approveOrganizationMember")
	@RestReturn(value=String.class)
	public RestResponse approveOrganizationMember(@Valid OrganizationMemberCommand cmd) {
		organizationService.approveOrganizationMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	//10. 人员管理驳回管理员
	/**
	 * <b>URL: /org/rejectOrganizationMember</b>
	 * <p>拒绝成员成为组织成员</p>
	 */
	@RequestMapping("rejectOrganizationMember")
	@RestReturn(value=String.class)
	public RestResponse rejectOrganizationMember(@Valid OrganizationMemberCommand cmd) {
		organizationService.rejectOrganizationMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>/org/deleteOrgMember</b>
	 * <p>删除组织成员</p>
	 */
	@RequestMapping("deleteOrgMember")
	@RestReturn(value=String.class)
	public RestResponse deleteOrgMember(@Valid OrganizationMemberCommand cmd) {
		organizationService.deleteOrgMember(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /org/userJoinOrganization</b>
	 * <p>用户加入组织，若组织不存在，则创建</p>
	 */
	@RequestMapping("userJoinOrganization")
	@RestReturn(value=String.class)
	public RestResponse userJoinOrganization(@Valid UserJoinOrganizationCommand cmd){
		this.organizationService.userJoinOrganization(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
		
	}
	
    /**
     * <b>URL: /org/updateTopicPrivacy</b>
     * <p>设置帖子是否公开</p>
     */
    @RequestMapping("updateTopicPrivacy")
    @RestReturn(value=String.class)
    public RestResponse updateTopicPrivacy(UpdateTopicPrivacyCommand cmd){
        this.organizationService.updateTopicPrivacy(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
        
    }
	
}
