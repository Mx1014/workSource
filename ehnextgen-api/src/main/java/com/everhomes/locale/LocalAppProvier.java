package com.everhomes.locale;

import com.everhomes.rest.local.AppVersionCommand;
import com.everhomes.rest.local.GetAppVersion;

public interface LocalAppProvier {
    GetAppVersion findAppVersion(AppVersionCommand appversion);
}
