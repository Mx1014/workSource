package com.everhomes.configuration;

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

@Component
public class ConfigurationsProviderImpl implements ConfigurationsProvider{

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Override
	public void createConfiguration(Configurations configurations) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfigurations.class));
		configurations.setId(id.intValue());
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhConfigurationsDao dao = new EhConfigurationsDao(context.configuration());
		dao.insert(configurations);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhConfigurationsDao.class, id);
	}

}
