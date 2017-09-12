// @formatter:off
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
import com.everhomes.server.schema.tables.daos.EhServiceAllianceCommentAttachmentsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCommentAttachments;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ServiceAllianceCommentAttachmentProviderImpl implements ServiceAllianceCommentAttachmentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createServiceAllianceCommentAttachment(ServiceAllianceCommentAttachment serviceAllianceCommentAttachment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceCommentAttachments.class));
		serviceAllianceCommentAttachment.setId(id);
		serviceAllianceCommentAttachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceAllianceCommentAttachment.setCreatorUid(UserContext.current().getUser().getId());
		serviceAllianceCommentAttachment.setUpdateTime(serviceAllianceCommentAttachment.getCreateTime());
		serviceAllianceCommentAttachment.setOperatorUid(serviceAllianceCommentAttachment.getCreatorUid());
		getReadWriteDao().insert(serviceAllianceCommentAttachment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceCommentAttachments.class, null);
	}

	@Override
	public void updateServiceAllianceCommentAttachment(ServiceAllianceCommentAttachment serviceAllianceCommentAttachment) {
		assert (serviceAllianceCommentAttachment.getId() != null);
		serviceAllianceCommentAttachment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		serviceAllianceCommentAttachment.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(serviceAllianceCommentAttachment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAllianceCommentAttachments.class, serviceAllianceCommentAttachment.getId());
	}

	@Override
	public ServiceAllianceCommentAttachment findServiceAllianceCommentAttachmentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ServiceAllianceCommentAttachment.class);
	}
	
	@Override
	public List<ServiceAllianceCommentAttachment> listServiceAllianceCommentAttachment() {
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_ALLIANCE_COMMENT_ATTACHMENTS)
				.orderBy(Tables.EH_SERVICE_ALLIANCE_COMMENT_ATTACHMENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ServiceAllianceCommentAttachment.class));
	}
	
	private EhServiceAllianceCommentAttachmentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhServiceAllianceCommentAttachmentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhServiceAllianceCommentAttachmentsDao getDao(DSLContext context) {
		return new EhServiceAllianceCommentAttachmentsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
