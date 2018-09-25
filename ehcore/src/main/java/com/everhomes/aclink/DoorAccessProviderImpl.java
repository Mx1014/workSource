package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.everhomes.server.schema.tables.records.EhDoorAuthLogsRecord;

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
import com.everhomes.rest.aclink.DoorAuthLiteDTO;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.QueryDoorAccessAdminCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhDoorAccessDao;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.server.schema.tables.pojos.EhDoorAuth;
import com.everhomes.server.schema.tables.records.EhDoorAccessRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
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
    private ShardingProvider shardingProvider;

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
        	doorDto.setVersion(r.getValue(Tables.EH_DOOR_ACCESS.FAREWARE_VERSION));
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


}
