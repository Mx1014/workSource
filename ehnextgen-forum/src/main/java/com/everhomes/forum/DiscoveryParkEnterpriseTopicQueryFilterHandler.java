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
import com.everhomes.group.GroupService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.ForumLocalStringCode;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.group.ListPublicGroupCommand;
import com.everhomes.rest.ui.forum.SelectorBooleanFlag;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.user.User;

@Component(TopicQueryFilterHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_park_enterprise")
public class DiscoveryParkEnterpriseTopicQueryFilterHandler implements TopicQueryFilterHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumServiceImpl.class);
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private OrganizationProvider organizationProvider;

    @Value("${server.contextPath:}")
    private String serverContectPath;
    
    public List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        Community community = null;
        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
        switch(entityType) {
        case COMMUNITY_RESIDENTIAL:
        case COMMUNITY_COMMERCIAL:
        case COMMUNITY:
            LOGGER.error("Unsupported scene of community for simple user, sceneToken=" + sceneToken);
            break;
        case FAMILY:
            LOGGER.error("Unsupported scene of family for simple user, sceneToken=" + sceneToken);
            break;
        case ORGANIZATION:
            Organization organization = organizationProvider.findOrganizationById(sceneToken.getEntityId());
            if(organization != null) {
                if(organization.getCommunityId() != null) {
                    community = communityProvider.findCommunityById(organization.getCommunityId());
                } else {
                    LOGGER.error("No community id found in organization, sceneToken=" + sceneToken);
                }
            } else {
                LOGGER.error("Organization not found, sceneToken=" + sceneToken);
            }
            break;
        default:
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
            break;
        }
        
        String menuName = null;
        String scope = ForumLocalStringCode.SCOPE;
        String code = "";
        String actionUrl = null;
        String avatarUri = null;
        Integer namespaceId = sceneToken.getNamespaceId();
        List<TopicFilterDTO> filterList = new ArrayList<TopicFilterDTO>();
        if(community != null) {
            long menuId = 1;
            
            // 菜单：小区圈
//            long group1Id = menuId++;
//            TopicFilterDTO filterDto = new TopicFilterDTO();
//            filterDto.setId(group1Id);
//            filterDto.setParentId(0L);
//            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_GROUP);
//            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//            filterDto.setName(menuName);
//            filterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
//            filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());;
//            filterList.add(filterDto);

            // 菜单：周边小区 （园区场景暂无周边小区）
//            filterDto = new TopicFilterDTO();
//            filterDto.setId(menuId++);
//            filterDto.setParentId(group1Id);
//            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_NEARBY);
//            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//            filterDto.setName(menuName);
//            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
//            filterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());; // 整组菜单只有一个是默认的
//            actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s", serverContectPath, 
//                "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.NEARBY_COMMUNITIES.getCode(), community.getId());
//            filterDto.setActionUrl(actionUrl);
//            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_nearby", "");
//            filterDto.setAvatar(avatarUri);
//            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//            filterList.add(filterDto);

            // 菜单：园区
            TopicFilterDTO filterDto = new TopicFilterDTO();
            filterDto.setId(menuId++);
            filterDto.setParentId(0L);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_PARK_ONLY);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            filterDto.setName(menuName);
            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
            filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
            actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s", serverContectPath, 
                "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.COMMUNITY.getCode(), community.getId());
            filterDto.setActionUrl(actionUrl);
            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_only", "");
            filterDto.setAvatar(avatarUri);
            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
            filterList.add(filterDto);


            long group2Id = menuId++;
            // 各兴趣圈
            List<TopicFilterDTO> tmpFilterList = new ArrayList<TopicFilterDTO>();
            ListPublicGroupCommand groupCmd = new ListPublicGroupCommand();
            groupCmd.setUserId(user.getId());
            List<GroupDTO> groupList = groupService.listPublicGroups(groupCmd);
            if(groupList != null && groupList.size() > 0) {
                for(GroupDTO groupDto : groupList) {
                    filterDto = new TopicFilterDTO();
                    filterDto.setId(menuId++);
                    filterDto.setParentId(group2Id);
                    filterDto.setName(groupDto.getName());
                    filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
                    filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());;
                    actionUrl = String.format("%s%s?forumId=%s&communityId=%s", serverContectPath, 
                        "/forum/listTopics", groupDto.getOwningForumId(), community.getId());
                    filterDto.setActionUrl(actionUrl);
                    avatarUri = groupDto.getAvatar();
                    if(avatarUri == null || avatarUri.trim().length() == 0) {
                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.all", "");
                    }
                    filterDto.setAvatar(avatarUri);
                    filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                    tmpFilterList.add(filterDto);
                }
            }
            // 当没有孩子节点时，组节点也不要显示 by lqs 20160426
            if(tmpFilterList.size() > 0) {
                // 菜单：兴趣圈组
                TopicFilterDTO group2FilterDto = new TopicFilterDTO();
                group2FilterDto.setId(group2Id);
                group2FilterDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_INTEREST_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                group2FilterDto.setName(menuName);
                group2FilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                group2FilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                filterList.add(group2FilterDto);
                
                // 各兴趣圈
                filterList.addAll(tmpFilterList);
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No interest group filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
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
}
