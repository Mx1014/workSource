package com.everhomes.archives;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.general_approval.GeneralFormFieldAttribute;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArchivesFormServiceImpl implements ArchivesFormService{

    private static final String ARCHIVES_FORM = "ARCHIVES_FORM";

    @Autowired
    private ArchivesFormProvider archivesFormProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public ArchivesFormDTO getArchivesFormByOrgId(Integer namespaceId, Long orgId) {
        ArchivesForm form = archivesFormProvider.findArchivesFormByOrgId(namespaceId, orgId);
        if (form == null)
            return null;
        ArchivesFormDTO dto = ConvertHelper.convert(form, ArchivesFormDTO.class);
        List<GeneralFormFieldDTO> fields = JSONObject.parseArray(form.getStaticFields(), GeneralFormFieldDTO.class);
        if(form.getDynamicFields() !=null)
            fields.addAll(JSONObject.parseArray(form.getDynamicFields(), GeneralFormFieldDTO.class));
        dto.setFormFields(fields);
        dto.setFormGroups(getFormGroupByFormId(form.getNamespaceId(), form.getId()));
        return dto;
    }

    @Override
    public ArchivesFormDTO getArchivesDefaultForm() {
        ArchivesForm form = archivesFormProvider.findArchivesDefaultForm(ARCHIVES_FORM);
        ArchivesFormDTO dto = ConvertHelper.convert(form, ArchivesFormDTO.class);
        List<GeneralFormFieldDTO> fields = JSONObject.parseArray(form.getStaticFields(), GeneralFormFieldDTO.class);
        dto.setFormFields(fields);
        dto.setFormGroups(getFormGroupByFormId(form.getNamespaceId(), form.getId()));
        return dto;
    }

    @Override
    public void updateArchivesForm(UpdateArchivesFormCommand cmd) {

        List<GeneralFormFieldDTO> dynamicFields = cmd.getFormFields().stream().filter(r -> !GeneralFormFieldAttribute.DEFAULT.getCode().equals(r.getFieldAttribute())).collect(Collectors.toList());
        Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if (org == null)
            return;

        //  1.Find the form which is belong to the company.
        ArchivesForm form = archivesFormProvider.findArchivesFormByOrgId(cmd.getNamespaceId(), cmd.getOrganizationId());
        //  2-1.Create it if not existed.
        if (form == null) {
            ArchivesForm newForm = archivesFormProvider.findArchivesDefaultForm(ARCHIVES_FORM);
            newForm.setOwnerId(cmd.getOrganizationId());
            newForm.setOwnerType("EhOrganizations");
            newForm.setFormName(org.getName());
            newForm.setNamespaceId(cmd.getNamespaceId());
            newForm.setDynamicFields(JSONObject.toJSONString(dynamicFields));
            archivesFormProvider.createArchivesForm(newForm);
            updateFormGroup(newForm, cmd.getFormGroups());
        } else {
            //  2-2.Update it
            form.setDynamicFields(JSONObject.toJSONString(dynamicFields));
            archivesFormProvider.updateArchivesForm(form);
            updateFormGroup(form, cmd.getFormGroups());
        }

    }

    @Override
    public GetArchivesFormResponse getArchivesForm(GetArchivesFormCommand cmd) {
        GetArchivesFormResponse response = new GetArchivesFormResponse();
        ArchivesFormDTO dto = getArchivesFormByOrgId(cmd.getNamespaceId(), cmd.getOrganizationId());
        if(dto == null)
            dto = getArchivesDefaultForm();
        response.setForm(dto);
        return response;
    }

    /* 增添人事档案动态字段值 */
    @Override
    public void addArchivesDynamicValues(OrganizationMemberDetails employee, List<PostApprovalFormItem> dynamicItems) {
        if (dynamicItems == null)
            return;
        ArchivesForm form = archivesFormProvider.findArchivesFormByOrgId(employee.getNamespaceId(), employee.getOrganizationId());
        if (form == null)
            return;

        for (PostApprovalFormItem val : dynamicItems) {
            ArchivesFormVal obj = archivesFormProvider.findArchivesFormValBySourceIdAndName(form.getId(), employee.getId(), val.getFieldName());
            if (obj != null) {
                obj.setFieldValue(val.getFieldValue());
                archivesFormProvider.updateArchivesFormVal(obj);
            } else {
                obj.setNamespaceId(form.getNamespaceId());
                obj.setOwnerId(form.getOwnerId());
                obj.setOwnerType(form.getOwnerType());
                obj.setFormId(form.getId());
                obj.setSourceId(employee.getId());
                obj.setFieldName(val.getFieldName());
                obj.setFieldType(val.getFieldType());
                obj.setFieldValue(val.getFieldValue());
                archivesFormProvider.createArchivesFormVal(obj);
            }
        }
    }

    public List<GeneralFormFieldDTO> getArchivesDynamicValues(Long formId, Long detailId){

    }

    private List<ArchivesFormGroupDTO> getFormGroupByFormId(Integer namespaceId, Long formId){
        ArchivesFormGroup group = archivesFormProvider.findArchivesFormGroupByFormId(namespaceId, formId);
        if(group == null)
            return null;
        return JSONObject.parseArray(group.getFormGroups(), ArchivesFormGroupDTO.class);
    }

    private void updateFormGroup(ArchivesForm form, List<ArchivesFormGroupDTO> groups){
        ArchivesFormGroup group = archivesFormProvider.findArchivesFormGroupByFormId(form.getNamespaceId(), form.getId());
        if(group == null){
            group = ConvertHelper.convert(form, ArchivesFormGroup.class);
            group.setFormId(form.getId());
            group.setFormGroups(JSONObject.toJSONString(groups));
            archivesFormProvider.createArchivesFormGroup(group);
        }else{
            group.setFormGroups(JSONObject.toJSONString(groups));
            archivesFormProvider.updateArchivesFormGroup(group);
        }
    }

    /*
        GetArchivesFormResponse response = new GetArchivesFormResponse();
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand();
        formCommand.setFormOriginId(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);

        //  摒弃冗余字段
        form.setTemplateText(null);
        //  由于业务的特殊性，此处的 formOriginId 由另外的接口去提供
        //  故屏蔽掉此处的返回以免造成误解
        form.setFormOriginId(null);
        form.setFormVersion(null);
        response.setForm(form);
        return response;*/

    /*
        //  1.如果无 formOriginId 时则使用的是模板，此时在组织结构新增一份表单，同时业务表单组新增记录
        //  2.如果有 formOriginId 时则说明已经拥有了表单，此时在组织架构做修改，同时业务表单组同步记录

        if (cmd.getFormOriginId() == 0L) {
            //  1.先在组织架构增加表单
            CreateApprovalFormCommand createCommand = new CreateApprovalFormCommand();
            createCommand.setOwnerId(cmd.getOrganizationId());
            createCommand.setOwnerType(ARCHIVES_OWNER_TYPE);
            createCommand.setOrganizationId(cmd.getOrganizationId());
            createCommand.setFormName(ARCHIVES_FORM);
            createCommand.setFormFields(cmd.getFormFields());
            createCommand.setFormGroups(cmd.getFormGroups());
            GeneralFormDTO form = dbProvider.execute((TransactionStatus status) -> {
                GeneralFormDTO dto = generalFormService.createGeneralForm(createCommand);
                //  2.在业务表单组新增记录
                createArchivesForm(dto);
                return dto;
            });
            return form;
        } else {
            GeneralFormDTO form = dbProvider.execute((TransactionStatus status) -> {
                //  1.为人事档案单独做一个表单的更新处理
                GeneralFormDTO dto = updateGeneralFormForArchives(cmd);
                //  2.在业务表单同步记录
                ArchivesFroms archivesFroms = archivesProvider.findArchivesFormOriginId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());
                archivesFroms.setFormOriginId(dto.getFormOriginId());
                archivesFroms.setFormVersion(dto.getFormVersion());
                archivesProvider.updateArchivesForm(archivesFroms);
                return dto;
            });
            return form;
            */

   /*
      为人事档案单独做一个表单的更新处理
    private ArchivesFormDTO updateGeneralFormForArchives(UpdateArchivesFormCommand cmd) {
        //  1.更新表单的字段
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(cmd
                .getFormOriginId());
        if (null == form)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "form not found");
        form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
        generalFormProvider.updateGeneralForm(form);
        //  2.更新表单的字段组
        GeneralFormGroup group = generalFormProvider.findGeneralFormGroupByFormOriginId(form.getFormOriginId());
        if (group == null) {
            //  若为空说明之前的表单建立并未建字段组
            generalFormService.createGeneralFormGroup(form, cmd.getFormGroups());
        } else {
            //  不为空则说明之前的表单建立过字段组
            generalFormService.updateGeneralFormGroupByFormId(group, form, cmd.getFormGroups());
        }
        return ConvertHelper.convert(form, GeneralFormDTO.class);
    }*/

    /*
    private void createArchivesForm(GeneralFormDTO form) {
        ArchivesFroms archivesFrom = new ArchivesFroms();
        archivesFrom.setNamespaceId(form.getNamespaceId());
        archivesFrom.setOrganizationId(form.getOrganizationId());
        archivesFrom.setFormOriginId(form.getFormOriginId());
        archivesFrom.setFormVersion(form.getFormVersion());
        archivesFrom.setStatus(form.getStatus());
        archivesProvider.createArchivesForm(archivesFrom);
    }
    */

    /**
     * 为人事档案单独做一个表单值的更新处理
     *//*
    private void addGeneralFormValuesForArchives(Long formOriginId, OrganizationMemberDetails employee, List<PostApprovalFormItem> dynamicItems) {
        if (null != dynamicItems) {
            GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(formOriginId);

            for (PostApprovalFormItem val : dynamicItems) {

                GeneralFormVal obj = generalFormValProvider.getGeneralFormValBySourceIdAndName(employee.getId(), GeneralFormSourceType.ARCHIVES_AUTH.getCode(), val.getFieldName());
                if (obj == null) {
                    obj = new GeneralFormVal();
                    //与表单信息一致
                    obj.setNamespaceId(form.getNamespaceId());
                    obj.setOrganizationId(form.getOrganizationId());
                    obj.setOwnerId(form.getOwnerId());
                    obj.setOwnerType(form.getOwnerType());
                    obj.setModuleId(form.getModuleId());
                    obj.setModuleType(form.getModuleType());
                    obj.setFormOriginId(form.getFormOriginId());
                    obj.setFormVersion(form.getFormVersion());

                    obj.setSourceType(GeneralFormSourceType.ARCHIVES_AUTH.getCode());
                    obj.setSourceId(employee.getId());
                    obj.setFieldName(val.getFieldName());
                    obj.setFieldType(val.getFieldType());
                    obj.setFieldValue(val.getFieldValue());
                    generalFormValProvider.createGeneralFormVal(obj);
                } else {
                    obj.setFieldValue(val.getFieldValue());
                    generalFormValProvider.updateGeneralFormVal(obj);
                }
            }
        }
    }*/
}
