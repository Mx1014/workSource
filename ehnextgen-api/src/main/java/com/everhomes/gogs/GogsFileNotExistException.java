package com.everhomes.gogs;

import com.everhomes.util.RuntimeErrorException;

/**
 * 文件不存在时, 抛出此异常
 */
public class GogsFileNotExistException extends RuntimeErrorException {

    public GogsFileNotExistException(String scope, int code, String message) {
        super(message);
        this.setErrorCode(code);
        this.setErrorScope(scope);
    }
}
