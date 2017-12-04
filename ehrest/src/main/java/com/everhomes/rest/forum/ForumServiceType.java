package com.everhomes.rest.forum;


/**
 *<ul>
 *<li>ACTIVITY("activity"):活动</li>
 *<li>TOPIC("topic"):活动</li>
 *<li>POLL("poll"):活动</li>
 *</ul>
 */
public enum ForumServiceType {

	ACTIVITY("activity"), TOPIC("topic"), POLL("poll");

    private String code;
    private ForumServiceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

    public static ForumServiceType fromCode(String code) {

        if(code == null){
            return null;
        }

        for(ForumServiceType v : ForumServiceType.values()) {
            if(v.getCode().equals(code))
                return v;
        }
        return null;
    }

}
