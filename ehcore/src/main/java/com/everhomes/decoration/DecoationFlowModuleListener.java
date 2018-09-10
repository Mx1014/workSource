package com.everhomes.decoration;

import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.decoration.DecorationRequestStatus;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.server.schema.tables.pojos.EhDecorationApprovalVals;
import com.everhomes.user.UserContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DecoationFlowModuleListener implements FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecoationFlowModuleListener.class);
    private static final SimpleDateFormat sdfyMd= new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private PortalService portalService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private DecorationProvider decorationProvider;
    @Autowired
    private DecorationService decorationService;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private DecorationSMSProcessor decorationSMSProcessor;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(DecorationController.moduleId);
        module.setModuleName(moduleDTO.getDisplayName());
        module.setModuleId(DecorationController.moduleId);
        return module;
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        // TODO Auto-generated method stub
        FlowCase flowCase = ctx.getFlowCase();
        if (DecorationRequestStatus.CONSTRACT.getFlowOwnerType().equals(flowCase.getReferType()))
            return;
        DecorationRequest request = decorationProvider.getRequestById(flowCase.getReferId());
        request.setCancelFlag((byte)1);
        this.decorationProvider.updateDecorationRequest(request);
        if (request.getStatus() == DecorationRequestStatus.FILE_APPROVAL.getCode()){
            //短信通知
            decorationSMSProcessor.fileApprovalFail(request);
        }else if (request.getStatus() == DecorationRequestStatus.CHECK.getCode()){
            decorationSMSProcessor.checkfail(request);
        }
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        if (DecorationRequestStatus.CONSTRACT.getFlowOwnerType().equals(flowCase.getReferType()))
            return;
        if (DecorationRequestStatus.APPLY.getFlowOwnerType().equals(flowCase.getReferType()))
            this.decorationService.DecorationApplySuccess(flowCase.getReferId());
        else if (DecorationRequestStatus.FILE_APPROVAL.getFlowOwnerType().equals(flowCase.getReferType()))
            this.decorationService.FileApprovalSuccess(flowCase.getReferId());
        else if (DecorationRequestStatus.CHECK.getFlowOwnerType().equals(flowCase.getReferType()))
            this.decorationService.DecorationCheckSuccess(flowCase.getReferId());
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        DecorationRequest request;
        if (DecorationRequestStatus.CONSTRACT.getFlowOwnerType().equals(flowCase.getReferType())){
            DecorationApprovalVal val = this.decorationProvider.getApprovalValById(flowCase.getReferId());
            request = this.decorationProvider.getRequestById(val.getRequestId());
            entities.addAll(this.decorationService.getFormEntitiesByApprovalVal(val));
        }else{
            request = this.decorationProvider.getRequestById(flowCase.getReferId());
        }
        FlowCaseEntity e;
        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("姓名");
        e.setValue(request.getApplyName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("联系电话");
        e.setValue(request.getApplyPhone());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("企业名称");
        e.setValue(request.getApplyCompany());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("装修地点");
        e.setValue(this.decorationService.convertAddress(request.getAddress()));
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("装修期限");
        e.setValue(sdfyMd.format(new Date(request.getStartTime().getTime()))+"至"+
                sdfyMd.format(new Date(request.getEndTime().getTime())));
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("装修公司");
        e.setValue(request.getDecoratorCompany());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("负责人姓名");
        e.setValue(request.getDecoratorName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("联系电话");
        e.setValue(request.getDecoratorPhone());
        entities.add(e);
        return entities;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
        listServiceModuleAppsCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        listServiceModuleAppsCommand.setModuleId(FlowConstants.DECORATION_MODULE);
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
        List<FlowServiceTypeDTO> dtos = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        dto.setNamespaceId(namespaceId);
        if (apps!=null && apps.getServiceModuleApps().size()>0)
            dto.setServiceName(apps.getServiceModuleApps().get(0).getName());
        else
            dto.setServiceName("装修办理");
        return dtos;
    }
}
