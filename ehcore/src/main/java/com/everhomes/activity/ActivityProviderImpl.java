// @formatter:off
package com.everhomes.activity;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.everhomes.rest.forum.NeedTemporaryType;
import com.everhomes.rest.forum.PostCloneFlag;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import org.jooq.*;
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
import com.everhomes.rest.activity.ActivityChargeFlag;
import com.everhomes.rest.activity.ActivityRosterPayFlag;
import com.everhomes.rest.activity.ActivityRosterStatus;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.StatisticsActivityDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.organization.OfficialFlag;
import com.everhomes.rest.user.UserGender;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
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
        
        //运营要求：官方活动--如果开始时间早于当前时间，则设置创建时间为开始时间之前一天  add by yanjun
        //去除创建时间为开始时间之前一天这个设置 add by yanlong.liang 20180614
//    	if(activity.getOfficialFlag() == OfficialFlag.YES.getCode() && null != activity.getStartTime()){
//        	if(activity.getStartTime().before(DateHelper.currentGMTTime())){
//        		activity.setCreateTime(new Timestamp(activity.getStartTime().getTime() - 24*60*60*1000));
//        	}
//    	}
        
        activity.setUpdateTime(activity.getCreateTime());
        EhActivitiesDao dao = new EhActivitiesDao(context.configuration());
        dao.insert(activity);
    }

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
    
    @Caching(evict = { @CacheEvict(value="findRosterByUidAndActivityId",key="{#activity.id,#uid}"),
    		@CacheEvict(value="findRosterById", allEntries=true)})
    @Override
    public ActivityRoster cancelSignup(Activity activity, Long uid, Long familyId) {
    	long startTime = System.currentTimeMillis();
        ActivityRoster[] rosters = new ActivityRoster[1];
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivities.class,activity.getId()));
        cxt.select().from(Tables.EH_ACTIVITY_ROSTER)
                .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activity.getId()))
                .and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()))
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
            // TODO: 临时修改，后面重新整理 by yanjun 20170525
            //this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
	            EhActivityRosterDao dao=new EhActivityRosterDao(context.configuration());
	            //为了保留支付信息，取消报名后保留信息，只是把状态置为已取消。 edit by yanjun 20170504  activity-3.0.0  start 
	            rosters[0].setStatus(ActivityRosterStatus.CANCEL.getCode());
	            rosters[0].setCancelTime(new Timestamp(DateHelper.currentGMTTime().getTime()));;
	            dao.update(rosters[0]);
	            //dao.delete(rosters[0]);
	            //为了保留支付信息，取消报名后保留信息，只是把状态置为已取消。 edit by yanjun 20170504  activity-3.0.0  start 
	            
	            // decrease count
	            
	            //因为使用新规则已报名=已确认。  if条件     add by yanjun 20170503
	            //1、signup：时不需要确认的话，立刻添加到已报名人数；2、conform：添加到已报名人数；3、reject：不处理；4、cancel：如果已确认则减，如果未确认则不处理
	            if(rosters[0].getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode()){
	            	 activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()
	 	                    - (rosters[0].getAdultCount() + rosters[0].getChildCount()));
	 	            if (familyId != null)
	 	                activity.setSignupFamilyCount(activity.getSignupFamilyCount() - 1);
	            }
	            ActivityProivider self = PlatformContext.getComponent(ActivityProivider.class);

            Activity temp = findActivityById(activity.getId());

            LOGGER.warn("***************************************************** tempcount: " + temp.getSignupAttendeeCount() + "activitycount: " + activity.getSignupAttendeeCount());


            self.updateActivity(activity);

            Activity temp2 = findActivityById(activity.getId());

            LOGGER.warn("***************************************************** tempcount: " + temp2.getSignupAttendeeCount());

            if(temp2.getSignupAttendeeCount() != activity.getSignupAttendeeCount()){
                for(int i=0; i<5; i++){
                    LOGGER.warn("***************************************************** signupAttendeeCount: " + activity.getSignupAttendeeCount());
                }
            }

	            // update dao and push event
	            DaoHelper.publishDaoAction(DaoAction.MODIFY, EhActivities.class, activity.getId());
	            
	        //    return null;
            //});
	        long endTime = System.currentTimeMillis();
	        LOGGER.debug("provider cancelSignup elapse={}", (endTime - startTime));
            return rosters[0];
        } else {
	        LOGGER.error("the user was checkin,cannot cancel operation.activityId={},uid={}", activity.getId(), uid);
	        throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
	                ActivityServiceErrorCode.ERROR_INVILID_OPERATION, "invalid operation.the user is checkin,cannot cancel");
        }
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
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        Long id = this.dbProvider.allocPojoRecordId(EhActivityRoster.class);
        //Long id = shardingProvider.allocShardableContentId(EhActivityRoster.class).second();
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

    //@Cacheable(value = "findRosterByUidAndActivityId", key = "{#activityId,#uid,}",unless="#result==null")
    @Override
    public ActivityRoster findRosterByUidAndActivityId(Long activityId, Long uid, Byte status) {
        ActivityRoster[] rosters = new ActivityRoster[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),null,
                (context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId))
                            .and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(status))
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
    public ActivityRoster findRosterByPhoneAndActivityId(Long activityId, String phone, Byte status) {
        ActivityRoster[] rosters = new ActivityRoster[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),null,
                (context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId))
                            .and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(status))
                            .and(Tables.EH_ACTIVITY_ROSTER.PHONE.eq(phone)).fetch().forEach(item -> {
                                rosters[0] = ConvertHelper.convert(item, ActivityRoster.class);
                            });
                    if (rosters[0] != null)
                        return false;
                    return true;
                });
        return rosters[0];
    }
    
    @Override
    public ActivityRoster findRosterByOrderNo(String orderNo) {
        ActivityRoster[] rosters = new ActivityRoster[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),null,
                (context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()))
                            .and(Tables.EH_ACTIVITY_ROSTER.ORDER_NO.eq(orderNo)).fetch().forEach(item -> {
                                rosters[0] = ConvertHelper.convert(item, ActivityRoster.class);
                            });
                    if (rosters[0] != null)
                        return false;
                    return true;
                });
        return rosters[0];
    }

    @Override
    public ActivityRoster findRosterByPayOrderId(Long payOrderId) {
        ActivityRoster[] rosters = new ActivityRoster[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),null,
                (context, obj) -> {
                    context.select().from(Tables.EH_ACTIVITY_ROSTER)
                            .where(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()))
                            .and(Tables.EH_ACTIVITY_ROSTER.PAY_ORDER_ID.eq(payOrderId)).fetch().forEach(item -> {
                        rosters[0] = ConvertHelper.convert(item, ActivityRoster.class);
                    });
                    if (rosters[0] != null)
                        return false;
                    return true;
                });
        return rosters[0];
    }


    @Override
    public List<ActivityRoster> listRosterPagination(CrossShardListingLocator locator, int  pageSize, Long activityId, boolean onlyConfirm) {
    	Condition conditon = Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode());
    	//一般用户只查已确认的，创建者查询确认和不确认的人 add by yanjun 20170505  feature activity 3.0.0
    	if(onlyConfirm){
    		conditon = conditon.and(Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(ConfirmStatus.CONFIRMED.getCode()));
    	}
       return listInvitationsByConditions(locator,pageSize,Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId), conditon);
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
    public List<ActivityRoster> listRosters(Long activityId, ActivityRosterStatus status) {
        List<ActivityRoster> rosters=new ArrayList<ActivityRoster>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class,activityId),null,
                (context, obj) -> {
                    SelectQuery<EhActivityRosterRecord> query = context.selectQuery(Tables.EH_ACTIVITY_ROSTER);
                    query.addConditions(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId));

                    //add by yanjun 20170830
                    if(status != null){
                        query.addConditions(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(status.getCode()));
                    }
                    query.fetch().forEach(item -> {
                        rosters.add(ConvertHelper.convert(item, ActivityRoster.class));
                    });

                    return true;
                });
        return rosters;
    }
    
    @Override
    public Integer countActivityRosterByCondition(Long activityId, Condition flagCondition) {
    	final Integer[]  count = new Integer[1];
    	count[0] = 0;
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class,activityId),null,
                (context, obj) -> {
                	Condition condition = Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId);
                	if(flagCondition != null){
                		condition = condition.and(flagCondition);
                	}
                	Integer[] c = context.select(DSL.sum(Tables.EH_ACTIVITY_ROSTER.ADULT_COUNT), DSL.sum(Tables.EH_ACTIVITY_ROSTER.CHILD_COUNT)).from(Tables.EH_ACTIVITY_ROSTER).where(condition).fetchOneInto(Integer[].class);
                	if(c[0] != null && c[1] != null){
                		count[0] = c[0] + c[1];
                	}
                    return true;
                });
        return count[0];
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
    public List<Activity> listActivities(CrossShardListingLocator locator, int count, Condition condition, Boolean orderByCreateTime, Byte needTemporary) {
    	
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
        
        //新增暂存活动，后台管理员在web端要看到暂存的活动 add by yanjun 20170513
        if(needTemporary == null || needTemporary.byteValue() == NeedTemporaryType.PUBLISH.getCode()){
            query.addConditions(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));
        }else if(needTemporary.byteValue() == NeedTemporaryType.ALL.getCode()){
            query.addConditions(Tables.EH_ACTIVITIES.STATUS.in(PostStatus.ACTIVE.getCode(), PostStatus.WAITING_FOR_CONFIRMATION.getCode()));
        }else if(needTemporary.byteValue() == NeedTemporaryType.TEMPORARY.getCode()){
            query.addConditions(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.WAITING_FOR_CONFIRMATION.getCode()));
        }else {
            return null;
        }

        //排序：1、待确认， 2、正常。排序会影响性能，需要待确认状态的活动时才按这个排序  add by yanjun 20170516  
        if(needTemporary != null && needTemporary.byteValue() == 1){
        	query.addOrderBy(Tables.EH_ACTIVITIES.STATUS.asc());
        }


        //置顶的优先排序  add by yanjun 20171023    edit by yanjun 又变卦了，待确认的置顶 20180322
        query.addOrderBy(Tables.EH_ACTIVITIES.STICK_FLAG.desc());
        query.addOrderBy(Tables.EH_ACTIVITIES.STICK_TIME.desc());


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
	public List<ActivityRoster> findRostersByUid(Long uid, CrossShardListingLocator locator, int count, Byte rosterStatus) {
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

            if(rosterStatus != null){
                query.addConditions(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(rosterStatus.byteValue()));
            }

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
	public List<Activity> listActivitiesForWarning(Integer namespaceId, Long categoryId, Timestamp queryStartTime, Timestamp queryEndTime) {
		// 1 2 3 4 5
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhActivitiesRecord> query = context.selectQuery(Tables.EH_ACTIVITIES);
        query.addConditions(Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
        if(categoryId != null){
            query.addConditions(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(categoryId));
        }
        query.addConditions(Tables.EH_ACTIVITIES.STATUS.eq((byte) 2));
        query.addConditions(Tables.EH_ACTIVITIES.START_TIME.gt(queryStartTime));
        query.addConditions(Tables.EH_ACTIVITIES.START_TIME.le(queryEndTime));

        return query.fetch().map(r->ConvertHelper.convert(r, Activity.class));
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
    public ActivityCategories findActivityCategoriesByEntryId(Long entryId, Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityCategories.class));
		return context.select()
			.from(Tables.EH_ACTIVITY_CATEGORIES)
			.where(Tables.EH_ACTIVITY_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_ACTIVITY_CATEGORIES.ENTRY_ID.eq(entryId))
			.and(Tables.EH_ACTIVITY_CATEGORIES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.and(Tables.EH_ACTIVITY_CATEGORIES.ENABLED.eq(TrueOrFalseFlag.TRUE.getCode()))
			.fetchOneInto(ActivityCategories.class);
    }

    @Override
    public void createActivityCategories(ActivityCategories activityCategory) {

        if(activityCategory.getId() == null){
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhActivityCategories.class));
            activityCategory.setId(id);
        }

        activityCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createActivityCategories: " + activityCategory);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(ActivityCategories.class, activityCategory.getId()));
        EhActivityCategoriesDao dao = new EhActivityCategoriesDao(context.configuration());
        dao.insert(activityCategory);

        DaoHelper.publishDaoAction(DaoAction.CREATE, ActivityCategories.class, null);
    }

    @Override
    public void updateActivityCategories(ActivityCategories activityCategory) {

        activityCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("updateActivityCategories: " + activityCategory);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(ActivityCategories.class, activityCategory.getId()));
        EhActivityCategoriesDao dao = new EhActivityCategoriesDao(context.configuration());
        dao.update(activityCategory);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, ActivityCategories.class, null);
    }

    @Override
    public void deleteActivityCategories(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhActivityCategoriesDao dao = new EhActivityCategoriesDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public ActivityBizPayee getActivityPayee(Long categoryId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityBizPayee.class));
        SelectQuery<EhActivityBizPayeeRecord> query = context.selectQuery(Tables.EH_ACTIVITY_BIZ_PAYEE);
        if (categoryId != null) {
            query.addConditions(Tables.EH_ACTIVITY_BIZ_PAYEE.OWNER_ID.eq(categoryId));
        }
        if (namespaceId != null) {
            query.addConditions(Tables.EH_ACTIVITY_BIZ_PAYEE.NAMESPACE_ID.eq(namespaceId));
        }
        EhActivityBizPayeeRecord activityBizPayee = query.fetchOne();
        if (activityBizPayee == null) {
            return null;
        }
        return ConvertHelper.convert(activityBizPayee, ActivityBizPayee.class);
    }

    @Override
    public void CreateActivityPayee(ActivityBizPayee activityBizPayee) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhActivityBizPayee.class));
        activityBizPayee.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhActivityBizPayeeDao dao = new EhActivityBizPayeeDao(context.configuration());
        dao.insert(activityBizPayee);

        DaoHelper.publishDaoAction(DaoAction.CREATE, ActivityBizPayee.class, null);

    }

    @Override
    public void updateActivityPayee(ActivityBizPayee activityBizPayee) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhActivityBizPayeeDao dao = new EhActivityBizPayeeDao(context.configuration());
        dao.update(activityBizPayee);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, ActivityBizPayee.class, null);
    }

    @Override
    public List<ActivityRoster> listActivityRosterByOrganizationId(Long organizationId, Integer namespaceId, Long pageAnchor, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select(Tables.EH_ACTIVITY_ROSTER.fields()).from(Tables.EH_ACTIVITY_ROSTER)
                .join(Tables.EH_ORGANIZATION_MEMBERS)
                .on(Tables.EH_ACTIVITY_ROSTER.UID.eq(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID))
                .where(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId))
                .and(pageAnchor==null?DSL.trueCondition():Tables.EH_ACTIVITY_ROSTER.ID.gt(pageAnchor))
                .orderBy(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.asc(), Tables.EH_ACTIVITY_ROSTER.ID.asc())
                .limit(pageSize + 1)
                .fetch();
        if (result != null && result.isNotEmpty()) {
            return result.map(r->RecordHelper.convert(r, ActivityRoster.class));
        }
        return new ArrayList<ActivityRoster>();
    }

    @Override
    public Long findActivityCategoriesMaxEntryId(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhActivityCategories.class));
        return context.select(DSL.max(Tables.EH_ACTIVITY_CATEGORIES.ENTRY_ID))
                .from(Tables.EH_ACTIVITY_CATEGORIES)
                .where(Tables.EH_ACTIVITY_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
                .fetchOneInto(Long.class);
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
                query.addConditions(Tables.EH_ACTIVITY_GOODS.ID.gt(locator.getAnchor()));

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
			categoryId = -1L;
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
	
	@Override
	public List<ActivityRoster> listActivityRoster(Long activityId, Long excludeUserId, Integer status, Integer cancelStatus, Integer pageOffset, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		SelectQuery<EhActivityRosterRecord>  query = context.selectQuery(Tables.EH_ACTIVITY_ROSTER);
		query.addConditions(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId));
		if(status != null){
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(status.byteValue()));
		}
		
		if(excludeUserId != null){
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.UID.ne(excludeUserId));
		}
		if(cancelStatus != null){
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(cancelStatus.byteValue()));
		}
		query.addOrderBy(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.asc());
		query.addLimit(pageOffset, pageSize);
		return query.fetch().map(r->ConvertHelper.convert(r, ActivityRoster.class));
	}
	@Override
	public Integer countActivityRoster(Long activityId, Integer status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		SelectQuery<EhActivityRosterRecord>  query = context.selectQuery(Tables.EH_ACTIVITY_ROSTER);
		query.addConditions(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId));
		if(status != null){
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(status.byteValue()));
		}
		
		return query.fetchCount();
	}
	
	@Override
	public  Integer countActivity(Integer namespaceId, Long categoryId, Long contentCategoryId, Timestamp startTime, Timestamp endTime, boolean isDelete){
		final Integer[]  count = new Integer[1];
		count[0] = 0;
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),
				null, (DSLContext context, Object reducingContext) -> {
					
					Condition condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
					//活动类型、内容类型
					if(categoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(categoryId));
					}
					if(contentCategoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(contentCategoryId));
					}
					//统计时，只统计real贴和normal贴
					Condition cloneCondition = Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode());
					cloneCondition  =cloneCondition.or(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.REAL.getCode()));
					condition = condition.and(cloneCondition);
					if(isDelete){
						condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.INACTIVE.getCode()));
						if(startTime != null && endTime != null){
							condition = condition.and(Tables.EH_ACTIVITIES.DELETE_TIME.ge(startTime));
							condition = condition.and(Tables.EH_ACTIVITIES.DELETE_TIME.lt(endTime));
							
							//当前时间段内创建活动不算，防止：当天创建活动并当前取消，这时新增没有增加而取消增加了一笔
							Condition otherCondition = Tables.EH_ACTIVITIES.CREATE_TIME.lt(startTime);
							otherCondition = otherCondition.or(Tables.EH_ACTIVITIES.CREATE_TIME.ge(endTime));
							condition = condition.and(otherCondition);
						}
					}else{
						condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));
						if(startTime != null && endTime != null){
							condition = condition.and(Tables.EH_ACTIVITIES.CREATE_TIME.ge(startTime));
							condition = condition.and(Tables.EH_ACTIVITIES.CREATE_TIME.lt(endTime));
						}
					}
					
					Integer c = context.selectCount().from(Tables.EH_ACTIVITIES).where(condition).fetchOneInto(Integer.class);
					if(c != null){
						count[0] += c;
					}
					return true;
				});
		return count[0];
	}
	
	@Override
	public Integer countActivityRoster(Integer namespaceId, Long categoryId, Long contentCategoryId, Timestamp startTime, Timestamp endTime, UserGender userGender, boolean isCancel){
		final Integer[]  count = new Integer[1];
		count[0] = 0;
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),
				null, (DSLContext context, Object reducingContext) -> {
					
					Condition condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
					//活动类型、内容类型
					if(categoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(categoryId));
					}
					if(contentCategoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(contentCategoryId));
					}
					
					//不统计创建者
					condition = condition.and(Tables.EH_ACTIVITY_ROSTER.UID.ne(Tables.EH_ACTIVITIES.CREATOR_UID));
					
					//已确认、已退款（需要支付的话）
					if(isCancel){
						condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.CANCEL.getCode()));
						condition = condition.and(Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(ConfirmStatus.CONFIRMED.getCode()));
						//不用支付或者已支付
						Condition chargeCondition = Tables.EH_ACTIVITIES.CHARGE_FLAG.eq(ActivityChargeFlag.UNCHARGE.getCode());
						chargeCondition = chargeCondition.or(Tables.EH_ACTIVITY_ROSTER.PAY_FLAG.eq(ActivityRosterPayFlag.REFUND.getCode()));
						condition = condition.and(chargeCondition);
						if(startTime != null && endTime != null){
							condition = condition.and(Tables.EH_ACTIVITY_ROSTER.CANCEL_TIME.ge(startTime));
							condition = condition.and(Tables.EH_ACTIVITY_ROSTER.CANCEL_TIME.lt(endTime));
							
							//当前时间段内报名不算，防止：当天报名并当前取消，这时新增没有增加而取消增加了一笔
							Condition otherCondition = Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.lt(startTime);
							otherCondition = otherCondition.or(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.ge(endTime));
							condition = condition.and(otherCondition);
						}
						
					}else{
						condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));
						condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
						//已确认并且已支付
						condition = addConfirmAndPay(condition);
						if(startTime != null && endTime != null){
							condition = condition.and(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.ge(startTime));
							condition = condition.and(Tables.EH_ACTIVITY_ROSTER.CREATE_TIME.lt(endTime));
						}
					}
					
					//性别
					if(userGender == null){
						count[0] += context.selectCount()
								.from(Tables.EH_ACTIVITY_ROSTER)
								.join(Tables.EH_ACTIVITIES)
								.on(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(Tables.EH_ACTIVITIES.ID))
								.where(condition).fetchOneInto(Integer.class);
					}else{
						condition = condition.and(Tables.EH_USERS.GENDER.eq(userGender.getCode()));
						condition = condition.and(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(Tables.EH_ACTIVITIES.ID));
						condition = condition.and(Tables.EH_ACTIVITY_ROSTER.UID.eq(Tables.EH_USERS.ID));
						count[0] += context.selectCount()
								.from(Tables.EH_ACTIVITY_ROSTER, Tables.EH_ACTIVITIES, Tables.EH_USERS)
								.where(condition).fetchOneInto(Integer.class);
					}
					
					return true;
				});
		return count[0];
	}

	@Override
	public List<Activity> statisticsActivity(Integer namespaceId, Long categoryId, Long contentCategoryId, Long startTime, Long endTime, String tag) {

		List<Activity> list = new ArrayList<Activity>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),
				null, (DSLContext context, Object reducingContext) -> {
					Condition condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
					//活动类型、内容类型
					if(categoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(categoryId));
					}
					if(contentCategoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(contentCategoryId));
					}
                    //统计时，只统计real贴和normal贴
                    Condition cloneCondition = Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode());
                    cloneCondition  =cloneCondition.or(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.REAL.getCode()));
                    condition = condition.and(cloneCondition);

					condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));
					if(startTime != null && endTime != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CREATE_TIME.ge(new Timestamp(startTime)));
						condition = condition.and(Tables.EH_ACTIVITIES.CREATE_TIME.lt(new Timestamp(endTime)));
					}
					
					if(tag != null && !"".equals(tag)){
						condition = condition.and(Tables.EH_ACTIVITIES.TAG.eq(tag));
					}
					context.select().from(Tables.EH_ACTIVITIES).where(condition).fetch().forEach(r -> {
						list.add(ConvertHelper.convert(r, Activity.class));
						return ;
					});

					return true;
				});
		return list;
	}
	
	@Override
	public List<Object[]> statisticsRosterPay(List<Long> activityIds){
		final List<Object[]>  response = new ArrayList<Object[]>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),
				null, (DSLContext context, Object reducingContext) -> {
					
					Condition condition = Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode());
					condition = condition.and(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.in(activityIds));

					//不统计创建者
					condition = condition.and(Tables.EH_ACTIVITY_ROSTER.UID.ne(Tables.EH_ACTIVITIES.CREATOR_UID));
					
					//已确认并且已支付
					condition = addConfirmAndPay(condition);
					
					List<Object[]> list = context.select(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID, DSL.count())
							.from(Tables.EH_ACTIVITY_ROSTER)
							.join(Tables.EH_ACTIVITIES)
							.on(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(Tables.EH_ACTIVITIES.ID))
							.where(condition)
							.groupBy(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID)
							.fetchInto(Object[].class);
					
					if(list != null){
						response.addAll(list);
					}
					return true;
				});
		return response;
	}
	
	@Override
	public List<Object[]> statisticsRosterTag(Integer namespaceId, Long categoryId, Long contentCategoryId){
		final List<Object[]>  response = new ArrayList<Object[]>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),
				null, (DSLContext context, Object reducingContext) -> {
					Condition condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
					//活动类型、内容类型
					if(categoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(categoryId));
					}
					if(contentCategoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(contentCategoryId));
					}
					
					condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
					condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));

					//不统计创建者
					condition = condition.and(Tables.EH_ACTIVITY_ROSTER.UID.ne(Tables.EH_ACTIVITIES.CREATOR_UID));
					
					//已确认并且已支付
					condition = addConfirmAndPay(condition);
					
					List<Object[]> list = context.select(Tables.EH_ACTIVITIES.TAG, DSL.count())
							.from(Tables.EH_ACTIVITY_ROSTER)
							.join(Tables.EH_ACTIVITIES)
							.on(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(Tables.EH_ACTIVITIES.ID))
							.where(condition)
							.groupBy(Tables.EH_ACTIVITIES.TAG)
							.fetchInto(Object[].class);
					
					if(list != null){
						response.addAll(list);
					}
					return true;
				});
		return response;
	}
	
	@Override
	public List<Object[]> statisticsActivityTag(Integer namespaceId, Long categoryId, Long contentCategoryId){
		final List<Object[]>  response = new ArrayList<Object[]>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),
				null, (DSLContext context, Object reducingContext) -> {
					Condition condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
					//活动类型、内容类型
					if(categoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(categoryId));
					}
					if(contentCategoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(contentCategoryId));
					}
                    //统计时，只统计real贴和normal贴
                    Condition cloneCondition = Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode());
                    cloneCondition  =cloneCondition.or(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.REAL.getCode()));
                    condition = condition.and(cloneCondition);
					condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));

					List<Object[]> list = context.select(Tables.EH_ACTIVITIES.TAG, DSL.count())
							.from(Tables.EH_ACTIVITIES)
							.where(condition)
							.groupBy(Tables.EH_ACTIVITIES.TAG)
							.fetchInto(Object[].class);
					
					if(list != null){
						response.addAll(list);
					}
					return true;
				});
		return response;
	}
	
	@Override
	public List<Object[]> statisticsOrganization(Integer namespaceId, Long categoryId, Long contentCategoryId){
		final List<Object[]>  response = new ArrayList<Object[]>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhActivities.class),
				null, (DSLContext context, Object reducingContext) -> {
					Condition condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
					//活动类型、内容类型
					if(categoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(categoryId));
					}
					if(contentCategoryId != null){
						condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(contentCategoryId));
					}
                    //统计时，只统计real贴和normal贴
                    Condition cloneCondition = Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode());
                    cloneCondition  =cloneCondition.or(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.REAL.getCode()));
                    condition = condition.and(cloneCondition);
					condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
					condition = condition.and(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()));

					//不统计创建者
					condition = condition.and(Tables.EH_ACTIVITY_ROSTER.UID.ne(Tables.EH_ACTIVITIES.CREATOR_UID));
					
					//已确认并且已支付
					condition = addConfirmAndPay(condition);

					List<Object[]> list = context.select(Tables.EH_ACTIVITY_ROSTER.ORGANIZATION_ID, 
							Tables.EH_ORGANIZATIONS.NAME, 
							DSL.count(), 
							DSL.countDistinct(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID))
							.from(Tables.EH_ACTIVITY_ROSTER)
							.join(Tables.EH_ACTIVITIES)
							.on(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(Tables.EH_ACTIVITIES.ID))
							.leftOuterJoin(Tables.EH_ORGANIZATIONS)
							.on(Tables.EH_ACTIVITY_ROSTER.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID))
							.where(condition)
							.groupBy(Tables.EH_ACTIVITY_ROSTER.ORGANIZATION_ID)
							.orderBy(DSL.count().desc())
							.fetchInto(Object[].class);

					if(list != null){
						response.addAll(list);
					}
					return true;
				});

		return response;
	}

	/**
	 * 添加已确认并且已支付的条件
	 * @param condition
	 * @return
	 */
	private Condition addConfirmAndPay(Condition condition){
		//已确认
		if(condition == null){
			condition = Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(ConfirmStatus.CONFIRMED.getCode());
		}else{
			condition = condition.and(Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(ConfirmStatus.CONFIRMED.getCode()));
		}
		//不用支付或者已支付
		Condition chargeCondition = Tables.EH_ACTIVITIES.CHARGE_FLAG.eq(ActivityChargeFlag.UNCHARGE.getCode());
		chargeCondition = chargeCondition.or(Tables.EH_ACTIVITY_ROSTER.PAY_FLAG.eq(ActivityRosterPayFlag.PAY.getCode()));
		condition = condition.and(chargeCondition);
		
		return condition;
	}
	@Override
	public List<ActivityRoster> findExpireRostersByActivityId(Long activityId) {
		List<ActivityRoster> rosters = new ArrayList<ActivityRoster>();

		AccessSpec accessSpec = AccessSpec.readOnlyWith(EhActivities.class);
		ShardIterator shardIterator = new ShardIterator(accessSpec);
		
		this.dbProvider.iterationMapReduce(shardIterator, null, (context, obj) -> {
			SelectQuery<EhActivityRosterRecord> query = context.selectQuery(Tables.EH_ACTIVITY_ROSTER);
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.ACTIVITY_ID.eq(activityId));
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.ORDER_EXPIRE_TIME.lt(new Timestamp(DateHelper.currentGMTTime().getTime())));
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
			query.addConditions(Tables.EH_ACTIVITY_ROSTER.PAY_FLAG.eq(ActivityRosterPayFlag.UNPAY.getCode()));
			query.fetch().map((r) -> {
				rosters.add(ConvertHelper.convert(r, ActivityRoster.class));
				return null;
			});

			return AfterAction.next;
		});

        return rosters;
	}

	@Override
	public List<Long> listActivityIds() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		List<Long> result = context.select(Tables.EH_ACTIVITIES.ID).from(Tables.EH_ACTIVITIES)
			.where(Tables.EH_ACTIVITIES.STATUS.eq(PostStatus.ACTIVE.getCode()))
			.fetchInto(Long.class);
		return result;
	}

//    @Override
//    public void createActivityRosterError(ActivityRosterError rosterError) {
//        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhActivityRosterError.class));
//        rosterError.setId(id);
//        if(rosterError.getCreateTime() == null){
//            rosterError.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        }
//
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//        EhActivityRosterErrorDao dao = new EhActivityRosterErrorDao(context.configuration());
//        dao.insert(rosterError);
//
//        DaoHelper.publishDaoAction(DaoAction.CREATE, EhActivityRosterError.class, null);
//    }

//    @Override
//    public List<ActivityRosterError> listActivityRosterErrorByJobId(Long jobId) {
//        List<ActivityRosterError> result = new ArrayList<>();
//
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//
//        Condition condition = Tables.EH_ACTIVITY_ROSTER_ERROR.JOB_ID.eq(jobId);
//
//        SelectJoinStep<Record> query = context.select().from(Tables.EH_ACTIVITY_ROSTER_ERROR);
//        query.where(condition);
//        query.orderBy(Tables.EH_ACTIVITY_ROSTER_ERROR.ROW_NUM.asc());
//        query.fetch().map((r) -> {
//            result.add(ConvertHelper.convert(r, ActivityRosterError.class));
//            return null;
//        });
//        return result;
//    }
}
