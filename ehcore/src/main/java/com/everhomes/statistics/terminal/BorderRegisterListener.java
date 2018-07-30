// @formatter:off
package com.everhomes.statistics.terminal;

import com.everhomes.rest.version.VersionRealmType;
import com.everhomes.user.*;
import com.everhomes.util.DateUtils;
import com.everhomes.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/7/14.
 */
@Component
public class BorderRegisterListener implements ApplicationListener<BorderRegisterEvent> {

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Autowired
    private StatTerminalProvider statTerminalProvider;

    @Async
    @Override
    public void onApplicationEvent(BorderRegisterEvent event) {
        UserLogin login = (UserLogin) event.getSource();
        if (login.getUserId() == 0L) {
            return;
        }

        UserActivity activity = new UserActivity();
        activity.setUid(login.getUserId());
        activity.setImeiNumber(String.valueOf(login.getUserId()));
        activity.setNamespaceId(login.getNamespaceId());
        String appVersion = login.getAppVersion();
        // 在这次修改之前登陆的用户的login里的appVersion为null
        if (appVersion == null) {
            UserActivity lastUserActivity = userActivityProvider.findLastUserActivity(login.getUserId());
            if (lastUserActivity != null) {
                appVersion = lastUserActivity.getAppVersionName();
            }
        }
        if (appVersion == null) {
            AppVersion version = statTerminalProvider.findLastAppVersion(login.getNamespaceId());
            if (version != null) {
                appVersion = version.getName();
            } else {
                appVersion = "5.0.0";

                version = new AppVersion();
                version.setName(appVersion);
                version.setDefaultOrder((int) Version.fromVersionString(appVersion).getEncodedValue());
                version.setNamespaceId(login.getNamespaceId());
                version.setType(OSType.Android.name());
                version.setRealm(VersionRealmType.ANDROID.getCode());
                statTerminalProvider.createAppVersion(version);
            }
        }
        // end

        activity.setAppVersionName(appVersion);
        activity.setCreateTime(DateUtils.currentTimestamp());
        activity.setActivityType(ActivityType.BORDER_REGISTER.getCode());

        userActivityProvider.addActivity(activity, activity.getUid());
    }
}
