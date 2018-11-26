package com.everhomes.blacklist;

import com.everhomes.acl.*;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalBusSubscriber.Action;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.blacklist.*;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;


@Service
public class BlacklistServiceImpl implements BlacklistService, LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlacklistServiceImpl.class);

	@Autowired
	private BlacklistProvider blacklistProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private RolePrivilegeService rolePrivilegeService;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private AclProvider aclProvider;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private LocaleTemplateService localeTemplateService;
	
    @Autowired
    private LocalBus localBus;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    void setup() {
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhUsers.class, null), this);   
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
	@Override
	public ListUserBlacklistsResponse listUserBlacklists(ListUserBlacklistsCommand cmd) {
		ListUserBlacklistsResponse res = new ListUserBlacklistsResponse();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Timestamp startTime = null == cmd.getStartTime() ? null : new Timestamp(cmd.getStartTime());
		Timestamp endTime = null == cmd.getEndTime() ? null : new Timestamp(cmd.getEndTime());
		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<UserBlacklist> userBlacklists = blacklistProvider.listUserBlacklists(namespaceId, locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_USER_BLACKLISTS.STATUS.eq(UserBlacklistStatus.ACTIVE.getCode()));
				if(!StringUtils.isEmpty(cmd.getOwnerType())){
					query.addConditions(Tables.EH_USER_BLACKLISTS.SCOPE_TYPE.eq(cmd.getOwnerType()));
				}

				if(null != cmd.getOwnerId()){
					query.addConditions(Tables.EH_USER_BLACKLISTS.SCOPE_ID.eq(cmd.getOwnerId()));
				}

				if(null != startTime){
					query.addConditions(Tables.EH_USER_BLACKLISTS.CREATE_TIME.ge(startTime));
				}

				if(null != endTime){
					query.addConditions(Tables.EH_USER_BLACKLISTS.CREATE_TIME.le(endTime));
				}

				if(!StringUtils.isEmpty(cmd.getKeywords())){
					query.addConditions(Tables.EH_USER_BLACKLISTS.CONTACT_NAME.like(cmd.getKeywords() + "%").or(Tables.EH_USER_BLACKLISTS.CONTACT_TOKEN.like(cmd.getKeywords() + "%")));
				}
				return query;
			}
		});
		res.setDtos(userBlacklists.stream().map(r -> {
			UserBlacklistDTO dto = ConvertHelper.convert(r, UserBlacklistDTO.class);
			dto.setCreateTime(r.getCreateTime().getTime());
			dto.setUserId(r.getOwnerUid());
			return dto;
		}).collect(Collectors.toList()));
		res.setNextPageAnchor(locator.getAnchor());

		return res;
	}

	@Override
	public UserBlacklistDTO checkUserBlacklist(CheckUserBlacklistCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		UserBlacklist userBlacklist = blacklistProvider.findUserBlacklistByContactToken(namespaceId, null, 0L, cmd.getContactToken());
		
		if(userBlacklist == null) {
		    userBlacklist = blacklistProvider.findUserBlacklistByContactToken(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getContactToken());    
		}
		

		if(null != userBlacklist){
			LOGGER.error("User blacklist already exists, contactToken ={},", cmd.getContactToken());
			throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_USERBLACKLIST_EXISTS,
					"User blacklist already exists");
		}

		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());

		if(null == userIdentifier){
			LOGGER.error("user does not exist, contactToken ={},", cmd.getContactToken());
			throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_USER_NOT_EXISTS,
					"user does not exist");
		}

		UserBlacklistDTO userBlacklistDTO = new UserBlacklistDTO();
		userBlacklistDTO.setUserId(userIdentifier.getOwnerUid());
		userBlacklistDTO.setContactToken(userIdentifier.getIdentifierToken());

		User user = userProvider.findUserById(userIdentifier.getOwnerUid());
		if(null != user){
			userBlacklistDTO.setContactName(user.getNickName());
		}
		return userBlacklistDTO;
	}
	
	private UserBlacklistDTO addUserBlacklistByPhone(AddUserBlacklistCommand cmd) {
	    Integer namespaceId = UserContext.getCurrentNamespaceId();
	    UserBlacklist userBlack = blacklistProvider.findUserBlacklistByContactToken(namespaceId, null, 0L, cmd.getPhone());
	    if(userBlack != null) {
	        throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_USERBLACKLIST_EXISTS, "User blacklist already exists");
	    }
	    
	    cmd.setOwnerId(0L);
	    cmd.setOwnerType("");
	    UserBlacklist userBlacklist = new UserBlacklist();
	    userBlacklist.setNamespaceId(UserContext.getCurrentNamespaceId());
	    userBlacklist.setScopeId(0L);
	    userBlacklist.setStatus(UserBlacklistStatus.ACTIVE.getCode());
	    userBlacklist.setOwnerUid(0L);
	    userBlacklist.setContactToken(cmd.getPhone());
	    
	    blacklistProvider.createUserBlacklist(userBlacklist);
	    UserBlacklistDTO dto = ConvertHelper.convert(userBlacklist, UserBlacklistDTO.class);
	    dto.setCreateTime(userBlacklist.getCreateTime().getTime());
	    dto.setUserId(userBlacklist.getOwnerUid());
	    return dto;
	}

	@Override
	public UserBlacklistDTO addUserBlacklist(AddUserBlacklistCommand cmd) {

		User user = null;
		
		if(cmd.getUserId() != null && !cmd.getUserId().equals(0L)) {
		    user = userProvider.findUserById(cmd.getUserId());   
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		if(null == user){
//			LOGGER.error("user does not exist, userId ={},", cmd.getUserId());
//			throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_USER_NOT_EXISTS,
//					"user does not exist");
		    //Added by Janson
		    return addUserBlacklistByPhone(cmd);
		}
		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		if(null == userIdentifier){
			LOGGER.error("user does not exist, userId ={},", cmd.getUserId());
			throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_USER_NOT_EXISTS,
					"user does not exist");
		}

		if(StringUtils.isEmpty(cmd.getOwnerType())){
			cmd.setOwnerType("");
		}

		if(null == cmd.getOwnerId()){
			cmd.setOwnerId(0L);
		}
		UserBlacklist userBlacklist = new UserBlacklist();
		userBlacklist.setNamespaceId(namespaceId);
		userBlacklist.setScopeId(cmd.getOwnerId());
		userBlacklist.setScopeType(cmd.getOwnerType());
		userBlacklist.setStatus(UserBlacklistStatus.ACTIVE.getCode());
		userBlacklist.setOwnerUid(cmd.getUserId());
		userBlacklist.setContactToken(userIdentifier.getIdentifierToken());
		userBlacklist.setContactType(userIdentifier.getIdentifierType());
		userBlacklist.setContactName(user.getNickName());
		userBlacklist.setGender(user.getGender());

		dbProvider.execute((TransactionStatus status) -> {
			blacklistProvider.createUserBlacklist(userBlacklist);
			rolePrivilegeService.assignmentPrivileges(cmd.getOwnerType(),cmd.getOwnerId(),EntityType.USER.getCode(), userBlacklist.getOwnerUid(), BlacklistErrorCode.SCOPE, cmd.getPrivilegeIds());
			return null;
		});
		// 加入黑名单发消息
		this.sendMessageToUser(user, BlacklistNotificationTemplateCode.JOIN_USER_BLACKLIST, new HashMap<>(), BlacklistNotificationTemplateCode.SCOPE, "由于您的发言涉及部分违反相关版规行为，您已被禁言，将不能正常使用部分板块的发言功能。如有疑问，请联系左邻客服。");

		UserBlacklistDTO dto = ConvertHelper.convert(userBlacklist, UserBlacklistDTO.class);
		dto.setCreateTime(userBlacklist.getCreateTime().getTime());
		dto.setUserId(userBlacklist.getOwnerUid());
		return dto;
	}

	@Override
	public void editUserBlacklist(AddUserBlacklistCommand cmd) {
	   User user = null;
	   if(cmd.getUserId() != null && !cmd.getUserId().equals(0l)) {
	       user = userProvider.findUserById(cmd.getUserId());
	   }

		if(null == user){
			LOGGER.error("user does not exist, userId ={},", cmd.getUserId());
			throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_USER_NOT_EXISTS,
					"user does not exist");
		}
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		if(null == userIdentifier){
			LOGGER.error("user does not exist, userId ={},", cmd.getUserId());
			throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_USER_NOT_EXISTS,
					"user does not exist");
		}

		if(StringUtils.isEmpty(cmd.getOwnerType())){
			cmd.setOwnerType("");
		}

		if(null == cmd.getOwnerId()){
			cmd.setOwnerId(0L);
		}

		dbProvider.execute((TransactionStatus status) -> {
			AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ROLE.getCode(), RoleConstants.BLACKLIST);
			List<Long> privilegeIds = new ArrayList<>();
			List<Acl> acls = aclProvider.getResourceAclByRole("system",null, descriptor);
			for (Acl acl: acls) {
				privilegeIds.add(acl.getPrivilegeId());
			}
			rolePrivilegeService.deleteAcls(cmd.getOwnerType(),cmd.getOwnerId(), EntityType.USER.getCode(), cmd.getUserId(), privilegeIds);
			rolePrivilegeService.assignmentPrivileges(cmd.getOwnerType(),cmd.getOwnerId(),EntityType.USER.getCode(), cmd.getUserId(), BlacklistErrorCode.SCOPE, cmd.getPrivilegeIds());
			return null;
		});
	}

	@Override
	public void batchDeleteUserBlacklist(BatchDeleteUserBlacklistCommand cmd) {
		if(StringUtils.isEmpty(cmd.getOwnerType())){
			cmd.setOwnerType("");
		}

		if(null == cmd.getOwnerId()){
			cmd.setOwnerId(0L);
		}
		if(null != cmd.getBlacklistIds() && cmd.getBlacklistIds().size() > 0){
			AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ROLE.getCode(), RoleConstants.BLACKLIST);
			List<Long> privilegeIds = new ArrayList<>();
			List<Acl> acls = aclProvider.getResourceAclByRole("system",null, descriptor);
			for (Acl acl: acls) {
				privilegeIds.add(acl.getPrivilegeId());
			}
			dbProvider.execute((TransactionStatus status) -> {
				for (Long blacklistId: cmd.getBlacklistIds()) {
					UserBlacklist userBlacklist = blacklistProvider.findUserBlacklistById(blacklistId);
					if(null != userBlacklist){
						userBlacklist.setStatus(UserBlacklistStatus.INACTIVE.getCode());
						blacklistProvider.updateUserBlacklist(userBlacklist);
						if(!userBlacklist.getOwnerUid().equals(0l)) {
						    //Added by Janson
						    rolePrivilegeService.deleteAcls(cmd.getOwnerType(),cmd.getOwnerId(),EntityType.USER.getCode(),userBlacklist.getOwnerUid(), privilegeIds);    
						}
						
					}
				}
				return null;
			});

			// 解除后 发消息
			for (Long blacklistId: cmd.getBlacklistIds()) {
				UserBlacklist userBlacklist = blacklistProvider.findUserBlacklistById(blacklistId);
				if(null != userBlacklist){
					User user = userProvider.findUserById(userBlacklist.getOwnerUid());
					if(null != user){
						this.sendMessageToUser(user, BlacklistNotificationTemplateCode.RELIEVE_USER_BLACKLIST, new HashMap<>(), BlacklistNotificationTemplateCode.SCOPE, "您的禁言已被解除，可继续使用各大板块的发言功能。如有疑问，请联系左邻客服。");
					}
				}
			}

		}
	}

	@Override
	public List<BlacklistPrivilegeDTO> listBlacklistPrivileges() {
		List<BlacklistPrivilegeDTO> dtos = new ArrayList<>();
		List<Privilege> privileges = rolePrivilegeService.listPrivilegesByTarget("system",null,EntityType.ROLE.getCode(), RoleConstants.BLACKLIST, null);
		for (Privilege privilege: privileges) {
			BlacklistPrivilegeDTO pririvileDTO = new BlacklistPrivilegeDTO();
			pririvileDTO.setPrivilegeId(privilege.getId());
			pririvileDTO.setPrivilegeName(privilege.getName());
			pririvileDTO.setDescription(privilege.getDescription());
			dtos.add(pririvileDTO);
		}
		return dtos;
	}

	@Override
	public List<BlacklistPrivilegeDTO> listUserBlacklistPrivileges(ListUserBlacklistPrivilegesCommand cmd) {
		if(StringUtils.isEmpty(cmd.getOwnerType())){
			cmd.setOwnerType("");
		}

		if(null == cmd.getOwnerId()){
			cmd.setOwnerId(0L);
		}
		
		//added by Janson
		if(cmd.getUserId() == null || cmd.getUserId().equals(0l)) {
		    return listBlacklistPrivileges();
		}
		
		List<BlacklistPrivilegeDTO> dtos = new ArrayList<>();

		List<Privilege> privileges = rolePrivilegeService.listPrivilegesByTarget(cmd.getOwnerType(),cmd.getOwnerId(), EntityType.USER.getCode(), cmd.getUserId(), BlacklistErrorCode.SCOPE);
		for (Privilege privilege: privileges) {
			BlacklistPrivilegeDTO pririvileDTO = new BlacklistPrivilegeDTO();

			pririvileDTO.setPrivilegeId(privilege.getId());
			pririvileDTO.setPrivilegeName(privilege.getName());
			pririvileDTO.setDescription(privilege.getDescription());
			dtos.add(pririvileDTO);
		}

		return dtos;
	}
	
	@Override
	public boolean checkUserPrivilege(Integer namespaceId, String phone, Long privilegeId) {
	    UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
	    ListUserBlacklistPrivilegesCommand cmd = new ListUserBlacklistPrivilegesCommand();
	    if(userIdentifier == null) {
	        cmd.setUserId(0L);
	        cmd.setOwnerId(0L);
	        UserBlacklist userBlack = blacklistProvider.findUserBlacklistByContactToken(namespaceId, null, 0L, phone);
	        if(userBlack == null) {
	            return false;
	        }
	        
	    } else {
	        cmd.setUserId(userIdentifier.getOwnerUid());
	        cmd.setOwnerId(0L);
	    }
	    List<BlacklistPrivilegeDTO> dtos = listUserBlacklistPrivileges(cmd);
	    if(dtos != null) {
	        for(BlacklistPrivilegeDTO dto : dtos) {
	            if(dto.getPrivilegeId().equals(privilegeId)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	private void sendMessageToUser(User user, int templateCode, Map<String,Object> map, String scope, String defaultValue) {
		String message = this.localeTemplateService.getLocaleTemplateString(scope,
				templateCode, user.getLocale(), map, defaultValue);
		if(message != null && message.length() != 0) {
			String channelType = MessageChannelType.USER.getCode();
			String channelToken = String.valueOf(user.getId());
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setChannels(new MessageChannel(channelType, channelToken));
			messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
			messageDto.setBody(message);
			messageDto.setMetaAppId(AppConstants.APPID_DEFAULT);
			messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, channelType,
					channelToken, messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
		}
	}
	
	@Override
	public void updateUserBlackWhenUserSignup(User user, UserIdentifier userIdentifier) {
	    UserBlacklist userBlack = blacklistProvider.findUserBlacklistByContactToken(user.getNamespaceId(), null, 0L, userIdentifier.getIdentifierToken());
	    if(userBlack == null) {
	        return;
	    }
	    
	    userBlack.setStatus(UserBlacklistStatus.INACTIVE.getCode());
	    blacklistProvider.updateUserBlacklist(userBlack);
	    
	    UserContext.setCurrentNamespaceId(user.getNamespaceId());
	    UserContext.setCurrentUser(user);
	    
	    AddUserBlacklistCommand cmd = new AddUserBlacklistCommand();
	    cmd.setOwnerId(0l);
	    cmd.setUserId(user.getId());
	    cmd.setOwnerType("");
	    
	    List<Privilege> privileges = rolePrivilegeService.listPrivilegesByTarget("system", null, EntityType.ROLE.getCode(), RoleConstants.BLACKLIST, null);
	    List<Long> privilegeIds = privileges.stream().map(p -> {
	        return p.getId();
	    }).collect(Collectors.toList());
	    
	    cmd.setPrivilegeIds(privilegeIds);
	    
	    addUserBlacklist(cmd);
	    
	    UserContext.setCurrentNamespaceId(null);
       UserContext.setCurrentUser(null);
	}

    @Override
    public Action onLocalBusMessage(Object arg0, String arg1, Object arg2,
            String arg3) {
        BlacklistService srv = this;
        if(arg1.indexOf(EntityType.USER.getCode()) >= 0) {
            Long userId = (Long)arg2;
            
            User user = userProvider.findUserById(userId);
            if(user == null) {
                return Action.none;
            }
            
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
            if(userIdentifier == null) {
                return Action.none;
            }
            
            ExecutorUtil.submit(new Runnable() {

                @Override
                public void run() {
                    try{
                        srv.updateUserBlackWhenUserSignup(user, userIdentifier);    
                    }catch(Exception ex) {
                        LOGGER.error("updateUserBlackWhenUserSignup failed ex=", ex);
                    }
                    
                }
                
            });   
        }
        
        return Action.none;
    }
}
