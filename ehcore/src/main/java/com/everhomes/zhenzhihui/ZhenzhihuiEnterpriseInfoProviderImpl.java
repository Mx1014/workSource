package com.everhomes.zhenzhihui;

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
import com.everhomes.server.schema.tables.daos.EhZhenzhihuiEnterpriseInfoDao;
import com.everhomes.server.schema.tables.pojos.EhZhenzhihuiEnterpriseInfo;
import com.everhomes.server.schema.tables.records.EhZhenzhihuiEnterpriseInfoRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class ZhenzhihuiEnterpriseInfoProviderImpl implements ZhenzhihuiEnterpriseInfoProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createZhenzhihuiEnterpriseInfo(ZhenzhihuiEnterpriseInfo obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhZhenzhihuiEnterpriseInfo.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiEnterpriseInfo.class));
        obj.setId(id);
        prepareObj(obj);
        EhZhenzhihuiEnterpriseInfoDao dao = new EhZhenzhihuiEnterpriseInfoDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateZhenzhihuiEnterpriseInfo(ZhenzhihuiEnterpriseInfo obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiEnterpriseInfo.class));
        EhZhenzhihuiEnterpriseInfoDao dao = new EhZhenzhihuiEnterpriseInfoDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteZhenzhihuiEnterpriseInfo(ZhenzhihuiEnterpriseInfo obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiEnterpriseInfo.class));
        EhZhenzhihuiEnterpriseInfoDao dao = new EhZhenzhihuiEnterpriseInfoDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ZhenzhihuiEnterpriseInfo getZhenzhihuiEnterpriseInfoById(Long id) {
        try {
        ZhenzhihuiEnterpriseInfo[] result = new ZhenzhihuiEnterpriseInfo[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiEnterpriseInfo.class));

        result[0] = context.select().from(Tables.EH_ZHENZHIHUI_ENTERPRISE_INFO)
            .where(Tables.EH_ZHENZHIHUI_ENTERPRISE_INFO.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ZhenzhihuiEnterpriseInfo.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ZhenzhihuiEnterpriseInfo> queryZhenzhihuiEnterpriseInfos(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiEnterpriseInfo.class));

        SelectQuery<EhZhenzhihuiEnterpriseInfoRecord> query = context.selectQuery(Tables.EH_ZHENZHIHUI_ENTERPRISE_INFO);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ZHENZHIHUI_ENTERPRISE_INFO.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ZhenzhihuiEnterpriseInfo> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ZhenzhihuiEnterpriseInfo.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public List<ZhenzhihuiEnterpriseInfo> listZhenzhihuiEnterpriseInfoByUserId(Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiEnterpriseInfo.class));

        SelectQuery<EhZhenzhihuiEnterpriseInfoRecord> query = context.selectQuery(Tables.EH_ZHENZHIHUI_ENTERPRISE_INFO);
        query.addConditions(Tables.EH_ZHENZHIHUI_ENTERPRISE_INFO.USER_ID.eq(userId));
        List<ZhenzhihuiEnterpriseInfo> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ZhenzhihuiEnterpriseInfo.class);
        });
        return objs;
    }

    private void prepareObj(ZhenzhihuiEnterpriseInfo obj) {
    }
}
