// @formatter:off
package com.everhomes.objectstorage;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.objectstorage.OsObjectQuery;
import com.everhomes.rest.objectstorage.OsObjectStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOsObjectsDao;
import com.everhomes.server.schema.tables.pojos.EhOsObjects;
import com.everhomes.server.schema.tables.records.EhOsObjectsRecord;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/2/16.
 */
@Repository
class OsObjectProviderImpl implements OsObjectProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createOsObject(OsObject obj) {
        long nextId = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOsObjects.class));
        obj.setId(nextId);
        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        User currentUser = UserContext.current().getUser();
        if (currentUser != null) {
            obj.setCreatorUid(currentUser.getId());
        }
        rwDao().insert(obj);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOsObjects.class, nextId);
    }

    @Override
    public List<OsObject> listOsObject(OsObjectQuery query, ListingLocator locator, ListingQueryBuilderCallback callback) {
        SelectQuery<EhOsObjectsRecord> selectQuery = context().selectFrom(Tables.EH_OS_OBJECTS).getQuery();

        buildQueryConditions(query, locator, selectQuery);

        if (callback != null) {
            callback.buildCondition(locator, selectQuery);
        }

        selectQuery.addConditions(Tables.EH_OS_OBJECTS.STATUS.eq(OsObjectStatus.ACTIVE.getCode()));
        selectQuery.addOrderBy(Tables.EH_OS_OBJECTS.CREATE_TIME.desc());
        List<OsObject> osObjects = selectQuery.fetchInto(OsObject.class);

        if (osObjects.size() > query.getPageSize()) {
            locator.setAnchor(osObjects.get(osObjects.size() - 1).getCreateTime().getTime());
            osObjects = osObjects.stream().limit(query.getPageSize()).collect(Collectors.toList());
        } else {
            locator.setAnchor(null);
        }
        return osObjects;
    }

    @Override
    public OsObject findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), OsObject.class);
    }

    @Override
    public void updateOsObject(OsObject obj) {
        obj.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        User currentUser = UserContext.current().getUser();
        if (currentUser != null) {
            obj.setUpdateUid(currentUser.getId());
        }
        rwDao().update(obj);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOsObjects.class, obj.getId());
    }

    private void buildQueryConditions(OsObjectQuery query, ListingLocator locator, SelectQuery<EhOsObjectsRecord> selectQuery) {
        ifNotNull(query.getNamespaceId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.NAMESPACE_ID.eq(query.getNamespaceId())));
        ifNotNull(query.getStatus(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.STATUS.eq(query.getStatus())));
        ifNotNull(query.getOwnerType(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.OWNER_TYPE.eq(query.getOwnerType())));
        ifNotNull(query.getOwnerId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.OWNER_ID.eq(query.getOwnerId())));
        ifNotNull(query.getServiceType(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.SERVICE_TYPE.eq(query.getServiceType())));
        ifNotNull(query.getServiceId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.SERVICE_ID.eq(query.getServiceId())));
        ifNotNull(query.getParentId(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.PARENT_ID.eq(query.getParentId())));
        ifNotNull(query.getObjectType(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.OBJECT_TYPE.eq(query.getObjectType())));
        ifNotNull(query.getMd5(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.MD5.eq(query.getMd5())));
        ifNotNull(query.getContentType(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.CONTENT_TYPE.eq(query.getContentType())));
        ifNotNull(query.getPath(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.PATH.like(DSL.concat(query.getPath(), "%"))));
        ifNotNull(query.getName(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.NAME.like(DSL.concat("%", query.getName(), "%"))));
        ifNotNull(query.getPageSize(), () -> selectQuery.addLimit(query.getPageSize()+1));
        ifNotNull(locator.getAnchor(), () -> selectQuery.addConditions(Tables.EH_OS_OBJECTS.CREATE_TIME.le(new Timestamp(locator.getAnchor()))));
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhOsObjectsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhOsObjectsDao(context.configuration());
    }

    // 可读dao
    private EhOsObjectsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhOsObjectsDao(context.configuration());
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
