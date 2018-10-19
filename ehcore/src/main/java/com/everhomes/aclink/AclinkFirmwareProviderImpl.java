package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.aclink.*;
import com.everhomes.server.schema.tables.daos.EhAclinkDeviceDao;
import com.everhomes.server.schema.tables.daos.EhAclinkFirmwareNewDao;
import com.everhomes.server.schema.tables.daos.EhAclinkFirmwarePackageDao;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAclinkFirmwareDao;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class AclinkFirmwareProviderImpl implements AclinkFirmwareProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    @Caching(evict={@CacheEvict(value="aclinkFirmwareMax", key="'fix'")})
    public Long createAclinkFirmware(AclinkFirmware obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkFirmware.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmware.class));
        obj.setId(id);
        prepareObj(obj);
        EhAclinkFirmwareDao dao = new EhAclinkFirmwareDao(context.configuration());
        dao.insert(obj);
        return id;
    }
    //add by liqingyan
    @Override
    @Caching(evict={@CacheEvict(value="aclinkFirmwareNewMax", key="'fix'")})
    public Long createFirmwareNew(AclinkFirmwareNew obj){
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkFirmwareNew.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmwareNew.class));
        obj.setId(id);
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        EhAclinkFirmwareNewDao dao = new EhAclinkFirmwareNewDao(context.configuration());
        dao.insert(obj);
        return null;
    }
//add by liqingyan
    @Override
    @Caching(evict = {@CacheEvict(value = "aclinkFirmwarePackageMax", key = "'fix'")})
    public Long createFirmwarePackage(AclinkFirmwarePackage obj){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmwarePackage.class));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkFirmware.class));
        obj.setId(id);
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        EhAclinkFirmwarePackageDao dao = new EhAclinkFirmwarePackageDao(context.configuration());
        dao.insert(obj);
    return null;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "aclinkFirmwarePackageMax", key = "'fix'")})
    public Long updateFirmwarePackage (AclinkFirmwarePackage obj){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmwarePackage.class));
        EhAclinkFirmwarePackageDao dao = new EhAclinkFirmwarePackageDao(context.configuration());
        dao.update(obj);
        return null;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "aclinkFirmwareNewMax", key = "'fix'")})
    public Long updateFirmwareNew (AclinkFirmwareNew obj)  {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmwareNew.class));
        EhAclinkFirmwareNewDao dao = new EhAclinkFirmwareNewDao(context.configuration());
        dao.update(obj);
        return null;
    }
    @Override
    @Caching(evict = {@CacheEvict(value = "AclinkDevice", key = "'fix'")})
    public Long updateAclinkDevice (AclinkDevice obj){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkDeviceDao dao = new EhAclinkDeviceDao(context.configuration());
        dao.update(obj);
        return null;
    }

    @Override
    public AclinkFirmwarePackage findPackageById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkFirmwarePackageDao dao = new EhAclinkFirmwarePackageDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), AclinkFirmwarePackage.class);
    }
    @Override
    public AclinkFirmwareNew findFirmwareById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkFirmwareNewDao dao = new EhAclinkFirmwareNewDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), AclinkFirmwareNew.class);
    }

    public AclinkDevice findDeviceById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkDeviceDao dao = new EhAclinkDeviceDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id),AclinkDevice.class);
    }

