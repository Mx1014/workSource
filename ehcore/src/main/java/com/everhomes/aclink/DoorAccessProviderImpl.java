package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.text.Collator;
import java.util.*;
import java.util.Comparator;

import com.everhomes.naming.NameMapper;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.admin.NamespaceDTO;
import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.rest.launchpadbase.groupinstanceconfig.Tab;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import org.jooq.*;
import org.jooq.impl.DSL;
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
                .groupBy(t.DEVICE_ID);
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
            if(null != r.getValue(t.DISPLAY_NAME) && r.getValue(t.DISPLAY_NAME).length()>0){
                dto.setDisplayName(r.getValue(t.DISPLAY_NAME));
            }else{
                dto.setDisplayName(r.getValue(t.NAME));
            }
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
    public List<NamespaceDTO> listDoorAccessNamespaces(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.schema.tables.EhNamespaces t = com.everhomes.schema.Tables.EH_NAMESPACES;
        com.everhomes.server.schema.tables.EhDoorAccess t1 = Tables.EH_DOOR_ACCESS;
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(t);
        query.addConditions(t.ID.in(context.select(t1.NAMESPACE_ID).from(t1).where(t1.STATUS.eq((byte)1))));
        List<NamespaceDTO> dtos = query.fetch().map((r) -> {
            NamespaceDTO dto = new NamespaceDTO();
            dto.setId(r.getValue(t.ID));
            dto.setName(r.getValue(t.NAME));
            return dto;
        });
//        SelectConditionStep<Record2<Integer,String>> result = context.select(t.ID,
//                t.NAME)
//                .from(t)
//                .where(t.ID.in(context.select(t1.NAMESPACE_ID).from(t1).where(t1.STATUS.eq((byte)1))));
        return dtos;
    }

    @Override
    public DoorAccess findDoorAccessById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDoorAccessDao dao = new EhDoorAccessDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), DoorAccess.class);
    }

    @Override
    public AclinkManagement findAclinkManagementById (Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkManagementDao dao = new EhAclinkManagementDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id),AclinkManagement.class);
    }

    @Override
    public List<AclinkFormValuesDTO> findAclinkFormValuesByAuthId (Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_FORM_VALUES);
        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.OWNER_ID.eq(id));
        //临时授权自定义字段
        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.TYPE.eq(AclinkFormValuesType.CUSTOM_FIELD.getCode()));
        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.STATUS.eq((byte)1));
        query.addOrderBy(Tables.EH_ACLINK_FORM_VALUES.ID.desc());
        List<AclinkFormValuesDTO> values = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkFormValuesDTO.class);
        });
        return values;
    }

    @Override
    public List<DoorsAndGroupsDTO> searchTempAuthPriority(ListTempAuthPriorityCommand cmd){
        com.everhomes.server.schema.tables.EhAclinkFormValues t = Tables.EH_ACLINK_FORM_VALUES;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(t);
        query.addConditions(t.OWNER_ID.eq(cmd.getOwnerId()));
        query.addConditions(t.OWNER_TYPE.eq(cmd.getOwnerType()));
        query.addConditions(t.TYPE.eq(AclinkFormValuesType.AUTH_PRIORITY_DOOR.getCode()).or(t.TYPE.eq(AclinkFormValuesType.AUTH_PRIORITY_GROUP.getCode())));
        query.addJoin(Tables.EH_DOOR_ACCESS,JoinType.LEFT_OUTER_JOIN,t.TYPE.eq(AclinkFormValuesType.AUTH_PRIORITY_DOOR.getCode()).and(Tables.EH_DOOR_ACCESS.ID.eq(DSL.cast(t.VALUE, Long.class))));
        query.addJoin(Tables.EH_ACLINK_GROUP,JoinType.LEFT_OUTER_JOIN,t.TYPE.eq(AclinkFormValuesType.AUTH_PRIORITY_GROUP.getCode()).and(Tables.EH_ACLINK_GROUP.ID.eq(DSL.cast(t.VALUE, Long.class))));
        query.addConditions(t.STATUS.eq((byte)1));
        query.addOrderBy(t.ID);
        List<DoorsAndGroupsDTO> values = query.fetch().map((r) -> {
            DoorsAndGroupsDTO door = new DoorsAndGroupsDTO();
            door.setFormId(r.getValue(t.ID));
//            door.setId(Long.parseLong(r.getValue(t.VALUE)));
            if(r.getValue(t.TYPE).equals(AclinkFormValuesType.AUTH_PRIORITY_DOOR.getCode())){
                door.setId(Long.parseLong(r.getValue(t.VALUE)));
                door.setType((byte)1);
                door.setName(r.getValue(Tables.EH_DOOR_ACCESS.DISPLAY_NAME));
            }else if(r.getValue(t.TYPE).equals(AclinkFormValuesType.AUTH_PRIORITY_GROUP.getCode())){
                door.setId(Long.parseLong(r.getValue(t.VALUE)));//门禁组前面加下划线
                door.setType((byte)2);
                door.setName(r.getValue(Tables.EH_ACLINK_GROUP.NAME));
            }
            door.setStatus((byte)1);
            return door;
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
    public AclinkFormValuesDTO updateAclinkFormValues(AclinkFormValues value){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkFormValuesDao dao = new EhAclinkFormValuesDao(context.configuration());
        dao.update(value);
        return ConvertHelper.convert(value,AclinkFormValuesDTO.class);
    }

    @Override
    public Long updateAclinkManagement(AclinkManagement manager){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkManagementDao dao = new EhAclinkManagementDao(context.configuration());
        dao.update(manager);
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
	public List<DoorAccessDTO> searchDoorAccessDTO(CrossShardListingLocator locator, QueryDoorAccessAdminCommand cmd, List<Long> doorIds) {
		final List<DoorAccessDTO> objs = new ArrayList<DoorAccessDTO>();
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhDoorAccess.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhDoorAccessRecord> query = context.selectQuery(Tables.EH_DOOR_ACCESS);
            //增加管理授权门禁
            query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(cmd.getOwnerId()).or(Tables.EH_DOOR_ACCESS.ID.in(doorIds)));
            query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(cmd.getOwnerType()).or(Tables.EH_DOOR_ACCESS.ID.in(doorIds)));
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
            	//园区门禁门牌号
                if(dto.getOwnerType() == (byte)1){
                    dto.getCommunityName();
                    if(null != dto.getAddressDetail() && dto.getAddressDetail().length()>0){
                        String[] str = dto.getAddressDetail().split("_");
                        dto.setBuildingName(str[0]);
                        dto.setFloorName(str[1]);
                    }
                }

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
        //门禁按displayName排序
        Comparator<DoorAccessDTO> valueComparator = (o1, o2) -> Collator.getInstance(Locale.CHINA).compare(o1.getDisplayName(), o2.getDisplayName());
        Collections.sort(objs,valueComparator);
//        Collections.sort(objs,new DoorAccessComparator());
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
    public List<AclinkGroupDTO> listAclinkGroup (CrossShardListingLocator locator, Integer count,
                                                 ListDoorGroupCommand cmd){
        List<AclinkGroupDTO> groups= new ArrayList<AclinkGroupDTO>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhAclinkGroup t = Tables.EH_ACLINK_GROUP;
        com.everhomes.server.schema.tables.EhDoorAccess t1 = Tables.EH_DOOR_ACCESS;
        com.everhomes.server.schema.tables.EhAclinkGroupDoors t2 = Tables.EH_ACLINK_GROUP_DOORS;

        Condition con = t.STATUS.eq((byte)1);
        if(cmd.getOwnerId() != null && cmd.getOwnerType() != null){
            con = con.and(t.OWNER_ID.eq(cmd.getOwnerId())).and(t.OWNER_TYPE.eq(cmd.getOwnerType()));
        }
        if(null != locator && null != locator.getAnchor()){
            con = con.and(t.ID.ge(locator.getAnchor()));
        }
        //列出门禁组及组内门禁数量 by liqingyan
        SelectOffsetStep<Record5<Integer,Long,String,Timestamp,Byte>> groupBy = context.select(t2.ID.count(),
                (t.ID),
                (t.NAME),
                (t.CREATE_TIME),
                (t.STATUS))
                .from(t)
                .leftOuterJoin(t2).on(t2.GROUP_ID.eq(t.ID)).and(t2.STATUS.eq((byte)1))
                .where(con)
                .groupBy(t.ID)
                .limit(count + 1);
//        旧方案不用
//        SelectOffsetStep groupBy = context.select(t1.ID.count(),
//                (t.ID),
//                (t.NAME),
//                (t.CREATE_TIME),
//                (t.STATUS))
//                .from(t)
//                .leftOuterJoin(t1).on(t1.GROUPID.eq(t.ID)).and(t1.DOOR_TYPE.ge(DoorAccessType.ZLACLINK_WIFI_2.getCode())).and(t1.STATUS.eq(DoorAccessStatus.ACTIVE.getCode()))
//                .where(con)
//                .groupBy(t.ID)
//                .limit(count + 1);
        groups = groupBy.fetch().map((r) ->{
            AclinkGroupDTO group = new AclinkGroupDTO();
            group.setGroupId(r.getValue(t.ID));
            group.setGroupName(r.getValue(t.NAME));
            group.setCreateTime(r.getValue(t.CREATE_TIME));
            group.setStatus(r.getValue(t.STATUS));
            group.setCount(r.getValue(t2.ID.count()));
            return group;
        });
        if(count > 0 && groups.size() > count) {
            locator.setAnchor(groups.get(groups.size() - 1).getGroupId());
            groups.remove(groups.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        //列出组内门禁
        if(null != groups && groups.size() > 0 ){
            for(AclinkGroupDTO group:groups){
                List<DoorAccessLiteDTO> doors = context.select().from(t2)
                        .leftOuterJoin(t1)
                        .on(t1.ID.eq(t2.DOOR_ID))
                        .and(t1.DOOR_TYPE.ge(DoorAccessType.ZLACLINK_WIFI_2.getCode()))
                        .and(t1.STATUS.eq(DoorAccessStatus.ACTIVE.getCode()))
                        .where(t2.GROUP_ID.eq(group.getGroupId()))
                        .and(t2.STATUS.eq((byte)1))
                        .fetch().map((r) ->{
                            DoorAccessLiteDTO door = new DoorAccessLiteDTO();
                            door.setId(r.getValue(t1.ID));
                            door.setName(r.getValue(t1.NAME));
                            door.setDisplayName(r.getValue(t1.DISPLAY_NAME));
                            return door;
                        });
                group.setDoors(doors);
//                旧方法不用
//                List<DoorAccessLiteDTO> doors = context.select().from(t1)
//                        .where(t1.GROUPID.eq(group.getGroupId()))
//                        .and(t1.DOOR_TYPE.ge(DoorAccessType.ZLACLINK_WIFI_2.getCode()))
//                        .and(t1.STATUS.eq(DoorAccessStatus.ACTIVE.getCode()))
//                        .fetch().map((r) ->{
//                            DoorAccessLiteDTO door = new DoorAccessLiteDTO();
//                            door.setId(r.getValue(t1.ID));
//                            door.setName(r.getValue(t1.NAME));
//                            door.setDisplayName(r.getValue(t1.DISPLAY_NAME));
//                            return door;
//                         });
//                group.setDoors(doors);
            }
        }
        return groups;
    }

    @Override
    public List<DoorAccessLiteDTO> listGroupDoors (Long groupId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhDoorAccess t1 = Tables.EH_DOOR_ACCESS;
        com.everhomes.server.schema.tables.EhAclinkGroupDoors t2 = Tables.EH_ACLINK_GROUP_DOORS;
        List<DoorAccessLiteDTO> doors = context.select().from(t2)
                .leftOuterJoin(t1)
                .on(t1.ID.eq(t2.DOOR_ID))
                .and(t1.DOOR_TYPE.ge(DoorAccessType.ZLACLINK_WIFI_2.getCode()))
                .and(t1.STATUS.eq(DoorAccessStatus.ACTIVE.getCode()))
                .where(t2.GROUP_ID.eq(groupId))
                .and(t2.STATUS.eq((byte)1))
                .fetch().map((r) ->{
                    DoorAccessLiteDTO door = new DoorAccessLiteDTO();
                    door.setId(r.getValue(t1.ID));
                    door.setName(r.getValue(t1.NAME));
                    door.setDisplayName(r.getValue(t1.DISPLAY_NAME));
                    return door;
                });
        return doors;
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
    public Long createDoorManagement (AclinkManagement obj){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkManagement.class));
        obj.setId(id);
        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhAclinkManagementDao dao = new EhAclinkManagementDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public List<AclinkManagementDTO> searchAclinkManagementByDoorId (Long doorId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_MANAGEMENT);
        query.addJoin(Tables.EH_ORGANIZATIONS,JoinType.LEFT_OUTER_JOIN,Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ACLINK_MANAGEMENT.MANAGER_ID));
        query.addConditions(Tables.EH_ACLINK_MANAGEMENT.DOOR_ID.eq(doorId));
        query.addConditions(Tables.EH_ACLINK_MANAGEMENT.STATUS.ne((byte)0));
        List<AclinkManagementDTO> dtos = query.fetch().map((r) -> {
            AclinkManagementDTO dto = ConvertHelper.convert(r ,AclinkManagementDTO.class );
            dto.setId(r.getValue(Tables.EH_ACLINK_MANAGEMENT.ID));
            dto.setNamespaceId(r.getValue(Tables.EH_ACLINK_MANAGEMENT.NAMESPACE_ID));
            dto.setDoorId(r.getValue(Tables.EH_ACLINK_MANAGEMENT.DOOR_ID));
            dto.setOwnerId(r.getValue(Tables.EH_ACLINK_MANAGEMENT.OWNER_ID));
            dto.setOwnerType(r.getValue(Tables.EH_ACLINK_MANAGEMENT.OWNER_TYPE));
            dto.setManagerId(r.getValue(Tables.EH_ACLINK_MANAGEMENT.MANAGER_ID));
            dto.setManagerType(r.getValue(Tables.EH_ACLINK_MANAGEMENT.MANAGER_TYPE));
            dto.setManagerName(r.getValue(Tables.EH_ORGANIZATIONS.NAME));
            dto.setCreatorUid(r.getValue(Tables.EH_ACLINK_MANAGEMENT.CREATOR_UID));
            dto.setCreateTime(r.getValue(Tables.EH_ACLINK_MANAGEMENT.CREATE_TIME));
            dto.setStatus(r.getValue(Tables.EH_ACLINK_MANAGEMENT.STATUS));
            return dto;
        });
        return dtos;
    }

    @Override
    public List<AclinkManagementDTO> searchAclinkManagementByManager(Long managerId, Byte managerType){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_MANAGEMENT);
        query.addConditions(Tables.EH_ACLINK_MANAGEMENT.MANAGER_ID.eq(managerId));
        if(null != managerType){
            query.addConditions(Tables.EH_ACLINK_MANAGEMENT.MANAGER_TYPE.eq(managerType));
        }
        query.addConditions(Tables.EH_ACLINK_MANAGEMENT.STATUS.ne((byte)0));
        List<AclinkManagementDTO> dtos = query.fetch().map((r) -> {
            AclinkManagementDTO dto = ConvertHelper.convert(r ,AclinkManagementDTO.class );
            return dto;
        });
        return dtos;
    }

    @Override
    public Long createAclinkFormValues(AclinkFormValues value){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkFormValues.class));
        value.setId(id);
        EhAclinkFormValuesDao dao = new EhAclinkFormValuesDao(context.configuration());
        dao.insert(value);
        return id;
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

    @Override
    public AclinkGroup createDoorGroup(AclinkGroup group){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkGroup.class));
        group.setId(id);
        EhAclinkGroupDao dao = new EhAclinkGroupDao(context.configuration());
        dao.insert(group);
        return group;
    }

    @Override
    public AclinkGroup findAclinkGroupById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkGroupDao dao = new EhAclinkGroupDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), AclinkGroup.class);
    }

    @Override
    public AclinkGroup updateDoorGroup(AclinkGroup group){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkGroupDao dao = new EhAclinkGroupDao(context.configuration());
        dao.update(group);
        return group;
    }

    @Override
    public void deleteAllDoorGroupRel(Long id){
        List<DoorAccess> doors = new ArrayList<DoorAccess>();
        doors = this.listNewDoorAccessByGroupId(id, 0);
        if(null == doors);
        if(null != doors && !doors.isEmpty()){
            for(DoorAccess door: doors){
                door.setGroupid(0L);
                this.updateDoorAccess(door);
            }
        }
    }

    @Override
    public List<DoorAccess> listNewDoorAccessByGroupId(Long groupId, int count){
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<DoorAccess> doors = queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.GROUPID.eq(groupId));
                query.addConditions(Tables.EH_DOOR_ACCESS.DOOR_TYPE.ge(DoorAccessType.ZLACLINK_WIFI_2.getCode()));
                query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
                return query;
            }

        });

        if(doors == null || doors.size() == 0) {
            return null;
        }

        return doors;
    }

