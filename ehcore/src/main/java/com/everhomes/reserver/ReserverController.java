// @formatter:off
package com.everhomes.reserver;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.reserver.CreateReserverOrderCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;


@RestDoc(value="Reserver controller", site="reserver")
@RestController
@RequestMapping("/reserver")
public class ReserverController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReserverController.class);

    @Autowired
    private FlowService flowService;

	/**
     * <b>URL: /reserver/createReserverOrder</b>
     * <p>新建位置预订</p>
     */
    @RequestMapping("createReserverOrder")
    @RestReturn(value=String.class)
    public RestResponse createReserverOrder(CreateReserverOrderCommand cmd) {

        //新建flowcase

        User user = UserContext.current().getUser();
        if (null == user) {

        }

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.RESERVER_PLACE,
                FlowModuleType.NO_MODULE.getCode(), 0L, FlowOwnerType.RESERVER_PLACE.getCode());
        if(null == flow) {
            LOGGER.error("Enable reserver flow not found, moduleId={}", FlowConstants.RESERVER_PLACE);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
                    "Enable reserver flow not found.");
        }
        CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
        createFlowCaseCommand.setApplyUserId(user.getId());
        createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
        createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
        createFlowCaseCommand.setReferId(Long.valueOf(cmd.getOrderId()));
        createFlowCaseCommand.setReferType(FlowOwnerType.RESERVER_PLACE.getCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder("");
        sb.append("就餐时间：").append(sdf.format(new Date(cmd.getReserverTime()))).append("\n");
        sb.append("就餐人数：").append(cmd.getReserverNum()).append("人").append("\n");
        sb.append("备注说明：").append(cmd.getRemark()).append("\n");
        sb.append("申请人：").append(cmd.getRequestorName()).append("\n");
        sb.append("店铺名称：").append(cmd.getShopName());
        createFlowCaseCommand.setContent(sb.toString());
//        createFlowCaseCommand.setProjectId(task.getOwnerId());
//        createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());

        FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


}