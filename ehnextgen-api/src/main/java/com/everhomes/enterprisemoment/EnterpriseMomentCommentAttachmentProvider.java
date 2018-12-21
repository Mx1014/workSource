package com.everhomes.enterprisemoment;

import java.util.List;

public interface EnterpriseMomentCommentAttachmentProvider {

    void batchCreateAttachments(List<EnterpriseMomentCommentAttachment> attachmentList);

    void deleteAttachmentsByCommentId(Integer namespaceId, Long commentId);

    List<EnterpriseMomentCommentAttachment> queryAttachmentsByCommentIds(Integer namespaceId, List<Long> commentIds);

}
