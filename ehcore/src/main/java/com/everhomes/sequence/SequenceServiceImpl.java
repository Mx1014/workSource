// @formatter:off
package com.everhomes.sequence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.schema.tables.pojos.EhAclPrivileges;
import com.everhomes.schema.tables.pojos.EhAclRoleAssignments;
import com.everhomes.schema.tables.pojos.EhAclRoles;
import com.everhomes.schema.tables.pojos.EhAcls;
import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.schema.tables.pojos.EhContentShardMap;
import com.everhomes.schema.tables.pojos.EhMessageBoxs;
import com.everhomes.schema.tables.pojos.EhMessages;
import com.everhomes.schema.tables.pojos.EhNamespaces;
import com.everhomes.schema.tables.pojos.EhServerShardMap;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

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
            if(max != null) {
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

        syncTableSequence(EhOrganizations.class, EhRentalv2ItemsOrders.class, Tables.EH_RENTALV2_ITEMS_ORDERS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_RENTALV2_ITEMS_ORDERS.ID.max()).from(Tables.EH_RENTALV2_ITEMS_ORDERS).fetchOne().value1(); 
        });
 

        syncTableSequence(EhOrganizations.class, EhRentalv2Items.class, Tables.EH_RENTALV2_ITEMS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_RENTALV2_ITEMS.ID.max()).from(Tables.EH_RENTALV2_ITEMS).fetchOne().value1(); 
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2Cells.class, Tables.EH_RENTALV2_CELLS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_RENTALV2_CELLS.ID.max()).from(Tables.EH_RENTALV2_CELLS).fetchOne().value1(); 
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
