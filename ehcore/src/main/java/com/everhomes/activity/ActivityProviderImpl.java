// @formatter:off
package com.everhomes.activity;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.organization.OfficialFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhActivitiesDao;
import com.everhomes.server.schema.tables.daos.EhActivityAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhActivityCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhActivityGoodsDao;
import com.everhomes.server.schema.tables.daos.EhActivityRosterDao;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhActivityAttachments;
import com.everhomes.server.schema.tables.pojos.EhActivityCategories;
import com.everhomes.server.schema.tables.pojos.EhActivityGoods;
import com.everhomes.server.schema.tables.pojos.EhActivityRoster;
import com.everhomes.server.schema.tables.records.EhActivitiesRecord;
import com.everhomes.server.schema.tables.records.EhActivityAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhActivityCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhActivityGoodsRecord;
import com.everhomes.server.schema.tables.records.EhActivityRosterRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import com.everhomes.util.RecordHelper;
import com.everhomes.util.RuntimeErrorException;
import com.mysql.jdbc.StringUtils;

@Component
public class ActivityProviderImpl implements ActivityProivider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private GroupProvider groupProvider;

    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Override
    public void createActity(Activity activity) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivities.class));
        if(activity.getUuid() == null) {
            activity.setUuid(UUID.randomUUID().toString());
        }
        if (activity.getOfficialFlag() == null) {
			activity.setOfficialFlag(OfficialFlag.NO.getCode());
		}
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        activity.setUpdateTime(activity.getCreateTime());
        EhActivitiesDao dao = new EhActivitiesDao(context.configuration());
        dao.insert(activity);
    }

    @Cacheable(value = "findActivityById", key = "#id",unless="#result==null")
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
    @Caching(evict = { @CacheEvict(value="findRosterByUidAndActivityId",key="{#activity.id,#uid}")})
    @Override
    public ActivityRoster cancelSignup(Activity activity, Long uid, Long familyId) {
        ActivityRoster[] rosters = new ActivityRoster[1];
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivities.class,activity.getId()));
        cxt.select().from(Tables.EH_ACTIVITY_ROSTER)
                .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activity.getId()))
                .and(Tables.EH_ACTIVITY_ROSTER.UID.eq(uid)).fetch().forEach(item -> {
                    rosters[0] = ConvertHelper.convert(item, ActivityRoster.class);
                });

        if (rosters[0] == null) {
            LOGGER.error("cannot find the activity roster record");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid operation.the user is not signup");
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivities.class,
                rosters[0].getActivityId()));
        if (CheckInStatus.UN_CHECKIN.getCode().equals(rosters[0].getCheckinFlag())||rosters[0].getCheckinFlag()==null) {
            LOGGER.warn("the user does not signin,can cancel the operation");
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
	            EhActivityRosterDao dao=new EhActivityRosterDao(context.configuration());
	            dao.delete(rosters[0]);
	            // decrease count
	            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()
	                    - (rosters[0].getAdultCount() + rosters[0].getChildCount()));
	            if (familyId != null)
	                activity.setSignupFamilyCount(activity.getSignupFamilyCount() - 1);
	            ActivityProivider self = PlatformContext.getComponent(ActivityProivider.class);
	            self.updateActivity(activity);
	            // update dao and push event
	            DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivities.class, activity.getId());
	            
	            return null;
            });
            return rosters[0];
        }
        LOGGER.error("the user was checkin,cannot cancel operation.activityId={},uid={}", activity.getId(), uid);
        throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                ActivityServiceErrorCode.ERROR_INVILID_OPERATION, "invalid operation.the user is checkin,cannot cancel");
    }

    @Caching(evict={@CacheEvict(value="findActivityById",key="#activity.id")})
    @Override
    public void updateActivity(Activity activity) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivities.class, activity.getId()));
        EhActivitiesDao dao = new EhActivitiesDao(context.configuration());
        activity.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(activity);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivities.class, activity.getId());
    }

    @Override
    public ActivityRoster checkIn(Activity activity, Long uid, Long familyId) {

        ActivityRoster[] activityRosters = new ActivityRoster[1];
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivities.class,activity.getId()));
        context.select().from(Tables.EH_ACTIVITY_ROSTER)
                    .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activity.getId()))
                    .and(Tables.EH_ACTIVITY_ROSTER.UID.eq(uid)).fetch().forEach(roster -> {
                        activityRosters[0] = ConvertHelper.convert(roster, ActivityRoster.class);
                        return;
                    });
              
        if (activityRosters[0] == null) {
            // TODO internal error
            LOGGER.error("can not find the roster");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER, "cannot find roster");

        }
        if (CheckInStatus.CHECKIN.getCode().equals(activityRosters[0].getCheckinFlag())) {
            // concurrent?
            LOGGER.warn("the user is checkin before");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_CHECKIN_BEFORE, "the user is checkin before");
        }
        activityRosters[0].setCheckinFlag(CheckInStatus.CHECKIN.getCode());
        activityRosters[0].setCheckinUid(uid);
        ActivityProivider self = PlatformContext.getComponent(ActivityProivider.class);
        self.updateRoster(activityRosters[0]);
        activity.setCheckinAttendeeCount(activity.getCheckinAttendeeCount()
                + (activityRosters[0].getAdultCount() + activityRosters[0].getChildCount()));
        if (familyId != null)
            activity.setCheckinFamilyCount(activity.getConfirmAttendeeCount() + 1);
        //special method
        self.updateActivity(activity);
        return activityRosters[0];

    }

    @Override
    public void createActivityRoster(ActivityRoster createRoster) {
        Long id = shardingProvider.allocShardableContentId(EhActivityRoster.class).second();
        if(createRoster.getUuid() == null) {
            createRoster.setUuid(UUID.randomUUID().toString());
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivities.class, createRoster.getActivityId()));
        EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
        createRoster.setId(id);
        createRoster.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(createRoster);
    }

    @Caching(evict = { @CacheEvict(value="findRosterByUidAndActivityId",key="{#roster.activityId,#roster.uid}"),@CacheEvict(value="findRosterById",key="#roster.id")})
    @Override
    public void updateRoster(ActivityRoster roster) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivities.class, roster.getActivityId()));
        EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
        dao.update(roster);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivityRoster.class, roster.getId());

    }

    @Cacheable(value = "findRosterById", key = "#rosterId")
    @Override
    public ActivityRoster findRosterById(Long rosterId) {
        ActivityRoster[] rosters=new ActivityRoster[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class), null, (context,obj)->{
            EhActivityRosterDao dao = new EhActivityRosterDao(context.configuration());
            EhActivityRoster result = dao.findById(rosterId);
            if(result!=null){
                rosters[0]=ConvertHelper.convert(result,ActivityRoster.class);
                return false;
            }
            return true;
        });
        return rosters[0];
      
    }

    @Cacheable(value = "findRosterByUidAndActivityId", key = "{#activityId,#uid}",unless="#result==null")
    @Override
    public ActivityRoster findRosterByUidAndActivityId(Long activityId, Long uid) {
        ActivityRoster[] rosters = new ActivityRoster[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),null,
                (context, obj) -> {
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

    @Override
    public List<ActivityRoster> listRosterPagination(CrossShardListingLocator locator, int  pageSize, Long activityId) {
       return listInvitationsByConditions(locator,pageSize,Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId));
    }
    
    
    
    private List<ActivityRoster> listInvitationsByConditions(CrossShardListingLocator locator, int count,
            Condition... conditons) {
        List<ActivityRoster> rosters = new ArrayList<ActivityRoster>();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhActivities.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
           SelectQuery<EhActivityRosterRecord> query = context.selectQuery(Tables.EH_ACTIVITY_ROSTER);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_ACTIVITY_ROSTER.ID.gt(locator.getAnchor()));
            if (conditons != null) {
                query.addConditions(conditons);
            }
            
            query.addOrderBy(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.asc());
            query.addLimit(count - rosters.size());

            query.fetch().map((r) -> {
                rosters.add(ConvertHelper.convert(r, ActivityRoster.class));
                return null;
            });

            if (rosters.size() >= count) {
                locator.setAnchor(rosters.get(rosters.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if (rosters.size() > 0) {
            locator.setAnchor(rosters.get(rosters.size() - 1).getId());
        }

        return rosters;
    }

    @Override
    public Activity findSnapshotByPostId(Long postId) {
        Activity[] activity = new Activity[1];
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

    @Caching(evict={@CacheEvict(value="findRosterById",key="#createRoster.id"),@CacheEvict(value="findRosterByUidAndActivityId",key="{#createRoster.activityId,#createRoster.uid}")})
    @Override
    public void deleteRoster(ActivityRoster createRoster) {
        DSLContext cxt = dbProvider
                .getDslContext(AccessSpec.readOnlyWith(EhActivities.class, createRoster.getId()));
        EhActivityRosterDao dao = new EhActivityRosterDao(cxt.configuration());
        dao.delete(createRoster);
    }

    @Override
    public List<ActivityRoster> listRosters(Long activityId) {
        List<ActivityRoster> rosters=new ArrayList<ActivityRoster>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class,activityId),null,
                (context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId)).fetch().forEach(item -> {
                                rosters.add(ConvertHelper.convert(item, ActivityRoster.class));
                            });
                    return true;
                });
        return rosters;
    }
    
    @Override
    public List<Activity> listNewActivities(CrossShardListingLocator locator, int count, Timestamp lastViewedTime, Condition condition) {
    	List<Activity> activities = new ArrayList<Activity>();
    	List<Long> ids = new ArrayList<Long>();
    	
    	if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhActivities.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
    	
    	this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhActivitiesRecord> query = context.selectQuery(Tables.EH_ACTIVITIES);
            
            if (lastViewedTime != null){
            	query.addConditions(Tables.EH_ACTIVITIES.CREATE_TIME.gt(lastViewedTime));
            }
            
            if(condition != null){
                query.addConditions(condition);
            }

            query.addConditions(Tables.EH_ACTIVITIES.STATUS.eq((byte) 2));
            query.addOrderBy(Tables.EH_ACTIVITIES.CREATE_TIME.desc());
            query.addLimit(count - activities.size());
            
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("listNewActivities, sql=" + query.getSQL());
                LOGGER.debug("listNewActivities, bindValues=" + query.getBindValues());
            }

            query.fetch().map((r) -> {
            	if(r != null) {
            		ids.add(r.getId());
            		return activities.add(ConvertHelper.convert(r, Activity.class));
            	}
				return null;
            });

            return AfterAction.next;
        });
    
    	userActivityProvider.updateViewedActivityProfileIfNotExist(UserContext.current().getUser().getId(), 
    			UserProfileContstant.VIEWED_ACTIVITY_NEW, System.currentTimeMillis(), ids);
    	return activities;
    }

    @Override
    public List<Activity> listActivities(CrossShardListingLocator locator, int count, Condition condition, Boolean orderByCreateTime) {
    	
    	//按时间排序 用offset方式替代原有anchor modified by xiongying 20160707
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	
    	List<Activity> activities=new ArrayList<Activity>();
        List<Activity> overdue =new ArrayList<Activity>();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        SelectQuery<EhActivitiesRecord> query = context.selectQuery(Tables.EH_ACTIVITIES);
        long pageOffset = 0L;
        if (locator.getAnchor() == null || locator.getAnchor() == 0L){
        	locator.setAnchor(0L);
        	pageOffset = 1L;
//            	query.addConditions(Tables.EH_ACTIVITIES.ID.gt(locator.getAnchor()));
        }
        if(locator.getAnchor() != 0L){
        	pageOffset = locator.getAnchor();
//            	query.addConditions(Tables.EH_ACTIVITIES.ID.lt(locator.getAnchor()));
        }
        
        if(condition != null){
            query.addConditions(condition);
        }
        Integer offset =  (int) ((pageOffset - 1 ) * (count-1));
        query.addConditions(Tables.EH_ACTIVITIES.STATUS.eq((byte) 2));

        if(orderByCreateTime) {
            query.addOrderBy(Tables.EH_ACTIVITIES.CREATE_TIME.desc());
        } else {
        	// 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
//          query.addOrderBy(Tables.EH_ACTIVITIES.END_TIME.desc());
        	query.addOrderBy(Tables.EH_ACTIVITIES.START_TIME.desc());
        }

//            query.addLimit(count - activities.size());
        query.addLimit(offset, count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query activities by geohash, sql=" + query.getSQL());
            LOGGER.debug("Query activities by geohash, bindValues=" + query.getBindValues());
        }
        
        // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        activities = query.fetch().map(r->ConvertHelper.convert(r, Activity.class));
       
//        query.fetch().map((r) -> {
//        	if(r.getEndTime().after(now)){
//        		activities.add(ConvertHelper.convert(r, Activity.class));
//        	}
//        	else
//        		overdue.add(ConvertHelper.convert(r, Activity.class));
//            return null;
//        });
//
//        activities.addAll(overdue);
        if (activities.size() >= count) {
            locator.setAnchor(pageOffset+1);
        }else {
        	locator.setAnchor(null);
		}

        if(activities.size()>=count){
            return activities.subList(0, count-1);
        }
        return activities;
    }

    @Override
    public Activity findActivityByUuid(String uuid) {
        Activity[] activities=new Activity[0];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class), null,(context,objc)->{
            return true;
        });
        return activities[0];
    }

	@Override
	public List<ActivityRoster> findRostersByUid(Long uid, CrossShardListingLocator locator, int count) {
		List<ActivityRoster> rosters = new ArrayList<ActivityRoster>();
		
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhActivities.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
           SelectQuery<EhActivityRosterRecord> query = context.selectQuery(Tables.EH_ACTIVITY_ROSTER);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.ge(new Timestamp(locator.getAnchor())));
            
            query.addConditions(Tables.EH_ACTIVITY_ROSTER.UID.eq(uid));
            
            query.addOrderBy(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.asc());
            query.addLimit(count - rosters.size());

            query.fetch().map((r) -> {
                rosters.add(ConvertHelper.convert(r, ActivityRoster.class));
                return null;
            });

            return AfterAction.next;
        });

        return rosters;
	}

	@Override
	public List<Activity> listActivitiesForWarning(Integer namespaceId, Timestamp queryStartTime, Timestamp queryEndTime) {
		// 1 2 3 4 5
		return dbProvider.getDslContext(AccessSpec.readOnly())
			.select()
			.from(Tables.EH_ACTIVITIES)
			.where(Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_ACTIVITIES.STATUS.eq((byte) 2))
			.and(Tables.EH_ACTIVITIES.START_TIME.gt(queryStartTime))
			.and(Tables.EH_ACTIVITIES.START_TIME.le(queryEndTime))
			.fetch()
			.map(r->ConvertHelper.convert(r, Activity.class));
	}

	@Override
	public List<ActivityCategories> listActivityEntryCategories(Integer namespaceId, String ownerType, Long ownerId, 
			Long parentId, CategoryAdminStatus status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        List<ActivityCategories> result = new ArrayList<ActivityCategories>();
        
        SelectQuery<EhActivityCategoriesRecord> query = context.selectQuery(Tables.EH_ACTIVITY_CATEGORIES);
        Condition condition = null;
        
        if(parentId != null)
            condition = Tables.EH_ACTIVITY_CATEGORIES.PARENT_ID.eq(parentId);
        else
            condition = Tables.EH_ACTIVITY_CATEGORIES.PARENT_ID.isNull().or(Tables.EH_ACTIVITY_CATEGORIES.PARENT_ID.eq(0L));
            
        if(status != null)
            condition = condition.and(Tables.EH_ACTIVITY_CATEGORIES.STATUS.eq(status.getCode()));
        
        if(!StringUtils.isNullOrEmpty(ownerType))
        	condition = condition.and(Tables.EH_ACTIVITY_CATEGORIES.OWNER_TYPE.eq(ownerType));
        
        if(ownerId != null)
        	condition = condition.and(Tables.EH_ACTIVITY_CATEGORIES.OWNER_ID.eq(ownerId));

        condition = condition.and(Tables.EH_ACTIVITY_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        if(condition != null) {
        	query.addConditions(condition);
        }
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listActivityEntryCategories, namespaceId=" + namespaceId + ", ownerType=" + ownerType + ", ownerId=" + ownerId + ", parentId=" + parentId + ", status=" + status);
        }

        query.fetch().map((EhActivityCategoriesRecord record) -> {
        	result.add(ConvertHelper.convert(record, ActivityCategories.class));
            return null;
        });
        
        return result;
	}

    @Override
    public ActivityCategories findActivityCategoriesById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityCategories.class, id));
        EhActivityCategoriesDao dao = new EhActivityCategoriesDao(context.configuration());
        com.everhomes.server.schema.tables.pojos.EhActivityCategories result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, ActivityCategories.class);
    }

    @Override
    public void createActivityAttachment(ActivityAttachment attachment) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhActivityAttachments.class));

        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createActivityAttachment: " + attachment);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityAttachments.class, id));
        EhActivityAttachmentsDao dao = new EhActivityAttachmentsDao(context.configuration());
        dao.insert(attachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhActivityAttachments.class, null);
    }

    @Override
    public void updateActivityAttachment(ActivityAttachment attachment) {
        assert(attachment.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityAttachments.class, attachment.getId()));
        EhActivityAttachmentsDao dao = new EhActivityAttachmentsDao(context.configuration());
        dao.update(attachment);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivityAttachments.class, attachment.getId());
    }

    @Override
    public ActivityAttachment findByActivityAttachmentId(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityAttachments.class, id));
        EhActivityAttachmentsDao dao = new EhActivityAttachmentsDao(context.configuration());
        EhActivityAttachments result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, ActivityAttachment.class);
    }

    @Override
    public void deleteActivityAttachment(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhActivityAttachmentsDao dao = new EhActivityAttachmentsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public List<ActivityAttachment> listActivityAttachments(CrossShardListingLocator locator, int count, Long activityId) {
        List<ActivityAttachment> attachments = new ArrayList<ActivityAttachment>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhActivityAttachments.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhActivityAttachmentsRecord> query = context.selectQuery(Tables.EH_ACTIVITY_ATTACHMENTS);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_ACTIVITY_ATTACHMENTS.ID.gt(locator.getAnchor()));

            query.addConditions(Tables.EH_ACTIVITY_ATTACHMENTS.ACTIVITY_ID.eq(activityId));

            query.addOrderBy(Tables.EH_ACTIVITY_ATTACHMENTS.ID.asc());
            query.addLimit(count - attachments.size());

            query.fetch().map((r) -> {
                attachments.add(ConvertHelper.convert(r, ActivityAttachment.class));
                return null;
            });

            return AfterAction.next;
        });

        return attachments;
    }

    @Override
    public boolean existActivityAttachments(Long activityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityAttachments.class));
        List<ActivityAttachment> attachments = new ArrayList<ActivityAttachment>();

        SelectQuery<EhActivityAttachmentsRecord> query = context.selectQuery(Tables.EH_ACTIVITY_ATTACHMENTS);

        query.addConditions(Tables.EH_ACTIVITY_ATTACHMENTS.ACTIVITY_ID.eq(activityId));

        query.fetch().map((r) -> {
            attachments.add(ConvertHelper.convert(r, ActivityAttachment.class));
            return null;
        });

        if(attachments.size() > 0) {
            return true;
        }

        return false;
    }

    @Override
    public void createActivityGoods(ActivityGoods goods) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhActivityGoods.class));

        goods.setId(id);
        goods.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createActivityGoods: " + goods);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityGoods.class, id));
        EhActivityGoodsDao dao = new EhActivityGoodsDao(context.configuration());
        dao.insert(goods);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhActivityGoods.class, null);
    }

    @Override
    public void updateActivityGoods(ActivityGoods goods) {
        assert(goods.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityGoods.class, goods.getId()));
        EhActivityGoodsDao dao = new EhActivityGoodsDao(context.configuration());
        dao.update(goods);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivityGoods.class, goods.getId());
    }

    @Override
    public void deleteActivityGoods(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhActivityGoodsDao dao = new EhActivityGoodsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public ActivityGoods findActivityGoodsById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityGoods.class, id));
        EhActivityGoodsDao dao = new EhActivityGoodsDao(context.configuration());
        EhActivityGoods result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, ActivityGoods.class);
    }

    @Override
    public List<ActivityGoods> listActivityGoods(CrossShardListingLocator locator, int count, Long activityId) {
        List<ActivityGoods> goods = new ArrayList<ActivityGoods>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhActivityGoods.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhActivityGoodsRecord> query = context.selectQuery(Tables.EH_ACTIVITY_GOODS);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_ACTIVITY_GOODS.ID.ge(locator.getAnchor()));

            query.addConditions(Tables.EH_ACTIVITY_GOODS.ACTIVITY_ID.eq(activityId));

            query.addOrderBy(Tables.EH_ACTIVITY_GOODS.ID.asc());
            query.addLimit(count - goods.size());

            query.fetch().map((r) -> {
                goods.add(ConvertHelper.convert(r, ActivityGoods.class));
                return null;
            });

            return AfterAction.next;
        });

        return goods;
    }

	@Override
	public List<ActivityCategories> listActivityCategory(Integer namespaceId, Long categoryId) {
		if (categoryId == null) {
			categoryId = 0L;
		}
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityCategories.class));
		
		return context.select()
			.from(Tables.EH_ACTIVITY_CATEGORIES)
			.where(Tables.EH_ACTIVITY_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_ACTIVITY_CATEGORIES.PARENT_ID.eq(categoryId))
			.and(Tables.EH_ACTIVITY_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.and(Tables.EH_ACTIVITY_CATEGORIES.ENABLED.eq(TrueOrFalseFlag.TRUE.getCode()))
			.orderBy(Tables.EH_ACTIVITY_CATEGORIES.ALL_FLAG.desc(), Tables.EH_ACTIVITY_CATEGORIES.DEFAULT_ORDER.asc(), Tables.EH_ACTIVITY_CATEGORIES.ID.asc())
			.fetch()
			.map(r->ConvertHelper.convert(r, ActivityCategories.class));
	}

	/**
	 * 金地取数据使用
	 */
	@Override
	public List<Activity> listActivityByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_ACTIVITIES)
			.where(Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_ACTIVITIES.UPDATE_TIME.eq(new Timestamp(timestamp)))
			.and(Tables.EH_ACTIVITIES.ID.gt(pageAnchor))
			.orderBy(Tables.EH_ACTIVITIES.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, Activity.class));
		}
		return new ArrayList<Activity>();
	}

	/**
	 * 金地取数据使用
	 */
	@Override
	public List<Activity> listActivityByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_ACTIVITIES)
			.where(Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_ACTIVITIES.UPDATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_ACTIVITIES.UPDATE_TIME.asc(), Tables.EH_ACTIVITIES.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, Activity.class));
		}
		return new ArrayList<Activity>();
	}

	/**
	 * 金地取数据使用
	 */
	@Override
	public List<ActivityRoster> listActivitySignupByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
			Long pageAnchor, int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_ACTIVITY_ROSTER)
			.join(Tables.EH_ACTIVITIES)
			.on(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(Tables.EH_ACTIVITIES.ID))
			.and(Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId))
			.where(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.eq(new Timestamp(timestamp)))
			.and(Tables.EH_ACTIVITY_ROSTER.ID.gt(pageAnchor))
			.orderBy(Tables.EH_ACTIVITY_ROSTER.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, ActivityRoster.class));
		}
		return new ArrayList<ActivityRoster>();
	}

	/**
	 * 金地取数据使用
	 */
	@Override
	public List<ActivityRoster> listActivitySignupByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_ACTIVITY_ROSTER)
			.join(Tables.EH_ACTIVITIES)
			.on(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(Tables.EH_ACTIVITIES.ID))
			.and(Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId))
			.where(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.asc(), Tables.EH_ACTIVITY_ROSTER.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, ActivityRoster.class));
		}
		return new ArrayList<ActivityRoster>();
	}

	@Override
	public List<ActivityRoster> listActivityRoster(Long activityId, Long pageAnchor, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select().from(Tables.EH_ACTIVITY_ROSTER)
		.where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId))
		.and(pageAnchor==null?DSL.trueCondition():Tables.EH_ACTIVITY_ROSTER.ID.gt(pageAnchor))
		.orderBy(Tables.EH_ACTIVITY_ROSTER.ID.asc())
		.limit(pageSize)
		.fetch()
		.map(r->ConvertHelper.convert(r, ActivityRoster.class));
	}
	
}
