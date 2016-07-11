package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class OpPromotionAssignedScopeDTO {
    private Byte     scopeCode;
    private Long     scopeId;
    private Long     id;
    private Long     promotionId;
    private String name;

    

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



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public Long getPromotionId() {
        return promotionId;
    }



    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}