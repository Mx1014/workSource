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
import com.everhomes.server.schema.tables.daos.EhContractBuildingMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhContractBuildingMappings;
import com.everhomes.util.ConvertHelper;

@Component
public class ContractBuildingMappingProviderImpl implements ContractBuildingMappingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createContractBuildingMapping(ContractBuildingMapping contractBuildingMapping) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractBuildingMappings.class));
		contractBuildingMapping.setId(id);
		getReadWriteDao().insert(contractBuildingMapping);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractBuildingMappings.class, null);
	}

	@Override
	public void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping) {
		assert (contractBuildingMapping.getId() != null);
		getReadWriteDao().update(contractBuildingMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractBuildingMappings.class, contractBuildingMapping.getId());
	}

	@Override
	public ContractBuildingMapping findContractBuildingMappingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ContractBuildingMapping.class);
	}
	
	@Override
	public List<ContractBuildingMapping> listContractBuildingMapping() {
		return getReadOnlyContext().select().from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
				.orderBy(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ContractBuildingMapping.class));
	}
	
	@Override
	public ContractBuildingMapping findContractBuildingMappingByName(String contractNumber, String buildingName,
			String apartmentName) {
		Record record = getReadOnlyContext().select().from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
				.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_NUMBER.eq(contractNumber))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME.eq(buildingName))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.APARTMENT_NAME.eq(apartmentName))
				.fetchOne();
		return null;
	}

	private EhContractBuildingMappingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhContractBuildingMappingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhContractBuildingMappingsDao getDao(DSLContext context) {
		return new EhContractBuildingMappingsDao(context.configuration());
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
