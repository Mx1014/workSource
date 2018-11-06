package com.everhomes.equipment;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.equipment.AdminFlag;
import com.everhomes.rest.equipment.EquipmentOperateObjectType;
import com.everhomes.rest.equipment.EquipmentPlanStatus;
import com.everhomes.rest.equipment.EquipmentReviewStatus;
import com.everhomes.rest.equipment.EquipmentStandardStatus;
import com.everhomes.rest.equipment.EquipmentStatus;
import com.everhomes.rest.equipment.EquipmentTaskProcessResult;
import com.everhomes.rest.equipment.EquipmentTaskProcessType;
import com.everhomes.rest.equipment.EquipmentTaskResult;
import com.everhomes.rest.equipment.EquipmentTaskStatus;
import com.everhomes.rest.equipment.ExecuteGroupAndPosition;
import com.everhomes.rest.equipment.ItemResultNormalFlag;
import com.everhomes.rest.equipment.ItemResultStat;
import com.everhomes.rest.equipment.ItemValueType;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.ReviewedTaskStat;
import com.everhomes.rest.equipment.StandardAndStatus;
import com.everhomes.rest.equipment.StatTodayEquipmentTasksCommand;
import com.everhomes.rest.equipment.Status;
import com.everhomes.rest.equipment.TaskCountDTO;
import com.everhomes.rest.equipment.TasksStatData;
import com.everhomes.rest.pmNotify.PmNotifyConfigurationStatus;
import com.everhomes.rest.pmtask.PmTaskFlowStatus;
import com.everhomes.rest.quality.QualityGroupType;
import com.everhomes.scheduler.EquipmentInspectionScheduleJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPmNotifyRecords;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionAccessoriesDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionAccessoryMapDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionEquipmentAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionEquipmentLogsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionEquipmentParametersDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionEquipmentPlanMapDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionEquipmentStandardMapDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionEquipmentsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionItemResultsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionItemsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionPlanGroupMapDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionPlansDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionReviewDateDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionStandardGroupMapDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionStandardsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionTaskAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionTaskLogsDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionTasksDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionTemplateItemMapDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionTemplatesDao;
import com.everhomes.server.schema.tables.daos.EhEquipmentModelCommunityMapDao;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessories;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessoryMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentAttachments;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentLogs;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentParameters;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentPlanMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentStandardMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipments;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionItemResults;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionItems;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionPlanGroupMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionPlans;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionReviewDate;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandardGroupMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTaskAttachments;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTasks;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTemplateItemMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTemplates;
import com.everhomes.server.schema.tables.pojos.EhEquipmentModelCommunityMap;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionAccessoriesRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionAccessoryMapRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionEquipmentAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionEquipmentParametersRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionEquipmentStandardMapRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionEquipmentsRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionItemResultsRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionPlanGroupMapRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionPlansRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionReviewDateRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionStandardGroupMapRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionStandardsRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionTaskAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionTaskLogsRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionTasksRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionTemplateItemMapRecord;
import com.everhomes.server.schema.tables.records.EhEquipmentInspectionTemplatesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import com.mysql.jdbc.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@DependsOn("platformContext")
public class EquipmentProviderImpl implements EquipmentProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentProviderImpl.class);

    @Value("${equipment.ip}")
    private String equipmentIp;

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
    private EquipmentTasksSearcher equipmentTasksSearcher;

    @Autowired
    EquipmentStandardMapSearcher equipmentStandardMapSearcher;
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void init() {
        String cronExpression = configurationProvider.getValue(ConfigConstants.SCHEDULE_EQUIPMENT_TASK_TIME, "0 0 0 * * ? ");
        //  String cronExpression = configurationProvider.getValue(ConfigConstants.SCHEDULE_EQUIPMENT_TASK_TIME, "0 */5 * * * ?");
        //  String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
        String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
        LOGGER.info("================================================taskServer: " + taskServer + ", equipmentIp: " + equipmentIp);
        if (taskServer.equals(equipmentIp)) {
            if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                LOGGER.info("starting  equipment scheduler.....");
                String equipmentInspectionTriggerName = "EquipmentInspection " + System.currentTimeMillis();
                scheduleProvider.scheduleCronJob(equipmentInspectionTriggerName, equipmentInspectionTriggerName,
                        cronExpression, EquipmentInspectionScheduleJob.class, null);
            }
        }
    }
    // 因为@Cacheable会生成Spring cglib 代理导致多次执行   by jiarui 20180612
    /*@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }*/
    
    @Cacheable(value = "findEquipmentByIdAndOwner", key = "{#id, #ownerType, #ownerId}", unless = "#result == null")
    @Override
    public EquipmentInspectionEquipments findEquipmentById(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.eq(id));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.OWNER_ID.eq(ownerId));

        List<EquipmentInspectionEquipments> result = new ArrayList<EquipmentInspectionEquipments>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionEquipments.class));
            return null;
        });
        if (result.size() == 0)
            return null;
        return result.get(0);
    }

    @Override
    public void creatEquipmentStandard(EquipmentInspectionStandards standard) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionStandards.class));

        standard.setId(id);
        standard.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("creatEquipmentStandard: " + standard);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionStandards.class, id));
        EhEquipmentInspectionStandardsDao dao = new EhEquipmentInspectionStandardsDao(context.configuration());
        dao.insert(standard);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionStandards.class, null);

    }

    @Override
    public void updateEquipmentStandard(EquipmentInspectionStandards standard) {

        assert (standard.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionStandards.class, standard.getId()));
        EhEquipmentInspectionStandardsDao dao = new EhEquipmentInspectionStandardsDao(context.configuration());
        standard.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(standard);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionStandards.class, standard.getId());

    }

    @Caching(evict = {@CacheEvict(value = "findEquipmentByIdAndOwner", key = "{#equipment.id, #equipment.ownerType, #equipment.ownerId}"),
            @CacheEvict(value = "findEquipmentById", key = "#equipment.id"),
            @CacheEvict(value = "listQualifiedEquipmentStandardEquipments", key = "'AllEquipments'"),
            @CacheEvict(value = "findEquipmentByQrCodeToken", key = "#equipment.qrCodeToken")})
    @Override
    public void creatEquipmentInspectionEquipment(
            EquipmentInspectionEquipments equipment) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionEquipments.class));

        equipment.setId(id);
        equipment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        equipment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        //equipment.setNamespaceId(UserContext.getCurrentNamespaceId());

        LOGGER.info("creatEquipmentInspectionEquipment: " + equipment);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipments.class, id));
        EhEquipmentInspectionEquipmentsDao dao = new EhEquipmentInspectionEquipmentsDao(context.configuration());
        dao.insert(equipment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionEquipments.class, null);

    }

    @Override
    public void creatEquipmentInspectionAccessories(
            EquipmentInspectionAccessories accessory) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionAccessories.class));

        accessory.setId(id);

        LOGGER.info("creatEquipmentInspectionAccessories: " + accessory);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionAccessories.class, id));
        EhEquipmentInspectionAccessoriesDao dao = new EhEquipmentInspectionAccessoriesDao(context.configuration());
        dao.insert(accessory);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionAccessories.class, null);
    }

    @Override
    public EquipmentInspectionStandards findStandardById(Long id,
                                                         String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionStandardsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.ID.eq(id));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.OWNER_ID.eq(ownerId));

        List<EquipmentInspectionStandards> result = new ArrayList<EquipmentInspectionStandards>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionStandards.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result.get(0);
    }

    @Caching(evict = {@CacheEvict(value = "findEquipmentByIdAndOwner", key = "{#equipment.id, #equipment.ownerType, #equipment.ownerId}"),
            @CacheEvict(value = "findEquipmentById", key = "#equipment.id"),
            @CacheEvict(value = "listQualifiedEquipmentStandardEquipments", key = "'AllEquipments'"),
            @CacheEvict(value = "findEquipmentByQrCodeToken", key = "#equipment.qrCodeToken")})
    @Override
    public void updateEquipmentInspectionEquipment(
            EquipmentInspectionEquipments equipment) {
        assert (equipment.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipments.class, equipment.getId()));
        EhEquipmentInspectionEquipmentsDao dao = new EhEquipmentInspectionEquipmentsDao(context.configuration());

        dao.update(equipment);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionEquipments.class, equipment.getId());

    }

    @Override
    public void creatEquipmentParameter(
            EquipmentInspectionEquipmentParameters parameter) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionEquipmentParameters.class));

        parameter.setId(id);

        LOGGER.info("creatEquipmentParameter: " + parameter);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipmentParameters.class, id));
        EhEquipmentInspectionEquipmentParametersDao dao = new EhEquipmentInspectionEquipmentParametersDao(context.configuration());
        dao.insert(parameter);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionEquipmentParameters.class, null);
    }

    @Override
    public void updateEquipmentParameter(
            EquipmentInspectionEquipmentParameters parameter) {
        assert (parameter.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipmentParameters.class, parameter.getId()));
        EhEquipmentInspectionEquipmentParametersDao dao = new EhEquipmentInspectionEquipmentParametersDao(context.configuration());
        dao.update(parameter);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionEquipmentParameters.class, parameter.getId());

    }

    @Override
    public void creatEquipmentAccessoryMap(EquipmentInspectionAccessoryMap map) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionAccessoryMap.class));

        map.setId(id);

        LOGGER.info("creatEquipmentAccessoryMap: " + map);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionAccessoryMap.class, id));
        EhEquipmentInspectionAccessoryMapDao dao = new EhEquipmentInspectionAccessoryMapDao(context.configuration());
        dao.insert(map);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionAccessoryMap.class, null);

    }

    @Override
    public void updateEquipmentAccessoryMap(EquipmentInspectionAccessoryMap map) {
        assert (map.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionAccessoryMap.class, map.getId()));
        EhEquipmentInspectionAccessoryMapDao dao = new EhEquipmentInspectionAccessoryMapDao(context.configuration());
        dao.update(map);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionAccessoryMap.class, map.getId());

    }

    @Override
    public void creatEquipmentAttachment(EquipmentInspectionEquipmentAttachments eqAttachment) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionEquipmentAttachments.class));

        eqAttachment.setId(id);
        eqAttachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("creatEquipmentAttachment: " + eqAttachment);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipmentAttachments.class, id));
        EhEquipmentInspectionEquipmentAttachmentsDao dao = new EhEquipmentInspectionEquipmentAttachmentsDao(context.configuration());
        dao.insert(eqAttachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionEquipmentAttachments.class, null);
    }

    @Override
    public void deleteEquipmentAttachmentById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipmentAttachments.class));
        EhEquipmentInspectionEquipmentAttachmentsDao dao = new EhEquipmentInspectionEquipmentAttachmentsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public void deleteEquipmentAccessoryMapByEquipmentId(Long equipmentId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.EQUIPMENT_ID.eq(equipmentId))
                .execute();
    }

    @Override
    public List<EquipmentInspectionEquipmentAttachments> findEquipmentAttachmentsByEquipmentId(Long equipmentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentAttachmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.EQUIPMENT_ID.eq(equipmentId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.ATTACHMENT_TYPE.eq((byte) 1));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("findEquipmentAttachmentsByEquipmentId, sql=" + query.getSQL());
            LOGGER.debug("findEquipmentAttachmentsByEquipmentId, bindValues=" + query.getBindValues());
        }
        List<EquipmentInspectionEquipmentAttachments> attachments = new ArrayList<>();
        query.fetch().map((r) -> {
            attachments.add(ConvertHelper.convert(r, EquipmentInspectionEquipmentAttachments.class));
            return null;
        });

        return attachments;
    }

    @Override
    public List<EquipmentStandardMap> findByStandardId(
            Long standardId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STANDARD_ID.eq(standardId));

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));

        List<EquipmentStandardMap> result = new ArrayList<EquipmentStandardMap>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentStandardMap.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public List<EquipmentStandardMap> findByTarget(Long targetId, String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));

        //只展示带审核的关联标准和审核通过的 add by xiongying20170927
       // query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.REVIEW_STATUS.eq(EquipmentReviewStatus.WAITING_FOR_APPROVAL.getCode())
        //        .or(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.REVIEW_STATUS.eq(EquipmentReviewStatus.REVIEWED.getCode()).and(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.REVIEW_RESULT.eq(ReviewResult.QUALIFIED.getCode()))));
        //兼容上版本的关联关系
       // query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.REVIEW_RESULT.ne(ReviewResult.UNQUALIFIED.getCode()));
        List<EquipmentStandardMap> result = new ArrayList<EquipmentStandardMap>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentStandardMap.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public void updateEquipmentInspectionAccessories(
            EquipmentInspectionAccessories accessory) {
        assert (accessory.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionAccessories.class, accessory.getId()));
        EhEquipmentInspectionAccessoriesDao dao = new EhEquipmentInspectionAccessoriesDao(context.configuration());
        dao.update(accessory);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionAccessories.class, accessory.getId());

    }

    @Override
    public EquipmentInspectionAccessories findAccessoryById(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionAccessoriesRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.ID.eq(id));
        if(ownerId!=null && !StringUtils.isNullOrEmpty(ownerType)){
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.OWNER_TYPE.eq(ownerType));
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.OWNER_ID.eq(ownerId));
        }
        List<EquipmentInspectionAccessories> result = new ArrayList<EquipmentInspectionAccessories>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionAccessories.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result.get(0);
    }

    @Override
    public EquipmentInspectionTasks findEquipmentTaskById(Long id,
                                                          String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.eq(id));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_ID.eq(ownerId));

        List<EquipmentInspectionTasks> result = new ArrayList<EquipmentInspectionTasks>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result.get(0);
    }

    @Caching(evict = {@CacheEvict(value = "listEquipmentInspectionTasksUseCache", allEntries = true)})
    @Override
    public void creatEquipmentTask(EquipmentInspectionTasks task) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionTasks.class));

        task.setId(id);
        task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("creatEquipmentTask: " + task);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTasks.class, id));
        EhEquipmentInspectionTasksDao dao = new EhEquipmentInspectionTasksDao(context.configuration());
        dao.insert(task);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionTasks.class, null);

    }

    @Caching(evict = {@CacheEvict(value = "listEquipmentInspectionTasksUseCache", allEntries = true)})
    @Override
    public void updateEquipmentTask(EquipmentInspectionTasks task) {
        LOGGER.info("update task id = {}" + task.getId());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTasks.class, task.getId()));
        EhEquipmentInspectionTasksDao dao = new EhEquipmentInspectionTasksDao(context.configuration());
        dao.update(task);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionTasks.class, task.getId());

    }

    @Override
    public void createEquipmentInspectionTasksLogs(EquipmentInspectionTasksLogs log) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionTaskLogs.class));
        log.setId(id);
        log.setProcessTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createEquipmentInspectionTasksLogs: " + log);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTasks.class, log.getTaskId()));
        EhEquipmentInspectionTaskLogsDao dao = new EhEquipmentInspectionTaskLogsDao(context.configuration());
        dao.insert(log);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionTaskLogs.class, null);

    }

    @Override
    public void createEquipmentInspectionTasksAttachment(
            EquipmentInspectionTasksAttachments attachment) {

        assert (attachment.getLogId() != null);
        assert (attachment.getTaskId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTasks.class, attachment.getTaskId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionTaskAttachments.class));
        attachment.setId(id);

        EhEquipmentInspectionTaskAttachmentsDao dao = new EhEquipmentInspectionTaskAttachmentsDao(context.configuration());
        dao.insert(attachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionTaskAttachments.class, null);

    }

    @Override
    public List<EquipmentInspectionTasksLogs> listLogsByTaskId(ListingLocator locator, int count, Long taskId,
                                                               List<Byte> processType,List<Long> equipmentId) {

        List<EquipmentInspectionTasksLogs> result = new ArrayList<EquipmentInspectionTasksLogs>();
        assert (locator.getEntityId() != 0);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTaskLogsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS);

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.lt(locator.getAnchor()));
        }
        if(equipmentId!=null && equipmentId.size()>0){
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.EQUIPMENT_ID.in(equipmentId));
        }

        if (processType != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_TYPE.in(processType));
        }
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.TASK_ID.eq(taskId));
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.desc());
        query.addLimit(count);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query EquipmentInspectionTasksLogs by count, sql=" + query.getSQL());
            LOGGER.debug("Query EquipmentInspectionTasksLogs by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionTasksLogs.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public List<EquipmentInspectionStandards> listEquipmentInspectionStandards(
            CrossShardListingLocator locator, Integer pageSize) {

        List<EquipmentInspectionStandards> standards = new ArrayList<EquipmentInspectionStandards>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionStandards.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionStandardsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS);

            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.ID.lt(locator.getAnchor()));
            }

            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.ID.desc());
            query.addLimit(pageSize - standards.size());

            query.fetch().map((r) -> {

                standards.add(ConvertHelper.convert(r, EquipmentInspectionStandards.class));
                return null;
            });

            if (standards.size() >= pageSize) {
                locator.setAnchor(standards.get(standards.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return standards;
    }

    @Override
    public List<EquipmentInspectionAccessories> listEquipmentInspectionAccessories(
            CrossShardListingLocator locator, Integer pageSize) {

        List<EquipmentInspectionAccessories> accessories = new ArrayList<EquipmentInspectionAccessories>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionAccessories.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionAccessoriesRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES);

            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.ID.lt(locator.getAnchor()));
            }
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.STATUS.ne((byte) 0));
            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.ID.desc());
            query.addLimit(pageSize - accessories.size());

            query.fetch().map((r) -> {

                accessories.add(ConvertHelper.convert(r, EquipmentInspectionAccessories.class));
                return null;
            });

            if (accessories.size() >= pageSize) {
                locator.setAnchor(accessories.get(accessories.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return accessories;
    }

    @Override
    public List<EquipmentInspectionEquipments> listEquipments(
            CrossShardListingLocator locator, Integer pageSize) {
        List<EquipmentInspectionEquipments> equipments = new ArrayList<EquipmentInspectionEquipments>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionEquipments.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionEquipmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS);

            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.lt(locator.getAnchor()));
            }

            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.desc());
            query.addLimit(pageSize - equipments.size());

            query.fetch().map((r) -> {

                equipments.add(ConvertHelper.convert(r, EquipmentInspectionEquipments.class));
                return null;
            });

            if (equipments.size() >= pageSize) {
                locator.setAnchor(equipments.get(equipments.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return equipments;
    }

    @Override
    public List<EquipmentInspectionTasks> listEquipmentInspectionTasks(
            CrossShardListingLocator locator, Integer pageSize) {

        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.NONE.getCode()));
            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
            }

            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.desc());
            query.addLimit(pageSize - tasks.size());

            query.fetch().map((r) -> {

                tasks.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
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
    public EquipmentInspectionStandards findStandardById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionStandardsDao dao = new EhEquipmentInspectionStandardsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EquipmentInspectionStandards.class);
    }

    @Cacheable(value = "findEquipmentById", key = "#id", unless = "#result == null")
    @Override
    public EquipmentInspectionEquipments findEquipmentById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionEquipmentsDao dao = new EhEquipmentInspectionEquipmentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EquipmentInspectionEquipments.class);
    }

    @Override
    public EquipmentInspectionEquipments findEquipmentById(Long id, Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.eq(id));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.NAMESPACE_ID.eq(namespaceId));

        List<EquipmentInspectionEquipments> result = new ArrayList<EquipmentInspectionEquipments>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionEquipments.class));
            return null;
        });
        if (result.size() == 0)
            return null;
        return result.get(0);
    }

    @Override
    public EquipmentInspectionAccessories findAccessoryById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionAccessoriesDao dao = new EhEquipmentInspectionAccessoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EquipmentInspectionAccessories.class);
    }

    @Override
    public EquipmentInspectionTasks findEquipmentTaskById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionTasksDao dao = new EhEquipmentInspectionTasksDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EquipmentInspectionTasks.class);
    }

    @Override
    public List<EquipmentInspectionEquipmentParameters> listParametersByEquipmentId(
            Long equipmentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentParametersRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS.EQUIPMENT_ID.eq(equipmentId));

        List<EquipmentInspectionEquipmentParameters> result = new ArrayList<EquipmentInspectionEquipmentParameters>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionEquipmentParameters.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public List<EquipmentInspectionEquipmentAttachments> listAttachmentsByEquipmentId(
            Long equipmentId, Byte attachmentType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentAttachmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.EQUIPMENT_ID.eq(equipmentId));
        if(attachmentType!=null)
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.ATTACHMENT_TYPE.eq(attachmentType));

        List<EquipmentInspectionEquipmentAttachments> result = new ArrayList<EquipmentInspectionEquipmentAttachments>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionEquipmentAttachments.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public List<EquipmentInspectionTasks> listEquipmentInspectionTasks(
            String ownerType, Long ownerId, Long inspectionCategoryId, List<String> targetType, List<Long> targetId,
            List<Long> executeStandardIds, List<Long> reviewStandardIds, Integer offset, Integer pageSize) {
        long startTime = System.currentTimeMillis();
        List<EquipmentInspectionTasks> result = new ArrayList<EquipmentInspectionTasks>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
//		if(locator.getAnchor() != null) {
//            query.addConditions(Tables.EH_QUALITY_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
//        }
//		
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.NONE.getCode()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.DELAY.getCode()));
        if (targetType != null && targetType.size() > 0)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.in(targetType));

        if (targetId != null && targetId.size() > 0)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.in(targetId));

        if (inspectionCategoryId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        }


        if (executeStandardIds == null && reviewStandardIds == null) {
            Condition con1 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.CLOSE.getCode());
            con1 = con1.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_RESULT.eq(ReviewResult.NONE.getCode()));

            Condition con2 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.CLOSE.getCode());
            Condition con3 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()));
            con3 = con3.or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));
            con2 = con2.and(con3);

            Condition con = con1.or(con2);
            query.addConditions(con);
        }

        Condition con = null;
        if (executeStandardIds != null && executeStandardIds.size() > 0) {
            Condition con4 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.in(executeStandardIds);
            con4 = con4.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode()));

            Condition con5 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.OPERATOR_ID.eq(UserContext.current().getUser().getId());
            con5 = con5.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.IN_MAINTENANCE.getCode()));

            Condition con3 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()));
            con3 = con3.or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));

            con4 = con4.or(con5);
            con4 = con4.and(con3);
            con = con4;
        }

        if (reviewStandardIds != null && reviewStandardIds.size() > 0) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.in(reviewStandardIds));

            //巡检完成关闭的任务
            Condition con1 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.CLOSE.getCode());
            con1 = con1.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_RESULT.eq(ReviewResult.NONE.getCode()));
            con1 = con1.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_EXPIRED_DATE.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));
            //需维修待审核的任务
            Condition con2 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.NEED_MAINTENANCE.getCode());
            Condition con3 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()));
            con3 = con3.or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));
            con2 = con2.and(con3);

