package com.everhomes.flow;

import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.user.UserInfo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <ul> 插入变量实现
 * <li>APPLIER: 申请者名字</li>
 * <li>APPLIER_PHONE: 申请者手机号</li>
 * <li>CURR_PROCESSOR_PHONE: 当前节点操作执行人</li>
 * <li>CURR_PROCESSOR_NAME: 当前节点操作执行人</li>
 * <li>PREFIX_PROCESSOR_NAME: 上个节点操作执行人名字</li>
 * <li>PREFIX_PROCESSOR_PHONE: 上个节点操作执行人手机</li>
 * <li>CURR_ALL_PROCESSORS_NAME: 当前所有节点处理人手机</li>
 * <li>CURR_ALL_PROCESSORS_PHONE: 当前所有节点处理人手机</li>
 * <li>PREFIX_ALL_PROCESSORS_NAME: 上节点所有节点处理人手机</li>
 * <li>PREFIX_ALL_PROCESSORS_PHONE: 上节点所有节点处理人手机</li>
 * <li>TRANSFER_TARGET_NAME: 转交的目标人手机</li>
 * <li>TRANSFER_TARGET_PHONE: 转交的目标人电话</li>
 * </ul>
 * @author janson
 *
 */
public interface FlowVariableTextResolver {

	String APPLIER_NAME = "flow-variable-applier-name";
	String APPLIER_PHONE = "flow-variable-applier-phone";
	String CURR_PROCESSOR_PHONE = "flow-variable-curr-processor-phone";
	String CURR_PROCESSOR_NAME = "flow-variable-curr-processor-name";
	String PREFIX_PROCESSOR_NAME = "flow-variable-prefix-processor-name";
	String PREFIX_PROCESSOR_PHONE = "flow-variable-prefix-processor-phone";
	String CURR_ALL_PROCESSORS_NAME = "flow-variable-curr-all-processors-name";
	String CURR_ALL_PROCESSORS_PHONE = "flow-variable-curr-all-processors-phone";
	String PREFIX_ALL_PROCESSORS_NAME = "flow-variable-prefix-all-processors-name";
	String PREFIX_ALL_PROCESSORS_PHONE = "flow-variable-prefix-all-processors-phone";
	String TRANSFER_TARGET_NAME = "flow-variable-transfer-target-name";
	String TRANSFER_TARGET_PHONE = "flow-variable-transfer-target-phone";
	
	String variableTextRender(FlowCaseState ctx, String variable);

	default String displayText(FlowService flowService, FlowCaseState ctx, List<Long> users, Function<UserInfo, String> func) {
        users = users.stream().distinct().collect(Collectors.toList());

        StringBuilder txt = new StringBuilder();
        int i = 0;
        for (Long u : users) {
			UserInfo ui = flowService.getUserInfoInContext(ctx, u);
			if (ui != null) {
                String ret = func.apply(ui);
                if (ret == null || ret.isEmpty()) {
                    continue;
                }

                txt.append(ret).append(", ");
                i++;
                if (i >= FlowConstants.FLOW_MAX_NAME_CNT) {
                    break;
                }
            }
		}

		if (txt.length() > 2) {
			txt = new StringBuilder(txt.substring(0, txt.length() - 2));
		}
		if (users.size() > FlowConstants.FLOW_MAX_NAME_CNT) {
			txt.append(" 等 ").append(users.size()).append(" 人");
		}
		return txt.toString();
	}

    default String getPhone(UserInfo userInfo) {
        return userInfo.getPhones() != null
                && userInfo.getPhones().size() > 0 ? userInfo.getPhones().iterator().next() : "";
    }

    default String getNickName(UserInfo userInfo) {
        return userInfo.getNickName();
    }
}
