package com.everhomes.payment_application;

import com.everhomes.asset.zjgkVOs.PaymentStatus;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.payment_application.PaymentApplicationStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPaymentApplicationsDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentApplications;
import com.everhomes.server.schema.tables.records.EhPaymentApplicationsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/12/27.
 */
@Component
public class PaymentApplicationProviderImpl implements PaymentApplicationProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createPaymentApplication(PaymentApplication application) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentApplications.class));
        application.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentApplicationsDao dao = new EhPaymentApplicationsDao(context.configuration());
        dao.insert(application);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentApplications.class, id);
    }

    @Override
    public PaymentApplication findPaymentApplication(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentApplicationsDao dao = new EhPaymentApplicationsDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), PaymentApplication.class);
    }

    @Override
    public List<PaymentApplication> listPaymentApplications(CrossShardListingLocator locator, Integer pageSize) {
        List<PaymentApplication> applications = new ArrayList<>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhPaymentApplications.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhPaymentApplicationsRecord> query = context.selectQuery(Tables.EH_PAYMENT_APPLICATIONS);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_PAYMENT_APPLICATIONS.ID.lt(locator.getAnchor()));
            }
            query.addConditions(Tables.EH_PAYMENT_APPLICATIONS.STATUS.ne(PaymentApplicationStatus.INACTIVE.getCode()));
            query.addOrderBy(Tables.EH_PAYMENT_APPLICATIONS.ID.desc());
            query.addLimit(pageSize - applications.size());

            query.fetch().map((r) -> {
                applications.add(ConvertHelper.convert(r, PaymentApplication.class));
                return null;
            });

            if (applications.size() >= pageSize) {
                locator.setAnchor(applications.get(applications.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return applications;
    }
}
