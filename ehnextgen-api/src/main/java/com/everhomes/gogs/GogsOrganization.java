package com.everhomes.gogs;

import com.everhomes.util.StringHelper;
import com.google.gson.annotations.SerializedName;

/**
 * {
 *   "id": 6,
 *   "username": "gogs",
 *   "full_name": "Gogs",
 *   "avatar_url": "/avatars/6",
 *   "description": "Gogs is a painless self-hosted Git Service.",
 *   "website": "https://gogs.io",
 *   "location": "USA"
 * }
 */
public class GogsOrganization {

    private Long id;
    private String username;
    @SerializedName("full_name")
    private String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
