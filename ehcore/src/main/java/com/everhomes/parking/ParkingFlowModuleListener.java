package com.everhomes.parking;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.parking.ParkingAttachmentDTO;
import com.everhomes.rest.parking.ParkingAttachmentType;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingFlowConstant;
import com.everhomes.rest.parking.ParkingRequestFlowType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ParkingFlowModuleListener implements FlowModuleListener {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingFlowModuleListener.class);
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowProvider flowProvider;
	@Autowired
    private ParkingProvider parkingProvider;
	
	private Long moduleId = ParkingFlowConstant.PARKING_RECHARGE_MODULE;
	@Autowired
    private ContentServerService contentServerService;
	
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
//		FlowCaseEntity e = new FlowCaseEntity();
//		e.setEntityType(FlowCaseEntityType.LIST.getCode());
//		e.setKey("test-list-key");
//		e.setValue("test-list-value");
//		entities.add(e);
//		
//		e = new FlowCaseEntity();
//		e.setEntityType(FlowCaseEntityType.LIST.getCode());
//		e.setKey("test-list-key2");
//		e.setValue("test-list-value2");
//		entities.add(e);
//		
//		e = new FlowCaseEntity();
//		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//		e.setKey("test-multi-key3");
//		e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
//		entities.add(e);
//		
//		e = new FlowCaseEntity();
//		e.setEntityType(FlowCaseEntityType.TEXT.getCode());
//		e.setKey("test-text-key2");
//		e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
//		entities.add(e);
		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(flowCase.getReferId());
		
		ParkingCardRequestDTO dto = ConvertHelper.convert(parkingCardRequest, ParkingCardRequestDTO.class);
    	
		if(null != parkingCardRequest.getCarSerieId()) {
			ParkingCarSerie carSerie = parkingProvider.findParkingCarSerie(parkingCardRequest.getCarSerieId());
			if(null != carSerie) {
    			ParkingCarSerie secondCarSerie = parkingProvider.findParkingCarSerie(carSerie.getParentId());
    			if(null != secondCarSerie) {
    				ParkingCarSerie carBrand = parkingProvider.findParkingCarSerie(secondCarSerie.getParentId());
    				dto.setCarSerieName(carSerie.getName());
    				if(null != carBrand)
    					dto.setCarBrand(carBrand.getName());
    			}
			}
		}
		
    	List<ParkingAttachment> attachments = parkingProvider.listParkingAttachments(parkingCardRequest.getId(), 
    			ParkingAttachmentType.PARKING_CARD_REQUEST.getCode());
    	
		List<ParkingAttachmentDTO> attachmentDtos =  attachments.stream().map(r -> {
			ParkingAttachmentDTO attachmentDto = ConvertHelper.convert(r, ParkingAttachmentDTO.class);
			
			String contentUrl = getResourceUrlByUir(r.getContentUri(), 
	                EntityType.USER.getCode(), r.getCreatorUid());
			attachmentDto.setContentUrl(contentUrl);
			attachmentDto.setInformationType(r.getDataType());
			return attachmentDto;
		}).collect(Collectors.toList());
    	
		dto.setAttachments(attachmentDtos);
		
		Integer count = parkingProvider.waitingCardCount(parkingCardRequest.getOwnerType(), parkingCardRequest.getOwnerId(),
				parkingCardRequest.getParkingLotId(), parkingCardRequest.getCreateTime());
		dto.setRanking(count);
		
		flowCase.setCustomObject(JSONObject.toJSONString(dto));
		return entities;
	}

	private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType, ownerId, e);
            }
        }
        
        return url;
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
		FlowCase flowCase = ctx.getFlowCase();

		String stepType = ctx.getStepType().getCode();
		String param = flowNode.getParams();
		
		Long flowId = flowNode.getFlowMainId();
		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(flowCase.getReferId());
		Flow flow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		String tag1 = flow.getStringTag1();
		
		long now = System.currentTimeMillis();
		LOGGER.debug("update parking request, stepType={}, tag1={}, param={}", stepType, tag1, param);
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
			if("AUDITING".equals(param)) {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
					parkingCardRequest.setAuditSucceedTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
			}
			else if("QUEUEING".equals(param)) {
				
				ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(parkingCardRequest.getOwnerType(), 
						parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId(), flowId);
				Integer requestedCount = parkingProvider.countParkingCardRequest(parkingCardRequest.getOwnerType(), 
						parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId(), flowId, 
						ParkingCardRequestStatus.SUCCEED.getCode(), null);
				
				Integer totalCount = parkingFlow.getMaxIssueNum();
				Integer surplusCount = totalCount - requestedCount;
				if(surplusCount <= 0) {
					LOGGER.error("surplusCount is 0.");
		    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_ISSUE_CARD,
		    				"surplusCount is 0.");
				}
				if(ParkingRequestFlowType.QUEQUE.getCode() == Integer.valueOf(tag1)) {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.PROCESSING.getCode());
					parkingCardRequest.setIssueTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}else {
					LOGGER.debug("update parking request, stepType={}, tag1={}", stepType, tag1);
					parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
					parkingCardRequest.setProcessSucceedTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}
			}else if("PROCESSING".equals(param)) {
				if(ParkingRequestFlowType.QUEQUE.getCode() == Integer.valueOf(tag1)) {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
					parkingCardRequest.setProcessSucceedTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}
			}
		}else if(FlowStepType.ABSORT_STEP.getCode().equals(stepType)) {
			if("SUCCEED".equals(param)) {
				parkingCardRequest.setStatus(ParkingCardRequestStatus.OPENED.getCode());
				parkingCardRequest.setOpenCardTime(new Timestamp(now));
				parkingProvider.updateParkingCardRequest(parkingCardRequest);
			}else {
				parkingCardRequest.setStatus(ParkingCardRequestStatus.INACTIVE.getCode());
				parkingCardRequest.setCancelTime(new Timestamp(now));
				parkingProvider.updateParkingCardRequest(parkingCardRequest);
			}
			
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
