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
import com.everhomes.server.schema.tables.daos.EhVisitorSysDevicesDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysDevices;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysDeviceProviderImpl implements VisitorSysDeviceProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysDevice(VisitorSysDevice visitorSysDevice) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysDevices.class));
		visitorSysDevice.setId(id);
		visitorSysDevice.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysDevice.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysDevice.setOperateTime(visitorSysDevice.getCreateTime());
		visitorSysDevice.setOperatorUid(visitorSysDevice.getCreatorUid());
		getReadWriteDao().insert(visitorSysDevice);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysDevices.class, null);
	}

	@Override
	public void updateVisitorSysDevice(VisitorSysDevice visitorSysDevice) {
		assert (visitorSysDevice.getId() != null);
		visitorSysDevice.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysDevice.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysDevice);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysDevices.class, visitorSysDevice.getId());
	}

	@Override
	public VisitorSysDevice findVisitorSysDeviceById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysDevice.class);
	}
	
	@Override
	public List<VisitorSysDevice> listVisitorSysDevice() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_DEVICES)
				.orderBy(Tables.EH_VISITOR_SYS_DEVICES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysDevice.class));
	}
	
	private EhVisitorSysDevicesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysDevicesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysDevicesDao getDao(DSLContext context) {
		return new EhVisitorSysDevicesDao(context.configuration());
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