//			Condition con = con1.or(con2);

            if (con == null) {
                con = con1.or(con2);
            } else {
                con = con.or(con1.or(con2));
            }

        }

        if (con != null) {
            query.addConditions(con);
        }


        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_EXPIRE_TIME, Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME);
//        query.addLimit(pageSize);
        query.addLimit(offset * (pageSize - 1), pageSize);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query tasks by count, sql=" + query.getSQL());
            LOGGER.debug("Query tasks by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhEquipmentInspectionTasksRecord record) -> {
            result.add(ConvertHelper.convert(record, EquipmentInspectionTasks.class));
            return null;
        });

        long endTime = System.currentTimeMillis();
        LOGGER.debug("TrackUserRelatedCost: listEquipmentInspectionTasks resultSize = " + result.size()
                + ", maxCount = " + pageSize + ", elapse=" + (endTime - startTime));

        return result;
    }

    @Override
    public List<EquipmentInspectionTasks> listDelayTasks(Long inspectionCategoryId, List<Long> planIds, String targetType, Long targetId,Integer offset, Integer pageSize, Byte adminFlag, Timestamp startTime) {
        List<EquipmentInspectionTasks> result = new ArrayList<EquipmentInspectionTasks>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.DELAY.getCode()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.eq(targetId));

        if (inspectionCategoryId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        }

        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag))) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.in(planIds));
        }

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(startTime));
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.desc());
//        query.addLimit(pageSize);
        //由于app端没做分页 去掉limit条件
