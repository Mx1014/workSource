// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;

import java.util.List;

public interface PointTutorialProvider {

	void createPointTutorial(PointTutorial pointTutorial);

	void updatePointTutorial(PointTutorial pointTutorial);

	PointTutorial findById(Long id);

    List<PointTutorial> listPointTutorials(Long systemId, int pageSize, ListingLocator locator);

    void deleteTutorial(PointTutorial tutorial);

    void deleteBySystemId(Long systemId);
}