//    @Override
//    public void createDoorGroupRel(Long groupId, Long doorId){
//        DoorAccess door = this.findDoorAccessById(doorId);
//        if(null != groupId){
//            door.setGroupid(groupId);
//        }
//        this.updateDoorAccess(door);
//    }

    @Override
    public AclinkGroupDoors createGroupDoors(AclinkGroupDoors door){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclinkGroupDoors.class));
        door.setId(id);
        EhAclinkGroupDoorsDao dao = new EhAclinkGroupDoorsDao(context.configuration());
        dao.insert(door);
        return door;
    }

    @Override
    public  AclinkGroupDoors updateGroupDoors(AclinkGroupDoors door){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkGroupDoorsDao dao = new EhAclinkGroupDoorsDao(context.configuration());
        dao.update(door);
        return door;
    }

    @Override
    public AclinkGroupDoors getGroupDoorsByDoorId(Long groupId, Long doorId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_GROUP_DOORS);
        query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.GROUP_ID.eq(groupId));
        query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.DOOR_ID.eq(doorId));
//        query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.STATUS.eq((byte)1));
        query.addLimit(1);
        AclinkGroupDoors door = query.fetchOneInto(AclinkGroupDoors.class);
        return door;
    }

    @Override
    public List<AclinkGroupDoors> getGroupDoorsByGroupId(Long groupId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_GROUP_DOORS);
        query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.GROUP_ID.eq(groupId));
        query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.STATUS.eq((byte)1));
        List<AclinkGroupDoors> doors = new ArrayList<AclinkGroupDoors>();
        doors = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkGroupDoors.class);
        });
        return doors;
    }

    @Override
    public List<AclinkGroupDoors> getGroupDoorsByOwnerId(Long ownerId,Byte ownerType){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_GROUP_DOORS);
        query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.OWNER_ID.eq(ownerId));
        if(null != ownerType){
            query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.OWNER_TYPE.eq(ownerType));
        }
        query.addConditions(Tables.EH_ACLINK_GROUP_DOORS.STATUS.eq((byte)1));
        List<AclinkGroupDoors> doors = new ArrayList<AclinkGroupDoors>();
        doors = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclinkGroupDoors.class);
        });
        return doors;
    }

    @Override
    public AclinkFormValues findAclinkFormValuesById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhAclinkFormValuesDao dao = new EhAclinkFormValuesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), AclinkFormValues.class);
    }

    @Override
    public AclinkFormValues findAclinkFormValues (Long ownerId, Byte ownerType, Byte type){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ACLINK_FORM_VALUES);
        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.TYPE.eq(type));
//        query.addConditions(Tables.EH_ACLINK_FORM_VALUES.STATUS.eq((byte)1));
        query.addLimit(1);
        AclinkFormValues value = new AclinkFormValues();
        value = query.fetchOneInto(AclinkFormValues.class);
        return value;
    }
}
