package com.everhomes.rest.launchpadbase.indexconfigjson;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 */
public class Application {

    private String router;

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
