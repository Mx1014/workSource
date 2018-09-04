package com.everhomes.investment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.investment.InvestmentEnterpriseStatus;
import com.everhomes.rest.investment.InvestmentStatisticsDTO;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.daos.EhEnterpriseInvestmentContactDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseInvestmentDemandDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseInvestmentNowInfoDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseInvestmentContact;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseInvestmentDemand;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseInvestmentNowInfo;
import com.everhomes.server.schema.tables.records.EhEnterpriseInvestmentContactRecord;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InvestmentEnterpriseProvideImpl implements InvestmentEnterpriseProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentEnterpriseProvideImpl.class);

    @Autowired
    SequenceProvider sequenceProvider;
    @Autowired
    DbProvider dbProvider;

    @Override
    public Long createContact(EnterpriseInvestmentContact contact) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhEnterpriseInvestmentContact.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentContact.class));
        contact.setId(id);

        User user = UserContext.current().getUser();
        contact.setOperatorBy(user.getNickName());
        contact.setCreateBy(user.getNickName());

        Long l2 = DateHelper.currentGMTTime().getTime();
        contact.setCreateTime(new Timestamp(l2));
        contact.setOperatorTime(new Timestamp(l2));

        EhEnterpriseInvestmentContactDao dao = new EhEnterpriseInvestmentContactDao(context.configuration());
        dao.insert(contact);
        return id;
    }

    @Override
    public Long updateContact(EnterpriseInvestmentContact contact) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentContact.class));

        User user = UserContext.current().getUser();
        contact.setOperatorBy(user.getNickName());

        Long l2 = DateHelper.currentGMTTime().getTime();
        contact.setOperatorTime(new Timestamp(l2));

        EhEnterpriseInvestmentContactDao dao = new EhEnterpriseInvestmentContactDao(context.configuration());
        dao.update(contact);
        return contact.getId();
    }

    @Override
    public EnterpriseInvestmentContact findContactById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentContact.class));

        EhEnterpriseInvestmentContactDao dao = new EhEnterpriseInvestmentContactDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EnterpriseInvestmentContact.class);
    }

    @Override
    public List<EnterpriseInvestmentContact> findContactByCustomerId(Long customerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhEnterpriseInvestmentContact.class));

        SelectQuery<EhEnterpriseInvestmentContactRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_INVESTMENT_CONTACT);
        query.addConditions(Tables.EH_ENTERPRISE_INVESTMENT_CONTACT.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_ENTERPRISE_INVESTMENT_CONTACT.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, EnterpriseInvestmentContact.class));
    }

    @Override
    public List<EnterpriseInvestmentContact> findContactByCustomerIdAndType(Long customerId, Byte type) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhEnterpriseInvestmentContact.class));

        SelectQuery<EhEnterpriseInvestmentContactRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_INVESTMENT_CONTACT);
        query.addConditions(Tables.EH_ENTERPRISE_INVESTMENT_CONTACT.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_ENTERPRISE_INVESTMENT_CONTACT.TYPE.eq(type));
        query.addConditions(Tables.EH_ENTERPRISE_INVESTMENT_CONTACT.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, EnterpriseInvestmentContact.class));
    }

    @Override
    public Long createDemand(EnterpriseInvestmentDemand demand) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhEnterpriseInvestmentDemand.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentDemand.class));
        demand.setId(id);

        User user = UserContext.current().getUser();
        demand.setOperatorBy(user.getNickName());
        demand.setCreateBy(user.getNickName());

        Long l2 = DateHelper.currentGMTTime().getTime();
        demand.setCreateTime(new Timestamp(l2));
        demand.setOperatorTime(new Timestamp(l2));

        EhEnterpriseInvestmentDemandDao dao = new EhEnterpriseInvestmentDemandDao(context.configuration());
        dao.insert(demand);
        return id;
    }

    @Override
    public Long updateDemand(EnterpriseInvestmentDemand demand) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentDemand.class));
        User user = UserContext.current().getUser();
        demand.setOperatorBy(user.getNickName());

        Long l2 = DateHelper.currentGMTTime().getTime();
        demand.setOperatorTime(new Timestamp(l2));

        EhEnterpriseInvestmentDemandDao dao = new EhEnterpriseInvestmentDemandDao(context.configuration());
        dao.update(demand);
        return demand.getId();
    }

    @Override
    public EnterpriseInvestmentDemand findDemandById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentDemand.class));

        EhEnterpriseInvestmentDemandDao dao = new EhEnterpriseInvestmentDemandDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), EnterpriseInvestmentDemand.class);
    }

    @Override
    public EnterpriseInvestmentDemand findNewestDemandByCustoemrId(Long customerId) {
        try {
            EnterpriseInvestmentDemand[] result = new EnterpriseInvestmentDemand[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseInvestmentDemand.class));
            result[0] = context.select().from(Tables.EH_ENTERPRISE_INVESTMENT_DEMAND)
                    .where(Tables.EH_ENTERPRISE_INVESTMENT_DEMAND.CUSTOMER_ID.eq(customerId))
                    .and(Tables.EH_ENTERPRISE_INVESTMENT_DEMAND.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()))
                    .orderBy(Tables.EH_ENTERPRISE_INVESTMENT_DEMAND.DEMAND_VERSION.desc()).fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseInvestmentDemand.class);
                    });
            return result[0];
        } catch (Exception ex) {
            // fetchAny() maybe return null
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Long createNowInfo(EnterpriseInvestmentNowInfo nowInfo) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhEnterpriseInvestmentNowInfo.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentNowInfo.class));
        nowInfo.setId(id);

        User user = UserContext.current().getUser();
        nowInfo.setOperatorBy(user.getNickName());
        nowInfo.setCreateBy(user.getNickName());

        Long l2 = DateHelper.currentGMTTime().getTime();
        nowInfo.setCreateTime(new Timestamp(l2));
        nowInfo.setOperatorTime(new Timestamp(l2));

        EhEnterpriseInvestmentNowInfoDao dao = new EhEnterpriseInvestmentNowInfoDao(context.configuration());
        dao.insert(nowInfo);
        return id;
    }

    @Override
    public Long updateNowInfo(EnterpriseInvestmentNowInfo nowInfo) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentNowInfo.class));

        User user = UserContext.current().getUser();
        nowInfo.setOperatorBy(user.getNickName());

        Long l2 = DateHelper.currentGMTTime().getTime();
        nowInfo.setOperatorTime(new Timestamp(l2));

        EhEnterpriseInvestmentNowInfoDao dao = new EhEnterpriseInvestmentNowInfoDao(context.configuration());
        dao.update(nowInfo);
        return nowInfo.getId();
    }

    @Override
    public EnterpriseInvestmentNowInfo findNowInfoById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhEnterpriseInvestmentNowInfo.class));


        EhEnterpriseInvestmentNowInfoDao dao = new EhEnterpriseInvestmentNowInfoDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EnterpriseInvestmentNowInfo.class);
    }

    @Override
    public EnterpriseInvestmentNowInfo findNewestNowInfoByCustoemrId(Long customerId) {
        try {
            EnterpriseInvestmentNowInfo[] result = new EnterpriseInvestmentNowInfo[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseInvestmentNowInfo.class));
            result[0] = context.select().from(Tables.EH_ENTERPRISE_INVESTMENT_NOW_INFO)
                    .where(Tables.EH_ENTERPRISE_INVESTMENT_NOW_INFO.CUSTOMER_ID.eq(customerId))
                    .and(Tables.EH_ENTERPRISE_INVESTMENT_NOW_INFO.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()))
                    .orderBy(Tables.EH_ENTERPRISE_INVESTMENT_NOW_INFO.ID.desc()).fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, EnterpriseInvestmentNowInfo.class);
                    });
            return result[0];
        } catch (Exception ex) {
            // fetchAny() maybe return null
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<InvestmentStatisticsDTO> getInvestmentStatistics(Integer namespaceId, Long communityId, Set<Long> itemIds,Map<Long, FieldItemDTO> itemsMap) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        Field<?>[] fieldArray = new Field[itemIds.size()];
        List<Field<?>> fields = new ArrayList<>();
        itemIds.forEach((itemId) -> {
            // dynamic build stastics decode
            Field<Long> staTmp = DSL.decode().when(customer.LEVEL_ITEM_ID.eq(itemId), itemId);
            fields.add(DSL.count(staTmp).as(itemId.toString()));
        });
        fields.toArray(fieldArray);
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(fieldArray);
        query.addConditions(customer.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(customer.COMMUNITY_ID.eq(communityId));
        query.addConditions(customer.NAMESPACE_ID.eq(namespaceId));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("count investment enterprise, sql=" + query.getSQL());
            LOGGER.debug("count  investment  enterprise , bindValues=" + query.getBindValues());
        }
        List<InvestmentStatisticsDTO> result = new ArrayList<>();
        query.fetch().map((r) -> {
            for (Long itemId : itemIds) {
                InvestmentStatisticsDTO dto = new InvestmentStatisticsDTO();
//                dto.setKey(itemId.toString());
                dto.setKey(itemsMap.get(itemId).getItemDisplayName());
                dto.setValue(r.getValue(itemId.toString(), Long.class).toString());
                result.add(dto);
            }
            return null;
        });
        return result;
    }

    @Override
    public void deleteInvestment(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        context.update(customer)
                .set(customer.STATUS, CommonStatus.INACTIVE.getCode())
                .where(customer.ID.eq(id))
                .execute();
    }

    @Override
    public void deleteInvestmentContacts(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhEnterpriseInvestmentContact contact = Tables.EH_ENTERPRISE_INVESTMENT_CONTACT;
        context.update(contact)
                .set(contact.STATUS, CommonStatus.INACTIVE.getCode())
                .where(contact.CUSTOMER_ID.eq(id))
                .execute();
    }
}
