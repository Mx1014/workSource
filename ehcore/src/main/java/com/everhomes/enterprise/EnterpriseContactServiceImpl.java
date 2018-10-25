// @formatter:off
package com.everhomes.enterprise;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Role;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.organization.*;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.QuestionMetaActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class EnterpriseContactServiceImpl implements EnterpriseContactService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EnterpriseContactServiceImpl.class);

	@Autowired
	DbProvider dbProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	EnterpriseService enterpriseService;

	@Autowired
	EnterpriseProvider enterpriseProvider;

	@Autowired
	private EnterpriseContactProvider enterpriseContactProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private UserService userService;

	@Autowired
	MessagingService messagingService;

	@Autowired
	LocaleTemplateService localeTemplateService;

	@Autowired
	GroupService groupService;

	@Autowired
	private GroupProvider groupProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private OrganizationRoleMapProvider organizationRoleMapProvider;
    
    @Autowired
    private AclProvider aclProvider;

    @Autowired
	private OrganizationService organizationService;
    
    

	@Override
	public void addContactGroupMember(EnterpriseContactGroupMember member) {
		this.enterpriseContactProvider.createContactGroupMember(member);
	}

	@Override
	public void removeContactGroupMember(EnterpriseContactGroupMember member) {
		this.enterpriseContactProvider.deleteContactGroupMember(member);
	}

	@Override
	public List<EnterpriseContact> queryContactByPhone(String phone) {
		return this.enterpriseContactProvider
				.queryEnterpriseContactByPhone(phone);
	}

	@Override
	public List<Enterprise> queryEnterpriseByPhone(String phone) {
		return this.enterpriseProvider.queryEnterpriseByPhone(phone);
	}

	@Override
	public EnterpriseContact queryContactByUserId(Long enterpriseId, Long userId) {
		return this.enterpriseContactProvider.queryContactByUserId(
				enterpriseId, userId);
	}

	@Override
	public EnterpriseContact processUserForContact(UserIdentifier identifier) {
		try {
		    User user = userProvider.findUserById(identifier.getOwnerUid());
	        List<Enterprise> enterprises = this.enterpriseProvider.queryEnterpriseByPhone(identifier.getIdentifierToken());
	        Map<Long, Long> ctx = new HashMap<Long, Long>();
	        for (Enterprise enterprise : enterprises) {
	            if(enterprise.getNamespaceId() == null || !enterprise.getNamespaceId().equals(identifier.getNamespaceId())) {
	                if(LOGGER.isDebugEnabled()) {
	                    LOGGER.debug("Ignore the enterprise who is dismatched to namespace, enterpriseId=" + enterprise.getId() 
	                        + ", enterpriseNamespaceId=" + enterprise.getNamespaceId() + ", userId=" + identifier.getOwnerUid() 
	                        + ", userNamespaceId=" + identifier.getNamespaceId());
	                }
	                continue;
	            }
	            EnterpriseContact contact = this.getContactByPhone(enterprise.getId(), identifier.getIdentifierToken());
	            if (contact != null) {
	                GroupMemberStatus status = GroupMemberStatus.fromCode(contact.getStatus());
	                if(!contact.getStatus().equals(GroupMemberStatus.ACTIVE.getCode())) {
	                    contact.setUserId(user.getId());
	                    contact.setStatus(GroupMemberStatus.ACTIVE.getCode());
	                    updatePendingEnterpriseContactToAuthenticated(contact);
	                    updateEnterpriseContactUser(contact);
	                    
	                    sendMessageForContactApproved(ctx, contact);
	                    if(LOGGER.isInfoEnabled()) {
	                        LOGGER.info("User join the enterprise automatically, userId=" + identifier.getOwnerUid() 
	                            + ", contactId=" + contact.getId() + ", enterpriseId=" + enterprise.getId());
	                    }
	                } else {
	                    if(LOGGER.isDebugEnabled()) {
	                        LOGGER.debug("Enterprise contact is already authenticated, userId=" + identifier.getOwnerUid() 
	                            + ", contactId=" + contact.getId() + ", enterpriseId=" + enterprise.getId());
	                    }
	                }
	                return contact;
	            } else {
	                if(LOGGER.isDebugEnabled()) {
	                    LOGGER.debug("Enterprise contact not found, ignore to match contact, userId=" + identifier.getOwnerUid() 
	                        + ", enterpriseId=" + enterprise.getId());
	                }
	            }
	        }
		} catch(Exception e) {
		    LOGGER.error("Failed to processStat the enterprise contact for the user, userId=" + identifier.getOwnerUid(), e);
		}
		return null;
	}

	/**
	 * 同意某用户绑定到某企业通讯录，此接口应该用不到
	 */
//	@Override
//	public void approveUserToContact(User user, EnterpriseContact contact) {
//		// TODO Auto-generated method stub
//
//	}

	/**
	 * 拒绝用户申请某企业的通讯录
	 */
