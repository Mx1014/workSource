package com.everhomes.parking;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.parking.IssueParkingCardsCommand;
import com.everhomes.rest.parking.ParkingCardIssueFlag;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingNotificationTemplateCode;
import com.everhomes.rest.parking.SetParkingCardIssueFlagCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ParkingFlowModuleListener implements FlowModuleListener {
	@Autowired
	private FlowService flowService;
	@Autowired
    private ParkingProvider parkingProvider;
	
	private Long moduleId = 111l;
	
	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		FlowModuleDTO moduleDTO = flowService.getModuleById(moduleId);
		module.setModuleName(moduleDTO.getDisplayName());
		module.setModuleId(moduleId);
		return module;
	}

	@Override
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("test-list-key");
		e.setValue("test-list-value");
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("test-list-key2");
		e.setValue("test-list-value2");
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("test-multi-key3");
		e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.TEXT.getCode());
		e.setKey("test-text-key2");
		e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
		entities.add(e);		
		
		return entities;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		
		FlowGraphNode currentNode = ctx.getCurrentNode();
		FlowNode flowNode = currentNode.getFlowNode();

		String stepType = flowNode.getAutoStepType();
		String param = flowNode.getParams();
		
		Long flowId = flowNode.getFlowMainId();
		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestByFlowId(flowId, 
				flowNode.getFlowVersion());
		
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
			if("QUEUEING".equals(param)) {
				if(flowId == 1) {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.PROCESSING.getCode());
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}else {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}
			}else if("PROCESSING".equals(param)) {
				if(flowId == 1) {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}
			}
//			else if("canceled".equals(param)) {
//				parkingCardRequest.setStatus(ParkingCardRequestStatus.INACTIVE.getCode());
//				parkingProvider.updateParkingCardRequest(parkingCardRequest);
//			}else if("agreement".equals(param)) {
//				parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
//				parkingProvider.updateParkingCardRequest(parkingCardRequest);
//			}
			else if("SUCCEED".equals(param)) {
				parkingCardRequest.setStatus(ParkingCardRequestStatus.OPENED.getCode());
				parkingProvider.updateParkingCardRequest(parkingCardRequest);
			}
		}else if(FlowStepType.REJECT_STEP.getCode().equals(stepType)) {
			parkingCardRequest.setStatus(ParkingCardRequestStatus.INACTIVE.getCode());
			parkingProvider.updateParkingCardRequest(parkingCardRequest);
		}
		
		
		
	}

//	@Override
//	public void issueParkingCards(IssueParkingCardsCommand cmd) {
//		
//		if(cmd.getCount() == null) {
//        	LOGGER.error("Count cannot be null.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"Count cannot be null.");
//        }
//		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
//        
//		StringBuilder strBuilder = new StringBuilder();
//		// 补上ownerType、ownerId、parkingLotId参数，以区分清楚是哪个小区哪个停车场的车牌，否则会发放其它园区的车牌 by lqs 20161103
//    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(null, cmd.getOwnerType(), 
//    			cmd.getOwnerId(), cmd.getParkingLotId(), null, ParkingCardRequestStatus.QUEUEING.getCode(),
//    			null, null, cmd.getCount())
//    			.stream().map(r -> {
//    				r.setStatus(ParkingCardRequestStatus.NOTIFIED.getCode());
//    				if(strBuilder.length() > 0) {
//    				    strBuilder.append(", ");
//    				}
//    				strBuilder.append(r.getId());
//					return r;
//    			}).collect(Collectors.toList());
//    	
//    	parkingProvider.updateParkingCardRequest(list);
//    	// 添加日志，方便定位哪个车牌被修改状态了（即发放了） by lqs 20161103
//    	if(LOGGER.isDebugEnabled()) {
//    	    LOGGER.debug("Issue parking cards, requestIds=[{}]", strBuilder.toString());
//    	}
//    	Integer namespaceId = UserContext.getCurrentNamespaceId();
//    	Map<String, Object> map = new HashMap<String, Object>();
//		String deadline = deadline(parkingLot.getCardReserveDays());
//	    map.put("deadline", deadline);
//		String scope = ParkingNotificationTemplateCode.SCOPE;
//		int code = ParkingNotificationTemplateCode.USER_APPLY_CARD;
//		String locale = "zh_CN";
//		String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(namespaceId, scope, code, locale, map, "");
//		list.forEach(applier -> {
//			sendMessageToUser(applier.getRequestorUid(), notifyTextForApplicant);
//		});
//    	
//    	
//	}
	
//	private void setParkingCardIssueFlag(SetParkingCardIssueFlagCommand cmd){
//		
//        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
//        if(cmd.getId() == null){
//        	LOGGER.error("Id cannot be null.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"Id cannot be null.");
//        }
//        
//        ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getId());
//        if(parkingCardRequest == null){
//        	LOGGER.error("ParkingCardRequest not found, cmd={}", cmd);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//				"ParkingCardRequest not found");
//        }
//        if(parkingCardRequest.getStatus() != ParkingCardRequestStatus.NOTIFIED.getCode()){
//        	LOGGER.error("ParkingCardRequest status is not notified, cmd={}", cmd);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//				"ParkingCardRequest status is not notified.");
//        }
//        //设置已领取状态和 领取时间
//        parkingCardRequest.setStatus(ParkingCardRequestStatus.ISSUED.getCode());
//        parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.ISSUED.getCode());
//        parkingCardRequest.setIssueTime(new Timestamp(System.currentTimeMillis()));
//        parkingProvider.updateParkingCardRequest(Collections.singletonList(parkingCardRequest));
//	}
}
