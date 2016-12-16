package com.everhomes.blacklist;

import com.everhomes.acl.*;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.blacklist.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BlacklistServiceImpl implements BlacklistService{

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

	@Override
	public ListUserBlacklistsResponse listUserBlacklists(ListUserBlacklistsCommand cmd) {
		ListUserBlacklistsResponse res = new ListUserBlacklistsResponse();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Timestamp startTime = null == cmd.getStartTime() ? null : new Timestamp(cmd.getStartTime());
		Timestamp endTime = null == cmd.getEndTime() ? null : new Timestamp(cmd.getEndTime());
		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getAnchor());
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
					query.addConditions(Tables.EH_USER_BLACKLISTS.CONTACT_NAME.like(cmd.getKeywords() + "%").or(Tables.EH_USER_BLACKLISTS.CONTACT_TOKEN.eq(cmd.getKeywords())));
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
		UserBlacklist userBlacklist = blacklistProvider.findUserBlacklistByContactToken(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getContactToken());

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
		return userBlacklistDTO;
	}

	@Override
	public void addUserBlacklist(AddUserBlacklistCommand cmd) {

		User user = userProvider.findUserById(cmd.getUserId());

		Integer namespaceId = UserContext.getCurrentNamespaceId();

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
						rolePrivilegeService.deleteAcls(cmd.getOwnerType(),cmd.getOwnerId(),EntityType.USER.getCode(),userBlacklist.getOwnerUid(), null, privilegeIds);
					}
				}
				return null;
			});
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

}
