package com.everhomes.pmtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.flow.node.FlowGraphNodeEnd;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue;
import com.everhomes.rest.general_approval.PostApprovalFormSubformValue;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PmtaskFlowModuleListener implements FlowModuleListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PmtaskFlowModuleListener.class);
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowProvider flowProvider;
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
	private PmTaskCommonServiceImpl pmTaskCommonService;
	@Autowired
	private PmTaskSearch pmTaskSearch;
	@Autowired
	private FlowUserSelectionProvider flowUserSelectionProvider;
	@Autowired
	private SmsProvider smsProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private PmTaskService pmTaskService;
	@Autowired
	private GeneralFormValProvider generalFormValProvider;
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	@Autowired
	private PortalService portalService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private AddressProvider addressProvider;

	private Long moduleId = FlowConstants.PM_TASK_MODULE;

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
		FlowCase flowCase = ctx.getFlowCase();
		PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
		task.setStatus(FlowCaseStatus.ABSORTED.getCode());
		pmTaskProvider.updateTask(task);
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);
	}

	//状态改变之后
	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		//当前节点已经变成上一个节点
//		FlowGraphNode currentNode = ctx.getPrefixNode();

		//业务的下一个节点是当前节点
		FlowGraphNode currentNode = ctx.getCurrentNode();

		String stepType = ctx.getStepType().getCode();

		if (null == currentNode)
			return;

		FlowNode currentFlowNode = currentNode.getFlowNode();
		FlowCase flowCase = ctx.getFlowCase();

		String params = currentFlowNode.getParams();

		PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
		Flow flow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		String tag1 = flow.getStringTag1();

		LOGGER.debug("update pmtask request, stepType={}, tag1={}", stepType, tag1);
		//当是下一步时，如果是end节点，直接return，如果是驳回，则不管下一个节点类型，都置任务状态为已取消
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {

			if (currentNode instanceof FlowGraphNodeEnd) {
				if (!StringUtils.isBlank(task.getReferType())){
					PmTaskListener listener = PlatformContext.getComponent(PmTaskListener.PMTASK_PREFIX + task.getReferType());
					listener.onTaskSuccess(task,task.getReferId());
				}
				return;
			}

			String nodeType = "";
			if (!StringUtils.isBlank(params)) {
				JSONObject paramJson = JSONObject.parseObject(params);
				nodeType = paramJson.getString("nodeType");
			}
			LOGGER.debug("update pmtask request, stepType={}, tag1={}, nodeType={}", stepType, tag1, nodeType);

			if ("HANDOVER".equals(nodeType)) {
				task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
				pmTaskProvider.updateTask(task);
				//通知第三方 config表中配置api请求地址
				//要求传的是转发项目经理填写的内容和图片 add by xiongying20170922
				FlowSubjectDTO subjectDTO = flowService.getSubectById(ctx.getCurrentEvent().getSubject().getId());
				pmTaskCommonService.handoverTaskToTrd(task, subjectDTO.getContent(), subjectDTO.getImages());
			}else if("MOTIFYFEE".equals(nodeType)){
				List<GeneralFormVal> vals = generalFormValProvider.queryGeneralFormVals(EntityType.PM_TASK.getCode(),flowCase.getReferId());
				//没产生费用
				if (vals==null || vals.size()==0){
					FlowEventLog log = new FlowEventLog();
					log.setId(flowEventLogProvider.getNextId());
					log.setFlowMainId(flowCase.getFlowMainId());
					log.setFlowVersion(flowCase.getFlowVersion());
					log.setNamespaceId(flowCase.getNamespaceId());
					log.setFlowNodeId(flowCase.getCurrentNodeId());
					log.setFlowCaseId(flowCase.getId());
					log.setStepCount(flowCase.getStepCount());
					log.setSubjectId(0L);
					log.setParentId(0L);
					log.setLogType(FlowLogType.NODE_TRACKER.getCode());
					log.setButtonFiredStep(FlowStepType.NO_STEP.getCode());
					log.setTrackerApplier(1L);
					log.setTrackerProcessor(1L);
					String content = "本次服务没有产生维修费";
					log.setLogContent(content);
					ctx.getLogs().add(log);
				}
				task.setIfUseFeelist((byte)1);
			}
		}
		pmTaskProvider.updateTask(task);
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);

	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		FlowCase flowCase = ctx.getFlowCase();
		PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
		PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(task.getId());
		if(null != order && null != order.getAmount())
			task.setAmount(order.getAmount());
		task.setStatus(FlowCaseStatus.FINISHED.getCode());
		pmTaskProvider.updateTask(task);
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);
	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {

	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {

		GetTaskDetailCommand cmd = new GetTaskDetailCommand();
		cmd.setId(flowCase.getReferId());
		cmd.setOwnerId(flowCase.getProjectId());
		cmd.setOwnerType(PmTaskOwnerType.COMMUNITY.getCode());

		PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());

		PmTaskDTO dto;
		//TODO:为科兴与一碑对接
		if(task.getNamespaceId() == 999983 &&
				task.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			EbeiPmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);
			dto = handler.getTaskDetail(cmd);
		}else {
			dto = pmTaskCommonService.getTaskDetail(cmd, false);
		}

