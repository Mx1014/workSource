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
import com.everhomes.server.schema.tables.daos.EhVisitorSysVisitorsDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitors;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysVisitorProviderImpl implements VisitorSysVisitorProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysVisitors.class));
		visitorSysVisitor.setId(id);
		visitorSysVisitor.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysVisitor.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysVisitor.setOperateTime(visitorSysVisitor.getCreateTime());
		visitorSysVisitor.setOperatorUid(visitorSysVisitor.getCreatorUid());
		getReadWriteDao().insert(visitorSysVisitor);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysVisitors.class, null);
	}

	@Override
	public void updateVisitorSysVisitor(VisitorSysVisitor visitorSysVisitor) {
		assert (visitorSysVisitor.getId() != null);
		visitorSysVisitor.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysVisitor.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysVisitor);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysVisitors.class, visitorSysVisitor.getId());
	}

	@Override
	public VisitorSysVisitor findVisitorSysVisitorById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysVisitor.class);
	}
	
	@Override
	public List<VisitorSysVisitor> listVisitorSysVisitor() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_VISITORS)
				.orderBy(Tables.EH_VISITOR_SYS_VISITORS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysVisitor.class));
	}
	
	private EhVisitorSysVisitorsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysVisitorsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysVisitorsDao getDao(DSLContext context) {
		return new EhVisitorSysVisitorsDao(context.configuration());
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
