package com.everhomes.user.smartcard;

public class SmartCardModuleInst implements Comparable<SmartCardModuleInst> {
	private SmartCardModuleInfo info;
	private SmartCardListener moduleListener;

	public SmartCardModuleInfo getInfo() {
		return info;
	}

	public void setInfo(SmartCardModuleInfo info) {
		this.info = info;
	}

    public SmartCardListener getModuleListener() {
        return moduleListener;
    }

    public void setModuleListener(SmartCardListener moduleListener) {
        this.moduleListener = moduleListener;
    }


    @Override
	public int compareTo(SmartCardModuleInst o) {
		return getInfo().compareTo(o.getInfo());
	}
}
