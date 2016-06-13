package com.everhomes.rest.user;

/**
 * <p>信息信道类型</p>
 * <ul>
 * <li>user: 文本</li>
 * <li>IMAGE: 图片</li>
 * <li>AUDIO: 音频</li>
 * <li>VIDEO: 视频</li>
 * </ul>
 * @author janson
 *
 */
public enum MessageChannelType {
    USER("user"), GROUP("group"), ADDRESS("address");//, ENTERPRISE("enterprise"); use group instead
    
    private String code;
    
    private MessageChannelType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static MessageChannelType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase(USER.getCode())) {
            return USER;
        }
        if(code.equalsIgnoreCase(GROUP.getCode())) {
            return GROUP;
        }
//        if(code.equalsIgnoreCase(ENTERPRISE.getCode())) {
//            return ENTERPRISE;
//        }
        if(code.equalsIgnoreCase(ADDRESS.getCode())) {
            return ADDRESS;
        }
        
        return null;
    }

}
