package com.everhomes.rest.notice;

public interface EnterpriseNoticeErrorCode {
    String SCOPE = "enterprise.notice";

    int NOTICE_SHARE_URL_INVALID = 10000;
    int SHARED_NOTICE_PRIVATE_ERROR = 10001;
    int NOTICE_NOT_FOUND_ERROR = 10002;
    int SHARED_NOTICE_TOKEN_INVALID = 10003;
}
