// @formatter:off
package com.everhomes.news;

import java.util.List;

public interface AttachmentProvider {

	void createAttachment(Class<?> pojoClass, Attachment attachment);

	void createAttachments(Class<?> pojoClass, List<Attachment> attachments);

	void updateAttachment(Class<?> pojoClass, Attachment attachment);

	void deleteAttachment(Class<?> pojoClass, Long id);

	Attachment findAttachmentById(Class<?> pojoClass, Long id);

	/**
	 * <p>按一个ownerId查询</p>
	 */
	List<Attachment> listAttachmentByOwnerId(Class<?> pojoClass, Long ownerId);

	/**
	 * <p>按多个ownerId查询</p>
	 */
	List<Attachment> listAttachmentByOwnerIds(Class<?> pojoClass, List<Long> ownerIds);
	
}
