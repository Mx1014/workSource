// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;

public interface ServiceAllianceCommentAttachmentProvider {

	void createServiceAllianceCommentAttachment(ServiceAllianceCommentAttachment serviceAllianceCommentAttachment);

	void updateServiceAllianceCommentAttachment(ServiceAllianceCommentAttachment serviceAllianceCommentAttachment);

	ServiceAllianceCommentAttachment findServiceAllianceCommentAttachmentById(Long id);

	List<ServiceAllianceCommentAttachment> listServiceAllianceCommentAttachment();

}