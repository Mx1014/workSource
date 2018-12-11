// @formatter:off
package com.everhomes.visitorsys;

import java.sql.Timestamp;
import java.util.*;

import com.everhomes.rest.visitorsys.VisitorsysOwnerType;
import com.everhomes.rest.visitorsys.VisitorsysSearchFlagType;
import com.everhomes.rest.visitorsys.VisitorsysStatus;
import com.everhomes.rest.visitorsys.VisitorsysVisitorType;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.impl.DSL;
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

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;

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
	public VisitorSysVisitor findVisitorSysVisitorById(Integer namespaceId, Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysVisitor.class);
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

	@Override
	public List<VisitorSysVisitor> listVisitorSysVisitor(ListBookedVisitorParams params) {
		com.everhomes.server.schema.tables.EhVisitorSysVisitors community = Tables.EH_VISITOR_SYS_VISITORS.as("community");
		com.everhomes.server.schema.tables.EhVisitorSysVisitors enterprise = Tables.EH_VISITOR_SYS_VISITORS.as("enterprise");
		Condition condition = DSL.trueCondition();
		if(params.getNamespaceId()!=null) {
			condition = community.NAMESPACE_ID.eq(params.getNamespaceId());
		}
		if(params.getOwnerType()!=null) {
			condition = condition.and(community.OWNER_TYPE.eq(params.getOwnerType()));
		}
		if(params.getOwnerId()!=null) {
			condition = condition.and(community.OWNER_ID.eq(params.getOwnerId()));
		}
		if(params.getVisitorType()!=null){
			condition=condition.and(community.VISITOR_TYPE.eq(params.getVisitorType()));
		}
		if(params.getVisitStatus()!=null){
			condition=condition.and(community.VISIT_STATUS.in(params.getVisitStatus()));
		}
		if(params.getBookingStatus()!=null){
			condition=condition.and(community.BOOKING_STATUS.in(params.getBookingStatus()));
		}
		if(params.getOfficeLocationId()!=null){
			condition=condition.and(community.OFFICE_LOCATION_ID.in(params.getOfficeLocationId()));
		}
		if(params.getVisitReasonId()!=null){
			condition = condition.and(community.VISIT_REASON_ID.lt(params.getVisitReasonId()));
		}

		SortField sortField = null;
		VisitorsysSearchFlagType visitorsysSearchFlagType = VisitorsysSearchFlagType.fromCode(params.getSearchFlag());
		if(visitorsysSearchFlagType == VisitorsysSearchFlagType.BOOKING_MANAGEMENT) {
			if (params.getStartPlannedVisitTime() != null) {
				condition = condition.and(community.PLANNED_VISIT_TIME.gt(new Timestamp(params.getStartPlannedVisitTime())));
			}
			if (params.getEndPlannedVisitTime() != null) {
				condition = condition.and(community.PLANNED_VISIT_TIME.lt(new Timestamp(params.getEndPlannedVisitTime())));
			}
			sortField = community.PLANNED_VISIT_TIME.desc();
			if(params.getPageAnchor()!=null) {
				condition = condition.and(community.PLANNED_VISIT_TIME.gt(new Timestamp(params.getPageAnchor())));
			}
		}else if (visitorsysSearchFlagType == VisitorsysSearchFlagType.VISITOR_MANAGEMENT){
			if (params.getStartVisitTime() != null) {
				condition = condition.and(community.VISIT_TIME.gt(new Timestamp(params.getStartVisitTime())));
			}
			if (params.getEndVisitTime() != null) {
				condition = condition.and(community.VISIT_TIME.lt(new Timestamp(params.getEndVisitTime())));
			}
			sortField = community.VISIT_TIME.asc();
			if(params.getPageAnchor()!=null) {
				condition = condition.and(community.VISIT_TIME.lt(new Timestamp(params.getPageAnchor())));
			}
		}else {
			sortField = community.ID.desc();
			if(params.getPageAnchor()!=null) {
				condition = condition.and(community.ID.lt(params.getPageAnchor()));
			}
		}

		//todo
		if(params.getEnterpriseId()!=null){
			condition=condition.and(enterprise.OWNER_ID.in(params.getEnterpriseId()));
			condition=condition.and(enterprise.OWNER_TYPE.in(VisitorsysOwnerType.ENTERPRISE.getCode()));
			List fields = new ArrayList(Arrays.asList(community.fields()));
			fields.add(enterprise.OWNER_ID.as("enterpriseId"));
			fields.add(enterprise.ENTERPRISE_NAME.as("enterpriseName"));
			getReadOnlyContext().select(fields).from(community)
					.leftOuterJoin(enterprise)
					.on(community.ID.eq(enterprise.PARENT_ID))
					.where(condition)
					.orderBy(sortField)
					.limit(params.getPageSize())
					.fetch().map(r->{
						return ConvertHelper.convert(r.intoMap(),VisitorSysVisitor.class);
					});
		}

		return getReadOnlyContext().select().from(community)
				.where(condition)
				.orderBy(sortField)
				.limit(params.getPageSize())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysVisitor.class));

	}

	@Override
	public VisitorSysVisitor findVisitorSysVisitorByParentId(Integer namespaceId, Long parentId) {
		List<VisitorSysVisitor> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_VISITORS)
				.where(Tables.EH_VISITOR_SYS_VISITORS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_VISITORS.PARENT_ID.eq(parentId))
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysVisitor.class));
		if(list==null || list.size()==0){
			return null;
		}
		return list.get(0);
	}

	@Override
	public void deleteVisitorSysVisitor(Integer namespaceId, Long visitorId) {
		getReadWriteContext().update(Tables.EH_VISITOR_SYS_VISITORS)
				.set(Tables.EH_VISITOR_SYS_VISITORS.VISIT_STATUS, VisitorsysStatus.DELETED.getCode())
				.where(Tables.EH_VISITOR_SYS_VISITORS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_VISITORS.ID.eq(visitorId)).execute();
	}

	@Override
	public void deleteVisitorSysVisitorAppoint(Integer namespaceId, Long visitorId) {
		getReadWriteContext().update(Tables.EH_VISITOR_SYS_VISITORS)
				.set(Tables.EH_VISITOR_SYS_VISITORS.BOOKING_STATUS, VisitorsysStatus.DELETED.getCode())
				.set(Tables.EH_VISITOR_SYS_VISITORS.VISIT_STATUS, VisitorsysStatus.DELETED.getCode())
				.where(Tables.EH_VISITOR_SYS_VISITORS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_VISITORS.ID.eq(visitorId)).execute();
	}

	@Override
	public List<VisitorSysVisitor> listVisitorSysVisitorByVisitorPhone(Integer namespaceId, String ownerType,
																	   Long ownerId, String visitorPhone,
																	   Timestamp startOfDay, Timestamp endOfDay) {

		List<VisitorSysVisitor> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_VISITORS)
				.where(Tables.EH_VISITOR_SYS_VISITORS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_VISITORS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_VISITORS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_VISITOR_SYS_VISITORS.VISITOR_PHONE.eq(visitorPhone))
				.and(Tables.EH_VISITOR_SYS_VISITORS.PLANNED_VISIT_TIME.gt(startOfDay))
				.and(Tables.EH_VISITOR_SYS_VISITORS.PLANNED_VISIT_TIME.lt(endOfDay))
				.and(Tables.EH_VISITOR_SYS_VISITORS.BOOKING_STATUS.eq(VisitorsysStatus.NOT_VISIT.getCode()))
				.and(Tables.EH_VISITOR_SYS_VISITORS.VISITOR_TYPE.eq(VisitorsysVisitorType.BE_INVITED.getCode()))
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysVisitor.class));
		return list;
	}

	@Override
	public List<VisitorSysVisitor> listFreqVisitor() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(cal.DATE,-30);
		Timestamp time = new Timestamp(cal.getTimeInMillis());
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_VISITORS)
				.where(Tables.EH_VISITOR_SYS_VISITORS.OWNER_TYPE.eq("community"))
				.and(Tables.EH_VISITOR_SYS_VISITORS.CREATE_TIME.ge(time))
				.groupBy(Tables.EH_VISITOR_SYS_VISITORS.VISITOR_PHONE,Tables.EH_VISITOR_SYS_VISITORS.OWNER_ID)
				.having("COUNT(1) > 1 AND max(create_time)")
				.fetch().map(r ->ConvertHelper.convert(r,VisitorSysVisitor.class));
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
