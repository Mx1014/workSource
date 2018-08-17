package com.everhomes.gogs;

import com.everhomes.util.RuntimeErrorException;

/**
 * 冲突时, 抛出此异常
 */
public class GogsConflictException extends RuntimeErrorException {

    public GogsConflictException(String scope, int code, String message) {
        super(message);
        this.setErrorCode(code);
        this.setErrorScope(scope);
    }
}
