// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPointTutorialsDao;
import com.everhomes.server.schema.tables.pojos.EhPointTutorials;
import com.everhomes.server.schema.tables.records.EhPointTutorialsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointTutorialProviderImpl implements PointTutorialProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointTutorial(PointTutorial pointTutorial) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointTutorials.class));
		pointTutorial.setId(id);
		pointTutorial.setCreateTime(DateUtils.currentTimestamp());
		// pointTutorial.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointTutorial);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointTutorials.class, id);
	}

	@Override
	public void updatePointTutorial(PointTutorial pointTutorial) {
		// pointTutorial.setUpdateTime(DateUtils.currentTimestamp());
		// pointTutorial.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointTutorial);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointTutorials.class, pointTutorial.getId());
	}

	@Override
	public PointTutorial findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointTutorial.class);
	}

    @Override
    public List<PointTutorial> listPointTutorials(Long systemId, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointTutorials t = Tables.EH_POINT_TUTORIALS;
        SelectQuery<EhPointTutorialsRecord> query = context().selectFrom(t).getQuery();

        query.addConditions(t.SYSTEM_ID.eq(systemId));
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.le(locator.getAnchor()));
        }
        query.addLimit(pageSize);

        List<PointTutorial> tutorials = query.fetchInto(PointTutorial.class);
        if (tutorials.size() > pageSize) {
            locator.setAnchor(tutorials.get(tutorials.size() - 1).getId());
            tutorials.remove(tutorials.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return tutorials;
    }

    @Override
    public void deleteTutorial(PointTutorial tutorial) {
        rwDao().delete(tutorial);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointTutorials.class, tutorial.getId());
    }

    private EhPointTutorialsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointTutorialsDao(context.configuration());
	}

	private EhPointTutorialsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointTutorialsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
