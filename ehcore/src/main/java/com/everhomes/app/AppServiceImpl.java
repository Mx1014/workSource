package com.everhomes.app;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.rest.app.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2018/4/11.
 */
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppProvider appProvider;

    @Autowired
    private AppNamespaceMappingProvider appNamespaceMappingProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public AppDTO createApp(CreateAppCommand cmd) {
        ValidatorUtil.validate(cmd);

        App app = ConvertHelper.convert(cmd, App.class);
        app.setAppKey(UUID.randomUUID().toString());
        app.setSecretKey(SignatureHelper.generateSecretKey());
        app.setCreatorUid(UserContext.currentUserId());
        app.setCreateTime(new Timestamp(System.currentTimeMillis()));
        app.setStatus((byte) 1);
        appProvider.createApp(app);

        AppNamespaceMapping map = new AppNamespaceMapping();
        map.setAppKey(app.getAppKey());
        map.setNamespaceId(cmd.getNamespaceId());
        appNamespaceMappingProvider.createAppNamespaceMapping(map);

        return toAppDTO(app);
    }

    private AppDTO toAppDTO(App app) {
        return ConvertHelper.convert(app, AppDTO.class);
    }

    @Override
    public ListAppsResponse listApps(ListAppsCommand cmd) {
        ValidatorUtil.validate(cmd);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<AppNamespaceMapping> mappingList = appNamespaceMappingProvider.listAppNamespaceMapping(
                cmd.getNamespaceId(), pageSize, locator);

        List<String> appKeyList = mappingList.stream().map(AppNamespaceMapping::getAppKey).collect(Collectors.toList());
        List<App> apps = appNamespaceMappingProvider.listAppsByAppKey(appKeyList);

        ListAppsResponse response = new ListAppsResponse();
        response.setApps(apps.stream().map(this::toAppDTO).collect(Collectors.toList()));
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public void deleteApp(DeleteAppCommand cmd) {
        ValidatorUtil.validate(cmd);
        App app = appProvider.findAppById(cmd.getId());
        if (app != null) {
            dbProvider.execute(status -> {
                appProvider.deleteApp(app);
                AppNamespaceMapping mapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(app.getAppKey());
                if (mapping != null) {
                    appNamespaceMappingProvider.deleteNamespaceMapping(mapping);
                }
                return true;
            });
        }
    }
}
