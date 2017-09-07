// @formatter:off
package com.everhomes.openapi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.contract.ContractParam;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.daos.EhContractParamsDao;
import com.everhomes.server.schema.tables.pojos.EhContractParams;
import com.everhomes.server.schema.tables.records.EhContractParamsRecord;
import com.everhomes.server.schema.tables.records.EhContractsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.util.IterationMapReduceCallback;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractsDao;
import com.everhomes.server.schema.tables.pojos.EhContracts;
import com.everhomes.util.ConvertHelper;

@Component
public class ContractProviderImpl implements ContractProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createContract(Contract contract) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContracts.class));
		contract.setId(id);
		getReadWriteDao().insert(contract);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhContracts.class, null);
	}

	@Override
	public void updateContract(Contract contract) {
		assert (contract.getId() != null);
		getReadWriteDao().update(contract);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContracts.class, contract.getId());
	}
	
	@Override
	public void deleteContract(Contract contract) {
		assert (contract.getId() != null);
		getReadWriteDao().delete(contract);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContracts.class, contract.getId());
	}

	@Override
	public Contract findContractById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Contract.class);
	}
	
	@Override
	public List<Contract> listContract() {
		return getReadOnlyContext().select().from(Tables.EH_CONTRACTS)
				.orderBy(Tables.EH_CONTRACTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Contract.class));
	}
	
	@Override
	public Contract findContractByNumber(Integer namespaceId, Long organizationId, String contractNumber) {
		Record record = getReadOnlyContext().select().from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(organizationId))
				.and(Tables.EH_CONTRACTS.CONTRACT_NUMBER.eq(contractNumber))
				.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, Contract.class);
		}
		return null;
	}

	@Override
	public void deleteContractByOrganizationName(Integer namespaceId, String organizationName) {
		getReadWriteContext().update(Tables.EH_CONTRACTS)
			.set(Tables.EH_CONTRACTS.STATUS, CommonStatus.INACTIVE.getCode())
			.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_CONTRACTS.CUSTOMER_NAME.eq(organizationName))
			.and(Tables.EH_CONTRACTS.STATUS.ne(CommonStatus.INACTIVE.getCode()))
			.execute();
	}

	@Override
	public List<Contract> listContractByContractNumbers(Integer namespaceId, List<String> contractNumbers) {
		Result<Record> result = getReadOnlyContext().select()
			.from(Tables.EH_CONTRACTS)
			.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.and(Tables.EH_CONTRACTS.CONTRACT_NUMBER.in(contractNumbers))
			.orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc())
			.fetch();
		
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}

	@Override
	public Contract findActiveContractByContractNumber(Integer namespaceId, String contractNumber) {
		Record result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
//				.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.CONTRACT_NUMBER.eq(contractNumber))
				.fetchAny();

		if (result != null) {
			return ConvertHelper.convert(result, Contract.class);
		}
		return null;
	}

	@Override
	public List<Contract> listContractByNamespaceId(Integer namespaceId, int from, int pageSize) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc())
				.limit(from, pageSize)
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}
	
	@Override
	public List<Contract> listContractByOrganizationId(Long organizationId) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(organizationId))
				.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc()) 
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}

	@Override
	public List<Object> findCustomerByContractNum(String contractNum) {
        List<Object> list = new ArrayList<>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhContracts t = Tables.EH_CONTRACTS.as("t");
        EhEnterpriseCustomers t1 = Tables.EH_ENTERPRISE_CUSTOMERS.as("t1");
        EhOrganizationOwners t2 = Tables.EH_ORGANIZATION_OWNERS.as("t2");
        EhUserIdentifiers t4 = Tables.EH_USER_IDENTIFIERS.as("t4");
        EhUsers t5 = Tables.EH_USERS.as("t5");
        final EhOrganizations[] t6 = {Tables.EH_ORGANIZATIONS.as("t6")};
        final Byte[] customerType = new Byte[1];
        final Long[] customerId = new Long[1];
        final String[] customerName = new String[1];
		final Long[] contractId = new Long[1];
        context.select(t.CUSTOMER_TYPE,t.CUSTOMER_ID,t.CUSTOMER_NAME,t.ID)
                .from(t)
                .where(t.CONTRACT_NUMBER.eq(contractNum))
                .fetch()
                .map(r -> {
                    customerType[0] = r.getValue(t.CUSTOMER_TYPE);
                    customerId[0] = r.getValue(t.CUSTOMER_ID);
                    customerName[0] = r.getValue(t.CUSTOMER_NAME);
                    contractId[0] = r.getValue(t.ID);
                    return null;
                });
        if(customerType[0] != null && customerType[0]==0) {
            Long organizationId = context.select(t1.ORGANIZATION_ID)
                    .from(t1)
                    .where(t1.ID.eq(customerId[0]))
                    .fetchOne(t1.ORGANIZATION_ID);
            list.add("eh_organization");
            list.add(organizationId);
            list.add(customerName[0]);
            list.add("");
            list.add(contractId[0]);
        }else if(customerType[0]!=null && customerType[0] == 1){
            String userIdentifier = context.select(t2.CONTACT_TOKEN)
                    .from(t2)
                    .where(t2.ID.eq(customerId[0]))
                    .fetchOne(t2.CONTACT_TOKEN);
            Long userId = context.select(t4.OWNER_UID)
                    .from(t4)
                    .where(t4.IDENTIFIER_TOKEN.eq(userIdentifier))
                    .fetchOne(t4.OWNER_UID);
            list.add("eh_user");
            list.add(userId);
            list.add(customerName[0]);
            list.add(userIdentifier);
            list.add(contractId[0]);
        }

        return list;
    }

	public List<Contract> listContractByCustomerId(Long communityId, Long customerId, byte customerType) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(customerId))
