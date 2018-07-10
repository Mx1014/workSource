package com.everhomes.archives;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhArchivesFormNavigation;
import com.everhomes.server.schema.tables.daos.EhArchivesFormNavigationDao;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ArchivesFormProviderImpl implements ArchivesFormProvider{

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createArchivesForm(ArchivesFromNavigation navigation) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesFormNavigation.class));
        navigation.setId(id);
        navigation.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormNavigationDao dao = new EhArchivesFormNavigationDao(context.configuration());
        dao.insert(navigation);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesFormNavigation.class, null);
    }

    @Override
    public void updateArchivesForm(ArchivesFroms form){
        form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormsDao dao = new EhArchivesFormsDao(context.configuration());
        dao.update(form);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesForms.class, form.getId());

    }

    @Override
    public ArchivesFroms findArchivesFormOriginId(Integer namespaceId, Long organizationId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesFormsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_FORMS);
        query.addConditions(Tables.EH_ARCHIVES_FORMS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_FORMS.ORGANIZATION_ID.eq(organizationId));
        return query.fetchOneInto(ArchivesFroms.class);
    }
}