//		query.addLimit(offset * (pageSize-1), pageSize);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query tasks by count, sql=" + query.getSQL());
            LOGGER.debug("Query tasks by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhEquipmentInspectionTasksRecord record) -> {
            result.add(ConvertHelper.convert(record, EquipmentInspectionTasks.class));
            return null;
        });

        return result;
    }

    @Cacheable(value = "listQualifiedEquipmentStandardEquipments", key = "'AllEquipments'", unless = "#result.size() == 0")
    @Override
    public List<EquipmentInspectionEquipments> listQualifiedEquipmentStandardEquipments() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.STATUS.eq(EquipmentStatus.IN_USE.getCode()));
//		query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.STANDARD_ID.ne(0L));
//		query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.REVIEW_STATUS.eq(EquipmentReviewStatus.REVIEWED.getCode()));
//		query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.REVIEW_RESULT.eq(ReviewResult.QUALIFIED.getCode()));

        List<EquipmentInspectionEquipments> result = new ArrayList<EquipmentInspectionEquipments>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionEquipments.class));
            return null;
        });
        if (result.size() == 0)
            return null;
        return result;
    }

    @Override
    public List<EquipmentInspectionTasks> listTasksByEquipmentId(Long equipmentId, List<Long> standardIds, Timestamp startDate, Timestamp endDate,
                                                                 CrossShardListingLocator locator, Integer pageSize, List<Byte> taskStatus) {
        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);

            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
            }

            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EQUIPMENT_ID.eq(equipmentId));

            if (standardIds != null && standardIds.size() > 0)
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.in(standardIds));

            if (taskStatus != null && taskStatus.size() > 0)
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(taskStatus));

            if (startDate != null && !"".equals(startDate)) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(startDate));
            }

            if (endDate != null && !"".equals(endDate)) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.le(endDate));
            }
            //产品要求把已失效的任务也显示出来 add by xiongying20170217
//            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.NONE.getCode()));
            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.desc());
            query.addLimit(pageSize - tasks.size());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("listTasksByEquipmentId, sql=" + query.getSQL());
                LOGGER.debug("listTasksByEquipmentId, bindValues=" + query.getBindValues());
            }

            query.fetch().map((r) -> {

                tasks.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
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
    public List<EquipmentInspectionTasks> listTasksByEquipmentIdAndStandards(List<StandardAndStatus> standards, Timestamp startDate, Timestamp endDate, CrossShardListingLocator locator, Integer pageSize) {
        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);

            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
            }

            if (standards != null && standards.size() > 0) {
                Condition standardCon = null;
                for (StandardAndStatus standardAndStatus : standards) {
                   // Condition con = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.eq(standardAndStatus.getStandardId());
                    Condition con = Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.eq(standardAndStatus.getPlanId());
                    con = con.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(standardAndStatus.getTaskStatus()));

                    if (standardCon == null) {
                        standardCon = con;
                    } else {
                        standardCon = standardCon.or(con);
                    }
                }
                query.addConditions(standardCon);
            }else {
                return null;
            }

            if (startDate != null) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(startDate));
            }

            if (endDate != null) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.le(endDate));
            }
            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.desc());
            query.addLimit(pageSize - tasks.size());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("listTasksByEquipmentId, sql=" + query.getSQL());
                LOGGER.debug("listTasksByEquipmentId, bindValues=" + query.getBindValues());
            }

            query.fetch().map((r) -> {

                tasks.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
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
    public List<Long> listStandardIdsByType(Byte type) {
        List<Long> standardIds = new ArrayList<Long>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionStandardsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.STANDARD_TYPE.eq(type));

        query.fetch().map((r) -> {
            standardIds.add(r.getId());
            return null;
        });
        if (standardIds.size() == 0)
            return null;

        return standardIds;
    }

    @Override
    public List<EquipmentInspectionAccessoryMap> listAccessoryMapByEquipmentId(
            Long equipmentId) {
        List<EquipmentInspectionAccessoryMap> map = new ArrayList<EquipmentInspectionAccessoryMap>();


        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionAccessoryMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.EQUIPMENT_ID.eq(equipmentId));

        query.fetch().map((r) -> {

            map.add(ConvertHelper.convert(r, EquipmentInspectionAccessoryMap.class));
            return null;
        });

        if (map.size() == 0)
            return null;

        return map;
    }

    @Override
    public List<EquipmentInspectionTasksAttachments> listTaskAttachmentsByLogId(
            Long logId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTaskAttachmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS.LOG_ID.eq(logId));

        List<EquipmentInspectionTasksAttachments> result = new ArrayList<EquipmentInspectionTasksAttachments>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionTasksAttachments.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public EquipmentInspectionTasksLogs getNearestReviewLogAfterProcess(
            Long taskId, Long id) {
        List<EquipmentInspectionTasksLogs> result = new ArrayList<EquipmentInspectionTasksLogs>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTaskLogsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS);

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.gt(id));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.TASK_ID.eq(taskId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_TYPE.eq(EquipmentTaskProcessType.REVIEW.getCode()));
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.asc());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query EquipmentInspectionTasksLogs by count, sql=" + query.getSQL());
            LOGGER.debug("Query EquipmentInspectionTasksLogs by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionTasksLogs.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result.get(0);
    }

    @Override
    public Long createEquipmentInspectionTemplates(
            EquipmentInspectionTemplates template) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionTemplates.class));

        template.setId(id);
        template.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        template.setStatus(Status.ACTIVE.getCode());
       // template.setNamespaceId(UserContext.getCurrentNamespaceId());

        LOGGER.info("createEquipmentInspectionTemplates: " + template);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTemplates.class, id));
        EhEquipmentInspectionTemplatesDao dao = new EhEquipmentInspectionTemplatesDao(context.configuration());
        dao.insert(template);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionTemplates.class, null);
        return id;
    }

    @Override
    public void updateEquipmentInspectionTemplates(
            EquipmentInspectionTemplates template) {
        assert (template.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTemplates.class, template.getId()));
        EhEquipmentInspectionTemplatesDao dao = new EhEquipmentInspectionTemplatesDao(context.configuration());
        dao.update(template);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionTemplates.class, template.getId());	
		
	}

    @Override
    public EquipmentInspectionTemplates findEquipmentInspectionTemplate(
            Long id, Long ownerId, String ownerType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTemplatesRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.ID.eq(id));
