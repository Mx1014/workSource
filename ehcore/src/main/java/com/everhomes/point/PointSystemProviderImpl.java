// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.point.PointCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPointSystemsDao;
import com.everhomes.server.schema.tables.pojos.EhPointSystems;
import com.everhomes.server.schema.tables.records.EhPointSystemsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointSystemProviderImpl implements PointSystemProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointSystem(PointSystem pointSystem) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointSystems.class));
		pointSystem.setId(id);
		pointSystem.setCreateTime(DateUtils.currentTimestamp());
		pointSystem.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointSystem);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointSystems.class, id);
	}

	@Override
	public void updatePointSystem(PointSystem pointSystem) {
		pointSystem.setUpdateTime(DateUtils.currentTimestamp());
		pointSystem.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointSystem);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointSystems.class, pointSystem.getId());
	}

    @Override
    public List<PointSystem> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointSystems t = Tables.EH_POINT_SYSTEMS;

        SelectQuery<EhPointSystemsRecord> query = context().selectQuery(t);
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

        List<PointSystem> list = query.fetchInto(PointSystem.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

	@Override
	public PointSystem findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointSystem.class);
	}

    @Override
    public List<PointSystem> getEnabledPointSystems(Integer namespaceId) {
        com.everhomes.server.schema.tables.EhPointSystems t = Tables.EH_POINT_SYSTEMS;
        return query(new ListingLocator(), -1, (locator, query) -> {
            if (namespaceId != null) {
                query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            }
            query.addConditions(t.STATUS.eq(PointCommonStatus.ENABLED.getCode()));
            return query;
        });
    }

    @Override
    public List<PointSystem> listPointSystems(Integer namespaceId, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointSystems t = Tables.EH_POINT_SYSTEMS;
        return this.query(locator, pageSize, (locator1, query) -> {
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            return query;
        });
    }

    private EhPointSystemsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointSystemsDao(context.configuration());
	}

	private EhPointSystemsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointSystemsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
