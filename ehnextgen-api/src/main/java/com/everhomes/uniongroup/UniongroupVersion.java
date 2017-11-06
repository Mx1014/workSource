package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupVersion;
import com.everhomes.util.StringHelper;


/**
 * Created by wuhan on 2017/10/19.
 */
public class UniongroupVersion extends EhUniongroupVersion {

    private static final long serialVersionUID = 8751516334864351356L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
