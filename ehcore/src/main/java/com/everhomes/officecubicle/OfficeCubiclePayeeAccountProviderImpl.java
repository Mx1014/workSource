// @formatter:off
package com.everhomes.officecubicle;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.officecubicle.OfficeCubiclePayeeAccountDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOfficeCubiclePayeeAccounts;
import com.everhomes.server.schema.tables.daos.EhOfficeCubiclePayeeAccountsDao;
import com.everhomes.server.schema.tables.daos.EhParkingBusinessPayeeAccountsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingBusinessPayeeAccounts;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class OfficeCubiclePayeeAccountProviderImpl implements OfficeCubiclePayeeAccountProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createOfficeCubiclePayeeAccount(OfficeCubiclePayeeAccount officeCubiclePayeeAccount) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhParkingBusinessPayeeAccounts.class));
		officeCubiclePayeeAccount.setId(id);
		officeCubiclePayeeAccount.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubiclePayeeAccount.setCreatorUid(UserContext.current().getUser().getId());
		officeCubiclePayeeAccount.setOperateTime(officeCubiclePayeeAccount.getCreateTime());
		officeCubiclePayeeAccount.setOperatorUid(officeCubiclePayeeAccount.getCreatorUid());
		getReadWriteDao().insert(officeCubiclePayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubiclePayeeAccounts.class, null);
	}

	@Override
	public void updateOfficeCubiclePayeeAccount(OfficeCubiclePayeeAccount officeCubiclePayeeAccount) {
		assert (officeCubiclePayeeAccount.getId() != null);
		officeCubiclePayeeAccount.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubiclePayeeAccount.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(officeCubiclePayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubiclePayeeAccounts.class, officeCubiclePayeeAccount.getId());
	}

	@Override
	public OfficeCubiclePayeeAccount findOfficeCubiclePayeeAccountById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), OfficeCubiclePayeeAccount.class);
	}
	
	@Override
	public List<OfficeCubiclePayeeAccount> listOfficeCubiclePayeeAccount() {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS)
				.orderBy(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubiclePayeeAccount.class));
	}
	
	private EhOfficeCubiclePayeeAccountsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhOfficeCubiclePayeeAccountsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhOfficeCubiclePayeeAccountsDao getDao(DSLContext context) {
		return new EhOfficeCubiclePayeeAccountsDao(context.configuration());
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
	public List<OfficeCubiclePayeeAccount> findRepeatOfficeCubiclePayeeAccounts(Long id, Integer namespaceId, String ownerType, Long ownerId, Long spaceId) {
		Condition condition = Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.NAMESPACE_ID.eq(namespaceId)
				.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.STATUS.eq((byte)2));
		if(spaceId!=null){
			condition=condition.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.SPACE_ID.eq(spaceId));
		}

		if(id!=null){
			condition=condition.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.ID.notEqual(id));
		}
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS)
				.where(condition)
				.orderBy(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubiclePayeeAccount.class));
	}

	
	@Override
	public List<OfficeCubiclePayeeAccount> listOfficeCubiclePayeeAccountByOwner(Integer namespaceId, String ownerType, Long ownerId, Long spaceId) {
		Condition condition = Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.NAMESPACE_ID.eq(namespaceId)
				.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.STATUS.eq((byte)2));
		if(spaceId!=null){
			condition=condition.and(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.SPACE_ID.eq(spaceId));
		}

		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS)
				.where(condition)
				.orderBy(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubiclePayeeAccount.class));
	}

	@Override
	public void deleteOfficeCubiclePayeeAccount(Long id) {
		getReadWriteContext()
				.update(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS)
				.set(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.STATUS,(byte)0)
				.where(Tables.EH_OFFICE_CUBICLE_PAYEE_ACCOUNTS.ID.eq(id)).execute();
	}
	
}
