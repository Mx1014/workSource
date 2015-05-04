// @formatter:off
package com.everhomes.forum;

/**
 * <p>帖子或评论信息：</p>
 * <ul>
 * <li>postId: 帖子或评论ID</li>
 * <li>parentPostId: 帖子或评论的父亲ID</li>
 * <li>forumId: 论坛ID</li>
 * <li>categoryId: 类型ID</li>
 * <li>visibilityScope: 可见性范围类型，{@link com.everhomes.visibility.VisibilityScope}</li>
 * <li>visibilityScopeId: 可见性范围类型对应的ID</li>
 * <li>longitude: 帖子或评论内容涉及到的经度如活动</li>
 * <li>latitude: 帖子或评论内容涉及到的纬度如活动</li>
 * <li>category_id: 帖子或评论类型ID</li>
 * <li>subject: 帖子或评论标题</li>
 * <li>content_type: 帖子或评论内容类型，{@link com.everhomes.forum.PostContentType}</li>
 * <li>content: 帖子或评论内容</li>
 * <li>embeddedType: 内嵌对象类型，{@link com.everhomes.forum.PostEmbeddedType}</li>
 * <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 * <li>isForwarded: 是否是转发帖的标记</li>
 * <li>childCount: 孩子数目，如帖子下的评论数目</li>
 * <li>forwardCount: 帖子或评论的转发数目</li>
 * <li>likeCount: 帖子或评论赞的数目</li>
 * <li>dislikeCount: 帖子或评论踩的数目</li>
 * <li>updateTime: 帖子或评论更新时间</li>
 * <li>createTime: 帖子或评论创建时间</li>
 * </ul>
 */
public class PostDTO {
	private Long postId;
	
	private Long parentPostId;
	
    private Long forumId;
    
    private Long categoryId;
    
    private Byte visibilityScope;
    
    private Long visibilityScopeId;
    
    private Double longitude;
    
    private Double latitude;
    
    private String subject;
    
    private String content;
    
    private String embeddedType;
    
    private Long embeddedId;
    
    // json encoded List<String> 
    private String embeddedJson;
    
    private Byte isForwarded;
    
    private Long childCount;
    
    private Long forwardCount;
    
    private Long likeCount;
    
    private Long dislikeCount;
    
    private String updateTime;
    
    private String createTime;
}
