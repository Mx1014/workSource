package com.everhomes.objectstorage;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.objectstorage.OsObjectQuery;

import java.util.List;

/**
 * 对象存储 provider
 *
 * <b>该接口是包访问权限，如果你想使用它的方法，最好去使用service {@link com.everhomes.objectstorage.ObjectStorageService}</b>
 *
 * Created by xq.tian on 2017/2/16.
 */
interface OsObjectProvider {

    void createOsObject(OsObject obj);

    List<OsObject> listOsObject(OsObjectQuery query, ListingLocator locator, ListingQueryBuilderCallback callback);

    OsObject findById(Long id);

    void updateOsObject(OsObject obj);
}
