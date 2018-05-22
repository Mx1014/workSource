package com.everhomes.decoration;

import com.everhomes.archives.ArchivesService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.archives.AddArchivesContactCommand;
import com.everhomes.rest.decoration.*;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommand;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommandResponse;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

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
    private PortalService portalService;
    @Autowired
    private GeneralApprovalService generalApprovalService;

    private static final SimpleDateFormat sdfyMd= new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfMd = new SimpleDateFormat("MM-dd");
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
        dto.setRefundAmount(request.getRefoundAmount());
        return dto;
    }

    @Override
    public GetDecorationFeeResponse getFeeInfo(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());

        return null;
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
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                            ErrorCodes.ERROR_INVALID_PARAMETER, "setting not found");
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

    private void addDecorationAttachment(Long settingId,List<DecorationAttachmentDTO> attachments){
        if (attachments != null){
            attachments.forEach(r->{
                DecorationAttachment attachment = ConvertHelper.convert(r,DecorationAttachment.class);
                attachment.setSettingId(settingId);
                this.decorationProvider.createDecorationAttachment(attachment);
            });
        }
    }

    @Override
    public DecorationWorkerDTO updateWorker(UpdateWorkerCommand cmd) {
        if (cmd.getRequestId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "request id can not be null");
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
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                        ErrorCodes.ERROR_INVALID_PARAMETER, "不可添加装修负责人");
            Long companyId = request.getDecoratorCompanyId();
            if (companyId == null)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                        ErrorCodes.ERROR_INVALID_PARAMETER, "不存在的装修公司");
            DecorationCompany dc = decorationProvider.getDecorationCompanyById(companyId);
            DecorationWorker worker = ConvertHelper.convert(cmd,DecorationWorker.class);
            VerifyPersonnelByPhoneCommand cmd1 = new VerifyPersonnelByPhoneCommand();
            cmd1.setEnterpriseId(dc.getOrganizationId());
            cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
            cmd1.setPhone(worker.getPhone());
            worker.setUid(0l);//注册未登陆
            try {
                VerifyPersonnelByPhoneCommandResponse res1 = organizationService.verifyPersonnelByPhone(cmd1);
               if (res1.getDto()!=null)
                   worker.setUid(res1.getDto().getTargetId());
                AddArchivesContactCommand cmd2 = new AddArchivesContactCommand();
                cmd2.setContactName(worker.getName());
                cmd2.setGender((byte)1);
                cmd2.setRegionCode("86");
                cmd2.setVisibleFlag((byte)0);
                cmd2.setContactToken(worker.getPhone());
                cmd2.setOrganizationId(dc.getOrganizationId());
                cmd2.setDepartmentIds(new ArrayList<>());
                cmd2.getDepartmentIds().add(dc.getOrganizationId());
                archivesService.addArchivesContact(cmd2);//加入企业
            }catch (RuntimeErrorException re){ //已经加入企业 忽略报错
                OrganizationMember member = organizationProvider.findOrganizationPersonnelByPhone(dc.getOrganizationId(), worker.getPhone());
                worker.setUid(member.getTargetId());
            }

            decorationProvider.createDecorationWorker(worker);
            worker.setQrid(createQrCode(worker.getId(),ProcessorType.WORKER.getCode()));
            decorationProvider.updateDecorationWorker(worker);
            dto =  ConvertHelper.convert(worker,DecorationWorkerDTO.class);
            if (!StringUtils.isBlank(dto.getImageUri()))
                dto.setImageUrl(this.contentServerService.parserUri(dto.getImageUri()));
        }

        return dto;
    }

    private String createQrCode(Long workerId,Byte processorType){
        return null;
    }

    @Override
    public ListWorkersResponse listWorkers(ListWorkersCommand cmd) {
        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0l);
        ListingLocator locator = new ListingLocator(cmd.getPageAnchor());
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
        this.dbProvider.execute((TransactionStatus status) -> {
            decorationProvider.createDecorationRequest(request);
            Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(),EntityType.COMMUNITY.getCode(),cmd.getCommunityId(),
                DecorationController.moduleId, DecorationController.moduleType, null, DecorationRequestStatus.APPLY.getFlowOwnerType());
            if (flow == null) {
                LOGGER.error("Enable decoration flow not found, moduleId={}", FlowConstants.DECORATION_MODULE);
                throw RuntimeErrorException.errorWith("decoration",
                        10001, "请开启工作流后重试");
            }
            CreateFlowCaseCommand cmd2 = new CreateFlowCaseCommand();
            cmd2.setApplyUserId(UserContext.currentUserId());
            cmd2.setReferId(request.getId());
            cmd2.setReferType(DecorationRequestStatus.APPLY.getFlowOwnerType());
            cmd2.setProjectId(request.getCommunityId());
            cmd2.setProjectType(EntityType.COMMUNITY.getCode());

            ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
            listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
            listServiceModuleAppsCommand.setModuleId(FlowConstants.DECORATION_MODULE);
            ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
            if (apps!=null && apps.getServiceModuleApps().size()>0)
                cmd2.setTitle(apps.getServiceModuleApps().get(0).getName());
            else
                cmd2.setTitle("装修办理");

            cmd2.setServiceType(cmd2.getTitle());
            cmd2.setTitle(cmd2.getTitle()+"("+parseRequestStatus(DecorationRequestStatus.APPLY.getCode())+")");
            StringBuilder content = new StringBuilder();
            content.append("装修公司:"+request.getDecoratorCompany()+"\n");
            content.append("装修地点:"+convertAddress(request.getAddress())+"\n");
            content.append("装修人日期:"+sdfyMd.format(new Date(request.getStartTime().getTime()))+"至"+
                    sdfyMd.format(new Date(request.getEndTime().getTime())));
            cmd2.setContent(content.toString());
            cmd2.setFlowMainId(flow.getFlowMainId());
            cmd2.setFlowVersion(flow.getFlowVersion());
            flowService.createFlowCase(cmd2);
           return null;
        });

        return convertRequest(request,ProcessorType.MASTER.getCode());
    }

    private DecorationRequestDTO convertRequest(DecorationRequest request, Byte processorType){
        DecorationRequestDTO dto = new DecorationRequestDTO();
        dto.setApplyName(request.getApplyName());
        dto.setApplyPhone(request.getApplyPhone());
        dto.setApplyCompany(request.getApplyCompany());
        dto.setAddress(convertAddress(request.getAddress()));
        dto.setStartTime(request.getStartTime().getTime());
        dto.setEndTime(request.getEndTime().getTime());

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
                        dto.setTotalAmount(fee.getTotalPrice());
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

        //TODO 工作流

        return dto;
    }

    private String convertAddress(String address) {
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
            }
        }

        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()){
            StringBuilder builder = new StringBuilder();
            String building = iterator.next();
            builder.append(building);
            List<String> ads = map.get(building);
            builder.append(ads.get(0));
            if (ads.size()>1){
                for (int i=1;i<ads.size();i++)
                    builder.append(ads.get(i));
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
    public void modifyFee(ModifyFeeCommand cmd) {
        this.dbProvider.execute((TransactionStatus status) -> {
            this.decorationProvider.deleteDecorationFeeByRequestId(cmd.getRequestId());
            DecorationFee fee = new DecorationFee();
            fee.setRequestId(cmd.getRequestId());
            fee.setTotalPrice(cmd.getTotalAmount());
            this.decorationProvider.createDecorationFee(fee);
            if (cmd.getDecorationFee()!=null)
                for (DecorationFeeDTO dto : cmd.getDecorationFee()){
                    fee = new DecorationFee();
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
        request.setRefoundAmount(cmd.getRefoundAmount());
        request.setRefoundComment(cmd.getRefoundComment());
        this.decorationProvider.updateDecorationRequest(request);
    }

    @Override
    public void confirmRefound(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (request.getStatus() != DecorationRequestStatus.REFOUND.getCode())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "错误的节点状态");
        request.setStatus(DecorationRequestStatus.COMPLETE.getCode());
        this.decorationProvider.updateDecorationRequest(request);
    }

    @Override
    public void confirmFee(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (request.getStatus() != DecorationRequestStatus.PAYMENT.getCode())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "错误的节点状态");
        request.setStatus(DecorationRequestStatus.CONSTRACT.getCode());
        this.decorationProvider.updateDecorationRequest(request);
    }

    @Override
    public void cancelRequest(RequestIdCommand cmd) {
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        request.setStatus((byte)2);
        this.decorationProvider.updateDecorationRequest(request);
    }

    @Override
    public void postApprovalForm(PostApprovalFormCommand cmd) {
        GetTemplateByApprovalIdCommand cmd2 = new GetTemplateByApprovalIdCommand();
        cmd2.setApprovalId(cmd.getApprovalId());
        GetTemplateByApprovalIdResponse res1 = this.generalApprovalService.getTemplateByApprovalId(cmd2);

    }
}
