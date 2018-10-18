// @formatter:off
package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupCustomField;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.UserOrganizationStatus;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhDoorAuthDao;
import com.everhomes.server.schema.tables.daos.EhDoorAuthLogsDao;
import com.everhomes.server.schema.tables.pojos.EhDoorAuth;
import com.everhomes.server.schema.tables.pojos.EhDoorAuthLogs;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhDoorAuthLogsRecord;
import com.everhomes.server.schema.tables.records.EhDoorAuthRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.User;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    
    @Autowired
    private GroupProvider groupProvider;

    @Override
    public Long getNextDoorAuth() {
    	long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorAuth.class));
    	return id;
    }

    @Override
    public Long createDoorAuth(DoorAuth obj) {
    	if(obj.getId() == null) {
    		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorAuth.class));
    		obj.setId(id);
    	}

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));
        prepareObj(obj);
        EhDoorAuthDao dao = new EhDoorAuthDao(context.configuration());
        dao.insert(obj);
        return obj.getId();
    }

    @Override
    public Long createDoorAuthLog(DoorAuthLog log) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDoorAuthLogs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuthLogs.class));
        log.setId(id);
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhDoorAuthLogsDao dao = new EhDoorAuthLogsDao(context.configuration());
        dao.insert(log);
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
            query.addConditions(Tables.EH_DOOR_AUTH.ID.ge(locator.getAnchor()));
            }

        if(count > 0) query.addLimit(count + 1);
        List<DoorAuth> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, DoorAuth.class);
        });

        if(count > 0 && objs.size() > count) {
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

        query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.in(context.select(Tables.EH_DOOR_ACCESS.ID).from(Tables.EH_DOOR_ACCESS).where(Tables.EH_DOOR_ACCESS.STATUS.eq(DoorAccessStatus.ACTIVE.getCode()))));
        query.addOrderBy(Tables.EH_DOOR_AUTH.CREATE_TIME.desc(),Tables.EH_DOOR_AUTH.ID.desc());
        if(locator.getAnchor() != null && locator.getAnchor() != 0) {
            query.addConditions(Tables.EH_DOOR_AUTH.CREATE_TIME.le(new Timestamp(locator.getAnchor())));
            }
        // count<=0默认查全部
        if(count > 0){
        	query.addLimit(count + 1);
        }
        List<DoorAuth> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, DoorAuth.class);
        });

        if(count > 0 && objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getCreateTime().getTime());
            objs.remove(objs.size() - 1);
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
                }else{
                	//不传则过滤掉bus门禁 by liuyilin 20180614
                	query.addConditions(Tables.EH_DOOR_AUTH.DRIVER.ne(DoorAccessDriverType.BUS.getCode()));
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
    public DoorAuth queryValidDoorAuthByDoorIdAndUserId(Long doorId, Long userId, Byte isRemote) {
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
                if(isRemote != null) {
                	query.addConditions(Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq(isRemote));
                }
                query.addConditions(c1.or(c2));
                return query;
            }
        });

        if(auths == null || auths.size() == 0) {
            return null;
        }
        return auths.get(0);
    }

    /**
     * 返回所有非 Forever 的授权记录
     */
    @Override
    public List<DoorAuth> queryDoorAuthByApproveId(ListingLocator locator, Long approveId, int count) {

        return queryDoorAuthByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.APPROVE_USER_ID.eq(approveId));
                query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.ne(DoorAuthType.FOREVER.getCode()));
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
    public List<DoorAuth> searchVisitorDoorAuthByAdmin(Long doorId, String keyword, Byte status, int pageSize, Long startTime, Long endTime) {
        return queryDoorAuthByTime(new ListingLocator(), pageSize, (locator, query) -> {
            if (status != null) {
                Long now = DateHelper.currentGMTTime().getTime();
                if(status.equals(DoorAuthStatus.INVALID.getCode())) {
                    query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.lt(now).or(Tables.EH_DOOR_AUTH.STATUS.eq(status)));
                } else {
                    query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now).and(Tables.EH_DOOR_AUTH.STATUS.eq(status)));
                }
            }
            if (doorId != null) {
                query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
            }
            if (startTime != null) {
                query.addConditions(Tables.EH_DOOR_AUTH.CREATE_TIME.ge(new Timestamp(startTime)));
            }
            if (endTime != null) {
                query.addConditions(Tables.EH_DOOR_AUTH.CREATE_TIME.le(new Timestamp(endTime)));
            }
            if (keyword != null) {
                query.addConditions(Tables.EH_DOOR_AUTH.NICKNAME.like(keyword+"%").or(Tables.EH_DOOR_AUTH.PHONE.like(keyword+"%")));
            }
            query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.ne(DoorAuthType.FOREVER.getCode()));
            return query;
        });
    }

    @Override
    public List<DoorAuth> searchVisitorDoorAuthByAdmin(ListingLocator locator, Long doorId, String keyword, Byte status, int count) {

        return queryDoorAuthByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {

                if(status != null) {
                	Long now = DateHelper.currentGMTTime().getTime();
                    if(status.equals(DoorAuthStatus.INVALID.getCode())) {
//                       query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.lt(now).or(Tables.EH_DOOR_AUTH.STATUS.eq(status)));
						query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(status).or(Tables.EH_DOOR_AUTH.VALID_END_MS.lt(now)
								.or(Tables.EH_DOOR_AUTH.VALID_AUTH_AMOUNT.le(0).and(Tables.EH_DOOR_AUTH.AUTH_RULE_TYPE.eq((byte) 1)))));
                    } else {
						query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now).and(Tables.EH_DOOR_AUTH.STATUS.eq(status)).and((Tables.EH_DOOR_AUTH.AUTH_RULE_TYPE.eq((byte) 0))
								.or(Tables.EH_DOOR_AUTH.AUTH_RULE_TYPE.isNull())
										.or(Tables.EH_DOOR_AUTH.VALID_AUTH_AMOUNT.gt(0).and(Tables.EH_DOOR_AUTH.AUTH_RULE_TYPE.eq((byte) 1)))));
                    }
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
    public List<DoorAuth> queryDoorAuthForeverByUserId(ListingLocator locator, Long userId, Byte rightRemote, String driver, int count) {
        return queryDoorAuthByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                query.addConditions(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode()));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                query.addConditions(Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte) 1));
                if(rightRemote != null && rightRemote == (byte) 1){
                	query.addConditions(Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq((byte) 1));
                }

	            if(driver != null && !driver.isEmpty()){
	            	query.addConditions(Tables.EH_DOOR_AUTH.DRIVER.eq(driver));
	            }else{
                	//不传则过滤掉bus门禁 by liuyilin 20180614
                	query.addConditions(Tables.EH_DOOR_AUTH.DRIVER.ne(DoorAccessDriverType.BUS.getCode()));
	            }
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

        //by liuyilin 20180328 目前授权是对门禁组授权，如果以后可以给组内单个门禁授权，则没有被统计
        if(cmd.getDoorAccessGroupId() != null){
        	condition = condition.and(Tables.EH_DOOR_AUTH.DOOR_ID.eq(cmd.getDoorAccessGroupId()));
        }

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
                .where(condition.and(
                        Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.INVALID.getCode())
                        .or(Tables.EH_DOOR_AUTH.VALID_END_MS.lt(DateHelper.currentGMTTime().getTime()))
                        )).fetch();

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

    @Override
    public List<User> listDoorAuthByOrganizationId(Long organizationId, Byte isOpenAuth, Long doorId, CrossShardListingLocator locator, int pageSize){
        List<User> users = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDoorAuth.class));

        Condition cond = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId);
        cond = getIsOpenAuthCond(cond, isOpenAuth);
        if(locator.getAnchor() != null ) {
            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.lt(locator.getAnchor()));
        }
        cond = cond.and(Tables.EH_USERS.ID.isNotNull());

        SelectOffsetStep<Record> step = context.select().from(
                context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                        .where(
                                Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId))
                        .and(
                                Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode())
                        )
                        .and(
                                Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode())
                        ).asTable(Tables.EH_ORGANIZATION_MEMBERS.getName())
            ).leftOuterJoin(
                context.select().from(Tables.EH_DOOR_AUTH)
                        .where(
                                Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode())
                        )
                        .and(
                                Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode()
                        )
                        .and(
                                Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId)
                        )
                ).asTable(Tables.EH_DOOR_AUTH.getName())
            )
                .on(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(
                        Tables.EH_DOOR_AUTH.USER_ID)
                )
                .leftOuterJoin(Tables.EH_USERS)
                .on(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(
                        Tables.EH_USERS.ID))
                .where(cond).orderBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.desc())
                .limit(pageSize + 1);

