// @formatter:off
package com.everhomes.building;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.community.BuildingAdminStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBuildingsDao;
import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.util.ConvertHelper;

@Component
public class BuildingProviderImpl implements BuildingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createBuilding(Building building) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBuildings.class));
		building.setId(id);
		getReadWriteDao().insert(building);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBuildings.class, null);
	}

	@Override
	public void deleteBuilding(Building building) {
		getReadWriteDao().delete(building);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBuildings.class, null);
	}

	@Override
	public void updateBuilding(Building building) {
		assert (building.getId() != null);
		getReadWriteDao().update(building);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBuildings.class, building.getId());
	}

	@Override
	public Building findBuildingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Building.class);
	}
	
	@Override
	public List<Building> listBuilding() {
		return getReadOnlyContext().select().from(Tables.EH_BUILDINGS)
				.orderBy(Tables.EH_BUILDINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Building.class));
	}
	
	@Override
	public Building findBuildingByName(Integer namespaceId, Long communityId, String buildingName) {
		Record record = getReadOnlyContext().select().from(Tables.EH_BUILDINGS)
			.where(Tables.EH_BUILDINGS.NAME.eq(buildingName))
			.and(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId))
			.and(Tables.EH_BUILDINGS.STATUS.eq(BuildingAdminStatus.ACTIVE.getCode()))
			.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, Building.class);
		}
		return null;
	}

	@Override
	public List<Building> listBuildingByNamespaceType(Integer namespaceId, Long communityId, String namespaceType) {
		return getReadOnlyContext().select().from(Tables.EH_BUILDINGS)
		.where(Tables.EH_BUILDINGS.NAMESPACE_BUILDING_TYPE.eq(namespaceType))
		.and(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId))
		.and(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId))
		.fetch()
		.map(r->ConvertHelper.convert(r, Building.class));
	}

	/**
	 * 根据域空间Id和项目编号来查询楼栋
	 * @param namespaceId
	 * @param communityId
	 * @return
	 */
	@Override
	public List<Building> findBuildingByNamespaceIdAndCommunityId(Integer namespaceId,Long communityId){
		return getReadOnlyContext().select().from(Tables.EH_BUILDINGS)
				.where(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId))
				.fetch()
				.map(r->ConvertHelper.convert(r, Building.class));
	}

	@Override
	public List<Building> listBuildingByNamespaceType(Integer namespaceId, String namespaceType) {
		return getReadOnlyContext().select().from(Tables.EH_BUILDINGS)
				.where(Tables.EH_BUILDINGS.NAMESPACE_BUILDING_TYPE.eq(namespaceType))
				.and(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId))
				.fetch()
				.map(r->ConvertHelper.convert(r, Building.class));
	}

	private EhBuildingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhBuildingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhBuildingsDao getDao(DSLContext context) {
		return new EhBuildingsDao(context.configuration());
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

	/**
	 * 根据项目编号communityId和域空间IdnamespaceId来查询eh_buildings表中的信息
	 * @param communityId
	 * @param namespaceId
	 * @return
	 */
	@Override
	public List<Building> getBuildingByCommunityIdAndNamespaceId(Long communityId,Integer namespaceId,String buildingName){
		//获取上下文
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Building> buildingList = context.select().from(Tables.EH_BUILDINGS)
				.where(Tables.EH_BUILDINGS.COMMUNITY_ID.eq(communityId))
				.and(Tables.EH_BUILDINGS.NAME.eq(buildingName))
				.and(Tables.EH_BUILDINGS.NAMESPACE_ID.eq(namespaceId))
				.fetchInto(Building.class);
		return buildingList;
	}


}
