// @formatter:off
package com.everhomes.approval;

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
import com.everhomes.server.schema.tables.daos.EhApprovalAttachmentsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalAttachments;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalAttachmentProviderImpl implements ApprovalAttachmentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalAttachment(ApprovalAttachment approvalAttachment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalAttachments.class));
		approvalAttachment.setId(id);
		getReadWriteDao().insert(approvalAttachment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalAttachments.class, null);
	}

	@Override
	public void updateApprovalAttachment(ApprovalAttachment approvalAttachment) {
		assert (approvalAttachment.getId() != null);
		getReadWriteDao().update(approvalAttachment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalAttachments.class, approvalAttachment.getId());
	}

	@Override
	public ApprovalAttachment findApprovalAttachmentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalAttachment.class);
	}
	
	@Override
	public List<ApprovalAttachment> listApprovalAttachment() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_ATTACHMENTS)
				.orderBy(Tables.EH_APPROVAL_ATTACHMENTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalAttachment.class));
	}
	
	private EhApprovalAttachmentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalAttachmentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalAttachmentsDao getDao(DSLContext context) {
		return new EhApprovalAttachmentsDao(context.configuration());
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
