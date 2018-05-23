package com.everhomes.configurations;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.schema.Tables;
import com.everhomes.schema.tables.daos.EhConfigurationsDao;
import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class ConfigurationsAdminProviderImpl implements ConfigurationsProvider{

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<Configurations> listConfigurations(Integer namespaceId, String name,
			String value, Integer pageSize, CrossShardListingLocator locator) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = null ;
		//默认域空间ID是不能为空的，若可为空该逻辑得改		
		condition = Tables.EH_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId);
		//不为空才拼接条件
		if( !StringUtils.isBlank(name)){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.NAME.like("%" + name + "%"));
		}
		//不为空才拼接条件
		if(!StringUtils.isBlank(value)){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.VALUE.like("%" + value + "%"));
		}
		//分页起点
		if(null != locator && null != locator.getAnchor()){
			condition  = condition.and(Tables.EH_CONFIGURATIONS.ID.lt(locator.getAnchor().intValue()));
		}
		
		//创建返回对象
		List<Configurations> result  = new ArrayList<Configurations>();
		
		SelectJoinStep<Record> query = context.select().from(Tables.EH_CONFIGURATIONS);
		query.where(condition);
		//默认ID倒序（更容易看到新增的）
		query.orderBy(Tables.EH_CONFIGURATIONS.ID.desc());
		//限制每页查询量
		pageSize = pageSize + 1;
		query.limit(pageSize);
		result = query.fetch().map((r) -> ConvertHelper.convert(r, Configurations.class));
		
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
	public Configurations getConfigurationById(Integer id, Integer namespaceId) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		EhConfigurations ehBo = dao.findById(id);
		
		return ConvertHelper.convert(ehBo, Configurations.class);
	}

	@Override
	public void crteateConfiguration(Configurations bo) {
		
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfigurations.class));
		bo.setId(id.intValue());
				
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.insert(bo);
		
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhConfigurations.class, null);
		
	}

	@Override
	public void updateConfiguration(Configurations bo) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.update(bo);
		
		//广播更新数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfigurations.class, bo.getId().longValue());
		
	}

	@Override
	public void deleteConfiguration(Integer id) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.deleteById(id);
		
		//广播删除数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfigurations.class, id.longValue());
		
	}
	


}
