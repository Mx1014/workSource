package com.everhomes.pushmessage.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.pushmessage.PushMessage;
import com.everhomes.pushmessage.PushMessageService;
import com.everhomes.pushmessage.PushMessageStatus;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pushmessage.CreatePushMessageCommand;
import com.everhomes.rest.pushmessage.DeletePushMessageCommand;
import com.everhomes.rest.pushmessage.ListPushMessageCommand;
import com.everhomes.rest.pushmessage.ListPushMessageResponse;
import com.everhomes.rest.pushmessage.ListPushMessageResultCommand;
import com.everhomes.rest.pushmessage.ListPushMessageResultResponse;
import com.everhomes.rest.pushmessage.PushMessageDTO;
import com.everhomes.rest.pushmessage.PushMessageResultDTO;
import com.everhomes.rest.pushmessage.UpdatePushMessageCommand;
import com.everhomes.rest.recommend.RecommendConfigDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;

@RestDoc(value="Address admin controller", site="core")
@RestController
@RequestMapping("/admin/pushmessage")
public class PushMessageAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMessageAdminController.class);
    
    @Autowired
    PushMessageService pushMessageService;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @RequestMapping("createPushMessage")
    @RestReturn(Long.class)
    public RestResponse createPushMessage(CreatePushMessageCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        PushMessage pushMessage = ConvertHelper.convert(cmd, PushMessage.class);
        pushMessage.setStatus(PushMessageStatus.Ready.getCode());
        pushMessageService.createPushMessage(pushMessage);
        
        RestResponse res = new RestResponse();
        res.setResponseObject(pushMessage.getId());
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }
    
    @RequestMapping("updatePushMessage")
    @RestReturn(String.class)
    public RestResponse updatePushMessage(UpdatePushMessageCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse res = new RestResponse();
        
        PushMessage pushMessage = ConvertHelper.convert(cmd, PushMessage.class);
        if(pushMessageService.updatePushMessage(pushMessage)) {
            res.setErrorDescription("OK");
            res.setErrorCode(ErrorCodes.SUCCESS);
        } else {
            res.setErrorCode(ErrorCodes.ERROR_INVALID_PARAMETER);
            res.setErrorDescription("Invalid command id");
        }
        
        return res;
    }
    
    @RequestMapping("deletePushMessage")
    @RestReturn(String.class)
    public RestResponse deletePushMessage(DeletePushMessageCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.pushMessageService.deleteByPushMesageId(cmd.getId());
        
        RestResponse res = new RestResponse();
        res.setErrorDescription("OK");
        res.setErrorCode(ErrorCodes.SUCCESS);
        return res;
    }
    
    @RequestMapping("listPushMessage")
    @RestReturn(ListPushMessageResponse.class)
    public RestResponse listPushMessages(ListPushMessageCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse res = new RestResponse();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        List<PushMessage> msgs = pushMessageService.queryPushMessages(locator, pageSize);
        List<PushMessageDTO> dtos = msgs.stream().map((r) -> {
            PushMessageDTO dto = ConvertHelper.convert(r, PushMessageDTO.class);
            if(r.getStatus() == PushMessageStatus.Ready.getCode()) {
                dto.setPushCount(0l);
            }
            return dto;
        }).collect(Collectors.toList());
        
        ListPushMessageResponse respObj = new ListPushMessageResponse();
        respObj.setNextPageAnchor(locator.getAnchor());
        
        respObj.setPushMessages(dtos);
        res.setResponseObject(respObj);
        
        cmd.setPageAnchor(locator.getAnchor());
        return res;
    }
    
    @RequestMapping("listPushMessageResult")
    @RestReturn(ListPushMessageResultResponse.class)
    public RestResponse listPushMessages(ListPushMessageResultCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListPushMessageResultResponse respObj = new ListPushMessageResultResponse();
        RestResponse res = new RestResponse(respObj);
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<PushMessageResultDTO> dtos = this.pushMessageService.queryPushMessageResult(locator, cmd);
        respObj.setNextPageAnchor(locator.getAnchor());
        respObj.setPushMessages(dtos);
        
        return res;
        
    }
    
}
