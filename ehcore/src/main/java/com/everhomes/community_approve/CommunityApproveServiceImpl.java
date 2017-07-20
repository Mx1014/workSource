package com.everhomes.community_approve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.community_approve.CommunityApprove;
import com.everhomes.community_approve.CommunityApproveProvider;
import com.everhomes.community_approve.CommunityApproveService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.community_approve.*;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/7/18.
 */
@Component
public class CommunityApproveServiceImpl implements CommunityApproveService {

    @Autowired
    private CommunityApproveProvider communityApproveProvider;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private CommunityApproveValProvider communityApproveValProvider;

    @Autowired
    private GeneralFormService generalFormService;

    @Override
    public CommunityApproveDTO updateCommunityApprove(UpdateCommunityApproveCommand cmd) {
        CommunityApprove ca = this.communityApproveProvider.getCommunityApproveById(cmd.getId());

        if (null!=cmd.getApproveName())
            ca.setApproveName(cmd.getApproveName());
        if (null!=cmd.getFormOriginId())
            ca.setFormOriginId(cmd.getFormOriginId());
        this.communityApproveProvider.updateCommunityApprove(ca);
        return this.processApprove(ca);
    }

    private CommunityApproveDTO processApprove(CommunityApprove r){
        CommunityApproveDTO result = ConvertHelper.convert(r,CommunityApproveDTO.class);
        //form name
        if (r.getFormOriginId() != null && !r.getFormOriginId().equals(0l)) {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(r
                    .getFormOriginId());
            if (form != null) {
                result.setFormName(form.getFormName());
            }
        }

        // flow
        Flow flow = flowService.getEnabledFlow(r.getNamespaceId(), r.getModuleId(),
                r.getModuleType(), r.getId(), FlowOwnerType.COMMUNITY_APPROVE.getCode());

        if (null != flow) {
            result.setFlowName(flow.getFlowName());
        }
        return result;
    }

    @Override
    public ListCommunityApproveResponse listCommunityApproves(ListCommunityApproveCommand cmd) {
        List<CommunityApprove> gas = this.communityApproveProvider.queryCommunityApproves(new ListingLocator(),
                Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE.OWNER_ID.eq(cmd
                                .getOwnerId()));
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE.OWNER_TYPE.eq(cmd
                                .getOwnerType()));
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE.MODULE_ID.eq(cmd
                                .getModuleId()));
                        query.addConditions(Tables.EH_COMMUNITY_APPROVE.MODULE_TYPE.eq(cmd
                                .getModuleType()));
                        return query;
                    }
                });
        ListCommunityApproveResponse resp = new ListCommunityApproveResponse();
        resp.setApproves(gas.stream().map((r)->{
            return processApprove(r);
        }).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public void enableCommunityApprove(CommunityApproveIdCommand cmd) {
        CommunityApprove ca = this.communityApproveProvider.getCommunityApproveById(cmd.getId());
        ca.setStatus(CommunityApproveStatus.RUNNING.getCode());
        this.communityApproveProvider.updateCommunityApprove(ca);
    }

    @Override
    public void disableCommunityApprove(CommunityApproveIdCommand cmd) {
        CommunityApprove ca = this.communityApproveProvider.getCommunityApproveById(cmd.getId());
        ca.setStatus(CommunityApproveStatus.INVALID.getCode());
        this.communityApproveProvider.updateCommunityApprove(ca);
    }

    @Override
    public GetTemplateByCommunityApproveIdResponse postApprovalForm(PostCommunityApproveFormCommand cmd) {

        CommunityApprove ca =  this.communityApproveProvider.getCommunityApproveById(cmd.getApprovalId());
        GeneralForm form = this.generalFormProvider.getGeneralFormById(ca.getFormOriginId());

        if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
            // 使用表单/审批 注意状态 config
            form.setStatus(GeneralFormStatus.RUNNING.getCode());
            this.generalFormProvider.updateGeneralForm(form);
        }

        Flow flow = flowService.getEnabledFlow(ca.getNamespaceId(), ca.getModuleId(),
                ca.getModuleType(), ca.getId(), FlowOwnerType.COMMUNITY_APPROVE.getCode());
        CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
        Long userId = UserContext.current().getUser().getId();
        cmd21.setApplyUserId(userId);
        cmd21.setReferType(FlowReferType.COMMUNITY_APPROVE.getCode());

        cmd21.setProjectId(ca.getProjectId());
        cmd21.setProjectType(ca.getProjectType());
        cmd21.setContent(JSON.toJSONString(cmd));
        cmd21.setCurrentOrganizationId(cmd.getOrganizationId());
        cmd21.setTitle(ca.getApproveName());

        FlowCase flowCase = null;
        if (null == flow) {
            // 给他一个默认哑的flow
            GeneralModuleInfo gm = ConvertHelper.convert(ca, GeneralModuleInfo.class);
            gm.setOwnerId(ca.getId());
            gm.setOwnerType(FlowOwnerType.COMMUNITY_APPROVE.getCode());
            flowCase = flowService.createDumpFlowCase(gm, cmd21);
        } else {
            cmd21.setFlowMainId(flow.getFlowMainId());
            cmd21.setFlowVersion(flow.getFlowVersion());
            flowCase = flowService.createFlowCase(cmd21);
        }

        CommunityApproveVal obj = ConvertHelper.convert(ca, CommunityApproveVal.class);
        obj.setApproveId(ca.getId());
        obj.setFlowCaseId(flowCase.getId());
        //取出姓名 手机号 公司信息
        for (PostApprovalFormItem val : cmd.getValues()) {
            if (GeneralFormDataSourceType.USER_NAME.getCode().equals(val.getFieldName()))
                obj.setNameValue(val.getFieldValue());
            if (GeneralFormDataSourceType.USER_PHONE.equals(val.getFieldName()))
                obj.setPhoneValue(val.getFieldValue());
            if (GeneralFormDataSourceType.USER_COMPANY.equals(val.getFieldName()))
                obj.setCompanyValue(val.getFieldValue());
        }
        Long communityApproveValId = this.communityApproveValProvider.createCommunityApproveVal(obj);

        //将值存起来
        addGeneralFormValuesCommand cmd2 = new addGeneralFormValuesCommand();
        cmd2.setGeneralFormId(form.getFormOriginId());
        cmd2.setSourceId(communityApproveValId);
        cmd2.setSourceType(ca.getModuleType());
        cmd2.setValues(cmd.getValues());

        generalFormService.addGeneralFormValues(cmd2);


        GetTemplateByCommunityApproveIdResponse response = ConvertHelper.convert(ca,
                GetTemplateByCommunityApproveIdResponse.class);
        response.setFlowCaseId(flowCase.getId());
        List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
        fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        response.setFormFields(fieldDTOs);
        response.setValues(cmd.getValues());
        return response;


    }

    @Override
    public void deleteCommunityApprove(CommunityApproveIdCommand cmd) {

    }

    @Override
    public CommunityApproveDTO createCommunityApprove(CreateCommunityApproveCommand cmd) {
        return null;
    }
}
