package com.everhomes.varField;

import com.everhomes.server.schema.tables.pojos.EhVarFieldScopes;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public class ScopeField extends EhVarFieldScopes {
    private static final long serialVersionUID = -7398024508426176266L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
