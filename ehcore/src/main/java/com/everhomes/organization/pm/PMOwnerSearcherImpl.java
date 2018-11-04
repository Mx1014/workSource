package com.everhomes.organization.pm;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.ListOrganizationOwnersResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressDTO;
import com.everhomes.rest.organization.pm.SearchOrganizationOwnersCommand;
import com.everhomes.rest.user.UserLocalStringCode;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.PMOwnerSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerAddress;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PMOwnerSearcherImpl extends AbstractElasticSearch implements PMOwnerSearcher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PMOwnerSearcherImpl.class);
	
	@Autowired
	private PropertyMgrProvider propertyMgrProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;

    @Autowired
    private LocaleStringProvider localeStringProvider;

    @Autowired
    private AddressService addressService;
    
    @Autowired
	private AddressProvider addressProvider;

    @Autowired
    private CommunityProvider communityProvider;

	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<CommunityPmOwner> owners) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (CommunityPmOwner owner : owners) {
            XContentBuilder source = createDoc(owner);
            if(null != source) {
                LOGGER.info("id:" + owner.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(owner.getId().toString()).source(source));    
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(CommunityPmOwner owner) {
		XContentBuilder source = createDoc(owner);
        feedDoc(owner.getId().toString(), source);
	}

    @Override
    public void syncFromDb() {
        this.deleteAll();
        Integer pageSize = 200;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for (; ; ) {
            List<CommunityPmOwner> owners = propertyMgrProvider.listCommunityPmOwnersWithLocator(locator, pageSize);
            if (owners.size() > 0) {
                this.bulkUpdate(owners);
            }
            if (locator.getAnchor() == null) {
                break;
            }
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for organization owner ok");
    }

	@Override
	public ListOrganizationOwnersResponse query(SearchOrganizationOwnersCommand cmd) {
        ListOrganizationOwnersResponse response = new ListOrganizationOwnersResponse();
        if (cmd.getPageSize() != null) {
            List<OrganizationOwnerDTO> ownerDTOList = new ArrayList<>();
            Long anchor = buildOrganizationOwnerDTOList(cmd, ownerDTOList);
            response.setNextPageAnchor(anchor);
            response.setOwners(ownerDTOList);
        }
        return response;
    }

    private Long getAnchor(SearchOrganizationOwnersCommand cmd) {
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        return anchor;
    }

    private List<Long> getMatchOwnerIds(SearchOrganizationOwnersCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("contactToken", 5.0f)
                    .field("contactName", 2.0f)
                    .field("contactExtraTels", 2.0f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("contactToken")
					.addHighlightedField("contactName");
        }

        FilterBuilder fb = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        if (cmd.getOrgOwnerTypeId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("orgOwnerTypeId", cmd.getOrgOwnerTypeId()));
        }
        // this way depends on  analyzing ,you can use array instead
        if (cmd.getBuildingId() != null) {
            MultiMatchQueryBuilder operatorNameQuery = QueryBuilders.multiMatchQuery(cmd.getBuildingId(), "buildingId");
            qb = QueryBuilders.boolQuery().must(qb).must(operatorNameQuery);
        }
        if (cmd.getAddressId() != null) {
            MultiMatchQueryBuilder operatorNameQuery = QueryBuilders.multiMatchQuery(cmd.getAddressId(), "addressId");
            qb = QueryBuilders.boolQuery().must(qb).must(operatorNameQuery);
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = getAnchor(cmd);

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(anchor.intValue())
                .setSize(pageSize + 1)
                .setQuery(qb);
        builder.addSort("createTime", SortOrder.DESC);

        SearchResponse rsp = builder.execute().actionGet();
        return getIds(rsp);
    }

    private Long buildOrganizationOwnerDTOList(SearchOrganizationOwnersCommand cmd, List<OrganizationOwnerDTO> ownerDTOList) {
        List<Long> ids = getMatchOwnerIds(cmd);
        Long anchor = 0L;
        if (ids != null && ids.size() > cmd.getPageSize()) {
            ids.remove(ids.size() - 1);
            anchor = anchor + ids.size();
        } else {
            anchor = null;
        }
        if (ids != null && ids.size() > 0) {
            List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwners(ids);
            ownerDTOList.addAll(pmOwners.stream().map(r -> {
                OrganizationOwnerDTO dto = ConvertHelper.convert(r, OrganizationOwnerDTO.class);
                OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(r.getOrgOwnerTypeId());
                dto.setOrgOwnerType(ownerType == null ? "" : ownerType.getDisplayName());
                LocaleString genderLocale = localeStringProvider.find(UserLocalStringCode.SCOPE, String.valueOf(r.getGender()),
                        UserContext.current().getUser().getLocale());
                dto.setGender(genderLocale != null ? genderLocale.getText() : "");
                
                dto.setBirthday(null!=r.getBirthday()?r.getBirthday().getTime():null);
                //添加门牌
                List<OrganizationOwnerAddress> addresses = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(r.getNamespaceId(), r.getId());
                dto.setAddresses(addresses.stream().map(r2 -> {
                	OrganizationOwnerAddressDTO d = ConvertHelper.convert(r2, OrganizationOwnerAddressDTO.class);
                	Address address = addressProvider.findAddressById(r2.getAddressId());
                    if(address != null) {
                        d.setAddressId(address.getId());
                        d.setAddress(address.getAddress());
                        d.setApartment(address.getApartmentName());
                        d.setBuilding(address.getBuildingName());
                    }
                	return d;
                }).collect(Collectors.toList()));
                //客户额外手机号添加
                List<String> contractExtraTels = new ArrayList<>();
                try{
                    List<String> extraTelsField = (List<String>)StringHelper.fromJsonString(r.getContactExtraTels(), ArrayList.class);
                    contractExtraTels.addAll(extraTelsField);
                }catch (Exception e){
                    LOGGER.error("failed to convert contact extra tels json to list, pmowner id ={}", r.getId());
                }
                //add by tangcen
                if (!contractExtraTels.contains(dto.getContactToken())) {
                	contractExtraTels.add(dto.getContactToken());
				}
                dto.setCustomerExtraTels(contractExtraTels);
                return dto;
            }).collect(Collectors.toList()));
        }

        return anchor;
    }

    private List<CommunityPmOwner> processCommunityPmOwners(List<CommunityPmOwner> pmOwners, List<Long> addressIds) {
        List<OrganizationOwnerAddress> ownerAddresses = propertyMgrProvider.listOrganizationOwnerAddressByAddressIds(UserContext.getCurrentNamespaceId(), addressIds);
        List<Long> organizationOwnerIds = ownerAddresses.stream().map(OrganizationOwnerAddress::getOrganizationOwnerId).distinct().collect(Collectors.toList());
        pmOwners = pmOwners.stream().filter(r -> organizationOwnerIds.contains(r.getId())).collect(Collectors.toList());
        return pmOwners;
    }

    @Override
	public String getIndexType() {
		return SearchUtils.PMOWNERINDEXTYPE;
	}
	
	private XContentBuilder createDoc(CommunityPmOwner owner){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            //将ContactExtraTels字段加入到es索引当中
            if (!org.springframework.util.StringUtils.isEmpty(owner.getContactExtraTels())) {
            	String contactExtraTels = owner.getContactExtraTels();
                List<String> contactExtraTelsList = (List<String>)StringHelper.fromJsonString(contactExtraTels, ArrayList.class);
                b.field("contactExtraTels", contactExtraTelsList.toString());  
			}

            b.field("createTime", owner.getCreateTime());
            b.field("id", owner.getId());
            b.field("contactToken", owner.getContactToken());
            b.field("contactName", owner.getContactName());
//            b.field("communityId", owner.getCommunityId());
            //一个个人用户可能会有多个地址 所以communityId字段变成用逗号分隔的id列表 by xiongying20170829
            //如果是同一个field下多个值，则全部加入到同一个field下
            if(owner.getCommunityId() != null) {
                String[] communities = owner.getCommunityId().split(",");
                if(null != communities && communities.length > 0) {
                    b.array("communityId", communities);
                }
            }
            List<OrganizationOwnerAddress> addresses = propertyMgrProvider.listOrganizationOwnerAddressByOwnerId(owner.getNamespaceId(), owner.getId());

            if(addresses!=null && addresses.size()>0){
                List<Long> addessList = addresses.stream().map(EhOrganizationOwnerAddress::getAddressId).collect(Collectors.toList());
                List<Long> buildingId = addresses.stream().map((a)->{
                   Address address =  addressProvider.findAddressById(a.getAddressId());
                   if(address!=null){
                       Building building =  communityProvider.findBuildingByCommunityIdAndName(address.getCommunityId(), address.getBuildingName());
                       if(building!=null){
                           return building.getId();
                       }
                   }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                b.field("addressId", StringUtils.join(addessList, "|"));
                b.field("buildingId", StringUtils.join(buildingId, "|"));
            }

            b.field("orgOwnerTypeId", owner.getOrgOwnerTypeId());

            b.endObject();
            return b;
		} catch (IOException ex) {
			LOGGER.error("Create owner " + owner.getId() + " error");
			return null;
		}
	}
}
