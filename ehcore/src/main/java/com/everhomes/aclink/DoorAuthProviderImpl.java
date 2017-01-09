package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SelectHavingStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.aclink.AuthVisitorStasticDTO;
import com.everhomes.rest.aclink.AuthVisitorStasticResponse;
import com.everhomes.rest.aclink.AuthVisitorStatisticCommand;
import com.everhomes.rest.aclink.DoorAccessDriverType;
import com.everhomes.rest.aclink.DoorAuthStatus;
import com.everhomes.rest.aclink.DoorAuthType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhDoorAuthDao;
import com.everhomes.server.schema.tables.pojos.EhDoorAuth;
import com.everhomes.server.schema.tables.records.EhDoorAuthRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class DoorAuthProviderImpl implements DoorAuthProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoorAuthProviderImpl.class);
    //Global tables for DoorAuth
    
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDoorAuth(DoorAuth obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorAuth.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));
        obj.setId(id);
        prepareObj(obj);
        EhDoorAuthDao dao = new EhDoorAuthDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateDoorAuth(DoorAuth obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));
        EhDoorAuthDao dao = new EhDoorAuthDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteDoorAuth(DoorAuth obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));
        EhDoorAuthDao dao = new EhDoorAuthDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public DoorAuth getDoorAuthById(Long id) {
        try {
        DoorAuth[] result = new DoorAuth[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));
        
        result[0] = context.select().from(Tables.EH_DOOR_AUTH)
            .where(Tables.EH_DOOR_AUTH.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, DoorAuth.class);
            });

        return result[0];
        } catch (Exception ex) {
            //TODO fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<DoorAuth> queryDoorAuth(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));

        SelectQuery<EhDoorAuthRecord> query = context.selectQuery(Tables.EH_DOOR_AUTH);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_DOOR_AUTH.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<DoorAuth> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, DoorAuth.class);
        });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }
    
    public List<DoorAuth> queryDoorAuthByTime(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));

        SelectQuery<EhDoorAuthRecord> query = context.selectQuery(Tables.EH_DOOR_AUTH);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        query.addOrderBy(Tables.EH_DOOR_AUTH.CREATE_TIME.desc());
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_DOOR_AUTH.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
            }

        query.addLimit(count);
        List<DoorAuth> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, DoorAuth.class);
        });
        
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getCreateTime().getTime());
        } else {
            locator.setAnchor(null);
        }
        
        return objs;
    }

    private void prepareObj(DoorAuth obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
    
    //Find by userId
    public List<DoorAuth> queryDoorAuthByUserId(ListingLocator locator, long userId, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        return queryDoorAuth(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                return query;
            }
            
        });
    }
    
    public List<DoorAuth> queryDoorAuthByOwner(ListingLocator locator, long ownerId, byte ownerType, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        return queryDoorAuth(locator, count, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_DOOR_AUTH.OWNER_TYPE.eq(ownerType));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                return query;
            }
            
        });
    }
    
    @Override
    public List<DoorAuth> queryValidDoorAuthByUserId(ListingLocator locator, long userId, String driver, int count) {
        
        long now = DateHelper.currentGMTTime().getTime();
        
        return queryDoorAuth(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                Condition c1 = Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.TEMPERATE.getCode()). 
                        and(Tables.EH_DOOR_AUTH.VALID_FROM_MS.le(now).
                        and(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now)));
                Condition c2 = Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode());
                query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                
                if(driver != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.DRIVER.eq(driver));    
                }
                
                query.addConditions(c1.or(c2));
                return query;
            }
            
        });        
    }
    
    @Override
    public DoorAuth queryValidDoorAuthForever(Long doorId, Long userId) {
        return queryValidDoorAuthForever(doorId, userId, null, null, null);
    }
    
    @Override
    public DoorAuth queryValidDoorAuthForever(Long doorId, Long userId, Byte rightOpen, Byte rightVisitor, Byte rightRemote) {
        ListingLocator locator = new ListingLocator();
        
        List<DoorAuth> auths = queryDoorAuth(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode()));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                if(rightOpen != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq(rightOpen.byteValue()));
                }
                if(rightVisitor != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.RIGHT_VISITOR.eq(rightVisitor.byteValue()));
                }
                if(rightRemote != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq(rightRemote.byteValue()));
                }
                
                return query;
            }
        }); 
        
        if(auths == null || auths.size() == 0) {
            return null;
        }
        return auths.get(0);        
    }
    
    @Override 
    public DoorAuth queryValidDoorAuthByDoorIdAndUserId(Long doorId, Long userId) {
        ListingLocator locator = new ListingLocator();
        long now = DateHelper.currentGMTTime().getTime();
        
        List<DoorAuth> auths = queryDoorAuth(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                Condition c1 = Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.TEMPERATE.getCode()). 
                        and(Tables.EH_DOOR_AUTH.VALID_FROM_MS.le(now).
                        and(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now)));
                Condition c2 = Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode());
                query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                query.addConditions(c1.or(c2));
                return query;
            }
        }); 
        
        if(auths == null || auths.size() == 0) {
            return null;
        }
        return auths.get(0);
    }
    
    @Override
    public List<DoorAuth> queryDoorAuthByApproveId(ListingLocator locator, Long approveId, int count) {
        
        return queryDoorAuthByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.APPROVE_USER_ID.eq(approveId));
                query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.TEMPERATE.getCode()));
                return query;
            }
            
        });        
    }
    
    @Override
    public List<DoorAuth> searchDoorAuthByAdmin(ListingLocator locator, Long doorId, String keyword, Byte status, int count) {
        
        return queryDoorAuthByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(status != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(status));    
                }
                
                if(doorId != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
                }
                
                if(keyword != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.NICKNAME.like(keyword+"%").or(Tables.EH_DOOR_AUTH.PHONE.like(keyword+"%")));                    
                }

                return query;
            }
            
        });        
    }
    
    @Override
    public List<DoorAuth> searchVisitorDoorAuthByAdmin(ListingLocator locator, Long doorId, String keyword, Byte status, int count) {
        
        return queryDoorAuthByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(status != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(status));    
                }
                
                if(doorId != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
                }
                
                if(keyword != null) {
                    query.addConditions(Tables.EH_DOOR_AUTH.NICKNAME.like(keyword+"%").or(Tables.EH_DOOR_AUTH.PHONE.like(keyword+"%")));                    
                }
                
                query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.ne(DoorAuthType.FOREVER.getCode()));

                return query;
            }
            
        });        
    }
    
    @Override
    public List<DoorAuth> queryDoorAuthForeverByUserId(ListingLocator locator, Long userId, int count) {
        return queryDoorAuthByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode()));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                return query;
            }
            
        });    
    }
    
    @Override
    public DoorAuth getLinglingDoorAuthByUuid(String uuid) {
        
        ListingLocator locator = new ListingLocator();
        
        List<DoorAuth> auths = queryDoorAuth(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.ne(DoorAuthStatus.INVALID.getCode()));
//                query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.LINGLING_VISITOR.getCode()));
                query.addConditions(AclinkAuthCustomField.AUTH_LINGLING_UUID.getField().eq(uuid));

                return query;
            }
            
        });
        
        if(auths == null || auths.size() == 0) {
            return null;
        }
        
        return auths.get(0);
    }
    
    @Override
    public AuthVisitorStasticResponse authVistorStatistic(AuthVisitorStatisticCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_DOOR_AUTH.OWNER_ID.eq(cmd.getOwnerId());
        condition = condition.and(Tables.EH_DOOR_AUTH.OWNER_TYPE.eq(cmd.getOwnerType()));
        condition = condition.and(Tables.EH_DOOR_AUTH.CREATE_TIME.between(new Timestamp(cmd.getStart()), new Timestamp(cmd.getEnd())));
        condition = condition.and(Tables.EH_DOOR_AUTH.AUTH_TYPE.ne(DoorAuthType.FOREVER.getCode()));
        AuthVisitorStasticResponse resp = new AuthVisitorStasticResponse();
        resp.setDtos(new ArrayList<AuthVisitorStasticDTO>());
        
        SelectHavingStep<Record2<Integer, Date>> groupBy = context.select(Tables.EH_DOOR_AUTH.ID.count().as("c"),
                DSL.date(Tables.EH_DOOR_AUTH.CREATE_TIME).as("d"))
                .from(Tables.EH_DOOR_AUTH)
                .where(condition).groupBy(DSL.date(Tables.EH_DOOR_AUTH.CREATE_TIME).as("d"));
        
//        LOGGER.info("statistics: " + groupBy);
        groupBy.fetch().map((r) -> {
                    AuthVisitorStasticDTO dto = new AuthVisitorStasticDTO();
                    dto.setCount(Long.parseLong(r.getValue("c").toString()));
                    dto.setDate(r.getValue("d").toString());
                    resp.getDtos().add(dto);
                    return null;
                });
        
        Result<Record1<Integer>> rlt = context.select(Tables.EH_DOOR_AUTH.ID.count().as("c")).from(Tables.EH_DOOR_AUTH)
                .where(condition).fetch();
        
        resp.setTotal(new Long((Integer)rlt.get(0).getValue("c")));
        
        rlt = context.select(Tables.EH_DOOR_AUTH.ID.count().as("c")).from(Tables.EH_DOOR_AUTH)
                .where(condition.and(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.INVALID.getCode()))).fetch();
        
        resp.setInvalidCount(new Long((Integer)rlt.get(0).getValue("c")));
        
        resp.setValidCount(resp.getTotal() - resp.getInvalidCount());
        return resp;
    }
    
    @Override
    public List<DoorAuth> queryValidDoorAuths(ListingLocator locator, Long userId, Long ownerId, Byte ownerType, int count) {
        
        long now = DateHelper.currentGMTTime().getTime();
        
        return queryDoorAuth(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
            	if(ownerId != null) {
            		query.addConditions(Tables.EH_DOOR_AUTH.OWNER_ID.eq(ownerId));
            		query.addConditions(Tables.EH_DOOR_AUTH.OWNER_TYPE.eq(ownerType));
            	}
                query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                
                return query;
            }
            
        });        
    }
    
    @Override
    public void updateDoorAuth(List<DoorAuth> objs) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));
        EhDoorAuthDao dao = new EhDoorAuthDao(context.configuration());
        dao.update(objs.toArray(new DoorAuth[objs.size()]));
    }
    
}
