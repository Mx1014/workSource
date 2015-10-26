package com.everhomes.enterprise;

import java.sql.Timestamp;

/**
 * <ul> 公司信息
 * <li>id: Enterprise Id</li>
 * <li>name: Enterprise Name</li>
 * <li>displayName: Enterprise display name</li>
 * <li>avatarUri: the Id from content server for Enterprise image</li>
 * <li>avatarUri: enterprise url</li>
 * <li>description: enterprise description</li>
 * <li>memberCount: members' count</li>
 * @author janson
 *
 */
public class EnterpriseDTO {
    private Long id;
    private String name;
    private String displayName;
    private String avatarUri;
    private String avatarUrl;
    private String description;
    private Long memberCount;
    private Timestamp createTime;
    
    
    //TODO address info ?
    //List<AddressDTO>
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getAvatarUri() {
        return avatarUri;
    }
    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getMemberCount() {
        return memberCount;
    }
    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
