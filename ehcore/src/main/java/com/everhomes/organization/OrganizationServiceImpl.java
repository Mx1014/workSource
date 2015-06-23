// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.CancelLikeTopicCommand;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.GetTopicCommand;
import com.everhomes.forum.LikeTopicCommand;
import com.everhomes.forum.ListPostCommandResponse;
import com.everhomes.forum.ListTopicCommand;
import com.everhomes.forum.ListTopicCommentCommand;
import com.everhomes.forum.NewCommentCommand;
import com.everhomes.forum.NewTopicCommand;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostContentType;
import com.everhomes.forum.PostDTO;
import com.everhomes.forum.PostEntityTag;
import com.everhomes.forum.PostPrivacy;
import com.everhomes.forum.PropertyPostDTO;
import com.everhomes.organization.pm.AssginPmTopicCommand;
import com.everhomes.organization.pm.CommunityPmMember;
import com.everhomes.organization.pm.CommunityPmTasks;
import com.everhomes.organization.pm.ListPropPostCommandResponse;
import com.everhomes.organization.pm.PmMemberStatus;
import com.everhomes.organization.pm.PmMemberTargetType;
import com.everhomes.organization.pm.PmTaskStatus;
import com.everhomes.organization.pm.PmTaskType;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.organization.pm.PropertyServiceErrorCode;
import com.everhomes.organization.pm.QueryPropTopicByCategoryCommand;
import com.everhomes.organization.pm.SetPmTopicStatusCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.visibility.VisibleRegionType;

