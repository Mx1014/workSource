package com.everhomes.qrcode.listener;

import com.everhomes.flow.*;
import com.everhomes.qrcode.QRCodeModuleListener;
import com.everhomes.rest.common.FlowCaseDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.qrcode.QRCodeSource;
import com.everhomes.user.UserContext;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xq.tian on 2017/11/15.
 */
@Deprecated
@Component
public class FlowQRCodeModuleListener implements QRCodeModuleListener {

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowEventLogProvider flowEventLogProvider;

    @Autowired
    private FlowListenerManager flowListenerManager;

    @Override
    public QRCodeHandler init() {
        return QRCodeHandler.FLOW;
    }

    @Override
    public void onGetQRCodeInfo(QRCodeDTO qrCode, QRCodeSource source) {
        Long userId = UserContext.currentUserId();

        if (userId != null) {
            FlowCaseDetailActionData actionData = RouterBuilder.parse(qrCode.getRouteUri(), Router.WORKFLOW_DETAIL, FlowCaseDetailActionData.class);
            FlowCase flowCase = flowService.getFlowCaseById(actionData.getFlowCaseId());

            if (source == QRCodeSource.INDEX) {
                // 是申请人
                if (flowCase.getApplyUserId().equals(userId)) {
                    return;
                }

                List<FlowCase> allFlowCase = flowService.getAllFlowCase(flowCase.getId());
                for (FlowCase aCase : allFlowCase) {
                    // 处理人
                    FlowEventLog processor = flowEventLogProvider.isProcessor(userId, aCase);
                    if (processor != null) {
                        return;
                    }

                    // 督办人
                    FlowEventLog supervisor = flowEventLogProvider.isSupervisor(userId, aCase);
                    if (supervisor != null) {
                        return;
                    }

                    // 历史处理人
                    FlowEventLog historyProcessors = flowEventLogProvider.isHistoryProcessors(userId, aCase);
                    if (historyProcessors != null) {
                        return;
                    }
                }
            } else if (source == QRCodeSource.FLOW) {
                flowListenerManager.onScanQRCode(flowCase, qrCode, userId);
                return;
            }
        }
        throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_ON_SCAN_QR_CODE,
                "Identity validate error");
    }
}