//        System.out.println("Query sql ====== " + step.getSQL());

        step.fetch().map(r ->{
            User user = new User();
            user.setId(r.getValue(Tables.EH_USERS.ID));
            user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
            user.setIdentifierToken(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN));
            user.setGender(r.getValue(Tables.EH_USERS.GENDER));
            user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
            users.add(user);
            return null;
        });

        locator.setAnchor(null);
        if(users.size() > pageSize){
            users.remove(users.size() - 1);
            locator.setAnchor(users.get(users.size() - 1).getId());
        }
        return users;
    }

    @Override
    public List<User> listDoorAuthByIsAuth(Byte isAuth, Byte isOpenAuth, Long doorId, CrossShardListingLocator locator, int pageSize, Integer namespaceId){
        List<User> users = new ArrayList<>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj)->{
            Condition cond = Tables.EH_USERS.ID.isNotNull();
            cond = getIsAuthCond(cond, isAuth);
            cond = getIsOpenAuthCond(cond, isOpenAuth);
            if(locator.getAnchor() != null ) {
                cond = cond.and(Tables.EH_USERS.ID.lt(locator.getAnchor()));
            }

            SelectOffsetStep<Record> step = context.select().from(
                    context.select().from(Tables.EH_USERS)
                            .where(
                                    Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId)
                            ).asTable(Tables.EH_USERS.getName()))
                    .leftOuterJoin(
                            context.select().from(Tables.EH_DOOR_AUTH)
                                    .where(
                                            Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()))
                                    .and(
                                            Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode())
                                    .and(
                                            Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId)))
                                    .asTable(Tables.EH_DOOR_AUTH.getName()))
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_DOOR_AUTH.USER_ID))
                    .leftOuterJoin(
                            context.select(
                                    Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID,
                                    Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE,
                                    Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN,
                                    Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME,
                                    Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID)
                                    .from(Tables.EH_ORGANIZATION_MEMBERS)
                                    .leftOuterJoin(Tables.EH_ORGANIZATIONS)
                                    .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID))
                                    .where(
                                            Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                                    .and(
                                            Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
                                    .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()))
                                    .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0L))
                                    .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()))
                                    .groupBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID)
                                    .asTable(Tables.EH_ORGANIZATION_MEMBERS.getName()))
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID))
                    .where(cond).orderBy(Tables.EH_USERS.ID.desc())
                    .limit(pageSize + 1);

