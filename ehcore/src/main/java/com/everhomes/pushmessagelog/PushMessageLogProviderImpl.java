package com.everhomes.pushmessagelog;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configurations.Configurations;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhConfigurationsDao;
import com.everhomes.server.schema.tables.daos.EhPushMessageLogDao;
import com.everhomes.server.schema.tables.pojos.EhConfigurations;
import com.everhomes.server.schema.tables.pojos.EhPushMessageLog;
import com.everhomes.util.ConvertHelper;

@Component
public class PushMessageLogProviderImpl implements PushMessageLogProvider {

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Override
	public List<PushMessageLog> listPushMessageLogByNamespaceIdAndOperator(Integer namespaceId, Integer operatorId, 
																Integer pageSize,CrossShardListingLocator locator) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = null ;
		//默认域空间ID是不能为空的，若可为空该逻辑得改		
		condition = Tables.EH_PUSH_MESSAGE_LOG.NAMESPACE_ID.eq(namespaceId);
		//不为空才拼接条件
		if( operatorId != null){
			condition  = condition.and(Tables.EH_PUSH_MESSAGE_LOG.OPERATOR_ID.eq(operatorId));
		}
		
		//分页起点
		if(null != locator && null != locator.getAnchor()){
			condition  = condition.and(Tables.EH_PUSH_MESSAGE_LOG.ID.lt(locator.getAnchor().intValue()));
		}
		
		//创建返回对象
		List<PushMessageLog> result  = new ArrayList<PushMessageLog>();
		
		SelectJoinStep<Record> query = context.select().from(Tables.EH_PUSH_MESSAGE_LOG);
		query.where(condition);
		//按推送创建时间倒序排
		query.orderBy(Tables.EH_PUSH_MESSAGE_LOG.CREATE_TIME.desc());
		
		if(pageSize != null){
			//限制每页查询量
			pageSize = pageSize + 1;
			query.limit(pageSize);
		}
		
		query.fetch().map((r) -> {	
			PushMessageLog bo = ConvertHelper.convert(r, PushMessageLog.class);
			result.add(bo);
			return null;
		});
		
		if(locator != null ){
			locator.setAnchor(null);//重置分页锚点类
			//因为在开始时查询数量增加了1，用于判断是否有后续页
			if(result.size() >= pageSize){
				result.remove(result.size() - 1);
				locator.setAnchor(result.get(result.size() - 1).getId().longValue());
			}
		}
		
		return result;
	}

	@Override
	public PushMessageLog getPushMessageLogById(Integer id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPushMessageLogDao dao = new EhPushMessageLogDao(context.configuration());
		EhPushMessageLog ehBo = dao.findById(id);
		
		return ConvertHelper.convert(ehBo, PushMessageLog.class);
	}

	@Override
	public Long crteatePushMessageLog(PushMessageLog bo) {

			//获取主键序列
			Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPushMessageLog.class));
			bo.setId(id.intValue());
						
			DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
			EhPushMessageLogDao dao = new EhPushMessageLogDao(context.configuration());
			dao.insert(bo);
				
			//广播插入数据事件
			DaoHelper.publishDaoAction(DaoAction.CREATE, EhPushMessageLog.class, null);
			return id;

	}

	@Override
	public void updatePushMessageLog(PushMessageLog bo) {
				
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPushMessageLogDao dao = new EhPushMessageLogDao(context.configuration());
		dao.update(bo);
				
		//广播更新数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPushMessageLog.class, bo.getId().longValue());

	}

	@Override
	public void deletePushMessageLog(PushMessageLog bo) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPushMessageLogDao dao = new EhPushMessageLogDao(context.configuration());
		dao.deleteById(bo.getId());
				
		//广播删除数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPushMessageLog.class, bo.getId().longValue());

	}

}
