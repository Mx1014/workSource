package com.everhomes.rest.yellowPage;

public interface YellowPageServiceErrorCode {
	static final String SCOPE = "yellowPage";

    static final int ERROR_CATEGORY_IN_USE = 10001;  //有机构正在使用此分类
    static final int ERROR_CATEGORY_ALREADY_DELETED = 10002;  // category already deleted
    static final int ERROR_YELLOWPAGE_ALREADY_DELETED = 10003;  //YellowPage already deleted
}
