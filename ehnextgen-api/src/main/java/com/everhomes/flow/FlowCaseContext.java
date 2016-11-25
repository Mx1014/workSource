package com.everhomes.flow;

public class FlowCaseContext {
	private String moduleName;
	private FlowModuleInfo module;
	private FlowCase flowCase;
	private Flow snapshotFlow;

	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public FlowModuleInfo getModule() {
		return module;
	}
	public void setModule(FlowModuleInfo module) {
		this.module = module;
	}
	public FlowCase getFlowCase() {
		return flowCase;
	}
	public void setFlowCase(FlowCase flowCase) {
		this.flowCase = flowCase;
	}
	public Flow getSnapshotFlow() {
		return snapshotFlow;
	}
	public void setSnapshotFlow(Flow snapshotFlow) {
		this.snapshotFlow = snapshotFlow;
	}
	

}
