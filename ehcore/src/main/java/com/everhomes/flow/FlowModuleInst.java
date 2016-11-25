package com.everhomes.flow;

public class FlowModuleInst implements Comparable<FlowModuleInst> {
	private FlowModuleInfo info;
	private FlowModuleListener listener;

	public FlowModuleInfo getInfo() {
		return info;
	}

	public void setInfo(FlowModuleInfo info) {
		this.info = info;
	}

	public FlowModuleListener getListener() {
		return listener;
	}

	public void setListener(FlowModuleListener listener) {
		this.listener = listener;
	}

	@Override
	public int compareTo(FlowModuleInst o) {
		return getInfo().compareTo(o.getInfo());
	}

}