//		query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.OWNER_TYPE.eq(ownerType));
//		query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.OWNER_ID.eq(ownerId));

        List<EquipmentInspectionTemplates> result = new ArrayList<EquipmentInspectionTemplates>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionTemplates.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result.get(0);
    }

    @Override
    public Long createEquipmentInspectionItems(EquipmentInspectionItems item) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionItems.class));

        item.setId(id);

        LOGGER.info("createEquipmentInspectionItems: " + item);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionItems.class, id));
        EhEquipmentInspectionItemsDao dao = new EhEquipmentInspectionItemsDao(context.configuration());
        dao.insert(item);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionItems.class, null);
        return id;
    }

    @Override
    public Long updateEquipmentInspectionItems(EquipmentInspectionItems item) {
        assert (item.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionItems.class, item.getId()));
        EhEquipmentInspectionItemsDao dao = new EhEquipmentInspectionItemsDao(context.configuration());
        dao.update(item);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionItems.class, item.getId());

        return item.getId();
    }

    @Override
    public void createEquipmentInspectionTemplateItemMap(
            EquipmentInspectionTemplateItemMap map) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionTemplateItemMap.class));

        map.setId(id);
        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createEquipmentInspectionTemplateItemMap: " + map);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTemplateItemMap.class, id));
        EhEquipmentInspectionTemplateItemMapDao dao = new EhEquipmentInspectionTemplateItemMapDao(context.configuration());
        dao.insert(map);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionTemplateItemMap.class, null);

    }

    @Override
    public void deleteEquipmentInspectionTemplateItemMap(Long id) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionTemplateItemMap.class));
        EhEquipmentInspectionTemplateItemMapDao dao = new EhEquipmentInspectionTemplateItemMapDao(context.configuration());
        dao.deleteById(id);

    }

    @Override
    public List<EquipmentInspectionTemplateItemMap> listEquipmentInspectionTemplateItemMap(Long templateId) {
        List<EquipmentInspectionTemplateItemMap> maps = new ArrayList<EquipmentInspectionTemplateItemMap>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTemplateItemMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.TEMPLATE_ID.eq(templateId));

        query.fetch().map((r) -> {
            maps.add(ConvertHelper.convert(r, EquipmentInspectionTemplateItemMap.class));
            return null;
        });
        if (maps.size() == 0)
            return null;

        return maps;
    }

    @Override
    public EquipmentInspectionTemplateItemMap findEquipmentInspectionTemplateItemMap(
            Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionTemplateItemMapDao dao = new EhEquipmentInspectionTemplateItemMapDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EquipmentInspectionTemplateItemMap.class);
    }

    @Override
    public EquipmentInspectionItems findEquipmentInspectionItem(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionItemsDao dao = new EhEquipmentInspectionItemsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EquipmentInspectionItems.class);
    }

    @Override
    public List<EquipmentInspectionTemplates> listInspectionTemplates(
            Long ownerId, String ownerType, String name) {
        List<EquipmentInspectionTemplates> templates = new ArrayList<EquipmentInspectionTemplates>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTemplatesRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.STATUS.eq(Status.ACTIVE.getCode()));

        if (!StringUtils.isNullOrEmpty(name)) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.NAME.like("%" + name + "%"));
        }

        query.fetch().map((r) -> {
            templates.add(ConvertHelper.convert(r, EquipmentInspectionTemplates.class));
            return null;
        });
        if (templates.size() == 0)
            return null;

        return templates;
    }

	@Override
	public List<EquipmentInspectionTemplates> listInspectionTemplates(Integer namespaceId, String name,Long targetId) {
		List<EquipmentInspectionTemplates> templates = new ArrayList<EquipmentInspectionTemplates>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTemplatesRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.STATUS.eq(Status.ACTIVE.getCode()));

		if(!StringUtils.isNullOrEmpty(name)) {
			query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.NAME.like("%"+name+"%"));
		}
		//如果为项目查看自定义 增加项目id过滤
		if(targetId!=null && targetId!=0L) {
			query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.TARGET_ID.eq(targetId));
		}

        query.fetch().map((r) -> {
            templates.add(ConvertHelper.convert(r, EquipmentInspectionTemplates.class));
            return null;
        });
        if (templates.size() == 0)
            return null;

        return templates;
    }

    @Override
    public List<EquipmentInspectionStandards> listEquipmentInspectionStandardsByTemplateId(
            Long templateId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionStandardsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.TEMPLATE_ID.eq(templateId));

        List<EquipmentInspectionStandards> result = new ArrayList<EquipmentInspectionStandards>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionStandards.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public void createEquipmentStandardMap(EquipmentStandardMap map) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionEquipmentStandardMap.class));

        map.setId(id);
        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        map.setStatus(Status.ACTIVE.getCode());

        LOGGER.info("createEquipmentStandardMap: " + map);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipmentStandardMap.class, id));
        EhEquipmentInspectionEquipmentStandardMapDao dao = new EhEquipmentInspectionEquipmentStandardMapDao(context.configuration());
        dao.insert(map);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionEquipmentStandardMap.class, null);

    }
    @Override
    public  void  inActiveEquipmentStandardMap(EquipmentStandardMap map){
        map.setStatus(Status.INACTIVE.getCode());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionEquipmentStandardMapDao dao = new EhEquipmentInspectionEquipmentStandardMapDao(context.configuration());
        dao.update(map);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionEquipmentStandardMap.class, map.getId());
    }

    @Override
    public void populateEquipments(EquipmentInspectionStandards standard) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STANDARD_ID.eq(standard.getId()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));
        List<EquipmentInspectionEquipments> equipments = new ArrayList<>();
        query.fetch().map((r)->{
            equipments.add(findEquipmentById(r.getTargetId()));
            return null;
        });
        standard.setEquipments(equipments);
    }

    @Override
    public void populateItems(EquipmentInspectionStandards standard) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTemplateItemMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.TEMPLATE_ID.eq(standard.getTemplateId()));
        List<EquipmentInspectionItems> items = new ArrayList<>();
        query.fetch().map((r)->{
            items.add(findEquipmentInspectionItem(r.getItemId()));
            return null;
        });
        if (items.size() > 0) {
            List<EquipmentInspectionItems> sortedItems = new ArrayList<>();
            sortedItems = items.stream().sorted(Comparator.comparing(EquipmentInspectionItems::getDefaultOrder)).collect(Collectors.toList());
            standard.setItems(sortedItems);
        }
    }

    @Override
    public EquipmentInspectionPlans createEquipmentInspectionPlans(EquipmentInspectionPlans plan) {
        Long planId = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionPlans.class));
        plan.setId(planId);
        plan.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionPlansDao dao = new EhEquipmentInspectionPlansDao(context.configuration());
        dao.insert(plan);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionPlans.class, null);

        return plan;
    }

    @Override
    public EquipmentInspectionPlans getEquipmmentInspectionPlanById(Long planId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhEquipmentInspectionPlansDao dao = new EhEquipmentInspectionPlansDao(context.configuration());
        return ConvertHelper.convert(dao.findById(planId), EquipmentInspectionPlans.class);
    }

    @Override
    public void createEquipmentPlanMaps(EquipmentInspectionEquipmentPlanMap map) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionEquipmentPlanMap.class));
        map.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionEquipmentPlanMapDao dao = new EhEquipmentInspectionEquipmentPlanMapDao(context.configuration());
        dao.insert(map);
        DaoHelper.publishDaoAction(DaoAction.CREATE,EhEquipmentInspectionEquipmentPlanMap.class,null);
    }

    @Override
    public List<EquipmentInspectionEquipmentPlanMap> getEquipmentInspectionPlanMap(Long planId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.PLAN_ID.eq(planId))
                .orderBy(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.DEFAULT_ORDER)
                .fetchInto(EquipmentInspectionEquipmentPlanMap.class);
    }

    @Override
    public void deleteEquipmentInspectionPlanById(Long  planId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionPlansDao dao = new EhEquipmentInspectionPlansDao(context.configuration());
        //dao.deleteById(id);
        dao.deleteById(planId);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionPlans.class, planId);
    }

    @Override
    public void deleteEquipmentInspectionPlanMap(Long planId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.PLAN_ID.eq(planId))
                .execute();
    }

    @Override
    public List<EquipmentInspectionPlans> ListEquipmentInspectionPlans(ListingLocator locator, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<EquipmentInspectionPlans> plans = new ArrayList<>();
        SelectQuery<EhEquipmentInspectionPlansRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_PLANS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_PLANS.STATUS.ne(EquipmentPlanStatus.INACTIVE.getCode()));
        if (locator.getAnchor() != null && locator.getAnchor() != 0) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_PLANS.ID.lt(locator.getAnchor()));
        }
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_PLANS.ID.desc());
        query.addLimit(pageSize);
        query.fetch().map((r) -> {
            plans.add(ConvertHelper.convert(r, EquipmentInspectionPlans.class));
            return null;
        });
        if (pageSize <= plans.size()) {
            locator.setAnchor(plans.get(plans.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return plans;
    }

    @Override
    public void updateEquipmentInspectionPlan(EquipmentInspectionPlans plan) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        plan.setOperatorUid(UserContext.currentUserId());
        plan.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEquipmentInspectionPlansDao dao = new EhEquipmentInspectionPlansDao(context.configuration());
        dao.update(plan);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionPlans.class, plan.getId());
    }

    @Override
    public void deleteEquipmentInspectionStandardMapByStandardId(Long standardId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STANDARD_ID.eq(standardId));
        query.fetch().map((r)->{
            deleteEquipmentInspectionEquipmentStandardMap(r.getId());
            equipmentStandardMapSearcher.feedDoc(ConvertHelper.convert(r,EquipmentStandardMap.class));
            return null;
        });
    }

    private void deleteEquipmentInspectionEquipmentStandardMap(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP)
                .set(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS,(Status.INACTIVE.getCode()))
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.eq(id))
                .execute();
    }

    @Override
    public void deleteEquipmentPlansMapByStandardId(Long standardId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.STANDARD_ID.eq(standardId))
                .execute();
    }

    @Override
    public List<EquipmentInspectionPlans> listQualifiedEquipmentInspectionPlans() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<EquipmentInspectionPlans> plans = new ArrayList<>();
        LOGGER.info("listQualifiedEquipmentInspectionPlans provider");
        SelectQuery<EhEquipmentInspectionPlansRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_PLANS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_PLANS.STATUS.eq(EquipmentPlanStatus.QUALIFIED.getCode()));

        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_PLANS.ID.desc());
        query.fetch().map((r) -> {
            plans.add(ConvertHelper.convert(r, EquipmentInspectionPlans.class));
            return null;
        });
        LOGGER.info("listQualifiedEquipmentInspectionPlans" + query.getSQL());
        LOGGER.info("plans.size()={}" + plans.size());

        return plans;
    }

    @Override
    public List<EquipmentInspectionEquipmentPlanMap> listPlanMapByEquipmentId(Long equipmentId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.EQUIPMENT_ID.eq(equipmentId))
                .fetchInto(EquipmentInspectionEquipmentPlanMap.class);
    }

    @Override
    public List<EquipmentInspectionTasks> listTaskByPlanMaps(List<Long> planIds,
      Timestamp startTime, Timestamp endTime, ListingLocator locator, int pageSize,List<Byte> taskStatus) {

        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        if (startTime != null && !"".equals(startTime)) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(startTime));
        }

        if (endTime != null && !"".equals(endTime)) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.le(endTime));
        }
        if (taskStatus != null && taskStatus.size() > 0) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(taskStatus));
        }
        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
        }
        if (planIds != null && planIds.size() > 0) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.in(planIds));
        }
        query.addLimit(pageSize);
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.desc());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listTaskByPlanMaps, sql=" + query.getSQL());
            LOGGER.debug("listTaskByPlanMaps, bindValues=" + query.getBindValues());
        }

        query.fetch().map((r) -> {
            tasks.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
            return null;
        });

        return tasks;

    }

    @Override
    public void updateEquipmentStandardMap(EquipmentStandardMap map) {
        assert (map.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionEquipmentStandardMap.class, map.getId()));
        EhEquipmentInspectionEquipmentStandardMapDao dao = new EhEquipmentInspectionEquipmentStandardMapDao(context.configuration());
        dao.update(map);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionEquipmentStandardMap.class, map.getId());
    }

    @Override
    public List<EquipmentInspectionTasks> listTasksByStandardId(
            Long standardId, CrossShardListingLocator locator, Integer pageSize) {
        List<EquipmentInspectionTasks> tasks = new ArrayList<EquipmentInspectionTasks>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);

            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
            }

            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.eq(standardId));

            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.NONE.getCode()));
            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.desc());
            query.addLimit(pageSize - tasks.size());

            query.fetch().map((r) -> {

                tasks.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
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
    public EquipmentStandardMap findEquipmentStandardMapById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.eq(id));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));

        List<EquipmentStandardMap> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentStandardMap.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result.get(0);
    }

    @Override
    public EquipmentStandardMap findEquipmentStandardMap(Long id,
                                                         Long standardId, Long targetId, String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.eq(id));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STANDARD_ID.eq(standardId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));

        List<EquipmentStandardMap> result = new ArrayList<EquipmentStandardMap>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentStandardMap.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result.get(0);
    }

    @Override
    public List<EquipmentStandardMap> findEquipmentStandardMap(Long standardId, Long targetId, String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STANDARD_ID.eq(standardId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));

        List<EquipmentStandardMap> result = new ArrayList<EquipmentStandardMap>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentStandardMap.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public void createEquipmentInspectionItemResults(
            EquipmentInspectionItemResults result) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionItemResults.class));

        result.setId(id);
        result.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("createEquipmentInspectionItemResults: " + result);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionItemResults.class, id));
        EhEquipmentInspectionItemResultsDao dao = new EhEquipmentInspectionItemResultsDao(context.configuration());
        dao.insert(result);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionItemResults.class, null);
    }

    @Override
    public List<EquipmentInspectionItemResults> findEquipmentInspectionItemResultsByLogId(
            Long logId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionItemResultsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.TASK_LOG_ID.eq(logId));

        List<EquipmentInspectionItemResults> result = new ArrayList<EquipmentInspectionItemResults>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionItemResults.class));
            return null;
        });
        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public List<EquipmentStandardMap> listQualifiedEquipmentStandardMap(Long equipmentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);

        if (equipmentId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.TARGET_ID.eq(equipmentId));
        }

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.REVIEW_STATUS.eq(EquipmentReviewStatus.REVIEWED.getCode()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.REVIEW_RESULT.eq(ReviewResult.QUALIFIED.getCode()));

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listQualifiedEquipmentStandardMap, sql=" + query.getSQL());
            LOGGER.debug("listQualifiedEquipmentStandardMap, bindValues=" + query.getBindValues());
        }
        List<EquipmentStandardMap> result = new ArrayList<EquipmentStandardMap>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentStandardMap.class));
            return null;
        });

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listQualifiedEquipmentStandardMap, result=" + result.toString());
        }

        if (result.size() == 0)
            return null;

        return result;
    }

    @Override
    public List<EquipmentStandardMap> listEquipmentStandardMap(
            CrossShardListingLocator locator, Integer pageSize) {

        List<EquipmentStandardMap> maps = new ArrayList<EquipmentStandardMap>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEquipmentInspectionEquipmentStandardMap.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEquipmentInspectionEquipmentStandardMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP);

            if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.lt(locator.getAnchor()));
            }
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()));
            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.desc());
            query.addLimit(pageSize - maps.size());

            query.fetch().map((r) -> {

                maps.add(ConvertHelper.convert(r, EquipmentStandardMap.class));
                return null;
            });

            if (maps.size() >= pageSize) {
                locator.setAnchor(maps.get(maps.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return maps;
    }

    @Override
    public void closeDelayTasks() {
        Timestamp current = new Timestamp(System.currentTimeMillis());

        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhEquipmentInspectionTasks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
            if (locator.getAnchor() != null && locator.getAnchor() != 0)
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.gt(locator.getAnchor()));


            Condition conExecutive = Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.lt(current);
            conExecutive = conExecutive.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode()));

            Condition conMaintenance = Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.lt(current).and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.lt(current));
            conMaintenance = conMaintenance.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.IN_MAINTENANCE.getCode()));

            conExecutive = conExecutive.or(conMaintenance);
            query.addConditions(conExecutive);

            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.asc());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("closeDelayTasks, sql=" + query.getSQL());
                LOGGER.debug("closeDelayTasks, bindValues=" + query.getBindValues());
            }

            query.fetch().map((r) -> {
                EquipmentInspectionTasks task = ConvertHelper.convert(r, EquipmentInspectionTasks.class);
                closeTask(task);
                equipmentTasksSearcher.feedDoc(task);
                return null;
            });
            return AfterAction.next;
        });
    }

    @Override
    public void closeExpiredReviewTasks() {
        Timestamp current = new Timestamp(System.currentTimeMillis());

        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhEquipmentInspectionTasks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
            if (locator.getAnchor() != null && locator.getAnchor() != 0)
                query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.gt(locator.getAnchor()));

            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(EquipmentTaskStatus.CLOSE.getCode(), EquipmentTaskStatus.NEED_MAINTENANCE.getCode()));
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_EXPIRED_DATE.lt(current));
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_RESULT.eq(ReviewResult.NONE.getCode()));
            query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.asc());


            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("closeDelayTasks, sql=" + query.getSQL());
                LOGGER.debug("closeDelayTasks, bindValues=" + query.getBindValues());
            }

            query.fetch().map((r) -> {
                EquipmentInspectionTasks task = ConvertHelper.convert(r, EquipmentInspectionTasks.class);
                closeReviewTasks(task);
                equipmentTasksSearcher.feedDoc(task);
                return null;
            });
            return AfterAction.next;
        });

    }

    @Caching(evict = {@CacheEvict(value = "listEquipmentInspectionTasksUseCache", allEntries = true)})
    @Override
    public void closeTask(EquipmentInspectionTasks task) {
        LOGGER.debug("EquipmentInspectionTasks closeTask before close: {}", task);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        if (task.getStatus().equals(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode()))

        task.setStatus(EquipmentTaskStatus.DELAY.getCode());
        //processTime for deleteing plan and update delay status recording  updateTime
        task.setProcessTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEquipmentInspectionTasks t = ConvertHelper.convert(task, EhEquipmentInspectionTasks.class);
        EhEquipmentInspectionTasksDao dao = new EhEquipmentInspectionTasksDao(context.configuration());
        dao.update(t);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionTasks.class, t.getId());

    }

    @Caching(evict = {@CacheEvict(value = "listEquipmentInspectionTasksUseCache", allEntries = true)})
    @Override
    public void closeReviewTasks(EquipmentInspectionTasks task) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        //task.setReviewResult(ReviewResult.REVIEW_DELAY.getCode());  3.0.3
        task.setStatus(EquipmentTaskStatus.REVIEW_DELAY.getCode());//3.0.3
