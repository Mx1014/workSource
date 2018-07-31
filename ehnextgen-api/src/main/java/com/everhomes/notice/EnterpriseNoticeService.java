package com.everhomes.notice;

import com.everhomes.rest.notice.*;

public interface EnterpriseNoticeService {

    EnterpriseNoticeDTO getEnterpriseNoticeDetailInfo(Long enterpriseNoticeId);

    EnterpriseNoticePreviewDTO createEnterpriseNotice(CreateEnterpriseNoticeCommand cmd);

    EnterpriseNoticePreviewDTO updateEnterpriseNotice(UpdateEnterpriseNoticeCommand cmd);

    void deleteEnterpriseNotice(DeleteEnterpriseNoticeCommand cmd);

    void cancelEnterpriseNotice(CancelEnterpriseNoticeCommand cmd);

    ListEnterpriseNoticeAdminResponse listEnterpriseNoticesByNamespaceId(ListEnterpriseNoticeAdminCommand cmd);

    ListEnterpriseNoticeResponse listEnterpriseNoticesByUserId(ListEnterpriseNoticeCommand cmd);

    UserContactSimpleInfoDTO getCurrentUserContactSimpleInfo(GetCurrentUserContactInfoCommand cmd);

    String getUserContactNameByUserId(Long userId);

    boolean isNoticeSendToCurrentUser(Long enterpriseNoticeId);

    EnterpriseNoticeDTO getSharedEnterpriseNoticeDetailInfo(String enterpriseNoticeToken);

}