//	@Override
//	public void rejectUserFromContact(EnterpriseContact contact) {
//		// 设置为删除
//		contact.setStatus(GroupMemberStatus.INACTIVE.getCode());
//		this.enterpriseContactProvider.updateContact(contact);
//
//		// 发消息
//		Map<String, Object> map = new HashMap<String, Object>();
//		Enterprise enterprise = this.enterpriseService
//				.getEnterpriseById(contact.getEnterpriseId());
//		User user = userProvider.findUserById(contact.getUserId());
//		map.put("enterpriseName", enterprise.getName());
//		String scope = EnterpriseNotifyTemplateCode.SCOPE;
//		int code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_REJECT_JOIN;
//		String notifyTextForApplicant = localeTemplateService
//				.getLocaleTemplateString(scope, code, user.getLocale(), map, "");
//		sendUserNotification(user.getId(), notifyTextForApplicant);
//	}

	/**
	 * 申请加入企业
	 * 
	 * @param
	 */
	@Override
	public EnterpriseContactDTO applyForContact(CreateContactByUserIdCommand cmd) {
		// Check exists
		EnterpriseContact existContact = this.enterpriseContactProvider.queryContactByUserId(cmd.getEnterpriseId(), cmd.getUserId());
		if (null != existContact) {
			// Should response error hear
			// contact.setId(existContact.getId());
			return ConvertHelper.convert(existContact, EnterpriseContactDTO.class);
		}

		// TODO check group?
		User user = UserContext.current().getUser();
		Long userId = (user == null) ? 0L : user.getId();
		EnterpriseContact result = this.dbProvider.execute((TransactionStatus status) -> {
			EnterpriseContact contact = new EnterpriseContact();
			contact.setCreatorUid(userId);
			contact.setEnterpriseId(cmd.getEnterpriseId());
			contact.setName(cmd.getName());
			contact.setNickName(cmd.getNickName());
			contact.setAvatar(user.getAvatar());
			contact.setUserId(userId);

			// Create it
			contact.setStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
			this.enterpriseContactProvider.createContact(contact);

			UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(userId,
							IdentifierType.MOBILE.getCode());
			EnterpriseContactEntry entry = new EnterpriseContactEntry();
			entry.setContactId(contact.getId());
			entry.setCreatorUid(UserContext.current().getUser().getId());
			entry.setEnterpriseId(contact.getEnterpriseId());
			entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
			entry.setEntryValue(identifier.getIdentifierToken());

			this.enterpriseContactProvider.createContactEntry(entry);
			
			createOrUpdateUserGroup(contact);

			return contact;
		});

		// Create contact entry from userinfo
		// UserInfo userInfo =
		// this.userService.getUserSnapshotInfoWithPhone(contact.getUserId());
		// List<String> phones = userInfo.getPhones();
		// for(String phone : phones) {
		// //TODO for email
		// EnterpriseContactEntry entry = new EnterpriseContactEntry();
		// entry.setContactId(contact.getId());
		// entry.setCreatorUid(UserContext.current().getUser().getId());
		// entry.setEnterpriseId(contact.getEnterpriseId());
		// entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
		// entry.setEntryValue(phone);
		// try {
		// this.enterpriseContactProvider.createContactEntry(entry);
		// } catch(Exception ex) {
		// LOGGER.info("create phone entry error contactId " + contact.getId() +
		// " phone " + phone);
		// }
		// }
		
		//添加企业部门member表记录
		if(null!= cmd.getGroupId() ){
			EnterpriseContactGroupMember enterpriseContactGroupMember = new EnterpriseContactGroupMember();
			enterpriseContactGroupMember.setContactGroupId(cmd.getGroupId());
			enterpriseContactGroupMember.setEnterpriseId(cmd.getEnterpriseId());
			enterpriseContactGroupMember.setContactId(result.getId());
			enterpriseContactGroupMember.setCreatorUid(UserContext.current().getUser().getId());
			enterpriseContactGroupMember.setCreateTime(new Timestamp(System.currentTimeMillis()));
			enterpriseContactProvider.createContactGroupMember(enterpriseContactGroupMember);
		}
		
		if (result == null) {
			LOGGER.error("Failed to apply for enterprise contact, userId="
					+ userId + ", cmd=" + cmd);
			return null;
		} else {
	        Map<Long, Long> ctx = new HashMap<Long, Long>();
		    sendMessageForContactRequestToJoin(ctx, result);
			return ConvertHelper.convert(result, EnterpriseContactDTO.class);
		}
	}
	
	
	
    private QuestionMetaObject createGroupQuestionMetaObject(Enterprise enterprise, EnterpriseContact requestor, EnterpriseContact target) {
        QuestionMetaObject metaObject = new QuestionMetaObject();
        
        if(enterprise != null) {
            metaObject.setResourceType(EntityType.GROUP.getCode());
            metaObject.setResourceId(enterprise.getId());
        }
        
        if(requestor != null) {
            metaObject.setRequestorUid(requestor.getUserId());
            metaObject.setRequestTime(requestor.getCreateTime());
            metaObject.setRequestorNickName(requestor.getName());
            String avatar = requestor.getAvatar();
            metaObject.setRequestorAvatar(avatar);
            if(avatar != null && avatar.length() > 0) {
                try{
                    String url = contentServerService.parserUri(avatar,  EntityType.USER.getCode(),UserContext.current().getUser().getId());
                    metaObject.setRequestorAvatarUrl(url);
                }catch(Exception e){
                    LOGGER.error("Failed to parse avatar uri of enterprise contact, enterpriseId=" + enterprise.getId() 
                        + ", contactId=" + requestor.getId() + ", userId=" + requestor.getUserId(), e);
                }
            }
            metaObject.setRequestId(requestor.getId());
        }
        
        if(target != null) {
            metaObject.setTargetType(EntityType.USER.getCode());
            metaObject.setTargetId(target.getUserId());
            metaObject.setRequestId(target.getId());
        }
        
        return metaObject;
    }

	/**
	 * 批准用户加入企业
	 */
	@Override
	public void approveContact(EnterpriseContact contact) {
		if (contact.getStatus().equals(GroupMemberStatus.ACTIVE.getCode())) {
		    LOGGER.info("The contact is already authenticated in enterprise");
			return;
		}

		contact.setStatus(GroupMemberStatus.ACTIVE.getCode());
		//this.enterpriseContactProvider.updateContact(contact);
		updatePendingEnterpriseContactToAuthenticated(contact);

		// Set group for this contact
		String applyGroup = contact.getApplyGroup();
		if (applyGroup != null && !applyGroup.isEmpty()) {
			EnterpriseContactGroup group = this.enterpriseContactProvider
					.getContactGroupByName(contact.getEnterpriseId(), applyGroup);
			if (null != group) {
				approveContactToGroup(contact, group);
			}

		}
		//add UserGroup 
		 
//		UserGroup uGroup= this.userProvider.findUserGroupByOwnerAndGroup(contact.getUserId(), contact.getEnterpriseId());
//		if (null == uGroup){
//			Group group = groupProvider.findGroupById(contact.getEnterpriseId());
//			uGroup =new UserGroup();
//			uGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
//			uGroup.setOwnerUid(contact.getUserId());
//			uGroup.setGroupId(group.getId());
//			uGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
//			uGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
//			uGroup.setRegionScopeId(group.getVisibleRegionId());
//			userProvider.createUserGroup(uGroup);
//		}

		// sendMessageForContactApproved(contact);
		sendMessageForContactApproved(null, contact);
	}
	
	private void updateEnterpriseContactUser(EnterpriseContact contact) {
	    try {
	        if(contact == null) {
	            return;
	        }
	        
	        User user = userProvider.findUserById(contact.getUserId());
	        if(user == null) {
	            LOGGER.error("User not found for the enterprise contact, contactId=" + contact.getId() 
	                + ", enterpriseId=" + contact.getEnterpriseId() + ", contactUserId=" + contact.getUserId());
	        } else {
	            boolean needUpdated = false;
	            if(user.getNickName() == null) {
	                String name = contact.getName();
	                String nickName = contact.getNickName();
	                if(nickName != null) {
	                    user.setNickName(nickName);
	                    needUpdated = true;
	                } else {
	                    if(name != null) {
	                        user.setNickName(name);
	                        needUpdated = true;
	                    }
	                }
	            }
	            
	            String avatarUri = contact.getAvatar();
	            if(user.getAvatar() == null && avatarUri != null) {
	                user.setAvatar(avatarUri);
	                needUpdated = true;
	            }
	            
	            if(needUpdated) {
	                userProvider.updateUser(user);
	                if(LOGGER.isDebugEnabled()) {
	                    LOGGER.debug("Update the user info for existing enterprise contact, contactId=" + contact.getId() 
	                        + ", enterpriseId=" + contact.getEnterpriseId() + ", contactUserId=" + contact.getUserId() 
	                        + ", nickName=" + user.getNickName() + ", avatar=" + user.getAvatar());
	                }
	            }
	        }
	    } catch (Exception e) {
	        LOGGER.error("Failed to update enterprise contact user info, contact=" + contact, e);
	    }
	}
	
    /**
     * 当企业成员加入group或者接受别人邀请加入group时，成员状态则待审核变为active，
     * 此时group里的成员数需要增加，为了保证成员数的正确性，需要添加锁；
     * @param
     */
    private void updatePendingEnterpriseContactToAuthenticated(EnterpriseContact contact) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                this.enterpriseContactProvider.updateContact(contact);
                createOrUpdateUserGroup(contact);
                
                Group group = this.groupProvider.findGroupById(contact.getEnterpriseId());
                group.setMemberCount(group.getMemberCount() + 1);
                this.groupProvider.updateGroup(group);
                return null;
            });
            return null;
        });
    }
    
    
	
    @Override
	public void createOrUpdateUserGroup(EnterpriseContact contact) {
        UserGroup userGroup = userProvider.findUserGroupByOwnerAndGroup(contact.getUserId(), contact.getEnterpriseId());
        if(userGroup == null) {
            createUserGroup(contact);
        } else {
            updateUserGroupStatus(contact);
        }
	}

    private void createUserGroup(EnterpriseContact contact) {
        Long enterpriseId = contact.getEnterpriseId();
        Long contactUserId = contact.getUserId();
        
        UserGroup userGroup = new UserGroup();
        userGroup.setOwnerUid(contactUserId);
        userGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
        userGroup.setGroupId(enterpriseId);
        userGroup.setMemberRole(contact.getRole());
        userGroup.setMemberStatus(contact.getStatus());
        this.userProvider.createUserGroup(userGroup);
    }
    
    private void updateUserGroupStatus(EnterpriseContact contact) {
        Long enterpriseId = contact.getEnterpriseId();
        Long contactUserId = contact.getUserId();
        UserGroup userGroup = userProvider.findUserGroupByOwnerAndGroup(contactUserId, enterpriseId);
        userGroup.setMemberStatus(contact.getStatus());
        userProvider.updateUserGroup(userGroup);
    }
	
    /**
     * 删除已认证联系人时，需要减去成员数量
     * @param operatorUid
     * @param contact
     * @param reason
     */
    private void deleteActiveEnterpriseContact(Long operatorUid, EnterpriseContact contact, boolean removeFromDb, String reason) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                if(removeFromDb) {
                    this.enterpriseContactProvider.deleteContactById(contact);
                } else {
                    this.enterpriseContactProvider.updateContact(contact);
                }
                this.userProvider.deleteUserGroup(contact.getUserId(), contact.getEnterpriseId());
                List<Long> contactIds = new ArrayList<Long>();
                contactIds.add(contact.getId());
                this.enterpriseContactProvider.deleteContactEntryByContactId(contactIds);
                
                Group group = this.groupProvider.findGroupById(contact.getEnterpriseId());
                long memberCount = group.getMemberCount() - 1;
                memberCount = (memberCount < 0) ? 0 : memberCount;
                group.setMemberCount(memberCount);
                this.groupProvider.updateGroup(group);
                return null;
            });
            return null;
        });
        
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Enterprise contact is deleted(active), operatorUid=" + operatorUid + ", contactId=" + contact.getUserId() 
                + ", enterpriseId=" + contact.getEnterpriseId() + ", status=" + contact.getStatus() + ", removeFromDb=" + removeFromDb);
        }
    } 
    
    /**
     * 对于通信录联系人，如果是主动申请进来的，若处理待审核状态则可直接删除
     * @param operatorUid 操作者
     * @param
     */
    private void deletePendingEnterpriseContact(Long operatorUid, EnterpriseContact contact, boolean removeFromDb) {
        this.dbProvider.execute((status) -> {
            if(removeFromDb) {
                this.enterpriseContactProvider.deleteContactById(contact);
            } else {
                this.enterpriseContactProvider.updateContact(contact);
            }
            List<Long> contactIds = new ArrayList<Long>();
            contactIds.add(contact.getId());
            this.enterpriseContactProvider.deleteContactEntryByContactId(contactIds);
            this.userProvider.deleteUserGroup(contact.getUserId(), contact.getEnterpriseId());
            return null;
        });
        
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Enterprise contact is deleted(pending), operatorUid=" + operatorUid + ", contactUserId=" + contact.getUserId() 
                + ", enterpriseId=" + contact.getEnterpriseId() + ", status=" + contact.getStatus() + ", removeFromDb=" + removeFromDb);
        }
    } 
    
