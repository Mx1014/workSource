package com.everhomes.forum;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.user.AppContextGenerator;
import com.everhomes.user.UserContext;
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
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.ForumLocalStringCode;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.group.ListPublicGroupCommand;
import com.everhomes.rest.ui.forum.SelectorBooleanFlag;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.user.User;
import com.everhomes.util.WebTokenGenerator;

@Component(PostSceneHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_park_tourist")
public class DiscoveryParkTouristPostSceneHandler implements PostSceneHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryParkTouristPostSceneHandler.class);
    
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
    protected ContentServerService contentServerService;

    @Value("${server.contextPath:}")
    protected String serverContectPath;
    
    public List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        List<TopicFilterDTO> filterList = null;
        
//        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
//        if(sceneType != SceneType.PARK_TOURIST) {
//            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneToken);
//            return filterList;
//        }

        //标准版
        AppContext appContext = UserContext.current().getAppContext();
        if(appContext == null || appContext.getCommunityId() == null){
            LOGGER.error("Unsupported scene for simple user, appContext={}", appContext);
            return filterList;
        }


        Community community = communityProvider.findCommunityById(appContext.getCommunityId());
        if(community != null) {
            filterList = getTopicQueryFilters(user, null, community);
        } else {
            LOGGER.error("Community not found, communityId={}, appContext={}", appContext.getCommunityId(), appContext);
        }
        
        return filterList;
    }
    
    protected List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken, Community community) {
        String menuName = null;
        String scope = ForumLocalStringCode.SCOPE;
        String code = "";
        String actionUrl = null;
        String avatarUri = null;
        //Integer namespaceId = sceneToken.getNamespaceId();

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<TopicFilterDTO> filterList = new ArrayList<TopicFilterDTO>();
        if(community != null) {
            long menuId = 1;
          //产品要求只留园区 mod by xiongying 20160601
            // 菜单：小区圈
//            long group1Id = menuId++;
            TopicFilterDTO filterDto = new TopicFilterDTO();
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
            filterDto = new TopicFilterDTO();
            filterDto.setId(menuId++);
            filterDto.setParentId(0L);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_PARK_ONLY);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            filterDto.setName(menuName);
            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
            filterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());
            actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s&excludeCategories[0]=%s", serverContectPath, 
                "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.COMMUNITY.getCode(), community.getId(), CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
            filterDto.setActionUrl(actionUrl);
            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_only", "");
            filterDto.setAvatar(avatarUri);
            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
            filterDto.setForumId(community.getDefaultForumId());
            filterList.add(filterDto);


//            long group2Id = menuId++;
//            // 各兴趣圈
//            List<TopicFilterDTO> tmpFilterList = new ArrayList<TopicFilterDTO>();
//            ListPublicGroupCommand groupCmd = new ListPublicGroupCommand();
//            groupCmd.setUserId(user.getId());
//            List<GroupDTO> groupList = groupService.listPublicGroups(groupCmd);
//            if(groupList != null && groupList.size() > 0) {
//                for(GroupDTO groupDto : groupList) {
//                    filterDto = new TopicFilterDTO();
//                    filterDto.setId(menuId++);
//                    filterDto.setParentId(group2Id);
//                    filterDto.setName(groupDto.getName());
//                    filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
//                    filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());;
//                    actionUrl = String.format("%s%s?forumId=%s&communityId=%s", serverContectPath, 
//                        "/forum/listTopics", groupDto.getOwningForumId(), community.getId());
//                    filterDto.setActionUrl(actionUrl);
//                    avatarUri = groupDto.getAvatar();
//                    if(avatarUri == null || avatarUri.trim().length() == 0) {
//                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.all", "");
//                    }
//                    filterDto.setAvatar(avatarUri);
//                    filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                    tmpFilterList.add(filterDto);
//                }
//            }
//            // 当没有孩子节点时，组节点也不要显示 by lqs 20160426
//            if(tmpFilterList.size() > 0) {
//                // 菜单：兴趣圈组
//                TopicFilterDTO group2FilterDto = new TopicFilterDTO();
//                group2FilterDto.setId(group2Id);
//                group2FilterDto.setParentId(0L);
//                code = String.valueOf(ForumLocalStringCode.POST_MEMU_INTEREST_GROUP);
//                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//                group2FilterDto.setName(menuName);
//                group2FilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
//                group2FilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
//                filterList.add(group2FilterDto);
//                
//                // 各兴趣圈
//                filterList.addAll(tmpFilterList);
//            } else {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.debug("No interest group filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
//                }
//            }
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
    public List<TopicScopeDTO> getTopicSentScopes(User user, SceneTokenDTO sceneToken) {
        List<TopicScopeDTO> filterList = new ArrayList<TopicScopeDTO>();
        
        //SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        //if(sceneType == SceneType.PARK_TOURIST) {

        AppContext appContext = UserContext.current().getAppContext();
        Community community = communityProvider.findCommunityById(appContext.getCommunityId());
            if(community != null) {
                filterList = getDiscoveryTopicSentScopes(user, null, community);
            } else {
                LOGGER.error("Community not found, communityId={}, appContext={}", appContext.getCommunityId(), appContext);
            }
//        } else {
//            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
//        }
        
        return filterList;
    }
    
    protected List<TopicScopeDTO> getDiscoveryTopicSentScopes(User user, SceneTokenDTO sceneTokenDto, Community community) {
        List<TopicScopeDTO> scopeList = new ArrayList<TopicScopeDTO>();
        String menuName = null;
        String scope = ForumLocalStringCode.SCOPE;
        String code = "";
        String avatarUri = null;
        //Integer namespaceId = sceneTokenDto.getNamespaceId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if(community != null) {
            //String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
            long menuId = 1;
            // 菜单：园区圈
            long group1Id = menuId++;
            TopicScopeDTO sentScopeDto = new TopicScopeDTO();
            sentScopeDto.setId(group1Id);
            sentScopeDto.setParentId(0L);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_PARK_GROUP);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            sentScopeDto.setName(menuName);
            sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());;
            scopeList.add(sentScopeDto);

            // 菜单：周边小区
