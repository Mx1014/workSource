// @formatter:off
package com.everhomes.visitorsys;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.approval.CommonStatus;
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
import com.everhomes.server.schema.tables.daos.EhVisitorSysOfficeLocationsDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysOfficeLocations;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysOfficeLocationProviderImpl implements VisitorSysOfficeLocationProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysOfficeLocation(VisitorSysOfficeLocation visitorSysOfficeLocation) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysOfficeLocations.class));
		visitorSysOfficeLocation.setId(id);
		visitorSysOfficeLocation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysOfficeLocation.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysOfficeLocation.setOperateTime(visitorSysOfficeLocation.getCreateTime());
		visitorSysOfficeLocation.setOperatorUid(visitorSysOfficeLocation.getCreatorUid());
		getReadWriteDao().insert(visitorSysOfficeLocation);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysOfficeLocations.class, null);
	}

	@Override
	public void updateVisitorSysOfficeLocation(VisitorSysOfficeLocation visitorSysOfficeLocation) {
		assert (visitorSysOfficeLocation.getId() != null);
		visitorSysOfficeLocation.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysOfficeLocation.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysOfficeLocation);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysOfficeLocations.class, visitorSysOfficeLocation.getId());
	}

	@Override
	public VisitorSysOfficeLocation findVisitorSysOfficeLocationById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysOfficeLocation.class);
	}
	
	@Override
	public List<VisitorSysOfficeLocation> listVisitorSysOfficeLocation() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS)
				.orderBy(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysOfficeLocation.class));
	}

	@Override
	public List<VisitorSysOfficeLocation> listVisitorSysOfficeLocation(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize, Long pageAnchor) {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS)
				.where(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.ID.lt(pageAnchor))
				.and(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.ID.desc())
				.limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysOfficeLocation.class));
	}

	@Override
	public void deleteVisitorSysOfficeLocation(Integer namespaceId, Long id) {
		getReadWriteContext().update(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS)
				.set(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.STATUS,CommonStatus.INACTIVE.getCode())
				.where(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_OFFICE_LOCATIONS.ID.eq(id))
				.execute();
	}

	private EhVisitorSysOfficeLocationsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysOfficeLocationsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysOfficeLocationsDao getDao(DSLContext context) {
		return new EhVisitorSysOfficeLocationsDao(context.configuration());
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
