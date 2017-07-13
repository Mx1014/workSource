// @formatter:off
package com.everhomes.sequence;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.schema.tables.pojos.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SequenceServiceImpl implements SequenceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceServiceImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public void syncSequence() {
        syncTableSequence(EhAcls.class, EhAcls.class, com.everhomes.schema.Tables.EH_ACLS.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACLS.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACLS).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclPrivileges.class, com.everhomes.schema.Tables.EH_ACL_PRIVILEGES.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_PRIVILEGES.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_PRIVILEGES).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclRoles.class, com.everhomes.schema.Tables.EH_ACL_ROLES.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_ROLES.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_ROLES).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclRoleAssignments.class, com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServerShardMap.class, com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.getName(), (dbContext) -> {
            Integer max = dbContext.select(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.ID.max())
                    .from(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP).fetchOne().value1();
            Long lmax = null;
            if (max != null) {
                lmax = Long.valueOf(max.longValue());
            }
            return lmax;
        });

        syncTableSequence(null, EhContentShardMap.class, com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.ID.max())
                    .from(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUsers.class, Tables.EH_USERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();
        });

        // user account is a special field, it default to be number stype, but it can be changed to any character only if they are unique in db
        syncUserAccountName();

        syncTableSequence(EhUsers.class, EhUserGroups.class, Tables.EH_USER_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_GROUPS.ID.max()).from(Tables.EH_USER_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserInvitations.class, Tables.EH_USER_INVITATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_INVITATIONS.ID.max()).from(Tables.EH_USER_INVITATIONS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserIdentifiers.class, Tables.EH_USER_IDENTIFIERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IDENTIFIERS.ID.max()).from(Tables.EH_USER_IDENTIFIERS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserInvitationRoster.class, Tables.EH_USER_INVITATION_ROSTER.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_INVITATION_ROSTER.ID.max()).from(Tables.EH_USER_INVITATION_ROSTER).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserPosts.class, Tables.EH_USER_POSTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_POSTS.ID.max()).from(Tables.EH_USER_POSTS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserLikes.class, Tables.EH_USER_LIKES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_LIKES.ID.max()).from(Tables.EH_USER_LIKES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserFavorites.class, Tables.EH_USER_FAVORITES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_FAVORITES.ID.max()).from(Tables.EH_USER_FAVORITES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserProfiles.class, Tables.EH_USER_PROFILES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_PROFILES.ID.max()).from(Tables.EH_USER_PROFILES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserServiceAddresses.class, Tables.EH_USER_SERVICE_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_SERVICE_ADDRESSES.ID.max()).from(Tables.EH_USER_SERVICE_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserCommunities.class, Tables.EH_USER_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_COMMUNITIES.ID.max()).from(Tables.EH_USER_COMMUNITIES).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForums.class, Tables.EH_FORUMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUMS.ID.max()).from(Tables.EH_FORUMS).fetchOne().value1();
        });

        syncTableSequence(EhAddresses.class, EhAddresses.class, Tables.EH_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ADDRESSES.ID.max()).from(Tables.EH_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhContentServerResources.class, Tables.EH_CONTENT_SERVER_RESOURCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTENT_SERVER_RESOURCES.ID.max())
                    .from(Tables.EH_CONTENT_SERVER_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroups.class, Tables.EH_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUPS.ID.max()).from(Tables.EH_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroupMembers.class, Tables.EH_GROUP_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_MEMBERS.ID.max()).from(Tables.EH_GROUP_MEMBERS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroupOpRequests.class, Tables.EH_GROUP_OP_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_OP_REQUESTS.ID.max()).from(Tables.EH_GROUP_OP_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumPosts.class, Tables.EH_FORUM_POSTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_POSTS.ID.max()).from(Tables.EH_FORUM_POSTS).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumAssignedScopes.class, Tables.EH_FORUM_ASSIGNED_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_FORUM_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumAttachments.class, Tables.EH_FORUM_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_ATTACHMENTS.ID.max()).from(Tables.EH_FORUM_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhCommunities.class, Tables.EH_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITIES.ID.max()).from(Tables.EH_COMMUNITIES).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhCommunityGeopoints.class, Tables.EH_COMMUNITY_GEOPOINTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_GEOPOINTS.ID.max()).from(Tables.EH_COMMUNITY_GEOPOINTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhNearbyCommunityMap.class, Tables.EH_NEARBY_COMMUNITY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NEARBY_COMMUNITY_MAP.ID.max()).from(Tables.EH_NEARBY_COMMUNITY_MAP).fetchOne().value1();
        });

        syncTableSequence(EhActivities.class, EhActivities.class, Tables.EH_ACTIVITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITIES.ID.max()).from(Tables.EH_ACTIVITIES).fetchOne().value1();
        });

        syncTableSequence(EhActivities.class, EhActivityRoster.class, Tables.EH_ACTIVITY_ROSTER.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_ROSTER.ID.max()).from(Tables.EH_ACTIVITY_ROSTER).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPolls.class, Tables.EH_POLLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_POLLS.ID.max()).from(Tables.EH_POLLS).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPollItems.class, Tables.EH_POLL_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_POLL_ITEMS.ID.max()).from(Tables.EH_POLL_ITEMS).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPollVotes.class, Tables.EH_POLL_VOTES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_POLL_VOTES.ID.max()).from(Tables.EH_POLL_VOTES).fetchOne().value1();
        });

        syncTableSequence(null, EhQrcodes.class, Tables.EH_QRCODES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QRCODES.ID.max()).from(Tables.EH_QRCODES).fetchOne().value1();
        });

        syncTableSequence(null, EhOauth2Codes.class, Tables.EH_OAUTH2_CODES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_CODES.ID.max()).from(Tables.EH_OAUTH2_CODES).fetchOne().value1();
        });

        syncTableSequence(null, EhOauth2Tokens.class, Tables.EH_OAUTH2_TOKENS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_TOKENS.ID.max()).from(Tables.EH_OAUTH2_TOKENS).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionRealm.class, Tables.EH_VERSION_REALM.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_REALM.ID.max()).from(Tables.EH_VERSION_REALM).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionUpgradeRules.class, Tables.EH_VERSION_UPGRADE_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_UPGRADE_RULES.ID.max()).from(Tables.EH_VERSION_UPGRADE_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionUrls.class, Tables.EH_VERSION_URLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_URLS.ID.max()).from(Tables.EH_VERSION_URLS).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionedContent.class, Tables.EH_VERSIONED_CONTENT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSIONED_CONTENT.ID.max()).from(Tables.EH_VERSIONED_CONTENT).fetchOne().value1();
        });

        syncTableSequence(null, EhCooperationRequests.class, Tables.EH_COOPERATION_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COOPERATION_REQUESTS.ID.max()).from(Tables.EH_COOPERATION_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinesses.class, Tables.EH_BUSINESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESSES.ID.max()).from(Tables.EH_BUSINESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessVisibleScopes.class, Tables.EH_BUSINESS_VISIBLE_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_VISIBLE_SCOPES.ID.max()).from(Tables.EH_BUSINESS_VISIBLE_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessAssignedScopes.class, Tables.EH_BUSINESS_ASSIGNED_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_BUSINESS_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessCategories.class, Tables.EH_BUSINESS_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_CATEGORIES.ID.max()).from(Tables.EH_BUSINESS_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPushMessageResults.class, Tables.EH_PUSH_MESSAGE_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUSH_MESSAGE_RESULTS.ID.max()).from(Tables.EH_PUSH_MESSAGE_RESULTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContacts.class, Tables.EH_ENTERPRISE_CONTACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACTS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactEntries.class, Tables.EH_ENTERPRISE_CONTACT_ENTRIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_ENTRIES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactGroups.class, Tables.EH_ENTERPRISE_CONTACT_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactGroupMembers.class, Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchDayLogs.class, Tables.EH_PUNCH_DAY_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_DAY_LOGS.ID.max()).from(Tables.EH_PUNCH_DAY_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchExceptionApprovals.class, Tables.EH_PUNCH_EXCEPTION_APPROVALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_APPROVALS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchExceptionRequests.class, Tables.EH_PUNCH_EXCEPTION_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchGeopoints.class, Tables.EH_PUNCH_GEOPOINTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_GEOPOINTS.ID.max()).from(Tables.EH_PUNCH_GEOPOINTS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchLogs.class, Tables.EH_PUNCH_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_LOGS.ID.max()).from(Tables.EH_PUNCH_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchRules.class, Tables.EH_PUNCH_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_RULES.ID.max()).from(Tables.EH_PUNCH_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWorkday.class, Tables.EH_PUNCH_WORKDAY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WORKDAY.ID.max()).from(Tables.EH_PUNCH_WORKDAY).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchTimeRules.class, Tables.EH_PUNCH_TIME_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_TIME_RULES.ID.max()).from(Tables.EH_PUNCH_TIME_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhPunchLocationRules.class, Tables.EH_PUNCH_LOCATION_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_LOCATION_RULES.ID.max()).from(Tables.EH_PUNCH_LOCATION_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWifis.class, Tables.EH_PUNCH_WIFIS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WIFIS.ID.max()).from(Tables.EH_PUNCH_WIFIS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWifiRules.class, Tables.EH_PUNCH_WIFI_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WIFI_RULES.ID.max()).from(Tables.EH_PUNCH_WIFI_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchHolidays.class, Tables.EH_PUNCH_HOLIDAYS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_HOLIDAYS.ID.max()).from(Tables.EH_PUNCH_HOLIDAYS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWorkdayRules.class, Tables.EH_PUNCH_WORKDAY_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WORKDAY_RULES.ID.max()).from(Tables.EH_PUNCH_WORKDAY_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchRuleOwnerMap.class, Tables.EH_PUNCH_RULE_OWNER_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.max()).from(Tables.EH_PUNCH_RULE_OWNER_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchStatistics.class, Tables.EH_PUNCH_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_STATISTICS.ID.max()).from(Tables.EH_PUNCH_STATISTICS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchSchedulings.class, Tables.EH_PUNCH_SCHEDULINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_SCHEDULINGS.ID.max()).from(Tables.EH_PUNCH_SCHEDULINGS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalBillAttachments.class, Tables.EH_RENTAL_BILL_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILL_ATTACHMENTS.ID.max()).from(Tables.EH_RENTAL_BILL_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalBills.class, Tables.EH_RENTAL_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILLS.ID.max()).from(Tables.EH_RENTAL_BILLS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalItemsBills.class, Tables.EH_RENTAL_ITEMS_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_ITEMS_BILLS.ID.max()).from(Tables.EH_RENTAL_ITEMS_BILLS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalRules.class, Tables.EH_RENTAL_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_RULES.ID.max()).from(Tables.EH_RENTAL_RULES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSiteItems.class, Tables.EH_RENTAL_SITE_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITE_ITEMS.ID.max()).from(Tables.EH_RENTAL_SITE_ITEMS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSiteRules.class, Tables.EH_RENTAL_SITE_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITE_RULES.ID.max()).from(Tables.EH_RENTAL_SITE_RULES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSites.class, Tables.EH_RENTAL_SITES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITES.ID.max()).from(Tables.EH_RENTAL_SITES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSitesBills.class, Tables.EH_RENTAL_SITES_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITES_BILLS.ID.max()).from(Tables.EH_RENTAL_SITES_BILLS).fetchOne().value1();
        });


        syncTableSequence(EhGroups.class, EhRentalBillPaybillMap.class, Tables.EH_RENTAL_BILL_PAYBILL_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILL_PAYBILL_MAP.ID.max()).from(Tables.EH_RENTAL_BILL_PAYBILL_MAP).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2DefaultRules.class, Tables.EH_RENTALV2_DEFAULT_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_DEFAULT_RULES.ID.max()).from(Tables.EH_RENTALV2_DEFAULT_RULES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2TimeInterval.class, Tables.EH_RENTALV2_TIME_INTERVAL.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_TIME_INTERVAL.ID.max()).from(Tables.EH_RENTALV2_TIME_INTERVAL).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2CloseDates.class, Tables.EH_RENTALV2_CLOSE_DATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_CLOSE_DATES.ID.max()).from(Tables.EH_RENTALV2_CLOSE_DATES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ConfigAttachments.class, Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ID.max()).from(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceRanges.class, Tables.EH_RENTALV2_RESOURCE_RANGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_RANGES.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_RANGES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourcePics.class, Tables.EH_RENTALV2_RESOURCE_PICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_PICS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_PICS).fetchOne().value1();
        });


        syncTableSequence(EhOrganizations.class, EhRentalv2OrderAttachments.class, Tables.EH_RENTALV2_ORDER_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDER_ATTACHMENTS.ID.max()).from(Tables.EH_RENTALV2_ORDER_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2Orders.class, Tables.EH_RENTALV2_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDERS.ID.max()).from(Tables.EH_RENTALV2_ORDERS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceNumbers.class, Tables.EH_RENTALV2_RESOURCE_NUMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_NUMBERS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_NUMBERS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2Items.class, Tables.EH_RENTALV2_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ITEMS.ID.max()).from(Tables.EH_RENTALV2_ITEMS).fetchOne().value1();
        });
        // cell的id从资源的cellendid取
        syncTableSequence(EhOrganizations.class, EhRentalv2Cells.class, Tables.EH_RENTALV2_CELLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCES.CELL_END_ID.max()).from(Tables.EH_RENTALV2_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2Resources.class, Tables.EH_RENTALV2_RESOURCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCES.ID.max()).from(Tables.EH_RENTALV2_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2RefundOrders.class, Tables.EH_RENTALV2_REFUND_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_REFUND_ORDERS.ID.max()).from(Tables.EH_RENTALV2_REFUND_ORDERS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceOrders.class, Tables.EH_RENTALV2_RESOURCE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_ORDERS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_ORDERS).fetchOne().value1();
        });


        syncTableSequence(EhOrganizations.class, EhRentalv2OrderPayorderMap.class, Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ID.max()).from(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceTypes.class, Tables.EH_RENTALV2_RESOURCE_TYPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_TYPES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhRechargeInfo.class, Tables.EH_RECHARGE_INFO.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RECHARGE_INFO.ID.max()).from(Tables.EH_RECHARGE_INFO).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseCommunityMap.class, Tables.EH_ENTERPRISE_COMMUNITY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.max()).from(Tables.EH_ENTERPRISE_COMMUNITY_MAP).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseAddresses.class, Tables.EH_ENTERPRISE_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_ADDRESSES.ID.max()).from(Tables.EH_ENTERPRISE_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseAttachments.class, Tables.EH_ENTERPRISE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_ATTACHMENTS.ID.max()).from(Tables.EH_ENTERPRISE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhBuildings.class, Tables.EH_BUILDINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUILDINGS.ID.max()).from(Tables.EH_BUILDINGS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhBuildingAttachments.class, Tables.EH_BUILDING_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUILDING_ATTACHMENTS.ID.max()).from(Tables.EH_BUILDING_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhParkCharge.class, Tables.EH_PARK_CHARGE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARK_CHARGE.ID.max()).from(Tables.EH_PARK_CHARGE).fetchOne().value1();
        });

        syncTableSequence(null, EhParkApplyCard.class, Tables.EH_PARK_APPLY_CARD.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARK_APPLY_CARD.ID.max()).from(Tables.EH_PARK_APPLY_CARD).fetchOne().value1();
        });

        syncTableSequence(null, EhRechargeInfo.class, Tables.EH_RECHARGE_INFO.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RECHARGE_INFO.ID.max()).from(Tables.EH_RECHARGE_INFO).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizations.class, Tables.EH_ORGANIZATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATIONS.ID.max()).from(Tables.EH_ORGANIZATIONS).fetchOne().value1();
        });


        syncTableSequence(null, EhYellowPages.class, Tables.EH_YELLOW_PAGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_YELLOW_PAGES.ID.max()).from(Tables.EH_YELLOW_PAGES).fetchOne().value1();
        });

        syncTableSequence(null, EhYellowPageAttachments.class, Tables.EH_YELLOW_PAGE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_YELLOW_PAGE_ATTACHMENTS.ID.max()).from(Tables.EH_YELLOW_PAGE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhNamespaceResources.class, Tables.EH_NAMESPACE_RESOURCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NAMESPACE_RESOURCES.ID.max()).from(Tables.EH_NAMESPACE_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseDetails.class, Tables.EH_ENTERPRISE_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_DETAILS.ID.max()).from(Tables.EH_ENTERPRISE_DETAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnterpriseOpRequests.class, Tables.EH_ENTERPRISE_OP_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.max()).from(Tables.EH_ENTERPRISE_OP_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhLeasePromotions.class, Tables.EH_LEASE_PROMOTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTIONS.ID.max()).from(Tables.EH_LEASE_PROMOTIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhLeasePromotionAttachments.class, Tables.EH_LEASE_PROMOTION_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.ID.max()).from(Tables.EH_LEASE_PROMOTION_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserGroupHistories.class, Tables.EH_USER_GROUP_HISTORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_GROUP_HISTORIES.ID.max()).from(Tables.EH_USER_GROUP_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfAccountCategories.class, Tables.EH_CONF_ACCOUNT_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNT_CATEGORIES.ID.max()).from(Tables.EH_CONF_ACCOUNT_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(EhConfOrders.class, EhConfInvoices.class, Tables.EH_CONF_INVOICES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_INVOICES.ID.max()).from(Tables.EH_CONF_INVOICES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfOrders.class, Tables.EH_CONF_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ORDERS.ID.max()).from(Tables.EH_CONF_ORDERS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfOrderAccountMap.class, Tables.EH_CONF_ORDER_ACCOUNT_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ID.max()).from(Tables.EH_CONF_ORDER_ACCOUNT_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhConfSourceAccounts.class, Tables.EH_CONF_SOURCE_ACCOUNTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_SOURCE_ACCOUNTS.ID.max()).from(Tables.EH_CONF_SOURCE_ACCOUNTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfAccounts.class, Tables.EH_CONF_ACCOUNTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNTS.ID.max()).from(Tables.EH_CONF_ACCOUNTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfAccountHistories.class, Tables.EH_CONF_ACCOUNT_HISTORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNT_HISTORIES.ID.max()).from(Tables.EH_CONF_ACCOUNT_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfConferences.class, Tables.EH_CONF_CONFERENCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_CONFERENCES.ID.max()).from(Tables.EH_CONF_CONFERENCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfEnterprises.class, Tables.EH_CONF_ENTERPRISES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ENTERPRISES.ID.max()).from(Tables.EH_CONF_ENTERPRISES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarningContacts.class, Tables.EH_WARNING_CONTACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WARNING_CONTACTS.ID.max()).from(Tables.EH_WARNING_CONTACTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfReservations.class, Tables.EH_CONF_RESERVATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_RESERVATIONS.ID.max()).from(Tables.EH_CONF_RESERVATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationCommunityRequests.class, Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.ID.max()).from(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationDetails.class, Tables.EH_ORGANIZATION_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_DETAILS.ID.max()).from(Tables.EH_ORGANIZATION_DETAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddresses.class, Tables.EH_ORGANIZATION_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESSES.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAttachments.class, Tables.EH_ORGANIZATION_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationRoleMap.class, Tables.EH_ORGANIZATION_ROLE_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ROLE_MAP.ID.max()).from(Tables.EH_ORGANIZATION_ROLE_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationTaskTargets.class, Tables.EH_ORGANIZATION_TASK_TARGETS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_TASK_TARGETS.ID.max()).from(Tables.EH_ORGANIZATION_TASK_TARGETS).fetchOne().value1();
        });

        syncTableSequence(EhMessages.class, EhMessages.class, com.everhomes.schema.Tables.EH_MESSAGES.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_MESSAGES.ID.max()).from(com.everhomes.schema.Tables.EH_MESSAGES).fetchOne().value1();
        });

        syncTableSequence(EhMessageBoxs.class, EhMessageBoxs.class, com.everhomes.schema.Tables.EH_MESSAGE_BOXS.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_MESSAGE_BOXS.ID.max()).from(com.everhomes.schema.Tables.EH_MESSAGE_BOXS).fetchOne().value1();
        });


        syncTableSequence(EhRepeatSettings.class, EhRepeatSettings.class, Tables.EH_REPEAT_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REPEAT_SETTINGS.ID.max()).from(Tables.EH_REPEAT_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionStandards.class, EhQualityInspectionStandards.class, Tables.EH_QUALITY_INSPECTION_STANDARDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARDS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARDS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionStandardGroupMap.class, EhQualityInspectionStandardGroupMap.class, Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTasks.class, EhQualityInspectionTasks.class, Tables.EH_QUALITY_INSPECTION_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASKS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASKS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionEvaluationFactors.class, EhQualityInspectionEvaluationFactors.class, Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionEvaluations.class, EhQualityInspectionEvaluations.class, Tables.EH_QUALITY_INSPECTION_EVALUATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_EVALUATIONS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTaskRecords.class, EhQualityInspectionTaskRecords.class, Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTaskAttachments.class, EhQualityInspectionTaskAttachments.class, Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionCategories.class, EhQualityInspectionCategories.class, Tables.EH_QUALITY_INSPECTION_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_CATEGORIES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_CATEGORIES).fetchOne().value1();
        });

        // 同步主表sequence时，第一个参数需要指定主表对应的pojo class by lqs 20160430
        //syncTableSequence(null, EhDoorAccess.class, Tables.EH_DOOR_ACCESS.getName(), (dbContext) -> {
        syncTableSequence(EhDoorAccess.class, EhDoorAccess.class, Tables.EH_DOOR_ACCESS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_ACCESS.ID.max()).from(Tables.EH_DOOR_ACCESS).fetchOne().value1();
        });
        syncTableSequence(null, EhOwnerDoors.class, Tables.EH_OWNER_DOORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OWNER_DOORS.ID.max()).from(Tables.EH_OWNER_DOORS).fetchOne().value1();
        });
        syncTableSequence(null, EhDoorAuth.class, Tables.EH_DOOR_AUTH.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_AUTH.ID.max()).from(Tables.EH_DOOR_AUTH).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkFirmware.class, Tables.EH_ACLINK_FIRMWARE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_FIRMWARE.ID.max()).from(Tables.EH_ACLINK_FIRMWARE).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAclinks.class, Tables.EH_ACLINKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINKS.ID.max()).from(Tables.EH_ACLINKS).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAesServerKey.class, Tables.EH_AES_SERVER_KEY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AES_SERVER_KEY.ID.max()).from(Tables.EH_AES_SERVER_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAesUserKey.class, Tables.EH_AES_USER_KEY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AES_USER_KEY.ID.max()).from(Tables.EH_AES_USER_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAclinkUndoKey.class, Tables.EH_ACLINK_UNDO_KEY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_UNDO_KEY.ID.max()).from(Tables.EH_ACLINK_UNDO_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhDoorCommand.class, Tables.EH_DOOR_COMMAND.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_COMMAND.ID.max()).from(Tables.EH_DOOR_COMMAND).fetchOne().value1();
        });

        syncTableSequence(null, EhOpPromotionAssignedScopes.class, Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhOpPromotionActivities.class, Tables.EH_OP_PROMOTION_ACTIVITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.max()).from(Tables.EH_OP_PROMOTION_ACTIVITIES).fetchOne().value1();
        });


        syncTableSequence(null, EhScheduleTasks.class, Tables.EH_SCHEDULE_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SCHEDULE_TASKS.ID.max()).from(Tables.EH_SCHEDULE_TASKS).fetchOne().value1();
        });


        syncTableSequence(null, EhScheduleTaskLogs.class, Tables.EH_SCHEDULE_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SCHEDULE_TASK_LOGS.ID.max()).from(Tables.EH_SCHEDULE_TASK_LOGS).fetchOne().value1();
        });


        syncTableSequence(null, EhOpPromotionMessages.class, Tables.EH_OP_PROMOTION_MESSAGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_MESSAGES.ID.max()).from(Tables.EH_OP_PROMOTION_MESSAGES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessAssignedNamespaces.class, Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.ID.max()).from(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES).fetchOne().value1();
        });

        syncTableSequence(null, EhParkingRechargeOrders.class, Tables.EH_PARKING_RECHARGE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_RECHARGE_ORDERS.ID.max()).from(Tables.EH_PARKING_RECHARGE_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingRechargeRates.class, Tables.EH_PARKING_RECHARGE_RATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_RECHARGE_RATES.ID.max()).from(Tables.EH_PARKING_RECHARGE_RATES).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCardRequests.class, Tables.EH_PARKING_CARD_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CARD_REQUESTS.ID.max()).from(Tables.EH_PARKING_CARD_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingLots.class, Tables.EH_PARKING_LOTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_LOTS.ID.max()).from(Tables.EH_PARKING_LOTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingVendors.class, Tables.EH_PARKING_VENDORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_VENDORS.ID.max()).from(Tables.EH_PARKING_VENDORS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmsyPayers.class, Tables.EH_PMSY_PAYERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_PAYERS.ID.max()).from(Tables.EH_PMSY_PAYERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyOrders.class, Tables.EH_PMSY_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_ORDERS.ID.max()).from(Tables.EH_PMSY_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyOrderItems.class, Tables.EH_PMSY_ORDER_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_ORDER_ITEMS.ID.max()).from(Tables.EH_PMSY_ORDER_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyCommunities.class, Tables.EH_PMSY_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_COMMUNITIES.ID.max()).from(Tables.EH_PMSY_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhWifiSettings.class, Tables.EH_WIFI_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WIFI_SETTINGS.ID.max()).from(Tables.EH_WIFI_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCards.class, Tables.EH_PAYMENT_CARDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARDS.ID.max()).from(Tables.EH_PAYMENT_CARDS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardIssuers.class, Tables.EH_PAYMENT_CARD_ISSUERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_ISSUERS.ID.max()).from(Tables.EH_PAYMENT_CARD_ISSUERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardIssuerCommunities.class, Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.ID.max()).from(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardRechargeOrders.class, Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.ID.max()).from(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardTransactions.class, Tables.EH_PAYMENT_CARD_TRANSACTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_TRANSACTIONS.ID.max()).from(Tables.EH_PAYMENT_CARD_TRANSACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionLogs.class, Tables.EH_QUALITY_INSPECTION_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_LOGS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhHotTags.class, Tables.EH_HOT_TAGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_HOT_TAGS.ID.max()).from(Tables.EH_HOT_TAGS).fetchOne().value1();
        });
        syncTableSequence(null, EhNews.class, Tables.EH_NEWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS.ID.max()).from(Tables.EH_NEWS).fetchOne().value1();
        });
        syncTableSequence(null, EhNewsComment.class, Tables.EH_NEWS_COMMENT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS_COMMENT.ID.max()).from(Tables.EH_NEWS_COMMENT).fetchOne().value1();
        });

        //小区导入时添加
        syncTableSequence(null, EhNamespaces.class, com.everhomes.schema.Tables.EH_NAMESPACES.getName(), (dbContext) -> {
            Integer maxId = dbContext.select(com.everhomes.schema.Tables.EH_NAMESPACES.ID.max()).from(com.everhomes.schema.Tables.EH_NAMESPACES).fetchOne().value1();
            if (maxId != null) {
                return Long.valueOf(maxId.longValue());
            }
            return null;
        });
        syncTableSequence(null, EhNamespaceDetails.class, Tables.EH_NAMESPACE_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NAMESPACE_DETAILS.ID.max()).from(Tables.EH_NAMESPACE_DETAILS).fetchOne().value1();
        });
        syncTableSequence(null, EhConfigurations.class, com.everhomes.schema.Tables.EH_CONFIGURATIONS.getName(), (dbContext) -> {
            Integer maxId = dbContext.select(com.everhomes.schema.Tables.EH_CONFIGURATIONS.ID.max()).from(com.everhomes.schema.Tables.EH_CONFIGURATIONS).fetchOne().value1();
            if (maxId != null) {
                return Long.valueOf(maxId.longValue());
            }
            return null;
        });

        syncTableSequence(null, EhEquipmentInspectionStandards.class, Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipments.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionAccessories.class, Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionAccessoryMap.class, Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentParameters.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentAttachments.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhLocaleTemplates.class, Tables.EH_LOCALE_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LOCALE_TEMPLATES.ID.max()).from(Tables.EH_LOCALE_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhCategories.class, Tables.EH_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CATEGORIES.ID.max()).from(Tables.EH_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTasks.class, Tables.EH_EQUIPMENT_INSPECTION_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASKS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTaskAttachments.class, Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTaskLogs.class, Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOfficeCubicleOrders.class, Tables.EH_OFFICE_CUBICLE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_ORDERS.ID.max()).from(Tables.EH_OFFICE_CUBICLE_ORDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhOfficeCubicleCategories.class, Tables.EH_OFFICE_CUBICLE_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_CATEGORIES.ID.max()).from(Tables.EH_OFFICE_CUBICLE_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhOfficeCubicleSpaces.class, Tables.EH_OFFICE_CUBICLE_SPACES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_SPACES.ID.max()).from(Tables.EH_OFFICE_CUBICLE_SPACES).fetchOne().value1();
        });
        syncTableSequence(null, EhOfficeCubicleAttachments.class, Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.ID.max()).from(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMembers.class, Tables.EH_ORGANIZATION_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBERS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBERS).fetchOne().value1();
        });
        syncTableSequence(null, EhRichTexts.class, Tables.EH_RICH_TEXTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RICH_TEXTS.ID.max()).from(Tables.EH_RICH_TEXTS).fetchOne().value1();
        });
        syncTableSequence(null, EhJournals.class, Tables.EH_JOURNALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_JOURNALS.ID.max()).from(Tables.EH_JOURNALS).fetchOne().value1();
        });
        syncTableSequence(null, EhJournalConfigs.class, Tables.EH_JOURNAL_CONFIGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_JOURNAL_CONFIGS.ID.max()).from(Tables.EH_JOURNAL_CONFIGS).fetchOne().value1();
        });

        syncTableSequence(null, EhCategories.class, Tables.EH_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CATEGORIES.ID.max()).from(Tables.EH_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTasks.class, Tables.EH_PM_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASKS.ID.max()).from(Tables.EH_PM_TASKS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskAttachments.class, Tables.EH_PM_TASK_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_PM_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskLogs.class, Tables.EH_PM_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_LOGS.ID.max()).from(Tables.EH_PM_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskStatistics.class, Tables.EH_PM_TASK_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_STATISTICS.ID.max()).from(Tables.EH_PM_TASK_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhUserImpersonations.class, Tables.EH_USER_IMPERSONATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IMPERSONATIONS.ID.max()).from(Tables.EH_USER_IMPERSONATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMembers.class, Tables.EH_ORGANIZATION_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBERS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBERS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatTaskLogs.class, Tables.EH_STAT_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_TASK_LOGS.ID.max()).from(Tables.EH_STAT_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatOrders.class, Tables.EH_STAT_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_ORDERS.ID.max()).from(Tables.EH_STAT_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatRefunds.class, Tables.EH_STAT_REFUNDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_REFUNDS.ID.max()).from(Tables.EH_STAT_REFUNDS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatTransactions.class, Tables.EH_STAT_TRANSACTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_TRANSACTIONS.ID.max()).from(Tables.EH_STAT_TRANSACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatSettlements.class, Tables.EH_STAT_SETTLEMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_SETTLEMENTS.ID.max()).from(Tables.EH_STAT_SETTLEMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatServiceSettlementResults.class, Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ID.max()).from(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAlliances.class, Tables.EH_SERVICE_ALLIANCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCES.ID.max()).from(Tables.EH_SERVICE_ALLIANCES).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceCategories.class, Tables.EH_SERVICE_ALLIANCE_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceAttachments.class, Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhYzbDevices.class, Tables.EH_YZB_DEVICES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_YZB_DEVICES.ID.max()).from(Tables.EH_YZB_DEVICES).fetchOne().value1();
        });
        syncTableSequence(null, EhSearchTypes.class, Tables.EH_SEARCH_TYPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SEARCH_TYPES.ID.max()).from(Tables.EH_SEARCH_TYPES).fetchOne().value1();
        });
        //审批相关表, add by tt, 160907
        syncTableSequence(null, EhApprovalDayActualTime.class, Tables.EH_APPROVAL_DAY_ACTUAL_TIME.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.ID.max()).from(Tables.EH_APPROVAL_DAY_ACTUAL_TIME).fetchOne().value1();
        });

        syncTableSequence(null, EhApprovalFlows.class, Tables.EH_APPROVAL_FLOWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_FLOWS.ID.max()).from(Tables.EH_APPROVAL_FLOWS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalFlowLevels.class, Tables.EH_APPROVAL_FLOW_LEVELS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_FLOW_LEVELS.ID.max()).from(Tables.EH_APPROVAL_FLOW_LEVELS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRules.class, Tables.EH_APPROVAL_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RULES.ID.max()).from(Tables.EH_APPROVAL_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRuleFlowMap.class, Tables.EH_APPROVAL_RULE_FLOW_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RULE_FLOW_MAP.ID.max()).from(Tables.EH_APPROVAL_RULE_FLOW_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalCategories.class, Tables.EH_APPROVAL_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_CATEGORIES.ID.max()).from(Tables.EH_APPROVAL_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRequests.class, Tables.EH_APPROVAL_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_REQUESTS.ID.max()).from(Tables.EH_APPROVAL_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalAttachments.class, Tables.EH_APPROVAL_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_ATTACHMENTS.ID.max()).from(Tables.EH_APPROVAL_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalTimeRanges.class, Tables.EH_APPROVAL_TIME_RANGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_TIME_RANGES.ID.max()).from(Tables.EH_APPROVAL_TIME_RANGES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalOpRequests.class, Tables.EH_APPROVAL_OP_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_OP_REQUESTS.ID.max()).from(Tables.EH_APPROVAL_OP_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRangeStatistics.class, Tables.EH_APPROVAL_RANGE_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RANGE_STATISTICS.ID.max()).from(Tables.EH_APPROVAL_RANGE_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestTemplates.class, Tables.EH_REQUEST_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_TEMPLATES.ID.max()).from(Tables.EH_REQUEST_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestTemplatesNamespaceMapping.class, Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING.ID.max()).from(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestAttachments.class, Tables.EH_REQUEST_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_ATTACHMENTS.ID.max()).from(Tables.EH_REQUEST_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceRequests.class, Tables.EH_SERVICE_ALLIANCE_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceNotifyTargets.class, Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS).fetchOne().value1();
        });
        syncTableSequence(null, EhSettleRequests.class, Tables.EH_SETTLE_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SETTLE_REQUESTS.ID.max()).from(Tables.EH_SETTLE_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskTargets.class, Tables.EH_PM_TASK_TARGETS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_TARGETS.ID.max()).from(Tables.EH_PM_TASK_TARGETS).fetchOne().value1();
        });

        syncTableSequence(null, EhStatActiveUsers.class, Tables.EH_STAT_ACTIVE_USERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_ACTIVE_USERS.ID.max()).from(Tables.EH_STAT_ACTIVE_USERS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarningSettings.class, Tables.EH_WARNING_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WARNING_SETTINGS.ID.max()).from(Tables.EH_WARNING_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhDoorUserPermission.class, Tables.EH_DOOR_USER_PERMISSION.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_USER_PERMISSION.ID.max()).from(Tables.EH_DOOR_USER_PERMISSION).fetchOne().value1();
        });

        syncTableSequence(null, EhGroupSettings.class, Tables.EH_GROUP_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_SETTINGS.ID.max()).from(Tables.EH_GROUP_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhBroadcasts.class, Tables.EH_BROADCASTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BROADCASTS.ID.max()).from(Tables.EH_BROADCASTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceReservationRequests.class, Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhResourceCategories.class, Tables.EH_RESOURCE_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RESOURCE_CATEGORIES.ID.max()).from(Tables.EH_RESOURCE_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhResourceCategoryAssignments.class, Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.ID.max()).from(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationJobPositions.class, Tables.EH_ORGANIZATION_JOB_POSITIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_JOB_POSITIONS.ID.max()).from(Tables.EH_ORGANIZATION_JOB_POSITIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationJobPositionMaps.class, Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ID.max()).from(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModules.class, Tables.EH_SERVICE_MODULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULES.ID.max()).from(Tables.EH_SERVICE_MODULES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModulePrivileges.class, Tables.EH_SERVICE_MODULE_PRIVILEGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_PRIVILEGES.ID.max()).from(Tables.EH_SERVICE_MODULE_PRIVILEGES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModuleAssignments.class, Tables.EH_SERVICE_MODULE_ASSIGNMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ID.max()).from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentStandardMap.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTemplates.class, Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTemplateItemMap.class, Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionItems.class, Tables.EH_EQUIPMENT_INSPECTION_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ITEMS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionItemResults.class, Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerAddress.class, Tables.EH_ORGANIZATION_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESSES.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCars.class, Tables.EH_ORGANIZATION_OWNER_CARS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CARS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CARS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerOwnerCar.class, Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerAttachments.class, Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCarAttachments.class, Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCarAttachments.class, Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerBehaviors.class, Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerType.class, Tables.EH_ORGANIZATION_OWNER_TYPE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_TYPE.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_TYPE).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCardCategories.class, Tables.EH_PARKING_CARD_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CARD_CATEGORIES.ID.max()).from(Tables.EH_PARKING_CARD_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlows.class, Tables.EH_FLOWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOWS.ID.max()).from(Tables.EH_FLOWS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowStats.class, Tables.EH_FLOW_STATS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_STATS.ID.max()).from(Tables.EH_FLOW_STATS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowNodes.class, Tables.EH_FLOW_NODES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_NODES.ID.max()).from(Tables.EH_FLOW_NODES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowButtons.class, Tables.EH_FLOW_BUTTONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_BUTTONS.ID.max()).from(Tables.EH_FLOW_BUTTONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowForms.class, Tables.EH_FLOW_FORMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_FORMS.ID.max()).from(Tables.EH_FLOW_FORMS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowActions.class, Tables.EH_FLOW_ACTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_ACTIONS.ID.max()).from(Tables.EH_FLOW_ACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowUserSelections.class, Tables.EH_FLOW_USER_SELECTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_USER_SELECTIONS.ID.max()).from(Tables.EH_FLOW_USER_SELECTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowCases.class, Tables.EH_FLOW_CASES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_CASES.ID.max()).from(Tables.EH_FLOW_CASES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowEventLogs.class, Tables.EH_FLOW_EVENT_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVENT_LOGS.ID.max()).from(Tables.EH_FLOW_EVENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowVariables.class, Tables.EH_FLOW_VARIABLES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_VARIABLES.ID.max()).from(Tables.EH_FLOW_VARIABLES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowEvaluates.class, Tables.EH_FLOW_EVALUATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVALUATES.ID.max()).from(Tables.EH_FLOW_EVALUATES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceApartmentRequests.class, Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeters.class, Tables.EH_ENERGY_METERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METERS.ID.max()).from(Tables.EH_ENERGY_METERS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterCategories.class, Tables.EH_ENERGY_METER_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_CATEGORIES.ID.max()).from(Tables.EH_ENERGY_METER_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterFormulas.class, Tables.EH_ENERGY_METER_FORMULAS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_FORMULAS.ID.max()).from(Tables.EH_ENERGY_METER_FORMULAS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterChangeLogs.class, Tables.EH_ENERGY_METER_CHANGE_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_CHANGE_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_CHANGE_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterReadingLogs.class, Tables.EH_ENERGY_METER_READING_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_READING_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_READING_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyDateStatistics.class, Tables.EH_ENERGY_DATE_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_DATE_STATISTICS.ID.max()).from(Tables.EH_ENERGY_DATE_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyCountStatistics.class, Tables.EH_ENERGY_COUNT_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_COUNT_STATISTICS.ID.max()).from(Tables.EH_ENERGY_COUNT_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyYoyStatistics.class, Tables.EH_ENERGY_YOY_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_YOY_STATISTICS.ID.max()).from(Tables.EH_ENERGY_YOY_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterSettingLogs.class, Tables.EH_ENERGY_METER_SETTING_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_SETTING_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_SETTING_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowScripts.class, Tables.EH_FLOW_SCRIPTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_SCRIPTS.ID.max()).from(Tables.EH_FLOW_SCRIPTS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowSubjects.class, Tables.EH_FLOW_SUBJECTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_SUBJECTS.ID.max()).from(Tables.EH_FLOW_SUBJECTS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowAttachments.class, Tables.EH_FLOW_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_ATTACHMENTS.ID.max()).from(Tables.EH_FLOW_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionCumulatives.class, Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionActives.class, Tables.EH_TERMINAL_APP_VERSION_ACTIVES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_ACTIVES).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionStatistics.class, Tables.EH_TERMINAL_APP_VERSION_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalDayStatistics.class, Tables.EH_TERMINAL_DAY_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_DAY_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_DAY_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalHourStatistics.class, Tables.EH_TERMINAL_HOUR_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_HOUR_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_HOUR_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalStatisticsTasks.class, Tables.EH_TERMINAL_STATISTICS_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_STATISTICS_TASKS.ID.max()).from(Tables.EH_TERMINAL_STATISTICS_TASKS).fetchOne().value1();
        });

        syncTableSequence(null, EhAppVersion.class, Tables.EH_APP_VERSION.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APP_VERSION.ID.max()).from(Tables.EH_APP_VERSION).fetchOne().value1();
        });


        syncTableSequence(null, EhServiceHotlines.class, Tables.EH_SERVICE_HOTLINES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_HOTLINES.ID.max()).from(Tables.EH_SERVICE_HOTLINES).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskTargetStatistics.class, Tables.EH_PM_TASK_TARGET_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_TARGET_STATISTICS.ID.max()).from(Tables.EH_PM_TASK_TARGET_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhContracts.class, Tables.EH_CONTRACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACTS.ID.max()).from(Tables.EH_CONTRACTS).fetchOne().value1();
        });

        syncTableSequence(null, EhContractBuildingMappings.class, Tables.EH_CONTRACT_BUILDING_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ID.max()).from(Tables.EH_CONTRACT_BUILDING_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhAppNamespaceMappings.class, Tables.EH_APP_NAMESPACE_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APP_NAMESPACE_MAPPINGS.ID.max()).from(Tables.EH_APP_NAMESPACE_MAPPINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowTimeouts.class, Tables.EH_FLOW_TIMEOUTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_TIMEOUTS.ID.max()).from(Tables.EH_FLOW_TIMEOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCarSeries.class, Tables.EH_PARKING_CAR_SERIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CAR_SERIES.ID.max()).from(Tables.EH_PARKING_CAR_SERIES).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingAttachments.class, Tables.EH_PARKING_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_ATTACHMENTS.ID.max()).from(Tables.EH_PARKING_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingStatistics.class, Tables.EH_PARKING_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_STATISTICS.ID.max()).from(Tables.EH_PARKING_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingFlow.class, Tables.EH_PARKING_FLOW.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_FLOW.ID.max()).from(Tables.EH_PARKING_FLOW).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddressMappings.class, Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhUserBlacklists.class, Tables.EH_USER_BLACKLISTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_BLACKLISTS.ID.max()).from(Tables.EH_USER_BLACKLISTS).fetchOne().value1();
        });
        syncTableSequence(null, EhActivityAttachments.class, Tables.EH_ACTIVITY_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_ATTACHMENTS.ID.max()).from(Tables.EH_ACTIVITY_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhActivityGoods.class, Tables.EH_ACTIVITY_GOODS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_GOODS.ID.max()).from(Tables.EH_ACTIVITY_GOODS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowTimeouts.class, Tables.EH_FLOW_TIMEOUTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_TIMEOUTS.ID.max()).from(Tables.EH_FLOW_TIMEOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingClearanceOperators.class, Tables.EH_PARKING_CLEARANCE_OPERATORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CLEARANCE_OPERATORS.ID.max()).from(Tables.EH_PARKING_CLEARANCE_OPERATORS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingClearanceLogs.class, Tables.EH_PARKING_CLEARANCE_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CLEARANCE_LOGS.ID.max()).from(Tables.EH_PARKING_CLEARANCE_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddressMappings.class, Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhLaunchAdvertisements.class, Tables.EH_LAUNCH_ADVERTISEMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LAUNCH_ADVERTISEMENTS.ID.max()).from(Tables.EH_LAUNCH_ADVERTISEMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhTechparkSyncdataBackup.class, Tables.EH_TECHPARK_SYNCDATA_BACKUP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TECHPARK_SYNCDATA_BACKUP.ID.max()).from(Tables.EH_TECHPARK_SYNCDATA_BACKUP).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowEvaluateItems.class, Tables.EH_FLOW_EVALUATE_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVALUATE_ITEMS.ID.max()).from(Tables.EH_FLOW_EVALUATE_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberLogs.class, Tables.EH_ORGANIZATION_MEMBER_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceJumpModule.class, Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceInvestRequests.class, Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralApprovals.class, Tables.EH_GENERAL_APPROVALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_APPROVALS.ID.max()).from(Tables.EH_GENERAL_APPROVALS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralForms.class, Tables.EH_GENERAL_FORMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_FORMS.ID.max()).from(Tables.EH_GENERAL_FORMS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralApprovalVals.class, Tables.EH_GENERAL_APPROVAL_VALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_APPROVAL_VALS.ID.max()).from(Tables.EH_GENERAL_APPROVAL_VALS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionStandardSpecificationMap.class, Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhBusinessPromotions.class, Tables.EH_BUSINESS_PROMOTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_PROMOTIONS.ID.max()).from(Tables.EH_BUSINESS_PROMOTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionTaskTemplates.class, Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhDoorAuthLogs.class, Tables.EH_DOOR_AUTH_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_AUTH_LOGS.ID.max()).from(Tables.EH_DOOR_AUTH_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSpecifications.class, Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhAssetBills.class, Tables.EH_ASSET_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_BILLS.ID.max()).from(Tables.EH_ASSET_BILLS).fetchOne().value1();
        });
        syncTableSequence(null, EhAssetBillTemplateFields.class, Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.ID.max()).from(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionCategories.class, Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionStandardGroupMap.class, Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaires.class, Tables.EH_QUESTIONNAIRES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRES.ID.max()).from(Tables.EH_QUESTIONNAIRES).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireQuestions.class, Tables.EH_QUESTIONNAIRE_QUESTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_QUESTIONS.ID.max()).from(Tables.EH_QUESTIONNAIRE_QUESTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireOptions.class, Tables.EH_QUESTIONNAIRE_OPTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_OPTIONS.ID.max()).from(Tables.EH_QUESTIONNAIRE_OPTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireAnswers.class, Tables.EH_QUESTIONNAIRE_ANSWERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.max()).from(Tables.EH_QUESTIONNAIRE_ANSWERS).fetchOne().value1();
        });
        syncTableSequence(null, EhOsObjects.class, Tables.EH_OS_OBJECTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OS_OBJECTS.ID.max()).from(Tables.EH_OS_OBJECTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOsObjectDownloadLogs.class, Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.ID.max()).from(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhDockingMappings.class, Tables.EH_DOCKING_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOCKING_MAPPINGS.ID.max()).from(Tables.EH_DOCKING_MAPPINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseIssuers.class, Tables.EH_LEASE_ISSUERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_ISSUERS.ID.max()).from(Tables.EH_LEASE_ISSUERS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseIssuerAddresses.class, Tables.EH_LEASE_ISSUER_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_ISSUER_ADDRESSES.ID.max()).from(Tables.EH_LEASE_ISSUER_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseConfigs.class, Tables.EH_LEASE_CONFIGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_CONFIGS.ID.max()).from(Tables.EH_LEASE_CONFIGS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceGolfRequests.class, Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceGymRequests.class, Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceServerRequests.class, Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceServerRequests.class, Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnterpriseOpRequestBuildings.class, Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.ID.max()).from(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOauth2Servers.class, Tables.EH_OAUTH2_SERVERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_SERVERS.ID.max()).from(Tables.EH_OAUTH2_SERVERS).fetchOne().value1();
        });
        syncTableSequence(null, EhOauth2ClientTokens.class, Tables.EH_OAUTH2_CLIENT_TOKENS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_CLIENT_TOKENS.ID.max()).from(Tables.EH_OAUTH2_CLIENT_TOKENS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterPriceConfig.class, Tables.EH_ENERGY_METER_PRICE_CONFIG.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_PRICE_CONFIG.ID.max()).from(Tables.EH_ENERGY_METER_PRICE_CONFIG).fetchOne().value1();
        });
        syncTableSequence(null, EhActivityVideo.class, Tables.EH_ACTIVITY_VIDEO.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_VIDEO.ID.max()).from(Tables.EH_ACTIVITY_VIDEO).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterDefaultSettings.class, Tables.EH_ENERGY_METER_DEFAULT_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS.ID.max()).from(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMonthStatistics.class, Tables.EH_ENERGY_MONTH_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_MONTH_STATISTICS.ID.max()).from(Tables.EH_ENERGY_MONTH_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhPreviews.class, Tables.EH_PREVIEWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PREVIEWS.ID.max()).from(Tables.EH_PREVIEWS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressCompanies.class, Tables.EH_EXPRESS_COMPANIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_COMPANIES.ID.max()).from(Tables.EH_EXPRESS_COMPANIES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressUsers.class, Tables.EH_EXPRESS_USERS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_USERS.ID.max()).from(Tables.EH_EXPRESS_USERS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressAddresses.class, Tables.EH_EXPRESS_ADDRESSES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ADDRESSES.ID.max()).from(Tables.EH_EXPRESS_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressOrders.class, Tables.EH_EXPRESS_ORDERS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ORDERS.ID.max()).from(Tables.EH_EXPRESS_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressQueryHistories.class, Tables.EH_EXPRESS_QUERY_HISTORIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_QUERY_HISTORIES.ID.max()).from(Tables.EH_EXPRESS_QUERY_HISTORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressOrderLogs.class, Tables.EH_EXPRESS_ORDER_LOGS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ORDER_LOGS.ID.max()).from(Tables.EH_EXPRESS_ORDER_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhImportFileTasks.class, Tables.EH_IMPORT_FILE_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_IMPORT_FILE_TASKS.ID.max()).from(Tables.EH_IMPORT_FILE_TASKS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskHistoryAddresses.class, Tables.EH_PM_TASK_HISTORY_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.max()).from(Tables.EH_PM_TASK_HISTORY_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberDetails.class, Tables.EH_ORGANIZATION_MEMBER_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_DETAILS).fetchOne().value1();
        });
        syncTableSequence(null, EhUserNotificationSettings.class, Tables.EH_USER_NOTIFICATION_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_NOTIFICATION_SETTINGS.ID.max()).from(Tables.EH_USER_NOTIFICATION_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberEducations.class, Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberWorkExperiences.class, Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberInsurances.class, Tables.EH_ORGANIZATION_MEMBER_INSURANCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_INSURANCES.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_INSURANCES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberContracts.class, Tables.EH_ORGANIZATION_MEMBER_CONTRACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_CONTRACTS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_CONTRACTS).fetchOne().value1();
        });

        syncTableSequence(null, EhGroupMemberLogs.class, Tables.EH_GROUP_MEMBER_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_MEMBER_LOGS.ID.max()).from(Tables.EH_GROUP_MEMBER_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberProfileLogs.class, Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS).fetchOne().value1();
        });
        //added by Janson
        syncTableSequence(null, EhWebMenus.class, Tables.EH_WEB_MENUS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WEB_MENUS.ID.max()).from(Tables.EH_WEB_MENUS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhServiceModuleAssignmentRelations.class, Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.ID.max()).from(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhAuthorizations.class, Tables.EH_AUTHORIZATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATIONS.ID.max()).from(Tables.EH_AUTHORIZATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhAuthorizationRelations.class, Tables.EH_AUTHORIZATION_RELATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATION_RELATIONS.ID.max()).from(Tables.EH_AUTHORIZATION_RELATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskHistoryAddresses.class, Tables.EH_PM_TASK_HISTORY_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.max()).from(Tables.EH_PM_TASK_HISTORY_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhRosterOrderSettings.class, Tables.EH_ROSTER_ORDER_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ROSTER_ORDER_SETTINGS.ID.max()).from(Tables.EH_ROSTER_ORDER_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhWarehouses.class, Tables.EH_WAREHOUSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSES.ID.max()).from(Tables.EH_WAREHOUSES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalents.class, Tables.EH_TALENTS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENTS.ID.max()).from(Tables.EH_TALENTS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalentCategories.class, Tables.EH_TALENT_CATEGORIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_CATEGORIES.ID.max()).from(Tables.EH_TALENT_CATEGORIES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalentQueryHistories.class, Tables.EH_TALENT_QUERY_HISTORIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_QUERY_HISTORIES.ID.max()).from(Tables.EH_TALENT_QUERY_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseMaterials.class, Tables.EH_WAREHOUSE_MATERIALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_MATERIALS.ID.max()).from(Tables.EH_WAREHOUSE_MATERIALS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseMaterialCategories.class, Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.max()).from(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseStocks.class, Tables.EH_WAREHOUSE_STOCKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_STOCKS.ID.max()).from(Tables.EH_WAREHOUSE_STOCKS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseStockLogs.class, Tables.EH_WAREHOUSE_STOCK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_STOCK_LOGS.ID.max()).from(Tables.EH_WAREHOUSE_STOCK_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseRequests.class, Tables.EH_WAREHOUSE_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_REQUESTS.ID.max()).from(Tables.EH_WAREHOUSE_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseRequestMaterials.class, Tables.EH_WAREHOUSE_REQUEST_MATERIALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.ID.max()).from(Tables.EH_WAREHOUSE_REQUEST_MATERIALS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseUnits.class, Tables.EH_WAREHOUSE_UNITS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_UNITS.ID.max()).from(Tables.EH_WAREHOUSE_UNITS).fetchOne().value1();

        });

        syncTableSequence(null, EhSmsLogs.class, Tables.EH_SMS_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SMS_LOGS.ID.max()).from(Tables.EH_SMS_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSamples.class, Tables.EH_QUALITY_INSPECTION_SAMPLES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLES).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleGroupMap.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleCommunityMap.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleScoreStat.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleCommunitySpecificationStat.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT).fetchOne().value1();

        });
        syncTableSequence(null, EhQualityInspectionSpecificationItemResults.class, Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS).fetchOne().value1();

        });
        syncTableSequence(null, EhUserOrganizations.class, Tables.EH_USER_ORGANIZATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_ORGANIZATIONS.ID.max()).from(Tables.EH_USER_ORGANIZATIONS).fetchOne().value1();

        });

        syncTableSequence(null, EhRentalv2PriceRules.class, Tables.EH_RENTALV2_PRICE_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_PRICE_RULES.ID.max()).from(Tables.EH_RENTALV2_PRICE_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhSmsLogs.class, Tables.EH_USER_IDENTIFIER_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IDENTIFIER_LOGS.ID.max()).from(Tables.EH_USER_IDENTIFIER_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhSmsLogs.class, Tables.EH_USER_APPEAL_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_APPEAL_LOGS.ID.max()).from(Tables.EH_USER_APPEAL_LOGS).fetchOne().value1();
        });
    }

    @SuppressWarnings("rawtypes")
    private void syncTableSequence(Class keytableCls, Class pojoClass, String tableName, SequenceQueryCallback callback) {
        AccessSpec spec = null;
        if(keytableCls == null) {
            spec = AccessSpec.readOnly();
        } else {
            spec = AccessSpec.readOnlyWith(keytableCls);
        }

        Long result[] = new Long[1];
        result[0] = 0L;
        dbProvider.mapReduce(spec, null, (dbContext, contextObj) -> {
            //Long max = dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();
            Long max = callback.maxSequence(dbContext);

            if(max != null && max.longValue() > result[0].longValue()) {
                result[0] = max;
            }
            return true;
        });
        String key = NameMapper.getSequenceDomainFromTablePojo(pojoClass);
        long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
        sequenceProvider.resetSequence(key, result[0].longValue() + 1);
        long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sync table sequence, tableName=" + tableName + ", key=" + key + ", newSequence=" + (result[0].longValue() + 1)
                + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
        }
    }

    private void syncUserAccountName() {
        int pageCount = 1000;
        int pageNum = 1;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<User> userList = null;
        long maxAccountSequence = 0;
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
        } while(userList != null && userList.size() > 0);

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
}