//		企业名称和楼栋门牌
		if(null != dto.getEnterpriseId() && dto.getEnterpriseId() > 0){
			dto.setEnterpriseName("");
			dto.setEnterpriseAddress("");
			Organization org = organizationProvider.findOrganizationById(dto.getEnterpriseId());
			if(null != org){
				dto.setEnterpriseName(org.getName());
				List<OrganizationAddress> orgAddrs = organizationProvider.findOrganizationAddressByOrganizationId(org.getId());
				StringBuffer addrs = new StringBuffer();
				for (OrganizationAddress orgAddr : orgAddrs){
					Address addr =  addressProvider.findAddressById(orgAddr.getAddressId());
					if(null != addr && null != addr.getAddress()){
						addrs.append(addr.getAddress() + "\n");
					}
				}
				if(addrs.length() > 0){
					dto.setEnterpriseAddress(addrs.substring(0,addrs.length() - 1));
				}
			}
		}


		if (null != flowCase.getModuleType()){
			if(FlowModuleType.NO_MODULE.getCode().equals(flowCase.getModuleType())){
				dto.setFeeModel(configurationProvider.getValue(dto.getNamespaceId(),"pmtask.feeModel.6","0"));
			}else if(FlowModuleType.SUGGESTION_MODULE.getCode().equals(flowCase.getModuleType())){
				dto.setFeeModel(configurationProvider.getValue(dto.getNamespaceId(),"pmtask.feeModel.9","0"));
			}
		}


		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e;

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("服务类型");
		e.setValue(dto.getTaskCategoryName());
		entities.add(e);

		if (StringUtils.isNotBlank(dto.getCategoryName())) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("所属分类");
			e.setValue(dto.getCategoryName());
			entities.add(e);
		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("服务内容");
		e.setValue(dto.getContent());
		entities.add(e);

		if (null != dto.getAttachments()) {
			for(PmTaskAttachmentDTO s: dto.getAttachments()) {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
				e.setKey("图片附件");
				e.setValue(s.getContentUrl());
				entities.add(e);
			}
		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("服务地点");
		e.setValue(dto.getAddress());
		entities.add(e);


		String name = dto.getRequestorName();
		String phone = dto.getRequestorPhone();
		//代发时填写代发人信息
		if (task.getOrganizationUid() != null) {
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(task.getOrganizationUid(), IdentifierType.MOBILE.getCode());
			OrganizationMember member = null;
			if (task.getOrganizationId() != null) {
				member = organizationProvider.findOrganizationMemberByOrgIdAndToken(userIdentifier.getIdentifierToken(), task.getOrganizationId());
			}
			if (member!=null){
				name = member.getContactName();
				phone = userIdentifier.getIdentifierToken();
			}else{
				phone = userIdentifier.getIdentifierToken();
				name = userProvider.findUserById(task.getOrganizationUid()).getNickName();
			}

		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("联系人");
		e.setValue(dto.getRequestorName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("联系人电话");
		e.setValue(dto.getRequestorPhone());
		entities.add(e);

		if(null != dto.getEnterpriseId() && dto.getEnterpriseId() > 0){
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("企业名称");
			e.setValue(dto.getEnterpriseName());
			entities.add(e);

			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("楼栋门牌");
			e.setValue(dto.getEnterpriseAddress());
			entities.add(e);
		}


//		代发情况才显示
		if (task.getOrganizationUid() != null) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("发起人");
			e.setValue(name);
			entities.add(e);

			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("发起人电话");
			e.setValue(phone);
			entities.add(e);
		}

		//TODO:为科兴与一碑对接
		if(dto.getNamespaceId() == 999983 &&
				dto.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("状态");
			e.setValue(EbeiPmTaskStatus.fromCode(dto.getStatus()).getDesc());
			entities.add(e);
		}

		PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(task.getId());
		List<PmTaskOrderDetail> products = pmTaskProvider.findOrderDetailsByTaskId(null,null,null,task.getId());
		PmTaskOrderDTO orderdto = new PmTaskOrderDTO();
		if(null != order){
			orderdto = ConvertHelper.convert(order,PmTaskOrderDTO.class);
			orderdto.setProducts(products.stream().map(r->ConvertHelper.convert(r,PmTaskOrderDetailDTO.class)).collect(Collectors.toList()));

			if (task.getStatus().equals(PmTaskFlowStatus.COMPLETED.getCode()) || task.getStatus().equals(PmTaskFlowStatus.CONFIRMED.getCode())){
				if (null != orderdto.getServiceFee()){
					e = new FlowCaseEntity();
					e.setEntityType(FlowCaseEntityType.LIST.getCode());
					e.setKey("服务费");
					BigDecimal serviceFee = BigDecimal.valueOf(order.getServiceFee());
					e.setValue(serviceFee.movePointLeft(2).toString() + "元");
					entities.add(e);
				}
				if(null != products && products.size() > 0){
					e = new FlowCaseEntity();
					e.setEntityType(FlowCaseEntityType.LIST.getCode());
					e.setKey("物品费");
					BigDecimal productFee = BigDecimal.valueOf(order.getProductFee());
					e.setValue(productFee.movePointLeft(2).toString() + "元");
					entities.add(e);
				}
			}
		}




		//填写费用清单
//		List<GeneralFormVal> list = generalFormValProvider.queryGeneralFormVals(EntityType.PM_TASK.getCode(),task.getId());
//		if (task.getIfUseFeelist()!=null && task.getIfUseFeelist()==1)
//			if (flowCase.getStatus() == FlowCaseStatus.FINISHED.getCode())
//				if (products!=null && products.size()>0){
//					e = new FlowCaseEntity();
//					e.setEntityType(FlowCaseEntityType.TEXT.getCode());
//					e.setKey("费用清单");
//					String content = "";
//					List<PostApprovalFormItem> items = list.stream().map(p->ConvertHelper.convert(p, PostApprovalFormItem.class))
//							.collect(Collectors.toList());
//					content += "本次服务的费用清单如下，请进行确认\n";
//					Long total = Long.valueOf(getTextString(getFormItem(items,"总计").getFieldValue()));
//					BigDecimal total = BigDecimal.valueOf(order.getAmount());
//					content += "总计:"+total.movePointLeft(2).toString()+"元\n";
//					Long serviceFee = Long.valueOf(getTextString(getFormItem(items,"服务费").getFieldValue()));
//					BigDecimal serviceFee = BigDecimal.valueOf(order.getServiceFee());
//					content += "服务费:"+serviceFee.movePointLeft(2).toString()+"元\n";
//					BigDecimal productFee = BigDecimal.valueOf(order.getProductFee());
//					content += "物品费:"+ productFee.movePointLeft(2) +"元\n";
//					PostApprovalFormItem subForm = getFormItem(items,"物品");
//					if (subForm!=null) {
//						PostApprovalFormSubformValue subFormValue = JSON.parseObject(subForm.getFieldValue(), PostApprovalFormSubformValue.class);
//						List<PostApprovalFormSubformItemValue> array = subFormValue.getForms();
//						if (array.size()!=0) {
//							content += "物品费详情：\n";
//							Gson g=new Gson();
//							for (PostApprovalFormSubformItemValue itemValue : array){
//								List<PostApprovalFormItem> values = itemValue.getValues();
//								content += getTextString(getFormItem(values,"物品名称").getFieldValue())+":";
//								content += getTextString(getFormItem(values,"小计").getFieldValue())+"元";
//								content += "("+getTextString(getFormItem(values,"单价").getFieldValue())+"元*"+
//										getTextString(getFormItem(values,"数量").getFieldValue())+")";
//							}
//							content += "如对上述费用有疑义请附言说明";
//						}
//					}
//					if (order.getProductFee().doubleValue() > 0){
//						content += "物品费详情：\n";
//						for (PmTaskOrderDetail r : products) {
//							BigDecimal price = BigDecimal.valueOf(r.getProductPrice());
//							BigDecimal amount = BigDecimal.valueOf(r.getProductAmount());
//							content += r.getProductName() + ":";
//							content += price.multiply(amount).movePointLeft(2).toString() + "元";
//							content += "(" + price.movePointLeft(2).toString() + "元*" + amount.intValue() + ")";
//						}
//					}
//					content += "如对上述费用有疑义请附言说明";
//					e.setValue(content);
//					entities.add(e);
//				}else {
//					e = new FlowCaseEntity();
//					e.setEntityType(FlowCaseEntityType.LIST.getCode());
//					e.setKey("费用清单");
//					e.setValue("本次服务没有产生维修费");
//					entities.add(e);
//				}

		pmTaskProvider.findPmTaskOrderById(task.getId());
		JSONObject jo = JSONObject.parseObject(JSONObject.toJSONString(dto));
		jo.put("formUrl",processFormURL(EntityType.PM_TASK.getCode(),""+task.getId(),FlowOwnerType.PMTASK.getCode(),"","费用确认"));
		if (flowUserType!=null)
			jo.put("flowUserType",flowUserType.getCode());
		flowCase.setCustomObject(jo.toJSONString());

		return entities;
	}

	private String getTextString(String json){
		if (StringUtils.isEmpty(json))
			return "";
		return JSONObject.parseObject(json).getString("text");
	}

	private String processFormURL(String sourceType, String sourceId, String ownerType,String ownerId,String displayName) {
		return "zl://form/create?sourceType="+sourceType+"&sourceId="+sourceId+"&ownerType="+ownerType+"&ownerId="+ownerId
				+"&displayName="+displayName+"&metaObject=";
	}

	private PostApprovalFormItem getFormItem(List<PostApprovalFormItem> values,String name){
		for (PostApprovalFormItem p:values)
			if (p.getFieldName().equals(name))
				return p;
		return null;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		return null;
	}

	//fireButton 之前
	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {

		FlowGraphNode currentNode = ctx.getCurrentNode();
		FlowNode flowNode = currentNode.getFlowNode();
		FlowCase flowCase = ctx.getFlowCase();
		FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
		flowCase = tree.getLeafNodes().get(0).getFlowCase();//获取真正正在进行的flowcase
		flowNode = ctx.getFlowGraph().getGraphNode(flowCase.getCurrentNodeId()).getFlowNode();

		String stepType = ctx.getStepType().getCode();
		String params = flowNode.getParams();
//motify by st.zheng 节点参数改为非必填
//		if(StringUtils.isBlank(params)) {
//			LOGGER.error("Invalid flowNode param.");
//			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
//					"Invalid flowNode param.");
//		}

		String nodeType = "";
		if (!StringUtils.isBlank(params)) {
			JSONObject paramJson = JSONObject.parseObject(params);
			nodeType = paramJson.getString("nodeType");
		}


		LOGGER.debug("update pmtask request, stepType={}, nodeType={}", stepType, nodeType);
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
			if ("ASSIGNING".equals(nodeType)) {
				FlowGraphEvent evt = ctx.getCurrentEvent();
				if(evt != null) {
					for(FlowEntitySel es : evt.getEntitySel()) {
						//update by janson
						if(!FlowEntityType.FLOW_SELECTION.getCode().equals(es.getFlowEntityType())) {
							continue;
						}
						PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
						FlowUserSelection sel = flowUserSelectionProvider.getFlowUserSelectionById(es.getEntityId());
						Long targetId = sel.getSourceIdA();

						PmTaskLog pmTaskLog = new PmTaskLog();
						pmTaskLog.setNamespaceId(task.getNamespaceId());
						pmTaskLog.setOperatorTime(new Timestamp(System.currentTimeMillis()));
						pmTaskLog.setOperatorUid(UserContext.current().getUser().getId());
						pmTaskLog.setOwnerId(task.getOwnerId());
						pmTaskLog.setOwnerType(task.getOwnerType());
						pmTaskLog.setStatus(PmTaskFlowStatus.PROCESSING.getCode());
						pmTaskLog.setTargetId(targetId);
						pmTaskLog.setTargetType(PmTaskTargetType.USER.getCode());
						pmTaskLog.setTaskId(task.getId());
						pmTaskProvider.createTaskLog(pmTaskLog);
					}

				}
			}
		}else if(FlowStepType.ABSORT_STEP.getCode().equals(stepType)) {
			PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
			//TODO:为科兴与一碑对接
			if(task.getNamespaceId() == 999983 &&
					task.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
				EbeiPmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);
				CancelTaskCommand command = new CancelTaskCommand();
				command.setId(task.getId());
				command.setOwnerId(task.getOwnerId());
				command.setOwnerType(task.getOwnerType());
				handler.cancelTask(command);

				FlowAutoStepDTO stepDTO = ConvertHelper.convert(flowCase, FlowAutoStepDTO.class);
				stepDTO.setFlowCaseId(flowCase.getId());
				stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
				stepDTO.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
				flowService.processAutoStep(stepDTO);
				ctx.setContinueFlag(false);

			}else if ("ASSIGNING".equals(nodeType)) {
				FlowAutoStepDTO stepDTO = ConvertHelper.convert(flowCase, FlowAutoStepDTO.class);
				stepDTO.setFlowCaseId(flowCase.getId());
				stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
				stepDTO.setAutoStepType(FlowStepType.END_STEP.getCode());
                FlowSubject subject = ctx.getCurrentEvent().getSubject();
                if (subject != null) {
                    stepDTO.setSubjectId(subject.getId());
                }
				flowService.processAutoStep(stepDTO);
				ctx.setContinueFlag(false);


//				task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
				task.setStatus(FlowCaseStatus.FINISHED.getCode());
				pmTaskProvider.updateTask(task);
				pmTaskSearch.feedDoc(task);
			}
		}else if(FlowStepType.NO_STEP.getCode().equals(stepType)) {
//			按钮参数
			String btnParam = ctx.getFlowGraph().getGraphButton(ctx.getCurrentEvent().getFiredButtonId()).getFlowButton().getParam();
			String btnNodeType = "";
			if (!StringUtils.isBlank(btnParam)) {
				JSONObject paramJson = JSONObject.parseObject(btnParam);
				btnNodeType = paramJson.getString("nodeType");
			}
//			发起人
//			ctx.getFlowCase().getApplyUserId();
//			处理人
//			ctx.getOperator().getId();
			if ("MOTIFYFEE".equals(btnNodeType)) {
//				FlowGraphEvent evt = ctx.getCurrentEvent();
//				if (FlowUserType.APPLIER.equals(evt.getUserType())){
//					LOGGER.info("nextStep:"+JSONObject.toJSONString(flowCase));
//					FlowAutoStepDTO dto = new FlowAutoStepDTO();
//					dto.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//					dto.setFlowCaseId(flowCase.getId());
//					dto.setFlowMainId(flowCase.getFlowMainId());
//					dto.setFlowNodeId(flowCase.getCurrentNodeId());
//					dto.setFlowVersion(flowCase.getFlowVersion());
//					dto.setStepCount(flowCase.getStepCount());
//					flowService.processAutoStep(dto);
//				}
			} else if ("CONFIRMFEE".equals(btnNodeType)){
// 费用确认客户端调用业务接口
//				PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
//				PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(task.getId());
//				task.setStatus(PmTaskFlowStatus.COMPLETED.getCode());
//				task.setAmount(order.getAmount());
//				pmTaskProvider.updateTask(task);
//				pmTaskSearch.feedDoc(task);
//
//				LOGGER.info("nextStep:"+JSONObject.toJSONString(flowCase));
//				FlowAutoStepDTO dto = new FlowAutoStepDTO();
//				dto.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//				dto.setFlowCaseId(flowCase.getId());
//				dto.setFlowMainId(flowCase.getFlowMainId());
//				dto.setFlowNodeId(flowCase.getCurrentNodeId());
//				dto.setFlowVersion(flowCase.getFlowVersion());
//				dto.setStepCount(flowCase.getStepCount());
//				flowService.processAutoStep(dto);
			} else if ("NEEDFEE".equals(btnNodeType)){
				LOGGER.info("nextStep:"+JSONObject.toJSONString(flowCase));
				FlowAutoStepDTO dto = new FlowAutoStepDTO();
				dto.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
				dto.setFlowCaseId(flowCase.getId());
				dto.setFlowMainId(flowCase.getFlowMainId());
				dto.setFlowNodeId(flowCase.getCurrentNodeId());
				dto.setFlowVersion(flowCase.getFlowVersion());
				dto.setStepCount(flowCase.getStepCount());
				flowService.processAutoStep(dto);
			}


		}

	}


	//同步数据到科技园
	private void synchronizedTaskToTechpark(PmTask task, Long targetId, Long organizationId) {
		UserContext context = UserContext.current();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		if(namespaceId == 1000000) {
			TechparkSynchronizedServiceImpl handler = PlatformContext.getComponent("techparkSynchronizedServiceImpl");
			handler.pushToQueque(task.getId() + "," + targetId + "," + organizationId);
		}
	}

	@Override
	public void onFlowCreating(Flow flow) {


	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {

	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {

	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
										List<Tuple<String, Object>> variables) {
		FlowCase flowCase = ctx.getFlowCase();
		PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
		PmTaskCategory category = null;
		//Todo:为科兴与一碑对接
		if (task.getNamespaceId()==999983 && null!= task.getTaskCategoryId() &&
				task.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			category = new PmTaskCategory();
			category.setName("物业报修");
		}else
			category = pmTaskProvider.findCategoryById(task.getTaskCategoryId());

		if (SmsTemplateCode.PM_TASK_CREATOR_CODE == templateId) {

			smsProvider.addToTupleList(variables, "operatorName", task.getRequestorName());
			smsProvider.addToTupleList(variables, "operatorPhone", task.getRequestorPhone());
			smsProvider.addToTupleList(variables, "categoryName", category.getName());
		}else if (SmsTemplateCode.PM_TASK_FLOW_ASSIGN_CODE == templateId) {
			//分配任务
			List<PmTaskLog> logs = pmTaskProvider.listPmTaskLogs(task.getId(), PmTaskFlowStatus.PROCESSING.getCode());

			if (logs.size() != 0) {
				Long targetId = logs.get(0).getTargetId();

				User targetUser = userProvider.findUserById(targetId);
				UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(targetId, IdentifierType.MOBILE.getCode());

				smsProvider.addToTupleList(variables, "creatorName", task.getRequestorName());
				smsProvider.addToTupleList(variables, "creatorPhone", task.getRequestorPhone());
				smsProvider.addToTupleList(variables, "operatorName", targetUser.getNickName());
				smsProvider.addToTupleList(variables, "operatorPhone", targetIdentifier.getIdentifierToken());
			}
		}else if (SmsTemplateCode.PM_TASK_ACCEPTING_NODE_SUPERVISE_CODE == templateId) {
			smsProvider.addToTupleList(variables, "operatorName", task.getRequestorName());
			smsProvider.addToTupleList(variables, "operatorPhone", task.getRequestorPhone());
			smsProvider.addToTupleList(variables, "categoryName", category.getName());
		}else if (SmsTemplateCode.PM_TASK_ASSIGN_NODE_CODE == templateId) {
			smsProvider.addToTupleList(variables, "operatorName", task.getRequestorName());
			smsProvider.addToTupleList(variables, "operatorPhone", task.getRequestorPhone());
			smsProvider.addToTupleList(variables, "categoryName", category.getName());
		}else if (SmsTemplateCode.PM_TASK_ASSIGN_NODE_SUPERVISE_CODE == templateId) {
			smsProvider.addToTupleList(variables, "operatorName", task.getRequestorName());
			smsProvider.addToTupleList(variables, "operatorPhone", task.getRequestorPhone());
			smsProvider.addToTupleList(variables, "categoryName", category.getName());
		}else if (SmsTemplateCode.PM_TASK_PROCESSING_BUTTON_APPROVE_CODE == templateId) {
			smsProvider.addToTupleList(variables, "categoryName", category.getName());
		}

	}

	@Override
	public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {

		List<ServiceModuleAppDTO> apps = new ArrayList<>();
		for (Long id: PmTaskAppType.TYPES) {
			ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
			listServiceModuleAppsCommand.setNamespaceId(namespaceId);
			listServiceModuleAppsCommand.setModuleId(FlowConstants.PM_TASK_MODULE);
			listServiceModuleAppsCommand.setCustomTag(String.valueOf(id));
			ListServiceModuleAppsResponse app = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
			if (app!=null && app.getServiceModuleApps().size()>0)
				apps.addAll(app.getServiceModuleApps());
		}
		return apps.stream().map(c -> {
			FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
			dto.setId(c.getOriginId());
			dto.setNamespaceId(namespaceId);
			dto.setServiceName(c.getName());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
		List<FlowConditionVariableDTO> list = new ArrayList<>();
		FlowConditionVariableDTO dto = new FlowConditionVariableDTO();
		dto.setDisplayName("报修类型");
		dto.setValue("taskCategoryId");
		dto.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		dto.setOperators(new ArrayList<>());
		dto.getOperators().add(FlowConditionRelationalOperatorType.EQUAL.getCode());
		Integer namespaceId = UserContext.getCurrentNamespaceId(flow.getNamespaceId());
		ListTaskCategoriesCommand cmd = new ListTaskCategoriesCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setOrganizationId(flow.getOrganizationId());
//		if (flow.getModuleType().equals(FlowModuleType.NO_MODULE.getCode()))
//			cmd.setTaskCategoryId(PmTaskAppType.REPAIR_ID);
//		else
//			cmd.setTaskCategoryId(PmTaskAppType.SUGGESTION_ID);
		cmd.setAppId(Long.valueOf(flow.getModuleType()));
		ListTaskCategoriesResponse response = pmTaskService.listTaskCategories(cmd);
		dto.setOptions(new ArrayList<>());
		if(null == response.getRequests()){
			LOGGER.error("Categories is null.");
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NULL,
					"Categories is null.");
		}
		response.getRequests().forEach(p-> dto.getOptions().add(p.getName()));
		list.add(dto);
		return list;
	}

	@Override
	public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String entityType, Long entityId, String extra) {
		//目前只有类型一个分支参数
		if ("taskCategoryId".equals(variable)) {
			FlowCase flowcase = ctx.getFlowCase();
			PmTask pmTask = pmTaskProvider.findTaskById(flowcase.getReferId());
			PmTaskCategory category = pmTaskProvider.findCategoryById(pmTask.getTaskCategoryId());
			FlowConditionStringVariable flowConditionStringVariable = new FlowConditionStringVariable(category.getName());
			return flowConditionStringVariable;
		}
		return null;
	}

	@Override
	public void onFlowCaseEvaluate(FlowCaseState ctx, List<FlowEvaluate> evaluates) {
	    Long flowCaseId = evaluates.get(0).getFlowCaseId();
	    if(null != flowCaseId){
	        PmTask task = pmTaskProvider.findTaskByFlowCaseId(flowCaseId);
	        Double avgEval = evaluates.stream().collect(Collectors.averagingDouble(FlowEvaluate::getStar));
	        BigDecimal avg = BigDecimal.valueOf(avgEval);
	        task.setStar(avg.setScale(1).toString());
	        pmTaskProvider.updateTask(task);
            pmTaskSearch.feedDoc(task);
        }
	}
}