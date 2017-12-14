package com.everhomes.rest.point;

@Deprecated
public enum PointType {
    //add point for each item,so you can do anything easy
    ADDRESS_APPROVAL("point.address.approval"), CREATE_TOPIC("point.post.created"), CREATE_COMMENT("point.comment.created"), APP_OPENED("point.app.opened"), AVATAR("point.avatar.approval"), INVITED_USER("point.user.invited"), OTHER("point.other");
    private String val;
    PointType(String value){
        val=value;
    }
    
    public String getValue(){
        return this.val;
    }
    
    public static PointType fromCode(String code) {
        if (code == null)
            return OTHER;
        for (PointType t : PointType.values()) {
            if (t.name().equals(code.toUpperCase())) {
                return t;
            }
        }
        return OTHER;
    }
}
