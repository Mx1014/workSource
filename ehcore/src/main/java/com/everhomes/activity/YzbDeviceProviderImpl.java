package com.everhomes.activity;

import java.util.List;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhYzbDevicesDao;
import com.everhomes.server.schema.tables.pojos.EhYzbDevices;
import com.everhomes.server.schema.tables.records.EhYzbDevicesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class YzbDeviceProviderImpl implements YzbDeviceProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createYzbDevice(YzbDevice obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhYzbDevices.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhYzbDevices.class));
        obj.setId(id);
        prepareObj(obj);
        EhYzbDevicesDao dao = new EhYzbDevicesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateYzbDevice(YzbDevice obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhYzbDevices.class));
        EhYzbDevicesDao dao = new EhYzbDevicesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteYzbDevice(YzbDevice obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhYzbDevices.class));
        EhYzbDevicesDao dao = new EhYzbDevicesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public YzbDevice getYzbDeviceById(Long id) {
        try {
        YzbDevice[] result = new YzbDevice[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhYzbDevices.class));

        result[0] = context.select().from(Tables.EH_YZB_DEVICES)
            .where(Tables.EH_YZB_DEVICES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, YzbDevice.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<YzbDevice> queryYzbDevices(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhYzbDevices.class));

        SelectQuery<EhYzbDevicesRecord> query = context.selectQuery(Tables.EH_YZB_DEVICES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_YZB_DEVICES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<YzbDevice> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, YzbDevice.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(YzbDevice obj) {
    }
}
