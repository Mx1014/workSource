package com.everhomes.forum;

import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.forum.ForumLocalStringCode;
import com.everhomes.rest.forum.OrganizationTopicMixType;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.ui.forum.SelectorBooleanFlag;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.user.User;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private OrganizationService organizationService;

    @Value("${server.contextPath:}")
    protected String serverContectPath;

    public List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if (sceneType != SceneType.PM_ADMIN) {
            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneToken);
            return null;
        } else {
            return getTopicQueryFilters(user, sceneToken, sceneToken.getEntityId());
        }
    }

    protected List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken, Long organizationId) {
        List<TopicFilterDTO> filterList = new ArrayList<TopicFilterDTO>();
        Organization organization = organizationProvider.findOrganizationById(organizationId);

        //子公司场景下，没有自己的论坛，也没有管理的园区，发现模块会出现异常
        // TODO 和产品商量 临时处理，子公司使用父公司的的场景   edit by yanjun 20170725
        while (organization != null && organization.getParentId() != null && organization.getParentId().longValue() != 0) {
            organization = organizationProvider.findOrganizationById(organization.getParentId());
        }

        if (organization == null) {
            LOGGER.error("Organization not found, sceneToken={}", sceneToken);
            return filterList;
        }

        // 由于公司删除后只是置状态，故需要判断状态是否正常 by lqs 20160430
        OrganizationStatus orgStatus = OrganizationStatus.fromCode(organization.getStatus());
        if (orgStatus == OrganizationStatus.ACTIVE) {
            String menuName = null;
            String scope = ForumLocalStringCode.SCOPE;
            String code = "";
            String actionUrl = null;
            long menuId = 1;
            String avatarUri = null;
            Integer namespaceId = sceneToken.getNamespaceId();
            // 由于有些分组没有，故默认的菜单也需要进行动态设置 by lqs 20160427
            boolean hasDefault = false;


            //去除公司圈  add  by yanjun 20171205
//            long group1Id = menuId++;
            // 本公司
//            List<TopicFilterDTO> tmpFilterList = new ArrayList<TopicFilterDTO>();
//            TopicFilterDTO filterDto = null;
//            Group group = null;
//            if(organization.getGroupId() != null) {
//                if(organization.getGroupId() != null) {
//                    group = groupProvider.findGroupById(organization.getGroupId());
//                }
//                if(group != null) {
//                    filterDto = new TopicFilterDTO();
//                    filterDto.setId(menuId++);
//                    filterDto.setParentId(group1Id);
//                    filterDto.setName(organization.getName());
//                    filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
//                    filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
//                    actionUrl = String.format("%s%s?forumId=%s&excludeCategories[0]=%s&pageSize=8", serverContectPath,
//                        "/forum/listTopics", group.getOwningForumId(), CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
//                    filterDto.setActionUrl(actionUrl);
//                    avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
//                    filterDto.setAvatar(avatarUri);
//                    filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                    filterDto.setForumId(group.getOwningForumId());
//                    tmpFilterList.add(filterDto);
//                }
//            } else {
//                LOGGER.warn("The group id of organization is null, sceneToken=" + sceneToken);
//            }


            //和产品沟通，论坛模块不显示子公司   forum-2.0分支  edit by yanjun  20170616
//            // 子公司
//            List<String> groupTypes = new ArrayList<String>();
//            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
//            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
//            if(subOrgList != null && subOrgList.size() > 0) {
//                for(Organization subOrg : subOrgList) {
//                    if(subOrg.getGroupId() != null) {
//                        if(subOrg.getGroupId() != null) {
//                            group = groupProvider.findGroupById(subOrg.getGroupId());
//                        }
//                        if(group != null) {
//                            filterDto = new TopicFilterDTO();
//                            filterDto.setId(menuId++);
//                            filterDto.setParentId(group1Id);
//                            filterDto.setName(subOrg.getName());
//                            filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
//                            filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
//                            actionUrl = String.format("%s%s?forumId=%s&excludeCategories[0]=%s&pageSize=8", serverContectPath,
//                                "/forum/listTopics", group.getOwningForumId(), CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
//                            filterDto.setActionUrl(actionUrl);
//                            avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
//                            filterDto.setAvatar(avatarUri);
//                            filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                            tmpFilterList.add(filterDto);
//                        }
//                    } else {
//                        LOGGER.warn("The group id of suborganization is null, subOrgId=" + subOrg.getId() + ", sceneToken=" + sceneToken);
//                    }
//                }
//            }

//            // 当公司和子公司都没有论坛的时候，此时不显示公司圈组 by lqs 20160426
//            if(tmpFilterList.size() > 0) {
//                // 菜单：公司圈组
//                TopicFilterDTO orgGroupFilterDto = new TopicFilterDTO();
//                orgGroupFilterDto.setId(group1Id);
//                orgGroupFilterDto.setParentId(0L);
//                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ORGANIZATION_GROUP);
//                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//                orgGroupFilterDto.setName(menuName);
//                orgGroupFilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
//                orgGroupFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
//                filterList.add(orgGroupFilterDto);
//
//                // 公司全部
//                TopicFilterDTO orgAllFilterDto = new TopicFilterDTO();
//                orgAllFilterDto.setId(menuId++);
//                orgAllFilterDto.setParentId(group1Id);
//                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ALL);
//                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//                orgAllFilterDto.setName(menuName);
//                orgAllFilterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
//                orgAllFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode()); // 整组菜单只有一个是默认的，改为默认管辖的小区全部
//                actionUrl = String.format("%s%s?organizationId=%s&mixType=%s&excludeCategories[0]=%s&pageSize=8", serverContectPath,
//                    "/org/listOrgMixTopics", organization.getId(), OrganizationTopicMixType.CHILDREN_ALL.getCode(), CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
//                orgAllFilterDto.setActionUrl(actionUrl);
//                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.all", "");
//                orgAllFilterDto.setAvatar(avatarUri);
//                orgAllFilterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                filterList.add(orgAllFilterDto);
//
//                // 公司圈及子公司圈
//                filterList.addAll(tmpFilterList);
//            } else {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.debug("No organization group filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
//                }
//            }

            long group2Id = menuId++;
            List<TopicFilterDTO> tmpFilterList = new ArrayList<TopicFilterDTO>();
            //TopicFilterDTO filterDto = null;
            //tmpFilterList.clear();
            // 公司管理的单个小区
            List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organization.getId());
            for (CommunityDTO community : communities) {
                TopicFilterDTO filterDto = new TopicFilterDTO();
                filterDto.setId(menuId++);
                filterDto.setParentId(group2Id);
                filterDto.setName(community.getName());
                filterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                filterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                String excludeCategories = "";
                try {
                    excludeCategories = URLEncoder.encode("excludeCategories[0]", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("encode excludeCategories error",e);
                }
                actionUrl = String.format("%s%s?forumId=%s&visibilityScope=%s&communityId=%s&%s=%s&pageSize=8", serverContectPath,
                        "/forum/listTopics", community.getDefaultForumId(), VisibilityScope.COMMUNITY.getCode(), community.getId(),excludeCategories, CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
                filterDto.setActionUrl(actionUrl);
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                filterDto.setAvatar(avatarUri);
                filterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                filterDto.setForumId(community.getDefaultForumId());
                tmpFilterList.add(filterDto);
            }

            if (tmpFilterList.size() > 0) {
                // 所管理的小区
                TopicFilterDTO cmntyGroupFilterDto = new TopicFilterDTO();
                cmntyGroupFilterDto.setId(group2Id);
                cmntyGroupFilterDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_PARK_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                cmntyGroupFilterDto.setName(menuName);
                cmntyGroupFilterDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                cmntyGroupFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
                filterList.add(cmntyGroupFilterDto);

                // 公司管理的全部小区
                // 军哥说不允许出现全部 这个数据 add by yanlong.liang 2018-07-20
//                TopicFilterDTO cmntyAllFilterDto = new TopicFilterDTO();
//                cmntyAllFilterDto.setId(menuId++);
//                cmntyAllFilterDto.setParentId(group2Id);
//                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ALL);
//                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//                cmntyAllFilterDto.setName(menuName);
//                cmntyAllFilterDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
//                if (hasDefault) {
//                    cmntyAllFilterDto.setDefaultFlag(SelectorBooleanFlag.FALSE.getCode());
//                } else {
//                    cmntyAllFilterDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());
//                    hasDefault = true;
//                }
//                actionUrl = String.format("%s%s?organizationId=%s&mixType=%s&excludeCategories[0]=%s&pageSize=8", serverContectPath,
//                        "/org/listOrgMixTopics", organization.getId(), OrganizationTopicMixType.COMMUNITY_ALL.getCode(), CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
//                cmntyAllFilterDto.setActionUrl(actionUrl);
//                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
//                cmntyAllFilterDto.setAvatar(avatarUri);
//                cmntyAllFilterDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                filterList.add(cmntyAllFilterDto);

                // 公司管理的各个小区
                filterList.addAll(tmpFilterList);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No organization community filter for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }
        } else {
            LOGGER.error("Organization not in active state, sceneToken={}, orgStatus={}", sceneToken, orgStatus);
        }

        return filterList;
    }

    private String getPostFilterDefaultAvatar(Integer namespaceId, Long userId, String avatarUri) {
        if (avatarUri != null) {
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
    public List<TopicScopeDTO> getTopicSentScopes(User user, SceneTokenDTO sceneTokenDto) {
        SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
        if (sceneType != SceneType.PM_ADMIN) {
            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneTokenDto);
            return null;
        } else {
            return getDiscoveryTopicSentScopes(user, sceneTokenDto, sceneTokenDto.getEntityId());
        }
    }

    protected List<TopicScopeDTO> getDiscoveryTopicSentScopes(User user, SceneTokenDTO sceneTokenDto, Long organizationId) {
        List<TopicScopeDTO> sentScopeList = new ArrayList<TopicScopeDTO>();
        Organization organization = organizationProvider.findOrganizationById(sceneTokenDto.getEntityId());

        //子公司场景下，没有自己的论坛，也没有管理的园区，发现模块会出现异常
        // TODO 和产品商量 临时处理，子公司使用父公司的的场景   edit by yanjun 20170725
        while (organization != null && organization.getParentId() != null && organization.getParentId().longValue() != 0) {
            organization = organizationProvider.findOrganizationById(organization.getParentId());
        }

        if (organization == null) {
            LOGGER.error("Organization not found, sceneToken={}", sceneTokenDto);
            return sentScopeList;
        }

        // 由于公司删除后只是置状态，故需要判断状态是否正常 by lqs 20160430
        OrganizationStatus orgStatus = OrganizationStatus.fromCode(organization.getStatus());
        if (orgStatus == OrganizationStatus.ACTIVE) {
            String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
            String menuName = null;
            String scope = ForumLocalStringCode.SCOPE;
            String code = "";
            long menuId = 1;
            String avatarUri = null;
            Integer namespaceId = sceneTokenDto.getNamespaceId();


            List<TopicScopeDTO> tmpSentScopeList = new ArrayList<TopicScopeDTO>();
            TopicScopeDTO sentScopeDto = null;

            //将小区移到上面，update by tt，160714
            long group2Id = menuId++;
            long allId = menuId++;
            // 公司管理的单个小区
            List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organization.getId());
            for (CommunityDTO community : communities) {
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(menuId++);
                sentScopeDto.setParentId(group2Id);
                sentScopeDto.setName(community.getName());
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
                ;
                sentScopeDto.setForumId(community.getDefaultForumId());
                sentScopeDto.setSceneToken(sceneToken);
                sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
                sentScopeDto.setAvatar(avatarUri);
                sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
                sentScopeDto.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
                sentScopeDto.setVisibleRegionId(community.getId());
                tmpSentScopeList.add(sentScopeDto);
            }

            if (tmpSentScopeList.size() > 0) {
                // 所管理的小区
                sentScopeDto = new TopicScopeDTO();
                sentScopeDto.setId(group2Id);
                sentScopeDto.setParentId(0L);
                code = String.valueOf(ForumLocalStringCode.POST_MEMU_PARK_GROUP);
                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
                sentScopeDto.setName(menuName);
                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
                ;
                sentScopeList.add(sentScopeDto);

                //客户端不需要全部了 add by yanjun 20171206
//                //如果大于一个小区，就加上全部
//                if (tmpSentScopeList.size() > 1) {
//                	sentScopeDto = new TopicScopeDTO();
//                    sentScopeDto.setId(allId);
//                    sentScopeDto.setParentId(group2Id);
//                    code = String.valueOf(ForumLocalStringCode.POST_MEMU_ALL);
//                    menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//                    sentScopeDto.setName(menuName);
//                    sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());;
//                    sentScopeDto.setForumId(tmpSentScopeList.get(0).getForumId());
//                    sentScopeDto.setSceneToken(sceneToken);
//                    sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
//                    avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
//                    sentScopeDto.setAvatar(avatarUri);
//                    sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                    sentScopeDto.setVisibleRegionType(VisibleRegionType.REGION.getCode());
//                    sentScopeDto.setVisibleRegionId(organization.getId());
//                    sentScopeList.add(sentScopeDto);
//				}

                // 公司管理的各个小区
                sentScopeList.addAll(tmpSentScopeList);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No community group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
                }
            }


            //long group1Id = menuId++;

            //去除公司圈  add  by yanjun 20171205
//            // 本公司
//            tmpSentScopeList.clear();
//            Group groupDto = null;
//            if(organization.getGroupId() != null) {
//                groupDto = groupProvider.findGroupById(organization.getGroupId());
//            }
//            if(groupDto != null) {
//                sentScopeDto = new TopicScopeDTO();
//                sentScopeDto.setId(menuId++);
//                sentScopeDto.setParentId(group1Id);
//                sentScopeDto.setName(organization.getName());
//                sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
//                sentScopeDto.setForumId(groupDto.getOwningForumId());
//                sentScopeDto.setSceneToken(sceneToken);
//                sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
//                avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
//                sentScopeDto.setAvatar(avatarUri);
//                sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                sentScopeDto.setVisibleRegionType(VisibleRegionType.REGION.getCode());
//                sentScopeDto.setVisibleRegionId(organization.getId());
//                tmpSentScopeList.add(sentScopeDto);
//            }

            //和产品沟通，论坛模块不显示子公司   forum-2.0分支  edit by yanjun  20170616
//            // 子公司
//            List<String> groupTypes = new ArrayList<String>();
//            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
//            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
//            if(subOrgList != null && subOrgList.size() > 0) {
//                for(Organization subOrg : subOrgList) {
//                    groupDto = null;
//                    if(subOrg.getGroupId() != null) {
//                        groupDto = groupProvider.findGroupById(subOrg.getGroupId());
//                    }
//                    if(groupDto != null) {
//                        sentScopeDto = new TopicScopeDTO();
//                        sentScopeDto.setId(menuId++);
//                        sentScopeDto.setParentId(group1Id);
//                        sentScopeDto.setName(subOrg.getName());
//                        sentScopeDto.setLeafFlag(SelectorBooleanFlag.TRUE.getCode());
//                        sentScopeDto.setForumId(groupDto.getOwningForumId());
//                        sentScopeDto.setSceneToken(sceneToken);
//                        sentScopeDto.setTargetTag(PostEntityTag.USER.getCode());
//                        avatarUri = configProvider.getValue(namespaceId, "post.menu.avatar.organization", "");
//                        sentScopeDto.setAvatar(avatarUri);
//                        sentScopeDto.setAvatarUrl(getPostFilterDefaultAvatar(namespaceId, user.getId(), avatarUri));
//                        sentScopeDto.setVisibleRegionType(VisibleRegionType.REGION.getCode());
//                        sentScopeDto.setVisibleRegionId(organization.getId());
//                        tmpSentScopeList.add(sentScopeDto);
//                    }
//                }
//            }

            //去除公司圈  add  by yanjun 20171205
//            if(tmpSentScopeList.size() > 0) {
//                // 菜单：公司圈
//                sentScopeDto = new TopicScopeDTO();
//                sentScopeDto.setId(group1Id);
//                sentScopeDto.setParentId(0L);
//                code = String.valueOf(ForumLocalStringCode.POST_MEMU_ORGANIZATION_GROUP);
//                menuName = localeStringService.getLocalizedString(scope, code, user.getLocale(), "");
//                sentScopeDto.setName(menuName);
//                sentScopeDto.setLeafFlag(SelectorBooleanFlag.FALSE.getCode());
//                sentScopeList.add(sentScopeDto);
//
//                // 本公司及各子公司
//                sentScopeList.addAll(tmpSentScopeList);
//            } else {
//                if(LOGGER.isDebugEnabled()) {
//                    LOGGER.debug("No organization group topic sent scope for the user, userId={}, sceneToken={}", user.getId(), sceneToken);
//                }
//            }


        } else {
            LOGGER.error("Organization not in active state, sceneToken={}, orgStatue", sceneTokenDto, orgStatus);
        }

        //保证整个菜单列表有且只有一个默认的叶子节点 如果上面创建菜单的时候没有指定该默认的叶子节点，则拿第一个叶子节点作为默认节点 by lqs 20160525
        setDefaultLeafMenu(sentScopeList);

        return sentScopeList;
    }

    private void setDefaultLeafMenu(List<TopicScopeDTO> sentScopeList) {
        TopicScopeDTO firstUndefaultScopeDto = null;
        boolean hasDefault = false;
        for (TopicScopeDTO sentScopeDto : sentScopeList) {
            if (SelectorBooleanFlag.fromCode(sentScopeDto.getLeafFlag()) == SelectorBooleanFlag.TRUE) {
                if (SelectorBooleanFlag.fromCode(sentScopeDto.getDefaultFlag()) == SelectorBooleanFlag.TRUE) {
                    hasDefault = true;
                    break;
                } else {
                    if (firstUndefaultScopeDto == null) {
                        firstUndefaultScopeDto = sentScopeDto;
                    }
                }
            }
        }

        if (!hasDefault && firstUndefaultScopeDto != null) {
            firstUndefaultScopeDto.setDefaultFlag(SelectorBooleanFlag.TRUE.getCode());
        }
    }
}
