// @formatter:off
package com.everhomes.socialSecurity;

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
import com.everhomes.server.schema.tables.daos.EhAccumulationFundBasesDao;
import com.everhomes.server.schema.tables.pojos.EhAccumulationFundBases;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AccumulationFundBaseProviderImpl implements AccumulationFundBaseProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createAccumulationFundBase(AccumulationFundBase accumulationFundBase) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAccumulationFundBases.class));
		accumulationFundBase.setId(id);
		accumulationFundBase.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		accumulationFundBase.setCreatorUid(UserContext.current().getUser().getId());
		accumulationFundBase.setUpdateTime(accumulationFundBase.getCreateTime());
		accumulationFundBase.setOperatorUid(accumulationFundBase.getCreatorUid());
		getReadWriteDao().insert(accumulationFundBase);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAccumulationFundBases.class, null);
	}

	@Override
	public void updateAccumulationFundBase(AccumulationFundBase accumulationFundBase) {
		assert (accumulationFundBase.getId() != null);
		accumulationFundBase.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		accumulationFundBase.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(accumulationFundBase);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAccumulationFundBases.class, accumulationFundBase.getId());
	}

	@Override
	public AccumulationFundBase findAccumulationFundBaseById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), AccumulationFundBase.class);
	}
	
	@Override
	public List<AccumulationFundBase> listAccumulationFundBase() {
		return getReadOnlyContext().select().from(Tables.EH_ACCUMULATION_FUND_BASES)
				.orderBy(Tables.EH_ACCUMULATION_FUND_BASES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, AccumulationFundBase.class));
	}
	
	private EhAccumulationFundBasesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhAccumulationFundBasesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhAccumulationFundBasesDao getDao(DSLContext context) {
		return new EhAccumulationFundBasesDao(context.configuration());
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
