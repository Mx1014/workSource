package com.everhomes.app;

import com.everhomes.rest.app.*;

/**
 * Created by xq.tian on 2018/4/11.
 */
public interface AppService {

    AppDTO createApp(CreateAppCommand cmd);

    ListAppsResponse listApps(ListAppsCommand cmd);

    void deleteApp(DeleteAppCommand cmd);

    App find(String appKey);

    boolean isGrantedApp(App app, Long userId);
}
