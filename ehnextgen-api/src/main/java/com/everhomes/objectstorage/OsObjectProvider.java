package com.everhomes.objectstorage;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.objectstorage.OsObjectQuery;

import java.util.List;

/**
 * 对象存储
 * Created by xq.tian on 2017/2/16.
 */
public interface OsObjectProvider {

    void createOsObject(OsObject obj);

    List<OsObject> listOsObject(OsObjectQuery query, ListingLocator locator, ListingQueryBuilderCallback callback);

    OsObject findById(Long id);

    void updateOsObject(OsObject obj);
}
