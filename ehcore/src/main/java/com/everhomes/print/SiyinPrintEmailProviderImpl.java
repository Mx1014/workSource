// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintEmailsDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintEmails;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinPrintEmailProviderImpl implements SiyinPrintEmailProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintEmailProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSiyinPrintEmail(SiyinPrintEmail siyinPrintEmail) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintEmails.class));
		siyinPrintEmail.setId(id);
		siyinPrintEmail.setCreatorUid(UserContext.current().getUser().getId());
		siyinPrintEmail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintEmail.setOperatorUid(siyinPrintEmail.getCreatorUid());
		siyinPrintEmail.setOperateTime(siyinPrintEmail.getCreateTime());
		dbProvider.execute(r->{
			getReadWriteContext().update(Tables.EH_SIYIN_PRINT_EMAILS).set(Tables.EH_SIYIN_PRINT_EMAILS.STATUS, CommonStatus.INACTIVE.getCode())
			.where(Tables.EH_SIYIN_PRINT_EMAILS.USER_ID.eq(siyinPrintEmail.getUserId())).execute();
			getReadWriteDao().insert(siyinPrintEmail);
			return null;
		});
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintEmails.class, null);
	}

	@Override
	public void updateSiyinPrintEmail(SiyinPrintEmail siyinPrintEmail) {
		assert (siyinPrintEmail.getId() != null);
		
		dbProvider.execute(r -> {
			getReadWriteContext().update(Tables.EH_SIYIN_PRINT_EMAILS).set(Tables.EH_SIYIN_PRINT_EMAILS.STATUS, CommonStatus.INACTIVE.getCode())
			.where(Tables.EH_SIYIN_PRINT_EMAILS.USER_ID.eq(siyinPrintEmail.getUserId())).execute();
			
			siyinPrintEmail.setOperatorUid(UserContext.current().getUser().getId());
			getReadWriteDao().update(siyinPrintEmail);
			return null;
		});
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintEmails.class, siyinPrintEmail.getId());
	}

	@Override
	public SiyinPrintEmail findSiyinPrintEmailByUserId(Long userId) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_EMAILS)
			.where(Tables.EH_SIYIN_PRINT_EMAILS.USER_ID.eq(userId))
			.and(Tables.EH_SIYIN_PRINT_EMAILS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		LOGGER.debug("findSiyinPrintEmailByUserId sql = {}, params = {}.",query.getSQL(),query.getBindValues());
		List<SiyinPrintEmail> list = query.fetch()
			.map(r->ConvertHelper.convert(r, SiyinPrintEmail.class));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
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
