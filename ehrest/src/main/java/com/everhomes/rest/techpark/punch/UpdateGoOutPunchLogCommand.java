package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * <li>description：备注 </li>
 * </ul>
 */
public class UpdateGoOutPunchLogCommand {
    private Long id;
    private String description;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
