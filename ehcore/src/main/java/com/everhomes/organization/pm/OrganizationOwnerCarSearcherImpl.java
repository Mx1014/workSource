package com.everhomes.organization.pm;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerCarResponse;
import com.everhomes.rest.organization.pm.OrganizationOwnerCarDTO;
import com.everhomes.rest.organization.pm.SearchOrganizationOwnerCarCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.OrganizationOwnerCarSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrganizationOwnerCarSearcherImpl extends AbstractElasticSearch implements OrganizationOwnerCarSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationOwnerCarSearcherImpl.class);

	@Autowired
	private PropertyMgrProvider propertyMgrProvider;

	@Autowired
	private ConfigurationProvider configProvider;

    @Autowired
    private LocaleStringProvider localeStringProvider;

	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<OrganizationOwnerCar> carList) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (OrganizationOwnerCar car : carList) {
            XContentBuilder source = createDoc(car);
            if(null != source) {
                LOGGER.info("id:" + car.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(car.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(OrganizationOwnerCar car) {
		XContentBuilder source = createDoc(car);

        feedDoc(car.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
        this.deleteAll();
        List<OrganizationOwnerCar> cars = propertyMgrProvider.listOrganizationOwnerCarsByIds(null);
        if(cars.size() > 0) {
            this.bulkUpdate(cars);
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("Sync for organization owner car index OK");
	}

	@Override
	public ListOrganizationOwnerCarResponse query(SearchOrganizationOwnerCarCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("plateNumber", 5.0f)
                    .field("contacts", 2.0f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("plateNumber")
					.addHighlightedField("contacts");
        }

        FilterBuilder fb = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        if (cmd.getParkingType() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("parkingType", cmd.getParkingType()));
        }

		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

        SearchResponse rsp = builder.execute().actionGet();

        ListOrganizationOwnerCarResponse response = new ListOrganizationOwnerCarResponse();
        List<OrganizationOwnerCarDTO> ownerCarDTOList = Collections.emptyList();
        List<Long> ids = getIds(rsp);
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }
        if (ids.size() > 0) {
            List<OrganizationOwnerCar> ownerCars = propertyMgrProvider.listOrganizationOwnerCarsByIds(ids);
            ownerCarDTOList = ownerCars.stream().map(r -> {
                OrganizationOwnerCarDTO dto = ConvertHelper.convert(r, OrganizationOwnerCarDTO.class);
                ParkingCardCategory category = propertyMgrProvider.findParkingCardCategory(r.getParkingType());
                dto.setParkingType(category != null ? category.getCategoryName() : "");
                return dto;
            }).collect(Collectors.toList());
        }
        response.setCars(ownerCarDTOList);
        return response;
	}

    @Override
	public String getIndexType() {
		return SearchUtils.PM_OWNER_CAR_INDEX_TYPE;
	}

	private XContentBuilder createDoc(OrganizationOwnerCar car){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("plateNumber", car.getPlateNumber());
            b.field("contacts", car.getContacts());
            b.field("communityId", car.getCommunityId());
            b.field("parkingType", car.getParkingType());
            b.endObject();
            return b;
		} catch (IOException ex) {
			LOGGER.error("Create owner car" + car.getId() + " error");
			return null;
		}
	}

}
