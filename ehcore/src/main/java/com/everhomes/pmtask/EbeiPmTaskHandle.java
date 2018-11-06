package com.everhomes.pmtask;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.category.Category;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.flow.*;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.ebei.*;
import com.everhomes.rest.address.NamespaceAddressType;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.pmtask.*;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.docking.DockingMapping;
import com.everhomes.docking.DockingMappingProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.docking.DockingMappingScope;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhDockingMappings;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI)
public class EbeiPmTaskHandle extends DefaultPmTaskHandle implements ApplicationListener<ContextRefreshedEvent> {

    private static final String LIST_SERVICE_TYPE = "/rest/crmFeedBackInfoJoin/serviceTypeList";
    private static final String CREATE_TASK = "/rest/crmFeedBackInfoJoin/uploadFeedBackOrder";
    private static final String GET_TASK_DETAIL = "/rest/crmFeedBackInfoJoin/feedBackOrderDetail";
    private static final String CANCEL_TASK = "/rest/crmFeedBackInfoJoin/cancelOrder";
    private static final String EVALUATE = "/rest/crmFeedBackInfoJoin/evaluateFeedBack";
    private static final String GET_TOKEN = "/rest/ebeiInfo/sysQueryToken";

    private SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String projectId = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(EbeiPmTaskHandle.class);

    private CloseableHttpClient httpclient = null;

    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private ConfigurationProvider configProvider;
    @Autowired
    private DockingMappingProvider dockingMappingProvider;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private BuildingProvider buildingProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private AddressProvider addressProvider;
    @Autowired
    private FlowEvaluateProvider flowEvaluateProvider;

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void init() {
        httpclient = HttpClients.createDefault();
        //对接的科兴，所以默认科兴 园区id
        projectId = configProvider.getValue("pmtask.ebei.projectId", "240111044331055940");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }
    
    private List<CategoryDTO> listServiceType(String projectId, Long parentId) {
        JSONObject param = new JSONObject();
        param.put("projectId", projectId);

        String json = postToEbei(param, LIST_SERVICE_TYPE, null);

        EbeiJsonEntity<EbeiServiceType> entity = JSONObject.parseObject(json, new TypeReference<EbeiJsonEntity<EbeiServiceType>>(){});

        if(entity.isSuccess()) {
            EbeiServiceType type = entity.getData();
//			List<EbeiServiceType> types = type.getItems();
            type.setServiceId(String.valueOf(PmTaskHandle.EBEI_TASK_CATEGORY));
            List<EbeiServiceType> types;

            if (null == parentId || PmTaskHandle.EBEI_TASK_CATEGORY == parentId) {
                types = type.getItems();
            }else {
                String mappingId = getMappingIdByCategoryId(parentId);
                types = getTypes(type, mappingId);

            }
            List<CategoryDTO> result = types.stream().map(c -> {
                return convertCategory(c);

            }).collect(Collectors.toList());

            return result;
        }

        return null;
    }

    private List<EbeiServiceType> getTypes(EbeiServiceType type, String parentId) {

        List<EbeiServiceType> result = new ArrayList<>();
        List<EbeiServiceType> types = type.getItems();

        if (parentId.equals(type.getServiceId())) {
            result.addAll(types);
            return result;
        }

        types.forEach(t -> {
            result.addAll(getTypes(t, parentId));
        });

        return result;
    }

    private CategoryDTO convertCategory(EbeiServiceType ebeiServiceType) {

        CategoryDTO dto = new CategoryDTO();
        dto.setId(getCategoryIdByMapping(ebeiServiceType.getServiceId()));
        String parentId = ebeiServiceType.getParentId();
        dto.setParentId("".equals(parentId)?PmTaskHandle.EBEI_TASK_CATEGORY:getCategoryIdByMapping(parentId));
        dto.setName(ebeiServiceType.getServiceName());
        dto.setIsSupportDelete((byte)0);

        List<EbeiServiceType> types = ebeiServiceType.getItems();
        if(null != types) {
            List<CategoryDTO> childrens = types.stream().map(r -> {
                return convertCategory(r);
            }).collect(Collectors.toList());
            dto.setChildrens(childrens);
        }

        return dto;
    }

