// @formatter:off
package com.everhomes.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityRosterSourceFlag;
import com.everhomes.rest.activity.ActivityRosterStatus;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.ActivitySignupCommand;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormRenderType;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP)
public class ActivitySignupFormHandler implements GeneralFormModuleHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitySignupFormHandler.class);
    public static final String GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP = "ActivitySignup";

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityProivider activityProivider;

    @Autowired
    private CommunityProvider communityProvider;
    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
        if (StringUtils.isBlank(cmd.getSourceType())) {
            cmd.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
        }
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
        List<PostApprovalFormItem> values = cmd.getValues();
        Long activityId = 0L;
        for (PostApprovalFormItem item :values) {
            if ("ACTIVITY_ID".equals(item.getFieldName())) {
                item.setFieldValue(item.getFieldValue().replaceAll("\\\\",""));
                PostApprovalFormTextValue textValue = JSONObject.parseObject(item.getFieldValue(), PostApprovalFormTextValue.class);
                try {
                    JSONObject object = JSON.parseObject(textValue.getText());
                    activityId = Long.valueOf(object.get("activityId").toString());
                }catch (Exception exception) {
                    LOGGER.error("parse activityId error,activityText = {}", textValue.getText());
                }
            }
        }
        if (activityId == null || activityId == 0L) {
            LOGGER.error("activityId is invalid");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
                    "activityId is invalid.");
        }
        List<ActivityRoster> rosterList = this.activityProivider.listRosters(activityId, ActivityRosterStatus.NORMAL);
        if (CollectionUtils.isEmpty(rosterList) || rosterList.get(0).getFormId() == null) {
            return dto;
        }
        GeneralForm generalForm = this.generalFormProvider.findGeneralFormById(rosterList.get(0).getFormId());
        ActivitySignupCommand activitySignupCommand = new ActivitySignupCommand();
        activitySignupCommand.setActivityId(activityId);
        activitySignupCommand.setAdultCount(1);
        activitySignupCommand.setSignupSourceFlag(ActivityRosterSourceFlag.SELF.getCode());
        activitySignupCommand.setFormOriginId(generalForm.getFormOriginId());
        activitySignupCommand.setValues(cmd.getValues());
        activitySignupCommand.setFormId(generalForm.getId());
        if (cmd.getCommunityId() != null) {
            Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
            if (community != null) {
                activitySignupCommand.setCommunityName(community.getName());
            }
        }
        LOGGER.info("activity signup, cmd={}",activitySignupCommand);
        ActivityDTO activityDTO = this.activityService.signup(activitySignupCommand);



        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        item.setFieldValue(JSONObject.toJSONString(activityDTO));

        items.add(item);
        dto.setValues(items);
        return dto;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        if (StringUtils.isBlank(cmd.getSourceType())) {
            cmd.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
        }
        GeneralFormDTO dto = new GeneralFormDTO();
        if (cmd.getOwnerId() == null) {
            LOGGER.error("activityId is invalid");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
                    "activityId is invalid.");
        }
        List<ActivityRoster> rosterList = this.activityProivider.listRosters(cmd.getOwnerId(), ActivityRosterStatus.NORMAL);
        if (CollectionUtils.isEmpty(rosterList)) {
            return null;
        }
        Long formId = 0L;
        for (ActivityRoster activityRoster : rosterList) {
            if (activityRoster == null) {
                continue;
            }
            if (activityRoster.getFormId() != null) {
                formId = activityRoster.getFormId();
            }
        }
        if (formId == 0L) {
            return null;
        }
        GeneralForm generalForm = this.generalFormProvider.findGeneralFormById(formId);
        if (generalForm != null) {
            dto = ConvertHelper.convert(generalForm,GeneralFormDTO.class);
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(dto.getTemplateText(), GeneralFormFieldDTO.class);
            GeneralFormFieldDTO generalFormFieldDTO = new GeneralFormFieldDTO();
            generalFormFieldDTO.setFieldName("ACTIVITY_ID");
            generalFormFieldDTO.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
            generalFormFieldDTO.setRequiredFlag(TrueOrFalseFlag.TRUE.getCode());
            generalFormFieldDTO.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
            generalFormFieldDTO.setFieldDisplayName("活动ID");
            JSONObject obj = new JSONObject();
            obj.put("activityId",cmd.getOwnerId().toString());
            generalFormFieldDTO.setFieldValue(obj.toJSONString());
            generalFormFieldDTO.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
            generalFormFieldDTO.setDataSourceType(GeneralFormDataSourceType.ACTIVITY_ID.getCode());
            fieldDTOs.add(generalFormFieldDTO);
            dto.setFormFields(fieldDTOs);
            return dto;
        }
        return null;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }


    GeneralForm getDefaultGeneralForm(String ownerType) {
        List<GeneralForm> forms = getGeneralForum(0, 0L, ownerType);
        if(forms == null || forms.isEmpty()) {
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "Init activity sign up form not found");
        }

        GeneralForm form = forms.get(0);
        if(form == null ) {
            throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                    GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "Init activity sign up form not found");
        }

        return form;
    }

    private List<GeneralForm> getGeneralForum(Integer namespaceId, Long ownerId, String ownerType) {
        List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_GENERAL_FORMS.NAMESPACE_ID.eq(namespaceId));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(ownerId));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(ownerType));
                        query.addConditions(Tables.EH_GENERAL_FORMS.STATUS.ne(GeneralFormStatus.INVALID.getCode()));
                        query.addOrderBy(Tables.EH_GENERAL_FORMS.ID.desc());
                        return query;
                    }
                });
        return forms;
    }
}
