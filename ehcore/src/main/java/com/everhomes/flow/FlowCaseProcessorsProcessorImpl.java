package com.everhomes.flow;

import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/11/13.
 */
public class FlowCaseProcessorsProcessorImpl implements FlowCaseProcessorsProcessor {

    private List<Long> processorIdList;
    private List<FlowCase> allFlowCase;
    private boolean allFlowCaseFlag;

    private FlowService flowService;
    private UserService userService;

    public FlowCaseProcessorsProcessorImpl(List<Long> processorIdList, List<FlowCase> allFlowCase,
                                           boolean allFlowCaseFlag, FlowService flowService, UserService userService) {
        this.processorIdList = processorIdList;
        this.flowService = flowService;
        this.userService = userService;
        this.allFlowCase = allFlowCase;
        this.allFlowCaseFlag = allFlowCaseFlag;
    }

    @Override
    public List<UserInfo> getProcessorsInfoList() {
        Long organizationId = getOrganizationId();

        List<UserInfo> userInfoList = new ArrayList<>();
        for (Long userId : getProcessorIdList()) {
            userInfoList.add(fixupUserInfo(organizationId, userId));
        }
        return userInfoList;
    }

    @Override
    public List<UserInfo> getProcessorsInfoList(FlowCaseState ctx) {
        Long organizationId = getOrganizationId();
        List<Long> idList = getProcessorsIdList(ctx);
        return idList.stream().map(r -> this.fixupUserInfo(organizationId, r)).collect(Collectors.toList());
    }

    @Override
    public List<Long> getProcessorsIdList(FlowCaseState ctx) {
        List<Long> processorsIdList = new ArrayList<>();

        if (allFlowCaseFlag) {
            for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
                processorsIdList.addAll(
                        ctxLogsFilter(flowCaseState)
                );
            }
        } else {
            processorsIdList.addAll(
                    ctxLogsFilter(ctx)
            );
        }
        processorsIdList.addAll(getProcessorIdList());
        return processorsIdList;
    }

    @Override
    public List<Long> getProcessorIdList() {
        return processorIdList;
    }

    private List<Long> ctxLogsFilter(FlowCaseState flowCaseState) {
        return flowCaseState.getLogs().stream()
                .filter(r -> FlowLogType.fromCode(r.getLogType()) == FlowLogType.NODE_ENTER
                        && r.getStepCount().equals(flowCaseState.getFlowCase().getStepCount()))
                .map(FlowEventLog::getFlowUserId)
                .collect(Collectors.toList());
    }

    private Long getOrganizationId() {
        Long organizationId = null;
        for (FlowCase flowCase : allFlowCase) {
            organizationId = flowCase.getOrganizationId();
            break;
        }
        return organizationId;
    }

    private UserInfo fixupUserInfo(Long organizationId, Long userId) {
        UserInfo userInfo = userService.getUserSnapshotInfo(userId);
        flowService.fixupUserInfo(organizationId, userInfo);
        return userInfo;
    }

    public void setProcessorIdList(List<Long> processorIdList) {
        this.processorIdList = processorIdList;
    }
}
