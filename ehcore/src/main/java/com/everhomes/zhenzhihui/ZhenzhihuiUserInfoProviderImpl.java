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
import com.everhomes.server.schema.tables.daos.EhZhenzhihuiUserInfoDao;
import com.everhomes.server.schema.tables.pojos.EhZhenzhihuiUserInfo;
import com.everhomes.server.schema.tables.records.EhZhenzhihuiUserInfoRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class ZhenzhihuiUserInfoProviderImpl implements ZhenzhihuiUserInfoProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createZhenzhihuiUserInfo(ZhenzhihuiUserInfo obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhZhenzhihuiUserInfo.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiUserInfo.class));
        obj.setId(id);
        prepareObj(obj);
        EhZhenzhihuiUserInfoDao dao = new EhZhenzhihuiUserInfoDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateZhenzhihuiUserInfo(ZhenzhihuiUserInfo obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiUserInfo.class));
        EhZhenzhihuiUserInfoDao dao = new EhZhenzhihuiUserInfoDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteZhenzhihuiUserInfo(ZhenzhihuiUserInfo obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiUserInfo.class));
        EhZhenzhihuiUserInfoDao dao = new EhZhenzhihuiUserInfoDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ZhenzhihuiUserInfo getZhenzhihuiUserInfoById(Long id) {
        try {
        ZhenzhihuiUserInfo[] result = new ZhenzhihuiUserInfo[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiUserInfo.class));

        result[0] = context.select().from(Tables.EH_ZHENZHIHUI_USER_INFO)
            .where(Tables.EH_ZHENZHIHUI_USER_INFO.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ZhenzhihuiUserInfo.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ZhenzhihuiUserInfo> queryZhenzhihuiUserInfos(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiUserInfo.class));

        SelectQuery<EhZhenzhihuiUserInfoRecord> query = context.selectQuery(Tables.EH_ZHENZHIHUI_USER_INFO);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ZHENZHIHUI_USER_INFO.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ZhenzhihuiUserInfo> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ZhenzhihuiUserInfo.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public List<ZhenzhihuiUserInfo> listZhenzhihuiUserInfosByUserId(Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZhenzhihuiUserInfo.class));

        SelectQuery<EhZhenzhihuiUserInfoRecord> query = context.selectQuery(Tables.EH_ZHENZHIHUI_USER_INFO);
        query.addConditions(Tables.EH_ZHENZHIHUI_USER_INFO.USER_ID.eq(userId));
        List<ZhenzhihuiUserInfo> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ZhenzhihuiUserInfo.class);
        });
        return objs;
    }

    private void prepareObj(ZhenzhihuiUserInfo obj) {
    }
}
