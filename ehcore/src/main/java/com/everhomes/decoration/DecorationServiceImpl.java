package com.everhomes.decoration;

import com.everhomes.archives.ArchivesService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.archives.AddArchivesContactCommand;
import com.everhomes.rest.decoration.*;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommand;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommandResponse;
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
import java.util.ArrayList;
import java.util.List;
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
    public DecorationIllustrationDTO createRequest(CreateRequestCommand cmd) {
        DecorationRequest request = ConvertHelper.convert(cmd,DecorationRequest.class);
        request.setNamespaceId(UserContext.getCurrentNamespaceId());
        request.setStartTime(new Timestamp(cmd.getStartTime()));
        request.setEndTime(new Timestamp(cmd.getEndTime()));
        request.setApplyUid(UserContext.currentUserId());
        request.setCancelFlag((byte)0);
        this.dbProvider.execute((TransactionStatus status) -> {
            decorationProvider.createDecorationRequest(request);
            Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(),EntityType.COMMUNITY.getCode(),cmd.getCommunityId(),
            DecorationController.moduleId, DecorationController.moduleType, null, DecorationRequestStatus.APPLY.getFlowOwnerType());
           return null;
        });

        return null;
    }
}
