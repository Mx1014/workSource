// @formatter:off
package com.everhomes.rest.community;

/**
 * <ul>
 * <li>NOCOLLECTING: ：自己已经爬完</li>
 * <li>COLLECTED: 用户收集完整</li>
 * <li>BECOLLECTING: 放开允许用户收集</li>
 * <li>COLLECTING: 用户正在收集</li>
 * </ul>
 */
public enum RequestStatus {
    NOCOLLECTING(1,"系统已预置门牌号"),COLLECTED(2,"门牌号已完整") ,BECOLLECTING(3,"门牌号待收集") ,
    COLLECTING(4,"门牌号正在收集") ;
    
    private long code;
    private String name;
    private RequestStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static RequestStatus fromCode(Integer code) {
        if(code == null)
            return null;
        
        switch(code.intValue()) {
        case 1:
            return NOCOLLECTING;
            
        case 2:
            return COLLECTED;
            
        case 3:
            return BECOLLECTING;
            
        case 4:
            return COLLECTING;

        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
