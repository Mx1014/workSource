// @formatter:off
package com.everhomes.officecubicle;

import java.util.List;

public interface OfficeCubicleCityProvider {

	void createOfficeCubicleCity(OfficeCubicleCity officeCubicleCity);

	void updateOfficeCubicleCity(OfficeCubicleCity officeCubicleCity);

	OfficeCubicleCity findOfficeCubicleCityById(Long id);

	List<OfficeCubicleCity> listOfficeCubicleCity();

}