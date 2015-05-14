// @formatter:off
package com.everhomes.pm;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressService;
import com.everhomes.address.ListAddressCommand;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.core.AppConfig;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Post;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class PropertyMgrServiceImpl implements PropertyMgrService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrServiceImpl.class);
    
    @Autowired
    private PropertyMgrProvider propertyMgrProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private SmsProvider smsProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    
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
    	CommunityPmMember communityPmMember = ConvertHelper.convert(cmd, CommunityPmMember.class);
    	propertyMgrProvider.createPropMember(communityPmMember);
    }
    
    @Override
	public void deletePropMember(DeletePropMemberCommand cmd) {
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
    	propertyMgrProvider.deletePropMember(cmd.getMemberId());
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
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<CommunityPmMember> entityResultList = propertyMgrProvider.listCommunityPmMembers(cmd.getCommunityId(), cmd.getUserId(), null, cmd.getPageOffset(),pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ 
                	 PropertyMemberDTO dto =ConvertHelper.convert(r, PropertyMemberDTO.class);
                	 dto.setCommunityName(community.getName());
                	 return dto; })
                 .collect(Collectors.toList()));
    	
        return commandResponse;
    }

	@Override
	public void improtCommunityAddressMapping(Long communityId) {
		User user  = UserContext.current().getUser();
		Community community = communityProvider.findCommunityById(communityId);
    	if(community == null){
    		LOGGER.error("Unable to find the community.communityId=" + communityId);
    		 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                     "Unable to find the community.");
    	}
    	//权限控制
    	for (int i = 1; i <= 100; i++) {
    		ListAddressCommand cmd = new ListAddressCommand();
    		cmd.setCommunityId(communityId);
    		cmd.setOffset(new Long(i));
    		cmd.setPageSize(new Long(100));
    		Tuple<Integer,List<Address>> addresses= addressService.listAddressByCommunityId(cmd);
    		List<Address> addessList = addresses.second();
    		if(addessList != null && addessList.size() > 0)
    		{
    			for (Address address : addessList) {
					CommunityAddressMapping m = new CommunityAddressMapping();
					m.setAddressId(address.getId());
					m.setCommunityId(communityId);
					m.setName(address.getAddress());
					propertyMgrProvider.createPropAddressMapping(m);
				}
    		}
    		else
    		{
    			break;
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
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(cmd.getCommunityId(), cmd.getPageOffset(), pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ return ConvertHelper.convert(r, PropAddressMappingDTO.class); })
                 .collect(Collectors.toList()));
    	
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
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<CommunityPmBill> entityResultList = propertyMgrProvider.listCommunityPmBills(cmd.getCommunityId(), cmd.getDateStr(), cmd.getAddress(), cmd.getPageOffset(), pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ return ConvertHelper.convert(r, PropBillDTO.class); })
                 .collect(Collectors.toList()));
    	
        return commandResponse;
    }
    
    @Override
    public ListPropOwnerCommandResponse listPMPropertyOwnerInfo(ListPropOwnerCommand cmd) {
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
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<CommunityPmOwner> entityResultList = propertyMgrProvider.listCommunityPmOwners(cmd.getCommunityId(),cmd.getAddress(),cmd.getContactToken(), cmd.getPageOffset(), pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ return ConvertHelper.convert(r, PropOwnerDTO.class); })
                 .collect(Collectors.toList()));
    	
        return commandResponse;
    }
    
    @Override
    public void assignPMTopics(AssginPmTopicCommand cmd) {
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
            if(pmTaskList != null && !pmTaskList.isEmpty()){
                LOGGER.error("Pm taks is exists.");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                        "Task is exists.");
            }
            CommunityPmTasks task = new CommunityPmTasks();
            task.setCommunityId(communityId);
            task.setEntityId(topicId);
            task.setEntityType(EntityType.TOPIC.getCode());
            task.setTargetId(cmd.getUserId());
            task.setTaskType(EntityType.USER.getCode());
            task.setTaskStatus(cmd.getStatus());
            this.propertyMgrProvider.createPmTask(task);
            //自动回复
            
            //发送短信
            Post topic = new Post();
            sendMSMToUser(topic,cmd.getUserId(),cmd.getStatus());
        }
    }

    private void sendMSMToUser(Post topic, Long userId, Byte status) {
        //??短信模板
        String content = "";
        List<UserIdentifier> userList = this.userProvider.listUserIdentifiersOfUser(userId);
        userList.parallelStream().filter((u) -> {
            if(u.getIdentifierType() != IdentifierType.MOBILE.getCode())
                return false;
            return true;
        });
        String cellPhone = userList.get(0).getIdentifierToken();
        this.smsProvider.sendSms(cellPhone, content);
        
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
        
        List<PropInvitedUserDTO> userDTOs = this.propertyMgrProvider.listInvitedUsers(cmd.getCommunityId(),
                cmd.getContactToken(),offset,pageSize);
        return new ListPropInvitedUserCommandResponse(0L,userDTOs);
    }
}
