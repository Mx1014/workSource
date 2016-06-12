package com.everhomes.test.core.api;

import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.test.core.base.UserContext;

public interface ApiProvider {
    void syncSequence();
    UserListUserRelatedScenesRestResponse listUserRelatedScenes(UserContext context);
}
