package com.everhomes.pmtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.flow.node.FlowGraphNodeEnd;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue;
import com.everhomes.rest.general_approval.PostApprovalFormSubformValue;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	private CategoryProvider categoryProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private ConfigurationProvider configProvider;
	@Autowired
	private PmTaskService pmTaskService;
	@Autowired
	private GeneralFormValProvider generalFormValProvider;

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

			if (currentNode instanceof FlowGraphNodeEnd)
				return;
//motify by st.zheng 修改为非每个节点都必须配参数值
//			if(StringUtils.isBlank(params)) {
//				LOGGER.error("Invalid flowNode param.");
//				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
//						"Invalid flowNode param.");
//			}
			String nodeType = "";
			if (!StringUtils.isBlank(params)) {
				JSONObject paramJson = JSONObject.parseObject(params);
				nodeType = paramJson.getString("nodeType");
			}
			LOGGER.debug("update pmtask request, stepType={}, tag1={}, nodeType={}", stepType, tag1, nodeType);

			if ("ACCEPTING".equals(nodeType)) {
				task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
				pmTaskProvider.updateTask(task);

			}else if ("ASSIGNING".equals(nodeType)) {

				task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
				pmTaskProvider.updateTask(task);

			}else if ("PROCESSING".equals(nodeType)) {
				task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
				task.setProcessingTime(new Timestamp(System.currentTimeMillis()));
				pmTaskProvider.updateTask(task);

				//TODO: 同步数据到科技园 （当受理之后才同步） 此处由于不好获取工作流中分配的人，所以当节点值（ASSIGNING）待分配时
				// 在fireButton存在eh_pm_task_logs表中，onFlowCaseStateChanged方法是状态已经更新之后，此处节点值是当前节点的下一个节点
				Integer namespaceId = UserContext.getCurrentNamespaceId();
				if(namespaceId == 1000000) {
					LOGGER.debug("synchronizedTaskToTechpark, stepType={}, tag1={}, nodeType={}", stepType, tag1, nodeType);
					List<PmTaskLog> logs = pmTaskProvider.listPmTaskLogs(task.getId(), PmTaskFlowStatus.PROCESSING.getCode());
					if (null != logs && logs.size() != 0) {
						for (PmTaskLog r: logs) {
							if (null != r.getTargetId()) {
								synchronizedTaskToTechpark(task, r.getTargetId(), flow.getOrganizationId());
								break;
							}
						}
					}
				}
			}else if ("COMPLETED".equals(nodeType)) {
				task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
				pmTaskProvider.updateTask(task);
			}else if ("HANDOVER".equals(nodeType)) {
				task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
				pmTaskProvider.updateTask(task);
				//通知第三方 config表中配置api请求地址
				//要求传的是转发项目经理填写的内容和图片 add by xiongying20170922
				FlowSubjectDTO subjectDTO = flowService.getSubectById(ctx.getCurrentEvent().getSubject().getId());
				pmTaskCommonService.handoverTaskToTrd(task, subjectDTO.getContent(), subjectDTO.getImages());
			}
		}else if(FlowStepType.ABSORT_STEP.getCode().equals(stepType)) {

			task.setStatus(PmTaskFlowStatus.INACTIVE.getCode());
			pmTaskProvider.updateTask(task);
		}
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);

	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {

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



		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e;

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("服务内容");
		e.setValue(dto.getContent());
		entities.add(e);

		if (null != dto.getAttachments()) {
			for(PmTaskAttachmentDTO s: dto.getAttachments()) {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
				e.setKey("");
				e.setValue(s.getContentUrl());
				entities.add(e);
			}
		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("服务地点");
		e.setValue(dto.getAddress());
		entities.add(e);

		if (StringUtils.isNotBlank(dto.getCategoryName())) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("所属分类");
			e.setValue(dto.getCategoryName());
			entities.add(e);
		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("发起人");
		e.setValue(dto.getRequestorName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("联系电话");
		e.setValue(dto.getRequestorPhone());
		entities.add(e);

		//TODO:为科兴与一碑对接
		if(dto.getNamespaceId() == 999983 &&
				dto.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("状态");
			e.setValue(EbeiPmTaskStatus.fromCode(dto.getStatus()).getDesc());
			entities.add(e);
		}

		//填写费用清单
		List<GeneralFormVal> list = generalFormValProvider.queryGeneralFormVals(EntityType.PM_TASK.getCode(),task.getId());
		if (flowCase.getStatus() == FlowCaseStatus.FINISHED.getCode())
			if (list!=null && list.size()>0){
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.TEXT.getCode());
				e.setKey("费用清单");
				String content = "";
				List<PostApprovalFormItem> items = list.stream().map(p->ConvertHelper.convert(p, PostApprovalFormItem.class))
						.collect(Collectors.toList());
				content += "本次服务的费用清单如下，请进行确认\n";
				Long total = Long.valueOf(getTextString(getFormItem(items,"总计").getFieldValue()));
				content += "总计:"+total+"元\n";
				Long serviceFee = Long.valueOf(getTextString(getFormItem(items,"服务费").getFieldValue()));
				content += "服务费:"+total+"元\n";
				content += "物品费:"+(total-serviceFee)+"元\n";
				PostApprovalFormItem subForm = getFormItem(items,"物品");
				if (subForm!=null) {
					PostApprovalFormSubformValue subFormValue = JSON.parseObject(subForm.getFieldValue(), PostApprovalFormSubformValue.class);
					List<PostApprovalFormSubformItemValue> array = subFormValue.getForms();
					if (array.size()!=0) {
						content += "物品费详情：\n";
						Gson g=new Gson();
						for (PostApprovalFormSubformItemValue itemValue : array){
							List<PostApprovalFormItem> values = itemValue.getValues();
							content += getTextString(getFormItem(values,"物品名称").getFieldValue())+":";
							content += getTextString(getFormItem(values,"小计").getFieldValue())+"元";
							content += "("+getTextString(getFormItem(values,"单价").getFieldValue())+"元*"+
									getTextString(getFormItem(values,"数量").getFieldValue())+")";
						}
						content += "如对上述费用有疑义请附言说明";
					}
				}
				e.setValue(content);
				entities.add(e);
			}else {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.LIST.getCode());
				e.setKey("费用清单");
				e.setValue("本次服务没有产生维修费");
				entities.add(e);
			}
		JSONObject jo = JSONObject.parseObject(JSONObject.toJSONString(dto));
		jo.put("formUrl",processFormURL(EntityType.PM_TASK.getCode(),""+task.getId(),FlowOwnerType.PMTASK.getCode(),"","费用确认"));
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
				task.setStatus(PmTaskFlowStatus.COMPLETED.getCode());
				pmTaskProvider.updateTask(task);
				pmTaskSearch.feedDoc(task);
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
		Category category = null;
		//Todo:为科兴与一碑对接
		if (task.getNamespaceId()==999983 && null!= task.getTaskCategoryId() &&
				task.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			category = new Category();
			category.setName("物业报修");
		}else
			category = categoryProvider.findCategoryById(task.getTaskCategoryId());

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
	public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId) {
		Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
		List<Category> categories = categoryProvider.listTaskCategories(namespaceId, defaultId, null,
				null, null);

		if(namespaceId == 999983) {
			EbeiPmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);
			CategoryDTO dto = handler.createCategoryDTO();
			Category category = ConvertHelper.convert(dto, Category.class);
			categories.add(category);
		}

		return categories.stream().map(c -> {
			FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
			dto.setId(c.getId());
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
		dto.setName("taskCategoryId");
		dto.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		dto.setOperators(new ArrayList<>());
		dto.getOperators().add(FlowConditionRelationalOperatorType.EQUAL.getCode());
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		ListTaskCategoriesCommand cmd = new ListTaskCategoriesCommand();
		cmd.setNamespaceId(namespaceId);
		ListTaskCategoriesResponse response = pmTaskService.listTaskCategories(cmd);
		dto.setOptions(new ArrayList<>());
		response.getRequests().forEach(p->{
			dto.getOptions().add(p.getName());
		});
		list.add(dto);
		return list;
	}

	@Override
	public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String extra) {
		//目前只有类型一个分支参数
		if ("taskCategoryId".equals(variable)) {
			FlowCase flowcase = ctx.getFlowCase();
			PmTask pmTask = pmTaskProvider.findTaskById(flowcase.getReferId());
			Category category = categoryProvider.findCategoryById(pmTask.getTaskCategoryId());
			FlowConditionStringVariable flowConditionStringVariable = new FlowConditionStringVariable(category.getName());
			return flowConditionStringVariable;
		}
		return null;
	}
}