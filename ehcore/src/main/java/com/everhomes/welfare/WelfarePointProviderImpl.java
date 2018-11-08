// @formatter:off
package com.everhomes.welfare;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWelfarePointsDao;
import com.everhomes.server.schema.tables.pojos.EhWelfarePoints;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class WelfarePointProviderImpl implements WelfarePointProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createWelfarePoint(WelfarePoint welfarePoint) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWelfarePoints.class));
		welfarePoint.setId(id);
		welfarePoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		getReadWriteDao().insert(welfarePoint);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhWelfarePoints.class, null);
	}

	@Override
	public void updateWelfarePoint(WelfarePoint welfarePoint) {
		assert (welfarePoint.getId() != null);
		getReadWriteDao().update(welfarePoint);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWelfarePoints.class, welfarePoint.getId());
	}

	@Override
	public WelfarePoint findWelfarePointById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), WelfarePoint.class);
	}
	
	@Override
	public List<WelfarePoint> listWelfarePoint() {
		return getReadOnlyContext().select().from(Tables.EH_WELFARE_POINTS)
				.orderBy(Tables.EH_WELFARE_POINTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, WelfarePoint.class));
	}
	
	private EhWelfarePointsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhWelfarePointsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhWelfarePointsDao getDao(DSLContext context) {
		return new EhWelfarePointsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
