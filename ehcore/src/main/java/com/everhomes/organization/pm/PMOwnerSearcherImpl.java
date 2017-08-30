package com.everhomes.organization.pm;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommand;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.ListOrganizationOwnersResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressDTO;
import com.everhomes.rest.organization.pm.SearchOrganizationOwnersCommand;
import com.everhomes.rest.user.UserLocalStringCode;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.PMOwnerSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;

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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        List<CommunityPmOwner> owners = propertyMgrProvider.listCommunityPmOwners(null, null);
        if(owners.size() > 0) {
            this.bulkUpdate(owners);
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

            if (ownerDTOList.size() - cmd.getPageSize() > 1) {
                response.setNextPageAnchor(anchor + cmd.getPageSize() - (ownerDTOList.size() - cmd.getPageSize()));
            } else if (ownerDTOList.size() - cmd.getPageSize() == 1) {
                response.setNextPageAnchor(anchor + cmd.getPageSize());
            } else if (ownerDTOList.size() == cmd.getPageSize()){
                response.setNextPageAnchor(anchor + cmd.getPageSize());
            }
            ownerDTOList = ownerDTOList.stream().limit(cmd.getPageSize()).collect(Collectors.toList());
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
                    .field("contactName", 2.0f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("contactToken")
					.addHighlightedField("contactName");
        }

        FilterBuilder fb = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        if (cmd.getOrgOwnerTypeId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("orgOwnerTypeId", cmd.getOrgOwnerTypeId()));
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = getAnchor(cmd);

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(anchor.intValue())
                .setSize(pageSize + 1)
                .setQuery(qb);

        SearchResponse rsp = builder.execute().actionGet();
        return getIds(rsp);
    }

    private Long buildOrganizationOwnerDTOList(SearchOrganizationOwnersCommand cmd, List<OrganizationOwnerDTO> ownerDTOList) {
        List<Long> ids = getMatchOwnerIds(cmd);
        if (ids.size() > 0) {
            List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwners(ids);
            // 如果有楼栋地址等信息的话, 过滤不符合条件的对象
            List<Long> addressIds = new ArrayList<>();
            if (cmd.getAddressId() != null) {
                addressIds.add(cmd.getAddressId());
                pmOwners = processCommunityPmOwners(pmOwners, addressIds);
            } else if (cmd.getBuildingName() != null && !cmd.getBuildingName().isEmpty()) {
            	//添加门牌地址的过滤
            	if(StringUtils.isNotBlank(cmd.getApartmentName())) {
            		ListPropApartmentsByKeywordCommand listPropApartmentsByKeywordCommand = new ListPropApartmentsByKeywordCommand();
                    listPropApartmentsByKeywordCommand.setCommunityId(cmd.getCommunityId());
                    listPropApartmentsByKeywordCommand.setOrganizationId(cmd.getOrganizationId());
                    listPropApartmentsByKeywordCommand.setNamespaceId(UserContext.current().getUser().getNamespaceId());
                    listPropApartmentsByKeywordCommand.setBuildingName(cmd.getBuildingName());
                    listPropApartmentsByKeywordCommand.setKeyword(cmd.getApartmentName());
                    Tuple<Integer, List<ApartmentDTO>> apts = addressService.listApartmentsByKeyword(listPropApartmentsByKeywordCommand);
                    List<ApartmentDTO> apartments = apts.second();
                    if(apartments.size() != 0)
                        addressIds = apartments.stream().map(ApartmentDTO::getAddressId).collect(Collectors.toList());
                    pmOwners = processCommunityPmOwners(pmOwners, addressIds);

                } else {
                	ListApartmentByBuildingNameCommand listBuildingNameCmd = new ListApartmentByBuildingNameCommand();
                    listBuildingNameCmd.setBuildingName(cmd.getBuildingName());
                    listBuildingNameCmd.setCommunityId(cmd.getCommunityId());

                    List<AddressDTO> addressDTOList = addressService.listAddressByBuildingName(listBuildingNameCmd);
                    if (addressDTOList != null && !addressDTOList.isEmpty()) {
                        addressIds = addressDTOList.stream().map(AddressDTO::getId).collect(Collectors.toList());
                    }
                    pmOwners = processCommunityPmOwners(pmOwners, addressIds);
                }
            }

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
                	d.setAddressId(address.getId());
                	d.setAddress(address.getAddress());
                	d.setApartment(address.getApartmentName());
                	d.setBuilding(address.getBuildingName());
                	return d;
                }).collect(Collectors.toList()));
                
                return dto;
            }).collect(Collectors.toList()));

            Long anchor = getAnchor(cmd);
            if (ownerDTOList.size() < cmd.getPageSize() && ids.size() > cmd.getPageSize()) {
                cmd.setPageAnchor(anchor + cmd.getPageSize() + 1);
                return buildOrganizationOwnerDTOList(cmd, ownerDTOList);
            }
            return anchor;
        }
        return 0L;
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
            b.field("contactToken", owner.getContactToken());
            b.field("contactName", owner.getContactName());
//            b.field("communityId", owner.getCommunityId());
            //一个个人用户可能会有多个地址 所以communityId字段变成用逗号分隔的id列表 by xiongying20170829
            //如果是同一个field下多个值，则全部加入到同一个field下
            if(owner.getCommunityId() != null) {
                String[] communities = owner.getCommunityId().split(",");
                if(null != communities && communities.length > 1) {
                    b.array("communityId", communities);
                }
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
