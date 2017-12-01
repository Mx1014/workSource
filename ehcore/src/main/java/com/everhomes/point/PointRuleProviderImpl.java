// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.point.ListPointRulesCommand;
import com.everhomes.rest.point.PointCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPointRulesDao;
import com.everhomes.server.schema.tables.pojos.EhPointRules;
import com.everhomes.server.schema.tables.records.EhPointRulesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Repository
public class PointRuleProviderImpl implements PointRuleProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

    @CacheEvict(value = "PointRule", allEntries = true)
	@Override
	public void createPointRule(PointRule pointRule) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointRules.class));
		pointRule.setId(id);
		pointRule.setCreateTime(DateUtils.currentTimestamp());
		// pointRule.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointRule);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointRules.class, id);
	}

    @CacheEvict(value = "PointRule", allEntries = true)
	@Override
	public void updatePointRule(PointRule pointRule) {
		// pointRule.setUpdateTime(DateUtils.currentTimestamp());
		// pointRule.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointRules.class, pointRule.getId());
	}

    @Override
    public List<PointRule> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;

        SelectQuery<EhPointRulesRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.lt(locator.getAnchor()));
        }
        query.addConditions(t.STATUS.ne(PointCommonStatus.INACTIVE.getCode()));

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<PointRule> list = query.fetchInto(PointRule.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Cacheable(value = "PointRule")
	@Override
	public PointRule findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointRule.class);
	}

    @Cacheable(value = "PointRule")
    @Override
    public List<PointRule> listPointRuleBySystemId(Long systemId, Integer pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;
        return this.query(locator, pageSize, (locator1, query) -> {
            query.addConditions(t.SYSTEM_ID.eq(systemId));
            return query;
        });
    }

    @Cacheable(value = "PointRule")
    @Override
    public List<PointRule> listPointRuleByCategoryId(Long categoryId, Integer pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;
        return this.query(locator, pageSize, (locator1, query) -> {
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
            return query;
        });
    }

    @CacheEvict(value = "PointRule", allEntries = true)
    @Override
    public void createPointRules(List<PointRule> pointRules) {
        Timestamp createTime = DateUtils.currentTimestamp();
        Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPointRules.class), pointRules.size());
        for (PointRule rule : pointRules) {
            rule.setId(id++);
            rule.setCreateTime(createTime);
        }
        rwDao().insert(Arrays.asList(pointRules.toArray(new PointRule[pointRules.size()])));
    }

    @Override
    public List<PointRule> listPointRules(ListPointRulesCommand cmd, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;

        return this.query(locator, pageSize, (locator1, query) -> {
            if (cmd.getSystemId() != null) {
                query.addConditions(t.SYSTEM_ID.eq(cmd.getSystemId()));
            }
            if (cmd.getCategoryId() != null) {
                query.addConditions(t.CATEGORY_ID.eq(cmd.getCategoryId()));
            }
            if (cmd.getArithmeticType() != null) {
                query.addConditions(t.ARITHMETIC_TYPE.eq(cmd.getArithmeticType()));
            }
            if (cmd.getStatus() != null) {
                query.addConditions(t.STATUS.eq(cmd.getStatus()));
            }
            query.addConditions(t.STATUS.ne(PointCommonStatus.INACTIVE.getCode()));
            // query.addConditions(t.DISPLAY_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()));
            return query;
        });
    }

    @Override
    public List<PointRule> listPointRuleByEventName(Integer namespaceId, Long systemId, String eventName) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;
        return this.query(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            query.addConditions(t.SYSTEM_ID.eq(systemId));
            query.addConditions(t.EVENT_NAME.eq(eventName));
            return query;
        });
    }

    private EhPointRulesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointRulesDao(context.configuration());
	}

	private EhPointRulesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointRulesDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
