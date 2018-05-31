// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPhoneWhiteListDao;
import com.everhomes.server.schema.tables.pojos.EhPhoneWhiteList;
import com.everhomes.server.schema.tables.records.EhPhoneWhiteListRecord;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WhiteListProviderImpl implements WhiteListProvider{

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createWhiteList(PhoneWhiteList phoneWhiteList) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhPhoneWhiteList.class));
        phoneWhiteList.setId(id);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPhoneWhiteListDao dao = new EhPhoneWhiteListDao(context.configuration());
        dao.insert(phoneWhiteList);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPhoneWhiteList.class, null);
    }

    @Override
    public void batchCreateWhiteList(List<PhoneWhiteList> phoneWhiteLists) {
        Long id = this.sequenceProvider.getNextSequenceBlock(NameMapper
                .getSequenceDomainFromTablePojo(EhPhoneWhiteList.class), (long)phoneWhiteLists.size());
        List<EhPhoneWhiteList> list = new ArrayList<>();
        for (PhoneWhiteList phoneWhiteList : phoneWhiteLists) {
            phoneWhiteList.setId(id);
            list.add(ConvertHelper.convert(phoneWhiteList, EhPhoneWhiteList.class));
            id++;
        }

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPhoneWhiteListDao dao = new EhPhoneWhiteListDao(context.configuration());
        dao.insert(list);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPhoneWhiteList.class, null);
    }

    @Override
    public void deleteWhiteList(PhoneWhiteList phoneWhiteList) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPhoneWhiteListDao dao = new EhPhoneWhiteListDao(context.configuration());
        dao.delete(phoneWhiteList);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPhoneWhiteList.class, null);

    }

    @Override
    public void updateWhiteList(PhoneWhiteList phoneWhiteList) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPhoneWhiteListDao dao = new EhPhoneWhiteListDao(context.configuration());
        dao.update(phoneWhiteList);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPhoneWhiteList.class, null);
    }

    @Override
    public PhoneWhiteList getWhiteList(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPhoneWhiteList.class));
        EhPhoneWhiteListDao dao = new EhPhoneWhiteListDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), PhoneWhiteList.class);
    }

    @Override
    public List<PhoneWhiteList> listWhiteList(String phoneNumber, Integer namespaceId, Long pageAnchor, Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPhoneWhiteList.class));
        SelectQuery<EhPhoneWhiteListRecord> query = context.selectQuery(Tables.EH_PHONE_WHITE_LIST);

        if (!StringUtils.isEmpty(phoneNumber)) {
            query.addConditions(Tables.EH_PHONE_WHITE_LIST.PHONE_NUMBER.like("%" + phoneNumber + "%"));
        }

        if (null != namespaceId) {
            query.addConditions(Tables.EH_PHONE_WHITE_LIST.NAMESPACE_ID.eq(namespaceId));
        }
        if (null != pageAnchor && 0L != pageAnchor) {
            query.addConditions(Tables.EH_PHONE_WHITE_LIST.ID.lt(pageAnchor));
        }
        if (null != pageSize) {
            query.addLimit(pageSize+1);
        }
        query.addOrderBy(Tables.EH_PHONE_WHITE_LIST.ID.desc());
        return query.fetch().map(r -> ConvertHelper.convert(r, PhoneWhiteList.class));
    }

    @Override
    public List<String> listAllWhiteList(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPhoneWhiteList.class));
        SelectQuery<EhPhoneWhiteListRecord> query = context.selectQuery(Tables.EH_PHONE_WHITE_LIST);
        query.addConditions(Tables.EH_PHONE_WHITE_LIST.NAMESPACE_ID.eq(namespaceId));
        return query.fetch(Tables.EH_PHONE_WHITE_LIST.PHONE_NUMBER);
    }

    @Override
    public PhoneWhiteList checkPhoneIsExists(Integer namespaceId, String phoneNumber) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPhoneWhiteList.class));
        SelectQuery<EhPhoneWhiteListRecord> query = context.selectQuery(Tables.EH_PHONE_WHITE_LIST);

        if (!StringUtils.isEmpty(phoneNumber)) {
            query.addConditions(Tables.EH_PHONE_WHITE_LIST.PHONE_NUMBER.eq(phoneNumber));
        }

        if (null != namespaceId) {
            query.addConditions(Tables.EH_PHONE_WHITE_LIST.NAMESPACE_ID.eq(namespaceId));
        }

        EhPhoneWhiteListRecord record = query.fetchOne();
        if (null != record) {
            return ConvertHelper.convert(record, PhoneWhiteList.class);
        }
        return null;
    }
}