//		task.setReviewTime(new Timestamp(System.currentTimeMillis()));
        EhEquipmentInspectionTasks t = ConvertHelper.convert(task, EhEquipmentInspectionTasks.class);
        EhEquipmentInspectionTasksDao dao = new EhEquipmentInspectionTasksDao(context.configuration());
        dao.update(t);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEquipmentInspectionTasks.class, t.getId());
    }

    @Cacheable(value = "findEquipmentByQrCodeToken", key = "#qrCodeToken", unless = "#result == null")
    @Override
    public EquipmentInspectionEquipments findEquipmentByQrCodeToken(
            String qrCodeToken) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.QR_CODE_TOKEN.eq(qrCodeToken));

        List<EquipmentInspectionEquipments> result = new ArrayList<EquipmentInspectionEquipments>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionEquipments.class));
            return null;
        });
        if (result.size() == 0)
            return null;
        return result.get(0);
    }

    @Override
    public List<EquipmentInspectionCategories> listEquipmentInspectionCategories(
            Long ownerId, Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionCategoriesRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        //仅按域空间区分 分公司拿不到总公司的类型 by xiongying20170324
//		query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.OWNER_ID.eq(ownerId));

        List<EquipmentInspectionCategories> result = new ArrayList<EquipmentInspectionCategories>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionCategories.class));
            return null;
        });
        return result;
    }

    @Override
    public Set<Long> listRecordsTaskIdByOperatorId(Long uId, Long pageAnchor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createEquipmentInspectionStandardGroupMap(
            EquipmentInspectionStandardGroupMap standardGroup) {
        assert (standardGroup.getStandardId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionStandardGroupMap.class, standardGroup.getStandardId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionStandardGroupMap.class));
        standardGroup.setId(id);
        standardGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        EhEquipmentInspectionStandardGroupMapDao dao = new EhEquipmentInspectionStandardGroupMapDao(context.configuration());
        dao.insert(standardGroup);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionStandardGroupMap.class, null);

    }

    @Override
    public void deleteEquipmentInspectionStandardGroupMap(Long standardGroupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEquipmentInspectionStandardGroupMap.class));
        EhEquipmentInspectionStandardGroupMapDao dao = new EhEquipmentInspectionStandardGroupMapDao(context.configuration());
        dao.deleteById(standardGroupId);

    }

    @Override
    public void deleteEquipmentInspectionStandardGroupMapByStandardId(
            Long standardId) {
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhEquipmentInspectionStandardGroupMap.class), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<EhEquipmentInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP);
                    query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.eq(standardId));
                    query.fetch().map((EhEquipmentInspectionStandardGroupMapRecord record) -> {
                        deleteEquipmentInspectionStandardGroupMap(record.getId());
                        return null;
                    });

                    return true;
                });
    }

    @Override
    public List<Long> listEquipmentInspectionStandardGroupMapByGroup(
            List<Long> groupIds, Byte groupType) {
        final List<Long> standardIds = new ArrayList<Long>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhEquipmentInspectionStandardGroupMap.class));

        SelectQuery<EhEquipmentInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP);

        if (groupIds != null)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.GROUP_ID.in(groupIds));
        if (groupType != null)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.GROUP_TYPE.eq(groupType));

        query.fetch().map((r) -> {
            standardIds.add(r.getStandardId());
            return null;
        });


        return standardIds;
    }

    @Override
    public List<TaskCountDTO> statEquipmentTasks(Long targetId, String targetType,
                                                 Long inspectionCategoryId, Long startTime, Long endTime, Integer offset, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<TaskCountDTO> dtos = new ArrayList<TaskCountDTO>();

        final Field<Byte> delay = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.DELAY.getCode()), EquipmentTaskStatus.DELAY.getCode());
        final Field<Byte> toExecuted = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode()), EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());
        final Field<Byte> completeInspection = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.RESULT.eq(EquipmentTaskResult.COMPLETE_OK.getCode()), EquipmentTaskResult.COMPLETE_OK.getCode());
        final Field<?>[] fields = {Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE, Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID,
                Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID, DSL.count().as("taskCount"),
                DSL.count(toExecuted).as("toExecuted"), DSL.count(delay).as("delay"),
                DSL.count(completeInspection).as("completeInspection")};

        final SelectQuery<Record> query = context.selectQuery();
        query.addSelect(fields);
        query.addFrom(Tables.EH_EQUIPMENT_INSPECTION_TASKS);

//        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_ID.eq(ownerId));

        if (!StringUtils.isNullOrEmpty(targetType)) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.eq(targetType));
        }

        if (targetId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.eq(targetId));
        }

        if (startTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.CREATE_TIME.ge(new Timestamp(startTime)));
        }

        if (endTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.CREATE_TIME.le(new Timestamp(endTime)));
        }

        if (inspectionCategoryId != null && inspectionCategoryId != 0L) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        }

//        query.addGroupBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE, Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID,
//                Tables.EH_EQUIPMENT_INSPECTION_TASKS.EQUIPMENT_ID, Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID);
        query.addGroupBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE, Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID,
                Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID);
        query.addLimit(offset, pageSize);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("countTasks, sql=" + query.getSQL());
            LOGGER.debug("countTasks, bindValues=" + query.getBindValues());
        }
        query.fetch().map((r) -> {
            TaskCountDTO dto = new TaskCountDTO();

            dto.setTargetId(r.getValue(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID));
            dto.setInspectionCategoryId(r.getValue(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID));
            dto.setEquipmentId(r.getValue(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EQUIPMENT_ID));
            dto.setStandardId(r.getValue(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID));
            dto.setTaskCount(r.getValue("taskCount", Long.class));
            dto.setToExecuted(r.getValue("toExecuted", Long.class));
            //dto.setInMaintance(r.getValue("inMaintance", Long.class));
           // dto.setNeedMaintance(r.getValue("needMaintance", Long.class));
            dto.setCompleteInspection(r.getValue("completeInspection", Long.class));
           // dto.setCompleteMaintance(r.getValue("completeMaintance", Long.class));
            dto.setDelay(r.getValue("delay", Long.class));
            dtos.add(dto);
            return null;
        });

        return dtos;
    }

//    @Override
//    public void populateStandardsGroups(List<EquipmentInspectionStandards> standards) {
//        if (standards == null || standards.size() == 0) {
//            return;
//        }
//
//        final List<Long> standardIds = new ArrayList<Long>();
//        final Map<Long, EquipmentInspectionStandards> mapStandards = new HashMap<Long, EquipmentInspectionStandards>();
//
//        for (EquipmentInspectionStandards standard : standards) {
//            standardIds.add(standard.getId());
//            standard.setExecutiveGroup(new ArrayList<EquipmentInspectionStandardGroupMap>());
//            standard.setReviewGroup(new ArrayList<EquipmentInspectionStandardGroupMap>());
//            mapStandards.put(standard.getId(), standard);
//        }
//
//        List<Integer> shards = this.shardingProvider.getContentShards(EhEquipmentInspectionStandards.class, standardIds);
//        this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class), null, (DSLContext context, Object reducingContext) -> {
//            SelectQuery<EhEquipmentInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP);
//            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.in(standardIds));
//            query.fetch().map((EhEquipmentInspectionStandardGroupMapRecord record) -> {
//                EquipmentInspectionStandards standard = mapStandards.get(record.getStandardId());
//
//                assert (standard != null);
//                if (QualityGroupType.EXECUTIVE_GROUP.getCode() == record.getGroupType()) {
//                    standard.getExecutiveGroup().add(ConvertHelper.convert(record, EquipmentInspectionStandardGroupMap.class));
//                }
//                if (record.getGroupType() == QualityGroupType.REVIEW_GROUP.getCode()) {
//                    standard.getReviewGroup().add(ConvertHelper.convert(record, EquipmentInspectionStandardGroupMap.class));
//                }
//
//                return null;
//            });
//            return true;
//        });
//
//    }
    @Override
    public void populatePlansGroups(List<EquipmentInspectionPlans> plans) {
        if (plans == null || plans.size() == 0) {
            return;
        }

        final List<Long> planIds = new ArrayList<>();
        final Map<Long, EquipmentInspectionPlans> mapPlans = new HashMap<>();

        for (EquipmentInspectionPlans plan : plans) {
            planIds.add(plan.getId());
            plan.setExecutiveGroup(new ArrayList<>());
            plan.setReviewGroup(new ArrayList<>());
            mapPlans.put(plan.getId(), plan);
        }

        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhEquipmentInspectionStandards.class, planIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhEquipmentInspectionPlanGroupMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP);
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.PLAN_ID.in(planIds));
            query.fetch().map((EhEquipmentInspectionPlanGroupMapRecord record) -> {
                EquipmentInspectionPlans plan = mapPlans.get(record.getPlanId());
                assert (plan != null);
                if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(record.getGroupType()))) {
                    plan.getExecutiveGroup().add(ConvertHelper.convert(record, EquipmentInspectionPlanGroupMap.class));
                }
                if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(record.getGroupType()))) {
                    plan.getReviewGroup().add(ConvertHelper.convert(record, EquipmentInspectionPlanGroupMap.class));
                }
                return null;
            });
            return true;
        });

    }

//    @Override
//    public void populateStandardGroups(EquipmentInspectionStandards standard) {
//        if (standard == null) {
//            return;
//        } else {
//            List<EquipmentInspectionStandards> standards = new ArrayList<EquipmentInspectionStandards>();
//            standards.add(standard);
//
//            populateStandardsGroups(standards);
//        }
//
//    }

    @Override
    public void populatePlanGroups(EquipmentInspectionPlans plan) {
        if (plan != null) {
            List<EquipmentInspectionPlans> plans = new ArrayList<>();
            plans.add(plan);
            populatePlansGroups(plans);
        }
    }

    @Override
    public List<EquipmentInspectionTasks> listEquipmentInspectionReviewTasks(
            String ownerType, Long ownerId, Long inspectionCategoryId,
            List<String> targetType, List<Long> targetId,
            List<Long> standardIds, Integer offset, Integer pageSize) {
        long startTime = System.currentTimeMillis();
        List<EquipmentInspectionTasks> result = new ArrayList<EquipmentInspectionTasks>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.NONE.getCode()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.DELAY.getCode()));
        if (targetType != null && targetType.size() > 0)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.in(targetType));

        if (targetId != null && targetId.size() > 0)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.in(targetId));

        if (inspectionCategoryId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        }

        if (standardIds != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.in(standardIds));
        }
        //巡检完成关闭的任务
        Condition con1 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.CLOSE.getCode());
        con1 = con1.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_RESULT.ne(ReviewResult.QUALIFIED.getCode()));
        con1 = con1.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_EXPIRED_DATE.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));
        //需维修待审核的任务
        Condition con2 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.NEED_MAINTENANCE.getCode());
        Condition con3 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime()));
        con3 = con3.or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_EXPIRE_TIME.ge(new Timestamp(DateHelper.currentGMTTime().getTime())));
        con2 = con2.and(con3);

        Condition con = con1.or(con2);

        query.addConditions(con);

        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_EXPIRE_TIME, Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME);
        query.addLimit(offset, pageSize);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query tasks by count, sql=" + query.getSQL());
            LOGGER.debug("Query tasks by count, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhEquipmentInspectionTasksRecord record) -> {
            result.add(ConvertHelper.convert(record, EquipmentInspectionTasks.class));
            return null;
        });

        long endTime = System.currentTimeMillis();
        LOGGER.debug("TrackUserRelatedCost: listEquipmentInspectionReviewTasks resultSize = " + result.size()
                + ", maxCount = " + pageSize + ", elapse=" + (endTime - startTime));
        return result;
    }

    @Override
    public List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByGroupAndPosition(
            List<ExecuteGroupAndPosition> reviewGroups, Byte groupType) {
        long startTime = System.currentTimeMillis();
        final List<EquipmentInspectionStandardGroupMap> maps = new ArrayList<EquipmentInspectionStandardGroupMap>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhEquipmentInspectionStandardGroupMap.class));

        SelectQuery<EhEquipmentInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP);

        Condition con = null;
        if (reviewGroups != null) {
            Condition con5 = null;
            for (ExecuteGroupAndPosition executiveGroup : reviewGroups) {
                Condition con4 = null;
                con4 = Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.GROUP_ID.eq(executiveGroup.getGroupId());
                con4 = con4.and(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.POSITION_ID.eq(executiveGroup.getPositionId()));
                if (con5 == null) {
                    con5 = con4;
                } else {
                    con5 = con5.or(con4);
                }
            }
            con = con5;
        }

