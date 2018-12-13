// @formatter:off
package com.everhomes.openapi;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.db.*;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomerAptitudeFlag;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.everhomes.asset.AssetProvider;
import com.everhomes.contract.ContractAttachment;
import com.everhomes.contract.ContractCategory;
import com.everhomes.contract.ContractChargingChange;
import com.everhomes.contract.ContractChargingItem;
import com.everhomes.contract.ContractEvents;
import com.everhomes.contract.ContractParam;
import com.everhomes.contract.ContractParamGroupMap;
import com.everhomes.contract.ContractReportformStatisticCommunitys;
import com.everhomes.contract.ContractTaskOperateLog;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractChargingItemReportformDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.contract.ContractLogDTO;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.ContractTemplateStatus;
import com.everhomes.rest.contract.ContractTrackingTemplateCode;
import com.everhomes.rest.contract.statistic.ContractStaticsListDTO;
import com.everhomes.rest.contract.statistic.ContractStatisticDateType;
import com.everhomes.rest.contract.statistic.TotalContractStaticsDTO;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhCommunities;
import com.everhomes.server.schema.tables.EhContractBuildingMappings;
import com.everhomes.server.schema.tables.EhContractEvents;
import com.everhomes.server.schema.tables.EhContractStatisticCommunities;
import com.everhomes.server.schema.tables.EhContractTemplates;
import com.everhomes.server.schema.tables.EhContracts;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.EhOrganizationOwners;
import com.everhomes.server.schema.tables.EhOrganizations;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhContractCategories;
import com.everhomes.server.schema.tables.pojos.EhContractParamGroupMap;
import com.everhomes.server.schema.tables.pojos.EhContractParams;
import com.everhomes.server.schema.tables.pojos.EhContractTaskOperateLogs;
import com.everhomes.server.schema.tables.records.EhContractParamGroupMapRecord;
import com.everhomes.server.schema.tables.records.EhContractParamsRecord;
import com.everhomes.server.schema.tables.records.EhContractsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.varField.FieldItem;
import com.everhomes.varField.FieldParams;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;

import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PmResourceReservation;
import com.everhomes.organization.pm.reportForm.PropertyReportFormStatus;

