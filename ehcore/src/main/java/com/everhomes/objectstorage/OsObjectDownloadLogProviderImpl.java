// @formatter:off
package com.everhomes.objectstorage;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.objectstorage.OsObjectDownloadLogQuery;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOsObjectDownloadLogsDao;
import com.everhomes.server.schema.tables.pojos.EhOsObjectDownloadLogs;
import com.everhomes.server.schema.tables.records.EhOsObjectDownloadLogsRecord;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/2/16.
 */
@Repository
class OsObjectDownloadLogProviderImpl implements OsObjectDownloadLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createOsObjectDownloadLog(OsObjectDownloadLog log) {
        long nextId = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOsObjectDownloadLogs.class));
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setId(nextId);
        rwDao().insert(log);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOsObjectDownloadLogs.class, log.getId());
    }

    @Override
    public List<OsObjectDownloadLog> listOsObjectDownloadLogs(OsObjectDownloadLogQuery query, ListingLocator locator, ListingQueryBuilderCallback callback) {
        SelectQuery<EhOsObjectDownloadLogsRecord> selectQuery = context().selectFrom(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS).getQuery();

        buildQueryConditions(query, locator, selectQuery);

        if (callback != null) {
            callback.buildCondition(locator, selectQuery);
        }

        selectQuery.addOrderBy(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.CREATE_TIME.desc());
        List<OsObjectDownloadLog> downloadLogs = selectQuery.fetchInto(OsObjectDownloadLog.class);

        if (downloadLogs.size() > query.getPageSize()) {
            locator.setAnchor(downloadLogs.get(downloadLogs.size() - 1).getCreateTime().getTime());
            downloadLogs = downloadLogs.stream().limit(query.getPageSize()).collect(Collectors.toList());
        } else {
            locator.setAnchor(null);
        }
        return downloadLogs;
    }

    private void buildQueryConditions(OsObjectDownloadLogQuery query, ListingLocator locator, SelectQuery<EhOsObjectDownloadLogsRecord> selectQuery) {
        ifNotNull(query.getNamespaceId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.NAMESPACE_ID.eq(query.getNamespaceId())));
        ifNotNull(query.getOwnerType(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.OWNER_TYPE.eq(query.getOwnerType())));
        ifNotNull(query.getOwnerId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.OWNER_ID.eq(query.getOwnerId())));
        ifNotNull(query.getServiceType(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.SERVICE_TYPE.eq(query.getServiceType())));
        ifNotNull(query.getServiceId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.SERVICE_ID.eq(query.getServiceId())));
        ifNotNull(query.getObjectId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.OBJECT_ID.eq(query.getObjectId())));
        ifNotNull(query.getCreatorUid(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.CREATOR_UID.eq(query.getCreatorUid())));
        ifNotNull(query.getPageSize(), () -> selectQuery.addLimit(query.getPageSize()+1));
        ifNotNull(locator.getAnchor(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.CREATE_TIME.le(new Timestamp(locator.getAnchor()))));
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhOsObjectDownloadLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhOsObjectDownloadLogsDao(context.configuration());
    }

    // 可读dao
    private EhOsObjectDownloadLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhOsObjectDownloadLogsDao(context.configuration());
    }

    private void ifNotNull(Object obj, Callback callback) {
        if (obj != null) {
            callback.addCondition();
        }
    }

    private interface Callback {
        void addCondition();
    }
}
