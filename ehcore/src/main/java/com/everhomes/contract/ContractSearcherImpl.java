package com.everhomes.contract;

import com.everhomes.asset.bill.AssetBillService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.customer.IndividualCustomerProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractApplicationScene;
import com.everhomes.rest.contract.ContractCategoryCommand;
import com.everhomes.rest.contract.ContractCategoryListDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.contract.ContractReportFormListContractsCommand;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.ListContractsResponse;
import com.everhomes.rest.contract.OpenapiListContractsCommand;
import com.everhomes.rest.contract.SearchContractCommand;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeFieldItem;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/17.
 */
@Component
public class ContractSearcherImpl extends AbstractElasticSearch implements ContractSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private IndividualCustomerProvider individualCustomerProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private PortalService portalService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
	protected UserProvider userProvider;
    
    @Autowired
	private ConfigurationProvider configurationProvider;
    
    @Autowired
	private AssetBillService assetBillService;

    @Override
    public String getIndexType() {
        return SearchUtils.CONTRACT;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<Contract> contracts) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Contract contract : contracts) {
            XContentBuilder source = createDoc(contract);
            if(null != source) {
                LOGGER.info("id:" + contract.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(contract.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(Contract contract) {
        XContentBuilder source = createDoc(contract);
        feedDoc(contract.getId().toString(), source);
    }

    private XContentBuilder createDoc(Contract contract) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();

            builder.field("id", contract.getId());
            builder.field("communityId", contract.getCommunityId());
            builder.field("namespaceId", contract.getNamespaceId());
            builder.field("name", contract.getName());
            builder.field("contractNumber", contract.getContractNumber());
            builder.field("customerName", contract.getCustomerName());
            builder.field("status", contract.getStatus());
            builder.field("contractType", contract.getContractType());
            builder.field("categoryItemId", contract.getCategoryItemId());
            builder.field("contractStartDate", contract.getContractStartDate());
            builder.field("contractEndDate", contract.getContractEndDate());
            builder.field("customerType", contract.getCustomerType());
            builder.field("partyAId", contract.getPartyAId());
            //builder.field("depositStatus", contract.getDepositStatus());
            if(contract.getPaymentFlag() == null){
                builder.field("paymentFlag", 0);
            }else{
                builder.field("paymentFlag", contract.getPaymentFlag());
            }
            builder.field("categoryId", contract.getCategoryId());
            
            if(contract.getRent() != null) {
                builder.field("rent", contract.getRent());
            } else {
                builder.field("rent", "");
            }

            if(contract.getUpdateTime() != null) {
                builder.field("updateTime", contract.getUpdateTime());
            } else {
                builder.field("updateTime", contract.getCreateTime());
            }

            List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(contract.getId());
            if(contractApartments != null && contractApartments.size() > 0) {
                List<Long> addresses = new ArrayList<>();
                List<Long> buildings = new ArrayList<>();
                for (ContractBuildingMapping contractApartment : contractApartments) {
                    try{
                        addresses.add(contractApartment.getAddressId());
                        Building building = communityProvider.findBuildingByCommunityIdAndName(contract.getCommunityId(), contractApartment.getBuildingName());
                        if (building != null) {
                            buildings.add(building.getId());
                        }
                    }catch (Exception e){
                        LOGGER.error("error occured during sync contract",e);
                        LOGGER.error("find building failed, contractApartment = {}, contract id ={}", contractApartment, contract.getId());
                        continue;
                    }
                }

                builder.field("buildingId", buildings);
                builder.field("addressId", addresses);
            } else {
                builder.field("buildingId", 0);
                builder.field("addressId", 0);
            }

            builder.endObject();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<Contract> contracts = contractProvider.listContracts(locator, pageSize);

            if(contracts.size() > 0) {
                this.bulkUpdate(contracts);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for contracts ok");
    }

    private void checkContractAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId, Byte paymentFlag) {
        ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
        cmd.setNamespaceId(namespaceId);
        //区分开付款合同和收款合同的moduleid
        if(paymentFlag == 1) {
        	//付款合同
        	cmd.setModuleId(ServiceModuleConstants.PAYMENT_CONTRACT_MODULE);
        } else {
        	//收款合同
        	cmd.setModuleId(ServiceModuleConstants.CONTRACT_MODULE);
        }
        cmd.setActionType(ActionType.OFFICIAL_URL.getCode());
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(cmd);
        Long appId = apps.getServiceModuleApps().get(0).getOriginId();
        if(!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), orgId,
                orgId, privilegeId, appId, null, communityId)) {
            LOGGER.error("Permission is prohibited, namespaceId={}, orgId={}, ownerType={}, ownerId={}, privilegeId={}",
                    namespaceId, orgId, EntityType.COMMUNITY.getCode(), communityId, privilegeId);
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check user privilege error");
        }
    }

    @Override
    public ListContractsResponse queryContracts(SearchContractCommand cmd) {
    	if (cmd.getOrgId() == null || cmd.getCommunityId() == null) {
    		throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
                    "OrgIdorCommunityId user privilege error");
		}
        if(cmd.getPaymentFlag() == 1) {
            checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_LIST, cmd.getOrgId(), cmd.getCommunityId(), cmd.getPaymentFlag());
        } else {
            checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_LIST, cmd.getOrgId(), cmd.getCommunityId(), cmd.getPaymentFlag());
        }

        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
