package com.everhomes.pmtask;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.pmtask.PmTaskHistoryAddressStatus;
import com.everhomes.server.schema.tables.records.*;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.pmtask.PmTaskProcessStatus;
import com.everhomes.rest.pmtask.PmTaskStatus;
import com.everhomes.rest.pmtask.PmTaskTargetStatus;
import com.everhomes.schema.tables.pojos.EhNamespaces;
import com.everhomes.schema.tables.records.EhNamespacesRecord;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class PmTaskProviderImpl implements PmTaskProvider{
	
	@Autowired 
    private SequenceProvider sequenceProvider;
	
	@Autowired
    private DbProvider dbProvider;
	
//	@Caching(evict = { @CacheEvict(value="listPmTask") })
	@Override
    public void createTask(PmTask pmTask){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmTasks.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhPmTasksDao dao = new EhPmTasksDao(context.configuration());
    	pmTask.setId(id);
    	dao.insert(pmTask);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmTasks.class, null);
    }

	@Override
	public void deleteTask(PmTask pmTask){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmTasksDao dao = new EhPmTasksDao(context.configuration());
		dao.delete(pmTask);
	}
	
	@Override
    public void createTaskTarget(PmTaskTarget pmTaskTarget){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmTaskTargets.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhPmTaskTargetsDao dao = new EhPmTaskTargetsDao(context.configuration());
    	pmTaskTarget.setId(id);
    	dao.insert(pmTaskTarget);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmTaskTargets.class, null);
    }

	@Override
	public void createTaskHistoryAddress(PmTaskHistoryAddress pmTaskHistoryAddress){
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmTaskHistoryAddresses.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmTaskHistoryAddressesDao dao = new EhPmTaskHistoryAddressesDao(context.configuration());
		pmTaskHistoryAddress.setId(id);
		dao.insert(pmTaskHistoryAddress);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmTaskHistoryAddresses.class, null);
	}

	@Override
	public PmTaskHistoryAddress findTaskHistoryAddressById(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmTaskHistoryAddressesDao dao = new EhPmTaskHistoryAddressesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), PmTaskHistoryAddress.class);
	}

	@Override
	public void updateTaskHistoryAddress(PmTaskHistoryAddress pmTaskHistoryAddress) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmTaskHistoryAddressesDao dao = new EhPmTaskHistoryAddressesDao(context.configuration());
		dao.update(pmTaskHistoryAddress);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmTaskHistoryAddresses.class, null);
	}

	@Override
	public List<PmTaskHistoryAddress> listTaskHistoryAddresses(Integer namespaceId, String ownerType, Long ownerId, Long userId,
															   Long pageAnchor, Integer pageSize) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
		SelectQuery<EhPmTaskHistoryAddressesRecord> query = context.selectQuery(Tables.EH_PM_TASK_HISTORY_ADDRESSES);

		query.addConditions(Tables.EH_PM_TASK_HISTORY_ADDRESSES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PM_TASK_HISTORY_ADDRESSES.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_PM_TASK_HISTORY_ADDRESSES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_PM_TASK_HISTORY_ADDRESSES.STATUS.eq(PmTaskHistoryAddressStatus.ACTIVE.getCode()));
		if(null != userId)
			query.addConditions(Tables.EH_PM_TASK_HISTORY_ADDRESSES.CREATOR_UID.eq(userId));
		if(null != pageAnchor && pageAnchor != 0)
			query.addConditions(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.gt(pageAnchor));
		if(null != pageSize)
			query.addLimit(pageSize);
		query.addOrderBy(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.asc());

		List<PmTaskHistoryAddress> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTaskHistoryAddress.class))
				.collect(Collectors.toList());

		return result;
	}

	@Override
    public List<PmTaskTarget> listTaskTargets(String ownerType, Long ownerId, Byte roleId, Long pageAnchor, Integer pageSize){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectQuery<EhPmTaskTargetsRecord> query = context.selectQuery(Tables.EH_PM_TASK_TARGETS);

        query.addConditions(Tables.EH_PM_TASK_TARGETS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PM_TASK_TARGETS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PM_TASK_TARGETS.STATUS.eq(PmTaskTargetStatus.ACTIVE.getCode()));
        if(null != roleId)
        	query.addConditions(Tables.EH_PM_TASK_TARGETS.ROLE_ID.eq(roleId));
        if(null != pageAnchor && pageAnchor != 0)
        	query.addConditions(Tables.EH_PM_TASK_TARGETS.ID.gt(pageAnchor));
        if(null != pageSize)
        	query.addLimit(pageSize);
        query.addOrderBy(Tables.EH_PM_TASK_TARGETS.ID.asc());
        
        List<PmTaskTarget> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTaskTarget.class))
        		.collect(Collectors.toList());
        
        return result;
    }
	
	@Override
    public PmTaskTarget findTaskTarget(String ownerType, Long ownerId, Byte roleId, String targetType, Long targetId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectQuery<EhPmTaskTargetsRecord> query = context.selectQuery(Tables.EH_PM_TASK_TARGETS);

        query.addConditions(Tables.EH_PM_TASK_TARGETS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PM_TASK_TARGETS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PM_TASK_TARGETS.ROLE_ID.eq(roleId));
        query.addConditions(Tables.EH_PM_TASK_TARGETS.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_PM_TASK_TARGETS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_PM_TASK_TARGETS.STATUS.eq(PmTaskTargetStatus.ACTIVE.getCode()));
        
        // 有可能有多行重复数据，此时使用fetchone会报错，但为什么会产生多行重复数据待春节后孙稳回来定位 by lqs 20170123
        // PmTaskTarget result = ConvertHelper.convert(query.fetchOne(), PmTaskTarget.class);
        PmTaskTarget result = ConvertHelper.convert(query.fetchAny(), PmTaskTarget.class);
        return result;
    }
	
	@Override
    public void updateTaskTarget(PmTaskTarget pmTaskTarget){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhPmTaskTargetsDao dao = new EhPmTaskTargetsDao(context.configuration());
    	dao.update(pmTaskTarget);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmTaskTargets.class, null);
    }

	@Override
	public void deleteTaskTarget(PmTaskTarget pmTaskTarget){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmTaskTargetsDao dao = new EhPmTaskTargetsDao(context.configuration());
		dao.delete(pmTaskTarget);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmTaskTargets.class, null);
	}
	
