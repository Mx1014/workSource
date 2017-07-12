package com.everhomes.parking;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.sms.SmsProvider;
import org.apache.commons.lang.StringUtils;
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
import com.everhomes.rest.flow.FlowCaseEntityType;
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
import com.everhomes.rest.parking.ParkingSupportRequestConfigStatus;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class ParkingFlowModuleListener implements FlowModuleListener {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingFlowModuleListener.class);
	private Long moduleId = ParkingFlowConstant.PARKING_RECHARGE_MODULE;

	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowProvider flowProvider;
	@Autowired
    private ParkingProvider parkingProvider;
	@Autowired
	private SmsProvider smsProvider;
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
		
		parkingCardRequest.setStatus(ParkingCardRequestStatus.INACTIVE.getCode());
		parkingCardRequest.setCancelTime(new Timestamp(System.currentTimeMillis()));
		parkingProvider.updateParkingCardRequest(parkingCardRequest);
		
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
		dto.setRanking(count + 1);
		
		flowCase.setCustomObject(JSONObject.toJSONString(dto));//StringHelper.toJsonString(dto)
		
		List<FlowCaseEntity> entities = new ArrayList<>();
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
		String params = flowNode.getParams();
		
		if(StringUtils.isBlank(params)) {
			LOGGER.error("Invalid flowNode param.");
    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
    				"Invalid flowNode param.");
		}
		
		JSONObject paramJson = JSONObject.parseObject(params);
		String nodeType = paramJson.getString("nodeType");
		
		Long flowId = flowNode.getFlowMainId();
		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(flowCase.getReferId());
		Flow flow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		String tag1 = flow.getStringTag1();
		
		long now = System.currentTimeMillis();
		LOGGER.debug("update parking request, stepType={}, tag1={}, nodeType={}", stepType, tag1, nodeType);
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
			if("AUDITING".equals(nodeType)) {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
					parkingCardRequest.setAuditSucceedTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
			}
			else if("QUEUEING".equals(nodeType)) {
				
				ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(parkingCardRequest.getOwnerType(), 
						parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId(), flowId);
				Integer issuedCount = parkingProvider.countParkingCardRequest(parkingCardRequest.getOwnerType(), 
						parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId(), flowId, 
						ParkingCardRequestStatus.SUCCEED.getCode(), null);
				
				if(null != parkingFlow && parkingFlow.getMaxIssueNumFlag() == ParkingSupportRequestConfigStatus.SUPPORT.getCode()) {
					Integer totalCount = parkingFlow.getMaxIssueNum();
					Integer surplusCount = totalCount - issuedCount;
					if(surplusCount <= 0) {
						LOGGER.error("surplusCount is 0.");
			    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_ISSUE_CARD,
			    				"surplusCount is 0.");
					}
				}
				
				if(ParkingRequestFlowType.INTELLIGENT.getCode().equals(Integer.valueOf(tag1))) {
					LOGGER.debug("update parking request, stepType={}, tag1={}", stepType, tag1);
					parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
					parkingCardRequest.setProcessSucceedTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}else {
					LOGGER.debug("update parking request, stepType={}, tag1={}", stepType, tag1);
					parkingCardRequest.setStatus(ParkingCardRequestStatus.PROCESSING.getCode());
					parkingCardRequest.setIssueTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}
			}else if("PROCESSING".equals(nodeType)) {
				
				ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(parkingCardRequest.getOwnerType(), 
						parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId(), flowId);
				Integer issuedCount = parkingProvider.countParkingCardRequest(parkingCardRequest.getOwnerType(), 
						parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId(), flowId, 
						ParkingCardRequestStatus.SUCCEED.getCode(), null);
				
				if(null != parkingFlow && parkingFlow.getMaxIssueNumFlag() == ParkingSupportRequestConfigStatus.SUPPORT.getCode()) {
					Integer totalCount = parkingFlow.getMaxIssueNum();
					Integer surplusCount = totalCount - issuedCount;
					if(surplusCount <= 0) {
						LOGGER.error("surplusCount is 0.");
			    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_ISSUE_CARD,
			    				"surplusCount is 0.");
					}
				}
				
				if(ParkingRequestFlowType.QUEQUE.getCode().equals(Integer.valueOf(tag1)) ||
						ParkingRequestFlowType.SEMI_AUTOMATIC.getCode().equals(Integer.valueOf(tag1))) {
					parkingCardRequest.setStatus(ParkingCardRequestStatus.SUCCEED.getCode());
					parkingCardRequest.setProcessSucceedTime(new Timestamp(now));
					parkingProvider.updateParkingCardRequest(parkingCardRequest);
				}
			}
		}else if(FlowStepType.ABSORT_STEP.getCode().equals(stepType)) {
			if("SUCCEED".equals(nodeType)) {
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

	@Override
	public void onFlowCreating(Flow flow) {
		// TODO Auto-generated method stub
		if("申请排队模式".equals(flow.getFlowName()))
			flow.setStringTag1("1");
		else if("半自动化模式".equals(flow.getFlowName()))
			flow.setStringTag1("2");
		else if("智能模式".equals(flow.getFlowName()))
			flow.setStringTag1("3");
		else
			flow.setStringTag1("1");

	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
		FlowCase flowCase = ctx.getFlowCase();
		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(flowCase.getReferId());
		ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingCardRequest.getParkingLotId());

		String parkingLotName = parkingLot.getName();
		String plateNumber = parkingCardRequest.getPlateNumber();

		if (SmsTemplateCode.PARKING_ENTER_QUEQUEING_NODE == templateId) {

			smsProvider.addToTupleList(variables, "parkingLotName", parkingLotName);

		}else if (SmsTemplateCode.PARKING_CANCEL_QUEQUEING == templateId){
			smsProvider.addToTupleList(variables, "parkingLotName", parkingLotName);
			smsProvider.addToTupleList(variables, "plateNumber", plateNumber);

		}else if (SmsTemplateCode.PARKING_ENTER_PROCESSING_NODE == templateId){
			smsProvider.addToTupleList(variables, "parkingLotName", parkingLotName);
			smsProvider.addToTupleList(variables, "plateNumber", plateNumber);
		}else if (SmsTemplateCode.PARKING_CANCEL_PROCESSING == templateId){
			smsProvider.addToTupleList(variables, "parkingLotName", parkingLotName);
			smsProvider.addToTupleList(variables, "plateNumber", plateNumber);
		}
	}
}
