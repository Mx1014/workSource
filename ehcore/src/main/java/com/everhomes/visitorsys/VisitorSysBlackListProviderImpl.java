// @formatter:off
package com.everhomes.visitorsys;

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
import com.everhomes.server.schema.tables.daos.EhVisitorSysBlackListDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysBlackList;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysBlackListProviderImpl implements VisitorSysBlackListProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysBlackList.class));
		visitorSysBlackList.setId(id);
		visitorSysBlackList.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysBlackList.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysBlackList.setOperateTime(visitorSysBlackList.getCreateTime());
		visitorSysBlackList.setOperatorUid(visitorSysBlackList.getCreatorUid());
		getReadWriteDao().insert(visitorSysBlackList);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysBlackList.class, null);
	}

	@Override
	public void updateVisitorSysBlackList(VisitorSysBlackList visitorSysBlackList) {
		assert (visitorSysBlackList.getId() != null);
		visitorSysBlackList.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysBlackList.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysBlackList);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysBlackList.class, visitorSysBlackList.getId());
	}

	@Override
	public VisitorSysBlackList findVisitorSysBlackListById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysBlackList.class);
	}
	
	@Override
	public List<VisitorSysBlackList> listVisitorSysBlackList() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_BLACK_LIST)
				.orderBy(Tables.EH_VISITOR_SYS_BLACK_LIST.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysBlackList.class));
	}
	
	private EhVisitorSysBlackListDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysBlackListDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysBlackListDao getDao(DSLContext context) {
		return new EhVisitorSysBlackListDao(context.configuration());
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
