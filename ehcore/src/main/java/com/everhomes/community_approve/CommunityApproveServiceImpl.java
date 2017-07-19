package com.everhomes.community_approve;

import com.everhomes.community_approve.CommunityApprove;
import com.everhomes.community_approve.CommunityApproveProvider;
import com.everhomes.community_approve.CommunityApproveService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.community_approve.*;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void deleteCommunityApprove(CommunityApproveIdCommand cmd) {

    }

    @Override
    public CommunityApproveDTO createCommunityApprove(CreateCommunityApproveCommand cmd) {
        return null;
    }
}
