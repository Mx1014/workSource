// @formatter:off
package com.everhomes.enterprise;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.app.AppConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupMemberStatus;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessageMetaConstant;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.MetaObjectType;
import com.everhomes.messaging.QuestionMetaObject;
import com.everhomes.region.RegionScope;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

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
	public void processUserForContact(UserIdentifier identifier) {
		User user = userProvider.findUserById(identifier.getOwnerUid());
		List<Enterprise> enterprises = this.enterpriseProvider.queryEnterpriseByPhone(identifier.getIdentifierToken());
		Map<Long, Long> ctx = new HashMap<Long, Long>();
		for (Enterprise enterprise : enterprises) {
			EnterpriseContact contact = this.getContactByPhone(enterprise.getId(), identifier.getIdentifierToken());
			if (contact != null) {
			    GroupMemberStatus status = GroupMemberStatus.fromCode(contact.getStatus());
			    if(status != GroupMemberStatus.ACTIVE) {
    				contact.setUserId(user.getId());
    				contact.setStatus(GroupMemberStatus.ACTIVE.getCode());
    				updatePendingEnterpriseContactToAuthenticated(contact);
    				
    				sendMessageForContactApproved(ctx, contact);
			    } else {
			        if(LOGGER.isDebugEnabled()) {
			            LOGGER.debug("Enterprise contact is already authenticated, userId=" + identifier.getOwnerUid() 
			                + ", contactId=" + contact.getId() + ", enterpriseId=" + enterprise.getId());
			        }
			    }
			} else {
			    if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Enterprise contact not found, ignore to match contact, userId=" + identifier.getOwnerUid() 
                        + ", enterpriseId=" + enterprise.getId());
                }
			}
		}
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
	 * @param contact
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
					contact.setName(user.getNickName());
					contact.setNickName(user.getNickName());
					contact.setAvatar(user.getAvatar());
					contact.setUserId(userId);

					// Create it
					contact.setStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
					this.enterpriseContactProvider.createContact(contact);

					UserIdentifier identifier = userProvider
							.findClaimedIdentifierByOwnerAndType(userId,
									IdentifierType.MOBILE.getCode());
					EnterpriseContactEntry entry = new EnterpriseContactEntry();
					entry.setContactId(contact.getId());
					entry.setCreatorUid(UserContext.current().getUser().getId());
					entry.setEnterpriseId(contact.getEnterpriseId());
					entry.setEntryType(EnterpriseContactEntryType.Mobile
							.getCode());
					entry.setEntryValue(identifier.getIdentifierToken());

					this.enterpriseContactProvider.createContactEntry(entry);

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
			enterpriseContactGroupMember
					.setEnterpriseId(cmd.getEnterpriseId());
			enterpriseContactGroupMember.setContactId(result.getId());
			enterpriseContactGroupMember.setCreatorUid(UserContext.current().getUser().getId());
			enterpriseContactGroupMember
					.setCreateTime(new Timestamp(System
							.currentTimeMillis()));
			enterpriseContactProvider
					.createContactGroupMember(enterpriseContactGroupMember);
		}
		// TODO 发消息给所有根管理员
		if (result == null) {
			LOGGER.error("Failed to apply for enterprise contact, userId="
					+ userId + ", cmd=" + cmd);
			return null;
		} else {
			return ConvertHelper.convert(result, EnterpriseContactDTO.class);
		}
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
		Group group = groupProvider.findGroupById(contact.getEnterpriseId());
		UserGroup uGroup =new UserGroup();
		uGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
		uGroup.setOwnerUid(contact.getUserId());
		uGroup.setGroupId(contact.getEnterpriseId());
		uGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
		uGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
		uGroup.setRegionScopeId(group.getVisibleRegionId());
		userProvider.createUserGroup(uGroup);
		

		// sendMessageForContactApproved(contact);
		sendMessageForContactApproved(null, contact);
	}
	
    /**
     * 当企业成员加入group或者接受别人邀请加入group时，成员状态则待审核变为active，
     * 此时group里的成员数需要增加，为了保证成员数的正确性，需要添加锁；
     * @param member 成员
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
	
	private void createOrUpdateUserGroup(EnterpriseContact contact) {
        UserGroup userGroup = userProvider.findUserGroupByOwnerAndGroup(contact.getId(), contact.getEnterpriseId());
        if(userGroup == null) {
            createUserGroup(contact);
        } else {
            updateUserGroupStatus(contact);
        }
	}

    private void createUserGroup(EnterpriseContact contact) {
        Long enterpriseId = contact.getEnterpriseId();
        Long contactId = contact.getId();
        
        UserGroup userGroup = new UserGroup();
        userGroup.setOwnerUid(contactId);
        userGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
        userGroup.setGroupId(enterpriseId);
        userGroup.setMemberRole(contact.getRole());
        userGroup.setMemberStatus(contact.getStatus());
        this.userProvider.createUserGroup(userGroup);
    }
    
    private void updateUserGroupStatus(EnterpriseContact contact) {
        Long enterpriseId = contact.getEnterpriseId();
        Long contactId = contact.getId();
        UserGroup userGroup = userProvider.findUserGroupByOwnerAndGroup(contactId, enterpriseId);
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
                this.userProvider.deleteUserGroup(contact.getId(), contact.getEnterpriseId());
                
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
            LOGGER.info("Enterprise contact is deleted(active), operatorUid=" + operatorUid + ", contactId=" + contact.getId() 
                + ", enterpriseId=" + contact.getEnterpriseId() + ", status=" + contact.getStatus() + ", removeFromDb=" + removeFromDb);
        }
    } 
    
    /**
     * 对于通信录联系人，如果是主动申请进来的，若处理待审核状态则可直接删除
     * @param operatorUid 操作者
     * @param member 成员
     */
    private void deletePendingEnterpriseContact(Long operatorUid, EnterpriseContact contact, boolean removeFromDb) {
        this.dbProvider.execute((status) -> {
            if(removeFromDb) {
                this.enterpriseContactProvider.deleteContactById(contact);
            } else {
                this.enterpriseContactProvider.updateContact(contact);
            }
            this.userProvider.deleteUserGroup(contact.getId(), contact.getEnterpriseId());
            return null;
        });
        
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Enterprise contact is deleted(pending), operatorUid=" + operatorUid + ", contactId=" + contact.getId() 
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
	 * @param count
	 * @return
	 */
	@Override
	public List<EnterpriseContactDetail> listContactByEnterpriseId(
			ListingLocator locator, Long enterpriseId, Integer pageSize) {

		if (locator.getAnchor() == null)
			locator.setAnchor(0L);
		int count = PaginationConfigHelper
				.getPageSize(configProvider, pageSize);
		List<EnterpriseContact> contacts = this.enterpriseContactProvider
				.listContactByEnterpriseId(locator, enterpriseId, count + 1);

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
					.listContactByEnterpriseId(locator, group.getId(), pageSize);
			for (EnterpriseContact contact : contacts) {
				GroupMember gb = new GroupMember();
				gb.setMemberId(contact.getUserId());
				gb.setMemberType(EntityType.USER.getCode());
				members.add(gb);
			}
		}
		return members;
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
			ListingLocator locator, Long enterpriseId, Integer pageSize) {

		if (locator.getAnchor() == null)
			locator.setAnchor(0L);
		int count = PaginationConfigHelper
				.getPageSize(configProvider, pageSize);
		List<EnterpriseContact> contacts = this.enterpriseContactProvider
				.listContactRequestByEnterpriseId(locator, enterpriseId, count);

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

	@Override
	public void approveContact(ApproveContactCommand cmd) {

		EnterpriseContact contact = this.enterpriseContactProvider
			.queryContactById(cmd.getContactId());
	
	 
		 
		UserGroup uGroup= this.userProvider.findUserGroupByOwnerAndGroup(contact.getUserId(), contact.getEnterpriseId());
		if (null == uGroup){
			Group group = groupProvider.findGroupById(contact.getEnterpriseId());
			uGroup =new UserGroup();
			uGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
			uGroup.setOwnerUid(contact.getUserId());
			uGroup.setGroupId(group.getId());
			uGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
			uGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
			uGroup.setRegionScopeId(group.getVisibleRegionId());
			userProvider.createUserGroup(uGroup);
		}
		// 添加menber表 
		this.approveContact(contact);
	}

	@Override
	public void rejectContact(RejectContactCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
		EnterpriseContact contact = checkEnterpriseContactParameter(cmd.getContactId(), operatorUid, "rejectContact");
		
		GroupMemberStatus status = GroupMemberStatus.fromCode(contact.getStatus());
		if(status == GroupMemberStatus.ACTIVE) {
		    deleteActiveEnterpriseContact(operatorUid, contact, false, "");
		} else {
		    deletePendingEnterpriseContact(operatorUid, contact, true);
		}
		
		sendMessageForContactReject(null, contact);
	}
	
	@Override
    public void leaveEnterprise(LeaveEnterpriseCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        String tag = "leaveGroup";
        
        Long enterpriseId = cmd.getEnterpriseId();
        checkEnterpriseParameter(enterpriseId, userId, tag);

        EnterpriseContact contact = checkEnterpriseContactParameter(userId, userId, tag);
        GroupMemberStatus status = GroupMemberStatus.fromCode(contact.getStatus());
        if(status == GroupMemberStatus.ACTIVE) {
            deleteActiveEnterpriseContact(userId, contact, false, "");
        } else {
            deletePendingEnterpriseContact(userId, contact, true);
        }
        
        sendMessageForContactLeave(null, contact);
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
		Long creatorId = UserContext.current().getUser().getId();
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
						contact.setCreatorUid(creatorId);
						contact.setStatus(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
						contact.setCreateTime(new Timestamp(System
								.currentTimeMillis()));
						// phone find user
						User user = userService.findUserByIndentifier(PhoneNum);
						if (null != user){
							contact.setUserId(user.getId());
							
							Group group = groupProvider.findGroupById(enterpriseId);
							UserGroup uGroup = userProvider.findUserGroupByOwnerAndGroup(user.getId(), enterpriseId) ;
							if(null == uGroup){
							uGroup=new UserGroup();
							uGroup.setGroupDiscriminator(GroupDiscriminator.ENTERPRISE.getCode());
							uGroup.setOwnerUid(user.getId());
							uGroup.setGroupId(enterpriseId);
							uGroup.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
							uGroup.setRegionScope(RegionScope.COMMUNITY.getCode());
							uGroup.setRegionScopeId(group.getVisibleRegionId());
							userProvider.createUserGroup(uGroup);
							}
						}
						// TODO: map aparment 2 user
						Long contactId = enterpriseContactProvider
								.createContact(contact);
						EnterpriseContactEntry contactEntry = new EnterpriseContactEntry();
						contactEntry.setContactId(contactId);
						contactEntry.setEnterpriseId(enterpriseId);
						contactEntry
								.setEntryType(EnterpriseContactEntryType.Mobile
										.getCode());
						contactEntry.setEntryValue(PhoneNum);
						contactEntry.setCreatorUid(creatorId);
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
												.setCreatorUid(creatorId);
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
									enterpriseContactGroup.setCreatorUid(creatorId);
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
							enterpriseContactGroupMember.setCreatorUid(creatorId);
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
		User user = userService.findUserByIndentifier(cmd.getPhone());
		if (null != user){
			contact.setUserId(user.getId());
			
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
		// TODO Auto-generated method stub
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
	}
}
