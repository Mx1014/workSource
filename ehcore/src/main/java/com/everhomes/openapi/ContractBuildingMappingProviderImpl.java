// @formatter:off
package com.everhomes.openapi;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.records.EhContractBuildingMappingsRecord;
import com.everhomes.server.schema.tables.records.EhVarFieldItemScopesRecord;
import com.everhomes.server.schema.tables.records.EhVarFieldItemsRecord;

import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.varField.VarFieldStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractBuildingMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhContractBuildingMappings;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RecordHelper;
import com.everhomes.varField.FieldItem;
import com.everhomes.varField.ScopeFieldItem;

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
	
	@Caching(evict = { @CacheEvict(value="listByContract", key="#contractBuildingMapping.contractId")} )
	@Override
	public void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping) {
		assert (contractBuildingMapping.getId() != null);
		getReadWriteDao().update(contractBuildingMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractBuildingMappings.class, contractBuildingMapping.getId());
	}
	
	@Caching(evict = { @CacheEvict(value="listByContract", key="#contractBuildingMapping.contractId")} )
	@Override
	public void deleteContractBuildingMapping(ContractBuildingMapping contractBuildingMapping) {
		assert (contractBuildingMapping.getId() != null);
		getReadWriteDao().delete(contractBuildingMapping);
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
	public ContractBuildingMapping findContractBuildingMappingByName(Integer namespaceId, Long organizationId, String contractNumber, String buildingName,
			String apartmentName) {
		Record record = getReadOnlyContext().select().from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
				.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_NUMBER.eq(contractNumber))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.APARTMENT_NAME.eq(apartmentName))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME.eq(buildingName))
				.fetchOne();
		
		if (record != null) {
			return ConvertHelper.convert(record, ContractBuildingMapping.class);
		}
		return null;
	}

	@Override
	public ContractBuildingMapping findAnyContractBuildingMappingByNumber(String contractNumber) {
		Record record = getReadOnlyContext().select()
			.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
			.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
			.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_NUMBER.eq(contractNumber))
			.fetchAny();
		if (record != null) {
			return ConvertHelper.convert(record, ContractBuildingMapping.class);
		}
		return null;
	}

	@Override
	public void deleteContractBuildingMappingByOrganizatinName(Integer namespaceId, String organizationName) {
		getReadWriteContext().update(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
		.set(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS, CommonStatus.INACTIVE.getCode())
		.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
		.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ORGANIZATION_NAME.eq(organizationName))
		.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.ne(CommonStatus.INACTIVE.getCode()))
		.execute();
	}

	@Override
	public List<String> listContractByKeywords(Integer namespaceId, String buildingName, String keywords) {
		SelectConditionStep<Record1<String>> step = getReadOnlyContext().selectDistinct(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_NUMBER)
		.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
		.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
		.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.NAMESPACE_ID.eq(namespaceId));
		
		if (StringUtils.isNotBlank(buildingName)) {
			step = step.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME.eq(buildingName));
		}
		
		if (StringUtils.isNotBlank(keywords)) {
			step = step.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_NUMBER.like("%"+keywords+"%"))
					.or(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ORGANIZATION_NAME.like("%"+keywords+"%"));
		}
		
		Result<Record1<String>> result = step.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->r.value1());
		}
		
		return new ArrayList<String>();
	}

	@Override
	public List<BuildingApartmentDTO> listBuildingsByContractNumber(Integer namespaceId, String contractNumber) {
		Result<Record2<String, String>> result = getReadOnlyContext().select(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME, Tables.EH_CONTRACT_BUILDING_MAPPINGS.APARTMENT_NAME)
				.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
				.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_NUMBER.eq(contractNumber))
				.orderBy(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME.asc(), Tables.EH_CONTRACT_BUILDING_MAPPINGS.APARTMENT_NAME.asc())
				.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, BuildingApartmentDTO.class));
		}
		
		return new ArrayList<BuildingApartmentDTO>();
	}

	@Override
	public List<ContractBuildingMapping> listContractBuildingMappingByContract(Integer namespaceId, Long organizationId,
			String contractNumber) {
		return getReadOnlyContext().select().from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
			.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ORGANIZATION_ID.eq(organizationId))
			.and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_NUMBER.eq(contractNumber))
			.fetch()
			.map(r->ConvertHelper.convert(r, ContractBuildingMapping.class));
	}

	@Cacheable(value = "listByContract", key="#contractId", unless="#result.size() == 0")
	@Override
	public List<ContractBuildingMapping> listByContract(Long contractId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractBuildingMappingsRecord> query = context.selectQuery(Tables.EH_CONTRACT_BUILDING_MAPPINGS);
		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(contractId));
		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

		List<ContractBuildingMapping> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ContractBuildingMapping.class));
			return null;
		});

		return result;
	}

	@Override
	public List<ContractBuildingMapping> listByAddress(Long addressId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractBuildingMappingsRecord> query = context.selectQuery(Tables.EH_CONTRACT_BUILDING_MAPPINGS);
		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.eq(addressId));
		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

		List<ContractBuildingMapping> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ContractBuildingMapping.class));
			return null;
		});

		return result;
	}
	
	@Override
	public FieldItem listFieldItems(String moduleName, Long fieldId, Byte businessValue) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<FieldItem> item = new ArrayList<>();
		SelectQuery<EhVarFieldItemsRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEMS);
		query.addConditions(Tables.EH_VAR_FIELD_ITEMS.FIELD_ID.eq(fieldId));
		query.addConditions(Tables.EH_VAR_FIELD_ITEMS.MODULE_NAME.eq(moduleName));
		query.addConditions(Tables.EH_VAR_FIELD_ITEMS.BUSINESS_VALUE.eq(businessValue));
		query.addConditions(Tables.EH_VAR_FIELD_ITEMS.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

		query.fetch().map((r) -> {
			item.add(ConvertHelper.convert(r, FieldItem.class));
			return null;
		});

		if (item.size() == 0)
			return null;

		return item.get(0);
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
