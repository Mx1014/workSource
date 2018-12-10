package com.everhomes.quality;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.Status;
import com.everhomes.rest.quality.ExecuteGroupAndPosition;
import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.rest.quality.QualityInspectionCategoryStatus;
import com.everhomes.rest.quality.QualityInspectionTaskResult;
import com.everhomes.rest.quality.QualityInspectionTaskReviewResult;
import com.everhomes.rest.quality.QualityInspectionTaskReviewStatus;
import com.everhomes.rest.quality.QualityInspectionTaskStatus;
import com.everhomes.rest.quality.QualityStandardStatus;
import com.everhomes.rest.quality.ScoreDTO;
import com.everhomes.rest.quality.SpecificationApplyPolicy;
import com.everhomes.rest.quality.SpecificationInspectionType;
import com.everhomes.rest.quality.SpecificationScopeCode;
import com.everhomes.rest.quality.TaskCountDTO;
import com.everhomes.scheduler.QualityInspectionScheduleJob;
import com.everhomes.scheduler.QualityInspectionStatScheduleJob;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.QualityTaskSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionEvaluationFactorsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionEvaluationsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionLogsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionModelCommunityMapDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionSampleCommunityMapDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionSampleCommunitySpecificationStatDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionSampleGroupMapDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionSampleScoreStatDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionSamplesDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionSpecificationItemResultsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionSpecificationsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionStandardGroupMapDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionStandardSpecificationMapDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionStandardsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionTaskAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionTaskRecordsDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionTaskTemplatesDao;
import com.everhomes.server.schema.tables.daos.EhQualityInspectionTasksDao;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionCategories;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionEvaluationFactors;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionEvaluations;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionLogs;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionModelCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleCommunitySpecificationStat;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleGroupMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleScoreStat;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSamples;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSpecificationItemResults;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSpecifications;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandardGroupMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandardSpecificationMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandards;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskAttachments;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskRecords;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskTemplates;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTasks;
import com.everhomes.server.schema.tables.records.EhQualityInspectionCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionEvaluationFactorsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionEvaluationsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionLogsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSampleCommunityMapRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSampleCommunitySpecificationStatRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSampleGroupMapRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSampleScoreStatRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSamplesRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSpecificationItemResultsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionSpecificationsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionStandardGroupMapRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionStandardSpecificationMapRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionStandardsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionTaskAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionTaskRecordsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionTaskTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionTasksRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import com.mysql.jdbc.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



@Component
@DependsOn("platformContext")
public class QualityProviderImpl implements QualityProvider, ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityProviderImpl.class);
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private ShardingProvider shardingProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private QualityTaskSearcher taskSearcher;

	@Value("${equipment.ip}")
	private String equipmentIp;
	
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
	//@PostConstruct
	public void init() {
		String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
		LOGGER.info("================================================taskServer: " + taskServer + ", equipmentIp: " + equipmentIp);
		if(taskServer.equals(equipmentIp)) {
			this.coordinationProvider.getNamedLock(CoordinationLocks.SCHEDULE_QUALITY_TASK.getCode()).enter(()-> {
				String qualityInspectionTriggerName = "QualityInspection" + System.currentTimeMillis();
				scheduleProvider.scheduleCronJob(qualityInspectionTriggerName, qualityInspectionTriggerName,
						"0 0 0 * * ? ", QualityInspectionScheduleJob.class, null);
				return null;
			});

			String qualityInspectionStatTriggerName = "QualityInspectionStat " + System.currentTimeMillis();
			String statCorn = configurationProvider.getValue(ConfigConstants.QUALITY_STAT_CORN, "0 0 0 * * ? ");
			this.coordinationProvider.getNamedLock(CoordinationLocks.SCHEDULE_QUALITY_STAT.getCode()).enter(()-> {
				scheduleProvider.scheduleCronJob(qualityInspectionStatTriggerName, qualityInspectionStatTriggerName,
						statCorn, QualityInspectionStatScheduleJob.class, null);
				return null;
			});
		}

//		//五分钟后启动通知
//		Long notifyTime = System.currentTimeMillis() + 300000;
//		String notifyCorn = CronDateUtils.getCron(new Timestamp(notifyTime));
//		String qualityInspectionNotifyTriggerName = "QualityInspectionNotify ";
//		String qualityInspectionNotifyJobName = "QualityInspectionNotify " + System.currentTimeMillis();
//		scheduleProvider.scheduleCronJob(qualityInspectionNotifyTriggerName, qualityInspectionNotifyJobName,
//				notifyCorn, QualityInspectionTaskNotifyScheduleJob.class, null);

	}
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }

	@Override
	public void createVerificationTasks(QualityInspectionTasks task) {
		
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionTasks.class));
		
		task.setId(id);
		task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
		LOGGER.info("createVerificationTasks: " + task);
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTasks.class, id));
        EhQualityInspectionTasksDao dao = new EhQualityInspectionTasksDao(context.configuration());
        dao.insert(task);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionTasks.class, null);
		
	}

	@Override
	public void updateVerificationTasks(QualityInspectionTasks task) {

		assert(task.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTasks.class, task.getId()));
        EhQualityInspectionTasksDao dao = new EhQualityInspectionTasksDao(context.configuration());
        dao.update(task);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionTasks.class, task.getId());		
	}

	@Override
	public void deleteVerificationTasks(Long taskId) {
		 DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTasks.class));
		 EhQualityInspectionTasksDao dao = new EhQualityInspectionTasksDao(context.configuration());
		 dao.deleteById(taskId);
		
	}

	@Override
	public List<QualityInspectionTasks> listVerificationTasks(Integer offset, int count, Long ownerId, String ownerType, Long targetId, String targetType,
		Byte taskType, Long executeUid, Timestamp startDate, Timestamp endDate, Byte executeStatus, Byte reviewStatus, boolean timeCompared,
		List<Long> standardIds, Byte manualFlag, List<ExecuteGroupAndPosition> groupDtos, Integer namespaceId, String taskName, Timestamp latestUpdateTime) {
//		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class));
		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
        SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
    
//        if(locator.getAnchor() != null) {
//            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
//        }
		//总公司 分公司 改用namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.NAMESPACE_ID.eq(namespaceId));

		if(targetId != null && targetId != 0) {
        	query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID.eq(targetId));
        }
		if(!StringUtils.isNullOrEmpty(targetType)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_TYPE.eq(targetType));    	
		}
		
		if(taskType != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TASK_TYPE.eq(taskType));
		}
		
		if(startDate != null && !"".equals(startDate)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.ge(startDate));
		}
		
		if(endDate != null && !"".equals(endDate)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.le(endDate));
		}

		if(executeUid != null && executeUid != 0) {
			//fix bug change OPERATOR_ID  to EXECUTOR_ID
			Condition con = Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTOR_ID.eq(executeUid);
			con = con.and(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.CORRECT.getCode()));

			if(standardIds != null) {
				Condition con1 = Tables.EH_QUALITY_INSPECTION_TASKS.STANDARD_ID.in(standardIds);
				//con1 = con1.and(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskStatus.NONE.getCode()));
				con = con.or(con1);
			}

			if(groupDtos != null) {
				Condition con2 = Tables.EH_QUALITY_INSPECTION_TASKS.STANDARD_ID.eq(0L);
				Condition con3 = null;
				for(ExecuteGroupAndPosition group : groupDtos) {
					Condition con4 = Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_GROUP_ID.eq(group.getGroupId());
					con4 = con4.and(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_POSITION_ID.eq(group.getPositionId()));
					if(con3 == null) {
						con3 = con4;
					} else {
						con3 = con3.or(con4);
					}
				}
				if(con3 != null) {
					con2 = con2.and(con3);
				}
				con2 = con2.and(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskStatus.NONE.getCode()));
				con = con.or(con2);
			}
			query.addConditions(con);
		}

		if(executeStatus != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(executeStatus));
		}
		if(reviewStatus != null) {
			if(QualityInspectionTaskReviewStatus.NONE.equals(QualityInspectionTaskReviewStatus.fromStatus(reviewStatus)))
				query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.REVIEW_RESULT.eq(QualityInspectionTaskReviewResult.NONE.getCode()));
			if(QualityInspectionTaskReviewStatus.REVIEWED.equals(QualityInspectionTaskReviewStatus.fromStatus(reviewStatus)))
				query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.REVIEW_RESULT.ne(QualityInspectionTaskReviewResult.NONE.getCode()));
		}
		if(taskName!=null){
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TASK_NAME.like(taskName));
		}
		
		if(timeCompared) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()))
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.isNull())
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()))));
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()));
		}


		if(manualFlag != null) {
			//fix bug :byte to long ...MANUAL_FLAG.eq(manualFlag));
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.MANUAL_FLAG.eq(Long.valueOf(manualFlag)));
		}
		//query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASKS.ID.desc());
		//add   syncTime  for offline
		if (latestUpdateTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.gt(latestUpdateTime)
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_TIME.gt(latestUpdateTime))
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.PROCESS_TIME.gt(latestUpdateTime)));
		}
		// add desc  11/20
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.desc());
        //query.addLimit(count);
		query.addLimit(offset * (count-1), count);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query tasks by count, sql=" + query.getSQL());
            LOGGER.debug("Query tasks by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionTasksRecord record) -> {
        	tasks.add(ConvertHelper.convert(record, QualityInspectionTasks.class));
        	return null;
        });
        
//        if(tasks.size() > 0) {
//            locator.setAnchor(tasks.get(tasks.size() -1).getId());
//        }
        
		return tasks;
	}

	@Override
	public List<QualityInspectionSampleGroupMap> listQualityInspectionSampleGroupMapByOrgAndPosition(List<ExecuteGroupAndPosition> groupIds) {
		final List<QualityInspectionSampleGroupMap> maps = new ArrayList<>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionSampleGroupMap.class));
		SelectQuery<EhQualityInspectionSampleGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP);
		Condition con = null;
		if(groupIds != null) {
			for(ExecuteGroupAndPosition executiveGroup : groupIds) {
				Condition con1 = null;
				con1 = Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.ORGANIZATION_ID.eq(executiveGroup.getGroupId());
				con1 = con1.and(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.POSITION_ID.eq(executiveGroup.getPositionId()));
				if(con == null) {
					con = con1;
				} else {
					con = con.or(con1);
				}
			}
		}

		query.addConditions(con);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("listQualityInspectionSampleGroupMapByOrgAndPosition, sql=" + query.getSQL());
			LOGGER.debug("listQualityInspectionSampleGroupMapByOrgAndPosition, bindValues=" + query.getBindValues());
		}
		query.fetch().map((r) -> {
			maps.add(ConvertHelper.convert(r, QualityInspectionSampleGroupMap.class));
			return null;
		});
		return maps;
	}

	@Override
	public List<QualityInspectionSamples> listActiveQualityInspectionSamples(ListingLocator locator, int count, String ownerType,
																			 Long ownerId, List<Long> sampleIds, Long communityId,Timestamp lastUpdateSyncTime) {
		assert(locator.getEntityId() != 0);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		List<QualityInspectionSamples> samples  = new ArrayList<>();
		SelectQuery<EhQualityInspectionSamplesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLES);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.STATUS.in(Status.ACTIVE.getCode()));
		if(locator.getAnchor() != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.lt(locator.getAnchor()));
		}

		if(sampleIds != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.in(sampleIds));
		}

        if(ownerId != null && ownerId != 0) {
        	query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.OWNER_ID.eq(ownerId));
        }
		if(!StringUtils.isNullOrEmpty(ownerType)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.OWNER_TYPE.eq(ownerType));
		}

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.START_TIME.le(now));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.END_TIME.ge(now));
		query.addJoin(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP, Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.SAMPLE_ID.eq(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));
		if(lastUpdateSyncTime!=null){
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.CREATE_TIME.gt(lastUpdateSyncTime)
					.or(Tables.EH_QUALITY_INSPECTION_SAMPLES.DELETE_TIME.gt(lastUpdateSyncTime)));
		}
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.desc());
		query.addLimit(count);

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Query samples by count, sql=" + query.getSQL());
			LOGGER.debug("Query samples by count, bindValues=" + query.getBindValues());
		}

		List<EhQualityInspectionSamplesRecord> records = query.fetch().map(new QualityInspectionSamplesRecordMapper());
		records.forEach((r) -> {
			samples.add(ConvertHelper.convert(r, QualityInspectionSamples.class));
		});
