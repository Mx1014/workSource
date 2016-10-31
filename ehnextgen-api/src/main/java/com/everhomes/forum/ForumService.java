// @formatter:off
package com.everhomes.forum;

import java.util.List;

import com.everhomes.rest.activity.GetActivityDetailByIdCommand;
import com.everhomes.rest.activity.GetActivityDetailByIdResponse;
import com.everhomes.rest.activity.ListOfficialActivityByNamespaceCommand;
import com.everhomes.rest.forum.AssignTopicScopeCommand;
import com.everhomes.rest.forum.AssignedScopeDTO;
import com.everhomes.rest.forum.CancelLikeTopicCommand;
import com.everhomes.rest.forum.CheckUserPostCommand;
import com.everhomes.rest.forum.CheckUserPostDTO;
import com.everhomes.rest.forum.FreeStuffCommand;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.LikeTopicCommand;
import com.everhomes.rest.forum.ListActivityTopicByCategoryAndTagCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.ListTopicAssignedScopeCommand;
import com.everhomes.rest.forum.ListTopicByForumCommand;
import com.everhomes.rest.forum.ListTopicCommand;
import com.everhomes.rest.forum.ListTopicCommentCommand;
import com.everhomes.rest.forum.ListUserRelatedTopicCommand;
import com.everhomes.rest.forum.LostAndFoundCommand;
import com.everhomes.rest.forum.NewCommentCommand;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PostPrivacy;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.forum.QueryTopicByCategoryCommand;
import com.everhomes.rest.forum.QueryTopicByEntityAndCategoryCommand;
import com.everhomes.rest.forum.UsedAndRentalCommand;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommand;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.group.ListUserGroupPostResponse;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.ui.forum.GetTopicQueryFilterCommand;
import com.everhomes.rest.ui.forum.GetTopicSentScopeCommand;
import com.everhomes.rest.ui.forum.ListNoticeBySceneCommand;
import com.everhomes.rest.ui.forum.NewTopicBySceneCommand;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.visibility.VisibleRegionType;

public interface ForumService {
    boolean isSystemForum(long forumId, Long communityId);
    PostDTO createTopic(NewTopicCommand cmd);
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
    ListPostCommandResponse queryTopicsByEntityAndCategory(QueryTopicByEntityAndCategoryCommand cmd);
    ListPostCommandResponse queryTopicsByCategory(QueryTopicByCategoryCommand cmd);
    ListPostCommandResponse listTopics(ListTopicCommand cmd);
    ListPostCommandResponse listUserRelatedTopics(ListUserRelatedTopicCommand cmd);
    ListPostCommandResponse listActivityPostByCategoryAndTag(ListActivityTopicByCategoryAndTagCommand cmd);
    CheckUserPostDTO checkUserPostStatus(CheckUserPostCommand cmd);
    /**
     * 机构查询自己可看的帖子列表
     * @param cmd 命令
     * @return 帖子列表
     */
    ListPostCommandResponse queryOrganizationTopics(QueryOrganizationTopicCommand cmd);
    void likeTopic(LikeTopicCommand cmd);
    void cancelLikeTopic(CancelLikeTopicCommand cmd);

    ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd);
    /**
     * 设置帖子是否公开
     * @param forumId 论坛ID
     * @param postId 帖子ID
     * @param privacy 公开标记
     */
    void updatePostPrivacy(Long forumId, Long postId, PostPrivacy privacy);
    PostDTO createComment(NewCommentCommand cmd);
    void assignTopicScope(AssignTopicScopeCommand cmd);
    List<AssignedScopeDTO> listTopicAssignedScope(ListTopicAssignedScopeCommand cmd);
    
    /**
     * 管理员搜索帖子信息
     * @param cmd 命令
     * @return 帖子列表
     */
    SearchTopicAdminCommandResponse searchTopic(SearchTopicAdminCommand cmd);
    SearchTopicAdminCommandResponse searchComment(SearchTopicAdminCommand cmd);
    
    void updateUsedAndRental(UsedAndRentalCommand cmd);
    void updateFreeStuff(FreeStuffCommand cmd);
    void updateLostAndFound(LostAndFoundCommand cmd);
    
    void checkForumPostPrivilege(Long forumId);
    void checkForumModifyItemPrivilege(Long forumId, Long topicId);
    void checkForumDeleteItemPrivilege(Long forumId, Long topicId);
    void deletePost(Long forumId, Long postId, boolean deleteUserPost);
    PostDTO createTopicByScene(NewTopicBySceneCommand cmd);
    List<TopicFilterDTO> getTopicQueryFilters(GetTopicQueryFilterCommand cmd);
    List<TopicScopeDTO> getTopicSentScopes(GetTopicSentScopeCommand cmd);
    ListPostCommandResponse listTopicsByForums(ListTopicByForumCommand cmd);
    ListPostCommandResponse listOrgTopics(QueryOrganizationTopicCommand cmd);
    ListPostCommandResponse listOfficialActivityTopics(QueryOrganizationTopicCommand cmd);
    ListPostCommandResponse listNoticeTopic(VisibleRegionType visibleRegionType, List<Long> visibleRegionIds, String publishStatus, Integer pageSize, Long pageAnchor);
    ListPostCommandResponse listNoticeByScene(ListNoticeBySceneCommand cmd);
    
    SearchContentsBySceneReponse searchContents(SearchContentsBySceneCommand cmd, SearchContentType contentType);
	ListPostCommandResponse listOfficialActivityByNamespace(ListOfficialActivityByNamespaceCommand cmd);
	GetActivityDetailByIdResponse getActivityDetailById(GetActivityDetailByIdCommand cmd); 
	ListUserGroupPostResponse listUserGroupPost(VisibilityScope scope, Long communityId, List<Long> forumIdList, Long userId, Long pageAnchor, Integer pageSize); 
}
