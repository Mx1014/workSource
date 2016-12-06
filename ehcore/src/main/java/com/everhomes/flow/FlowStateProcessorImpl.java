package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowTimeoutStepDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.tables.pojos.EhFlowAttachments;
import com.everhomes.server.schema.tables.pojos.EhNewsAttachments;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component
public class FlowStateProcessorImpl implements FlowStateProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowStateProcessorImpl.class);
	
	@Autowired
	private FlowListenerManager flowListenerManager;
	
	@Autowired
	private FlowCaseProvider flowCaseProvider;
	
	@Autowired
	private FlowService flowService;
	
	@Autowired
	private FlowNodeProvider flowNodeProvider;
	
	@Autowired
	private FlowSubjectProvider flowSubjectProvider;
	
    @Autowired
    private AttachmentProvider attachmentProvider;
	
   @Autowired
   BigCollectionProvider bigCollectionProvider;
   
   @Autowired
   private UserService userService;
   
   @Autowired
   private UserProvider userProvider;
    
//	private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	
	@Override
	public FlowCaseState prepareStart(UserInfo logonUser, FlowCase flowCase) {
		FlowCaseState ctx = new FlowCaseState();
		if(flowCase == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists");
		}
		
		ctx.setFlowCase(flowCase);
		ctx.setModuleName(flowCase.getModuleName());
		
		FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		ctx.setFlowGraph(flowGraph);
		
		FlowGraphStartEvent event = new FlowGraphStartEvent();
		ctx.setCurrentEvent(event);
		ctx.setOperator(logonUser);
		
		return ctx;
	}
	
	@Override
	public FlowCaseState prepareStepTimeout(FlowTimeout ft) {
		FlowCaseState ctx = new FlowCaseState();
		FlowTimeoutStepDTO stepDTO = (FlowTimeoutStepDTO) StringHelper.fromJsonString(ft.getJson(), FlowTimeoutStepDTO.class);
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(stepDTO.getFlowCaseId());
		if(flowCase.getStepCount().equals(stepDTO.getStepCount()) 
				&& stepDTO.getFlowNodeId().equals(flowCase.getCurrentNodeId())) {
			
	    	User user = userProvider.findUserById(User.SYSTEM_UID);
	    	UserContext.current().setUser(user);
			
			ctx.setFlowCase(flowCase);
			ctx.setModuleName(flowCase.getModuleName());
			
			FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
			ctx.setFlowGraph(flowGraph);
			
			FlowGraphNode node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
			if(node == null) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flownode noexists");
			}
			ctx.setCurrentNode(node);
			
			UserInfo userInfo = userService.getUserInfo(User.SYSTEM_UID);
			ctx.setOperator(userInfo);
			FlowGraphStepTimeoutEvent event = new FlowGraphStepTimeoutEvent(stepDTO);
			ctx.setCurrentEvent(event);
			
			return ctx;
		}
		return null;
	}
	
	@Override
	public FlowCaseState prepareButtonFire(UserInfo logonUser, FlowFireButtonCommand cmd) {
		FlowCaseState ctx = new FlowCaseState();
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
		if(flowCase == null 
				|| flowCase.getStatus().equals(FlowCaseStatus.INVALID.getCode())
				|| flowCase.getStatus().equals(FlowCaseStatus.FINISHED.getCode())
				|| flowCase.getStatus().equals(FlowCaseStatus.ABSORTED.getCode())) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists");
		}
		ctx.setFlowCase(flowCase);
		ctx.setModuleName(flowCase.getModuleName());
		ctx.setOperator(logonUser);
		
		FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		ctx.setFlowGraph(flowGraph);
		
		FlowGraphNode node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
		if(node == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flownode noexists");
		}
		ctx.setCurrentNode(node);
		
		FlowGraphButton button = flowGraph.getGraphButton(cmd.getButtonId());
		FlowSubject subject = new FlowSubject();
		subject.setBelongEntity(FlowEntityType.FLOW_BUTTON.getCode());
		subject.setBelongTo(cmd.getButtonId());
		subject.setContent(cmd.getContent());
		subject.setNamespaceId(button.getFlowButton().getNamespaceId());
		subject.setStatus(FlowStatusType.VALID.getCode());
		subject.setTitle(cmd.getTitle());
		flowSubjectProvider.createFlowSubject(subject);
		
		if(null != cmd.getImages() && cmd.getImages().size() > 0) {
			List<Attachment> attachments = new ArrayList<>();
			for(String image : cmd.getImages()) {
				Attachment attach = new Attachment();
				attach.setContentType(NewsCommentContentType.IMAGE.getCode());
				attach.setContentUri(image);
				attach.setCreatorUid(logonUser.getId());
				attach.setOwnerId(subject.getId());
				attachments.add(attach);
			}
			attachmentProvider.createAttachments(EhFlowAttachments.class, attachments);
		}
		
		FlowGraphButtonEvent event = new FlowGraphButtonEvent();
		event.setUserType(FlowUserType.fromCode(button.getFlowButton().getFlowUserType()));
		event.setCmd(cmd);
		event.setSubject(subject);
		event.setFiredUser(ctx.getOperator());
		ctx.setCurrentEvent(event);
		
		return ctx;
	}
	
	@Override
	public void step(FlowCaseState ctx, FlowGraphEvent event) {
		boolean stepOK = true;
		FlowGraphNode currentNode = ctx.getCurrentNode();
		if(event != null) {
			event.fire(ctx);
		}
		FlowGraphNode nextNode = ctx.getNextNode();
		if(ctx.getStepType() != null && currentNode != nextNode) {
			try {
				if(currentNode != null) {
					//Only leave once time
					currentNode.stepLeave(ctx, nextNode);	
				}
				
				ctx.setPrefixNode(currentNode);
				ctx.setCurrentNode(nextNode);
				ctx.setNextNode(null);
				
				//create processor's
				flowService.createSnapshotNodeProcessors(ctx, nextNode);	
				
				nextNode.stepEnter(ctx, currentNode);	
				
				stepOK = true;
			} catch(FlowStepErrorException ex) {
				stepOK = false;
			}
		}
		
		//Now save info to databases here
		if(stepOK) {
			flowService.flushState(ctx);
		} else {
			LOGGER.warn("step error flowCaseId=" + ctx.getFlowCase().getId());
		}
	}
}
