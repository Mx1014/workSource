// @formatter:off
package com.everhomes.officecubicle;

import com.everhomes.rest.officecubicle.admin.CityDTO;

import java.util.List;

public interface OfficeCubicleCityProvider {

	void createOfficeCubicleCity(OfficeCubicleCity officeCubicleCity);

	void updateOfficeCubicleCity(OfficeCubicleCity officeCubicleCity);

	OfficeCubicleCity findOfficeCubicleCityById(Long id);

	List<OfficeCubicleCity> listOfficeCubicleCity(Integer namespaceId);

	List<OfficeCubicleCity> listOfficeCubicleCity(Integer namespaceId, Long nextPageAnchor, int pageSize);

	void deleteOfficeCubicleCity(Long cityId);

	OfficeCubicleCity findOfficeCubicleCityByProvinceAndCity(String provinceName, String cityName, Integer namespaceId);

	List<OfficeCubicleCity> listOfficeCubicleProvince(Integer namespaceId);

	List<OfficeCubicleCity> listOfficeCubicleCitiesByProvince(String provinceName, Integer namespaceId);

	List<OfficeCubicleCity> listOfficeCubicleCityByOwnerId(String ownerType,Long ownerId);
}