//		return query.fetch().map(new DefaultRecordMapper(Tables.EH_QUALITY_INSPECTION_SAMPLES.recordType(), QualityInspectionSamples.class));

		return samples;
	}

	@Override
	public List<QualityInspectionStandardGroupMap> listQualityInspectionStandardGroupMapByGroupAndPosition(List<ExecuteGroupAndPosition> groupIds) {
		final List<QualityInspectionStandardGroupMap> maps = new ArrayList<QualityInspectionStandardGroupMap>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionStandardGroupMap.class));

		SelectQuery<EhQualityInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP);

		Condition con = null;
		if(groupIds != null) {
			Condition con5 = null;
			for(ExecuteGroupAndPosition executiveGroup : groupIds) {
				Condition con4 = null;
				con4 = Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.GROUP_ID.eq(executiveGroup.getGroupId());
				con4 = con4.and(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.POSITION_ID.eq(executiveGroup.getPositionId()));
				if(con5 == null) {
					con5 = con4;
				} else {
					con5 = con5.or(con4);
				}
			}
			con = con5;
		}

		query.addConditions(con);
		query.fetch().map((r) -> {
			maps.add(ConvertHelper.convert(r, QualityInspectionStandardGroupMap.class));
			return null;
		});
		List<QualityInspectionStandardGroupMap> personalGroupMap = listPersonalGroupMaps();
		if (personalGroupMap != null && personalGroupMap.size() > 0) {
			maps.addAll(personalGroupMap);
		}
		return maps;
	}

	private List<QualityInspectionStandardGroupMap> listPersonalGroupMaps() {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.INSPECTOR_UID.eq(UserContext.currentUserId()));
		return query.fetchInto(QualityInspectionStandardGroupMap.class);
	}

	@Override
	public List<QualityInspectionStandardGroupMap> listQualityInspectionStandardGroupMapByStandardIdAndGroupType(Long standardId, Byte groupType) {
		final List<QualityInspectionStandardGroupMap> maps = new ArrayList<QualityInspectionStandardGroupMap>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionStandardGroupMap.class));

		SelectQuery<EhQualityInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP);

		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.eq(standardId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.GROUP_TYPE.eq(groupType));

		query.fetch().map((r) -> {
			maps.add(ConvertHelper.convert(r, QualityInspectionStandardGroupMap.class));
			return null;
		});


		return maps;
	}

	@Override
	public void createQualityInspectionStandards(
			QualityInspectionStandards standard) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionStandards.class));
		
		standard.setId(id);
		standard.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		standard.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		standard.setStatus(QualityStandardStatus.WAITING.getCode());
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionStandards.class, id));
        EhQualityInspectionStandardsDao dao = new EhQualityInspectionStandardsDao(context.configuration());
        dao.insert(standard);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionStandards.class, null);
		
	}

	@Override
	public void updateQualityInspectionStandards(
			QualityInspectionStandards standard) {

		assert(standard.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionStandards.class, standard.getId()));
        EhQualityInspectionStandardsDao dao = new EhQualityInspectionStandardsDao(context.configuration());
        standard.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(standard);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionStandards.class, standard.getId());	
		
	}

	@Override
	public List<QualityInspectionStandards> listQualityInspectionStandards(ListingLocator locator, int count, 
			Long ownerId, String ownerType, String targetType, List<Long> targetIds, Byte reviewResult, String planCondition) {
		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionStandards.class, locator.getEntityId()));
		List<QualityInspectionStandards> standards = new ArrayList<QualityInspectionStandards>();
        SelectQuery<EhQualityInspectionStandardsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARDS);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.ID.lt(locator.getAnchor()));
        }
		//总公司分公司问题 改成namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
//        if(ownerId != null && ownerId != 0) {
//        	query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.OWNER_ID.eq(ownerId));
//        }
//		if(!StringUtils.isNullOrEmpty(ownerType)) {
//			query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.OWNER_TYPE.eq(ownerType));
//		}
		
		if(targetIds != null && targetIds.size() > 0) {
			if(targetIds.size()==1) {
				query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.TARGET_ID.eq(targetIds.get(0)));
			}else {
				query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.TARGET_ID.in(targetIds));
			}
        }
		if(!StringUtils.isNullOrEmpty(targetType)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.TARGET_TYPE.eq(targetType));    	
		}
		
		if(reviewResult != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.REVIEW_RESULT.eq(reviewResult));
			if(ReviewResult.UNQUALIFIED.equals(ReviewResult.fromStatus(reviewResult))) {
				query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.STATUS.eq(QualityStandardStatus.INACTIVE.getCode()));
			}
			if(ReviewResult.QUALIFIED.equals(ReviewResult.fromStatus(reviewResult))) {
				query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
			}
			if(ReviewResult.NONE.equals(ReviewResult.fromStatus(reviewResult))) {
				query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.STATUS.eq(QualityStandardStatus.WAITING.getCode()));
			}

		}
		if (planCondition != null && !StringUtils.isNullOrEmpty(planCondition)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.STANDARD_NUMBER.like("%"+planCondition+"%")
					.or(Tables.EH_QUALITY_INSPECTION_STANDARDS.NAME.like("%"+planCondition+"%")));
		}
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.DELETER_UID.eq(0L));
        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_STANDARDS.ID.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query standards by count, sql=" + query.getSQL());
            LOGGER.debug("Query standards by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionStandardsRecord record) -> {
        	standards.add(ConvertHelper.convert(record, QualityInspectionStandards.class));
        	return null;
        });
        
        if(standards.size() > 0) {
            locator.setAnchor(standards.get(standards.size() -1).getId());
        }
        
		return standards;
	}

	@Override
	public void createQualityInspectionEvaluationFactors(
			QualityInspectionEvaluationFactors factor) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionEvaluationFactors.class));
		
		factor.setId(id);
		factor.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionEvaluationFactors.class, id));
        EhQualityInspectionEvaluationFactorsDao dao = new EhQualityInspectionEvaluationFactorsDao(context.configuration());
        dao.insert(factor);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionEvaluationFactors.class, null);
		
	}

	@Override
	public void updateQualityInspectionEvaluationFactors(
			QualityInspectionEvaluationFactors factor) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionEvaluationFactors.class, factor.getId()));
		EhQualityInspectionEvaluationFactorsDao dao = new EhQualityInspectionEvaluationFactorsDao(context.configuration());
        dao.update(factor);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionEvaluationFactors.class, factor.getId());
	}

	@Override
	public void deleteQualityInspectionEvaluationFactors(Long factorId) {
		 
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionEvaluationFactors.class));
		 EhQualityInspectionEvaluationFactorsDao dao = new EhQualityInspectionEvaluationFactorsDao(context.configuration());
		 dao.deleteById(factorId);
	}

	@Override
	public List<QualityInspectionEvaluationFactors> listQualityInspectionEvaluationFactors(
			ListingLocator locator, int count, Long ownerId, String ownerType) {
		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionEvaluationFactors.class, locator.getEntityId()));
		List<QualityInspectionEvaluationFactors> factors = new ArrayList<QualityInspectionEvaluationFactors>();
        SelectQuery<EhQualityInspectionEvaluationFactorsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS);
    
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.ID.lt(locator.getAnchor()));
        }
		//分公司 总公司 改用namespaceId by xiongying 20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
		//       if(ownerId != null && ownerId != 0) {
//        	query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.OWNER_ID.eq(ownerId));
//        }
//		if(!StringUtils.isNullOrEmpty(ownerType)) {
//			query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.OWNER_TYPE.eq(ownerType));
//		}
		

        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.ID.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query factors by count, sql=" + query.getSQL());
            LOGGER.debug("Query factors by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionEvaluationFactorsRecord record) -> {
        	factors.add(ConvertHelper.convert(record, QualityInspectionEvaluationFactors.class));
        	return null;
        });
        
        if(factors.size() > 0) {
            locator.setAnchor(factors.get(factors.size() -1).getId());
        }
        
		return factors;
	}

	@Override
	public void createQualityInspectionStandardGroupMap(
			QualityInspectionStandardGroupMap standardGroup) {

		assert(standardGroup.getStandardId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionStandardGroupMap.class, standardGroup.getStandardId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionStandardGroupMap.class));
        standardGroup.setId(id);
        standardGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        EhQualityInspectionStandardGroupMapDao dao = new EhQualityInspectionStandardGroupMapDao(context.configuration());
        dao.insert(standardGroup);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionStandardGroupMap.class, null);
	}


	@Override
	public void deleteQualityInspectionStandardGroupMap(Long standardGroupId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionStandardGroupMap.class));
        EhQualityInspectionStandardGroupMapDao dao = new EhQualityInspectionStandardGroupMapDao(context.configuration());
        dao.deleteById(standardGroupId);
		
	}

	@Override
	public void createQualityInspectionTaskAttachments(
			QualityInspectionTaskAttachments attachment) {

		assert(attachment.getRecordId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTaskRecords.class, attachment.getRecordId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionTaskAttachments.class));
        attachment.setId(id);
        
        EhQualityInspectionTaskAttachmentsDao dao = new EhQualityInspectionTaskAttachmentsDao(context.configuration());
        dao.insert(attachment);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionTaskAttachments.class, null);
		
	}

	@Override
	public void deleteQualityInspectionTaskAttachments(Long attachmentId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTasks.class));
        EhQualityInspectionTaskAttachmentsDao dao = new EhQualityInspectionTaskAttachmentsDao(context.configuration());
        dao.deleteById(attachmentId);
	}

	@Override
	public void createQualityInspectionEvaluations(
			QualityInspectionEvaluations evaluation) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionEvaluations.class));
        evaluation.setId(id);
        evaluation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        EhQualityInspectionEvaluationsDao dao = new EhQualityInspectionEvaluationsDao(context.configuration());
        dao.insert(evaluation);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionEvaluations.class, null);
	}

	@Override
	public List<QualityInspectionEvaluations> listQualityInspectionEvaluations(ListingLocator locator, int count,
			Long ownerId, String ownerType, Timestamp startDate, Timestamp endDate) {

		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionEvaluations.class, locator.getEntityId()));
		List<QualityInspectionEvaluations> evaluations = new ArrayList<QualityInspectionEvaluations>();
        SelectQuery<EhQualityInspectionEvaluationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_EVALUATIONS);
    
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.ID.lt(locator.getAnchor()));
        }
        if(ownerId != null && ownerId != 0) {
        	query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.OWNER_ID.eq(ownerId));
        }
		if(!StringUtils.isNullOrEmpty(ownerType)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.OWNER_TYPE.eq(ownerType));    	
		}
		
		if(startDate != null && !"".equals(startDate)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.CREATE_TIME.ge(startDate));
		}
		
		if(endDate != null && !"".equals(endDate)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.CREATE_TIME.le(endDate));
		}
		
        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.ID.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query evaluations by count, sql=" + query.getSQL());
            LOGGER.debug("Query evaluations by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionEvaluationsRecord record) -> {
        	evaluations.add(ConvertHelper.convert(record, QualityInspectionEvaluations.class));
        	return null;
        });
        
        if(evaluations.size() > 0) {
            locator.setAnchor(evaluations.get(evaluations.size() -1).getId());
        }
        
		return evaluations;
	}

	@Override
	public QualityInspectionTasks findVerificationTaskById(Long taskId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.ID.eq(taskId));
		 
		List<QualityInspectionTasks> result = new ArrayList<QualityInspectionTasks>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTasks.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}
