// @formatter:off
package com.everhomes.region;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SortField;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqHelper;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRegionsDao;
import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.server.schema.tables.records.EhRegionsRecord;
import com.everhomes.util.ConvertHelper;
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
    
    @Override
    public void createRegion(Region region) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        
        EhRegionsRecord record = ConvertHelper.convert(region, EhRegionsRecord.class);
        InsertQuery<EhRegionsRecord> query = context.insertQuery(Tables.EH_REGIONS);
        query.setRecord(record);
        query.setReturning(Tables.EH_REGIONS.ID);
        query.execute();
        
        region.setId(query.getReturnedRecord().getId());
    }

    @Override
    @Caching(evict = { @CacheEvict(value="Region", key="region.id"),
            @CacheEvict(value="listRegion"),
            @CacheEvict(value="listChildRegion"),
            @CacheEvict(value="listDescendantRegion")})
    public void updateRegion(Region region) {
        assert(region.getId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRegionsDao dao = new EhRegionsDao(context.configuration());
        dao.update(ConvertHelper.convert(region, EhRegions.class));
    }

    @Caching(evict = { @CacheEvict(value="Region", key="region.id"),
            @CacheEvict(value="listRegion"),
            @CacheEvict(value="listChildRegion"),
            @CacheEvict(value="listDescendantRegion")})
    @Override
    public void deleteRegion(Region region) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRegionsDao dao = new EhRegionsDao(context.configuration());
        
        dao.deleteById(region.getId());
    }
    
    @Caching(evict = { @CacheEvict(value="Region", key="#regionId") })
    @Override
    public void deleteRegionById(long regionId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRegionsDao dao = new EhRegionsDao(context.configuration());
        
        dao.deleteById((int)regionId);
    }

    @Cacheable(value="Region", key="#regionId")
    @Override
    public Region findRegionById(long regionId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRegionsDao dao = new EhRegionsDao(context.configuration());
        return ConvertHelper.convert(dao.findById((int)regionId), Region.class);
    }

    @Cacheable(value = "listRegion")
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public List<Region> listRegion(RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);
        List<Region> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
        Condition condition = null;
        if(scope != null)
            condition = Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode());
        
        if(status != null) {
            if(condition != null)
                condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(status.getCode()));
            else
                condition = Tables.EH_REGIONS.SCOPE_CODE.eq(status.getCode());
        }
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
            );
        }
        
        return result;
    }
    
    @Cacheable(value = "listChildRegion")
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public List<Region> listChildRegion(Long parentRegionId, RegionScope scope, 
            RegionAdminStatus status, Tuple<String, SortOrder>... orderBy) {
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);
        List<Region> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
        Condition condition = null;
        
        if(parentRegionId != null)
            condition = Tables.EH_REGIONS.PARENT_ID.eq((int)parentRegionId.longValue());
        else
            condition = Tables.EH_REGIONS.PARENT_ID.isNull();
            
        if(scope != null)
            condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));
        
        if(status != null)
            condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(status.getCode()));
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
            );
        }
        
        return result;
    }
    
    @Cacheable(value = "listDescendantRegion")
    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public List<Region> listDescendantRegion(Long parentRegionId, RegionScope scope, 
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

        SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_REGIONS, orderBy);
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_REGIONS);
        Condition condition = Tables.EH_REGIONS.PATH.like(pathLike);
        
        if(parentRegionId != null)
            condition = condition.and(Tables.EH_REGIONS.PARENT_ID.eq((int)parentRegionId.longValue()));
        else
            condition = condition.and(Tables.EH_REGIONS.PARENT_ID.isNull());
            
        if(scope != null)
            condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(scope.getCode()));
        
        if(status != null)
            condition = condition.and(Tables.EH_REGIONS.SCOPE_CODE.eq(status.getCode()));
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_REGIONS.recordType(), Region.class)
            );
        }
        
        return result;
    }
}