//	@Caching(evict = { 
//			@CacheEvict(value="PmTask", key="#pmTask.id")
//			/*@CacheEvict(value="listPmTask")*/})
	@Override
    public void updateTask(PmTask pmTask){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhPmTasksDao dao = new EhPmTasksDao(context.configuration());
    	dao.update(pmTask);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmTasks.class, null);
    }
	
	@Override
    public void createTaskLog(PmTaskLog pmTaskLog){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmTaskLogs.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmTaskLogsDao dao = new EhPmTaskLogsDao(context.configuration());
        pmTaskLog.setId(id);
        pmTaskLog.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
    	dao.insert(pmTaskLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmTaskLogs.class, null);
    }
	
	@Override
	public void createTaskAttachment(PmTaskAttachment pmTaskAttachment){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmTaskAttachments.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhPmTaskAttachmentsDao dao = new EhPmTaskAttachmentsDao(context.configuration());
    	pmTaskAttachment.setId(id);
    	dao.insert(pmTaskAttachment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmTaskAttachments.class, null);
    }
    
//	@Cacheable(value="PmTask", key="#id", unless="#result == null")
	@Override
    public PmTask findTaskById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
    	EhPmTasksDao dao = new EhPmTasksDao(context.configuration());
    	
    	return ConvertHelper.convert(dao.findById(id), PmTask.class);
    }
	
	@Override
    public PmTaskLog findTaskLogById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTaskLogs.class));
    	EhPmTaskLogsDao dao = new EhPmTaskLogsDao(context.configuration());
    	
    	return ConvertHelper.convert(dao.findById(id), PmTaskLog.class);
    }
	
	@Override
	public List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId,
			Long pageAnchor, Integer pageSize){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectQuery<EhPmTasksRecord> query = context.selectQuery(Tables.EH_PM_TASKS);

        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_ID.eq(ownerId));
        if(null != userId)
        	query.addConditions(Tables.EH_PM_TASKS.CREATOR_UID.eq(userId));
        if(null != pageAnchor && pageAnchor != 0)
        	query.addConditions(Tables.EH_PM_TASKS.CREATE_TIME.gt(new Timestamp(pageAnchor)));
        
        query.addConditions(Tables.EH_PM_TASKS.STATUS.ne(PmTaskStatus.INACTIVE.getCode()));
        query.addOrderBy(Tables.EH_PM_TASKS.CREATE_TIME.asc());
        if(null != pageSize)
        	query.addLimit(pageSize);
        
        List<PmTask> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTask.class)).collect(Collectors.toList());
        return result;
	}
	
	@Override
	public List<PmTask> listPmTask(String ownerType, Long ownerId, Long taskCategoryId, Byte status){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectQuery<EhPmTasksRecord> query = context.selectQuery(Tables.EH_PM_TASKS);

        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_ID.eq(ownerId));
        if(null != taskCategoryId)
        	query.addConditions(Tables.EH_PM_TASKS.TASK_CATEGORY_ID.eq(taskCategoryId));
        if(null != status)
        	query.addConditions(Tables.EH_PM_TASKS.STATUS.eq(status));
        
        List<PmTask> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTask.class)).collect(Collectors.toList());
        return result;
	}
	
	@Override
	public List<PmTask> listPmTask4Stat(String ownerType, Long ownerId, Long taskCategoryId, Long userId, Timestamp startDate, Timestamp endDate){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectQuery<EhPmTasksRecord> query = context.selectQuery(Tables.EH_PM_TASKS);

        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_ID.eq(ownerId));
        if(null != taskCategoryId)
        	query.addConditions(Tables.EH_PM_TASKS.TASK_CATEGORY_ID.eq(taskCategoryId));
        if(null != userId) {
        	query.addJoin(Tables.EH_PM_TASK_LOGS, Tables.EH_PM_TASK_LOGS.TASK_ID.eq(Tables.EH_PM_TASKS.ID));
        	query.addConditions(Tables.EH_PM_TASK_LOGS.OPERATOR_UID.eq(userId));
        	query.addConditions(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.PROCESSED.getCode()).
        			or(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.REVISITED.getCode())));
        	query.addGroupBy(Tables.EH_PM_TASKS.ID);
        }
        if(null != startDate)
            query.addConditions(Tables.EH_PM_TASKS.CREATE_TIME.gt(startDate));
        if(null != endDate)
            query.addConditions(Tables.EH_PM_TASKS.CREATE_TIME.lt(endDate));
        query.addConditions(Tables.EH_PM_TASKS.OPERATOR_STAR.ne((byte)0));
        
        List<PmTask> result = query.fetch().map(
                new DefaultRecordMapper(Tables.EH_PM_TASKS.recordType(), PmTask.class));
        return result;
	}
	
	//查询管理员已办任务，未办任务， 用户发的任务
