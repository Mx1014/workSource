package com.everhomes.socialSecurity;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.socialSecurity.InOutLogType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSocialSecurityInoutLogDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityInoutLog;
import com.everhomes.server.schema.tables.records.EhSocialSecurityInoutLogRecord;
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
public class SocialSecurityInoutLogProviderImpl implements SocialSecurityInoutLogProvider{

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createSocialSecurityInoutLog(SocialSecurityInoutLog inoutLog){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityInoutLog.class));
        inoutLog.setId(id);
        inoutLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        inoutLog.setCreatorUid(UserContext.currentUserId());
        inoutLog.setUpdateTime(inoutLog.getCreateTime());
        inoutLog.setOperatorUid(inoutLog.getCreatorUid());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhSocialSecurityInoutLogDao dao = new EhSocialSecurityInoutLogDao(context.configuration());
        dao.insert(inoutLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityInoutLog.class, null);
    }

    @Override
    public List<SocialSecurityInoutLog> listSocialSecurityInoutLogs(Long organizationId, Long detailId){
        List<SocialSecurityInoutLog> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhSocialSecurityInoutLogRecord> query = context.selectQuery(Tables.EH_SOCIAL_SECURITY_INOUT_LOG);
        query.addConditions(Tables.EH_SOCIAL_SECURITY_INOUT_LOG.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_SOCIAL_SECURITY_INOUT_LOG.DETAIL_ID.eq(detailId));

        query.addOrderBy(Tables.EH_SOCIAL_SECURITY_INOUT_LOG.LOG_DATE.asc());
        query.fetch().map(r ->{
            results.add(ConvertHelper.convert(r, SocialSecurityInoutLog.class));
            return null;
        });

        return results;
    }

    @Override
    public List<Long> listSocialSecurityInoutLogDetailIds(Long ownerId, String month, InOutLogType accumulationFundIn) {
        List<Long> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhSocialSecurityInoutLogRecord> query = context.selectQuery(Tables.EH_SOCIAL_SECURITY_INOUT_LOG);
        query.addConditions(Tables.EH_SOCIAL_SECURITY_INOUT_LOG.ORGANIZATION_ID.eq(ownerId));
        query.addConditions(Tables.EH_SOCIAL_SECURITY_INOUT_LOG.LOG_MONTH.eq(month));
        query.addConditions(Tables.EH_SOCIAL_SECURITY_INOUT_LOG.TYPE.eq(accumulationFundIn.getCode()));

        query.addOrderBy(Tables.EH_SOCIAL_SECURITY_INOUT_LOG.LOG_DATE.asc());
        query.fetch().map(r ->{
            results.add(ConvertHelper.convert(r, SocialSecurityInoutLog.class).getDetailId());
            return null;
        });
        if (results.size() == 0) {
            return null;
        }
        return results;
    }
}
