// @formatter:off
package com.everhomes.sequence;

import com.everhomes.acl.AuthorizationProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.message.MessageProvider;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.admin.GetSequenceCommand;
import com.everhomes.rest.admin.GetSequenceDTO;
import com.everhomes.schema.tables.pojos.*;
import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SequenceServiceImpl implements SequenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceServiceImpl.class);

    private static final Long STD_DEFAULT_MAX_ID = 500000000L;

    private Long DEFAULT_MAX_ID = 1L;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private AuthorizationProvider authorizationProvider;

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    private final Schema[] schemas = new Schema[] {
            com.everhomes.schema.Ehcore.EHCORE,
            com.everhomes.server.schema.Ehcore.EHCORE
    };

    private final Set<Table<?>> excludeTables = new HashSet<>();
    private final Set<String> excludeTableNames = new HashSet<>();

    private final Map<String, Long> otherTableNames = new HashMap<>();

    {
        otherTableNames.put(Tables.EH_PM_NOTIFY_LOGS.getName(), STD_DEFAULT_MAX_ID + 193460427L);
        otherTableNames.put(Tables.EH_RENTALV2_CELLS.getName(), STD_DEFAULT_MAX_ID + 862484218L);
        otherTableNames.put(Tables.EH_MESSAGE_RECORDS.getName(), STD_DEFAULT_MAX_ID + 20151290L);
        otherTableNames.put(Tables.EH_SERVICE_MODULES.getName(), STD_DEFAULT_MAX_ID + 10800001L);
        otherTableNames.put(com.everhomes.schema.Tables.EH_ACL_PRIVILEGES.getName(), STD_DEFAULT_MAX_ID + 4920049200L);
        otherTableNames.put(Tables.EH_WEB_MENUS.getName(), STD_DEFAULT_MAX_ID + 76010000L);
        otherTableNames.put(Tables.EH_ADDRESSES.getName(), STD_DEFAULT_MAX_ID + 239825274387476000L);
        otherTableNames.put(Tables.EH_COMMUNITIES.getName(), STD_DEFAULT_MAX_ID + 240111044332061000L);
        otherTableNames.put(Tables.EH_COMMUNITY_GEOPOINTS.getName(), STD_DEFAULT_MAX_ID + 240111044331094000L);
    }

    @Override
    public void syncSequence() {
        boolean isStandard = configurationProvider.getBooleanValue("server.standard.flag", false);
        if (isStandard) {
            DEFAULT_MAX_ID = 500000000L;
        } else {
            DEFAULT_MAX_ID = 1L;
        }

        try {
            syncSequence0();
        } catch (Exception e) {
            LOGGER.error("Sync sequence0 error.", e);
        }
        syncSequence1();
    }

    private void syncSequence1() {
        for (Schema schema : this.schemas) {
            for (Table<?> table : schema.getTables()) {
                if (excludeTables.contains(table) || excludeTableNames.contains(table.getName())) {
                    continue;
                }
                try {
                    doSync(table);
                } catch (Exception e) {
                    LOGGER.error("Table " + table.getName() + " doSync error.", e);
                }
            }
        }
    }

    private void doSync(Table<?> table) {
        doSyncTableSequence(null, table.getClass(), table.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            Field<Long> id = DSL.fieldByName(DSL.getDataType(Long.class), "id");
            return dbContext.select(id.max()).from(table).fetchOne().value1();
        });
    }

    private void syncSequence0() {
        syncTableSequence(EhAcls.class, EhAcls.class, com.everhomes.schema.Tables.EH_ACLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACLS.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACLS).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclRoles.class, com.everhomes.schema.Tables.EH_ACL_ROLES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_ROLES.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_ROLES).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclRoleAssignments.class, com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServerShardMap.class, com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            Integer max = dbContext.select(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.ID.max())
                    .from(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP).fetchOne().value1();
            Long lmax = null;
            if (max != null) {
                lmax = Long.valueOf(max.longValue());
            }
            return lmax;
        });

        syncTableSequence(null, EhContentShardMap.class, com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.ID.max())
                    .from(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUsers.class, Tables.EH_USERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();
        });

        // user account is a special field, it default to be number stype, but it can be changed to any character only if they are unique in db
        syncUserAccountName();

        syncActiveAppId();

        syncMessageIndexId();

        syncAuthorizationControlId();

        //add by lei.lv
        syncActiveAppId();

        syncTableSequence(EhUniongroupConfigures.class, EhUniongroupConfigures.class, Tables.EH_UNIONGROUP_CONFIGURES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_UNIONGROUP_CONFIGURES.ID.max()).from(Tables.EH_UNIONGROUP_CONFIGURES).fetchOne().value1();
        });
        syncTableSequence(EhUniongroupMemberDetails.class, EhUniongroupMemberDetails.class, Tables.EH_UNIONGROUP_MEMBER_DETAILS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ID.max()).from(Tables.EH_UNIONGROUP_MEMBER_DETAILS).fetchOne().value1();
        });
        syncTableSequence(EhUsers.class, EhUserGroups.class, Tables.EH_USER_GROUPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_GROUPS.ID.max()).from(Tables.EH_USER_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserInvitations.class, Tables.EH_USER_INVITATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_INVITATIONS.ID.max()).from(Tables.EH_USER_INVITATIONS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserIdentifiers.class, Tables.EH_USER_IDENTIFIERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IDENTIFIERS.ID.max()).from(Tables.EH_USER_IDENTIFIERS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserInvitationRoster.class, Tables.EH_USER_INVITATION_ROSTER.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_INVITATION_ROSTER.ID.max()).from(Tables.EH_USER_INVITATION_ROSTER).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserPosts.class, Tables.EH_USER_POSTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_POSTS.ID.max()).from(Tables.EH_USER_POSTS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserLikes.class, Tables.EH_USER_LIKES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_LIKES.ID.max()).from(Tables.EH_USER_LIKES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserFavorites.class, Tables.EH_USER_FAVORITES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_FAVORITES.ID.max()).from(Tables.EH_USER_FAVORITES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserProfiles.class, Tables.EH_USER_PROFILES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_PROFILES.ID.max()).from(Tables.EH_USER_PROFILES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserServiceAddresses.class, Tables.EH_USER_SERVICE_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_SERVICE_ADDRESSES.ID.max()).from(Tables.EH_USER_SERVICE_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserCommunities.class, Tables.EH_USER_COMMUNITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_COMMUNITIES.ID.max()).from(Tables.EH_USER_COMMUNITIES).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForums.class, Tables.EH_FORUMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FORUMS.ID.max()).from(Tables.EH_FORUMS).fetchOne().value1();
        });

        syncTableSequence(null, EhContentServerResources.class, Tables.EH_CONTENT_SERVER_RESOURCES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONTENT_SERVER_RESOURCES.ID.max())
                    .from(Tables.EH_CONTENT_SERVER_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroups.class, Tables.EH_GROUPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GROUPS.ID.max()).from(Tables.EH_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroupMembers.class, Tables.EH_GROUP_MEMBERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_MEMBERS.ID.max()).from(Tables.EH_GROUP_MEMBERS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroupOpRequests.class, Tables.EH_GROUP_OP_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_OP_REQUESTS.ID.max()).from(Tables.EH_GROUP_OP_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumPosts.class, Tables.EH_FORUM_POSTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_POSTS.ID.max()).from(Tables.EH_FORUM_POSTS).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumAssignedScopes.class, Tables.EH_FORUM_ASSIGNED_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_FORUM_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumAttachments.class, Tables.EH_FORUM_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_ATTACHMENTS.ID.max()).from(Tables.EH_FORUM_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhNearbyCommunityMap.class, Tables.EH_NEARBY_COMMUNITY_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NEARBY_COMMUNITY_MAP.ID.max()).from(Tables.EH_NEARBY_COMMUNITY_MAP).fetchOne().value1();
        });

        syncTableSequence(EhActivities.class, EhActivities.class, Tables.EH_ACTIVITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITIES.ID.max()).from(Tables.EH_ACTIVITIES).fetchOne().value1();
        });

        syncTableSequence(EhActivities.class, EhActivityRoster.class, Tables.EH_ACTIVITY_ROSTER.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_ROSTER.ID.max()).from(Tables.EH_ACTIVITY_ROSTER).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPolls.class, Tables.EH_POLLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_POLLS.ID.max()).from(Tables.EH_POLLS).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPollItems.class, Tables.EH_POLL_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_POLL_ITEMS.ID.max()).from(Tables.EH_POLL_ITEMS).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPollVotes.class, Tables.EH_POLL_VOTES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_POLL_VOTES.ID.max()).from(Tables.EH_POLL_VOTES).fetchOne().value1();
        });

        syncTableSequence(null, EhQrcodes.class, Tables.EH_QRCODES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QRCODES.ID.max()).from(Tables.EH_QRCODES).fetchOne().value1();
        });

        syncTableSequence(null, EhOauth2Codes.class, Tables.EH_OAUTH2_CODES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_CODES.ID.max()).from(Tables.EH_OAUTH2_CODES).fetchOne().value1();
        });

        syncTableSequence(null, EhOauth2Tokens.class, Tables.EH_OAUTH2_TOKENS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_TOKENS.ID.max()).from(Tables.EH_OAUTH2_TOKENS).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionRealm.class, Tables.EH_VERSION_REALM.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_REALM.ID.max()).from(Tables.EH_VERSION_REALM).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionUpgradeRules.class, Tables.EH_VERSION_UPGRADE_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_UPGRADE_RULES.ID.max()).from(Tables.EH_VERSION_UPGRADE_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionUrls.class, Tables.EH_VERSION_URLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_URLS.ID.max()).from(Tables.EH_VERSION_URLS).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionedContent.class, Tables.EH_VERSIONED_CONTENT.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VERSIONED_CONTENT.ID.max()).from(Tables.EH_VERSIONED_CONTENT).fetchOne().value1();
        });

        syncTableSequence(null, EhCooperationRequests.class, Tables.EH_COOPERATION_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_COOPERATION_REQUESTS.ID.max()).from(Tables.EH_COOPERATION_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinesses.class, Tables.EH_BUSINESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESSES.ID.max()).from(Tables.EH_BUSINESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessVisibleScopes.class, Tables.EH_BUSINESS_VISIBLE_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_VISIBLE_SCOPES.ID.max()).from(Tables.EH_BUSINESS_VISIBLE_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessAssignedScopes.class, Tables.EH_BUSINESS_ASSIGNED_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_BUSINESS_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessCategories.class, Tables.EH_BUSINESS_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_CATEGORIES.ID.max()).from(Tables.EH_BUSINESS_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPushMessageResults.class, Tables.EH_PUSH_MESSAGE_RESULTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUSH_MESSAGE_RESULTS.ID.max()).from(Tables.EH_PUSH_MESSAGE_RESULTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContacts.class, Tables.EH_ENTERPRISE_CONTACTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACTS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactEntries.class, Tables.EH_ENTERPRISE_CONTACT_ENTRIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_ENTRIES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactGroups.class, Tables.EH_ENTERPRISE_CONTACT_GROUPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactGroupMembers.class, Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchDayLogs.class, Tables.EH_PUNCH_DAY_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_DAY_LOGS.ID.max()).from(Tables.EH_PUNCH_DAY_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchExceptionApprovals.class, Tables.EH_PUNCH_EXCEPTION_APPROVALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_APPROVALS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchExceptionRequests.class, Tables.EH_PUNCH_EXCEPTION_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchGeopoints.class, Tables.EH_PUNCH_GEOPOINTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_GEOPOINTS.ID.max()).from(Tables.EH_PUNCH_GEOPOINTS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchLogs.class, Tables.EH_PUNCH_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_LOGS.ID.max()).from(Tables.EH_PUNCH_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchRules.class, Tables.EH_PUNCH_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_RULES.ID.max()).from(Tables.EH_PUNCH_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWorkday.class, Tables.EH_PUNCH_WORKDAY.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WORKDAY.ID.max()).from(Tables.EH_PUNCH_WORKDAY).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchTimeRules.class, Tables.EH_PUNCH_TIME_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_TIME_RULES.ID.max()).from(Tables.EH_PUNCH_TIME_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhPunchLocationRules.class, Tables.EH_PUNCH_LOCATION_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_LOCATION_RULES.ID.max()).from(Tables.EH_PUNCH_LOCATION_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWifis.class, Tables.EH_PUNCH_WIFIS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WIFIS.ID.max()).from(Tables.EH_PUNCH_WIFIS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWifiRules.class, Tables.EH_PUNCH_WIFI_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WIFI_RULES.ID.max()).from(Tables.EH_PUNCH_WIFI_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchHolidays.class, Tables.EH_PUNCH_HOLIDAYS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_HOLIDAYS.ID.max()).from(Tables.EH_PUNCH_HOLIDAYS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWorkdayRules.class, Tables.EH_PUNCH_WORKDAY_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WORKDAY_RULES.ID.max()).from(Tables.EH_PUNCH_WORKDAY_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchRuleOwnerMap.class, Tables.EH_PUNCH_RULE_OWNER_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.max()).from(Tables.EH_PUNCH_RULE_OWNER_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchStatistics.class, Tables.EH_PUNCH_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_STATISTICS.ID.max()).from(Tables.EH_PUNCH_STATISTICS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchSchedulings.class, Tables.EH_PUNCH_SCHEDULINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_SCHEDULINGS.ID.max()).from(Tables.EH_PUNCH_SCHEDULINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhUniongroupVersion.class, Tables.EH_UNIONGROUP_VERSION.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_UNIONGROUP_VERSION.ID.max()).from(Tables.EH_UNIONGROUP_VERSION).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalBillAttachments.class, Tables.EH_RENTAL_BILL_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILL_ATTACHMENTS.ID.max()).from(Tables.EH_RENTAL_BILL_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalBills.class, Tables.EH_RENTAL_BILLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILLS.ID.max()).from(Tables.EH_RENTAL_BILLS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalItemsBills.class, Tables.EH_RENTAL_ITEMS_BILLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_ITEMS_BILLS.ID.max()).from(Tables.EH_RENTAL_ITEMS_BILLS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalRules.class, Tables.EH_RENTAL_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_RULES.ID.max()).from(Tables.EH_RENTAL_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalSiteItems.class, Tables.EH_RENTAL_SITE_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITE_ITEMS.ID.max()).from(Tables.EH_RENTAL_SITE_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalSiteRules.class, Tables.EH_RENTAL_SITE_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITE_RULES.ID.max()).from(Tables.EH_RENTAL_SITE_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalSites.class, Tables.EH_RENTAL_SITES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITES.ID.max()).from(Tables.EH_RENTAL_SITES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalSitesBills.class, Tables.EH_RENTAL_SITES_BILLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITES_BILLS.ID.max()).from(Tables.EH_RENTAL_SITES_BILLS).fetchOne().value1();
        });


        syncTableSequence(null, EhRentalBillPaybillMap.class, Tables.EH_RENTAL_BILL_PAYBILL_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILL_PAYBILL_MAP.ID.max()).from(Tables.EH_RENTAL_BILL_PAYBILL_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2DefaultRules.class, Tables.EH_RENTALV2_DEFAULT_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_DEFAULT_RULES.ID.max()).from(Tables.EH_RENTALV2_DEFAULT_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2TimeInterval.class, Tables.EH_RENTALV2_TIME_INTERVAL.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_TIME_INTERVAL.ID.max()).from(Tables.EH_RENTALV2_TIME_INTERVAL).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2CloseDates.class, Tables.EH_RENTALV2_CLOSE_DATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_CLOSE_DATES.ID.max()).from(Tables.EH_RENTALV2_CLOSE_DATES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2ConfigAttachments.class, Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ID.max()).from(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2ResourceRanges.class, Tables.EH_RENTALV2_RESOURCE_RANGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_RANGES.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_RANGES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2OrderAttachments.class, Tables.EH_RENTALV2_ORDER_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDER_ATTACHMENTS.ID.max()).from(Tables.EH_RENTALV2_ORDER_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2Orders.class, Tables.EH_RENTALV2_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDERS.ID.max()).from(Tables.EH_RENTALV2_ORDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2ResourceNumbers.class, Tables.EH_RENTALV2_RESOURCE_NUMBERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_NUMBERS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_NUMBERS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2Items.class, Tables.EH_RENTALV2_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ITEMS.ID.max()).from(Tables.EH_RENTALV2_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2Resources.class, Tables.EH_RENTALV2_RESOURCES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCES.ID.max()).from(Tables.EH_RENTALV2_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2RefundOrders.class, Tables.EH_RENTALV2_REFUND_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_REFUND_ORDERS.ID.max()).from(Tables.EH_RENTALV2_REFUND_ORDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2ResourceOrders.class, Tables.EH_RENTALV2_RESOURCE_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_ORDERS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_ORDERS).fetchOne().value1();
        });


        syncTableSequence(null, EhRentalv2OrderPayorderMap.class, Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ID.max()).from(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2ResourceTypes.class, Tables.EH_RENTALV2_RESOURCE_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_TYPES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhRechargeInfo.class, Tables.EH_RECHARGE_INFO.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RECHARGE_INFO.ID.max()).from(Tables.EH_RECHARGE_INFO).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseCommunityMap.class, Tables.EH_ENTERPRISE_COMMUNITY_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.max()).from(Tables.EH_ENTERPRISE_COMMUNITY_MAP).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseAddresses.class, Tables.EH_ENTERPRISE_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_ADDRESSES.ID.max()).from(Tables.EH_ENTERPRISE_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseAttachments.class, Tables.EH_ENTERPRISE_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_ATTACHMENTS.ID.max()).from(Tables.EH_ENTERPRISE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhBuildings.class, Tables.EH_BUILDINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUILDINGS.ID.max()).from(Tables.EH_BUILDINGS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhBuildingAttachments.class, Tables.EH_BUILDING_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUILDING_ATTACHMENTS.ID.max()).from(Tables.EH_BUILDING_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhParkCharge.class, Tables.EH_PARK_CHARGE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARK_CHARGE.ID.max()).from(Tables.EH_PARK_CHARGE).fetchOne().value1();
        });

        syncTableSequence(null, EhParkApplyCard.class, Tables.EH_PARK_APPLY_CARD.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARK_APPLY_CARD.ID.max()).from(Tables.EH_PARK_APPLY_CARD).fetchOne().value1();
        });

        syncTableSequence(null, EhRechargeInfo.class, Tables.EH_RECHARGE_INFO.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RECHARGE_INFO.ID.max()).from(Tables.EH_RECHARGE_INFO).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizations.class, Tables.EH_ORGANIZATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATIONS.ID.max()).from(Tables.EH_ORGANIZATIONS).fetchOne().value1();
        });


        syncTableSequence(null, EhYellowPages.class, Tables.EH_YELLOW_PAGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_YELLOW_PAGES.ID.max()).from(Tables.EH_YELLOW_PAGES).fetchOne().value1();
        });

        syncTableSequence(null, EhYellowPageAttachments.class, Tables.EH_YELLOW_PAGE_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_YELLOW_PAGE_ATTACHMENTS.ID.max()).from(Tables.EH_YELLOW_PAGE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhNamespaceResources.class, Tables.EH_NAMESPACE_RESOURCES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NAMESPACE_RESOURCES.ID.max()).from(Tables.EH_NAMESPACE_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseDetails.class, Tables.EH_ENTERPRISE_DETAILS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_DETAILS.ID.max()).from(Tables.EH_ENTERPRISE_DETAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnterpriseOpRequests.class, Tables.EH_ENTERPRISE_OP_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.max()).from(Tables.EH_ENTERPRISE_OP_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhLeasePromotions.class, Tables.EH_LEASE_PROMOTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTIONS.ID.max()).from(Tables.EH_LEASE_PROMOTIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhLeasePromotionAttachments.class, Tables.EH_LEASE_PROMOTION_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.ID.max()).from(Tables.EH_LEASE_PROMOTION_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserGroupHistories.class, Tables.EH_USER_GROUP_HISTORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_GROUP_HISTORIES.ID.max()).from(Tables.EH_USER_GROUP_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfAccountCategories.class, Tables.EH_CONF_ACCOUNT_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNT_CATEGORIES.ID.max()).from(Tables.EH_CONF_ACCOUNT_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(EhConfOrders.class, EhConfInvoices.class, Tables.EH_CONF_INVOICES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_INVOICES.ID.max()).from(Tables.EH_CONF_INVOICES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfOrders.class, Tables.EH_CONF_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ORDERS.ID.max()).from(Tables.EH_CONF_ORDERS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfOrderAccountMap.class, Tables.EH_CONF_ORDER_ACCOUNT_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ID.max()).from(Tables.EH_CONF_ORDER_ACCOUNT_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhConfSourceAccounts.class, Tables.EH_CONF_SOURCE_ACCOUNTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_SOURCE_ACCOUNTS.ID.max()).from(Tables.EH_CONF_SOURCE_ACCOUNTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfAccounts.class, Tables.EH_CONF_ACCOUNTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNTS.ID.max()).from(Tables.EH_CONF_ACCOUNTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfAccountHistories.class, Tables.EH_CONF_ACCOUNT_HISTORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNT_HISTORIES.ID.max()).from(Tables.EH_CONF_ACCOUNT_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfConferences.class, Tables.EH_CONF_CONFERENCES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_CONFERENCES.ID.max()).from(Tables.EH_CONF_CONFERENCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfEnterprises.class, Tables.EH_CONF_ENTERPRISES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ENTERPRISES.ID.max()).from(Tables.EH_CONF_ENTERPRISES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarningContacts.class, Tables.EH_WARNING_CONTACTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WARNING_CONTACTS.ID.max()).from(Tables.EH_WARNING_CONTACTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfReservations.class, Tables.EH_CONF_RESERVATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_RESERVATIONS.ID.max()).from(Tables.EH_CONF_RESERVATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationCommunityRequests.class, Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.ID.max()).from(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationDetails.class, Tables.EH_ORGANIZATION_DETAILS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_DETAILS.ID.max()).from(Tables.EH_ORGANIZATION_DETAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddresses.class, Tables.EH_ORGANIZATION_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESSES.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAttachments.class, Tables.EH_ORGANIZATION_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationRoleMap.class, Tables.EH_ORGANIZATION_ROLE_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ROLE_MAP.ID.max()).from(Tables.EH_ORGANIZATION_ROLE_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationTaskTargets.class, Tables.EH_ORGANIZATION_TASK_TARGETS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_TASK_TARGETS.ID.max()).from(Tables.EH_ORGANIZATION_TASK_TARGETS).fetchOne().value1();
        });

        syncTableSequence(EhMessages.class, EhMessages.class, com.everhomes.schema.Tables.EH_MESSAGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_MESSAGES.ID.max()).from(com.everhomes.schema.Tables.EH_MESSAGES).fetchOne().value1();
        });

        syncTableSequence(EhMessageBoxs.class, EhMessageBoxs.class, com.everhomes.schema.Tables.EH_MESSAGE_BOXS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_MESSAGE_BOXS.ID.max()).from(com.everhomes.schema.Tables.EH_MESSAGE_BOXS).fetchOne().value1();
        });


        syncTableSequence(EhRepeatSettings.class, EhRepeatSettings.class, Tables.EH_REPEAT_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_REPEAT_SETTINGS.ID.max()).from(Tables.EH_REPEAT_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionStandards.class, EhQualityInspectionStandards.class, Tables.EH_QUALITY_INSPECTION_STANDARDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARDS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARDS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionStandardGroupMap.class, EhQualityInspectionStandardGroupMap.class, Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTasks.class, EhQualityInspectionTasks.class, Tables.EH_QUALITY_INSPECTION_TASKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASKS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASKS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionEvaluationFactors.class, EhQualityInspectionEvaluationFactors.class, Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionEvaluations.class, EhQualityInspectionEvaluations.class, Tables.EH_QUALITY_INSPECTION_EVALUATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_EVALUATIONS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTaskRecords.class, EhQualityInspectionTaskRecords.class, Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTaskAttachments.class, EhQualityInspectionTaskAttachments.class, Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionCategories.class, EhQualityInspectionCategories.class, Tables.EH_QUALITY_INSPECTION_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_CATEGORIES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_CATEGORIES).fetchOne().value1();
        });

        // 同步主表sequence时，第一个参数需要指定主表对应的pojo class by lqs 20160430
        //syncTableSequence(null, EhDoorAccess.class, Tables.EH_DOOR_ACCESS.getName(), (dbContext) -> {
        syncTableSequence(EhDoorAccess.class, EhDoorAccess.class, Tables.EH_DOOR_ACCESS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_ACCESS.ID.max()).from(Tables.EH_DOOR_ACCESS).fetchOne().value1();
        });
        syncTableSequence(null, EhOwnerDoors.class, Tables.EH_OWNER_DOORS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OWNER_DOORS.ID.max()).from(Tables.EH_OWNER_DOORS).fetchOne().value1();
        });
        syncTableSequence(null, EhDoorAuth.class, Tables.EH_DOOR_AUTH.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_AUTH.ID.max()).from(Tables.EH_DOOR_AUTH).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkFirmware.class, Tables.EH_ACLINK_FIRMWARE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_FIRMWARE.ID.max()).from(Tables.EH_ACLINK_FIRMWARE).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAclinks.class, Tables.EH_ACLINKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINKS.ID.max()).from(Tables.EH_ACLINKS).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAesServerKey.class, Tables.EH_AES_SERVER_KEY.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_AES_SERVER_KEY.ID.max()).from(Tables.EH_AES_SERVER_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAesUserKey.class, Tables.EH_AES_USER_KEY.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_AES_USER_KEY.ID.max()).from(Tables.EH_AES_USER_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAclinkUndoKey.class, Tables.EH_ACLINK_UNDO_KEY.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_UNDO_KEY.ID.max()).from(Tables.EH_ACLINK_UNDO_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhDoorCommand.class, Tables.EH_DOOR_COMMAND.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_COMMAND.ID.max()).from(Tables.EH_DOOR_COMMAND).fetchOne().value1();
        });

        syncTableSequence(null, EhOpPromotionAssignedScopes.class, Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhOpPromotionActivities.class, Tables.EH_OP_PROMOTION_ACTIVITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.max()).from(Tables.EH_OP_PROMOTION_ACTIVITIES).fetchOne().value1();
        });


        syncTableSequence(null, EhScheduleTasks.class, Tables.EH_SCHEDULE_TASKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SCHEDULE_TASKS.ID.max()).from(Tables.EH_SCHEDULE_TASKS).fetchOne().value1();
        });


        syncTableSequence(null, EhScheduleTaskLogs.class, Tables.EH_SCHEDULE_TASK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SCHEDULE_TASK_LOGS.ID.max()).from(Tables.EH_SCHEDULE_TASK_LOGS).fetchOne().value1();
        });

