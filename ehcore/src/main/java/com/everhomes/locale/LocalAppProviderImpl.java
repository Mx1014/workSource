package com.everhomes.locale;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.local.AppVersionCommand;
import com.everhomes.rest.local.GetAppVersion;
import com.everhomes.schema.Tables;
import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.schema.tables.records.EhConfigurationsRecord;

/**
 * 
 * @author elians get app version from this code
 */
@Component
public class LocalAppProviderImpl implements LocalAppProvier {
    private static final String DEAULT_CHANNEL = "1";
    private static final String APP_VERSION = "app.version";
    private static final String APP_LINK = "app.link";
    private static final String APP_DESC_LINK = "app.desc.link";
    private static final String APP_MIN_VERSION = "app.min.version";
    private static final String MKT_DATA_VERSION = "mkt.data.version";
    private static final String USER_LOC_RPT_FREQ = "user.loc.rpt.freq";
    private static final String USER_CONTACT_RPT_FREQ = "user.contact.rpt.freq";
    private static final String USER_RPT_CONFIG_VERSION = "user.rpt.config.version";
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public GetAppVersion findAppVersion(AppVersionCommand appversion) {
        // find path from database
        Map<String, String> map = findAppInfos();
        GetAppVersion version = new GetAppVersion();
        String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        switch (appversion.getPlatformType()) {
        case 1:
            version.setDownloadLink(homeUrl + "/m/downloadres/" + DEAULT_CHANNEL);
            version.setVersionDescLink(map.get("android." + APP_DESC_LINK));
            updateCommonInfo(version, appversion.getCurrVersionCode(), "android", map);
            return version;
        case 2:
            version.setDownloadLink(map.get("ios." + APP_LINK));
            version.setVersionDescLink(map.get("ios." + APP_DESC_LINK));
            updateCommonInfo(version, appversion.getCurrVersionCode(), "ios", map);
            return version;
        default:
            // default set to android
            version.setDownloadLink(homeUrl + "/m/downloadres/" + DEAULT_CHANNEL);
            version.setVersionDescLink(map.get("android." + APP_DESC_LINK));
            updateCommonInfo(version, appversion.getCurrVersionCode(), "android", map);
            return version;
        }
    }

    private void updateCommonInfo(GetAppVersion version, String currentVersion, String os, Map<String, String> map) {
        String appVersion = map.get(os + "." + APP_VERSION);
        String v = "";
        String name = "";
        if (appVersion == null) {
            v = "";
            name = "";

        } else {
            String[] arr = appVersion.split("|");

            if (arr.length < 2) {
                v = "";// version
                name = "";// name
            } else {
                v = arr[0];
                name = arr[1];
            }
        }

        version.setVersionCode(v);
        version.setVersionName(name);
        String mini_version = map.get(os + "." + APP_MIN_VERSION);
        version.setOperation(NumberUtils.toInt(mini_version) >= NumberUtils.toInt(currentVersion) ? 0 : 1);
        version.setMktDataVersion(NumberUtils.toInt(map.get(MKT_DATA_VERSION), 0));
        version.setUserLocRptFreq(NumberUtils.toInt(map.get(USER_LOC_RPT_FREQ), 0));
        version.setUserRptConfigVersion(NumberUtils.toInt(map.get(USER_RPT_CONFIG_VERSION), 0));
        version.setUserContactRptFreq(NumberUtils.toInt(map.get(USER_CONTACT_RPT_FREQ), 0));

    }

    @Cacheable(value = "findAppInfos")
    public Map<String, String> findAppInfos() {
        Map<String, String> map = new HashMap<String, String>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfigurations.class));
        context.select().from(Tables.EH_CONFIGURATIONS).where(Tables.EH_CONFIGURATIONS.NAME.like("android.%"))
                .or(Tables.EH_CONFIGURATIONS.NAME.like("ios.%")).or(Tables.EH_CONFIGURATIONS.NAME.like("user.%"))
                .or(Tables.EH_CONFIGURATIONS.NAME.eq(MKT_DATA_VERSION)).fetch().stream().forEach(r -> {
                    EhConfigurationsRecord record = (EhConfigurationsRecord) r;
                    map.put(record.getName(), record.getValue());
                });
        return map;
    }

}
