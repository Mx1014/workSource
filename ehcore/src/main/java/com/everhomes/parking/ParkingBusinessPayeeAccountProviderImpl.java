// @formatter:off
package com.everhomes.parking;

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
import com.everhomes.server.schema.tables.daos.EhParkingBusinessPayeeAccountsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingBusinessPayeeAccounts;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ParkingBusinessPayeeAccountProviderImpl implements ParkingBusinessPayeeAccountProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createParkingBusinessPayeeAccount(ParkingBusinessPayeeAccount parkingBusinessPayeeAccount) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhParkingBusinessPayeeAccounts.class));
		parkingBusinessPayeeAccount.setId(id);
		parkingBusinessPayeeAccount.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		parkingBusinessPayeeAccount.setCreatorUid(UserContext.current().getUser().getId());
		parkingBusinessPayeeAccount.setOperateTime(parkingBusinessPayeeAccount.getCreateTime());
		parkingBusinessPayeeAccount.setOperatorUid(parkingBusinessPayeeAccount.getCreatorUid());
		getReadWriteDao().insert(parkingBusinessPayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingBusinessPayeeAccounts.class, null);
	}

	@Override
	public void updateParkingBusinessPayeeAccount(ParkingBusinessPayeeAccount parkingBusinessPayeeAccount) {
		assert (parkingBusinessPayeeAccount.getId() != null);
		parkingBusinessPayeeAccount.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		parkingBusinessPayeeAccount.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(parkingBusinessPayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingBusinessPayeeAccounts.class, parkingBusinessPayeeAccount.getId());
	}

	@Override
	public ParkingBusinessPayeeAccount findParkingBusinessPayeeAccountById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ParkingBusinessPayeeAccount.class);
	}
	
	@Override
	public List<ParkingBusinessPayeeAccount> listParkingBusinessPayeeAccount() {
		return getReadOnlyContext().select().from(Tables.EH_PARKING_BUSINESS_PAYEE_ACCOUNTS)
				.orderBy(Tables.EH_PARKING_BUSINESS_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ParkingBusinessPayeeAccount.class));
	}
	
	private EhParkingBusinessPayeeAccountsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhParkingBusinessPayeeAccountsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhParkingBusinessPayeeAccountsDao getDao(DSLContext context) {
		return new EhParkingBusinessPayeeAccountsDao(context.configuration());
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
