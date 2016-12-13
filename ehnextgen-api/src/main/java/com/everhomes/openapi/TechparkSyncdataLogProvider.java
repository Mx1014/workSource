// @formatter:off
package com.everhomes.openapi;

import java.util.List;

public interface TechparkSyncdataLogProvider {

	void createTechparkSyncdataLog(TechparkSyncdataLog techparkSyncdataLog);

	void updateTechparkSyncdataLog(TechparkSyncdataLog techparkSyncdataLog);

	TechparkSyncdataLog findTechparkSyncdataLogById(Long id);

	List<TechparkSyncdataLog> listTechparkSyncdataLog();

}