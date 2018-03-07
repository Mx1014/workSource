package com.everhomes.rest.banner.targetdata;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entryId: 入口id</li>
 *     <li>entryName: 入口名称</li>
 *     <li>postId: 帖子id</li>
 *     <li>postName: 帖子名称</li>
 * </ul>
 */
public class BannerPostTargetData {

    private Long entryId;
    private String entryName;
    private Long postId;
    private String postName;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
