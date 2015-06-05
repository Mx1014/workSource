// @formatter:off
package com.everhomes.pm;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.address.ApartmentDTO;
import com.everhomes.address.BuildingDTO;
import com.everhomes.address.ListAddressByKeywordCommand;
import com.everhomes.address.ListApartmentByKeywordCommand;
import com.everhomes.address.ListBuildingByKeywordCommand;
import com.everhomes.app.AppConstants;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.core.AppConfig;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.ApproveMemberCommand;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyDTO;
import com.everhomes.family.FamilyMemberDTO;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.family.RejectMemberCommand;
import com.everhomes.family.RevokeMemberCommand;
import com.everhomes.forum.ForumConstants;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.ListPostCommandResponse;
import com.everhomes.forum.NewTopicCommand;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostContentType;
import com.everhomes.forum.PostDTO;
import com.everhomes.forum.PostEntityTag;
import com.everhomes.forum.PostPrivacy;
import com.everhomes.forum.QueryTopicByCategoryCommand;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessagingService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.SetCurrentCommunityCommand;
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
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.handler.ProcessBillModel1;
import com.everhomes.util.excel.handler.PropMgrBillHandler;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.visibility.VisibleRegionType;

@Component
public class PropertyMgrServiceImpl implements PropertyMgrService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrServiceImpl.class);
    private static final String ASSIGN_TASK_AUTO_COMMENT = "assign.task.auto.comment";
    private static final String ASSIGN_TASK_AUTO_SMS = "assign.task.auto.sms";
    private static final String PROP_MESSAGE_BILL = "prop.message.bill";
    @Autowired
    private PropertyMgrProvider propertyMgrProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private SmsProvider smsProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private FamilyService familySerivce;
    
    @Autowired
    private  ForumService forumService;
    
    @Override
    public void applyPropertyMember(applyPropertyMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
    	Long communityId = user.getCommunityId();
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
    	if(!communityId .equals( cmd.getCommunityId())){
    		LOGGER.error("you not belong to the community.communityId=" + cmd.getCommunityId());
   		 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "you not belong to the community.");
    	}
    	CommunityPmMember communityPmMember = createCommunityPmMember(communityId,cmd.getContactDescription(),user);
    	propertyMgrProvider.createPropMember(communityPmMember);
    	
    }
    
    public CommunityPmMember createCommunityPmMember(Long communityId,String description,User user) {
    	UserIdentifier identifier = null; 
    	List<UserIdentifier> userIndIdentifiers  = userProvider.listUserIdentifiersOfUser(user.getId());
    	if(userIndIdentifiers != null && userIndIdentifiers.size() > 0){
    		for (UserIdentifier userIdentifier : userIndIdentifiers) {
				if(userIdentifier.getIdentifierType() == IdentifierType.MOBILE.getCode()){
					identifier = userIdentifier;
					break;
				}
			}
    	}
    	CommunityPmMember communityPmMember = new CommunityPmMember();
    	communityPmMember.setCommunityId(communityId);
    	communityPmMember.setContactName(user.getNickName());
    	if(identifier != null){
	    	communityPmMember.setContactToken(identifier.getIdentifierToken());
	    	communityPmMember.setContactType(identifier.getIdentifierType());
    	}
    	communityPmMember.setPmGroup(PmGroup.MANAGER.getCode());
    	communityPmMember.setStatus(PmMemberStatus.CONFIRMING.getCode());
    	communityPmMember.setTargetType(PmMemberTargetType.USER.getCode());
    	communityPmMember.setTargetId(user.getId());
    	communityPmMember.setContactDescription(description);
		return communityPmMember;
	}

	@Override
    public void createPropMember(CreatePropMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
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
    	//权限控制
    	//先判断，如果不属于这个小区的物业，才添加物业成员。状态直接设为正常
    	Long addUserId =  cmd.getTargetId();
    	//添加已注册用户为管理员。
    	if(addUserId != null && addUserId != 0){
	    	List<CommunityPmMember> list = propertyMgrProvider.findPmMemberByCommunityAndTarget(cmd.getCommunityId(), cmd.getTargetType(), cmd.getTargetId());
	    	if(list == null || list.size() == 0)
	    	{
	    		CommunityPmMember communityPmMember = ConvertHelper.convert(cmd, CommunityPmMember.class);
	        	communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
	        	propertyMgrProvider.createPropMember(communityPmMember);
	    	}
    	}
    }
    
    @Override
    public ListPropMemberCommandResponse getUserOwningProperties() {
    	ListPropMemberCommandResponse commandResponse = new ListPropMemberCommandResponse();
    	User user  = UserContext.current().getUser();
    	
    	
    	List<CommunityPmMember> entityResultList = propertyMgrProvider.listUserCommunityPmMembers(user.getId());
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ 
                	 PropertyMemberDTO dto =ConvertHelper.convert(r, PropertyMemberDTO.class);
                	 Community community = communityProvider.findCommunityById(dto.getCommunityId());
                	 dto.setCommunityName(community.getName());
                	 return dto; })
                 .collect(Collectors.toList()));
    	
        return commandResponse;
    }
    
    @Override
    public ListPropMemberCommandResponse listCommunityPmMembers(ListPropMemberCommand cmd) {
    	ListPropMemberCommandResponse commandResponse = new ListPropMemberCommandResponse();
    	User user  = UserContext.current().getUser();
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
    	//权限控制
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
    	int totalCount = propertyMgrProvider.countCommunityPmMembers(cmd.getCommunityId(), null);
    	if(totalCount == 0) return commandResponse;
    	
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	int pageCount = getPageCount(totalCount, pageSize);
    	
    	List<CommunityPmMember> entityResultList = propertyMgrProvider.listCommunityPmMembers(cmd.getCommunityId(), null, cmd.getPageOffset(),pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ 
                	 PropertyMemberDTO dto =ConvertHelper.convert(r, PropertyMemberDTO.class);
                	 dto.setCommunityName(community.getName());
                	 return dto; })
                 .collect(Collectors.toList()));
    	commandResponse.setPageCount(pageCount);
        return commandResponse;
    }

	@Override
	public void importPMAddressMapping(PropCommunityIdCommand cmd) {
		User user  = UserContext.current().getUser();
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
    	if(community == null){
    		LOGGER.error("Unable to find the community.communityId=" + cmd.getCommunityId());
    		 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                     "Unable to find the community.");
    	}
    	//权限控制
    	
    	//只需要导入一次。先查询总数如果没有，继续导入。
    	List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(cmd.getCommunityId(), 1, 10);
    	if(entityResultList == null || entityResultList.size() == 0)
    	{
    		ListAddressByKeywordCommand comand = new ListAddressByKeywordCommand();
    		comand.setCommunityId(cmd.getCommunityId());
    		comand.setKeyword("");
    		List<Address> addresses= addressService.listAddressByKeyword(comand);
    		if(addresses != null && addresses.size() > 0)
    		{
    			for (Address address : addresses) {
					CommunityAddressMapping m = new CommunityAddressMapping();
					m.setAddressId(address.getId());
					m.setCommunityId(cmd.getCommunityId());
					m.setName(address.getAddress());
					m.setLivingStatus((byte)0);
					propertyMgrProvider.createPropAddressMapping(m);
				}
    		}
        }
    	
    }
    
    @Override
    public ListPropAddressMappingCommandResponse ListAddressMappings(
    		ListPropAddressMappingCommand cmd) {
    	ListPropAddressMappingCommandResponse commandResponse = new ListPropAddressMappingCommandResponse();
    	User user  = UserContext.current().getUser();
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
    	//权限控制
    	int totalCount = propertyMgrProvider.countCommunityAddressMappings(cmd.getCommunityId());
    	if(totalCount == 0) return commandResponse;
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	int pageCount = getPageCount(totalCount, pageSize);
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
    	List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(cmd.getCommunityId(), cmd.getPageOffset(), pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ 
                	 PropAddressMappingDTO dto = ConvertHelper.convert(r, PropAddressMappingDTO.class);
                	 Address address = addressProvider.findAddressById(dto.getAddressId());
                	 if(address != null)
                		 dto.setAddressName(address.getAddress());
                	 return  dto;})
                 .collect(Collectors.toList()));
    	commandResponse.setPageCount(pageCount);
        return commandResponse;
    }
    
    @Override
    public ListPropBillCommandResponse listPropertyBill(ListPropBillCommand cmd) {
    	ListPropBillCommandResponse commandResponse = new ListPropBillCommandResponse();
    	User user  = UserContext.current().getUser();
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
    	//权限控制
    	int totalCount = propertyMgrProvider.countCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress());
    	if(totalCount == 0) return commandResponse;
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
    	int pageCount = getPageCount(totalCount, pageSize);
    	List<CommunityPmBill> entityResultList = propertyMgrProvider.listCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress(), cmd.getPageOffset(), pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ 
                	 PropBillDTO dto = ConvertHelper.convert(r, PropBillDTO.class);
                	 List<CommunityPmBillItem> itemList = propertyMgrProvider.listCommunityPmBillItems(dto.getId());
                	 List<PropBillItemDTO> itemDtoList = new ArrayList<PropBillItemDTO>();
                	 for (CommunityPmBillItem item : itemList) {
                		 PropBillItemDTO itemDto = ConvertHelper.convert(item, PropBillItemDTO.class);
                		 itemDtoList.add(itemDto);
					 }
                	 dto.setItemList(itemDtoList);
                	 return  dto;})
                 .collect(Collectors.toList()));
    	commandResponse.setPageCount(pageCount);
        return commandResponse;
    }
    

    
    @Override
    public ListPropOwnerCommandResponse  listPMPropertyOwnerInfo(ListPropOwnerCommand cmd) {
    	ListPropOwnerCommandResponse commandResponse = new ListPropOwnerCommandResponse();
    	User user  = UserContext.current().getUser();
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
    	//权限控制
    	int totalCount = propertyMgrProvider.countCommunityPmOwners(cmd.getCommunityId(),cmd.getAddress(),cmd.getContactToken());
    	if(totalCount == 0) return commandResponse;
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	
    	int pageCount = getPageCount(totalCount, pageSize);
    	List<CommunityPmOwner> entityResultList = propertyMgrProvider.listCommunityPmOwners(cmd.getCommunityId(),cmd.getAddress(),cmd.getContactToken(), cmd.getPageOffset(), pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                .map(r->{ return ConvertHelper.convert(r, PropOwnerDTO.class); })
                .collect(Collectors.toList()));
    	commandResponse.setPageCount(pageCount);
   	
       return commandResponse;
    }
    private int getPageCount(int totalCount, int pageSize){
        int pageCount = totalCount/pageSize;
        
        if(totalCount % pageSize != 0){
            pageCount ++;
        }
        return pageCount;
    }
    
    @Override
    public void assignPMTopics(AssginPmTopicCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        if(cmd.getTopicIds() == null || cmd.getTopicIds().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid topicIds paramter.");
        if(cmd.getStatus() == PmTaskStatus.TREATED.getCode())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid status paramter.");
        if(cmd.getCommunityId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId paramter.");
        long communityId = cmd.getCommunityId();
        
        List<CommunityPmMember> pmMemberList = this.propertyMgrProvider.findPmMemberByCommunityAndTarget(communityId, 
                EntityType.USER.getCode(), cmd.getUserId());
        if(pmMemberList == null || pmMemberList.isEmpty()){
            LOGGER.error("User is not the community pm member.userId=" + cmd.getUserId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid userId paramter,user is not the community pm member.");
        }
        
        for(Long topicId : cmd.getTopicIds()){
            List<CommunityPmTasks> pmTaskList = this.propertyMgrProvider.findPmTaskEntityIdAndTargetId(communityId, topicId, 
                    EntityType.TOPIC.getCode(), cmd.getUserId(),EntityType.USER.getCode(),cmd.getStatus());
            //任务已存在跳过
            if(pmTaskList != null && !pmTaskList.isEmpty()){
                LOGGER.error("Pm task is exists.");
                continue;
            }
            
            Post topic = this.forumProvider.findPostById(topicId);
            if(topic == null){
                LOGGER.error("Topic is not found.topicId=" + topicId + ",userId=" + userId);
                continue;
            }
            
            dbProvider.execute((status) -> {
                CommunityPmTasks task = new CommunityPmTasks();
                task.setCommunityId(communityId);
                task.setEntityId(topicId);
                task.setEntityType(EntityType.TOPIC.getCode());
                task.setTargetId(cmd.getUserId());
                task.setTaskType(EntityType.USER.getCode());
                task.setTaskStatus(cmd.getStatus());
                this.propertyMgrProvider.createPmTask(task);
                
                if(cmd.getStatus() == PmTaskStatus.TREATING.getCode()){
                  //发送评论
                    sendComment(topicId,topic.getForumId(),userId,topic.getCategoryId());
                    //发送短信
                    sendMSMToUser(topicId,cmd.getUserId(),topic.getCreatorUid(),cmd.getStatus(),topic.getCategoryId());
                }
                
                return null;
            });
           
        }
    }

    private void sendComment(long topicId, long forumId, long userId, long category) {
        Post comment = new Post();
        comment.setParentPostId(topicId);
        comment.setForumId(forumId);
        comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        comment.setCreatorUid(userId);
        comment.setContentType(PostContentType.TEXT.getCode());
        String template = configProvider.getValue(ASSIGN_TASK_AUTO_COMMENT, "");

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
     * @param status 状态
     * @param category 分类
     */
    private void sendMSMToUser(long topicId, long userId, long owerId,byte status, long category) {
        //给维修人员发送短信是否显示业主地址
        String template = configProvider.getValue(ASSIGN_TASK_AUTO_SMS, "");
        List<UserIdentifier> userList = this.userProvider.listUserIdentifiersOfUser(userId);
        userList.stream().filter((u) -> {
            if(u.getIdentifierType() != IdentifierType.MOBILE.getCode())
                return false;
            return true;
        });
        if(userList == null || userList.isEmpty()) return ;
        String cellPhone = userList.get(0).getIdentifierToken();
        this.smsProvider.sendSms(cellPhone, template);
        
    }
    
    public void setPMTopicStatus(SetPmTopicStatusCommand cmd){
        if(cmd.getTopicIds() == null || cmd.getStatus() == null || cmd.getCommunityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid topicIds or status or communityId paramter.");
        }
        
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        for(long topicId : cmd.getTopicIds()){
            Post topic = this.forumProvider.findPostById(topicId);
            if(topic == null){ 
                LOGGER.error("Topic is not found.topicId=" + topicId + ",userId=" + userId);
                continue;
            }
            List<CommunityPmTasks> tasks = this.propertyMgrProvider.findPmTaskEntityId(cmd.getCommunityId(), 
                    topicId, EntityType.TOPIC.getCode());
            if(tasks == null){
                LOGGER.error("Pm task is not found.topicId=" + topicId + ",userId=" + userId);
                continue;
            }
            this.propertyMgrProvider.updatePmTaskListStatus(tasks);
            
        }
    }

    @Override
    public ListPropInvitedUserCommandResponse listInvitedUsers(ListPropInvitedUserCommand cmd) {
        Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
        if(community == null){
            LOGGER.error("Community is not found.communityId=" + cmd.getCommunityId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId paramter,community is not found.");
        }
        
        //??权限控制
        Long pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        
        Long pageSize = cmd.getPageSize() == null ? this.configProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize(); 
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        
        return this.propertyMgrProvider.listInvitedUsers(cmd.getCommunityId(),
                cmd.getContactToken(),offset,pageSize);
    }

	@Override
	public void approvePropMember(CommunityPropMemberCommand cmd) {
		User user  = UserContext.current().getUser();
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
    	CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
    	if(communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode())
    	{
    		LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find the property member or the property member status is not confirming.");
    	}
    	communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
    	propertyMgrProvider.updatePropMember(communityPmMember);
		
    	//发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
    	long userId = communityPmMember.getTargetId();
    	if(userId != 0)
    	{
    		
    	}
	}

	@Override
	public void rejectPropMember(CommunityPropMemberCommand cmd) {
		User user  = UserContext.current().getUser();
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
    	CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
    	if(communityPmMember == null || communityPmMember.getStatus() != PmMemberStatus.CONFIRMING.getCode())
    	{
    		LOGGER.error("Unable to find the property member or the property member status is not confirming.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find the property member or the property member status is not confirming.");
    	}
    	long userId = communityPmMember.getTargetId();
    	propertyMgrProvider.deletePropMember(communityPmMember);
    	
    	//发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
    	if(userId != 0)
    	{
    		
    	}
	}
	
	@Override
	public void revokePMGroupMember(DeletePropMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
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
    	CommunityPmMember communityPmMember = propertyMgrProvider.findPropMemberById(cmd.getMemberId());
    	if(communityPmMember == null)
    	{
    		LOGGER.error("Unable to find the property member.communityId=" + cmd.getCommunityId() +",memberId=" + cmd.getMemberId());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find the property member.");
    	}
    	long userId = communityPmMember.getTargetId();
    	propertyMgrProvider.deletePropMember(communityPmMember);
    	
    	//发通知 : 物业成员如果是注册用户发通知，如果不是 发短信。
    	if(userId != 0)
    	{
    		
    	}
    	
	}
	
	@Override
	public void approvePropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
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
    	FamilyDTO family = familyProvider.getFamilyById(cmd.getFamilyId());
    	if(family == null ||family.getCommunityId() != cmd.getCommunityId())
    	{
    		LOGGER.error("family is not existed or family is not belong to the community.communityId=" + cmd.getCommunityId()+",familyId=" + cmd.getFamilyId());
   		 	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find the community.");
    	}

    	//familyProvider.approveMember(cmd.getFamilyId(),cmd.getUserId(),Role.ResourceAdmin);
    	ApproveMemberCommand comand = new ApproveMemberCommand();
    	comand.setId(cmd.getFamilyId());
    	comand.setMemberUid(cmd.getUserId());
    	comand.setOperatorRole(Role.ResourceAdmin);
    	familySerivce.approveMember(comand);
    	
		
	}
	
	@Override
	public void rejectPropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
		User user  = UserContext.current().getUser();
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
    	FamilyDTO family = familyProvider.getFamilyById(cmd.getFamilyId());
    	if(family == null ||family.getCommunityId() != cmd.getCommunityId())
    	{
    		LOGGER.error("family is not existed or family is not belong to the community.communityId=" + cmd.getCommunityId()+",familyId=" + cmd.getFamilyId());
   		 	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find the community.");
    	}
    	
    	String reason = "";
//    	familyProvider.rejectMember(cmd.getFamilyId(), cmd.getUserId(), reason, Role.ResourceAdmin);
    	
    	RejectMemberCommand command = new RejectMemberCommand();
    	command.setId(cmd.getFamilyId());
    	command.setMemberUid(cmd.getUserId());
    	command.setOperatorRole(Role.ResourceAdmin);
    	command.setReason(reason);
		familySerivce.rejectMember(command );
	}
    
    @Override
    public void revokePropFamilyMember(CommunityPropFamilyMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
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
    	FamilyDTO family = familyProvider.getFamilyById(cmd.getFamilyId());
    	if(family == null ||family.getCommunityId() != cmd.getCommunityId())
    	{
    		LOGGER.error("family is not existed or family is not belong to the community.communityId=" + cmd.getCommunityId()+",familyId=" + cmd.getFamilyId());
   		 	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find the community.");
    	}
    	
       	String reason = "";
//    	familyProvider.revokeMember(cmd.getFamilyId(), cmd.getUserId(), reason,Role.ResourceAdmin);
    	RevokeMemberCommand comand = new RevokeMemberCommand();
    	comand.setId(cmd.getFamilyId());
    	comand.setMemberUid(cmd.getUserId());
    	comand.setOperatorRole(Role.ResourceAdmin);
    	comand.setReason(reason);
		familySerivce.revokeMember(comand );
    }
    
    @Override
    public Tuple<Integer, List<BuildingDTO>> listPropBuildingsByKeyword(ListBuildingByKeywordCommand cmd) {
    	User user  = UserContext.current().getUser();
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
    	//权限控制
    	
    	return addressService.listBuildingsByKeyword(cmd);
    }
    
    @Override
    public List<PropFamilyDTO> listPropApartmentsByKeyword(ListApartmentByKeywordCommand cmd) {
    	List<PropFamilyDTO> list = new ArrayList<PropFamilyDTO>();
    	User user  = UserContext.current().getUser();
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
    	//权限控制
    	Tuple<Integer,List<ApartmentDTO>> apts = addressService.listApartmentsByKeyword(cmd);
    	List<ApartmentDTO> aptList = apts.second();
    	for (ApartmentDTO apartmentDTO : aptList) {
    		PropFamilyDTO dto = new PropFamilyDTO();
    		Family family = familyProvider.findFamilyByAddressId(apartmentDTO.getAddressId());
    		if(family != null )
    		{
    			dto.setAddress(cmd.getBuildingName()+"-"+apartmentDTO.getApartmentName());
        		dto.setName(family.getDisplayName());
        		dto.setAddressId(family.getAddressId());
        		dto.setId(family.getId());
        		dto.setMemberCount(family.getMemberCount());
    		}
    		else
    		{
    			dto.setAddress(cmd.getBuildingName()+"-"+apartmentDTO.getApartmentName());
        		dto.setName(apartmentDTO.getApartmentName());
        		dto.setAddressId(apartmentDTO.getAddressId());
        		dto.setId(0l);
        		dto.setMemberCount(0l);
    		}
    		CommunityAddressMapping mapping = propertyMgrProvider.findPropAddressMappingByAddressId(cmd.getCommunityId(),apartmentDTO.getAddressId());
        	if(mapping != null){
        		dto.setLivingStatus(mapping.getLivingStatus());
        	}
        	else{
        		dto.setLivingStatus(PmAddressMappingStatus.LIVING.getCode());
        	}
        	list.add(dto);
		}
    	return list;
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
    public void setPropCurrentCommunity(SetCurrentCommunityCommand cmd) {
    	User user  = UserContext.current().getUser();
    	//根据communityId 和userId 判断权限
    	
    	userService.setUserCurrentCommunity(cmd.getCommunityId());
    }
    
    @Override
    public ListPropFamilyWaitingMemberCommandResponse listPropFamilyWaitingMember(ListPropFamilyWaitingMemberCommand cmd) {
    	ListPropFamilyWaitingMemberCommandResponse commandResponse = new ListPropFamilyWaitingMemberCommandResponse();
    	
    	User user  = UserContext.current().getUser();
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
    	cmd.setPageOffset(cmd.getPageOffset() == null ? 1: cmd.getPageOffset());
    	int totalCount = familyProvider.countWaitApproveFamily(cmd.getCommunityId());
    	if(totalCount == 0) return commandResponse;
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	int pageCount = getPageCount(totalCount, pageSize);
    	List<FamilyDTO>  entityResultList = familyProvider.listWaitApproveFamily(cmd.getCommunityId(), new Long(cmd.getPageOffset()), new Long(pageSize));
    	commandResponse.setMembers( entityResultList.stream()
                .map(r->{ return r; })
                .collect(Collectors.toList()));
    	commandResponse.setPageCount(pageCount);
    	return commandResponse;
    }
    
    @Override
    public void setApartmentStatus(SetPropAddressStatusCommand cmd) {
    	User user  = UserContext.current().getUser();
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
    	
    	CommunityAddressMapping mapping = propertyMgrProvider.findPropAddressMappingByAddressId(cmd.getCommunityId(), cmd.getAddressId());
    	if(mapping == null)
    	{
    		PropCommunityIdCommand comand = new PropCommunityIdCommand();
    		comand.setCommunityId(cmd.getCommunityId());
    		importPMAddressMapping(comand);
    		mapping = propertyMgrProvider.findPropAddressMappingByAddressId(cmd.getCommunityId(), cmd.getAddressId());
    	}
    	mapping.setLivingStatus(cmd.getStatus());
    	propertyMgrProvider.updatePropAddressMapping(mapping);
    }
    
    @Override
    public PropFamilyDTO findFamilyByAddressId(ListPropCommunityAddressCommand cmd) {
    	Family family = familyProvider.findFamilyByAddressId(cmd.getAddressId());
    	User user  = UserContext.current().getUser();
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
    	if(family == null)
    	{
    		LOGGER.error("family is not existed.communityId=" + cmd.getCommunityId()+",addressId=" + cmd.getAddressId());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Unable to find the family.");
    	}
    	if(family.getCommunityId() != cmd.getCommunityId())
    	{
    		LOGGER.error("family is not belong to the community.communityId=" + cmd.getCommunityId()+",addressId=" + cmd.getAddressId());
   		 	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "family is not belong to the community.");
    	}
    	PropFamilyDTO dto = new PropFamilyDTO();
    	if(family != null){
	    	dto.setName(family.getDisplayName());
	    	dto.setId(family.getId());
	    	dto.setMemberCount(family.getMemberCount());
	    	dto.setAddressId(family.getAddressId());
	    	dto.setAddress(family.getName());
    	}
    	CommunityAddressMapping mapping = propertyMgrProvider.findPropAddressMappingByAddressId(cmd.getCommunityId(), cmd.getAddressId());
    	if(mapping != null){
    		dto.setLivingStatus(mapping.getLivingStatus());
    	}
    	else{
    		dto.setLivingStatus(PmAddressMappingStatus.LIVING.getCode());
    	}
    	return dto;
    }
    
    @Override
    public List<FamilyMemberDTO> listFamilyMembersByFamilyId(ListPropFamilyMemberCommand cmd) {
    	User user  = UserContext.current().getUser();
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
    	List<GroupMember> entityResultList = groupProvider.findGroupMemberByGroupId(cmd.getFamilyId());
    	List<FamilyMemberDTO> results = new ArrayList<FamilyMemberDTO>();
    	
    	for (GroupMember member : entityResultList) {
    		FamilyMemberDTO dto = new FamilyMemberDTO();
    		dto.setId(member.getId());
    		dto.setFamilyId(member.getGroupId());
    		dto.setMemberUid(member.getMemberId());
    		dto.setMemberName(member.getMemberNickName());
    		dto.setMemberAvatarUri((member.getMemberAvatar()));
    		results.add(dto);
		}
    	return results;
    }
    
    @Override
    public void importPMPropertyOwnerInfo(@Valid PropCommunityIdCommand cmd,
    		MultipartFile[] files) {
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
    	
    	ArrayList resultList = new ArrayList();
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<CommunityPmOwner> contactList = PropMrgOwnerHandler.processorPropMgrContact(user.getId(), communityId, resultList);
		ListPropAddressMappingCommand command = new ListPropAddressMappingCommand();
		command.setCommunityId(communityId);
		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listCommunityAddressMappings(communityId);
		long addressId = 0;
		if(contactList != null && contactList.size() > 0)
		{
			for (CommunityPmOwner contact : contactList)
			{
				if(mappingList != null && mappingList.size() > 0)
				{
					for (CommunityAddressMapping mapping : mappingList)
					{
						if(contact != null && contact.getAddress().equals(mapping.getName()))
						{
							addressId = mapping.getAddressId();
							break;
						}
					}
					
				}
				contact.setAddressId(addressId);
				propertyMgrProvider.createPropOwner(contact);
				
			}
		}
    	
    }
    
    @Override
    public void importPropertyBills(@Valid PropCommunityIdCommand cmd, MultipartFile[] files) {
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
    	
    	ArrayList resultList = new ArrayList();
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<CommunityPmBill> billList = PropMgrBillHandler.processorPropBill(user.getId(), communityId, resultList, ProcessBillModel1.PROCESS_TO_OBJECT_PER_ROW, ProcessBillModel1.PROCESS_PREVIOUS_ROW_TITLE);
		ListPropAddressMappingCommand command = new ListPropAddressMappingCommand();
		command.setCommunityId(communityId);
		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listCommunityAddressMappings(communityId);
		long addressId = 0;
		if(billList != null && billList.size() > 0)
		{
			for (CommunityPmBill bill : billList)
			{
				if(mappingList != null && mappingList.size() > 0)
				{
					for (CommunityAddressMapping mapping : mappingList)
					{
						if(bill != null && bill.getAddress().equals(mapping.getName()))
						{
							addressId = mapping.getAddressId();
							break;
						}
					}
					
				}
				bill.setEntityId(addressId);
				bill.setEntityType(PmBillEntityType.ADDRESS.getCode());
				createPropBill(bill);
			}
		}
    }
    
    @Override
    public void createPropBill(CommunityPmBill bill) {
    	User user  = UserContext.current().getUser();
    	this.dbProvider.execute((status) ->{
    		propertyMgrProvider.createPropBill(bill);
    		System.out.println(bill);
    		List<CommunityPmBillItem> itemList =  bill.getItemList();
    		if(itemList != null && itemList.size() > 0){
    			for (CommunityPmBillItem communityPmBillItem : itemList) {
    				communityPmBillItem.setBillId(bill.getId());
    				propertyMgrProvider.createPropBillItem(communityPmBillItem);
				}
    		}
    		return status;
    	});
    }
    
    @Override
    public void sendPropertyBillById(PropCommunityBillIdCommand cmd) {
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
    	CommunityPmBill bill = propertyMgrProvider.findPropBillById(cmd.getBillId());
    	sendPropertyBillById(bill);
    	
    }
    
    @Override
    public void sendPropertyBillByMonth(PropCommunityBillDateCommand cmd) {
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
    	List<CommunityPmBill> billList = propertyMgrProvider.listCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr());
    	sendPropertyBills(billList);
    	
    	
    }
    
    
    public void sendPropertyBills(List<CommunityPmBill> billList) {
    	if(billList != null && billList.size() > 0){
    		for (CommunityPmBill communityPmBill : billList) {
    			Address address = addressProvider.findAddressById(communityPmBill.getEntityId());
    			if(address != null){
    				Family family = familyProvider.findFamilyByAddressId(address.getId());
    				if(family != null){
    					String message = buildBillMessage(communityPmBill);
    					sendNoticeToFamilyById(family.getId(), message);
    				}
    			}
			}
    	}
		
	}

	public String buildBillMessage(CommunityPmBill bill) {
		String template = configProvider.getValue(PROP_MESSAGE_BILL, "");
		String content = "\n" + template +"账单时间：" + bill.getDateStr() +"\n";
		List<CommunityPmBillItem> itemList  = propertyMgrProvider.listCommunityPmBillItems(bill.getId());
		if(itemList != null && itemList.size() > 0)
		{
			for (CommunityPmBillItem item : itemList)
			{
				content += item.getItemName()+": " + item.getTotalAmount() + "元\n";
			}
		}
		content += "总费用：" + bill.getTotalAmount();
		return content;
	}

	public void sendPropertyBillById(CommunityPmBill bill) {
    	if(bill == null)
    	{
    		LOGGER.error("Unable to find the bill." );
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,PropertyServiceErrorCode.ERROR_INVALID_BILL, 
                     "Unable to find the bill.");
    	}
    	Address address = addressProvider.findAddressById(bill.getEntityId());
    	if(address == null)
    	{
    		LOGGER.error("Unable to find the address.addressId=" + bill.getEntityId());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,PropertyServiceErrorCode.ERROR_INVALID_ADDRESS, 
                     "Unable to find the address.");
    	}
    	Family family = familyProvider.findFamilyByAddressId(address.getId());
    	if(family == null)
    	{
    		LOGGER.error("Unable to find the family.familyId=" +address.getId());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,PropertyServiceErrorCode.ERROR_INVALID_FAMILY, 
                     "Unable to find the family.");
    	}
    	String message = buildBillMessage(bill);
		sendNoticeToFamilyById(family.getId(), message);
    }
    
    @Override
    public void sendNoticeToFamily(PropCommunityBuildAddessCommand cmd) {
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
    	
    	List<String> buildingNames = cmd.getBuildingNames();
    	List<Long> addressIds = cmd.getAddressIds();
    	
    	//物业发通知机制： 对于已注册的发消息 -familyIds   未注册的发短信-userIds。
    	//已注册的user 分两种： 1-已加入家庭，发家庭消息。 2-还未加入家庭，发个人信息。
    	List<CommunityPmOwner> owners  = new ArrayList<CommunityPmOwner>();
     	List<Long> familyIds = new ArrayList<Long>();
    	
    	
    	//按小区发送: buildingNames 和 addressIds 为空。
    	if((buildingNames == null || buildingNames.size() == 0) && (addressIds == null || addressIds.size() == 0)){
    		ListAddressByKeywordCommand comand = new ListAddressByKeywordCommand();
    		comand.setCommunityId(cmd.getCommunityId());
    		comand.setKeyword("");
    		List<Address> addresses= addressService.listAddressByKeyword(comand);
    		if(addresses != null && addresses.size() > 0){
    			for (Address address : addresses) {
    				Family family = familyProvider.findFamilyByAddressId(address.getId());
    				if(family != null){
    					familyIds.add(family.getId());
    				}
    				List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(communityId, address.getId());
    				owners.addAll(ownerList);

				}
    		}
    	}
    	
    	//按地址发送：
    	else if(addressIds != null && addressIds.size()  > 0){
    		for (Long addressId : addressIds) {
				Family family = familyProvider.findFamilyByAddressId(addressId);
				if(family != null){
					familyIds.add(family.getId());
				}
				List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(communityId, addressId);
				owners.addAll(ownerList);
			}
    	}
    	
    	//按楼栋发送：
    	else if((addressIds == null || addressIds.size()  == 0 )  && (buildingNames != null && buildingNames.size() > 0)){
    		for (String buildingName : buildingNames) {
    			int pageSize = configProvider.getIntValue("pagination.page.size",  AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
    			List<ApartmentDTO> addresses =  addressProvider.listApartmentsByBuildingName(communityId, buildingName, 1, pageSize);
        		if(addresses != null && addresses.size() > 0){
        			for (ApartmentDTO address : addresses) {
        				Family family = familyProvider.findFamilyByAddressId(address.getAddressId());
        				if(family != null){
        					familyIds.add(family.getId());
        				}
        				List<CommunityPmOwner> ownerList = propertyMgrProvider.listCommunityPmOwners(communityId, address.getAddressId());
        				owners.addAll(ownerList);
    				}
        		}
			}
		}
    	
    	if(familyIds != null && familyIds.size() > 0){
    		for (Long familyId : familyIds) {
    			sendNoticeToFamilyById(familyId, cmd.getMessage());
			}
    	}
    	
    	//处理业主信息表 :1- 是user，已加入家庭，发家庭消息已包含该user。 2- 是user，还未加入家庭，发个人信息。 3-不是user，发短信。
    	processCommunityPmOwner(communityId,owners,cmd.getMessage());
    }
    
    private void processCommunityPmOwner(Long communityId,List<CommunityPmOwner> owners,String message) {
    	List<String> phones = new ArrayList<String>();
    	List<Long> userIds = new ArrayList<Long>();
		if(owners != null && owners.size() > 0){
			for (CommunityPmOwner communityPmOwner : owners) {
				User userPhone = userService.findUserByIndentifier(communityPmOwner.getContactToken());
				if(userPhone == null){
					phones.add(communityPmOwner.getContactToken());
				}
				else{
					Family family = familyProvider.findFamilyByAddressId(communityPmOwner.getAddressId());
    				if(family != null){
    					GroupMember member = groupProvider.findGroupMemberByMemberInfo(family.getId(), GroupDiscriminator.FAMILY.getCode(), userPhone.getId());
    					if(member == null){
    						userIds.add(userPhone.getId());
    					}
    				}
					
				}
			}
		}
		
		//是user，还未加入家庭，发个人信息.
		if(userIds != null && userIds.size() > 0){
			for (Long userId : userIds) {
				sendNoticeToUserById(userId, message);
			}
		}
		
		//不是user，发短信。
		if(phones != null && phones.size() > 0 ){
			for (String phone : phones) {
				smsProvider.sendSms(phone, message);
			}
		}
		
	}

	public void sendNoticeToFamilyById(Long familyId,String message){
    	MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_FAMILY);
        messageDto.setSenderUid(UserContext.current().getUser().getId());
        messageDto.setChannels(new MessageChannel("group", String.valueOf(familyId)));
        messageDto.setMetaAppId(AppConstants.APPID_FAMILY);
        messageDto.setBody(message);
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_FAMILY, "group", 
       		 String.valueOf(familyId), messageDto, MessagingService.MSG_FLAG_STORED);
    }
	
	public void sendNoticeToUserById(Long userId,String message){
    	MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_FAMILY);
        messageDto.setSenderUid(UserContext.current().getUser().getId());
        messageDto.setChannels(new MessageChannel("user", String.valueOf(userId)));
        messageDto.setMetaAppId(AppConstants.APPID_USER);
        messageDto.setBody(message);
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_USER, "user", 
       		 String.valueOf(userId), messageDto, MessagingService.MSG_FLAG_STORED);
    }
	
	@Override
	public void sendMsgToPMGroup(PropCommunityIdMessageCommand cmd) {
		List<CommunityPmMember> memberList = propertyMgrProvider.listCommunityPmMembers(cmd.getCommunityId());
		if(memberList != null && memberList.size() > 0){
			for (CommunityPmMember communityPmMember : memberList) {
				sendNoticeToUserById(communityPmMember.getTargetId(), cmd.getMessage());
			}
		}
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
    	if(cmd.getForumId() == null){
    		cmd.setForumId(ForumConstants.SYSTEM_FORUM);
    	}
    	if(cmd.getPrivateFlag() == null){
    		cmd.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
    	}
    	if(cmd.getVisibleRegionType() == null){
    		cmd.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
    	}
    	if(cmd.getTargetTag() == null ){
    		cmd.setTargetTag(PostEntityTag.USER.getCode());
    	}
    	PostDTO post = forumService.createTopic(cmd);
    	return post;
	}
	
	@Override
	public ListPostCommandResponse queryTopicsByCategory(QueryTopicByCategoryCommand cmd) {
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
    	return forumService.queryTopicsByCategory(cmd);
	}
}