//				.and(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId))
				.and(Tables.EH_CONTRACTS.CUSTOMER_TYPE.eq(customerType))
				.and(Tables.EH_CONTRACTS.STATUS.ne(ContractStatus.INACTIVE.getCode()))
				.fetch();

		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}

		return new ArrayList<Contract>();
	}

	@Override
	public Map<Long, List<Contract>> listContractGroupByCommunity() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);

		query.addConditions(Tables.EH_CONTRACTS.STATUS.in(ContractStatus.WAITING_FOR_LAUNCH.getCode(),
				ContractStatus.ACTIVE.getCode(), ContractStatus.EXPIRING.getCode(), ContractStatus.APPROVE_QUALITIED.getCode()));

		Map<Long, List<Contract>> result = new HashMap<>();
		query.fetch().map((r) -> {
			List<Contract> contracts = result.get(r.getCommunityId());
			if(contracts == null) {
				contracts = new ArrayList<>();
				contracts.add(ConvertHelper.convert(r, Contract.class));
				result.put(r.getCommunityId(), contracts);
			} else {
				contracts.add(ConvertHelper.convert(r, Contract.class));
				result.put(r.getCommunityId(), contracts);
			}
			return null;
		});

		return result;
	}


	@Override
	public List<Contract> listContractsByEndDateRange(Timestamp minValue, Timestamp maxValue) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.CONTRACT_END_DATE.gt(minValue))
				.and(Tables.EH_CONTRACTS.CONTRACT_END_DATE.le(maxValue))
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}

	@Override
	public List<Contract> listContractsByCreateDateRange(Timestamp minValue, Timestamp maxValue) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.CREATE_TIME.gt(minValue))
				.and(Tables.EH_CONTRACTS.CREATE_TIME.le(maxValue))
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}

	@Override
	public List<Contract> listContractByOrganizationId(Integer namespaceId, Long organizationId) {
		return getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(organizationId))
				.fetch()
				.map(r->ConvertHelper.convert(r, Contract.class));
	}

	@Override
	public List<Contract> listContracts(CrossShardListingLocator locator, Integer pageSize) {
		List<Contract> contracts = new ArrayList<Contract>();

		if (locator.getShardIterator() == null) {
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhContracts.class);
			ShardIterator shardIterator = new ShardIterator(accessSpec);
			locator.setShardIterator(shardIterator);
		}
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
			SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);

			if(locator.getAnchor() != null && locator.getAnchor() != 0L){
				query.addConditions(Tables.EH_CONTRACTS.ID.lt(locator.getAnchor()));
			}

			query.addOrderBy(Tables.EH_CONTRACTS.ID.desc());
			query.addLimit(pageSize - contracts.size());

			query.fetch().map((r) -> {
				contracts.add(ConvertHelper.convert(r, Contract.class));
				return null;
			});

			if (contracts.size() >= pageSize) {
				locator.setAnchor(contracts.get(contracts.size() - 1).getId());
				return IterationMapReduceCallback.AfterAction.done;
			} else {
				locator.setAnchor(null);
			}
			return IterationMapReduceCallback.AfterAction.next;
		});

		return contracts;
	}

	@Override
	public Map<Long, Contract> listContractsByIds(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);
		query.addConditions(Tables.EH_CONTRACTS.ID.in(ids));

		Map<Long, Contract> result = new HashMap<>();
		query.fetch().map((r) -> {
			result.put(r.getId(), ConvertHelper.convert(r, Contract.class));
			return null;
		});

		return result;
	}

	@Override
	public void createContractParam(ContractParam param) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractParams.class));
		param.setId(id);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractParams.class, id));
		EhContractParamsDao dao = new EhContractParamsDao(context.configuration());
		dao.insert(param);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractParams.class, null);
	}

	@Override
	public ContractParam findContractParamByCommunityId(Long communityId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractParamsRecord> query = context.selectQuery(Tables.EH_CONTRACT_PARAMS);
		query.addConditions(Tables.EH_CONTRACT_PARAMS.COMMUNITY_ID.eq(communityId));

		List<ContractParam> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ContractParam.class));
			return null;
		});

		if(result.size() == 0) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public void updateContractParam(ContractParam param) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractParams.class, param.getId()));
		EhContractParamsDao dao = new EhContractParamsDao(context.configuration());

		dao.update(param);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractParams.class, param.getId());

	}

	private EhContractsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhContractsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhContractsDao getDao(DSLContext context) {
		return new EhContractsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
