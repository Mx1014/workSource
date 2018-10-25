package com.everhomes.decoration;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.archives.ArchivesService;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.qrcode.QRCodeService;
import com.everhomes.rest.archives.AddArchivesContactCommand;
import com.everhomes.rest.decoration.*;
import com.everhomes.rest.decoration.PostApprovalFormCommand;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommand;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommandResponse;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.tables.pojos.EhDecorationApprovalVals;
import com.everhomes.server.schema.tables.pojos.EhDecorationRequests;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DecorationServiceImpl implements  DecorationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationServiceImpl.class);

    @Autowired
    private DecorationProvider decorationProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private PortalService portalService;
    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private QRCodeService qRCodeService;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private DecorationSMSProcessor decorationSMSProcessor;
    @Autowired
    private GeneralApprovalService generalApprovalService;


    private static final SimpleDateFormat sdfyMd= new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfMd = new SimpleDateFormat("MM-dd");
    private static final SimpleDateFormat sdfyMdHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    public DecorationIllustrationDTO getIllustration(GetIlluStrationCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        DecorationSetting setting =  decorationProvider.getDecorationSetting(namespaceId,cmd.getCommunityId(),
                cmd.getOwnerType(),cmd.getOwnerId());
        if (setting == null)
            return null;
        DecorationIllustrationDTO dto = ConvertHelper.convert(setting,DecorationIllustrationDTO.class);
        List<DecorationAttachmentDTO> attachment = decorationProvider.listDecorationAttachmentBySettingId(setting.getId());
        if (attachment!=null && attachment.size()>0){
            attachment.forEach(r->{
                if (r.getAttachmentType().equals(DecorationAttachmentType.FILE))
                  r.setFileUrl(this.contentServerService.parserUri(r.getFileUri()));
            });
        }
        dto.setAttachments(attachment);
        return dto;
    }

    @Override
    public DecorationIllustrationDTO getRefundInfo(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (request == null || request.getRefoundAmount()==null)
            return null;
        GetIlluStrationCommand cmd2 = new GetIlluStrationCommand();
        cmd2.setCommunityId(request.getCommunityId());
        cmd2.setOwnerType(IllustrationType.REFOUND.getCode());
        DecorationIllustrationDTO dto = this.getIllustration(cmd2);
        if (dto != null) {
            dto.setRefundAmount(request.getRefoundAmount());
            dto.setRefoundComment(request.getRefoundComment());
        }
        return dto;
    }

    @Override
    public GetDecorationFeeResponse getFeeInfo(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (request == null)
            return null;
        GetDecorationFeeResponse response = new GetDecorationFeeResponse();
        GetIlluStrationCommand cmd2 = new GetIlluStrationCommand();
        cmd2.setOwnerType(IllustrationType.FEE.getCode());
        cmd2.setCommunityId(request.getCommunityId());
        DecorationIllustrationDTO dto = this.getIllustration(cmd2);
        if(dto != null) {
            response.setAddress(dto.getAddress());
            response.setLatitude(dto.getLatitude());
            response.setLongitude(dto.getLongitude());
        }

        List<DecorationFee> list = this.decorationProvider.listDecorationFeeByRequestId(request.getId());
        if(list == null || list.size()==0)
            return null;
        for (int i=0;i<list.size();i++)
            if (list.get(i).getTotalPrice() != null){
                response.setTotalPrice(list.get(i).getTotalPrice());
                list.remove(i);
                break;
            }
        response.setFee(list.stream().map(r->{
            DecorationFeeDTO feeDTO = ConvertHelper.convert(r,DecorationFeeDTO.class);
            return feeDTO;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public void setIllustration(UpdateIllustrationCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        this.dbProvider.execute((TransactionStatus status) -> {
            DecorationSetting setting = null;
            if (cmd.getId()==null){
                setting = ConvertHelper.convert(cmd,DecorationSetting.class);
                setting.setNamespaceId(namespaceId);
                decorationProvider.createDecorationSetting(setting);
            }else{
                setting = decorationProvider.getDecorationSettingById(cmd.getId());
                if (setting == null)
                    throw RuntimeErrorException.errorWith("decoration",
                            1001, "setting not found");
                org.springframework.beans.BeanUtils.copyProperties(cmd,setting);
                setting.setNamespaceId(namespaceId);
                decorationProvider.updateDecorationSetting(setting);
            }
            //删除附件
            decorationProvider.deleteDecorationBySettingId(setting.getId());
            //重新添加附件
            addDecorationAttachment(setting.getId(),cmd.getAttachments());
            return null;
        });
    }

    @Override
    public void setApplySetting(UpdateApplySettingCommand cmd) {
        if (cmd.getSettings() != null && cmd.getSettings().size()>0)
            for (UpdateIllustrationCommand cmd2 : cmd.getSettings()){
                cmd2.setOwnerType(IllustrationType.APPLY.getCode());
                this.setIllustration(cmd2);
            }
    }

    private void addDecorationAttachment(Long settingId, List<DecorationAttachmentDTO> attachments){
        if (attachments != null){
            attachments.forEach(r->{
                DecorationAttachment attachment = ConvertHelper.convert(r,DecorationAttachment.class);
                attachment.setSettingId(settingId);
                attachment.setNamespaceId(UserContext.getCurrentNamespaceId());
                this.decorationProvider.createDecorationAttachment(attachment);
            });
        }
    }

    @Override
    public DecorationWorkerDTO updateWorker(UpdateWorkerCommand cmd) {
        if (cmd.getRequestId() == null)
            throw RuntimeErrorException.errorWith("decoration",
                    1001, "request id can not be null");
        DecorationWorkerDTO dto = null;
        if (cmd.getId()!=null){
            DecorationWorker worker = decorationProvider.getDecorationWorkerById(cmd.getId());
            org.springframework.beans.BeanUtils.copyProperties(cmd,worker);
            decorationProvider.updateDecorationWorker(worker);
            dto =  ConvertHelper.convert(worker,DecorationWorkerDTO.class);
            if (!StringUtils.isBlank(dto.getImageUri()))
                dto.setImageUrl(this.contentServerService.parserUri(dto.getImageUri()));
        }else{
            DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
            if (request.getDecoratorPhone().equals(cmd.getPhone()))
                throw RuntimeErrorException.errorWith("decoration",
                        1001, "不可添加装修负责人");
            Long companyId = request.getDecoratorCompanyId();
            if (companyId == null)
                throw RuntimeErrorException.errorWith("decoration",
                        1001, "不存在的装修公司");
            DecorationWorker worker = this.decorationProvider.queryDecorationWorker(request.getId(),cmd.getPhone());
            if (worker != null)
                throw RuntimeErrorException.errorWith("decoration",
                        1001, "不可重复添加已有员工");
            DecorationCompany dc = decorationProvider.getDecorationCompanyById(companyId);
            worker = ConvertHelper.convert(cmd,DecorationWorker.class);
            VerifyPersonnelByPhoneCommand cmd1 = new VerifyPersonnelByPhoneCommand();
            cmd1.setEnterpriseId(dc.getOrganizationId());
            cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
            cmd1.setPhone(worker.getPhone());
            worker.setUid(0l);//注册未登陆
            worker.setNamespaceId(UserContext.getCurrentNamespaceId());

            //merge conflict add  UserContext.getCurrentNamespaceId()
            OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(dc.getOrganizationId(),
                    worker.getPhone(), UserContext.getCurrentNamespaceId());
            if (member == null) {
                VerifyPersonnelByPhoneCommandResponse res1 = organizationService.verifyPersonnelByPhone(cmd1);
                if (res1.getDto() != null)
                    worker.setUid(res1.getDto().getTargetId());
                AddArchivesContactCommand cmd2 = new AddArchivesContactCommand();
                cmd2.setContactName(worker.getName());
                cmd2.setGender((byte) 1);
                cmd2.setRegionCode("86");
                cmd2.setVisibleFlag((byte) 0);
                cmd2.setContactToken(worker.getPhone());
                cmd2.setOrganizationId(dc.getOrganizationId());
                cmd2.setDepartmentIds(new ArrayList<>());
                cmd2.getDepartmentIds().add(dc.getOrganizationId());
                archivesService.addArchivesContact(cmd2);//加入企业
            } else { //已经加入企业
                worker.setUid(member.getTargetId());
            }
            decorationProvider.createDecorationWorker(worker);
            worker.setQrid(createQrCode(request.getId(),worker.getId(),worker.getPhone(),ProcessorType.WORKER.getCode()));
            decorationProvider.updateDecorationWorker(worker);
            dto =  ConvertHelper.convert(worker,DecorationWorkerDTO.class);
            if (!StringUtils.isBlank(dto.getImageUri()))
                dto.setImageUrl(this.contentServerService.parserUri(dto.getImageUri()));
            //短信通知
            decorationSMSProcessor.createWorker(request,worker);
        }

        return dto;
    }

    private String createQrCode(Long requestId,Long workerId,String phone,Byte processorType){
        NewQRCodeCommand cmd = new NewQRCodeCommand();
        cmd.setActionType((byte)13);
        String url = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        if(!url.endsWith("/")) {
            url += "/";
        }
        url += "decoration-management/build/index.html#/license-detail?requestId=";
        if (requestId != null)
            url += requestId;
        url += "&workerId=";
        if (workerId != null)
            url += workerId;
        url += "&phone=";
        if (phone != null)
            url += phone;
        url += "&processorType=";
        if (processorType != null)
            url += processorType;
        JSONObject json = new JSONObject();
        json.put("url",url);
        cmd.setActionData(json.toJSONString());
        QRCodeDTO qrCode = this.qRCodeService.createQRCode(cmd);
        return qrCode.getQrid();
    }

    @Override
    public ListWorkersResponse listWorkers(ListWorkersCommand cmd) {
        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0l);
        ListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<DecorationWorker> workers = decorationProvider.listWorkersByRequestId(cmd.getRequestId(),cmd.getKeyword(),locator,pageSize+1);
        if (workers == null)
            return  null;
        ListWorkersResponse response = new ListWorkersResponse();
        if (workers.size()>pageSize){
            workers.remove(workers.size()-1);
            response.setNextPageAnchor(workers.get(workers.size()-1).getId());
        }
       List<DecorationWorkerDTO> dtos =  workers.stream().map(r->{
            DecorationWorkerDTO dto = ConvertHelper.convert(r,DecorationWorkerDTO.class);
            dto.setImageUri(r.getImage());
            if (!StringUtils.isBlank(r.getImage()))
                dto.setImageUrl(this.contentServerService.parserUri(r.getImage()));
            return dto;
        }).collect(Collectors.toList());
        response.setWorkers(dtos);
        return  response;
    }

    @Override
    public void deleteWorker(DeleteWorkerCommand cmd) {
        decorationProvider.deleteDecorationWorkerById(cmd.getId());
    }

    @Override
    public DecorationRequestDTO createRequest(CreateRequestCommand cmd) {
        DecorationRequest request = ConvertHelper.convert(cmd,DecorationRequest.class);
        request.setNamespaceId(UserContext.getCurrentNamespaceId());
        request.setStartTime(new Timestamp(cmd.getStartTime()));
        request.setEndTime(new Timestamp(cmd.getEndTime()));
        request.setApplyUid(UserContext.currentUserId());
        request.setCancelFlag((byte)0);
        request.setStatus(DecorationRequestStatus.APPLY.getCode());
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), cmd.getDecoratorPhone());
        if (userIdentifier != null)
            request.setDecoratorUid(userIdentifier.getOwnerUid());
        if (request.getDecoratorCompanyId() == null){
            Organization org = organizationProvider.findOrganizationByName(request.getDecoratorCompany(), UserContext.getCurrentNamespaceId());
            if (org!=null)
                throw RuntimeErrorException.errorWith("decoration",
                        1001, "公司名已被占用");
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            decorationProvider.createDecorationRequest(request);
            Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(),EntityType.COMMUNITY.getCode(),cmd.getCommunityId(),
                DecorationController.moduleId, DecorationController.moduleType, cmd.getCommunityId(), DecorationRequestStatus.APPLY.getFlowOwnerType());
            if (flow == null) {
                LOGGER.error("Enable decoration flow not found, moduleId={}", FlowConstants.DECORATION_MODULE);
                request.setCancelFlag((byte)1);
                decorationProvider.updateDecorationRequest(request);
                throw RuntimeErrorException.errorWith("decoration",
                        10001, "请开启工作流后重试");
            }
            CreateFlowCaseCommand cmd2 = new CreateFlowCaseCommand();
            cmd2.setApplyUserId(UserContext.currentUserId());
            cmd2.setReferId(request.getId());
            cmd2.setReferType(DecorationRequestStatus.APPLY.getFlowOwnerType());
            cmd2.setProjectId(request.getCommunityId());
            cmd2.setProjectType(EntityType.COMMUNITY.getCode());
            cmd2.setTitle(getAppName());

            cmd2.setServiceType(cmd2.getTitle());
            cmd2.setTitle(cmd2.getTitle()+"("+DecorationRequestStatus.APPLY.getDescribe()+")");
            StringBuilder content = new StringBuilder();
            content.append("公司名称:"+request.getApplyCompany()+"\n");
            content.append("装修地点:"+convertAddress(request.getAddress())+"\n");
            content.append("装修日期:"+sdfyMd.format(new Date(request.getStartTime().getTime()))+"至"+
                    sdfyMd.format(new Date(request.getEndTime().getTime())));
            cmd2.setContent(content.toString());
            cmd2.setFlowMainId(flow.getFlowMainId());
            cmd2.setFlowVersion(flow.getFlowVersion());
            flowService.createFlowCase(cmd2);
           return null;
        });

        return convertRequest(request,ProcessorType.MASTER.getCode());
    }

    @Override
    public SearchRequestResponse searchRequest(SearchRequestsCommand cmd) {
        if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4300043010L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        SearchRequestResponse response = new SearchRequestResponse();
        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = null;
        if (cmd.getPageAnchor() != null) {
            locator = new CrossShardListingLocator();
            locator.setAnchor(cmd.getPageAnchor());
        }
        if (cmd.getStatus() != null)
            cmd.setCancelFlag((byte)0);

        String address = cmd.getBuildingName();
        if (cmd.getDoorPlate() != null)
            address += "&"+cmd.getDoorPlate();
        List<DecorationRequest> requests =  this.decorationProvider.queryDecorationRequests(UserContext.getCurrentNamespaceId(),cmd.getCommunityId(),cmd.getStartTime(),
                cmd.getEndTime(),address,cmd.getStatus(),cmd.getKeyword(),cmd.getCancelFlag(),pageSize+1,locator);
        if (requests == null || requests.size() == 0)
            return null;
        if (requests.size()>pageSize){
            requests.remove(requests.size()-1);
            response.setNextPageAnchor(requests.get(requests.size()-1).getId());
        }
        response.setRequests(processSearchResult(requests));

        return response;
    }

    private List<DecorationRequestDTO> processSearchResult(List<DecorationRequest> requests){
        return requests.stream().map(r->{
            DecorationRequestDTO dto = new DecorationRequestDTO();
            dto.setId(r.getId());
            dto.setCreateTime(r.getCreateTime().getTime());
            dto.setAddress(convertAddress(r.getAddress()));
            dto.setStartTime(r.getStartTime().getTime());
            dto.setEndTime(r.getEndTime().getTime());
            dto.setApplyName(r.getApplyName());
            dto.setApplyPhone(r.getApplyPhone());
            dto.setApplyCompany(r.getApplyCompany());
            dto.setStatus(r.getStatus());
            dto.setProcessorType(ProcessorType.ROOT.getCode());
            dto.setCancelFlag(r.getCancelFlag());

            DecorationRequestStatus status = DecorationRequestStatus.fromCode(r.getStatus());
            FlowCase flowCase;
            DecorationFlowCaseDTO flowCaseDTO;
            switch (status) {
                case APPLY:
                case FILE_APPROVAL:
                case CHECK:
                    flowCase = this.flowCaseProvider.findFlowCaseByReferId(r.getId(), status.getFlowOwnerType(),
                            DecorationController.moduleId);
                    if (flowCase == null)
                        break;
                    dto.setFlowCasees(new ArrayList<>());
                    flowCaseDTO = new DecorationFlowCaseDTO();
                    flowCaseDTO.setFlowCaseId(flowCase.getId());
                    flowCaseDTO.setStatus(flowCase.getStatus());
                    dto.getFlowCasees().add(flowCaseDTO);
                    break;
                default:
                    break;
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public SearchRequestResponse searchUserRelateRequest(ListUserRequestsCommand cmd) {
        SearchRequestResponse response = new SearchRequestResponse();
        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = null;
        if (cmd.getPageAnchor() != null) {
            locator = new CrossShardListingLocator(cmd.getPageAnchor());
            locator.setAnchor(cmd.getPageAnchor());
        }
        List<DecorationRequest> requests =  this.decorationProvider.queryUserRelateRequests(UserContext.getCurrentNamespaceId(),
                cmd.getCommunityId(),cmd.getPhone());
        if (requests == null || requests.size() == 0)
            return null;
        List<Long> ids = requests.stream().map(EhDecorationRequests::getId).collect(Collectors.toList());
        requests = decorationProvider.queryRequestsByIds(ids,pageSize+1,locator);
        if (requests.size()>pageSize){
            requests.remove(requests.size()-1);
            response.setNextPageAnchor(requests.get(requests.size()-1).getId());
        }
        response.setRequests(requests.stream().map(r->{
            DecorationRequestDTO dto = new DecorationRequestDTO();
            dto.setId(r.getId());
            dto.setCreateTime(r.getCreateTime().getTime());
            dto.setAddress(convertAddress(r.getAddress()));
            dto.setStartTime(r.getStartTime().getTime());
            dto.setEndTime(r.getEndTime().getTime());
            dto.setApplyName(r.getApplyName());
            dto.setApplyPhone(r.getApplyPhone());
            dto.setApplyCompany(r.getApplyCompany());
            dto.setStatus(r.getStatus());
            if (r.getApplyPhone().equals(cmd.getPhone())) {
                dto.setProcessorType(ProcessorType.MASTER.getCode());
            }
            else if (r.getDecoratorPhone().equals(cmd.getPhone())) {
                dto.setProcessorType(ProcessorType.CHIEF.getCode());
                if (r.getDecoratorUid() == null){
                    r.setDecoratorUid(UserContext.currentUserId());
                    this.decorationProvider.updateDecorationRequest(r);
                }
            }
            else
                dto.setProcessorType(ProcessorType.WORKER.getCode());
            dto.setCancelFlag(r.getCancelFlag());

            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public DecorationRequestDTO getRequestDetail(GetDecorationDetailCommand cmd) {
        DecorationRequest request = this.decorationProvider.getRequestById(cmd.getId());
        return convertRequest(request,cmd.getProcessorType());
    }

    private String getAppName(){
        ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
        listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        listServiceModuleAppsCommand.setModuleId(FlowConstants.DECORATION_MODULE);
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
        if (apps!=null && apps.getServiceModuleApps().size()>0)
            return(apps.getServiceModuleApps().get(0).getName());
        else
            return ("装修办理");
    }

    private DecorationRequestDTO convertRequest(DecorationRequest request, Byte processorType){
        DecorationRequestDTO dto = new DecorationRequestDTO();
        dto.setApplyName(request.getApplyName());
        dto.setApplyPhone(request.getApplyPhone());
        dto.setApplyCompany(request.getApplyCompany());
        dto.setAddress(convertAddress(request.getAddress()));
        dto.setStartTime(request.getStartTime().getTime());
        dto.setEndTime(request.getEndTime().getTime());
        dto.setStatus(request.getStatus());
        dto.setCancelFlag(request.getCancelFlag());
        dto.setCancelReason(request.getCancelReason());

        dto.setDecoratorName(request.getDecoratorName());
        dto.setDecoratorPhone(request.getDecoratorPhone());
        dto.setDecoratorCompany(request.getDecoratorCompany());

        //装修费用
        if (ProcessorType.ROOT.getCode() == processorType ||
                ((ProcessorType.MASTER.getCode() == processorType || ProcessorType.CHIEF.getCode() == processorType) &&
                        (request.getStatus()> DecorationRequestStatus.PAYMENT.getCode()||request.getCancelFlag() != (byte)0))){
            List<DecorationFee> fees = decorationProvider.listDecorationFeeByRequestId(request.getId());
            if (fees!=null && fees.size()>0){
                dto.setDecorationFee(new ArrayList<>());
                for (DecorationFee fee:fees)
                    if (fee.getTotalPrice()!=null)
                        dto.setTotalAmount(fee.getTotalPrice().setScale(2).toString());
                    else{
                        DecorationFeeDTO dto2 = ConvertHelper.convert(fee,DecorationFeeDTO.class);
                        dto.getDecorationFee().add(dto2);
                    }
            }
        }

        //退款
        if (ProcessorType.ROOT.getCode() == processorType ||
                ((ProcessorType.MASTER.getCode() == processorType || ProcessorType.CHIEF.getCode() == processorType) &&
                        (request.getStatus()== DecorationRequestStatus.COMPLETE.getCode()||request.getCancelFlag() != (byte)0))){
            dto.setRefoundAmount(request.getRefoundAmount());
            dto.setRefoundComment(request.getRefoundComment());
        }

        FlowCase flowCase;
        DecorationFlowCaseDTO flowCaseDTO;
        DecorationRequestStatus status = DecorationRequestStatus.fromCode(request.getStatus());
        switch (status){
            case APPLY:
            case FILE_APPROVAL:
            case CHECK:
                 flowCase = this.flowCaseProvider.findFlowCaseByReferId(request.getId(),status.getFlowOwnerType(),
                        DecorationController.moduleId);
                 if (flowCase == null)
                     break;
                dto.setFlowCasees(new ArrayList<>());
                flowCaseDTO = new DecorationFlowCaseDTO();
                flowCaseDTO.setFlowCaseId(flowCase.getId());
                flowCaseDTO.setStatus(flowCase.getStatus());
                dto.getFlowCasees().add(flowCaseDTO);
                break;
            case CONSTRACT:
                List<DecorationApprovalVal> vals = this.decorationProvider.listApprovalVals(request.getId(),null);
                if (vals != null && vals.size()>0){
                    dto.setFlowCasees(new ArrayList<>());
                    for (DecorationApprovalVal val:vals){
                        flowCaseDTO = new DecorationFlowCaseDTO();
                        flowCaseDTO.setApprovalName(val.getApprovalName());
                        flowCase = this.flowCaseProvider.findFlowCaseByReferId(val.getId(),status.getFlowOwnerType(),
                                DecorationController.moduleId);
                        if (flowCase == null)
                            continue;
                        flowCaseDTO.setStatus(flowCase.getStatus());
                        flowCaseDTO.setFlowCaseId(flowCase.getId());
                        if (ProcessorType.ROOT.getCode() == processorType)
                            flowCaseDTO.setFlowCaseForm(getFormEntitiesByApprovalVal(val));
                        dto.getFlowCasees().add(flowCaseDTO);
                    }
                    break;
                }
            default :
                break;
        }

        return dto;
    }

    @Override
    public String convertAddress(String address) {
        if (StringUtils.isBlank(address))
            return "";
        String[] addresses = address.split(";");
        Map<String,List<String>> map = new HashMap<>();
        StringBuilder finalAddress = new StringBuilder();
        for(String ad1 : addresses){
            String [] adSplit = ad1.split("&");
            if (adSplit.length == 2){
                if (map.get(adSplit[0])!=null)
                    map.get(adSplit[0]).add(adSplit[1]);
                else{
                    map.put(adSplit[0],new ArrayList<>());
                    map.get(adSplit[0]).add(adSplit[1]);
                }
            }else if (map.containsKey(adSplit[0]))
                map.put(adSplit[0],new ArrayList<>());
        }

        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            StringBuilder builder = new StringBuilder();
            String building = iterator.next();
            builder.append(building);
            List<String> ads = map.get(building);
            if (ads.size()>0) {
                builder.append(ads.get(0));
                if (ads.size() > 1) {
                    for (int i = 1; i < ads.size(); i++)
                        builder.append("/").append(ads.get(i));
                }
            }
            builder.append(";");
            finalAddress.append(builder.toString());
        }
        if (finalAddress.length()<0)
            return "";
        finalAddress.deleteCharAt(finalAddress.length()-1);
        return finalAddress.toString();
    }

    private String parseRequestStatus(Byte status){
        DecorationRequestStatus ds = DecorationRequestStatus.fromCode(status);
        switch (ds){
            case APPLY: return "装修申请";
            case FILE_APPROVAL: return "资料审核";
            case PAYMENT : return "缴费";
            case CONSTRACT : return "进场施工";
            case CHECK : return "验收";
            case REFOUND : return "押金退回";
            case COMPLETE : return "完成";
        }
        return "";
    }

    @Override
    public List<FlowCaseEntity> getFormEntitiesByApprovalVal(DecorationApprovalVal val) {
        GetGeneralFormValuesCommand cmd = new GetGeneralFormValuesCommand();
        cmd.setSourceType(EhDecorationApprovalVals.class.getSimpleName());
        cmd.setSourceId(val.getId());
        List<FlowCaseEntity> formEntities = generalFormService.getGeneralFormFlowEntities(cmd);
        formEntities.forEach(r -> {
            if (StringUtils.isBlank(r.getValue())) {
                r.setValue("无");
            }
        });
        return formEntities;
    }

    @Override
    public void modifyFee(ModifyFeeCommand cmd) {
        this.dbProvider.execute((TransactionStatus status) -> {
            List<DecorationFee> fees = this.decorationProvider.listDecorationFeeByRequestId(cmd.getRequestId());
            if (fees == null || fees.size() == 0){
                DecorationRequest request = this.decorationProvider.getRequestById(cmd.getRequestId());
                //短信通知
                decorationSMSProcessor.feeListGenerate(request);
            }else {
                this.decorationProvider.deleteDecorationFeeByRequestId(cmd.getRequestId());
                DecorationRequest request = this.decorationProvider.getRequestById(cmd.getRequestId());
                //短信通知
                decorationSMSProcessor.modifyFee(request);
            }
            DecorationFee fee = new DecorationFee();
            fee.setRequestId(cmd.getRequestId());
            fee.setTotalPrice(cmd.getTotalAmount());
            fee.setNamespaceId(UserContext.getCurrentNamespaceId());
            this.decorationProvider.createDecorationFee(fee);
            if (cmd.getDecorationFee()!=null)
                for (DecorationFeeDTO dto : cmd.getDecorationFee()){
                    fee = new DecorationFee();
                    fee.setNamespaceId(UserContext.getCurrentNamespaceId());
                    fee.setRequestId(cmd.getRequestId());
                    fee.setAmount(dto.getAmount());
                    fee.setFeeName(dto.getFeeName());
                    fee.setFeePrice(dto.getFeePrice());
                    this.decorationProvider.createDecorationFee(fee);
                }
            return null;
        });
    }

    @Override
    public void modifyRefoundAmount(ModifyRefoundAmountCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (request.getRefoundAmount() == null){
            //短信通知
            decorationSMSProcessor.refoundGenerate(request);
        }else{
            //短信通知
            decorationSMSProcessor.modifyRefundFee(request);
        }
        request.setRefoundAmount(cmd.getRefoundAmount());
        request.setRefoundComment(cmd.getRefoundComment());
        this.decorationProvider.updateDecorationRequest(request);
    }

    @Override
    public void confirmRefound(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (request.getStatus() != DecorationRequestStatus.REFOUND.getCode())
            throw RuntimeErrorException.errorWith("decoration",
                    1001, "错误的节点状态");
        request.setStatus(DecorationRequestStatus.COMPLETE.getCode());
        this.decorationProvider.updateDecorationRequest(request);
        //短信通知
        decorationSMSProcessor.refoundConfirm(request);
    }

    @Override
    public void confirmFee(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (request.getStatus() != DecorationRequestStatus.PAYMENT.getCode())
            throw RuntimeErrorException.errorWith("decoration",
                    1001, "错误的节点状态");
        request.setStatus(DecorationRequestStatus.CONSTRACT.getCode());
        this.decorationProvider.updateDecorationRequest(request);
        //短信通知
        decorationSMSProcessor.feeConfirm(request);
    }

    @Override
    public void cancelRequest(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        request.setCancelFlag((byte)2);
        request.setCancelReason(cmd.getReason());
        this.decorationProvider.updateDecorationRequest(request);
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(UserContext.currentUserId(), IdentifierType.MOBILE.getCode());
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(UserContext.currentUserId(),cmd.getOrganizationId());
        if (member != null){
            decorationSMSProcessor.decorationCancel(request,member.getContactName(),userIdentifier.getIdentifierToken());
        }else{
            User user = userProvider.findUserById(UserContext.currentUserId());
            decorationSMSProcessor.decorationCancel(request,user.getNickName(),userIdentifier.getIdentifierToken());
        }
    }

    @Override
    public Long postApprovalForm(PostApprovalFormCommand cmd) {
        GetTemplateByApprovalIdCommand cmd2 = new GetTemplateByApprovalIdCommand();
        cmd2.setApprovalId(cmd.getApprovalId());
        Long []flowCaseId = {0L};
        //获取审批
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
                .getApprovalId());
        this.dbProvider.execute((TransactionStatus status) -> {
            //先删除旧的审批
            this.decorationProvider.deleteApprovalValByRequestId(cmd.getRequestId(),cmd.getApprovalId());
            //保存审批数据
            DecorationApprovalVal val = new DecorationApprovalVal();
            val.setApprovalId(cmd.getApprovalId());
            val.setApprovalName(ga.getApprovalName());
            val.setDeleteFlag((byte)0);
            val.setRequestId(cmd.getRequestId());
            val.setFormOriginId(ga.getFormOriginId());
            val.setFormVersion(ga.getFormVersion());
            val.setNamespaceId(UserContext.getCurrentNamespaceId());
            this.decorationProvider.createApprovalVals(val);

            //创建工作流
            Flow flow = flowService.getEnabledFlow(ga.getNamespaceId(), ga.getModuleId(),
                    ga.getModuleType(), ga.getId(), FlowOwnerType.GENERAL_APPROVAL.getCode());
            if (flow == null) {
                LOGGER.error("Enable decoration flow not found, moduleId={}", FlowConstants.DECORATION_MODULE);
                throw RuntimeErrorException.errorWith("decoration",
                        10001, "请开启工作流后重试");
            }
            DecorationRequest request = this.decorationProvider.getRequestById(cmd.getRequestId());
            CreateFlowCaseCommand cfcc = new CreateFlowCaseCommand();
            cfcc.setApplyUserId(UserContext.currentUserId());
            cfcc.setReferId(val.getId());
            cfcc.setReferType(DecorationRequestStatus.CONSTRACT.getFlowOwnerType());
            cfcc.setProjectId(request.getCommunityId());
            cfcc.setProjectType(EntityType.COMMUNITY.getCode());
            cfcc.setTitle(ga.getApprovalName());

            cfcc.setServiceType(getAppName());
            StringBuilder content = new StringBuilder();
            content.append("装修公司:" + request.getDecoratorCompany() + "\n");
            content.append("装修地点:" + convertAddress(request.getAddress()) + "\n");
            content.append("装修人日期:" + sdfyMd.format(new Date(request.getStartTime().getTime())) + "至" +
                    sdfyMd.format(new Date(request.getEndTime().getTime())));
            cfcc.setContent(content.toString());
            cfcc.setFlowMainId(flow.getFlowMainId());
            cfcc.setFlowVersion(flow.getFlowVersion());
            FlowCase flowCase = flowService.createFlowCase(cfcc);
            val.setFlowCaseId(flowCase.getId());
            flowCaseId[0] = flowCase.getId();
            this.decorationProvider.updateApprovalVals(val);
            //将表单数据存起来
            addGeneralFormValuesCommand cmd3 = new addGeneralFormValuesCommand();
            cmd3.setValues(cmd.getValues());
            cmd3.setGeneralFormId(ga.getFormOriginId());
            cmd3.setSourceId(val.getId());
            cmd3.setSourceType(EhDecorationApprovalVals.class.getSimpleName());
            this.generalFormService.addGeneralFormValues(cmd3);

            return null;
        });
        return flowCaseId[0];
    }

    @Override
    public void DecorationApplySuccess(Long requestId) {
        DecorationRequest request = this.decorationProvider.getRequestById(requestId);
        if (DecorationRequestStatus.APPLY.getCode() != request.getStatus())
            throw RuntimeErrorException.errorWith("decoration",
                    1001, "错误的节点状态");
        try{
            //注册公司
            if (request.getDecoratorCompanyId() == null){
                Organization org = organizationProvider.findOrganizationByName(request.getDecoratorCompany(), request.getNamespaceId());
                if (org == null) {//再次检测
                    CreateEnterpriseCommand cmd = new CreateEnterpriseCommand();
                    cmd.setName(request.getDecoratorCompany());
                    cmd.setAddress("待补充");
                    cmd.setCommunityId(request.getCommunityId());
                    cmd.setLatitude("0.00");
                    cmd.setLongitude("0.00");
                    OrganizationDTO organizationDTO = this.organizationService.createEnterprise(cmd);
                    //新建装修公司
                    DecorationCompany company = new DecorationCompany();
                    company.setName(request.getDecoratorCompany());
                    company.setNamespaceId(UserContext.getCurrentNamespaceId());
                    company.setOrganizationId(organizationDTO.getId());
                    this.decorationProvider.createDecorationCompany(company);
                    request.setDecoratorCompanyId(company.getId());
                }else{
                    List<DecorationCompany> companies = this.decorationProvider.listDecorationCompanies(request.getNamespaceId(),request.getDecoratorCompany());
                    if (companies!=null && companies.size()>0)
                        request.setDecoratorCompanyId(companies.get(0).getId());
                }
            }
            // 注册装修公司负责人
            DecorationCompany decorationCompany = this.decorationProvider.getDecorationCompanyById(request.getDecoratorCompanyId());

            //merge conflict add  UserContext.getCurrentNamespaceId()
            OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(decorationCompany.getOrganizationId(),
                    request.getDecoratorPhone(), UserContext.getCurrentNamespaceId());
            if (member == null) {
                AddArchivesContactCommand cmd2 = new AddArchivesContactCommand();
                cmd2.setContactName(request.getDecoratorName());
                cmd2.setGender((byte) 1);
                cmd2.setRegionCode("86");
                cmd2.setVisibleFlag((byte) 0);
                cmd2.setContactToken(request.getDecoratorPhone());
                cmd2.setOrganizationId(decorationCompany.getOrganizationId());
                cmd2.setDepartmentIds(new ArrayList<>());
                cmd2.getDepartmentIds().add(decorationCompany.getOrganizationId());
                archivesService.addArchivesContact(cmd2);//加入企业
            }
            List<DecorationCompanyChief> chiefs = this.decorationProvider.listChiefsByCompanyId(decorationCompany.getId());
            if (chiefs == null || chiefs.stream().noneMatch(r-> r.getPhone().equals(request.getDecoratorPhone()))){
                DecorationCompanyChief chief = new DecorationCompanyChief();
                chief.setCompanyId(decorationCompany.getId());
                chief.setName(request.getDecoratorName());
                chief.setNamespaceId(request.getNamespaceId());
                chief.setPhone(request.getDecoratorPhone());
                chief.setUid(request.getDecoratorUid());
                this.decorationProvider.createCompanyChief(chief);
            }else{
                chiefs = chiefs.stream().filter(r-> r.getPhone().equals(request.getDecoratorPhone())).collect(Collectors.toList());
                DecorationCompanyChief chief = chiefs.get(0);
                chief.setName(request.getDecoratorName());
                this.decorationProvider.updateCompanyChief(chief);
            }
            //工作流
            Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(),EntityType.COMMUNITY.getCode(),request.getCommunityId(),
                    DecorationController.moduleId, DecorationController.moduleType, request.getCommunityId(), DecorationRequestStatus.FILE_APPROVAL.getFlowOwnerType());
            if (flow == null) {
                LOGGER.error("Enable decoration flow not found, moduleId={}", FlowConstants.DECORATION_MODULE);
                throw RuntimeErrorException.errorWith("decoration",
                        10001, "请开启工作流后重试");
            }
            CreateFlowCaseCommand cmd2 = new CreateFlowCaseCommand();
            cmd2.setApplyUserId(request.getApplyUid());
            cmd2.setReferId(request.getId());
            cmd2.setReferType(DecorationRequestStatus.FILE_APPROVAL.getFlowOwnerType());
            cmd2.setProjectId(request.getCommunityId());
            cmd2.setProjectType(EntityType.COMMUNITY.getCode());
            cmd2.setTitle(getAppName());

            cmd2.setServiceType(cmd2.getTitle());
            cmd2.setTitle(cmd2.getTitle()+"("+DecorationRequestStatus.FILE_APPROVAL.getDescribe()+")");
            StringBuilder content = new StringBuilder();
            content.append("公司名称:"+request.getApplyCompany()+"\n");
            content.append("装修地点:"+convertAddress(request.getAddress())+"\n");
            content.append("装修日期:"+sdfyMd.format(new Date(request.getStartTime().getTime()))+"至"+
                    sdfyMd.format(new Date(request.getEndTime().getTime())));
            cmd2.setContent(content.toString());
            cmd2.setFlowMainId(flow.getFlowMainId());
            cmd2.setFlowVersion(flow.getFlowVersion());
            flowService.createFlowCase(cmd2);

            request.setStatus(DecorationRequestStatus.FILE_APPROVAL.getCode());
            request.setDecoratorQrid(createQrCode(requestId,null,request.getDecoratorPhone(),ProcessorType.CHIEF.getCode()));
            this.decorationProvider.updateDecorationRequest(request);

            //短信通知
            decorationSMSProcessor.applySuccess(request);
        }catch (Exception e){
            LOGGER.error("DecorationApplySuccess error e:",e);
            throw e;
        }
    }

    @Override
    public void FileApprovalSuccess(Long requestId) {
        DecorationRequest request = this.decorationProvider.getRequestById(requestId);
        if (DecorationRequestStatus.FILE_APPROVAL.getCode() != request.getStatus())
            throw RuntimeErrorException.errorWith("decoration",
                    1001, "错误的节点状态");
        request.setStatus(DecorationRequestStatus.PAYMENT.getCode());
        this.decorationProvider.updateDecorationRequest(request);
        //短信通知
        decorationSMSProcessor.fileApprovalSuccess(request);
    }

    @Override
    public void DecorationCheckSuccess(Long requestId) {
        DecorationRequest request = this.decorationProvider.getRequestById(requestId);
        if (DecorationRequestStatus.CHECK.getCode() != request.getStatus())
            throw RuntimeErrorException.errorWith("decoration",
                    1001, "错误的节点状态");
        request.setStatus(DecorationRequestStatus.REFOUND.getCode());
        this.decorationProvider.updateDecorationRequest(request);
        //短信通知
        decorationSMSProcessor.checkSuccess(request);
    }

    @Override
    public DecorationFlowCaseDTO completeDecoration(RequestIdCommand cmd) {
        DecorationRequest request = this.decorationProvider.getRequestById(cmd.getRequestId());
        if (DecorationRequestStatus.CONSTRACT.getCode() != request.getStatus())
            throw RuntimeErrorException.errorWith("decoration",
                    1001, "错误的节点状态");
        Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(),EntityType.COMMUNITY.getCode(),request.getCommunityId(),
                DecorationController.moduleId, DecorationController.moduleType, request.getCommunityId(), DecorationRequestStatus.CHECK.getFlowOwnerType());
        if (flow == null) {
            LOGGER.error("Enable decoration flow not found, moduleId={}", FlowConstants.DECORATION_MODULE);
            throw RuntimeErrorException.errorWith("decoration",
                    10001, "请开启工作流后重试");
        }
        CreateFlowCaseCommand cmd2 = new CreateFlowCaseCommand();
        cmd2.setApplyUserId(request.getApplyUid());
        cmd2.setReferId(request.getId());
        cmd2.setReferType(DecorationRequestStatus.CHECK.getFlowOwnerType());
        cmd2.setProjectId(request.getCommunityId());
        cmd2.setProjectType(EntityType.COMMUNITY.getCode());
        cmd2.setTitle(getAppName());

        cmd2.setServiceType(cmd2.getTitle());
        cmd2.setTitle(cmd2.getTitle()+"("+DecorationRequestStatus.CHECK.getDescribe()+")");
        StringBuilder content = new StringBuilder();
        content.append("公司名称:"+request.getApplyCompany()+"\n");
        content.append("装修地点:"+convertAddress(request.getAddress())+"\n");
        content.append("装修日期:"+sdfyMd.format(new Date(request.getStartTime().getTime()))+"至"+
                sdfyMd.format(new Date(request.getEndTime().getTime())));
        cmd2.setContent(content.toString());
        cmd2.setFlowMainId(flow.getFlowMainId());
        cmd2.setFlowVersion(flow.getFlowVersion());
        FlowCase flowCase = flowService.createFlowCase(cmd2);

        request.setStatus(DecorationRequestStatus.CHECK.getCode());
        this.decorationProvider.updateDecorationRequest(request);
        DecorationFlowCaseDTO dto = new DecorationFlowCaseDTO();
        dto.setFlowCaseId(flowCase.getId());
        return dto;
    }

    @Override
    public List<DecorationCompanyDTO> listDecorationCompanies(ListDecorationCompaniesCommand cmd) {
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        List<DecorationCompany> list = this.decorationProvider.listDecorationCompanies(cmd.getNamespaceId(),cmd.getCompanyName());
        if (list == null || list.size() == 0)
            return  null;
        return list.stream().map(r->{
            DecorationCompanyDTO dto = ConvertHelper.convert(r,DecorationCompanyDTO.class);
            List<DecorationCompanyChief> chiefs = this.decorationProvider.listChiefsByCompanyId(r.getId());
            if (chiefs !=null && chiefs.size()!= 0)
                dto.setCompanyChiefs(chiefs.stream().map(p->ConvertHelper.convert(p,CompanyChiefDTO.class)).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public QrDetailDTO getQrDetail(GetLicenseCommand cmd) {
        DecorationRequest request = this.decorationProvider.getRequestById(cmd.getRequestId());
        QrDetailDTO dto = new QrDetailDTO();
        if (cmd.getProcessorType() == null || cmd.getProcessorType() == ProcessorType.CHIEF.getCode()){
            dto.setName(request.getDecoratorName());
            dto.setPhone(request.getDecoratorPhone());
            dto.setWorkerType("装修负责人");
        }else{
            DecorationWorker worker = this.decorationProvider.getDecorationWorkerById(cmd.getWorkerId());
            dto.setName(worker.getName());
            dto.setPhone(worker.getPhone());
            dto.setWorkerType(worker.getWorkerType());
            if (!StringUtils.isBlank(worker.getImage()))
                dto.setImageUrl(this.contentServerService.parserUri(worker.getImage()));
        }
        DecorationRequestDTO requestDTO = convertRequest(request, ProcessorType.WORKER.getCode());
        org.springframework.beans.BeanUtils.copyProperties(requestDTO,dto);
        if (request.getStatus() != DecorationRequestStatus.CONSTRACT.getCode())
            dto.setFlowCasees(null);
        else{//填充其他申请
            if (dto.getFlowCasees() == null)
                dto.setFlowCasees(new ArrayList<>());
            ListGeneralApprovalCommand cmd2 = new ListGeneralApprovalCommand();
            cmd2.setOwnerId(request.getCommunityId());
            cmd2.setOwnerType(EntityType.COMMUNITY.getCode());
            cmd2.setModuleId(DecorationController.moduleId);
            cmd2.setModuleType(DecorationController.moduleType);
            ListGeneralApprovalResponse response = generalApprovalService.listGeneralApproval(cmd2);
            List<GeneralApprovalDTO> dtos = response.getDtos();
            if (dtos != null && dtos.size()>0)
                for (GeneralApprovalDTO dto2 : dtos){
                    boolean flag = dto.getFlowCasees().stream().noneMatch(r -> dto2.getApprovalName().equals(r.getApprovalName()));
                    flag = flag && dto2.getStatus().equals((byte)2);
                    if (flag){
                        DecorationFlowCaseDTO dto3 = new DecorationFlowCaseDTO();
                        dto3.setStatus((byte)-1);
                        dto3.setApprovalName(dto2.getApprovalName());
                        dto.getFlowCasees().add(dto3);
                    }
                }
        }
        return dto;
    }

    @Override
    public DecorationLicenseDTO getLicense(GetLicenseCommand cmd) {
        DecorationLicenseDTO dto = new DecorationLicenseDTO();
        if (cmd.getProcessorType() == ProcessorType.CHIEF.getCode()){
            if (cmd.getWorkerId() == null){//查看负责人自己的
                DecorationRequest request = this.decorationProvider.getRequestById(cmd.getRequestId());
                dto.setName(request.getDecoratorName());
                dto.setQrUrl(processQrUrl(request.getDecoratorQrid()));
            }else{ //查看工人的
                DecorationWorker worker = this.decorationProvider.getDecorationWorkerById(cmd.getWorkerId());
                dto.setName(worker.getName());
                dto.setWorkerType(worker.getWorkerType());
                dto.setQrUrl(processQrUrl(worker.getQrid()));
                dto.setImageUrl(this.contentServerService.parserUri(worker.getImage()));
            }
        }else if (cmd.getProcessorType() == ProcessorType.WORKER.getCode()){ //查看工人自己的
            DecorationWorker worker =  this.decorationProvider.queryDecorationWorker(cmd.getRequestId(),cmd.getPhone());
            dto.setName(worker.getName());
            dto.setWorkerType(worker.getWorkerType());
            dto.setQrUrl(processQrUrl(worker.getQrid()));
            dto.setImageUrl(this.contentServerService.parserUri(worker.getImage()));
            if (worker.getUid() == null){
                worker.setUid(UserContext.currentUserId());
                this.decorationProvider.updateDecorationWorker(worker);
            }
        }
        return dto;
    }

    private String processQrUrl(String qrid){
        String url = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        if(!url.endsWith("/")) {
            url += "/";
        }
        url += "qr?qrid=" + qrid;
        return url;
    }

    @Override
    public GetUserMemberGroupResponse getUserMemberGroup(GetUserMemberGroupCommand cmd) {
        Long uid = UserContext.currentUserId();
        GetUserMemberGroupResponse response = new GetUserMemberGroupResponse();
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(uid,cmd.getOrganizationId());
        if (member == null)
            return null;
        response.setMemberGroup(member.getMemberGroup());
        return response;
    }

    @Override
    public void exportRequests(SearchRequestsCommand cmd, HttpServletResponse response) {
        if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4300043010L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        Integer pageSize = Integer.MAX_VALUE;
        String address = cmd.getBuildingName();
        if (cmd.getStatus() != null)
            cmd.setCancelFlag((byte)0);
        if (cmd.getDoorPlate() != null)
            address += "&"+cmd.getDoorPlate();
        List<DecorationRequest> requests =  this.decorationProvider.queryDecorationRequests(UserContext.getCurrentNamespaceId(),cmd.getCommunityId(),cmd.getStartTime(),
                cmd.getEndTime(),address,cmd.getStatus(),cmd.getKeyword(),cmd.getCancelFlag(),pageSize,null);

        List<DecorationRequestDTO> dtos = processSearchResult(requests);

        ByteArrayOutputStream out = createRequestsStream(dtos);

        DownloadUtils.download(out, response);
    }

    @Override
    public DecorationFlowCaseDTO getApprovalVals(getApprovalValsCommand cmd) {
        List<DecorationApprovalVal> vals = this.decorationProvider.listApprovalVals(cmd.getRequestId(),cmd.getApprovalId());
        if (vals == null || vals.isEmpty())
            return null;
        DecorationFlowCaseDTO dto  = new DecorationFlowCaseDTO();
        dto.setApprovalName(vals.get(0).getApprovalName());
        FlowCase flowCase = this.flowCaseProvider.findFlowCaseByReferId(vals.get(0).getId(),DecorationRequestStatus.CONSTRACT.getFlowOwnerType(),
                DecorationController.moduleId);
        dto.setStatus(flowCase.getStatus());
        dto.setFlowCaseId(flowCase.getId());
        return dto;
    }

    private ByteArrayOutputStream createRequestsStream(List<DecorationRequestDTO> dtos){
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (null == dtos || dtos.isEmpty()) {
            return out;
        }
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("request");
        createRequestsBookSheetHead(sheet);
        for (DecorationRequestDTO dto:dtos){
            this.setNewRequestsBookRow(sheet,dto);
        }

        try {
            wb.write(out);
            wb.close();

        } catch (IOException e) {
            LOGGER.error("export is fail", e);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CREATE_EXCEL,
                    "export is fail.");
        }

        return out;
    }

    private void createRequestsBookSheetHead(Sheet sheet){
        Row row = sheet.createRow(sheet.getLastRowNum());
        int i =-1 ;
        row.createCell(++i).setCellValue("序号");
        row.createCell(++i).setCellValue("提交时间");
        row.createCell(++i).setCellValue("装修地点");
        row.createCell(++i).setCellValue("装修日期");
        row.createCell(++i).setCellValue("申请人");
        row.createCell(++i).setCellValue("联系方式");
        row.createCell(++i).setCellValue("公司名称");
        row.createCell(++i).setCellValue("装修进度");
    }

    private void setNewRequestsBookRow(Sheet sheet,DecorationRequestDTO dto){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
        //序号
        row.createCell(++i).setCellValue(row.getRowNum());
        //提交时间
        row.createCell(++i).setCellValue(sdfyMdHm.format(new Date(dto.getCreateTime())));
        //装修地点
        row.createCell(++i).setCellValue(dto.getAddress());
        //装修日期
        row.createCell(++i).setCellValue(sdfyMd.format(new Date(dto.getStartTime()))+"至"+sdfMd.format(new Date(dto.getEndTime())));
        //申请人
        row.createCell(++i).setCellValue(dto.getApplyName());
        //联系方式
        row.createCell(++i).setCellValue(dto.getApplyPhone());
        //公司名称
        row.createCell(++i).setCellValue(dto.getApplyCompany());
        //装修进度
        if (dto.getCancelFlag() == null || dto.getCancelFlag() == 0) {
            if (dto.getStatus() != null)
                row.createCell(++i).setCellValue(DecorationRequestStatus.fromCode(dto.getStatus()).getDescribe());
        }
        else
            row.createCell(++i).setCellValue("已取消");
    }

    @Override
    public void exportWorkers(ListWorkersCommand cmd, HttpServletResponse response) {
        Integer pageSize = Integer.MAX_VALUE;
        List<DecorationWorker> workers = decorationProvider.listWorkersByRequestId(cmd.getRequestId(),cmd.getKeyword(),null,pageSize);
        List<DecorationWorkerDTO> dtos = workers.stream().map(r->ConvertHelper.convert(r,DecorationWorkerDTO.class)).collect(Collectors.toList());
        ByteArrayOutputStream out = createWorkersStream(dtos);

        DownloadUtils.download(out, response);
    }

    private ByteArrayOutputStream createWorkersStream(List<DecorationWorkerDTO> dtos){
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (null == dtos || dtos.isEmpty()) {
            return out;
        }
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("worker");
        createWorkersBookSheetHead(sheet);

        for (DecorationWorkerDTO dto:dtos){
            this.setNewWorkersBookRow(sheet,dto);
        }
        try {
            wb.write(out);
            wb.close();

        } catch (IOException e) {
            LOGGER.error("export is fail", e);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CREATE_EXCEL,
                    "export is fail.");
        }

        return out;

    }

    private void createWorkersBookSheetHead(Sheet sheet){
        Row row = sheet.createRow(sheet.getLastRowNum());
        int i =-1 ;
        row.createCell(++i).setCellValue("序号");
        row.createCell(++i).setCellValue("姓名");
        row.createCell(++i).setCellValue("联系方式");
        row.createCell(++i).setCellValue("工种");
    }

    private void setNewWorkersBookRow(Sheet sheet,DecorationWorkerDTO dto){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
        //序号
        row.createCell(++i).setCellValue(row.getRowNum());
        //姓名
        row.createCell(++i).setCellValue(dto.getName());
        //联系方式
        row.createCell(++i).setCellValue(dto.getPhone());
        //工种
        row.createCell(++i).setCellValue(dto.getWorkerType());
    }
}
