// @formatter:off
package com.everhomes.openapi;

import java.util.List;

public interface TechparkSyncdataBackupProvider {

	void createTechparkSyncdataBackup(TechparkSyncdataBackup techparkSyncdataBackup);

	void updateTechparkSyncdataBackup(TechparkSyncdataBackup techparkSyncdataBackup);

	TechparkSyncdataBackup findTechparkSyncdataBackupById(Long id);

	List<TechparkSyncdataBackup> listTechparkSyncdataBackup();

	List<TechparkSyncdataBackup> listTechparkSyncdataBackupByParam(Integer namespaceId, Byte dataType, Byte allFlag);

	void updateTechparkSyncdataBackupInactive(List<TechparkSyncdataBackup> backupList);

}