package com.everhomes.profile;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.profile.ProfileContactsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhProfileContactsStickyDao;
import com.everhomes.server.schema.tables.pojos.EhProfileContactsSticky;
import com.everhomes.server.schema.tables.records.EhProfileContactsStickyRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileProviderImpl implements ProfileProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createProfileContactsSticky(ProfileContactsSticky profileContactsSticky) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhProfileContactsSticky.class));
        profileContactsSticky.setId(id);
        profileContactsSticky.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        profileContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        profileContactsSticky.setUpdateTime(profileContactsSticky.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProfileContactsStickyDao dao = new EhProfileContactsStickyDao(context.configuration());
        dao.insert(profileContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProfileContactsSticky.class, null);
    }

    @Override
    public void deleteProfileContactsSticky(ProfileContactsSticky profileContactsSticky){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProfileContactsStickyDao dao = new EhProfileContactsStickyDao(context.configuration());
        dao.deleteById(profileContactsSticky.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhProfileContactsSticky.class, profileContactsSticky.getId());
    }

    @Override
    public ProfileContactsSticky findProfileContactsStickyById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhProfileContactsStickyDao dao = new EhProfileContactsStickyDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ProfileContactsSticky.class);
    }

    @Override
    public List<Long> listProfileContactsStickyIds(Integer namespaceId, Long organizationId) {
        List<Long> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_PROFILE_CONTACTS_STICKY.DETAIL_ID)
                .from(Tables.EH_PROFILE_CONTACTS_STICKY)
                .where(Tables.EH_PROFILE_CONTACTS_STICKY.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PROFILE_CONTACTS_STICKY.ORGANIZATION_ID.eq(organizationId))
                .fetchInto(Long.class);
    }

    @Override
    public ProfileContactsSticky findProfileContactsStickyByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhProfileContactsStickyRecord> query = context.selectQuery(Tables.EH_PROFILE_CONTACTS_STICKY);
        query.addConditions(Tables.EH_PROFILE_CONTACTS_STICKY.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_PROFILE_CONTACTS_STICKY.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_PROFILE_CONTACTS_STICKY.DETAIL_ID.eq(detailId));
        if(query.fetch() != null){
            return ConvertHelper.convert(query.fetchOne(),ProfileContactsSticky.class);
        }else
            return null;
    }
}
