package com.everhomes.contract;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCustomersDao;
import com.everhomes.server.schema.tables.pojos.EhContractAttachments;
import com.everhomes.server.schema.tables.records.EhContractAttachmentsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/16.
 */
@Component
public class ContractAttachmentProviderImpl implements ContractAttachmentProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractAttachmentProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createContractAttachment(ContractAttachment attachment) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractAttachments.class));
        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        attachment.setCreatorUid(UserContext.current().getUser().getId());

        LOGGER.info("createContractAttachment: " + attachment);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractAttachments.class, id));
        EhContractAttachmentsDao dao = new EhContractAttachmentsDao(context.configuration());
        dao.insert(attachment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractAttachments.class, null);
    }

    @Override
    public void deleteContractAttachment(ContractAttachment attachment) {
        assert(attachment.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractAttachments.class, attachment.getId()));
        EhContractAttachmentsDao dao = new EhContractAttachmentsDao(context.configuration());
        dao.delete(attachment);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractAttachments.class, attachment.getId());
    }

    @Override
    public List<ContractAttachment> listByContractId(Long contractId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhContractAttachmentsRecord> query = context.selectQuery(Tables.EH_CONTRACT_ATTACHMENTS);
        query.addConditions(Tables.EH_CONTRACT_ATTACHMENTS.CONTRACT_ID.eq(contractId));

        List<ContractAttachment> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ContractAttachment.class));
            return null;
        });

        return result;
    }

    @Override
    public ContractAttachment findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractAttachmentsDao dao = new EhContractAttachmentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ContractAttachment.class);
    }
}
