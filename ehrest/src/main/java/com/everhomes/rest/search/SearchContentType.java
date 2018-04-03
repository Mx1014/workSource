package com.everhomes.rest.search;


/**
 * <ul>
 * <li>ACTIVITY("activity"): </li>
 * <li>POLL("poll"): </li>
 * <li>TOPIC("topic"): </li>
 * <li>NEWS("news"): </li>
 * <li>LAUNCHPADITEM("launchpaditem"): </li>
 * <li>SHOP("shop"): </li>
 * <li>ALL("all"): </li>
 * </ul>
 */
public enum SearchContentType {
	ACTIVITY("activity"), 
    POLL("poll"), 
    TOPIC("topic"), 
    NEWS("news"),
    LAUNCHPADITEM("launchpaditem"), 
    SHOP("shop"),
	ALL("all");
    
    private String code;
       
    private SearchContentType(String code) {
        this.code = code;
    }
       
    public String getCode() {
        return this.code;
    }
       
    public static SearchContentType fromCode(String code) {
    	SearchContentType[] values = SearchContentType.values();
        for(SearchContentType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
       
        return null;
    }
}
