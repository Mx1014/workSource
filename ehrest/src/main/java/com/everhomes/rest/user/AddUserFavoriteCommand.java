package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>targetType:收藏类型,topic-帖子,biz-商家</li>
 *<li>targetId:收藏对象的Id</li>
 *<ul>
 */
public class AddUserFavoriteCommand {
    private String targetType;
    private Long targetId;
    public String getTargetType() {
        return targetType;
    }
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    
    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }
}
