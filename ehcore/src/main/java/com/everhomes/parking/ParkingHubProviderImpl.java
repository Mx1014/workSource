// @formatter:off
package com.everhomes.parking;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
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
import com.everhomes.server.schema.tables.daos.EhParkingHubsDao;
import com.everhomes.server.schema.tables.pojos.EhParkingHubs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ParkingHubProviderImpl implements ParkingHubProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createParkingHub(ParkingHub parkingHub) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhParkingHubs.class));
		parkingHub.setId(id);
		parkingHub.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		parkingHub.setCreatorUid(UserContext.current().getUser().getId());
		parkingHub.setUpdateTime(parkingHub.getCreateTime());
		parkingHub.setUpdateUid(parkingHub.getCreatorUid());
		getReadWriteDao().insert(parkingHub);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhParkingHubs.class, null);
	}

	@Override
	public void updateParkingHub(ParkingHub parkingHub) {
		assert (parkingHub.getId() != null);
		parkingHub.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		parkingHub.setUpdateUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(parkingHub);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhParkingHubs.class, parkingHub.getId());
	}

	@Override
	public ParkingHub findParkingHubById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ParkingHub.class);
	}
	
	@Override
	public List<ParkingHub> listParkingHub() {
		return getReadOnlyContext().select().from(Tables.EH_PARKING_HUBS)
				.orderBy(Tables.EH_PARKING_HUBS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ParkingHub.class));
	}

	@Override
	public List<ParkingHub> listParkingHub(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, Long pageAnchor, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_PARKING_HUBS)
				.where(Tables.EH_PARKING_HUBS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_PARKING_HUBS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_PARKING_HUBS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_PARKING_HUBS.PARKING_LOT_ID.eq(parkingLotId))
				.and(Tables.EH_PARKING_HUBS.STATUS.eq((byte)2))
				.and(pageAnchor==null? DSL.trueCondition():Tables.EH_PARKING_HUBS.ID.gt(pageAnchor))
				.orderBy(Tables.EH_PARKING_HUBS.ID.asc())
				.limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, ParkingHub.class));
	}

	@Override
	public ParkingHub findParkingHubByMac(Integer namespaceId, String hubMac) {
		List<ParkingHub> list = getReadOnlyContext().select().from(Tables.EH_PARKING_HUBS)
				.where(Tables.EH_PARKING_HUBS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_PARKING_HUBS.STATUS.eq((byte) 2))
				.and(Tables.EH_PARKING_HUBS.HUB_MAC.eq(hubMac))
				.orderBy(Tables.EH_PARKING_HUBS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ParkingHub.class));
		return list==null||list.size()==0?null:list.get(0);
	}

	private EhParkingHubsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhParkingHubsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhParkingHubsDao getDao(DSLContext context) {
		return new EhParkingHubsDao(context.configuration());
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