@Component
public class OrganizationServiceImpl implements OrganizationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	private static final String ASSIGN_TASK_AUTO_COMMENT = "assign.task.auto.comment";
	private static final String ASSIGN_TASK_AUTO_SMS = "assign.task.auto.sms";
	
	@Autowired
	private DbProvider dbProvider;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private PropertyMgrProvider propertyMgrProvider;
    
    @Autowired
    private PropertyMgrService propertyMgrService;
    
    @Autowired
    private ForumService forumService ;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
    private SmsProvider smsProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private UserService userService;
    
    private int getPageCount(int totalCount, int pageSize){
        int pageCount = totalCount/pageSize;
        
        if(totalCount % pageSize != 0){
            pageCount ++;
        }
        return pageCount;
    }
    
    @Override
    public void createOrganization(CreateOrganizationCommand cmd) {
    	User user  = UserContext.current().getUser();
    	
    	//权限控制
    	//先判断，后台管理员才能创建。状态直接设为正常
    	Organization department  = ConvertHelper.convert(cmd, Organization.class);
    	if(cmd.getParentId() == null)
    	{
    		department.setParentId(0L);
    	}
    	if(cmd.getLevel() == null){
    		department.setLevel(0);
    	}
    	department.setStatus(OrganizationStatus.ACTIVE.getCode());
    	organizationProvider.createOrganization(department);
    	
    	
    }
    
    @Override
    public void createOrganizationMember(CreateOrganizationMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
    	
    	//权限控制
    	//先判断，后台管理员才能创建。状态直接设为正常
    	Long addUserId =  cmd.getTargetId();
    	//添加已注册用户为管理员。
    	if(addUserId != null && addUserId != 0){
    		List<OrganizationMember> list = organizationProvider.listOrganizationMembers(cmd.getOrganizationId(), addUserId, 1, 20);
	    	if(list == null || list.size() == 0)
	    	{
	    		OrganizationMember departmentMember = ConvertHelper.convert(cmd, OrganizationMember.class);
	    		departmentMember.setStatus(PmMemberStatus.ACTIVE.getCode());
	    		organizationProvider.createOrganizationMember(departmentMember);
	    	}
    	}

    }
    
    @Override
    public void createOrganizationCommunity(CreateOrganizationCommunityCommand cmd) {
    	User user  = UserContext.current().getUser();
    	
    	//权限控制
    	//先判断，后台管理员才能创建。状态直接设为正常
    	List<Long> communityIds = cmd.getCommunityIds();
    	if(communityIds != null && communityIds.size() > 0){
    		for (Long id : communityIds) {
				Community community = communityProvider.findCommunityById(id);
				if(community == null) {
					LOGGER.error("Unable to find the community.communityId=" + id);
		    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
		                     "Unable to find the community.");
				}
				OrganizationCommunity departmentCommunity = new OrganizationCommunity();
				departmentCommunity.setCommunityId(id);
				departmentCommunity.setOrganizationId(cmd.getOrganizationId());
				organizationProvider.createOrganizationCommunity(departmentCommunity);
			}
    	}
    	
    }
    
    @Override
    public ListOrganizationsCommandResponse listOrganizations(ListOrganizationsCommand cmd) {
    	User user  = UserContext.current().getUser();
    	//权限控制
    	ListOrganizationsCommandResponse response = new ListOrganizationsCommandResponse();
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
    	int totalCount = organizationProvider.countOrganizations(cmd.getName());
    	if(totalCount == 0) return response;
    	int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
    	int pageCount = getPageCount(totalCount, pageSize);
    	List<Organization> result = organizationProvider.listOrganizations(cmd.getOrganizationType(),cmd.getName(), cmd.getPageOffset(), pageSize);
    	response.setMembers( result.stream()
                .map(r->{ return ConvertHelper.convert(r,OrganizationDTO.class); })
                .collect(Collectors.toList()));
    	response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
    	return response;
    }
    
    @Override
    public ListOrganizationMemberCommandResponse getUserOwningOrganizations() {
    	User user  = UserContext.current().getUser();
    	//权限控制
    	ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
    	List<OrganizationMember> reslut = organizationProvider.listOrganizationMembers (user.getId());
    	response.setMembers(reslut.stream()
    			.map(r->{ return ConvertHelper.convert(r,OrganizationMemberDTO.class); })
                .collect(Collectors.toList()));
    	return response;
    }
    
    @Override
    public UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd) {
    	UserTokenCommandResponse commandResponse = new UserTokenCommandResponse();
    	User user = userService.findUserByIndentifier(cmd.getUserIdentifier());
    	if(user != null)
    	{
    		List<String> phones = new ArrayList<String>();
    		phones.add(cmd.getUserIdentifier());
    		UserInfo userInfo = ConvertHelper.convert(user, UserInfo.class);
        	userInfo.setPhones(phones);
        	commandResponse.setUser(userInfo);
    	}
    	return commandResponse;
    }
    
    @Override
    public ListOrganizationMemberCommandResponse listOrganizationMembers(ListOrganizationMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
    	//权限控制
    	ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
    	int totalCount = organizationProvider.countOrganizationMembers(cmd.getOrganizationId(), null);
    	if(totalCount == 0) return response;
    	int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
    	int pageCount = getPageCount(totalCount, pageSize);
    	List<OrganizationMember> reslut = organizationProvider.listOrganizationMembers(cmd.getOrganizationId(), null, cmd.getPageOffset(), pageSize);
    	response.setMembers(reslut.stream()
    			.map(r->{ return ConvertHelper.convert(r,OrganizationMemberDTO.class); })
                .collect(Collectors.toList()));
    	response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
    	return response;
    }
    
    @Override
	public PostDTO createTopic(NewTopicCommand cmd) {
		User user  = UserContext.current().getUser();
    	if(cmd.getVisibleRegionId() == null){
    		LOGGER.error("propterty communityId paramter can not be null or empty");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "propterty communityId paramter can not be null or empty");
    	}
    	Community community = communityProvider.findCommunityById(cmd.getVisibleRegionId());
    	if(community == null){
    		LOGGER.error("Unable to find the community.communityId=" + cmd.getVisibleRegionId());
    		 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                     "Unable to find the community.");
    	}
    	//权限控制
    	if(cmd.getCreatorTag() == null || "".equals(cmd.getCreatorTag())){
    		cmd.setCreatorTag(PostEntityTag.PM.getCode());
    	}
    	if(cmd.getPrivateFlag() == null|| "".equals(cmd.getCreatorTag())){
    		cmd.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
    	}
    	if(cmd.getVisibleRegionType() == null || cmd.getVisibleRegionType() == 0){
    		cmd.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
    	}
    	if(cmd.getTargetTag() == null || "".equals(cmd.getCreatorTag())){
    		cmd.setTargetTag(PostEntityTag.USER.getCode());
    	}
    	PostDTO post = forumService.createTopic(cmd);
    	return post;
	}
	
	@Override
	public ListPropPostCommandResponse  queryTopicsByCategory(QueryPropTopicByCategoryCommand cmd) {
		ListPropPostCommandResponse response = new ListPropPostCommandResponse();
		User user  = UserContext.current().getUser();
    	Long communityId = cmd.getCommunityId();
    	if(communityId == null){
    		LOGGER.error("propterty communityId paramter can not be null or empty");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "propterty communityId paramter can not be null or empty");
    	}
    	Community community = communityProvider.findCommunityById(communityId);
    	if(community == null){
    		LOGGER.error("Unable to find the community.communityId=" + communityId);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                     "Unable to find the community.");
    	}
    	
    	//权限控制
    	int totalCount = propertyMgrProvider.countCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, PmTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus());
    	if(totalCount == 0) return response;
    	int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
    	int pageCount = getPageCount(totalCount, pageSize);
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
    	List<PropertyPostDTO> results = new ArrayList<PropertyPostDTO>();
    	List<CommunityPmTasks> tasks = propertyMgrProvider.listCommunityPmTasks(cmd.getCommunityId(), null, null, null, null, PmTaskType.fromCode(cmd.getActionCategory()).getCode(), cmd.getTaskStatus(), cmd.getPageOffset(), pageSize);
    	if(tasks != null && tasks.size() > 0){
    		for (CommunityPmTasks task : tasks) {
    			GetTopicCommand command = new GetTopicCommand();
    			command.setForumId(1l);
    			command.setTopicId(task.getEntityId());
				PostDTO post = forumService.getTopic(command);		
				PropertyPostDTO dto = ConvertHelper.convert(post, PropertyPostDTO.class);
				dto.setCommunityId(task.getOrganizationId());
				dto.setEntityType(task.getEntityType());
				dto.setEntityId(task.getEntityId());
				dto.setTargetType(task.getTargetType());
				dto.setTargetId(task.getTargetId());
				dto.setTaskType(task.getTaskType());
				dto.setTaskStatus(task.getTaskStatus());
				results.add(dto);
			}
    	}
    	response.setPosts(results);
    	response.setNextPageOffset(cmd.getPageOffset()==pageCount? null : cmd.getPageOffset()+1);
    	return response;
	}
	
	@Override
	public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
		User user  = UserContext.current().getUser();
    	Long communityId = cmd.getCommunityId();
    	if(communityId == null){
    		LOGGER.error("propterty communityId paramter can not be null or empty");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "propterty communityId paramter can not be null or empty");
    	}
    	Community community = communityProvider.findCommunityById(communityId);
    	if(community == null){
    		LOGGER.error("Unable to find the community.communityId=" + communityId);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                     "Unable to find the community.");
    	}
    	
    	//权限控制
		return forumService.listTopics(cmd);
	}
	
	@Override
	public PostDTO getTopic(GetTopicCommand cmd) {
		
		//权限控制
		return forumService.getTopic(cmd);
	}
	
	@Override
	public PostDTO createComment(NewCommentCommand cmd) {
		
		//权限控制
		return forumService.createComment(cmd);
	}
	
	@Override
	public ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd) {
		
		//权限控制
		return forumService.listTopicComments(cmd);
	}
	
	@Override
	public void likeTopic(LikeTopicCommand cmd) {
		
		//权限控制
		forumService.likeTopic(cmd);
	}
	
	@Override
	public void cancelLikeTopic(CancelLikeTopicCommand cmd) {
		
		//权限控制
		forumService.cancelLikeTopic(cmd);
	}
	
	 @Override
	    public void assignPMTopics(AssginPmTopicCommand cmd) {
	        User user = UserContext.current().getUser();
	        long userId = user.getId();
	        if(cmd.getTopicId() == null || cmd.getTopicId() == 0)
	            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Invalid topicIds paramter.");
	        if(cmd.getCommunityId() == null)
	            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Invalid communityId paramter.");
	        long communityId = cmd.getCommunityId();
	        
	        List<CommunityPmMember> pmMemberList = this.propertyMgrProvider.findPmMemberByCommunityAndTarget(communityId, 
	        		PmMemberTargetType.USER.getCode(), cmd.getUserId());
	        if(pmMemberList == null || pmMemberList.isEmpty()){
	            LOGGER.error("User is not the community pm member.userId=" + cmd.getUserId()+",communityId=" + communityId);
	            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Invalid userId paramter,user is not the community pm member.");
	        }
	        
	        long topicId = cmd.getTopicId();
	        
	        Post topic = this.forumProvider.findPostById(topicId);
	        if(topic == null){
	        	 LOGGER.error("Unable to find the topic.topicId=" + topicId+",communityId=" + communityId);
	             throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TOPIC, 
	                     "Unable to find the topic.");
	        }
	        
	        CommunityPmTasks task = this.propertyMgrProvider.findPmTaskByEntityId(communityId, topicId, 
	                EntityType.TOPIC.getCode());
	        if(task == null){
	        	 LOGGER.error("Unable to find the topic task.topicId=" + topicId +",communityId=" + communityId);
	             throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TASK, 
	                     "Unable to find the topic task.");
	        }
	        
	        dbProvider.execute((status) -> {
	        	task.setTaskStatus(PmTaskStatus.TREATING.getCode());
	            this.propertyMgrProvider.updatePmTask(task);
	        
	            //发送评论
	            sendComment(topicId,topic.getForumId(),userId,topic.getCategoryId());
	            //发送短信
	            sendMSMToUser(topicId,cmd.getUserId(),topic.getCreatorUid(),topic.getCategoryId());
	        
	            return null;
	        });
	           
	       
	    }

	    @Caching(evict={@CacheEvict(value="ForumPostById", key="#topicId")})
	    private void sendComment(long topicId, long forumId, long userId, long category) {
	        Post comment = new Post();
	        comment.setParentPostId(topicId);
	        comment.setForumId(forumId);
	        comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	        comment.setCreatorUid(userId);
	        comment.setContentType(PostContentType.TEXT.getCode());
	        String template = configurationProvider.getValue(ASSIGN_TASK_AUTO_COMMENT, "");
	        if(StringUtils.isEmpty(template)){
	        	template = "该物业已在处理";
	        }
	        if (!StringUtils.isEmpty(template)) {
	            comment.setContent(template);
	            forumProvider.createPost(comment);
	        }
	       
	    }

	    /**
	     * 
	     * @param topicId
	     * @param userId 维修人员id
	     * @param owerId 业主id
	     * @param category 分类
	     */
	    private void sendMSMToUser(long topicId, long userId, long owerId, long category) {
	        //给维修人员发送短信是否显示业主地址
	        String template = configurationProvider.getValue(ASSIGN_TASK_AUTO_SMS, "");
	        List<UserIdentifier> userList = this.userProvider.listUserIdentifiersOfUser(userId);
	        userList.stream().filter((u) -> {
	            if(u.getIdentifierType() != IdentifierType.MOBILE.getCode())
	                return false;
	            return true;
	        });
	        if(userList == null || userList.isEmpty()) return ;
	        String cellPhone = userList.get(0).getIdentifierToken();
	        if(StringUtils.isEmpty(template)){
	        	template = "该物业已在处理";
	        }
	        if (!StringUtils.isEmpty(template)) {
	        	this.smsProvider.sendSms(cellPhone, template);
	        }
	    }
	    
	    @Override
	    public void setPMTopicStatus(SetPmTopicStatusCommand cmd){
	    	User user  = UserContext.current().getUser();
	    	long userId = user.getId();
	    	if(cmd.getCommunityId() == null){
	    		LOGGER.error("propterty communityId paramter can not be null or empty");
	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "propterty communityId paramter can not be null or empty");
	    	}
	    	Community community = communityProvider.findCommunityById(cmd.getCommunityId());
	    	if(community == null){
	    		LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
	    		 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                     "Unable to find the community.");
	    	}
	    	//权限控制--admin角色
	        if(cmd.getTopicId() == null || cmd.getStatus() == null ){
	            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Invalid topicIds or status  paramter.");
	        }
	        if(cmd.getStatus() == PmTaskStatus.TREATING.getCode()){
	        	 throw RuntimeErrorException.errorWith(PropertyServiceErrorCode.SCOPE, PropertyServiceErrorCode.ERROR_INVALID_TASK_STATUS, 
	                     "Invalid topic task status  paramter. please assign task");
	        }
	        long topicId = cmd.getTopicId();
	        Post topic = this.forumProvider.findPostById(topicId);
	        if(topic == null){ 
	            LOGGER.error("Topic is not found.topicId=" + topicId + ",userId=" + userId);
	         }
	        CommunityPmTasks task = this.propertyMgrProvider.findPmTaskByEntityId(cmd.getCommunityId(), 
	                topicId, EntityType.TOPIC.getCode());
	        if(task == null){
	            LOGGER.error("Pm task is not found.topicId=" + topicId + ",userId=" + userId);
	        }
	        task.setTaskStatus(cmd.getStatus());
	        this.propertyMgrProvider.updatePmTask(task);
	        if(cmd.getStatus() == PmTaskStatus.TREATED.getCode()){
	        	//发送评论
	            sendComment(topicId,topic.getForumId(),userId,topic.getCategoryId());
	        }
	        
	    }
}
