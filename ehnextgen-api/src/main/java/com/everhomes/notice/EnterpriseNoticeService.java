package com.everhomes.notice;

import com.everhomes.rest.notice.CancelEnterpriseNoticeCommand;
import com.everhomes.rest.notice.CreateEnterpriseNoticeCommand;
import com.everhomes.rest.notice.DeleteEnterpriseNoticeCommand;
import com.everhomes.rest.notice.EnterpriseNoticeDTO;
import com.everhomes.rest.notice.EnterpriseNoticePreviewDTO;
import com.everhomes.rest.notice.GetCurrentUserContactInfoCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeAdminCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeAdminResponse;
import com.everhomes.rest.notice.ListEnterpriseNoticeCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeResponse;
import com.everhomes.rest.notice.StickyEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UnStickyEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UpdateEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UserContactSimpleInfoDTO;

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

    boolean isNoticeSendToCurrentUser(Long organizationId, Long enterpriseNoticeId);

    EnterpriseNoticeDTO getSharedEnterpriseNoticeDetailInfo(String enterpriseNoticeToken);

	void stickyEnterpriseNotice(StickyEnterpriseNoticeCommand cmd);

	void unStickyEnterpriseNotice(UnStickyEnterpriseNoticeCommand cmd);

}
