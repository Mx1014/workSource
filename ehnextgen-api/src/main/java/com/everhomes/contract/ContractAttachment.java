package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractAttachments;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/16.
 */
public class ContractAttachment extends EhContractAttachments {
    private static final long serialVersionUID = -6523108376419372339L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
