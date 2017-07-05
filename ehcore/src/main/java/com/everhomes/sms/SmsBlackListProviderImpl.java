package com.everhomes.sms;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSmsBlackListsDao;
import com.everhomes.server.schema.tables.pojos.EhSmsBlackLists;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by xq.tian on 2017/7/4.
 */
@Repository
public class SmsBlackListProviderImpl implements SmsBlackListProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSmsBlackList(SmsBlackList blackList) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSmsBlackLists.class));
        blackList.setId(id);
        blackList.setCreateTime(DateUtils.currentTimestamp());
        blackList.setCreatorUid(UserContext.currentUserId());
        rwDao().insert(blackList);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSmsBlackLists.class, id);
    }

    @Override
    public SmsBlackList findByContactToken(String contactToken) {
        return context().selectFrom(Tables.EH_SMS_BLACK_LISTS)
                .where(Tables.EH_SMS_BLACK_LISTS.CONTACT_TOKEN.eq(contactToken))
                .fetchAnyInto(SmsBlackList.class);
    }

    private EhSmsBlackListsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhSmsBlackListsDao(context.configuration());
    }

    private EhSmsBlackListsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhSmsBlackListsDao(context.configuration());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
