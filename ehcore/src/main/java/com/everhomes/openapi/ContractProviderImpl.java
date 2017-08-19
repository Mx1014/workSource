// @formatter:off
package com.everhomes.openapi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
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
				.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
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
	public List<Contract> listContractsByIds(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);
		query.addConditions(Tables.EH_CONTRACTS.ID.in(ids));

		List<Contract> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, Contract.class));
			return null;
		});

		return result;
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
