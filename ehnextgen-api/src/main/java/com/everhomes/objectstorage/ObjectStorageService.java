package com.everhomes.objectstorage;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.objectstorage.OsObjectDTO;
import com.everhomes.rest.objectstorage.OsObjectDownloadLogQuery;
import com.everhomes.rest.objectstorage.OsObjectQuery;

import java.util.List;

/**
 * 对象存储
 * Created by xq.tian on 2017/2/16.
 */
public interface ObjectStorageService {

    /**
     * 创建一条对象记录
     * @param obj
     * @return  返回该记录本身
     */
    OsObject createOsObject(OsObject obj);

    /**
     * OsObject转换成OsObjectDTO
     */
    OsObjectDTO toOsObjectDTO(OsObject obj);

    /**
     * 根据不同条件查询osObject
     * @param query
     * @return
     */
    List<OsObject> listOsObject(OsObjectQuery query, ListingLocator locator);

    /**
     * 可以根据callback进行自定义查询条件, 其他跟上面的类似
     * @param query
     * @param locator
     * @param callback
     * @return
     */
    List<OsObject> listOsObject(OsObjectQuery query, ListingLocator locator, ListingQueryBuilderCallback callback);

    /**
     * 根据id查询
     * @param namespaceId
     * @param id
     * @return
     */
    OsObject findById(Integer namespaceId, Long id);

    /**
     * 删除对象
     * @param obj
     */
    void deleteOsObject(OsObject obj);

    /**
     * 更新对象
     * @param obj
     */
    void updateOsObject(OsObject obj);

    /**
     * 记录一条下载记录
     * @param objectId
     * @param userId
     * @return
     */
    OsObjectDownloadLog createOsObjectDownloadLog(Long objectId, Long userId);

    /**
     * 根据条件查询下载记录
     * @param query
     * @param locator
     * @return
     */
    List<OsObjectDownloadLog> listOsObjectDownloadLogs(OsObjectDownloadLogQuery query, ListingLocator locator);

    /**
     * 根据条件查询下载记录
     * @param query
     * @param locator
     * @return
     */
    List<OsObjectDownloadLog> listOsObjectDownloadLogs(OsObjectDownloadLogQuery query, ListingLocator locator, ListingQueryBuilderCallback callback);
}
