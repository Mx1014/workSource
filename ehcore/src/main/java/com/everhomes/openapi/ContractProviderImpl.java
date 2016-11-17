// @formatter:off
package com.everhomes.openapi;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
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
				.and(Tables.EH_CONTRACTS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_CONTRACTS.CONTRACT_NUMBER.eq(contractNumber))
				.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, Contract.class);
		}
		return null;
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
