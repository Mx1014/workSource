// @formatter:off
package com.everhomes.officecubicle;

import java.util.List;

public interface OfficeCubicleSelectedCityProvider {

	void createOfficeCubicleSelectedCity(OfficeCubicleSelectedCity officeCubicleSelectedCity);

	void updateOfficeCubicleSelectedCity(OfficeCubicleSelectedCity officeCubicleSelectedCity);

	OfficeCubicleSelectedCity findOfficeCubicleSelectedCityById(Long id);

	List<OfficeCubicleSelectedCity> listOfficeCubicleSelectedCity();

	OfficeCubicleSelectedCity findOfficeCubicleSelectedCityByCreator(Long creator);

	void deleteSelectedCityByCreator(Long id);

}