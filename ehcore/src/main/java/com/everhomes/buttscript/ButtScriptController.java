package com.everhomes.buttscript;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.buttscript.bus.SystemEventButtListener;
import com.everhomes.buttscript.engine.ButtScriptSchedulerMain;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.buttscript.*;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * buttScript  Controller
 * @author huanglm
 *
 */
@RestDoc(value="ButtScript  controller", site="core")
@RestController
@RequestMapping("/buttScript")
public class ButtScriptController extends ControllerBase{

	private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptController.class);


	@Autowired
	private ButtScriptSchedulerMain buttScriptSchedulerMain ;

	@Autowired
	private UserProvider userProvider ;

	/**
     * <b>URL: /buttScript/buttScriptTest</b>
     * <p>测试调用接口</p>
     */
	@RequestMapping("buttScriptTest")
    @RestReturn(value=String.class)
	public RestResponse buttScriptTest( ButtScriptTestCommand cmd ) {
		LocalEvent event = new LocalEvent();
		LocalEventContext context = new LocalEventContext();

		if(cmd.getNamespaceId() == null){
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		Long userId = UserContext.currentUserId();
		if(!StringUtils.isBlank(cmd.getPhone())){
			UserIdentifier iden = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(),cmd.getPhone());
			userId = iden.getOwnerUid() ;
		}

		context.setUid(userId);
		context.setNamespaceId(cmd.getNamespaceId());
		event.setContext(context);

		event.setEntityType("User");
		event.setEntityId(userId);
		event.setEventName(cmd.getEventName());
		LOGGER.debug("event:" + event);
		String resultStr = buttScriptSchedulerMain.doButtScriptMethod(event);
        RestResponse response = new RestResponse(resultStr);
        setResponseSuccess(response);
        return response;
	}


	/**
	 * <p>设置response 成功信息</p>
	 * @param response
	 */
	private void setResponseSuccess(RestResponse response){
		if(response == null ) return ;
		
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
	}
}
