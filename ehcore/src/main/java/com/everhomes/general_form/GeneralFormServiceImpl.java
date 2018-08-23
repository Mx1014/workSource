package com.everhomes.general_form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.general_approval.GeneralApprovalFieldProcessor;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.gogs.GogsCommit;
import com.everhomes.gogs.GogsConflictException;
import com.everhomes.gogs.GogsFileNotExistException;
import com.everhomes.gogs.GogsRawFileParam;
import com.everhomes.gogs.GogsRepo;
import com.everhomes.gogs.GogsRepoType;
import com.everhomes.gogs.GogsService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GeneralFormServiceImpl implements GeneralFormService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralFormServiceImpl.class);

    private static final String FORM_PRINT_TEMPLATE_OWNER_TYPE = "EhGeneralForm";
    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private GeneralFormValProvider generalFormValProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private GeneralApprovalFieldProcessor generalApprovalFieldProcessor;

    @Autowired
    private GeneralApprovalValProvider generalApprovalValProvider;

    @Autowired
    private GogsService gogsService;

    @Autowired
    private UserService userService;

    @Override
    public GeneralFormDTO getTemplateByFormId(GetTemplateByFormIdCommand cmd) {
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd.getFormId());
        if (form == null)
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());

        return handler.getTemplateBySourceId(cmd);
    }

    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormValCommand cmd) {
        GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());
        return handler.postGeneralFormVal(cmd);
    }

    private GeneralFormModuleHandler getOrderHandler(String type) {
        String handler = GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + type;
        return PlatformContext.getComponent(handler);
    }

    @Override
    public void addGeneralFormValues(addGeneralFormValuesCommand cmd) {
        // 把values 存起来
        if (null != cmd.getValues()) {
            GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(cmd.getGeneralFormId());

            dbProvider.execute(status -> {
                if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
                    // 使用表单/审批 注意状态 config
                    form.setStatus(GeneralFormStatus.RUNNING.getCode());
                    generalFormProvider.updateGeneralForm(form);
                }

                for (PostApprovalFormItem val : cmd.getValues()) {
                    GeneralFormVal obj = new GeneralFormVal();
                    //与表单信息一致
                    obj.setNamespaceId(form.getNamespaceId());
                    obj.setOrganizationId(form.getOrganizationId());
                    obj.setOwnerId(form.getOwnerId());
                    obj.setOwnerType(form.getOwnerType());
                    obj.setModuleId(form.getModuleId());
                    obj.setModuleType(form.getModuleType());
                    obj.setFormOriginId(form.getFormOriginId());
                    obj.setFormVersion(form.getFormVersion());

                    obj.setSourceType(cmd.getSourceType());
                    obj.setSourceId(cmd.getSourceId());
                    obj.setFieldName(val.getFieldName());
                    obj.setFieldType(val.getFieldType());
                    obj.setFieldValue(val.getFieldValue());
                    generalFormValProvider.createGeneralFormVal(obj);
                }
                return null;
            });
        }
    }

    @Override
    public List<PostApprovalFormItem> getGeneralFormValues(GetGeneralFormValuesCommand cmd) {
        List<PostApprovalFormItem> results = new ArrayList<>();
        List<GeneralFormVal> values = generalFormValProvider.queryGeneralFormVals(cmd.getSourceType(), cmd.getSourceId());

        if (null != values && values.size() != 0) {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    values.get(0).getFormOriginId(), values.get(0).getFormVersion());
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                    GeneralFormFieldDTO.class);

            if (null == cmd.getOriginFieldFlag()) {
                cmd.setOriginFieldFlag(NormalFlag.NONEED.getCode());
            }

            for (GeneralFormVal val : values) {
                try {
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                    //  若没有找到字段则跳过
                    if (null == dto) {
                        continue;
                    }
                    //  若没有值则不用返回
                    if (StringUtils.isEmpty(val.getFieldValue())) {
                        continue;
                    }
                    PostApprovalFormItem formVal = new PostApprovalFormItem();

                    if (NormalFlag.NEED.getCode() == cmd.getOriginFieldFlag()) {
                        formVal.setFieldName(dto.getFieldName());
                        formVal.setFieldDisplayName(dto.getFieldDisplayName());
                    } else {
                        formVal.setFieldName(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
                    }
                    formVal.setFieldType(val.getFieldType());

                    GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(val.getFieldType());
                    if (null != fieldType) {
                        switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                            case SINGLE_LINE_TEXT:
                            case NUMBER_TEXT:
                            case DATE:
                            case DROP_BOX:
                                results.add(processDropBoxField(formVal, val.getFieldValue(), cmd.getOriginFieldFlag()));
                                break;
                            case MULTI_LINE_TEXT:
                                results.add(processMultiLineTextField(formVal, val.getFieldValue(), cmd.getOriginFieldFlag()));
                                break;
                            case IMAGE:
                                formVal = processImageField(formVal, val.getFieldValue());
                                if (formVal != null)
                                    results.add(formVal);
                                break;
                            case FILE:
                                formVal = processFileField(formVal, val.getFieldValue());
                                if (formVal != null)
                                    results.add(formVal);
                                break;
                            case INTEGER_TEXT:
                                results.add(processIntegerTextField(formVal, val.getFieldValue(), cmd.getOriginFieldFlag()));
                                break;
                            case SUBFORM:
                                results.add(processSubFormField(formVal, dto, val.getFieldValue(), cmd.getOriginFieldFlag()));
                                break;
                        }
                    }
                } catch (NullPointerException e) {
                    LOGGER.error(" ********** null pointer val = " + JSON.toJSONString(val), e);
                } catch (Exception e) {
                    LOGGER.error(" ********** error  = " + JSON.toJSONString(val), e);
                }
            }
        }
        return results;
    }

    /**********     form field process start      **********/

    private PostApprovalFormItem processDropBoxField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag) {
        if (NormalFlag.NEED.getCode() == originFieldFlag) {
            formVal.setFieldValue(jsonVal);
        } else {
            formVal.setFieldValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        }
        return formVal;
    }

    private PostApprovalFormItem processMultiLineTextField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag) {
        if (NormalFlag.NEED.getCode() == originFieldFlag) {
            formVal.setFieldValue(jsonVal);
        } else {
            formVal.setFieldValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        }
        return formVal;
    }

    private PostApprovalFormItem processImageField(PostApprovalFormItem formVal, String jsonVal) {
        PostApprovalFormImageValue imageObj = JSON.parseObject(jsonVal, PostApprovalFormImageValue.class);
        if (null == imageObj || imageObj.getUris() == null)
            return null;
        GeneralFormImageValue imageValue = new GeneralFormImageValue();
        imageValue.setUris(imageObj.getUris());
        List<String> urls = new ArrayList<>();
        for (String uriString : imageObj.getUris()) {
            String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
            urls.add(url);
        }
        imageValue.setUrls(urls);
        formVal.setFieldValue(imageValue.toString());
        return formVal;
    }

    private PostApprovalFormItem processFileField(PostApprovalFormItem formVal, String jsonVal) {
        PostApprovalFormFileValue fileObj = JSON.parseObject(jsonVal, PostApprovalFormFileValue.class);
        if (null == fileObj || fileObj.getFiles() == null)
            return null;
        List<GeneralFormFileValueDTO> files = new ArrayList<>();
        for (PostApprovalFormFileDTO dto2 : fileObj.getFiles()) {
            GeneralFormFileValueDTO fileDTO = new GeneralFormFileValueDTO();
            String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
            ContentServerResource resource = contentServerService.findResourceByUri(dto2.getUri());
            fileDTO.setUri(dto2.getUri());
            fileDTO.setUrl(url);
            fileDTO.setFileName(dto2.getFileName());
            fileDTO.setFileSize(resource.getResourceSize());
            files.add(fileDTO);
        }
        GeneralFormFileValue fileValue = new GeneralFormFileValue();
        fileValue.setFiles(files);
        formVal.setFieldValue(fileValue.toString());
        return formVal;
    }

    private PostApprovalFormItem processIntegerTextField(PostApprovalFormItem formVal, String jsonVal, Byte originFieldFlag) {
        if (NormalFlag.NEED.getCode() == originFieldFlag) {
            formVal.setFieldValue(jsonVal);
        } else {
            formVal.setFieldValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        }
        return formVal;
    }

    private PostApprovalFormItem processSubFormField(PostApprovalFormItem formVal, GeneralFormFieldDTO dto, String jsonVal, Byte originFieldFlag) {
        //  取出子表单字段值
        PostApprovalFormSubformValue postSubFormValue = JSON.parseObject(jsonVal, PostApprovalFormSubformValue.class);
        List<GeneralFormSubFormValueDTO> subForms = new ArrayList<>();
        //  解析子表单的值
        for (PostApprovalFormSubformItemValue itemValue : postSubFormValue.getForms()) {
            subForms.add(processSubFormItemField(dto.getFieldExtra(), itemValue, originFieldFlag));
        }
        GeneralFormSubFormValue subFormValue = new GeneralFormSubFormValue();
        subFormValue.setSubForms(subForms);
        formVal.setFieldValue(subFormValue.toString());
        return formVal;
    }

    private GeneralFormSubFormValueDTO processSubFormItemField(String extraJson, PostApprovalFormSubformItemValue value, Byte originFieldFlag) {
        //  1.取出子表单字段初始内容
        //  2.将子表单中的值解析
        //  3.将得到的Value放入原有的Extra类中，并组装放入fieldValue中
        GeneralFormSubFormValueDTO result = JSON.parseObject(extraJson, GeneralFormSubFormValueDTO.class);
        Map<String, String> fieldMap = new HashMap<>();

        for (PostApprovalFormItem formVal : value.getValues()) {
            switch (GeneralFormFieldType.fromCode(formVal.getFieldType())) {
                case SINGLE_LINE_TEXT:
                case NUMBER_TEXT:
                case DATE:
                case DROP_BOX:
                    processDropBoxField(formVal, formVal.getFieldValue(), originFieldFlag);
                    break;
                case MULTI_LINE_TEXT:
                    processMultiLineTextField(formVal, formVal.getFieldValue(), originFieldFlag);
                    break;
                case IMAGE:
                    processImageField(formVal, formVal.getFieldValue());
                    break;
                case FILE:
                    processFileField(formVal, formVal.getFieldValue());
                    break;
                case INTEGER_TEXT:
                    processIntegerTextField(formVal, formVal.getFieldValue(), originFieldFlag);
                    break;
                case SUBFORM:
                    break;
            }
            fieldMap.put(formVal.getFieldName(), formVal.getFieldValue());
        }
        for (GeneralFormFieldDTO dto : result.getFormFields())
            dto.setFieldValue(fieldMap.get(dto.getFieldName()));
        return result;
    }

    /**********     form field process end      **********/

    @Override
    public List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd) {
        return getGeneralFormFlowEntities(cmd, false);
    }

    @Override
    public List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd, boolean showDefaultFields) {

        List<GeneralFormVal> vals = generalFormValProvider.queryGeneralFormVals(cmd.getSourceType(), cmd.getSourceId());

        List<FlowCaseEntity> entities = new ArrayList<>();

        if (null != vals && vals.size() != 0) {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                    GeneralFormFieldDTO.class);

            processFlowEntities(entities, vals, fieldDTOs, showDefaultFields);
        }
        return entities;
    }

    @Override
    public GeneralFormFieldDTO getGeneralFormValueByOwner(String moduleType, Long moduleId, String ownerType, Long ownerId, String fieldName) {
        GeneralFormFieldDTO dto = null;
        // 审批的值是在一张表
        if (moduleId == 52000L) {
            GeneralApprovalVal approvalVal = generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(ownerId, fieldName);
            if (approvalVal != null) {
                dto = new GeneralFormFieldDTO();
                dto.setFieldType(approvalVal.getFieldType());
                dto.setFieldValue(approvalVal.getFieldStr3());
                dto.setFieldName(approvalVal.getFieldName());
            }
        }
        // 其他表单的值是在另一张表
        else {
            GeneralFormVal formVal = generalFormValProvider.getGeneralFormValBySourceIdAndName(ownerId, ownerType, fieldName);
            if (formVal != null) {
                dto = new GeneralFormFieldDTO();
                dto.setFieldType(formVal.getFieldType());
                dto.setFieldValue(formVal.getFieldValue());
                dto.setFieldName(formVal.getFieldName());
            }
        }
        return dto;
    }

    @Override
    public void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals, List<GeneralFormFieldDTO> fieldDTOs) {
        processFlowEntities(entities, vals, fieldDTOs, false);
    }

    @Override
    public void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals, List<GeneralFormFieldDTO> fieldDTOs, boolean showDefaultFields) {

        List<String> defaultFields = Arrays.stream(GeneralFormDataSourceType.values()).map(GeneralFormDataSourceType::getCode)
                .collect(Collectors.toList());

        for (GeneralFormVal val : vals) {
            try {
                if (showDefaultFields || !defaultFields.contains(val.getFieldName())) {
                    // 不在默认fields的就是自定义字符串，组装这些
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                    if (null == dto || GeneralFormDataVisibleType.fromCode(dto.getVisibleType()) == GeneralFormDataVisibleType.HIDDEN) {
                        continue;
                    }
                    entities.addAll(resolveFormVal(dto, val));
                }
            } catch (NullPointerException e) {
                LOGGER.error(" ********** 空指针错误  val = " + JSON.toJSONString(val), e);
            } catch (Exception e) {
                LOGGER.error(" ********** 这是什么错误  = " + JSON.toJSONString(val), e);

            }
        }
    }

    @Override
    public List<FlowCaseEntity> resolveFormVal(GeneralFormFieldDTO dto, GeneralFormVal val) {

        List<FlowCaseEntity> entities = new ArrayList<>();
        FlowCaseEntity e = new FlowCaseEntity();

        e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());

        switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
            case SINGLE_LINE_TEXT:
            case NUMBER_TEXT:
            case DATE:
            case DROP_BOX:
                generalApprovalFieldProcessor.processDropBoxField(entities, e, val.getFieldValue());
                break;
            case MULTI_LINE_TEXT:
                generalApprovalFieldProcessor.processMultiLineTextField(entities, e, val.getFieldValue());
                break;
            case IMAGE:
                generalApprovalFieldProcessor.processImageField(entities, e, val.getFieldValue());
                break;
            case FILE:
                generalApprovalFieldProcessor.processFileField(entities, e, val.getFieldValue());
                break;
            case INTEGER_TEXT:
                generalApprovalFieldProcessor.processIntegerTextField(entities, e, val.getFieldValue());
                break;
            case SUBFORM:
                generalApprovalFieldProcessor.processSubFormField(entities, dto, val.getFieldValue());
                break;
        }

        return entities;
    }

    private GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    @Override
    public GeneralFormDTO createGeneralForm(CreateApprovalFormCommand cmd) {

        GeneralForm form = ConvertHelper.convert(cmd, GeneralForm.class);
        form.setTemplateType(GeneralFormTemplateType.DEFAULT_JSON.getCode());
        form.setStatus(GeneralFormStatus.CONFIG.getCode());
        form.setNamespaceId(UserContext.getCurrentNamespaceId());
        form.setFormVersion(0L);
        form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));

        this.generalFormProvider.createGeneralForm(form);

        return processGeneralFormDTO(form);
    }

    @Override
    public GeneralFormDTO updateGeneralForm(UpdateApprovalFormCommand cmd) {
        return this.dbProvider.execute((TransactionStatus status) -> {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
                    .getFormOriginId());
            if (null == form || form.getStatus().equals(GeneralFormStatus.INVALID.getCode()))
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                        ErrorCodes.ERROR_INVALID_PARAMETER, "form not found");

            if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
                // 如果是config状态的直接改,
                //这里更新老的form之后才把表单内容，才把内容设置到新表单里面 by dengs issue:12817
                form.setFormName(cmd.getFormName());
                form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
                this.generalFormProvider.updateGeneralForm(form);
            } else if (form.getStatus().equals(GeneralFormStatus.RUNNING.getCode())) {
                // 如果是RUNNING状态的,置原form为失效,重新create一个版本+1的config状态的form
                form.setStatus(GeneralFormStatus.INVALID.getCode());
                form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                //这里更新老的form之后才把表单内容，才把内容设置到新z表单里面 by dengs issue:12817
                this.generalFormProvider.updateGeneralForm(form);
                form.setFormName(cmd.getFormName());
                form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
                form.setFormVersion(form.getFormVersion() + 1);
                form.setStatus(GeneralFormStatus.CONFIG.getCode());
                form.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                form.setUpdateTime(null);
                this.generalFormProvider.createGeneralForm(form);
            }
            return processGeneralFormDTO(form);
        });
    }

    /**
     * 取状态不为失效的form
     */
    @Override
    public ListGeneralFormResponse listGeneralForms(ListGeneralFormsCommand cmd) {

        List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(cmd.getOwnerId()));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(cmd.getOwnerType()));
                        Condition condition = DSL.trueCondition();
                        if (cmd.getModuleId() != null && cmd.getModuleType() != null) {
                            condition = condition.and(Tables.EH_GENERAL_FORMS.MODULE_ID.eq(cmd.getModuleId()));
                            condition = condition.and(Tables.EH_GENERAL_FORMS.MODULE_TYPE.eq(cmd.getModuleType()));
                        }
                        condition = condition.and(Tables.EH_GENERAL_FORMS.STATUS.ne(GeneralFormStatus.INVALID.getCode()));
                        query.addConditions(condition);
                        query.addOrderBy(Tables.EH_GENERAL_FORMS.FORM_ORIGIN_ID.asc());
                        return query;
                    }
                });
        ListGeneralFormResponse resp = new ListGeneralFormResponse();
        resp.setForms(forms.stream().map(this::processGeneralFormDTO).collect(Collectors.toList()));
        return resp;
    }

    //  listGeneralForms 中调用的方法，不知道list为何也需要转换
    private GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    @Override
    public void deleteGeneralFormById(GeneralFormIdCommand cmd) {
        //  删除是状态置为invalid
        this.generalFormProvider.invalidForms(cmd.getFormOriginId());
    }

    @Override
    public GeneralFormDTO getGeneralForm(GeneralFormIdCommand cmd) {
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
                .getFormOriginId());

        GeneralFormDTO result = processGeneralFormDTO(form);
        //	added by LiMingDang for approval1.6
        if (cmd.getModuleType() != null)
            result.setModuleType(cmd.getModuleType());
        return result;
    }

    @Override
    public GeneralFormDTO verifyApprovalFormName(VerifyApprovalFormNameCommand cmd) {
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByName(cmd.getModuleId(),
                cmd.getOwnerId(), cmd.getOwnerType(), cmd.getFormName());
        if (form != null)
            return ConvertHelper.convert(form, GeneralFormDTO.class);
        return null;
    }

    @Override
    public GeneralFormDTO createGeneralFormByTemplate(Long templateId, CreateFormTemplatesCommand cmd) {
        GeneralFormTemplate form = generalFormProvider.findGeneralFormTemplateByIdAndModuleId(
                templateId, cmd.getModuleId());
        GeneralForm gf = generalFormProvider.getGeneralFormByTemplateId(cmd.getModuleId(), cmd.getOwnerId(),
                cmd.getOwnerType(), form.getId());
        if (gf != null) {
            if (GeneralFormStatus.fromCode(gf.getStatus()) == GeneralFormStatus.CONFIG) {
                gf = convertFormFromTemplate(gf, form, cmd);
                generalFormProvider.updateGeneralForm(gf);
            } else {
                //  更新版本时需要保存原版本及id信息
                Long version = gf.getFormVersion();
                Long formOriginId = gf.getFormOriginId();
                gf.setStatus(GeneralFormStatus.INVALID.getCode());
                generalFormProvider.updateGeneralForm(gf);
                gf = ConvertHelper.convert(form, GeneralForm.class);
                gf = convertFormFromTemplate(gf, form, cmd);
                gf.setStatus(GeneralFormStatus.CONFIG.getCode());
                gf.setFormOriginId(formOriginId);
                gf.setFormVersion(version + 1);
                generalFormProvider.createGeneralForm(gf);
            }
        } else {
            gf = ConvertHelper.convert(form, GeneralForm.class);
            gf = convertFormFromTemplate(gf, form, cmd);
            gf.setStatus(GeneralFormStatus.CONFIG.getCode());
            gf.setFormVersion(0L);
            generalFormProvider.createGeneralForm(gf);
        }
        return ConvertHelper.convert(gf, GeneralFormDTO.class);
    }

    private GeneralForm convertFormFromTemplate(GeneralForm gf, GeneralFormTemplate form, CreateFormTemplatesCommand cmd) {
        gf.setFormAttribute(GeneralApprovalAttribute.DEFAULT.getCode());
        gf.setNamespaceId(UserContext.getCurrentNamespaceId());
        gf.setFormTemplateId(form.getId());
        gf.setFormTemplateVersion(form.getVersion());
        gf.setOwnerId(cmd.getOwnerId());
        gf.setOwnerType(cmd.getOwnerType());
        gf.setOrganizationId(cmd.getOrganizationId());
        return gf;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());
        return handler.updateGeneralFormVal(cmd);
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());
        return handler.getGeneralFormReminder(cmd);
    }

    @Override
    public GeneralFormPrintTemplateDTO createGeneralFormPrintTemplate(AddGeneralFormPrintTemplateCommand cmd) {
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
                .getOwnerId());
        if (form == null) {
            LOGGER.error("generalForm is null,formOriginId = {}", cmd.getOwnerId());
            throw RuntimeErrorException.errorWith(GeneralFormPrintTemplateErrorCode.SCOPE, GeneralFormPrintTemplateErrorCode.ERROR_FORM_IS_NOT_EXISTS,
                    "generalForm is null");
        }
        GeneralFormPrintTemplate generalFormPrintTemplate = ConvertHelper.convert(cmd, GeneralFormPrintTemplate.class);
        generalFormPrintTemplate.setName(form.getFormName());
        generalFormPrintTemplate.setOwnerType(FORM_PRINT_TEMPLATE_OWNER_TYPE);
        generalFormPrintTemplate.setOwnerId(form.getId());
        //使用gogs存储合同内容
        //1.建仓库 不同应用建立不同仓库
        try {
            String moduleType = "GeneralFormPrintTemplate_" + generalFormPrintTemplate.getOwnerId();
            GogsRepo repo = gogsRepo(cmd.getNamespaceId(), moduleType, 0L, FORM_PRINT_TEMPLATE_OWNER_TYPE, generalFormPrintTemplate.getOwnerId());
            //2.提交脚本
            GogsCommit commit = gogsCommitScript(repo, generalFormPrintTemplate.gogsPath(), "", cmd.getContents(), true);
            //3.存储提交脚本返回的id
            generalFormPrintTemplate.setLastCommit(commit.getId());

            generalFormProvider.createGeneralFormPrintTemplate(generalFormPrintTemplate);
        } catch (GogsConflictException e) {
            LOGGER.error("generalFormPrintTemplate {} in namespace {} already exist!", generalFormPrintTemplate.gogsPath(), cmd.getNamespaceId());
            throw RuntimeErrorException.errorWith(GeneralFormPrintTemplateErrorCode.SCOPE, GeneralFormPrintTemplateErrorCode.ERROR_FORM_PRINT_TEMPLATE_IS_EXISTS,
                    "generalFormPrintTemplate is already exist");
        } catch (GogsFileNotExistException e) {
            LOGGER.error("generalFormGogsFileNotExist {} in namespace {} not exist!", generalFormPrintTemplate.gogsPath(), cmd.getNamespaceId());
            throw RuntimeErrorException.errorWith(GeneralFormPrintTemplateErrorCode.SCOPE, GeneralFormPrintTemplateErrorCode.ERROR_FORM_PRINT_TEMPLATE_NOT_FOUND,
                    "generalFormGogsFileNotExist not exist");
        } catch (Exception e){
            LOGGER.error("Gogs OthersException .", e);
        }
        return ConvertHelper.convert(generalFormPrintTemplate, GeneralFormPrintTemplateDTO.class);
    }

    @Override
    public GeneralFormPrintTemplateDTO updateGeneralFormPrintTemplate(UpdateGeneralFormPrintTemplateCommand cmd) {
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
                .getOwnerId());
        if (form == null) {
            LOGGER.error("generalForm is null,formOriginId = {}", cmd.getOwnerId());
            throw RuntimeErrorException.errorWith(GeneralFormPrintTemplateErrorCode.SCOPE, GeneralFormPrintTemplateErrorCode.ERROR_FORM_IS_NOT_EXISTS,
                    "generalForm is null");
        }
        GeneralFormPrintTemplate generalFormPrintTemplate = ConvertHelper.convert(cmd, GeneralFormPrintTemplate.class);
        generalFormPrintTemplate.setOwnerType(FORM_PRINT_TEMPLATE_OWNER_TYPE);
        generalFormPrintTemplate.setOwnerId(form.getId());
        generalFormPrintTemplate.setName(form.getFormName());
        boolean isNewFile = false;
        GeneralFormPrintTemplate oldTemplate = generalFormProvider.getGeneralFormPrintTemplateById(cmd.getId());
        if (!oldTemplate.getName().equals(form.getFormName())) {
            isNewFile = true;
        }
        //使用gogs存储合同内容
        //1.建仓库 不同应用建立不同仓库
        try {
            String moduleType = "GeneralFormPrintTemplate_" + generalFormPrintTemplate.getOwnerId();
            //如果表单ID不一致，则新增一条数据，以免旧模板的commit被冲掉
            if (!oldTemplate.getOwnerId().equals(generalFormPrintTemplate.getOwnerId())) {
                GogsRepo repo = gogsRepo(cmd.getNamespaceId(), moduleType, 0L, FORM_PRINT_TEMPLATE_OWNER_TYPE, generalFormPrintTemplate.getOwnerId());
                //2.提交脚本
                GogsCommit commit = gogsCommitScript(repo, generalFormPrintTemplate.gogsPath(), "", cmd.getContents(), true);
                //3.存储提交脚本返回的id
                generalFormPrintTemplate.setLastCommit(commit.getId());
                generalFormProvider.createGeneralFormPrintTemplate(generalFormPrintTemplate);
            }else {
                GogsRepo repo = gogsRepo(cmd.getNamespaceId(), moduleType, 0L, FORM_PRINT_TEMPLATE_OWNER_TYPE, generalFormPrintTemplate.getOwnerId());
                //如果改了名称，在版本仓库里必须删掉原来的，在创建新的
                if (isNewFile) {
                    gogsDeleteScript(repo,oldTemplate.gogsPath(),oldTemplate.getLastCommit());
                }
                //2.提交脚本
                GogsCommit commit = gogsCommitScript(repo, generalFormPrintTemplate.gogsPath(), oldTemplate.getLastCommit(), cmd.getContents(), isNewFile);
                //3.存储提交脚本返回的id
                generalFormPrintTemplate.setLastCommit(commit.getId());
                generalFormProvider.updateGeneralFormPrintTemplate(generalFormPrintTemplate);
            }
        } catch (GogsConflictException e) {
            LOGGER.error("generalFormPrintTemplate {} in namespace {} already exist!", generalFormPrintTemplate.gogsPath(), cmd.getNamespaceId());
            throw RuntimeErrorException.errorWith(GeneralFormPrintTemplateErrorCode.SCOPE, GeneralFormPrintTemplateErrorCode.ERROR_FORM_PRINT_TEMPLATE_IS_EXISTS,
                    "generalFormPrintTemplate is already exist");
        } catch (GogsFileNotExistException e) {
            LOGGER.error("generalFormGogsFileNotExist {} in namespace {} not exist!", generalFormPrintTemplate.gogsPath(), cmd.getNamespaceId());
            throw RuntimeErrorException.errorWith(GeneralFormPrintTemplateErrorCode.SCOPE, GeneralFormPrintTemplateErrorCode.ERROR_FORM_PRINT_TEMPLATE_NOT_FOUND,
                    "generalFormGogsFileNotExist not exist");
        } catch (Exception e){
            LOGGER.error("Gogs OthersException .", e);
        }
        return ConvertHelper.convert(generalFormPrintTemplate, GeneralFormPrintTemplateDTO.class);
    }

    @Override
    public GeneralFormPrintTemplateDTO getGeneralFormPrintTemplate(GetGeneralFormPrintTemplateCommand cmd) {
        GeneralForm generalForm = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd.getFormOriginId());
        if (generalForm == null) {
            LOGGER.error("generalForm is null,formOriginId = {}", cmd.getFormOriginId());
            throw RuntimeErrorException.errorWith(GeneralFormPrintTemplateErrorCode.SCOPE, GeneralFormPrintTemplateErrorCode.ERROR_FORM_IS_NOT_EXISTS,
                    "generalForm is null");
        }
        GeneralFormPrintTemplate generalFormPrintTemplate = this.generalFormProvider.getGeneralFormPrintTemplate(cmd.getNamespaceId(),
                generalForm.getId(),FORM_PRINT_TEMPLATE_OWNER_TYPE);
        if (generalFormPrintTemplate == null) {
            LOGGER.error("generalFormPrintTemplate in namespace {} not exist!", UserContext.getCurrentNamespaceId());
            return null;
        }
        String moduleType = "GeneralFormPrintTemplate_" + generalFormPrintTemplate.getOwnerId();
        GogsRepo repo = gogsRepo(UserContext.getCurrentNamespaceId(), moduleType, 0L, FORM_PRINT_TEMPLATE_OWNER_TYPE, generalFormPrintTemplate.getOwnerId());
        String contents = gogsGet(repo,generalFormPrintTemplate.gogsPath(),generalFormPrintTemplate.getLastCommit());
        GeneralFormPrintTemplateDTO generalFormPrintTemplateDTO = ConvertHelper.convert(generalFormPrintTemplate, GeneralFormPrintTemplateDTO.class);
        generalFormPrintTemplateDTO.setContents(contents);
        UserInfo user  = this.userService.getUserInfo(generalFormPrintTemplate.getCreatorUid());
        generalFormPrintTemplateDTO.setCreatorName(user.getNickName());
        return generalFormPrintTemplateDTO;
    }

    private GogsRepo gogsRepo(Integer namespaceId, String moduleType, Long moduleId, String ownerType, Long ownerId) {
        GogsRepo repo = gogsService.getAnyRepo(namespaceId, moduleType, moduleId, ownerType, ownerId);
        if (repo == null) {
            repo = new GogsRepo();
            repo.setName("generalFormPrintTemplate");
            repo.setNamespaceId(namespaceId);
            repo.setModuleType(moduleType);
            repo.setModuleId(moduleId);
            repo.setOwnerType(ownerType);
            repo.setOwnerId(ownerId);
            repo.setRepoType(GogsRepoType.NORMAL.name());
            repo = gogsService.createRepo(repo);
        }
        return repo;
    }

    private GogsCommit gogsCommitScript(GogsRepo repo, String path, String lastCommit, String content, boolean isNewFile) {
        GogsRawFileParam param = new GogsRawFileParam();
        param.setCommitMessage(gogsCommitMessage());
        param.setNewFile(isNewFile);
        param.setContent(content);
        param.setLastCommit(lastCommit);
        return gogsService.commitFile(repo, path, param);
    }

    private String gogsCommitMessage() {
        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(UserContext.currentUserId());
        return String.format(
                "Author: %s\n UID: %s\n Identifier: %s", userInfo.getNickName(), userInfo.getId(), userInfo.getPhones());
    }

    private GogsCommit gogsDeleteScript(GogsRepo repo, String path, String lastCommit) {
        GogsRawFileParam param = new GogsRawFileParam();
        param.setCommitMessage(gogsCommitMessage());
        param.setLastCommit(lastCommit);
        return gogsService.deleteFile(repo, path, param);
    }

    private String gogsGet(GogsRepo repo, String path, String lastCommit) {
        byte[] file = gogsService.getFile(repo, path, lastCommit);
        return new String(file, Charset.forName("UTF-8"));
    }
}

