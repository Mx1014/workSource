// @formatter:off
package com.everhomes.print;

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
import com.everhomes.server.schema.tables.daos.EhSiyinPrintEmailsDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintEmails;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinPrintEmailProviderImpl implements SiyinPrintEmailProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSiyinPrintEmail(SiyinPrintEmail siyinPrintEmail) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintEmails.class));
		siyinPrintEmail.setId(id);
		siyinPrintEmail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintEmail.setCreatorUid(UserContext.current().getUser().getId());
//		siyinPrintEmail.setUpdateTime(siyinPrintEmail.getCreateTime());
		siyinPrintEmail.setOperatorUid(siyinPrintEmail.getCreatorUid());
		getReadWriteDao().insert(siyinPrintEmail);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintEmails.class, null);
	}

	@Override
	public void updateSiyinPrintEmail(SiyinPrintEmail siyinPrintEmail) {
		assert (siyinPrintEmail.getId() != null);
//		siyinPrintEmail.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintEmail.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(siyinPrintEmail);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintEmails.class, siyinPrintEmail.getId());
	}

	@Override
	public SiyinPrintEmail findSiyinPrintEmailById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintEmail.class);
	}
	
	@Override
	public List<SiyinPrintEmail> listSiyinPrintEmail() {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_EMAILS)
				.orderBy(Tables.EH_SIYIN_PRINT_EMAILS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintEmail.class));
	}
	
	private EhSiyinPrintEmailsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinPrintEmailsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinPrintEmailsDao getDao(DSLContext context) {
		return new EhSiyinPrintEmailsDao(context.configuration());
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
