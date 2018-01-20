package com.everhomes.officecubicle;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.AddSpaceCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersResponse;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminResponse;
import com.everhomes.rest.officecubicle.admin.UpdateSpaceCommand;

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

	void addSpaceOrder(AddSpaceOrderCommand cmd);

	List<OfficeOrderDTO> getUserOrders();

	void deleteUserSpaceOrder(DeleteUserSpaceOrderCommand cmd);

	QuerySpacesResponse querySpaces(QuerySpacesCommand cmd);

}
