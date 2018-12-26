package com.everhomes.rest.enterprisemoment;

import com.everhomes.rest.comment.CommentDTO;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>id: id</li>
 * <li>content: 内容</li>
 * <li>contentType: 内容类型 目前都是text,参考{@link com.everhomes.rest.enterprisemoment.ContentType}</li>
 * <li>tagId: 标签id</li>
 * <li>tagName: 标签名称</li>
 * <li>scopes: 可见范围列表 参考{@link com.everhomes.rest.enterprisemoment.MomentScopeDTO}</li>
 * <li>longitude: 经度</li>
 * <li>latitude: 纬度</li>
 * <li>location: 坐标信息</li>
 * <li>likeFlag: 是否点了赞 0-否 1-是</li>
 * <li>likeCount: 点赞数</li>
 * <li>commentCount: 评论数</li>
 * <li>creatorUid: 创建人id</li>
 * <li>creatorDetailId: 创建人detailid</li>
 * <li>creatorName: 创建人名称</li>
 * <li>createTime: 创建时间</li>
 * <li>creatorAvatarUrl: 创建人头像</li>
 * <li>attachments: 附件列表 目前都是图片 最多九张可能为空  参考{@link com.everhomes.rest.enterprisemoment.MommentAttachmentDTO}</li>
 * <li>comments: 评论列表 只顺序展示30条 参考{@link com.everhomes.rest.comment.CommentDTO}</li>
 * <li>favourites: 点赞列表 只展示3条 {@link com.everhomes.rest.enterprisemoment.FavouriteDTO}</li>
 * <li>ownerToken: 用于评论接口参数</li>
 * <li>editAble: 是否可以评论点赞(0-不可以 1-可以)</li>
 * </ul>
 */
public class MomentDTO {
    private Long id;
    private String content;
    private String contentType;
    private Long tagId;
    private String tagName;
    private List<MomentScopeDTO> scopes;
    private Double longitude;
    private Double latitude;
    private String geohash;
    private String location;
    private Byte likeFlag;
    private Integer likeCount;
    private Integer commentCount;
    private String creatorName;
    private Long creatorUid;
    private Long creatorDetailId;
    private Long createTime;
    private String creatorAvatarUrl;
    private List<MommentAttachmentDTO> attachments;
    private List<CommentDTO> comments;
    private List<FavouriteDTO> favourites;
    private String ownerToken;
    private Byte editAble;
    
    public MomentDTO() {
        this.attachments = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.favourites = new ArrayList<>();
        this.editAble = (byte) 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<MomentScopeDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<MomentScopeDTO> scopes) {
        this.scopes = scopes;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Byte getLikeFlag() {
		return likeFlag;
	}

	public void setLikeFlag(Byte likeFlag) {
		this.likeFlag = likeFlag;
	}

	public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getCreatorDetailId() {
        return creatorDetailId;
    }

    public void setCreatorDetailId(Long creatorDetailId) {
        this.creatorDetailId = creatorDetailId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreatorAvatarUrl() {
        return creatorAvatarUrl;
    }

    public void setCreatorAvatarUrl(String creatorAvatarUrl) {
        this.creatorAvatarUrl = creatorAvatarUrl;
    }

    public List<MommentAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MommentAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<FavouriteDTO> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<FavouriteDTO> favourites) {
        this.favourites = favourites;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public Byte getEditAble() {
		return editAble;
	}

	public void setEditAble(Byte editAble) {
		this.editAble = editAble;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