//            qb = QueryBuilders.multiMatchQuery(cmd.getKeywords())
//                    .field("name", 1.2f)
//                    .field("customerName", 1.2f)
//                    .field("contractNumber", 1.2f);
        	String pattern = "*" + cmd.getKeywords() + "*";
            qb = QueryBuilders.boolQuery()
            					.should(QueryBuilders.wildcardQuery("name", pattern))
            					.should(QueryBuilders.wildcardQuery("customerName", pattern))
            					.should(QueryBuilders.wildcardQuery("contractNumber", pattern));

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name").addHighlightedField("customerName").addHighlightedField("contractNumber");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", ContractStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("paymentFlag", cmd.getPaymentFlag()));

        if(cmd.getStatus() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));

        if(cmd.getCategoryItemId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryItemId", cmd.getCategoryItemId()));

        if(cmd.getContractType() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("contractType", cmd.getContractType()));

        if(cmd.getCustomerType() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("customerType", cmd.getCustomerType()));
        }

        if(cmd.getBuildingId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("buildingId", cmd.getBuildingId()));
        }

        if(cmd.getAddressId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("addressId", cmd.getAddressId()));
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        Long pageNumber = 1l;
        //cmd.setPageNumber(pageNumber);
        
        //传入的变为页码 pageNumber
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        if(cmd.getPageNumber() != null) {
        	pageNumber = cmd.getPageNumber();
        }
        
        if(cmd.getCategoryId() != null) {
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryId", cmd.getCategoryId()));
        }
        
        /*合同跟着园区，不跟着管理公司
         * if(cmd.getOrgId() != null) {
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("partyAId", cmd.getOrgId()));
        }*/
        if(cmd.getDepositStatus() != null) {
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("depositStatus", cmd.getDepositStatus()));
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        //builder.setFrom((pageNumber.intValue()-1) * pageSize).setSize(pageSize + 1);
        builder.setFrom((pageNumber.intValue()-1) * pageSize).setSize(pageSize);
        
        builder.setQuery(qb);
        if(cmd.getSortField() != null && cmd.getSortType() != null) {
            if(cmd.getSortType() == 0) {
                builder.addSort(cmd.getSortField(), SortOrder.ASC);
            } else if(cmd.getSortType() == 1) {
                builder.addSort(cmd.getSortField(), SortOrder.DESC);
            }
        } else {
            builder.addSort("updateTime", SortOrder.DESC);
        }
        SearchResponse rsp = builder.execute().actionGet();
        Long totalHits = rsp.getHits().getTotalHits();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("ContractSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        ListContractsResponse response = new ListContractsResponse();

        response.setTotalNum(totalHits);
        /*if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }*/

        List<ContractDTO> dtos = new ArrayList<ContractDTO>();
        Map<Long, Contract> contracts = contractProvider.listContractsByIds(ids);
        if(contracts != null && contracts.size() > 0) {
            //把取出来的列表顺序和搜索引擎中得到的ids的顺序不一定一样 以搜索引擎的为准 by xiongying 20170907
            ids.forEach(id -> {
                Contract contract = contracts.get(id);
                ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
                if(contract.getCustomerType() != null && CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                    EnterpriseCustomer customer = enterpriseCustomerProvider.findById(contract.getCustomerId());
                    if(customer != null) {
                        dto.setCustomerName(customer.getName());
                    }
                } else if(contract.getCustomerType() != null && CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                    OrganizationOwner owner = individualCustomerProvider.findOrganizationOwnerById(contract.getCustomerId());
                    if(owner != null) {
                        dto.setCustomerName(owner.getContactName());
                    }

                }
                if(contract.getPartyAId() != null && contract.getPartyAType() != null) {
                    if(0 == contract.getPartyAType()) {
                        Organization organization = organizationProvider.findOrganizationById(contract.getPartyAId());
                        if(organization != null) {
                            dto.setPartyAName(organization.getName());
                        }
                    }

                }
                if(contract.getCategoryItemId() != null) {
                    ScopeFieldItem item =  fieldProvider.findScopeFieldItemByFieldItemId(contract.getNamespaceId(),cmd.getOrgId(), contract.getCommunityId(), contract.getCategoryItemId());
                    if(item != null) {
                        dto.setCategoryItemName(item.getItemDisplayName());
                    }
                }
                
				if (contract.getSponsorUid() != null) {
					// 用户可能不在组织架构中 所以用nickname,//由于瑞安传过来的是名字,没有办法获取id，所以对于对接的发起人直接存名字
					if (cmd.getNamespaceId() == 999929) {
						dto.setSponsorName(contract.getSponsorUid());
					} else {
						User user = userProvider.findUserById(Long.parseLong(contract.getSponsorUid()));
						if (user != null) {
							dto.setSponsorName(user.getNickName());
						}
					}
				}
                
                processContractApartments(dto);
                //查询合同适用场景，物业合同不修改资产状态。
		        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
		        if (contractCategory != null) {
		        	dto.setContractApplicationScene(contractCategory.getContractApplicationScene());
				}
		        /*if (contract.getDepositStatus() != null) {
					dto.setDepositStatus(contract.getDepositStatus());
				}*/
		        
                dtos.add(dto);
            });
        }
        response.setContracts(dtos);
        return response;
    }

    private void processContractApartments(ContractDTO dto) {
        List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(dto.getId());
        if(contractApartments != null && contractApartments.size() > 0) {
            List<BuildingApartmentDTO> apartmentDtos = contractApartments.stream().map(apartment -> {
                BuildingApartmentDTO apartmentDto = ConvertHelper.convert(apartment, BuildingApartmentDTO.class);
                apartmentDto.setChargeArea(apartment.getAreaSize());
                return apartmentDto;
            }).collect(Collectors.toList());
            dto.setBuildings(apartmentDtos);
        }
    }
    
	@Override
	public ListContractsResponse openapiListContracts(OpenapiListContractsCommand cmd) {
		// 合同跟着园区走
		if (cmd.getCommunityId() == null) {
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE,
					ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY, "openapiListContracts CommunityId is null");
		}
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
		if (cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
			qb = QueryBuilders.matchAllQuery();
		} else {
			String pattern = "*" + cmd.getKeywords() + "*";
			qb = QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery("name", pattern))
					.should(QueryBuilders.wildcardQuery("customerName", pattern))
					.should(QueryBuilders.wildcardQuery("contractNumber", pattern));

			builder.setHighlighterFragmentSize(60);
			builder.setHighlighterNumOfFragments(8);
			builder.addHighlightedField("name").addHighlightedField("customerName")
					.addHighlightedField("contractNumber");
		}

		FilterBuilder fb = null;
		FilterBuilder nfb = FilterBuilders.termFilter("status", ContractStatus.INACTIVE.getCode());
		fb = FilterBuilders.notFilter(nfb);
		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("paymentFlag", cmd.getPaymentFlag()));
		
		if (cmd.getCommunityId() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

		if (cmd.getStatus() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));

		if (cmd.getContractType() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("contractType", cmd.getContractType()));

		if (cmd.getCustomerType() != null) {
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("customerType", cmd.getCustomerType()));
		}

		if (cmd.getBuildingId() != null) {
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("buildingId", cmd.getBuildingId()));
		}

		if (cmd.getAddressId() != null) {
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("addressId", cmd.getAddressId()));
		}
		// 根据域空间查询应用 只传租赁合同
		ContractCategoryCommand contractCategoryCommand = new ContractCategoryCommand();
		contractCategoryCommand.setNamespaceId(cmd.getNamespaceId());
		ContractService contractService = getContractService(cmd.getNamespaceId());
		List<ContractCategoryListDTO> categoryList = contractService.getContractCategoryList(contractCategoryCommand);
		List<Long> rentalCategoryList = new ArrayList<Long>();
		for (ContractCategoryListDTO contractCategory : categoryList) {
			if (contractCategory.getContractApplicationScene() != ContractApplicationScene.PROPERTY.getCode()) {
				//qb = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("categoryId", contractCategory.getCategoryId()));
				rentalCategoryList.add(contractCategory.getCategoryId());
			}
		}
		if (rentalCategoryList.size() > 0) {
			qb = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("categoryId", rentalCategoryList));
		}
		
		// 传过来的时间进行格式化时间戳转化
		if (cmd.getUpdateTime() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = sdf.format(cmd.getUpdateTime());
			Date dateUpdateTime = null;
			try {
				dateUpdateTime = sdf.parse(str);
			} catch (ParseException e) {
				LOGGER.info("ContractSearcherImpl openapiListContracts SimpleDateFormat  is error");
			}
			Date date = null;
			try {
				date = sdf.parse(str);
				System.out.println(date);
			} catch (ParseException e) {
				LOGGER.info("ContractSearcherImpl openapiListContracts SimpleDateFormat  is error");
			}
			if (dateUpdateTime != null) {
				qb = QueryBuilders.boolQuery()
						.must(QueryBuilders.rangeQuery("updateTime").from(dateUpdateTime).to(new Date()));
			}
		}
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Long anchor = 0l;
		Long pageNumber = 1l;

		// 传入的变为页码 pageNumber
		if (cmd.getPageAnchor() != null) {
			anchor = cmd.getPageAnchor();
		}

		if (cmd.getPageNumber() != null) {
			pageNumber = cmd.getPageNumber();
		}

		qb = QueryBuilders.filteredQuery(qb, fb);
		builder.setSearchType(SearchType.QUERY_THEN_FETCH);
		builder.setFrom((pageNumber.intValue() - 1) * pageSize).setSize(pageSize + 1);
		builder.setQuery(qb);
		builder.addSort("updateTime", SortOrder.DESC);

		SearchResponse rsp = builder.execute().actionGet();
		Long totalHits = rsp.getHits().getTotalHits();

		if (LOGGER.isDebugEnabled())
			LOGGER.info("ContractSearcherImpl query builder: {}, rsp: {}", builder, rsp);

		List<Long> ids = getIds(rsp);
		ListContractsResponse response = new ListContractsResponse();

		response.setTotalNum(totalHits);

		if (ids.size() > pageSize) {
			response.setNextPageAnchor(anchor + 1);
			ids.remove(ids.size() - 1);
		}

		List<ContractDTO> dtos = new ArrayList<ContractDTO>();
		Map<Long, Contract> contracts = contractProvider.listContractsByIds(ids);
		if (contracts != null && contracts.size() > 0) {
			ids.forEach(id -> {
				Contract contract = contracts.get(id);
				ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
				if (contract.getCustomerType() != null
						&& CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
					EnterpriseCustomer customer = enterpriseCustomerProvider.findById(contract.getCustomerId());
					if (customer != null) {
						dto.setCustomerName(customer.getName());
					}
				} else if (contract.getCustomerType() != null
						&& CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
					OrganizationOwner owner = individualCustomerProvider
							.findOrganizationOwnerById(contract.getCustomerId());
					if (owner != null) {
						dto.setCustomerName(owner.getContactName());
					}

				}
				if (contract.getPartyAId() != null && contract.getPartyAType() != null) {
					if (0 == contract.getPartyAType()) {
						Organization organization = organizationProvider.findOrganizationById(contract.getPartyAId());
						if (organization != null) {
							dto.setPartyAName(organization.getName());
						}
					}

				}
				if (contract.getSponsorUid() != null) {
					if (cmd.getNamespaceId() == 999929) {
						dto.setSponsorName(contract.getSponsorUid());
					} else {
						User user = userProvider.findUserById(Long.parseLong(contract.getSponsorUid()));
						if (user != null) {
							dto.setSponsorName(user.getNickName());
						}
					}
				}
				processContractApartments(dto);
				// 查询合同适用场景，物业合同不修改资产状态。
				ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
				if (contractCategory != null) {
					dto.setContractApplicationScene(contractCategory.getContractApplicationScene());
				}
				dtos.add(dto);
			});
		}
		response.setContracts(dtos);
		return response;
	}
	
	private ContractService getContractService(Integer namespaceId) {
		String handler = configurationProvider.getValue(namespaceId, "contractService", "");
		return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
	}
	
	
	//合同报表查询 查询所有的合同 正常合同
	@Override
	public ListContractsResponse contractReportFormListContracts(ContractReportFormListContractsCommand cmd) {
		
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
		if (cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
			qb = QueryBuilders.matchAllQuery();
		} else {
			String pattern = "*" + cmd.getKeywords() + "*";
			qb = QueryBuilders.boolQuery().should(QueryBuilders.wildcardQuery("name", pattern))
					.should(QueryBuilders.wildcardQuery("customerName", pattern))
					.should(QueryBuilders.wildcardQuery("contractNumber", pattern));

			builder.setHighlighterFragmentSize(60);
			builder.setHighlighterNumOfFragments(8);
			builder.addHighlightedField("name").addHighlightedField("customerName")
					.addHighlightedField("contractNumber");
		}

		FilterBuilder fb = null;
		FilterBuilder nfb = FilterBuilders.termFilter("status", ContractStatus.INACTIVE.getCode());
		fb = FilterBuilders.notFilter(nfb);
		
		if (cmd.getNamespaceId() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
		
		//查询租赁合同
		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("paymentFlag", ContractStatus.INACTIVE.getCode()));
		
		if (cmd.getCommunityId() != null)
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

		//统计正常合同，退约合同
		List<Byte> statusList = new ArrayList<Byte>();
		statusList.add(ContractStatus.ACTIVE.getCode());
		statusList.add(ContractStatus.DENUNCIATION.getCode());
		if (statusList != null) {
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("status", statusList));
		}
		if (cmd.getContractType() != null){
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("contractType", cmd.getContractType()));
		}
		if (cmd.getCustomerType() != null) {
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("customerType", cmd.getCustomerType()));
		}
		if (cmd.getBuildingId() != null) {
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("buildingId", cmd.getBuildingId()));
		}
		if (cmd.getAddressId() != null) {
			fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("addressId", cmd.getAddressId()));
		}
		
		
		// 传过来的时间进行格式化时间戳转化
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置本月第一天
		Calendar firstCa = Calendar.getInstance();
		firstCa.add(Calendar.MONTH, 0);
		firstCa.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		firstCa.set(Calendar.HOUR_OF_DAY, 0);
		firstCa.set(Calendar.MINUTE, 0);
		firstCa.set(Calendar.SECOND, 0);
		firstCa.set(Calendar.MILLISECOND, 0);
		String firststr = sdf.format(firstCa.getTime());
		
		//设置本月最后
		Calendar lastCa = Calendar.getInstance();
		lastCa.set(Calendar.DAY_OF_MONTH, lastCa.getActualMaximum(Calendar.DAY_OF_MONTH));
		lastCa.set(Calendar.HOUR_OF_DAY, 23);
		lastCa.set(Calendar.MINUTE, 59);
		lastCa.set(Calendar.SECOND, 59);
		String laststr = sdf.format(lastCa.getTime());*/
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfMM = new SimpleDateFormat("yyyy-MM");
		//设置本月第一天
		Calendar firstCa = Calendar.getInstance();
		// 设置本月最后
		Calendar lastCa = Calendar.getInstance();
		
		Date userDate = null;
		try {
			userDate = sdfMM.parse(cmd.getDateStr());
		} catch (ParseException e1) {

		}
		firstCa.setTime(userDate);
		firstCa.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		// 设置本月第一天
		//Calendar firstCa = Calendar.getInstance();
		/*firstCa.add(Calendar.MONTH, 0);
		firstCa.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		firstCa.set(Calendar.HOUR_OF_DAY, 0);
		firstCa.set(Calendar.MINUTE, 0);
		firstCa.set(Calendar.SECOND, 0);
		firstCa.set(Calendar.MILLISECOND, 0);*/
		String firststr = sdf.format(firstCa.getTime());

		// 设置本月最后
		//Calendar lastCa = Calendar.getInstance();
		lastCa.setTime(userDate);
		lastCa.set(Calendar.DAY_OF_MONTH, lastCa.getActualMaximum(Calendar.DAY_OF_MONTH));
		lastCa.set(Calendar.HOUR_OF_DAY, 23);
		lastCa.set(Calendar.MINUTE, 59);
		lastCa.set(Calendar.SECOND, 59);
		String laststr = sdf.format(lastCa.getTime());
		
		Date firstdateUpdateTime = null;
		Date lastdateUpdateTime = null;
		try {
			firstdateUpdateTime = sdf.parse(firststr);
			lastdateUpdateTime = sdf.parse(laststr);
		} catch (ParseException e) {
			LOGGER.info("ContractSearcherImpl openapiListContracts SimpleDateFormat  is error");
		}
		qb = QueryBuilders.boolQuery()
					.must(QueryBuilders.rangeQuery("updateTime").from(firstdateUpdateTime).to(lastdateUpdateTime));
		
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Long anchor = 0l;
		Long pageNumber = 1l;

		// 传入的变为页码 pageNumber
		if (cmd.getPageAnchor() != null) {
			anchor = cmd.getPageAnchor();
		}

		if (cmd.getPageNumber() != null) {
			pageNumber = cmd.getPageNumber();
		}

		qb = QueryBuilders.filteredQuery(qb, fb);
		builder.setSearchType(SearchType.QUERY_THEN_FETCH);
		builder.setFrom((pageNumber.intValue() - 1) * pageSize).setSize(pageSize + 1);
		builder.setQuery(qb);
		builder.addSort("communityId", SortOrder.DESC);

		SearchResponse rsp = builder.execute().actionGet();
		Long totalHits = rsp.getHits().getTotalHits();

		if (LOGGER.isDebugEnabled())
			LOGGER.info("ContractSearcherImpl query builder: {}, rsp: {}", builder, rsp);

		List<Long> ids = getIds(rsp);
		ListContractsResponse response = new ListContractsResponse();

		response.setTotalNum(totalHits);

		if (ids.size() > pageSize) {
			response.setNextPageAnchor(anchor + 1);
			ids.remove(ids.size() - 1);
		}

		List<ContractDTO> dtos = new ArrayList<ContractDTO>();
		Map<Long, Contract> contracts = contractProvider.listContractsByIds(ids);
		if (contracts != null && contracts.size() > 0) {
			ids.forEach(id -> {
				Contract contract = contracts.get(id);
				ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
				if (contract.getCustomerType() != null
						&& CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
					EnterpriseCustomer customer = enterpriseCustomerProvider.findById(contract.getCustomerId());
					if (customer != null) {
						dto.setCustomerName(customer.getName());
					}
				} else if (contract.getCustomerType() != null
						&& CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
					OrganizationOwner owner = individualCustomerProvider
							.findOrganizationOwnerById(contract.getCustomerId());
					if (owner != null) {
						dto.setCustomerName(owner.getContactName());
					}

				}
				if (contract.getPartyAId() != null && contract.getPartyAType() != null) {
					if (0 == contract.getPartyAType()) {
						Organization organization = organizationProvider.findOrganizationById(contract.getPartyAId());
						if (organization != null) {
							dto.setPartyAName(organization.getName());
						}
					}

				}
				if (contract.getSponsorUid() != null) {
					if (contract.getNamespaceId() == 999929) {
						dto.setSponsorName(contract.getSponsorUid());
					} else {
						User user = userProvider.findUserById(Long.parseLong(contract.getSponsorUid()));
						if (user != null) {
							dto.setSponsorName(user.getNickName());
						}
					}
				}
				processContractApartments(dto);
				// 查询合同适用场景，物业合同不修改资产状态。
				ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
				if (contractCategory != null) {
					dto.setContractApplicationScene(contractCategory.getContractApplicationScene());
				}
				dtos.add(dto);
			});
		}
		response.setContracts(dtos);
		return response;
	}
}
