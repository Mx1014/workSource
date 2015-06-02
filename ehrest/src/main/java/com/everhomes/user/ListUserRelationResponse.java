package com.everhomes.user;

public class ListUserRelationResponse<T> {
    private T values;
    private Long nextAnchor;
    public Long getNextAnchor() {
        return nextAnchor;
    }
    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }
    public T getValues() {
        return values;
    }
    public void setValues(T values) {
        this.values = values;
    }
    
    
}
