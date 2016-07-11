package com.everhomes.version;

import com.everhomes.server.schema.tables.pojos.EhVersionedContent;
import com.everhomes.util.StringHelper;

public class VersionedContent extends EhVersionedContent {
    private static final long serialVersionUID = 811205021502258883L;

    public VersionedContent() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
