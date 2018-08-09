// @formatter:off
package com.everhomes.community_form;

import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.community_form.CommunityGeneralFormDTO;
import com.everhomes.rest.community_form.CreateOrUpdateCommunityFormCommand;
import com.everhomes.rest.community_form.DeleteCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormResponse;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.module.Project;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommunityFormServiceImpl implements CommunityFormService{

    @Autowired
    private CommunityFormProvider communityFormProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;
    @Override
    public CommunityGeneralFormDTO createOrUpdateCommunityGeneralForm(CreateOrUpdateCommunityFormCommand cmd) {
        CommunityGeneralForm communityGeneralForm  = this.communityFormProvider.findCommunityGeneralForm(cmd.getCommunityId(),cmd.getType());
        if (communityGeneralForm == null) {
            communityGeneralForm = ConvertHelper.convert(cmd,CommunityGeneralForm.class);
            this.communityFormProvider.createCommunityGeneralForm(communityGeneralForm);
        }else {
            communityGeneralForm.setFormId(cmd.getFormId());
            this.communityFormProvider.updateCommunityGeneralForm(communityGeneralForm);
        }
        return ConvertHelper.convert(communityGeneralForm,CommunityGeneralFormDTO.class);
    }

    @Override
    public void deleteCommunityGeneralForm(DeleteCommunityFormCommand cmd) {
        CommunityGeneralForm communityGeneralForm  = this.communityFormProvider.findCommunityGeneralForm(cmd.getCommunityId(),cmd.getType());
        if (communityGeneralForm != null) {
            this.communityFormProvider.deleteCommunityGeneralForm(communityGeneralForm);
        }
    }

    @Override
    public ListCommunityFormResponse listCommunityForm(ListCommunityFormCommand cmd) {
        ListUserRelatedProjectByModuleCommand listUserRelatedProjectByModuleCommand = ConvertHelper.convert(cmd, ListUserRelatedProjectByModuleCommand.class);
        List<ProjectDTO> projects  = this.serviceModuleService.listUserRelatedCategoryProjectByModuleId(listUserRelatedProjectByModuleCommand);
        List<CommunityGeneralFormDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(projects)) {
            for (ProjectDTO projectDTO : projects) {
                if (projectDTO == null)
                    continue;
                CommunityGeneralForm communityGeneralForm = this.communityFormProvider.findCommunityGeneralForm(projectDTO.getProjectId(),cmd.getType());
                if (communityGeneralForm != null) {
                    list.add(ConvertHelper.convert(communityGeneralForm,CommunityGeneralFormDTO.class));
                }
            }
        }
        ListCommunityFormResponse response = new ListCommunityFormResponse();
        response.setDtos(list);
        return response;
    }
}
