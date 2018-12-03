package com.everhomes.general_form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.general_approval.*;
import com.everhomes.gogs.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.activity.ruian.ActivityCategoryList;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormFilterUserMap;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.ValidatorUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

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
    private GeneralFormKvConfigProvider generalFormKvConfigProvider;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;

    @Autowired
    private GeneralFormSearcher generalFormSearcher;

    @Autowired
    private GogsService gogsService;

    @Autowired
    private UserService userService;

    @Autowired
    private GeneralFormFieldsConfigProvider generalFormFieldsConfigProvider;

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
            GeneralForm form;
            if(cmd.getGeneralFormVersion() != null){
                form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(cmd.getGeneralFormId(), cmd.getGeneralFormVersion());
            }else{
                form = generalFormProvider.getActiveGeneralFormByOriginId(cmd.getGeneralFormId());
            }

            dbProvider.execute(status -> {
                if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
                    // 使用表单/审批 注意状态 config
                    form.setStatus(GeneralFormStatus.RUNNING.getCode());
                    generalFormProvider.updateGeneralForm(form);
                }

                List<GeneralFormVal> objs = new ArrayList<>();
                for (PostApprovalFormItem val : cmd.getValues()) {
                    if(StringUtils.isNotBlank(val.getFieldName()) && StringUtils.isNotBlank(val.getFieldType()) && StringUtils.isNotBlank(val.getFieldValue())) {
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
                        objs.add(obj);
                    }

                }
                generalFormSearcher.feedDoc(objs);
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
    public GeneralFormFieldDTO getGeneralFormValueByOwner(Long formOriginId, Long formVersion, String moduleType,
                                                          Long moduleId, String ownerType, Long ownerId, String fieldName) {
        GeneralFormFieldDTO dto = null;
        // 审批的值是在一张表
        if (moduleId == 52000L) {
            GeneralApprovalVal approvalVal = generalApprovalValProvider.getGeneralApprovalVal(ownerId,
                    formOriginId, 0L/*这张表的version好像都是0*/, fieldName);
            if (approvalVal != null) {
                dto = new GeneralFormFieldDTO();
                dto.setFieldType(approvalVal.getFieldType());
                dto.setFieldValue(approvalVal.getFieldStr3());
                dto.setFieldName(approvalVal.getFieldName());
            }
        }
        // 其他表单的值是在另一张表
        else {
            GeneralFormVal formVal = generalFormValProvider.getGeneralFormVal(formOriginId,
                    null/*这个version不清楚怎么用的, 先不加这个条件*/, ownerId, ownerType, fieldName);
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
        //处理一下入参
        List<GeneralFormFieldDTO> dtos = processGeneralFormField(cmd.getFormFields());
        cmd.setFormFields(dtos);

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
            //处理一下入参
            List<GeneralFormFieldDTO> dtos = processGeneralFormField(cmd.getFormFields());
            cmd.setFormFields(dtos);

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

        //CrossShardListingLocator locator = new CrossShardListingLocator();
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
                        if (cmd.getProjectId() != null && cmd.getProjectType() != null) {
                            condition = condition.and(Tables.EH_GENERAL_FORMS.PROJECT_ID.eq(cmd.getProjectId()));
                            condition = condition.and(Tables.EH_GENERAL_FORMS.PROJECT_TYPE.eq(cmd.getProjectType()));
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
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByName(
                cmd.getProjectType(), cmd.getProjectId(), cmd.getModuleId(),
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
    public SearchFormValDTO searchGeneralFormVals(SearchFormValsCommand cmd) {
        return null;
    }

    @Override
    public List<GeneralFormFieldDTO> getDefaultFieldsByModuleId(ListDefaultFieldsCommand cmd) {

        GeneralFormTemplate request;

        if(cmd.getModuleId() != null && cmd.getNamespaceId() != null) {
            request = generalFormProvider.getDefaultFieldsByModuleId(cmd.getModuleId(), cmd.getNamespaceId());
        }else{
            LOGGER.error("getDefaultFieldsByModuleId false: All param cannot be null. namespaceId: " + cmd.getNamespaceId() + ", moduleId: " + cmd.getModuleId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"All param cannot be null.");
        }

        return JSONObject.parseArray(request.getTemplateText(), GeneralFormFieldDTO.class);
    }

    @Override
    public Long deleteGeneralFormVal(PostGeneralFormValCommand cmd){
        if(cmd.getSourceId() != null && cmd.getModuleId() != null){
            generalFormProvider.deleteGeneralFormVal(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getSourceId());
            //generalFormProvider.updateGeneralFormValRequestStatus(cmd.getRequisitionId(), (byte)0);
            generalFormSearcher.deleteById(cmd.getSourceId());
            return cmd.getSourceId();
        }else{
            LOGGER.error("deleteGeneralFormVal false: param cannot be null. namespaceId: " + cmd.getNamespaceId() + ", currentOrganizationId: "
                    + cmd.getCurrentOrganizationId() + ", ownerId: " + cmd.getOwnerId() + ", sourceId: " + cmd.getSourceId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId,currentOrganizationId,ownerId,sourceId cannot be null.");
        }

    }

    @Override
    public Long deleteGeneralFormValWithPrivi(PostGeneralFormValCommand cmd){
        GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());
        return handler.deleteGeneralFormVal(cmd);
    }


    @Override
    public Long deleteGeneralForm(PostGeneralFormValCommand cmd){
        if(cmd.getSourceId() != null && cmd.getNamespaceId() !=null && cmd.getCurrentOrganizationId() != null && cmd.getOwnerId() != null ){
            //generalFormProvider.deleteGeneralFormVal(cmd.getOwnerType(), cmd.getSourceType(), cmd.getNamespaceId(), cmd.getCurrentOrganizationId(), cmd.getOwnerId(), cmd.getSourceId());
            generalFormProvider.updateGeneralFormValRequestStatus(cmd.getSourceId(), GeneralFormValRequestStatus.DELETE.getCode());
            generalFormSearcher.deleteById(cmd.getSourceId());
            return cmd.getSourceId();
        }else{
            LOGGER.error("deleteGeneralFormVal false: param cannot be null. namespaceId: " + cmd.getNamespaceId() + ", currentOrganizationId: "
                    + cmd.getCurrentOrganizationId() + ", ownerId: " + cmd.getOwnerId() + ", sourceId: " + cmd.getSourceId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId,currentOrganizationId,ownerId,sourceId cannot be null.");
        }

    }

    @Override
    public List<GeneralFormValDTO> getGeneralFormVal(GetGeneralFormValCommand cmd){
        List<GeneralFormVal> request;

        if(cmd.getSourceId() != null && cmd.getNamespaceId() !=null && cmd.getOwnerId() != null){


            request = generalFormProvider.getGeneralFormVal(cmd.getNamespaceId(), cmd.getSourceId(), cmd.getModuleId(), cmd.getOwnerId());
            return request.stream().map(r -> ConvertHelper.convert(r, GeneralFormValDTO.class)).collect(Collectors.toList());
        }else{
            LOGGER.error("getGeneralFormVal false: param cannot be null. namespaceId: " + cmd.getNamespaceId() + ", ownerType: "
                    + cmd.getOwnerType() + ", sourceType: " + cmd.getSourceType() + ", ownerId: " + cmd.getOwnerId() + ", sourceId: " + cmd.getSourceId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId,ownerType,sourceType,ownerId,sourceId cannot be null.");
        }
    }


    @Override
    public Long saveGeneralFormVal(PostGeneralFormValCommand cmd){
        GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());
        return handler.saveGeneralFormVal(cmd);
    }

    @Override
    public List<GeneralFormValDTO> getGeneralFormValWithPrivi(GetGeneralFormValCommand cmd){
        GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());
        return handler.getGeneralFormVal(cmd);
    }

    @Override
    public Long saveGeneralForm(PostGeneralFormValCommand cmd) {

        //先新建表单字段的集合
        return this.dbProvider.execute((status) -> {
            Long source_id;
            GeneralForm generalForm = null;
            GeneralApproval generalApproval =generalApprovalProvider.getGeneralApprovalByNameAndRunning(cmd.getNamespaceId(), cmd.getSourceId(), cmd.getOwnerId(), cmd.getOwnerType());
            if(generalApproval != null) {
                generalForm = generalFormProvider.getActiveGeneralFormByOriginId(generalApproval.getFormOriginId());
            }
            if(cmd.getSourceId() != null && cmd.getNamespaceId() !=null && cmd.getOwnerId() != null && generalForm != null) {
                source_id = generalFormProvider.saveGeneralFormValRequest(cmd.getNamespaceId(), cmd.getSourceType(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getSourceId(), cmd.getInvestmentAdId(),generalForm.getFormOriginId(), generalForm.getFormVersion());
            }else{
                LOGGER.error("getGeneralFormVal false: param cannot be null. namespaceId: " + cmd.getNamespaceId() + ", ownerType: "
                        + cmd.getOwnerType() + ", sourceType: " + cmd.getSourceType() + ", ownerId: " + cmd.getOwnerId() + ", sourceId: " + cmd.getSourceId());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "namespaceId,ownerType,sourceType,ownerId,sourceId cannot be null.");
            }
            String source_type = "EhGeneralFormValRequests";

            addGeneralFormValuesCommand cmd2 = new addGeneralFormValuesCommand();
            cmd2.setGeneralFormVersion(generalForm.getFormVersion());
            cmd2.setGeneralFormId(generalForm.getFormOriginId());
            cmd2.setSourceId(source_id);
            cmd2.setSourceType(source_type);
            cmd2.setValues(cmd.getValues());
            this.addGeneralFormValues(cmd2);
            return source_id;
        });
    }




    @Override
    public List<String> listGeneralFormFilter(GetGeneralFormFilterCommand cmd){
        List<GeneralFormFilterUserMap> generalFormFilterUserMaps = generalFormProvider.listGeneralFormFilter(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(),
                UserContext.current().getUser().getUuid(), cmd.getFormOriginId(), cmd.getFormVersion());


        List<String> fieldNames = generalFormFilterUserMaps.stream().map(EhGeneralFormFilterUserMap::getFieldName).collect(Collectors.toList());
        if(fieldNames.size() > 0){
            return fieldNames;
        }else{
            LOGGER.error("this form not filter");
            GeneralFormTemplate request = generalFormProvider.getDefaultFieldsByModuleId(cmd.getModuleId(), cmd.getNamespaceId());
            if(request != null){
                Gson gson = new Gson();
                List<GeneralFormFieldDTO> formFields = gson.fromJson(request.getTemplateText(), new TypeToken<List<GeneralFormFieldDTO>>(){}.getType());
                GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(cmd.getFormOriginId(), cmd.getFormVersion());
                List<GeneralFormFieldDTO> allFormFields = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
                for(GeneralFormFieldDTO dto : allFormFields){
                    if(dto.getFilterFlag() != null && dto.getFilterFlag() == (byte)1){
                        formFields.add(dto);
                    }
                }
                return formFields.stream().map(GeneralFormFieldDTO::getFieldName).collect(Collectors.toList());
            }else{
                LOGGER.error("can not find running approval." + cmd.getNamespaceId() + cmd.getModuleId());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
                        "listGeneralFormFilter false: can not find running approval.");
            }
        }
    }

    @Override
    public List<String> saveGeneralFormFilter(PostGeneralFormFilterCommand cmd){
        String UserUuid = UserContext.current().getUser().getUuid();
        if(cmd.getNamespaceId() != null && cmd.getFormFields().size() > 0 && cmd.getFormOriginId() != null && cmd.getFormVersion() != null
                && cmd.getModuleId() != null && cmd.getOwnerId() != null) {
            dbProvider.execute(status -> {
                generalFormProvider.deleteGeneralFormFilter(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getMuduleType(), cmd.getOwnerId(), cmd.getOwnerType(),
                                UserUuid, cmd.getFormOriginId(), cmd.getFormVersion());

                for(GeneralFormFieldDTO dto: cmd.getFormFields()){
                    if(StringUtils.isNotBlank(dto.getFieldName())) {
                        if(!dto.getFieldType().equals(GeneralFormFieldType.FILE.getCode()) && !dto.getFieldType().equals(GeneralFormFieldType.IMAGE.getCode()) && !dto.getFieldType().equals(GeneralFormFieldType.SUBFORM.getCode())){
                            generalFormProvider.saveGeneralFormFilter(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getMuduleType(), cmd.getOwnerId(), cmd.getOwnerType(),
                                    UserUuid, cmd.getFormOriginId(), cmd.getFormVersion(), dto.getFieldName());
                        }else{
                            LOGGER.error("getGeneralFormVal false: can not list this field type. the fieldType is :" + dto.getFieldType());
                            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                                    "getGeneralFormVal false: can not list this field type. the fieldType is :" + dto.getFieldType());
                        }

                    }else{
                        LOGGER.error("getGeneralFormVal false: param cannot be null. ");
                    }
                }
                return null;
            });
            return cmd.getFormFields().stream().map(GeneralFormFieldDTO::getFieldName).collect(Collectors.toList());

        }else{
            LOGGER.error("getGeneralFormVal false: param cannot be null. namespaceId: " + cmd.getNamespaceId() + ", ownerId: " + cmd.getOwnerId() + ", FormFields size: " + cmd.getFormFields().size() +
                    ", moduleId: " + cmd.getModuleId() + ", formOriginId: " + cmd.getFormOriginId() + ", formVersion: " + cmd.getFormVersion());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId,ownerType,sourceType,ownerId,sourceId cannot be null.");
        }

    }


    @Override
    public void enableProjectCustomize(EnableProjectCustomizeCommand cmd) {
        ValidatorUtil.validate(cmd);

        GeneralFormKvConfig config = generalFormKvConfigProvider.findByKey(
                cmd.getNamespaceId(), cmd.getModuleType(), cmd.getModuleId(), cmd.getProjectType(), cmd.getProjectId(),
                cmd.getOwnerType(), cmd.getOwnerId(), GeneralFormConstants.KV_CONFIG_PROJECT_CUSTOMIZE);
        if (config != null) {
            config.setValue(TrueOrFalseFlag.TRUE.getCode().toString());
            generalFormKvConfigProvider.updateGeneralFormKvConfig(config);
        } else {
            config = ConvertHelper.convert(cmd, GeneralFormKvConfig.class);
            if (config.getModuleType() == null) {
                config.setModuleType("any-module");
            }
            config.setValue(TrueOrFalseFlag.TRUE.getCode().toString());
            config.setKey(GeneralFormConstants.KV_CONFIG_PROJECT_CUSTOMIZE);
            generalFormKvConfigProvider.createGeneralFormKvConfig(config);
        }
    }

    @Override
    public GeneralForm mirrorGeneralForm(Long formId, String mirrorModuleType, Long mirrorModuleId,
                                         String mirrorProjectType, Long mirrorProjectId, String mirrorOwnerType, Long mirrorOwnerId) {
        GeneralForm generalForm = generalFormProvider.getGeneralFormById(formId);

        if (mirrorModuleType != null) {
            generalForm.setModuleType(mirrorModuleType);
        }
        if (mirrorModuleId != null) {
            generalForm.setModuleId(mirrorModuleId);
        }
        if (mirrorProjectType != null) {
            generalForm.setProjectType(mirrorProjectType);
        }
        if (mirrorProjectId != null) {
            generalForm.setProjectId(mirrorProjectId);
        }
        if (mirrorOwnerType != null) {
            generalForm.setOwnerType(mirrorOwnerType);
        }
        if (mirrorOwnerId != null) {
            generalForm.setOwnerId(mirrorOwnerId);
        }
        generalForm.setFormOriginId(null);
        generalFormProvider.createGeneralForm(generalForm);
        return generalForm;
    }

    @Override
    public void disableProjectCustomize(DisableProjectCustomizeCommand cmd) {
        ValidatorUtil.validate(cmd);

        GeneralFormKvConfig config = generalFormKvConfigProvider.findByKey(
                cmd.getNamespaceId(), cmd.getModuleType(), cmd.getModuleId(), cmd.getProjectType(), cmd.getProjectId(),
                cmd.getOwnerType(), cmd.getOwnerId(), GeneralFormConstants.KV_CONFIG_PROJECT_CUSTOMIZE);
        if (config != null) {
            config.setValue(TrueOrFalseFlag.FALSE.getCode().toString());
            generalFormKvConfigProvider.updateGeneralFormKvConfig(config);
        }

        List<GeneralForm> list = generalFormProvider.listGeneralForm(
                cmd.getNamespaceId(), cmd.getModuleType(), cmd.getModuleId(), cmd.getProjectType(), cmd.getProjectId(),
                cmd.getOwnerType(), cmd.getOwnerId());
        for (GeneralForm form : list) {
            form.setStatus(GeneralFormStatus.INVALID.getCode());
            generalFormProvider.updateGeneralForm(form);
        }
    }

    @Override
    public Byte getProjectCustomize(GetProjectCustomizeCommand cmd) {
        ValidatorUtil.validate(cmd);

        GeneralFormKvConfig config = generalFormKvConfigProvider.findByKey(
                cmd.getNamespaceId(), cmd.getModuleType(), cmd.getModuleId(), cmd.getProjectType(), cmd.getProjectId(),
                cmd.getOwnerType(), cmd.getOwnerId(), GeneralFormConstants.KV_CONFIG_PROJECT_CUSTOMIZE);
        if (config != null) {
            return Byte.valueOf(config.getValue());
        }
        return TrueOrFalseFlag.FALSE.getCode();
    }

    @Override
    public void doFormMirror(DoFormMirrorCommand cmd) {
        ValidatorUtil.validate(cmd);

        for (Long id : cmd.getFormIds()) {
            mirrorGeneralForm(id, cmd.getModuleType(), cmd.getModuleId(),
                    cmd.getProjectType(), cmd.getProjectId(), cmd.getOwnerType(), cmd.getOwnerId());
        }
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
        } catch (GogsNotExistException e) {
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
        } catch (GogsNotExistException e) {
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
        if (generalFormPrintTemplate.getCreatorUid() != null) {
            UserInfo user  = this.userService.getUserInfo(generalFormPrintTemplate.getCreatorUid());
            generalFormPrintTemplateDTO.setCreatorName(user.getNickName());
        }
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

    //对fieldType　为＂MULTI_SELECT＂的GeneralFormFieldDTO进行处理
    private List<GeneralFormFieldDTO>  processGeneralFormField(List<GeneralFormFieldDTO> dtos){
        if(CollectionUtils.isEmpty(dtos)){
            return dtos ;
        }
        for(GeneralFormFieldDTO dto : dtos){
            String type = dto.getFieldType();
            if(GeneralFormFieldType.MULTI_SELECT.getCode().equals(type)){
                String selectValue = dto.getFieldExtra();
                selectValue = multiSelectTransferPingyin(selectValue);
                dto.setFieldExtra(selectValue);
            }
        }
        return dtos ;
    }

    /**
     * 初始化出拼音
     * @param selectValue
     * @return
     */
    private String multiSelectTransferPingyin(String selectValue){
        if(StringUtils.isBlank(selectValue)){
            LOGGER.info("multiSelectTransferPingyin : selectValue is blank !");
            return selectValue ;
        }

        GeneralFormMultiSelectDTO dto = (GeneralFormMultiSelectDTO)StringHelper.fromJsonString(selectValue,GeneralFormMultiSelectDTO.class);
        if(CollectionUtils.isEmpty(dto.getSelectValue())){
            LOGGER.info("multiSelectTransferPingyin : selectValue is empty !");
            return selectValue ;
        }
        List<String> pinyins = new ArrayList<String>();
        for(String str : dto.getSelectValue()){
            String pinyin = "";
            try{ //转换拼音出错不应该影响该功能的进行
                 pinyin = PinYinHelper.getPinYin(str);
            }catch(Exception e ){
                LOGGER.info("multiSelectTransferPingyin  : str:{} can not transfer pinyin !",str);
            }

            pinyins.add(pinyin);
        }

        dto.setPingYin(pinyins);
        selectValue = StringHelper.toJsonString(dto);

        return selectValue ;
    }

    /**
     * 新建表单字段配置信息
     */
    @Override
    public GeneralFormFieldsConfigDTO createFormFieldsConfig(CreateFormFieldsConfigCommand cmd){
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(cmd.getFormOriginId(),cmd.getFormVersion());
        if(form == null){
            LOGGER.error("The form using to create formFieldsConfig is null, formOriginId = {}, formVersion = {}",
                    cmd.getFormOriginId(), cmd.getFormVersion());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "The form using to create formFieldsConfig is null is null");
        }
        GeneralFormFieldsConfig formFieldsConfig = ConvertHelper.convert(cmd, GeneralFormFieldsConfig.class);
        formFieldsConfig.setNamespaceId(UserContext.getCurrentNamespaceId());
        formFieldsConfig.setOrganizationId(form.getOrganizationId());
        formFieldsConfig.setOwnerId(form.getOwnerId());
        formFieldsConfig.setOwnerType(form.getOwnerType());
        formFieldsConfig.setModuleId(form.getModuleId());
        formFieldsConfig.setModuleType(form.getModuleType());
        formFieldsConfig.setProjectId(cmd.getProjectId() == null ? form.getProjectId() : cmd.getProjectId());
        formFieldsConfig.setProjectType(cmd.getProjectType() == null ? form.getProjectType() : cmd.getProjectType());
        formFieldsConfig.setConfigVersion(0L);
        if(formFieldsConfig.getConfigType() == null || "".equals(formFieldsConfig.getConfigType())){
            formFieldsConfig.setConfigType(GeneralFormConstants.FORM_FIELDS_CONFIG_TYPE);
        }
        formFieldsConfig.setFormFields(JSON.toJSONString(cmd.getFormFields()));
        formFieldsConfig.setStatus(GeneralFormFieldsConfigStatus.CONFIG.getCode());
        GeneralFormFieldsConfig result = generalFormFieldsConfigProvider.createFormFieldsConfig(formFieldsConfig);

        GeneralFormFieldsConfigDTO dto = ConvertHelper.convert(result, GeneralFormFieldsConfigDTO.class);
        dto.setFormFields(cmd.getFormFields());
        return dto;
    }

    /**
     * 修改表单字段配置信息
     */
    @Override
    public GeneralFormFieldsConfigDTO updateFormFieldsConfig(UpdateFormFieldsConfigCommand cmd){
        GeneralFormFieldsConfig formFieldsConfig = generalFormFieldsConfigProvider.getActiveFormFieldsConfig(cmd.getFormFieldsConfigId());
        if(formFieldsConfig == null){
            LOGGER.error("The formFieldsConfig updating is null, formFieldsConfigId = {}", cmd.getFormFieldsConfigId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "The formFieldsConfig updating is null");
        }
        GeneralFormFieldsConfig result = new GeneralFormFieldsConfig();
        if(formFieldsConfig.getStatus().equals(GeneralFormFieldsConfigStatus.CONFIG.getCode())){
            // 如果配置是config状态，直接修改配置内容
            formFieldsConfig.setFormOriginId(cmd.getFormOriginId());
            formFieldsConfig.setFormVersion(cmd.getFormVersion());
            if(cmd.getConfigType() != null && !"".equals(cmd.getConfigType())){
                formFieldsConfig.setConfigType(cmd.getConfigType());
            }
            formFieldsConfig.setFormFields(JSON.toJSONString(cmd.getFormFields()));
            result = generalFormFieldsConfigProvider.updateFormFieldsConfig(formFieldsConfig);
        } else if(formFieldsConfig.getStatus().equals(GeneralFormFieldsConfigStatus.RUNNING.getCode())){
            // 如果配置是running状态，新建一个版本+1的config状态的配置
            formFieldsConfig.setFormOriginId(cmd.getFormOriginId());
            formFieldsConfig.setFormVersion(cmd.getFormVersion());
            formFieldsConfig.setConfigVersion(formFieldsConfig.getFormVersion() + 1);
            if(cmd.getConfigType() != null && !"".equals(cmd.getConfigType())){
                formFieldsConfig.setConfigType(cmd.getConfigType());
            }
            formFieldsConfig.setFormFields(JSON.toJSONString(cmd.getFormFields()));
            formFieldsConfig.setStatus(GeneralFormFieldsConfigStatus.CONFIG.getCode());
            formFieldsConfig.setUpdaterUid(null);
            formFieldsConfig.setUpdateTime(null);
            result = generalFormFieldsConfigProvider.createFormFieldsConfig(formFieldsConfig);
        }
        GeneralFormFieldsConfigDTO dto = ConvertHelper.convert(result, GeneralFormFieldsConfigDTO.class);
        dto.setFormFields(cmd.getFormFields());
        return dto;
    }

    /**
     * 将配置状态修改成RUNNING状态，提供给工作流发布新版本时使用
     * @param formFieldsConfigId 表单字段配置ID
     * @return 表单字段配置DTO
     */
    @Override
    public GeneralFormFieldsConfigDTO updateFormFieldsConfigStatus(Long formFieldsConfigId){
        GeneralFormFieldsConfig formFieldsConfig = generalFormFieldsConfigProvider.getActiveFormFieldsConfig(formFieldsConfigId);
        if(formFieldsConfig == null){
            LOGGER.error("The formFieldsConfig updating is null, formFieldsConfigId = {}", formFieldsConfigId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "The formFieldsConfig updating is null");
        }
        if(!formFieldsConfig.getStatus().equals(GeneralFormFieldsConfigStatus.CONFIG.getCode())){
            LOGGER.error("The formFieldsConfig updating is not config status, formFieldsConfigId = {}", formFieldsConfigId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "The formFieldsConfig updating is not config status");
        }
        formFieldsConfig.setStatus(GeneralFormFieldsConfigStatus.RUNNING.getCode());
        GeneralFormFieldsConfig result = generalFormFieldsConfigProvider.updateFormFieldsConfig(formFieldsConfig);
        GeneralFormFieldsConfigDTO dto = ConvertHelper.convert(result, GeneralFormFieldsConfigDTO.class);
        List<GeneralFormFieldsConfigFieldDTO> fieldDTOs = JSONObject.parseArray(result.getFormFields(), GeneralFormFieldsConfigFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    /**
     * 逻辑删除表单字段配置，状态修改为INVALID
     */
    @Override
    public void deleteFormFieldsConfig(DeleteFormFieldsConfigCommand cmd){
        GeneralFormFieldsConfig formFieldsConfig = generalFormFieldsConfigProvider.getActiveFormFieldsConfig(cmd.getFormFieldsConfigId());
        if(formFieldsConfig == null){
            LOGGER.error("The formFieldsConfig deleting is null, formFieldsConfigId = {}", cmd.getFormFieldsConfigId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "The formFieldsConfig deleting is null");
        }
        formFieldsConfig.setStatus(GeneralFormFieldsConfigStatus.INVALID.getCode());
        generalFormFieldsConfigProvider.updateFormFieldsConfig(formFieldsConfig);
    }

    /**
     * 根据 配置ID 查询表单字段配置信息，未判断配置的状态
     */
    @Override
    public GeneralFormFieldsConfigDTO getFormFieldsConfig(GetFormFieldsConfigCommand cmd){
        GeneralFormFieldsConfig formFieldsConfig = generalFormFieldsConfigProvider.getFormFieldsConfig(cmd.getFormFieldsConfigId());
        if(formFieldsConfig == null){
            LOGGER.error("The formFieldsConfig gotten is null, formFieldsConfigId = {}", cmd.getFormFieldsConfigId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "The formFieldsConfig gotten is null");
        }
        GeneralFormFieldsConfigDTO dto = ConvertHelper.convert(formFieldsConfig, GeneralFormFieldsConfigDTO.class);
        // 将formFieldsConfig里的formFields从json形式解析成List
        List<GeneralFormFieldsConfigFieldDTO> fieldDTOs = JSONObject.parseArray(formFieldsConfig.getFormFields(), GeneralFormFieldsConfigFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    /**
     * 根据 表单原始ID和版本 查询表单信息
     */
    @Override
    public GeneralFormDTO getGeneralFormByOriginIdAndVersion(GetGeneralFormByOriginIdAndVersionCommand cmd){
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(cmd.getFormOriginId(), cmd.getFormVersion());
        if (form == null) {
            LOGGER.error("The form is null, formOriginId = {}, formVersion = {}", cmd.getFormOriginId(), cmd.getFormVersion());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "The form is null");
        }
        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        // 将form里的templateText从json形式解析为List
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }
}

