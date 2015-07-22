package com.everhomes.version;

public interface VersionServiceErrorCode {
    static final String SCOPE = "version";

    static final int ERROR_NO_UPGRADE_RULE_SET = 1;
    static final int ERROR_NO_VERSIONED_CONTENT_SET = 2;
}
