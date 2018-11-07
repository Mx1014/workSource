// @formatter:off
package com.everhomes.welfare;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.archives.ArchivesService;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.paySDK.PaySDKController;
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.socialSecurity.NormalFlag;
import com.everhomes.rest.welfare.*;
import com.everhomes.salary.SalaryService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelfareServiceImpl implements WelfareService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WelfareServiceImpl.class);
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private PayService payService;
    @Autowired
    private WelfareProvider welfareProvider;
    @Autowired
    private WelfareItemProvider welfareItemProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private WelfareReceiverProvider welfareReceiverProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private SalaryService salaryService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private OrganizationProvider organizationProvider;
    @Override
    public ListWelfaresResponse listWelfares(ListWelfaresCommand cmd) {
        ListWelfaresResponse response = new ListWelfaresResponse();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (null != cmd.getPageAnchor()) {
            locator.setAnchor(cmd.getPageAnchor());
        }
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();

        List<Welfare> results = welfareProvider.listWelfare(cmd.getOrganizationId(), locator, pageSize + 1);
        if (null == results || results.size() == 0) {
            return response;
        }
        Long nextPageAnchor = null;
        if (results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setWelfares(results.stream().map(r -> processWelfaresDTO(r)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public GetWelfareResponse getWelfare(GetWelfareCommand cmd) {
        Welfare welfare = welfareProvider.findWelfareById(cmd.getWelfareId());
        return new GetWelfareResponse(processWelfaresDTO(welfare));
    }
    private WelfaresDTO processWelfaresDTO(Welfare r) {
        WelfaresDTO dto = ConvertHelper.convert(r, WelfaresDTO.class);
        dto.setUpdateTime(r.getUpdateTime().getTime());
        if (null != r.getSendTime()) {
            dto.setSendTime(r.getSendTime().getTime());
        }
        if (null != r.getImgUri()) {
            dto.setImgUrl(contentServerService.parserUri(r.getImgUri(),
                    EntityType.USER.getCode(), UserContext.currentUserId()));
            ContentServerResource resource = contentServerService.findResourceByUri(r.getImgUri());
            if (null != resource) {
                dto.setImgName(resource.getResourceName());
                dto.setImgSize(resource.getResourceSize());
            }
        }
        dto.setItems(new ArrayList<>());
        dto.setReceivers(new ArrayList<>());
        List<WelfareItem> items = welfareItemProvider.listWelfareItem(r.getId());
        if (null != items) {
            for (WelfareItem item : items) {
                WelfareItemDTO itemDTO = ConvertHelper.convert(item, WelfareItemDTO.class);
                dto.getItems().add(itemDTO);
            }
        }
        List<WelfareReceiver> receivers = welfareReceiverProvider.listWelfareReceiver(r.getId());
        if (null != receivers) {
            for (WelfareReceiver receiver : receivers) {
                WelfareReceiverDTO reDTO = ConvertHelper.convert(receiver, WelfareReceiverDTO.class);
                dto.getReceivers().add(reDTO);
            }
        }
        return dto;
    }

    @Override
    public DraftWelfareResponse draftWelfare(DraftWelfareCommand cmd) {
        String lockName = CoordinationLocks.WELFARE_EDIT_LOCK.getCode();
        WelfaresDTO welfareDTO = cmd.getWelfare();
        if (welfareDTO.getId() != null) {
            lockName = lockName + welfareDTO.getOrganizationId() + welfareDTO.getId();
        } else {
            lockName = lockName + welfareDTO.getOrganizationId();
        }
        this.coordinationProvider.getNamedLock(lockName).enter(() -> {
            if (welfareDTO.getId() != null) {
                Welfare welfare = welfareProvider.findWelfareById(welfareDTO.getId());
                if(null == welfare){
                	throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                            WelfareConstants.ERROR_WELFARE_NOT_FOUND, "福利被删除");
                }
                if (WelfareStatus.SENDED == WelfareStatus.fromCode(welfare.getStatus())) {
                    throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                            WelfareConstants.ERROR_WELFARE_SENDED, "已发送不能保存草稿");
                }
            }
            welfareDTO.setStatus(WelfareStatus.DRAFT.getCode());
            welfareDTO.setId(saveWelfare(cmd.getWelfare()).getId());
            return null;
        });
        return new DraftWelfareResponse(welfareDTO);
    }

    private Welfare saveWelfare(WelfaresDTO welfareDTO) {
        Welfare welfare = ConvertHelper.convert(welfareDTO, Welfare.class);
        String uName = salaryService.findNameByOwnerAndUser(welfare.getOrganizationId(), UserContext.currentUserId());
        welfare.setCreatorName(uName);
        welfare.setOperatorName(uName);
        if (WelfareStatus.SENDED == WelfareStatus.fromCode(welfare.getStatus())) {
            welfare.setSendTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//            welfare.setSenderUid(UserContext.currentUserId());
        }
        if (null == welfare.getId()) {
            welfareProvider.createWelfare(welfare);
        } else {
            welfareProvider.updateWelfare(welfare);
        }
        
        welfareReceiverProvider.deleteWelfareReceivers(welfare.getId());
        if (null != welfareDTO.getReceivers()) {
            for (WelfareReceiverDTO dto : welfareDTO.getReceivers()) {
                WelfareReceiver receiver = ConvertHelper.convert(dto, WelfareReceiver.class);
                receiver.setOrganizationId(welfare.getOrganizationId());
                receiver.setWelfareId(welfare.getId());
                welfareReceiverProvider.createWelfareReceiver(receiver);
            }
        }
        return welfare;
    }

    String getWelfareZLUrl(Long welfareId) {
        return "zl:" + welfareId;
    }

    @Override
    public SendWelfaresResponse sendWelfare(SendWelfareCommand cmd) {
        String lockName = CoordinationLocks.WELFARE_EDIT_LOCK.getCode();
        if (cmd.getWelfare().getId() != null) {
            lockName = lockName + cmd.getWelfare().getOrganizationId() + cmd.getWelfare().getId();
        } else {
            lockName = lockName + cmd.getWelfare().getOrganizationId();
        }
        return this.coordinationProvider.getNamedLock(lockName).enter(() -> {
            SendWelfaresResponse response = new SendWelfaresResponse();
            if (cmd.getWelfare().getId() != null) {
                Welfare welfare = welfareProvider.findWelfareById(cmd.getWelfare().getId());
                if(null == welfare){
                	throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                            WelfareConstants.ERROR_WELFARE_NOT_FOUND, "福利被删除");
                }
                if (WelfareStatus.SENDED == WelfareStatus.fromCode(welfare.getStatus())) {
                    throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                            WelfareConstants.ERROR_WELFARE_SENDED, "已发送不能发送");
                }
            }
            cmd.getWelfare().setStatus(WelfareStatus.SENDED.getCode());
            Welfare welfare = saveWelfare(cmd.getWelfare());
            response.setCheckStatus(NormalFlag.NO.getCode());
            response.setDismissReceivers(new ArrayList<>());
            OrganizationMemberDetails member = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getWelfare().getSenderDetailId());
            if (null != member) {
                cmd.getWelfare().setSenderUid(member.getTargetId());
            }
            if (archivesService.checkDismiss(member)) {
                response.setCheckStatus(NormalFlag.YES.getCode());
                response.setDismissSenderDetailId(cmd.getWelfare().getSenderDetailId());
                response.setDismissSenderUid(cmd.getWelfare().getSenderUid());
            }
                //校验所有人是否离职
            for(WelfareReceiverDTO receiverDTO :cmd.getWelfare().getReceivers()){

                OrganizationMemberDetails receiverDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(receiverDTO.getReceiverDetailId());
                receiverDTO.setReceiverUid(member.getTargetId());
                if (archivesService.checkDismiss(receiverDetail)) {
                    response.setCheckStatus(NormalFlag.YES.getCode());
                    response.getDismissReceivers().add(receiverDTO);
                }

            }
            if (NormalFlag.YES == NormalFlag.fromCode(response.getCheckStatus())) {
                return response;
            }
          
            //发消息
            cmd.getWelfare().getReceivers().forEach(r -> {

                
                //查企业账户有没有足够的钱
                //拿用户的账户(没有就创建)
                //转账
                PayUserDTO accountDTO = payService.createPersonalPayUserIfAbsent("EhUser" + r.getReceiverUid(), "NS" + UserContext.getCurrentNamespaceId().toString());
                //todo 转账
                sendPayslipMessage(welfare.getOperatorName(), welfare.getSubject(), welfare.getId(), r.getReceiverUid());
            });
            return response;
        }).first();

    }

    private void sendPayslipMessage(String senderName, String subject, Long welfareId, Long receiverId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("senderName", senderName);
        map.put("subject", subject);
        String content = localeTemplateService.getLocaleTemplateString(0, WelfareConstants.SEND_NOTIFICATION_SCOPE, WelfareConstants.SEND_NOTIFICATION_CODE,
                "zh_CN", map, senderName + "给你发放了" + subject + ",快去查看吧!");
        sendMessage(content, subject, receiverId, welfareId);
    }

    private void sendMessage(String content, String subject, Long receiverId, Long welfareId) {
        //  set the message
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));
        //  set the route
        String url = getWelfareZLUrl(welfareId);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, subject);
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiverId),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }

    @Override
    public GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd) {
        Long userId = UserContext.currentUserId();
        if (!checkUserInReceivers(userId, cmd.getWelfareId())) {
            return null;
        }
        Welfare welfare = welfareProvider.findWelfareById(cmd.getWelfareId());
        if (null == welfare) {
            return null;
        }
        GetUserWelfareResponse response = ConvertHelper.convert(welfare, GetUserWelfareResponse.class);
        if (null != welfare.getSendTime()) {
            response.setSendTime(welfare.getSendTime().getTime());
        }
        if (null != welfare.getImgUri()) {
            response.setImgUrl(contentServerService.parserUri(welfare.getImgUri(),
                    EntityType.USER.getCode(), UserContext.currentUserId()));
            ContentServerResource resource = contentServerService.findResourceByUri(welfare.getImgUri());
        }
        if (welfare.getSendTime() != null) {
            response.setSendTime(welfare.getSendTime().getTime());
        }

        response.setItems(new ArrayList<>());
        List<WelfareItem> items = welfareItemProvider.listWelfareItem(welfare.getId());
        if (null != items) {
            for (WelfareItem item : items) {
                WelfareItemDTO itemDTO = ConvertHelper.convert(item, WelfareItemDTO.class);
                response.getItems().add(itemDTO);
            }
        }
        return response;
    }

    @Override
    public void deleteWelfare(DeleteWelfareCommand cmd) {
        Welfare welfare = welfareProvider.findWelfareById(cmd.getWelfareId());
        if (WelfareStatus.SENDED == WelfareStatus.fromCode(welfare.getStatus())) {
            throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                    WelfareConstants.ERROR_WELFARE_SENDED, "已发送不能删除");
        }
        welfareReceiverProvider.deleteWelfareReceivers(cmd.getWelfareId());
        welfareItemProvider.deleteWelfareItems(cmd.getWelfareId());
        welfareProvider.deleteWelfare(cmd.getWelfareId());
    }

    private boolean checkUserInReceivers(Long userId, Long welfareId) {
        if (welfareReceiverProvider.findWelfareReceiverByUser(welfareId, userId) != null) {
            return true;
        }
        return false;
    }

}