//            System.out.println("query sql:" + step.getSQL());

            step.fetch().map(r ->{
                User user = new User();
                user.setId(r.getValue(Tables.EH_USERS.ID));
                user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
                user.setIdentifierToken(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN));
                user.setGender(r.getValue(Tables.EH_USERS.GENDER));
                user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
                users.add(user);
                return null;
            });
            return true;
        });

        locator.setAnchor(null);
        if(users.size() > pageSize){
            users.remove(users.size() - 1);
            locator.setAnchor(users.get(users.size() - 1).getId());
        }
        return users;
    }
    
    /**
     * 办公楼的门禁授权
     * @param isOpenAuth
     * @param doorId
     * @param buildingName
     * @param locator
     * @param pageSize
     * @param namespaceId
     * @return
     */
    @Override
    public List<Long> listDoorAuthByBuildingName2(Byte isOpenAuth, Long doorId, Long communityId, String buildingName, CrossShardListingLocator locator, int pageSize, Integer namespaceId){
        List<Long> users = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj)->{
            Condition condOrg = Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.BUILDING_NAME.eq(buildingName)
                    .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                    .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
                    .and(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId));
                    
            Condition cond = Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId)
                    .and(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()))
                    ;
            
            if(isOpenAuth != null) {
                Condition openCond = Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte)1);
                openCond = openCond.or(Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq((byte)1));
                openCond = openCond.or(Tables.EH_DOOR_AUTH.RIGHT_VISITOR.eq((byte)1));
                cond = cond.and(openCond);
            }
            
            Long anchor = 0l;
            if(null != locator.getAnchor()) {
                anchor = locator.getAnchor();
            }
            
            SelectOffsetStep<Record1<Long>> userIdStep = null;
            SelectOnConditionStep<Record1<Long>> firstStep = context.select(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID).from(Tables.EH_ORGANIZATION_MEMBERS)
            .join(Tables.EH_DOOR_AUTH)
            .on(Tables.EH_DOOR_AUTH.USER_ID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID))
            .join(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
            .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID))
            ;
            
            if(isOpenAuth == null) {
                userIdStep = context.select(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID).from(Tables.EH_ORGANIZATION_MEMBERS)
                        .join(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                        .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID))
                        .where(condOrg.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.gt(anchor))).groupBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID).limit(pageSize);
            } else if(isOpenAuth == 0) {
                userIdStep = context.select(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID).from(Tables.EH_ORGANIZATION_MEMBERS)
                .join(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID))
                .where(condOrg
                        .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.notIn(firstStep.where(cond.and(condOrg)).groupBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID)))
                        .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.gt(anchor))
                        )
                .groupBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID).limit(pageSize);
            } else {
                userIdStep = firstStep.where(cond.and(condOrg).and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.gt(anchor))).groupBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID).limit(pageSize);
            }
            
            userIdStep.fetch().map(r ->{
                if(users.size() >= pageSize) {
                    return null;
                }
                users.add(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID));        
                return null;
                
            });
            return true;

        });

        locator.setAnchor(null);
        if(users.size() >= pageSize){
            locator.setAnchor(users.get(users.size() - 1));
        }
        
        return users;
    }


    @Override
    public Long countDoorAuthUser(Byte isAuth, Byte isOpenAuth, Long doorId, Integer namespaceId, Byte rightType){

        List<Long> counts = new ArrayList<>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj)->{
            Condition cond = Tables.EH_USERS.ID.isNotNull();
            cond = getIsAuthCond(cond, isAuth);

            if(null != isOpenAuth){
                Condition authCond = null;
                if(DoorAuthRightType.fromCode(rightType) == DoorAuthRightType.RIGHT_OPEN){
                    authCond = Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq(isOpenAuth);
                }else if(DoorAuthRightType.fromCode(rightType) == DoorAuthRightType.RIGHT_VISITOR){
                    authCond = Tables.EH_DOOR_AUTH.RIGHT_VISITOR.eq(isOpenAuth);
                }else if(DoorAuthRightType.fromCode(rightType) == DoorAuthRightType.RIGHT_REMOTE){
                    authCond = Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq(isOpenAuth);
                }

                if(isOpenAuth < 1){
                    authCond = authCond.or(Tables.EH_DOOR_AUTH.DOOR_ID.isNull());
                }
                cond = cond.and(authCond);
            }

            SelectConditionStep<Record1<Integer>> step = context.selectCount().from(
                    context.select().from(Tables.EH_USERS)
                            .where(
                                    Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId)
                            ).asTable(Tables.EH_USERS.getName()))
                    .leftOuterJoin(
                            context.select().from(Tables.EH_DOOR_AUTH)
                                    .where(
                                            Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()))
                                    .and(
                                            Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode())
                                                    .and(
                                                            Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId)))
                                    .asTable(Tables.EH_DOOR_AUTH.getName()))
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_DOOR_AUTH.USER_ID))
                    .leftOuterJoin(context.select(
                            Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID,
                            Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE,
                            Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN,
                            Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME,
                            Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID)
                            .from(Tables.EH_ORGANIZATION_MEMBERS)
                            .leftOuterJoin(Tables.EH_ORGANIZATIONS)
                            .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID))
                            .where(
                                    Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                            .and(
                                    Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
                            .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()))
                            .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0L))
                            .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()))
                            .groupBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID)
                            .asTable(Tables.EH_ORGANIZATION_MEMBERS.getName()))
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID))
                    .where(cond);

            System.out.println("query sql:" + step.getSQL());
            counts.add(Long.valueOf(step.fetchOne().getValue(0).toString()));
            return true;
        });

        return counts.get(0);
    }

    @Override
    public List<DoorAuthLog> listDoorAuthLogsByUserId(CrossShardListingLocator locator, int pageSize, Long userId, Long doorId){
        List<DoorAuthLog> logs = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhDoorAuthLogsRecord> query = context.selectQuery(Tables.EH_DOOR_AUTH_LOGS);
        query.addConditions(Tables.EH_DOOR_AUTH_LOGS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_DOOR_AUTH_LOGS.DOOR_ID.eq(doorId));
        if(locator.getAnchor() != null ) {
            query.addConditions(Tables.EH_DOOR_AUTH_LOGS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
        }
        query.addOrderBy(Tables.EH_DOOR_AUTH_LOGS.CREATE_TIME.desc());
        query.addLimit(pageSize + 1);
        query.fetch().map(r -> {
            logs.add(ConvertHelper.convert(r, DoorAuthLog.class));
            return null;
        });

        locator.setAnchor(null);
        if(logs.size() > pageSize){
            logs.remove(logs.size() - 1);
            locator.setAnchor(logs.get(logs.size() - 1).getCreateTime().getTime());
        }

        return logs;
    }

    private Condition getIsOpenAuthCond(Condition cond, Byte isOpenAuth){
        if(null == isOpenAuth){

        }else if(isOpenAuth > 0){
            Condition openCond = Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte)1);
            openCond = openCond.or(Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq((byte)1));
            openCond = openCond.or(Tables.EH_DOOR_AUTH.RIGHT_VISITOR.eq((byte)1));
            cond = cond.and(openCond);
        }else{
            Condition openCond = Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte)0);
            openCond = openCond.and(Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq((byte)0));
            openCond = openCond.and(Tables.EH_DOOR_AUTH.RIGHT_VISITOR.eq((byte)0));
            openCond = openCond.or(Tables.EH_DOOR_AUTH.DOOR_ID.isNull());
            cond = cond.and(openCond);
        }

        return cond;
    }

    private Condition getIsAuthCond(Condition cond, Byte isAuth){
        if(null == isAuth){
        }else if(isAuth > 0){
            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.isNotNull());
        }else{
            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.isNull());
        }
        return cond;
    }

    private Condition getIsAuthCond(Byte isAuth){
        if(null == isAuth){
        	return null;
        }else if(isAuth > 0){
            return Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.isNotNull();
        }else{
            return Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.isNull();
        }
    }

    @Override
    public List<Long> listDoorAuthByBuildingName(Byte isOpenAuth, Long doorId, Long communityId, String buildingName, CrossShardListingLocator locator, int pageSize, Integer namespaceId) {
        List<Long> users = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj)->{
            Condition condMember = GroupCustomField.FAMILY_COMMUNITY_ID.getField().eq(communityId)
                    .and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
                    .and(Tables.EH_GROUPS.NAME.like(buildingName+"%"))
                    .and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
                    .and(Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode()))
                    .and(Tables.EH_GROUPS.NAMESPACE_ID.eq(namespaceId));
            
            Condition cond = Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId)
                    .and(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()))
                    ;

            if(isOpenAuth != null) {
                Condition openCond = Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte)1);
                openCond = openCond.or(Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq((byte)1));
                openCond = openCond.or(Tables.EH_DOOR_AUTH.RIGHT_VISITOR.eq((byte)1));
                cond = cond.and(openCond);
            }

            Long anchor = 0l;
            if(null != locator.getAnchor()) {
                anchor = locator.getAnchor();
            }
            SelectOffsetStep<Record1<Long>> userIdStep = null;
            SelectOnConditionStep<Record1<Long>> firstStep = context.select(Tables.EH_GROUP_MEMBERS.MEMBER_ID).from(Tables.EH_GROUP_MEMBERS).join(Tables.EH_GROUPS)
            .on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID)).join(Tables.EH_DOOR_AUTH)
            .on(Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(Tables.EH_DOOR_AUTH.USER_ID));
            
            if(isOpenAuth == null) {
                userIdStep = context.select(Tables.EH_GROUP_MEMBERS.MEMBER_ID).from(Tables.EH_GROUP_MEMBERS).join(Tables.EH_GROUPS)
                .on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
                .where(condMember).and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.gt(anchor))
                .groupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID).limit(pageSize);
            } else if(isOpenAuth == 0) {
                userIdStep = context.select(Tables.EH_GROUP_MEMBERS.MEMBER_ID).from(Tables.EH_GROUP_MEMBERS).join(Tables.EH_GROUPS)
                .on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
                .where(condMember.and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.notIn(firstStep.where(cond.and(condMember)).groupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID)))
                        .and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.gt(anchor)))
                .groupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID).limit(pageSize);
            } else {
                userIdStep = firstStep.where(cond.and(condMember).and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.gt(anchor)))
                        .groupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID).limit(pageSize);
            }
            
            userIdStep.fetch().map(r ->{
                if(users.size() >= pageSize) {
                    return null;
                }
                users.add(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));            
                return null;
                
            });
            return true;
        });
        
        locator.setAnchor(null);
        if(users.size() >= pageSize){
            locator.setAnchor(users.get(users.size() - 1));
        }
        
        return users;
    }
    
    @Override
    public DoorAuth queryValidDoorAuthByVisitorPhone(Long doorId, String phone) {
        ListingLocator locator = new ListingLocator();
        long now = DateHelper.currentGMTTime().getTime();

        List<DoorAuth> auths = queryDoorAuthByTime(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                Condition c1 = Tables.EH_DOOR_AUTH.AUTH_TYPE.ne(DoorAuthType.FOREVER.getCode()).
                        and(Tables.EH_DOOR_AUTH.VALID_FROM_MS.le(now).
                        and(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now)));
                query.addConditions(Tables.EH_DOOR_AUTH.PHONE.eq(phone));
                query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                query.addConditions(c1);
                return query;
            }
        });

        if(auths == null || auths.size() == 0) {
            return null;
        }
        return auths.get(0);
    }

	@Override
	public List<DoorAuth> listValidDoorAuthByVisitorPhone(Long doorId, String phone) {
		ListingLocator locator = new ListingLocator();
        long now = DateHelper.currentGMTTime().getTime();

        List<DoorAuth> auths = queryDoorAuthByTime(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                Condition c1 = Tables.EH_DOOR_AUTH.AUTH_TYPE.ne(DoorAuthType.FOREVER.getCode()).
                        and(Tables.EH_DOOR_AUTH.VALID_FROM_MS.le(now).
                        and(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now)));
                query.addConditions(Tables.EH_DOOR_AUTH.PHONE.eq(phone));
                query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
                query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
                query.addConditions(c1);
                return query;
            }
        });
        return auths;
	}

	@Override
	public List<User> listCommunityAclinkUsers(Byte isAuth, Byte isOpenAuth, Long doorId, Byte communityType,
			Long communityId, CrossShardListingLocator locator, int pageSize, Integer namespaceId) {
		List<User> users = new ArrayList<>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj) -> {
			Condition cond = Tables.EH_USERS.ID.isNotNull();
			cond = getIsOpenAuthCond(cond, isOpenAuth);
			if (isAuth == null) {
				// 查全部小区的用户
				cond = cond.and((getCommunityAuthedCondition(context, namespaceId, communityType, communityId))
						.or(excludeAuthUser(context, namespaceId, communityType, communityId)));
			} else if (isAuth == 0) {
				// 查未认证,communityId在EH_USER_PROFILES里面查
				cond = cond.and(excludeAuthUser(context, namespaceId, communityType, communityId));
			} else if (isAuth == 1) {
				// 查已认证,communityId在EH_ORGANIZATION_COMMUNITY_REQUESTS或EH_GROUP_MEMBERS里
				cond = cond.and(getCommunityAuthedCondition(context, namespaceId, communityType, communityId));
			} else {
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
						AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "isAuth无效");
			}

			if (locator.getAnchor() != null) {
				cond = cond.and(Tables.EH_USERS.ID.lt(locator.getAnchor()));
			}

			SelectOffsetStep<Record> step = context.selectDistinct()
					.from(context.select().from(Tables.EH_USERS).where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
							.asTable(Tables.EH_USERS.getName()))
					.leftOuterJoin(context.select().from(Tables.EH_DOOR_AUTH)
							.where(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()))
							.and(Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode())
									.and(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId)))
							.asTable(Tables.EH_DOOR_AUTH.getName()))
					.on(Tables.EH_USERS.ID.eq(Tables.EH_DOOR_AUTH.USER_ID))
					.leftOuterJoin(context.select().from(Tables.EH_USER_PROFILES)
							// .where(Tables.EH_USER_PROFILES.ITEM_NAME.in(communityType
							// == 1
							// ?
							// UserCurrentEntityType.COMMUNITY_COMMERCIAL.getUserProfileKey()
							// :
							// UserCurrentEntityType.COMMUNITY_RESIDENTIAL.getUserProfileKey()))
							// -----与用户认证保持一致,按照下面这样写 by liuyilin 20180517
							.where(Tables.EH_USER_PROFILES.ITEM_NAME.in(
									UserCurrentEntityType.COMMUNITY_COMMERCIAL.getUserProfileKey(),
									UserCurrentEntityType.COMMUNITY_RESIDENTIAL.getUserProfileKey()))

							.asTable(Tables.EH_USER_PROFILES.getName()))
					.on(Tables.EH_USERS.ID.eq(Tables.EH_USER_PROFILES.OWNER_ID))
					.leftOuterJoin(context
							.select(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN, Tables.EH_USER_IDENTIFIERS.OWNER_UID)
							.from(Tables.EH_USER_IDENTIFIERS).asTable(Tables.EH_USER_IDENTIFIERS.getName()))
					.on(Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID))

					.where(cond).orderBy(Tables.EH_USERS.ID.desc()).limit(pageSize + 1);
			step.fetch().map(r -> {
				User user = new User();
				user.setId(r.getValue(Tables.EH_USERS.ID));
				user.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
				user.setIdentifierToken(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
				user.setGender(r.getValue(Tables.EH_USERS.GENDER));
				user.setCreateTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
				if (isAuth != null) {
					user.setStatus(isAuth);
				}
				users.add(user);
				return null;
			});
			return true;
		});
		locator.setAnchor(null);
		if (users.size() > pageSize) {
			users.remove(users.size() - 1);
			locator.setAnchor(users.get(users.size() - 1).getId());
		}
		return users;

	}


	private Condition getCommunityAuthedCondition(DSLContext context, Integer namespaceId,
			Byte communityType, Long communityId) {
		if(CommunityType.fromCode(communityType) == CommunityType.COMMERCIAL){
	        //商业型园区 1
	        Condition subQueryCondition =Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.ACTIVE.getCode())
	                .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
	        if(namespaceId != null){
	            subQueryCondition = subQueryCondition.and(Tables.EH_USER_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
	        }
        	if(communityId != null){
            	subQueryCondition = subQueryCondition.and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(communityId));
            }
        	return Tables.EH_USERS.ID.in(
                    context.selectDistinct(Tables.EH_USER_ORGANIZATIONS.USER_ID).from(Tables.EH_USER_ORGANIZATIONS)
                            .join(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS)
                            .on(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID))
                            .where(subQueryCondition)
            );
	    }else if(CommunityType.fromCode(communityType) == CommunityType.RESIDENTIAL){
	        //住宅型小区 0
        	List<Group> groups = groupProvider.listGroupByCommunityId(communityId, (loc, query) -> {
                Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
                query.addConditions(c);
                return query;
            });

    		List<Long> groupIds = new ArrayList<Long>();
    		for (Group group : groups) {
    			groupIds.add(group.getId());
    		}
    		List<GroupMember> groupMembers = groupProvider.listGroupMemberByGroupIds(groupIds, new CrossShardListingLocator(), null, (loc, query) -> {
    			Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
    			c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
                query.addConditions(c);

    			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID.desc());
    			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.desc());
    			query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
                return query;
            });

    		List<Long> userIds = new ArrayList<Long>();
    		for(GroupMember member : groupMembers){
    			userIds.add(member.getMemberId());
    		}
	        Condition subQueryCondition = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode())
	                .and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
	                .and(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));

	        if(namespaceId != null){
	            subQueryCondition = subQueryCondition.and(Tables.EH_GROUPS.NAMESPACE_ID.eq(namespaceId));
	        }
			subQueryCondition = subQueryCondition
					.and(Tables.EH_GROUP_MEMBERS.GROUP_ID.in(context.select(Tables.EH_GROUPS.ID)
							.from(Tables.EH_GROUPS).where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId)
									.and(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode())))));
			return Tables.EH_USERS.ID.in(userIds);
	    }
		return Tables.EH_USERS.ID.isNotNull();
	}

	private Condition excludeAuthUser(DSLContext context, Integer namespaceId, Byte communityType, Long communityId){
        if(CommunityType.fromCode(communityType) == CommunityType.COMMERCIAL){
            //子查询的条件  认证中暂算作未认证    UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode()
            Condition subQueryCondition =Tables.EH_USER_ORGANIZATIONS.STATUS.in(UserOrganizationStatus.ACTIVE.getCode())
                    .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
            if(namespaceId != null){
                subQueryCondition = subQueryCondition.and(Tables.EH_USER_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
            }
            return Tables.EH_USERS.ID.notIn(
                    context.select(Tables.EH_USER_ORGANIZATIONS.USER_ID).from(Tables.EH_USER_ORGANIZATIONS)
                            .join(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS)
                            .on(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID))
                            .where(subQueryCondition)
            ).and(Tables.EH_USER_PROFILES.ITEM_VALUE.eq(String.valueOf(communityId)));
        }else if(CommunityType.fromCode(communityType) == CommunityType.RESIDENTIAL){
            //子查询的条件 认证中暂算作未认证 GroupMemberStatus.REJECT.getCode()
            Condition subQueryCondition = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode())
            		.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.in(GroupMemberStatus.ACTIVE.getCode()))
                    .and(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));

            if(namespaceId != null){
                subQueryCondition = subQueryCondition.and(Tables.EH_GROUPS.NAMESPACE_ID.eq(namespaceId));
            }
            return Tables.EH_USERS.ID.notIn(
                    context.select(Tables.EH_GROUP_MEMBERS.MEMBER_ID).from(Tables.EH_GROUP_MEMBERS)
                            .join(Tables.EH_GROUPS)
                            .on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
                            .where(subQueryCondition)
            ).and(Tables.EH_USER_PROFILES.ITEM_VALUE.eq(String.valueOf(communityId)));

        }

        return Tables.EH_USERS.ID.isNotNull();
    }

	@Override
	public Long countCommunityDoorAuthUser(Byte isAuth, Byte isOpenAuth, Long doorId, Long communityId, Byte communityType,
			Integer namespaceId, Byte rightType) {
		List<Long> counts = new ArrayList<>();

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj)->{
            Condition cond = Tables.EH_USERS.ID.isNotNull();
            //授权条件
            if(null != isOpenAuth){
                Condition authCond = null;
                if(DoorAuthRightType.fromCode(rightType) == DoorAuthRightType.RIGHT_OPEN){
                    authCond = Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq(isOpenAuth);
                }else if(DoorAuthRightType.fromCode(rightType) == DoorAuthRightType.RIGHT_VISITOR){
                    authCond = Tables.EH_DOOR_AUTH.RIGHT_VISITOR.eq(isOpenAuth);
                }else if(DoorAuthRightType.fromCode(rightType) == DoorAuthRightType.RIGHT_REMOTE){
                    authCond = Tables.EH_DOOR_AUTH.RIGHT_REMOTE.eq(isOpenAuth);
                }

                if(isOpenAuth < 1){
                    authCond = authCond.or(Tables.EH_DOOR_AUTH.DOOR_ID.isNull());
                }
                cond = cond.and(authCond);
            }

            //认证条件
            if (isAuth == null) {
				// 全部用户(不在园区中,且在公司也没有认证的用户不统计)
            	//已认证的公司用户
            	Condition isAuthCond = getIsAuthCond((byte) 1);
            	//+全部的园区用户
				cond = cond.and(isAuthCond.or((getCommunityAuthedCondition(context, namespaceId, communityType, communityId))
						.or(excludeAuthUser(context, namespaceId, communityType, communityId))));
			} else if (isAuth == 0) {
				//公司未认证(用户不在公司中)
				Condition isAuthCond = getIsAuthCond((byte) 0);
				//&用户在园区中,未认证
				cond = cond.and(isAuthCond.and(excludeAuthUser(context, namespaceId, communityType, communityId)));
			} else if (isAuth == 1) {
				//已认证的公司用户
				Condition isAuthCond = getIsAuthCond((byte) 1);
            	//+已认证的园区用户
				cond = cond.and(isAuthCond.or(getCommunityAuthedCondition(context, namespaceId, communityType, communityId)));
			} else {
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
						AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "isAuth无效");
			}

            SelectConditionStep<Record1<Integer>> step = context.selectCount().from(
                    context.select().from(Tables.EH_USERS)
                            .where(
                                    Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId)
                            ).asTable(Tables.EH_USERS.getName()))
                    .leftOuterJoin(
                            context.select().from(Tables.EH_DOOR_AUTH)
                                    .where(
                                            Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()))
                                    .and(
                                            Tables.EH_DOOR_AUTH.AUTH_TYPE.eq(DoorAuthType.FOREVER.getCode())
                                                    .and(
                                                            Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId)))
                                    .asTable(Tables.EH_DOOR_AUTH.getName()))
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_DOOR_AUTH.USER_ID))
                    .leftOuterJoin(context.select(
                            Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID,
                            Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE,
                            Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN,
                            Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME,
                            Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID)
                            .from(Tables.EH_ORGANIZATION_MEMBERS)
                            .leftOuterJoin(Tables.EH_ORGANIZATIONS)
                            .on(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID))
                            .where(
                                    Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                            .and(
                                    Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
                            .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()))
                            .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0L))
                            .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()))
                            .groupBy(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID)
                            .asTable(Tables.EH_ORGANIZATION_MEMBERS.getName()))
                    .on(Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID))
                    .leftOuterJoin(context.select(
                    		Tables.EH_USER_PROFILES.ITEM_NAME,
                    		Tables.EH_USER_PROFILES.ITEM_VALUE,
                    		Tables.EH_USER_PROFILES.OWNER_ID
                    		).from(Tables.EH_USER_PROFILES)
							// .where(Tables.EH_USER_PROFILES.ITEM_NAME.in(communityType
							// == 1
							// ?
							// UserCurrentEntityType.COMMUNITY_COMMERCIAL.getUserProfileKey()
							// :
							// UserCurrentEntityType.COMMUNITY_RESIDENTIAL.getUserProfileKey()))
							// -----与用户认证保持一致,按照下面这样写 by liuyilin 20180517
							.where(Tables.EH_USER_PROFILES.ITEM_NAME.in(
									UserCurrentEntityType.COMMUNITY_COMMERCIAL.getUserProfileKey(),
									UserCurrentEntityType.COMMUNITY_RESIDENTIAL.getUserProfileKey()))
							.asTable(Tables.EH_USER_PROFILES.getName()))
					.on(Tables.EH_USERS.ID.eq(Tables.EH_USER_PROFILES.OWNER_ID))
                    .where(cond);
            counts.add(Long.valueOf(step.fetchOne().getValue(0).toString()));
            return true;
        });

        return counts.get(0);
	}




    @Override
    public List<DoorAuth> listValidDoorAuthByUser(long userId, String driver) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhDoorAuthRecord> query = context.selectQuery(Tables.EH_DOOR_AUTH);
        query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
        if (null != driver){
            query.addConditions(Tables.EH_DOOR_AUTH.DRIVER.eq(driver));
        }
        query.addConditions(Tables.EH_DOOR_AUTH.STATUS.eq(DoorAuthStatus.VALID.getCode()));
        return query.fetch().stream().map(r -> ConvertHelper.convert(r,DoorAuth.class)).collect(Collectors.toList());
    }
}
