package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.*;

import com.everhomes.naming.NameMapper;
import com.everhomes.rest.aclink.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAclinkFormTitlesDao;
import com.everhomes.server.schema.tables.daos.EhAclinkFormValuesDao;
import com.everhomes.server.schema.tables.pojos.*;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectOffsetStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAccessGroupRelDTO;
import com.everhomes.rest.aclink.DoorAccessLiteDTO;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorAccessStatus;
import com.everhomes.rest.aclink.DoorAccessType;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.QueryDoorAccessAdminCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhDoorAccessDao;
import com.everhomes.server.schema.tables.records.EhDoorAccessRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class DoorAccessProviderImpl implements DoorAccessProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private AclinkServerProvider aclinkServerProvider;

    @Override
    public Long createDoorAccess(DoorAccess obj) {
        // 主表的ID不能使用getSequence的方式 by lqs 20160430
        //long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorAccess.class));
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        long id = this.dbProvider.allocPojoRecordId(EhDoorAccess.class);
        //long id = this.shardingProvider.allocShardableContentId(EhDoorAccess.class).second();
        obj.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getId()));
        obj.setId(id);
        prepareObj(obj);
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateDoorAccess(DoorAccess obj) {
    	User user = UserContext.current().getUser();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getId()));
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        dao.update(obj);
        
        AclinkServer server = aclinkServerProvider.findServerById(obj.getLocalServerId());
        if(server != null){
        	server.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        	server.setOperatorUid(user.getId());
			aclinkServerProvider.updateLocalServer(server);
        }
    }

    @Override
    public void deleteDoorAccess(DoorAccess obj) {
    	User user = UserContext.current().getUser();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAccess.class, obj.getId()));
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        dao.deleteById(obj.getId());
        AclinkServer server = aclinkServerProvider.findServerById(obj.getLocalServerId());
        if(server != null){
        	server.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        	server.setOperatorUid(user.getId());
			aclinkServerProvider.updateLocalServer(server);
        }
    }

    @Override
    public DoorAccess getDoorAccessById(Long id) {
        DoorAccess[] result = new DoorAccess[1];

        try {
            dbProvider.mapReduce(AccessSpec.readOnlyWith(EhDoorAccess.class), null,
                    (DSLContext context, Object reducingContext) -> {
                        result[0] = context.select().from(Tables.EH_DOOR_ACCESS)
                            .where(Tables.EH_DOOR_ACCESS.ID.eq(id))
                            .fetchAny().map((r) -> {
                                return ConvertHelper.convert(r, DoorAccess.class);
                            });

                        if (result[0] != null) {
                            return false;
                        } else {
                            return true;
                        }
                    });

                return result[0];
    
        } catch(Exception ex) {
            return null;
        }
      }

    @Override
    public List<DoorAccess> queryDoorAccesss(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        final List<DoorAccess> objs = new ArrayList<DoorAccess>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhDoorAccessRecord> query = context.selectQuery(Tables.EH_DOOR_ACCESS);

            if(queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_DOOR_ACCESS.ID.ge(locator.getAnchor()));
            query.addOrderBy(Tables.EH_DOOR_ACCESS.ID.asc());
            //if count == 0 ,list all, by liuyilin 20180408 
            if(count >0){
            	query.addLimit(count + 1);
            }
            query.fetch().map((r) -> {
                objs.add(ConvertHelper.convert(r, DoorAccess.class));
                return null;
            });

            if(count>0 && objs.size() > count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
                objs.remove(objs.size() - 1);
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;

        });
        return objs;
    }

    private void prepareObj(DoorAccess obj) {
        if(obj.getUuid() == null) {
            String uuid = UUID.randomUUID().toString();
            obj.setUuid(uuid.replace("-", ""));
        }
        //TODO all use GMT time
        //Long l1 = System.currentTimeMillis();
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    @Override
    public List<DoorAccess> listDoorAccessByOwnerId(CrossShardListingLocator locator, Long ownerId, DoorAccessOwnerType ownerType, int count) {
        locator.setEntityId(ownerId);
        
        return this.queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(ownerType.getCode()));
                return query;
            }
            
        });
    }
    
    @Override
    public DoorAccess queryDoorAccessByHardwareId(String hardware) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<DoorAccess> das = queryDoorAccesss(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.HARDWARE_ID.eq(hardware));
                query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
                return query;
            }
            
        });
        
        if(das == null || das.size() == 0) {
            return null;
        }
        
        return das.get(0);
    }
    
    @Override
    public DoorAccess queryDoorAccessByUuid(String uuid) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<DoorAccess> das = queryDoorAccesss(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.UUID.eq(uuid));
                query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
                return query;
            }
            
        });
        
        if(das == null || das.size() == 0) {
            return null;
        }
        
        return das.get(0);
    }
    
    @Override
    public List<DoorAccess> listDoorAccessByGroupId(Long groupId, int count) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<DoorAccess> das = queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.GROUPID.eq(groupId));
                query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
                return query;
            }
            
        });
        
        if(das == null || das.size() == 0) {
            return null;
        }
        
        return das;
    }
    
    @Override
    public List<DoorAccess> listAllDoorAccessLingling(Long ownerId, Byte ownerType, int count) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<DoorAccess> das = queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_DOOR_ACCESS.GROUPID.ne(0l));
                query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
                return query;
            }
            
        });
        
        if(das == null || das.size() == 0) {
            return null;
        }
        
        return das;        
    }

	@Override
	public List<DoorAccess> listDoorAccessByServerId(Long id, Integer count) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<DoorAccess> das = queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.LOCAL_SERVER_ID.eq(id));
                query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
                query.addOrderBy(Tables.EH_DOOR_ACCESS.ID.desc());
                if(count > 0){
                	query.addLimit(count);
                }
                if(locator != null && locator.getAnchor() !=null){
                	query.addConditions(Tables.EH_DOOR_ACCESS.ID.lt(locator.getAnchor()));
                }
                return query;
            }
        });
        
        if(das == null || das.size() == 0) {
            return null;
        }
        
        return das;
	}
	//add by liqingyan
    @Override
	public List<ActiveDoorByNamespaceDTO> queryDoorAccessByNamespace(DoorStatisticEhCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhDoorAccess t = Tables.EH_DOOR_ACCESS.as("t");
        com.everhomes.server.schema.tables.EhCommunities t1 = Tables.EH_COMMUNITIES.as("t1");
        com.everhomes.server.schema.tables.EhOrganizations t2 = Tables.EH_ORGANIZATIONS.as("t2");
        com.everhomes.schema.tables.EhNamespaces t3 = com.everhomes.schema.Tables.EH_NAMESPACES.as("t3");
        List<ActiveDoorByNamespaceDTO> dtos = new ArrayList<ActiveDoorByNamespaceDTO>();
        SelectHavingStep<Record3<Integer,String,Byte>> groupBy1 = context.select(t.ID.count().as("num"),
                (t3.NAME).as("namespace"), (t.OWNER_TYPE).as("type"))
                .from(t)
                .leftOuterJoin(t1)
                .on(t.OWNER_ID.eq(t1.ID))
                .leftOuterJoin(t2)
                .on(t.OWNER_ID.eq(t2.ID))
                .leftOuterJoin(t3)
                .on(t3.ID.eq(t1.NAMESPACE_ID)).or(t3.ID.eq(t2.NAMESPACE_ID))
                .groupBy(t.OWNER_TYPE,t3.NAME);
        groupBy1.fetch().map((r) -> {
            ActiveDoorByNamespaceDTO dto = new ActiveDoorByNamespaceDTO();
            dto.setActiveDoorNumber(r.value1());
//            dto.setActivePublicDoorNumber(Long.parseLong(r.getValue("num").toString()));
            dto.setNamespaceName(r.value2());
            dto.setOwnerType(r.value3());
            dtos.add(dto);
            return null;
        });
        return dtos;
    }

    @Override
    public List<ActiveDoorByNamespaceDTO> queryDoorAccessByNamespaceNew(DoorStatisticEhCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhDoorAccess t = Tables.EH_DOOR_ACCESS.as("t");
        com.everhomes.schema.tables.EhNamespaces t3 = com.everhomes.schema.Tables.EH_NAMESPACES.as("t3");
        List<ActiveDoorByNamespaceDTO> dtos = new ArrayList<ActiveDoorByNamespaceDTO>();
        SelectHavingStep<Record3<Integer,String,Byte>> groupBy1 = context.select(t.ID.count().as("num"),
                (t3.NAME).as("namespace"), (t.OWNER_TYPE).as("type"))
                .from(t)
                .leftOuterJoin(t3)
                .on(t3.ID.eq(t.NAMESPACE_ID))
                .groupBy(t.OWNER_TYPE,t3.NAME);
        groupBy1.fetch().map((r) -> {
            ActiveDoorByNamespaceDTO dto = new ActiveDoorByNamespaceDTO();
            dto.setActiveDoorNumber(r.value1());
            dto.setNamespaceName(r.value2());
            dto.setOwnerType(r.value3());
            dtos.add(dto);
            return null;
        });
        return dtos;
    }






    @Override
    public List<ActiveDoorByEquipmentDTO> queryDoorAccessByEquipment(DoorStatisticEhCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhDoorAccess t = Tables.EH_DOOR_ACCESS.as("t");

        List<ActiveDoorByEquipmentDTO> dtos = new ArrayList<ActiveDoorByEquipmentDTO>();
        SelectHavingStep<Record2<Integer,String>> groupBy = context.select(t.ID.count().as("num")
                ,t.DEVICE_NAME.as("type"))
                .from(t)
                .groupBy(t.DOOR_TYPE);
        groupBy.fetch().map((r) -> {
            ActiveDoorByEquipmentDTO dto = new ActiveDoorByEquipmentDTO();
            dto.setActiveDoorNumber(r.value1());
            dto.setEquipment(r.value2());
            dtos.add(dto);
            return null;
        });
        return dtos;
    }

    @Override
    public List<ActiveDoorByFirmwareDTO> queryDoorAccessByFirmware(DoorStatisticEhCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhDoorAccess t = Tables.EH_DOOR_ACCESS.as("t");

        List<ActiveDoorByFirmwareDTO> dtos = new ArrayList<ActiveDoorByFirmwareDTO>();
        SelectHavingStep<Record2<Integer,String>> groupBy = context.select(t.ID.count().as("num")
                ,t.FIRMWARE_NAME.as("firmware"))
                .from(t)
                .groupBy(t.FIRMWARE_NAME);
        groupBy.fetch().map((r) -> {
            ActiveDoorByFirmwareDTO dto = new ActiveDoorByFirmwareDTO();
            dto.setFirmware(r.value2());
            dto.setActiveDoorNumber(r.value1());
            dtos.add(dto);
            return null;
        });

        return dtos;
    }

    @Override
    public List<ActiveDoorByPlaceDTO> queryDoorAccessByPlace(DoorStatisticEhCommand cmd){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhDoorAccess t = Tables.EH_DOOR_ACCESS.as("t");
        com.everhomes.server.schema.tables.EhCommunities t1 = Tables.EH_COMMUNITIES.as("t1");
        com.everhomes.server.schema.tables.EhOrganizationCommunityRequests t2 = Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.as("t2");
        com.everhomes.server.schema.tables.EhRegions t3 = Tables.EH_REGIONS.as("t3");
        com.everhomes.server.schema.tables.EhRegions t4 = Tables.EH_REGIONS.as("t4");
        List<ActiveDoorByPlaceDTO> dtos = new ArrayList<ActiveDoorByPlaceDTO>();
        SelectHavingStep<Record2<Integer,String>> groupBy = context.select(t.ID.count().as("num")
                ,t4.NAME.as("province"))
                .from(t)
                .leftOuterJoin(t2)
                .on(t.OWNER_ID.eq(t2.MEMBER_ID))
                .leftOuterJoin(t1)
                .on(t1.ID.eq(t.OWNER_ID)).or(t1.ID.eq(t2.COMMUNITY_ID))
                .leftOuterJoin(t3)
                .on(t3.ID.eq(t1.CITY_ID))
                .leftOuterJoin(t4)
                .on(t4.ID.eq(t3.PARENT_ID))
                .groupBy(t4.NAME);
        groupBy.fetch().map((r) -> {
            ActiveDoorByPlaceDTO dto = new ActiveDoorByPlaceDTO();
            dto.setName(r.value2());
            dto.setValue(r.value1());
            dtos.add(dto);
            return null;
        });
        return dtos;
    }
    @Override
    public List<ActiveDoorByPlaceDTO> queryDoorAccessByPlaceNew (DoorStatisticEhCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhDoorAccess t = Tables.EH_DOOR_ACCESS.as("t");
        com.everhomes.server.schema.tables.EhRegions t1 = Tables.EH_REGIONS.as("t1");
        com.everhomes.server.schema.tables.EhRegions t2 = Tables.EH_REGIONS.as("t2");
        List<ActiveDoorByPlaceDTO> dtos = new ArrayList<ActiveDoorByPlaceDTO>();
        SelectHavingStep<Record2<Integer,String>> groupBy = context.select(t.ID.count().as("num")
                ,t2.NAME.as("province"))
                .from(t)
                .leftOuterJoin(t1)
                .on(t1.ID.eq(t.CITY_ID))
//                .on(t1.ID.eq(Long.parseLong((t.CITY_ID).toString())))
                .leftOuterJoin(t2)
                .on(t2.ID.eq(t1.PARENT_ID))
                .groupBy(t2.NAME);
        groupBy.fetch().map((r) -> {
            ActiveDoorByPlaceDTO place = new ActiveDoorByPlaceDTO();
            place.setName(r.value2());
            place.setValue(r.value1());
            dtos.add(place);
            return null;
        });
        return dtos;
    }

    @Override
    public List<DoorAccessNewDTO> listDoorAccessEh(ListingLocator locator, int count,ListingQueryBuilderCallback queryBuilderCallback){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhDoorAccess t = Tables.EH_DOOR_ACCESS.as("t");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_DOOR_ACCESS);
        query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.eq((byte)1));
        if(null != locator.getAnchor()){
            query.addConditions(Tables.EH_DOOR_ACCESS.ID.ge(locator.getAnchor()));
        }
        query.addJoin(com.everhomes.schema.Tables.EH_NAMESPACES, JoinType.LEFT_OUTER_JOIN,Tables.EH_DOOR_ACCESS.NAMESPACE_ID.eq(com.everhomes.schema.Tables.EH_NAMESPACES.ID));
        query.addJoin(Tables.EH_COMMUNITIES, JoinType.LEFT_OUTER_JOIN, Tables.EH_DOOR_ACCESS.OWNER_ID.eq(Tables.EH_COMMUNITIES.ID));
        query.addJoin(Tables.EH_ORGANIZATIONS, JoinType.LEFT_OUTER_JOIN, Tables.EH_DOOR_ACCESS.OWNER_ID.eq(Tables.EH_ORGANIZATIONS.ID));
        query.addJoin(Tables.EH_USERS, JoinType.LEFT_OUTER_JOIN, Tables.EH_DOOR_ACCESS.CREATOR_USER_ID.eq(Tables.EH_USERS.ID));

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        if (count > 0){
            query.addLimit(count + 1);
        }
        List<DoorAccessNewDTO> objs = query.fetch().map((r) -> {
            DoorAccessNewDTO dto = ConvertHelper.convert(r, DoorAccessNewDTO.class);
//            dto.setNamespaceId(r.getValue(Tables.EH_DOOR_ACCESS.NAMESPACE_ID));
            dto.setId(r.getValue(t.ID));
            dto.setName(r.getValue(t.NAME));
            dto.setDisplayName(r.getValue(t.DISPLAY_NAME));
            dto.setDeviceId(r.getValue(t.DEVICE_ID));
            dto.setDeviceName(r.getValue(t.DEVICE_NAME));
            dto.setFirmwareName(r.getValue(t.FIRMWARE_NAME));
            dto.setNamespaceId(r.getValue(t.NAMESPACE_ID));
            dto.setOwnerType(r.getValue(t.OWNER_TYPE));
            dto.setOwnerId(r.getValue(t.OWNER_ID));
            dto.setDescription(r.getValue(t.DESCRIPTION));
            dto.setAddress(r.getValue(t.ADDRESS));
            dto.setHardwareId(r.getValue(t.HARDWARE_ID));
            dto.setCreateTime(r.getValue(t.CREATE_TIME));
            dto.setCreatorUserName(r.getValue(Tables.EH_USERS.NICK_NAME));
            dto.setNamespaceName(r.getValue(com.everhomes.schema.Tables.EH_NAMESPACES.NAME));
            dto.setCommunityName(r.getValue(Tables.EH_COMMUNITIES.NAME));
            dto.setOrganizationName(r.getValue(Tables.EH_ORGANIZATIONS.NAME));
            return dto;
        });
        if(count > 0 && objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return objs;
    }
    @Override
    public DoorAccess findDoorAccessById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), DoorAccess.class);
    }
    @Override
    public List<AclinkFormValuesDTO> findAclinkFormValuesByAuthId (Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_FORM_VALUES);
        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.OWNER_ID.eq(id));
        query.addOrderBy(Tables.EH_ACLINK_FORM_VALUES.ID.desc());
        List<AclinkFormValuesDTO> values = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkFormValuesDTO.class);
        });
        return values;
    }

    @Override
    public AclinkFormTitles findAclinkFormTitlesById (Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhAclinkFormTitlesDao dao = new EhAclinkFormTitlesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id),AclinkFormTitles.class);
    }

    @Override
    public Long updateAclinkFormTitles(AclinkFormTitles form){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkFormTitlesDao dao = new EhAclinkFormTitlesDao(context.configuration());
        dao.update(form);
        return null;
    }
    @Override
    public Long updateDoorAccessNew (DoorAccess obj){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        dao.update(obj);
        return null;
    }

	@Override
	public List<DoorAccessDTO> searchDoorAccessDTO(CrossShardListingLocator locator, QueryDoorAccessAdminCommand cmd) {
		final List<DoorAccessDTO> objs = new ArrayList<DoorAccessDTO>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhDoorAccessRecord> query = context.selectQuery(Tables.EH_DOOR_ACCESS);

            query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(cmd.getOwnerId()));
            query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(cmd.getOwnerType()));
            if(cmd.getName() != null) {
                query.addConditions(Tables.EH_DOOR_ACCESS.NAME.like("%" + cmd.getName() + "%"));
            }
            if(cmd.getDisplayName() != null){
            	query.addConditions(Tables.EH_DOOR_ACCESS.DISPLAY_NAME.like("%" + cmd.getDisplayName() + "%"));
            }
            if(cmd.getHardwareId() != null){
            	query.addConditions(Tables.EH_DOOR_ACCESS.HARDWARE_ID.like("%" + cmd.getHardwareId() + "%"));
            }
            
            query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
            
            if(cmd.getGroupId() == null) {
                //Select door access only
                Condition cond = Tables.EH_DOOR_ACCESS.GROUPID.ne(0l)
                .or(Tables.EH_DOOR_ACCESS.DOOR_TYPE.ne(DoorAccessType.ACLINK_LINGLING_GROUP.getCode())
                        .and(Tables.EH_DOOR_ACCESS.DOOR_TYPE.ne(DoorAccessType.ACLINK_ZL_GROUP.getCode())));
                query.addConditions(cond);
            } else if(cmd.getGroupId().equals(-1l)) {
                query.addConditions(Tables.EH_DOOR_ACCESS.GROUPID.eq(0l));
            } else if(!cmd.getGroupId().equals(0l)) {
                query.addConditions(Tables.EH_DOOR_ACCESS.GROUPID.eq(cmd.getGroupId()));
            }
            //else select all include groups
            
            if(cmd.getDoorType() != null) {
                query.addConditions(Tables.EH_DOOR_ACCESS.DOOR_TYPE.eq(cmd.getDoorType()));
            }else{
            	//不传doorType,过滤掉巴士门禁 by liuyilin 20180614
            	query.addConditions(Tables.EH_DOOR_ACCESS.DOOR_TYPE.ne(DoorAccessType.ACLINK_BUS.getCode()));
            }
            if(cmd.getServerId() != null){
            	query.addConditions(Tables.EH_DOOR_ACCESS.LOCAL_SERVER_ID.eq(cmd.getServerId()));
            }
            if(cmd.getLinkStatus() != null){
            	query.addConditions(Tables.EH_DOOR_ACCESS.LINK_STATUS.eq(cmd.getLinkStatus()));
            }
            if(locator.getAnchor() != null){
            	query.addConditions(Tables.EH_DOOR_ACCESS.ID.ge(locator.getAnchor()));
            }
                
            query.addOrderBy(Tables.EH_DOOR_ACCESS.ID.asc());
            int count = cmd.getPageSize();
            if(count >0){
            	query.addLimit(count + 1);
            }
            
            query.fetch().map((r) -> {
            	DoorAccessDTO dto =ConvertHelper.convert(r, DoorAccessDTO.class);
            	dto.setGroupId(r.getValue(Tables.EH_DOOR_ACCESS.GROUPID));
                objs.add(dto);
                return null;
            });

            if(count>0 && objs.size() > count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
                objs.remove(objs.size() - 1);
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;

        });
        objs.removeAll(Collections.singleton(null));
        return objs;
	}

	@Override
	public List<DoorAccessGroupRelDTO> listDoorGroupRel(CrossShardListingLocator locator, Integer count,
			ListDoorAccessGroupCommand cmd) {
		List<DoorAccessGroupRelDTO> listDoorGroupRel = new ArrayList<DoorAccessGroupRelDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));

		Condition con = Tables.EH_DOOR_ACCESS.STATUS.eq(DoorAccessStatus.ACTIVE.getCode());
		if(cmd.getOwnerId() != null && cmd.getOwnerType() != null){
			con = con.and(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(cmd.getOwnerId())).and(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(cmd.getOwnerType()));
		}
		if (cmd.getDoorType() != null) {
			con = con.and(Tables.EH_DOOR_ACCESS.DOOR_TYPE.eq(cmd.getDoorType()));
		} else {
			// 不传doorType,过滤掉巴士门禁 by liuyilin 20180614
			con = con.and(Tables.EH_DOOR_ACCESS.DOOR_TYPE.ne(DoorAccessType.ACLINK_BUS.getCode()));
		}
		SelectOffsetStep<Record> step = context.select().from(Tables.EH_DOOR_ACCESS)
				.leftOuterJoin(Tables.EH_ACLINK_GROUP).on(Tables.EH_DOOR_ACCESS.GROUPID.eq(Tables.EH_ACLINK_GROUP.ID).and(Tables.EH_DOOR_ACCESS.DOOR_TYPE.eq(DoorAccessType.ZLACLINK_WIFI_2.getCode())))
				.where(con)
				//TODO locator 
				.orderBy(Tables.EH_DOOR_ACCESS.CREATE_TIME.desc()).limit(count > 0? count : 999999);
		
		HashMap<Long, DoorAccessGroupRelDTO> groupRelMap = new HashMap<Long, DoorAccessGroupRelDTO>();
        step.fetch().map((r) -> {
        	DoorAccessGroupRelDTO groupDto = new DoorAccessGroupRelDTO();
        	DoorAccessLiteDTO doorDto = new DoorAccessLiteDTO();
        	doorDto.setId(r.getValue(Tables.EH_DOOR_ACCESS.ID));
        	doorDto.setDisplayName(r.getValue(Tables.EH_DOOR_ACCESS.DISPLAY_NAME));
        	doorDto.setName(r.getValue(Tables.EH_DOOR_ACCESS.NAME));
        	doorDto.setCreateTime(r.getValue(Tables.EH_DOOR_ACCESS.CREATE_TIME));
        	doorDto.setDoorType(r.getValue(Tables.EH_DOOR_ACCESS.DOOR_TYPE));
        	doorDto.setVersion(r.getValue(Tables.EH_DOOR_ACCESS.FIRMWARE_VERSION));
        	if(r.getValue(Tables.EH_DOOR_ACCESS.GROUPID) == 0L || r.getValue(Tables.EH_DOOR_ACCESS.DOOR_TYPE) != DoorAccessType.ZLACLINK_WIFI_2.getCode()){
        		if(groupRelMap.get(0L) != null){
        			groupDto = groupRelMap.get(0L);
        		}else{
        			groupDto.setGroupId(0L);
        			groupDto.setDoors(new ArrayList<DoorAccessLiteDTO>());
        			listDoorGroupRel.add(groupDto);
        			groupRelMap.put(0L, groupDto);
        		}
        	}else if(r.getValue(Tables.EH_DOOR_ACCESS.GROUPID) != 0L && r.getValue(Tables.EH_DOOR_ACCESS.DOOR_TYPE) != DoorAccessType.ZLACLINK_WIFI_2.getCode()){
        		return null;
        	}else{
        		if(groupRelMap.get(r.getValue(Tables.EH_DOOR_ACCESS.GROUPID)) != null){
        			groupDto = groupRelMap.get(r.getValue(Tables.EH_DOOR_ACCESS.GROUPID));
        		}else{
        			groupDto.setGroupId(r.getValue(Tables.EH_DOOR_ACCESS.GROUPID));
        			groupDto.setGroupName(r.getValue(Tables.EH_ACLINK_GROUP.NAME));
        			groupDto.setDoors(new ArrayList<DoorAccessLiteDTO>());
        			listDoorGroupRel.add(groupDto);
        			groupRelMap.put(r.getValue(Tables.EH_DOOR_ACCESS.GROUPID), groupDto);
        		}
        	}
        	groupDto.getDoors().add(doorDto);
            return null;
        });
		return listDoorGroupRel;
	}

    @Override
    public Long createAclinkFormTitles(AclinkFormTitles form){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkFormTitles.class));
        form.setId(id);
        EhAclinkFormTitlesDao dao = new EhAclinkFormTitlesDao(context.configuration());
        dao.insert(form);
        return id;
    }

    @Override
    public Long createAclinkFormValues(AclinkFormValues value){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkFormValues.class));
        value.setId(id);
        value.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhAclinkFormValuesDao dao = new EhAclinkFormValuesDao(context.configuration());
        dao.insert(value);
        return null;
    }

    @Override
    public List<AclinkFormTitlesDTO> searchAclinkFormTitles (ListingLocator locator,Integer count,ListingQueryBuilderCallback queryBuilderCallback){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_FORM_TITLES);
        query.addConditions(Tables.EH_ACLINK_FORM_TITLES.STATUS.ne(AclinkFormTitlesStatus.DELETE.getCode()));
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
        List<AclinkFormTitlesDTO> dtos = query.fetch().map((r) -> {
            AclinkFormTitlesDTO dto = ConvertHelper.convert(r ,AclinkFormTitlesDTO.class );
            return dto;
        });
        return dtos;
    }

}
