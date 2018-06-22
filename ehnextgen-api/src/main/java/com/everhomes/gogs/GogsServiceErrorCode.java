package com.everhomes.gogs;

public interface GogsServiceErrorCode {
    String SCOPE = "gogs";

    // 没有前缀的是 gogs 那边报的一些异常
    int ERROR_CONFLICT = 409;
    int ERROR_NOT_EXIST = 404;

    // 10 前缀的是左邻这边的一些异常
    int ERROR_REPO_NOT_EXIST = 10404;
}
