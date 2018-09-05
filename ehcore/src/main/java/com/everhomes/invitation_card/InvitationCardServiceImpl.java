package com.everhomes.invitation_card;

import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.invitation_Card.InvitationCardProvider;
import com.everhomes.invitation_Card.InvitationCardService;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.invitation_card.GetInvitationFormCommond;
import com.everhomes.rest.invitation_card.InvitationCardErrorCode;
import com.everhomes.rest.invitation_card.UpdateInvitationActiveStatusCommond;
import com.everhomes.rest.invitation_card.UpdateInvitationApprovalActiveFormCommond;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationCardServiceImpl implements InvitationCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitationCardServiceImpl.class);


    @Autowired
    GeneralFormProvider generalFormProvider;

    @Autowired
    GeneralApprovalProvider generalApprovalProvider;

    @Autowired
    InvitationCardProvider invitationCardProvider;


    @Override
    public void updateInvitationApprovalActiveForm(UpdateInvitationApprovalActiveFormCommond cmd) {
        GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(cmd.getSourceId());
        if(cmd.getFormOriginId() != null && cmd.getFormOriginId() > 0l && cmd.getFormVersion() > 0 && cmd.getFormVersion() != null) {
            approval.setFormOriginId(cmd.getFormOriginId());
            approval.setFormVersion(cmd.getFormVersion());
            generalApprovalProvider.updateGeneralApproval(approval);
        }else{
            LOGGER.error("form origin id or form version param cannot null. form_origin_id : " + cmd.getFormOriginId() + ", form_version : " + cmd.getFormVersion());
            throw RuntimeErrorException.errorWith(InvitationCardErrorCode.SCOPE,
                    InvitationCardErrorCode.ERROR_FORM_PARAM, "form origin id or form version param cannot null");

        }

    }


    @Override
    public void updateInvitationApprovalActiveStatus(UpdateInvitationActiveStatusCommond cmd) {
        GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(cmd.getId());
        if(cmd.getStatus() != null) {
            approval.setStatus(cmd.getStatus());
            if(cmd.getStatus().equals(GeneralApprovalStatus.RUNNING.getCode())){
                GeneralApproval oldRunning = invitationCardProvider.getRunnintInvitationApproval(cmd.getModuleId(), cmd.getNamespaceId());
                oldRunning.setStatus(GeneralApprovalStatus.INVALID.getCode());
                generalApprovalProvider.updateGeneralApproval(oldRunning);
                generalApprovalProvider.updateGeneralApproval(approval);
            }else{
                generalApprovalProvider.updateGeneralApproval(approval);
            }
        }

    }

    @Override
    public GeneralFormDTO getInvitationForm(GetInvitationFormCommond cmd) {
        GeneralApproval runningApproval = invitationCardProvider.getRunnintInvitationApproval(cmd.getModuleId(), cmd.getNamespaceId());
        GeneralForm form = generalFormProvider.getGeneralFormById(runningApproval.getFormOriginId());
        return ConvertHelper.convert(form, GeneralFormDTO.class);
    }
}
