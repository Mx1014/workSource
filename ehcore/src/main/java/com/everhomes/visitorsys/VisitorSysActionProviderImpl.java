// @formatter:off
package com.everhomes.visitorsys;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.util.derby.sys.Sys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhVisitorSysActionsDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysActions;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysActionProviderImpl implements VisitorSysActionProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysAction(VisitorSysAction visitorSysAction) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysActions.class));
		visitorSysAction.setId(id);
		visitorSysAction.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysAction.setOperatorUid(visitorSysAction.getCreatorUid());
		getReadWriteDao().insert(visitorSysAction);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysActions.class, null);
	}

	@Override
	public void updateVisitorSysAction(VisitorSysAction visitorSysAction) {
		assert (visitorSysAction.getId() != null);
		visitorSysAction.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysAction.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysAction);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysActions.class, visitorSysAction.getId());
	}

	@Override
	public VisitorSysAction findVisitorSysActionById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysAction.class);
	}
	
	@Override
	public List<VisitorSysAction> listVisitorSysAction() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_ACTIONS)
				.orderBy(Tables.EH_VISITOR_SYS_ACTIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysAction.class));
	}

	@Override
	public VisitorSysAction findVisitorSysActionByAction(Long visitorId, Byte actionFlag) {
		List<VisitorSysAction> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_ACTIONS)
				.where(Tables.EH_VISITOR_SYS_ACTIONS.VISITOR_ID.eq(visitorId))
				.and(Tables.EH_VISITOR_SYS_ACTIONS.ACTION_TYPE.eq(actionFlag))
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysAction.class));
		if(list==null || list.size()==0){
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<VisitorSysAction> listVisitorSysActionByVisitorId(Long visitorId) {
		List<VisitorSysAction> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_ACTIONS)
				.where(Tables.EH_VISITOR_SYS_ACTIONS.VISITOR_ID.eq(visitorId))
				.orderBy(Tables.EH_VISITOR_SYS_ACTIONS.ACTION_TYPE.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysAction.class));
		return list;
	}

	private EhVisitorSysActionsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysActionsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysActionsDao getDao(DSLContext context) {
		return new EhVisitorSysActionsDao(context.configuration());
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
