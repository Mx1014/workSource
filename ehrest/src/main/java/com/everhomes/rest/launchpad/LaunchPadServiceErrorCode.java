package com.everhomes.rest.launchpad;

public interface LaunchPadServiceErrorCode {
    static final String SCOPE = "launchpad";
    
    static final int ERROR_LAUNCHPAD_LAYOUT_NOT_EXISTS = 10001;  //layout 不存在
    static final int ERROR_LAUNCHPAD_ITEM_NOT_EXISTS = 10002;  //item 不存在
}
