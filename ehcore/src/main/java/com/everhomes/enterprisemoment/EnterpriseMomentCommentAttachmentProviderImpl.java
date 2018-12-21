package com.everhomes.enterprisemoment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentCommentAttachmentsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentCommentAttachments;
import com.everhomes.server.schema.tables.records.EhEnterpriseMomentCommentAttachmentsRecord;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EnterpriseMomentCommentAttachmentProviderImpl implements EnterpriseMomentCommentAttachmentProvider {
    @Autowired
    DbProvider dbProvider;
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void batchCreateAttachments(List<EnterpriseMomentCommentAttachment> attachmentList) {
        if (CollectionUtils.isEmpty(attachmentList)) {
            return;
        }
        Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentCommentAttachments.class), attachmentList.size());
        Timestamp now = new Timestamp(new Date().getTime());
        List<EhEnterpriseMomentCommentAttachments> attachments = new ArrayList<>(attachmentList.size());
        for (EnterpriseMomentCommentAttachment a : attachmentList) {
            EhEnterpriseMomentCommentAttachments persist = ConvertHelper.convert(a, EhEnterpriseMomentCommentAttachments.class);
            persist.setId(id++);
            persist.setCreateTime(now);
            attachments.add(persist);
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseMomentCommentAttachments.class));
        EhEnterpriseMomentCommentAttachmentsDao dao = new EhEnterpriseMomentCommentAttachmentsDao(context.configuration());
        dao.insert(attachments);
    }

    @Override
    public void deleteAttachmentsByCommentId(Integer namespaceId, Long commentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseMomentCommentAttachments.class));
        DeleteQuery<EhEnterpriseMomentCommentAttachmentsRecord> deleteQuery = context.deleteQuery(Tables.EH_ENTERPRISE_MOMENT_COMMENT_ATTACHMENTS);
        deleteQuery.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENT_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId));
        deleteQuery.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENT_ATTACHMENTS.COMMENT_ID.eq(commentId));
        deleteQuery.execute();
    }

    @Override
    public List<EnterpriseMomentCommentAttachment> queryAttachmentsByCommentIds(Integer namespaceId, List<Long> commentIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseMomentCommentAttachmentsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_MOMENT_COMMENT_ATTACHMENTS);
        query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENT_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENT_ATTACHMENTS.COMMENT_ID.in(commentIds));
        query.addOrderBy(Tables.EH_ENTERPRISE_MOMENT_COMMENT_ATTACHMENTS.ID.asc());

        Result<EhEnterpriseMomentCommentAttachmentsRecord> record = query.fetch();
        if (record == null || record.size() == 0) {
            return new ArrayList<>();
        }
        return record.map(r -> ConvertHelper.convert(r, EnterpriseMomentCommentAttachment.class));
    }

}