//	@Caching(evict={@CacheEvict(value="listRecordsByOperatorId", key="#record.operatorId")})
	@Override
	public void createQualityInspectionTaskRecords(QualityInspectionTaskRecords record) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionTaskRecords.class));
		record.setId(id);
		record.setProcessTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		record.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTaskRecords.class, id));
        EhQualityInspectionTaskRecordsDao dao = new EhQualityInspectionTaskRecordsDao(context.configuration());
        dao.insert(record);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionTaskRecords.class, null);
		
	}

	@Override
	public List<QualityInspectionTaskRecords> listRecordsByTaskId(Long taskId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTaskRecordsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID.eq(taskId));
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.CREATE_TIME.desc());
		 
		List<QualityInspectionTaskRecords> result = new ArrayList<QualityInspectionTaskRecords>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTaskRecords.class));
			return null;
		});
		
		return result;
	}


	@Override
	public void populateRecordAttachments(final List<QualityInspectionTaskRecords> records) {
        if(records == null || records.size() == 0) {
            return;
        }
            
        final List<Long> recordIds = new ArrayList<Long>();
        final Map<Long, QualityInspectionTaskRecords> mapRecords = new HashMap<Long, QualityInspectionTaskRecords>();
        
        for(QualityInspectionTaskRecords record: records) {
        	recordIds.add(record.getId());
        	mapRecords.put(record.getId(), record);
        }

        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhQualityInspectionTaskRecords.class, recordIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhQualityInspectionTaskRecords.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionTaskRecords.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhQualityInspectionTaskAttachmentsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS);
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS.RECORD_ID.in(recordIds));
            query.fetch().map((EhQualityInspectionTaskAttachmentsRecord record) -> {
            	QualityInspectionTaskRecords r = mapRecords.get(record.getRecordId());
                assert(r != null);
                r.getAttachments().add(ConvertHelper.convert(record, QualityInspectionTaskAttachments.class));
            
                return null;
            });
            return true;
        });
    }

	@Override
	public void populateRecordAttachment(QualityInspectionTaskRecords record) {
		if(record == null) {
            return;
        } else {
            List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
            records.add(record);
            
            populateRecordAttachments(records);
        }
		
	}

	@Override
	public void deleteQualityInspectionStandardGroupMapByStandardId(
			Long standardId) {
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionStandardGroupMap.class), null, 
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhQualityInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP);
					query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.eq(standardId));
		            query.fetch().map((EhQualityInspectionStandardGroupMapRecord record) -> {
		            	deleteQualityInspectionStandardGroupMap(record.getId());
		            	return null;
					});

					return true;
				});
		
	}

	@Override
	public QualityInspectionStandards findStandardById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionStandardsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.ID.eq(id));
		 
		List<QualityInspectionStandards> result = new ArrayList<QualityInspectionStandards>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionStandards.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

	@Override
	public void populateStandardsGroups(List<QualityInspectionStandards> standards) {
		if(standards == null || standards.size() == 0) {
            return;
        }
            
        final List<Long> standardIds = new ArrayList<Long>();
        final Map<Long, QualityInspectionStandards> mapStandards = new HashMap<Long, QualityInspectionStandards>();
        
        for(QualityInspectionStandards standard: standards) {
        	standardIds.add(standard.getId());
        	standard.setExecutiveGroup(new ArrayList<>());
        	standard.setReviewGroup(new ArrayList<>());
        	mapStandards.put(standard.getId(), standard);
        }

        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516       
        //List<Integer> shards = this.shardingProvider.getContentShards(EhQualityInspectionStandards.class, standardIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhQualityInspectionTasks.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhQualityInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP);
            query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.in(standardIds));
            query.fetch().map((EhQualityInspectionStandardGroupMapRecord record) -> {
            	QualityInspectionStandards standard = mapStandards.get(record.getStandardId());
            	
                assert(standard != null);
                if(QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(record.getGroupType()))) {
                	standard.getExecutiveGroup().add(ConvertHelper.convert(record, QualityInspectionStandardGroupMap.class));
				 }
				 if(QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(record.getGroupType()))) {
					 standard.getReviewGroup().add(ConvertHelper.convert(record, QualityInspectionStandardGroupMap.class));
				 }
                return null;
            });
            return true;
        });
	}

	@Override
	public void populateStandardGroups(QualityInspectionStandards standard) {
		if(standard == null) {
            return;
        } else {
            List<QualityInspectionStandards> standards = new ArrayList<QualityInspectionStandards>();
            standards.add(standard);
            
            populateStandardsGroups(standards);
        }
	}

	@Override
	public void createQualityInspectionCategories(
			QualityInspectionCategories category) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionCategories.class));
		
		category.setId(id);
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		category.setPath(category.getPath() + category.getId());
		
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionCategories.class, id));
        EhQualityInspectionCategoriesDao dao = new EhQualityInspectionCategoriesDao(context.configuration());
        dao.insert(category);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionCategories.class, null);
	}

	@Override
	public void updateQualityInspectionCategories(
			QualityInspectionCategories category) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionCategories.class, category.getId()));
		EhQualityInspectionCategoriesDao dao = new EhQualityInspectionCategoriesDao(context.configuration());
        dao.update(category);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionCategories.class, category.getId());		
		
	}

	@Override
	public QualityInspectionCategories findQualityInspectionCategoriesByCategoryId(
			Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionCategoriesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_CATEGORIES);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.ID.eq(id));
		 
		List<QualityInspectionCategories> result = new ArrayList<QualityInspectionCategories>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionCategories.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

	@Override
	public List<QualityInspectionCategories> listQualityInspectionCategories(
			ListingLocator locator, int count, Long ownerId, String ownerType, Long parentId) {
		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionCategories.class, locator.getEntityId()));
		List<QualityInspectionCategories> categories = new ArrayList<QualityInspectionCategories>();
        SelectQuery<EhQualityInspectionCategoriesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_CATEGORIES);
    
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.ID.lt(locator.getAnchor()));
        }

		//总公司分公司问题 改成namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
//        if(ownerId != null && ownerId != 0) {
//        	query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.OWNER_ID.eq(ownerId));
//        }
//		if(!StringUtils.isNullOrEmpty(ownerType)) {
//			query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.OWNER_TYPE.eq(ownerType));
//		}
		
		if(parentId != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.PARENT_ID.eq(parentId));
		}
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.STATUS.ne(QualityInspectionCategoryStatus.DISABLED.getCode()));
        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_CATEGORIES.ID.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query categories by count, sql=" + query.getSQL());
            LOGGER.debug("Query categories by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionCategoriesRecord record) -> {
        	categories.add(ConvertHelper.convert(record, QualityInspectionCategories.class));
        	return null;
        });
        
        if(categories.size() > 0) {
            locator.setAnchor(categories.get(categories.size() -1).getId());
        }
        
		return categories;
	}

	@Override
	public QualityInspectionEvaluationFactors findQualityInspectionFactorById(
			Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionEvaluationFactorsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.ID.eq(id));
		 
		List<QualityInspectionEvaluationFactors> result = new ArrayList<QualityInspectionEvaluationFactors>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionEvaluationFactors.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

	@Override
	public List<QualityInspectionTasks> listClosedTask(Timestamp startDate, Timestamp endDate) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class));
		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
        SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
        
        query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.in(QualityInspectionTaskStatus.EXECUTED.getCode(), QualityInspectionTaskStatus.DELAY.getCode()));

        if(startDate != null && !"".equals(startDate)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_TIME.ge(startDate));
		}
		
		if(endDate != null && !"".equals(endDate)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_TIME.le(endDate));
		}
        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASKS.ID.desc());
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query tasks by count, sql=" + query.getSQL());
            LOGGER.debug("Query tasks by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionTasksRecord record) -> {
        	tasks.add(ConvertHelper.convert(record, QualityInspectionTasks.class));
        	return null;
        });
		return tasks;
	}

	@Override
	public List<QualityInspectionTasks> listTodayQualityInspectionTasks(Long startTime, Long endTime) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class));
		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
		SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);

		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(new Timestamp(startTime)));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_START_TIME.le(new Timestamp(endTime)));

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("listTodayQualityInspectionTasks, sql=" + query.getSQL());
			LOGGER.debug("listTodayQualityInspectionTasks, bindValues=" + query.getBindValues());
		}

		query.fetch().map((EhQualityInspectionTasksRecord record) -> {
			tasks.add(ConvertHelper.convert(record, QualityInspectionTasks.class));
			return null;
		});

		return tasks;
	}

	@Override
	public QualityInspectionTasks findLastestQualityInspectionTask(Long startTime) {
		QualityInspectionTasks[] result = new QualityInspectionTasks[1];

		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class), null,
				(DSLContext context, Object reducingContext) -> {
					result[0] = context.select().from(Tables.EH_QUALITY_INSPECTION_TASKS)
							.where(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(new Timestamp(startTime)))
							.orderBy(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_START_TIME)
							.fetchAnyInto(QualityInspectionTasks.class);

					if (result[0] != null) {
						return false;
					} else {
						return true;
					}
				});

		return result[0];
	}

	@Override
	public QualityInspectionStandardSpecificationMap getMapByStandardId(Long standardId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.selectFrom(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP)
				.where(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()))
				.and(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.STANDARD_ID.eq(standardId))
				.fetchOneInto(QualityInspectionStandardSpecificationMap.class);
	}


	@Override
	public QualityInspectionEvaluationFactors findQualityInspectionFactorByGroupIdAndCategoryId(
			Long groupId, Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionEvaluationFactorsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.CATEGORY_ID.eq(categoryId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.GROUP_ID.eq(groupId));
		 
		List<QualityInspectionEvaluationFactors> result = new ArrayList<QualityInspectionEvaluationFactors>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionEvaluationFactors.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

	@Override
	public QualityInspectionTaskRecords listLastRecordByTaskId(Long taskId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTaskRecordsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID.eq(taskId));
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.CREATE_TIME.desc());
		 
		List<QualityInspectionTaskRecords> result = new ArrayList<QualityInspectionTaskRecords>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTaskRecords.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

	@Override
	public void populateTaskRecords(QualityInspectionTasks task) {
		if(task == null) {
            return;
        } else {
            List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
            tasks.add(task);
            
            populateTasksRecords(tasks);
        }
		
	}

	@Override
	public void populateTasksRecords(List<QualityInspectionTasks> tasks) {
		if(tasks == null || tasks.size() == 0) {
            return;
        }
            
        final List<Long> taskIds = new ArrayList<Long>();
        final Map<Long, QualityInspectionTasks> mapTasks = new HashMap<Long, QualityInspectionTasks>();
        
        for(QualityInspectionTasks task: tasks) {
        	taskIds.add(task.getId());
        	mapTasks.put(task.getId(), task);
        }
 
        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhQualityInspectionTasks.class, taskIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhQualityInspectionTasks.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class), null, (DSLContext context, Object reducingContext) -> {    
        	SelectQuery<EhQualityInspectionTaskRecordsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS);
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID.in(taskIds));
            query.fetch().map((EhQualityInspectionTaskRecordsRecord record) -> {
            	QualityInspectionTasks task = mapTasks.get(record.getTaskId());
                assert(task != null);
 //               task.getRecords().add(ConvertHelper.convert(record, QualityInspectionTaskRecords.class));
            
                return null;
            });
            return true;
        });
		
	}

	@Override
	public List<QualityInspectionStandards> findStandardsByCategoryId(
			Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionStandardsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARDS);
		//query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.CATEGORY_ID.eq(categoryId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.STATUS.ne(QualityStandardStatus.INACTIVE.getCode()));
		
		List<QualityInspectionStandards> result = new ArrayList<QualityInspectionStandards>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionStandards.class));
			return null;
		});
		
		return result;
	}

	@Override
	public int countInspectionEvaluations(Long ownerId, String ownerType, Timestamp startDate,Timestamp endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_QUALITY_INSPECTION_EVALUATIONS);
    
        Condition condition = Tables.EH_QUALITY_INSPECTION_EVALUATIONS.OWNER_ID.equal(ownerId);
		condition = condition.and(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.OWNER_TYPE.equal(ownerType));
		
		if(startDate != null && !"".equals(startDate)) {
			condition = condition.and(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.CREATE_TIME.ge(startDate));
		}
		
		if(endDate != null && !"".equals(endDate)) {
			condition = condition.and(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.CREATE_TIME.le(endDate));
		}
		
       return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public int countVerificationTasks(Long ownerId, String ownerType,
			Byte taskType, Long executeUid, Timestamp startDate,
			Timestamp endDate, Long groupId, List<Byte> executeStatus,
			Byte reviewStatus) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_QUALITY_INSPECTION_TASKS);
    
