package com.everhomes.yellowPage;

import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceExtraEventsDao;
import com.everhomes.server.schema.tables.pojos.EhAllianceExtraEvents;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceProviders;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AllianceExtraEventProviderImpl implements AllianceExtraEventProvider{
	
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	
	
	com.everhomes.server.schema.tables.EhAllianceExtraEvents table = Tables.EH_ALLIANCE_EXTRA_EVENTS;
	

	@Override
	public void createAllianceExtraEvent(AllianceExtraEvent allianceExtraEvent) {
		// 获取jooq上下文  
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		
		// 设置动态属性 如id，createTime
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceProviders.class));
		allianceExtraEvent.setId(id);
		allianceExtraEvent.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		allianceExtraEvent.setCreateUid(UserContext.currentUserId());
		
		// 使用dao方法
		EhAllianceExtraEventsDao dao = new EhAllianceExtraEventsDao(context.configuration());
		dao.insert(allianceExtraEvent);
		
		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceProviders.class, null);
	}

	@Override
	public AllianceExtraEvent findAllianceExtraEventById(Long id) {
		// 获取jooq上下文
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhAllianceExtraEventsDao dao = new EhAllianceExtraEventsDao(context.configuration());
		EhAllianceExtraEvents event = dao.findById(id);
		if (null == event) {
			return null;
		}
		
		return ConvertHelper.convert(event, AllianceExtraEvent.class);
	}

}