//	@Override
//	public void approveByContactId(Long contactId) {
//		EnterpriseContact contact = this.enterpriseContactProvider
//				.getContactById(contactId);
//		this.approveContact(contact);
//	}

	/**
	 * 将contact加入组
	 * 
	 * @param contact
	 * @param group
	 */
	public void approveContactToGroup(EnterpriseContact contact,
			EnterpriseContactGroup group) {
		EnterpriseContactGroupMember member = this.enterpriseContactProvider
				.getContactGroupMemberByContactId(contact.getEnterpriseId(),
						contact.getId(), group.getId());
		if (null == member) {
			member = new EnterpriseContactGroupMember();
			member.setContactGroupId(group.getId());
			member.setContactId(contact.getId());
			member.setEnterpriseId(contact.getEnterpriseId());
			// Default, set for approved
			member.setContactStatus(EnterpriseGroupMemberStatus.ACTIVE
					.getCode());
			this.enterpriseContactProvider.createContactGroupMember(member);

			return;
		}

		if (member.getContactStatus() == EnterpriseGroupMemberStatus.WAITING_FOR_APPROVAL
				.getCode()) {
			member.setContactStatus(EnterpriseGroupMemberStatus.ACTIVE
					.getCode());
			this.enterpriseContactProvider.updateContactGroupMember(member);
		}

	}

	/**
	 * 显示所有通讯录列表
	 * 
	 * @param locator
	 * @param enterpriseId
	 * @param
	 * @return
	 */
	@Override
	public List<EnterpriseContactDetail> listContactByEnterpriseId(
			ListingLocator locator, Long enterpriseId, Integer pageSize,String keyWord) {
		Enterprise enterprise = this.enterpriseProvider.getEnterpriseById(enterpriseId);
		if(null==enterprise){
			 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Enterprise id is wrong");
		}
		if (locator.getAnchor() == null)
			locator.setAnchor(0L);
//		int count = PaginationConfigHelper
//				.getPageSize(configProvider, pageSize);
		int count = pageSize==null? AppConstants.PAGINATION_MAX_SIZE:pageSize;
		List<EnterpriseContact> contacts = this.enterpriseContactProvider
				.listContactByEnterpriseId(locator, enterpriseId, count + 1,keyWord);

		Long nextPageAnchor = null;
		if (contacts != null && contacts.size() > count) {
			contacts.remove(contacts.size() - 1);
			nextPageAnchor = contacts.get(contacts.size() - 1).getId();
		}
		locator.setAnchor(nextPageAnchor);
		List<EnterpriseContactDetail> details = new ArrayList<EnterpriseContactDetail>();

		for (EnterpriseContact contact : contacts) {
			EnterpriseContactDetail detail = ConvertHelper.convert(contact,
					EnterpriseContactDetail.class);
			EnterpriseContactGroupMember member = this.enterpriseContactProvider
					.getContactGroupMemberByContactId(enterpriseId,
							contact.getId());
			if (member != null) {
				EnterpriseContactGroup group = this.enterpriseContactProvider
						.getContactGroupById(member.getContactGroupId());
				if (group != null) {
					detail.setGroupName(group.getName());
				}
			}

			// 对设置为1的联系人则不显示其手机号 add by lqs 20160118
			// 由于这部分代码需要重写到organization那边，故在这里不对1进行枚举（1表示私有不显示号码）
			if(!Long.valueOf(1).equals(contact.getIntegralTag1())) {
    			List<EnterpriseContactEntry> entries = this.enterpriseContactProvider
    					.queryContactEntryByContactId(contact);
    			if (entries != null && entries.size() > 0) {
    				detail.setPhone(entries.get(0).getEntryValue());
    			}
			}
			
			if(StringUtils.isEmpty(detail.getAvatar())){
				User user = userProvider.findUserById(contact.getUserId());
				if(null != user){
					detail.setAvatar(user.getAvatar());
				}
				else{
					detail.setAvatar(userService.getUserAvatarUriByGender(contact.getUserId(), enterprise.getNamespaceId(), UserGender.UNDISCLOSURED.getCode()));
				}
				
				
			}
				
			String url = contentServerService.parserUri(detail.getAvatar(),  EntityType.USER.getCode(),detail.getUserId());
			detail.setAvatar(url);
			details.add(detail);

		}

		return details;
	}

	@Override
	public List<EnterpriseContactDetail> listContactByStatus(
			CrossShardListingLocator locator, GroupMemberStatus status, Integer pageSize) {
		int count = PaginationConfigHelper
				.getPageSize(configProvider, pageSize);
		List<EnterpriseContact> contacts = this.enterpriseContactProvider
				.queryContacts(locator, count,
						new ListingQueryBuilderCallback() {

							@Override
							public SelectQuery<? extends Record> buildCondition(
									ListingLocator locator,
									SelectQuery<? extends Record> query) {
								query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.STATUS
										.eq(status.getCode()));
								return query;
							}

						});

		List<EnterpriseContactDetail> details = new ArrayList<EnterpriseContactDetail>();
		for (EnterpriseContact contact : contacts) {
			EnterpriseContactDetail detail = ConvertHelper.convert(contact,
					EnterpriseContactDetail.class);
			EnterpriseContactGroupMember member = this.enterpriseContactProvider
					.getContactGroupMemberByContactId(
							contact.getEnterpriseId(), contact.getId());
			if (member != null) {
				EnterpriseContactGroup group = this.enterpriseContactProvider
						.getContactGroupById(member.getContactGroupId());
				if (group != null) {
					detail.setGroupName(group.getName());
				}
			}

			List<EnterpriseContactEntry> entries = this.enterpriseContactProvider
					.queryContactEntryByContactId(contact);
			if (entries != null && entries.size() > 0) {
				detail.setPhone(entries.get(0).getEntryValue());
			}

			details.add(detail);
		}
		return details;
	}

	/**
	 * 同一个手机好可能在多个企业，可以搜索某一个手机号属于的企业，在查询企业下的具体手机号。
	 * 
	 * @param phone
	 * @return
	 */
	public EnterpriseContactDetail getContactByPhone(Long enterpriseId,
			String phone) {
		EnterpriseContactDetail detail = null;
		EnterpriseContactEntry entry = this.enterpriseContactProvider.getEnterpriseContactEntryByPhone(enterpriseId, phone);
		if (entry != null) {
			EnterpriseContact contact = this.enterpriseContactProvider.getContactById(entry.getContactId());
			if (contact != null) {
				detail = ConvertHelper.convert(contact, EnterpriseContactDetail.class);
				detail.setPhone(phone);
			}
		}

		return detail;
	}

	/**
	 * 后台管理员员创建公司通讯录 TODO for privilege
	 * 
	 * @param contact
	 * @return
	 */
	@Override
	public void createEnterpriseContact(EnterpriseContact contact) {
		contact.setCreatorUid(UserContext.current().getUser().getId());
		this.enterpriseContactProvider.createContact(contact);
	}

	/**
	 * 后台管理员创建公司通讯录条目
	 */
	@Override
	public void createEnterpriseContactEntry(EnterpriseContactEntry entry) {
		this.enterpriseContactProvider.createContactEntry(entry);
	}

	/**
	 * 仅仅用于消息路由
	 */
	@Override
	public List<GroupMember> listMessageGroupMembers(Group group, int pageSize) {
		List<GroupMember> members = new ArrayList<GroupMember>();
		if (group.getDiscriminator().equals(
				GroupDiscriminator.ENTERPRISE.getCode())) {
			ListingLocator locator = new ListingLocator();
			// List approved members
			List<EnterpriseContact> contacts = this.enterpriseContactProvider
					.listContactByEnterpriseId(locator, group.getId(), pageSize,null);
			for (EnterpriseContact contact : contacts) {
				GroupMember gb = new GroupMember();
				gb.setMemberId(contact.getUserId());
				gb.setMemberType(EntityType.USER.getCode());
				members.add(gb);
			}
		}
		return members;
	}    
	
	private List<Long> getEnterpriseAdminIncludeList(Long enterpriseId, Long operatorId, Long targetId) {
        CrossShardListingLocator locator = new CrossShardListingLocator(enterpriseId);
        List<EnterpriseContact> adminMembers = this.enterpriseContactProvider.queryContactByEnterpriseId(locator, enterpriseId, Integer.MAX_VALUE, (loc, query) -> {
            Condition c = Tables.EH_ENTERPRISE_CONTACTS.ROLE.eq(Role.ResourceCreator);
            c = c.or(Tables.EH_ENTERPRISE_CONTACTS.ROLE.eq(Role.ResourceAdmin));
            query.addConditions(c);
            return query;
        });
        List<Long> includeList = new ArrayList<Long>();
        for(EnterpriseContact adminMember : adminMembers) {
            if((operatorId == null || !operatorId.equals(adminMember.getUserId())) 
                && (targetId == null || !targetId.equals(adminMember.getUserId()))) {
                includeList.add(adminMember.getUserId());
            }
        }
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Get enterprise contact admin include list, enterpriseId=" + enterpriseId + ", operatorId=" + operatorId 
                + ", targetId=" + targetId + ", includeList=" + includeList);
        }
        
        return includeList;
    }

    private void sendMessageForContactRequestToJoin(Map<Long, Long> ctx, EnterpriseContact contact) {
        Long check = null;
        if (ctx != null) {
            check = ctx.get(contact.getEnterpriseId());
        }

        if (check == null) {
            if (ctx != null) {
                ctx.put(contact.getEnterpriseId(), 1l);
            }

            Enterprise enterprise = this.enterpriseService.getEnterpriseById(contact.getEnterpriseId());
            User user = userProvider.findUserById(contact.getUserId());

            Map<String, String> map = new HashMap<String, String>();
            map.put("enterpriseName", enterprise.getName());

            String userName = contact.getNickName();
            if (userName == null || userName.trim().isEmpty()) {
                userName = contact.getName();
                if (null != userName) {
                    userName = "";
                }
            }
            map.put("userName", userName);
            String locale = user.getLocale();

            // send notification to who is requesting to join the enterprise
            String scope = EnterpriseNotifyTemplateCode.SCOPE;
            int code = EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_REQUEST_TO_JOIN_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = new ArrayList<Long>();
            includeList.add(contact.getUserId());
            sendEnterpriseNotification(enterprise.getId(), includeList, null, notifyTextForApplicant, null, null);

            // send notification to all the other members in the group
            code = EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_REQUEST_TO_JOIN_FOR_OPERATOR;
            notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            includeList = getEnterpriseAdminIncludeList(enterprise.getId(), user.getId(), user.getId());
            if(includeList.size() > 0) {
                QuestionMetaObject metaObject = createGroupQuestionMetaObject(enterprise, contact, null);
                metaObject.setRequestInfo(notifyTextForApplicant);

                QuestionMetaActionData actionData = new QuestionMetaActionData();
                actionData.setMetaObject(metaObject);

                String routerUri = RouterBuilder.build(Router.ENTERPRISE_MEMBER_APPLY, actionData);
                sendRouterEnterpriseNotificationUseSystemUser(includeList, null, notifyTextForApplicant, routerUri);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Send waiting approval message to admin contact in enterprise, userId=" + user.getId() 
                        + ", enterpriseId=" + enterprise.getId() + ", adminList=" + includeList);
                }
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No admin contact found in enterprise, userId=" + user.getId() + ", enterpriseId=" + enterprise.getId());
                }
            }
        }
    }

	private void sendMessageForContactApproved(Map<Long, Long> ctx, EnterpriseContact contact) {
		Long check = null;
		if (ctx != null) {
			check = ctx.get(contact.getEnterpriseId());
		}

		if (check == null) {
			if (ctx != null) {
				ctx.put(contact.getEnterpriseId(), 1l);
			}

			Enterprise enterprise = this.enterpriseService.getEnterpriseById(contact.getEnterpriseId());
			User user = userProvider.findUserById(contact.getUserId());

			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseName", enterprise.getName());

			String userName = contact.getNickName();
			if (userName == null || userName.trim().isEmpty()) {
				userName = contact.getName();
				if (null != userName) {
					userName = "";
				}
			}
			map.put("userName", userName);
			String locale = user.getLocale();

			// send notification to who is requesting to join the enterprise
			String scope = EnterpriseNotifyTemplateCode.SCOPE;
			int code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_MYSELF;
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			List<Long> includeList = new ArrayList<Long>();
			includeList.add(contact.getUserId());
			sendEnterpriseNotification(enterprise.getId(), includeList, null, notifyTextForApplicant, null, null);

			// send notification to all the other members in the group
			code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_OTHER;
			notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendEnterpriseNotification(enterprise.getId(), null, includeList, notifyTextForApplicant, null, null);
		}
	}
	
   private void sendMessageForContactReject(Map<Long, Long> ctx, EnterpriseContact contact) {
        Long check = null;
        if (ctx != null) {
            check = ctx.get(contact.getEnterpriseId());
        }

        if (check == null) {
            if (ctx != null) {
                ctx.put(contact.getEnterpriseId(), 1l);
            }

            Enterprise enterprise = this.enterpriseService.getEnterpriseById(contact.getEnterpriseId());
            User user = userProvider.findUserById(contact.getUserId());

            Map<String, String> map = new HashMap<String, String>();
            map.put("enterpriseName", enterprise.getName());

            String userName = contact.getNickName();
            if (userName == null || userName.trim().isEmpty()) {
                userName = contact.getName();
                if (null != userName) {
                    userName = "";
                }
            }
            map.put("userName", userName);
            String locale = user.getLocale();

            // send notification to who is requesting to join the enterprise
            String scope = EnterpriseNotifyTemplateCode.SCOPE;
            int code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_REJECT_JOIN;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = new ArrayList<Long>();
            includeList.add(contact.getUserId());
            sendEnterpriseNotification(enterprise.getId(), includeList, null, notifyTextForApplicant, null, null);

            // send notification to all the other members in the group
            // code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_OTHER;
            // notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            // sendEnterpriseNotification(enterprise.getId(), null, includeList, notifyTextForApplicant, null, null);
        }
   }
   
   private void sendMessageForContactLeave(Map<Long, Long> ctx, EnterpriseContact contact) {
       Long check = null;
       if (ctx != null) {
           check = ctx.get(contact.getEnterpriseId());
       }

       if (check == null) {
           if (ctx != null) {
               ctx.put(contact.getEnterpriseId(), 1l);
           }

           Enterprise enterprise = this.enterpriseService.getEnterpriseById(contact.getEnterpriseId());
           User user = userProvider.findUserById(contact.getUserId());

           Map<String, String> map = new HashMap<String, String>();
           map.put("enterpriseName", enterprise.getName());

           String userName = contact.getNickName();
           if (userName == null || userName.trim().isEmpty()) {
               userName = contact.getName();
               if (null != userName) {
                   userName = "";
               }
           }
           map.put("userName", userName);
           String locale = user.getLocale();

           // send notification to who is requesting to join the enterprise
           String scope = EnterpriseNotifyTemplateCode.SCOPE;
           int code = EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_LEAVE_FOR_APPLICANT;
           String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
           List<Long> includeList = new ArrayList<Long>();
           includeList.add(contact.getUserId());
           sendEnterpriseNotification(enterprise.getId(), includeList, null, notifyTextForApplicant, null, null);

           // send notification to all the other members in the enterprise
           code = EnterpriseNotifyTemplateCode.ENTERPRISE_CONTACT_LEAVE_FOR_OTHER;
           notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
           sendEnterpriseNotification(enterprise.getId(), null, includeList, notifyTextForApplicant, null, null);
       }
   }

	private void sendEnterpriseNotification(Long enterpriseId,
			List<Long> includeList, List<Long> excludeList, String message,
			MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
		if (message != null && message.length() != 0) {
			String channelType = MessageChannelType.GROUP.getCode();
			String channelToken = String.valueOf(enterpriseId);
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto
					.setChannels(new MessageChannel(channelType, channelToken));
			messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
			messageDto.setBody(message);
			messageDto.setMetaAppId(AppConstants.APPID_ENTERPRISE);
			if (includeList != null && includeList.size() > 0) {
				messageDto.getMeta().put(MessageMetaConstant.INCLUDE,
						StringHelper.toJsonString(includeList));
			}
			if (excludeList != null && excludeList.size() > 0) {
				messageDto.getMeta().put(MessageMetaConstant.EXCLUDE,
						StringHelper.toJsonString(excludeList));
			}
			if (metaObjectType != null && metaObject != null) {
				messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE,
						metaObjectType.getCode());
				messageDto.getMeta().put(MessageMetaConstant.META_OBJECT,
						StringHelper.toJsonString(metaObject));
			}
			messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
					AppConstants.APPID_MESSAGING, channelType, channelToken,
					messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
		}
	}

	private void sendRouterEnterpriseNotificationUseSystemUser(List<Long> includeList, List<Long> excludeList, String message, String routerUri) {
        if(message == null || message.isEmpty()) {
            // return;
        }

        if(includeList != null && includeList.size() > 0) {
            if (excludeList != null && excludeList.size() > 0) {
                includeList = includeList.stream().filter(r -> !excludeList.contains(r)).collect(Collectors.toList());
            }

            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_GROUP);

            RouterMetaObject mo = new RouterMetaObject();
            mo.setUrl(routerUri);
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
            messageDto.setMeta(meta);

            includeList.stream().distinct().forEach(targetId -> {
                messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                        AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                        messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
            });
        }
	}

	private void sendUserNotification(Long userId, String message) {
		if (message != null && message.length() != 0) {
			String channelType = MessageChannelType.USER.getCode();
			String channelToken = String.valueOf(userId);
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto
					.setChannels(new MessageChannel(channelType, channelToken));
			messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
			messageDto.setBody(message);
			messageDto.setMetaAppId(AppConstants.APPID_ENTERPRISE);
			messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
					AppConstants.APPID_MESSAGING, channelType, channelToken,
					messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
		}
	}

	@Override
	public List<EnterpriseContactDetail> listContactsRequestByEnterpriseId(
			ListingLocator locator, Long enterpriseId, Integer pageSize,String keyWord) {

		if (locator.getAnchor() == null)
			locator.setAnchor(0L);
		int count = PaginationConfigHelper
				.getPageSize(configProvider, pageSize);
		List<EnterpriseContact> contacts = this.enterpriseContactProvider
				.listContactRequestByEnterpriseId(locator, enterpriseId, count,keyWord);

		Long nextPageAnchor = null;
		if (contacts != null && contacts.size() > count) {
			contacts.remove(contacts.size() - 1);
			nextPageAnchor = contacts.get(contacts.size() - 1).getId();
		}
		locator.setAnchor(nextPageAnchor);

		List<EnterpriseContactDetail> details = new ArrayList<EnterpriseContactDetail>();
		for (EnterpriseContact contact : contacts) {
			EnterpriseContactDetail detail = ConvertHelper.convert(contact,
					EnterpriseContactDetail.class);
			EnterpriseContactGroupMember member = this.enterpriseContactProvider
					.getContactGroupMemberByContactId(enterpriseId,
							contact.getId());
			if (member != null) {
				EnterpriseContactGroup group = this.enterpriseContactProvider
						.getContactGroupById(member.getContactGroupId());
				if (group != null) {
					detail.setGroupName(group.getName());
				}
			}

			List<EnterpriseContactEntry> entries = this.enterpriseContactProvider
					.queryContactEntryByContactId(contact);
			if (entries != null && entries.size() > 0) {
				detail.setPhone(entries.get(0).getEntryValue());
			}

			details.add(detail);

		}

		return details;
	}

	
    private Enterprise checkEnterpriseParameter(Long enterpriseId, Long operatorUid, String tag) {
        if(enterpriseId == null) {
            LOGGER.error("Enterprise id is null, operatorUid=" + operatorUid + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Enterprise id can not be null");
        }
        
        Enterprise enterprise = this.enterpriseProvider.findEnterpriseById(enterpriseId);
        if(enterprise == null) {
            LOGGER.error("Enterprise not found, operatorUid=" + operatorUid + ", enterpriseId=" + enterpriseId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_NOT_FOUND, 
                    "Unable to find the enterprise");
        }
        
        return enterprise;
    }
	
    
    
    private EnterpriseContact checkEnterpriseContactParameter(Long contactId, Long operatorUid, String tag) {
        if(contactId == null) {
            LOGGER.error("Enterprise contact id is null, operatorUid=" + operatorUid + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Enterprise contact id can not be null");
        }
        
        EnterpriseContact contact = this.enterpriseContactProvider.queryContactById(contactId);
        if(contact == null) {
            LOGGER.error("Enterprise contact not found, operatorUid=" + operatorUid + ", contactId=" + contactId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_CONTACT_NOT_FOUND, 
                    "Unable to find the enterprise contact");
        }
        
        return contact;
    }    
    
    private OrganizationMember checkEnterpriseContactParameter(Long enterpriseId, Long targetId, Long operatorUid, String tag) {
        if(targetId == null) {
            LOGGER.error("Enterprise contact target user id is null, operatorUid=" + operatorUid 
                + ", enterpriseId=" + enterpriseId + ", targetId=" + targetId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Enterprise contact target user id can not be null");
        }
        
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(targetId, enterpriseId);
        if(member == null) {
            LOGGER.error("Enterprise contact not found, operatorUid=" + operatorUid 
                + ", enterpriseId=" + enterpriseId + ", targetId=" + targetId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_CONTACT_NOT_FOUND, 
                    "Unable to find the enterprise contact");
        }
        
        return member;
    }

	private String setExecCommand(String jarPath, Long orgId, String filePath1,
			String filePath2) {
		StringBuilder builder = new StringBuilder();
		// builder.append("java -jar D:/dev_documents/workspace2/ehparser/target/ehparser-0.0.1-SNAPSHOT.jar");
		builder.append("java -jar" + " " + jarPath);
		builder.append(" ");
		builder.append(orgId);
		builder.append(" ");
		builder.append(filePath1);
		builder.append(" ");
		builder.append(filePath2);
		return builder.toString();
	}

	private String getJarPath() {
		String rootPath = System.getProperty("user.dir");
		String jarPath = rootPath + File.separator
				+ "ehparser-0.0.1-SNAPSHOT.jar";

		File dir = new File(rootPath);
		if (dir.isDirectory()) {
			File[] fileList = dir.listFiles();
			if (fileList != null && fileList.length > 0) {
				for (File file : fileList) {
					String name = file.getName();
					if (LOGGER.isDebugEnabled())
						LOGGER.error("jarFileName=" + name);

					if (name.startsWith("ehparser") && name.endsWith(".jar")) {
						jarPath = rootPath + File.separator + file.getName();
						break;
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.error("jarPath=" + jarPath);
		return jarPath;
	}

	private void storeFile(MultipartFile file, String filePath1)
			throws Exception {
		OutputStream out = new FileOutputStream(new File(filePath1));
		InputStream in = file.getInputStream();
		byte[] buffer = new byte[1024];
		while (in.read(buffer) != -1) {
			out.write(buffer);
		}
		out.close();
		in.close();
	}

	@Override
	public void importContacts(importContactsCommand cmd, MultipartFile[] files) {
		this.importContacts(cmd.getEnterpriseId(), files);
	}

	private void importContacts(Long enterpriseId, MultipartFile[] files) {
		convertContactExcelFile(enterpriseId, files);
	}

	private void convertContactExcelFile(Long enterpriseId,
			MultipartFile[] files) {
        User operator = UserContext.current().getUser();
        Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
        if(operator != null) {
            namespaceId = operator.getNamespaceId();
        }
        
		List<EnterpriseContact> contacts = new ArrayList<EnterpriseContact>();
		Map<String, Long> groupMap = new HashMap<String, Long>();
		initContactGroupMap(enterpriseId, groupMap);
		try {
			ArrayList resultList = new ArrayList();
			try {
				resultList = PropMrgOwnerHandler.processorExcel(files[0]
						.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (resultList != null && resultList.size() > 0) {
				int row = resultList.size();
				if (resultList != null && resultList.size() > 0) {
					for (int rowIndex = 1; rowIndex < row; rowIndex++) {
						RowResult result = (RowResult) resultList.get(rowIndex);
						String employeeNo    = RowResult.trimString(result.getA());
						String applyGroup    = RowResult.trimString(result.getB());
						String name          = RowResult.trimString(result.getC());
						String sex           = RowResult.trimString(result.getD());
						String PhoneNum      = RowResult.trimString(result.getE());
						String building      = RowResult.trimString(result.getF());
						String apartment     = RowResult.trimString(result.getG());  
						// 如果已有该号码在该单位的通讯录，则删除EnterpriseContactEntry
						// EnterpriseContactGroupMember EnterpriseContact
						// 三个表的该contact记录
						List<EnterpriseContactEntry> oldContactEntrys = enterpriseContactProvider
								.queryContactEntryByEnterpriseIdAndPhone(null,
										enterpriseId, PhoneNum, null);
						if (oldContactEntrys.size() > 1)
							throw RuntimeErrorException
									.errorWith(ErrorCodes.SCOPE_GENERAL,
											ErrorCodes.ERROR_GENERAL_EXCEPTION,
											"more than one record for a phone num !!! ");
						if (oldContactEntrys.size() == 1) {
							List<EnterpriseContactGroupMember> enterpriseContactGroupMembers = enterpriseContactProvider
									.queryContactGroupMemberByEnterpriseIdAndContactId(
											null, enterpriseId,
											oldContactEntrys.get(0)
													.getContactId(), null);
							for (EnterpriseContactGroupMember ecgm : enterpriseContactGroupMembers) {
								enterpriseContactProvider
										.deleteContactGroupMember(ecgm);
							}
							enterpriseContactProvider
									.deleteContactById(enterpriseContactProvider
											.queryContactById(oldContactEntrys
													.get(0).getContactId()));
							enterpriseContactProvider
									.deleteContactEntry(oldContactEntrys.get(0));
						}
						// TODO: Role =
						EnterpriseContact contact = new EnterpriseContact();
						contact.setEnterpriseId(enterpriseId);
						contact.setEmployeeNo(employeeNo);
						contact.setApplyGroup(applyGroup);
						contact.setName(name);
						contact.setSex(sex);
						contact.setCreatorUid(operator.getId());
						contact.setStatus(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
						contact.setCreateTime(new Timestamp(System
								.currentTimeMillis()));
						Long contactId = enterpriseContactProvider
								.createContact(contact);
						// phone find user
						User user = userService.findUserByIndentifier(namespaceId, PhoneNum);
						if (null != user){
							//已有用户，设置为正常状态，并把userId放入contact表 复用大师的代码
							contact.setUserId(user.getId());
							contact.setStatus(GroupMemberStatus.ACTIVE.getCode());  
		    				updatePendingEnterpriseContactToAuthenticated(contact);
						}
						// TODO: map aparment 2 user 
					
						EnterpriseContactEntry contactEntry = new EnterpriseContactEntry();
						contactEntry.setContactId(contactId);
						contactEntry.setEnterpriseId(enterpriseId);
						contactEntry
								.setEntryType(EnterpriseContactEntryType.Mobile
										.getCode());
						contactEntry.setEntryValue(PhoneNum);
						contactEntry.setCreatorUid(operator.getId());
						contactEntry.setCreateTime(new Timestamp(System
								.currentTimeMillis()));
						enterpriseContactProvider
								.createContactEntry(contactEntry);
						// EnterpriseGroupMemberStatus
						if(null!= applyGroup){
							if (applyGroup.contains("\\")) {
								String[] groups = applyGroup.split("\\\\");
								StringBuffer groupPath = new StringBuffer();
								groupPath.append("\\");
								for (int groupNode = 0; groupNode < groups.length; groupNode++) {
									
									StringBuffer groupNamePath = new StringBuffer();
									for (int groupSubNode = 0; groupSubNode <= groupNode; groupSubNode++) {
										if(groupSubNode>0)
											groupNamePath.append("\\");
										groupNamePath.append(groups[groupSubNode]);
									}
									if (null == groupMap.get(groupNamePath
											.toString())) {
										// 如果不存在则添加新group
										EnterpriseContactGroup enterpriseContactGroup = new EnterpriseContactGroup();
										enterpriseContactGroup
												.setEnterpriseId(enterpriseId);
										enterpriseContactGroup
												.setName(groups[groupNode]);
										if (groupNode == 0) {
	
										} else {
											enterpriseContactGroup
													.setParentId(groupMap
															.get(groups[groupNode - 1]));
	
										}
										enterpriseContactGroup.setPath(groupPath
												.toString());
										enterpriseContactGroup.setApplyGroup(groupNamePath.toString());
										enterpriseContactGroup
												.setCreatorUid(operator.getId());
										enterpriseContactGroup
												.setCreateTime(new Timestamp(System
														.currentTimeMillis()));
										enterpriseContactProvider
												.createContactGroup(enterpriseContactGroup);
										groupMap.put(groupNamePath.toString(),
												enterpriseContactGroup.getId());
										groupPath.append(enterpriseContactGroup
												.getId()); 
										groupPath.append("\\");
	
									}
	
								}
							} else {
								if (null == groupMap.get(applyGroup)) {
									// 如果不存在则添加新group
									EnterpriseContactGroup enterpriseContactGroup = new EnterpriseContactGroup();
									enterpriseContactGroup
											.setEnterpriseId(enterpriseId);
									enterpriseContactGroup.setName(applyGroup);
									enterpriseContactGroup.setApplyGroup(applyGroup);
									enterpriseContactGroup.setPath("\\");
									enterpriseContactGroup.setCreatorUid(operator.getId());
									enterpriseContactGroup.setCreateTime(new Timestamp(System
													.currentTimeMillis()));
									enterpriseContactProvider
											.createContactGroup(enterpriseContactGroup);
									groupMap.put(applyGroup,
											enterpriseContactGroup.getId());
								}
							}
							// 添加menber表
							EnterpriseContactGroupMember enterpriseContactGroupMember = new EnterpriseContactGroupMember();
							enterpriseContactGroupMember.setContactGroupId(groupMap
									.get(applyGroup));
							enterpriseContactGroupMember
									.setEnterpriseId(enterpriseId);
							enterpriseContactGroupMember.setContactId(contactId);
							enterpriseContactGroupMember.setCreatorUid(operator.getId());
							enterpriseContactGroupMember
									.setCreateTime(new Timestamp(System
											.currentTimeMillis()));
							enterpriseContactProvider
									.createContactGroupMember(enterpriseContactGroupMember);
						}
					}
				}
			} else {
				LOGGER.error("excel data format is not correct.rowCount="
						+ resultList.size());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"excel data format is not correct");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initContactGroupMap(Long enterpriseId,
			Map<String, Long> groupMap) {
		List<EnterpriseContactGroup> enterpriseContactGroups = enterpriseContactProvider
				.queryContactGroupByEnterpriseId(null, enterpriseId, Integer.MAX_VALUE, null);
		for (EnterpriseContactGroup enterpriseContactGroup : enterpriseContactGroups) {
			groupMap.put(enterpriseContactGroup.getApplyGroup(),
					enterpriseContactGroup.getId());
		}
	}

	@Override
	public ListContactGroupsByEnterpriseIdCommandResponse listContactGroupsByEnterpriseId(
			ListContactGroupsByEnterpriseIdCommand cmd) {
		 ListContactGroupsByEnterpriseIdCommandResponse response = new ListContactGroupsByEnterpriseIdCommandResponse();
		 
		 ListingLocator locator = new ListingLocator();
         locator.setAnchor(cmd.getPageAnchor());
         List<EnterpriseContactGroup> details = this.listContactGroupsByEnterpriseId(locator, cmd.getEnterpriseId(), cmd.getPageSize());
         List<EnterpriseContactGroupDTO> dtos = new ArrayList<EnterpriseContactGroupDTO>();
         for(EnterpriseContactGroup detail : details) {
             EnterpriseContactGroupDTO dto =  ConvertHelper.convert(detail, EnterpriseContactGroupDTO.class);
             if(null== detail.getParentId() || detail.getParentId() == 0){
            	//has no parent id
             }else {

                 EnterpriseContactGroup parentGroup = this.enterpriseContactProvider.queryContactGroupById(detail.getParentId());
                 dto.setParentGroupName(parentGroup.getName());
			}
             dtos.add(dto);
         }
         
         
         response.setContactGroups(dtos);
         response.setNextPageAnchor(locator.getAnchor());
		return response;
	}

	public List<EnterpriseContactGroup> listContactGroupsByEnterpriseId(
			ListingLocator locator, Long enterpriseId, Integer pageSize) {

		if (locator.getAnchor() == null)
			locator.setAnchor(0L);
		int count = PaginationConfigHelper
				.getPageSize(configProvider, pageSize);
		List<EnterpriseContactGroup> contactGroups = this.enterpriseContactProvider
				.listContactGroupsByEnterpriseId(locator, enterpriseId, count);

		Long nextPageAnchor = null;
		if (contactGroups != null && contactGroups.size() > count) {
			contactGroups.remove(contactGroups.size() - 1);
			nextPageAnchor = contactGroups.get(contactGroups.size() - 1).getId();
		}
		locator.setAnchor(nextPageAnchor);
 
		return contactGroups;
	}

	@Override
	public ListContactGroupNamesByEnterpriseIdCommandResponse listContactGroupNamesByEnterpriseId(
			ListContactGroupNamesByEnterpriseIdCommand cmd) { 
		ListContactGroupNamesByEnterpriseIdCommandResponse response = new ListContactGroupNamesByEnterpriseIdCommandResponse();
		  
        
		 List<EnterpriseContactGroup> contactGroups = this.enterpriseContactProvider
					.listContactGroupsByEnterpriseId(null, cmd.getEnterpriseId(), Integer.MAX_VALUE);
		 
        List<EnterpriseContactGroupDTO> dtos = new ArrayList<EnterpriseContactGroupDTO>();
        for(EnterpriseContactGroup detail : contactGroups) {
            EnterpriseContactGroupDTO dto =  ConvertHelper.convert(detail, EnterpriseContactGroupDTO.class);
           
            dtos.add(dto);
        }
        
        
        response.setContactGroups(dtos);
        
		return response;
	}

	@Override
	public void addContactGroup(AddContactGroupCommand cmd) { 
		EnterpriseContactGroup group = new EnterpriseContactGroup() ;
		group.setName(cmd.getGroupName());
		group.setEnterpriseId(cmd.getEnterpriseId());

		group.setCreatorUid(UserContext.current().getUser().getId());
		group.setCreateTime(new Timestamp(System
				.currentTimeMillis()));
		if(null!= cmd.getParentId()){
			 EnterpriseContactGroup parentGroup = this.enterpriseContactProvider.queryContactGroupById(cmd.getParentId());
			 group.setApplyGroup(parentGroup.getApplyGroup()+"\\"+cmd.getGroupName());
			 group.setPath(parentGroup.getPath()+parentGroup.getId()+"\\");
			 group.setParentId(parentGroup.getId());
		}else {
			 group.setApplyGroup(cmd.getGroupName());
			 group.setPath("\\");
		}
		this.enterpriseContactProvider.createContactGroup(group);
	}

	@Override
	public void deleteContactGroupById(DeleteContactGroupByIdCommand cmd) { 
		List<EnterpriseContactGroup> results = this.enterpriseContactProvider.queryContactGroupByPath(cmd.getEnterpriseId(),cmd.getGroupId());
		if(null != results && results.size() >= 1){
			throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_CONTACT_GROUP_HAS_CHILD, 
                    "ERROR : cannot delete group which has children group !!!");
		}
		else{
			EnterpriseContactGroup group = this.enterpriseContactProvider.getContactGroupById(cmd.getGroupId());
			this.enterpriseContactProvider.deleteContactGroup(group);
			
		}
	}

	@Override
	public void addContact(AddContactCommand cmd) { 
	    User operator = UserContext.current().getUser();
	    
		List<EnterpriseContactEntry> oldContactEntrys = enterpriseContactProvider
				.queryContactEntryByEnterpriseIdAndPhone(null,
						cmd.getEnterpriseId(), cmd.getPhone(), null);
		if (oldContactEntrys.size() >= 1)
			throw RuntimeErrorException
					.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_CONTACT_PHONENUM_USED,
							"already used this phone num !!! ");
		EnterpriseContactGroup contactGroup= this.enterpriseContactProvider.getContactGroupById(cmd.getGroupId());
		EnterpriseContact contact = new EnterpriseContact();
		contact.setEnterpriseId(cmd.getEnterpriseId());
		if(null!=cmd.getEmployeeNo())
			contact.setEmployeeNo(cmd.getEmployeeNo());
		contact.setApplyGroup(contactGroup.getApplyGroup());
		contact.setName(cmd.getName());
		if(null!=cmd.getSex())
			contact.setSex(cmd.getSex());
		contact.setCreatorUid(UserContext.current().getUser().getId());
		contact.setStatus(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
		contact.setCreateTime(new Timestamp(System
				.currentTimeMillis()));
		// phone find user
		User user = userService.findUserByIndentifier(operator.getNamespaceId(), cmd.getPhone());
		if (null != user){
			contact.setUserId(user.getId());
			contact.setStatus(GroupMemberStatus.ACTIVE.getCode());
			Group group = groupProvider.findGroupById(cmd.getEnterpriseId());
			UserGroup uGroup =new UserGroup();
			uGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
			uGroup.setOwnerUid(user.getId());
			uGroup.setGroupId(group.getId());
			uGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
			uGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
			uGroup.setRegionScopeId(group.getVisibleRegionId());
			userProvider.createUserGroup(uGroup);
		}
		// TODO: map aparment 2 user
		Long contactId = enterpriseContactProvider
				.createContact(contact);
		EnterpriseContactEntry contactEntry = new EnterpriseContactEntry();
		contactEntry.setContactId(contactId);
		contactEntry.setEnterpriseId(cmd.getEnterpriseId());
		contactEntry
				.setEntryType(EnterpriseContactEntryType.Mobile
						.getCode());
		contactEntry.setEntryValue(cmd.getPhone());
		contactEntry.setCreatorUid(UserContext.current().getUser().getId());
		contactEntry.setCreateTime(new Timestamp(System
				.currentTimeMillis()));
		enterpriseContactProvider
				.createContactEntry(contactEntry);
		 
		// 添加menber表
		if(null!=cmd.getGroupId()){
			EnterpriseContactGroupMember enterpriseContactGroupMember = new EnterpriseContactGroupMember();
			enterpriseContactGroupMember.setContactGroupId(cmd.getGroupId());
			enterpriseContactGroupMember
					.setEnterpriseId(cmd.getEnterpriseId());
			enterpriseContactGroupMember.setContactId(contactId);
			enterpriseContactGroupMember.setCreatorUid(UserContext.current().getUser().getId());
			enterpriseContactGroupMember
					.setCreateTime(new Timestamp(System
							.currentTimeMillis()));
			enterpriseContactProvider
					.createContactGroupMember(enterpriseContactGroupMember);
		}
	}

	@Override
	public void deleteContactById(DeleteContactByIdCommand cmd) { 
		EnterpriseContact contact = this.enterpriseContactProvider.getContactById(cmd.getContactId());
		EnterpriseContactGroupMember enterpriseContactGroupMember = this.enterpriseContactProvider.getContactGroupMemberByContactId(cmd.getEnterpriseId(), cmd.getContactId());
		List<EnterpriseContactEntry> contactEntrys = this.enterpriseContactProvider.queryContactEntryByContactId(contact);
		
		UserGroup uGroup= this.userProvider.findUserGroupByOwnerAndGroup(contact.getUserId(), cmd.getEnterpriseId());
		if (null != uGroup)
				this.userProvider.deleteUserGroup(uGroup);
		if(null !=contactEntrys &&contactEntrys.size()>0){
			for(EnterpriseContactEntry entry : contactEntrys){
				this.enterpriseContactProvider.deleteContactEntry(entry);
			}
		}
		if (null!= enterpriseContactGroupMember)
			this.enterpriseContactProvider.deleteContactGroupMember(enterpriseContactGroupMember);
		
		this.enterpriseContactProvider.deleteContactById(contact);
		
		GroupMemberStatus status = GroupMemberStatus.fromCode(contact.getStatus());
		if(0 != contact.getUserId() && null != contact.getUserId()){
			 if(status == GroupMemberStatus.ACTIVE) {
			      contact.setStatus(GroupMemberStatus.INACTIVE.getCode());
			      deleteActiveEnterpriseContact(contact.getUserId(), contact, true, "");
			 } else {
			      deletePendingEnterpriseContact(contact.getUserId(), contact, true);
			 }
		}
	   
	}

	@Override
	public EnterpriseContactDTO updateContact(UpdateContactCommand cmd) {
		EnterpriseContact contact = this.enterpriseContactProvider.getContactById(cmd.getContactId());
		contact.setName(cmd.getName());
		contact.setNickName(cmd.getNickName());
		contact.setRole(cmd.getRole());
		contact.setSex(cmd.getSex());
		contact.setEmployeeNo(cmd.getEmployeeNo());
		//phone num changeEnergyMeter
		EnterpriseContactGroupMember enterpriseContactGroupMember = this.enterpriseContactProvider.getContactGroupMemberByContactId(contact.getEnterpriseId(), cmd.getContactId());
		if(null!=cmd.getContactGroupId()){
			if(null == enterpriseContactGroupMember){
				if(null != cmd.getContactGroupId()){
					enterpriseContactGroupMember = new EnterpriseContactGroupMember();
					enterpriseContactGroupMember.setContactGroupId(cmd.getContactGroupId());
					enterpriseContactGroupMember
							.setEnterpriseId(contact.getEnterpriseId());
					enterpriseContactGroupMember.setContactId(contact.getId());
					enterpriseContactGroupMember.setCreatorUid(UserContext.current().getUser().getId());
					enterpriseContactGroupMember
							.setCreateTime(new Timestamp(System
									.currentTimeMillis()));
					enterpriseContactProvider
							.createContactGroupMember(enterpriseContactGroupMember);
				}
			}else if(!enterpriseContactGroupMember.getContactGroupId().equals(cmd.getContactGroupId())){
				enterpriseContactGroupMember.setContactGroupId(cmd.getContactGroupId());
				contact.setApplyGroup(this.enterpriseContactProvider.getContactGroupById(cmd.getContactGroupId()).getApplyGroup());
	
				enterpriseContactProvider.updateContactGroupMember(enterpriseContactGroupMember);
			}
		}
		this.enterpriseContactProvider.updateContact(contact);
		return ConvertHelper.convert(contact, EnterpriseContactDTO.class);
	}

	@Override
	public EnterpriseContactDTO getUserEnterpriseContact(
			GetUserEnterpriseContactCommand cmd) { 
		EnterpriseContact  contact = this.enterpriseContactProvider.queryContactByUserId(cmd.getEnterpriseId(),   UserContext.current().getUser().getId());
		if(null == contact)
			throw RuntimeErrorException.errorWith(EnterpriseServiceErrorCode.SCOPE, EnterpriseServiceErrorCode.ERROR_ENTERPRISE_CONTACT_NOT_FOUND, 
                    "can not find enterprise contact !!!");
		EnterpriseContactDTO  dto = ConvertHelper.convert(contact, EnterpriseContactDTO.class);
		EnterpriseContactGroupMember member = this.enterpriseContactProvider
				.getContactGroupMemberByContactId(contact.getEnterpriseId(),
						contact.getId());
		if (member != null) {
			EnterpriseContactGroup group = this.enterpriseContactProvider
					.getContactGroupById(member.getContactGroupId());
			if (group != null) {
				dto.setGroupName(group.getName());
			}
		}

		List<EnterpriseContactEntry> entries = this.enterpriseContactProvider
				.queryContactEntryByContactId(contact);
		if (entries != null && entries.size() > 0) {
			dto.setPhone(entries.get(0).getEntryValue());
		}
		
		return dto;
	}
	
	@Override
	public void syncEnterpriseContacts() {
	    long startTime = System.currentTimeMillis();
	    int pageSize = 1000;
        AtomicInteger round = new AtomicInteger(0);
	    AtomicInteger insertCount = new AtomicInteger(0);
	    AtomicInteger dupCount = new AtomicInteger(0);
	    this.enterpriseContactProvider.iterateEnterpriseContacts(pageSize, (locator, query) -> {
	        query.addConditions(Tables.EH_ENTERPRISE_CONTACTS.USER_ID.gt(0L));
	        round.incrementAndGet();
	        return null;
	    }, (contact) -> {
	        Long userId = contact.getUserId();
	        Long enterpriseId = contact.getEnterpriseId();
	        UserGroup userGroup = this.userProvider.findUserGroupByOwnerAndGroup(userId, enterpriseId);
	        if(userGroup == null) {
	            try {
	                createUserGroup(contact);
	                insertCount.incrementAndGet();
	            } catch (DuplicateKeyException e) {
	                // Skip the duplicated records
	                dupCount.incrementAndGet();
	            } catch (Exception e) {
	                LOGGER.error("Failed to sync the enterprise contacts, contact=" + contact, e);
	            }
	        }
	    });
	    
	    if(LOGGER.isDebugEnabled()) {
	        long endTime = System.currentTimeMillis();
	        LOGGER.debug("Sync the enterprise contacts, round=" + round.intValue() + ", insertCount=" + insertCount.intValue() 
	            + ", dupCount=" + dupCount.intValue() + ", elapse=" + (endTime - startTime));
	    }
	}
	
	
	
	
	
	
	/*****************************  新调整的接口   *****************************************************/
	
	
	/**
	 * 申请加入企业
	 * 
	 * @param
	 */
	@Override
	public EnterpriseContactDTO applyForEnterpriseContact(CreateOrganizationMemberCommand cmd) {
		User user = UserContext.current().getUser();
		if(StringUtils.isEmpty(cmd.getTargetId())){
			cmd.setTargetId(user.getId());
		}
		// Check exists
		OrganizationMember organizationmember = organizationProvider.findOrganizationMemberByUIdAndOrgId(cmd.getTargetId(), cmd.getOrganizationId());
		if (null != organizationmember) {
			// Should response error hear
			// contact.setId(existContact.getId());
			EnterpriseContact contact = new EnterpriseContact();
			contact.setEnterpriseId(organizationmember.getOrganizationId());
			contact.setUserId(organizationmember.getTargetId());
			contact.setStatus(organizationmember.getStatus());
			
			contact.setCreatorUid(user.getId());
			contact.setName(organizationmember.getContactName());
			contact.setNickName(user.getNickName());
			contact.setAvatar(user.getAvatar());
			return ConvertHelper.convert(contact, EnterpriseContactDTO.class);
		}
		
		EnterpriseContact result = this.dbProvider.execute((TransactionStatus status) -> {
			UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(cmd.getTargetId(),
					IdentifierType.MOBILE.getCode());
			
			OrganizationMember member = new OrganizationMember();
			member.setContactToken(identifier.getIdentifierToken());
			member.setContactType(identifier.getIdentifierType());
			member.setContactName(StringUtils.isEmpty(cmd.getContactName()) ? user.getNickName() : cmd.getContactName());
			member.setOrganizationId(cmd.getOrganizationId());
			member.setTargetType(OrganizationMemberTargetType.USER.getCode());
			member.setTargetId(cmd.getTargetId());
			member.setStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
			
			organizationProvider.createOrganizationMember(member);
			
			EnterpriseContact contact = new EnterpriseContact();
			contact.setEnterpriseId(member.getOrganizationId());
			contact.setUserId(member.getTargetId());
			contact.setStatus(member.getStatus());
			
			contact.setCreatorUid(user.getId());
			contact.setName(member.getContactName());
			contact.setNickName(user.getNickName());
			contact.setAvatar(user.getAvatar());

			// Create it
			createOrUpdateUserGroup(contact);

			return contact;
		});
		
		if (result == null) {
			LOGGER.error("Failed to apply for enterprise contact, userId="
					+ cmd.getTargetId() + ", cmd=" + cmd);
			return null;
		} else {
	        Map<Long, Long> ctx = new HashMap<Long, Long>();
		    sendMessageForContactRequestToJoin(ctx, result);
			return ConvertHelper.convert(result, EnterpriseContactDTO.class);
		}
	}
	
	/**
	 * 批准用户加入企业
	 */
	@Override
	public void approveForEnterpriseContact(OrganizationMember member) {
		if (member.getStatus().equals(GroupMemberStatus.ACTIVE.getCode())) {
		    LOGGER.info("The contact is already authenticated in enterprise");
			return;
		}
		User user = UserContext.current().getUser();
		member.setStatus(GroupMemberStatus.ACTIVE.getCode());
		EnterpriseContact contact = new EnterpriseContact();
		contact.setEnterpriseId(member.getOrganizationId());
		contact.setUserId(member.getTargetId());
		contact.setStatus(member.getStatus());
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                this.organizationProvider.updateOrganizationMember(member);
                createOrUpdateUserGroup(contact);
                return null;
            });
            return null;
        });

		sendMessageForEnterpriseContactReject(user.getId(), member);
	}
	
	/**
	 * 申请
	 */
	@Override
	public void approveContact(ApproveContactCommand cmd) {

        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        OrganizationMember member = checkEnterpriseContactParameter(cmd.getEnterpriseId(), cmd.getUserId(), operatorUid, "rejectContact");
	 
		// 添加menber表 
		this.approveForEnterpriseContact(member);
	}

	/**
	 * 拒绝申请
	 */
	@Override
	public void rejectContact(RejectContactCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        OrganizationMember member = checkEnterpriseContactParameter(cmd.getEnterpriseId(), cmd.getUserId(), operatorUid, "rejectContact");
		
        deleteEnterpriseContactStatus(operatorUid, member);
		
        sendMessageForEnterpriseContactReject(operatorUid, member);
	}
	
	/**
	 * 退出企业
	 */
	@Override
    public void leaveEnterprise(LeaveEnterpriseCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        String tag = "leaveGroup";
        
        Long enterpriseId = cmd.getEnterpriseId();
        checkEnterpriseParameter(enterpriseId, userId, tag);

        OrganizationMember member = checkEnterpriseContactParameter(cmd.getEnterpriseId(), userId, userId, "rejectContact");
       
        updateEnterpriseContactStatus(userId, member);
        
        sendMessageForEnterpriseContactReject(userId, member);
    }
	
	@Override
	public ListOrganizationMemberCommandResponse listOrganizationPersonnels(ListOrganizationContactCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
		Organization org = this.checkOrganization(cmd.getOrganizationId());
		if(null == org)
			return response;
		
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		
		Organization orgCommoand = new Organization();
		orgCommoand.setId(org.getId());
		orgCommoand.setStatus(GroupMemberStatus.ACTIVE.getCode());
		orgCommoand.setGroupType(org.getGroupType());
		
		List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(cmd.getNamespaceId(),
				cmd.getKeywords(),orgCommoand, null,null, locator, pageSize);
		
		if(0 == organizationMembers.size()){
			return response;
		}
		
		response.setNextPageAnchor(locator.getAnchor());

		Organization enterPrise = this.checkOrganization(cmd.getEnterpriseId());
		response.setMembers(this.convertDTO(organizationMembers, enterPrise));
		
		return response;
	}
	
	@Override
	public ListOrganizationMemberCommandResponse listOrgAuthPersonnels(ListOrganizationContactCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
		Organization org = this.checkOrganization(cmd.getOrganizationId());
		if(null == org)
			return response;
		
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		
		Organization orgCommoand = new Organization();
		orgCommoand.setId(org.getId());
		orgCommoand.setStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
		orgCommoand.setGroupType(org.getGroupType());
		
		List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(cmd.getNamespaceId(),
				cmd.getKeywords(), orgCommoand, null,null, locator, pageSize);
		
		if(0 == organizationMembers.size()){
			return response;
		}
		
		response.setNextPageAnchor(locator.getAnchor());
		
		response.setMembers(organizationMembers.stream().map((c) ->{
			return ConvertHelper.convert(c, OrganizationMemberDTO.class);
		}).collect(Collectors.toList()));
		
		return response;
	}
	
//	@Override
//	public ListOrganizationMemberCommandResponse ListParentOrganizationPersonnels(
//			ListOrganizationMemberCommand cmd) {
//		ListOrganizationMemberCommandResponse response = new ListOrganizationMemberCommandResponse();
//		Organization org = this.checkOrganization(cmd.getOrganizationId());
//		if(null == org)
//			return response;
//
//		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//
//		CrossShardListingLocator locator = new CrossShardListingLocator();
//		locator.setAnchor(cmd.getPageAnchor());
//		List<OrganizationMember> organizationMembers = this.organizationProvider.listParentOrganizationMembers(org.getPath(), cmd.getGroupTypes(), locator, pageSize);
//		response.setNextPageAnchor(locator.getAnchor());
//
//		response.setMembers(this.convertDTO(organizationMembers, org));
//
//		return response;
//	}
	
	@Override
	public VerifyPersonnelByPhoneCommandResponse verifyPersonnelByPhone(VerifyPersonnelByPhoneCommand cmd){
		
		VerifyPersonnelByPhoneCommandResponse res = new VerifyPersonnelByPhoneCommandResponse();
		
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		
		OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(cmd.getEnterpriseId(), cmd.getPhone(),cmd.getNamespaceId());
		
		if(member != null){
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_PHONE_ALREADY_EXIST,
					"phone number already exists.");
		}
		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getPhone());
		
		if(null != userIdentifier){
			User user = userProvider.findUserById(userIdentifier.getId());
			OrganizationMemberDTO dto = new OrganizationMemberDTO();
			dto.setTargetId(user.getId());
			dto.setContactToken(userIdentifier.getIdentifierToken());
			dto.setContactName(user.getNickName());
			dto.setTargetType(OrganizationMemberTargetType.USER.getCode());
			res.setDto(dto);
		}
		return res;
	}
	
	@Override
	public void updateOrganizationPersonnel(UpdateOrganizationMemberCommand cmd) {
		OrganizationMember member = organizationProvider.findOrganizationMemberById(cmd.getId());
		member.setContactName(cmd.getContactName());
		organizationProvider.updateOrganizationMember(member);
	}

	@Override
	public OrganizationMemberDTO createOrganizationPersonnel(
			CreateOrganizationMemberCommand cmd) {
		Organization org = checkOrganization(cmd.getOrganizationId());
		
		OrganizationMember organizationMember = ConvertHelper.convert(cmd, OrganizationMember.class);
		organizationMember.setStatus(GroupMemberStatus.ACTIVE.getCode());
		organizationMember.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
		organizationMember.setContactType(IdentifierType.MOBILE.getCode());
		
		if(org.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode())){
			organizationMember.setGroupId(org.getId());
		}
		
		 this.dbProvider.execute((status) -> {
			 organizationProvider.createOrganizationMember(organizationMember);
             if(organizationMember.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())){
             	EnterpriseContact contact = new EnterpriseContact();
             	contact.setUserId(organizationMember.getTargetId());
             	contact.setEnterpriseId(organizationMember.getOrganizationId());
             	contact.setStatus(organizationMember.getStatus());
             	this.createOrUpdateUserGroup(contact);
             }
             return null;
         });
		return ConvertHelper.convert(organizationMember, OrganizationMemberDTO.class);
	}
	
	@Override
	public void updatePersonnelsToDepartment(UpdatePersonnelsToDepartment cmd) {
		Organization org = checkOrganization(cmd.getGroupId());
		List<Long> ids = cmd.getIds();
		if(null == ids || 0 == ids.size()){
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"Invalid parameter.");
		}
//		organizationProvider.updateOrganizationMemberByIds(ids, org.getId());
	}
	
	/**
	 * 发消息
	 * @param operatorUid
	 * @param member
	 */
	private void sendMessageForEnterpriseContactReject(Long operatorUid, OrganizationMember member){
		
		User user = userProvider.findUserById(member.getTargetId());
		EnterpriseContact contact = new EnterpriseContact();
		contact.setEnterpriseId(member.getOrganizationId());
		contact.setUserId(member.getTargetId());
		contact.setStatus(member.getStatus());
		
		contact.setCreatorUid(operatorUid);
		contact.setName(member.getContactName());
		contact.setNickName(user.getNickName());
		contact.setAvatar(user.getAvatar());
		
		sendMessageForContactReject(null, contact);
		
	}
	
	/**
	 * 修改通讯录人员的状态
	 * @param operatorUid
	 * @param member
	 */
	private void updateEnterpriseContactStatus(Long operatorUid, OrganizationMember member){
		 this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
	            this.dbProvider.execute((status) -> {
	                this.organizationProvider.updateOrganizationMember(member);
	                if(member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())){
	                	EnterpriseContact contact = new EnterpriseContact();
	                	contact.setUserId(member.getTargetId());
	                	contact.setEnterpriseId(member.getOrganizationId());
	                	contact.setStatus(member.getStatus());
	                	this.updateUserGroupStatus(contact);
	                }
	                return null;
	            });
	            return null;
	        });
	        
	        if(LOGGER.isInfoEnabled()) {
	            LOGGER.info("Enterprise contact is deleted(active), operatorUid=" + operatorUid + ", contactId=" + member.getTargetId() 
	                + ", enterpriseId=" + member.getOrganizationId() + ", status=" + member.getStatus() + ", removeFromDb=" + member.getStatus());
	        }
	}
	
	/**
	 * 从企业通讯录里面删除人员
	 * @param operatorUid
	 * @param member
	 */
	private void deleteEnterpriseContactStatus(Long operatorUid, OrganizationMember member){
		 this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
			 
	            this.dbProvider.execute((status) -> {
	                this.organizationProvider.deleteOrganizationMemberById(member.getId());
	                if(member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())){
	                	this.userProvider.deleteUserGroup(member.getTargetId(), member.getOrganizationId());
	                }
	                return null;
	            });
	            return null;
	        });
	        
	        if(LOGGER.isInfoEnabled()) {
	            LOGGER.info("Enterprise contact is deleted(active), operatorUid=" + operatorUid + ", contactId=" + member.getTargetId() 
	                + ", enterpriseId=" + member.getOrganizationId() + ", status=" + member.getStatus() + ", removeFromDb=" + member.getStatus());
	        }
	}
	
	
	/**
	 * 补充返回用户信息，部门 角色
	 * @param organizationMembers
	 * @param
	 * @return
	 */
	private List<OrganizationMemberDTO> convertDTO(List<OrganizationMember> organizationMembers, Organization org){
		return organizationMembers.stream().map((c) ->{
			OrganizationMemberDTO dto =  ConvertHelper.convert(c, OrganizationMemberDTO.class);
			/**
			 * 补充用户部门
			 */
			if(c.getDetailId() != null){
				Long departmentId = organizationService.getDepartmentByDetailIdAndOrgId(c.getDetailId(), org.getId());
				dto.setDepartmentName(checkOrganization(departmentId).getName());
			}
			/**
			 * 补充用户角色
			 */
			if(c.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())){
				List<Long> resources = aclProvider.getRolesFromResourceAssignments("system", null, EntityType.USER.getCode(), c.getTargetId(), null);
				if(null != resources && 0 != resources.size()){
					List<AclRoleAssignmentsDTO> aclRoles = new ArrayList<AclRoleAssignmentsDTO>();
					for (Long roleId : resources) {
						AclRoleAssignmentsDTO aclRoleAssignmentsDTO = new AclRoleAssignmentsDTO();
						aclRoleAssignmentsDTO.setRoleId(roleId);
						aclRoles.add(aclRoleAssignmentsDTO);
					}
				}
			}
			return dto;
		}).collect(Collectors.toList());
	}
	
	/**
	 * 转换map
	 * @param organizationRoleMaps
	 * @return
	 */
	private Map<Long, OrganizationRoleMap> convertOrganizationRoleMap(List<OrganizationRoleMap> organizationRoleMaps){
		Map<Long, OrganizationRoleMap> map = new HashMap<Long, OrganizationRoleMap>();
		if(null == organizationRoleMaps){
			return map;
		}
		for (OrganizationRoleMap organizationRoleMap : organizationRoleMaps) {
			Role role = aclProvider.getRoleById(organizationRoleMap.getRoleId());
			organizationRoleMap.setRoleName(role.getName());
			map.put(organizationRoleMap.getRoleId(), organizationRoleMap);
		}
		return map;
	}
	
	private Map<Long, Organization> convertListToMap(List<Organization> depts){
		Map<Long, Organization> map = new HashMap<Long, Organization>();
		if(null == depts){
			return map;
		}
		for (Organization dept : depts) {
			map.put(dept.getId(), dept);
		}
		return map;
	}
	
	/**
	 * 检查企业是否存在
	 * @param orgId
	 * @return
	 */
	private Organization checkOrganization(Long orgId) {
		Organization org = organizationProvider.findOrganizationById(orgId);
		if(org == null){
			LOGGER.error("Unable to find the organization.organizationId=" + orgId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		return org;
	}
}