//        Condition condition = Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_ID.equal(ownerId);
//		condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_TYPE.equal(ownerType));
		//总公司 分公司 改用namespaceId by xiongying20170329
		Condition condition = Tables.EH_QUALITY_INSPECTION_TASKS.NAMESPACE_ID.equal(UserContext.getCurrentNamespaceId());

		
		if(taskType != null) {
			condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.TASK_TYPE.eq(taskType));
		}
		if(executeUid != null && executeUid != 0) {
			Condition con = Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTOR_ID.eq(executeUid);
			con = con.or(Tables.EH_QUALITY_INSPECTION_TASKS.OPERATOR_ID.eq(executeUid));
			condition = condition.and(con);
		}
		
		if(startDate != null && !"".equals(startDate)) {
			condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.ge(startDate));
		}
		
		if(endDate != null && !"".equals(endDate)) {
			condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.le(endDate));
		}
		
		if(groupId != null && groupId != 0) {
			condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_GROUP_ID.eq(groupId));
		}
		if(executeStatus != null) {
			condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.in(executeStatus));
		}
		if(reviewStatus != null) {
			if(QualityInspectionTaskReviewStatus.NONE.equals(reviewStatus))
				condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.REVIEW_RESULT.eq(QualityInspectionTaskReviewResult.NONE.getCode()));
			if(QualityInspectionTaskReviewStatus.REVIEWED.equals(reviewStatus))
				condition = condition.and(Tables.EH_QUALITY_INSPECTION_TASKS.REVIEW_RESULT.ne(QualityInspectionTaskReviewResult.NONE.getCode()));
		}

        
		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public void closeDelayTasks() {
		Timestamp current = new Timestamp(System.currentTimeMillis());

		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhQualityInspectionTasks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.ID.gt(locator.getAnchor()));
			
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.lt(current));
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.lt(current)
					.or(Tables.EH_QUALITY_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.isNull()));
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()));
			
			query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASKS.ID.asc());
			
			query.fetch().map((r) -> {
				QualityInspectionTasks task = ConvertHelper.convert(r, QualityInspectionTasks.class);
				closeTask(task);
//				if(r.getStatus().equals(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()))
//					r.setResult(QualityInspectionTaskResult.INSPECT_DELAY.getCode());
//				if(r.getStatus().equals(QualityInspectionTaskStatus.RECTIFING.getCode()))
//					r.setResult(QualityInspectionTaskResult.CORRECT_DELAY.getCode());
//				if(r.getStatus().equals(QualityInspectionTaskStatus.RECTIFIED_AND_WAITING_APPROVAL.getCode())
//						|| r.getStatus().equals(QualityInspectionTaskStatus.RECTIFY_CLOSED_AND_WAITING_APPROVAL.getCode()))
//					r.setResult(QualityInspectionTaskResult.RECTIFY_DELAY.getCode());
//				
//				r.setStatus(QualityInspectionTaskStatus.CLOSED.getCode());;
//				EhQualityInspectionTasks task = ConvertHelper.convert(r, EhQualityInspectionTasks.class);
//				EhQualityInspectionTasksDao dao = new EhQualityInspectionTasksDao(context.configuration());
//		        dao.update(task);
//		        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionTasks.class, task.getId());
				return null;
			});
			return AfterAction.next;
		});
		
	}

	@Override
	public List<QualityInspectionStandards> listActiveStandards() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionStandardsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.REVIEW_RESULT.eq(ReviewResult.QUALIFIED.getCode()));
		
		List<QualityInspectionStandards> result = new ArrayList<QualityInspectionStandards>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionStandards.class));
			return null;
		});
		
		return result;
	}

	@Override
	public List<Long> listQualityInspectionStandardGroupMapByGroup(
			List<ExecuteGroupAndPosition> groupIds, Byte groupType) {
		final List<Long> standardIds = new ArrayList<Long>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionStandardGroupMap.class));
 
        SelectQuery<EhQualityInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP);
       
        if(groupIds != null) {
			Condition con5 = null;
			for(ExecuteGroupAndPosition groupId : groupIds) {
				Condition con4 = null;
				con4 = Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.GROUP_ID.eq(groupId.getGroupId());
//				con4 = con4.and(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.POSITION_ID.eq(groupId.getPositionId()));
				con5 = con5.or(con4);
			}
			query.addConditions(con5);
		}

        if(groupType != null)
        	query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.GROUP_TYPE.eq(groupType));
        
        query.fetch().map((r) -> {
        	standardIds.add(r.getStandardId());
             return null;
        });
        
       
        return standardIds;
	}

	@Override
	public void closeTask(QualityInspectionTasks task) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		if(task.getResult().equals(QualityInspectionTaskResult.NONE.getCode()))
			task.setResult(QualityInspectionTaskResult.INSPECT_DELAY.getCode());
		if(task.getResult().equals(QualityInspectionTaskResult.CORRECT.getCode()))
			task.setResult(QualityInspectionTaskResult.CORRECT_DELAY.getCode());
		
		task.setStatus(QualityInspectionTaskStatus.DELAY.getCode());
		task.setExecutiveTime(new Timestamp(System.currentTimeMillis()));
		EhQualityInspectionTasks t = ConvertHelper.convert(task, EhQualityInspectionTasks.class);
		EhQualityInspectionTasksDao dao = new EhQualityInspectionTasksDao(context.configuration());
        dao.update(t);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionTasks.class, t.getId());

		taskSearcher.feedDoc(task);
	}

	@Override
	public List<QualityInspectionCategories> listQualityInspectionCategoriesByPath(
			String superiorPath) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<QualityInspectionCategories> categories = new ArrayList<QualityInspectionCategories>();
        SelectQuery<EhQualityInspectionCategoriesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_CATEGORIES);
    
		if(!StringUtils.isNullOrEmpty(superiorPath)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.PATH.like(superiorPath + "/%"));    	
		}
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_CATEGORIES.STATUS.ne(QualityInspectionCategoryStatus.DISABLED.getCode()));
        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_CATEGORIES.ID.desc());
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query categories by count, sql=" + query.getSQL());
            LOGGER.debug("Query categories by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionCategoriesRecord record) -> {
        	categories.add(ConvertHelper.convert(record, QualityInspectionCategories.class));
        	return null;
        });
        
		return categories;
	}

	@Override
	public List<QualityInspectionLogs> listQualityInspectionLogs(
			String ownerType, Long ownerId, String targetType, Long targetId,List<Long> scopeIds,
			ListingLocator locator, int count) {
		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionLogs.class, locator.getEntityId()));
		List<QualityInspectionLogs> logs = new ArrayList<QualityInspectionLogs>();
        SelectQuery<EhQualityInspectionLogsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_LOGS);
    
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_QUALITY_INSPECTION_LOGS.ID.lt(locator.getAnchor()));
        }
		// 总公司 分公司 改用namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_LOGS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));

		if(targetId != null && targetId != 0) {
        	query.addConditions(Tables.EH_QUALITY_INSPECTION_LOGS.TARGET_ID.eq(targetId));
        }
		if(!StringUtils.isNullOrEmpty(targetType)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_LOGS.TARGET_TYPE.eq(targetType));    	
		}
		if (scopeIds != null && scopeIds.size()>0 ) {
			if(scopeIds.size()==1) {
				query.addConditions(Tables.EH_QUALITY_INSPECTION_LOGS.SCOPE_ID.eq(scopeIds.get(0)));
			}else {
				query.addConditions(Tables.EH_QUALITY_INSPECTION_LOGS.SCOPE_ID.in(scopeIds));
			}
		}
		
        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_LOGS.ID.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query logs by count, sql=" + query.getSQL());
            LOGGER.debug("Query logs by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionLogsRecord record) -> {
        	logs.add(ConvertHelper.convert(record, QualityInspectionLogs.class));
        	return null;
        });
        
		return logs;
	}

	@Override
	public void createQualityInspectionLogs(QualityInspectionLogs log) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionLogs.class));
		
		log.setId(id);
		log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
		LOGGER.info("createQualityInspectionLogs: " + log);
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionLogs.class, id));
        EhQualityInspectionLogsDao dao = new EhQualityInspectionLogsDao(context.configuration());
        dao.insert(log);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionLogs.class, null);
	}

	@Override
	public QualityInspectionStandards findStandardById(Long id,
			String ownerType, Long ownerId, String targetType, Long targetId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionStandardsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.ID.eq(id));
		//总公司 分公司 改用namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.TARGET_ID.eq(targetId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARDS.TARGET_TYPE.eq(targetType));
		 
		List<QualityInspectionStandards> result = new ArrayList<QualityInspectionStandards>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionStandards.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

	@Override
	public void deleteQualityInspectionStandardSpecificationMapByStandardId(
			Long standardId) {
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionStandardSpecificationMap.class), null, 
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhQualityInspectionStandardSpecificationMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP);
					query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.STANDARD_ID.eq(standardId));
					query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
					query.fetch().map((EhQualityInspectionStandardSpecificationMapRecord record) -> {
						deleteQualityInspectionStandardSpecificationMap(record.getId());
		            	return null;
					});

					return true;
				});
		
	}
	
	@Override
	public void deleteQualityInspectionStandardSpecificationMap(Long standardSpecificationId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionStandardSpecificationMap.class));
		EhQualityInspectionStandardSpecificationMapDao dao = new EhQualityInspectionStandardSpecificationMapDao(context.configuration());
        dao.deleteById(standardSpecificationId);
		
	}

	@Override
	public QualityInspectionStandardSpecificationMap createQualityInspectionStandardSpecificationMap(
			QualityInspectionStandardSpecificationMap map) {

		assert(map.getStandardId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionStandardSpecificationMap.class, map.getStandardId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionStandardSpecificationMap.class));
        map.setId(id);
        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        map.setStatus((byte) 2);
        
        EhQualityInspectionStandardSpecificationMapDao dao = new EhQualityInspectionStandardSpecificationMapDao(context.configuration());
        dao.insert(map);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionStandardSpecificationMap.class, null);
        
        return map;
	}

	@Override
	public QualityInspectionSpecifications findSpecificationById(Long id,
			String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.ID.eq(id));
		//总公司 分公司 改用namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
		 
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("findSpecificationById, sql=" + query.getSQL());
            LOGGER.debug("findSpecificationById, bindValues=" + query.getBindValues());
        }
		
		List<QualityInspectionSpecifications> result = new ArrayList<QualityInspectionSpecifications>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecifications.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

	@Override
	public QualityInspectionSpecifications getSpecificationById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.ID.eq(id));

		List<QualityInspectionSpecifications> result = new ArrayList<QualityInspectionSpecifications>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecifications.class));
			return null;
		});
		if(result.size()==0)
			return null;

		return result.get(0);
	}

	@Override
	public void populateStandardsSpecifications(
			List<QualityInspectionStandards> standards) {
		if(standards == null || standards.size() == 0) {
            return;
        }
            
        final List<Long> standardIds = new ArrayList<Long>();
        final Map<Long, QualityInspectionStandards> mapStandards = new HashMap<Long, QualityInspectionStandards>();
        
        for(QualityInspectionStandards standard: standards) {
        	standardIds.add(standard.getId());
        	mapStandards.put(standard.getId(), standard);
        }
 
        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhQualityInspectionStandards.class, standardIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhQualityInspectionStandards.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionStandards.class), null, (DSLContext context, Object reducingContext) -> {    
        	SelectQuery<EhQualityInspectionStandardSpecificationMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP);
            query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.STANDARD_ID.in(standardIds));
            query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Query StandardsSpecifications, sql=" + query.getSQL());
                LOGGER.debug("Query StandardsSpecifications, bindValues=" + query.getBindValues());
            }
            query.fetch().map((EhQualityInspectionStandardSpecificationMapRecord record) -> {
            	QualityInspectionStandards standard = mapStandards.get(record.getStandardId());
                assert(standard != null);
                QualityInspectionSpecifications specification =
						findSpecificationById(record.getSpecificationId(), standard.getOwnerType(), standard.getOwnerId());
            
                if(standard.getSpecifications() == null) {
                	standard.setSpecifications(new ArrayList<QualityInspectionSpecifications>());
                }
                
                standard.getSpecifications().add(specification);
                return null;
            });
            return true;
        });
	}

	@Override
	public void populateStandardSpecifications(
			QualityInspectionStandards standard) {
		if(standard == null) {
            return;
        } else {
            List<QualityInspectionStandards> standards = new ArrayList<QualityInspectionStandards>();
            standards.add(standard);
            
            populateStandardsSpecifications(standards);
        }
	}

	@Override
	public void createQualitySpecification(
			QualityInspectionSpecifications specification) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionSpecifications.class));
		
		specification.setId(id);
		specification.setNamespaceId(UserContext.getCurrentNamespaceId());
		specification.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		specification.setPath(specification.getPath() + specification.getId());
		specification.setStatus(QualityStandardStatus.ACTIVE.getCode());
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSpecifications.class, id));
        EhQualityInspectionSpecificationsDao dao = new EhQualityInspectionSpecificationsDao(context.configuration());
        dao.insert(specification);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionSpecifications.class, null);
		
	}

	@Override
	public void updateQualitySpecification(
			QualityInspectionSpecifications specification) {

		assert(specification.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSpecifications.class, specification.getId()));
        EhQualityInspectionSpecificationsDao dao = new EhQualityInspectionSpecificationsDao(context.configuration());
        dao.update(specification);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionSpecifications.class, specification.getId());	
		
	}

	@Override
	public void inactiveQualityInspectionStandardSpecificationMapBySpecificationId(Long specificationId) {
		Long userId = UserContext.current().getUser().getId();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionStandardSpecificationMap.class), null, 
				(DSLContext context, Object reducingContext) -> {
					SelectQuery<EhQualityInspectionStandardSpecificationMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP);
					query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.SPECIFICATION_ID.eq(specificationId));
					query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
					query.fetch().map((EhQualityInspectionStandardSpecificationMapRecord record) -> {
						QualityInspectionStandardSpecificationMap map = ConvertHelper.convert(record, QualityInspectionStandardSpecificationMap.class);
						map.setStatus(QualityStandardStatus.INACTIVE.getCode());
						map.setDeleterUid(userId);
						map.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						updateQualityInspectionStandardSpecificationMap(map);
						deleteQualityInspectionTaskByStandardId(map.getStandardId());
						return null;
					});

					return true;
				});
		
	}

	private void deleteQualityInspectionTaskByStandardId(Long standardId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.update(Tables.EH_QUALITY_INSPECTION_TASKS)
				.set(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS, QualityInspectionTaskStatus.NONE.getCode())
				.set(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
				.where(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.eq(standardId))
				.execute();
	}

	@Override
	public void updateQualityInspectionStandardSpecificationMap(
			QualityInspectionStandardSpecificationMap map) {
		assert(map.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionStandardSpecificationMap.class, map.getId()));
        EhQualityInspectionStandardSpecificationMapDao dao = new EhQualityInspectionStandardSpecificationMapDao(context.configuration());
        dao.update(map);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionStandardSpecificationMap.class, map.getId());	
	}

	@Override
	public List<QualityInspectionSpecifications> listAllChildrenSpecifications(
			String superiorPath, String ownerType, Long ownerId, Byte scopeCode, Long scopeId, Byte inspectionType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<QualityInspectionSpecifications> result  = new ArrayList<QualityInspectionSpecifications>();
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.PATH.like(superiorPath));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_ID.eq(ownerId));
		//改成用namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));

		if(scopeCode != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_CODE.eq(scopeCode));
		if(scopeId != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_ID.eq(scopeId));
		if(inspectionType != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.INSPECTION_TYPE.eq(inspectionType));

		//issue-43661 【智谷汇】品质核查，后台制定的计划，执行人和审阅人在APP端都看不到 by djm
		//pc把类型和标准建到了全部下面，而在具体园区下面设置任务，在pc可以看到任务
		// app端同步数据，根据园区获取数据，具体园区下面没有对应的ownerId 所以前端传过来null取查询类型、标准，sql里面ownerId=null 这样查询不出数据，导致categories为空，app端数据无法显示
		if(SpecificationScopeCode.ALL.equals(SpecificationScopeCode.fromCode(scopeCode)) && ownerId !=null){
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_ID.eq(ownerId));
		}
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
		
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecifications.class));
			return null;
		});
		
		return result;
	}

	@Override
	public List<QualityInspectionSpecifications> listChildrenSpecifications(
			String ownerType, Long ownerId, Byte scopeCode, Long scopeId, Long parentId, Byte inspectionType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<QualityInspectionSpecifications> result  = new ArrayList<QualityInspectionSpecifications>();
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.PARENT_ID.eq(parentId));
		//分公司 总公司 改用namespaceId by xiongying 20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_ID.eq(ownerId));
		
		if(scopeCode != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_CODE.eq(scopeCode));
		if(scopeId != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_ID.eq(scopeId));
		if(inspectionType != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.INSPECTION_TYPE.eq(inspectionType));
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
		
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecifications.class));
			return null;
		});
		
		return result;
		
	}

	@Override
	public List<QualityInspectionSpecifications> listAddAndModifyChildrenSpecifications(String ownerType, Long ownerId, Byte scopeCode, List<Long> scopeIds, Long parentId, Byte inspectionType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<QualityInspectionSpecifications> result  = new ArrayList<QualityInspectionSpecifications>();
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.PARENT_ID.eq(parentId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.APPLY_POLICY.eq(SpecificationApplyPolicy.ADD.getCode())
				.or(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.APPLY_POLICY.eq(SpecificationApplyPolicy.MODIFY.getCode())));
		if(scopeCode != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_CODE.eq(scopeCode));
		if(scopeIds != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_ID.in(scopeIds));
		if(inspectionType != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.INSPECTION_TYPE.eq(inspectionType));

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));

		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecifications.class));
			return null;
		});

		return result;
	}

	@Override
	public List<TaskCountDTO> countTasks(String ownerType, Long ownerId,
			String targetType, Long targetId, Long startTime, Long endTime,
			int offset, int count, Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<TaskCountDTO> dtos = new ArrayList<TaskCountDTO>();
		
		final Field<Byte> none = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.NONE.getCode()), QualityInspectionTaskResult.NONE.getCode());
		final Field<Byte> correct = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.CORRECT.getCode()), QualityInspectionTaskResult.CORRECT.getCode());
		final Field<Byte> inspectComplete = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.INSPECT_COMPLETE.getCode()), QualityInspectionTaskResult.INSPECT_COMPLETE.getCode());
		final Field<Byte> correctComplete = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.CORRECT_COMPLETE.getCode()), QualityInspectionTaskResult.CORRECT_COMPLETE.getCode());
		final Field<Byte> inspectDelay = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.INSPECT_DELAY.getCode()), QualityInspectionTaskResult.INSPECT_DELAY.getCode());
		final Field<Byte> correctDelay = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.CORRECT_DELAY.getCode()), QualityInspectionTaskResult.CORRECT_DELAY.getCode());
		final Field<?>[] fields = {Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_TYPE, Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID, DSL.count().as("taskCount"), 
				DSL.count(none).as("toExecuted"), DSL.count(correct).as("inCorrection"), DSL.count(inspectComplete).as("completeInspection"), 
				DSL.count(correctComplete).as("completeCorrection"),DSL.count(inspectDelay).as("delayInspection"),DSL.count(correctDelay).as("delayCorrection")};
		final SelectQuery<Record> query = context.selectQuery();
		query.addSelect(fields);
		query.addFrom(Tables.EH_QUALITY_INSPECTION_TASKS);

		//总公司 分公司 改用namespaceId by xiongying 20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.OWNER_ID.eq(ownerId));
		if(sampleId != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.PARENT_ID.eq(sampleId));
		}

		if(!StringUtils.isNullOrEmpty(targetType)) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_TYPE.eq(targetType));
		}
		
		if(targetId != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID.eq(targetId));
		}
		
		if(startTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.ge(new Timestamp(startTime)));
		}
		
		if(endTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.le(new Timestamp(endTime)));
		}
		
		query.addGroupBy(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_TYPE, Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID);
		query.addLimit(offset, count);
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("countTasks, sql=" + query.getSQL());
            LOGGER.debug("countTasks, bindValues=" + query.getBindValues());
        }
		query.fetch().map((r) -> {
			TaskCountDTO dto = new TaskCountDTO();
			dto.setTargetId(r.getValue(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID));
			dto.setTaskCount(r.getValue("taskCount", Long.class));
			dto.setToExecuted(r.getValue("toExecuted", Long.class));
			dto.setInCorrection(r.getValue("inCorrection", Long.class));
			dto.setCompleteInspection(r.getValue("completeInspection", Long.class));
			dto.setCompleteCorrection(r.getValue("completeCorrection", Long.class));
			dto.setDelayInspection(r.getValue("delayInspection", Long.class));
			dto.setDelayCorrection(r.getValue("delayCorrection", Long.class));
			dtos.add(dto);
			return null;
		});
		
		return dtos;
	}

	@Override
	public void populateRecordItemResults(
			List<QualityInspectionTaskRecords> records) {
		if(records == null || records.size() == 0) {
            return;
        }
            
        final List<Long> recordIds = new ArrayList<Long>();
        final Map<Long, QualityInspectionTaskRecords> mapRecords = new HashMap<Long, QualityInspectionTaskRecords>();
        
        for(QualityInspectionTaskRecords record: records) {
        	recordIds.add(record.getId());
        	mapRecords.put(record.getId(), record);
        }
 
        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhQualityInspectionTaskRecords.class, recordIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhQualityInspectionTaskRecords.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhQualityInspectionTaskRecords.class), null, (DSLContext context, Object reducingContext) -> { 
        	SelectQuery<EhQualityInspectionSpecificationItemResultsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS);
            query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TASK_RECORD_ID.in(recordIds));
            query.fetch().map((EhQualityInspectionSpecificationItemResultsRecord record) -> {
            	QualityInspectionTaskRecords r = mapRecords.get(record.getTaskRecordId());
                assert(r != null);
                QualityInspectionSpecificationItemResults result = findItemResultById(record.getId());
            
                r.getItemResults().add(result);
                return null;
            });
            return true;
        });
        
        LOGGER.info("populateRecordItemResults: " + records);
		
	}
	
	private QualityInspectionSpecificationItemResults findItemResultById(Long resultId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionSpecificationItemResults.class, resultId));
		EhQualityInspectionSpecificationItemResultsDao dao = new EhQualityInspectionSpecificationItemResultsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(resultId), QualityInspectionSpecificationItemResults.class);
	}

	@Override
	public void populateRecordItemResult(QualityInspectionTaskRecords record) {
		if(record == null) {
            return;
        } else {
            List<QualityInspectionTaskRecords> records = new ArrayList<QualityInspectionTaskRecords>();
            records.add(record);
            
            populateRecordItemResults(records);
        }
		
	}

	@Override
	public void createSpecificationItemResults(
			QualityInspectionSpecificationItemResults result) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionSpecificationItemResults.class));
		
		result.setId(id);
		result.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
		LOGGER.info("createSpecificationItemResults: " + result);
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSpecificationItemResults.class, id));
        EhQualityInspectionSpecificationItemResultsDao dao = new EhQualityInspectionSpecificationItemResultsDao(context.configuration());
        dao.insert(result);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionTasks.class, null);
	}

	@Override
	public ScoreDTO countScores(String ownerType, Long ownerId, String targetType, Long targetId,
			String superiorPath, Long startTime, Long endTime, Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		ScoreDTO score = new ScoreDTO();
		score.setScore(0.0);
		
		final Field<?>[] fields = {Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TARGET_TYPE, Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TARGET_ID, 
				Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SPECIFICATION_PARENT_ID, DSL.sum(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TOTAL_SCORE).as("totalScore")};
		final SelectQuery<Record> query = context.selectQuery();
		query.addSelect(fields);
		query.addFrom(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS);
		
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TARGET_ID.eq(targetId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TARGET_TYPE.eq(targetType));
		//分公司 总公司 改用namespaceId by xiongying20170329
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SPECIFICATION_PATH.like(superiorPath));
		if(startTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.CREATE_TIME.ge(new Timestamp(startTime)));
		}
		
		if(endTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.CREATE_TIME.le(new Timestamp(endTime)));
		}

		if(sampleId != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SAMPLE_ID.eq(sampleId));
		}
		//不加上的话 在上级统计分数的时候会扣除所有扣分，但点进看下级情况时，已删除的规范不会列出来，会造成上下两层分数的差异 by xiongying20170123
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SPECIFICATION_ID.in(getActiveSpecifications(ownerType, ownerId)));
		
		query.addGroupBy(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TARGET_TYPE, Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TARGET_ID, Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SPECIFICATION_PARENT_ID);
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.TARGET_ID.desc());
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("countScores, sql=" + query.getSQL());
            LOGGER.debug("countScores, bindValues=" + query.getBindValues());
        }
		score.setScore(0.0);
		query.fetch().map((r) -> {
			Double totalScore = r.getValue("totalScore", Double.class);
			QualityInspectionSpecifications parentSpecification = getSpecificationById(r.getValue(
					Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SPECIFICATION_PARENT_ID));
			if(parentSpecification.getScore() < totalScore) {
				score.setScore(parentSpecification.getScore() + score.getScore());
			} else {
				score.setScore(totalScore + score.getScore());
			}
			
			return null;
		});
		return score;
	}

	private List<Long> getActiveSpecifications(String ownerType, Long ownerId) {
		List<Long> specificationIds = new ArrayList<Long>();
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));
		
		query.fetch().map((r) -> {
			specificationIds.add(r.getId());
			return null;
		});
		
		return specificationIds;
	}
	
	@Override
	public Set<Long> listRecordsTaskIdByOperatorId(Long operatorId, Long maxTaskId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTaskRecordsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.OPERATOR_ID.eq(operatorId));
		
		if(maxTaskId != null && maxTaskId != 0L) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID.lt(maxTaskId));
		}
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID.desc());
		 
		Set<Long> result = new HashSet<Long>();
		query.fetch().map((r) -> {
			result.add(r.getTaskId());
			return null;
		});
		
		return result;
	}

	@Override
	public Set<Long> listRecordsTaskIdByOperatorId(Long operatorId, Timestamp beginTime, Long targetId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		query.addFrom(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.OPERATOR_ID.eq(operatorId));

		if(beginTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.CREATE_TIME.ge(beginTime));
		}
		Condition con = Tables.EH_QUALITY_INSPECTION_TASKS.ID.eq(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID);
		if(targetId != null) {
			con = con.and(Tables.EH_QUALITY_INSPECTION_TASKS.TARGET_ID.eq(targetId));
		}

		query.addJoin(Tables.EH_QUALITY_INSPECTION_TASKS, con);
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID.desc());

		Set<Long> result = new HashSet<Long>();
		query.fetch().map((r) -> {
			result.add(r.getValue(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID));
			return null;
		});

		return result;
	}