//        syncTableSequence(null, EhSocialSecurityInoutReport.class, Tables.EH_SOCIAL_SECURITY_INOUT_REPORT.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_INOUT_REPORT.ID.max()).from(Tables.EH_SOCIAL_SECURITY_INOUT_REPORT).fetchOne().value1();
//        });
//
//        syncTableSequence(null, EhSocialSecurityBases.class, Tables.EH_SOCIAL_SECURITY_BASES.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_BASES.ID.max()).from(Tables.EH_SOCIAL_SECURITY_BASES).fetchOne().value1();
//        });
//
//        syncTableSequence(null, EhSocialSecurityDepartmentSummary.class, Tables.EH_SOCIAL_SECURITY_DEPARTMENT_SUMMARY.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_DEPARTMENT_SUMMARY.ID.max()).from(Tables.EH_SOCIAL_SECURITY_DEPARTMENT_SUMMARY).fetchOne().value1();
//        });
//
//        syncTableSequence(null, EhSocialSecurityPaymentLogs.class, Tables.EH_SOCIAL_SECURITY_PAYMENT_LOGS.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_PAYMENT_LOGS.ID.max()).from(Tables.EH_SOCIAL_SECURITY_PAYMENT_LOGS).fetchOne().value1();
//        });
//
//        syncTableSequence(null, EhSocialSecurityPayments.class, Tables.EH_SOCIAL_SECURITY_PAYMENTS.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_PAYMENTS.ID.max()).from(Tables.EH_SOCIAL_SECURITY_PAYMENTS).fetchOne().value1();
//        });
//
//        syncTableSequence(null, EhSocialSecuritySettings.class, Tables.EH_SOCIAL_SECURITY_SETTINGS.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.max()).from(Tables.EH_SOCIAL_SECURITY_SETTINGS).fetchOne().value1();
//        });
//
//        syncTableSequence(null, EhSocialSecuritySummary.class, Tables.EH_SOCIAL_SECURITY_SUMMARY.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_SUMMARY.ID.max()).from(Tables.EH_SOCIAL_SECURITY_SUMMARY).fetchOne().value1();
//        });
//
//        syncTableSequence(null, EhSocialSecurityReport.class, Tables.EH_SOCIAL_SECURITY_REPORT.getName(), (dbContext) -> {
//            return dbContext.select(Tables.EH_SOCIAL_SECURITY_REPORT.ID.max()).from(Tables.EH_SOCIAL_SECURITY_REPORT).fetchOne().value1();
//        });


        syncTableSequence(null, EhOpPromotionMessages.class, Tables.EH_OP_PROMOTION_MESSAGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_MESSAGES.ID.max()).from(Tables.EH_OP_PROMOTION_MESSAGES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessAssignedNamespaces.class, Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.ID.max()).from(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES).fetchOne().value1();
        });

        syncTableSequence(null, EhParkingRechargeOrders.class, Tables.EH_PARKING_RECHARGE_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_RECHARGE_ORDERS.ID.max()).from(Tables.EH_PARKING_RECHARGE_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingRechargeRates.class, Tables.EH_PARKING_RECHARGE_RATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_RECHARGE_RATES.ID.max()).from(Tables.EH_PARKING_RECHARGE_RATES).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCardRequests.class, Tables.EH_PARKING_CARD_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CARD_REQUESTS.ID.max()).from(Tables.EH_PARKING_CARD_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingLots.class, Tables.EH_PARKING_LOTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_LOTS.ID.max()).from(Tables.EH_PARKING_LOTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingVendors.class, Tables.EH_PARKING_VENDORS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_VENDORS.ID.max()).from(Tables.EH_PARKING_VENDORS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmsyPayers.class, Tables.EH_PMSY_PAYERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_PAYERS.ID.max()).from(Tables.EH_PMSY_PAYERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyOrders.class, Tables.EH_PMSY_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_ORDERS.ID.max()).from(Tables.EH_PMSY_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyOrderItems.class, Tables.EH_PMSY_ORDER_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_ORDER_ITEMS.ID.max()).from(Tables.EH_PMSY_ORDER_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyCommunities.class, Tables.EH_PMSY_COMMUNITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_COMMUNITIES.ID.max()).from(Tables.EH_PMSY_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhWifiSettings.class, Tables.EH_WIFI_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WIFI_SETTINGS.ID.max()).from(Tables.EH_WIFI_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCards.class, Tables.EH_PAYMENT_CARDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARDS.ID.max()).from(Tables.EH_PAYMENT_CARDS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardIssuers.class, Tables.EH_PAYMENT_CARD_ISSUERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_ISSUERS.ID.max()).from(Tables.EH_PAYMENT_CARD_ISSUERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardIssuerCommunities.class, Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.ID.max()).from(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardRechargeOrders.class, Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.ID.max()).from(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardTransactions.class, Tables.EH_PAYMENT_CARD_TRANSACTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_TRANSACTIONS.ID.max()).from(Tables.EH_PAYMENT_CARD_TRANSACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionLogs.class, Tables.EH_QUALITY_INSPECTION_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_LOGS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhHotTags.class, Tables.EH_HOT_TAGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_HOT_TAGS.ID.max()).from(Tables.EH_HOT_TAGS).fetchOne().value1();
        });
        syncTableSequence(null, EhNews.class, Tables.EH_NEWS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS.ID.max()).from(Tables.EH_NEWS).fetchOne().value1();
        });
        syncTableSequence(null, EhNewsComment.class, Tables.EH_NEWS_COMMENT.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS_COMMENT.ID.max()).from(Tables.EH_NEWS_COMMENT).fetchOne().value1();
        });

        //小区导入时添加
        syncTableSequence(null, EhNamespaces.class, com.everhomes.schema.Tables.EH_NAMESPACES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            Integer maxId = dbContext.select(com.everhomes.schema.Tables.EH_NAMESPACES.ID.max()).from(com.everhomes.schema.Tables.EH_NAMESPACES).fetchOne().value1();
            if (maxId != null) {
                return Long.valueOf(maxId.longValue());
            }
            return null;
        });
        syncTableSequence(null, EhNamespaceDetails.class, Tables.EH_NAMESPACE_DETAILS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NAMESPACE_DETAILS.ID.max()).from(Tables.EH_NAMESPACE_DETAILS).fetchOne().value1();
        });
        syncTableSequence(null, EhConfigurations.class, com.everhomes.schema.Tables.EH_CONFIGURATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            Integer maxId = dbContext.select(com.everhomes.schema.Tables.EH_CONFIGURATIONS.ID.max()).from(com.everhomes.schema.Tables.EH_CONFIGURATIONS).fetchOne().value1();
            if (maxId != null) {
                return Long.valueOf(maxId.longValue());
            }
            return null;
        });

        syncTableSequence(null, EhEquipmentInspectionStandards.class, Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipments.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionAccessories.class, Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionAccessoryMap.class, Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentParameters.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentAttachments.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhLocaleTemplates.class, Tables.EH_LOCALE_TEMPLATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LOCALE_TEMPLATES.ID.max()).from(Tables.EH_LOCALE_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhCategories.class, Tables.EH_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CATEGORIES.ID.max()).from(Tables.EH_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionPlans.class, Tables.EH_EQUIPMENT_INSPECTION_PLANS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_PLANS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_PLANS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionPlanGroupMap.class, Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentPlanMap.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionReviewDate.class, Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentLogs.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_LOGS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTasks.class, Tables.EH_EQUIPMENT_INSPECTION_TASKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASKS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTaskAttachments.class, Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTaskLogs.class, Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOfficeCubicleOrders.class, Tables.EH_OFFICE_CUBICLE_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_ORDERS.ID.max()).from(Tables.EH_OFFICE_CUBICLE_ORDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhOfficeCubicleCategories.class, Tables.EH_OFFICE_CUBICLE_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_CATEGORIES.ID.max()).from(Tables.EH_OFFICE_CUBICLE_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhOfficeCubicleSpaces.class, Tables.EH_OFFICE_CUBICLE_SPACES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_SPACES.ID.max()).from(Tables.EH_OFFICE_CUBICLE_SPACES).fetchOne().value1();
        });
        syncTableSequence(null, EhOfficeCubicleAttachments.class, Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.ID.max()).from(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMembers.class, Tables.EH_ORGANIZATION_MEMBERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBERS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBERS).fetchOne().value1();
        });
        syncTableSequence(null, EhRichTexts.class, Tables.EH_RICH_TEXTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RICH_TEXTS.ID.max()).from(Tables.EH_RICH_TEXTS).fetchOne().value1();
        });
        syncTableSequence(null, EhJournals.class, Tables.EH_JOURNALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_JOURNALS.ID.max()).from(Tables.EH_JOURNALS).fetchOne().value1();
        });
        syncTableSequence(null, EhJournalConfigs.class, Tables.EH_JOURNAL_CONFIGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_JOURNAL_CONFIGS.ID.max()).from(Tables.EH_JOURNAL_CONFIGS).fetchOne().value1();
        });

        syncTableSequence(null, EhCategories.class, Tables.EH_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CATEGORIES.ID.max()).from(Tables.EH_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTasks.class, Tables.EH_PM_TASKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASKS.ID.max()).from(Tables.EH_PM_TASKS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskAttachments.class, Tables.EH_PM_TASK_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_PM_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskLogs.class, Tables.EH_PM_TASK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_LOGS.ID.max()).from(Tables.EH_PM_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskStatistics.class, Tables.EH_PM_TASK_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_STATISTICS.ID.max()).from(Tables.EH_PM_TASK_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhUserImpersonations.class, Tables.EH_USER_IMPERSONATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IMPERSONATIONS.ID.max()).from(Tables.EH_USER_IMPERSONATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMembers.class, Tables.EH_ORGANIZATION_MEMBERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBERS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBERS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatTaskLogs.class, Tables.EH_STAT_TASK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_TASK_LOGS.ID.max()).from(Tables.EH_STAT_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatOrders.class, Tables.EH_STAT_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_ORDERS.ID.max()).from(Tables.EH_STAT_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatRefunds.class, Tables.EH_STAT_REFUNDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_REFUNDS.ID.max()).from(Tables.EH_STAT_REFUNDS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatTransactions.class, Tables.EH_STAT_TRANSACTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_TRANSACTIONS.ID.max()).from(Tables.EH_STAT_TRANSACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatSettlements.class, Tables.EH_STAT_SETTLEMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_SETTLEMENTS.ID.max()).from(Tables.EH_STAT_SETTLEMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatServiceSettlementResults.class, Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ID.max()).from(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAlliances.class, Tables.EH_SERVICE_ALLIANCES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCES.ID.max()).from(Tables.EH_SERVICE_ALLIANCES).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceCategories.class, Tables.EH_SERVICE_ALLIANCE_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceAttachments.class, Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceSkipRule.class, Tables.EH_SERVICE_ALLIANCE_SKIP_RULE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SKIP_RULE.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SKIP_RULE).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhYzbDevices.class, Tables.EH_YZB_DEVICES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_YZB_DEVICES.ID.max()).from(Tables.EH_YZB_DEVICES).fetchOne().value1();
        });
        syncTableSequence(null, EhSearchTypes.class, Tables.EH_SEARCH_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SEARCH_TYPES.ID.max()).from(Tables.EH_SEARCH_TYPES).fetchOne().value1();
        });
        //审批相关表, add by tt, 160907
        syncTableSequence(null, EhApprovalDayActualTime.class, Tables.EH_APPROVAL_DAY_ACTUAL_TIME.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.ID.max()).from(Tables.EH_APPROVAL_DAY_ACTUAL_TIME).fetchOne().value1();
        });

        syncTableSequence(null, EhApprovalFlows.class, Tables.EH_APPROVAL_FLOWS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_FLOWS.ID.max()).from(Tables.EH_APPROVAL_FLOWS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalFlowLevels.class, Tables.EH_APPROVAL_FLOW_LEVELS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_FLOW_LEVELS.ID.max()).from(Tables.EH_APPROVAL_FLOW_LEVELS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRules.class, Tables.EH_APPROVAL_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RULES.ID.max()).from(Tables.EH_APPROVAL_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRuleFlowMap.class, Tables.EH_APPROVAL_RULE_FLOW_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RULE_FLOW_MAP.ID.max()).from(Tables.EH_APPROVAL_RULE_FLOW_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalCategories.class, Tables.EH_APPROVAL_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_CATEGORIES.ID.max()).from(Tables.EH_APPROVAL_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRequests.class, Tables.EH_APPROVAL_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_REQUESTS.ID.max()).from(Tables.EH_APPROVAL_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalAttachments.class, Tables.EH_APPROVAL_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_ATTACHMENTS.ID.max()).from(Tables.EH_APPROVAL_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalTimeRanges.class, Tables.EH_APPROVAL_TIME_RANGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_TIME_RANGES.ID.max()).from(Tables.EH_APPROVAL_TIME_RANGES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalOpRequests.class, Tables.EH_APPROVAL_OP_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_OP_REQUESTS.ID.max()).from(Tables.EH_APPROVAL_OP_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRangeStatistics.class, Tables.EH_APPROVAL_RANGE_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RANGE_STATISTICS.ID.max()).from(Tables.EH_APPROVAL_RANGE_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestTemplates.class, Tables.EH_REQUEST_TEMPLATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_TEMPLATES.ID.max()).from(Tables.EH_REQUEST_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestTemplatesNamespaceMapping.class, Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING.ID.max()).from(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestAttachments.class, Tables.EH_REQUEST_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_ATTACHMENTS.ID.max()).from(Tables.EH_REQUEST_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceRequests.class, Tables.EH_SERVICE_ALLIANCE_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceNotifyTargets.class, Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS).fetchOne().value1();
        });
        syncTableSequence(null, EhSettleRequests.class, Tables.EH_SETTLE_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SETTLE_REQUESTS.ID.max()).from(Tables.EH_SETTLE_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskTargets.class, Tables.EH_PM_TASK_TARGETS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_TARGETS.ID.max()).from(Tables.EH_PM_TASK_TARGETS).fetchOne().value1();
        });

        syncTableSequence(null, EhStatActiveUsers.class, Tables.EH_STAT_ACTIVE_USERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_ACTIVE_USERS.ID.max()).from(Tables.EH_STAT_ACTIVE_USERS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarningSettings.class, Tables.EH_WARNING_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WARNING_SETTINGS.ID.max()).from(Tables.EH_WARNING_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhDoorUserPermission.class, Tables.EH_DOOR_USER_PERMISSION.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_USER_PERMISSION.ID.max()).from(Tables.EH_DOOR_USER_PERMISSION).fetchOne().value1();
        });

        syncTableSequence(null, EhGroupSettings.class, Tables.EH_GROUP_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_SETTINGS.ID.max()).from(Tables.EH_GROUP_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhBroadcasts.class, Tables.EH_BROADCASTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BROADCASTS.ID.max()).from(Tables.EH_BROADCASTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceReservationRequests.class, Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhResourceCategories.class, Tables.EH_RESOURCE_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RESOURCE_CATEGORIES.ID.max()).from(Tables.EH_RESOURCE_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhResourceCategoryAssignments.class, Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.ID.max()).from(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationJobPositions.class, Tables.EH_ORGANIZATION_JOB_POSITIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_JOB_POSITIONS.ID.max()).from(Tables.EH_ORGANIZATION_JOB_POSITIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationJobPositionMaps.class, Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ID.max()).from(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModulePrivileges.class, Tables.EH_SERVICE_MODULE_PRIVILEGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_PRIVILEGES.ID.max()).from(Tables.EH_SERVICE_MODULE_PRIVILEGES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModuleAssignments.class, Tables.EH_SERVICE_MODULE_ASSIGNMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ID.max()).from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentStandardMap.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTemplates.class, Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTemplateItemMap.class, Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionItems.class, Tables.EH_EQUIPMENT_INSPECTION_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ITEMS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionItemResults.class, Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationAddresses.class, Tables.EH_ORGANIZATION_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESSES.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCars.class, Tables.EH_ORGANIZATION_OWNER_CARS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CARS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CARS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerOwnerCar.class, Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerAttachments.class, Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCarAttachments.class, Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCarAttachments.class, Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerBehaviors.class, Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerType.class, Tables.EH_ORGANIZATION_OWNER_TYPE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_TYPE.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_TYPE).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCardCategories.class, Tables.EH_PARKING_CARD_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CARD_CATEGORIES.ID.max()).from(Tables.EH_PARKING_CARD_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlows.class, Tables.EH_FLOWS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOWS.ID.max()).from(Tables.EH_FLOWS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowStats.class, Tables.EH_FLOW_STATS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_STATS.ID.max()).from(Tables.EH_FLOW_STATS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowNodes.class, Tables.EH_FLOW_NODES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_NODES.ID.max()).from(Tables.EH_FLOW_NODES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowButtons.class, Tables.EH_FLOW_BUTTONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_BUTTONS.ID.max()).from(Tables.EH_FLOW_BUTTONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowForms.class, Tables.EH_FLOW_FORMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_FORMS.ID.max()).from(Tables.EH_FLOW_FORMS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowActions.class, Tables.EH_FLOW_ACTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_ACTIONS.ID.max()).from(Tables.EH_FLOW_ACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowUserSelections.class, Tables.EH_FLOW_USER_SELECTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_USER_SELECTIONS.ID.max()).from(Tables.EH_FLOW_USER_SELECTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowCases.class, Tables.EH_FLOW_CASES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_CASES.ID.max()).from(Tables.EH_FLOW_CASES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowEventLogs.class, Tables.EH_FLOW_EVENT_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVENT_LOGS.ID.max()).from(Tables.EH_FLOW_EVENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowVariables.class, Tables.EH_FLOW_VARIABLES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_VARIABLES.ID.max()).from(Tables.EH_FLOW_VARIABLES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowEvaluates.class, Tables.EH_FLOW_EVALUATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVALUATES.ID.max()).from(Tables.EH_FLOW_EVALUATES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceApartmentRequests.class, Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeters.class, Tables.EH_ENERGY_METERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METERS.ID.max()).from(Tables.EH_ENERGY_METERS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterCategories.class, Tables.EH_ENERGY_METER_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_CATEGORIES.ID.max()).from(Tables.EH_ENERGY_METER_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterFormulas.class, Tables.EH_ENERGY_METER_FORMULAS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_FORMULAS.ID.max()).from(Tables.EH_ENERGY_METER_FORMULAS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterChangeLogs.class, Tables.EH_ENERGY_METER_CHANGE_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_CHANGE_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_CHANGE_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterReadingLogs.class, Tables.EH_ENERGY_METER_READING_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_READING_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_READING_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyDateStatistics.class, Tables.EH_ENERGY_DATE_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_DATE_STATISTICS.ID.max()).from(Tables.EH_ENERGY_DATE_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyCountStatistics.class, Tables.EH_ENERGY_COUNT_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_COUNT_STATISTICS.ID.max()).from(Tables.EH_ENERGY_COUNT_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyYoyStatistics.class, Tables.EH_ENERGY_YOY_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_YOY_STATISTICS.ID.max()).from(Tables.EH_ENERGY_YOY_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterSettingLogs.class, Tables.EH_ENERGY_METER_SETTING_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_SETTING_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_SETTING_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowScripts.class, Tables.EH_FLOW_SCRIPTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_SCRIPTS.ID.max()).from(Tables.EH_FLOW_SCRIPTS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowSubjects.class, Tables.EH_FLOW_SUBJECTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_SUBJECTS.ID.max()).from(Tables.EH_FLOW_SUBJECTS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowAttachments.class, Tables.EH_FLOW_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_ATTACHMENTS.ID.max()).from(Tables.EH_FLOW_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionCumulatives.class, Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionActives.class, Tables.EH_TERMINAL_APP_VERSION_ACTIVES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_ACTIVES).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionStatistics.class, Tables.EH_TERMINAL_APP_VERSION_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalDayStatistics.class, Tables.EH_TERMINAL_DAY_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_DAY_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_DAY_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalHourStatistics.class, Tables.EH_TERMINAL_HOUR_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_HOUR_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_HOUR_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalStatisticsTasks.class, Tables.EH_TERMINAL_STATISTICS_TASKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_STATISTICS_TASKS.ID.max()).from(Tables.EH_TERMINAL_STATISTICS_TASKS).fetchOne().value1();
        });

        syncTableSequence(null, EhAppVersion.class, Tables.EH_APP_VERSION.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APP_VERSION.ID.max()).from(Tables.EH_APP_VERSION).fetchOne().value1();
        });


        syncTableSequence(null, EhServiceHotlines.class, Tables.EH_SERVICE_HOTLINES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_HOTLINES.ID.max()).from(Tables.EH_SERVICE_HOTLINES).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskTargetStatistics.class, Tables.EH_PM_TASK_TARGET_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_TARGET_STATISTICS.ID.max()).from(Tables.EH_PM_TASK_TARGET_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhContracts.class, Tables.EH_CONTRACTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACTS.ID.max()).from(Tables.EH_CONTRACTS).fetchOne().value1();
        });

        syncTableSequence(null, EhContractBuildingMappings.class, Tables.EH_CONTRACT_BUILDING_MAPPINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ID.max()).from(Tables.EH_CONTRACT_BUILDING_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhAppNamespaceMappings.class, Tables.EH_APP_NAMESPACE_MAPPINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_APP_NAMESPACE_MAPPINGS.ID.max()).from(Tables.EH_APP_NAMESPACE_MAPPINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowTimeouts.class, Tables.EH_FLOW_TIMEOUTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_TIMEOUTS.ID.max()).from(Tables.EH_FLOW_TIMEOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCarSeries.class, Tables.EH_PARKING_CAR_SERIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CAR_SERIES.ID.max()).from(Tables.EH_PARKING_CAR_SERIES).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingAttachments.class, Tables.EH_PARKING_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_ATTACHMENTS.ID.max()).from(Tables.EH_PARKING_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingStatistics.class, Tables.EH_PARKING_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_STATISTICS.ID.max()).from(Tables.EH_PARKING_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingFlow.class, Tables.EH_PARKING_FLOW.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_FLOW.ID.max()).from(Tables.EH_PARKING_FLOW).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddressMappings.class, Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhUserBlacklists.class, Tables.EH_USER_BLACKLISTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_BLACKLISTS.ID.max()).from(Tables.EH_USER_BLACKLISTS).fetchOne().value1();
        });
        syncTableSequence(null, EhActivityAttachments.class, Tables.EH_ACTIVITY_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_ATTACHMENTS.ID.max()).from(Tables.EH_ACTIVITY_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhActivityGoods.class, Tables.EH_ACTIVITY_GOODS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_GOODS.ID.max()).from(Tables.EH_ACTIVITY_GOODS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowTimeouts.class, Tables.EH_FLOW_TIMEOUTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_TIMEOUTS.ID.max()).from(Tables.EH_FLOW_TIMEOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingClearanceOperators.class, Tables.EH_PARKING_CLEARANCE_OPERATORS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CLEARANCE_OPERATORS.ID.max()).from(Tables.EH_PARKING_CLEARANCE_OPERATORS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingClearanceLogs.class, Tables.EH_PARKING_CLEARANCE_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CLEARANCE_LOGS.ID.max()).from(Tables.EH_PARKING_CLEARANCE_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddressMappings.class, Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhLaunchAdvertisements.class, Tables.EH_LAUNCH_ADVERTISEMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LAUNCH_ADVERTISEMENTS.ID.max()).from(Tables.EH_LAUNCH_ADVERTISEMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhTechparkSyncdataBackup.class, Tables.EH_TECHPARK_SYNCDATA_BACKUP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TECHPARK_SYNCDATA_BACKUP.ID.max()).from(Tables.EH_TECHPARK_SYNCDATA_BACKUP).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowEvaluateItems.class, Tables.EH_FLOW_EVALUATE_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVALUATE_ITEMS.ID.max()).from(Tables.EH_FLOW_EVALUATE_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberLogs.class, Tables.EH_ORGANIZATION_MEMBER_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceJumpModule.class, Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceInvestRequests.class, Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralApprovals.class, Tables.EH_GENERAL_APPROVALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_APPROVALS.ID.max()).from(Tables.EH_GENERAL_APPROVALS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralForms.class, Tables.EH_GENERAL_FORMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_FORMS.ID.max()).from(Tables.EH_GENERAL_FORMS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralApprovalVals.class, Tables.EH_GENERAL_APPROVAL_VALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_APPROVAL_VALS.ID.max()).from(Tables.EH_GENERAL_APPROVAL_VALS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionStandardSpecificationMap.class, Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhBusinessPromotions.class, Tables.EH_BUSINESS_PROMOTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_PROMOTIONS.ID.max()).from(Tables.EH_BUSINESS_PROMOTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionTaskTemplates.class, Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhDoorAuthLogs.class, Tables.EH_DOOR_AUTH_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_AUTH_LOGS.ID.max()).from(Tables.EH_DOOR_AUTH_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSpecifications.class, Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhAssetBills.class, Tables.EH_ASSET_BILLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_BILLS.ID.max()).from(Tables.EH_ASSET_BILLS).fetchOne().value1();
        });
        syncTableSequence(null, EhAssetBillTemplateFields.class, Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.ID.max()).from(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionCategories.class, Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionStandardGroupMap.class, Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaires.class, Tables.EH_QUESTIONNAIRES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRES.ID.max()).from(Tables.EH_QUESTIONNAIRES).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireQuestions.class, Tables.EH_QUESTIONNAIRE_QUESTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_QUESTIONS.ID.max()).from(Tables.EH_QUESTIONNAIRE_QUESTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireOptions.class, Tables.EH_QUESTIONNAIRE_OPTIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_OPTIONS.ID.max()).from(Tables.EH_QUESTIONNAIRE_OPTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireAnswers.class, Tables.EH_QUESTIONNAIRE_ANSWERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.max()).from(Tables.EH_QUESTIONNAIRE_ANSWERS).fetchOne().value1();
        });
        syncTableSequence(null, EhOsObjects.class, Tables.EH_OS_OBJECTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OS_OBJECTS.ID.max()).from(Tables.EH_OS_OBJECTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOsObjectDownloadLogs.class, Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.ID.max()).from(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhDockingMappings.class, Tables.EH_DOCKING_MAPPINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DOCKING_MAPPINGS.ID.max()).from(Tables.EH_DOCKING_MAPPINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseIssuers.class, Tables.EH_LEASE_ISSUERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_ISSUERS.ID.max()).from(Tables.EH_LEASE_ISSUERS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseIssuerAddresses.class, Tables.EH_LEASE_ISSUER_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_ISSUER_ADDRESSES.ID.max()).from(Tables.EH_LEASE_ISSUER_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseConfigs.class, Tables.EH_LEASE_CONFIGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_CONFIGS.ID.max()).from(Tables.EH_LEASE_CONFIGS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceGolfRequests.class, Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceGymRequests.class, Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceServerRequests.class, Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceServerRequests.class, Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnterpriseOpRequestBuildings.class, Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.ID.max()).from(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOauth2Servers.class, Tables.EH_OAUTH2_SERVERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_SERVERS.ID.max()).from(Tables.EH_OAUTH2_SERVERS).fetchOne().value1();
        });
        syncTableSequence(null, EhOauth2ClientTokens.class, Tables.EH_OAUTH2_CLIENT_TOKENS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_CLIENT_TOKENS.ID.max()).from(Tables.EH_OAUTH2_CLIENT_TOKENS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterPriceConfig.class, Tables.EH_ENERGY_METER_PRICE_CONFIG.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_PRICE_CONFIG.ID.max()).from(Tables.EH_ENERGY_METER_PRICE_CONFIG).fetchOne().value1();
        });
        syncTableSequence(null, EhActivityVideo.class, Tables.EH_ACTIVITY_VIDEO.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_VIDEO.ID.max()).from(Tables.EH_ACTIVITY_VIDEO).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterDefaultSettings.class, Tables.EH_ENERGY_METER_DEFAULT_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS.ID.max()).from(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMonthStatistics.class, Tables.EH_ENERGY_MONTH_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_MONTH_STATISTICS.ID.max()).from(Tables.EH_ENERGY_MONTH_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhPreviews.class, Tables.EH_PREVIEWS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PREVIEWS.ID.max()).from(Tables.EH_PREVIEWS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressCompanies.class, Tables.EH_EXPRESS_COMPANIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_COMPANIES.ID.max()).from(Tables.EH_EXPRESS_COMPANIES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressUsers.class, Tables.EH_EXPRESS_USERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_USERS.ID.max()).from(Tables.EH_EXPRESS_USERS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressAddresses.class, Tables.EH_EXPRESS_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ADDRESSES.ID.max()).from(Tables.EH_EXPRESS_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressOrders.class, Tables.EH_EXPRESS_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ORDERS.ID.max()).from(Tables.EH_EXPRESS_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressQueryHistories.class, Tables.EH_EXPRESS_QUERY_HISTORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_QUERY_HISTORIES.ID.max()).from(Tables.EH_EXPRESS_QUERY_HISTORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressOrderLogs.class, Tables.EH_EXPRESS_ORDER_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ORDER_LOGS.ID.max()).from(Tables.EH_EXPRESS_ORDER_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhImportFileTasks.class, Tables.EH_IMPORT_FILE_TASKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_IMPORT_FILE_TASKS.ID.max()).from(Tables.EH_IMPORT_FILE_TASKS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskHistoryAddresses.class, Tables.EH_PM_TASK_HISTORY_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.max()).from(Tables.EH_PM_TASK_HISTORY_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberDetails.class, Tables.EH_ORGANIZATION_MEMBER_DETAILS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_DETAILS).fetchOne().value1();
        });
        syncTableSequence(null, EhUserNotificationSettings.class, Tables.EH_USER_NOTIFICATION_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_NOTIFICATION_SETTINGS.ID.max()).from(Tables.EH_USER_NOTIFICATION_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberEducations.class, Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberWorkExperiences.class, Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberInsurances.class, Tables.EH_ORGANIZATION_MEMBER_INSURANCES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_INSURANCES.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_INSURANCES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberContracts.class, Tables.EH_ORGANIZATION_MEMBER_CONTRACTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_CONTRACTS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_CONTRACTS).fetchOne().value1();
        });

        syncTableSequence(null, EhGroupMemberLogs.class, Tables.EH_GROUP_MEMBER_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_MEMBER_LOGS.ID.max()).from(Tables.EH_GROUP_MEMBER_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberProfileLogs.class, Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModuleAssignmentRelations.class, Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.ID.max()).from(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhAuthorizations.class, Tables.EH_AUTHORIZATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATIONS.ID.max()).from(Tables.EH_AUTHORIZATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhAuthorizationRelations.class, Tables.EH_AUTHORIZATION_RELATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATION_RELATIONS.ID.max()).from(Tables.EH_AUTHORIZATION_RELATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskHistoryAddresses.class, Tables.EH_PM_TASK_HISTORY_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.max()).from(Tables.EH_PM_TASK_HISTORY_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhRosterOrderSettings.class, Tables.EH_ROSTER_ORDER_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ROSTER_ORDER_SETTINGS.ID.max()).from(Tables.EH_ROSTER_ORDER_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhWarehouses.class, Tables.EH_WAREHOUSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSES.ID.max()).from(Tables.EH_WAREHOUSES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalents.class, Tables.EH_TALENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENTS.ID.max()).from(Tables.EH_TALENTS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalentCategories.class, Tables.EH_TALENT_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_CATEGORIES.ID.max()).from(Tables.EH_TALENT_CATEGORIES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalentQueryHistories.class, Tables.EH_TALENT_QUERY_HISTORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_QUERY_HISTORIES.ID.max()).from(Tables.EH_TALENT_QUERY_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseMaterials.class, Tables.EH_WAREHOUSE_MATERIALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_MATERIALS.ID.max()).from(Tables.EH_WAREHOUSE_MATERIALS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseMaterialCategories.class, Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.max()).from(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseStocks.class, Tables.EH_WAREHOUSE_STOCKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_STOCKS.ID.max()).from(Tables.EH_WAREHOUSE_STOCKS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseStockLogs.class, Tables.EH_WAREHOUSE_STOCK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_STOCK_LOGS.ID.max()).from(Tables.EH_WAREHOUSE_STOCK_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseRequests.class, Tables.EH_WAREHOUSE_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_REQUESTS.ID.max()).from(Tables.EH_WAREHOUSE_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseRequestMaterials.class, Tables.EH_WAREHOUSE_REQUEST_MATERIALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.ID.max()).from(Tables.EH_WAREHOUSE_REQUEST_MATERIALS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseUnits.class, Tables.EH_WAREHOUSE_UNITS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_UNITS.ID.max()).from(Tables.EH_WAREHOUSE_UNITS).fetchOne().value1();

        });

        syncTableSequence(null, EhSmsLogs.class, Tables.EH_SMS_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SMS_LOGS.ID.max()).from(Tables.EH_SMS_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSamples.class, Tables.EH_QUALITY_INSPECTION_SAMPLES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLES).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleGroupMap.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleCommunityMap.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleScoreStat.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleCommunitySpecificationStat.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT).fetchOne().value1();

        });
        syncTableSequence(null, EhQualityInspectionSpecificationItemResults.class, Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS).fetchOne().value1();

        });
        syncTableSequence(null, EhNewsCommentRule.class, Tables.EH_NEWS_COMMENT_RULE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS_COMMENT_RULE.ID.max()).from(Tables.EH_NEWS_COMMENT_RULE).fetchOne().value1();

        });
        syncTableSequence(null, EhNewsCommunities.class, Tables.EH_NEWS_COMMUNITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
                    return dbContext.select(Tables.EH_NEWS_COMMUNITIES.ID.max()).from(Tables.EH_NEWS_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhUserOrganizations.class, Tables.EH_USER_ORGANIZATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_ORGANIZATIONS.ID.max()).from(Tables.EH_USER_ORGANIZATIONS).fetchOne().value1();

        });

        syncTableSequence(null, EhRentalv2PriceRules.class, Tables.EH_RENTALV2_PRICE_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_PRICE_RULES.ID.max()).from(Tables.EH_RENTALV2_PRICE_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhCommunityApprove.class, Tables.EH_COMMUNITY_APPROVE.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_APPROVE.ID.max()).from(Tables.EH_COMMUNITY_APPROVE).fetchOne().value1();
        });

        syncTableSequence(null, EhCommunityApproveRequests.class, Tables.EH_COMMUNITY_APPROVE_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_APPROVE_REQUESTS.ID.max()).from(Tables.EH_COMMUNITY_APPROVE_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhTalentMessageSenders.class, Tables.EH_TALENT_MESSAGE_SENDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_MESSAGE_SENDERS.ID.max()).from(Tables.EH_TALENT_MESSAGE_SENDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhTalentRequests.class, Tables.EH_TALENT_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_REQUESTS.ID.max()).from(Tables.EH_TALENT_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhGeneralFormVals.class, Tables.EH_GENERAL_FORM_VALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_GENERAL_FORM_VALS.ID.max()).from(Tables.EH_GENERAL_FORM_VALS).fetchOne().value1();
        });
        syncTableSequence(null, EhYzxSmsLogs.class, Tables.EH_YZX_SMS_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_YZX_SMS_LOGS.ID.max()).from(Tables.EH_YZX_SMS_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerAddress.class, Tables.EH_ORGANIZATION_OWNER_ADDRESS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_ADDRESS).fetchOne().value1();
        });
        syncTableSequence(null, EhZjSyncdataBackup.class, Tables.EH_ZJ_SYNCDATA_BACKUP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ZJ_SYNCDATA_BACKUP.ID.max()).from(Tables.EH_ZJ_SYNCDATA_BACKUP).fetchOne().value1();
        });
        syncTableSequence(null, EhThirdpartConfigurations.class, Tables.EH_THIRDPART_CONFIGURATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_THIRDPART_CONFIGURATIONS.ID.max()).from(Tables.EH_THIRDPART_CONFIGURATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldGroups.class, Tables.EH_VAR_FIELD_GROUPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_GROUPS.ID.max()).from(Tables.EH_VAR_FIELD_GROUPS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFields.class, Tables.EH_VAR_FIELDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELDS.ID.max()).from(Tables.EH_VAR_FIELDS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldItems.class, Tables.EH_VAR_FIELD_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_ITEMS.ID.max()).from(Tables.EH_VAR_FIELD_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldGroupScopes.class, Tables.EH_VAR_FIELD_GROUP_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_GROUP_SCOPES.ID.max()).from(Tables.EH_VAR_FIELD_GROUP_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldScopes.class, Tables.EH_VAR_FIELD_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_SCOPES.ID.max()).from(Tables.EH_VAR_FIELD_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldItemScopes.class, Tables.EH_VAR_FIELD_ITEM_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_ITEM_SCOPES.ID.max()).from(Tables.EH_VAR_FIELD_ITEM_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhEnterpriseCustomers.class, Tables.EH_ENTERPRISE_CUSTOMERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CUSTOMERS.ID.max()).from(Tables.EH_ENTERPRISE_CUSTOMERS).fetchOne().value1();
        });
        syncTableSequence(null, EhContractAttachments.class, Tables.EH_CONTRACT_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_ATTACHMENTS.ID.max()).from(Tables.EH_CONTRACT_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhContractChargingItems.class, Tables.EH_CONTRACT_CHARGING_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_CHARGING_ITEMS.ID.max()).from(Tables.EH_CONTRACT_CHARGING_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhContractChargingItemAddresses.class, Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.ID.max()).from(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhContractParams.class, Tables.EH_CONTRACT_PARAMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_PARAMS.ID.max()).from(Tables.EH_CONTRACT_PARAMS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerTalents.class, Tables.EH_CUSTOMER_TALENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_TALENTS.ID.max()).from(Tables.EH_CUSTOMER_TALENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerTrademarks.class, Tables.EH_CUSTOMER_TRADEMARKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_TRADEMARKS.ID.max()).from(Tables.EH_CUSTOMER_TRADEMARKS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerPatents.class, Tables.EH_CUSTOMER_PATENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_PATENTS.ID.max()).from(Tables.EH_CUSTOMER_PATENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerApplyProjects.class, Tables.EH_CUSTOMER_APPLY_PROJECTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_APPLY_PROJECTS.ID.max()).from(Tables.EH_CUSTOMER_APPLY_PROJECTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerCommercials.class, Tables.EH_CUSTOMER_COMMERCIALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_COMMERCIALS.ID.max()).from(Tables.EH_CUSTOMER_COMMERCIALS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerInvestments.class, Tables.EH_CUSTOMER_INVESTMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_INVESTMENTS.ID.max()).from(Tables.EH_CUSTOMER_INVESTMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerEconomicIndicators.class, Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.ID.max()).from(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwners.class, Tables.EH_ORGANIZATION_OWNERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNERS.ID.max()).from(Tables.EH_ORGANIZATION_OWNERS).fetchOne().value1();
        });

        syncTableSequence(null, EhCommunityMapInfos.class, Tables.EH_COMMUNITY_MAP_INFOS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_MAP_INFOS.ID.max()).from(Tables.EH_COMMUNITY_MAP_INFOS).fetchOne().value1();
        });
        syncTableSequence(null, EhCommunityMapSearchTypes.class, Tables.EH_COMMUNITY_MAP_SEARCH_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES.ID.max()).from(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES).fetchOne().value1();
        });
        syncTableSequence(null, EhCommunityBuildingGeos.class, Tables.EH_COMMUNITY_BUILDING_GEOS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_BUILDING_GEOS.ID.max()).from(Tables.EH_COMMUNITY_BUILDING_GEOS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeasePromotionCommunities.class, Tables.EH_LEASE_PROMOTION_COMMUNITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTION_COMMUNITIES.ID.max()).from(Tables.EH_LEASE_PROMOTION_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseBuildings.class, Tables.EH_LEASE_BUILDINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
                    return dbContext.select(Tables.EH_LEASE_BUILDINGS.ID.max()).from(Tables.EH_LEASE_BUILDINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventDeviceLogs.class, Tables.EH_STAT_EVENT_DEVICE_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_DEVICE_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_DEVICE_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventUploadStrategies.class, Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES.ID.max()).from(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEvents.class, Tables.EH_STAT_EVENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENTS.ID.max()).from(Tables.EH_STAT_EVENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventParams.class, Tables.EH_STAT_EVENT_PARAMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PARAMS.ID.max()).from(Tables.EH_STAT_EVENT_PARAMS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventContentLogs.class, Tables.EH_STAT_EVENT_CONTENT_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_CONTENT_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_CONTENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventLogs.class, Tables.EH_STAT_EVENT_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventParamLogs.class, Tables.EH_STAT_EVENT_PARAM_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PARAM_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_PARAM_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventAppAttachmentLogs.class, Tables.EH_STAT_EVENT_APP_ATTACHMENT_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_APP_ATTACHMENT_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_APP_ATTACHMENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventPortalConfigs.class, Tables.EH_STAT_EVENT_PORTAL_CONFIGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.ID.max()).from(Tables.EH_STAT_EVENT_PORTAL_CONFIGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventPortalStatistics.class, Tables.EH_STAT_EVENT_PORTAL_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID.max()).from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventStatistics.class, Tables.EH_STAT_EVENT_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_STATISTICS.ID.max()).from(Tables.EH_STAT_EVENT_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventTaskLogs.class, Tables.EH_STAT_EVENT_TASK_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_TASK_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalItemGroups.class, Tables.EH_PORTAL_ITEM_GROUPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEM_GROUPS.ID.max()).from(Tables.EH_PORTAL_ITEM_GROUPS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalItems.class, Tables.EH_PORTAL_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEMS.ID.max()).from(Tables.EH_PORTAL_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalNavigationBars.class, Tables.EH_PORTAL_NAVIGATION_BARS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_NAVIGATION_BARS.ID.max()).from(Tables.EH_PORTAL_NAVIGATION_BARS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalLayouts.class, Tables.EH_PORTAL_LAYOUTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAYOUTS.ID.max()).from(Tables.EH_PORTAL_LAYOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalLaunchPadMappings.class, Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.ID.max()).from(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalLayouts.class, Tables.EH_PORTAL_LAYOUTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAYOUTS.ID.max()).from(Tables.EH_PORTAL_LAYOUTS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalItems.class, Tables.EH_PORTAL_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEMS.ID.max()).from(Tables.EH_PORTAL_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalItemGroups.class, Tables.EH_PORTAL_ITEM_GROUPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEM_GROUPS.ID.max()).from(Tables.EH_PORTAL_ITEM_GROUPS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalItemCategories.class, Tables.EH_PORTAL_ITEM_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEM_CATEGORIES.ID.max()).from(Tables.EH_PORTAL_ITEM_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalContentScopes.class, Tables.EH_PORTAL_CONTENT_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_CONTENT_SCOPES.ID.max()).from(Tables.EH_PORTAL_CONTENT_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalLayoutTemplates.class, Tables.EH_PORTAL_LAYOUT_TEMPLATES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAYOUT_TEMPLATES.ID.max()).from(Tables.EH_PORTAL_LAYOUT_TEMPLATES).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalNavigationBars.class, Tables.EH_PORTAL_NAVIGATION_BARS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_NAVIGATION_BARS.ID.max()).from(Tables.EH_PORTAL_NAVIGATION_BARS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalLaunchPadMappings.class, Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.ID.max()).from(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalPublishLogs.class, Tables.EH_PORTAL_PUBLISH_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_PUBLISH_LOGS.ID.max()).from(Tables.EH_PORTAL_PUBLISH_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModuleApps.class, Tables.EH_SERVICE_MODULE_APPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_APPS.ID.max()).from(Tables.EH_SERVICE_MODULE_APPS).fetchOne().value1();
        });

        syncTableSequence(null, EhItemServiceCategries.class, Tables.EH_ITEM_SERVICE_CATEGRIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ITEM_SERVICE_CATEGRIES.ID.max()).from(Tables.EH_ITEM_SERVICE_CATEGRIES).fetchOne().value1();
        });

        syncTableSequence(null, EhLaunchPadItems.class, Tables.EH_LAUNCH_PAD_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LAUNCH_PAD_ITEMS.ID.max()).from(Tables.EH_LAUNCH_PAD_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhLaunchPadLayouts.class, Tables.EH_LAUNCH_PAD_LAYOUTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LAUNCH_PAD_LAYOUTS.ID.max()).from(Tables.EH_LAUNCH_PAD_LAYOUTS).fetchOne().value1();
        });

        syncTableSequence(null, EhUserLaunchPadItems.class, Tables.EH_USER_LAUNCH_PAD_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_USER_LAUNCH_PAD_ITEMS.ID.max()).from(Tables.EH_USER_LAUNCH_PAD_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintEmails.class, Tables.EH_SIYIN_PRINT_EMAILS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_EMAILS.ID.max()).from(Tables.EH_SIYIN_PRINT_EMAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintOrders.class, Tables.EH_SIYIN_PRINT_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_ORDERS.ID.max()).from(Tables.EH_SIYIN_PRINT_ORDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintPrinters.class, Tables.EH_SIYIN_PRINT_PRINTERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_PRINTERS.ID.max()).from(Tables.EH_SIYIN_PRINT_PRINTERS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintRecords.class, Tables.EH_SIYIN_PRINT_RECORDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_RECORDS.ID.max()).from(Tables.EH_SIYIN_PRINT_RECORDS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintSettings.class, Tables.EH_SIYIN_PRINT_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_SETTINGS.ID.max()).from(Tables.EH_SIYIN_PRINT_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhActivityCategories.class, Tables.EH_ACTIVITY_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_CATEGORIES.ID.max()).from(Tables.EH_ACTIVITY_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowConditions.class, Tables.EH_FLOW_CONDITIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_CONDITIONS.ID.max()).from(Tables.EH_FLOW_CONDITIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowConditionExpressions.class, Tables.EH_FLOW_CONDITION_EXPRESSIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_CONDITION_EXPRESSIONS.ID.max()).from(Tables.EH_FLOW_CONDITION_EXPRESSIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowLanes.class, Tables.EH_FLOW_LANES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_LANES.ID.max()).from(Tables.EH_FLOW_LANES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowLinks.class, Tables.EH_FLOW_LINKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_LINKS.ID.max()).from(Tables.EH_FLOW_LINKS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowPredefinedParams.class, Tables.EH_FLOW_PREDEFINED_PARAMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_PREDEFINED_PARAMS.ID.max()).from(Tables.EH_FLOW_PREDEFINED_PARAMS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowBranches.class, Tables.EH_FLOW_BRANCHES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_BRANCHES.ID.max()).from(Tables.EH_FLOW_BRANCHES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressParamSettings.class, Tables.EH_EXPRESS_PARAM_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EXPRESS_PARAM_SETTINGS.ID.max()).from(Tables.EH_EXPRESS_PARAM_SETTINGS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhExpressCompanyBusinesses.class, Tables.EH_EXPRESS_COMPANY_BUSINESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EXPRESS_COMPANY_BUSINESSES.ID.max()).from(Tables.EH_EXPRESS_COMPANY_BUSINESSES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhIncubatorApplies.class, Tables.EH_INCUBATOR_APPLIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_INCUBATOR_APPLIES.ID.max()).from(Tables.EH_INCUBATOR_APPLIES).fetchOne().value1();
        });

        syncTableSequence(null, EhExpressHotlines.class, Tables.EH_EXPRESS_HOTLINES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_EXPRESS_HOTLINES.ID.max()).from(Tables.EH_EXPRESS_HOTLINES).fetchOne().value1();
        });

        syncTableSequence(null, EhPmNotifyConfigurations.class, Tables.EH_PM_NOTIFY_CONFIGURATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_NOTIFY_CONFIGURATIONS.ID.max()).from(Tables.EH_PM_NOTIFY_CONFIGURATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmNotifyRecords.class, Tables.EH_PM_NOTIFY_RECORDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_NOTIFY_RECORDS.ID.max()).from(Tables.EH_PM_NOTIFY_RECORDS).fetchOne().value1();
        });

        syncTableSequence(null, EhCustomerTrackings.class, Tables.EH_CUSTOMER_TRACKINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_TRACKINGS.ID.max()).from(Tables.EH_CUSTOMER_TRACKINGS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhCustomerTrackingPlans.class, Tables.EH_CUSTOMER_TRACKING_PLANS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_TRACKING_PLANS.ID.max()).from(Tables.EH_CUSTOMER_TRACKING_PLANS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhCustomerEvents.class, Tables.EH_CUSTOMER_EVENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_EVENTS.ID.max()).from(Tables.EH_CUSTOMER_EVENTS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTrackingNotifyLogs.class, Tables.EH_TRACKING_NOTIFY_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_TRACKING_NOTIFY_LOGS.ID.max()).from(Tables.EH_TRACKING_NOTIFY_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhCommunityMapShops.class, Tables.EH_COMMUNITY_MAP_SHOPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
                    return dbContext.select(Tables.EH_COMMUNITY_MAP_SHOPS.ID.max()).from(Tables.EH_COMMUNITY_MAP_SHOPS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingUserInvoices.class, Tables.EH_PARKING_USER_INVOICES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_USER_INVOICES.ID.max()).from(Tables.EH_PARKING_USER_INVOICES).fetchOne().value1();
        });

        syncTableSequence(null, EhParkingInvoiceTypes.class, Tables.EH_PARKING_INVOICE_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_INVOICE_TYPES.ID.max()).from(Tables.EH_PARKING_INVOICE_TYPES).fetchOne().value1();
        });

        syncTableSequence(null, EhParkingCardTypes.class, Tables.EH_PARKING_CARD_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CARD_TYPES.ID.max()).from(Tables.EH_PARKING_CARD_TYPES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceCommentAttachments.class, Tables.EH_SERVICE_ALLIANCE_COMMENT_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_SERVICE_ALLIANCE_COMMENT_ATTACHMENTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_COMMENT_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceComments.class, Tables.EH_SERVICE_ALLIANCE_COMMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
        	return dbContext.select(Tables.EH_SERVICE_ALLIANCE_COMMENTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_COMMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhRegions.class, Tables.EH_REGIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_REGIONS.ID.max()).from(Tables.EH_REGIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhWebMenuScopes.class, Tables.EH_WEB_MENU_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_WEB_MENU_SCOPES.ID.max()).from(Tables.EH_WEB_MENU_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhAssetPaymentOrderBills.class, Tables.EH_ASSET_PAYMENT_ORDER_BILLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_PAYMENT_ORDER_BILLS.ID.max())
                    .from(Tables.EH_ASSET_PAYMENT_ORDER_BILLS).fetchOne().value1();
        });

        syncTableSequence(null, EhAssetPaymentOrder.class, Tables.EH_ASSET_PAYMENT_ORDER.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_PAYMENT_ORDER.ID.max())
                    .from(Tables.EH_ASSET_PAYMENT_ORDER).fetchOne().value1();
        });

        syncTableSequence(null, EhPaymentUsers.class, Tables.EH_PAYMENT_USERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_USERS.ID.max())
                    .from(Tables.EH_PAYMENT_USERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentOrderRecords.class, Tables.EH_PAYMENT_ORDER_RECORDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_ORDER_RECORDS.ID.max())
                    .from(Tables.EH_PAYMENT_ORDER_RECORDS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentTypes.class, Tables.EH_PAYMENT_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_TYPES.ID.max())
                    .from(Tables.EH_PAYMENT_TYPES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentAccounts.class, Tables.EH_PAYMENT_ACCOUNTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_ACCOUNTS.ID.max())
                    .from(Tables.EH_PAYMENT_ACCOUNTS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentServiceConfigs.class, Tables.EH_PAYMENT_SERVICE_CONFIGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_SERVICE_CONFIGS.ID.max())
                    .from(Tables.EH_PAYMENT_SERVICE_CONFIGS).fetchOne().value1();
        });


		syncTableSequence(null, EhArchivesStickyContacts.class, Tables.EH_ARCHIVES_STICKY_CONTACTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ARCHIVES_STICKY_CONTACTS.ID.max()).from(Tables.EH_ARCHIVES_STICKY_CONTACTS).fetchOne().value1();
        });

        syncTableSequence(null, EhArchivesDismissEmployees.class, Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.ID.max()).from(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentChargingStandardsScopes.class, Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.ID.max())
                    .from(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentChargingItems.class, Tables.EH_PAYMENT_CHARGING_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CHARGING_ITEMS.ID.max())
                    .from(Tables.EH_PAYMENT_CHARGING_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentChargingItemScopes.class, Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.ID.max())
                    .from(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentBillGroups.class, Tables.EH_PAYMENT_BILL_GROUPS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_BILL_GROUPS.ID.max())
                    .from(Tables.EH_PAYMENT_BILL_GROUPS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentBillGroupsRules.class, Tables.EH_PAYMENT_BILL_GROUPS_RULES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.max())
                    .from(Tables.EH_PAYMENT_BILL_GROUPS_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentBills.class, Tables.EH_PAYMENT_BILLS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_BILLS.ID.max())
                    .from(Tables.EH_PAYMENT_BILLS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentBillItems.class, Tables.EH_PAYMENT_BILL_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_BILL_ITEMS.ID.max())
                    .from(Tables.EH_PAYMENT_BILL_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentContractReceiver.class, Tables.EH_PAYMENT_CONTRACT_RECEIVER.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CONTRACT_RECEIVER.ID.max())
                    .from(Tables.EH_PAYMENT_CONTRACT_RECEIVER).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentBillNoticeRecords.class, Tables.EH_PAYMENT_BILL_NOTICE_RECORDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_BILL_NOTICE_RECORDS.ID.max())
                    .from(Tables.EH_PAYMENT_BILL_NOTICE_RECORDS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentExemptionItems.class, Tables.EH_PAYMENT_EXEMPTION_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_EXEMPTION_ITEMS.ID.max())
                    .from(Tables.EH_PAYMENT_EXEMPTION_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentVariables.class, Tables.EH_PAYMENT_VARIABLES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_VARIABLES.ID.max())
                    .from(Tables.EH_PAYMENT_VARIABLES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentFormula.class, Tables.EH_PAYMENT_FORMULA.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_FORMULA.ID.max())
                    .from(Tables.EH_PAYMENT_FORMULA).fetchOne().value1();
        });

        syncTableSequence(null, EhNewsTag.class, Tables.EH_NEWS_TAG.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS_TAG.ID.max()).from(Tables.EH_NEWS_TAG).fetchOne().value1();
        });
        syncTableSequence(null, EhNewsTagVals.class, Tables.EH_NEWS_TAG_VALS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS_TAG_VALS.ID.max()).from(Tables.EH_NEWS_TAG_VALS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseProjects.class, Tables.EH_LEASE_PROJECTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROJECTS.ID.max()).from(Tables.EH_LEASE_PROJECTS).fetchOne().value1();
        });

        syncTableSequence(null, EhLeaseProjectCommunities.class, Tables.EH_LEASE_PROJECT_COMMUNITIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROJECT_COMMUNITIES.ID.max()).from(Tables.EH_LEASE_PROJECT_COMMUNITIES).fetchOne().value1();
        });


        syncTableSequence(null, EhParkingCarVerifications.class, Tables.EH_PARKING_CAR_VERIFICATIONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CAR_VERIFICATIONS.ID.max()).from(Tables.EH_PARKING_CAR_VERIFICATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireRanges.class, Tables.EH_QUESTIONNAIRE_RANGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_RANGES.ID.max()).from(Tables.EH_QUESTIONNAIRE_RANGES).fetchOne().value1();
        });

        syncTableSequence(null, EhForumCategories.class, Tables.EH_FORUM_CATEGORIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_CATEGORIES.ID.max()).from(Tables.EH_FORUM_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhInteractSettings.class, Tables.EH_INTERACT_SETTINGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
                    return dbContext.select(Tables.EH_INTERACT_SETTINGS.ID.max()).from(Tables.EH_INTERACT_SETTINGS).fetchOne().value1();
                });
        syncTableSequence(null, EhEnergyMeterAddresses.class, Tables.EH_ENERGY_METER_ADDRESSES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_ADDRESSES.ID.max()).from(Tables.EH_ENERGY_METER_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyPlans.class, Tables.EH_ENERGY_PLANS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_PLANS.ID.max()).from(Tables.EH_ENERGY_PLANS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyPlanGroupMap.class, Tables.EH_ENERGY_PLAN_GROUP_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_PLAN_GROUP_MAP.ID.max()).from(Tables.EH_ENERGY_PLAN_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyPlanMeterMap.class, Tables.EH_ENERGY_PLAN_METER_MAP.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_PLAN_METER_MAP.ID.max()).from(Tables.EH_ENERGY_PLAN_METER_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterTasks.class, Tables.EH_ENERGY_METER_TASKS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_TASKS.ID.max()).from(Tables.EH_ENERGY_METER_TASKS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterLogs.class, Tables.EH_ENERGY_METER_LOGS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhDefaultChargingItemProperties.class, Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.ID.max()).from(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES).fetchOne().value1();
        });
        syncTableSequence(null, EhDefaultChargingItems.class, Tables.EH_DEFAULT_CHARGING_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DEFAULT_CHARGING_ITEMS.ID.max()).from(Tables.EH_DEFAULT_CHARGING_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerEconomicIndicatorStatistics.class, Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.ID.max()).from(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhDoorAuthLevel.class, Tables.EH_DOOR_AUTH_LEVEL.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_AUTH_LEVEL.ID.max()).from(Tables.EH_DOOR_AUTH_LEVEL).fetchOne().value1();
        });

		syncTableSequence(null, EhGuildApplies.class, Tables.EH_GUILD_APPLIES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_GUILD_APPLIES.ID.max()).from(Tables.EH_GUILD_APPLIES).fetchOne().value1();
        });
        syncTableSequence(null, EhIndustryTypes.class, Tables.EH_INDUSTRY_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_INDUSTRY_TYPES.ID.max()).from(Tables.EH_INDUSTRY_TYPES).fetchOne().value1();
        });

        syncTableSequence(null, EhRentalv2PricePackages.class, Tables.EH_RENTALV2_PRICE_PACKAGES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_PRICE_PACKAGES.ID.max()).from(Tables.EH_RENTALV2_PRICE_PACKAGES).fetchOne().value1();
        });

        syncTableSequence(null, EhRelocationRequests.class, Tables.EH_RELOCATION_REQUESTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RELOCATION_REQUESTS.ID.max()).from(Tables.EH_RELOCATION_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhRelocationRequestItems.class, Tables.EH_RELOCATION_REQUEST_ITEMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RELOCATION_REQUEST_ITEMS.ID.max()).from(Tables.EH_RELOCATION_REQUEST_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhRelocationRequestAttachments.class, Tables.EH_RELOCATION_REQUEST_ATTACHMENTS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_RELOCATION_REQUEST_ATTACHMENTS.ID.max()).from(Tables.EH_RELOCATION_REQUEST_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhBanners.class, Tables.EH_BANNERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_BANNERS.ID.max()).from(Tables.EH_BANNERS).fetchOne().value1();
        });
		
		syncTableSequence(null, EhPaymentWithdrawOrders.class, Tables.EH_PAYMENT_WITHDRAW_ORDERS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_WITHDRAW_ORDERS.ID.max()).from(Tables.EH_PAYMENT_WITHDRAW_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhMeWebMenus.class, Tables.EH_ME_WEB_MENUS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_ME_WEB_MENUS.ID.max()).from(Tables.EH_ME_WEB_MENUS).fetchOne().value1();
        });

        syncTableSequence(null, EhForumServiceTypes.class, Tables.EH_FORUM_SERVICE_TYPES.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_SERVICE_TYPES.ID.max()).from(Tables.EH_FORUM_SERVICE_TYPES).fetchOne().value1();
        });

        syncTableSequence(null, EhAuthorizationThirdPartyRecords.class, Tables.EH_AUTHORIZATION_THIRD_PARTY_RECORDS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATION_THIRD_PARTY_RECORDS.ID.max()).from(Tables.EH_AUTHORIZATION_THIRD_PARTY_RECORDS).fetchOne().value1();
        });
        syncTableSequence(null, EhAuthorizationThirdPartyForms.class, Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS.ID.max()).from(Tables.EH_AUTHORIZATION_THIRD_PARTY_FORMS).fetchOne().value1();
        });
        syncTableSequence(null, EhAuthorizationThirdPartyButtons.class, Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS.getName(), DEFAULT_MAX_ID, (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS.ID.max()).from(Tables.EH_AUTHORIZATION_THIRD_PARTY_BUTTONS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmNotifyLogs.class, Tables.EH_PM_NOTIFY_LOGS.getName(), DEFAULT_MAX_ID + 193460427L, (dbContext) -> {
            return dbContext.select(Tables.EH_PM_NOTIFY_LOGS.ID.max()).from(Tables.EH_PM_NOTIFY_LOGS).fetchOne().value1();
        });

        // cell的id从资源的cellendid取
        syncTableSequence(null, EhRentalv2Cells.class, Tables.EH_RENTALV2_CELLS.getName(), DEFAULT_MAX_ID + 862484218L, (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_PRICE_RULES.CELL_END_ID.max()).from(Tables.EH_RENTALV2_PRICE_RULES).fetchOne().value1();
        });

        // syncTableSequence(null, EhMessageRecords.class, Tables.EH_MESSAGE_RECORDS.getName(), DEFAULT_MAX_ID + 20151290L, (dbContext) -> {
        //     return dbContext.select(Tables.EH_MESSAGE_RECORDS.ID.max()).from(Tables.EH_MESSAGE_RECORDS).fetchOne().value1();
        // });

        syncTableSequence(null, EhServiceModules.class, Tables.EH_SERVICE_MODULES.getName(), DEFAULT_MAX_ID + 10800001L, (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULES.ID.max()).from(Tables.EH_SERVICE_MODULES).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclPrivileges.class, com.everhomes.schema.Tables.EH_ACL_PRIVILEGES.getName(), DEFAULT_MAX_ID + 4920049200L, (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_PRIVILEGES.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_PRIVILEGES).fetchOne().value1();
        });

        syncTableSequence(null, EhWebMenus.class, Tables.EH_WEB_MENUS.getName(), DEFAULT_MAX_ID + 76010000L, (dbContext) -> {
            return dbContext.select(Tables.EH_WEB_MENUS.ID.max()).from(Tables.EH_WEB_MENUS).fetchOne().value1();
        });

        syncTableSequence(EhAddresses.class, EhAddresses.class, Tables.EH_ADDRESSES.getName(), DEFAULT_MAX_ID + 239825274387476000L, (dbContext) -> {
            return dbContext.select(Tables.EH_ADDRESSES.ID.max()).from(Tables.EH_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhCommunities.class, Tables.EH_COMMUNITIES.getName(), DEFAULT_MAX_ID + 240111044332061000L, (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITIES.ID.max()).from(Tables.EH_COMMUNITIES).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhCommunityGeopoints.class, Tables.EH_COMMUNITY_GEOPOINTS.getName(), DEFAULT_MAX_ID + 240111044331094000L, (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_GEOPOINTS.ID.max()).from(Tables.EH_COMMUNITY_GEOPOINTS).fetchOne().value1();
        });

        //
        // 以后不用在这里加重复的代码了，已经做成自动获取所有表去同步id了，如果有特殊的表，可以写在这里.
        //
    }

    private void syncTableSequence(Class keytableCls, Class pojoClass, String tableName, Long defaultVal, SequenceQueryCallback callback) {
        excludeTableNames.add(tableName);
        doSyncTableSequence(keytableCls, pojoClass, tableName, defaultVal, callback);
    }

    private void doSyncTableSequence(Class keytableCls, Class pojoClass, String tableName, Long defaultVal, SequenceQueryCallback callback) {
        AccessSpec spec;
        if(keytableCls == null) {
            spec = AccessSpec.readOnly();
        } else {
            spec = AccessSpec.readOnlyWith(keytableCls);
        }

        Long result[] = new Long[1];
        result[0] = defaultVal;
        dbProvider.mapReduce(spec, null, (dbContext, contextObj) -> {
            //Long max = dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();
            Long max = callback.maxSequence(dbContext);
            if(max != null && max > result[0]) {
                result[0] = determineMax(tableName, dbContext, max);
            }
            return true;
        });
        String key = NameMapper.getSequenceDomainFromTablePojo(pojoClass);
        long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
        sequenceProvider.resetSequence(key, result[0] + 1);
        long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sync table sequence, tableName=" + tableName + ", key=" + key + ", newSequence=" + (result[0] + 1)
                + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
        }
    }

    private Long determineMax(String tableName, DSLContext dbContext, Long max) {
        boolean isStandard = configurationProvider.getBooleanValue("server.standard.flag", false);

        Long determinedMax;
        // 标准版
        if (isStandard) {
            determinedMax = max;
        } else {
            Long stdDefaultId = STD_DEFAULT_MAX_ID;
            // id 较大的表处理
            if (otherTableNames.containsKey(tableName)) {
                stdDefaultId = otherTableNames.get(tableName);
            }

            // 基线从数据库查出来的 id > 500000000L
            if (max >= stdDefaultId) {
                Field<Long> id = DSL.fieldByName(DSL.getDataType(Long.class), "id");
                determinedMax = dbContext.select(id.max()).from(DSL.table(tableName)).where(id.lt(stdDefaultId)).fetchOne().value1();
            } else {
                // 使用数据库里的值
                determinedMax = max;
            }
        }
        return determinedMax;
    }

    private void syncUserAccountName() {
        int pageCount = 1000;
        int pageNum = 1;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<User> userList = null;
        long maxAccountSequence = DEFAULT_MAX_ID + 999437843496L;
        long temp = 0;
        long startTime = 0;
        long endTime = 0;
        do {
            startTime = System.currentTimeMillis();
            userList = userProvider.queryUsers(locator, pageCount, null);
            for(User user : userList) {
                try {
                    temp = Long.parseLong(user.getAccountName());
                    if(temp > maxAccountSequence) {
                        maxAccountSequence = temp;
                    }
                } catch(Exception e) {
                    // no need to log and do nothing
                }
            }
            endTime = System.currentTimeMillis();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sync user account name sequence, pageNum=" + pageNum + ", pageCount=" + pageCount
                    + ", dbSize=" + userList.size() + ", elapse=" + (endTime - startTime));
            }
            pageNum++;
        } while(userList.size() > 0);

        if(maxAccountSequence > 0) {
            String key = "usr";
            long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
            sequenceProvider.resetSequence(key, maxAccountSequence + 1);
            long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sync user account name sequence, key=" + key + ", newSequence=" + (maxAccountSequence + 1)
                    + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
            }
        }
    }

    private void syncAuthorizationControlId() {
        Long id = DEFAULT_MAX_ID;

        Long maxAuthorizationControlId = this.authorizationProvider.getMaxControlIdInAuthorizations();
        if (maxAuthorizationControlId != null && maxAuthorizationControlId > id) {
            id = maxAuthorizationControlId;
        }

        String key = "authControlId";
        long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
        sequenceProvider.resetSequence(key, id + 1);
        long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("maxAuthorizationControlId sequence, key=" + key + ", newSequence=" + (id + 1)
                    + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
        }
    }

    private void syncActiveAppId() {
        Long id = DEFAULT_MAX_ID;

        Long maxSyncActiveAppId = this.serviceModuleProvider.getMaxActiveAppId();
        if (maxSyncActiveAppId != null && maxSyncActiveAppId > id) {
            id = maxSyncActiveAppId;
        }

        String key = "activeAppId";
        long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
        sequenceProvider.resetSequence(key, id + 1);
        long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("maxSyncActiveAppId sequence, key=" + key + ", newSequence=" + (id + 1)
                    + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
        }
    }

    private void syncMessageIndexId() {
        long maxMessageIndexId = 0;
        maxMessageIndexId = this.messageProvider.getMaxMessageIndexId();

        if (maxMessageIndexId > 0) {
            String key = "messageIndexId";
            long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
            sequenceProvider.resetSequence(key, maxMessageIndexId + 1);
            long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("syncMessageIndexId sequence, key=" + key + ", newSequence=" + (maxMessageIndexId + 1)
                        + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
            }
        }
    }

    @Override
    public GetSequenceDTO getSequence(GetSequenceCommand cmd) {
        long startSequence = DEFAULT_MAX_ID;
        
        long blockSize = (cmd.getBlockSize() == null ? 0L : cmd.getBlockSize());
        if(blockSize <= 1) {
            startSequence = sequenceProvider.getNextSequence(cmd.getSequenceDomain());
            blockSize = 1;
        } else {
            startSequence = sequenceProvider.getNextSequenceBlock(cmd.getSequenceDomain(), blockSize);
        }
        
        GetSequenceDTO dto = new GetSequenceDTO();
        dto.setSequenceDomain(cmd.getSequenceDomain());
        dto.setStartSequence(startSequence);
        dto.setBlockSize(blockSize);
        
        return dto;
    }
}
