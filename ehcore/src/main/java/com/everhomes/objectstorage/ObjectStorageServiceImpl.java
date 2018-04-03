// @formatter:off
package com.everhomes.objectstorage;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.objectstorage.OsObjectDTO;
import com.everhomes.rest.objectstorage.OsObjectDownloadLogQuery;
import com.everhomes.rest.objectstorage.OsObjectQuery;
import com.everhomes.rest.objectstorage.OsObjectStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by xq.tian on 2017/2/16.
 */
@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {

    @Autowired
    private OsObjectProvider osObjectProvider;

    @Autowired
    private OsObjectDownloadLogProvider osObjectDownloadLogProvider;

    @Override
    public OsObject createOsObject(OsObject obj) {
        osObjectProvider.createOsObject(obj);
        return obj;
    }

    @Override
    public OsObjectDTO toOsObjectDTO(OsObject obj) {
        throw new RuntimeException("toOsObjectDTO are not implementation");
    }

    @Override
    public List<OsObject> listOsObject(OsObjectQuery query, ListingLocator locator) {
        return this.listOsObject(query, locator, null);
    }

    @Override
    public List<OsObject> listOsObject(OsObjectQuery query, ListingLocator locator, ListingQueryBuilderCallback callback) {
        return osObjectProvider.listOsObject(query, locator, callback);
    }

    @Override
    public OsObject findById(Integer namespaceId, Long id) {
        OsObject osObject = osObjectProvider.findById(id);
        if (osObject != null && osObject.getNamespaceId().equals(namespaceId)) {
            return osObject;
        }
        return null;
    }

    @Override
    public void deleteOsObject(OsObject obj) {
        if (OsObjectStatus.fromCode(obj.getStatus()) != OsObjectStatus.INACTIVE) {
            obj.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            User currUser = UserContext.current().getUser();
            if (currUser != null) {
                obj.setDeleteUid(currUser.getId());
            }
            obj.setStatus(OsObjectStatus.INACTIVE.getCode());
            osObjectProvider.updateOsObject(obj);
        }
    }

    @Override
    public void updateOsObject(OsObject obj) {
        osObjectProvider.updateOsObject(obj);
    }

    @Override
    public OsObjectDownloadLog createOsObjectDownloadLog(Long objectId, Long userId) {
        OsObject osObject = osObjectProvider.findById(objectId);
        if (osObject != null) {
            OsObjectDownloadLog log = new OsObjectDownloadLog();
            log.setOwnerType(osObject.getOwnerType());
            log.setOwnerId(osObject.getOwnerId());
            log.setServiceType(osObject.getServiceType());
            log.setServiceId(osObject.getServiceId());
            log.setNamespaceId(osObject.getNamespaceId());
            log.setObjectId(objectId);
            log.setCreatorUid(userId);
            osObjectDownloadLogProvider.createOsObjectDownloadLog(log);
            return log;
        }
        return null;
    }

    @Override
    public List<OsObjectDownloadLog> listOsObjectDownloadLogs(OsObjectDownloadLogQuery query, ListingLocator locator) {
        return this.listOsObjectDownloadLogs(query, locator, null);
    }

    @Override
    public List<OsObjectDownloadLog> listOsObjectDownloadLogs(OsObjectDownloadLogQuery query, ListingLocator locator, ListingQueryBuilderCallback callback) {
        return osObjectDownloadLogProvider.listOsObjectDownloadLogs(query, locator, callback);
    }
}
