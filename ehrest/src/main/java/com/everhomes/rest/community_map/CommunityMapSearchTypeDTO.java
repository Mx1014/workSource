package com.everhomes.rest.community_map;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/14.
 */
public class CommunityMapSearchTypeDTO {
    private Long id;

    private String name;

    private String contentType;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
