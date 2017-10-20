package com.everhomes.repeat;

import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.repeat.RepeatSettingStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhRepeatSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhRepeatSettings;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class RepeatProviderImpl implements RepeatProvider {

private static final Logger LOGGER = LoggerFactory.getLogger(RepeatProviderImpl.class);
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
    private ShardingProvider shardingProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	@Override
	public void createRepeatSettings(RepeatSettings repeat) {

		long id = this.shardingProvider.allocShardableContentId(EhRepeatSettings.class).second();
		
		repeat.setId(id);
		repeat.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		repeat.setStatus(RepeatSettingStatus.ACTIVE.getCode());
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhRepeatSettings.class, id));
        EhRepeatSettingsDao dao = new EhRepeatSettingsDao(context.configuration());
        dao.insert(repeat);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRepeatSettings.class, null);
		
	}

	@Override
	public void updateRepeatSettings(RepeatSettings repeat) {
		assert(repeat.getId() != null);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhRepeatSettings.class, repeat.getId()));
		EhRepeatSettingsDao dao = new EhRepeatSettingsDao(context.configuration());
		dao.update(repeat);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRepeatSettings.class, repeat.getId());

	}

	@Override
	public void deleteRepeatSettingsById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RepeatSettings findRepeatSettingById(Long id) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhRepeatSettings.class, id));
        
		EhRepeatSettingsDao dao = new EhRepeatSettingsDao(context.configuration());
		EhRepeatSettings record = dao.findById(id);
        return ConvertHelper.convert(record, RepeatSettings.class);
	}

}
