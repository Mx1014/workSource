package com.everhomes.officecubicle;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.*;

public interface OfficeCubicleService {

	SearchSpacesAdminResponse searchSpaces(SearchSpacesAdminCommand cmd);

	void addSpace(AddSpaceCommand cmd);

	void updateSpace(UpdateSpaceCommand cmd);

	void deleteSpace(DeleteSpaceCommand cmd);

	SearchSpaceOrdersResponse searchSpaceOrders(SearchSpaceOrdersCommand cmd);

	HttpServletResponse exportSpaceOrders(SearchSpaceOrdersCommand cmd,
			HttpServletResponse response);

	List<CityDTO> queryCities(QueryCitiesCommand cmd);

	OfficeSpaceDTO getSpaceDetail(GetSpaceDetailCommand cmd);

	AddSpaceOrderResponse addSpaceOrder(AddSpaceOrderCommand cmd);

	List<OfficeOrderDTO> getUserOrders();

	void deleteUserSpaceOrder(DeleteUserSpaceOrderCommand cmd);

	QuerySpacesResponse querySpaces(QuerySpacesCommand cmd);

    void dataMigration();

	ListRegionsResponse listRegions(ListRegionsCommand cmd);

	ListCitiesResponse listCities(ListCitiesCommand cmd);

    void deleteCity(DeleteCityCommand cmd);

	void createOrUpdateCity(CreateOrUpdateCityCommand cmd);

	void reOrderCity(ReOrderCityCommand cmd);

	void updateCurrentUserSelectedCity(String provinceName, String cityName);

	CityDTO getCityById(GetCityByIdCommand cmd);

	ListCitiesResponse listProvinceAndCites(ListCitiesCommand cmd);

	ListCitiesResponse copyCities(CopyCitiesCommand cmd);

	ListCitiesResponse removeCustomizedCities(CopyCitiesCommand cmd);

	Byte getProjectCustomize(GetCustomizeCommand cmd);

	Byte getCurrentProjectOnlyFlag(GetCurrentProjectOnlyFlagCommand cmd);
}
