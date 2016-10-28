package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhDoorUserPermissionDao;
import com.everhomes.server.schema.tables.pojos.EhDoorUserPermission;
import com.everhomes.server.schema.tables.records.EhDoorUserPermissionRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class DoorUserPermissionProviderImpl implements DoorUserPermissionProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDoorUserPermission(DoorUserPermission obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorUserPermission.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorUserPermission.class));
        obj.setId(id);
        prepareObj(obj);
        EhDoorUserPermissionDao dao = new EhDoorUserPermissionDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateDoorUserPermission(DoorUserPermission obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorUserPermission.class));
        EhDoorUserPermissionDao dao = new EhDoorUserPermissionDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteDoorUserPermission(DoorUserPermission obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorUserPermission.class));
        EhDoorUserPermissionDao dao = new EhDoorUserPermissionDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public DoorUserPermission getDoorUserPermissionById(Long id) {
        try {
        DoorUserPermission[] result = new DoorUserPermission[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorUserPermission.class));

        result[0] = context.select().from(Tables.EH_DOOR_USER_PERMISSION)
            .where(Tables.EH_DOOR_USER_PERMISSION.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, DoorUserPermission.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<DoorUserPermission> queryDoorUserPermissions(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorUserPermission.class));

        SelectQuery<EhDoorUserPermissionRecord> query = context.selectQuery(Tables.EH_DOOR_USER_PERMISSION);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_DOOR_USER_PERMISSION.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<DoorUserPermission> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, DoorUserPermission.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(DoorUserPermission obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
