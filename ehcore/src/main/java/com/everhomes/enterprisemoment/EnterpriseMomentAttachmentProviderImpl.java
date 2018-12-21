// @formatter:off
package com.everhomes.enterprisemoment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentAttachmentsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentAttachments;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class EnterpriseMomentAttachmentProviderImpl implements EnterpriseMomentAttachmentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createEnterpriseMomentAttachment(EnterpriseMomentAttachment enterpriseMomentAttachment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentAttachments.class));
		enterpriseMomentAttachment.setId(id);
		enterpriseMomentAttachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		getReadWriteDao().insert(enterpriseMomentAttachment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseMomentAttachments.class, null);
	}

	@Override
	public void updateEnterpriseMomentAttachment(EnterpriseMomentAttachment enterpriseMomentAttachment) {
		assert (enterpriseMomentAttachment.getId() != null);
		getReadWriteDao().update(enterpriseMomentAttachment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseMomentAttachments.class, enterpriseMomentAttachment.getId());
	}

	@Override
	public EnterpriseMomentAttachment findEnterpriseMomentAttachmentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), EnterpriseMomentAttachment.class);
	}
	
	@Override
	public List<EnterpriseMomentAttachment> listEnterpriseMomentAttachment() {
		return getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENT_ATTACHMENTS)
				.orderBy(Tables.EH_ENTERPRISE_MOMENT_ATTACHMENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, EnterpriseMomentAttachment.class));
	}

	@Override
	public List<EnterpriseMomentAttachment> listEnterpriseMomentAttachment(Integer namespaceId, Long organizationId, Long momentId) {
		Result<Record> records = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENT_ATTACHMENTS)
				.where(Tables.EH_ENTERPRISE_MOMENT_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENT_ATTACHMENTS.ENTERPRISE_MOMENT_ID.eq(momentId))
				.orderBy(Tables.EH_ENTERPRISE_MOMENT_ATTACHMENTS.ID.asc())
				.fetch();
		if (CollectionUtils.isEmpty(records)) {
			return new ArrayList<>();
		}
		return records.map(r -> ConvertHelper.convert(r, EnterpriseMomentAttachment.class));
	}

	private EhEnterpriseMomentAttachmentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentAttachmentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentAttachmentsDao getDao(DSLContext context) {
		return new EhEnterpriseMomentAttachmentsDao(context.configuration());
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
