// @formatter:off
package com.everhomes.visitorsys;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhVisitorSysVisitReasonDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitReason;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysVisitReasonProviderImpl implements VisitorSysVisitReasonProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysVisitReason(VisitorSysVisitReason visitorSysVisitReason) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysVisitReason.class));
		visitorSysVisitReason.setId(id);
		visitorSysVisitReason.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysVisitReason.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysVisitReason.setOperateTime(visitorSysVisitReason.getCreateTime());
		visitorSysVisitReason.setOperatorUid(visitorSysVisitReason.getCreatorUid());
		getReadWriteDao().insert(visitorSysVisitReason);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysVisitReason.class, null);
	}

	@Override
	public void updateVisitorSysVisitReason(VisitorSysVisitReason visitorSysVisitReason) {
		assert (visitorSysVisitReason.getId() != null);
		visitorSysVisitReason.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysVisitReason.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysVisitReason);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysVisitReason.class, visitorSysVisitReason.getId());
	}

	@Override
	public VisitorSysVisitReason findVisitorSysVisitReasonById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysVisitReason.class);
	}
	
	@Override
	@Cacheable(value="listVisitorSysVisitReason", key="{#namespaceId,#communityType}", unless="#result.size() == 0")
	public List<VisitorSysVisitReason> listVisitorSysVisitReason(Integer namespaceId,Byte communityType) {
		List<VisitorSysVisitReason> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_VISIT_REASON)
				.where(Tables.EH_VISITOR_SYS_VISIT_REASON.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_VISIT_REASON.STATUS.eq((byte)2))
				.orderBy(Tables.EH_VISITOR_SYS_VISIT_REASON.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysVisitReason.class));
		if(list==null || list.size()==0){
			return  getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_VISIT_REASON)
					.where(Tables.EH_VISITOR_SYS_VISIT_REASON.NAMESPACE_ID.eq(0))
					.and(Tables.EH_VISITOR_SYS_VISIT_REASON.STATUS.eq((byte)2))
					.and(Tables.EH_VISITOR_SYS_VISIT_REASON.REASON_TYPE.eq(communityType))
					.orderBy(Tables.EH_VISITOR_SYS_VISIT_REASON.ID.asc())
					.fetch().map(r -> ConvertHelper.convert(r, VisitorSysVisitReason.class));
		}
		return list;
	}
	
	private EhVisitorSysVisitReasonDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysVisitReasonDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysVisitReasonDao getDao(DSLContext context) {
		return new EhVisitorSysVisitReasonDao(context.configuration());
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
