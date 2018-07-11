package com.everhomes.archives;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhArchivesFormGroupsDao;
import com.everhomes.server.schema.tables.daos.EhArchivesFormValsDao;
import com.everhomes.server.schema.tables.daos.EhArchivesFormsDao;
import com.everhomes.server.schema.tables.pojos.EhArchivesFormGroups;
import com.everhomes.server.schema.tables.pojos.EhArchivesFormVals;
import com.everhomes.server.schema.tables.pojos.EhArchivesForms;
import com.everhomes.server.schema.tables.records.EhArchivesFormGroupsRecord;
import com.everhomes.server.schema.tables.records.EhArchivesFormValsRecord;
import com.everhomes.server.schema.tables.records.EhArchivesFormsRecord;
import com.everhomes.user.UserContext;
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
    public Long createArchivesForm(ArchivesForm form) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesForms.class));
        form.setId(id);
        form.setOperatorUid(UserContext.currentUserId());
        form.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormsDao dao = new EhArchivesFormsDao(context.configuration());
        dao.insert(form);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesForms.class, null);
        return form.getId();
    }

    @Override
    public void updateArchivesForm(ArchivesForm form) {
        form.setOperatorUid(UserContext.currentUserId());
        form.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormsDao dao = new EhArchivesFormsDao(context.configuration());
        dao.update(form);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesForms.class, form.getId());
    }

    @Override
    public ArchivesForm findArchivesFormByOrgId(Integer namespaceId, Long orgId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesFormsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_FORMS);
        query.addConditions(Tables.EH_ARCHIVES_FORMS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_FORMS.OWNER_ID.eq(orgId));
        return query.fetchAnyInto(ArchivesForm.class);
    }

    @Override
    public ArchivesForm findArchivesDefaultForm(String ownerType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesFormsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_FORMS);
        query.addConditions(Tables.EH_ARCHIVES_FORMS.NAMESPACE_ID.eq(0));
        query.addConditions(Tables.EH_ARCHIVES_FORMS.OWNER_ID.eq(0L));
        query.addConditions(Tables.EH_ARCHIVES_FORMS.OWNER_TYPE.eq(ownerType));
        return query.fetchAnyInto(ArchivesForm.class);
    }

    @Override
    public ArchivesFormGroup createArchivesFormGroup(ArchivesFormGroup group){
        Long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhArchivesFormGroups.class));
        group.setId(id);
        group.setOperatorUid(UserContext.currentUserId());
        group.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormGroupsDao dao = new EhArchivesFormGroupsDao(context.configuration());
        dao.insert(group);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesFormGroups.class, null);
        return group;
    }

/*    @Override
    public void deleteGeneralFormGroupsByFormOriginId(Long formOriginId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_GENERAL_FORM_GROUPS)
                .where(Tables.EH_GENERAL_FORM_GROUPS.FORM_ORIGIN_ID.eq(formOriginId))
                .execute();
    }*/

    @Override
    public ArchivesFormGroup findArchivesFormGroupByFormId(Integer namespaceId, Long formId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesFormGroupsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_FORM_GROUPS);
        query.addConditions(Tables.EH_ARCHIVES_FORM_GROUPS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_FORM_GROUPS.FORM_ID.eq(formId));
        return query.fetchOneInto(ArchivesFormGroup.class);
    }

    @Override
    public void updateArchivesFormGroup(ArchivesFormGroup group){
        group.setOperatorUid(UserContext.currentUserId());
        group.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormGroupsDao dao = new EhArchivesFormGroupsDao(context.configuration());
        dao.update(group);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesFormGroups.class, group.getId());
    }

    @Override
    public void createArchivesFormVal(ArchivesFormVal val) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesFormVals.class));
        val.setId(id);
        val.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormValsDao dao = new EhArchivesFormValsDao(context.configuration());
        dao.insert(val);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesFormVals.class, null);
    }

    @Override
    public void updateArchivesFormVal(ArchivesFormVal val) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormValsDao dao = new EhArchivesFormValsDao(context.configuration());
        dao.update(val);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesFormVals.class, val.getId());
    }

    @Override
    public ArchivesFormVal findArchivesFormValBySourceIdAndName(Long formId, Long sourceId, String fieldName){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhArchivesFormVals.class));
        SelectQuery<EhArchivesFormValsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_FORM_VALS);
        query.addConditions(Tables.EH_ARCHIVES_FORM_VALS.FORM_ID.eq(formId));
        query.addConditions(Tables.EH_ARCHIVES_FORM_VALS.SOURCE_ID.eq(sourceId));
        query.addConditions(Tables.EH_ARCHIVES_FORM_VALS.FIELD_NAME.eq(fieldName));
        return query.fetchAnyInto(ArchivesFormVal.class);
    }
}
