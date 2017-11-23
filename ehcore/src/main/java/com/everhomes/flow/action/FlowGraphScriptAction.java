package com.everhomes.flow.action;

import com.everhomes.flow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowScriptType;

public class FlowGraphScriptAction extends FlowGraphAction {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowGraphScriptAction.class);
	
	private FlowScriptProvider flowScriptProvider;
	
	public FlowGraphScriptAction() {
		flowScriptProvider = PlatformContext.getComponent(FlowScriptProvider.class);
	}
	
	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
		FlowScript flowScript = flowScriptProvider.getFlowScriptById(this.getFlowAction().getScriptId());
		FlowScriptFire runnableScript = null;
		if(flowScript != null) {
			//TODO SpringWorker
			if(FlowScriptType.BEAN_ID.equals(flowScript.getScriptType())) {
				runnableScript = (FlowScriptFire)PlatformContext.getComponent(flowScript.getScriptCls());	
			} else {
				try {
					Class clz = Class.forName(flowScript.getScriptCls());
					runnableScript = (FlowScriptFire)PlatformContext.getComponent(clz);
				} catch (ClassNotFoundException e) {
					LOGGER.error("flow script class not found", e);
				}				
			}
			
         if(runnableScript != null) {
        	 runnableScript.fireAction(ctx, event);
            }

		}
	}

}
