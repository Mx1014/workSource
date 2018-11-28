package com.everhomes.officecubicle;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.*;
import com.everhomes.rest.parking.ListPayeeAccountCommand;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;

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

	void addCubicle(AddCubicleAdminCommand cmd);

	CreateOfficeCubicleOrderResponse createCubicleGeneralOrder(CreateOfficeCubicleOrderCommand cmd);

	OfficeCubicleDTO getCubicleDetail(GetCubicleDetailCommand cmd);

	void createOrUpdateOfficeCubiclePayeeAccount(CreateOrUpdateOfficeCubiclePayeeAccountCommand cmd);

	List<ListOfficeCubicleAccountDTO> listOfficeCubicleAccount(ListOfficeCubicleAccountCommand cmd);

	SearchCubicleOrdersResponse searchCubicleOrders(SearchCubicleOrdersCommand cmd);

	CreateCubicleOrderBackgroundResponse createCubicleOrderBackground(CreateCubicleOrderBackgroundCommand cmd);

	void addRoom(AddRoomAdminCommand cmd);

	void updateRoom(AddRoomAdminCommand cmd);

	ListOfficeCubicleStatusResponse listOfficeCubicleStatus(ListOfficeCubicleStatusCommand cmd);

	void payNotify(MerchantPaymentNotificationCommand cmd);

	ListRentCubicleResponse listRentCubicle(ListRentCubicleCommand cmd);

	GetOfficeCubicleRentOrderResponse getOfficeCubicleRentOrder(GetOfficeCubicleRentOrderCommand cmd);

	void updateCubicle(AddCubicleAdminCommand cmd);

	void deleteRoom(DeleteRoomAdminCommand cmd);

	void deleteCubicle(DeleteCubicleAdminCommand cmd);

	void refundOrder(RefundOrderCommand cmd);

	HttpServletResponse exportCubicleOrders(SearchCubicleOrdersCommand cmd, HttpServletResponse response);

	ListOfficeCubicleAccountDTO getOfficeCubiclPayeeAccount(GetOfficeCubiclePayeeAccountCommand cmd);

	ListCitiesByOrgIdAndCommunitIdResponse listCitiesByOrgIdAndCommunitId(ListCitiesByOrgIdAndCommunitIdCommand cmd);

	GetSpaceResponse getSpace(GetSpaceCommand cmd);

	ListSpaceByCityResponse listSpaceByCity(ListSpaceByCityCommand cmd);

	GetRoomDetailResponse getRoomDetail(GetRoomDetailCommand cmd);

	GetStationDetailResponse getCubicleDetail(GetStationDetailCommand cmd);


}
