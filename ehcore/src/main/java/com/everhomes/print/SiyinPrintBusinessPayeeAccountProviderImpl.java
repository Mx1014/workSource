// @formatter:off
package com.everhomes.print;

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
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintBusinessPayeeAccountsDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintBusinessPayeeAccounts;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinPrintBusinessPayeeAccountProviderImpl implements SiyinPrintBusinessPayeeAccountProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSiyinPrintBusinessPayeeAccount(SiyinPrintBusinessPayeeAccount siyinPrintBusinessPayeeAccount) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintBusinessPayeeAccounts.class));
		siyinPrintBusinessPayeeAccount.setId(id);
		siyinPrintBusinessPayeeAccount.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintBusinessPayeeAccount.setCreatorUid(UserContext.current().getUser().getId());
		siyinPrintBusinessPayeeAccount.setOperateTime(siyinPrintBusinessPayeeAccount.getCreateTime());
		siyinPrintBusinessPayeeAccount.setOperatorUid(siyinPrintBusinessPayeeAccount.getCreatorUid());
		getReadWriteDao().insert(siyinPrintBusinessPayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintBusinessPayeeAccounts.class, null);
	}

	@Override
	public void updateSiyinPrintBusinessPayeeAccount(SiyinPrintBusinessPayeeAccount siyinPrintBusinessPayeeAccount) {
		assert (siyinPrintBusinessPayeeAccount.getId() != null);
		siyinPrintBusinessPayeeAccount.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintBusinessPayeeAccount.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(siyinPrintBusinessPayeeAccount);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintBusinessPayeeAccounts.class, siyinPrintBusinessPayeeAccount.getId());
	}

	@Override
	public SiyinPrintBusinessPayeeAccount findSiyinPrintBusinessPayeeAccountById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintBusinessPayeeAccount.class);
	}
	
	@Override
	public List<SiyinPrintBusinessPayeeAccount> listSiyinPrintBusinessPayeeAccount() {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS)
				.orderBy(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintBusinessPayeeAccount.class));
	}
	
	private EhSiyinPrintBusinessPayeeAccountsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinPrintBusinessPayeeAccountsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinPrintBusinessPayeeAccountsDao getDao(DSLContext context) {
		return new EhSiyinPrintBusinessPayeeAccountsDao(context.configuration());
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
	public List<SiyinPrintBusinessPayeeAccount> findRepeatBusinessPayeeAccounts(Long id, Integer namespaceId, String ownerType, Long ownerId) {
		Condition condition = Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.NAMESPACE_ID.eq(namespaceId)
				.and(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.STATUS.eq((byte)2));
		if(id!=null){
			condition=condition.and(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.ID.notEqual(id));
		}
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS)
				.where(condition)
				.orderBy(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintBusinessPayeeAccount.class));
	}

	@Override
	public SiyinPrintBusinessPayeeAccount getSiyinPrintBusinessPayeeAccountByOwner(Integer namespaceId, String ownerType, Long ownerId) {
		Condition condition = Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.NAMESPACE_ID.eq(namespaceId)
				.and(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.STATUS.eq((byte)2));
		List<SiyinPrintBusinessPayeeAccount> list = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS)
				.where(condition)
				.orderBy(Tables.EH_SIYIN_PRINT_BUSINESS_PAYEE_ACCOUNTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintBusinessPayeeAccount.class));
		if(list == null || list.size()==0){
			return null;
		}
		return list.get(0);
	}


}
