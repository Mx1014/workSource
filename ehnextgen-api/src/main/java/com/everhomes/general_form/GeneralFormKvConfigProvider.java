// @formatter:off
package com.everhomes.general_form;

public interface GeneralFormKvConfigProvider {

	void createGeneralFormKvConfig(GeneralFormKvConfig config);

	void updateGeneralFormKvConfig(GeneralFormKvConfig config);

	GeneralFormKvConfig findById(Long id);

	GeneralFormKvConfig findByKey(Integer namespaceId, String moduleType, Long moduleId,
								  String projectType, Long projectId, String ownerType, Long ownerId, String key);
}