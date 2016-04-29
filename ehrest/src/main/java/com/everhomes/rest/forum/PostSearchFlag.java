// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>帖子搜索全部标记
 * <li>NONE(0): 非全局搜索</li>
 * <li>GLOBAL(1): 全局搜索</li>
 * </ul>
 */
public enum PostSearchFlag {
    NONE(0), GLOBAL(1);
    
    // 由于SearchTopicCommand:searchFlag已经定好使用Integer，为了避免客户端也要修改，故保留使用Integer而不是Byte by lqs 20160422
    private int code;
    
    private PostSearchFlag(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public static PostSearchFlag fromCode(Integer code) {
        if(code != null) {
            PostSearchFlag[] values = PostSearchFlag.values();
            for(PostSearchFlag value : values) {
                if(value.code == code.intValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
