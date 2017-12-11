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
import com.everhomes.server.schema.tables.records.EhPointActionsRecord;
import com.everhomes.server.schema.tables.daos.EhPointActionsDao;
import com.everhomes.server.schema.tables.pojos.EhPointActions;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class PointActionProviderImpl implements PointActionProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointAction(PointAction pointAction) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointActions.class));
		pointAction.setId(id);
		pointAction.setCreateTime(DateUtils.currentTimestamp());
		// pointAction.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointAction);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointActions.class, id);
	}

	@Override
	public void updatePointAction(PointAction pointAction) {
		// pointAction.setUpdateTime(DateUtils.currentTimestamp());
		// pointAction.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointAction);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointActions.class, pointAction.getId());
	}

    @Override
    public List<PointAction> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointActions t = Tables.EH_POINT_ACTIONS;

        SelectQuery<EhPointActionsRecord> query = context().selectQuery(t);
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

        List<PointAction> list = query.fetchInto(PointAction.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

	@Override
	public PointAction findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointAction.class);
	}

    @Override
    public List<PointAction> listPointActionsBySystemId(Long systemId) {
        com.everhomes.server.schema.tables.EhPointActions t = Tables.EH_POINT_ACTIONS;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            query.addConditions(t.SYSTEM_ID.eq(systemId));
            return query;
        });
    }

    @Override
    public void createPointActions(List<PointAction> pointActions) {
        Timestamp createTime = DateUtils.currentTimestamp();
        for (PointAction action : pointActions) {
            Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointActions.class));
            action.setId(id);
            action.setCreateTime(createTime);
        }
        rwDao().insert(pointActions.toArray(new EhPointActions[pointActions.size()]));
    }

    @Override
    public PointAction findByOwner(Integer namespaceId, Long systemId, String ownerType, Long ownerId) {
        com.everhomes.server.schema.tables.EhPointActions t = Tables.EH_POINT_ACTIONS;
        List<PointAction> list = this.query(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            query.addConditions(t.SYSTEM_ID.eq(systemId));
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
            query.addConditions(t.OWNER_ID.eq(ownerId));
            return query;
        });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private EhPointActionsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointActionsDao(context.configuration());
	}

	private EhPointActionsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointActionsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
