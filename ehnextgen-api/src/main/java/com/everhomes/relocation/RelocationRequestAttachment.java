package com.everhomes.relocation;

import com.everhomes.server.schema.tables.pojos.EhRelocationRequestAttachments;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/11/20.
 */
public class RelocationRequestAttachment extends EhRelocationRequestAttachments {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