    private Long getCategoryIdByMapping(String serviceId) {

        if (StringUtils.isBlank(serviceId)) {
            return 0L;
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        String scope = DockingMappingScope.EBEI_PM_TASK.getCode();

        return coordinationProvider.getNamedLock(CoordinationLocks.PMTASK_STATISTICS.getCode()).enter(()-> {
            DockingMapping dockingMapping = dockingMappingProvider
                    .findDockingMappingByScopeAndMappingValue(namespaceId, scope, serviceId);

            if (null == dockingMapping) {
                dockingMapping = new DockingMapping();
                long id = sequenceProvider.getNextSequence(NameMapper
                        .getSequenceDomainFromTablePojo(EhDockingMappings.class));
                dockingMapping.setId(id);
                dockingMapping.setScope(scope);
                dockingMapping.setMappingValue(serviceId);

                dockingMappingProvider.createDockingMapping(dockingMapping);
            }
            return dockingMapping;
        }).first().getId();
    }

    private String getMappingIdByCategoryId(Long categoryId) {

        DockingMapping dockingMapping = dockingMappingProvider
                .findDockingMappingById(categoryId);

        return dockingMapping.getMappingValue();
    }

    public String postToEbei(JSONObject param, String method, Map<String, String> headers) {

        String url = configProvider.getValue("pmtask.ebei.url", "");
        HttpPost httpPost = new HttpPost(url + method);
        CloseableHttpResponse response = null;

        String json = null;

        try {
            StringEntity stringEntity = new StringEntity(param.toString(), "utf8");
            httpPost.setEntity(stringEntity);
//			httpPost.addHeader("EBEI_TOKEN", "");
//			httpPost.addHeader("HTMIMI_USERID", "");

            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }

        } catch (IOException e) {
            LOGGER.error("Pmtask request error, param={}", param, e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_REMOTE_INVOKE_FAIL,
                    "Pmtask request error.");
        }finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("Pmtask close instream, response error, param={}", param, e);
                }
            }
        }

        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Data from Ebei, param={}, json={}", param, json);

        return json;
    }


    @PreDestroy
    public void destroy() {
        if(null != httpclient) {
            try {
                httpclient.close();
            } catch (IOException e) {
                LOGGER.error("Pmtask close httpclient, response error, httpclient={}", httpclient, e);
            }
        }
    }

    private EbeiTaskResult createTask(PmTask task, List<AttachmentDescriptor> attachments,Long companyId) {

        JSONObject param = new JSONObject();

        param.put("userId", "");
        param.put("address", task.getAddress());

        param.put("linkName", task.getRequestorName());
        param.put("linkTel", task.getRequestorPhone());
        String fileAddrs = "";
        if(null != attachments) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for(AttachmentDescriptor ad: attachments) {
                String contentUrl = getResourceUrlByUir(ad.getContentUri(), EntityType.USER.getCode(), task.getCreatorUid());
                if(i == 0)
                    sb.append(contentUrl);
                else
                    sb.append(",").append(contentUrl);
                i++;
            }
            fileAddrs = sb.toString();
        }

        param.put("buildingId", "");
        //param.put("serviceId", getMappingIdByCategoryId(task.getCategoryId()));
        if (companyId!=null)
            param.put("companyName",organizationProvider.getOrganizationNameById(companyId));

        if (task.getAddressId()!=null) {
            Address address = addressProvider.findAddressById(task.getAddressId());
            if (address != null && NamespaceAddressType.EBEI.getCode().equals(address.getNamespaceAddressType()))
                param.put("infoId", address.getNamespaceAddressToken());
        }

        param.put("submitter","正中会");
        param.put("serviceId", getMappingIdByCategoryId(task.getCategoryId()));
        param.put("type", "1");
        param.put("remarks", task.getContent());
        param.put("projectId", projectId);
        //把科兴的园区映射到一碑的projectId
        if ("community".equals(task.getOwnerType())){
            String jsonString = configProvider.getValue("pmtask.projectId.mapping","{}");
            JSONObject object = JSONObject.parseObject(jsonString);
            Community community = communityProvider.findCommunityById(task.getOwnerId());
            if (community!=null) {
                String pid = object.getString(community.getName());
                if (!StringUtils.isEmpty(pid))
                    param.put("projectId", pid);
            }

        }
        param.put("anonymous", "0");
        param.put("fileAddrs", fileAddrs);
        param.put("callBackUrl", configProvider.getValue(999983,"pmtask.ebei.callback", ""));
        if (EbeiBuildingType.publicArea.equals(task.getBuildingName()))
            param.put("buildingType", "1");
        else
            param.put("buildingType", "0");

        String json = postToEbei(param, CREATE_TASK, null);

        EbeiJsonEntity<EbeiTaskResult> entity = JSONObject.parseObject(json, new TypeReference<EbeiJsonEntity<EbeiTaskResult>>(){});

        if(entity.isSuccess()) {
            EbeiTaskResult dto = entity.getData();
            if(dto.getResult() == 1) {
                return dto;
            }
        }
        throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_REMOTE_INVOKE_FAIL,
                "Request of third failed.");
    }

    private Boolean cancelTask(PmTask task) {

        JSONObject param = new JSONObject();

        param.put("orderId", task.getStringTag1());

        String json = postToEbei(param, CANCEL_TASK, null);

        EbeiJsonEntity<EbeiResult> entity = JSONObject.parseObject(json, new TypeReference<EbeiJsonEntity<EbeiResult>>(){});

        if(entity.isSuccess()) {
            EbeiResult ebeiResult = entity.getData();
            if(ebeiResult.getResult() == 1) {
                return true;
            }
        }

        throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_REMOTE_INVOKE_FAIL,
                "Request of third failed.");

    }

    private Boolean evaluateTask(PmTask task) {

        JSONObject param = new JSONObject();

        param.put("userId", "");
        param.put("recordId", task.getStringTag1());
        String star = task.getStar();
        if(null == star)
            star = "0";
        param.put("serviceAttitude", star);
        param.put("serviceEfficiency", star);
        param.put("serviceQuality", star);
        param.put("remark", "");
        param.put("fileAddrs", "");
        param.put("ownerName", task.getRequestorName());
        param.put("ownerPhone", task.getRequestorPhone());
        param.put("projectId", projectId);

        String json = postToEbei(param, EVALUATE, null);

        EbeiJsonEntity<EbeiResult> entity = JSONObject.parseObject(json, new TypeReference<EbeiJsonEntity<EbeiResult>>(){});

        if(entity.isSuccess()) {
            EbeiResult ebeiResult = entity.getData();
            if(ebeiResult.getResult() == 1) {
                return true;
            }
        }

        return false;
    }

    private EbeiPmTaskDTO getTaskDetail(PmTask task) {

        JSONObject param = new JSONObject();

        param.put("orderId", task.getStringTag1());

        String json = postToEbei(param, GET_TASK_DETAIL, null);

        EbeiJsonEntity<EbeiPmTaskDTO> entity = JSONObject.parseObject(json, new TypeReference<EbeiJsonEntity<EbeiPmTaskDTO>>(){});

        if(entity.isSuccess())
            return entity.getData();

        throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_REMOTE_INVOKE_FAIL,
                "Request of third failed.");
    }

    @Override
    public PmTaskDTO createTask(CreateTaskCommand cmd, Long userId, String requestorName, String requestorPhone){

        if(null == cmd.getCategoryId()){
            LOGGER.error("Invalid categoryId parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NULL,
                    "Invalid categoryId parameter.");
        }
        if(null == cmd.getAddressType()){
            LOGGER.error("Invalid addressType parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
                    "Invalid addressType parameter.");
        }
        final PmTask task = new PmTask();
        dbProvider.execute((TransactionStatus status) -> {

            User user = UserContext.current().getUser();
            Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
            String ownerType = cmd.getOwnerType();
            Long ownerId = cmd.getOwnerId();
            Long taskCategoryId = cmd.getTaskCategoryId();
            Long categoryId = cmd.getCategoryId();
            String content = cmd.getContent();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            checkCreateTaskParam(ownerType, ownerId, taskCategoryId, content);

            //设置门牌地址,楼栋地址,服务地点
            pmTaskCommonService.setPmTaskAddressInfo(cmd, task);

            task.setNamespaceId(namespaceId);
            task.setOwnerId(ownerId);
            task.setOwnerType(ownerType);

            task.setTaskCategoryId(taskCategoryId);
            task.setCategoryId(categoryId);
            task.setContent(content);
            task.setUnprocessedTime(now);
            task.setCreatorUid(user.getId());
            task.setCreateTime(now);
            if(null != cmd.getReserveTime())
                task.setReserveTime(new Timestamp(cmd.getReserveTime()));
            task.setPriority(cmd.getPriority());
            task.setSourceType(cmd.getSourceType() == null ? PmTaskSourceType.APP.getCode() : cmd.getSourceType());

            task.setOrganizationId(cmd.getOrganizationId());
            task.setRequestorName(requestorName);
            task.setRequestorPhone(requestorPhone);
            task.setOrganizationName(cmd.getOrganizationName());
            task.setIfUseFeelist((byte)0);
            task.setReferType(cmd.getReferType());
            task.setReferId(cmd.getReferId());
            //代发，设置创建者为被代发的人（如果是注册用户）userId
            if (null != cmd.getOrganizationId()) {
                if (null!=userId)
                    task.setCreatorUid(userId);
                task.setOrganizationUid(user.getId());
            }
            task.setIfUseFeelist((byte)0);
//      新增需求人企业Id用于物业线根据企业查询报修任务
            task.setEnterpriseId(cmd.getEnterpriseId());

            pmTaskProvider.createTask(task);
            createFlowCase(task);
            Long time  = System.currentTimeMillis();
            EbeiTaskResult createTaskResultDTO = createTask(task, cmd.getAttachments(),cmd.getFlowOrganizationId());
            LOGGER.info("--------------------------------------timecost:"+(System.currentTimeMillis()-time));
            if(null != createTaskResultDTO) {
                task.setStringTag1(createTaskResultDTO.getOrderId());
            }

            //附件
            pmTaskCommonService.addAttachments(cmd.getAttachments(), user.getId(), task.getId(), PmTaskAttachmentType.TASK.getCode());
            task.setStatus(FlowCaseStatus.PROCESS.getCode());
           pmTaskProvider.updateTask(task);
            return null;
        });

        pmTaskSearch.feedDoc(pmTaskProvider.findTaskById(task.getId()));
        return ConvertHelper.convert(task, PmTaskDTO.class);
    }


    private void createFlowCase(PmTask task) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(task.getNamespaceId());

        Flow flow = null;
        Long parentTaskId = pmTaskProvider.findCategoryById(task.getTaskCategoryId()).getParentId();

        if (parentTaskId == PmTaskAppType.SUGGESTION_ID)
            flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
                    FlowModuleType.SUGGESTION_MODULE.getCode(), task.getOwnerId(), FlowOwnerType.PMTASK.getCode());
        else
             flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
                 FlowModuleType.NO_MODULE.getCode(), task.getOwnerId(), FlowOwnerType.PMTASK.getCode());
        if(null == flow) {
            LOGGER.error("Enable pmtask flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
                    "Enable pmtask flow not found.");
        }


        CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
        PmTaskCategory taskCategory = pmTaskProvider.findCategoryById(task.getTaskCategoryId());
        createFlowCaseCommand.setTitle("物业报修");
        createFlowCaseCommand.setApplyUserId(task.getCreatorUid());
        createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
        createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
        createFlowCaseCommand.setReferId(task.getId());
        createFlowCaseCommand.setReferType(EntityType.PM_TASK.getCode());
        //createFlowCaseCommand.setContent("发起人：" + requestorName + "\n" + "联系方式：" + requestorPhone);
        createFlowCaseCommand.setContent(task.getContent());
        createFlowCaseCommand.setCurrentOrganizationId(task.getOrganizationId());

        createFlowCaseCommand.setProjectId(task.getOwnerId());
        createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());
        if (StringUtils.isNotBlank(task.getBuildingName())) {
            Building building = buildingProvider.findBuildingByName(namespaceId, task.getOwnerId(),
                    task.getBuildingName());
            if(building != null){
                ResourceCategoryAssignment resourceCategory = communityProvider.findResourceCategoryAssignment(building.getId(),
                        EntityType.BUILDING.getCode(), namespaceId);
                if (null != resourceCategory) {
                    createFlowCaseCommand.setProjectIdA(resourceCategory.getResourceCategryId());
                    createFlowCaseCommand.setProjectTypeA(EntityType.CHILD_PROJECT.getCode());
                }
            }
        }

        FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);
        task.setFlowCaseId(flowCase.getId());
        pmTaskProvider.updateTask(task);
    }

    private void checkCreateTaskParam(String ownerType, Long ownerId, Long taskCategoryId, String content){
        checkOwnerIdAndOwnerType(ownerType, ownerId);
        if(null == taskCategoryId) {
            LOGGER.error("Invalid taskCategoryId parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
                    "Invalid taskCategoryId parameter.");
        }

        if(StringUtils.isBlank(content)) {
            LOGGER.error("Invalid content parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CONTENT_NULL,
                    "Invalid content parameter.");
        }

    }

    private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
        if(null == ownerId) {
            LOGGER.error("Invalid ownerId parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
                    "Invalid ownerId parameter.");
        }

        if(StringUtils.isBlank(ownerType)) {
            LOGGER.error("Invalid ownerType parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
                    "Invalid ownerType parameter.");
        }
    }


    void cancelTask(CancelTaskCommand cmd) {
        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
        checkId(cmd.getId());

        dbProvider.execute((TransactionStatus transactionStatus) -> {

            PmTask task = checkPmTask(cmd.getId());
            EbeiPmTaskDTO dto = getTaskDetail(task);
//            if(!(dto.getState().byteValue() == PmTaskStatus.UNPROCESSED.getCode())){
//                LOGGER.error("Task cannot be canceled. cmd={}", cmd);
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CANCEL_TASK,
//                        "Task cannot be canceled.");
//            }

            if(cancelTask(task)) {
                User user = UserContext.current().getUser();
                Timestamp now = new Timestamp(System.currentTimeMillis());
                task.setStatus(FlowCaseStatus.ABSORTED.getCode());
                task.setDeleteUid(user.getId());
                task.setDeleteTime(now);
                pmTaskProvider.updateTask(task);
            }
            //更新工作流case状态
            FlowCase flowCase = flowCaseProvider.getFlowCaseById(task.getFlowCaseId());
            flowCase.setStatus(FlowCaseStatus.ABSORTED.getCode());
            //elasticsearch更新
            pmTaskSearch.deleteById(task.getId());

            return null;
        });
    }

    void evaluateTask(EvaluateTaskCommand cmd) {
        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
        checkId(cmd.getId());

        PmTask task = checkPmTask(cmd.getId());
        if(!task.getStatus().equals(PmTaskFlowStatus.COMPLETED.getCode())){
            LOGGER.error("Task have not been completed, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
                    "Task have not been completed.");
        }
        task.setOperatorStar(cmd.getOperatorStar());
        task.setStar(cmd.getStar());
        if(evaluateTask(task)) {
            pmTaskProvider.updateTask(task);
        }

    }

    private void checkId(Long id){
        if(null == id) {
            LOGGER.error("Invalid id parameter.");
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
                    "Invalid id parameter.");
        }
    }

    private PmTask checkPmTask(Long id){
        PmTask pmTask = pmTaskProvider.findTaskById(id);
        if(null == pmTask) {
            LOGGER.error("PmTask not found, id={}", id);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_NOT_EXIST,
                    "PmTask not found.");
        }
        return pmTask;
    }

    PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
        // TODO Auto-generated method stub

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        PmTask task = pmTaskProvider.findTaskById(cmd.getId());

        PmTaskDTO dto = ConvertHelper.convert(task, PmTaskDTO.class);

        dbProvider.execute((TransactionStatus status) -> {

            EbeiPmTaskDTO ebeiPmTask = getTaskDetail(task);

            dto.setStatus(ebeiPmTask.getState().byteValue());


            CategoryDTO taskCategory = createCategoryDTO();
            dto.setTaskCategoryName(taskCategory.getName());
            dto.setCategoryName(ebeiPmTask.getServiceName());

            String filePath = ebeiPmTask.getFilePath();
            if(StringUtils.isNotBlank(filePath)) {
                String[] filePaths = filePath.split(",");

                List<PmTaskAttachmentDTO> attachments = new ArrayList<>();
                for (String url: filePaths) {
                    PmTaskAttachmentDTO d = new PmTaskAttachmentDTO();
                    d.setContentUrl(url);
                    attachments.add(d);
                }
                dto.setAttachments(attachments);
            }

            List<EbeiPmtaskLogDTO> logs = ebeiPmTask.getScheduleStr();

            if(null != logs) {
                List<PmTaskLogDTO> taskLogs = new ArrayList<>();
                for(EbeiPmtaskLogDTO ebeiLog: logs) {
                    PmTaskLogDTO taskLog = new PmTaskLogDTO();
                    taskLog.setId(0L);
                    taskLog.setNamespaceId(namespaceId);
                    taskLog.setOwnerId(task.getOwnerId());
                    taskLog.setOwnerType(task.getOwnerType());
                    taskLog.setOperatorTime(strDateToTimestamp(ebeiLog.getOperateDate()));
                    taskLog.setText(ebeiLog.getOperateResult());
                    taskLog.setStatus((byte)0);
                    taskLog.setStatusName(ebeiLog.getOperateName());
                    taskLogs.add(taskLog);
                }
                dto.setTaskLogs(taskLogs);
            }

            return null;
        });

        return dto;
    }

    private Timestamp strDateToTimestamp(String s) {
        try {
            Date date = datetimeSF.parse(s);
            if(null != date)
                return new Timestamp(date.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd) {

        ListTaskCategoriesResponse response = new ListTaskCategoriesResponse();

        List<CategoryDTO> childrens = listServiceType(projectId, null != cmd.getParentId() ? cmd.getParentId() : null);
//      V3.6 过滤正中会办事的中间类型
//        if(childrens.size() == 1){
//            childrens = childrens.get(0).getChildrens();
//        }

        if(null == cmd.getParentId()) {
            CategoryDTO dto = createCategoryDTO();
            dto.setChildrens(childrens);

            response.setRequests(Collections.singletonList(dto));
        }else {
            response.setRequests(childrens);

        }

        return response;
    }

    @Override
    public List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {

        List<CategoryDTO> children = listServiceType(projectId, null);
        CategoryDTO dto = createCategoryDTO();
        dto.setChildrens(children);

        return Collections.singletonList(dto);
    }

    CategoryDTO createCategoryDTO() {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(PmTaskHandle.EBEI_TASK_CATEGORY);
        dto.setName("物业报修");
        dto.setParentId(0L);
        dto.setIsSupportDelete((byte)0);
        return dto;
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
    public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
        checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
        Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        SearchTasksResponse response = new SearchTasksResponse();
        List<PmTaskDTO> dtos = new ArrayList<>();
        List<PmTaskDTO> list = pmTaskSearch.searchAllDocsByType(cmd,pageSize + 1);
//        List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(),
//                cmd.getTaskCategoryId(), cmd.getStartDate(), cmd.getEndDate(), cmd.getAddressId(), cmd.getBuildingName(),cmd.getCreatorType(),
//                cmd.getPageAnchor(), pageSize+1);
        int listSize = list.size();
        if(listSize > 0){
            dtos = list.stream().map(t -> {
                PmTaskDTO dto = ConvertHelper.convert(t, PmTaskDTO.class);

                CategoryDTO taskCategory = createCategoryDTO();
                dto.setTaskCategoryId(taskCategory.getId());
                dto.setTaskCategoryName(taskCategory.getName());

//                PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(t.getId());
//                if(null != order){
//                    dto.setAmount(order.getAmount());
//                }

                return dto;
            }).collect(Collectors.toList());
            response.setRequests(dtos);
            if(listSize <= pageSize){
                response.setNextPageAnchor(null);
            }else{
                response.setNextPageAnchor(list.get(listSize-1).getCreateTime().getTime());
                response.getRequests().remove(list.get(listSize-1));
            }
        }

        return response;
    }

}
