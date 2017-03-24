package com.everhomes.flow;

/**
 * <ul> 插入变量实现
 * <li>APPLIER_NAME: 申请者名字</li>
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
//	public static final String TARGET_PROCESSOR_NAME = "flow-variable-target-processor-name";
//	public static final String SUPERVISOR_NAME = "flow-variable-supervisor-name";
//	public static final String LAST_NODE_PROCESSOR_NAME = "flow-varible-last-processor-name";
//	public static final String N_NODE_PROCESSOR_NAME = "flow-varible-n-processor-name";

	public static final String APPLIER_NAME = "flow-variable-applier-name";
	public static final String APPLIER_PHONE = "flow-variable-applier-phone";
	public static final String CURR_PROCESSOR_PHONE = "flow-variable-curr-processor-phone";
	public static final String CURR_PROCESSOR_NAME = "flow-variable-curr-processor-name";
	public static final String PREFIX_PROCESSOR_NAME = "flow-variable-prefix-processor-name";
	public static final String PREFIX_PROCESSOR_PHONE = "flow-variable-prefix-processor-phone";
	public static final String CURR_ALL_PROCESSORS_NAME = "flow-variable-curr-all-processors-name";
	public static final String CURR_ALL_PROCESSORS_PHONE = "flow-variable-curr-all-processors-phone";
	public static final String PREFIX_ALL_PROCESSORS_NAME = "flow-variable-prefix-all-processors-name";
	public static final String PREFIX_ALL_PROCESSORS_PHONE = "flow-variable-prefix-all-processors-phone";
	public static final String TRANSFER_TARGET_NAME = "flow-variable-transfer-target-name";
	public static final String TRANSFER_TARGET_PHONE = "flow-variable-transfer-target-phone";
	
	String variableTextRender(FlowCaseState ctx, String variable);
}