//	@Cacheable(value="listRecordsByOperatorId", key="{#operatorId}", unless="#result.size() == 0")
	@Override
	public List<QualityInspectionTaskRecords> listRecordsByOperatorId(Long operatorId, Timestamp createTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTaskRecordsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.OPERATOR_ID.eq(operatorId));

//		if(createTime != null) {
//			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.CREATE_TIME.lt(createTime));
//		}
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.CREATE_TIME.desc());

		List<QualityInspectionTaskRecords> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTaskRecords.class));
			return null;
		});

		return result;
	}

	@Override
	public List<QualityInspectionTasks> listTaskByIds(List<Long> taskIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.ID.in(taskIds));
		
		List<QualityInspectionTasks> result = new ArrayList<QualityInspectionTasks>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTasks.class));
			return null;
		});
		
		return result;
	}

	@Override
	public List<QualityInspectionTasks> listTaskByIds(Set<Long> taskIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.ID.in(taskIds));

		List<QualityInspectionTasks> result = new ArrayList<QualityInspectionTasks>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTasks.class));
			return null;
		});

		return result;
	}

	@Override
	public void createQualityInspectionTaskTemplates(
			QualityInspectionTaskTemplates template) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionTaskTemplates.class));
		
		template.setId(id);
        
		LOGGER.info("createQualityInspectionTaskTemplates: " + template);
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTaskTemplates.class, id));
        EhQualityInspectionTaskTemplatesDao dao = new EhQualityInspectionTaskTemplatesDao(context.configuration());
        dao.insert(template);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionTaskTemplates.class, null);
		
	}

	@Override
	public void updateQualityInspectionTaskTemplates(
			QualityInspectionTaskTemplates template) {

		assert(template.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTaskTemplates.class, template.getId()));
        EhQualityInspectionTaskTemplatesDao dao = new EhQualityInspectionTaskTemplatesDao(context.configuration());
        dao.update(template);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionTaskTemplates.class, template.getId());		
		
	}

	@Override
	public void deleteQualityInspectionTaskTemplates(Long templateId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionTaskTemplates.class));
		EhQualityInspectionTaskTemplatesDao dao = new EhQualityInspectionTaskTemplatesDao(context.configuration());
		dao.deleteById(templateId);
		
	}

	@Override
	public List<QualityInspectionTaskTemplates> listUserQualityInspectionTaskTemplates(
			ListingLocator locator, int count, Long uid) {
		assert(locator.getEntityId() != 0);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionTaskTemplates.class, locator.getEntityId()));
		List<QualityInspectionTaskTemplates> templates = new ArrayList<QualityInspectionTaskTemplates>();
        SelectQuery<EhQualityInspectionTaskTemplatesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES);
    
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.ID.lt(locator.getAnchor()));
        }
        
		
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.CREATOR_UID.eq(uid));
        query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.ID.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query task templates by count, sql=" + query.getSQL());
            LOGGER.debug("Query task templates by count, bindValues=" + query.getBindValues());
        }
        
        query.fetch().map((EhQualityInspectionTaskTemplatesRecord record) -> {
        	templates.add(ConvertHelper.convert(record, QualityInspectionTaskTemplates.class));
        	return null;
        });
        
		return templates;
	}

	@Override
	public QualityInspectionTaskTemplates findQualityInspectionTaskTemplateById(
			Long templateId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTaskTemplatesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.ID.eq(templateId));

		List<QualityInspectionTaskTemplates> result = new ArrayList<QualityInspectionTaskTemplates>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTaskTemplates.class));
			return null;
		});
		if(result.size()==0)
			return null;

		return result.get(0);
	}

	@Override
	public void createQualityInspectionSample(QualityInspectionSamples sample) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionSamples.class));

		sample.setId(id);
		sample.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		LOGGER.info("createQualityInspectionSample: " + sample);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSamples.class, id));
		EhQualityInspectionSamplesDao dao = new EhQualityInspectionSamplesDao(context.configuration());
		dao.insert(sample);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionSamples.class, null);
	}

	@Override
	public void createQualityInspectionSampleCommunityMap(QualityInspectionSampleCommunityMap map) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionSampleCommunityMap.class));

		map.setId(id);
		LOGGER.info("createQualityInspectionSampleCommunityMap: " + map);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleCommunityMap.class, id));
		EhQualityInspectionSampleCommunityMapDao dao = new EhQualityInspectionSampleCommunityMapDao(context.configuration());
		dao.insert(map);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionSampleCommunityMap.class, null);
	}

	@Override
	public void createQualityInspectionSampleGroupMap(QualityInspectionSampleGroupMap map) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionSampleGroupMap.class));

		map.setId(id);
		LOGGER.info("createQualityInspectionSampleGroupMap: " + map);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleGroupMap.class, id));
		EhQualityInspectionSampleGroupMapDao dao = new EhQualityInspectionSampleGroupMapDao(context.configuration());
		dao.insert(map);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionSampleGroupMap.class, null);
	}

	@Override
	public QualityInspectionSamples findQualityInspectionSample(Long id, String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSamplesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLES);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.eq(id));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.OWNER_ID.eq(ownerId));

		List<QualityInspectionSamples> result = new ArrayList<QualityInspectionSamples>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSamples.class));
			return null;
		});
		if(result.size()==0)
			return null;

		return result.get(0);
	}

	@Override
	public List<QualityInspectionTasks> listTaskByParentId(Long parentId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.PARENT_ID.eq(parentId));

		List<QualityInspectionTasks> result = new ArrayList<QualityInspectionTasks>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTasks.class));
			return null;
		});

		return result;
	}

	@Override
	public void updateQualityInspectionSample(QualityInspectionSamples sample) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSamples.class, sample.getId()));
		EhQualityInspectionSamplesDao dao = new EhQualityInspectionSamplesDao(context.configuration());
		dao.update(sample);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionSamples.class, sample.getId());
	}

	@Override
	public void deleteQualityInspectionSampleCommunityMap(QualityInspectionSampleCommunityMap map) {
		assert(map.getId() != null);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleCommunityMap.class, map.getId()));
		EhQualityInspectionSampleCommunityMapDao dao = new EhQualityInspectionSampleCommunityMapDao(context.configuration());
		dao.delete(map);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionSampleCommunityMap.class, map.getId());
	}

	@Override
	public void deleteQualityInspectionSampleGroupMap(QualityInspectionSampleGroupMap map) {
		assert(map.getId() != null);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleGroupMap.class, map.getId()));
		EhQualityInspectionSampleGroupMapDao dao = new EhQualityInspectionSampleGroupMapDao(context.configuration());
		dao.delete(map);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionSampleGroupMap.class, map.getId());
	}

	@Override
	public List<QualityInspectionSampleCommunityMap> findQualityInspectionSampleCommunityMapBySample(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleCommunityMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.SAMPLE_ID.eq(sampleId));

		List<QualityInspectionSampleCommunityMap> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSampleCommunityMap.class));
			return null;
		});

		return result;
	}

	@Override
	public QualityInspectionSampleCommunityMap findQualityInspectionSampleCommunityMapBySampleAndCommunity(Long sampleId, Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleCommunityMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.SAMPLE_ID.eq(sampleId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId));

		List<QualityInspectionSampleCommunityMap> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSampleCommunityMap.class));
			return null;
		});
		if(result.size()==0)
			return null;

		return result.get(0);
	}

	@Override
	public List<QualityInspectionSampleGroupMap> findQualityInspectionSampleGroupMapBySample(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.SAMPLE_ID.eq(sampleId));

		List<QualityInspectionSampleGroupMap> result = new ArrayList<>();

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("findQualityInspectionSampleGroupMapBySample, sql=" + query.getSQL());
			LOGGER.debug("findQualityInspectionSampleGroupMapBySample, bindValues=" + query.getBindValues());
		}
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSampleGroupMap.class));
			return null;
		});

		return result;
	}

	@Override
	public QualityInspectionSampleGroupMap findQualityInspectionSampleGroupMapBySampleAndOrg(Long sampleId, Long organizationId, Long positionId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.SAMPLE_ID.eq(sampleId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.POSITION_ID.eq(positionId));

		List<QualityInspectionSampleGroupMap> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSampleGroupMap.class));
			return null;
		});

		if(result.size()==0)
			return null;

		return result.get(0);
	}

	@Override
	public List<QualityInspectionSamples> listQualityInspectionSamples(CrossShardListingLocator locator, Integer pageSize) {
		List<QualityInspectionSamples> samples = new ArrayList<>();

		if (locator.getShardIterator() == null) {
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhQualityInspectionSamples.class);
			ShardIterator shardIterator = new ShardIterator(accessSpec);
			locator.setShardIterator(shardIterator);
		}
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
			SelectQuery<EhQualityInspectionSamplesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLES);

			if(locator.getAnchor() != null && locator.getAnchor() != 0L){
				query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.lt(locator.getAnchor()));
			}
			query.addOrderBy(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.desc());
			query.addLimit(pageSize - samples.size());

			query.fetch().map((r) -> {

				samples.add(ConvertHelper.convert(r, QualityInspectionSamples.class));
				return null;
			});

			if (samples.size() >= pageSize) {
				locator.setAnchor(samples.get(samples.size() - 1).getId());
				return AfterAction.done;
			} else {
				locator.setAnchor(null);
			}
			return AfterAction.next;
		});

		return samples;
	}

	@Override
	public List<QualityInspectionTasks> listQualityInspectionTasks(CrossShardListingLocator locator, Integer pageSize) {
		List<QualityInspectionTasks> tasks = new ArrayList<>();

		if (locator.getShardIterator() == null) {
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhQualityInspectionTasks.class);
			ShardIterator shardIterator = new ShardIterator(accessSpec);
			locator.setShardIterator(shardIterator);
		}
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
			SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);

			if(locator.getAnchor() != null && locator.getAnchor() != 0L){
				query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
			}
			query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASKS.ID.desc());
			query.addLimit(pageSize - tasks.size());

			query.fetch().map((r) -> {

				tasks.add(ConvertHelper.convert(r, QualityInspectionTasks.class));
				return null;
			});

			if (tasks.size() >= pageSize) {
				locator.setAnchor(tasks.get(tasks.size() - 1).getId());
				return AfterAction.done;
			} else {
				locator.setAnchor(null);
			}
			return AfterAction.next;
		});

		return tasks;
	}

	@Override
	public Integer getSampleCommunities(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Integer count = context.selectCount()
				.from(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP)
				.where(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.SAMPLE_ID.eq(sampleId))
				.fetchOneInto(Integer.class);

		return count;
	}

	@Override
	public void createQualityInspectionSampleScoreStat(QualityInspectionSampleScoreStat stat) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionSampleScoreStat.class));

		stat.setId(id);
		stat.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		LOGGER.info("createQualityInspectionSampleScoreStat: " + stat);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleScoreStat.class, id));
		EhQualityInspectionSampleScoreStatDao dao = new EhQualityInspectionSampleScoreStatDao(context.configuration());
		dao.insert(stat);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionSampleScoreStat.class, null);
	}

	@Override
	public QualityInspectionSampleScoreStat findQualityInspectionSampleScoreStat(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionSampleScoreStat.class));

		QualityInspectionSampleScoreStat stat = context.select()
				.from(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT)
				.where(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.SAMPLE_ID.eq(sampleId))
				.fetchAnyInto(QualityInspectionSampleScoreStat.class);

		return stat;
	}

	@Override
	public Map<Long, QualityInspectionSampleScoreStat> getQualityInspectionSampleScoreStat(List<Long> sampleIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleScoreStatRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.SAMPLE_ID.in(sampleIds));

		Map<Long, QualityInspectionSampleScoreStat> result = new HashMap<>();
		query.fetch().map((r) -> {
			result.put(r.getSampleId(), ConvertHelper.convert(r, QualityInspectionSampleScoreStat.class));
			return null;
		});

		return result;
	}

	@Override
	public void updateQualityInspectionSampleScoreStat(QualityInspectionSampleScoreStat stat) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleScoreStat.class, stat.getId()));
		EhQualityInspectionSampleScoreStatDao dao = new EhQualityInspectionSampleScoreStatDao(context.configuration());
		dao.update(stat);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionSampleScoreStat.class, stat.getId());
	}

	@Override
	public void createQualityInspectionSampleCommunitySpecificationStat(QualityInspectionSampleCommunitySpecificationStat stat) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionSampleCommunitySpecificationStat.class));

		stat.setId(id);
		stat.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		LOGGER.info("createQualityInspectionSampleCommunitySpecificationStat: " + stat);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleCommunitySpecificationStat.class, id));
		EhQualityInspectionSampleCommunitySpecificationStatDao dao = new EhQualityInspectionSampleCommunitySpecificationStatDao(context.configuration());
		dao.insert(stat);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhQualityInspectionSampleCommunitySpecificationStat.class, null);
	}

	@Override
	public void updateQualityInspectionSampleCommunitySpecificationStat(QualityInspectionSampleCommunitySpecificationStat stat) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhQualityInspectionSampleCommunitySpecificationStat.class, stat.getId()));
		EhQualityInspectionSampleCommunitySpecificationStatDao dao = new EhQualityInspectionSampleCommunitySpecificationStatDao(context.configuration());
		dao.update(stat);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhQualityInspectionSampleCommunitySpecificationStat.class, stat.getId());
	}

	@Override
	public List<QualityInspectionSpecificationItemResults> listSpecifitionItemResultsBySampleId(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSpecificationItemResultsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SAMPLE_ID.eq(sampleId));

		List<QualityInspectionSpecificationItemResults> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecificationItemResults.class));
			return null;
		});

		return result;
	}

	@Override
	public List<QualityInspectionTasks> listQualityInspectionTasksBySample(Long sampleId, Timestamp startTime, Timestamp endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.PARENT_ID.eq(sampleId));

		if(startTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.ge(startTime));
		}

		if(endTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.le(endTime));
		}
		List<QualityInspectionTasks> result = new ArrayList<QualityInspectionTasks>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionTasks.class));
			return null;
		});

		return result;
	}

	@Override
	public List<QualityInspectionSpecificationItemResults> listSpecifitionItemResultsBySampleId(Long sampleId, Timestamp startTime, Timestamp endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSpecificationItemResultsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.SAMPLE_ID.eq(sampleId));

		if(startTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.CREATE_TIME.ge(startTime));
		}

		if(endTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.CREATE_TIME.le(endTime));
		}
		List<QualityInspectionSpecificationItemResults> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecificationItemResults.class));
			return null;
		});

		return result;
	}

	@Override
	public Map<Long, List<QualityInspectionSampleCommunitySpecificationStat>> listCommunitySpecifitionStatBySampleId(List<Long> sampleIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleCommunitySpecificationStatRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.SAMPLE_ID.in(sampleIds));


		Map<Long, List<QualityInspectionSampleCommunitySpecificationStat>> result = new HashMap<>();
		query.fetch().map((r) -> {
			if(result.get(r.getSampleId()) == null) {
				List<QualityInspectionSampleCommunitySpecificationStat> stat = new ArrayList<>();
				stat.add(ConvertHelper.convert(r, QualityInspectionSampleCommunitySpecificationStat.class));
				result.put(r.getSampleId(), stat);
			} else {
				result.get(r.getSampleId()).add(ConvertHelper.convert(r, QualityInspectionSampleCommunitySpecificationStat.class));
				result.put(r.getSampleId(), result.get(r.getSampleId()));
			}
			return null;
		});

		return result;
	}

	@Override
	public List<QualityInspectionSampleCommunitySpecificationStat> listSampleCommunitySpecifitionStat(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleCommunitySpecificationStatRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.SAMPLE_ID.eq(sampleId));


		List<QualityInspectionSampleCommunitySpecificationStat> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSampleCommunitySpecificationStat.class));
			return null;
		});

		return result;
	}

	@Override
	public Map<Long, Double> listCommunityScore(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleCommunitySpecificationStatRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.SAMPLE_ID.eq(sampleId));


		Map<Long, Double> result = new HashMap<>();
		query.fetch().map((r) -> {
//			CommunitySpecification cs = new CommunitySpecification();
//			cs.setCommunityId(r.getCommunityId());
//			cs.setSpecificationId(r.getSpecificationId());
			if(result.get(r.getCommunityId()) == null) {
				result.put(r.getCommunityId(), r.getDeductScore());
			} else {
				result.put(r.getCommunityId(), result.get(r.getCommunityId()) + r.getDeductScore());
			}
			return null;
		});

		return result;
	}

	@Override
	public Map<Long, Double> listSpecificationScore(Long sampleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleCommunitySpecificationStatRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.SAMPLE_ID.eq(sampleId));


		Map<Long, Double> result = new HashMap<>();
		query.fetch().map((r) -> {
			if(result.get(r.getSpecificationId()) == null) {
				result.put(r.getSpecificationId(), r.getDeductScore());
			} else {
				result.put(r.getSpecificationId(), result.get(r.getSpecificationId()) + r.getDeductScore());
			}
			return null;
		});

		return result;
	}

	@Override
	public List<QualityInspectionSamples> listActiveQualityInspectionSamples(Timestamp lastStatTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSamplesRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLES);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.STATUS.in(Status.ACTIVE.getCode()));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.START_TIME.le(new Timestamp(DateHelper.currentGMTTime().getTime())));

		if(lastStatTime != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLES.END_TIME.ge(lastStatTime));
		}

		List<QualityInspectionSamples> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSamples.class));
			return null;
		});

		return result;
	}

	@Override
	public QualityInspectionSampleCommunitySpecificationStat findBySampleCommunitySpecification(Long sampleId, Long communityId, Long specificationId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSampleCommunitySpecificationStatRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.SAMPLE_ID.eq(sampleId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.COMMUNITY_ID.eq(communityId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.SPECIFICATION_ID.eq(specificationId));


		List<QualityInspectionSampleCommunitySpecificationStat> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSampleCommunitySpecificationStat.class));
			return null;
		});

		if(result.size() == 0) {
			return  null;
		}
		return result.get(0);
	}

	@Override
	public Map<Long, QualityInspectionSpecifications> listSpecificationByIds(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.ID.in(ids));

		Map<Long, QualityInspectionSpecifications> result = new HashMap<>();
		query.fetch().map((r) -> {
			result.put(r.getId(), ConvertHelper.convert(r, QualityInspectionSpecifications.class));
			return null;
		});

		return result;
	}

	@Override
	public Map<Long, QualityInspectionTaskRecords> listLastRecordByTaskIds(Set<Long> taskIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionTaskRecordsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.TASK_ID.in(taskIds));
		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.CREATE_TIME.desc());

		Map<Long, QualityInspectionTaskRecords> result = new HashMap<>();
		query.fetch().map((r) -> {
			QualityInspectionTaskRecords record = result.get(r.getTaskId());
			if(record == null) {
				result.put(r.getTaskId(), ConvertHelper.convert(r, QualityInspectionTaskRecords.class));
			}else if(record != null && record.getCreateTime().before(r.getCreateTime())) {
				result.put(r.getTaskId(), ConvertHelper.convert(r, QualityInspectionTaskRecords.class));
			}
			return null;
		});

		return result;
	}

	@Override
	public void createQualityModelCommunityMap(QualityInspectionModelCommunityMap map) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhQualityInspectionModelCommunityMap.class));
		map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		map.setId(id);
		EhQualityInspectionModelCommunityMapDao dao = new EhQualityInspectionModelCommunityMapDao(context.configuration());
		dao.insert(map);
	}

	@Override
	public void deleteQualityModelCommunityMapByCommunityIdAndModelId(Long modelId, Long targetId, byte modelType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP)
				.where(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_ID.eq(modelId))
				.and(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType))
				.and(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.TARGET_ID.eq(targetId))
				.execute();
	}

	@Override
	public List<QualityInspectionModelCommunityMap> listQualityModelCommunityMapByTargetId(Long targetId, byte  modelType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectFrom(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP)
				.where(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.TARGET_ID.eq(targetId))
				.and(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType))
				.fetchInto(QualityInspectionModelCommunityMap.class);
	}

	@Override
	public List<Long> listQualityModelCommunityIdsMapByModelId(Long modelId, byte modelType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.select(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.TARGET_ID)
				.from(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP)
				.where(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_ID.eq(modelId))
				.and(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType))
				.fetchInto(Long.class);
	}

	/*@Override
	public void deleteQualityModelCommunityMapByCommunityAndSpecificationId(Long id, Long scopeId, Byte inspectionType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery query = context.deleteQuery(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(inspectionType));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.TARGET_ID.eq(scopeId));
		if (QualityModelType.SPECIFICATION.equals(QualityModelType.fromStatu(inspectionType))) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.SPECIFICATION_ID.eq(id));
		} else if (QualityModelType.CATEGORY.equals(QualityModelType.fromStatu(inspectionType))) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.CATEGORY_ID.eq(id));
		}
		query.execute();
	}

	@Override
	public void deleteQualityModelCommunityMapBySpecificationId(Long id, Byte inspectionType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery query = context.deleteQuery(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(inspectionType));
		if (QualityModelType.SPECIFICATION.equals(QualityModelType.fromStatu(inspectionType))) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.SPECIFICATION_ID.eq(id));
		} else if (QualityModelType.CATEGORY.equals(QualityModelType.fromStatu(inspectionType))) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.CATEGORY_ID.eq(id));
		}
		query.execute();
	}*/

	@Override
	public void deleteQualityModelCommunityMapByModelId(Long modelId ,byte modelType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery query = context.deleteQuery(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_MODEL_COMMUNITY_MAP.MODEL_ID.eq(modelId));
		query.execute();
	}

	@Override
	public List<QualityInspectionSpecifications> listAllCommunitiesChildrenSpecifications(String superiorPath, String ownerType, Long ownerId,List<Long> scopeIds, Byte inspectionType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<QualityInspectionSpecifications> result  = new ArrayList<QualityInspectionSpecifications>();
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.PATH.like(superiorPath));

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_CODE.eq(SpecificationScopeCode.COMMUNITY.getCode()));

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_ID.in(scopeIds));

		if(inspectionType != null)
			query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.INSPECTION_TYPE.eq(inspectionType));

		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()));

		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, QualityInspectionSpecifications.class));
			return null;
		});

		return result;
	}

	@Override
	public List<QualityInspectionSpecifications> listSpecifitionByParentIds(List<Long> parentIds) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.selectFrom(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS)
				.where(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
				.and(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.PARENT_ID.in(parentIds))
				.and(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.INSPECTION_TYPE.eq(SpecificationInspectionType.SPECIFICATION.getCode()))
				.and(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.ACTIVE.getCode()))
				.fetchInto(QualityInspectionSpecifications.class);
	}

	@Override
	public List<QualityInspectionSpecifications> listDeletedSpecifications(Long communityId, Long ownerId, String ownerType, Timestamp lastUpdateSyncTime) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionSpecificationsRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_CODE.eq(SpecificationScopeCode.COMMUNITY.getCode()));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.SCOPE_ID.eq(communityId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.STATUS.eq(QualityStandardStatus.INACTIVE.getCode()));
		query.addConditions(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.DELETE_TIME.gt(lastUpdateSyncTime));

		return  query.fetchInto(QualityInspectionSpecifications.class);
	}

	@Override
	public List<QualityInspectionTasks> listVerificationTasksRefactor(Integer offset, int pageSize, Timestamp startDate,
																	  Timestamp endDate, List<Long> executeStandardIds, List<Long> reviewStandardIds,
																	  List<ExecuteGroupAndPosition> groupDtos,
																	  ListingQueryBuilderCallback builderCallback) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class));
		List<QualityInspectionTasks> tasks = new ArrayList<QualityInspectionTasks>();
		SelectQuery<EhQualityInspectionTasksRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_TASKS);

		if (startDate != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.ge(startDate));
		}
		if (endDate != null) {
			query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.CREATE_TIME.le(endDate));
		}

		if (groupDtos != null) {//isAdmin =false
			Long executeUid = UserContext.currentUserId();
			if (executeUid != null && executeUid != 0) {
				Condition con = Tables.EH_QUALITY_INSPECTION_TASKS.OPERATOR_ID.eq(executeUid);
				con = con.and(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.CORRECT.getCode()));

				if (executeStandardIds != null) {
					Condition con1 = Tables.EH_QUALITY_INSPECTION_TASKS.STANDARD_ID.in(executeStandardIds)
							.and(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.in(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode(),
									QualityInspectionTaskStatus.EXECUTED.getCode(),QualityInspectionTaskStatus.DELAY.getCode(),QualityInspectionTaskStatus.NONE.getCode()));
					con = con.or(con1);
				}

				if (reviewStandardIds != null) {
					Condition con2 = Tables.EH_QUALITY_INSPECTION_TASKS.STANDARD_ID.in(reviewStandardIds)
							.and(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.EXECUTED.getCode()))
							.and(Tables.EH_QUALITY_INSPECTION_TASKS.REVIEW_RESULT.eq(QualityInspectionTaskReviewResult.NONE.getCode()));
					con = con.or(con2);
				}

				query.addConditions(con);
			}
		}

		builderCallback.buildCondition(null, query);

		query.addOrderBy(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS,Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME);
		query.addLimit(offset * (pageSize), pageSize + 1);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Query tasks by count, sql=" + query.getSQL());
			LOGGER.debug("Query tasks by count, bindValues=" + query.getBindValues());
		}

		query.fetch().map((EhQualityInspectionTasksRecord record) -> {
			tasks.add(ConvertHelper.convert(record, QualityInspectionTasks.class));
			return null;
		});

		return tasks;
	}

	@Override
	public void getTodayTaskCountStat(ListQualityInspectionTasksResponse response, List<Long> executeStandardIds, List<Long> reviewStandardIds, List<ExecuteGroupAndPosition> groupDtos, Timestamp todayBegin,ListingQueryBuilderCallback builderCallback) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhQualityInspectionTasks.class));
		SelectQuery<Record> query = context.selectQuery();

		if (groupDtos != null) {//isAdmin =false
			Long executeUid = UserContext.currentUserId();
			if (executeUid != null && executeUid != 0) {
				Condition con = Tables.EH_QUALITY_INSPECTION_TASKS.OPERATOR_ID.eq(executeUid);
				con = con.and(Tables.EH_QUALITY_INSPECTION_TASKS.RESULT.eq(QualityInspectionTaskResult.CORRECT.getCode()));

				if (executeStandardIds != null) {
					Condition con1 = Tables.EH_QUALITY_INSPECTION_TASKS.STANDARD_ID.in(executeStandardIds)
							.and(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()));
					con = con.or(con1);
				}

//				if (reviewStandardIds != null) {
//					Condition con2 = Tables.EH_QUALITY_INSPECTION_TASKS.STANDARD_ID.in(reviewStandardIds)
//							.and(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.EXECUTED.getCode()))
//							.and(Tables.EH_QUALITY_INSPECTION_TASKS.REVIEW_RESULT.eq(QualityInspectionTaskReviewResult.NONE.getCode()));
//					con = con.or(con2);
//				}

				query.addConditions(con);
			}
		}

		// 设置成当天的执行任务
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		final Field<?> todayCompleteCount = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.in(
				QualityInspectionTaskStatus.EXECUTED.getCode())
						.and(Tables.EH_QUALITY_INSPECTION_TASKS.EXECUTIVE_TIME.gt(getDayBegin(calendar))),
				QualityInspectionTaskStatus.EXECUTED.getCode());

		final Field<?> totayTasksCount = DSL.decode().when(Tables.EH_QUALITY_INSPECTION_TASKS.STATUS.eq(QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode()),
				QualityInspectionTaskStatus.WAITING_FOR_EXECUTING.getCode());


		final Field<?>[] fields = {DSL.count(totayTasksCount).as("totayTasksCount"),
				DSL.count(todayCompleteCount).as("todayCompleteCount")};

		builderCallback.buildCondition(null, query);

		query.addSelect(fields);
		query.addFrom(Tables.EH_QUALITY_INSPECTION_TASKS);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Query tasks count, sql=" + query.getSQL());
			LOGGER.debug("Query tasks count, bindValues=" + query.getBindValues());
		}

		query.fetch().map((r)->{
			response.setTodayExecutedCount(r.getValue("todayCompleteCount",Integer.class));
			response.setTodayTotalCount(r.getValue("totayTasksCount",Integer.class));
			return null;
		});
	}

	private Timestamp getDayBegin(Calendar todayStart) {
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return new Timestamp(todayStart.getTime().getTime());
	}

	@Override
	public List<QualityInspectionStandardGroupMap> listPlanGroupMapsByPlanId(Long standardId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhQualityInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP);
		query.addConditions(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.eq(standardId));
		return query.fetchInto(QualityInspectionStandardGroupMap.class);
	}
}
