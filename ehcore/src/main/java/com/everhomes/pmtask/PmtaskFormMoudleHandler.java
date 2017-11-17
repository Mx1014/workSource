package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.general_approval.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */
@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhPmTasks")
public class PmtaskFormMoudleHandler implements GeneralFormModuleHandler {

    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private PmTaskProvider pmTaskProvider;
    @Autowired
    private GeneralFormService generalFormService;

    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {
        if (cmd.getOwnerType()==null)
            cmd.setOwnerType("PMTASK");
        GetTemplateBySourceIdCommand cmd2 = ConvertHelper.convert(cmd,GetTemplateBySourceIdCommand.class);
        GeneralForm form = getGeneralForm(cmd2);
        PmTask pmTask = pmTaskProvider.findTaskById(cmd.getSourceId());
        if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
            // 使用表单/审批 注意状态 config
            form.setStatus(GeneralFormStatus.RUNNING.getCode());
            this.generalFormProvider.updateGeneralForm(form);
        }
        addGeneralFormValuesCommand cmd3 = new addGeneralFormValuesCommand();
        cmd3.setGeneralFormId(form.getFormOriginId());
        cmd3.setSourceId(cmd.getSourceId());
        cmd3.setSourceType(cmd.getSourceType());
        cmd3.setValues(cmd.getValues());

        generalFormService.addGeneralFormValues(cmd3);
        return null;
    }

    //目前所有报修使用同一个费用清单
    private GeneralForm getGeneralForm(GetTemplateBySourceIdCommand cmd){
        List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        if (cmd.getOwnerId()!=null)
                            query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(cmd.getOwnerId()));
                        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(cmd.getOwnerType()));

                        query.addConditions(Tables.EH_GENERAL_FORMS.STATUS
                                .ne(GeneralFormStatus.INVALID.getCode()));
                        return query;
                    }
                });
        if (forms==null || forms.size()==0)
            return null;
        else
            return forms.get(0);
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {

        if (cmd.getOwnerType()==null)
            cmd.setOwnerType("PMTASK");

        GeneralForm form = getGeneralForm(cmd);
        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }
}