//        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.GROUP_TYPE.eq(groupType));
        query.addConditions(con);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listEquipmentInspectionStandardGroupMapByGroupAndPosition, sql=" + query.getSQL());
            LOGGER.debug("listEquipmentInspectionStandardGroupMapByGroupAndPosition, bindValues=" + query.getBindValues());
        }
        query.fetch().map((r) -> {
            maps.add(ConvertHelper.convert(r, EquipmentInspectionStandardGroupMap.class));
            return null;
        });

        long endTime = System.currentTimeMillis();
        LOGGER.debug("TrackUserRelatedCost: listEquipmentInspectionStandardGroupMapByGroupAndPosition resultSize = " + maps.size()
                + ", elapse=" + (endTime - startTime));

        return maps;
    }

    @Override
    public List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByStandardIdAndGroupType(Long standardId, Byte groupType) {
        final List<EquipmentInspectionStandardGroupMap> maps = new ArrayList<EquipmentInspectionStandardGroupMap>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhEquipmentInspectionStandardGroupMap.class));

        SelectQuery<EhEquipmentInspectionStandardGroupMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP);

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.eq(standardId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.GROUP_TYPE.eq(groupType));
        query.fetch().map((r) -> {
            maps.add(ConvertHelper.convert(r, EquipmentInspectionStandardGroupMap.class));
            return null;
        });

        return maps;
    }

    @Override
    public List<EquipmentInspectionTasks> listTodayEquipmentInspectionTasks(Long startTime, Long endTime, Byte groupType) {
        List<EquipmentInspectionTasks> result = new ArrayList<EquipmentInspectionTasks>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
//		query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.CREATE_TIME.ge(new Timestamp(createTime)));

        if (QualityGroupType.EXECUTIVE_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(new Timestamp(startTime)));
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.le(new Timestamp(endTime)));
        }

        if (QualityGroupType.REVIEW_GROUP.equals(QualityGroupType.fromStatus(groupType))) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.ge(new Timestamp(startTime)));
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME.le(new Timestamp(endTime)));
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listTodayEquipmentInspectionTasks, sql=" + query.getSQL());
            LOGGER.debug("listTodayEquipmentInspectionTasks, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhEquipmentInspectionTasksRecord record) -> {
            result.add(ConvertHelper.convert(record, EquipmentInspectionTasks.class));
            return null;
        });


        return result;
    }

    @Override
    public EquipmentInspectionTasks findLastestEquipmentInspectionTask(Long startTime) {
        EquipmentInspectionTasks[] result = new EquipmentInspectionTasks[1];

        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhEquipmentInspectionTasks.class), null,
                (DSLContext context, Object reducingContext) -> {
                    result[0] = context.select().from(Tables.EH_EQUIPMENT_INSPECTION_TASKS)
                            .where(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(new Timestamp(startTime)))
                            .orderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME)
                            .fetchAny().map((r) -> {
                                return ConvertHelper.convert(r, EquipmentInspectionTasks.class);
                            });

                    if (result[0] != null) {
                        return false;
                    } else {
                        return true;
                    }
                });

        return result[0];
    }

    @Cacheable(value = "listEquipmentInspectionTasksUseCache", key = "{#cacheKey}", unless = "#result.size() == 0")
    @Override
    public List<EquipmentInspectionTasks> listEquipmentInspectionTasksUseCache(List<Byte> taskStatus, Long inspectionCategoryId,
                                                                               List<String> targetType, List<Long> targetId, List<Long> executeStandardIds, List<Long> reviewStandardIds,
                                                                               Long offset, Integer pageSize, String cacheKey, Byte adminFlag,Timestamp lastSyncTime) {
        long startTime = System.currentTimeMillis();
        List<EquipmentInspectionTasks> result = new ArrayList<>();
        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag))
                && (executeStandardIds == null || executeStandardIds.size() == 0)
                && (reviewStandardIds == null || reviewStandardIds.size() == 0)) {
            return null;
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        if (taskStatus != null && taskStatus.size()>0){
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(taskStatus));
        }

        if (targetType != null && targetType.size() > 0)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.in(targetType));

        if (targetId != null && targetId.size() > 0)
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.in(targetId));

        if (inspectionCategoryId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        }

        if (AdminFlag.YES.equals(AdminFlag.fromStatus(adminFlag))) {
            Condition con2 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.NONE.getCode());
            query.addConditions(con2);
        }

        Condition con = null;
        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag)) && executeStandardIds != null && executeStandardIds.size() > 0) {
            Condition con4 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.in(executeStandardIds);
            con4 = con4.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode(),
                    EquipmentTaskStatus.DELAY.getCode(),EquipmentTaskStatus.NONE.getCode()));
            con = con4;
        }

        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag)) && reviewStandardIds != null && reviewStandardIds.size() > 0) {
            Condition con3 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.in(reviewStandardIds);
            //巡检完成关闭的任务
            Condition con1 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(EquipmentTaskStatus.CLOSE.getCode(),
                     EquipmentTaskStatus.REVIEW_DELAY.getCode(),EquipmentTaskStatus.QUALIFIED.getCode());
            con3 = con3.and(con1);
            if (con == null) {
                con = con3;
            } else {
                con = con.or(con3);
            }
        }

        if (con != null) {
            query.addConditions(con);
        }

        if(lastSyncTime!=null){
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.CREATE_TIME.gt(lastSyncTime)
                    .or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_TIME.gt(lastSyncTime))
                    .or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_TIME.gt(lastSyncTime))
                     .or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_TIME.gt(lastSyncTime)));
        }
        // 只显示离创建时间三个月的任务
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.CREATE_TIME.ge(addMonths(new Timestamp(System.currentTimeMillis()), -3)));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.gt(offset));
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.asc());
        // now just require inactive and waitingExcecute status by jiarui 20180630
//        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.asc());
//        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_EXPIRE_TIME);
        query.addLimit(pageSize);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query tasks by count, sql=" + query.getSQL());
            LOGGER.debug("Query tasks by count, bindValues=" + query.getBindValues());
        }
        query.fetch().map((EhEquipmentInspectionTasksRecord record) -> {
            result.add(ConvertHelper.convert(record, EquipmentInspectionTasks.class));
            return null;
        });

        long endTime = System.currentTimeMillis();
        LOGGER.debug("TrackUserRelatedCost: listEquipmentInspectionTasksUseCache resultSize = " + result.size()
                + ", maxCount = " + pageSize + ", elapse=" + (endTime - startTime));

        return result;

    }

    @Override
    public Map<Long, EquipmentInspectionEquipments> listEquipmentsById(Set<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.in(ids));

        Map<Long, EquipmentInspectionEquipments> result = new HashMap<>();
        query.fetch().map((r) -> {
            result.put(r.getId(), ConvertHelper.convert(r, EquipmentInspectionEquipments.class));
            return null;
        });

        return result;
    }

    @Override
    public List<EquipmentInspectionEquipments> listEquipmentsById(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionEquipmentsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.in(ids));

        List<EquipmentInspectionEquipments> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionEquipments.class));
            return null;
        });

        return result;
    }

    @Override
    public List<EquipmentInspectionTasks> listTaskByIds(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.in(ids));

        List<EquipmentInspectionTasks> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
            return null;
        });

        return result;
    }

    @Override
    public TasksStatData statDaysEquipmentTasks(Long targetId, String targetType, Long inspectionCategoryId,
                                                Timestamp startTime, Timestamp endTime, Integer namespaceId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        TasksStatData resp = new TasksStatData();

        final Field<Byte> delayInpsectionTasks = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS
                .eq(EquipmentTaskStatus.DELAY.getCode()), EquipmentTaskStatus.DELAY.getCode());


        final Field<Byte> reviewDelay = DSL.decode()
                .when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.REVIEW_DELAY.getCode()),
                        EquipmentTaskStatus.REVIEW_DELAY.getCode());

        final Field<Byte> waitingForExecuting = DSL.decode()
                .when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode()),
                        EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());

        Condition completeWaitingForApprovalCondition = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS
                .eq(EquipmentTaskStatus.CLOSE.getCode());

        Condition completeInspectionTask = Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS
                .in(EquipmentTaskStatus.CLOSE.getCode(),EquipmentTaskStatus.QUALIFIED.getCode());
        final Field<Byte> completeInspectionWaitingForApproval = DSL.decode()
                .when(completeWaitingForApprovalCondition, EquipmentTaskStatus.CLOSE.getCode());
        final Field<Byte> completeInspectionTasks = DSL.decode()
                .when(completeInspectionTask, EquipmentTaskStatus.CLOSE.getCode());

        final Field<?>[] fields = {DSL.count().as("total"),
                DSL.count(waitingForExecuting).as("waitingForExecuting"),
                DSL.count(completeInspectionWaitingForApproval).as("completeInspectionWaitingForApproval"),
                DSL.count(completeInspectionTasks).as("completeInspectionTasks"),
                DSL.count(delayInpsectionTasks).as("delayInpsectionTasks"),
                DSL.count(reviewDelay).as("reviewDelay")};

        final SelectQuery<Record> query = context.selectQuery();
        query.addSelect(fields);
        query.addFrom(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.NAMESPACE_ID.eq(namespaceId));

        if (targetId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.eq(targetId));
        }
        if (!StringUtils.isNullOrEmpty(targetType)) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_TYPE.eq(targetType));
        }

