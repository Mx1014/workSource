package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.List;

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
import com.everhomes.server.schema.tables.daos.EhAllianceExtraEventAttachmentDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceProviders;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component
public class AllianceExtraEventAttachProviderImpl implements AllianceExtraEventAttachProvider{
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	
	com.everhomes.server.schema.tables.EhAllianceExtraEventAttachment table = Tables.EH_ALLIANCE_EXTRA_EVENT_ATTACHMENT;

	@Override
	public void createAllianceExtraEventAttach(AllianceExtraEventAttachment allianceExtraEventAttachment) {
		// 获取jooq上下文
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		
		// 设置动态属性 如id，createTime
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceProviders.class));
		allianceExtraEventAttachment.setId(id);
		allianceExtraEventAttachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		allianceExtraEventAttachment.setCreateUid(UserContext.currentUserId());
		
		// 使用dao方法
		EhAllianceExtraEventAttachmentDao dao = new EhAllianceExtraEventAttachmentDao(context.configuration());
		dao.insert(allianceExtraEventAttachment);
		
		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceProviders.class, null);
	}

	@Override
	public List<AllianceExtraEventAttachment> listAttachmentsByEventId(Long eventId) {

		if (eventId == null || eventId <= 0) {
			return null;
		}

		// 获取jooq上下文
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		return context.select().from(table).where(table.OWNER_ID.eq(eventId))
				.fetchInto(AllianceExtraEventAttachment.class);
	}

}
