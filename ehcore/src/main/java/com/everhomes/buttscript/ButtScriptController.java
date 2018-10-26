package com.everhomes.buttscript;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.buttscript.engine.ButtScriptSchedulerMain;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.point.rpc.PointServiceRPCRest;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.buttscript.*;
import com.everhomes.rest.point.GetUserPointCommand;
import com.everhomes.rest.point.PointScoreDTO;
import com.everhomes.rest.user.FindUserByPhoneCommand;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.user.*;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
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

	@Autowired
	private UserService userService ;

	@Autowired
	private PointServiceRPCRest pointServiceRPCRest;


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
	 * 用于获取用户的会员信息及积分信息
	 * @param cmd
	 * @return
	 */
	@RequestMapping("buttScriptTest2GetUserInfo")
	@RestReturn(value=String.class)
	public RestResponse buttScriptTest2GetUserInfo( ButtScriptTestCommand cmd ) {

		if(cmd.getNamespaceId() == null){
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		//获取人员的会员等级及积分
		Long userId = null;
		JSONObject Job = new JSONObject();
		if(!StringUtils.isBlank(cmd.getPhone())){
			FindUserByPhoneCommand	fcmd = new 	FindUserByPhoneCommand();
			fcmd.setNamespaceId(cmd.getNamespaceId());
			fcmd.setPhone(cmd.getPhone());
			UserIdentifier iden = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(),cmd.getPhone());
			userId = iden.getOwnerUid() ;
			User user = userProvider.findUserById(userId);
			if(user != null && user.getId() != null){
				//查询积分
				PointScoreDTO pdto = null ;
				Job.put("name",user.getNickName());
				Job.put("phone",cmd.getPhone());
				Job.put("vipLevel",user.getVipLevelText());
				Job.put("vipLevelCode",user.getVipLevel());
				try {
					GetUserPointCommand gcmd = new GetUserPointCommand();
					gcmd.setNamespaceId(cmd.getNamespaceId());
					gcmd.setUid(user.getId());
					pdto = pointServiceRPCRest.getUserPoint(gcmd);
					if(pdto != null){
						Job.put("point",pdto.getScore());
					}
				}catch (Exception e) {
					LOGGER.error("get point exception");
					Job.put("point","get point error");
				}
			}
		}
		RestResponse response = new RestResponse(Job.toJSONString());
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
