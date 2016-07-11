package com.everhomes.version;

import com.everhomes.server.schema.tables.pojos.EhVersionUpgradeRules;
import com.everhomes.util.StringHelper;

public class VersionUpgradeRule extends EhVersionUpgradeRules {
    private static final long serialVersionUID = -4315991140626118040L;

    public VersionUpgradeRule() {
    }
 
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
