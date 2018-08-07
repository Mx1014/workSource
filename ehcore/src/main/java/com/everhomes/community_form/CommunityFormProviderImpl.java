// @formatter:off
package com.everhomes.community_form;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCommunityGeneralFormDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityGeneralForm;
import com.everhomes.server.schema.tables.records.EhCommunityGeneralFormRecord;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommunityFormProviderImpl implements CommunityFormProvider{

    @Autowired
    private DbProvider dbProvider;
    @Override
    public void createCommunityGeneralForm(CommunityGeneralForm communityGeneralForm) {
        Long id = this.dbProvider.allocPojoRecordId(EhCommunityGeneralForm.class);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityGeneralForm.class));
        EhCommunityGeneralFormDao dao = new EhCommunityGeneralFormDao(context.configuration());
        communityGeneralForm.setId(id);
        dao.insert(communityGeneralForm);
    }

    @Override
    public void updateCommunityGeneralForm(CommunityGeneralForm communityGeneralForm) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityGeneralForm.class));
        EhCommunityGeneralFormDao dao = new EhCommunityGeneralFormDao(context.configuration());
        dao.update(communityGeneralForm);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityGeneralForm.class, communityGeneralForm.getId());
    }

    @Override
    public void deleteCommunityGeneralForm(CommunityGeneralForm communityGeneralForm) {
        DSLContext cxt = dbProvider
                .getDslContext(AccessSpec.readOnlyWith(EhCommunityGeneralForm.class, communityGeneralForm.getId()));
        EhCommunityGeneralFormDao dao = new EhCommunityGeneralFormDao(cxt.configuration());
        dao.delete(communityGeneralForm);
    }

    @Override
    public CommunityGeneralForm findCommunityGeneralForm(Long communityId, String type) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunityGeneralForm.class));
        return context.select()
                .from(Tables.EH_COMMUNITY_GENERAL_FORM)
                .where(Tables.EH_COMMUNITY_GENERAL_FORM.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_COMMUNITY_GENERAL_FORM.TYPE.eq(type))
                .fetchOneInto(CommunityGeneralForm.class);
    }

    @Override
    public List<CommunityGeneralForm> listCommunityGeneralFormByFormId(Long formId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityGeneralForm.class));
        SelectQuery<EhCommunityGeneralFormRecord> query = context.selectQuery(Tables.EH_COMMUNITY_GENERAL_FORM);
        query.addConditions(Tables.EH_COMMUNITY_GENERAL_FORM.FORM_ID.eq(formId));
        List<EhCommunityGeneralFormRecord> records = query.fetch();
        if (!CollectionUtils.isEmpty(records)) {
            List<CommunityGeneralForm> generalForms = records.stream().map(r->{
               return ConvertHelper.convert(r,CommunityGeneralForm.class) ;
            }).collect(Collectors.toList());
            return generalForms;
        }
        return null;
    }
}
