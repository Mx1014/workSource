// @formatter:off
package com.everhomes.welfare;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.everhomes.rest.promotion.coupon.enterprise.TransferToPersonalDTO;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.archives.ArchivesService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.promotion.coupon.controller.EnterpriseDistributionToPersonRestResponse;
import com.everhomes.rest.promotion.coupon.enterprise.ObtainDetailsExtendDTO;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.welfare.DeleteWelfareCommand;
import com.everhomes.rest.welfare.DraftWelfareCommand;
import com.everhomes.rest.welfare.DraftWelfareResponse;
import com.everhomes.rest.welfare.GetUserWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareResponse;
import com.everhomes.rest.welfare.GetWelfareCommand;
import com.everhomes.rest.welfare.GetWelfareResponse;
import com.everhomes.rest.welfare.ListUserWelfaresCommand;
import com.everhomes.rest.welfare.ListUserWelfaresResponse;
import com.everhomes.rest.welfare.ListWelfaresCommand;
import com.everhomes.rest.welfare.ListWelfaresResponse;
import com.everhomes.rest.welfare.SendWelfareCommand;
import com.everhomes.rest.welfare.SendWelfaresResponse;
import com.everhomes.rest.welfare.WelfareCheckStatus;
import com.everhomes.rest.welfare.WelfareCouponDTO;
import com.everhomes.rest.welfare.WelfareReceiverDTO;
import com.everhomes.rest.welfare.WelfareStatus;
import com.everhomes.rest.welfare.WelfaresDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component
public class WelfareServiceImpl implements WelfareService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WelfareServiceImpl.class);
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private WelfareProvider welfareProvider;
    @Autowired
    private WelfareCouponProvider welfareCouponProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private WelfareReceiverProvider welfareReceiverProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private GeneralOrderService generalOrderService;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private UserProvider userProvider;
    @Override
    public ListWelfaresResponse listWelfares(ListWelfaresCommand cmd) {
        ListWelfaresResponse response = new ListWelfaresResponse();

        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        int pageOffset =(cmd.getPageOffset()==null || cmd.getPageOffset()<1) ? 1 : cmd.getPageOffset();
        int	offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);

        List<Welfare> results = welfareProvider.listWelfare(cmd.getOrganizationId(), offset, pageSize + 1);
        if (null == results || results.size() == 0) {
            return response;
        }
        Integer nextPageOffSet = null;
        if (results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageOffSet = pageOffset+1;
        }
        response.setNextPageOffset(nextPageOffSet);
        response.setWelfares(results.stream().map(this::processWelfaresDTO).collect(Collectors.toList()));
        return response;
    }

    @Override
    public GetWelfareResponse getWelfare(GetWelfareCommand cmd) {
        Welfare welfare = welfareProvider.findWelfareById(cmd.getWelfareId());
        return new GetWelfareResponse(processWelfaresDTO(welfare));
    }
    private WelfaresDTO processWelfaresDTO(Welfare r) {
    	return processWelfaresDTO(r, true);
    }

	private WelfaresDTO processWelfaresDTO(Welfare r, boolean receviersFlag) {
	
        WelfaresDTO dto = ConvertHelper.convert(r, WelfaresDTO.class);
        //用户头像
        if (r.getSenderUid() != null) {
            User queryUser = userProvider.findUserById(r.getSenderUid());
            if (queryUser != null) {

                String avatarUri = queryUser.getAvatar();
                if (avatarUri == null || avatarUri.trim().length() == 0) {
                    avatarUri = userService.getUserAvatarUriByGender(queryUser.getId(), queryUser.getNamespaceId(), queryUser.getGender());
                }
                String url = contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), r.getSenderUid());
                dto.setSenderAvatarUrl(url);
            }
        }
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
        dto.setCoupons(new ArrayList<>());
        dto.setReceivers(new ArrayList<>());
        List<WelfareCoupon> coupons = welfareCouponProvider.listWelfareCoupon(r.getId());
        if (null != coupons) {
            for (WelfareCoupon coupon : coupons) {
                WelfareCouponDTO couponDTO = getWelfareCouponDTO(coupon);
                dto.getCoupons().add(couponDTO);
            }
        }
        if(receviersFlag){
	        List<WelfareReceiver> receivers = welfareReceiverProvider.listWelfareReceiver(r.getId());
	        if (null != receivers) {
	            for (WelfareReceiver receiver : receivers) {
	                WelfareReceiverDTO reDTO = ConvertHelper.convert(receiver, WelfareReceiverDTO.class);
	                dto.getReceivers().add(reDTO);
	            }
	        }
        }
        return dto;
    }

    private WelfareCouponDTO getWelfareCouponDTO(WelfareCoupon coupon) {
        WelfareCouponDTO couponDTO = ConvertHelper.convert(coupon, WelfareCouponDTO.class);
        if (coupon.getValidDate() != null) {
            couponDTO.setValidDate(coupon.getValidDate().getTime());
        }
        if (coupon.getBeginDate() != null) {
            couponDTO.setBeginDate(coupon.getBeginDate().getTime());
        }
        return couponDTO;
    }

    @Override
    public DraftWelfareResponse draftWelfare(DraftWelfareCommand cmd) {
        String lockName = CoordinationLocks.WELFARE_EDIT_LOCK.getCode();
        WelfaresDTO welfareDTO = ConvertHelper.convert(cmd, WelfaresDTO.class);
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
            welfareDTO.setId(saveWelfare(welfareDTO).getId());
            return null;
        });
        return new DraftWelfareResponse(welfareDTO);
    }

    private Welfare saveWelfare(WelfaresDTO welfareDTO) {

        Welfare welfare = ConvertHelper.convert(welfareDTO, Welfare.class);
        welfare.setNamespaceId(UserContext.getCurrentNamespaceId());
        this.dbProvider.execute((TransactionStatus status) -> {
            String uName = "-";
            OrganizationMemberDetails operator = organizationProvider.findOrganizationMemberDetailsByTargetId(UserContext.currentUserId(), welfareDTO.getOrganizationId());
            if (operator != null) {
                uName = operator.getContactName();
            }
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
	        welfareCouponProvider.deleteWelfareCoupons(welfare.getId());
	        if(welfareDTO.getCoupons() != null){
	        	for(WelfareCouponDTO couponDto : welfareDTO.getCoupons()){
	        		WelfareCoupon coupon = convertDTO2WelfareCoupon(couponDto, welfare.getOrganizationId(), welfare.getId(), welfare.getNamespaceId());
	        		welfareCouponProvider.createWelfareCoupon(coupon);
	        	}
	        }
	        welfareReceiverProvider.deleteWelfareReceivers(welfare.getId());
	        if (null != welfareDTO.getReceivers()) {
	            for (WelfareReceiverDTO dto : welfareDTO.getReceivers()) {
	                WelfareReceiver receiver = ConvertHelper.convert(dto, WelfareReceiver.class);
	                receiver.setOrganizationId(welfare.getOrganizationId());
	                receiver.setWelfareId(welfare.getId());
	                receiver.setNamespaceId(welfare.getNamespaceId());
	                welfareReceiverProvider.createWelfareReceiver(receiver);
	            }
	        }
	        return null;
        });
        return welfare;
    }

    private WelfareCoupon convertDTO2WelfareCoupon(WelfareCouponDTO couponDto, Long organizationId, Long welfareId, Integer namespaceId) {
    	WelfareCoupon coupon = ConvertHelper.convert(couponDto, WelfareCoupon.class);
    	coupon.setNamespaceId(namespaceId);
        if (couponDto.getValidDate() != null) {
            coupon.setValidDate(new Date(couponDto.getValidDate()));
        }
        if (couponDto.getBeginDate() != null) {
            coupon.setBeginDate(new Date(couponDto.getBeginDate()));
        }
        coupon.setOrganizationId(organizationId);
    	coupon.setWelfareId(welfareId);
		return coupon;
	}

	private String getWelfareZLUrl(Welfare welfare, HttpServletRequest request) {

        String homeUrl = request.getHeader("Host");
        return "http://"+homeUrl+"/enterprise-welfare/build/index.html?organizationId="+welfare.getOrganizationId()+"&namespaceId="+UserContext.getCurrentNamespaceId()
        		+"&id="+welfare.getId()+"#/detail#sign_suffix";
    }

    @Override
    public SendWelfaresResponse sendWelfare(SendWelfareCommand cmd, HttpServletRequest request) {
        String lockName = CoordinationLocks.WELFARE_EDIT_LOCK.getCode();
        WelfaresDTO welfaresDTO = ConvertHelper.convert(cmd, WelfaresDTO.class);
        if (welfaresDTO.getId() != null) {
            lockName = lockName + welfaresDTO.getOrganizationId() + welfaresDTO.getId();
        } else {
            lockName = lockName + welfaresDTO.getOrganizationId();
        }
        return this.coordinationProvider.getNamedLock(lockName).enter(() -> {
            SendWelfaresResponse response = new SendWelfaresResponse();
            if (welfaresDTO.getId() != null) {
                Welfare welfare = welfareProvider.findWelfareById(welfaresDTO.getId());
                if(null == welfare){
                	throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                            WelfareConstants.ERROR_WELFARE_NOT_FOUND, "福利被删除");
                }
                if (WelfareStatus.SENDED == WelfareStatus.fromCode(welfare.getStatus())) {
                    throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                            WelfareConstants.ERROR_WELFARE_SENDED, "已发送不能发送");
                }
            }
            Boolean isDarft = false;
            if (welfaresDTO.getId() != null) {
                isDarft = true;
            }
            welfaresDTO.setStatus(WelfareStatus.SENDED.getCode());
            //校验在职离职
            response.setCheckStatus(WelfareCheckStatus.SUCESS.getCode());
            response.setDismissReceivers(new ArrayList<>());
            OrganizationMemberDetails member = organizationProvider.findOrganizationMemberDetailsByDetailId(welfaresDTO.getSenderDetailId());
            if (null != member) {
                welfaresDTO.setSenderUid(member.getTargetId());
            }
            if (archivesService.checkDismiss(member)) {
                response.setCheckStatus(WelfareCheckStatus.EMPLOYEE_RESIGNED.getCode());
                response.setDismissSenderDetailId(welfaresDTO.getSenderDetailId());
                response.setDismissSenderUid(welfaresDTO.getSenderUid());
            }
            List<Long> targetUserIds = new ArrayList<>();
            //校验所有人是否离职
            for(WelfareReceiverDTO receiverDTO :welfaresDTO.getReceivers()){
                OrganizationMemberDetails receiverDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(receiverDTO.getReceiverDetailId());
                receiverDTO.setReceiverUid(member != null ? member.getTargetId() : null);
                targetUserIds.add(receiverDetail.getTargetId());
                if (archivesService.checkDismiss(receiverDetail)) {
                    response.setCheckStatus(WelfareCheckStatus.EMPLOYEE_RESIGNED.getCode());
                    response.getDismissReceivers().add(receiverDTO);
                }
            }
            if (WelfareCheckStatus.SUCESS != WelfareCheckStatus.fromCode(response.getCheckStatus())) {
                return response;
            }
            //校验没问题保存福利
            Welfare welfare = saveWelfare(welfaresDTO);

            try{
                //调用发送接口
                if (CollectionUtils.isNotEmpty(welfaresDTO.getCoupons())) {
                    welfare.setCouponOrders(StringHelper.toJsonString(sendCouponsToUsers(welfaresDTO, response, targetUserIds, welfare.getOrganizationId())));
                }
                welfareProvider.updateWelfare(welfare);
                //积分 TODO
            }catch(Exception e){
            	//转账异常处理 删除接受人和卡券记录
            	if(WelfareCheckStatus.SUCESS == WelfareCheckStatus.fromCode(response.getCheckStatus())){
                    LOGGER.error("意外的错误:", e);
                    response.setCheckStatus(WelfareCheckStatus.OTHER.getCode());
            	}
                welfareReceiverProvider.deleteWelfareReceivers(welfare.getId());
                welfareCouponProvider.deleteWelfareCoupons(welfare.getId());
                //原本是草稿的状态恢复成草稿,直接发送的删除welfare
                if(isDarft) {
                    welfare.setStatus(WelfareStatus.DRAFT.getCode());
                    welfareProvider.updateWelfare(welfare);
                }else{
                    welfareProvider.deleteWelfare(welfare.getId());
                }
                return response;
            }
            //发消息
            welfaresDTO.getReceivers().forEach(r -> sendPayslipMessage(welfare, r.getReceiverUid(), request));
            return response;
        }).first();

    }

    private EnterpriseDistributionToPersonRestResponse sendCouponsToUsers(WelfaresDTO welfaresDTO, SendWelfaresResponse response, List<Long> targetUserIds, Long organizationId) throws Exception {
        //卡券
        TransferToPersonalDTO cmd1 = new TransferToPersonalDTO();
        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
        cmd1.setOrganizationId(organizationId);
        cmd1.setTargetUserList(targetUserIds);
        List<ObtainDetailsExtendDTO> obtainsList = welfaresDTO.getCoupons().stream()
                .map(r->{
                    ObtainDetailsExtendDTO dto = new ObtainDetailsExtendDTO();
                    dto.setId(r.getCouponId());
                    dto.setDistributionPerAmount(r.getAmount());
                    return dto;
                }).collect(Collectors.toList());

        cmd1.setObtainDetailsExtendDTOList(obtainsList);
        EnterpriseDistributionToPersonRestResponse resp = generalOrderService.distributionToPerson(cmd1);
        if (resp.getErrorCode().equals(200)) {
            response.setCheckStatus(WelfareCheckStatus.SUCESS.getCode());
        } else if (resp.getErrorCode().equals(104)) {
            response.setCheckStatus(WelfareCheckStatus.NO_ENOUGH_BALANCE.getCode());
            LOGGER.error("发送卡券出错 resp:" + StringHelper.toJsonString(resp));
            throw new Exception();
        } else{
            response.setCheckStatus(WelfareCheckStatus.OTHER.getCode());
            LOGGER.error("发送卡券出错 resp:" + StringHelper.toJsonString(resp));
            throw new Exception();
        }
        return resp;
    }

    private void sendPayslipMessage(Welfare welfare, Long receiverId, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("subject", welfare.getSubject());
        String content = localeTemplateService.getLocaleTemplateString(0, WelfareConstants.SEND_NOTIFICATION_SCOPE, WelfareConstants.SEND_NOTIFICATION_CODE,
                "zh_CN", map, "你收到了" + welfare.getSubject() + ",快去查看吧!");
        sendMessage(content, welfare, receiverId, request);
    }

    private void sendMessage(String content, Welfare welfare, Long receiverId, HttpServletRequest request) {
        //  set the message
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));
        //  set the route
        String url = getWelfareZLUrl(welfare, request);

        RouterMetaObject metaObject = new RouterMetaObject();
        OfficialActionData data = new OfficialActionData();
        data.setUrl(url);
        metaObject.setUrl(RouterBuilder.build(Router.BROWSER_I, data, welfare.getSubject()));
        
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, welfare.getSubject());
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

        UserInfo senderInfo = userService.getUserInfo(welfare.getSenderUid());
        if (senderInfo != null) {
            response.setSenderAvatarUrl(senderInfo.getAvatarUrl());
        }
        if (null != welfare.getSendTime()) {
            response.setSendTime(welfare.getSendTime().getTime());
        }
        if (null != welfare.getImgUri()) {
            response.setImgUrl(contentServerService.parserUri(welfare.getImgUri(),
                    EntityType.USER.getCode(), UserContext.currentUserId()));
        }
        if (welfare.getSendTime() != null) {
            response.setSendTime(welfare.getSendTime().getTime());
        }

        response.setCoupons(new ArrayList<>());
        List<WelfareCoupon> coupons = welfareCouponProvider.listWelfareCoupon(welfare.getId());
        if (null != coupons) {
            for (WelfareCoupon coupon : coupons) {
                WelfareCouponDTO couponDTO = getWelfareCouponDTO(coupon);
                response.getCoupons().add(couponDTO);
            }
        }
        return response;
    }

    private void checkOperatorPrivilege(Long orgId, Long appId) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        if (resolver.checkSuperAdmin(UserContext.currentUserId(), orgId)
                || resolver.checkModuleAppAdmin(UserContext.getCurrentNamespaceId(), orgId, UserContext.currentUserId(), appId)) {
        }else{
            throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                    WelfareConstants.ERROR_PRIVILEGE, "权限不足");
        }
    }
    @Override
    public void deleteWelfare(DeleteWelfareCommand cmd) {
        Welfare welfare = welfareProvider.findWelfareById(cmd.getWelfareId());
        if (welfare == null) {
            throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                    WelfareConstants.ERROR_WELFARE_NOT_FOUND, "福利不存在");
        }
        checkOperatorPrivilege(welfare.getOrganizationId(), cmd.getAppId());
        if (WelfareStatus.SENDED == WelfareStatus.fromCode(welfare.getStatus())) {
            throw RuntimeErrorException.errorWith(WelfareConstants.SCOPE,
                    WelfareConstants.ERROR_WELFARE_SENDED, "已发送不能删除");
        }
        welfareReceiverProvider.deleteWelfareReceivers(cmd.getWelfareId());
        welfareCouponProvider.deleteWelfareCoupons(cmd.getWelfareId());
        welfareProvider.deleteWelfare(cmd.getWelfareId());
    }

    private boolean checkUserInReceivers(Long userId, Long welfareId) {
        return welfareReceiverProvider.findWelfareReceiverByUser(welfareId, userId) != null;
    }

	@Override
	public ListUserWelfaresResponse listUserWelfares(ListUserWelfaresCommand cmd) {
		ListUserWelfaresResponse response = new ListUserWelfaresResponse();
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        int pageOffset =(cmd.getPageOffset()==null || cmd.getPageOffset()<1) ? 1 : cmd.getPageOffset();
        int	offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);

		List<Long> welfareIds = welfareReceiverProvider.listWelfareIdsByUser(cmd.getOrganizationId(),UserContext.currentUserId(), offset, pageSize +1);
		if(CollectionUtils.isEmpty(welfareIds))
			return response;
        List<Welfare> results = welfareProvider.listWelfareByIds(welfareIds);
        if (null == results || results.size() == 0) {
            return response;
        }
        Integer nextPageOffSet = null;
        if (results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageOffSet = pageOffset+1;
        }
        response.setNextPageOffset(nextPageOffSet);
        response.setWelfares(results.stream().map(r -> processWelfaresDTO(r, false)).collect(Collectors.toList()));
        return response;
	}

}