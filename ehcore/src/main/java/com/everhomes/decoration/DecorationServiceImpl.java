package com.everhomes.decoration;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.decoration.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

@Component
public class DecorationServiceImpl implements  DecorationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationServiceImpl.class);

    @Autowired
    private DecorationProvider decorationProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private DbProvider dbProvider;
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
        DecorationRequest request = decorationProvider.getRequestById(cmd.getRequestId());
        if (cmd.getId()!=null){
            DecorationWorker worker = decorationProvider.getDecorationWorkerById(cmd.getId());
            org.springframework.beans.BeanUtils.copyProperties(cmd,worker);
            decorationProvider.updateDecorationWorker(worker);
        }
        return null;
    }
}
