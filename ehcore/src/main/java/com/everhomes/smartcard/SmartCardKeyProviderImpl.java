package com.everhomes.smartcard;

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
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhSmartCardKeysDao;
import com.everhomes.server.schema.tables.pojos.EhSmartCardKeys;
import com.everhomes.server.schema.tables.records.EhSmartCardKeysRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SmartCardKeyProviderImpl implements SmartCardKeyProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createSmartCardKey(SmartCardKey obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSmartCardKeys.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSmartCardKeys.class));
        obj.setId(id);
        prepareObj(obj);
        EhSmartCardKeysDao dao = new EhSmartCardKeysDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateSmartCardKey(SmartCardKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSmartCardKeys.class));
        EhSmartCardKeysDao dao = new EhSmartCardKeysDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteSmartCardKey(SmartCardKey obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSmartCardKeys.class));
        EhSmartCardKeysDao dao = new EhSmartCardKeysDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public SmartCardKey getSmartCardKeyById(Long id) {
        try {
        SmartCardKey[] result = new SmartCardKey[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSmartCardKeys.class));

        result[0] = context.select().from(Tables.EH_SMART_CARD_KEYS)
            .where(Tables.EH_SMART_CARD_KEYS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, SmartCardKey.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<SmartCardKey> querySmartCardKeys(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSmartCardKeys.class));

        SelectQuery<EhSmartCardKeysRecord> query = context.selectQuery(Tables.EH_SMART_CARD_KEYS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SMART_CARD_KEYS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<SmartCardKey> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, SmartCardKey.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(SmartCardKey obj) {
        Long t = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(t));
        obj.setUpdateTime(new Timestamp(t));
    }
    
    @Override
    public List<SmartCardKey> queryLatestSmartCardKeys(Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSmartCardKeys.class));
        SelectQuery<EhSmartCardKeysRecord> query = context.selectQuery(Tables.EH_SMART_CARD_KEYS);
        query.addConditions(Tables.EH_SMART_CARD_KEYS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_SMART_CARD_KEYS.STATUS.eq(TrueOrFalseFlag.TRUE.getCode()));
        query.addLimit(100);
        query.addOrderBy(Tables.EH_SMART_CARD_KEYS.CREATE_TIME.desc());
        List<SmartCardKey> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, SmartCardKey.class);
        });
        return objs;
    }
}
