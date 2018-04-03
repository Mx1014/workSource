// @formatter:off
package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserIdentifierLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/6/27.
 */
public class UserIdentifierLog extends EhUserIdentifierLogs {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public boolean checkVerificationCode(String code) {
        code = (code != null ? code.trim() : null);
        String oldCode = getVerificationCode() != null ? getVerificationCode().trim() : null;
        return code != null && code.equals(oldCode);
    }

    public boolean notExpire(Long interval) {
        return (System.currentTimeMillis() - this.getCreateTime().getTime() <= interval);
    }
}
