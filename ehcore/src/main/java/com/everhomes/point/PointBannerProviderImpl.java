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
import com.everhomes.server.schema.tables.daos.EhPointBannersDao;
import com.everhomes.server.schema.tables.pojos.EhPointBanners;
import com.everhomes.server.schema.tables.records.EhPointBannersRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class PointBannerProviderImpl implements PointBannerProvider {

    private final com.everhomes.server.schema.tables.EhPointBanners t = Tables.EH_POINT_BANNERS;

    @Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointBanner(PointBanner pointBanner) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointBanners.class));
		pointBanner.setId(id);
		pointBanner.setCreateTime(DateUtils.currentTimestamp());
		pointBanner.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointBanner);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointBanners.class, id);
	}

	@Override
	public void updatePointBanner(PointBanner pointBanner) {
		pointBanner.setUpdateTime(DateUtils.currentTimestamp());
		pointBanner.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointBanner);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointBanners.class, pointBanner.getId());
	}

    @Override
    public List<PointBanner> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        SelectQuery<EhPointBannersRecord> query = context().selectQuery(t);
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

        List<PointBanner> list = query.fetchInto(PointBanner.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

	@Override
	public PointBanner findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointBanner.class);
	}

    @Override
    public List<PointBanner> listByIds(Set<Long> ids) {
        return context().selectFrom(t)
                .where(t.ID.in(ids))
                .fetchInto(PointBanner.class);
    }

    @Override
    public List<PointBanner> listPointBannersBySystemId(Long systemId, int pageSize, ListingLocator locator) {
        return this.query(locator, pageSize, (locator1, query) -> {
            query.addConditions(t.SYSTEM_ID.eq(systemId));
            query.addOrderBy(t.DEFAULT_ORDER.asc());
            return query;
        });
    }

    @Override
    public void deletePointBanner(PointBanner banner) {
        rwDao().delete(banner);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointBanners.class, banner.getId());
    }

    private EhPointBannersDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointBannersDao(context.configuration());
	}

	private EhPointBannersDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointBannersDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }
}
