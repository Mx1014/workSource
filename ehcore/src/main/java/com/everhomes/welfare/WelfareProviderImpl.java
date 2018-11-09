// @formatter:off
package com.everhomes.welfare;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWelfaresDao;
import com.everhomes.server.schema.tables.pojos.EhWelfares;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class WelfareProviderImpl implements WelfareProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createWelfare(Welfare welfare) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWelfares.class));
		welfare.setId(id);
		welfare.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		welfare.setCreatorUid(UserContext.currentUserId());
		welfare.setUpdateTime(welfare.getCreateTime());
		welfare.setOperatorUid(welfare.getCreatorUid());
		getReadWriteDao().insert(welfare);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhWelfares.class, null);
	}

	@Override
	public void updateWelfare(Welfare welfare) {
		assert (welfare.getId() != null);
		welfare.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		welfare.setOperatorUid(UserContext.currentUserId());
		getReadWriteDao().update(welfare);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWelfares.class, welfare.getId());
	}

	@Override
	public Welfare findWelfareById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Welfare.class);
	}
	
	@Override
	public List<Welfare> listWelfare(Long ownerId, Integer offset, Integer pageSize) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_WELFARES)
				.where(Tables.EH_WELFARES.ORGANIZATION_ID.eq(ownerId));
		if (null != pageSize && null != offset) {
			step.limit(offset,pageSize);
		}
		Result<Record> records = step.orderBy(Tables.EH_WELFARES.ID.desc()).fetch();
		if(null == records){
			return null;
		}
		return records.map(r -> ConvertHelper.convert(r, Welfare.class));
	}

	@Override
	public void deleteWelfare(Long welfareId) {
		getReadWriteDao().deleteById(welfareId);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWelfares.class, welfareId);

	}

	private EhWelfaresDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhWelfaresDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhWelfaresDao getDao(DSLContext context) {
		return new EhWelfaresDao(context.configuration());
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

	@Override
	public List<Welfare> listWelfareByIds(List<Long> welfaeIds) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_WELFARES)
				.where(Tables.EH_WELFARES.ID.in(welfaeIds));
		Result<Record> records = step.orderBy(Tables.EH_WELFARES.SEND_TIME.desc()).fetch();
		if(null == records){
			return null;
		}
		return records.map(r -> ConvertHelper.convert(r, Welfare.class));
	}
}
