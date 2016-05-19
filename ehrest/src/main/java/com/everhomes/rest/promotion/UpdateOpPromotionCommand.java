package com.everhomes.rest.promotion;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class UpdateOpPromotionCommand {
    @NotNull
    private Long     id;
    
    @NotNull
    private String     title;
    
    @NotNull
    private String     description;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