//add by liqingyan
    @Override
    public List<AclinkDeviceDTO> listFirmwareDevice(CrossShardListingLocator locator, int count, ListDoorTypeCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhAclinkDeviceRecord> query = context.selectQuery(Tables.EH_ACLINK_DEVICE);
        query.addConditions(Tables.EH_ACLINK_DEVICE.STATUS.eq((byte)1));
        if(null != cmd.getType()){
            query.addConditions(Tables.EH_ACLINK_DEVICE.TYPE.eq(cmd.getType()));
        }
        if(null != cmd.getFirmwareId()){
            query.addConditions(Tables.EH_ACLINK_DEVICE.FIRMWARE_ID.eq(cmd.getFirmwareId()));
        }
        query.addOrderBy(Tables.EH_ACLINK_DEVICE.ID);
        if(null != locator.getAnchor()) {
            query.addConditions(Tables.EH_ACLINK_DEVICE.ID.ge(locator.getAnchor()));
        }
        if (count > 0){
            query.addLimit(count + 1);
        }

        List<AclinkDeviceDTO> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkDeviceDTO.class);
            });
        return objs;

    }

    @Override
    public List<FirmwarePackageDTO> listFirmwarePackage (CrossShardListingLocator locator, int count, ListFirmwarePackageCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAclinkFirmwarePackage.class));
        SelectQuery<EhAclinkFirmwarePackageRecord> query = context.selectQuery(Tables.EH_ACLINK_FIRMWARE_PACKAGE);
        query.addConditions(Tables.EH_ACLINK_FIRMWARE_PACKAGE.STATUS.eq((byte)1));
        if(null != cmd.getType()){
            query.addConditions(Tables.EH_ACLINK_FIRMWARE_PACKAGE.TYPE.eq(cmd.getType()));
        }
        query.addOrderBy(Tables.EH_ACLINK_FIRMWARE_PACKAGE.ID);
        if(null != locator.getAnchor()) {
            query.addConditions(Tables.EH_ACLINK_FIRMWARE_PACKAGE.ID.ge(locator.getAnchor()));
        }
        if (count > 0){
            query.addLimit(count + 1);
        }

        List<FirmwarePackageDTO> objs = query.fetch().map((r) -> {
             return ConvertHelper.convert(r, FirmwarePackageDTO.class);
        });
        return objs;
    }

    @Override
    public List<FirmwareNewDTO> listFirmwareNew (CrossShardListingLocator locator, int count, ListFirmwareCommand cmd){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhAclinkFirmwareNewRecord> query = context.selectQuery(Tables.EH_ACLINK_FIRMWARE_NEW);
//        query.addJoin(Tables.EH_ACLINK_DEVICE,Tables.EH_ACLINK_FIRMWARE_NEW.ID.eq(Tables.EH_ACLINK_DEVICE.FIRMWARE_ID));
        query.addConditions(Tables.EH_ACLINK_FIRMWARE_NEW.STATUS.eq((byte)1));
        query.addOrderBy(Tables.EH_ACLINK_FIRMWARE_NEW.ID);
        if(null != locator.getAnchor()) {
            query.addConditions(Tables.EH_ACLINK_FIRMWARE_NEW.ID.ge(locator.getAnchor()));
        }
        if (count > 0){
            query.addLimit(count + 1);
        }
        List<FirmwareNewDTO> objs = query.fetch().map((r) -> {
            FirmwareNewDTO dto = ConvertHelper.convert(r, FirmwareNewDTO.class);
//            dto.setDevice(r.getValue(Tables.EH_ACLINK_DEVICE.NAME));
            return dto;
        });
        return objs;
    }


    @Override
    @Caching(evict={@CacheEvict(value="aclinkFirmwareMax", key="'fix'")})
    public void updateAclinkFirmware(AclinkFirmware obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmware.class));
        EhAclinkFirmwareDao dao = new EhAclinkFirmwareDao(context.configuration());
        dao.update(obj);
    }

    @Override
    @Caching(evict={@CacheEvict(value="aclinkFirmwareMax", key="'fix'")})
    public void deleteAclinkFirmware(AclinkFirmware obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmware.class));
        EhAclinkFirmwareDao dao = new EhAclinkFirmwareDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public AclinkFirmware getAclinkFirmwareById(Long id) {
        try {
            AclinkFirmware[] result = new AclinkFirmware[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmware.class));

            result[0] = context.select().from(Tables.EH_ACLINK_FIRMWARE)
                    .where(Tables.EH_ACLINK_FIRMWARE.ID.eq(id))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, AclinkFirmware.class);
                    });
    
            return result[0];
        } catch (Exception ex) {
            //TODO fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<AclinkFirmware> queryAclinkFirmware(ListingLocator locator
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclinkFirmware.class));

        SelectQuery<EhAclinkFirmwareRecord> query = context.selectQuery(Tables.EH_ACLINK_FIRMWARE);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACLINK_FIRMWARE.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<AclinkFirmware> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkFirmware.class);
        });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }
    
    private void prepareObj(AclinkFirmware obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    @Cacheable(value="aclinkFirmwareMax", key="'fix'")
    public AclinkFirmware queryAclinkFirmwareMax() {
        
        ListingLocator locator = new ListingLocator();
        List<AclinkFirmware> firms = this.queryAclinkFirmware(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addOrderBy(Tables.EH_ACLINK_FIRMWARE.MAJOR.desc(), Tables.EH_ACLINK_FIRMWARE.MINOR.desc(), Tables.EH_ACLINK_FIRMWARE.REVISION.desc());
                return query;
            }
            
        });
        
        if(firms == null || firms.isEmpty()) {
            return null;
        }
        
        return firms.get(0);
    }
}