//        if (ownerId != null) {
//            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_ID.eq(ownerId));
//        }
//        if (!StringUtils.isNullOrEmpty(ownerType)) {
//            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.OWNER_TYPE.eq(ownerType));
//        }

        if (inspectionCategoryId != null && inspectionCategoryId != 0L) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        }
        if (startTime != null && endTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.between(startTime, endTime));
        } else if (startTime == null && endTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.le(endTime));
        } else if (startTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_START_TIME.ge(startTime));
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("countTasks, sql=" + query.getSQL());
            LOGGER.debug("countTasks, bindValues=" + query.getBindValues());
        }
        query.fetchAny().map((r) -> {
            resp.setCompleteWaitingForApproval(r.getValue("completeInspectionWaitingForApproval", Long.class));
            resp.setWaitingForExecuting(r.getValue("waitingForExecuting", Long.class));
            resp.setCompleteInspectionTasks(r.getValue("completeInspectionTasks", Long.class));
            resp.setTotalTasks(r.getValue("total", Long.class));
            resp.setDelayInspection(r.getValue("delayInpsectionTasks", Long.class));
            resp.setReviewDelayTasks(r.getValue("reviewDelay", Long.class));
            return null;
        });
        return resp;
    }

    @Override
    public ReviewedTaskStat statDaysReviewedTasks(Long communityId, Long inspectionCategoryId, Timestamp startTime,
                                                  Timestamp endTime, Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        ReviewedTaskStat stat = new ReviewedTaskStat();
        final Field<Byte> qualifiedTasks = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_RESULT.eq(EquipmentTaskProcessResult.REVIEW_QUALIFIED.getCode()), EquipmentTaskProcessResult.REVIEW_QUALIFIED.getCode());
        final Field<Byte> unqualifiedTasks = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_RESULT.eq(EquipmentTaskProcessResult.REVIEW_UNQUALIFIED.getCode()), EquipmentTaskProcessResult.REVIEW_UNQUALIFIED.getCode());
        final Field<?>[] fields = {
                DSL.count(qualifiedTasks).as("qualifiedTasks"),
                DSL.count(unqualifiedTasks).as("unqualifiedTasks")
        };
        final SelectQuery<Record> query = context.selectQuery();
        query.addSelect(fields);
        query.addFrom(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_TYPE.eq(EquipmentTaskProcessType.REVIEW.getCode()));
        //query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.NAMESPACE_ID.eq(namespaceId));
        if (communityId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.COMMUNITY_ID.eq(communityId));
        }

        if (startTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.CREATE_TIME.ge(startTime));
        }

        if (endTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.CREATE_TIME.le(endTime));
        }


        query.fetchAny().map((r) -> {
            stat.setQualifiedTasks(r.getValue("qualifiedTasks", Long.class));
            stat.setUnqualifiedTasks(r.getValue("unqualifiedTasks", Long.class));
            return null;
        });
        return stat;
    }

    @Override
    public List<ItemResultStat> statItemResults(Long equipmentId, Long standardId, Timestamp startTime, Timestamp endTime) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<ItemResultStat> results = new ArrayList<>();

        final Field<Byte> abnormalTimes = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.NORMAL_FLAG.eq(ItemResultNormalFlag.ABNORMAL.getCode()), ItemResultNormalFlag.ABNORMAL.getCode());
        final Field<Byte> normalTimes = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.NORMAL_FLAG.eq(ItemResultNormalFlag.NORMAL.getCode()), ItemResultNormalFlag.NORMAL.getCode());
        final Field<String> averageValue = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ITEM_VALUE_TYPE.eq(ItemValueType.RANGE.getCode()), Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ITEM_VALUE);
        final Field<?>[] fields = {Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ITEM_ID, Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ITEM_NAME,
                DSL.count(abnormalTimes).as("abnormalTimes"), DSL.count(normalTimes).as("normalTimes"),
                DSL.groupConcat(averageValue).as("averageValue"), DSL.groupConcat(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.TASK_ID).as("taskIds")};
        final SelectQuery<Record> query = context.selectQuery();
        query.addSelect(fields);
        query.addFrom(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS);

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.EQUIPMENT_ID.eq(equipmentId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.STANDARD_ID.eq(standardId));


        if (startTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.CREATE_TIME.ge(startTime));
        }

        if (endTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.CREATE_TIME.le(endTime));
        }

        query.addGroupBy(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ITEM_ID);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("statItemResults, sql=" + query.getSQL());
            LOGGER.debug("statItemResults, bindValues=" + query.getBindValues());
        }
        query.fetch().map((r) -> {
            ItemResultStat result = new ItemResultStat();
            result.setItemId(r.getValue(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ITEM_ID));
            result.setItemName(r.getValue(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ITEM_NAME));
            result.setAbnormalTimes(r.getValue("abnormalTimes", Long.class));
            result.setNormalTimes(r.getValue("normalTimes", Long.class));
            String taskIds = r.getValue("taskIds", String.class);
            String[] ids = taskIds.split(",");
            if (ids != null && ids.length > 0) {
                Set<Long> tasks = new HashSet<Long>();
                for (String id : ids) {
                    if (!StringUtils.isNullOrEmpty(id)) {
                        tasks.add(Long.valueOf(id));
                    }
                }
                result.setAbnormalTaskIds(tasks);
            }
            String value = r.getValue("averageValue", String.class);
            if (value != null) {
                String[] values = value.split(",");
                if (values != null && values.length > 0) {
                    Double average = 0.0;
                    for (String v : values) {
                        average = average + Double.valueOf(v);
                    }

                    average = average / values.length;
                    result.setAverageValue(average);
                }
            }
            results.add(result);
            return null;
        });

		return results;
	}

	@Override
	public void createEquipmentModelCommunityMap(EquipmentModelCommunityMap map) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentModelCommunityMap.class));
		EhEquipmentModelCommunityMapDao dao = new EhEquipmentModelCommunityMapDao(context.configuration());
		map.setId(id);
		map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		dao.insert(map);
	}

	@Override
	public List<EquipmentModelCommunityMap> listModelCommunityMapByCommunityId(Long targetId, byte modelType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.selectFrom(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP)
				.where(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.TARGET_ID.eq(targetId))
				.and(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType))
				.fetchInto(EquipmentModelCommunityMap.class);
	}

	@Override
	public void deleteModelCommunityMapByModelIdAndCommunityId(Long modelId,Long targetId,byte modelType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP)
				.where(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.MODEL_ID.eq(modelId))
				.and(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.TARGET_ID.eq(targetId))
				.and(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType))
				.execute();
	}

	@Override
	public List<Integer> listDistinctNameSpace() {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return context.selectDistinct(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.NAMESPACE_ID)
				.from(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES)
				.fetchInto(Integer.class);
	}

	@Override
	public List<Long> listModelCommunityMapByModelId(Long modelId , byte modelType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		return context.select(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.TARGET_ID)
				.from(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP)
				.where(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.MODEL_ID.eq(modelId))
				.and(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType))
				.fetchInto(Long.class);
	}



	@Override
	public void deleteModelCommunityMapByModelId(Long modelId,byte modelType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP)
				.where(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.MODEL_ID.eq(modelId))
				.and(Tables.EH_EQUIPMENT_MODEL_COMMUNITY_MAP.MODEL_TYPE.eq(modelType))
				.execute();
	}

    @Override
    public void deleteEquipmentInspectionPlanGroupMapByPlanId(Long planId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.PLAN_ID.eq(planId))
                .execute();
    }

    @Override
    public void createEquipmentInspectionPlanGroupMap(EquipmentInspectionPlanGroupMap map) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionPlanGroupMap.class));
        LOGGER.info("create EquipmentInspectionPlanGroupMap id :{}"+id);
        map.setId(id);
        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEquipmentInspectionPlanGroupMapDao dao = new EhEquipmentInspectionPlanGroupMapDao(context.configuration());
        dao.insert(map);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEquipmentInspectionPlanGroupMap.class, null);
    }

    @Override
    public List<EquipmentInspectionPlanGroupMap> listEquipmentInspectionPlanGroupMapByPlanIdAndGroupType(Long planId, byte groupType) {
        final List<EquipmentInspectionPlanGroupMap> maps = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhEquipmentInspectionPlanGroupMapRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP);

        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.PLAN_ID.eq(planId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.GROUP_TYPE.eq(groupType));
        query.fetch().map((r) -> {
            maps.add(ConvertHelper.convert(r, EquipmentInspectionPlanGroupMap.class));
            return null;
        });

        return maps;
    }

    @Override
    public List<EquipmentInspectionPlanGroupMap> listEquipmentInspectionPlanGroupMapByGroupAndPosition(
            List<ExecuteGroupAndPosition> groupDtos, Byte groupType) {
        long startTime = System.currentTimeMillis();
        final List<EquipmentInspectionPlanGroupMap> maps = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhEquipmentInspectionPlanGroupMapRecord> query = context
                .selectQuery(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP);

        Condition con = null;
        if (groupDtos != null) {
            Condition con5 = null;
            for (ExecuteGroupAndPosition executiveGroup : groupDtos) {
                Condition con4 = null;
                con4 = Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.GROUP_ID.eq(executiveGroup.getGroupId());
                con4 = con4.and(Tables.EH_EQUIPMENT_INSPECTION_PLAN_GROUP_MAP.POSITION_ID.eq(executiveGroup.getPositionId()));
                if (con5 == null) {
                    con5 = con4;
                } else {
                    con5 = con5.or(con4);
                }
            }
            con = con5;
        }
        query.addConditions(con);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listEquipmentInspectionPlanGroupMapByGroupAndPosition, sql=" + query.getSQL());
            LOGGER.debug("listEquipmentInspectionPlanGroupMapByGroupAndPosition, bindValues=" + query.getBindValues());
        }
        query.fetch().map((r) -> {
            maps.add(ConvertHelper.convert(r, EquipmentInspectionPlanGroupMap.class));
            return null;
        });

        long endTime = System.currentTimeMillis();
        LOGGER.debug("TrackUserRelatedCost: listEquipmentInspectionPlanGroupMapByGroupAndPosition resultSize = " + maps.size()
                + ", elapse=" + (endTime - startTime));

        return maps;
    }

    @Override
    public List<EquipmentInspectionTasks> listTasksByPlanId(Long planId, CrossShardListingLocator locator, int pageSize) {
        List<EquipmentInspectionTasks> tasks = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        if(locator.getAnchor()!=null && locator.getAnchor()!=0L){
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.lt(locator.getAnchor()));
        }
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.eq(planId));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.NONE.getCode()));
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.desc());
        query.addLimit(pageSize+1);
        tasks =  query.fetchInto(EquipmentInspectionTasks.class);
        if(tasks.size()>pageSize){
            tasks.remove(tasks.size()-1);
            locator.setAnchor(tasks.get(tasks.size()-1).getId());
        }else {
            locator.setAnchor(null);
        }
        LOGGER.debug("listTasksByPlanId sql={} ",query.getSQL());
        LOGGER.debug("listTasksByPlanId bindValues={} ",query.getBindValues());
        return  tasks;
    }

    @Override
    public List<Long> listNotifyRecordByPlanId(Long planId, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> result = new ArrayList<>();
        EhPmNotifyRecords record = Tables.EH_PM_NOTIFY_RECORDS;
        com.everhomes.server.schema.tables.EhEquipmentInspectionTasks tasks = Tables.EH_EQUIPMENT_INSPECTION_TASKS;
        result = context.select(record.ID)
                .from(record, tasks)
                .where(tasks.PLAN_ID.eq(planId))
                .and(tasks.STATUS.in(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode(), EquipmentTaskStatus.DELAY.getCode()))
                .and(record.OWNER_ID.eq(tasks.ID))
                .and(record.OWNER_TYPE.eq(EntityType.EQUIPMENT_TASK.getCode()))
                .and(record.ID.gt(locator.getAnchor()))
                .orderBy(record.ID.asc())
                .limit(pageSize + 1)
                .fetchInto(Long.class);
        if (result != null && result.size() > pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1));
        }else {
            locator.setAnchor(null);
        }
        return result;
    }

    @Override
    public List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByStandardId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return  context.selectFrom(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.STANDARD_ID.eq(id))
                .fetchInto(EquipmentInspectionStandardGroupMap.class);
    }

    @Override
    public List<EquipmentStandardMap> listAllActiveEquipmentStandardMap() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.STATUS.eq(Status.ACTIVE.getCode()))
                .fetchInto(EquipmentStandardMap.class);
    }

    @Override
    public void createReviewExpireDays(EquipmentInspectionReviewDate reviewDate) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionReviewDate.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionReviewDateDao dateDao = new EhEquipmentInspectionReviewDateDao(context.configuration());
        reviewDate.setId(id);
        reviewDate.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dateDao.insert(reviewDate);
    }

    @Override
    public void updateReviewExpireDays(EquipmentInspectionReviewDate reviewDate) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEquipmentInspectionReviewDateDao dateDao = new EhEquipmentInspectionReviewDateDao(context.configuration());
        dateDao.update(reviewDate);
    }

    @Override
    public void deleteReviewExpireDaysByScope(Byte scopeType, Long scopeId,Long targetId,String targetType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhEquipmentInspectionReviewDateRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.SCOPE_TYPE.eq(scopeType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.SCOPE_ID.eq(scopeId));
        if(targetId!=null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.TARGET_ID.eq(targetId));
        }
        if(StringUtils.isNullOrEmpty(targetType)){
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.TARGET_TYPE.eq(targetType));
        }
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.STATUS.eq(PmNotifyConfigurationStatus.VAILD.getCode()));
        query.fetch().map((r)->{
            deleteReviewExpireDays(r.getId());
            return null;
        });
    }

    @Override
    public void deleteReviewExpireDaysByReferId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE)
                .where(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.REFER_ID.eq(id))
                .execute();
    }

    private void deleteReviewExpireDays(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE)
                .where(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.ID.eq(id))
                .execute();
    }

    public EquipmentInspectionReviewDate getEquipmentInspectiomExpireDaysById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionReviewDateRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE);
        Condition condition = Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.ID.eq(id);
        query.addConditions(condition);
        return ConvertHelper.convert(query.fetchAny(), EquipmentInspectionReviewDate.class);

    }

    @Override
    public List<EquipmentInspectionReviewDate> getEquipmentInspectiomExpireDays(Long scopeId, Byte scopeType,Long targetId,String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionReviewDateRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.SCOPE_TYPE.eq(scopeType));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.SCOPE_ID.eq(scopeId));
        if(targetId!=null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.TARGET_ID.eq(targetId));
        }
        if(!StringUtils.isNullOrEmpty(targetType)) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.TARGET_TYPE.eq(targetType));
        }
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_REVIEW_DATE.STATUS.eq(PmNotifyConfigurationStatus.VAILD.getCode()));
        return query.fetchInto(EquipmentInspectionReviewDate.class);

    }

    @Override
    public void deleteEquipmentPlansMapByEquipmentId(Long equipmentId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.EQUIPMENT_ID.eq(equipmentId))
                .execute();
    }

    @Override
    public EquipmentInspectionTasksLogs getMaintanceLogByEquipmentId(Long referId,Long pmTaskId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEquipmentInspectionTaskLogsRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.EQUIPMENT_ID.eq(referId));
