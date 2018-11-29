package com.everhomes.general_form;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGeneralFormFieldsConfigDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormFieldsConfig;
import com.everhomes.server.schema.tables.records.EhGeneralFormFieldsConfigRecord;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author huqi
 */
@Component
public class GeneralFormFieldsConfigProviderImpl implements GeneralFormFieldsConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFormFieldsConfig(GeneralFormFieldsConfig formFieldsConfig){
        long id = this.sequenceProvider.getNextSequence(NameMapper
                        .getSequenceDomainFromTablePojo(EhGeneralFormFieldsConfig.class));
        formFieldsConfig.setId(id);
        User user = UserContext.current().getUser();
        formFieldsConfig.setCreatorUid(user.getId());
        Long currentTime = DateHelper.currentGMTTime().getTime();
        formFieldsConfig.setCreateTime(new Timestamp(currentTime));

        rwDao().insert(formFieldsConfig);
        return id;
    }

    @Override
    public void updateFormFieldsConfig(GeneralFormFieldsConfig formFieldsConfig){
        User user = UserContext.current().getUser();
        formFieldsConfig.setUpdaterUid(user.getId());
        Long currentTime = DateHelper.currentGMTTime().getTime();
        formFieldsConfig.setUpdateTime(new Timestamp(currentTime));
        rwDao().update(formFieldsConfig);
    }

    @Override
    public GeneralFormFieldsConfig getFormFieldsConfig(Long formFieldsConfigId){

        com.everhomes.server.schema.tables.EhGeneralFormFieldsConfig t = Tables.EH_GENERAL_FORM_FIELDS_CONFIG;

        SelectQuery<EhGeneralFormFieldsConfigRecord> query = rContext().selectFrom(t).getQuery();
        query.addConditions(t.ID.eq(formFieldsConfigId));
        return query.fetchAnyInto(GeneralFormFieldsConfig.class);
    }

    private DSLContext rContext() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }

    private EhGeneralFormFieldsConfigDao rwDao() {
        return new EhGeneralFormFieldsConfigDao(rwContext().configuration());
    }

}
