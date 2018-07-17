package com.everhomes.invitation_card;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.invitation_Card.InvitationCardProvider;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovals;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

public class InvitationCardProviderImpl implements InvitationCardProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public GeneralApproval getRunnintInvitationApproval(Long moduleId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralApprovals.class));

        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);

        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(GeneralApprovalStatus.RUNNING.getCode()));
        return (GeneralApproval)query.fetch().map(r -> ConvertHelper.convert(r, GeneralApproval.class));
    }


}
