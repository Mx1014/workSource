// @formatter:off
package com.everhomes.forum;

import com.everhomes.rest.activity.GetActivityDetailByIdCommand;
import com.everhomes.rest.activity.GetActivityDetailByIdResponse;
import com.everhomes.rest.activity.ListOfficialActivityByNamespaceCommand;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommand;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.forum.StickPostCommand;
import com.everhomes.rest.group.ListUserGroupPostResponse;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.ui.forum.*;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.user.User;

import java.util.List;

public interface ForumService {
    boolean isSystemForum(long forumId, Long communityId);
    PostDTO createTopic(NewTopicCommand cmd);

    /**
     * 发布后，更新帖子
     * add by yanlong.liang 2018-5-29
     */
    PostDTO updateTopic(NewTopicCommand cmd);

    /**
     * 传入创建者的id
     * @param creatorUid    该帖的创建者
     */
    PostDTO createTopic(NewTopicCommand cmd, Long creatorUid);

    PostDTO getTopic(GetTopicCommand cmd);
    List<PostDTO> getTopicById(List<Long> topicIds, List<Long> communityIds, boolean isDetail);
    List<PostDTO> getTopicById(List<Long> topicIds, Long communityId, boolean isDetail);
    List<PostDTO> getTopicById(List<Long> topicIds, Long communityId, boolean isDetail, boolean getByOwnerId);
    /**
     * 根据帖子Id获取帖子信息（不暴露给客户端）
     * @param topicId 帖子ID
     * @param communityId 用户当前所在的小区ID
     * @param isDetail 是否包含帖子详情
     * @return 帖子信息
     */
    PostDTO getTopicById(Long topicId, Long communityId, boolean isDetail);
    PostDTO getTopicById(Long topicId, Long communityId, boolean isDetail, boolean getByOwnerId);
    void deletePost(Long forumId, Long postId);
    void deletePost(Long forumId, Long postId, Long currentOrgId, String ownerType, Long ownerId);
    ListPostCommandResponse queryTopicsByEntityAndCategory(QueryTopicByEntityAndCategoryCommand cmd);
    ListPostCommandResponse queryTopicsByCategory(QueryTopicByCategoryCommand cmd);
    ListPostCommandResponse listTopics(ListTopicCommand cmd);
    ListPostCommandResponse listUserRelatedTopics(ListUserRelatedTopicCommand cmd);
    ListPostCommandResponse listActivityPostByCategoryAndTag(ListActivityTopicByCategoryAndTagCommand cmd);
    CheckUserPostDTO checkUserPostStatus(CheckUserPostCommand cmd);

    Long populateCommunityIdAndForumId(Long communityId, Long organizationId, Integer namespaceId, List<Long> communityIds, List<Long> forumIds);

    /**
     * 机构查询自己可看的帖子列表
     * @param cmd 命令
     * @return 帖子列表
     */
    ListPostCommandResponse queryOrganizationTopics(QueryOrganizationTopicCommand cmd);
    void likeTopic(LikeTopicCommand cmd);
    void cancelLikeTopic(CancelLikeTopicCommand cmd);

    void stickPost(StickPostCommand cmd);

    ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd);
    /**
     * 设置帖子是否公开
     * @param forumId 论坛ID
     * @param postId 帖子ID
     * @param privacy 公开标记
     */
    void updatePostPrivacy(Long forumId, Long postId, PostPrivacy privacy);
    PostDTO createComment(NewCommentCommand cmd);

    void sendMessageToUserWhenCommentNotSupport(User user);

    void assignTopicScope(AssignTopicScopeCommand cmd);
    List<AssignedScopeDTO> listTopicAssignedScope(ListTopicAssignedScopeCommand cmd);
    
    /**
     * 管理员搜索帖子信息
     * @param cmd 命令
     * @return 帖子列表
     */
    SearchTopicAdminCommandResponse searchTopic(SearchTopicAdminCommand cmd);

    Byte getInteractFlagByPost(Post post);

    InteractSetting findInteractSettingByPost(Post post);

    SearchTopicAdminCommandResponse searchComment(SearchTopicAdminCommand cmd);
    
    void updateUsedAndRental(UsedAndRentalCommand cmd);
    void updateFreeStuff(FreeStuffCommand cmd);
    void updateLostAndFound(LostAndFoundCommand cmd);
    
    void checkForumPostPrivilege(Long forumId);
    void checkForumModifyItemPrivilege(Long forumId, Long topicId);
    void checkForumDeleteItemPrivilege(Long forumId, Long topicId);
    void deletePost(Long forumId, Long postId, boolean deleteUserPost);
    void deletePost(Long forumId, Long postId, boolean deleteUserPost, Long currentOrgId, String ownerType, Long ownerId);
    PostDTO createTopicByScene(NewTopicBySceneCommand cmd);
    List<TopicFilterDTO> getTopicQueryFilters(GetTopicQueryFilterCommand cmd);
    List<TopicScopeDTO> getTopicSentScopes(GetTopicSentScopeCommand cmd);
    ListPostCommandResponse listTopicsByForums(ListTopicByForumCommand cmd);
    ListPostCommandResponse listOrgTopics(QueryOrganizationTopicCommand cmd);
//    ListPostCommandResponse listOfficialActivityTopics(QueryOrganizationTopicCommand cmd);
    ListPostCommandResponse listNoticeTopic(List<Long> organizationIds, List<Long> communityIds, String publishStatus, Integer pageSize, Long pageAnchor);
    ListPostCommandResponse listNoticeTopic(VisibleRegionType visibleRegionType, List<Long> visibleRegionIds, String publishStatus, Integer pageSize, Long pageAnchor);
    ListPostCommandResponse listNoticeByScene(ListNoticeBySceneCommand cmd);
    
    SearchContentsBySceneReponse searchContents(SearchContentsBySceneCommand cmd, SearchContentType contentType);
	ListPostCommandResponse listOfficialActivityByNamespace(ListOfficialActivityByNamespaceCommand cmd);
	GetActivityDetailByIdResponse getActivityDetailById(GetActivityDetailByIdCommand cmd); 
	ListUserGroupPostResponse listUserGroupPost(VisibilityScope scope, Long communityId, List<Long> forumIdList, Long userId, Long pageAnchor, Integer pageSize); 
	
	//发布暂存的帖子
	void publisTopic(PublishTopicCommand cmd);

    Forum findFourmByNamespaceId(Integer namespaceId);

    ListForumCategoryResponse listForumCategory(ListForumCategoryCommand cmd);

    ForumCategoryDTO findForumCategory(FindForumCategoryCommand cmd);

    GetForumSettingResponse getForumSetting(GetForumSettingCommand cmd);

    void updateForumSetting(UpdateForumSettingCommand cmd);

    InteractSettingDTO getInteractSetting(GetInteractSettingCommand cmd);

    void updateInteractSetting(UpdateInteractSettingCommand cmd);

    void saveInteractSetting(Integer namespaceId, Byte moduleType, Long categoryId, Byte interactFlag);

    ListForumServiceTypesResponse listForumServiceTypes(ListForumServiceTypesCommand cmd);

    CheckModuleAppAdminResponse checkForumModuleAppAdmin(CheckModuleAppAdminCommand cmd);

    FindDefaultForumResponse findDefaultForum(FindDefaultForumCommand cmd);

    ListTopicsByForumEntryIdResponse listTopicsByForumEntryId(ListTopicsByForumEntryIdCommand cmd);

    PostDTO getPreviewTopic(NewTopicCommand cmd);

}
