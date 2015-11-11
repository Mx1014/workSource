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
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.elasticsearch.common.lang3.StringUtils;
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
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
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
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.SAXHandlerEventUserModel;
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
		// UserInfo user =
		// userService.getUserSnapshotInfoWithPhone(identifier.getOwnerUid());
		User user = userProvider.findUserById(identifier.getOwnerUid());
		// List<Enterprise> enterprises =
		// this.enterpriseService.listEnterpriseByPhone(identifier.getIdentifierToken());
		List<Enterprise> enterprises = this.enterpriseProvider
				.queryEnterpriseByPhone(identifier.getIdentifierToken());
		Map<Long, Long> ctx = new HashMap<Long, Long>();
		for (Enterprise en : enterprises) {
			// Try to create a contact for this enterprise
			// TODO change to queryContactByPhone
			// EnterpriseContact contact =
			// this.enterpriseContactProvider.queryContactByUserId(en.getId(),
			// identifier.getOwnerUid());
			EnterpriseContact contact = this.getContactByPhone(en.getId(),
					identifier.getIdentifierToken());
			if (null == contact) {
				// create new contact for it
				contact = new EnterpriseContact();
				contact.setAvatar(user.getAvatar());
				contact.setEnterpriseId(en.getId());
				contact.setName(user.getNickName());
				contact.setNickName(user.getNickName());
				contact.setUserId(user.getId());

				// auto approved
				contact.setStatus(EnterpriseContactStatus.AUTHENTICATED
						.getCode());
				this.enterpriseContactProvider.createContact(contact);

				// create a entry for it, but not for all user identifier
				EnterpriseContactEntry entry = new EnterpriseContactEntry();
				entry.setContactId(contact.getId());
				entry.setCreatorUid(UserContext.current().getUser().getId());
				entry.setEnterpriseId(contact.getEnterpriseId());
				entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
				entry.setEntryValue(identifier.getIdentifierToken());
				this.enterpriseContactProvider.createContactEntry(entry);

				sendMessageForContactApproved(ctx, contact);
			} else {
				// auto approved
				contact.setUserId(user.getId());
				contact.setStatus(EnterpriseContactStatus.AUTHENTICATED
						.getCode());
				this.enterpriseContactProvider.createContact(contact);
				sendMessageForContactApproved(ctx, contact);
			}
		}

	}

	/**
	 * 同意某用户绑定到某企业通讯录，此接口应该用不到
	 */
	@Override
	public void approveUserToContact(User user, EnterpriseContact contact) {
		// TODO Auto-generated method stub

	}

	/**
	 * 拒绝用户申请某企业的通讯录
	 */
	@Override
	public void rejectUserFromContact(EnterpriseContact contact) {
		// 设置为删除
		contact.setStatus(EnterpriseContactStatus.INACTIVE.getCode());
		this.enterpriseContactProvider.updateContact(contact);

		// 发消息
		Map<String, Object> map = new HashMap<String, Object>();
		Enterprise enterprise = this.enterpriseService
				.getEnterpriseById(contact.getEnterpriseId());
		User user = userProvider.findUserById(contact.getUserId());
		map.put("enterpriseName", enterprise.getName());
		String scope = EnterpriseNotifyTemplateCode.SCOPE;
		int code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_REJECT_JOIN;
		String notifyTextForApplicant = localeTemplateService
				.getLocaleTemplateString(scope, code, user.getLocale(), map, "");
		sendUserNotification(user.getId(), notifyTextForApplicant);
	}

	/**
	 * 申请加入企业
	 * 
	 * @param contact
	 */
	@Override
	public EnterpriseContactDTO applyForContact(CreateContactByUserIdCommand cmd) {
		// Check exists
		EnterpriseContact existContact = this.enterpriseContactProvider
				.queryContactByUserId(cmd.getEnterpriseId(), cmd.getUserId());
		if (null != existContact) {
			// Should response error hear
			// contact.setId(existContact.getId());
			return ConvertHelper.convert(existContact,
					EnterpriseContactDTO.class);
		}

		// TODO check group?
		User user = UserContext.current().getUser();
		Long userId = (user == null) ? 0L : user.getId();
		EnterpriseContact result = this.dbProvider
				.execute((TransactionStatus status) -> {
					EnterpriseContact contact = new EnterpriseContact();
					contact.setCreatorUid(userId);
					contact.setEnterpriseId(cmd.getEnterpriseId());
					contact.setName(user.getNickName());
					contact.setNickName(user.getNickName());
					contact.setAvatar(user.getAvatar());

					// Create it
					contact.setStatus(EnterpriseContactStatus.WAITING_AUTH
							.getCode());
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
		// assert contact is from db and status is approve

		// TODO generate a error code
		if (contact.getStatus().equals(
				EnterpriseContactStatus.AUTHENTICATED.getCode())) {
			return;
		}

		contact.setStatus(EnterpriseContactStatus.AUTHENTICATED.getCode());
		this.enterpriseContactProvider.updateContact(contact);

		// TODO create group member for this user???

		// Set group for this contact
		String applyGroup = contact.getApplyGroup();
		if (applyGroup != null && !applyGroup.isEmpty()) {
			EnterpriseContactGroup group = this.enterpriseContactProvider
					.getContactGroupByName(contact.getEnterpriseId(),
							applyGroup);
			if (null != group) {
				approveContactToGroup(contact, group);
			}

		}

		// sendMessageForContactApproved(contact);
		sendMessageForContactApproved(null, contact);
	}

	@Override
	public void approveByContactId(Long contactId) {
		EnterpriseContact contact = this.enterpriseContactProvider
				.getContactById(contactId);
		this.approveContact(contact);
	}

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
			CrossShardListingLocator locator, EnterpriseContactStatus status,
			Integer pageSize) {
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
		EnterpriseContactEntry entry = this.enterpriseContactProvider
				.getEnterpriseContactEntryByPhone(enterpriseId, phone);
		if (entry != null) {
			EnterpriseContact contact = this.enterpriseContactProvider
					.getContactById(entry.getContactId());
			if (contact != null) {
				detail = ConvertHelper.convert(contact,
						EnterpriseContactDetail.class);
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
				GroupDiscriminator.Enterprise.getCode())) {
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

	private void sendMessageForContactApproved(Map<Long, Long> ctx,
			EnterpriseContact contact) {
		Long check = null;
		if (ctx != null) {
			check = ctx.get(contact.getEnterpriseId());
		}

		if (check == null) {
			if (ctx != null) {
				ctx.put(contact.getEnterpriseId(), 1l);
			}

			Enterprise enterprise = this.enterpriseService
					.getEnterpriseById(contact.getEnterpriseId());
			User user = userProvider.findUserById(contact.getUserId());

			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseName", enterprise.getName());
			;

			String userName = "";
			if (contact.getNickName() != null
					&& !contact.getNickName().trim().isEmpty()) {
				userName = contact.getNickName();
			} else {
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
			String notifyTextForApplicant = localeTemplateService
					.getLocaleTemplateString(scope, code, locale, map, "");
			List<Long> includeList = new ArrayList<Long>();
			includeList.add(contact.getUserId());
			sendEnterpriseNotification(enterprise.getId(), includeList, null,
					notifyTextForApplicant, null, null);

			// send notification to all the other members in the group
			code = EnterpriseNotifyTemplateCode.ENTERPRISE_USER_SUCCESS_OTHER;
			notifyTextForApplicant = localeTemplateService
					.getLocaleTemplateString(scope, code, locale, map, "");
			sendEnterpriseNotification(enterprise.getId(), null, includeList,
					notifyTextForApplicant, null, null);
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
		contact.setStatus(EnterpriseContactStatus.AUTHENTICATED.getCode());
		this.enterpriseContactProvider.updateContact(contact);

	}

	@Override
	public void rejectContact(RejectContactCommand cmd) {

		EnterpriseContact contact = this.enterpriseContactProvider
				.queryContactById(cmd.getContactId());
		contact.setStatus(EnterpriseContactStatus.INACTIVE.getCode());
		this.enterpriseContactProvider.updateContact(contact);

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
		// TODO Auto-generated method stub
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
						String applyGroup = RowResult.trimString(result.getA());
						String name = RowResult.trimString(result.getB());
						String sex = RowResult.trimString(result.getC());
						String PhoneNum = RowResult.trimString(result.getD());
						String building = RowResult.trimString(result.getE());
						String apartment = RowResult.trimString(result.getF());
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
						contact.setApplyGroup(applyGroup);
						contact.setName(name);
						contact.setSex(sex);
						contact.setCreatorUid(creatorId);
						contact.setCreateTime(new Timestamp(System
								.currentTimeMillis()));
						// phone find user
						User user = userService.findUserByIndentifier(PhoneNum);
						if (null != user)
							contact.setUserId(user.getId());
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
						if (applyGroup.contains("\\")) {
							String[] groups = applyGroup.split("\\");
							for (int groupNode = 0; groupNode < groups.length; groupNode++) {
								StringBuffer groupPath = new StringBuffer();
								StringBuffer groupNamePath = new StringBuffer();
								for (int groupSubNode = 0; groupSubNode <= groupNode; groupSubNode++) {
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
									groupNamePath.append("\\");

								}

							}
						} else {
							if (null == groupMap.get(applyGroup)) {
								// 如果不存在则添加新group
								EnterpriseContactGroup enterpriseContactGroup = new EnterpriseContactGroup();
								enterpriseContactGroup
										.setEnterpriseId(enterpriseId);
								enterpriseContactGroup.setName(applyGroup);
								enterpriseContactGroup.setPath("\\");
								enterpriseContactGroup.setCreatorUid(creatorId);
								enterpriseContactGroup
										.setCreateTime(new Timestamp(System
												.currentTimeMillis()));
								enterpriseContactProvider
										.createContactGroup(enterpriseContactGroup);
								groupMap.put(applyGroup,
										enterpriseContactGroup.getId());
							}
							// 添加menber表
							EnterpriseContactGroupMember enterpriseContactGroupMember = new EnterpriseContactGroupMember();
							enterpriseContactGroupMember
									.setContactGroupId(groupMap.get(applyGroup));
							enterpriseContactGroupMember
									.setEnterpriseId(enterpriseId);
							enterpriseContactGroupMember
									.setContactId(contactId);
							enterpriseContactGroupMember
									.setCreatorUid(creatorId);
							enterpriseContactGroupMember
									.setCreateTime(new Timestamp(System
											.currentTimeMillis()));
							enterpriseContactProvider
									.createContactGroupMember(enterpriseContactGroupMember);

						}
					}
				} else {
					LOGGER.error("excel data format is not correct.rowCount="
							+ resultList.size());
					throw RuntimeErrorException.errorWith(
							ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_GENERAL_EXCEPTION,
							"excel data format is not correct");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initContactGroupMap(Long enterpriseId,
			Map<String, Long> groupMap) {
		List<EnterpriseContactGroup> enterpriseContactGroups = enterpriseContactProvider
				.queryContactGroupByEnterpriseId(null, enterpriseId, 999999,
						null);
		for (EnterpriseContactGroup enterpriseContactGroup : enterpriseContactGroups) {
			groupMap.put(enterpriseContactGroup.getApplyGroup(),
					enterpriseContactGroup.getId());
		}

	}

}
