package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.pmtask.PmTaskStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPmTaskAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhPmTaskLogsDao;
import com.everhomes.server.schema.tables.daos.EhPmTasksDao;
import com.everhomes.server.schema.tables.pojos.EhPmTaskAttachments;
import com.everhomes.server.schema.tables.pojos.EhPmTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhPmTasks;
import com.everhomes.server.schema.tables.records.EhPmTaskAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhPmTaskLogsRecord;
import com.everhomes.server.schema.tables.records.EhPmTasksRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class PmProviderImpl implements PmTaskProvider{
	
	@Autowired 
    private SequenceProvider sequenceProvider;
	
	@Autowired
    private DbProvider dbProvider;
	
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
	public List<PmTask> listPmTask(String ownerType, Long ownerId, Long userId, Byte status, Boolean flag,
			Long pageAnchor, Integer pageSize){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
        SelectQuery<EhPmTasksRecord> query = context.selectQuery(Tables.EH_PM_TASKS);
        
        if(null == flag)
        	flag = true;
        
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
        	query.addConditions(Tables.EH_PM_TASKS.OWNER_ID.eq(ownerId));
        if(null != userId){
        	query.addConditions(Tables.EH_PM_TASKS.CREATOR_UID.eq(userId));
        }
        if(flag && null != status)
        	query.addConditions(Tables.EH_PM_TASKS.STATUS.eq(status));
        if(!flag && null != status){
        	query.addConditions(Tables.EH_PM_TASKS.STATUS.ne(status));
        }
        if(null != pageAnchor && pageAnchor != 0)
        	query.addConditions(Tables.EH_PM_TASKS.CREATE_TIME.gt(new Timestamp(pageAnchor)));
        query.addOrderBy(Tables.EH_PM_TASKS.CREATE_TIME.asc());
        if(null != pageSize)
        	query.addLimit(pageSize);
        
        List<PmTask> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTask.class)).collect(Collectors.toList());
        return result;
	}
	
//	@Override
//	public List<PmTask> listPmTask(String ownerType, Long ownerId, Long categoryId, Byte status, Boolean flag, String keyword, 
//			Long pageAnchor, Integer pageSize){
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTasks.class));
//        SelectQuery<EhPmTasksRecord> query = context.selectQuery(Tables.EH_PM_TASKS);
//        
//        if(null == flag)
//        	flag = true;
//        
//        if(StringUtils.isNotBlank(ownerType))
//        	query.addConditions(Tables.EH_PM_TASKS.OWNER_TYPE.eq(ownerType));
//        if(null != ownerId)
//        	query.addConditions(Tables.EH_PM_TASKS.OWNER_ID.eq(ownerId));
//        if(null != categoryId){
//        	query.addJoin(Tables.EH_CATEGORIES, Tables.EH_PM_TASKS.CATEGORY_ID.eq(Tables.EH_CATEGORIES.ID));
//        	query.addConditions(Tables.EH_CATEGORIES.PARENT_ID.eq(categoryId));
//        }
//        if(flag && null != status)
//        	query.addConditions(Tables.EH_PM_TASKS.STATUS.eq(status));
//        if(!flag && null != status){
//        	query.addConditions(Tables.EH_PM_TASKS.STATUS.ne(status));
//        }
//        if(null != pageAnchor && pageAnchor != 0)
//        	query.addConditions(Tables.EH_PM_TASKS.CREATE_TIME.gt(new Timestamp(pageAnchor)));
//        query.addOrderBy(Tables.EH_PM_TASKS.CREATE_TIME.asc());
//        if(null != pageSize)
//        	query.addLimit(pageSize);
//        
//        List<PmTask> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTask.class)).collect(Collectors.toList());
//        return result;
//	}
	
	@Override
	public List<PmTaskLog> listPmTaskLogs(Long taskId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTaskLogs.class));
        
        SelectQuery<EhPmTaskLogsRecord> query = context.selectQuery(Tables.EH_PM_TASK_LOGS);
        if(null != taskId)
        	query.addConditions(Tables.EH_PM_TASK_LOGS.TASK_ID.eq(taskId));
        query.addConditions(Tables.EH_PM_TASK_LOGS.STATUS.ne(PmTaskStatus.INACTIVE.getCode()));
        query.addOrderBy(Tables.EH_PM_TASK_LOGS.OPERATOR_TIME.asc());
        List<PmTaskLog> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTaskLog.class))
        		.collect(Collectors.toList());
        
        return result;
	}
	
	@Override
	public List<PmTaskAttachment> listPmTaskAttachments(Long ownerId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmTaskAttachments.class));
        
        SelectQuery<EhPmTaskAttachmentsRecord> query = context.selectQuery(Tables.EH_PM_TASK_ATTACHMENTS);
        if(null != ownerId)
        	query.addConditions(Tables.EH_PM_TASK_ATTACHMENTS.OWNER_ID.eq(ownerId));
        
        List<PmTaskAttachment> result = query.fetch().stream().map(r -> ConvertHelper.convert(r, PmTaskAttachment.class))
        		.collect(Collectors.toList());
        
        return result;
	}
	
}
