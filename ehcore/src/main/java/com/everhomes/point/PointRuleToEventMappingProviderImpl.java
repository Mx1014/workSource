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
import com.everhomes.server.schema.tables.daos.EhPointRuleToEventMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhPointRuleToEventMappings;
import com.everhomes.server.schema.tables.records.EhPointRuleToEventMappingsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointRuleToEventMappingProviderImpl implements PointRuleToEventMappingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointRuleToEventMapping(PointRuleToEventMapping pointRuleToEventMapping) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointRuleToEventMappings.class));
		pointRuleToEventMapping.setId(id);
		// pointRuleToEventMapping.setCreateTime(DateUtils.currentTimestamp());
		// pointRuleToEventMapping.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointRuleToEventMapping);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointRuleToEventMappings.class, id);
	}

	@Override
	public void updatePointRuleToEventMapping(PointRuleToEventMapping pointRuleToEventMapping) {
		// pointRuleToEventMapping.setUpdateTime(DateUtils.currentTimestamp());
		// pointRuleToEventMapping.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointRuleToEventMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointRuleToEventMappings.class, pointRuleToEventMapping.getId());
	}

    @Override
    public List<PointRuleToEventMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointRuleToEventMappings t = Tables.EH_POINT_RULE_TO_EVENT_MAPPINGS;

        SelectQuery<EhPointRuleToEventMappingsRecord> query = context().selectQuery(t);
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

        List<PointRuleToEventMapping> list = query.fetchInto(PointRuleToEventMapping.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

	@Override
	public PointRuleToEventMapping findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointRuleToEventMapping.class);
	}

    @Override
    public List<PointRuleToEventMapping> listByPointRule(Long systemId, Long pointRuleId) {
        com.everhomes.server.schema.tables.EhPointRuleToEventMappings t = Tables.EH_POINT_RULE_TO_EVENT_MAPPINGS;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            if (systemId != null) {
                query.addConditions(t.SYSTEM_ID.eq(systemId));
            }
            if (pointRuleId != null) {
                query.addConditions(t.RULE_ID.eq(pointRuleId));
            }
            return query;
        });
    }

    @Override
    public void createPointRuleToEventMappings(List<PointRuleToEventMapping> mappings) {
        for (PointRuleToEventMapping mapping : mappings) {
            Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointRuleToEventMappings.class));
            mapping.setId(id);
        }
        rwDao().insert(mappings.toArray(new EhPointRuleToEventMappings[mappings.size()]));
    }

    @Override
    public List<PointRuleToEventMapping> listByEventName(Integer namespaceId, Long systemId, String eventName) {
        com.everhomes.server.schema.tables.EhPointRuleToEventMappings t = Tables.EH_POINT_RULE_TO_EVENT_MAPPINGS;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            if (systemId != null) {
                query.addConditions(t.SYSTEM_ID.eq(systemId));
            }
            if (eventName != null) {
                query.addConditions(t.EVENT_NAME.eq(eventName));
            }
            return query;
        });
    }

    @Override
    public void deleteBySystemId(Long systemId) {
        com.everhomes.server.schema.tables.EhPointRuleToEventMappings t = Tables.EH_POINT_RULE_TO_EVENT_MAPPINGS;

        rwContext().delete(t)
                .where(t.SYSTEM_ID.eq(systemId))
                .execute();
    }

    private EhPointRuleToEventMappingsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointRuleToEventMappingsDao(context.configuration());
	}

	private EhPointRuleToEventMappingsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointRuleToEventMappingsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }
}
