package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhOwnerDoorsDao;
import com.everhomes.server.schema.tables.pojos.EhOwnerDoors;
import com.everhomes.server.schema.tables.records.EhOwnerDoorsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class OwnerDoorProviderImpl implements OwnerDoorProvider {
    //This is a global table 
    
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createOwnerDoor(OwnerDoor obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOwnerDoors.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOwnerDoors.class));
        obj.setId(id);
        prepareObj(obj);
        EhOwnerDoorsDao dao = new EhOwnerDoorsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateOwnerDoor(OwnerDoor obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOwnerDoors.class));
        EhOwnerDoorsDao dao = new EhOwnerDoorsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteOwnerDoor(OwnerDoor obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOwnerDoors.class));
        EhOwnerDoorsDao dao = new EhOwnerDoorsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public OwnerDoor getOwnerDoorById(Long id) {
        try {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOwnerDoors.class));

        return context.select().from(Tables.EH_OWNER_DOORS)
            .where(Tables.EH_OWNER_DOORS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, OwnerDoor.class);
            });
        } catch (Exception ex) {
            //TODO fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<OwnerDoor> queryOwnerDoors(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOwnerDoors.class));
        SelectQuery<EhOwnerDoorsRecord> query = context.selectQuery(Tables.EH_OWNER_DOORS);

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_OWNER_DOORS.ID.gt(locator.getAnchor()));
        query.addOrderBy(Tables.EH_OWNER_DOORS.ID.asc());
        query.addLimit(count);

        List<OwnerDoor> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OwnerDoor.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        return objs;
    }

    private void prepareObj(OwnerDoor obj) {
    }
}
