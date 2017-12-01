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
import com.everhomes.server.schema.tables.daos.EhPointTutorialToPointRuleMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhPointTutorialToPointRuleMappings;
import com.everhomes.server.schema.tables.records.EhPointTutorialToPointRuleMappingsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointTutorialToPointRuleMappingProviderImpl implements PointTutorialToPointRuleMappingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointTutorialToPointRuleMapping(PointTutorialToPointRuleMapping pointTutorialToPointRuleMapping) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointTutorialToPointRuleMappings.class));
		pointTutorialToPointRuleMapping.setId(id);
		pointTutorialToPointRuleMapping.setCreateTime(DateUtils.currentTimestamp());
		// pointTutorialToPointRuleMapping.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointTutorialToPointRuleMapping);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointTutorialToPointRuleMappings.class, id);
	}

	@Override
	public void updatePointTutorialToPointRuleMapping(PointTutorialToPointRuleMapping pointTutorialToPointRuleMapping) {
		// pointTutorialToPointRuleMapping.setUpdateTime(DateUtils.currentTimestamp());
		// pointTutorialToPointRuleMapping.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointTutorialToPointRuleMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointTutorialToPointRuleMappings.class, pointTutorialToPointRuleMapping.getId());
	}

    @Override
    public List<PointTutorialToPointRuleMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointTutorialToPointRuleMappings t = Tables.EH_POINT_TUTORIAL_TO_POINT_RULE_MAPPINGS;

        SelectQuery<EhPointTutorialToPointRuleMappingsRecord> query = context().selectQuery(t);
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

        List<PointTutorialToPointRuleMapping> list = query.fetchInto(PointTutorialToPointRuleMapping.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

	@Override
	public PointTutorialToPointRuleMapping findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointTutorialToPointRuleMapping.class);
	}

    @Override
    public List<PointTutorialToPointRuleMapping> listMappings(Long tutorialId, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointTutorialToPointRuleMappings t = Tables.EH_POINT_TUTORIAL_TO_POINT_RULE_MAPPINGS;
        return this.query(locator, pageSize, (locator1, query) -> {
            query.addConditions(t.TUTORIAL_ID.eq(tutorialId));
            return query;
        });
    }

    @Override
    public void deleteByTutorialId(Long tutorialId) {
        com.everhomes.server.schema.tables.EhPointTutorialToPointRuleMappings t = Tables.EH_POINT_TUTORIAL_TO_POINT_RULE_MAPPINGS;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        context.delete(t)
                .where(t.TUTORIAL_ID.eq(tutorialId))
                .execute();
    }

    private EhPointTutorialToPointRuleMappingsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointTutorialToPointRuleMappingsDao(context.configuration());
	}

	private EhPointTutorialToPointRuleMappingsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointTutorialToPointRuleMappingsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
