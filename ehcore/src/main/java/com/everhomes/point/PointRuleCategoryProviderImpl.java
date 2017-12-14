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
import com.everhomes.server.schema.tables.daos.EhPointRuleCategoriesDao;
import com.everhomes.server.schema.tables.pojos.EhPointRuleCategories;
import com.everhomes.server.schema.tables.records.EhPointRuleCategoriesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class PointRuleCategoryProviderImpl implements PointRuleCategoryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointRuleCategory(PointRuleCategory pointRuleCategory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointRuleCategories.class));
		pointRuleCategory.setId(id);
		pointRuleCategory.setCreateTime(DateUtils.currentTimestamp());
		// pointRuleCategory.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointRuleCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointRuleCategories.class, id);
	}

	@Override
	public void updatePointRuleCategory(PointRuleCategory pointRuleCategory) {
		// pointRuleCategory.setUpdateTime(DateUtils.currentTimestamp());
		// pointRuleCategory.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointRuleCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointRuleCategories.class, pointRuleCategory.getId());
	}

    @Override
    public List<PointRuleCategory> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointRuleCategories t = Tables.EH_POINT_RULE_CATEGORIES;

        SelectQuery<EhPointRuleCategoriesRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.lt(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<PointRuleCategory> list = query.fetchInto(PointRuleCategory.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

	@Override
	public PointRuleCategory findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointRuleCategory.class);
	}

    @Override
    public List<PointRuleCategory> listPointRuleCategories() {
        return this.query(new ListingLocator(), -1, null);
    }

    @Override
    public List<PointRuleCategory> listPointRuleCategoriesByServerId(String serverId) {
        com.everhomes.server.schema.tables.EhPointRuleCategories t = Tables.EH_POINT_RULE_CATEGORIES;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            query.addConditions(t.SERVER_ID.eq(serverId));
            return query;
        });
    }

    @Override
    public void registerDefaultPointRuleCategory(List<Long> ids, String serverId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhPointRuleCategories t = Tables.EH_POINT_RULE_CATEGORIES;

        UpdateQuery<EhPointRuleCategoriesRecord> query = context.updateQuery(t);

        query.addValue(t.SERVER_ID, serverId);
        query.addValue(t.UPDATE_TIME, new Timestamp(System.currentTimeMillis()));
        query.addConditions(t.SERVER_ID.eq("default"));

        if (ids != null) {
            query.addConditions(t.ID.in(ids));
        }

        query.execute();
    }

    private EhPointRuleCategoriesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointRuleCategoriesDao(context.configuration());
	}

	private EhPointRuleCategoriesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointRuleCategoriesDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
