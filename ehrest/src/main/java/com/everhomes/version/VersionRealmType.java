// @formatter:off
package com.everhomes.version;

/**
 * <p>版本realm类型</p>
 * <ul>
 * <li>ANDROID("Android"): 左邻Android版APP</li>
 * <li>IOS("iOS"): 左邻iOS版APP</li>
 * <li>ANDROID_TECHPARK("Android_Techpark"): 科技园Android版APP</li>
 * <li>IOS_TECHPARK("iOS_Techpark"): 科技园iOS版APP</li>
 * <li>ANDROID_XUNMEI("Android_Xunmei"): 讯美Android版APP</li>
 * <li>IOS_XUNMEI("iOS_Xunmei"): 讯美iOS版APP</li>
 * <li>ANDROID_HWPARK("Android_Hwpark"): 华为Android版APP</li>
 * <li>IOS_HWPARK("iOS_Hwpark"): 华为iOS版APP</li>
 * <li>ANDROID_ISERVICE("Android_IService"): 左邻服务Android版APP</li>
 * <li>IOS_ISERVICE("iOS_IService"): 左邻服务iOS版APP</li>
 * <li>ANDROID_SHUNICOM("Android_ShUnicom"): 上海联通Android版APP</li>
 * <li>IOS_SHUNICOM("iOS_ShUnicom"): 上海联通iOS版APP</li>
 * <li>ANDROID_JYJY("Android_JYJY"): 金隅嘉业Android版APP</li>
 * <li>IOS_JYJY("iOS_JYJY"): 金隅嘉业iOS版APP</li>
 * </ul>
 */
public enum VersionRealmType {
    ANDROID("Android"), 
    IOS("iOS"), 
    ANDROID_TECHPARK("Android_Techpark"), 
    IOS_TECHPARK("iOS_Techpark"), 
    ANDROID_XUNMEI("Android_Xunmei"), 
    IOS_XUNMEI("iOS_Xunmei"), 
    ANDROID_HWPARK("Android_Hwpark"), 
    IOS_HWPARK("iOS_Hwpark"), 
    ANDROID_ISERVICE("Android_IService"), 
    IOS_ISERVICE("iOS_IService"), 
    ANDROID_SHUNICOM("Android_ShUnicom"), 
    IOS_SHUNICOM("iOS_ShUnicom"), 
    ANDROID_JYJY("Android_JYJY"), 
    IOS_JYJY("iOS_JYJY");
    
    private String code;
    private VersionRealmType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static VersionRealmType fromCode(String code) {
        if(code != null) {
            for(VersionRealmType value : VersionRealmType.values()) {
                if(code.equalsIgnoreCase(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
