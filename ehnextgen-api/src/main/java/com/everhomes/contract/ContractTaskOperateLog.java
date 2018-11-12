// @formatter:off
package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractTaskOperateLogs;
import com.everhomes.util.StringHelper;

public class ContractTaskOperateLog extends EhContractTaskOperateLogs {

    private static final long serialVersionUID = -2614415176151224956L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
