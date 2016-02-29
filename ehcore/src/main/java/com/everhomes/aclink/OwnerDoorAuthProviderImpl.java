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
import com.everhomes.server.schema.tables.daos.EhOwnerDoorAuthDao;
import com.everhomes.server.schema.tables.pojos.EhOwnerDoorAuth;
import com.everhomes.server.schema.tables.records.EhOwnerDoorAuthRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class OwnerDoorAuthProviderImpl implements OwnerDoorAuthProvider {
    //This is a global table
    
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createOwnerDoorAuth(OwnerDoorAuth obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOwnerDoorAuth.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOwnerDoorAuth.class));
        obj.setId(id);
        prepareObj(obj);
        EhOwnerDoorAuthDao dao = new EhOwnerDoorAuthDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateOwnerDoorAuth(OwnerDoorAuth obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOwnerDoorAuth.class));
        EhOwnerDoorAuthDao dao = new EhOwnerDoorAuthDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteOwnerDoorAuth(OwnerDoorAuth obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOwnerDoorAuth.class));
        EhOwnerDoorAuthDao dao = new EhOwnerDoorAuthDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public OwnerDoorAuth getOwnerDoorAuthById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOwnerDoorAuth.class));
        
        return context.select().from(Tables.EH_OWNER_DOOR_AUTH)
            .where(Tables.EH_OWNER_DOOR_AUTH.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, OwnerDoorAuth.class);
           });
    }

    @Override
    public List<OwnerDoorAuth> queryOwnerDoorAuths(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOwnerDoorAuth.class));
        
            SelectQuery<EhOwnerDoorAuthRecord> query = context.selectQuery(Tables.EH_OWNER_DOOR_AUTH);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_OWNER_DOOR_AUTH.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_OWNER_DOOR_AUTH.ID.asc());
            query.addLimit(count);

            List<OwnerDoorAuth> objs = query.fetch().map((r) -> {
                return ConvertHelper.convert(r, OwnerDoorAuth.class);
            });

            if(objs.size() >= count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
            }

        return objs;
    }

    private void prepareObj(OwnerDoorAuth obj) {
    }
}
