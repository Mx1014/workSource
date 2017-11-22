// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPointScoresDao;
import com.everhomes.server.schema.tables.pojos.EhPointScores;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

@Repository
public class PointScoreProviderImpl implements PointScoreProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointScore(PointScore pointScore) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointScores.class));
		pointScore.setId(id);
		pointScore.setCreateTime(DateUtils.currentTimestamp());
		// pointScore.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointScore);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointScores.class, id);
	}

	@Override
	public void updatePointScore(PointScore pointScore) {
		// pointScore.setUpdateTime(DateUtils.currentTimestamp());
		// pointScore.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointScore);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointScores.class, pointScore.getId());
	}

	@Override
	public PointScore findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointScore.class);
	}

    @Override
    public <T> T findUserPointScore(Integer namespaceId, Long systemId, Long uid, Class<T> clazz) {
        com.everhomes.server.schema.tables.EhPointScores t = Tables.EH_POINT_SCORES;
        return context().selectFrom(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.SYSTEM_ID.eq(systemId))
                .and(t.USER_ID.eq(uid))
                .orderBy(t.ID.desc())
                .fetchAnyInto(clazz);
    }

    private EhPointScoresDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointScoresDao(context.configuration());
	}

	private EhPointScoresDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointScoresDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
