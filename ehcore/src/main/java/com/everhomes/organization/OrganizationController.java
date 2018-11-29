// @formatter:off
package com.everhomes.organization;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.XssExclude;
import com.everhomes.controller.XssCleaner;
import com.everhomes.controller.XssExclude;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.ListBuildingsByKeywordAndNameSpaceCommand;
import com.everhomes.rest.archives.UpdateArchivesEmployeeCommand;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.CreateResourceCategoryCommand;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.group.GetRemainBroadcastCountCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.FrequencyControl;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/org")
public class OrganizationController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationSearcher organizationSearcher;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    /**
     * <b>URL: /org/getUserOwningOrganizations</b>
     * <p>查询用户加入的政府机构</p>
     */
    //checked
    //no use
    @RequestMapping("getUserOwningOrganizations")
    @RestReturn(value = ListOrganizationMemberCommandResponse.class)
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
	//checked
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
    //checked
    @RequestMapping("applyOrganizationMember")
    @RestReturn(value = String.class)
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
    //checked
    @RequestMapping("addOrgMemberByPhone")
    @RestReturn(value = String.class)
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
    //checked
    @RequestMapping("deleteOrganizationMember")
    @RestReturn(value = String.class)
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
    //checked
    @RequestMapping("createOrganizationCommunity")
    @RestReturn(value = String.class)
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
     */
    //checked
    @RequestMapping("deleteOrganizationCommunity")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationCommunity(@Valid DeleteOrganizationCommunityCommand cmd) {
        organizationService.deleteOrganizationCommunity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrganizationCommunities</b>
     * <p>根据orgId列出org和comm的信息</p>
     */
    //checked
    @RequestMapping("listOrganizationCommunities")
    @RestReturn(value = ListOrganizationCommunityCommandResponse.class)
    public RestResponse listOrganizationCommunities(@Valid ListOrganizationCommunityCommand cmd) {
        ListOrganizationCommunityCommandResponse commandResponse = organizationService.listOrganizationCommunities(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrganizationCommunitiesV2</b>
     * <p>根据机构ID列出机构所管辖的小区信息，与listOrganizationCommunities的区别是返回值是CommunityDTO</p>
     */
    //checked
    @RequestMapping("listOrganizationCommunitiesV2")
    @RestReturn(value = ListOrganizationCommunityCommandResponse.class)
    public RestResponse listOrganizationCommunitiesV2(@Valid ListOrganizationCommunityCommand cmd) {
        ListOrganizationCommunityV2CommandResponse commandResponse = organizationService.listOrganizationCommunitiesV2(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/findUserByIndentifier</b>
     * <p>根据用户token查询用户信息</p>
     */
    //checked
    @RequestMapping("findUserByIndentifier")
    @RestReturn(value = UserTokenCommandResponse.class)
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
    //checked
    @XssExclude
    @RequestMapping("newOrgTopic")
    @RestReturn(value = PostDTO.class)
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
    //checked
    @RequestMapping("queryOrgTopicsByCategory")
    @RestReturn(value = ListPostCommandResponse.class)
    @RequireAuthentication(false)
    public RestResponse queryOrgTopicsByCategory(QueryOrganizationTopicCommand cmd) {

		/*是PM_ADMIN的场景下*/
        if (null != cmd.getOrganizationId()) {
            /**
             * 校验权限
             */
//			SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//			resolver.checkUserAuthority(UserContext.current().getUser().getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.NoticeManagementPost);
        }
        ListPostCommandResponse cmdResponse = organizationService.queryTopicsByCategory(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrgTopics</b>
     * <p>查询指定论坛的帖子列表（不区分类型查询）</p>
     */
    //checked
    @RequestMapping("listOrgTopics")
    @RestReturn(value = ListPostCommandResponse.class)
    public RestResponse listOrgTopics(ListTopicCommand cmd) {
        ListPostCommandResponse cmdResponse = organizationService.listTopics(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrgMixTopics</b>
     * <p>查询指定机构的所有帖子列表</p>
     */
    //checked
    @RequestMapping("listOrgMixTopics")
    @RestReturn(value = ListPostCommandResponse.class)
    public RestResponse listOrgMixTopics(ListOrgMixTopicCommand cmd) {
        ListPostCommandResponse cmdResponse = organizationService.listOrgMixTopics(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/deleteOrgTopic</b>
     * <p>删除指定论坛里的指定帖子（需要有删帖权限）</p>
     */
    /*@RequestMapping("deleteOrgTopic")
    @RestReturn(value=String.class)
	public RestResponse deleteOrgTopic(DeleteTopicCommand cmd) {
		// ???
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/

	/**
	 * <b>URL: /org/deleteOrgComment</b>
	 * <p>删除指定论坛里的指定评论（需要有删评论权限）</p>
	 */
	/*@RequestMapping("deleteOrgComment")
	@RestReturn(value=String.class)
	public RestResponse deleteOrgComment(DeleteCommonCommentCommand cmd) {

		// ???
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}*/    

	//行政热线
	/**
	 * <b>URL: /org/createOrgContact</b>
	 * <p>添加组织联系电话</p>
	 */
	//checked
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
     */
    //checked
    @RequestMapping("updateOrgContact")
    @RestReturn(value = String.class)
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
     */
    //checked
    @RequestMapping("deleteOrgContact")
    @RestReturn(value = String.class)
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
    //checked
    @RequestMapping("listOrgContact")
    @RestReturn(value = ListOrganizationContactCommandResponse.class)
    public RestResponse listOrgContact(@Valid ListOrganizationContactCommand cmd) {
        ListOrganizationContactCommandResponse commandResponse = organizationService.listOrgContact(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/sendOrgMessage</b>
     * <p>发消息给组织管辖的小区用户</p>
     */
    //checked
    @RequestMapping("sendOrgMessage")
    @RestReturn(value = String.class)
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
    //checked
    @RequestMapping("getCurrentOrganization")
    @RestReturn(OrganizationDTO.class)
    public RestResponse getCurrentOrganization() throws Exception {
        OrganizationDTO organization = organizationService.getUserCurrentOrganization();
        RestResponse response = new RestResponse(organization);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/setCurrentOrganization</b>
     * <p>设置用户当前组织</p>
     */
    //checked
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
     */
    //checked
    @RequestMapping("listUserRelatedOrganizations")
    @RestReturn(value = OrganizationSimpleDTO.class, collection = true)
    public RestResponse listUserRelatedOrganizations(@Valid ListUserRelatedOrganizationsCommand cmd) throws Exception {

        List<OrganizationSimpleDTO> list = organizationService.listUserRelateOrganizations(cmd);
        RestResponse response = new RestResponse(list);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/getOrganizationDetails</b>
     * <p>根据小区Id和组织类型查询对应的组织</p>
     */
    //checked
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
     * <b>URL: /org/getOrganizationDetailById</b>
     * <p>根据Id查询对应的企业详情信息</p>
     */
    //checked
    @RequestMapping("getOrganizationDetailById")
    @RestReturn(OrganizationDetailDTO.class)
    public RestResponse getOrganizationDetailById(GetOrganizationDetailByIdCommand cmd){

        OrganizationDetailDTO org = organizationService.getOrganizationDetailById(cmd);
        RestResponse response = new RestResponse(org);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/rejectOrganization</b>
     */
    //checked
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
     *
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
    @RestReturn(value = PostDTO.class)
    @RequireAuthentication(false)
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
    @RestReturn(value = ListPostCommandResponse.class)
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
    @RestReturn(value = PostDTO.class)
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
     *
     * @return 点赞的结果
     */
    @RequestMapping("likeOrgTopic")
    @RestReturn(value = String.class)
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
     *
     * @return 取消赞的结果
     */
    @RequestMapping("cancelLikeOrgTopic")
    @RestReturn(value = String.class)
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
    @RestReturn(value = ListTopicsByTypeCommandResponse.class)
    public RestResponse listTopicsByType(ListTopicsByTypeCommand cmd) {
        ListTopicsByTypeCommandResponse result = organizationService.listTopicsByType(cmd);//forumService.getTopic(cmd)
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
    @RestReturn(value = String.class)
    public RestResponse assignOrgTopic(@Valid AssginOrgTopicCommand cmd) {
        organizationService.assignOrgTopic(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /org/listAllPmOrganizations</b>
	 * <p>查询 所有域空间物业管理公司</p>
	 */
	@RequestMapping("listAllPmOrganizations")
	@RestReturn(value=OrganizationTreeDTO.class, collection = true)
	public RestResponse listAllPmOrganizations(){
		//TODO:加权限校验
		RestResponse res = new RestResponse(organizationService.listAllPmOrganizations());
		res.setErrorCode(ErrorCodes.SUCCESS);
		res.setErrorDescription("OK");
		return res;
	}

    /**
     * <b>URL: /org/listPmOrganizationsByNamespaceId</b>
     * <p>查询 域空间的PM公司</p>
     */
    @RequestMapping("listPmOrganizationsByNamespaceId")
    @RestReturn(value=OrganizationTreeDTO.class)
    public RestResponse listPmOrganizationsByNamespaceId(ListBuildingsByKeywordAndNameSpaceCommand cmd){
        //TODO:加权限校验
        RestResponse res = new RestResponse(organizationService.listPmOrganizationsByNamespaceId(cmd.getNamespaceId()));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }


    /**
     * <b>URL: /org/setOrgTopicStatus</b>
     * <p>设置帖状态：待处理、处理中、已处理、其它</p>
     */
    @RequestMapping("setOrgTopicStatus")
    @RestReturn(value = String.class)
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
    @RestReturn(value = String.class)
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
    @RestReturn(value = String.class)
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
    @RestReturn(value = String.class)
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
    @RestReturn(value = String.class)
    public RestResponse userJoinOrganization(@Valid UserJoinOrganizationCommand cmd) {
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
    @RestReturn(value = String.class)
    public RestResponse updateTopicPrivacy(UpdateTopicPrivacyCommand cmd) {
        this.organizationService.updateTopicPrivacy(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listUserTask</b>
     * <p>查询分配给自己的任务</p>
     */
    @RequestMapping("listUserTask")
    @RestReturn(value = ListTopicsByTypeCommandResponse.class)
    public RestResponse listUserTask(ListUserTaskCommand cmd) {
        ListTopicsByTypeCommandResponse tasks = this.organizationService.listUserTask(cmd);

        RestResponse response = new RestResponse(tasks);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/searchTopicsByType</b>
     * <p>搜索任务贴</p>
     */
    @RequestMapping("searchTopicsByType")
    @RestReturn(value = SearchTopicsByTypeResponse.class)
    public RestResponse searchTopicsByType(SearchTopicsByTypeCommand cmd) {
        SearchTopicsByTypeResponse result = organizationService.searchTopicsByType(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /org/searchEnterprise</b>
     * <p>搜索企业</p>
     */
    @RequestMapping("searchEnterprise")
    @RestReturn(value = OrganizationDetailDTO.class, collection = true)
    public RestResponse searchEnterprise(@Valid SearchOrganizationCommand cmd) {
        ListEnterprisesCommandResponse res = organizationService.searchEnterprise(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/searchOrganization</b>
     * <p>搜索 机构</p>
     */
    @RequestMapping("searchOrganization")
    @RestReturn(value = SearchOrganizationCommandResponse.class, collection = true)
    public RestResponse searchOrganization(@Valid SearchOrganizationCommand cmd) {
        SearchOrganizationCommandResponse res = organizationService.searchOrganization(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /org/listEnterprises</b>
     * <p>企业列表</p>
     */
    @RequestMapping("listEnterprises")
    @RestReturn(value = OrganizationDetailDTO.class, collection = true)
    public RestResponse listEnterprises(@Valid ListEnterprisesCommand cmd) {
        cmd.setQryAdminRoleFlag(false);
        ListEnterprisesCommandResponse res = organizationService.listEnterprises(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/applyForEnterpriseContact</b>
     * <p>申请加入企业</p>
     */
    @FrequencyControl(count = 1 , key={"#cmd.organizationId","#cmd.targetId","cmd.contactToken", "cmd.contactName"})
    @RequestMapping("applyForEnterpriseContact")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse applyForEnterpriseContact(@Valid CreateOrganizationMemberCommand cmd) {
        OrganizationDTO dto = organizationService.applyForEnterpriseContact(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrganizationsByEmail</b>
     * <p>通过email域名搜索企业 </p>
     */
    @RequestMapping("listOrganizationsByEmail")
    @RestReturn(value = OrganizationDTO.class, collection = true)
    public RestResponse listOrganizationsByEmail(@Valid ListOrganizationsByEmailCommand cmd) {
        List<OrganizationDTO> dtos = organizationService.listOrganizationsByEmail(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/applyForEnterpriseContactByEmail</b>
     * <p>通过email域名搜索企业 </p>
     */
    @RequestMapping("applyForEnterpriseContactByEmail")
    @RestReturn(value = String.class)
    public RestResponse applyForEnterpriseContactByEmail(@Valid ApplyForEnterpriseContactByEmailCommand cmd) {
        organizationService.applyForEnterpriseContactByEmail(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/applyForEnterpriseContactNew</b>
     * <p>新申请加入企业</p>
     */
    @RequestMapping("applyForEnterpriseContactNew")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse applyForEnterpriseContactNew(@Valid ApplyForEnterpriseContactNewCommand cmd) {
        OrganizationDTO dto = organizationService.applyForEnterpriseContactNew(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/verifyEnterpriseContact</b>
     * <p>通过点击邮箱认证通过认证申请</p>
     *
     * @return {@link String}
     * @throws IOException
     */
    @RequestMapping("verifyEnterpriseContact")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public void verifyEnterpriseContact(@Valid VerifyEnterpriseContactCommand cmd, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl = this.organizationService.verifyEnterpriseContact(cmd);
        response.sendRedirect(redirectUrl);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("controller verifyEnterpriseContact redirectUrl = {}", redirectUrl);
        }
    }

    /**
     * <b>URL: /org/leaveForEnterpriseContact</b>
     * <p>退出指定企业</p>
     */
    @RequestMapping("leaveForEnterpriseContact")
    @RestReturn(value = String.class)
    public RestResponse leaveForEnterpriseContact(@Valid LeaveEnterpriseCommand cmd) {

        this.organizationService.leaveForEnterpriseContact(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listCommunitiesByOrganizationId</b>
     * <p>机构官署的小区</p>
     */
    @RequestMapping("listCommunitiesByOrganizationId")
    @RestReturn(value = ListCommunityByNamespaceCommandResponse.class)
    public RestResponse listCommunitiesByOrganizationId(@Valid ListCommunitiesByOrganizationIdCommand cmd) {
        ListCommunityByNamespaceCommandResponse res = new ListCommunityByNamespaceCommandResponse();
        res.setCommunities(organizationService.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId()));
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/syncIndex</b>
     * <p>搜索索引同步 TODO: 求敢哥优化</p>
     *
     * @return {String.class}
     */
    @RequestMapping("syncIndex")
    @RestReturn(value = String.class)
    public RestResponse syncIndex() {
        organizationSearcher.syncFromDb();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listUserRelatedEnterprises</b>
     * <p>列出个人相关的企业</p>
     *
     * @return {@link OrganizationDetailDTO}
     */
    @RequestMapping("listUserRelatedEnterprises")
    @RestReturn(value = OrganizationDetailDTO.class, collection = true)
    public RestResponse listUserRelatedEnterprises(@Valid ListUserRelatedEnterprisesCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listUserRelateEnterprises(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /org/listOrganizationTopics</b>
     * <p>机构人员的帖子查询</p>
     *
      * @return
     */
    @RequestMapping("listOrganizationTopics")
    @RestReturn(value = ListPostCommandResponse.class)
    @RequireAuthentication(false)
    public RestResponse listOrgTopics(@Valid QueryOrganizationTopicCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrgTopics(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /org/checkOfficalPrivilegeByScene</b>
     * <p>检查是否具有官方的权限</p>
     */
    @RequestMapping("checkOfficalPrivilegeByScene")
    @RestReturn(value = CheckOfficalPrivilegeResponse.class)
    public RestResponse checkOfficalPrivilegeByScene(CheckOfficalPrivilegeBySceneCommand cmd) {
        RestResponse res = new RestResponse(organizationService.checkOfficalPrivilegeByScene(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /org/checkOfficalPrivilege</b>
     * <p>检查是否具有官方的权限</p>
     */
    @RequestMapping("checkOfficalPrivilege")
    @RestReturn(value = CheckOfficalPrivilegeResponse.class)
    public RestResponse checkOfficalPrivilege(CheckOfficalPrivilegeCommand cmd) {
        RestResponse res = new RestResponse(organizationService.checkOfficalPrivilege(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /org/listOrganizationPersonnelsByRoleIds</b>
     * <p>查看角色人员</p>
     */
    @RequestMapping("listOrganizationPersonnelsByRoleIds")
    @RestReturn(value = ListOrganizationMemberCommandResponse.class)
    public RestResponse listOrganizationPersonnelsByRoleIds(ListOrganizationPersonnelByRoleIdsCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationPersonnelsByRoleIds(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /org/updateOrganizationContactVisibleFlag</b>
     * <p>修改隐藏显示通讯录</p>
     */
    @RequestMapping("updateOrganizationContactVisibleFlag")
    @RestReturn(value = String.class)
    public RestResponse updateOrganizationContactVisibleFlag(UpdateOrganizationContactVisibleFlagCommand cmd) {
        organizationService.updateOrganizationContactVisibleFlag(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/batchUpdateOrganizationContactVisibleFlag</b>
     * <p>批量 修改隐藏显示通讯录</p>
     */
    @RequestMapping("batchUpdateOrganizationContactVisibleFlag")
    @RestReturn(value = String.class)
    public RestResponse batchUpdateOrganizationContactVisibleFlag(BatchUpdateOrganizationContactVisibleFlagCommand cmd) {
        organizationService.batchUpdateOrganizationContactVisibleFlag(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listAllTreeOrganizations</b>
     * <p>查询 机构子树形结构</p>
     */
    @RequestMapping("listAllTreeOrganizations")
    @RestReturn(value = OrganizationTreeDTO.class)
    public RestResponse listAllTreeOrganizations(ListAllTreeOrganizationsCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listAllTreeOrganizations(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /*
     * <b>URL: /org/listOrganizationContacts</b>
     * <p>通讯录（停用 at 2018/04/06）</p>
     */
    @RequestMapping("listOrganizationContacts")
    @RestReturn(value = ListOrganizationContactCommandResponse.class)
    public RestResponse listOrganizationContacts(ListOrganizationContactCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationContacts(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/getContactTopDepartment</b>
     * <p>获取用户在当前机构的最高部门</p>
     */
    @RequestMapping("getContactTopDepartment")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse getContactTopDepartment(GetContactTopDepartmentCommand cmd) {
        RestResponse res = new RestResponse(organizationService.getContactTopDepartment(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/createChildrenOrganizationJobPosition</b>
     * <p>创建子机构岗位</p>
     */
    @RequestMapping("createChildrenOrganizationJobPosition")
    @RestReturn(value = String.class)
    public RestResponse createChildrenOrganizationJobPosition(@Valid CreateOrganizationCommand cmd) {
        organizationService.createChildrenOrganizationJobPosition(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/updateChildrenOrganizationJobPosition</b>
     * <p>修改子机构岗位</p>
     */
    @RequestMapping("updateChildrenOrganizationJobPosition")
    @RestReturn(value = String.class)
    public RestResponse updateChildrenOrganizationJobPosition(@Valid UpdateOrganizationsCommand cmd) {
        organizationService.updateChildrenOrganizationJobPosition(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/deleteChildrenOrganizationJobPosition</b>
     * <p>删除子机构岗位</p>
     */
    @RequestMapping("deleteChildrenOrganizationJobPosition")
    @RestReturn(value = String.class)
    public RestResponse deleteChildrenOrganizationJobPosition(@Valid DeleteOrganizationIdCommand cmd) {
        RestResponse response = new RestResponse(organizationService.deleteChildrenOrganizationJobPosition(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listChildrenOrganizationJobPositions</b>
     * <p>子机构岗位列表</p>
     */
    @RequestMapping("listChildrenOrganizationJobPositions")
    @RestReturn(value = ListChildrenOrganizationJobPositionResponse.class)
    public RestResponse listChildrenOrganizationJobPositions(@Valid ListAllChildrenOrganizationsCommand cmd) {
        ListChildrenOrganizationJobPositionResponse resp = organizationService.listChildrenOrganizationJobPositions(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/createOrganizationJobPosition</b>
     * <p>创建通用岗位</p>
     */
    @RequestMapping("createOrganizationJobPosition")
    @RestReturn(value = String.class)
    public RestResponse createOrganizationJobPosition(@Valid CreateOrganizationJobPositionCommand cmd) {
        organizationService.createOrganizationJobPosition(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/updateOrganizationJobPosition</b>
     * <p>修改通用岗位</p>
     */
    @RequestMapping("updateOrganizationJobPosition")
    @RestReturn(value = String.class)
    public RestResponse updateOrganizationJobPosition(@Valid UpdateOrganizationJobPositionCommand cmd) {
        RestResponse response = new RestResponse(organizationService.updateOrganizationJobPosition(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL:
     * /org/deleteOrganizationJobPosition</b>
     * <p>删除通用岗位</p>
     */
    @RequestMapping("deleteOrganizationJobPosition")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationJobPosition(@Valid DeleteOrganizationIdCommand cmd) {
        RestResponse response = new RestResponse(organizationService.deleteOrganizationJobPosition(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrganizationJobPositions</b>
     * <p>通用岗位列表</p>
     */
    @RequestMapping("listOrganizationJobPositions")
    @RestReturn(value = ListOrganizationJobPositionResponse.class)
    public RestResponse listOrganizationJobPositions(@Valid ListOrganizationJobPositionCommand cmd) {
        ListOrganizationJobPositionResponse resp = organizationService.listOrganizationJobPositions(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /org/createChildrenOrganizationJobLevel</b>
     * <p>创建子机构职级</p>
     */
    @RequestMapping("createChildrenOrganizationJobLevel")
    @RestReturn(value = String.class)
    public RestResponse createChildrenOrganizationJobLevel(@Valid CreateOrganizationCommand cmd) {
        organizationService.createChildrenOrganizationJobLevel(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/updateChildrenOrganizationJobLevel</b>
     * <p>修改子机构职级</p>
     */
    @RequestMapping("updateChildrenOrganizationJobLevel")
    @RestReturn(value = String.class)
    public RestResponse updateChildrenOrganizationJobLevel(@Valid UpdateOrganizationsCommand cmd) {
        organizationService.updateChildrenOrganizationJobLevel(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/deleteChildrenOrganizationJobLevel</b>
     * <p>删除子机构职级</p>
     */
    @RequestMapping("deleteChildrenOrganizationJobLevel")
    @RestReturn(value = String.class)
    public RestResponse deleteChildrenOrganizationJobLevel(@Valid DeleteOrganizationIdCommand cmd) {
        RestResponse response = new RestResponse(organizationService.deleteChildrenOrganizationJobLevel(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listChildrenOrganizationJobLevels</b>
     * <p>子机构职级列表</p>
     */
    @RequestMapping("listChildrenOrganizationJobLevels")
    @RestReturn(value = ListChildrenOrganizationJobLevelResponse.class)
    public RestResponse listChildrenOrganizationJobLevels(@Valid ListAllChildrenOrganizationsCommand cmd) {
        ListChildrenOrganizationJobLevelResponse resp = organizationService.listChildrenOrganizationJobLevels(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/getUserRelatedEnterprises</b>
     * <p>获取用户所属公司</p>
     */
    @RequestMapping("getUserRelatedEnterprises")
    @RestReturn(value = OrganizationDTO.class, collection = true)
    public RestResponse getUserRelatedEnterprises() {

        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<OrganizationDTO> enterpriseDtos = organizationService.listUserRelateOrganizations(namespaceId, user.getId(), OrganizationGroupType.ENTERPRISE);

        RestResponse res = new RestResponse(enterpriseDtos);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listCommunityOrganizationTree</b>
     * <p>获取小区与小区下属企业的树形结构数据</p>
     */
    @RequestMapping("listCommunityOrganizationTree")
    @RestReturn(value = CommunityOrganizationTreeResponse.class)
    public RestResponse listCommunityOrganizationTree(@Valid ListCommunityOrganizationTreeCommand cmd) {
        CommunityOrganizationTreeResponse res = organizationService.listCommunityOrganizationTree(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrganizationsByModuleId</b>
     * <p>获取获取业务部门的机构</p>
     */
    @RequestMapping("listOrganizationsByModuleId")
    @RestReturn(value = OrganizationDTO.class, collection = true)
    public RestResponse listOrganizationsByModuleId(ListOrganizationByModuleIdCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationsByModuleId(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listOrganizationsContactByModuleId</b>
     * <p>获取获取业务部门的机构人员</p>
     */
    @RequestMapping("listOrganizationsContactByModuleId")
    @RestReturn(value = OrganizationContactDTO.class, collection = true)
    public RestResponse listOrganizationsContactByModuleId(ListOrganizationByModuleIdCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationsContactByModuleId(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listOrganizationContactByJobPositionId</b>
     * <p>根据通用岗位获取人员</p>
     */
    @RequestMapping("listOrganizationContactByJobPositionId")
    @RestReturn(value = OrganizationContactDTO.class, collection = true)
    public RestResponse listOrganizationContactByJobPositionId(ListOrganizationContactByJobPositionIdCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationContactByJobPositionId(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listModuleOrganizationContactByJobPositionId</b>
     * <p>获取通用岗位业务责任部门人员</p>
     */
    @RequestMapping("listModuleOrganizationContactByJobPositionId")
    @RestReturn(value = OrganizationContactDTO.class, collection = true)
    public RestResponse listModuleOrganizationContactByJobPositionId(ListModuleOrganizationContactByJobPositionIdCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listModuleOrganizationContactByJobPositionId(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listOrganizationManagers</b>
     * <p>获取机构经理人员</p>
     */
    @RequestMapping("listOrganizationManagers")
    @RestReturn(value = OrganizationManagerDTO.class, collection = true)
    public RestResponse listOrganizationManagers(ListOrganizationManagersCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationManagers(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listOrganizationAllManagers</b>
     * <p>获取机构所有经理人员</p>
     */
    @RequestMapping("listOrganizationAllManagers")
    @RestReturn(value = OrganizationManagerDTO.class, collection = true)
    public RestResponse listOrganizationAllManagers(ListOrganizationManagersCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationAllManagers(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listModuleOrganizationManagers</b>
     * <p>获取模块负责机构的经理人员</p>
     */
    @RequestMapping("listModuleOrganizationManagers")
    @RestReturn(value = OrganizationManagerDTO.class, collection = true)
    public RestResponse listModuleOrganizationManagers(ListOrganizationByModuleIdCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listModuleOrganizationManagers(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/getOrganizationActiveCommunityId</b>
     * <p>获取企业所在园区id</p>
     */
    @RequestMapping("getOrganizationActiveCommunityId")
    @RestReturn(value = Long.class)
    public RestResponse getOrganizationActiveCommunityId(GetOrganizationActiveCommunityId cmd) {
        RestResponse res = new RestResponse(organizationService.getOrganizationActiveCommunityId(cmd.getOrganizationId()));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }


    /**
     * <b>URL: /org/listUserRelatedOrganizationAddresses</b>
     * <p>获取用户机构地址</p>
     */
    @RequestMapping("listUserRelatedOrganizationAddresses")
    @RestReturn(value = OrgAddressDTO.class, collection = true)
    public RestResponse listUserRelatedOrganizationAddresses(ListUserRelatedOrganizationAddressesCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listUserRelatedOrganizationAddresses(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /org/listOrganizationAddresses</b>
     * <p>获取机构地址</p>
     */
    @RequestMapping("listOrganizationAddresses")
    @RestReturn(value = OrgAddressDTO.class, collection = true)
    public RestResponse listOrganizationAddresses(ListOrganizationAddressesCommand cmd) {
        RestResponse res = new RestResponse(organizationService.listOrganizationAddresses(cmd));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

/*New*/

    /**
     * <b>URL: /org/leaveTheJob</b>
     * <p>人事档案离职</p>
     */
    @RequestMapping("leaveTheJob")
    @RestReturn(value = String.class)
    public RestResponse leaveTheJob(@Valid LeaveTheJobCommand cmd) {
        this.organizationService.leaveTheJob(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/syncOrganizationMemberStatus</b>
     * <p>同步员工状态</p>
     */
    @RequestMapping("syncOrganizationMemberStatus")
    @RestReturn(value = String.class)
    public RestResponse syncOrganizationMemberStatus() {
        ListOrganizationMemberCommandResponse members = this.organizationService.syncOrganizationMemberStatus();
        RestResponse response = new RestResponse(members);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /org/sortOrganizationsAtSameLevel</b>
     * <p>同级节点排序</p>
     */
    @RequestMapping("sortOrganizationsAtSameLevel")
    @RestReturn(value = String.class)
    public RestResponse sortOrganizationsAtSameLevel(SortOrganizationsAtSameLevelCommand cmd) {
        this.organizationService.sortOrganizationsAtSameLevel(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/findOrgPersonel</b>
     * <p>超级查询接口</p>
     */
    @RequestMapping("findOrgPersonel")
    @RestReturn(value = FindOrgPersonelCommandResponse.class)
    public RestResponse findOrgPersonel(FindOrgPersonelCommand cmd) {
        FindOrgPersonelCommandResponse res = this.organizationService.findOrgPersonel(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listOrganizationPersonnelsWithDownStream</b>
     * <p>组织架构查询人员接口/p>
     */
    @RequestMapping("listOrganizationPersonnelsWithDownStream")
    @RestReturn(value = FindOrgPersonelCommandResponse.class)
    public RestResponse listOrganizationPersonnelsWithDownStream(ListOrganizationContactCommand cmd) {
        ListOrganizationMemberCommandResponse res = this.organizationService.listOrganizationPersonnelsWithDownStream(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/findOrgByName</b>
     * <p>根据名字查部门</p>
     */
    @RequestMapping("findOrgByName")
    @RestReturn(value = String.class)
    public RestResponse findOrgByName(CreateResourceCategoryCommand cmd) {
        this.organizationService.getOrganizationNameByNameAndType(cmd.getName(), "DEPARTMENT");
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/deleteChildrenOrganizationAsList</b>
     * <p>批量删除子机构(职级或部门岗位)</p>
     */
    @RequestMapping("deleteChildrenOrganizationAsList")
    @RestReturn(value = String.class)
    public RestResponse deleteChildrenOrganizationAsList(@Valid DeleteChildrenOrganizationAsListCommand cmd) {
        RestResponse response = new RestResponse(organizationService.deleteChildrenOrganizationAsList(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL:
     * /org/deleteOrganizationJobPositionsByPositionIdAndDetails</b>
     * <p>批量撤销通用岗位下的人员</p>
     */
    @RequestMapping("deleteOrganizationJobPositionsByPositionIdAndDetails")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationJobPositionsByPositionIdAndDetails(@Valid DeleteOrganizationJobPositionsByPositionIdAndDetailsCommand cmd) {
        organizationService.deleteOrganizationJobPositionsByPositionIdAndDetails(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
     * <b>URL: /org/cleanWrongStatusOrganizationMembers</b>
     * <p>同步失效的organizaitonMember记录</p>
     */
    @RequestMapping("cleanWrongStatusOrganizationMembers")
    @RestReturn(value = String.class)
    public RestResponse cleanWrongStatusOrganizationMembers(GetRemainBroadcastCountCommand cmd) {
        Integer count = this.organizationService.cleanWrongStatusOrganizationMembers(cmd.getNamespaceId());
        RestResponse response = new RestResponse(count);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/modifyPhoneNumberByDetailId</b>
     * <p>修改未激活人员的手机号</p>
     */
    @RequestMapping("modifyPhoneNumberByDetailId")
    @RestReturn(value = Long.class)
    public RestResponse modifyPhoneNumberByDetailId(@Valid UpdateArchivesEmployeeCommand cmd) {
        RestResponse response = new RestResponse(organizationService.modifyPhoneNumberByDetailId(cmd.getDetailId(), cmd.getContactToken()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/setOrganizationDetailFlag</b>
     * <p>设置是否开启查看园区企业详情开关</p>
     */
    @RequestMapping("setOrganizationDetailFlag")
    @RestReturn(value = Byte.class)
    public RestResponse setOrganizationDetailFlag(@Valid SetOrganizationDetailFlagCommand cmd) {
        RestResponse response = new RestResponse(organizationService.setOrganizationDetailFlag(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/getOrganizationDetailFlag</b>
     * <p>查看是否开启查看园区企业详情</p>
     */
    @RequestMapping("getOrganizationDetailFlag")
    @RestReturn(value = Byte.class)
    public RestResponse getOrganizationDetailFlag(@Valid GetOrganizationDetailFlagCommand cmd) {
        RestResponse response = new RestResponse(organizationService.getOrganizationDetailFlag(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listPMOrganizations</b>
     * <p>查询管理公司列表</p>
     */
    @RequestMapping("listPMOrganizations")
    @RestReturn(value = ListPMOrganizationsResponse.class)
    @RequireAuthentication(false)
    public RestResponse listPMOrganizations(@Valid ListPMOrganizationsCommand cmd) {
        RestResponse response = new RestResponse(organizationService.listPMOrganizations(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    /**
//     * <b>URL: /org/listOrganizationsByUser</b>
//     * <p>获取用户所在的已认证公司及公司与人的相关信息</p>
//     */
//    @RequestMapping("listOrganizationsByUser")
//    @RestReturn(value = ListOrganizationsByUserResponse.class)
//    public RestResponse listOrganizationsByUser() {
//        List<OrganizationUserDTO> res = null;
//        RestResponse response = new RestResponse(res);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

    /**
     * <b>URL: /org/listEnterpriseByNamespaceIds</b>
     * <p>查询域空间下公司列表</p>
     */
    @RequestMapping("listEnterpriseByNamespaceIds")
    @RestReturn(value = ListPMOrganizationsResponse.class)
    public RestResponse listEnterpriseByNamespaceIds(@Valid ListEnterpriseByNamespaceIdCommand cmd) {
        RestResponse response = new RestResponse(organizationService.listEnterpriseByNamespaceIds(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/destoryOrganizationByOrgId</b>
     * <p>根据企业编号来删除企业信息</p>
     * @param cmd
     * @return
     */
    @RequestMapping(value = "destoryOrganizationByOrgId")
    @RestReturn(value = String.class)
    public RestResponse destoryOrganizationByOrgId(DestoryOrganizationCommand cmd){
        RestResponse response = new RestResponse();
        organizationService.destoryOrganizationByOrgId(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/enterprise/detail</b>
     * <p>查看单个公司的具体属性(标准版)</p>
     */
    @RequestMapping("/enterprise/detail")
    @RestReturn(value = EnterpriseDTO.class)
    public RestResponse findEnterpriseDetail(@Valid FindEnterpriseDetailCommand cmd) {
        RestResponse response = new RestResponse(organizationService.getOrganizationDetailByOrgId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/updateWholeAddressName</b>
     * <p>修改办公地点名称全称（标准版）</p>
     * @param cmd
     * @return
     */
    @RequestMapping("updateWholeAddressName")
    @RestReturn(value = String.class)
    public RestResponse updateWholeAddressName(WholeAddressComamnd cmd){
        RestResponse response = new RestResponse();
        //// TODO: 2018/5/25
        organizationService.updateWholeAddressName(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/enterprise/closeOrOpenWorkBench</b>
     * <p>禁用或者是开启移动工作台（标准版）</p>
     * @param cmd
     * @return
     */
    @RequestMapping("/enterprise/closeOrOpenWorkBench")
    @RestReturn(value = String.class)
    public RestResponse changeWorkBenchFlag(ChangeWorkBenchFlagCommand cmd){
        RestResponse response = new RestResponse();
        organizationService.changeWorkBenchFlag(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/enterprise/edit</b>
     * <p>编辑单个公司的具体属性(标准版)</p>
     */
    @RequestMapping("/enterprise/edit")
    @RestReturn(value = EnterpriseDTO.class)
    public RestResponse updateEnterpriseDetail(@Valid UpdateEnterpriseDetailCommand cmd) {
        organizationService.updateEnterpriseDetail(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/workplace/edit</b>
     * <p>编辑办公地点（包括其中的楼栋和门牌）(标准版)</p>
     * @param cmd
     * @return
     */
    @RequestMapping(value = "/workplace/edit")
    @RestReturn(value = String.class)
    public RestResponse insertWorkPlacesAndBuildings(UpdateWorkPlaceCommand cmd){
        organizationService.insertWorkPlacesAndBuildings(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/deleteWorkPlaces/edit</b>
     * <p>根据组织ID删除办公地点的方法(标准版)</p>
     * @param cmd
     * @return
     */
    @RequestMapping("/deleteWorkPlaces/edit")
    @RestReturn(value = String.class)
    public RestResponse deleteWorkPlacesByOrganizationId(DeleteWorkPlacesCommand cmd){
        RestResponse response = new RestResponse();
        organizationService.deleteWorkPlacesByOrgId(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/superAdmin/edit</b>
     * <p>更改超级管理员(标准版)</p>
     * @param cmd
     * @return
     */
    @RequestMapping(value = "/superAdmin/edit")
    @RestReturn(value = String.class)
    public RestResponse updateSuperAdminToken(UpdateSuperAdminCommand cmd){
        organizationService.updateSuperAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/getAdminType</b>
     * <p>更改超级管理员(标准版)</p>
     * @param cmd
     * @return
     */
    @RequestMapping("getAdminType")
    @RestReturn(value = GetAdminTypeResponse.class)
    public RestResponse getAdminType(GetAdminTypeCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        GetAdminTypeResponse  adminType = new GetAdminTypeResponse();
        adminType.setSuperAdminFlag(TrueOrFalseFlag.FALSE.getCode());
        try {
            resolver.checkCurrentUserAuthority(cmd.getOrganizationId(), PrivilegeConstants.SUPER_ADMIN_LIST);
            adminType.setSuperAdminFlag(TrueOrFalseFlag.TRUE.getCode());
        } catch(Exception ex) {
            //nothing
            
        }
        RestResponse response = new RestResponse(adminType);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /org/getAuthOrgByProjectIdAndAppId</b>
     * <p>根据项目id和应用Id，查询管理公司</p>
     * @param cmd
     * @return
     */
    @RequestMapping("getAuthOrgByProjectIdAndAppId")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse getAuthOrgByProjectIdAndAppId(GetAuthOrgByProjectIdAndAppIdCommand cmd) {

        OrganizationDTO dto = organizationService.getAuthOrgByProjectIdAndAppId(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }



    /**
     * <b>URL: /org/listUserOrganizations</b>
     * <p>通过项目id，获取当前项目用户认证的企业列表，</p>
     */
    @RequestMapping("listUserOrganizations")
    @RestReturn(value = ListUserOrganizationsResponse.class)
    public RestResponse listUserOrganizations(ListUserOrganizationsCommand cmd){

        ListUserOrganizationsResponse res = organizationService.listUserOrganizations(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/processUserForMember</b>
     * <p>通过项目id，获取当前项目用户认证的企业列表，</p>
     */
    @RequestMapping("processUserForMember")
    @RestReturn(String.class)
    public RestResponse processUserForMember(ProcessUserForMemberCommand cmd){
        organizationService.processUserForMember(cmd.getNamespaceId(),cmd.getIdentifierToken(),cmd.getOwnerId());
        return new RestResponse();
    }


    /**
     * <b>URL: /org/createUserAuthenticationOrganization</b>
     * <p>保存用户认证权限</p>
     */
    @RequestMapping("createUserAuthenticationOrganization")
    @RestReturn(value = UserAuthenticationOrganizationDTO.class)
    public RestResponse createUserAuthenticationOrganization(CreateUserAuthenticationOrganizationCommand cmd){

        UserAuthenticationOrganizationDTO res = organizationService.createUserAuthenticationOrganization(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/getUserAuthenticationOrganization</b>
     * <p>获取用户认证权限</p>
     */
    @RequestMapping("getUserAuthenticationOrganization")
    @RestReturn(value = UserAuthenticationOrganizationDTO.class)
    public RestResponse getUserAuthenticationOrganization(GetUserAuthenticationOrganizationCommand cmd){

        UserAuthenticationOrganizationDTO res = organizationService.getUserAuthenticationOrganization(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}