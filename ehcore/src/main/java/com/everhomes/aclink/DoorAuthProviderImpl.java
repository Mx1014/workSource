package com.everhomes.aclink;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationStatus;
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
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    public List<DoorAuth> searchVisitorDoorAuthByAdmin(Long doorId, String keyword, Byte status, int pageSize, Long startTime, Long endTime) {
        return queryDoorAuthByTime(new ListingLocator(), pageSize, (locator, query) -> {
            if(status != null) {
                Long now = DateHelper.currentGMTTime().getTime();
                if(status.equals(DoorAuthStatus.INVALID.getCode())) {
                    query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.lt(now).or(Tables.EH_DOOR_AUTH.STATUS.eq(status)));
                } else {
                    query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now).and(Tables.EH_DOOR_AUTH.STATUS.eq(status)));
                }
            }
            if(doorId != null) {
                query.addConditions(Tables.EH_DOOR_AUTH.DOOR_ID.eq(doorId));
            }
            if (startTime != null) {
                query.addConditions(Tables.EH_DOOR_AUTH.CREATE_TIME.ge(new Timestamp(startTime)));
            }
            if (endTime != null) {
                query.addConditions(Tables.EH_DOOR_AUTH.CREATE_TIME.le(new Timestamp(endTime)));
            }
            if(keyword != null) {
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
                      query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.lt(now).or(Tables.EH_DOOR_AUTH.STATUS.eq(status)));
                    } else {
                    	query.addConditions(Tables.EH_DOOR_AUTH.VALID_END_MS.ge(now).and(Tables.EH_DOOR_AUTH.STATUS.eq(status)));
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

        System.out.println("Query sql ====== " + step.getSQL());

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

            System.out.println("query sql:" + step.getSQL());

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

}
