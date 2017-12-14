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
import com.everhomes.server.schema.tables.daos.EhAccumulationFundPaymentsDao;
import com.everhomes.server.schema.tables.pojos.EhAccumulationFundPayments;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AccumulationFundPaymentProviderImpl implements AccumulationFundPaymentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createAccumulationFundPayment(AccumulationFundPayment accumulationFundPayment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAccumulationFundPayments.class));
		accumulationFundPayment.setId(id);
		accumulationFundPayment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		accumulationFundPayment.setCreatorUid(UserContext.current().getUser().getId());
		accumulationFundPayment.setUpdateTime(accumulationFundPayment.getCreateTime());
		accumulationFundPayment.setOperatorUid(accumulationFundPayment.getCreatorUid());
		getReadWriteDao().insert(accumulationFundPayment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAccumulationFundPayments.class, null);
	}

	@Override
	public void updateAccumulationFundPayment(AccumulationFundPayment accumulationFundPayment) {
		assert (accumulationFundPayment.getId() != null);
		accumulationFundPayment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		accumulationFundPayment.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(accumulationFundPayment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAccumulationFundPayments.class, accumulationFundPayment.getId());
	}

	@Override
	public AccumulationFundPayment findAccumulationFundPaymentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), AccumulationFundPayment.class);
	}
	
	@Override
	public List<AccumulationFundPayment> listAccumulationFundPayment() {
		return getReadOnlyContext().select().from(Tables.EH_ACCUMULATION_FUND_PAYMENTS)
				.orderBy(Tables.EH_ACCUMULATION_FUND_PAYMENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, AccumulationFundPayment.class));
	}
	
	private EhAccumulationFundPaymentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhAccumulationFundPaymentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhAccumulationFundPaymentsDao getDao(DSLContext context) {
		return new EhAccumulationFundPaymentsDao(context.configuration());
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
