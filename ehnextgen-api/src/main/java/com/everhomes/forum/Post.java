package com.everhomes.forum;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Post represents post (both original and replied) to a forum
 * 
 * @author Kelven Yang
 *
 */
public class Post extends EhForumPosts {
    private static final long serialVersionUID = 3902864739541467548L;

    private Long contentCategory;
    
    private Long actionCategory;

    private List<Post> commentPosts = new ArrayList<Post>();
    private List<Attachment> attachments = new ArrayList<Attachment>();
    
    private String creatorNickName;
    
    private String creatorAvatar;
    
    private String creatorAvatarUrl;
    
    private Byte creatorAdminFlag;
    
    private String creatorCommunityName;
    
    private String forumName;
    
    private Byte likeFlag;
    
    private Byte favoriteFlag;
    
    private String shareUrl;
    
    private Long communityId;

    private Integer namespaceId;

    private String ownerToken;

    public Post() {
    }

    public List<Post> getCommentPosts() {
        return commentPosts;
    }

    public void setCommentPosts(List<Post> commentPosts) {
        this.commentPosts = commentPosts;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
    
    public String getCreatorNickName() {
        return ForumPostCustomField.CREATOR_NICK_NAME.getStringValue(this);
    }

    public void setCreatorNickName(String creatorNickName) {
        this.creatorNickName = creatorNickName;
        ForumPostCustomField.CREATOR_NICK_NAME.setStringValue(this, creatorNickName);
    }

    public String getCreatorAvatar() {
        return ForumPostCustomField.CREATOR_AVTAR.getStringValue(this);
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
        ForumPostCustomField.CREATOR_AVTAR.setStringValue(this, creatorAvatar);
    }

    public String getCreatorAvatarUrl() {
        return this.creatorAvatarUrl;
    }

    public void setCreatorAvatarUrl(String creatorAvatarUrl) {
        this.creatorAvatarUrl = creatorAvatarUrl;
    }

    public Byte getCreatorAdminFlag() {
        return creatorAdminFlag;
    }

    public void setCreatorAdminFlag(Byte creatorAdminFlag) {
        this.creatorAdminFlag = creatorAdminFlag;
    }

    public String getCreatorCommunityName() {
		return creatorCommunityName;
	}

	public void setCreatorCommunityName(String creatorCommunityName) {
		this.creatorCommunityName = creatorCommunityName;
	}

	public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public Byte getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(Byte likeFlag) {
        this.likeFlag = likeFlag;
    }

    public Byte getFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(Byte favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
    }

    public Long getContentCategory() {
        this.contentCategory = getCategoryId(); 
        return this.contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
        setCategoryId(contentCategory);
    }

    public Long getActionCategory() {
        this.actionCategory = ForumPostCustomField.ACTION_CATEGORY_ID.getIntegralValue(this);
        return this.actionCategory;
    }
    
    public void setActionCategory(Long actionCategoryId) {
        this.actionCategory = actionCategoryId;
        ForumPostCustomField.ACTION_CATEGORY_ID.setIntegralValue(this, actionCategoryId);
    }
    
    public String getActionCategoryPath() {
        return ForumPostCustomField.ACTION_CATEGORY_PATH.getStringValue(this);
    }
    
    public void setActionCategoryPath(String actionCategoryPath) {
        ForumPostCustomField.ACTION_CATEGORY_PATH.setStringValue(this, actionCategoryPath);
    }
    
    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
