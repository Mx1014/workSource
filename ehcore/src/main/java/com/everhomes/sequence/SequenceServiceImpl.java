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
import com.everhomes.schema.tables.pojos.EhAclRoleAssignments;
import com.everhomes.schema.tables.pojos.EhAcls;
import com.everhomes.schema.tables.pojos.EhContentShardMap;
import com.everhomes.schema.tables.pojos.EhServerShardMap;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhActivityRoster;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhBuildingAttachments;
import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.server.schema.tables.pojos.EhBusinessAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhBusinessCategories;
import com.everhomes.server.schema.tables.pojos.EhBusinessVisibleScopes;
import com.everhomes.server.schema.tables.pojos.EhBusinesses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhCommunityGeopoints;
import com.everhomes.server.schema.tables.pojos.EhContentServerResources;
import com.everhomes.server.schema.tables.pojos.EhCooperationRequests;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseAddresses;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseAttachments;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactEntries;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroups;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContacts;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseDetails;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;
import com.everhomes.server.schema.tables.pojos.EhForumAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhForumAttachments;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.server.schema.tables.pojos.EhForums;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhGroupOpRequests;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotionAttachments;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotions;
import com.everhomes.server.schema.tables.pojos.EhNamespaceResources;
import com.everhomes.server.schema.tables.pojos.EhNearbyCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhOauth2Codes;
import com.everhomes.server.schema.tables.pojos.EhOauth2Tokens;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.pojos.EhParkApplyCard;
import com.everhomes.server.schema.tables.pojos.EhParkCharge;
import com.everhomes.server.schema.tables.pojos.EhPollItems;
import com.everhomes.server.schema.tables.pojos.EhPollVotes;
import com.everhomes.server.schema.tables.pojos.EhPolls;
import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionApprovals;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionRequests;
import com.everhomes.server.schema.tables.pojos.EhPunchGeopoints;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkday;
import com.everhomes.server.schema.tables.pojos.EhPushMessageResults;
import com.everhomes.server.schema.tables.pojos.EhQrcodes;
import com.everhomes.server.schema.tables.pojos.EhRechargeInfo;
import com.everhomes.server.schema.tables.pojos.EhRentalBillAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalBillPaybillMap;
import com.everhomes.server.schema.tables.pojos.EhRentalBills;
import com.everhomes.server.schema.tables.pojos.EhRentalItemsBills;
import com.everhomes.server.schema.tables.pojos.EhRentalRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteItems;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.server.schema.tables.pojos.EhRentalSitesBills;
import com.everhomes.server.schema.tables.pojos.EhUserCommunities;
import com.everhomes.server.schema.tables.pojos.EhUserFavorites;
import com.everhomes.server.schema.tables.pojos.EhUserGroups;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUserInvitationRoster;
import com.everhomes.server.schema.tables.pojos.EhUserInvitations;
import com.everhomes.server.schema.tables.pojos.EhUserLikes;
import com.everhomes.server.schema.tables.pojos.EhUserPosts;
import com.everhomes.server.schema.tables.pojos.EhUserProfiles;
import com.everhomes.server.schema.tables.pojos.EhUserServiceAddresses;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhVersionRealm;
import com.everhomes.server.schema.tables.pojos.EhVersionUpgradeRules;
import com.everhomes.server.schema.tables.pojos.EhVersionUrls;
import com.everhomes.server.schema.tables.pojos.EhVersionedContent;
import com.everhomes.server.schema.tables.pojos.EhYellowPageAttachments;
import com.everhomes.server.schema.tables.pojos.EhYellowPages;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserServiceAddress;

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
        
        syncTableSequence(EhGroups.class, EhPunchDayLogs.class, Tables.EH_PUNCH_DAY_LOGS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_PUNCH_DAY_LOGS.ID.max()).from(Tables.EH_PUNCH_DAY_LOGS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhGroups.class, EhPunchExceptionApprovals.class, Tables.EH_PUNCH_EXCEPTION_APPROVALS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_APPROVALS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhGroups.class, EhPunchExceptionRequests.class, Tables.EH_PUNCH_EXCEPTION_REQUESTS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS).fetchOne().value1(); 
        });
        

        syncTableSequence(EhGroups.class, EhPunchGeopoints.class, Tables.EH_PUNCH_GEOPOINTS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_PUNCH_GEOPOINTS.ID.max()).from(Tables.EH_PUNCH_GEOPOINTS).fetchOne().value1(); 
        });


        syncTableSequence(EhGroups.class, EhPunchLogs.class, Tables.EH_PUNCH_LOGS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_PUNCH_LOGS.ID.max()).from(Tables.EH_PUNCH_LOGS).fetchOne().value1(); 
        });

        syncTableSequence(EhGroups.class, EhPunchRules.class, Tables.EH_PUNCH_RULES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_PUNCH_RULES.ID.max()).from(Tables.EH_PUNCH_RULES).fetchOne().value1(); 
        });

        syncTableSequence(EhGroups.class, EhPunchWorkday.class, Tables.EH_PUNCH_WORKDAY.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_PUNCH_WORKDAY.ID.max()).from(Tables.EH_PUNCH_WORKDAY).fetchOne().value1(); 
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
