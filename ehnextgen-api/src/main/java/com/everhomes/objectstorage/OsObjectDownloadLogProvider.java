package com.everhomes.objectstorage;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.objectstorage.OsObjectDownloadLogQuery;

import java.util.List;

/**
 * 对象下载记录
 * Created by xq.tian on 2017/2/16.
 */
public interface OsObjectDownloadLogProvider {

    void createOsObjectDownloadLog(OsObjectDownloadLog log);

    List<OsObjectDownloadLog> listOsObjectDownloadLogs(OsObjectDownloadLogQuery query, ListingLocator locator, ListingQueryBuilderCallback callback);
}
