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
import com.everhomes.server.schema.tables.records.EhPointGoodsRecord;
import com.everhomes.server.schema.tables.daos.EhPointGoodsDao;
import com.everhomes.server.schema.tables.pojos.EhPointGoods;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointGoodProviderImpl implements PointGoodProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointGood(PointGood pointGood) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointGoods.class));
		pointGood.setId(id);
		pointGood.setCreateTime(DateUtils.currentTimestamp());
		// pointGood.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointGood);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointGoods.class, id);
	}

	@Override
	public void updatePointGood(PointGood pointGood) {
		// pointGood.setUpdateTime(DateUtils.currentTimestamp());
		// pointGood.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointGood);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointGoods.class, pointGood.getId());
	}

    @Override
    public List<PointGood> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointGoods t = Tables.EH_POINT_GOODS;

        SelectQuery<EhPointGoodsRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.lt(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count);
        }
        query.addOrderBy(t.ID.desc());

        List<PointGood> list = query.fetchInto(PointGood.class);
        if (list.size() > count) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

	@Override
	public PointGood findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointGood.class);
	}

    @Override
    public List<PointGood> listPointGood(Integer namespaceId, Long systemId, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointGoods t = Tables.EH_POINT_GOODS;
        return this.query(locator, pageSize, (locator1, query) -> {
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            query.addOrderBy(t.TOP_TIME.desc());
            return query;
        });
    }

    private EhPointGoodsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointGoodsDao(context.configuration());
	}

	private EhPointGoodsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointGoodsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
