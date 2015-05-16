package com.everhomes.activity;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhActivitiesDao;
import com.everhomes.server.schema.tables.daos.EhActivityRosterDao;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhActivityRoster;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class ActivityProviderImpl implements ActivityProivider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private GroupProvider groupProvider;

    @Override
    public void createActity(Activity activity) {
        Long id = shardingProvider.allocShardableContentId(EhActivities.class).second();
        activity.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivities.class));
        EhActivitiesDao dao = new EhActivitiesDao(context.configuration());
        dao.insert(activity);
    }

    @Cacheable(value = "findActivityById", key = "#id")
    @Override
    public Activity findActivityById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivities.class, id));
        EhActivitiesDao dao = new EhActivitiesDao(context.configuration());
        EhActivities result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, Activity.class);
    }

    @Override
    public ActivityRoster cancelSignup(Activity activity, Long uid) {
        ActivityRoster[] rosters = new ActivityRoster[1];
        dbProvider.mapReduce(
                AccessSpec.readOnlyWith(EhActivityRoster.class),
                rosters,
                (context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activity.getId()))
                            .and(Tables.EH_ACTIVITY_ROSTER.UID.eq(uid)).fetch().forEach(item -> {
                                rosters[0] = ConvertHelper.convert(item, ActivityRoster.class);
                            });
                    if (rosters[0] != null) {
                        return false;
                    }
                    return true;
                });

        if (rosters[0] == null) {
            LOGGER.error("cannot find the activity roster record");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ID, "invalid operation.the user is not signup");
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityRoster.class,
                rosters[0].getId()));
        if (CheckInStatus.UN_CHECKIN.getCode().equals(rosters[0].getCheckinFlag())) {
            LOGGER.warn("the user does not signin,can cancel the operation");
            context.delete(Tables.EH_ACTIVITY_ROSTER).where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activity.getId()))
                    .and(Tables.EH_ACTIVITY_ROSTER.UID.eq(uid))
                    .and(Tables.EH_ACTIVITY_ROSTER.CHECKIN_FLAG.eq(rosters[0].getCheckinFlag()));
            // decrease count
            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()
                    - (rosters[0].getAdultCount() + rosters[0].getChildCount()));
            activity.setSignupFamilyCount(activity.getSignupFamilyCount() - 1);
            updateActivity(activity);
            // update dao and push event
            DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivities.class, activity.getId());
            // unsubscribe?
            return rosters[0];
        }
        LOGGER.error("the user was signin,cannot cancel operation.activityId={},uid={}", activity.getId(), uid);
        throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                ActivityServiceErrorCode.INVILID_OPERATION, "invalid operation.the user is not signup");
    }

    @Override
    public void updateActivity(Activity activity) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivities.class, activity.getId()));
        EhActivitiesDao dao = new EhActivitiesDao(context.configuration());
        dao.update(activity);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivities.class, activity.getId());
    }

    @Cacheable(value = "checkIn", key = "{#activity.id,#familyId}")
    @Override
    public void checkIn(Activity activity, Long uid, Long familyId) {

        ActivityRoster[] activityRosters = new ActivityRoster[1];
        dbProvider.mapReduce(
                AccessSpec.readOnlyWith(Void.class),
                activityRosters,
                (context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activity.getId()))
                            .and(Tables.EH_ACTIVITY_ROSTER.UID.eq(uid)).fetch().forEach(roster -> {
                                activityRosters[0] = ConvertHelper.convert(roster, ActivityRoster.class);
                            });
                    if (activityRosters[0] != null)
                        return false;
                    return true;
                });
        if (activityRosters[0] == null) {
            // TODO internal error
            LOGGER.error("can not find the roster");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.INVALID_ACTIVITY_ROSTER, "cannot find roster");

        }
        if (CheckInStatus.CHECKIN.getCode().equals(activityRosters[0].getCheckinFlag())) {
            // concurrent?
            LOGGER.warn("can not find the roster");
            return;
        }
        activityRosters[0].setCheckinFlag(CheckInStatus.CHECKIN.getCode());
        activityRosters[0].setCheckinUid(uid);
        updateRoster(activityRosters[0]);
        activity.setCheckinAttendeeCount(activity.getCheckinAttendeeCount()
                + (activityRosters[0].getAdultCount() + activityRosters[0].getChildCount()));
        activity.setCheckinFamilyCount(activity.getConfirmAttendeeCount() + 1);
        updateActivity(activity);

    }

    @Override
    public void createActivityRoster(ActivityRoster createRoster) {
        Long id = shardingProvider.allocShardableContentId(EhActivityRoster.class).second();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityRoster.class, id));
        EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
        createRoster.setId(id);
        dao.insert(createRoster);
    }

    @Override
    public void updateRoster(ActivityRoster roster) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityRoster.class, roster.getId()));
        EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
        dao.update(roster);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivityRoster.class, roster.getId());

    }

    @Cacheable(value = "findRosterById", key = "#rosterId")
    @Override
    public ActivityRoster findRosterById(Long rosterId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityRoster.class, rosterId));
        EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
        EhActivityRoster result = dao.findById(rosterId);
        return result == null ? null : ConvertHelper.convert(result, ActivityRoster.class);
    }

    @Cacheable(value = "findRosterByUidAndActivityId", key = "{#activityId,#uid}")
    @Override
    public ActivityRoster findRosterByUidAndActivityId(Long activityId, Long uid) {
        ActivityRoster[] rosters = new ActivityRoster[0];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivityRoster.class),rosters,(context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId))
                            .and(Tables.EH_ACTIVITY_ROSTER.UID.eq(uid)).fetch().forEach(item -> {
                                rosters[0] = ConvertHelper.convert(item, ActivityRoster.class);
                            });
                    if (rosters[0] != null)
                        return false;
                    return true;
                });
        return rosters[0];
    }

    @Cacheable(value = "listRosterPagination", key = "{#pageOffset,#pageSize,#activityId}")
    @Override
    public List<ActivityRoster> listRosterPagination(Long pageOffset, Long pageSize, Long activityId) {
        List<ActivityRoster> rosters = new ArrayList<ActivityRoster>(pageSize.intValue());
        if (pageOffset == null) {
            dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivityRoster.class), null, (context, obj) -> {
                EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
                dao.fetchByActivityId(activityId).forEach(roster -> {
                    if (rosters.size() < pageSize)
                        rosters.add(ConvertHelper.convert(roster, ActivityRoster.class));
                });
                if (pageSize == rosters.size()) {
                    return false;
                }
                return true;
            });
            return rosters;
        }
        // find all shard
        List<Long> counts = new ArrayList<Long>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivityRoster.class), null,(context, obj) -> {
                    Long count = context.selectCount().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId)).fetchOne(0, Long.class);
                    counts.add(count);
                    return true;

                });
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        Tuple<Integer, Long> shard = PaginationHelper.offsetFallsAt(counts, offset);
        if (shard.first() < 0) {
            return rosters;
        }
        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivityRoster.class), null, (context, object) -> {
            if (currentShard[0] < shard.first()) {
                currentShard[0] += 1;
                return true;
            }
            EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
            dao.fetchByActivityId(activityId).forEach(item -> {
                if (rosters.size() < pageSize)
                    rosters.add(ConvertHelper.convert(item, ActivityRoster.class));
            });
            if (rosters.size() >= pageSize) {
                return false;
            }
            return true;
        });
        return rosters;
    }

    @Override
    public Activity findSnapshotByPostId(Long postId) {
        Activity[] activity = new Activity[0];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class), null, (context, obj) -> {
            EhActivitiesDao dao = new EhActivitiesDao(context.configuration());
            dao.fetchByPostId(postId).forEach(result -> {
                activity[0] = ConvertHelper.convert(result, Activity.class);
            });
            if (activity[0] != null) {
                return false;
            }
            return true;
        });
        return activity[0];
    }
}
