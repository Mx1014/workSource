package com.everhomes.search;

public enum SearchSyncType {
    GROUP("group"), POST("post"), COMMUNITY("community"), ALL("all");
    
    private String code;
    private SearchSyncType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static SearchSyncType fromCode(String code) {
        for(SearchSyncType t : SearchSyncType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        
        return null;
    }
}
