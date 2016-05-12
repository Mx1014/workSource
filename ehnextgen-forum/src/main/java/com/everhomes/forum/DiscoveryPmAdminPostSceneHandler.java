package com.everhomes.forum;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.forum.ForumLocalStringCode;
import com.everhomes.rest.forum.OrganizationTopicMixType;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.ui.forum.SelectorBooleanFlag;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.user.User;
import com.everhomes.util.WebTokenGenerator;

@Component(PostSceneHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_pm_admin")
public class DiscoveryPmAdminPostSceneHandler implements PostSceneHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryPmAdminPostSceneHandler.class);
    
    @Autowired
    protected CommunityProvider communityProvider;
    
    @Autowired
    protected FamilyProvider familyProvider;
    
    @Autowired
    protected LocaleStringService localeStringService;
    
    @Autowired
    protected ConfigurationProvider configProvider;
    
    @Autowired
    protected GroupService groupService;
    
    @Autowired
    protected GroupProvider groupProvider;
    
    @Autowired
    protected ContentServerService contentServerService;
    
    @Autowired
    protected OrganizationProvider organizationProvider;

    @Value("${server.contextPath:}")
    protected String serverContectPath;
    
    public List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if(sceneType == SceneType.PM_ADMIN) {
            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneToken);
            return null;
        } else {
            return getTopicQueryFilters(user, sceneToken, sceneToken.getEntityId());
        }
    }
    
    protected List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken, Long organizationId) {
        List<TopicFilterDTO> filterList = new ArrayList<TopicFilterDTO>();
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if(organization == null) {
            LOGGER.error("Organization not found, sceneToken={}", sceneToken);
            return filterList;
        }
        
        // 由于公司删除后只是置状态，故需要判断状态是否正常 by lqs 20160430
        OrganizationStatus orgStatus = OrganizationStatus.fromCode(organization.getStatus());
        if(orgStatus == OrganizationStatus.ACTIVE) {
            String menuName = null;
            String scope = ForumLocalStringCode.SCOPE;
            String code = "";
            String actionUrl = null;            
            long menuId = 1;
            String avatarUri = null;
            Integer namespaceId = sceneToken.getNamespaceId();
            // 由于有些分组没有，故默认的菜单也需要进行动态设置 by lqs 20160427
            boolean hasDefault = false; 

            long group1Id = menuId++;
            // 本公司
            List<TopicFilterDTO> tmpFilterList = new ArrayList<TopicFilterDTO>();
            TopicFilterDTO filterDto = null;
            Group group = null;
            if(organization.getGroupId() != null) {
                if(organization.getGroupId() != null) {
                    group = groupProvider.findGroupById(organization.getGroupId());
                }
                if(group != null) {
                    filterDto = new TopicFilterDTO();
                    filterDto.setId(menuId++);
                    filterDto.setParentId(group1Id);
                    filterDto.setName(organization.getName());
                    filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                    filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                    actionUrl = String.format("%s%s?forumId=%s", serverContectPath, 
                        "/forum/listTopics", group.getOwningForumId());
                    filterDto.setActionUrl(actionUrl);
                    avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                    filterDto.setAvatar(avatarUri);
                    filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                    tmpFilterList.add(filterDto);
                }
            } else {
                LOGGER.warn("The group id of organization is null, sceneToken=" + sceneToken);
            }

            // 子公司
            List<String> groupTypes = new ArrayList<String>();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
            if(subOrgList != null && subOrgList.size() > 0) {
                for(Organization subOrg : subOrgList) {
                    if(subOrg.getGroupId() != null) {
                        if(subOrg.getGroupId() != null) {
                            group = groupProvider.findGroupById(subOrg.getGroupId());
                        }
                        if(group != null) {
                            filterDto = new TopicFilterDTO();
                            filterDto.setId(menuId++);
                            filterDto.setParentId(group1Id);
                            filterDto.setName(subOrg.getName());
                            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                            filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                            actionUrl = String.format("%s%s?forumId=%s", serverContectPath, 
                                "/forum/listTopics", group.getOwningForumId());
                            filterDto.setActionUrl(actionUrl);
                            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                            filterDto.setAvatar(avatarUri);
                            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                            tmpFilterList.add(filterDto);
                        }
                    } else {
                        LOGGER.warn("The group id of suborganization is null, subOrgId=" + subOrg.getId() + ", sceneToken=" + sceneToken);
                    }
                }
            }
            
            // 当公司和子公司都没有论坛的时候，此时不显示公司圈组 by lqs 20160426
            if(tmpFilterList.size() > 0) {
                // 菜单：公司圈组
                TopicFilterDTO orgGroupFilterDto = new TopicFilterDTO();
                orgGroupFilterDto.setId(group1Id);
                orgGroupFilterDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ORGANIZATION_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                orgGroupFilterDto.setName(menuName);
                orgGroupFilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                orgGroupFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                filterList.add(orgGroupFilterDto);
                
                // 公司全部
                TopicFilterDTO orgAllFilterDto = new TopicFilterDTO();
                orgAllFilterDto.setId(menuId++);
                orgAllFilterDto.setParentId(group1Id);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ALL);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                orgAllFilterDto.setName(menuName);
                orgAllFilterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                orgAllFilterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode()); // 整组菜单只有一个是默认的
                hasDefault = true;
                actionUrl = String.format("%s%s?organizationId=%s&mixType=%s", serverContectPath, 
                    "/org/listOrgMixTopics", organization.getId(), OrganizationTopicMixType.CHILDREN_ALL.getCode());
                orgAllFilterDto.setActionUrl(actionUrl);
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.all", "");
                orgAllFilterDto.setAvatar(avatarUri);
                orgAllFilterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                filterList.add(orgAllFilterDto);
                
                // 公司圈及子公司圈
                filterList.addAll(tmpFilterList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No organization group filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }

            long group2Id = menuId++;
            tmpFilterList.clear();
            // 公司管理的单个小区
            List<OrganizationCommunity> organizationCommunitys = organizationProvider.listOrganizationCommunities(organization.getId());
            for(OrganizationCommunity orgCmnty : organizationCommunitys) {
                Community community = communityProvider.findCommunityById(orgCmnty.getCommunityId());
                if(community != null) {
                    filterDto = new TopicFilterDTO();
                    filterDto.setId(menuId++);
                    filterDto.setParentId(group2Id);
                    filterDto.setName(community.getName());
                    filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                    filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                    actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s", serverContectPath, 
                        "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.COMMUNITY.getCode(), community.getId());
                    filterDto.setActionUrl(actionUrl);
                    avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                    filterDto.setAvatar(avatarUri);
                    filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                    tmpFilterList.add(filterDto);
                } else {
                    LOGGER.warn("Community not found, sceneToken=" + sceneToken + ", organizationId=" + organization.getId());
                }
            }
            
            if(tmpFilterList.size() > 0) {
                // 所管理的小区
                TopicFilterDTO cmntyGroupFilterDto = new TopicFilterDTO();
                cmntyGroupFilterDto.setId(group2Id);
                cmntyGroupFilterDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                cmntyGroupFilterDto.setName(menuName);
                cmntyGroupFilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                cmntyGroupFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                filterList.add(cmntyGroupFilterDto);
                
                // 公司管理的全部小区
                TopicFilterDTO cmntyAllFilterDto = new TopicFilterDTO();
                cmntyAllFilterDto.setId(menuId++);
                cmntyAllFilterDto.setParentId(group2Id);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ALL);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                cmntyAllFilterDto.setName(menuName);
                cmntyAllFilterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                if(hasDefault) {
                    cmntyAllFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                } else {
                    cmntyAllFilterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());
                    hasDefault = true;
                }
                actionUrl = String.format("%s%s?organizationId=%s&mixType=%s", serverContectPath, 
                    "/org/listOrgMixTopics", organization.getId(), OrganizationTopicMixType.COMMUNITY_ALL.getCode());
                cmntyAllFilterDto.setActionUrl(actionUrl);
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                cmntyAllFilterDto.setAvatar(avatarUri);
                cmntyAllFilterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                filterList.add(cmntyAllFilterDto);
                
                // 公司管理的各个小区
                filterList.addAll(tmpFilterList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No organization community filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
        } else {
            LOGGER.error("Organization not in active state, sceneToken={}, orgStatus={}", sceneToken, orgStatus);
        }
        
        return filterList;
    }
    
    private String getPostFilterDefaultAvatar(Integer namespaceId, Long userId, String avatarUri) {
        if(avatarUri != null) {
            try {
                return contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), userId);
            } catch (Exception e) {
                LOGGER.error("Failed to parse post filter default avatar uri, namespaceId={}, userId={}, avatarUri={}", 
                    namespaceId, userId, avatarUri, e);
            }
        }
        
        return null;
    }

    @Override
    public List<TopicScopeDTO> getDiscoveryTopicSentScopes(User user, SceneTokenDTO sceneTokenDto) {
        SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
        if(sceneType == SceneType.PM_ADMIN) {
            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneTokenDto);
            return null;
        } else {
            return getDiscoveryTopicSentScopes(user, sceneTokenDto, sceneTokenDto.getEntityId());
        }
    }
    
    protected List<TopicScopeDTO> getDiscoveryTopicSentScopes(User user, SceneTokenDTO sceneTokenDto, Long organizationId) {
        List<TopicScopeDTO> sentScopeList = new ArrayList<TopicScopeDTO>();
        Organization organization = organizationProvider.findOrganizationById(sceneTokenDto.getEntityId());
        if(organization == null) {
            LOGGER.error("Organization not found, sceneToken={}", sceneTokenDto);
            return sentScopeList;
        }
        
        // 由于公司删除后只是置状态，故需要判断状态是否正常 by lqs 20160430
        OrganizationStatus orgStatus = OrganizationStatus.fromCode(organization.getStatus());
        if(orgStatus == OrganizationStatus.ACTIVE) {
            String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
            String menuName = null;
            String scope = ForumLocalStringCode.SCOPE;
            String code = "";      
            long menuId = 1;
            String avatarUri = null;
            Integer namespaceId = sceneTokenDto.getNamespaceId();

            long group1Id = menuId++;
            List<TopicScopeDTO> tmpSentScopeList = new ArrayList<TopicScopeDTO>();
            
            // 本公司
            TopicScopeDTO sentScopeDto = null;
            Group groupDto = null;
            if(organization.getGroupId() != null) {
                groupDto = groupProvider.findGroupById(organization.getGroupId());
            }
            if(groupDto != null) {
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(menuId++);
                sentScopeDto.setParentId(group1Id);
                sentScopeDto.setName(organization.getName());
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                sentScopeDto.setForumId(groupDto.getOwningForumId());
                sentScopeDto.setSceneToken(sceneToken);
                sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                sentScopeDto.setAvatar(avatarUri);
                sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                tmpSentScopeList.add(sentScopeDto);
            }

            // 子公司
            List<String> groupTypes = new ArrayList<String>();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
            if(subOrgList != null && subOrgList.size() > 0) {
                for(Organization subOrg : subOrgList) {
                    groupDto = null;
                    if(subOrg.getGroupId() != null) {
                        groupDto = groupProvider.findGroupById(subOrg.getGroupId());
                    }
                    if(groupDto != null) {
                        sentScopeDto = new TopicScopeDTO();
                        sentScopeDto.setId(menuId++);
                        sentScopeDto.setParentId(group1Id);
                        sentScopeDto.setName(subOrg.getName());
                        sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                        sentScopeDto.setForumId(groupDto.getOwningForumId());
                        sentScopeDto.setSceneToken(sceneToken);
                        sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                        sentScopeDto.setAvatar(avatarUri);
                        sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                        tmpSentScopeList.add(sentScopeDto);
                    }
                }
            }
            
            if(tmpSentScopeList.size() > 0) {
                // 菜单：公司圈
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(group1Id);
                sentScopeDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ORGANIZATION_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                sentScopeDto.setName(menuName);
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                sentScopeList.add(sentScopeDto);
                
                // 本公司及各子公司
                sentScopeList.addAll(tmpSentScopeList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No organization group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
            

            long group2Id = menuId++;
            // 公司管理的单个小区
            tmpSentScopeList.clear();
            List<OrganizationCommunity> organizationCommunitys = organizationProvider.listOrganizationCommunities(organization.getId());
            for(OrganizationCommunity orgCmnty : organizationCommunitys) {
                Community community = communityProvider.findCommunityById(orgCmnty.getCommunityId());
                if(community != null) {
                    sentScopeDto = new TopicScopeDTO();
                    sentScopeDto.setId(menuId++);
                    sentScopeDto.setParentId(group2Id);
                    sentScopeDto.setName(community.getName());
                    sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
                    sentScopeDto.setForumId(community.getDefaultForumId());
                    sentScopeDto.setSceneToken(sceneToken);
                    sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                    avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                    sentScopeDto.setAvatar(avatarUri);
                    sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                    tmpSentScopeList.add(sentScopeDto);
                } else {
                    LOGGER.warn("Community not found, sceneToken=" + sceneToken + ", organizationId=" + organization.getId());
                }
            }
            
            if(tmpSentScopeList.size() > 0) {
                // 所管理的小区
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(group2Id);
                sentScopeDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                sentScopeDto.setName(menuName);
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());;
                sentScopeList.add(sentScopeDto);
                
                // 公司管理的各个小区
                sentScopeList.addAll(tmpSentScopeList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No community group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
        } else {
            LOGGER.error("Organization not in active state, sceneToken={}, orgStatue", sceneTokenDto, orgStatus);
        }
        
        return sentScopeList;
    }
}
