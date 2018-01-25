package com.everhomes.contract;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractPaymentPlansDao;
import com.everhomes.server.schema.tables.pojos.EhContractPaymentPlans;
import com.everhomes.server.schema.tables.records.EhContractPaymentPlansRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2018/1/2.
 */
@Component
public class ContractPaymentPlanProviderImpl implements ContractPaymentPlanProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractAttachmentProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createContractPaymentPlan(ContractPaymentPlan plan) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractPaymentPlans.class));
        plan.setId(id);
        plan.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        plan.setStatus(CommonStatus.ACTIVE.getCode());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractPaymentPlans.class, id));
        EhContractPaymentPlansDao dao = new EhContractPaymentPlansDao(context.configuration());
        dao.insert(plan);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractPaymentPlans.class, id);

    }

    @Override
    public void deleteContractPaymentPlan(ContractPaymentPlan plan) {
        assert(plan.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractPaymentPlans.class, plan.getId()));
        EhContractPaymentPlansDao dao = new EhContractPaymentPlansDao(context.configuration());
        dao.delete(plan);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractPaymentPlans.class, plan.getId());

    }

    @Override
    public List<ContractPaymentPlan> listByContractId(Long contractId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhContractPaymentPlansRecord> query = context.selectQuery(Tables.EH_CONTRACT_PAYMENT_PLANS);
        query.addConditions(Tables.EH_CONTRACT_PAYMENT_PLANS.CONTRACT_ID.eq(contractId));
        query.addConditions(Tables.EH_CONTRACT_PAYMENT_PLANS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("listByContractId, sql=" + query.getSQL());
            LOGGER.debug("listByContractId, bindValues=" + query.getBindValues());
        }
        List<ContractPaymentPlan> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ContractPaymentPlan.class));
            return null;
        });

        return result;
    }

    @Override
    public ContractPaymentPlan findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractPaymentPlansDao dao = new EhContractPaymentPlansDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ContractPaymentPlan.class);
    }
}
