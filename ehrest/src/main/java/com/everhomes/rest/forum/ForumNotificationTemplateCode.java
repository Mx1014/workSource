package com.everhomes.rest.forum;

public class ForumNotificationTemplateCode {
    public static final String SCOPE = "forum.notification";
    public static final int FORUM_REPLAY_ONE_TO_CREATOR = 1; //有人回复话题/活动/投票，通知话题/活动/投票发起者
    public static final int FORUM_COMMENT_TO_CREATOR = 2; //有人评论了你的帖子\t${userName} 评论了你的帖子 ${postName}。		使用\t分割标题和内容模板
    public static final int FORUM_COMMENT_TO_PARENT = 3; //有人回复了你的评论\t${userName} 回复了你在帖子 ${postName} 的评论。	使用\t分割标题和内容模板
}
