package com.everhomes.meeting;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.meeting.MeetingGeneralFlag;
import com.everhomes.rest.meeting.MeetingMemberSourceType;
import com.everhomes.rest.meeting.MeetingRoomStatus;
import com.everhomes.rest.meeting.QueryMyMeetingTemplateCondition;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhMeetingAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhMeetingInvitationTemplatesDao;
import com.everhomes.server.schema.tables.daos.EhMeetingInvitationsDao;
import com.everhomes.server.schema.tables.daos.EhMeetingRecordsDao;
import com.everhomes.server.schema.tables.daos.EhMeetingReservationsDao;
import com.everhomes.server.schema.tables.daos.EhMeetingRoomsDao;
import com.everhomes.server.schema.tables.daos.EhMeetingTemplatesDao;
import com.everhomes.server.schema.tables.pojos.EhMeetingAttachments;
import com.everhomes.server.schema.tables.pojos.EhMeetingInvitationTemplates;
import com.everhomes.server.schema.tables.pojos.EhMeetingInvitations;
import com.everhomes.server.schema.tables.pojos.EhMeetingRecords;
import com.everhomes.server.schema.tables.pojos.EhMeetingReservations;
import com.everhomes.server.schema.tables.pojos.EhMeetingRooms;
import com.everhomes.server.schema.tables.pojos.EhMeetingTemplates;
import com.everhomes.server.schema.tables.records.EhMeetingAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhMeetingInvitationTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhMeetingInvitationsRecord;
import com.everhomes.server.schema.tables.records.EhMeetingRecordsRecord;
import com.everhomes.server.schema.tables.records.EhMeetingReservationsRecord;
import com.everhomes.server.schema.tables.records.EhMeetingRoomsRecord;
import com.everhomes.server.schema.tables.records.EhMeetingTemplatesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class MeetingProviderImpl implements MeetingProvider {
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public MeetingRoom findMeetingRoomById(Integer namespaceId, Long organizationId, Long meetingRoomId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingRoomsRecord> query = context.selectQuery(Tables.EH_MEETING_ROOMS);
        query.addConditions(Tables.EH_MEETING_ROOMS.ID.eq(meetingRoomId));
        query.addConditions(Tables.EH_MEETING_ROOMS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_MEETING_ROOMS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_MEETING_ROOMS.STATUS.ne(MeetingRoomStatus.DELETED.getCode()));
        EhMeetingRoomsRecord meetingRoomsRecord = query.fetchOne();
        return ConvertHelper.convert(meetingRoomsRecord, MeetingRoom.class);
    }

    @Override
    public boolean checkMeetingRoomNameExist(CheckMeetingRoomNameExistCondition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingRoomsRecord> query = context.selectQuery(Tables.EH_MEETING_ROOMS);
        query.addConditions(Tables.EH_MEETING_ROOMS.NAMESPACE_ID.eq(condition.getNamespaceId()));
        query.addConditions(Tables.EH_MEETING_ROOMS.ORGANIZATION_ID.eq(condition.getOrganizationId()));
        query.addConditions(Tables.EH_MEETING_ROOMS.OWNER_TYPE.eq(condition.getOwnerType()));
        query.addConditions(Tables.EH_MEETING_ROOMS.OWNER_ID.eq(condition.getOwnerId()));
        query.addConditions(Tables.EH_MEETING_ROOMS.NAME.eq(condition.getName()));
        query.addConditions(Tables.EH_MEETING_ROOMS.STATUS.ne(MeetingRoomStatus.DELETED.getCode()));
        if (condition.getId() != null) {
            query.addConditions(Tables.EH_MEETING_ROOMS.ID.ne(condition.getId()));
        }
        return query.fetchCount() > 0;
    }

    @Override
    public Long createMeetingRoom(MeetingRoom meetingRoom) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMeetingRooms.class));
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        meetingRoom.setId(id);
        meetingRoom.setCreateTime(now);
        meetingRoom.setOperateTime(now);
        if (meetingRoom.getCreatorUid() == null) {
            meetingRoom.setCreatorUid(UserContext.currentUserId());
            meetingRoom.setOperatorUid(UserContext.currentUserId());
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingRooms.class));
        EhMeetingRoomsDao dao = new EhMeetingRoomsDao(context.configuration());
        dao.insert(meetingRoom);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhMeetingRooms.class, null);
        return id;
    }

    @Override
    public Long updateMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoom.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meetingRoom.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingRooms.class));
        EhMeetingRoomsDao dao = new EhMeetingRoomsDao(context.configuration());
        dao.update(meetingRoom);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMeetingRooms.class, meetingRoom.getId());

        return meetingRoom.getId();
    }

    @Override
    public List<MeetingRoom> findMeetingRooms(Integer pageSize, Integer offset, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhMeetingRoomsRecord> query = context.selectQuery(Tables.EH_MEETING_ROOMS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(new ListingLocator(), query);

        query.addConditions(Tables.EH_MEETING_ROOMS.STATUS.ne(MeetingRoomStatus.DELETED.getCode()));
        query.addOrderBy(Tables.EH_MEETING_ROOMS.CREATE_TIME.desc());
        if (offset != null && pageSize != null) {
            query.addLimit(offset, pageSize);
        }

        Result<EhMeetingRoomsRecord> result = query.fetch();

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        return result.map((r) -> {
            return ConvertHelper.convert(r, MeetingRoom.class);
        });
    }

    @Override
    public boolean shouldInitDefaultMeetingRoom(Integer namespaceId, Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhMeetingRoomsRecord> query = context.selectQuery(Tables.EH_MEETING_ROOMS);
        query.addConditions(Tables.EH_MEETING_ROOMS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_MEETING_ROOMS.ORGANIZATION_ID.eq(organizationId));
        return query.fetchCount() == 0;
    }

    @Override
    public boolean checkMeetingTimeIsAvailable(CheckMeetingTimeAvailableCondition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_ROOM_ID.eq(condition.getMeetingRoomId()));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_DATE.eq(condition.getMeetingDate()));
        Condition condition1 = Tables.EH_MEETING_RESERVATIONS.LOCK_BEGIN_TIME.le(condition.getBeginDateTime()).and(Tables.EH_MEETING_RESERVATIONS.LOCK_END_TIME.gt(condition.getBeginDateTime()));
        Condition condition2 = Tables.EH_MEETING_RESERVATIONS.LOCK_BEGIN_TIME.ge(condition.getBeginDateTime()).and(Tables.EH_MEETING_RESERVATIONS.LOCK_END_TIME.le(condition.getEndDateTime()));
        Condition condition3 = Tables.EH_MEETING_RESERVATIONS.LOCK_BEGIN_TIME.lt(condition.getEndDateTime()).and(Tables.EH_MEETING_RESERVATIONS.LOCK_END_TIME.ge(condition.getEndDateTime()));
        query.addConditions(condition1.or(condition2).or(condition3));
        if (condition.getMeetingReservationId() != null) {
            query.addConditions(Tables.EH_MEETING_RESERVATIONS.ID.ne(condition.getMeetingReservationId()));
        }
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.in(Arrays.asList(MeetingReservationStatus.TIME_LOCK.getCode(), MeetingReservationStatus.NORMAL.getCode())));

        return query.fetchCount() == 0;
    }

    @Override
    public List<MeetingReservation> findMeetingReservationByTimeUnit(Long meetingRoomId, Date meetingDate, Time beginTime, Time endTime) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Timestamp beginDateTime = new Timestamp(LocalDateTime.of(meetingDate.toLocalDate(), beginTime.toLocalTime()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Timestamp endDateTime = new Timestamp(LocalDateTime.of(meetingDate.toLocalDate(), endTime.toLocalTime()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_ROOM_ID.eq(meetingRoomId));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_DATE.eq(meetingDate));
        Condition condition1 = Tables.EH_MEETING_RESERVATIONS.LOCK_BEGIN_TIME.ge(beginDateTime).and(Tables.EH_MEETING_RESERVATIONS.LOCK_BEGIN_TIME.lt(endDateTime));
        Condition condition2 = Tables.EH_MEETING_RESERVATIONS.LOCK_END_TIME.gt(beginDateTime).and(Tables.EH_MEETING_RESERVATIONS.LOCK_END_TIME.lt(endDateTime));
        Condition condition3 = Tables.EH_MEETING_RESERVATIONS.LOCK_BEGIN_TIME.le(beginDateTime).and(Tables.EH_MEETING_RESERVATIONS.LOCK_END_TIME.ge(endDateTime));

        query.addConditions(condition1.or(condition2).or(condition3));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.in(Arrays.asList(MeetingReservationStatus.TIME_LOCK.getCode(), MeetingReservationStatus.NORMAL.getCode())));
        Result<EhMeetingReservationsRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, MeetingReservation.class);
        });
    }

    @Override
    public List<MeetingReservation> findNotBeginningMeetingReservationByRoomId(Long meetingRoomId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_ROOM_ID.eq(meetingRoomId));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.eq(MeetingReservationStatus.NORMAL.getCode()));
        // 过滤掉进行中或已结束的会议
        // 未开始
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.EXPECT_BEGIN_TIME.gt(now));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ACT_BEGIN_TIME.isNull());
        // 未结束
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.EXPECT_END_TIME.gt(now));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ACT_END_TIME.isNull());

        Result<EhMeetingReservationsRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, MeetingReservation.class);
        });
    }

    @Override
    public List<MeetingReservation> findMeetingRoomReservationDetails(List<Long> meetingRoomIds, Date queryBeginDate, Date queryEndDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_ROOM_ID.in(meetingRoomIds));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_DATE.ge(queryBeginDate));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_DATE.le(queryEndDate));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.in(Arrays.asList(MeetingReservationStatus.TIME_LOCK.getCode(), MeetingReservationStatus.NORMAL.getCode())));

        Result<EhMeetingReservationsRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, MeetingReservation.class);
        });
    }

    /**
     * 正常的会议预订记录是使用status=0逻辑删除，而会议预订时间锁定超时是物理删除，
     * 因此判断该id是否物理删除来判断是否是超时被回收
     */
    @Override
    public boolean checkMeetingReservationBeRecovery(Long meetingReservationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ID.eq(meetingReservationId));
        return query.fetchCount() == 0;
    }

    @Override
    public MeetingReservation findMeetingReservationById(Integer namespaceId, Long organizationId, Long meetingReservationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ID.eq(meetingReservationId));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.ne(MeetingRoomStatus.DELETED.getCode()));
        EhMeetingReservationsRecord meetingReservationsRecord = query.fetchOne();
        return ConvertHelper.convert(meetingReservationsRecord, MeetingReservation.class);
    }

    @Override
    public List<EhMeetingReservations> findLockStatusMeetingReservations(Timestamp beforeLockTime, int limitCount) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.OPERATE_TIME.le(beforeLockTime));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.eq(MeetingReservationStatus.TIME_LOCK.getCode()));
        query.addLimit(limitCount);
        Result<EhMeetingReservationsRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, EhMeetingReservations.class);
        });
    }

    @Override
    public List<EhMeetingReservations> findLockStatusMeetingReservationsBySponsorUserId(Long sponsorUserId, Long meetingRoomId) {
        if (sponsorUserId == null || sponsorUserId <= 0) {
            return Collections.emptyList();
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_SPONSOR_USER_ID.eq(sponsorUserId));
        if (meetingRoomId != null) {
            query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_ROOM_ID.eq(meetingRoomId));
        }
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.eq(MeetingReservationStatus.TIME_LOCK.getCode()));
        Result<EhMeetingReservationsRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, EhMeetingReservations.class);
        });
    }

    @Override
    public List<MeetingReservation> findComingSoonMeetingReservations(int comingSoonMinute, int limitCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateHelper.currentGMTTime());
        calendar.add(Calendar.MINUTE, comingSoonMinute);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp comingSoon = new Timestamp(calendar.getTimeInMillis());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingReservationsRecord> query = context.selectQuery(Tables.EH_MEETING_RESERVATIONS);
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.MEETING_DATE.eq(new Date(comingSoon.getTime())));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.LOCK_BEGIN_TIME.between(now, comingSoon));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.LOCK_END_TIME.gt(now));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ACT_BEGIN_TIME.isNull());
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ACT_END_TIME.isNull());
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.SYSTEM_MESSAGE_FLAG.eq(MeetingGeneralFlag.ON.getCode()));
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.ACT_REMIND_TIME.isNull());
        query.addConditions(Tables.EH_MEETING_RESERVATIONS.STATUS.eq(MeetingReservationStatus.NORMAL.getCode()));
        query.addLimit(limitCount);
        Result<EhMeetingReservationsRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, MeetingReservation.class);
        });
    }

    @Override
    public Long createMeetingReservation(MeetingReservation meetingReservation) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMeetingReservations.class));
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        meetingReservation.setId(id);
        meetingReservation.setCreateTime(now);
        meetingReservation.setCreatorUid(UserContext.currentUserId());
        meetingReservation.setOperateTime(now);
        meetingReservation.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingReservations.class));
        EhMeetingReservationsDao dao = new EhMeetingReservationsDao(context.configuration());
        dao.insert(meetingReservation);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhMeetingReservations.class, null);

        return id;
    }

    @Override
    public Long updateMeetingReservation(MeetingReservation meetingReservation) {
        meetingReservation.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meetingReservation.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingReservations.class));
        EhMeetingReservationsDao dao = new EhMeetingReservationsDao(context.configuration());
        dao.update(meetingReservation);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMeetingReservations.class, meetingReservation.getId());

        return meetingReservation.getId();
    }

    @Override
    public void deleteMeetingReservation(MeetingReservation meetingReservation) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingReservations.class));
        EhMeetingReservationsDao dao = new EhMeetingReservationsDao(context.configuration());
        dao.delete(meetingReservation);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMeetingReservations.class, meetingReservation.getId());
    }

    @Override
    public void batchDeleteMeetingReservations(List<EhMeetingReservations> meetingReservations) {
        if (CollectionUtils.isEmpty(meetingReservations)) {
            return;
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingReservations.class));
        EhMeetingReservationsDao dao = new EhMeetingReservationsDao(context.configuration());
        dao.delete(meetingReservations);
    }

    @Override
    public void batchUpdateMeetingReservationRemindStatus(List<MeetingReservation> meetingReservations) {
        if (CollectionUtils.isEmpty(meetingReservations)) {
            return;
        }
        Collection<Long> ids = new HashSet<>();
        meetingReservations.forEach(meetingReservation -> {
            ids.add(meetingReservation.getId());
        });
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingReservations.class));
        UpdateQuery<EhMeetingReservationsRecord> updateQuery = context.updateQuery(Tables.EH_MEETING_RESERVATIONS);
        updateQuery.addValue(Tables.EH_MEETING_RESERVATIONS.ACT_REMIND_TIME, new Timestamp(System.currentTimeMillis()));
        updateQuery.addConditions(Tables.EH_MEETING_RESERVATIONS.ID.in(ids));
        updateQuery.execute();
    }

    @Override
    public List<MeetingReservation> findMeetingReservationsByDetailId(QueryMyMeetingsCondition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> query = context.selectDistinct(Tables.EH_MEETING_RESERVATIONS.fields()).from(Tables.EH_MEETING_RESERVATIONS).join(Tables.EH_MEETING_INVITATIONS).on(Tables.EH_MEETING_RESERVATIONS.ID.eq(Tables.EH_MEETING_INVITATIONS.MEETING_RESERVATION_ID));
        Condition queryCondition = Tables.EH_MEETING_RESERVATIONS.NAMESPACE_ID.eq(condition.getNamespaceId());
        queryCondition = queryCondition.and(Tables.EH_MEETING_RESERVATIONS.ORGANIZATION_ID.eq(condition.getOrganizationId()));
        queryCondition = queryCondition.and(Tables.EH_MEETING_INVITATIONS.SOURCE_TYPE.eq(MeetingMemberSourceType.MEMBER_DETAIL.getCode()));
        queryCondition = queryCondition.and(Tables.EH_MEETING_INVITATIONS.SOURCE_ID.eq(condition.getDetailId()));
        queryCondition = queryCondition.and(Tables.EH_MEETING_INVITATIONS.ROLE_TYPE.in(MeetingInvitationRoleType.ATTENDEE.getCode(), MeetingInvitationRoleType.MEETING_MANAGER.getCode()));
        queryCondition = queryCondition.and(Tables.EH_MEETING_RESERVATIONS.STATUS.eq(MeetingReservationStatus.NORMAL.getCode()));
        if (condition.getBetweenFromDate() != null) {
            queryCondition = queryCondition.and(Tables.EH_MEETING_RESERVATIONS.MEETING_DATE.ge(condition.getBetweenFromDate()));
        }
        if (condition.getBetweenToDate() != null) {
            queryCondition = queryCondition.and(Tables.EH_MEETING_RESERVATIONS.MEETING_DATE.le(condition.getBetweenToDate()));
        }

        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        if (condition.isEndFlag()) {
            queryCondition = queryCondition.and(Tables.EH_MEETING_RESERVATIONS.EXPECT_END_TIME.lt(now)
                    .or(Tables.EH_MEETING_RESERVATIONS.ACT_END_TIME.isNotNull()));
            query.orderBy(Tables.EH_MEETING_RESERVATIONS.EXPECT_BEGIN_TIME.asc(), Tables.EH_MEETING_RESERVATIONS.CREATE_TIME.asc());
        } else {
            queryCondition = queryCondition.and(Tables.EH_MEETING_RESERVATIONS.EXPECT_END_TIME.ge(now)
                    .and(Tables.EH_MEETING_RESERVATIONS.ACT_END_TIME.isNull()));
            query.orderBy(Tables.EH_MEETING_RESERVATIONS.EXPECT_BEGIN_TIME.asc(), Tables.EH_MEETING_RESERVATIONS.CREATE_TIME.asc());
        }
        query.where(queryCondition);
        if (condition.getOffset() != null && condition.getPageSize() != null) {
            query.limit(condition.getOffset(), condition.getPageSize());
        }
        List<MeetingReservation> results = query.fetch().map(new DefaultRecordMapper(Tables.EH_MEETING_RESERVATIONS.recordType(), MeetingReservation.class));
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results;
    }

    @Override
    public List<EhMeetingInvitations> findMeetingInvitationsByMeetingId(Long meetingReservationId, String... roleTypes) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingInvitationsRecord> query = context.selectQuery(Tables.EH_MEETING_INVITATIONS);
        query.addConditions(Tables.EH_MEETING_INVITATIONS.MEETING_RESERVATION_ID.eq(meetingReservationId));
        if (roleTypes != null && roleTypes.length > 0) {
            query.addConditions(Tables.EH_MEETING_INVITATIONS.ROLE_TYPE.in(roleTypes));
        } else {
            return new ArrayList<>();
        }

        Result<EhMeetingInvitationsRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, EhMeetingInvitations.class);
        });
    }

    @Override
    public int countMeetingInvitation(Long meetingReservationId, String sourceType, Long sourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingInvitationsRecord> query = context.selectQuery(Tables.EH_MEETING_INVITATIONS);
        query.addConditions(Tables.EH_MEETING_INVITATIONS.MEETING_RESERVATION_ID.eq(meetingReservationId));
        query.addConditions(Tables.EH_MEETING_INVITATIONS.SOURCE_TYPE.eq(sourceType));
        query.addConditions(Tables.EH_MEETING_INVITATIONS.SOURCE_ID.eq(sourceId));
        return query.fetchCount();
    }

    @Override
    public void batchCreateMeetingInvitations(List<EhMeetingInvitations> meetingInvitations) {
        if (CollectionUtils.isEmpty(meetingInvitations)) {
            return;
        }
        long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhMeetingInvitations.class), meetingInvitations.size());
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());

        for (EhMeetingInvitations meetingInvitation : meetingInvitations) {
            meetingInvitation.setId(id++);
            meetingInvitation.setCreateTime(now);
            meetingInvitation.setCreatorUid(UserContext.currentUserId());
            meetingInvitation.setOperateTime(now);
            meetingInvitation.setOperatorUid(UserContext.currentUserId());
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingInvitations.class));
        EhMeetingInvitationsDao dao = new EhMeetingInvitationsDao(context.configuration());
        dao.insert(meetingInvitations);
    }

    @Override
    public void batchDeleteMeetingInvitationsByMeetingId(Long meetingReservationId, String... roleTypes) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhMeetingInvitationsRecord> deleteQuery = context.deleteQuery(Tables.EH_MEETING_INVITATIONS);
        deleteQuery.addConditions(Tables.EH_MEETING_INVITATIONS.MEETING_RESERVATION_ID.eq(meetingReservationId));
        deleteQuery.addConditions(Tables.EH_MEETING_INVITATIONS.ROLE_TYPE.in(roleTypes));
        deleteQuery.execute();
    }

    @Override
    public void batchDeleteMeetingInvitations(List<EhMeetingInvitations> meetingInvitations) {
        if (CollectionUtils.isEmpty(meetingInvitations)) {
            return;
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingInvitations.class));
        EhMeetingInvitationsDao dao = new EhMeetingInvitationsDao(context.configuration());
        dao.delete(meetingInvitations);
    }

    @Override
    public Long createMeetingRecord(MeetingRecord meetingRecord) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMeetingRecords.class));
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        meetingRecord.setId(id);
        meetingRecord.setCreateTime(now);
        meetingRecord.setCreatorUid(UserContext.currentUserId());
        meetingRecord.setOperateTime(now);
        meetingRecord.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingRecords.class));
        EhMeetingRecordsDao dao = new EhMeetingRecordsDao(context.configuration());
        dao.insert(meetingRecord);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhMeetingRecords.class, null);

        return id;
    }

    @Override
    public Long updateMeetingRecord(MeetingRecord meetingRecord) {
        meetingRecord.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meetingRecord.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingRecords.class));
        EhMeetingRecordsDao dao = new EhMeetingRecordsDao(context.configuration());
        dao.update(meetingRecord);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMeetingRecords.class, meetingRecord.getId());

        return meetingRecord.getId();
    }

    @Override
    public void deleteMeetingRecord(MeetingRecord meetingRecord) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingRecords.class));
        EhMeetingRecordsDao dao = new EhMeetingRecordsDao(context.configuration());
        dao.delete(meetingRecord);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMeetingRecords.class, meetingRecord.getId());
    }

    @Override
    public MeetingRecord findMeetingRecordById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhMeetingRecordsDao dao = new EhMeetingRecordsDao(context.configuration());
        EhMeetingRecords meetingRecordsRecord = (EhMeetingRecords) dao.findById(id);
        return ConvertHelper.convert(meetingRecordsRecord, MeetingRecord.class);
    }

    @Override
    public MeetingRecord findMeetingRecordByMeetingReservationId(Long meetingReservationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingRecordsRecord> query = context.selectQuery(Tables.EH_MEETING_RECORDS);
        query.addConditions(Tables.EH_MEETING_RECORDS.MEETING_RESERVATION_ID.eq(meetingReservationId));
        query.addLimit(1);

        EhMeetingRecordsRecord meetingRecordsRecord = query.fetchOne();
        return ConvertHelper.convert(meetingRecordsRecord, MeetingRecord.class);
    }

    @Override
    public List<MeetingRecord> findMeetingRecordsByDetailId(QueryMyMeetingRecordsCondition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record> query = context.selectDistinct(Tables.EH_MEETING_RECORDS.fields()).from(Tables.EH_MEETING_RECORDS
                .join(Tables.EH_MEETING_RESERVATIONS).on(Tables.EH_MEETING_RESERVATIONS.ID.eq(Tables.EH_MEETING_RECORDS.MEETING_RESERVATION_ID))
                .join(Tables.EH_MEETING_INVITATIONS).on(Tables.EH_MEETING_RESERVATIONS.ID.eq(Tables.EH_MEETING_INVITATIONS.MEETING_RESERVATION_ID)));
        query.where(Tables.EH_MEETING_RESERVATIONS.NAMESPACE_ID.eq(condition.getNamespaceId())
                .and(Tables.EH_MEETING_RESERVATIONS.ORGANIZATION_ID.eq(condition.getOrganizationId()))
                .and(Tables.EH_MEETING_RESERVATIONS.STATUS.in(Arrays.asList(MeetingReservationStatus.CANCELED.getCode(), MeetingReservationStatus.NORMAL.getCode())))
                .and(Tables.EH_MEETING_INVITATIONS.SOURCE_TYPE.eq(MeetingMemberSourceType.MEMBER_DETAIL.getCode()))
                .and(Tables.EH_MEETING_INVITATIONS.SOURCE_ID.eq(condition.getDetailId())));
        query.orderBy(Tables.EH_MEETING_RECORDS.OPERATE_TIME.desc());
        if (condition.getOffset() != null && condition.getPageSize() != null) {
            query.limit(condition.getOffset(), condition.getPageSize());
        }
        List<MeetingRecord> results = query.fetch().map(new DefaultRecordMapper(Tables.EH_MEETING_RECORDS.recordType(), MeetingRecord.class));
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results;
    }

	@Override
	public List<MeetingAttachment> listMeetingAttachments(Long ownerId, String ownerType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectConditionStep<Record> query = context.select().from(Tables.EH_MEETING_ATTACHMENTS)
        		.where(Tables.EH_MEETING_ATTACHMENTS.OWNER_ID.eq(ownerId))
        		.and(Tables.EH_MEETING_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
         
        List<MeetingAttachment> results = query.fetch().map(r->ConvertHelper.convert(r, MeetingAttachment.class));
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results;
	}

	@Override
	public void batchDeleteMeetingAttachments(List<MeetingAttachment> deleteAttachements) {
		if (CollectionUtils.isEmpty(deleteAttachements)) {
            return;
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingReservations.class));
        EhMeetingAttachmentsDao dao = new EhMeetingAttachmentsDao(context.configuration());
        dao.delete(deleteAttachements.stream().map(r->{return ConvertHelper.convert(r, EhMeetingAttachments.class);}).collect(Collectors.toList()));
	}

	@Override
	public void batchCreateMeetingAttachments(List<MeetingAttachment> addAttachements) {
		if (CollectionUtils.isEmpty(addAttachements)) {
            return;
        }
        long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhMeetingInvitations.class), addAttachements.size());
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        
        List<EhMeetingAttachments> attachments = new ArrayList<>();
        for (MeetingAttachment meetingAttachment : addAttachements) {
        	EhMeetingAttachments attachment = ConvertHelper.convert(meetingAttachment, EhMeetingAttachments.class);
        	attachment.setId(id++);
        	attachment.setCreateTime(now);
        	attachment.setCreatorUid(UserContext.currentUserId()); 
        	attachments.add(attachment);
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingInvitations.class));
        EhMeetingAttachmentsDao dao = new EhMeetingAttachmentsDao(context.configuration());
		dao.insert(attachments);
	}

    @Override
    public void deleteMeetingAttachmentsByOwnerId(Integer namespaceId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingAttachments.class));
        DeleteQuery<EhMeetingAttachmentsRecord> deleteQuery = context.deleteQuery(Tables.EH_MEETING_ATTACHMENTS);
        deleteQuery.addConditions(Tables.EH_MEETING_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId));
        deleteQuery.addConditions(Tables.EH_MEETING_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
        deleteQuery.addConditions(Tables.EH_MEETING_ATTACHMENTS.OWNER_ID.eq(ownerId));
        deleteQuery.execute();
    }

    @Override
    public MeetingTemplate findMeetingTemplateById(Long id, Integer namespaceId, Long organizationId, Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingTemplatesRecord> query = context.selectQuery(Tables.EH_MEETING_TEMPLATES);
        query.addConditions(Tables.EH_MEETING_TEMPLATES.ID.eq(id));
        query.addConditions(Tables.EH_MEETING_TEMPLATES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_MEETING_TEMPLATES.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_MEETING_TEMPLATES.USER_ID.eq(userId));
        query.addLimit(1);

        EhMeetingTemplatesRecord record = query.fetchOne();
        return ConvertHelper.convert(record, MeetingTemplate.class);
    }

    @Override
    public Long createMeetingTemplate(MeetingTemplate template) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingTemplates.class));
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMeetingTemplates.class));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        template.setId(id);
        template.setCreatorUid(UserContext.currentUserId());
        template.setCreateTime(now);
        template.setOperatorUid(UserContext.currentUserId());
        template.setOperateTime(now);

        EhMeetingTemplatesDao dao = new EhMeetingTemplatesDao(context.configuration());
        dao.insert(template);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhMeetingTemplates.class, null);
        return id;
    }

    @Override
    public Long updateMeetingTemplate(MeetingTemplate template) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingTemplates.class));

        template.setOperatorUid(UserContext.currentUserId());
        template.setOperateTime(new Timestamp(System.currentTimeMillis()));

        EhMeetingTemplatesDao dao = new EhMeetingTemplatesDao(context.configuration());
        dao.update(template);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMeetingTemplates.class, template.getId());
        return template.getId();
    }

    @Override
    public void deleteMeetingTemplate(MeetingTemplate template) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingTemplates.class));

        EhMeetingTemplatesDao dao = new EhMeetingTemplatesDao(context.configuration());
        dao.delete(template);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMeetingTemplates.class, template.getId());
    }

    @Override
    public List<MeetingTemplate> findMeetingTemplates(QueryMyMeetingTemplateCondition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingTemplatesRecord> query = context.selectQuery(Tables.EH_MEETING_TEMPLATES);
        query.addConditions(Tables.EH_MEETING_TEMPLATES.NAMESPACE_ID.eq(condition.getNamespaceId()));
        query.addConditions(Tables.EH_MEETING_TEMPLATES.ORGANIZATION_ID.eq(condition.getOrganizationId()));
        query.addConditions(Tables.EH_MEETING_TEMPLATES.USER_ID.eq(condition.getUserId()));
        query.addOrderBy(Tables.EH_MEETING_TEMPLATES.CREATE_TIME.desc());
        if (condition.getOffset() != null && condition.getPageSize() != null) {
            query.addLimit(condition.getOffset(), condition.getPageSize());
        }
        List<MeetingTemplate> results = query.fetch().map(new DefaultRecordMapper(Tables.EH_MEETING_TEMPLATES.recordType(), MeetingTemplate.class));
        if (results == null || results.isEmpty()) {
            return new ArrayList<>();
        }
        return results;
    }

    @Override
    public List<MeetingInvitationTemplate> findMeetingInvitationTemplates(Integer namespaceId, Long organizationId, Long meetingTemplateId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeetingInvitationTemplatesRecord> query = context.selectQuery(Tables.EH_MEETING_INVITATION_TEMPLATES);
        query.addConditions(Tables.EH_MEETING_INVITATION_TEMPLATES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_MEETING_INVITATION_TEMPLATES.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_MEETING_INVITATION_TEMPLATES.MEETING_TEMPLATE_ID.eq(meetingTemplateId));

        List<MeetingInvitationTemplate> results = query.fetch().map(new DefaultRecordMapper(Tables.EH_MEETING_INVITATION_TEMPLATES.recordType(), MeetingInvitationTemplate.class));
        if (results == null || results.isEmpty()) {
            return new ArrayList<>();
        }
        return results;
    }

    @Override
    public void deleteMeetingInvitationTemplates(Integer namespaceId, Long organizationId, Long meetingTemplateId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingInvitationTemplates.class));
        DeleteQuery<EhMeetingInvitationTemplatesRecord> query = context.deleteQuery(Tables.EH_MEETING_INVITATION_TEMPLATES);
        query.addConditions(Tables.EH_MEETING_INVITATION_TEMPLATES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_MEETING_INVITATION_TEMPLATES.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_MEETING_INVITATION_TEMPLATES.MEETING_TEMPLATE_ID.eq(meetingTemplateId));
        query.execute();
    }

    @Override
    public void batchCreateMeetingInvitationTemplate(List<MeetingInvitationTemplate> invitationTemplates) {
        if (CollectionUtils.isEmpty(invitationTemplates)) {
            return;
        }
        long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhMeetingInvitationTemplates.class), invitationTemplates.size());
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        List<EhMeetingInvitationTemplates> ehMeetingInvitationTemplates = new ArrayList<>();
        for (MeetingInvitationTemplate template : invitationTemplates) {
            EhMeetingInvitationTemplates ehMeetingInvitationTemplate = ConvertHelper.convert(template, EhMeetingInvitationTemplates.class);
            ehMeetingInvitationTemplate.setId(id++);
            ehMeetingInvitationTemplate.setCreatorUid(UserContext.currentUserId());
            ehMeetingInvitationTemplate.setCreateTime(now);
            ehMeetingInvitationTemplate.setOperatorUid(UserContext.currentUserId());
            ehMeetingInvitationTemplate.setOperateTime(now);
            ehMeetingInvitationTemplates.add(ehMeetingInvitationTemplate);
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingInvitationTemplates.class));
        EhMeetingInvitationTemplatesDao dao = new EhMeetingInvitationTemplatesDao(context.configuration());
        dao.insert(ehMeetingInvitationTemplates);
    }

    @Override
    public void batchDeleteMeetingInvitationTemplate(List<MeetingInvitationTemplate> invitationTemplates) {
        if (CollectionUtils.isEmpty(invitationTemplates)) {
            return;
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhMeetingInvitationTemplates.class));
        EhMeetingInvitationTemplatesDao dao = new EhMeetingInvitationTemplatesDao(context.configuration());
        dao.delete(invitationTemplates.stream().map(r -> {
            return ConvertHelper.convert(r, EhMeetingInvitationTemplates.class);
        }).collect(Collectors.toList()));
    }
}
