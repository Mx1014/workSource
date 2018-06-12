package com.everhomes.flow;

public class FlowModuleInst implements Comparable<FlowModuleInst> {

	private FlowModuleInfo info;
	private FlowModuleListener moduleListener;

	public FlowModuleInfo getInfo() {
		return info;
	}

	public void setInfo(FlowModuleInfo info) {
		this.info = info;
	}

    public FlowModuleListener getModuleListener() {
        return moduleListener;
    }

    public void setModuleListener(FlowModuleListener moduleListener) {
        this.moduleListener = moduleListener;
    }

    @Override
	public int compareTo(FlowModuleInst o) {
		return getInfo().compareTo(o.getInfo());
	}

}