@Component
public class ContractProviderImpl implements ContractProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContractProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
	private FieldService fieldService;
	
	@Autowired
	private FieldProvider fieldProvider;
	
	@Autowired
	private AssetProvider assetProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
    private OrganizationService organizationService;

	@Override
	public void createContract(Contract contract) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContracts.class));
		contract.setId(id);
		contract.setCreateUid(UserContext.currentUserId());
		contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(contract);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhContracts.class, null);
	}

	@Override
	public void updateContract(Contract contract) {
		assert (contract.getId() != null);
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(contract);
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
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
	public List<Contract> listContractByContractNumbers(Integer namespaceId, List<String> contractNumbers, Long categoryId) {
		Result<Record> result = null;
		if(categoryId == null || categoryId.longValue() <= 0L) {
		    result = getReadOnlyContext().select()
		            .from(Tables.EH_CONTRACTS)
		            .where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
		            .and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
		            .and(Tables.EH_CONTRACTS.CONTRACT_NUMBER.in(contractNumbers))
		            .orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc())
		            .fetch();
		} else {
    		result = getReadOnlyContext().select()
    			.from(Tables.EH_CONTRACTS)
    			.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
    			.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
    			.and(Tables.EH_CONTRACTS.CONTRACT_NUMBER.in(contractNumbers))
    			.and(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId))
    			.orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc())
    			.fetch();
		}
		
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}

	@Override
	public Contract findActiveContractByContractNumber(Integer namespaceId, String contractNumber, Long categoryId) {
		SelectConditionStep<Record> query = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.STATUS.ne(CommonStatus.INACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.CONTRACT_NUMBER.eq(contractNumber));
				
		if (categoryId != null || "".equals(categoryId)) {
			query.and(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId));
		}
		
		Record result = query.fetchAny();
		if (result != null) {
			return ConvertHelper.convert(result, Contract.class);
		}
		return null;
	}

	@Override
	public List<Contract> listContractByBuildingName(String buildingName, Long communityId) {
		List<Contract> result = new ArrayList<>();
		SelectQuery<Record> query = getReadOnlyContext().selectQuery();
		query.addSelect(Tables.EH_CONTRACTS.ID,Tables.EH_CONTRACTS.STATUS);
		query.addFrom(Tables.EH_CONTRACTS);
		query.addJoin(Tables.EH_CONTRACT_BUILDING_MAPPINGS, JoinType.LEFT_OUTER_JOIN, Tables.EH_CONTRACTS.ID.eq(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID));

		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME.eq(buildingName));
		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		query.addConditions(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId));

		LOGGER.debug("query sql:{}", query.getSQL());
		LOGGER.debug("query param:{}", query.getBindValues());
		query.fetch().map((r) -> {
			Contract contract = new Contract();
			contract.setId(r.getValue(Tables.EH_CONTRACTS.ID));
			contract.setStatus(r.getValue(Tables.EH_CONTRACTS.STATUS));
			result.add(contract);
			return null;
		});
		return result;
	}

	@Override
	public List<Contract> listContractByAddressId(Long addressId) {

		List<Contract> result = new ArrayList<>();
		SelectQuery<Record> query = getReadOnlyContext().selectQuery();
		query.addSelect(Tables.EH_CONTRACTS.ID,Tables.EH_CONTRACTS.STATUS);
		query.addFrom(Tables.EH_CONTRACTS);
		query.addJoin(Tables.EH_CONTRACT_BUILDING_MAPPINGS, JoinType.LEFT_OUTER_JOIN, Tables.EH_CONTRACTS.ID.eq(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID));

		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.eq(addressId));
		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

		LOGGER.debug("query sql:{}", query.getSQL());
		LOGGER.debug("query param:{}", query.getBindValues());
		query.fetch().map((r) -> {
			Contract contract = new Contract();
			contract.setId(r.getValue(Tables.EH_CONTRACTS.ID));
			contract.setStatus(r.getValue(Tables.EH_CONTRACTS.STATUS));
			result.add(contract);
			return null;
		});
		return result;
	}

	@Override
	public List<Contract> listContractByNamespaceId(Integer namespaceId, int from, int pageSize, Long categoryId) {
	    Result<Record> result = null;
	    if(categoryId == null || categoryId.longValue() >= 0L) {
    		result = getReadOnlyContext().select()
    				.from(Tables.EH_CONTRACTS)
    				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
    				.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
    				.orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc())
    				.limit(from, pageSize)
    				.fetch();
	    } else {
	        result = getReadOnlyContext().select()
                    .from(Tables.EH_CONTRACTS)
                    .where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                    .and(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId))
                    .orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc())
                    .limit(from, pageSize)
                    .fetch();
	    }
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}
	
	@Override
	public List<Contract> listContractByNamespaceId(Integer namespaceId) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.orderBy(Tables.EH_CONTRACTS.CONTRACT_NUMBER.asc())
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}
	
	@Override
	public List<Contract> listContractByOrganizationId(Long organizationId, Long categoryId) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(organizationId))
				.and(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId))
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

	@Override
	public List<Contract> listContractByCustomerId(Long communityId, Long customerId, byte customerType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);
		query.addConditions(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(customerId));
		if(communityId != null) {
			query.addConditions(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId));
		}
		query.addConditions(Tables.EH_CONTRACTS.CUSTOMER_TYPE.eq(customerType));
		query.addConditions(Tables.EH_CONTRACTS.STATUS.ne(ContractStatus.INACTIVE.getCode()));

		List<Contract> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, Contract.class));
			return null;
		});
		return result;
	}

	@Override
	public List<Contract> listContractByCustomerId(Long communityId, Long customerId, byte customerType, Byte status, Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);
		query.addConditions(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(customerId));
		if(communityId != null) {
			query.addConditions(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId));
		}
		query.addConditions(Tables.EH_CONTRACTS.CUSTOMER_TYPE.eq(customerType));

		if(status != null) {
			query.addConditions(Tables.EH_CONTRACTS.STATUS.eq(status));
		} else {
			query.addConditions(Tables.EH_CONTRACTS.STATUS.ne(ContractStatus.INACTIVE.getCode()));
		}
		/*if(categoryId != null) {
			query.addConditions(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId));
		}*/

		List<Contract> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, Contract.class));
			return null;
		});
		return result;
	}

	@Override
	public Map<Long, List<Contract>> listContractGroupByCommunity() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);

		query.addConditions(Tables.EH_CONTRACTS.STATUS.in(ContractStatus.WAITING_FOR_LAUNCH.getCode(),
				ContractStatus.ACTIVE.getCode(), ContractStatus.EXPIRING.getCode(), ContractStatus.APPROVE_QUALITIED.getCode()));

		Map<Long, List<Contract>> result = new HashMap<>();
		query.fetch().forEach((r) -> {
            Long communityId = r.getCommunityId();
            if(communityId == null){
                return;
            }
            List<Contract> contracts = result.get(communityId);
			if(contracts == null) {
				contracts = new ArrayList<>();
				contracts.add(ConvertHelper.convert(r, Contract.class));
				result.put(r.getCommunityId(), contracts);
			} else {
				contracts.add(ConvertHelper.convert(r, Contract.class));
				result.put(r.getCommunityId(), contracts);
			}
		});

		return result;
	}


	@Override
	public List<Contract> listContractsByEndDateRange(Timestamp minValue, Timestamp maxValue, Integer namespaceId) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.CONTRACT_END_DATE.gt(minValue))
				.and(Tables.EH_CONTRACTS.CONTRACT_END_DATE.le(maxValue))
				.and(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}

	@Override
	public List<Contract> listContractsByCreateDateRange(Timestamp minValue, Timestamp maxValue, Integer namespaceId) {
		Result<Record> result = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.CREATE_TIME.gt(minValue))
				.and(Tables.EH_CONTRACTS.CREATE_TIME.le(maxValue))
				.and(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.fetch();
			
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, Contract.class));
		}
		
		return new ArrayList<Contract>();
	}

	@Override
	public List<Contract> listContractByOrganizationId(Integer namespaceId, Long organizationId, Long categoryId) {
		return getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(organizationId))
				.and(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId))
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
	public List<Contract> listContractsByAddressId(Long addressId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<Contract> result  = new ArrayList<Contract>();
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);
		query.addJoin(Tables.EH_CONTRACT_BUILDING_MAPPINGS, Tables.EH_CONTRACTS.ID.eq(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID));
		query.addConditions(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.eq(addressId));
		query.addConditions(Tables.EH_CONTRACTS.STATUS.notEqual(CommonStatus.INACTIVE.getCode()));

		query.addOrderBy(Tables.EH_CONTRACTS.ID.desc());
		result = query.fetch().map(new DefaultRecordMapper(Tables.EH_CONTRACTS.recordType(), Contract.class));

		return result;
	}

	@Override
	public String findContractIdByThirdPartyId(String contractId, String code) {
		DSLContext context = getReadOnlyContext();
		List<Long> zuolinContractId = context.select(Tables.EH_CONTRACTS.ID)
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_CONTRACT_TOKEN.eq(contractId))
				.and(Tables.EH_CONTRACTS.NAMESPACE_CONTRACT_TYPE.eq(code))
				.fetch(Tables.EH_CONTRACTS.ID);
		if(zuolinContractId.size() > 0) {
			return String.valueOf(zuolinContractId.get(0));
		}
		return null;
	}

	@Override
	public List<Long> SimpleFindContractByNumber(String contractNumber) {
		DSLContext context = getReadOnlyContext();
        List<Long> fetch = context.select(Tables.EH_CONTRACTS.ID)
                .from(Tables.EH_CONTRACTS)
                .where(Tables.EH_CONTRACTS.CONTRACT_NUMBER.eq(contractNumber))
                .fetch(Tables.EH_CONTRACTS.ID);
        return fetch;
    }

	public List<ContractLogDTO> listContractsBySupplier(Long supplierId, Long pageAnchor, Integer pageSize) {
		DSLContext context = getReadOnlyContext();
		List<ContractLogDTO> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		context.select(Tables.EH_CONTRACTS.CONTRACT_NUMBER,Tables.EH_CONTRACTS.CONTRACT_START_DATE
                ,Tables.EH_CONTRACTS.CONTRACT_END_DATE,Tables.EH_CONTRACTS.CONTRACT_TYPE
                ,Tables.EH_CONTRACTS.NAME,Tables.EH_CONTRACTS.STATUS,Tables.EH_CONTRACTS.RENT)
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.CUSTOMER_ID.eq(supplierId))
				.and(Tables.EH_CONTRACTS.CUSTOMER_TYPE.eq(CustomerType.SUPPLIER.getCode()))
				.limit(pageAnchor.intValue(),pageSize)
				.fetch()
				.forEach(r -> {
					ContractLogDTO dto = new ContractLogDTO();
					dto.setContractNumber(r.getValue(Tables.EH_CONTRACTS.CONTRACT_NUMBER));
                    Timestamp startDate = r.getValue(Tables.EH_CONTRACTS.CONTRACT_START_DATE);
                    Timestamp endDate = r.getValue(Tables.EH_CONTRACTS.CONTRACT_END_DATE);
                    if(startDate != null){
                        String startDateFormatted = sdf.format(startDate);
                        dto.setContractStartDate(startDateFormatted);
                    }
                    if(endDate != null){
                        String endDateFormatted = sdf.format(endDate);
                        dto.setContractEndDate(endDateFormatted);
                    }
                    dto.setContractType(r.getValue(Tables.EH_CONTRACTS.CONTRACT_TYPE));
                    dto.setName(r.getValue(Tables.EH_CONTRACTS.NAME));
                    dto.setStatus(r.getValue(Tables.EH_CONTRACTS.STATUS));
                    dto.setTotalAmount(r.getValue(Tables.EH_CONTRACTS.RENT).toString());
                    list.add(dto);
				});
		return list;
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
	public ContractParam findContractParamByCommunityId(Integer namespaceId, Long communityId, Byte payorreceiveContractType, Long ownerId,Long categoryId, Long appId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractParamsRecord> query = context.selectQuery(Tables.EH_CONTRACT_PARAMS);
		query.addConditions(Tables.EH_CONTRACT_PARAMS.NAMESPACE_ID.eq(namespaceId));
		if(payorreceiveContractType != null) {
			query.addConditions(Tables.EH_CONTRACT_PARAMS.PAYORRECEIVE_CONTRACT_TYPE.eq(payorreceiveContractType));
		}
		if(categoryId != null) {
			query.addConditions(Tables.EH_CONTRACT_PARAMS.CATEGORY_ID.eq(categoryId));
		}
		if (categoryId == null) {
			query.addConditions(Tables.EH_CONTRACT_PARAMS.CATEGORY_ID.isNull());
		}
		if(communityId != null) {
			query.addConditions(Tables.EH_CONTRACT_PARAMS.COMMUNITY_ID.eq(communityId));
		}
		if(communityId == null) {
			query.addConditions(Tables.EH_CONTRACT_PARAMS.COMMUNITY_ID.eq(0L)
					.or(Tables.EH_CONTRACT_PARAMS.COMMUNITY_ID.isNull()));
			
			query.addConditions(Tables.EH_CONTRACT_PARAMS.OWNER_ID.eq(ownerId));
			
		}
		// zuolin base 
		/*if(ownerId!=null){
			query.addConditions(Tables.EH_CONTRACT_PARAMS.OWNER_ID.eq(ownerId));
		}*/
		
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

	@Override
	public void createContractParamGroupMap(ContractParamGroupMap map) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractParamGroupMap.class));
		map.setId(id);
		map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractParamGroupMap.class, id));
		EhContractParamGroupMapDao dao = new EhContractParamGroupMapDao(context.configuration());
		dao.insert(map);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractParamGroupMap.class, id);
	}

	@Override
	public void deleteContractParamGroupMap(ContractParamGroupMap map) {
		assert(map.getId() != null);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractParamGroupMap.class, map.getId()));
		EhContractParamGroupMapDao dao = new EhContractParamGroupMapDao(context.configuration());
		dao.delete(map);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractParamGroupMap.class, map.getId());
	}

	@Override
	public List<ContractParamGroupMap> listByParamId(Long paramId, Byte groupType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractParamGroupMapRecord> query = context.selectQuery(Tables.EH_CONTRACT_PARAM_GROUP_MAP);
		query.addConditions(Tables.EH_CONTRACT_PARAM_GROUP_MAP.PARAM_ID.eq(paramId));
		query.addConditions(Tables.EH_CONTRACT_PARAM_GROUP_MAP.GROUP_TYPE.eq(groupType));

		List<ContractParamGroupMap> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ContractParamGroupMap.class));
			return null;
		});

		return result;
	}

	@Override
	public String findLastContractVersionByCommunity(Integer namespaceId, Long communityId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId))
				.orderBy(Tables.EH_CONTRACTS.VERSION.desc())
				.limit(1)
				.fetchOne();
		if (record != null) {
			return record.getValue(Tables.EH_CONTRACTS.VERSION);
		}
		return null;
	}

	@Override
	public Timestamp findLastContractVersionByCommunity(Integer namespaceId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.orderBy(Tables.EH_CONTRACTS.VERSION.desc())
				.limit(1)
				.fetchOne();
		if (record != null) {
			return record.getValue(Tables.EH_CONTRACTS.UPDATE_TIME);
		}
		return null;
	}


	@Override
	public List<Contract> listContractByNamespaceType(Integer namespaceId, String namespaceType, Long communityId, Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhContractsRecord> query = context.selectQuery(Tables.EH_CONTRACTS);
		query.addConditions(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_CONTRACTS.NAMESPACE_CONTRACT_TYPE.eq(namespaceType));
		/*query.addConditions(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId));
		query.addConditions(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId));*/

		List<Contract> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, Contract.class));
			return null;
		});

		return result;
	}
	
	@Override
	public Contract findContractByNamespaceToken(Integer namespaceId, String namespaceContractType, Long namespaceContractToken, Long categoryId) {
		SelectConditionStep<Record> query = getReadOnlyContext().select()
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_CONTRACTS.STATUS.ne(CommonStatus.INACTIVE.getCode()))
				.and(Tables.EH_CONTRACTS.NAMESPACE_CONTRACT_TYPE.eq(namespaceContractType));
				
		if (categoryId != null || "".equals(categoryId)) {
			query.and(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId));
		}
		if (namespaceContractToken != null || "".equals(namespaceContractToken)) {
			query.and(Tables.EH_CONTRACTS.NAMESPACE_CONTRACT_TOKEN.eq(namespaceContractToken.toString()));
		}
		
		Record result = query.fetchAny();
		if (result != null) {
			return ConvertHelper.convert(result, Contract.class);
		}
		return null;
	}

	@Override
	public void createContractCategory(ContractCategory contractCategory) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractCategories.class));
		contractCategory.setId(id);
		contractCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractCategories.class, id));
		EhContractCategoriesDao dao = new EhContractCategoriesDao(context.configuration());
		dao.insert(contractCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractCategories.class, id);
	}
	
	@Override
	public ContractCategory findContractCategoryById(Long categoryId) { 
		assert(categoryId != null);
		EhContractCategoriesDao dao = new EhContractCategoriesDao(getReadOnlyContext().configuration());
		return ConvertHelper.convert(dao.findById(categoryId), ContractCategory.class);
	}
	@Override
	public void updateContractCategory(ContractCategory contractCategory) {
		new EhContractCategoriesDao(getContext(AccessSpec.readWrite()).configuration()).update(contractCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, ContractCategory.class, null);
	}
	
	//显示合同日志
	@Override
	public List<ContractEvents> listContractEvents(Long contractId) {
		List<ContractEvents> result = new ArrayList<>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractEvents.class));
        context.select()
        	   .from(Tables.EH_CONTRACT_EVENTS)
        	   .where(Tables.EH_CONTRACT_EVENTS.CONTRACT_ID.eq(contractId))
        	   .orderBy(Tables.EH_CONTRACT_EVENTS.OPEARTE_TIME.desc())
        	   .fetch()
        	   .map(r->{
        		   ContractEvents contractEvents = ConvertHelper.convert(r, ContractEvents.class);
        		   result.add(contractEvents);
        		   return null;
        	   });
		return result;
	}
	
	//记录合同修改日志 by tangcen
	@Override
	public void saveContractEvent(int opearteType, Contract contract, Contract comparedContract) {	
		//根据不同操作类型获取具体描述，1:ADD,2:DELETE,3:MODIFY,20:COPY,21:INITIALIZE,22:EXEMPTION
		String content = null;
		Map<String,Object> map = new HashMap<String,Object>();
		switch(opearteType){
			case ContractTrackingTemplateCode.CONTRACT_ADD : 
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_ADD , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			case ContractTrackingTemplateCode.CONTRACT_DELETE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_DELETE , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			case ContractTrackingTemplateCode.CONTRACT_UPDATE:
				content = compareContract(contract,comparedContract);
				break;
			case ContractTrackingTemplateCode.CONTRACT_RENEW:
				map.put("contractName", comparedContract.getName());
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_RENEW , UserContext.current().getUser().getLocale(), map, "");
				break;
			case ContractTrackingTemplateCode.CONTRACT_CHANGE:
				map.put("contractName", comparedContract.getName());
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_CHANGE , UserContext.current().getUser().getLocale(), map, "");
				break;
			case ContractTrackingTemplateCode.CONTRACT_COPY:
				map.put("contractName", comparedContract.getContractNumber());
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_COPY , UserContext.current().getUser().getLocale(), map, "");
				break;
			case ContractTrackingTemplateCode.CONTRACT_INITIALIZE:
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_INITIALIZE , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			case ContractTrackingTemplateCode.CONTRACT_EXEMPTION:
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_EXEMPTION , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			ContractEvents event = new ContractEvents(); 
			long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractEvents.class));
			event.setId(id);
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setContractId(contract.getId());	
			event.setOperatorUid(UserContext.currentUserId());
			if (opearteType==ContractTrackingTemplateCode.CONTRACT_RENEW||opearteType==ContractTrackingTemplateCode.CONTRACT_CHANGE) {
				event.setOpearteTime(comparedContract.getUpdateTime());
				//如果是续约合同或者是变更合同，则归为合同修改操作
				opearteType=ContractTrackingTemplateCode.CONTRACT_UPDATE;
			}else {
				event.setOpearteTime(contract.getUpdateTime());
			}
			event.setOpearteType((byte)opearteType);
	        LOGGER.info("saveContractEventWithInsert: " + event);
	        insertContractEvents(event);
		}
	}
	
	@Override
	public void saveContractEventAboutApartments(int opearteType, Contract contract, ContractBuildingMapping mapping) {
		//根据不同操作类型获取具体描述，1:ADD,2:DELETE
		String content = null;
		HashMap<String, String> dataMap = new HashMap<>();
		switch(opearteType){
			case ContractTrackingTemplateCode.APARTMENT_ADD : 
				dataMap.put("apartmentName", mapping.getApartmentName());
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.APARTMENT_ADD , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.APARTMENT_DELETE :
				dataMap.put("apartmentName", mapping.getApartmentName());
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.APARTMENT_DELETE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			ContractEvents event = new ContractEvents(); 
			long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractEvents.class));
			event.setId(id);
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setContractId(contract.getId());	
			event.setOperatorUid(UserContext.currentUserId());
			event.setOpearteTime(contract.getUpdateTime());
			//因为关联资产的新增或删除是基于合同的修改，因此日志类型归为合同修改类型
			event.setOpearteType((byte)ContractTrackingTemplateCode.CONTRACT_UPDATE);
	        LOGGER.info("saveContractEventWithInsert: " + event);
	        insertContractEvents(event);
		}
	}
	
	@Override
	public void saveContractEventAboutApartments(int opearteType, Contract contract, String oldApartmnets,String newApartmnets) {
		//根据不同操作类型获取具体描述，1:ADD,2:DELETE
		String content = null;
		HashMap<String, String> dataMap = new HashMap<>();
		switch(opearteType){
		case ContractTrackingTemplateCode.APARTMENT_ADD : 
			dataMap.put("apartmentName", newApartmnets.substring(1,newApartmnets.length()-1));
			content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.APARTMENT_ADD , UserContext.current().getUser().getLocale(), dataMap, "");
			break;
		case ContractTrackingTemplateCode.APARTMENT_UPDATE :
			dataMap.put("oldApartmnets", oldApartmnets.substring(1,oldApartmnets.length()-1));
			dataMap.put("newApartmnets", newApartmnets.substring(1,newApartmnets.length()-1));
			content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.APARTMENT_UPDATE , UserContext.current().getUser().getLocale(), dataMap, "");
			break;
		default :
			break;
		}
		if(!StringUtils.isEmpty(content)){
			ContractEvents event = new ContractEvents(); 
			long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractEvents.class));
			event.setId(id);
			event.setContent(content);
			event.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setContractId(contract.getId());	
			event.setOperatorUid(UserContext.currentUserId());
			event.setOpearteTime(contract.getUpdateTime());
			//因为关联资产的新增或删除是基于合同的修改，因此日志类型归为合同修改类型
			event.setOpearteType((byte)ContractTrackingTemplateCode.CONTRACT_UPDATE);
	        LOGGER.info("saveContractEventWithInsert: " + event);
	        insertContractEvents(event);
		}
	}
	

	@Override
	public void saveContractEventAboutChargingItem(int opearteType, Contract contract,ContractChargingItem contractChargingItem) {
		//根据不同操作类型获取具体描述，1:ADD,2:DELETE,3:UPDATE
		HashMap<String, String> dataMap = new HashMap<>();
		String chargingItemName = assetProvider.findChargingItemNameById(contractChargingItem.getChargingItemId());
		dataMap.put("chargingItemName", chargingItemName);
		String content = null;
		switch(opearteType){
			case ContractTrackingTemplateCode.CHARGING_ITEM_ADD : 
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CHARGING_ITEM_ADD , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.CHARGING_ITEM_DELETE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CHARGING_ITEM_DELETE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.CHARGING_ITEM_UPDATE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CHARGING_ITEM_UPDATE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			//保存合同日志,因为计价条款的新增或删除是基于合同的修改，因此日志类型归为合同修改类型
			saveGeneralContractEvent((byte)ContractTrackingTemplateCode.CONTRACT_UPDATE,content,contract);
		}
	}
	
	@Override
	public void saveContractEventAboutAttachment(int opearteType, Contract contract, ContractAttachment contractAttachment){
		//根据不同操作类型获取具体描述，1:ADD,2:DELETE
		HashMap<String, String> dataMap = new HashMap<>();
		String attachmentName = contractAttachment.getName();
		dataMap.put("attachmentName", attachmentName);
		String content = null;
		switch(opearteType){
			case ContractTrackingTemplateCode.ATTACHMENT_ADD : 
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.ATTACHMENT_ADD , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.ATTACHMENT_DELETE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.ATTACHMENT_DELETE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			//保存合同日志,因为合同附件的新增或删除是基于合同的修改，因此日志类型归为合同修改类型
			saveGeneralContractEvent((byte)ContractTrackingTemplateCode.CONTRACT_UPDATE,content,contract);
		}
	}
	
	@Override
	public void saveContractEventAboutChargingChange(int opearteType, Contract contract,ContractChargingChange contractChargingChange){
		//根据不同操作类型获取具体描述，1:ADD,2:DELETE
		HashMap<String, String> dataMap = new HashMap<>();
		String chargingChangeName = assetProvider.findChargingItemNameById(contractChargingChange.getChargingItemId());
		dataMap.put("chargingChangeName", chargingChangeName);
		String content = null;
		switch(opearteType){
			case ContractTrackingTemplateCode.ADJUST_ADD : 
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.ADJUST_ADD , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.ADJUST_DELETE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.ADJUST_DELETE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.ADJUST_UPDATE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.ADJUST_UPDATE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;	
			case ContractTrackingTemplateCode.FREE_ADD : 
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.FREE_ADD , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.FREE_DELETE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.FREE_DELETE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			case ContractTrackingTemplateCode.FREE_UPDATE :
				content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.FREE_UPDATE , UserContext.current().getUser().getLocale(), dataMap, "");
				break;
			default :
				break;
		}
		if(!StringUtils.isEmpty(content)){
			//保存合同日志,因为合同附件的新增或删除是基于合同的修改，因此日志类型归为合同修改类型
			saveGeneralContractEvent((byte)ContractTrackingTemplateCode.CONTRACT_UPDATE,content,contract);
		}
	}
	
	@Override
	public void insertContractEvents(ContractEvents event){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractEvents.class, event.getId()));
		EhContractEvents t = Tables.EH_CONTRACT_EVENTS;
        context
        	.insertInto(t, t.ID, t.NAMESPACE_ID, t.CONTRACT_ID, t.OPERATOR_UID, t.OPEARTE_TIME, t.OPEARTE_TYPE, t.CONTENT)
        	.values(event.getId(),event.getNamespaceId(),
        			event.getContractId(),event.getOperatorUid(),
        			event.getOpearteTime(),event.getOpearteType(),
        			event.getContent())
        	.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractEvents.class, null);
	}
	
	//保存合同日志 by tangcen
	private void saveGeneralContractEvent(Byte opearteType,String content,Contract contract){
		ContractEvents event = new ContractEvents(); 
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractEvents.class));
		event.setId(id);
		event.setContent(content);
		event.setNamespaceId(UserContext.getCurrentNamespaceId());
		event.setContractId(contract.getId());	
		event.setOperatorUid(UserContext.currentUserId());
		event.setOpearteTime(contract.getUpdateTime());
		event.setOpearteType(opearteType);
        LOGGER.info("saveContractEventWithInsert: " + event);
        insertContractEvents(event);
	}
	
	//比较已存在的contract和待修改的contract的区别 by tangcen
	private String compareContract(Contract contract, Contract exist) {
		//查出模板配置的参数
		ListFieldCommand command = new ListFieldCommand();
		command.setNamespaceId(contract.getNamespaceId());
		command.setModuleName(ContractTrackingTemplateCode.MODULE_NAME);
		command.setGroupPath(ContractTrackingTemplateCode.GROUP_PATH);
		command.setCommunityId(contract.getCommunityId());
		command.setCategoryId(contract.getCategoryId());
		List<FieldDTO> fields = fieldService.listFields(command);
		String  getPrefix = "get";
		StringBuffer buffer = new StringBuffer();
		if(null != fields && fields.size() > 0){
			for(FieldDTO field : fields){
				String getter = getPrefix + StringUtils.capitalize(field.getFieldName());
				Method methodNew = ReflectionUtils.findMethod(contract.getClass(), getter);
				Method methodOld = ReflectionUtils.findMethod(exist.getClass(), getter);
                String fieldType = field.getFieldType();
				Object objNew = null;
				Object objOld = null;
				try {
					if(null != methodNew && null != exist){
						objNew = methodNew.invoke(contract, new Object[] {});
						objOld = methodOld.invoke(exist, new Object[] {});
					}
				} catch (Exception e) {
					throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACT_TRACKING_NOT_EXIST,
		                    "reflect exception");
				}
				
				//处理 首付总额、押金 字段
				if(null != objNew || null != objOld){
					//解决前端传过来的数值和后端获取的数值不等的问题（比如前端传1000，后端获取的是1000.00,如不处理则会判断为不相等）
					if(null != objNew && null != objOld){
						if ("downpayment".equals(field.getFieldName()) || "deposit".equals(field.getFieldName())) {
							BigDecimal newDigit = (BigDecimal)objNew;
							BigDecimal oldDigit = (BigDecimal)objOld;
							if (newDigit.compareTo(oldDigit)==0) {
								continue;
							}
						}
					}
					
					if(!(objNew == null ? "" : objNew).equals((objOld == null ? "" : objOld))){
						String  content = "";
						String  newData = objNew == null ? "空" : objNew.toString();
						String  oldData = objOld == null ? "空" : objOld.toString();
                        LOGGER.debug("compareContract FieldName: {}; newData: {}; oldData: {}",
                                field.getFieldName(), newData, oldData);
						
                        if(field.getFieldName().lastIndexOf("ItemId") > -1){
							ScopeFieldItem levelItemNew = fieldProvider.findScopeFieldItemByFieldItemId(contract.getNamespaceId(),contract.getOrgId(), contract.getCommunityId(),(objNew == null ? -1l : Long.parseLong(objNew.toString())));
					        if(levelItemNew != null) {
					        	newData = levelItemNew.getItemDisplayName();
					        }
					        ScopeFieldItem levelItemOld = fieldProvider.findScopeFieldItemByFieldItemId(exist.getNamespaceId(),contract.getOrgId(),exist.getCommunityId(), (objOld == null ? -1l : Long.parseLong(objOld.toString())));
					        if(levelItemOld != null) {
					        	oldData = levelItemOld.getItemDisplayName();
					        }
						}
						//处理 合同状态 字段
						if("status".equals(field.getFieldName())){
							FieldItem fieldItemsNew = fieldProvider.findFieldItemByBusinessValue(field.getFieldId(),(Byte)objNew);
							if(fieldItemsNew != null) {
					        	newData = fieldItemsNew.getDisplayName();
					        }
							FieldItem fieldItemsOld = fieldProvider.findFieldItemByBusinessValue(field.getFieldId(),(Byte)objOld);
							if(fieldItemsOld != null) {
					        	oldData = fieldItemsOld.getDisplayName();
					        }
						}
						//处理 退约经办人 字段
						if("denunciationUid".equals(field.getFieldName())){
							//查找组织架构中的contact_name
				            if (objNew != null) {
				            	User userNew = userProvider.findUserById((Long)objNew);
								if (userNew != null) {
									OrganizationMemberDetails organizationMember = organizationProvider.findOrganizationMemberDetailsByTargetId(userNew.getId());
									if(organizationMember != null) {
										newData = organizationMember.getContactName();
									}else{
										newData = userNew.getNickName();
									}
								}
							}
				            if (objOld != null) {
				            	User userOld = userProvider.findUserById((Long)objOld);
								if (userOld != null) {
									OrganizationMemberDetails organizationMember = organizationProvider.findOrganizationMemberDetailsByTargetId(userOld.getId());
									if(organizationMember != null) {
										oldData = organizationMember.getContactName();
									}else{
										oldData = userOld.getNickName();
									}
								}
				            }
						}
						//templateId
						if("templateId".equals(field.getFieldName())){
							//获取模板的名字
				            if (objNew!=null) {
				            	//根据id查询数据，
				        		ContractTemplate contractTemplateParent = this.findContractTemplateById((Long)objNew);
				            	if(contractTemplateParent != null) {
					            	newData = contractTemplateParent.getName();
					            }
							}
				            if (objOld!=null) {
				            	//根据id查询数据，
				        		ContractTemplate contractTemplateParent = this.findContractTemplateById((Long)objOld);
					            if(contractTemplateParent != null) {
					            	oldData = contractTemplateParent.getName();
					            }
							}
						}
                        FieldParams params = (FieldParams) StringHelper.fromJsonString(field.getFieldParam(), FieldParams.class);
                        if((params.getFieldParamType().equals("select") || params.getFieldParamType().equals("customizationSelect")) && field.getFieldName().lastIndexOf("Id") > -1){
                            ScopeFieldItem levelItemNew = fieldProvider.findScopeFieldItemByFieldItemId(contract.getNamespaceId(), contract.getCreateOrgId(),contract.getCommunityId(),(objNew == null ? -1l : Long.parseLong(objNew.toString())));
                            if(levelItemNew != null) {
                                newData = levelItemNew.getItemDisplayName();
                            }
                            ScopeFieldItem levelItemOld = fieldProvider.findScopeFieldItemByFieldItemId(exist.getNamespaceId(),contract.getCreateOrgId(),exist.getCommunityId(), (objOld == null ? -1l : Long.parseLong(objOld.toString())));
                            if(levelItemOld != null) {
                                oldData = levelItemOld.getItemDisplayName();
                            }
                        }
                        if((params.getFieldParamType().equals("datetime"))){
                            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            if (objNew != null) {
                            	newData = sdf.format((Timestamp)objNew); 
							}
                            if (objOld != null) {
                            	oldData = sdf.format((Timestamp)objOld);
							} 
                        }
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("display", field.getFieldDisplayName());
						map.put("oldData", oldData);
						map.put("newData", newData);
						content = localeTemplateService.getLocaleTemplateString(ContractTrackingTemplateCode.SCOPE, ContractTrackingTemplateCode.CONTRACT_UPDATE, UserContext.current().getUser().getLocale(), map, "");
						buffer.append(content);
						buffer.append(";");
					}
				}
			}
		}
		//去除最后一个分号
		return buffer.toString().length() > 0 ? buffer.toString().substring(0,buffer.toString().length() -1) : buffer.toString();
	}
	

	@Override
	public boolean isNormal(Long cid) {
		List<Byte> cids = getReadOnlyContext().select(Tables.EH_CONTRACTS.STATUS)
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.ID.eq(cid))
				.fetch(Tables.EH_CONTRACTS.STATUS);
		if(cids == null || cids.size() < 1){
			return false;
		}
		if(cids.size() == 1){
            Byte aByte = cids.get(0);
            if(aByte != 7 && aByte != 8 && aByte != 9 && aByte != 10){
                return true;
            }
        }
		return false;
	}

	@Override
	public Long findContractCategoryIdByContractId(Long contractId) {
		DSLContext context = getReadOnlyContext();
        return context.select(Tables.EH_CONTRACTS.CATEGORY_ID)
                .from(Tables.EH_CONTRACTS)
                .where(Tables.EH_CONTRACTS.ID.eq(contractId))
                .fetchOne(Tables.EH_CONTRACTS.CATEGORY_ID);
	}
	
	@Override
	public void createContractTemplate(ContractTemplate contractTemplate) {
        long id = this.dbProvider.allocPojoRecordId(EhContractTemplates.class);
        contractTemplate.setId(id);
        contractTemplate.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        contractTemplate.setStatus(ContractTemplateStatus.ACTIVE.getCode()); //有效的状态
        contractTemplate.setCreatorUid(UserContext.currentUserId());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractTemplates.class, id));
        EhContractTemplatesDao dao = new EhContractTemplatesDao(context.configuration());
        dao.insert(contractTemplate);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractTemplates.class, null);
	}

	@Override
	public ContractTemplate findContractTemplateById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhContractTemplates.class, id));
        EhContractTemplatesDao dao = new EhContractTemplatesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ContractTemplate.class);
	}

	@Override
	public void updateContractTemplate(ContractTemplate contractTemplate) {
		assert(contractTemplate.getId() != null);
		contractTemplate.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractTemplate.setUpdateUid(UserContext.currentUserId());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractTemplates.class, contractTemplate.getId()));
        EhContractTemplatesDao dao = new EhContractTemplatesDao(context.configuration());
        dao.update(contractTemplate);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractTemplates.class, contractTemplate.getId());
	}

	@Override
	public List<ContractTemplate> listContractTemplates(Integer namespaceId, Long ownerId, String ownerType,Long orgId,
			Long categoryId, String name, Long pageAnchor, Integer pageSize, Long appId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractTemplates.class));
        EhContractTemplates t1 = Tables.EH_CONTRACT_TEMPLATES.as("t1");
        EhContractTemplates t2 = Tables.EH_CONTRACT_TEMPLATES.as("t2");
        SelectJoinStep<Record> query = context.select(Tables.EH_CONTRACT_TEMPLATES.fields()).from(Tables.EH_CONTRACT_TEMPLATES);
        
		Condition cond = Tables.EH_CONTRACT_TEMPLATES.NAMESPACE_ID.eq(namespaceId);
		cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode()));
		
		if(null != pageAnchor && pageAnchor != 0){
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ID.gt(pageAnchor));
		}
		if(null != name && !"".equals(name)){
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.NAME.like('%'+name+'%'));
		}
		if(null != categoryId){
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.CATEGORY_ID.eq(categoryId));
		}
		//取到最大的versionid
		if (null != ownerId && ownerId != -1) {
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.OWNER_ID.eq(ownerId).or(Tables.EH_CONTRACT_TEMPLATES.OWNER_ID.eq(0L)));
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ID
					.notIn(context.select(t1.ID).from(t1, t2).where(t1.ID.eq(t2.PARENT_ID).and(t2.OWNER_ID.eq(0L)))));
			
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ID
					.notIn(context.select(t1.ID).from(t1, t2).where(t1.ID.eq(t2.PARENT_ID).and(t1.OWNER_ID.eq(ownerId)))));
			
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ORG_ID.eq(orgId).or(Tables.EH_CONTRACT_TEMPLATES.ORG_ID.eq(0L)));
		}else {
			//查询所有的通用模板
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ID
					.notIn(context.select(t1.ID).from(t1, t2).where(t1.ID.eq(t2.PARENT_ID).and(t2.OWNER_ID.eq(0L)))));
			cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ID
					.notIn(context.select(t1.ID).from(t1, t2).where(t1.ID.eq(t2.PARENT_ID).and(t1.OWNER_ID.notEqual(0L)))));
			// get all communities data and all organization owner general data
			if (orgId != null) {
				cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ORG_ID.eq(orgId).or(Tables.EH_CONTRACT_TEMPLATES.ORG_ID.eq(0L)));
				//cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.ORG_ID.eq(orgId).and(Tables.EH_CONTRACT_TEMPLATES.OWNER_ID.eq(0L)).or(Tables.EH_CONTRACT_TEMPLATES.OWNER_ID.ne(0L)));
			}
			
			 List<Long> communityIds = new ArrayList<>();
		     communityIds =  organizationService.getOrganizationProjectIdsByAppId(orgId, appId);
		     cond = cond.and(Tables.EH_CONTRACT_TEMPLATES.OWNER_ID.in(communityIds).or(Tables.EH_CONTRACT_TEMPLATES.OWNER_ID.eq(0L)));
		}

		query.orderBy(Tables.EH_CONTRACT_TEMPLATES.CREATE_TIME.desc());
		
		if(null != pageSize)
			query.limit(pageSize);
		
		List<ContractTemplate> contracttemplates = query.where(cond).fetch().
				map(new DefaultRecordMapper(Tables.EH_CONTRACT_TEMPLATES.recordType(), ContractTemplate.class));

		return contracttemplates;
	}

	@Override
	public void setPrintContractTemplate(Integer namespaceId, Long contractId, Long categoryId, String contractNumber,
			Long ownerId, Long templateId) {
		getReadWriteContext().update(Tables.EH_CONTRACTS)
		.set(Tables.EH_CONTRACTS.TEMPLATE_ID, templateId)
		.where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
		.and(Tables.EH_CONTRACTS.ID.eq(contractId))
		.and(Tables.EH_CONTRACTS.CATEGORY_ID.eq(categoryId))
		.execute();
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

	@Override
	public Boolean getContractTemplateById(Long id) {
		List<Byte> cids = getReadOnlyContext().select(Tables.EH_CONTRACTS.STATUS)
				.from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.TEMPLATE_ID.eq(id))
				.and(Tables.EH_CONTRACTS.STATUS.notEqual(ContractTemplateStatus.INACTIVE.getCode()))
				.fetch(Tables.EH_CONTRACTS.STATUS);
		if(cids == null || cids.size() < 1){
			//模板没有关联合同
			return false;
		}
		
		return true;
	}

	@Override
	public Long findCategoryIdByNamespaceId(Integer namespaceId) {
		DSLContext context = getReadOnlyContext();
        return context.select(Tables.EH_CONTRACT_CATEGORIES.ID)
                .from(Tables.EH_CONTRACT_CATEGORIES)
                .where(Tables.EH_CONTRACT_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
                .fetchOne(Tables.EH_CONTRACT_CATEGORIES.ID);
	}

	@Override
	public Double getTotalRentInCommunity(Long communityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Double> rentList = context.select(Tables.EH_CONTRACTS.RENT)
										.from(Tables.EH_CONTRACTS)
										.where(Tables.EH_CONTRACTS.COMMUNITY_ID.eq(communityId))
										.and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
										.fetchInto(Double.class);
		Double result = 0.0;
		for (Double rent : rentList) {
			result += (rent != null ? rent : 0.0);
		}
		return result;
	}

	@Override
	public Integer countRelatedContractNumberInBuilding(String buildingName,Long communityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		//issue-36117:门牌只有一个，在租合同却有134份
		//因为在EH_CONTRACT_BUILDING_MAPPINGS只存了building_name,没有community_id,
		//因此在查询时,有可能不同的community里有同名的building,导致查到的contract数量不准，会多查
		List<Long> addressIds = context.selectDistinct(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID)
										.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
										.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME.eq(buildingName))
										.fetchInto(Long.class);

		List<Long> addressIdsInCommunity = context.select(Tables.EH_ADDRESSES.ID)
													.from(Tables.EH_ADDRESSES)
													.where(Tables.EH_ADDRESSES.ID.in(addressIds))
													.and(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
													.fetchInto(Long.class);

		List<Long> contractIds = context.selectDistinct(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID)
										.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
										.leftOuterJoin(Tables.EH_CONTRACTS)
										.on(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(Tables.EH_CONTRACTS.ID))
										.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.in(addressIdsInCommunity))
										.and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
										.fetchInto(Long.class);

		if (contractIds!=null && contractIds.size()>0) {
			return contractIds.size();
		}
		return 0;
	}

	@Override
	public Double getTotalRentInBuilding(String buildingName) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		List<Long> contractIds = context.selectDistinct(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID)
										.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
										.leftOuterJoin(Tables.EH_CONTRACTS)
										.on(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(Tables.EH_CONTRACTS.ID))
										.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.BUILDING_NAME.eq(buildingName))
										.and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
										.fetchInto(Long.class);

		List<Double> rentList =  context.select(Tables.EH_CONTRACTS.RENT)
										.from(Tables.EH_CONTRACTS)
										.where(Tables.EH_CONTRACTS.ID.in(contractIds))
										.fetchInto(Double.class);
		Double result = 0.0;
		for (Double rent : rentList) {
			result += (rent != null ? rent : 0.0);
		}
		return result;
	}

	@Override
	public List<Contract> findContractByAddressId(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return 	 context.select()
						.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
						.leftOuterJoin(Tables.EH_CONTRACTS)
						.on(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(Tables.EH_CONTRACTS.ID))
						.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.eq(addressId))
						.and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
						.fetchInto(Contract.class);
	}

	@Override
	public Byte filterAptitudeCustomer(Long ownerId, Integer namespaceId){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomerAptitudeFlag.class));
		EhEnterpriseCustomerAptitudeFlagDao dao = new EhEnterpriseCustomerAptitudeFlagDao(context.configuration());

		SelectConditionStep<Record> query = context.select()
				.from(Tables.EH_ENTERPRISE_CUSTOMER_APTITUDE_FLAG)
				.where(Tables.EH_ENTERPRISE_CUSTOMER_APTITUDE_FLAG.OWNER_ID.eq(ownerId))
				.and(Tables.EH_ENTERPRISE_CUSTOMER_APTITUDE_FLAG.NAMESPACE_ID.eq(namespaceId));
		Record result = query.fetchAny();

		if(result != null){
			EnterpriseCustomerAptitudeFlag flag = ConvertHelper.convert(result, EnterpriseCustomerAptitudeFlag.class);
			return flag.getValue();
		}else{
			LOGGER.error("the namespace and communityid not find flag");
			return 0;
		}
	}

	@Override
	public EnterpriseCustomerAptitudeFlag updateAptitudeCustomer(Long ownerId, Integer namespaceId, Byte adptitudeFlag){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomerAptitudeFlag.class));
		EhEnterpriseCustomerAptitudeFlagDao dao = new EhEnterpriseCustomerAptitudeFlagDao(context.configuration());

		SelectConditionStep<Record> query = context.select()
				.from(Tables.EH_ENTERPRISE_CUSTOMER_APTITUDE_FLAG)
				.where(Tables.EH_ENTERPRISE_CUSTOMER_APTITUDE_FLAG.OWNER_ID.eq(ownerId))
				.and(Tables.EH_ENTERPRISE_CUSTOMER_APTITUDE_FLAG.NAMESPACE_ID.eq(namespaceId));
		Record result = query.fetchAny();

		if(result != null){
			EnterpriseCustomerAptitudeFlag flag = ConvertHelper.convert(result, EnterpriseCustomerAptitudeFlag.class);
			flag.setValue(adptitudeFlag);
			dao.update(flag);
			return flag;
		}else{
			long id = this.dbProvider.allocPojoRecordId(EhEnterpriseCustomerAptitudeFlag.class);
			EhEnterpriseCustomerAptitudeFlag flag2 = new EhEnterpriseCustomerAptitudeFlag();
			flag2.setId(id);
			flag2.setNamespaceId(namespaceId);
			flag2.setOwnerId(ownerId);
			flag2.setOwnerType("community");
			flag2.setValue(adptitudeFlag);
			dao.insert(flag2);
			return ConvertHelper.convert(flag2, EnterpriseCustomerAptitudeFlag.class);

		}
	}

	@Override
	public Integer getRelatedContractCountByAddressIds(List<Long> addressIdList) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		List<Long> contractIds = context.selectDistinct(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID)
										.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
										.leftOuterJoin(Tables.EH_CONTRACTS)
										.on(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(Tables.EH_CONTRACTS.ID))
										.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.in(addressIdList))
										.and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
										.fetchInto(Long.class);

		if (contractIds!=null && contractIds.size()>0) {
			return contractIds.size();
		}
		return 0;
	}

	@Override
	public Double getTotalRentByAddressIds(List<Long> addressIdList) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		List<Long> contractIds = context.selectDistinct(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID)
										.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
										.leftOuterJoin(Tables.EH_CONTRACTS)
										.on(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(Tables.EH_CONTRACTS.ID))
										.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.in(addressIdList))
										.and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
										.fetchInto(Long.class);

		List<Double> rentList =  context.select(Tables.EH_CONTRACTS.RENT)
										.from(Tables.EH_CONTRACTS)
										.where(Tables.EH_CONTRACTS.ID.in(contractIds))
										.fetchInto(Double.class);
		Double result = 0.0;
		for (Double rent : rentList) {
			result += (rent != null ? rent : 0.0);
		}
		return result;
	}

	@Override
	public List<Contract> listContractsByNamespaceIdAndStatus(Integer namespaceId, byte statusCode) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		
		return context.select()
					  .from(Tables.EH_CONTRACTS)
					  .where(Tables.EH_CONTRACTS.NAMESPACE_ID.eq(namespaceId))
					  .and(Tables.EH_CONTRACTS.STATUS.eq(statusCode))
					  .fetchInto(Contract.class);
	}
	
	@Override
	public List<ContractCategory> listContractAppCategory(Integer namespaceId) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<ContractCategory> list = dslContext.select()
	        .from(Tables.EH_CONTRACT_CATEGORIES)
	        .where(Tables.EH_CONTRACT_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
	        .and(Tables.EH_CONTRACT_CATEGORIES.STATUS.eq(ContractStatus.ACTIVE.getCode()))
	        .fetchInto(ContractCategory.class);
		return list;
	}
	
	@Override
	public Map<String, BigDecimal> getChargeAreaByContractIdAndAddress(List<Long> contractIds,
			List<String> buildindNames, List<String> apartmentNames) {
		Map<String, BigDecimal> resultMap = new HashMap<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhContractBuildingMappings a = Tables.EH_CONTRACT_BUILDING_MAPPINGS;
		
		context.select(a.CONTRACT_ID,a.BUILDING_NAME,a.APARTMENT_NAME,a.AREA_SIZE)
		       .from(a)
		       .where(a.CONTRACT_ID.in(contractIds))
		       .and(a.BUILDING_NAME.in(buildindNames))
		       .and(a.APARTMENT_NAME.in(apartmentNames))
		       .and(a.STATUS.eq((byte)2))
		       .fetch()
		       .stream()
		       .forEach(r->{
		    	   Long contractId = r.getValue(a.CONTRACT_ID);
		    	   String buildingName = r.getValue(a.BUILDING_NAME);
		    	   String apartmentName = r.getValue(a.APARTMENT_NAME);
		    	   Double areaSize = r.getValue(a.AREA_SIZE);
		    	   
		    	   String key = contractId + "-" + buildingName + "-" + apartmentName;
		    	   resultMap.put(key, areaSize!=null ? new BigDecimal(areaSize): BigDecimal.ZERO);
		       });
		return resultMap;
	}
	
	@Override
	public BigDecimal getTotalChargeArea(List<Long> contractIds,List<String> buildindNames, List<String> apartmentNames) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhContractBuildingMappings a = Tables.EH_CONTRACT_BUILDING_MAPPINGS;
		
		return context.select(DSL.sum(a.AREA_SIZE))
				       .from(a)
				       .where(a.CONTRACT_ID.in(contractIds))
				       .and(a.BUILDING_NAME.in(buildindNames))
				       .and(a.APARTMENT_NAME.in(apartmentNames))
				       .and(a.STATUS.eq((byte)2))
				       .fetchAnyInto(BigDecimal.class);
	}
	//合同4.0
	@Override
	public Boolean possibleEnterContractFuture(ContractDetailDTO currentExistContract,ContractBuildingMapping contractBuildingMapping) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Contract> list = dslContext.select().from(Tables.EH_CONTRACTS)
				.where(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()).or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.WAITING_FOR_APPROVAL.getCode()))
						.or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.APPROVE_QUALITIED.getCode())).or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.EXPIRING.getCode())))
				.and((Tables.EH_CONTRACTS.CONTRACT_START_DATE.between(currentExistContract.getContractStartDate(), currentExistContract.getContractEndDate()))
						.or(Tables.EH_CONTRACTS.CONTRACT_END_DATE.between(currentExistContract.getContractStartDate(), currentExistContract.getContractEndDate())))
				
				.and(Tables.EH_CONTRACTS.ID.eq(contractBuildingMapping.getContractId()))
				
				.fetchInto(Contract.class);

		if (list!=null && list.size()>0) {
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean resoucreReservationsFuture(ContractDetailDTO contractDetailDTO,BuildingApartmentDTO apartment) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<PmResourceReservation> list = dslContext.select().from(Tables.EH_PM_RESOUCRE_RESERVATIONS)
				.where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID.eq(apartment.getAddressId()))
				.and((Tables.EH_PM_RESOUCRE_RESERVATIONS.START_TIME.between(contractDetailDTO.getContractStartDate(), contractDetailDTO.getContractEndDate()))
						.or(Tables.EH_PM_RESOUCRE_RESERVATIONS.END_TIME.between(contractDetailDTO.getContractStartDate(), contractDetailDTO.getContractEndDate())))
				
				.and(Tables.EH_PM_RESOUCRE_RESERVATIONS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
				.fetchInto(PmResourceReservation.class);

		if (list!=null && list.size()>0) {
			return true;
		}
		return false;
	}
	
	@Override
    public void createContractOperateTask(ContractTaskOperateLog job) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractTaskOperateLogs.class));
        job.setId(id);
        job.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        job.setStatus(ContractTemplateStatus.ACTIVE.getCode()); //有效的状态
        job.setCreatorUid(UserContext.currentUserId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractTaskOperateLogsDao dao = new EhContractTaskOperateLogsDao(context.configuration());
        dao.insert(job);
    }

    @Override
    public void updateContractOperateTask(ContractTaskOperateLog job) {
        assert(job.getId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractTaskOperateLogsDao dao = new EhContractTaskOperateLogsDao(context.configuration());
        dao.update(job);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractTaskOperateLogs.class, job.getId());
    }


    @Override
    public ContractTaskOperateLog findContractOperateTaskById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhContractTaskOperateLogs.class, id));
        EhContractTaskOperateLogsDao dao = new EhContractTaskOperateLogsDao(context.configuration());
        EhContractTaskOperateLogs result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, ContractTaskOperateLog.class);
    }

	@Override
	public List<Contract> findAnyStatusContractByAddressId(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return 	 context.select()
						.from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
						.leftOuterJoin(Tables.EH_CONTRACTS)
						.on(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(Tables.EH_CONTRACTS.ID))
						.where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.eq(addressId))
						.and(
								Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.APPROVE_QUALITIED.getCode())
								.or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.WAITING_FOR_APPROVAL.getCode()))
								.or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()))
								.or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.EXPIRING.getCode()))
								.or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.DRAFT.getCode()))
								.or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.WAITING_FOR_LAUNCH.getCode()))
							)
						.fetchInto(Contract.class);
	}

	//合同报表
	@Override
	public void deleteCommunityDataByDateStr(String dateStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_CONTRACT_STATISTIC_COMMUNITIES)
				//.where(Tables.EH_CONTRACT_STATISTIC_COMMUNITIES.DATE_STR.eq(dateStr))
				.execute();
	}
	
	@Override
	public int getTotalContractCount(Timestamp firstdateUpdateTime, Timestamp lastdateUpdateTime) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectCount()
				       .from(Tables.EH_CONTRACTS)
				       .where(Tables.EH_CONTRACTS.NAMESPACE_ID.ne(0))
				       .and(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()).or(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.DENUNCIATION.getCode())))
				       //.and(Tables.EH_CONTRACTS.COMMUNITY_ID.isNotNull())
				       //.and(Tables.EH_CONTRACTS.UPDATE_TIME.between(firstdateUpdateTime, lastdateUpdateTime))
				       .fetchAnyInto(Integer.class);
	}
	
	@Override
	public void createCommunityStatics(ContractReportformStatisticCommunitys communityStatistics) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractStatisticCommunities.class));
		EhContractStatisticCommunitiesDao dao = new EhContractStatisticCommunitiesDao(context.configuration());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractStatisticCommunities.class));
		communityStatistics.setId(id);
		communityStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		dao.insert(communityStatistics);
	}
	
	@Override
	public List<ContractChargingItemReportformDTO> getContractChargingItemInfoList(ContractDTO contract) {
		List<ContractChargingItemReportformDTO> list = new ArrayList<ContractChargingItemReportformDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBillItems pbt = Tables.EH_PAYMENT_BILL_ITEMS.as("pbt");
		list = context.select(pbt.NAMESPACE_ID, pbt.OWNER_ID,pbt.CHARGING_ITEMS_ID,
	    		DSL.sum(pbt.AMOUNT_RECEIVABLE).as("AMOUNT_RECEIVABLE"), 
	    		DSL.sum(pbt.AMOUNT_RECEIVED).as("AMOUNT_RECEIVED"), 
	    		DSL.sum(pbt.AMOUNT_OWED).as("AMOUNT_OWED"),
	    		DSL.sum(pbt.AMOUNT_RECEIVABLE_WITHOUT_TAX).as("AMOUNT_RECEIVABLE_WITHOUT_TAX"), 
	    		DSL.sum(pbt.AMOUNT_RECEIVED_WITHOUT_TAX).as("AMOUNT_RECEIVED_WITHOUT_TAX"), 
	    		DSL.sum(pbt.AMOUNT_OWED_WITHOUT_TAX).as("AMOUNT_OWED_WITHOUT_TAX"),
	    		DSL.sum(pbt.TAX_AMOUNT).as("TAX_AMOUNT"))
	        .from(pbt)
	        .where(pbt.CONTRACT_ID.eq(contract.getId()))
	        //.and(pbt.DELETE_FLAG.eq(ContractStatus.ACTIVE.getCode()))
	        .fetchInto(ContractChargingItemReportformDTO.class);
		return list;
	}
	
	@Override
	public List<ContractStaticsListDTO> listCommunityContractStaticsList(Integer namespaceId, List<Long> communityIds, String formatDateStr, 
			String startTimeStr,String endTimeStr,  Byte dateType, Integer pageOffSet, Integer pageSize){
		
		List<ContractStaticsListDTO> result = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhCommunities a = Tables.EH_COMMUNITIES;
		EhContractStatisticCommunities b = Tables.EH_CONTRACT_STATISTIC_COMMUNITIES;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(a.ID,a.NAME,
				DSL.sum(b.RENT_AMOUNT),DSL.sum(b.RENTAL_AREA),DSL.sum(b.CONTRACT_COUNT),DSL.sum(b.CUSTOMER_COUNT),
				DSL.sum(b.ORG_CONTRACT_COUNT),DSL.sum(b.ORG_CONTRACT_AMOUNT),DSL.sum(b.USER_CONTRACT_COUNT),
				DSL.sum(b.USER_CONTRACT_AMOUNT),DSL.sum(b.DEPOSIT_AMOUNT),b.DATE_STR,DSL.sum(b.DATE_TYPE),
				DSL.sum(b.NEW_CONTRACT_AMOUNT),DSL.sum(b.NEW_CONTRACT_AREA),DSL.sum(b.NEW_CONTRACT_COUNT),
				DSL.sum(b.DENUNCIATION_CONTRACT_AMOUNT),DSL.sum(b.DENUNCIATION_CONTRACT_AREA),DSL.sum(b.DENUNCIATION_CONTRACT_COUNT),
				DSL.sum(b.CHANGE_CONTRACT_AMOUNT),DSL.sum(b.CHANGE_CONTRACT_AREA),DSL.sum(b.CHANGE_CONTRACT_COUNT),
				DSL.sum(b.RENEW_CONTRACT_AMOUNT),DSL.sum(b.RENEW_CONTRACT_AREA),DSL.sum(b.RENEW_CONTRACT_COUNT));
		query.addFrom(a);
		query.addJoin(b, JoinType.LEFT_OUTER_JOIN, a.ID.eq(b.COMMUNITY_ID));
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(a.ID.in(communityIds));
		if (dateType == ContractStatisticDateType.YEARMMSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr)) {
				query.addConditions(b.DATE_STR.le(endTimeStr));
				query.addConditions(b.DATE_STR.ge(startTimeStr));
			}else {
				query.addConditions(b.DATE_STR.eq(formatDateStr));
			}
			
		}
		if (dateType == ContractStatisticDateType.YEARSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr)) {
				query.addConditions(DSL.left(b.DATE_STR, 4).le(endTimeStr));
				query.addConditions(DSL.left(b.DATE_STR, 4).ge(startTimeStr));
			}else {
				query.addConditions(b.DATE_STR.like("%" + formatDateStr + "%"));
			}
		}
		query.addGroupBy(b.COMMUNITY_ID);
		
		query.addConditions(b.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		if (pageOffSet != null && pageSize != null) {
			query.addLimit(pageOffSet, pageSize + 1);
		}
		query.fetch().forEach(r->{
			ContractStaticsListDTO dto = new ContractStaticsListDTO();
			dto.setCommunityId(r.getValue(a.ID));
			dto.setCommunityName(r.getValue(a.NAME));
			//dto.setDateStr(r.getValue(b.DATE_STR));
			if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
				dto.setDateStr((r.getValue(b.DATE_STR)).substring(0,4));
			}else {
				dto.setDateStr(r.getValue(b.DATE_STR));
			}
			
			BigDecimal contractCount  = r.getValue(DSL.sum(b.CONTRACT_COUNT));
			BigDecimal customerCount  = r.getValue(DSL.sum(b.CUSTOMER_COUNT));
			BigDecimal orgContractCount  = r.getValue(DSL.sum(b.ORG_CONTRACT_COUNT));
			BigDecimal userContractCount  = r.getValue(DSL.sum(b.USER_CONTRACT_COUNT));
			BigDecimal newContractCount  = r.getValue(DSL.sum(b.NEW_CONTRACT_COUNT));
			BigDecimal denunciationContractCount  = r.getValue(DSL.sum(b.DENUNCIATION_CONTRACT_COUNT));
			BigDecimal changeContractCount  = r.getValue(DSL.sum(b.CHANGE_CONTRACT_COUNT));
			BigDecimal renewContractCount  = r.getValue(DSL.sum(b.RENEW_CONTRACT_COUNT));
			
			dto.setRentAmount(r.getValue(DSL.sum(b.RENT_AMOUNT)));
			dto.setRentalArea(r.getValue(DSL.sum(b.RENTAL_AREA)));
			dto.setContractCount(contractCount!=null ? contractCount.intValue() : null);
			dto.setCustomerCount(customerCount!=null ? customerCount.intValue() : null);
			dto.setOrgContractCount(orgContractCount!=null ? orgContractCount.intValue() : null);
			dto.setOrgContractAmount(r.getValue(DSL.sum(b.ORG_CONTRACT_AMOUNT)));
			dto.setUserContractCount(userContractCount!=null ? userContractCount.intValue() : null);
			dto.setUserContractAmount(r.getValue(DSL.sum(b.USER_CONTRACT_AMOUNT)));
			dto.setNewContractCount(newContractCount!=null ? newContractCount.intValue() : null);
			dto.setNewContractAmount(r.getValue(DSL.sum(b.NEW_CONTRACT_AMOUNT)));
			dto.setNewContractArea(r.getValue(DSL.sum(b.NEW_CONTRACT_AREA)));
			dto.setDenunciationContractCount(denunciationContractCount!=null ? denunciationContractCount.intValue() : null);
			dto.setDenunciationContractAmount(r.getValue(DSL.sum(b.DENUNCIATION_CONTRACT_AMOUNT)));
			dto.setDenunciationContractArea(r.getValue(DSL.sum(b.DENUNCIATION_CONTRACT_AREA)));
			dto.setChangeContractCount(changeContractCount!=null ? changeContractCount.intValue() : null);
			dto.setChangeContractAmount(r.getValue(DSL.sum(b.CHANGE_CONTRACT_AMOUNT)));
			dto.setChangeContractArea(r.getValue(DSL.sum(b.CHANGE_CONTRACT_AREA)));
			dto.setRenewContractCount(renewContractCount!=null ? renewContractCount.intValue() : null);
			dto.setRenewContractAmount(r.getValue(DSL.sum(b.RENEW_CONTRACT_AMOUNT)));
			dto.setRenewContractArea(r.getValue(DSL.sum(b.RENEW_CONTRACT_AREA)));
			dto.setDepositAmount(r.getValue(DSL.sum(b.DEPOSIT_AMOUNT)));
			result.add(dto);
		});
		return result;
	}
	
	@Override
	public TotalContractStaticsDTO getTotalContractStatics(Integer namespaceId, List<Long> communityIds,String formatDateStr, String startTimeStr,String endTimeStr, Byte dateType) {
		TotalContractStaticsDTO result = new TotalContractStaticsDTO();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhContractStatisticCommunities a = Tables.EH_CONTRACT_STATISTIC_COMMUNITIES;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(DSL.countDistinct(a.COMMUNITY_ID),DSL.sum(a.RENT_AMOUNT),DSL.sum(a.RENTAL_AREA),DSL.sum(a.CONTRACT_COUNT),DSL.sum(a.CUSTOMER_COUNT),DSL.sum(a.ORG_CONTRACT_COUNT),
				DSL.sum(a.ORG_CONTRACT_AMOUNT),DSL.sum(a.USER_CONTRACT_COUNT),DSL.sum(a.USER_CONTRACT_AMOUNT),
				DSL.sum(a.NEW_CONTRACT_COUNT),DSL.sum(a.NEW_CONTRACT_AMOUNT),DSL.sum(a.NEW_CONTRACT_AREA),
				DSL.sum(a.DENUNCIATION_CONTRACT_COUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AREA),
				DSL.sum(a.CHANGE_CONTRACT_COUNT),DSL.sum(a.CHANGE_CONTRACT_AMOUNT),DSL.sum(a.CHANGE_CONTRACT_AREA),
				DSL.sum(a.RENEW_CONTRACT_COUNT),DSL.sum(a.RENEW_CONTRACT_AMOUNT),DSL.sum(a.RENEW_CONTRACT_AREA),
				DSL.sum(a.DEPOSIT_AMOUNT));
		query.addFrom(a);
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		if (communityIds != null) {
			query.addConditions(a.COMMUNITY_ID.in(communityIds));
		}
		//处理时间问题
		if (dateType != null && dateType == ContractStatisticDateType.YEARMMSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(a.DATE_STR.le(endTimeStr));
				query.addConditions(a.DATE_STR.ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.eq(formatDateStr));
			}
		}
		if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(DSL.left(a.DATE_STR, 4).le(endTimeStr));
				query.addConditions(DSL.left(a.DATE_STR, 4).ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.like("%" + formatDateStr + "%"));
			}
		}
		query.addConditions(a.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.fetch().forEach(r->{
			Integer communityCount = r.getValue(DSL.countDistinct(a.COMMUNITY_ID));
			BigDecimal contractCount  = r.getValue(DSL.sum(a.CONTRACT_COUNT));
			BigDecimal customerCount  = r.getValue(DSL.sum(a.CUSTOMER_COUNT));
			BigDecimal orgContractCount  = r.getValue(DSL.sum(a.ORG_CONTRACT_COUNT));
			BigDecimal userContractCount  = r.getValue(DSL.sum(a.USER_CONTRACT_COUNT));
			BigDecimal newContractCount  = r.getValue(DSL.sum(a.NEW_CONTRACT_COUNT));
			BigDecimal denunciationContractCount  = r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_COUNT));
			BigDecimal changeContractCount  = r.getValue(DSL.sum(a.CHANGE_CONTRACT_COUNT));
			BigDecimal renewContractCount  = r.getValue(DSL.sum(a.RENEW_CONTRACT_COUNT));
			
			result.setCommunityCount(communityCount!=null ? communityCount : null);
			result.setRentAmount(r.getValue(DSL.sum(a.RENT_AMOUNT)));
			result.setRentalArea(r.getValue(DSL.sum(a.RENTAL_AREA)));
			result.setContractCount(contractCount!=null ? contractCount.intValue() : null);
			result.setCustomerCount(customerCount!=null ? customerCount.intValue() : null);
			result.setOrgContractCount(orgContractCount!=null ? orgContractCount.intValue() : null);
			result.setOrgContractAmount(r.getValue(DSL.sum(a.ORG_CONTRACT_AMOUNT)));
			result.setUserContractCount(userContractCount!=null ? userContractCount.intValue() : null);
			result.setUserContractAmount(r.getValue(DSL.sum(a.USER_CONTRACT_AMOUNT)));
			result.setNewContractCount(newContractCount!=null ? newContractCount.intValue() : null);
			result.setNewContractAmount(r.getValue(DSL.sum(a.NEW_CONTRACT_AMOUNT)));
			result.setNewContractArea(r.getValue(DSL.sum(a.NEW_CONTRACT_AREA)));
			result.setDenunciationContractCount(denunciationContractCount!=null ? denunciationContractCount.intValue() : null);
			result.setDenunciationContractAmount(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT)));
			result.setDenunciationContractArea(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AREA)));
			result.setChangeContractCount(changeContractCount!=null ? changeContractCount.intValue() : null);
			result.setChangeContractAmount(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AMOUNT)));
			result.setChangeContractArea(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AREA)));
			result.setRenewContractCount(renewContractCount!=null ? renewContractCount.intValue() : null);
			result.setRenewContractAmount(r.getValue(DSL.sum(a.RENEW_CONTRACT_AMOUNT)));
			result.setRenewContractArea(r.getValue(DSL.sum(a.RENEW_CONTRACT_AREA)));
			result.setDepositAmount(r.getValue(DSL.sum(a.DEPOSIT_AMOUNT)));
			
		});
		return result;
	}
	
	@Override
	public List<TotalContractStaticsDTO> listcontractStaticsListTimeDimension(Integer namespaceId, List<Long> communityIds, String formatDateStr, 
			String startTimeStr,String endTimeStr,  Byte dateType, Integer pageOffSet, Integer pageSize){
		
		List<TotalContractStaticsDTO> resultList = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhContractStatisticCommunities a = Tables.EH_CONTRACT_STATISTIC_COMMUNITIES;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(a.DATE_STR,DSL.countDistinct(a.COMMUNITY_ID),DSL.sum(a.RENT_AMOUNT),DSL.sum(a.RENTAL_AREA),DSL.sum(a.CONTRACT_COUNT),DSL.sum(a.CUSTOMER_COUNT),DSL.sum(a.ORG_CONTRACT_COUNT),
				DSL.sum(a.ORG_CONTRACT_AMOUNT),DSL.sum(a.USER_CONTRACT_COUNT),DSL.sum(a.USER_CONTRACT_AMOUNT),
				DSL.sum(a.NEW_CONTRACT_COUNT),DSL.sum(a.NEW_CONTRACT_AMOUNT),DSL.sum(a.NEW_CONTRACT_AREA),
				DSL.sum(a.DENUNCIATION_CONTRACT_COUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AREA),
				DSL.sum(a.CHANGE_CONTRACT_COUNT),DSL.sum(a.CHANGE_CONTRACT_AMOUNT),DSL.sum(a.CHANGE_CONTRACT_AREA),
				DSL.sum(a.RENEW_CONTRACT_COUNT),DSL.sum(a.RENEW_CONTRACT_AMOUNT),DSL.sum(a.RENEW_CONTRACT_AREA),
				DSL.sum(a.DEPOSIT_AMOUNT));
		query.addFrom(a);
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		if (communityIds != null) {
			query.addConditions(a.COMMUNITY_ID.in(communityIds));
		}
		//处理时间问题
		if (dateType != null && dateType == ContractStatisticDateType.YEARMMSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(a.DATE_STR.le(endTimeStr));
				query.addConditions(a.DATE_STR.ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.eq(formatDateStr));
			}
			query.addGroupBy(a.DATE_STR);
		}
		if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(DSL.left(a.DATE_STR, 4).le(endTimeStr));
				query.addConditions(DSL.left(a.DATE_STR, 4).ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.like("%" + formatDateStr + "%"));
			}
			query.addGroupBy(DSL.left(a.DATE_STR, 4));
		}
		query.addConditions(a.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		//query.addGroupBy(a.DATE_STR);
		//query.addGroupBy(a.COMMUNITY_ID);
		
		query.fetch().forEach(r->{
			TotalContractStaticsDTO result = new TotalContractStaticsDTO();
			
			Integer communityCount = r.getValue(DSL.count(a.COMMUNITY_ID));
			BigDecimal contractCount  = r.getValue(DSL.sum(a.CONTRACT_COUNT));
			BigDecimal customerCount  = r.getValue(DSL.sum(a.CUSTOMER_COUNT));
			BigDecimal orgContractCount  = r.getValue(DSL.sum(a.ORG_CONTRACT_COUNT));
			BigDecimal userContractCount  = r.getValue(DSL.sum(a.USER_CONTRACT_COUNT));
			BigDecimal newContractCount  = r.getValue(DSL.sum(a.NEW_CONTRACT_COUNT));
			BigDecimal denunciationContractCount  = r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_COUNT));
			BigDecimal changeContractCount  = r.getValue(DSL.sum(a.CHANGE_CONTRACT_COUNT));
			BigDecimal renewContractCount  = r.getValue(DSL.sum(a.RENEW_CONTRACT_COUNT));
			
			
			if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
				result.setDateStr((r.getValue(a.DATE_STR)).substring(0,4));
			}else {
				result.setDateStr(r.getValue(a.DATE_STR));
			}
			//result.setDateStr(r.getValue(a.DATE_STR));
			result.setCommunityCount(communityCount!=null ? communityCount : null);
			result.setRentAmount(r.getValue(DSL.sum(a.RENT_AMOUNT)));
			result.setRentalArea(r.getValue(DSL.sum(a.RENTAL_AREA)));
			result.setContractCount(contractCount!=null ? contractCount.intValue() : null);
			result.setCustomerCount(customerCount!=null ? customerCount.intValue() : null);
			result.setOrgContractCount(orgContractCount!=null ? orgContractCount.intValue() : null);
			result.setOrgContractAmount(r.getValue(DSL.sum(a.ORG_CONTRACT_AMOUNT)));
			result.setUserContractCount(userContractCount!=null ? userContractCount.intValue() : null);
			result.setUserContractAmount(r.getValue(DSL.sum(a.USER_CONTRACT_AMOUNT)));
			result.setNewContractCount(newContractCount!=null ? newContractCount.intValue() : null);
			result.setNewContractAmount(r.getValue(DSL.sum(a.NEW_CONTRACT_AMOUNT)));
			result.setNewContractArea(r.getValue(DSL.sum(a.NEW_CONTRACT_AREA)));
			result.setDenunciationContractCount(denunciationContractCount!=null ? denunciationContractCount.intValue() : null);
			result.setDenunciationContractAmount(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT)));
			result.setDenunciationContractArea(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AREA)));
			result.setChangeContractCount(changeContractCount!=null ? changeContractCount.intValue() : null);
			result.setChangeContractAmount(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AMOUNT)));
			result.setChangeContractArea(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AREA)));
			result.setRenewContractCount(renewContractCount!=null ? renewContractCount.intValue() : null);
			result.setRenewContractAmount(r.getValue(DSL.sum(a.RENEW_CONTRACT_AMOUNT)));
			result.setRenewContractArea(r.getValue(DSL.sum(a.RENEW_CONTRACT_AREA)));
			result.setDepositAmount(r.getValue(DSL.sum(a.DEPOSIT_AMOUNT)));
			
			resultList.add(result);
		});
		return resultList;
	}
	
	@Override
	public List<TotalContractStaticsDTO> listcontractStaticsListCommunityTotal(Integer namespaceId, List<Long> communityIds, String formatDateStr, 
			String startTimeStr,String endTimeStr,  Byte dateType, Integer pageOffSet, Integer pageSize){
		
		List<TotalContractStaticsDTO> resultList = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhContractStatisticCommunities a = Tables.EH_CONTRACT_STATISTIC_COMMUNITIES;
		EhCommunities b = Tables.EH_COMMUNITIES;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(b.NAME,a.DATE_STR,DSL.count(a.COMMUNITY_ID),DSL.sum(a.RENT_AMOUNT),DSL.sum(a.RENTAL_AREA),DSL.sum(a.CONTRACT_COUNT),DSL.sum(a.CUSTOMER_COUNT),DSL.sum(a.ORG_CONTRACT_COUNT),
				DSL.sum(a.ORG_CONTRACT_AMOUNT),DSL.sum(a.USER_CONTRACT_COUNT),DSL.sum(a.USER_CONTRACT_AMOUNT),
				DSL.sum(a.NEW_CONTRACT_COUNT),DSL.sum(a.NEW_CONTRACT_AMOUNT),DSL.sum(a.NEW_CONTRACT_AREA),
				DSL.sum(a.DENUNCIATION_CONTRACT_COUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AREA),
				DSL.sum(a.CHANGE_CONTRACT_COUNT),DSL.sum(a.CHANGE_CONTRACT_AMOUNT),DSL.sum(a.CHANGE_CONTRACT_AREA),
				DSL.sum(a.RENEW_CONTRACT_COUNT),DSL.sum(a.RENEW_CONTRACT_AMOUNT),DSL.sum(a.RENEW_CONTRACT_AREA),
				DSL.sum(a.DEPOSIT_AMOUNT));
		query.addFrom(a,b);
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		if (communityIds != null) {
			query.addConditions(a.COMMUNITY_ID.in(communityIds));
		}
		query.addConditions(b.ID.eq(a.COMMUNITY_ID));
		//处理时间问题
		if (dateType != null && dateType == ContractStatisticDateType.YEARMMSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(a.DATE_STR.le(endTimeStr));
				query.addConditions(a.DATE_STR.ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.eq(formatDateStr));
			}
			query.addGroupBy(a.DATE_STR);
		}
		if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(DSL.left(a.DATE_STR, 4).le(endTimeStr));
				query.addConditions(DSL.left(a.DATE_STR, 4).ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.like("%" + formatDateStr + "%"));
			}
			query.addGroupBy(DSL.left(a.DATE_STR, 4));
		}
		query.addConditions(a.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		//query.addGroupBy(a.DATE_STR);
		query.addGroupBy(a.COMMUNITY_ID);
		
		query.fetch().forEach(r->{
			TotalContractStaticsDTO result = new TotalContractStaticsDTO();
			
			Integer communityCount = r.getValue(DSL.count(a.COMMUNITY_ID));
			BigDecimal contractCount  = r.getValue(DSL.sum(a.CONTRACT_COUNT));
			BigDecimal customerCount  = r.getValue(DSL.sum(a.CUSTOMER_COUNT));
			BigDecimal orgContractCount  = r.getValue(DSL.sum(a.ORG_CONTRACT_COUNT));
			BigDecimal userContractCount  = r.getValue(DSL.sum(a.USER_CONTRACT_COUNT));
			BigDecimal newContractCount  = r.getValue(DSL.sum(a.NEW_CONTRACT_COUNT));
			BigDecimal denunciationContractCount  = r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_COUNT));
			BigDecimal changeContractCount  = r.getValue(DSL.sum(a.CHANGE_CONTRACT_COUNT));
			BigDecimal renewContractCount  = r.getValue(DSL.sum(a.RENEW_CONTRACT_COUNT));
			
			
			if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
				result.setDateStr((r.getValue(a.DATE_STR)).substring(0,4));
			}else {
				result.setDateStr(r.getValue(a.DATE_STR));
			}
			result.setCommunityName(r.getValue(b.NAME));
			result.setCommunityCount(communityCount!=null ? communityCount : null);
			result.setRentAmount(r.getValue(DSL.sum(a.RENT_AMOUNT)));
			result.setRentalArea(r.getValue(DSL.sum(a.RENTAL_AREA)));
			result.setContractCount(contractCount!=null ? contractCount.intValue() : null);
			result.setCustomerCount(customerCount!=null ? customerCount.intValue() : null);
			result.setOrgContractCount(orgContractCount!=null ? orgContractCount.intValue() : null);
			result.setOrgContractAmount(r.getValue(DSL.sum(a.ORG_CONTRACT_AMOUNT)));
			result.setUserContractCount(userContractCount!=null ? userContractCount.intValue() : null);
			result.setUserContractAmount(r.getValue(DSL.sum(a.USER_CONTRACT_AMOUNT)));
			result.setNewContractCount(newContractCount!=null ? newContractCount.intValue() : null);
			result.setNewContractAmount(r.getValue(DSL.sum(a.NEW_CONTRACT_AMOUNT)));
			result.setNewContractArea(r.getValue(DSL.sum(a.NEW_CONTRACT_AREA)));
			result.setDenunciationContractCount(denunciationContractCount!=null ? denunciationContractCount.intValue() : null);
			result.setDenunciationContractAmount(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT)));
			result.setDenunciationContractArea(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AREA)));
			result.setChangeContractCount(changeContractCount!=null ? changeContractCount.intValue() : null);
			result.setChangeContractAmount(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AMOUNT)));
			result.setChangeContractArea(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AREA)));
			result.setRenewContractCount(renewContractCount!=null ? renewContractCount.intValue() : null);
			result.setRenewContractAmount(r.getValue(DSL.sum(a.RENEW_CONTRACT_AMOUNT)));
			result.setRenewContractArea(r.getValue(DSL.sum(a.RENEW_CONTRACT_AREA)));
			result.setDepositAmount(r.getValue(DSL.sum(a.DEPOSIT_AMOUNT)));
			
			resultList.add(result);
		});
		return resultList;
	}
	
	@Override
	public List<ContractStaticsListDTO> listSearchContractStaticsTimeDimension(Integer namespaceId, List<Long> communityIds, String formatDateStr, 
			String startTimeStr,String endTimeStr,  Byte dateType, Integer pageOffSet, Integer pageSize){
		
		List<ContractStaticsListDTO> resultList = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhContractStatisticCommunities a = Tables.EH_CONTRACT_STATISTIC_COMMUNITIES;
		EhCommunities b = Tables.EH_COMMUNITIES;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(b.NAME,a.DATE_STR,DSL.count(a.COMMUNITY_ID),DSL.sum(a.RENT_AMOUNT),DSL.sum(a.RENTAL_AREA),DSL.sum(a.CONTRACT_COUNT),DSL.sum(a.CUSTOMER_COUNT),DSL.sum(a.ORG_CONTRACT_COUNT),
				DSL.sum(a.ORG_CONTRACT_AMOUNT),DSL.sum(a.USER_CONTRACT_COUNT),DSL.sum(a.USER_CONTRACT_AMOUNT),
				DSL.sum(a.NEW_CONTRACT_COUNT),DSL.sum(a.NEW_CONTRACT_AMOUNT),DSL.sum(a.NEW_CONTRACT_AREA),
				DSL.sum(a.DENUNCIATION_CONTRACT_COUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT),DSL.sum(a.DENUNCIATION_CONTRACT_AREA),
				DSL.sum(a.CHANGE_CONTRACT_COUNT),DSL.sum(a.CHANGE_CONTRACT_AMOUNT),DSL.sum(a.CHANGE_CONTRACT_AREA),
				DSL.sum(a.RENEW_CONTRACT_COUNT),DSL.sum(a.RENEW_CONTRACT_AMOUNT),DSL.sum(a.RENEW_CONTRACT_AREA),
				DSL.sum(a.DEPOSIT_AMOUNT));
		//query.addFrom(a);
		/*query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		if (communityIds != null) {
			query.addConditions(a.COMMUNITY_ID.in(communityIds));
		}
		*/
		query.addFrom(a,b);
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		if (communityIds != null) {
			query.addConditions(a.COMMUNITY_ID.in(communityIds));
		}
		query.addConditions(b.ID.eq(a.COMMUNITY_ID));
		//处理时间问题
		if (dateType != null && dateType == ContractStatisticDateType.YEARMMSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(a.DATE_STR.le(endTimeStr));
				query.addConditions(a.DATE_STR.ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.eq(formatDateStr));
			}
			query.addGroupBy(a.DATE_STR);
			query.addGroupBy(a.COMMUNITY_ID);
		}
		if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
			if (!"".equals(endTimeStr) && !"".equals(startTimeStr) && startTimeStr != null && endTimeStr != null) {
				query.addConditions(DSL.left(a.DATE_STR, 4).le(endTimeStr));
				query.addConditions(DSL.left(a.DATE_STR, 4).ge(startTimeStr));
			}else {
				query.addConditions(a.DATE_STR.like("%" + formatDateStr + "%"));
			}
			query.addGroupBy(DSL.left(a.DATE_STR, 4));
			query.addGroupBy(a.COMMUNITY_ID);
		}
		query.addConditions(a.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		//query.addGroupBy(a.DATE_STR);
		//query.addGroupBy(a.COMMUNITY_ID);
		
		query.fetch().forEach(r->{
			ContractStaticsListDTO result = new ContractStaticsListDTO();
			
			Integer communityCount = r.getValue(DSL.count(a.COMMUNITY_ID));
			BigDecimal contractCount  = r.getValue(DSL.sum(a.CONTRACT_COUNT));
			BigDecimal customerCount  = r.getValue(DSL.sum(a.CUSTOMER_COUNT));
			BigDecimal orgContractCount  = r.getValue(DSL.sum(a.ORG_CONTRACT_COUNT));
			BigDecimal userContractCount  = r.getValue(DSL.sum(a.USER_CONTRACT_COUNT));
			BigDecimal newContractCount  = r.getValue(DSL.sum(a.NEW_CONTRACT_COUNT));
			BigDecimal denunciationContractCount  = r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_COUNT));
			BigDecimal changeContractCount  = r.getValue(DSL.sum(a.CHANGE_CONTRACT_COUNT));
			BigDecimal renewContractCount  = r.getValue(DSL.sum(a.RENEW_CONTRACT_COUNT));
			
			
			if (dateType != null && dateType == ContractStatisticDateType.YEARSTR.getCode()) {
				result.setDateStr((r.getValue(a.DATE_STR)).substring(0,4));
			}else {
				result.setDateStr(r.getValue(a.DATE_STR));
			}
			//result.setDateStr(r.getValue(a.DATE_STR));
			//result.setCommunityCount(communityCount!=null ? communityCount : null);
			result.setCommunityName(r.getValue(b.NAME));
			result.setRentAmount(r.getValue(DSL.sum(a.RENT_AMOUNT)));
			result.setRentalArea(r.getValue(DSL.sum(a.RENTAL_AREA)));
			result.setContractCount(contractCount!=null ? contractCount.intValue() : null);
			result.setCustomerCount(customerCount!=null ? customerCount.intValue() : null);
			result.setOrgContractCount(orgContractCount!=null ? orgContractCount.intValue() : null);
			result.setOrgContractAmount(r.getValue(DSL.sum(a.ORG_CONTRACT_AMOUNT)));
			result.setUserContractCount(userContractCount!=null ? userContractCount.intValue() : null);
			result.setUserContractAmount(r.getValue(DSL.sum(a.USER_CONTRACT_AMOUNT)));
			result.setNewContractCount(newContractCount!=null ? newContractCount.intValue() : null);
			result.setNewContractAmount(r.getValue(DSL.sum(a.NEW_CONTRACT_AMOUNT)));
			result.setNewContractArea(r.getValue(DSL.sum(a.NEW_CONTRACT_AREA)));
			result.setDenunciationContractCount(denunciationContractCount!=null ? denunciationContractCount.intValue() : null);
			result.setDenunciationContractAmount(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AMOUNT)));
			result.setDenunciationContractArea(r.getValue(DSL.sum(a.DENUNCIATION_CONTRACT_AREA)));
			result.setChangeContractCount(changeContractCount!=null ? changeContractCount.intValue() : null);
			result.setChangeContractAmount(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AMOUNT)));
			result.setChangeContractArea(r.getValue(DSL.sum(a.CHANGE_CONTRACT_AREA)));
			result.setRenewContractCount(renewContractCount!=null ? renewContractCount.intValue() : null);
			result.setRenewContractAmount(r.getValue(DSL.sum(a.RENEW_CONTRACT_AMOUNT)));
			result.setRenewContractArea(r.getValue(DSL.sum(a.RENEW_CONTRACT_AREA)));
			result.setDepositAmount(r.getValue(DSL.sum(a.DEPOSIT_AMOUNT)));
			
			resultList.add(result);
		});
		return resultList;
	}
	
	public Timestamp findLastVersionByBackup(Integer namespaceId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_ZJ_SYNCDATA_BACKUP)
				.where(Tables.EH_ZJ_SYNCDATA_BACKUP.NAMESPACE_ID.eq(namespaceId))
				.orderBy(Tables.EH_ZJ_SYNCDATA_BACKUP.CREATE_TIME.desc())
				.limit(1)
				.fetchOne();
		if (record != null) {
			return record.getValue(Tables.EH_ZJ_SYNCDATA_BACKUP.CREATE_TIME);
		}
		return null;
	}
}
