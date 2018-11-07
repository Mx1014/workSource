// @formatter:off
package com.everhomes.region;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METERS;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.naming.NameMapper;
import com.everhomes.rest.region.RegionCodeStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhRegionCodesDao;
import com.everhomes.server.schema.tables.pojos.EhRegionCodes;
import com.everhomes.server.schema.tables.records.EhRegionCodesRecord;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.energy.EnergyMeter;
import com.everhomes.rest.energy.EnergyMeterStatus;
import com.everhomes.rest.region.RegionActiveStatus;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.namespace.Namespace;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRegionsDao;
import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.server.schema.tables.records.EhRegionsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

/**
 * Region management implementation
 * 
 * @author Kelven Yang
 *
 */
@Component
public class RegionProviderImpl implements RegionProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegionProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Caching(evict = { @CacheEvict(value="listRegion"),
			@CacheEvict(value="listChildRegion"),
			@CacheEvict(value="listDescendantRegion") })
	@Override
	public void createRegion(Region region) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhRegionsRecord record = ConvertHelper.convert(region, EhRegionsRecord.class);
		InsertQuery<EhRegionsRecord> query = context.insertQuery(Tables.EH_REGIONS);
		query.setRecord(record);
		query.setReturning(Tables.EH_REGIONS.ID);
		query.execute();

		region.setId(query.getReturnedRecord().getId());

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRegions.class, null);
	}

	@Override
	@Caching(evict = { @CacheEvict(value="Region", key="#region.id"),
			@CacheEvict(value="listRegion"),
			@CacheEvict(value="listChildRegion"),
			@CacheEvict(value="listDescendantRegion")})
	public void updateRegion(Region region) {
		assert(region.getId() != null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionsDao dao = new EhRegionsDao(context.configuration());
		dao.update(region);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRegions.class, region.getId());
	}

	@Caching(evict = { @CacheEvict(value="Region", key="#region.id"),
			@CacheEvict(value="listRegion"),
			@CacheEvict(value="listChildRegion"),
			@CacheEvict(value="listDescendantRegion")})
	@Override
	public void deleteRegion(Region region) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionsDao dao = new EhRegionsDao(context.configuration());

		dao.deleteById(region.getId());
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRegions.class, region.getId());
	}

	@Caching(evict = { @CacheEvict(value="Region", key="#regionId"),
			@CacheEvict(value="listRegion"),
			@CacheEvict(value="listChildRegion"),
			@CacheEvict(value="listDescendantRegion") })
	@Override
	public void deleteRegionById(long regionId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionsDao dao = new EhRegionsDao(context.configuration());

		dao.deleteById(regionId);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRegions.class, regionId);
	}

	@Cacheable(value="Region", key="#regionId")
	@Override
	public Region findRegionById(long regionId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionsDao dao = new EhRegionsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(regionId), Region.class);
	}

	@Cacheable(value = "listRegion")
	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public List<Region> listRegions(Integer namespaceId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		//暂不向客户端开放排序字段指定 20150729
		//SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);
		List<Region> result;

		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = null;
		if(scope != null)
			condition = Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode());
		if(status == null){
			status = RegionAdminStatus.ACTIVE;
		}
		if(condition != null)
			condition = condition.and(Tables.EH_REGIONS.STATUS.eq(status.getCode()));
		else
			condition = Tables.EH_REGIONS.STATUS.eq(status.getCode());

		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		if(condition != null) {
			selectStep.where(condition);
		}

		//        if(orderByFields != null) {
			//            result = selectStep.orderBy(orderByFields).fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        } else {
		//            result = selectStep.fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        }
		result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);
		return result;
	}

	@Cacheable(value = "listChildRegion")
	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public List<Region> listChildRegions(Integer namespaceId, Long parentRegionId, RegionScope scope, 
			RegionAdminStatus status, Tuple<String, SortOrder>... orderBy) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		//暂不向客户端开放排序字段指定 20150729
		//SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);
		List<Region> result;

		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = null;

		if(parentRegionId != null)
			condition = Tables.EH_REGIONS.PARENT_ID.eq(parentRegionId.longValue());
		else
			condition = Tables.EH_REGIONS.PARENT_ID.isNull();

		if(scope != null)
			condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));

		if(status == null){
			status = RegionAdminStatus.ACTIVE;
		}
		if(condition != null)
			condition = condition.and(Tables.EH_REGIONS.STATUS.eq(status.getCode()));
		else
			condition = Tables.EH_REGIONS.STATUS.eq(status.getCode());

		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		if(condition != null) {
			selectStep.where(condition);
		}

		//        if(orderByFields != null) {
			//            result = selectStep.orderBy(orderByFields).fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        } else {
		//            result = selectStep.fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        }
		result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);
		return result;
	}

	@Cacheable(value = "listDescendantRegion")
	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public List<Region> listDescendantRegions(Integer namespaceId, Long parentRegionId, RegionScope scope, 
			RegionAdminStatus status, Tuple<String, SortOrder>... orderBy) {

		List<Region> result = new ArrayList<>();

		String pathLike = "%";
		if(parentRegionId != null) {
			Region parentRegion = this.findRegionById(parentRegionId);
			if(parentRegion == null) {
				LOGGER.error("Could not find parent region " + parentRegionId);
				return result;
			}

			if(parentRegion.getPath() == null || parentRegion.getPath().isEmpty()) {
				LOGGER.error("Parent region " + parentRegionId + " does not have valid path info" );
				return result;
			}

			pathLike = parentRegion.getPath() + "/%"; 
		}

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		//SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);

		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = Tables.EH_REGIONS.PATH.like(pathLike);

		if(parentRegionId != null)
			condition = condition.and(Tables.EH_REGIONS.PARENT_ID.eq(parentRegionId.longValue()));
		else
			condition = condition.and(Tables.EH_REGIONS.PARENT_ID.isNull());

		if(scope != null)
			condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));

		if(status == null){
			status = RegionAdminStatus.ACTIVE;
		}
		if(condition != null)
			condition = condition.and(Tables.EH_REGIONS.STATUS.eq(status.getCode()));
		else
			condition = Tables.EH_REGIONS.STATUS.eq(status.getCode());

		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		if(condition != null) {
			selectStep.where(condition);
		}

		//        if(orderByFields != null) {
			//            result = selectStep.orderBy(orderByFields).fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        } else {
		//            result = selectStep.fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        }
		result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);
		return result;
	}

	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public List<Region> listRegionByKeyword(Long parentRegionId, RegionScope scope, RegionAdminStatus status,
			Tuple<String, SortOrder> orderBy, String keyword, int namespaceId) {
//		int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
		List<Region> result = new ArrayList<>();
		if(StringUtils.isEmpty(keyword)){
			LOGGER.error("Keyword is null or empty" );
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid keyword parameter,keyword is null or empty.");
		}
		if(scope == null){
			LOGGER.error("Scope is null." );
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid scope parameter,scope is null.");
		}

		//如果为空，只查状态正常的
		if(status == null)
			status = RegionAdminStatus.ACTIVE;

		String likeVal = "%" + keyword + "%";

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		//SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);

		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = Tables.EH_REGIONS.NAME.like(likeVal);
		condition = condition.or(Tables.EH_REGIONS.PINYIN_NAME.like(likeVal.toUpperCase()));
		condition = condition.or(Tables.EH_REGIONS.PINYIN_PREFIX.like(likeVal.toUpperCase()));

		if(parentRegionId != null)
			condition = condition.and(Tables.EH_REGIONS.PARENT_ID.eq(parentRegionId.longValue()));

		if(scope != null)
			condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));

		if(status != null)
			condition = condition.and(Tables.EH_REGIONS.STATUS.eq(status.getCode()));

		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		if(condition != null) {
			selectStep.where(condition);
		}

		//        if(orderByFields != null) {
		//            result = selectStep.orderBy(orderByFields).fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        } else {
		//            result = selectStep.fetch().map(
		//                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
		//            );
		//        }
		result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);
		return result;
	}

	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public List<Region> listRegionByName(Long parentRegionId, RegionScope scope, RegionAdminStatus status,
			Tuple<String, SortOrder> orderBy, String keyword) {
		int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
		List<Region> result = new ArrayList<Region>();
		if(StringUtils.isEmpty(keyword)){
			LOGGER.error("Keyword is null or empty" );
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid keyword parameter,keyword is null or empty.");
		}
		if(scope == null){
			LOGGER.error("Scope is null." );
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid scope parameter,scope is null.");
		}

		//如果为空，只查状态正常的
		if(status == null)
			status = RegionAdminStatus.ACTIVE;

		//String likeVal = "%" + keyword + "%";

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		//SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);

		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = Tables.EH_REGIONS.NAME.eq(keyword);

		if(parentRegionId != null)
			condition = condition.and(Tables.EH_REGIONS.PARENT_ID.eq(parentRegionId.longValue()));

		if(scope != null)
			condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));

		if(status != null)
			condition = condition.and(Tables.EH_REGIONS.STATUS.eq(status.getCode()));

		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		if(condition != null) {
			selectStep.where(condition);
		}

		selectStep.fetch().map(r -> {
			result.add(ConvertHelper.convert(r, Region.class));
			return null;
		});
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Region> listActiveRegion(RegionScope scope) {

		int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
		List<Region> result = new ArrayList<>();

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = Tables.EH_REGIONS.STATUS.eq(RegionAdminStatus.ACTIVE.getCode());
		condition = condition.and(Tables.EH_REGIONS.HOT_FLAG.eq(RegionActiveStatus.ACTIVE.getCode()));

		if(scope != null)
			condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));

		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		if(condition != null) {

			selectStep.where(condition);
		}
		result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);

		return result;
	}
	
	@Override
	public Region findRegionByPath(String path){
		int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = Tables.EH_REGIONS.PATH.like("%" +path);
		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		selectStep.where(condition);
		Result<Record> r = selectStep.fetch();
		if(r.size() == 0){
			return new Region();
		}
		return ConvertHelper.convert(r.get(0), Region.class);
	}

	@Override
	public Region findRegionByPath(Integer namespaceId, String path) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		//fetchOne()没查到记录会返回null
		try {
			return context.select().from(Tables.EH_REGIONS).where(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId))
					.and(Tables.EH_REGIONS.PATH.eq(path)).fetchOne().map(t->ConvertHelper.convert(t, Region.class));
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public Region findRegionByName(Integer namespaceId, String name) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		//fetchOne()没查到记录会返回null
		try {
			return context.select().from(Tables.EH_REGIONS).where(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId))
					.and(Tables.EH_REGIONS.NAME.eq(name)).fetchOne().map(t->ConvertHelper.convert(t, Region.class));
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public List<Region> listRegionByParentId(Integer namespaceId,
			Long parentId, RegionScope scope, RegionAdminStatus status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Region> result;
		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
		Condition condition = null;
		
		if(parentId != null) {
			condition = Tables.EH_REGIONS.PARENT_ID.eq(parentId.longValue());
		}
		if(scope != null){
			if(condition != null) {
				condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));
			} else {
				condition = Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode());
			}
		}
		if(status == null){
			status = RegionAdminStatus.ACTIVE;
		}
		if(condition != null)
			condition = condition.and(Tables.EH_REGIONS.STATUS.eq(status.getCode()));
		else
			condition = Tables.EH_REGIONS.STATUS.eq(status.getCode());

		condition = condition.and(Tables.EH_REGIONS.NAMESPACE_ID.eq(namespaceId));
		if(condition != null) {
			selectStep.where(condition);
		}

		result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
				);
		return result;
	}

	@Override
	@Caching(evict = { @CacheEvict(value="listRegionCodes") })
	public void createRegionCode(RegionCodes regionCode) {
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRegionCodes.class));
		regionCode.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionCodesDao dao = new EhRegionCodesDao(context.configuration());
		dao.insert(regionCode);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRegionCodes.class, id);
	}

	@Override
	@Caching(evict = { @CacheEvict(value="listRegionCodes"),
			@CacheEvict(value="regionCode", key="#regionCode.id")
	})
	public void updateRegionCode(RegionCodes regionCode) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionCodesDao dao = new EhRegionCodesDao(context.configuration());
		dao.update(regionCode);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRegionCodes.class, regionCode.getId());
	}

	@Override
	@Cacheable(value = "listRegionCodes")
	public List<RegionCodes> listRegionCodes(String name, Integer code) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhRegionCodesRecord> query = context.selectQuery(Tables.EH_REGION_CODES);
		query.addConditions(Tables.EH_REGION_CODES.STATUS.eq(RegionCodeStatus.ACTIVE.getCode()));
		if(!StringUtils.isEmpty(name)){
			query.addConditions(Tables.EH_REGION_CODES.NAME.eq(name));
		}

		if(null != code){
			query.addConditions(Tables.EH_REGION_CODES.CODE.eq(code));
		}

		query.addOrderBy(Tables.EH_REGION_CODES.FIRST_LETTER.asc());

		return query.fetch().map(r -> {
			return ConvertHelper.convert(r,RegionCodes.class);
		});
	}

	@Override
	@Cacheable(value="regionCode", key="#id")
	public RegionCodes findRegionCodeById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRegionCodesDao dao = new EhRegionCodesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), RegionCodes.class);
	}
}
