// @formatter:off
package com.everhomes.ui.organization;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.organization.ListTopicsByTypeCommand;
import com.everhomes.rest.ui.organization.ListTaskPostsCommand;
import com.everhomes.rest.ui.organization.ListTaskPostsResponse;
import com.everhomes.rest.ui.organization.ProcessingTaskCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.WebTokenGenerator;

/**
 * <ul>
 * <li>客户端的机构相关api</li>
 * </ul>
 */
@RestDoc(value="OrganizationUi controller", site="organizationUi")
@RestController
@RequestMapping("/ui/org")
public class OrganizationUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrganizationService organizationService;
   
    /**
     * <b>URL: /ui/org/listMyTaskPostsByScene</b>
     * <p>获取我的任务贴</p>
     */
    @RequestMapping("listMyTaskPostsByScene")
    @RestReturn(value=ListTaskPostsResponse.class)
    public RestResponse listMyTaskPostsByScene(@Valid ListTaskPostsCommand cmd) {
        ListTopicsByTypeCommand command = ConvertHelper.convert(cmd, ListTopicsByTypeCommand.class);
        WebTokenGenerator webToken = WebTokenGenerator.getInstance();
        SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
		if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
			command.setOrganizationId(sceneToken.getEntityId());
		}
		command.setPageOffset(cmd.getPageAnchor());
		ListPostCommandResponse res = organizationService.listMyTaskTopics(command);
        ListTaskPostsResponse response = new ListTaskPostsResponse();
        response.setDtos(res.getPosts());
        response.setNextPageAnchor(res.getNextPageAnchor());
        
        RestResponse resp =  new RestResponse(response);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /ui/org/listTaskPostsByScene</b>
     * <p>任务贴列表</p>
     */
    @RequestMapping("listTaskPostsByScene")
    @RestReturn(value=ListTaskPostsResponse.class)
    public RestResponse listTaskPostsByScene(@Valid ListTaskPostsCommand cmd) {
		ListTopicsByTypeCommand command = ConvertHelper.convert(cmd, ListTopicsByTypeCommand.class);
	    WebTokenGenerator webToken = WebTokenGenerator.getInstance();
	    SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
		if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
			command.setOrganizationId(sceneToken.getEntityId());
		}
		command.setPageOffset(cmd.getPageAnchor());
		ListPostCommandResponse res = organizationService.listAllTaskTopics(command);
        ListTaskPostsResponse response = new ListTaskPostsResponse();
        response.setDtos(res.getPosts());
        response.setNextPageAnchor(res.getNextPageAnchor());
        
        RestResponse resp =  new RestResponse(response);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    
    /**
     * <b>URL: /ui/org/listGrabTaskTopics</b>
     * <p>抢单任务贴列表</p>
     */
    @RequestMapping("listGrabTaskTopics")
    @RestReturn(value=ListTaskPostsResponse.class)
    public RestResponse listGrabTaskTopics(@Valid ListTaskPostsCommand cmd) {
		ListTopicsByTypeCommand command = ConvertHelper.convert(cmd, ListTopicsByTypeCommand.class);
	    WebTokenGenerator webToken = WebTokenGenerator.getInstance();
	    SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
		if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
			command.setOrganizationId(sceneToken.getEntityId());
		}
		command.setPageOffset(cmd.getPageAnchor());
		ListPostCommandResponse res = organizationService.listGrabTaskTopics(command);
        ListTaskPostsResponse response = new ListTaskPostsResponse();
        response.setDtos(res.getPosts());
        response.setNextPageAnchor(res.getNextPageAnchor());
        
        RestResponse resp =  new RestResponse(response);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
   
    /**
     * <b>URL: /ui/org/acceptTask</b>
     * <p>接受任务</p>
     */
     @RequestMapping("acceptTask")
     @RestReturn(value=String.class)
     public RestResponse acceptTask(@Valid ProcessingTaskCommand cmd) {
    	WebTokenGenerator webToken = WebTokenGenerator.getInstance();
 	    SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
 		if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
 			organizationService.acceptTask(cmd, sceneToken.getEntityId());
 		}
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
     }
     
     /**
      * <b>URL: /ui/org/refuseTask</b>
      * <p>拒绝任务</p>
      */
      @RequestMapping("refuseTask")
      @RestReturn(value=String.class)
      public RestResponse refuseTask(@Valid ProcessingTaskCommand cmd) {
    	  WebTokenGenerator webToken = WebTokenGenerator.getInstance();
   	      SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
   	      if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
   			organizationService.refuseTask(cmd, sceneToken.getEntityId());
   	      }
    	  RestResponse res = new RestResponse();
    	  res.setErrorCode(ErrorCodes.SUCCESS);
    	  res.setErrorDescription("OK");
         
         return res;
      }
      
      /**
       * <b>URL: /ui/org/grabTask</b>
       * <p>抢单</p>
       */
       @RequestMapping("grabTask")
       @RestReturn(value=String.class)
       public RestResponse grabTask(@Valid ProcessingTaskCommand cmd) {
    	   WebTokenGenerator webToken = WebTokenGenerator.getInstance();
    	   SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
    	   if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
    		   organizationService.grabTask(cmd, sceneToken.getEntityId());
    	   }
    	   RestResponse res = new RestResponse();
    	   res.setErrorCode(ErrorCodes.SUCCESS);
           res.setErrorDescription("OK");
          
          return res;
       }
       
       /**
        * <b>URL: /ui/org/processingTask</b>
        * <p>处理</p>
        */
        @RequestMapping("processingTask")
        @RestReturn(value=String.class)
        public RestResponse processingTask(@Valid ProcessingTaskCommand cmd) {
           WebTokenGenerator webToken = WebTokenGenerator.getInstance();
      	   SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);
      	   if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
      		   organizationService.processingTask(cmd, sceneToken.getEntityId());
      	   }
           RestResponse res = new RestResponse();
           res.setErrorCode(ErrorCodes.SUCCESS);
           res.setErrorDescription("OK");
           
           return res;
        }
}