//	@Cacheable(value="listPmTask",key="{#ownerType, #ownerId, #userId, #status}", unless="#result.size() == 0")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Byte status, Long taskCategoryId,
			Long pageAnchor, Integer pageSize){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectJoinStep<Record> query = context.select(Tables.EH_PM_TASKS.fields()).from(Tables.EH_PM_TASKS);
        Condition condition = Tables.EH_PM_TASKS.OWNER_TYPE.eq(ownerType);
        condition = condition.and(Tables.EH_PM_TASKS.OWNER_ID.eq(ownerId));
        
        if(null != pageAnchor && pageAnchor != 0)
        	condition = condition.and(Tables.EH_PM_TASKS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        
        if(null != status && status.equals(PmTaskProcessStatus.UNPROCESSED.getCode())){
        	query.join(Tables.EH_PM_TASK_LOGS).on(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(Tables.EH_PM_TASKS.ID));
        	condition = condition.and(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.UNPROCESSED.getCode())
        			.or(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.PROCESSING.getCode())
        					.and(Tables.EH_PM_TASK_LOGS.TARGET_ID.eq(userId))));
        	query.groupBy(Tables.EH_PM_TASKS.ID);
        	query.orderBy(Tables.EH_PM_TASKS.CREATE_TIME.desc());
        	
    	}else if(null != status && status.equals(PmTaskProcessStatus.PROCESSED.getCode())){
    		
//    		query.join(Tables.EH_PM_TASK_LOGS).on(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(Tables.EH_PM_TASKS.ID));
    		
    		query.join(context.select(Tables.EH_PM_TASK_LOGS.OPERATOR_UID,Tables.EH_PM_TASK_LOGS.OPERATOR_TIME,Tables.EH_PM_TASK_LOGS.TASK_ID)
    				.from(Tables.EH_PM_TASK_LOGS).where(Tables.EH_PM_TASK_LOGS.STATUS.ge(PmTaskStatus.PROCESSING.getCode()))
    				.and(Tables.EH_PM_TASK_LOGS.OPERATOR_UID.eq(userId))
    				.and(Tables.EH_PM_TASK_LOGS.ID.in(context.select(Tables.EH_PM_TASK_LOGS.ID.max())
    	    				.from(Tables.EH_PM_TASK_LOGS).groupBy(Tables.EH_PM_TASK_LOGS.TASK_ID)))
    				.asTable(Tables.EH_PM_TASK_LOGS.getName()))
    		.on(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(Tables.EH_PM_TASKS.ID));
    		
//    		condition = condition.and(Tables.EH_PM_TASK_LOGS.OPERATOR_UID.eq(userId));
//    		condition = condition.and(Tables.EH_PM_TASKS.STATUS.ge(PmTaskStatus.PROCESSING.getCode())
//    				.or(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.PROCESSED.getCode()))
//    				.or(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.CLOSED.getCode())));
    		condition = condition.and(Tables.EH_PM_TASKS.STATUS.ge(PmTaskStatus.PROCESSING.getCode()));
//    		query.groupBy(Tables.EH_PM_TASKS.ID);
    		query.orderBy(Tables.EH_PM_TASK_LOGS.OPERATOR_TIME.desc());
    	}else if(null != status && status.equals(PmTaskProcessStatus.USER_UNPROCESSED.getCode())){
//        	query.join(Tables.EH_PM_TASK_LOGS).on(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(Tables.EH_PM_TASKS.ID));
//        	condition = condition.and(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.PROCESSING.getCode())
//        					.and(Tables.EH_PM_TASK_LOGS.TARGET_ID.eq(userId)));
//        	query.groupBy(Tables.EH_PM_TASKS.ID);   
    		
    		query.join(context.select(Tables.EH_PM_TASK_LOGS.TARGET_ID,Tables.EH_PM_TASK_LOGS.OPERATOR_TIME,Tables.EH_PM_TASK_LOGS.TASK_ID)
    				.from(Tables.EH_PM_TASK_LOGS).where(Tables.EH_PM_TASK_LOGS.STATUS.eq(PmTaskStatus.PROCESSING.getCode()))
    				.and(Tables.EH_PM_TASK_LOGS.ID.in(context.select(Tables.EH_PM_TASK_LOGS.ID.max())
    	    				.from(Tables.EH_PM_TASK_LOGS).groupBy(Tables.EH_PM_TASK_LOGS.TASK_ID)))
    				.asTable(Tables.EH_PM_TASK_LOGS.getName()))
    		.on(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(Tables.EH_PM_TASKS.ID));
        	condition = condition.and(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.PROCESSING.getCode())
        					.and(Tables.EH_PM_TASK_LOGS.TARGET_ID.eq(userId)));
        	query.orderBy(Tables.EH_PM_TASK_LOGS.OPERATOR_TIME.desc());
        	
    	}else{
    		if(null != taskCategoryId)
        		condition = condition.and(Tables.EH_PM_TASKS.TASK_CATEGORY_ID.eq(taskCategoryId));
    		condition = condition.and(Tables.EH_PM_TASKS.CREATOR_UID.eq(userId));
    		condition = condition.and(Tables.EH_PM_TASKS.STATUS.ne(PmTaskStatus.INACTIVE.getCode()));
    		query.orderBy(Tables.EH_PM_TASKS.CREATE_TIME.desc());
    	}
        
        if(null != pageSize)
        	query.limit(pageSize);
        
        List<PmTask> result = query.where(condition).fetch().map(
            new DefaultRecordMapper(Tables.EH_PM_TASKS.recordType(), PmTask.class));
        return result;
	}
	
	@Override
	public Integer countUserProccsingPmTask(String ownerType, Long ownerId, Long userId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectJoinStep<Record> query = context.select(Tables.EH_PM_TASKS.fields()).from(Tables.EH_PM_TASKS);
        Condition condition = Tables.EH_PM_TASKS.OWNER_TYPE.eq(ownerType);
        condition = condition.and(Tables.EH_PM_TASKS.OWNER_ID.eq(ownerId));
        
        query.join(context.select(Tables.EH_PM_TASK_LOGS.TARGET_ID,Tables.EH_PM_TASK_LOGS.TASK_ID)
				.from(Tables.EH_PM_TASK_LOGS).where(Tables.EH_PM_TASK_LOGS.STATUS.eq(PmTaskStatus.PROCESSING.getCode()))
				.and(Tables.EH_PM_TASK_LOGS.ID.in(context.select(Tables.EH_PM_TASK_LOGS.ID.max())
	    				.from(Tables.EH_PM_TASK_LOGS).groupBy(Tables.EH_PM_TASK_LOGS.TASK_ID)))
				.asTable(Tables.EH_PM_TASK_LOGS.getName()))
		.on(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(Tables.EH_PM_TASKS.ID));
    	condition = condition.and(Tables.EH_PM_TASKS.STATUS.eq(PmTaskStatus.PROCESSING.getCode())
    					.and(Tables.EH_PM_TASK_LOGS.TARGET_ID.eq(userId)));	
        	
        return query.where(condition).fetchCount();
	}
	
	@Override
	public List<PmTaskLog> listPmTaskLogs(Long taskId, Byte status){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTaskLogs.class));
        
        SelectQuery<EhPmTaskLogsRecord> query = context.selectQuery(Tables.EH_PM_TASK_LOGS);
        if(null != taskId)
        	query.addConditions(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(taskId));
        if(null != status)
        	query.addConditions(Tables.EH_PM_TASK_LOGS.STATUS.eq(status));
        else
        	query.addConditions(Tables.EH_PM_TASK_LOGS.STATUS.ne(PmTaskStatus.INACTIVE.getCode()));
        query.addOrderBy(Tables.EH_PM_TASK_LOGS.OPERATOR_TIME.desc());
        List<PmTaskLog> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTaskLog.class))
        		.collect(Collectors.toList());
        
        return result;
	}
	
	@Override
	public List<PmTaskAttachment> listPmTaskAttachments(Long ownerId, String ownerType){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTaskAttachments.class));
        
        SelectQuery<EhPmTaskAttachmentsRecord> query = context.selectQuery(Tables.EH_PM_TASK_ATTACHMENTS);
        query.addConditions(Tables.EH_PM_TASK_ATTACHMENTS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PM_TASK_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
        
        List<PmTaskAttachment> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTaskAttachment.class))
        		.collect(Collectors.toList());
        
        return result;
	}
	
	@Override
	public List<Namespace> listNamespace(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhNamespaces.class));
        SelectQuery<EhNamespacesRecord> query = context.selectQuery(com.everhomes.schema.Tables.EH_NAMESPACES);
        
        return query.fetch().stream().map(r -> ConvertHelper.convert(r, Namespace.class))
        		.collect(Collectors.toList());
	}
	@Override
	public Integer countTask(Long ownerId, Byte status, Long taskCategoryId, Long categoryId, Byte star, Timestamp startDate, Timestamp endDate){
        //DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhPmTasks.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	
                	SelectJoinStep<Record1<Integer>> query = context.selectCount().from(Tables.EH_PM_TASKS);
                	
                	Condition condition = Tables.EH_PM_TASKS.OWNER_ID.equal(ownerId);
                	condition = condition.and(Tables.EH_PM_TASKS.STATUS.ne(PmTaskStatus.INACTIVE.getCode()));
                	if(null != taskCategoryId)
                    	condition = condition.and(Tables.EH_PM_TASKS.TASK_CATEGORY_ID.eq(taskCategoryId));
                	if(null != categoryId)
                    	condition = condition.and(Tables.EH_PM_TASKS.CATEGORY_ID.eq(categoryId));
                	if(null != status)
                		condition = condition.and(Tables.EH_PM_TASKS.STATUS.equal(status));
                    if(null != star)
                    	condition = condition.and(Tables.EH_PM_TASKS.STAR.equal(star));
                	if(null != startDate)
                    	condition = condition.and(Tables.EH_PM_TASKS.CREATE_TIME.gt(startDate));
                	if(null != endDate)
                    	condition = condition.and(Tables.EH_PM_TASKS.CREATE_TIME.lt(endDate));
                	
                    count[0] = query.where(condition).fetchOneInto(Integer.class);
                    return true;
                });
		return count[0];
	}
	
	@Override
    public void createTaskStatistics(PmTaskStatistics statistics){
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmTaskStatistics.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmTaskStatisticsDao dao = new EhPmTaskStatisticsDao(context.configuration());
        statistics.setId(id);
    	dao.insert(statistics);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmTaskStatistics.class, null);
    }
	
	@Override
	public List<PmTaskStatistics> searchTaskStatistics(Integer namespaceId, Long ownerId, Long taskCategoryId, String keyword, Timestamp dateStr,
			Long pageAnchor, Integer pageSize){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTaskStatistics.class));
        
        SelectJoinStep<Record> query = context.select(Tables.EH_PM_TASK_STATISTICS.fields()).from(Tables.EH_PM_TASK_STATISTICS);
        Condition condition = Tables.EH_PM_TASK_STATISTICS.NAMESPACE_ID.eq(namespaceId);
        
        if(null != taskCategoryId)
        	condition = condition.and(Tables.EH_PM_TASK_STATISTICS.TASK_CATEGORY_ID.eq(taskCategoryId));
        if(null != ownerId)
        	condition = condition.and(Tables.EH_PM_TASK_STATISTICS.OWNER_ID.eq(ownerId));
        if(null != dateStr)
        	condition = condition.and(Tables.EH_PM_TASK_STATISTICS.DATE_STR.eq(dateStr));
        if(StringUtils.isNotBlank(keyword)){
        	query.join(Tables.EH_COMMUNITIES).on(Tables.EH_COMMUNITIES.ID.eq(Tables.EH_PM_TASK_STATISTICS.OWNER_ID));
        	condition = condition.and(Tables.EH_COMMUNITIES.NAME.like("%"+keyword+"%").or(Tables.EH_COMMUNITIES.ALIAS_NAME.like("%"+keyword+"%")));
        }
        if(null != pageAnchor)
            condition = condition.and(Tables.EH_PM_TASK_STATISTICS.ID.gt(pageAnchor));
        query.orderBy(Tables.EH_PM_TASK_STATISTICS.ID.asc());
        if(null != pageSize)
        	query.limit(pageSize);
        
		return query.where(condition).fetch().map(new DefaultRecordMapper(Tables.EH_PM_TASK_STATISTICS.recordType(), PmTaskStatistics.class));
	}
	
	@Override
	public List<PmTaskTargetStatistic> searchTaskTargetStatistics(Integer namespaceId, Long ownerId, Long taskCategoryId, Long userId, 
			Timestamp dateStr, Long pageAnchor, Integer pageSize){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTaskTargetStatistics.class));
        
        SelectJoinStep<Record> query = context.select(Tables.EH_PM_TASK_TARGET_STATISTICS.fields()).from(Tables.EH_PM_TASK_TARGET_STATISTICS);
        Condition condition = Tables.EH_PM_TASK_TARGET_STATISTICS.NAMESPACE_ID.eq(namespaceId);
        
        if(null != taskCategoryId)
        	condition = condition.and(Tables.EH_PM_TASK_TARGET_STATISTICS.TASK_CATEGORY_ID.eq(taskCategoryId));
        if(null != ownerId)
        	condition = condition.and(Tables.EH_PM_TASK_TARGET_STATISTICS.OWNER_ID.eq(ownerId));
        if(null != dateStr)
        	condition = condition.and(Tables.EH_PM_TASK_TARGET_STATISTICS.DATE_STR.eq(dateStr));
        if(null != userId){
        	condition = condition.and(Tables.EH_PM_TASK_TARGET_STATISTICS.TARGET_ID.eq(userId));        }
        if(null != pageAnchor)
            condition = condition.and(Tables.EH_PM_TASK_TARGET_STATISTICS.ID.gt(pageAnchor));
        query.orderBy(Tables.EH_PM_TASK_TARGET_STATISTICS.ID.asc());
        if(null != pageSize)
        	query.limit(pageSize);
        
		return query.where(condition).fetch().map(new DefaultRecordMapper(Tables.EH_PM_TASK_TARGET_STATISTICS.recordType(), 
				PmTaskTargetStatistic.class));
	}
	
	@Override
	public Integer countTaskStatistics(Long ownerId, Long taskCategoryId, Timestamp dateStr){
        //DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhPmTasks.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	
                	SelectJoinStep<Record1<BigDecimal>> query = context.select(Tables.EH_PM_TASK_STATISTICS.TOTAL_COUNT.sum()).from(Tables.EH_PM_TASK_STATISTICS);
                	
                	Condition condition = Tables.EH_PM_TASK_STATISTICS.OWNER_ID.equal(ownerId);
                	if(null != taskCategoryId)
                    	condition = condition.and(Tables.EH_PM_TASK_STATISTICS.TASK_CATEGORY_ID.eq(taskCategoryId));
                	if(null != dateStr)
                    	condition = condition.and(Tables.EH_PM_TASK_STATISTICS.DATE_STR.eq(dateStr));

                    count[0] = query.where(condition).fetchOneInto(BigDecimal.class).intValue();
                    return true;
                });
		return count[0];
	}
	
	@Override
    public void createTaskTargetStatistic(PmTaskTargetStatistic pmTaskTargetStatistic) {
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmTaskTargetStatistics.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmTaskTargetStatisticsDao dao = new EhPmTaskTargetStatisticsDao(context.configuration());
        pmTaskTargetStatistic.setId(id);
    	dao.insert(pmTaskTargetStatistic);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmTaskTargetStatistics.class, null);
    }

	/**
	 * 金地同步数据使用
	 */
	@Override
	public List<PmTaskLog> listRepairByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_PM_TASK_LOGS)
			.where(Tables.EH_PM_TASK_LOGS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_PM_TASK_LOGS.OPERATOR_TIME.eq(new Timestamp(timestamp)))
			.and(Tables.EH_PM_TASK_LOGS.ID.gt(pageAnchor))
			.orderBy(Tables.EH_PM_TASK_LOGS.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, PmTaskLog.class));
		}
		return new ArrayList<PmTaskLog>();
	}
	
	/**
	 * 金地同步数据使用
	 */
	@Override
	public List<PmTaskLog> listRepairByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_PM_TASK_LOGS)
			.where(Tables.EH_PM_TASK_LOGS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_PM_TASK_LOGS.OPERATOR_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_PM_TASK_LOGS.OPERATOR_TIME.asc(), Tables.EH_PM_TASK_LOGS.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, PmTaskLog.class));
		}
		return new ArrayList<PmTaskLog>();
	}

	@Override
	public List<PmTask> listTasksById(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
		SelectQuery<EhPmTasksRecord> query = context.selectQuery(Tables.EH_PM_TASKS);

		if(null != ids)
			query.addConditions(Tables.EH_PM_TASKS.ID.in(ids));

		List<PmTask> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTask.class)).collect(Collectors.toList());
		return result;
	}
}
