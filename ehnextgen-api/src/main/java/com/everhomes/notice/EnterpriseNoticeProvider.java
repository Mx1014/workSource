package com.everhomes.notice;

import java.util.List;

public interface EnterpriseNoticeProvider {

    EnterpriseNotice findById(Long id);

    void createEnterpriseNotice(EnterpriseNotice enterpriseNotice);

    void updateEnterpriseNotice(EnterpriseNotice enterpriseNotice);

    List<EnterpriseNotice> listEnterpriseNoticesByNamespaceId(Integer namespaceId, Long organizationId, Integer offset, Integer pageSize);

    int totalCountEnterpriseNoticesByNamespaceId(Integer namespaceId, Long organizationId);

    List<EnterpriseNotice> listEnterpriseNoticesByOwnerId(List<EnterpriseNoticeReceiver> owner, Integer namespaceId, Integer offset, Integer pageSize);

    void createEnterpriseNoticeAttachment(EnterpriseNoticeAttachment attachment);

    List<EnterpriseNoticeAttachment> findEnterpriseNoticeAttachmentsByNoticeId(Long enterpriseNoticeId);

    void logicDeleteEnterpriseNoticeAttachmentsByNoticeId(Long enterpriseNoticeId);

    void logicDeleteEnterpriseNoticeReceiversByNoticeId(Long enterpriseNoticeId);

    void deleteEnterpriseNoticeAttachmentsByNoticeId(Long enterpriseNoticeId);

    void deleteEnterpriseNoticeReceiversByNoticeId(Long enterpriseNoticeId);

    void createEnterpriseNoticeReceiver(EnterpriseNoticeReceiver receiver);

    List<EnterpriseNoticeReceiver> findEnterpriseNoticeReceiversByNoticeId(Long enterpriseNoticeId);

}
