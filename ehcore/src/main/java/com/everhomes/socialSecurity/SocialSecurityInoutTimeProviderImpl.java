package com.everhomes.socialSecurity;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSocialSecurityInoutTimeDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityInoutTime;
import com.everhomes.server.schema.tables.records.EhSocialSecurityInoutTimeRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class SocialSecurityInoutTimeProviderImpl implements SocialSecurityInoutTimeProvider{

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSocialSecurityInoutTime(SocialSecurityInoutTime inOutTime) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityInoutTime.class));
        inOutTime.setId(id);
        inOutTime.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        inOutTime.setCreatorUid(UserContext.currentUserId());
        inOutTime.setUpdateTime(inOutTime.getCreateTime());
        inOutTime.setOperatorUid(inOutTime.getCreatorUid());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhSocialSecurityInoutTimeDao dao = new EhSocialSecurityInoutTimeDao(context.configuration());
        dao.insert(inOutTime);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityInoutTime.class, null);
    }

    @Override
    public SocialSecurityInoutTime getSocialSecurityInoutTimeById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhSocialSecurityInoutTimeDao dao = new EhSocialSecurityInoutTimeDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), SocialSecurityInoutTime.class);
    }

    @Override
    public void updateSocialSecurityInoutTime(SocialSecurityInoutTime inOutTime) {
        inOutTime.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        inOutTime.setOperatorUid(UserContext.currentUserId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhSocialSecurityInoutTimeDao dao = new EhSocialSecurityInoutTimeDao(context.configuration());
        dao.update(inOutTime);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityInoutTime.class, inOutTime.getId());
    }

    @Override
    public SocialSecurityInoutTime getSocialSecurityInoutTimeByDetailId(Byte inOutType, Long detailId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhSocialSecurityInoutTimeRecord> query = context.selectQuery(Tables.EH_SOCIAL_SECURITY_INOUT_TIME);
        query.addConditions(Tables.EH_SOCIAL_SECURITY_INOUT_TIME.DETAIL_ID.eq(detailId));
        query.addConditions(Tables.EH_SOCIAL_SECURITY_INOUT_TIME.TYPE.eq(inOutType));
        return query.fetchOneInto(SocialSecurityInoutTime.class);
    }
}
