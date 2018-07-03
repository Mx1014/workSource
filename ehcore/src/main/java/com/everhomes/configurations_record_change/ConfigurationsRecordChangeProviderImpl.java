package com.everhomes.configurations_record_change;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.schema.tables.daos.EhConfigurationsDao;
import com.everhomes.schema.tables.pojos.EhConfigurations;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhConfigurationsRecordChangeDao;
import com.everhomes.server.schema.tables.pojos.EhConfigurationsRecordChange;

/**
 * 
 * @author huanglm
 *
 */
@Component
public class ConfigurationsRecordChangeProviderImpl implements ConfigurationsRecordChangeProvider{

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void crteateConfiguration(ConfigurationsRecordChange bo) {
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfigurationsRecordChange.class));
		bo.setId(id.intValue());
						
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsRecordChangeDao dao = new EhConfigurationsRecordChangeDao(context.configuration());
		dao.insert(bo);
				
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhConfigurationsRecordChange.class, null);
		
		
	}

}
