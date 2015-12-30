package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 附件ID</li>
 * <li>ownerId: 帖子ID</li>
 * <li>scopeCode: 范围类型，参考{@link com.everhomes.rest.forum.ForumAssignedScopeCode}</li>
 * <li>scopeId: 范围类型对应的ID</li>
 * <li>scopeName: 城市名称、小区名称等</li>
 * </ul>
 */
public class AssignedScopeDTO {
    private Long id;
    private Long ownerId;
    private Byte scopeCode;
    private Long scopeId;
    private String scopeName;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    
    public Byte getScopeCode() {
        return scopeCode;
    }
    
    public void setScopeCode(Byte scopeCode) {
        this.scopeCode = scopeCode;
    }
    
    public Long getScopeId() {
        return scopeId;
    }
    
    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }
    
    public String getScopeName() {
        return scopeName;
    }
    
    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
