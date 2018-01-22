package com.everhomes.message;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.schema.tables.daos.EhMessagesDao;
import com.everhomes.schema.tables.pojos.EhMessages;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class MessageProviderImpl implements MessageProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createMessage(Message message) {
        message.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMessages.class));
        message.setId(id);
        EhMessagesDao dao = new EhMessagesDao(context.configuration());
        dao.insert(message);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhMessages.class, message.getId());
    }

    @Override
    public void updateMessage(Message message) {
        assert (message.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhMessagesDao dao = new EhMessagesDao(context.configuration());
        dao.update(message);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMessages.class, message.getId());
    }

    @Override
    public void deleteMessageById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhMessagesDao dao = new EhMessagesDao(context.configuration());
        dao.deleteById(id);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMessages.class, id);
    }

    @Override
    public List<Organization> listMessage(Integer namespaceId) {
        return null;
    }
}
