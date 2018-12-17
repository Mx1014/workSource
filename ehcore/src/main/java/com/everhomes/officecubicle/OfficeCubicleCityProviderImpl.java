// @formatter:off
package com.everhomes.officecubicle;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.officecubicle.DeleteRoomAdminCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOfficeCubicleRoom;
import com.everhomes.server.schema.tables.EhOfficeCubicleStation;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleCitiesDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleRoomDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleStationDao;
import com.everhomes.server.schema.tables.daos.EhParkingRechargeRatesDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleCities;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeRates;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class OfficeCubicleCityProviderImpl implements OfficeCubicleCityProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createOfficeCubicleCity(OfficeCubicleCity officeCubicleCity) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleCities.class));
		officeCubicleCity.setId(id);
		officeCubicleCity.setDefaultOrder(id);
		officeCubicleCity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubicleCity.setCreatorUid(UserContext.current().getUser().getId());
		officeCubicleCity.setOperateTime(officeCubicleCity.getCreateTime());
		officeCubicleCity.setOperatorUid(officeCubicleCity.getCreatorUid());
		getReadWriteDao().insert(officeCubicleCity);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleCities.class, null);
	}

	@Override
	public void updateOfficeCubicleCity(OfficeCubicleCity officeCubicleCity) {
		assert (officeCubicleCity.getId() != null);
		officeCubicleCity.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubicleCity.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(officeCubicleCity);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleCities.class, officeCubicleCity.getId());
	}

	@Override
	public OfficeCubicleCity findOfficeCubicleCityById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), OfficeCubicleCity.class);
	}

	@Override
	public OfficeCubicleCity findOfficeCubicleCityByProvinceAndCity(String provinceName,String cityName,Integer namespaceId) {
		List<OfficeCubicleCity> map = getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.PROVINCE_NAME.eq(provinceName))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.CITY_NAME.eq(cityName))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
		if(map!=null && map.size()>0){
			return map.get(0);
		}
		return null;
	}

	@Override
	public OfficeCubicleCity findOfficeCubicleCityByProvinceAndCity(String provinceName,String cityName,Integer namespaceId,String ownerType,Long ownerId) {
		List<OfficeCubicleCity> map = getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.PROVINCE_NAME.eq(provinceName))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.CITY_NAME.eq(cityName))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
		if(map!=null && map.size()>0){
			return map.get(0);
		}
		return null;
	}


	@Override
	public List<OfficeCubicleCity> listOfficeCubicleCity(Integer namespaceId) {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
				.and((Tables.EH_OFFICE_CUBICLE_CITIES.NAMESPACE_ID.eq(namespaceId)))
				.orderBy(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.desc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
	}

	@Override
	public List<OfficeCubicleCity> listOfficeCubicleCity(Integer namespaceId, Long nextPageAnchor, int pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.lt(nextPageAnchor))
				.orderBy(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.desc())
				.limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
	}

	@Override
	public List<OfficeCubicleCity> listOfficeCubicleCity(Integer namespaceId,Long orgId,String ownerType,Long ownerId, Long nextPageAnchor, int pageSize) {

		DSLContext context = getReadOnlyContext();
		SelectQuery query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_CITIES);
		if(null != namespaceId)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_CITIES.NAMESPACE_ID.eq(namespaceId));
		if(null != orgId)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_CITIES.ORG_ID.eq(orgId));
		if(StringUtils.isNotEmpty(ownerType))
			query.addConditions(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_TYPE.eq(ownerType));
		if(null != ownerId)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.eq(ownerId));
		else
		    query.addConditions(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.isNull());
		query.addConditions(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2));
		query.addOrderBy(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.desc());
		query.addConditions(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.lt(nextPageAnchor));
		query.addLimit(pageSize);

		return query.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
	}

	@Override
	public void deleteOfficeCubicleCity(Long cityId) {
		getReadWriteContext().update(Tables.EH_OFFICE_CUBICLE_CITIES)
				.set(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS,(byte)0)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.ID.eq(cityId))
				.execute();
	}

	private EhOfficeCubicleCitiesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhOfficeCubicleCitiesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhOfficeCubicleCitiesDao getDao(DSLContext context) {
		return new EhOfficeCubicleCitiesDao(context.configuration());
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

	@Override
	public List<OfficeCubicleCity> listOfficeCubicleProvince(Integer namespaceId,Long ownerId) {
		List<OfficeCubicleCity> res = getReadOnlyContext().selectDistinct(Tables.EH_OFFICE_CUBICLE_CITIES.PROVINCE_NAME).from(Tables.EH_OFFICE_CUBICLE_CITIES)
		.where(Tables.EH_OFFICE_CUBICLE_CITIES.NAMESPACE_ID.eq(namespaceId))
		.and(ownerId == null ? Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.isNull() : Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.eq(ownerId))
		.and(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
		.orderBy(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.desc())
		.fetch().map(r->{
			OfficeCubicleCity province = new OfficeCubicleCity();
			province.setProvinceName(r.getValue(Tables.EH_OFFICE_CUBICLE_CITIES.PROVINCE_NAME));
			return province;
		});
		return res;
	}

	@Override
	public List<OfficeCubicleCity> listOfficeCubicleCitiesByProvince(String provinceName, Integer namespaceId,Long ownerId) {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.NAMESPACE_ID.eq(namespaceId))
				.and(ownerId == null ? Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.isNull() : Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.PROVINCE_NAME.eq(provinceName))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
				.orderBy(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.desc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
	}

	@Override
	public List<OfficeCubicleCity> listOfficeCubicleCityByOwnerId(String ownerType, Long ownerId) {
		assert (null != ownerType && null != ownerId);
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
				.orderBy(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.desc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
	}

	@Override
	public List<OfficeCubicleCity> listOfficeCubicleCityByOrgId(Long orgId) {
		assert (null != orgId);
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_CITIES.ORG_ID.eq(orgId))
				.and(Tables.EH_OFFICE_CUBICLE_CITIES.STATUS.eq((byte)2))
                .and(Tables.EH_OFFICE_CUBICLE_CITIES.OWNER_ID.isNull())
				.orderBy(Tables.EH_OFFICE_CUBICLE_CITIES.DEFAULT_ORDER.desc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
	}
    
}
