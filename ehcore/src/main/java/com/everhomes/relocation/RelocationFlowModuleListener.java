// @formatter:off
package com.everhomes.relocation;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.AssetService;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.flow.*;
import com.everhomes.flow.conditionvariable.FlowConditionStringVariable;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.qrcode.QRCodeService;
import com.everhomes.rest.asset.CheckEnterpriseHasArrearageCommand;
import com.everhomes.rest.asset.CheckEnterpriseHasArrearageResponse;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.relocation.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RelocationFlowModuleListener implements FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelocationFlowModuleListener.class);

    @Autowired
    private RelocationProvider relocationProvider;
    @Autowired
    private QRCodeService qRCodeService;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private RelocationService relocationService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        // 更新状态
        FlowCase flowCase = ctx.getFlowCase();
        Byte status = flowCase.getStatus();

        if (status != null) {
            RelocationRequest request = relocationProvider.findRelocationRequestById(flowCase.getReferId());
            if (null != request) {
                request.setStatus(convertStatus(status));
                relocationProvider.updateRelocationRequest(request);
            }
        }
    }

    private Byte convertStatus(Byte flowCaseStatus) {
        FlowCaseStatus status = FlowCaseStatus.fromCode(flowCaseStatus);
        if (null != status) {
            switch (status) {
                case PROCESS: return RelocationRequestStatus.PROCESSING.getCode();
                case ABSORTED: return RelocationRequestStatus.CANCELED.getCode();
                case FINISHED: return RelocationRequestStatus.COMPLETED.getCode();
                default: return RelocationRequestStatus.PROCESSING.getCode();
            }
        }
        return null;
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {

        String content = flowCase.getContent();
        if (StringUtils.isNotBlank(content) && flowUserType == FlowUserType.APPLIER) {
            int lineBreak = content.indexOf("\\r\\n");
            content = content.substring(lineBreak + 4);
        }
        return content;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {

        GetRelocationRequestDetailCommand cmd = new GetRelocationRequestDetailCommand();
        cmd.setId(flowCase.getReferId());

        RelocationRequestDTO dto = relocationService.getRelocationRequestDetail(cmd);
        if (null != dto) {

            String flowCaseUrl = configProvider.getValue(ConfigConstants.RELOCATION_FLOWCASE_URL, "");

            NewQRCodeCommand qrCmd = new NewQRCodeCommand();
            qrCmd.setRouteUri(String.format(flowCaseUrl, flowCase.getId(), FlowUserType.PROCESSOR.getCode()));
            qrCmd.setHandler(QRCodeHandler.FLOW.getCode());
            QRCodeDTO qRCodeDTO = qRCodeService.createQRCode(qrCmd);
            dto.setQrCodeUrl(qRCodeDTO.getUrl());
            flowCase.setCustomObject(JSONObject.toJSONString(dto));

        } else {
            LOGGER.warn("RelocationRequest ot found flowCase: {}", StringHelper.toJsonString(flowCase));
        }
        return new ArrayList<>();
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleDTO module = flowService.getModuleById(FlowConstants.RELOCATION_MODULE);
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        moduleInfo.setModuleId(module.getModuleId());
        moduleInfo.setModuleName(module.getModuleName());
        return moduleInfo;
    }

    @Override
    public void onFlowCreating(Flow flow) {

    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
                                        List<Tuple<String, Object>> variables) {}

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        List<FlowServiceTypeDTO> result = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        dto.setNamespaceId(namespaceId);

        String locale = UserContext.current().getUser().getLocale();

        String serviceName = localeStringService.getLocalizedString(RelocationTemplateCode.SCOPE,
                String.valueOf(RelocationTemplateCode.SERVICE_TYPE_NAME), locale, "");

        dto.setServiceName(serviceName);
        result.add(dto);
        return result;
    }

    /**
     * 获取工作流条件参数
     * @param flow  工作流
     * @param flowEntityType 不同地方的参数，比如条件，节点，按钮等
     * @param ownerType 归属类型
     * @param ownerId   归属id
     * @return  返回参数列表
     */
    public List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {

        FlowConditionVariableDTO dto = new FlowConditionVariableDTO();
        dto.setName("relocationMode");
        dto.setDisplayName("relocationMode");
//        dto.setOperators(Collections.singletonList("="));
        return Collections.singletonList(dto);
    }

    public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String extra) {

        FlowCase flowCase = ctx.getFlowCase();

        String mode = RelocationFlowMode.TRADITIONAL.getCode();
        RelocationRequest request = relocationProvider.findRelocationRequestById(flowCase.getReferId());
        if (null != request) {

            CheckEnterpriseHasArrearageCommand cmd = new CheckEnterpriseHasArrearageCommand();
            cmd.setCommunityId(request.getOwnerId());
            cmd.setNamespaceId(request.getNamespaceId());
            cmd.setOrganizationId(request.getRequestorEnterpriseId());
            CheckEnterpriseHasArrearageResponse response = assetService.checkEnterpriseHasArrearage(cmd);

            if (null != response && response.getHasArrearage() == 0) {
                mode = RelocationFlowMode.INTELLIGENT.getCode();
            }

        }
        return new FlowConditionStringVariable(mode);
    }
}
