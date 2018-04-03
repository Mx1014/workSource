// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPointRuleConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhPointRuleConfigs;
import com.everhomes.server.schema.tables.records.EhPointRuleConfigsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointRuleConfigProviderImpl implements PointRuleConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createPointRuleConfig(PointRuleConfig pointRuleConfig) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointRuleConfigs.class));
        pointRuleConfig.setId(id);
        pointRuleConfig.setCreateTime(DateUtils.currentTimestamp());
        // pointRuleConfig.setCreatorUid(UserContext.currentUserId());
        rwDao().insert(pointRuleConfig);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointRuleConfigs.class, id);
    }

    @Override
    public void updatePointRuleConfig(PointRuleConfig pointRuleConfig) {
        // pointRuleConfig.setUpdateTime(DateUtils.currentTimestamp());
        // pointRuleConfig.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointRuleConfig);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointRuleConfigs.class, pointRuleConfig.getId());
    }

    @Override
    public List<PointRuleConfig> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointRuleConfigs t = Tables.EH_POINT_RULE_CONFIGS;

        SelectQuery<EhPointRuleConfigsRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.le(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<PointRuleConfig> list = query.fetchInto(PointRuleConfig.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public PointRuleConfig findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), PointRuleConfig.class);
    }

    @Override
    public void deleteBySystemId(Long systemId) {
        com.everhomes.server.schema.tables.EhPointRuleConfigs t = Tables.EH_POINT_RULE_CONFIGS;

        rwContext().delete(t)
                .where(t.SYSTEM_ID.eq(systemId))
                .execute();
    }

    @Override
    public PointRuleConfig findByRuleIdAndSystemId(Long systemId, Long ruleId) {
        com.everhomes.server.schema.tables.EhPointRuleConfigs t = Tables.EH_POINT_RULE_CONFIGS;
        List<PointRuleConfig> list = query(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(t.SYSTEM_ID.eq(systemId));
            query.addConditions(t.RULE_ID.eq(ruleId));
            return query;
        });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private EhPointRuleConfigsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointRuleConfigsDao(context.configuration());
    }

    private EhPointRuleConfigsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointRuleConfigsDao(context.configuration());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }
}
