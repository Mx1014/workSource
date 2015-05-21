// @formatter:off
package com.everhomes.pm;

import java.io.IOException;
import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.spi.DirStateFactory.Result;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.address.ApartmentDTO;
import com.everhomes.address.BuildingDTO;
import com.everhomes.address.ListAddressCommand;
import com.everhomes.address.ListApartmentByKeywordCommand;
import com.everhomes.address.ListBuildingByKeywordCommand;
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
import com.everhomes.family.FindFamilyByAddressIdCommand;
import com.everhomes.family.GetFamilyCommand;
import com.everhomes.family.ListOwningFamilyMembersCommand;
import com.everhomes.forum.Post;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
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
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.handler.ProcessBillModel1;
import com.everhomes.util.excel.handler.PropMgrBillHandler;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

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
    	//先判断，如果不属于这个小区的物业，才添加物业成员
    	List<CommunityPmMember> list = propertyMgrProvider.findPmMemberByCommunityAndTarget(cmd.getCommunityId(), cmd.getTargetType(), cmd.getTargetId());
    	if(list == null || list.size() == 0)
    	{
    		CommunityPmMember communityPmMember = ConvertHelper.convert(cmd, CommunityPmMember.class);
        	communityPmMember.setStatus(PmMemberStatus.ACTIVE.getCode());
        	propertyMgrProvider.createPropMember(communityPmMember);
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
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<CommunityPmMember> entityResultList = propertyMgrProvider.listCommunityPmMembers(cmd.getCommunityId(), null, cmd.getPageOffset(),pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ 
                	 PropertyMemberDTO dto =ConvertHelper.convert(r, PropertyMemberDTO.class);
                	 dto.setCommunityName(community.getName());
                	 return dto; })
                 .collect(Collectors.toList()));
    	
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
    		for (int i = 1; i <= 100; i++) {
        		ListAddressCommand comand = new ListAddressCommand();
        		comand.setCommunityId(cmd.getCommunityId());
        		comand.setOffset(new Long(i));
        		comand.setPageSize(new Long(100));
        		Tuple<Integer,List<Address>> addresses= addressService.listAddressByCommunityId(comand);
        		List<Address> addessList = addresses.second();
        		if(addessList != null && addessList.size() > 0)
        		{
        			for (Address address : addessList) {
    					CommunityAddressMapping m = new CommunityAddressMapping();
    					m.setAddressId(address.getId());
    					m.setCommunityId(cmd.getCommunityId());
    					m.setName(address.getAddress());
    					m.setLivingStatus((byte)0);
    					propertyMgrProvider.createPropAddressMapping(m);
    				}
        		}
        		else
        		{
        			break;
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
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<CommunityAddressMapping> entityResultList = propertyMgrProvider.listCommunityAddressMappings(cmd.getCommunityId(), cmd.getPageOffset(), pageSize);
    	commandResponse.setMembers( entityResultList.stream()
                 .map(r->{ 
                	 PropAddressMappingDTO dto = ConvertHelper.convert(r, PropAddressMappingDTO.class);
                	 Address address = addressProvider.findAddressById(dto.getAddressId());
                	 if(address != null)
                		 dto.setAddressName(address.getAddress());
                	 return  dto;})
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
                    "Unable to find the community.");
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
                    "Unable to find the community.");
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
                    "Unable to find the community.");
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

    	familyProvider.approveMember(cmd.getFamilyId(),cmd.getUserId());
		
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
    	Byte operatorRole = 0;
    	familyProvider.rejectMember(cmd.getFamilyId(), cmd.getUserId(), reason, operatorRole);
		
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
    	familyProvider.revokeMember(cmd.getFamilyId(), cmd.getUserId(), reason);
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
    	int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<FamilyDTO>  entityResultList = familyProvider.listWaitApproveFamily(cmd.getCommunityId(), new Long(cmd.getPageOffset()), new Long(pageSize));
    	commandResponse.setMembers( entityResultList.stream()
                .map(r->{ return r; })
                .collect(Collectors.toList()));
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
}
