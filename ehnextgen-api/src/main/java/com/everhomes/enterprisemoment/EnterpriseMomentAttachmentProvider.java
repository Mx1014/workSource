// @formatter:off
package com.everhomes.enterprisemoment;

import java.util.List;

public interface EnterpriseMomentAttachmentProvider {

	void createEnterpriseMomentAttachment(EnterpriseMomentAttachment enterpriseMomentAttachment);

	void updateEnterpriseMomentAttachment(EnterpriseMomentAttachment enterpriseMomentAttachment);

	EnterpriseMomentAttachment findEnterpriseMomentAttachmentById(Long id);

	List<EnterpriseMomentAttachment> listEnterpriseMomentAttachment();

	List<EnterpriseMomentAttachment> listEnterpriseMomentAttachment(Integer namespaceId, Long organizationId, Long momentId);
}