//        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_TYPE.eq(EquipmentTaskProcessType.NEED_MAINTENANCE.getCode()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PM_TASK_ID.eq(pmTaskId));
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.CREATE_TIME.desc());
        List<EquipmentInspectionTasksLogs> logs = new ArrayList<>();
        query.fetch((r) -> {
            logs.add(ConvertHelper.convert(r, EquipmentInspectionTasksLogs.class));
            return null;
        });
        if (logs.size() > 0) {
            return logs.get(0);
        }
        return null;
    }

    @Override
    public void updateMaintanceInspectionLogsById(Long taskLogId, Byte status, Long flowCaseId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        UpdateQuery<EhEquipmentInspectionTaskLogsRecord> updateQuery = context.updateQuery(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS);
        updateQuery.addValue(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.MAINTANCE_STATUS, status);

        //updateQuery.addValue(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.MAINTANCE_STATUS, status);
        updateQuery.addValue(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.FLOW_CASE_ID, flowCaseId);
        if(PmTaskFlowStatus.COMPLETED.equals(PmTaskFlowStatus.fromCode(status))){
            updateQuery.addValue(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_RESULT,
                    EquipmentTaskProcessResult.NEED_MAINTENANCE_OK_COMPLETE_OK.getCode());
        }
        updateQuery.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.eq(taskLogId));
        updateQuery.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_TYPE.eq(EquipmentTaskProcessType.NEED_MAINTENANCE.getCode()));
        updateQuery.execute();
        LOGGER.debug("updateMaintanceInspectionLogsById：{}",updateQuery.getSQL());
        LOGGER.debug("updateMaintanceInspectionLogsById bindValues：{}",updateQuery.getBindValues());
    }

    @Override
    public void statInMaintanceTaskCount(TasksStatData stat, Timestamp startTime, Timestamp endTime, StatTodayEquipmentTasksCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        Condition inMaintanceCountCondition =
                Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_RESULT.ne(EquipmentTaskProcessResult.NEED_MAINTENANCE_OK_COMPLETE_OK.getCode());
        inMaintanceCountCondition = inMaintanceCountCondition.
                and(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_TYPE.eq(EquipmentTaskProcessType.NEED_MAINTENANCE.getCode()));
        Field<?> inMaintanceCount = DSL.decode().when(inMaintanceCountCondition, Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.TASK_ID);

        Condition allMaintanceCountCondition =
                Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.PROCESS_TYPE.eq(EquipmentTaskProcessType.NEED_MAINTENANCE.getCode());

        Field<?> maintanceTotalCount = DSL.decode().when(allMaintanceCountCondition, Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.TASK_ID);
        final Field<?>[] fields = {DSL.countDistinct(inMaintanceCount).as("inMaintanceCount"),
                DSL.countDistinct(maintanceTotalCount).as("maintanceTotalCount")};


        query.addSelect(fields);
        query.addFrom(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.NAMESPACE_ID.eq(cmd.getNamespaceId()));

        if (cmd.getTargetId() != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.COMMUNITY_ID.eq(cmd.getTargetId()));
        }

        if (cmd.getInspectionCategoryId() != null && cmd.getInspectionCategoryId() != 0L) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.INSPECTION_CATEGORY_ID.eq(cmd.getInspectionCategoryId()));
        }
        if (startTime != null && endTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.CREATE_TIME.between(startTime, endTime));
        } else if (startTime == null && endTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.CREATE_TIME.le(endTime));
        } else if (startTime != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.CREATE_TIME.ge(startTime));
        }
        query.fetchAny().map((r) -> {
            stat.setInMaintance(r.getValue("inMaintanceCount", Long.class));
            stat.setCompleteMaintance(r.getValue("maintanceTotalCount", Long.class) - r.getValue("inMaintanceCount", Long.class));
            return null;
        });
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "findEquipmentById", key = "#equipmentId"),
            @CacheEvict(value = "listQualifiedEquipmentStandardEquipments", key = "'AllEquipments'")})
    public void updateEquipmentStatus(Long equipmentId, Byte status) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS)
                .set(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.STATUS, status)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.eq(equipmentId))
                .execute();
    }

    @Override
    public void createEquipmentOperateLogs(EquipmentInspectionEquipmentLogs log) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEquipmentInspectionEquipmentLogs.class));
        EhEquipmentInspectionEquipmentLogsDao dao = new EhEquipmentInspectionEquipmentLogsDao(context.configuration());
        log.setId(id);
        log.setOperatorUid(UserContext.currentUserId());
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(log);
    }

    @Override
    public List<EquipmentInspectionEquipmentLogs> listEquipmentOperateLogsByTargetId(Long equipmentId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_LOGS)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_LOGS.TARGET_ID.eq(equipmentId))
                .and(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_LOGS.TARGET_TYPE.eq(EquipmentOperateObjectType.EQUIPMENT.getOperateObjectType()))
                .fetchInto(EquipmentInspectionEquipmentLogs.class);
    }

    @Override
    public void populateTodayTaskStatusCount(List<Long> executePlanIds, List<Long> reviewPlanIds, Byte adminFlag, ListEquipmentTasksResponse response, ListingQueryBuilderCallback queryBuilderCallback) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        queryBuilderCallback.buildCondition(null, query);

        Condition con = null;
        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag)) && executePlanIds != null && executePlanIds.size() > 0) {
            con = Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.in(executePlanIds);
//            con4 = con4.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode(),
//                    EquipmentTaskStatus.DELAY.getCode()));
//            con = con4;
        }
        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag)) && (executePlanIds == null || executePlanIds.size() == 0)) {
            response.setTotayTasksCount(0L);
            response.setTodayCompleteCount(0L);
            return;
        }

        if (con != null) {
            query.addConditions(con);
        }

        // 设置成当天的执行任务
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        final Field<?> todayCompleteCount = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(
                EquipmentTaskStatus.CLOSE.getCode(),
                EquipmentTaskStatus.QUALIFIED.getCode(),
                EquipmentTaskStatus.REVIEW_DELAY.getCode())
                        .and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_TIME.gt(getDayBegin(calendar))),
                EquipmentTaskStatus.CLOSE.getCode());

        final Field<?> totayTasksCount = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode()),
                EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode());


        final Field<?>[] fields = {DSL.count(totayTasksCount).as("totayTasksCount"),
                DSL.count(todayCompleteCount).as("todayCompleteCount")};

        query.addSelect(fields);
        query.addFrom(Tables.EH_EQUIPMENT_INSPECTION_TASKS);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query Today tasks count , sql=" + query.getSQL());
            LOGGER.debug("Query Today tasks count =" + query.getBindValues());
        }
        query.fetch().map((r)->{
            response.setTodayCompleteCount(r.getValue("todayCompleteCount",Long.class));
            response.setTotayTasksCount(r.getValue("totayTasksCount",Long.class));
            return null;
        });
    }

    @Override
    public void populateReviewTaskStatusCount(List<Long> executePlanIds, List<Long> reviewPlanIds, Byte adminFlag, ListEquipmentTasksResponse response, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        queryBuilderCallback.buildCondition(null, query);

        Condition con = null;
        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag)) && reviewPlanIds != null && reviewPlanIds.size() > 0) {
                Condition con4 = Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.in(reviewPlanIds);
            con4 = con4.and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(EquipmentTaskStatus.CLOSE.getCode(),
                    EquipmentTaskStatus.QUALIFIED.getCode(),EquipmentTaskStatus.REVIEW_DELAY.getCode()));
            con = con4;
        }
        if (AdminFlag.NO.equals(AdminFlag.fromStatus(adminFlag)) && (reviewPlanIds == null || reviewPlanIds.size() == 0)) {
            response.setTotayTasksCount(0L);
            response.setTodayCompleteCount(0L);
            return;
        }

        if (con != null) {
            query.addConditions(con);
        }

        // 设置成当天任务
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        final Field<?> todayCompleteCount = DSL.decode()
                .when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.QUALIFIED.getCode())
                .and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEW_TIME.gt(getDayBegin(calendar))),
                        EquipmentTaskStatus.QUALIFIED.getCode());
        final Field<?> totayTasksCount = DSL.decode().when(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.in(EquipmentTaskStatus.CLOSE.getCode()),
                EquipmentTaskStatus.QUALIFIED.getCode());


        final Field<?>[] fields = {DSL.count(totayTasksCount).as("totayTasksCount"),
                DSL.count(todayCompleteCount).as("todayCompleteCount")};

        query.addSelect(fields);
        query.addFrom(Tables.EH_EQUIPMENT_INSPECTION_TASKS);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query Today review tasks count , sql=" + query.getSQL());
            LOGGER.debug("Query Today review tasks count =" + query.getBindValues());
        }
        query.fetch().map((r)->{
            response.setTodayCompleteCount(r.getValue("todayCompleteCount",Long.class));
            response.setTotayTasksCount(r.getValue("totayTasksCount",Long.class));
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
    public List<EquipmentInspectionStandards> listEquipmentStandardWithReferId(Long targetId, String targetType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS)
                .where(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.STATUS.eq(EquipmentStandardStatus.ACTIVE.getCode()))
                .and(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.TARGET_ID.eq(targetId))
                .and(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.TARGET_TYPE.eq(targetType))
                .and(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.REFER_ID.ne(0L))
                .and(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .fetchInto(EquipmentInspectionStandards.class);
    }

    @Override
    public void deletePlanMapByEquipmentIdAndStandardId(Long equipmentId, Long standardId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP)
                .where(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.EQUIPMENT_ID.eq(equipmentId))
                .and(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP.STANDARD_ID.eq(standardId))
                .execute();
    }
    private Timestamp addMonths(Timestamp now, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, months);
        Timestamp time = new Timestamp(calendar.getTimeInMillis());

        return time;
    }

    @Override
    public List<EquipmentInspectionEquipmentPlanMap> listEquipmentPlanMaps() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PLAN_MAP)
                .fetchInto(EquipmentInspectionEquipmentPlanMap.class);
    }

    @Override
    public void transferPlanIdForTasks(Long equipmentId, Long standardId,Long planId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_EQUIPMENT_INSPECTION_TASKS)
                .set(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID, planId)
                .where(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EQUIPMENT_ID.eq(equipmentId))
                .and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STANDARD_ID.eq(standardId))
                .and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.ne(EquipmentTaskStatus.NONE.getCode()))
                .execute();
        LOGGER.info("transferPlanIdForTasks....");
    }

    @Override
    public void batchUpdateUnusedTaskStatus() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_EQUIPMENT_INSPECTION_TASKS)
                .set(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS, EquipmentTaskStatus.NONE.getCode())
                .where(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.eq(0L))
                .execute();
        LOGGER.info("batchUpdateUnusedTaskStatus....");

    }

    @Override
    public void updateEquipmentTaskByPlanId(Long planId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_EQUIPMENT_INSPECTION_TASKS)
                .set(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS, EquipmentTaskStatus.NONE.getCode())
                .set(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PROCESS_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
                .where(Tables.EH_EQUIPMENT_INSPECTION_TASKS.PLAN_ID.eq(planId))
                .and(Tables.EH_EQUIPMENT_INSPECTION_TASKS.STATUS.eq(EquipmentTaskStatus.WAITING_FOR_EXECUTING.getCode()))
                .execute();
    }

    @Override
    public List<EquipmentInspectionTasks> listPersonalDoneTasks(Long targetId, Long inspectionCategoryId, int pageSize, Integer offset, Timestamp startTime) {
        List<EquipmentInspectionTasks> tasksList = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhEquipmentInspectionTasksRecord> query = context.selectQuery(Tables.EH_EQUIPMENT_INSPECTION_TASKS);
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTOR_ID.eq(UserContext.currentUserId())
                .or(Tables.EH_EQUIPMENT_INSPECTION_TASKS.REVIEWER_ID.eq(UserContext.currentUserId())));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.TARGET_ID.eq(targetId));

        if (inspectionCategoryId != null) {
            query.addConditions(Tables.EH_EQUIPMENT_INSPECTION_TASKS.INSPECTION_CATEGORY_ID.eq(inspectionCategoryId));
        }
        query.addLimit(offset * (pageSize - 1), pageSize);
        query.addOrderBy(Tables.EH_EQUIPMENT_INSPECTION_TASKS.EXECUTIVE_TIME.desc());

        query.fetch().map((r) -> {
            tasksList.add(ConvertHelper.convert(r, EquipmentInspectionTasks.class));
            return null;
        });


        return tasksList;
    }


}
