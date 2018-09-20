// @formatter:off
package com.everhomes.community_form;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.community_form.CommunityGeneralFormDTO;
import com.everhomes.rest.community_form.CreateOrUpdateCommunityFormCommand;
import com.everhomes.rest.community_form.DeleteCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormResponse;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.module.Project;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommunityFormServiceImpl implements CommunityFormService{

    @Autowired
    private CommunityFormProvider communityFormProvider;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private CommunityProvider communityProvider;
    @Override
    public CommunityGeneralFormDTO createOrUpdateCommunityGeneralForm(CreateOrUpdateCommunityFormCommand cmd) {
        CommunityGeneralForm communityGeneralForm  = this.communityFormProvider.findCommunityGeneralForm(cmd.getCommunityId(),cmd.getType());
        if (communityGeneralForm == null) {
            communityGeneralForm = ConvertHelper.convert(cmd,CommunityGeneralForm.class);
            this.communityFormProvider.createCommunityGeneralForm(communityGeneralForm);
        }else {
            communityGeneralForm.setFormOriginId(cmd.getFormOriginId());
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

    private List<Long> getCommunityIds(ProjectDTO projectDTO) {
        List<Long> communnityIds = new ArrayList<>();
        if (projectDTO != null) {
            communnityIds.add(projectDTO.getProjectId());
            if (!CollectionUtils.isEmpty(projectDTO.getProjects())) {
                for (ProjectDTO p : projectDTO.getProjects()) {
                    communnityIds.addAll(getCommunityIds(p));
                }
            }
        }
        return communnityIds;
    }
    @Override
    public ListCommunityFormResponse listCommunityForm(ListCommunityFormCommand cmd) {
        ListUserRelatedProjectByModuleCommand listUserRelatedProjectByModuleCommand = ConvertHelper.convert(cmd, ListUserRelatedProjectByModuleCommand.class);
        List<ProjectDTO> projects  = this.serviceModuleService.listUserRelatedCategoryProjectByModuleId(listUserRelatedProjectByModuleCommand);
        List<CommunityGeneralFormDTO> list = new ArrayList<>();
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        Integer pageOffset = 1;
        if (cmd.getPageOffset() != null){
            pageOffset = cmd.getPageOffset();
        }
        Integer offset =  (int) ((pageOffset - 1 ) * pageSize);
        if (!CollectionUtils.isEmpty(projects)) {
            List<Long> communnityIds = new ArrayList<>();
            for (ProjectDTO projectDTO : projects) {
                if (projectDTO == null)
                    continue;

                communnityIds.addAll(getCommunityIds(projectDTO));
            }
            List<CommunityGeneralForm> generalFormList = this.communityFormProvider.listCommunityGeneralFormByCommunityIds(communnityIds,cmd.getType(),offset,pageSize+1);
            if (!CollectionUtils.isEmpty(generalFormList)) {
                for (CommunityGeneralForm communityGeneralForm : generalFormList) {
                    CommunityGeneralFormDTO communityGeneralFormDTO = ConvertHelper.convert(communityGeneralForm, CommunityGeneralFormDTO.class);
                    Community community = this.communityProvider.findCommunityById(communityGeneralFormDTO.getCommunityId());
                    if (community != null) {
                        communityGeneralFormDTO.setCommunityName(community.getName());
                    }
                    if (communityGeneralFormDTO.getFormOriginId() != null) {
                        GeneralForm generalForm = this.generalFormProvider.getActiveGeneralFormByOriginId(communityGeneralFormDTO.getFormOriginId());
                        if (generalForm != null) {
                            communityGeneralFormDTO.setFormName(generalForm.getFormName());
                        }else {
                            communityGeneralFormDTO.setFormOriginId(null);
                        }
                    }
                    list.add(communityGeneralFormDTO);
                }
            }
        }
        Collections.sort(list,new Comparator<CommunityGeneralFormDTO>() {
            public int compare(CommunityGeneralFormDTO arg0, CommunityGeneralFormDTO arg1) {
                return arg1.getCommunityId().compareTo(arg0.getCommunityId());
            }
        });
        Integer nextPageOffset = null;
        if (list.size() > pageSize) {
            list.remove(list.size()-1);
            nextPageOffset = pageOffset + 1;
        }
        ListCommunityFormResponse response = new ListCommunityFormResponse();
        response.setNextPageOffset(nextPageOffset);
        response.setDtos(list);
        return response;
    }
}