//            sentScopeDto = new TopicScopeDTO();
//            sentScopeDto.setId(menuId++);
//            sentScopeDto.setParentId(group1Id);
//            code = String.valueOf(ForumLocalStringCode.POST_MEMU_COMMUNITY_NEARBY);
//            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//            sentScopeDto.setName(menuName);
//            sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
//            sentScopeDto.setForumId(community.getDefaultForumId());
//            sentScopeDto.setSceneToken(sceneToken);
//            sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
//            String avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_nearby", "");
//            sentScopeDto.setAvatar(avatarUri);
//            sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//            scopeList.add(sentScopeDto);

            // 菜单：本小区
            sentScopeDto = new TopicScopeDTO();
            sentScopeDto.setId(menuId++);
            sentScopeDto.setParentId(group1Id);
            code = String.valueOf(ForumLocalStringCode.POST_MEMU_PARK_ONLY);
            menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
            sentScopeDto.setName(menuName);
            sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
            sentScopeDto.setForumId(community.getDefaultForumId());
            AppContext appContext = UserContext.current().getAppContext();

            String appcontextToken = AppContextGenerator.toBase64WebToken(appContext);
            sentScopeDto.setSceneToken(appcontextToken);
            sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.community_only", "");
            sentScopeDto.setAvatar(avatarUri);
            sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
            sentScopeDto.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
            sentScopeDto.setVisibleRegionId(community.getId());
            scopeList.add(sentScopeDto);

            // 各兴趣圈
//            long group2Id = menuId++;
//            List<TopicScopeDTO> tmpFilterList = new ArrayList<TopicScopeDTO>();
//            ListPublicGroupCommand groupCmd = new ListPublicGroupCommand();
//            groupCmd.setUserId(user.getId());
//            List<GroupDTO> groupList = groupService.listPublicGroups(groupCmd);
//            if(groupList != null && groupList.size() > 0) {
//                for(GroupDTO groupDto : groupList) {
//                    sentScopeDto = new TopicScopeDTO();
//                    sentScopeDto.setId(menuId++);
//                    sentScopeDto.setParentId(group2Id);
//                    sentScopeDto.setName(groupDto.getName());
//                    sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
//                    sentScopeDto.setForumId(groupDto.getOwningForumId());
//                    sentScopeDto.setSceneToken(sceneToken);
//                    sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
//                    if(groupDto.getAvatar() != null) {
//                        sentScopeDto.setAvatar(groupDto.getAvatar());
//                        sentScopeDto.setAvatarUrl(groupDto.getAvatarUrl());
//                    } else {
//                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.group", "");
//                        sentScopeDto.setAvatar(avatarUri);
//                        sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                    }
//                    tmpFilterList.add(sentScopeDto);
//                }
//            }
//            
//            if(tmpFilterList.size() > 0) {
//                // 菜单：兴趣圈
//                sentScopeDto = new TopicScopeDTO();
//                sentScopeDto.setId(group2Id);
//                sentScopeDto.setParentId(0L);
//                code = String.valueOf(ForumLocalStringCode.POST_MEMU_INTEREST_GROUP);
//                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//                sentScopeDto.setName(menuName);
//                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());;
//                scopeList.add(sentScopeDto);
//                
//                // 各兴趣圈
//                scopeList.addAll(tmpFilterList);
//            } else {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.debug("No interest group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
//                }
//            }
        }
        
        //保证整个菜单列表有且只有一个默认的叶子节点 如果上面创建菜单的时候没有指定该默认的叶子节点，则拿第一个叶子节点作为默认节点 by lqs 20160525
        setDefaultLeafMenu(scopeList);
        
        return scopeList;
    }
    
    private void setDefaultLeafMenu(List<TopicScopeDTO> sentScopeList) {
    	TopicScopeDTO firstUndefaultScopeDto = null;
        boolean hasDefault = false;
        for(TopicScopeDTO sentScopeDto : sentScopeList) {
        	if(SelectorBooleanFlag.fromCode(sentScopeDto.getLeafFlag()) == SelectorBooleanFlag.TRUE) {
        		if(SelectorBooleanFlag.fromCode(sentScopeDto.getDefaultFlag()) == SelectorBooleanFlag.TRUE) {
        			hasDefault = true;
        			break;
        		} else {
        			if(firstUndefaultScopeDto == null) {
        				firstUndefaultScopeDto = sentScopeDto;
        			}
        		}
        	}
        }
        
        if(!hasDefault && firstUndefaultScopeDto != null) {
        	firstUndefaultScopeDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());
        }
    }
}
