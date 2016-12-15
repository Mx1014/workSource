package com.everhomes.flow;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowUserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FlowListenerManagerImpl implements FlowListenerManager, ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowListenerManagerImpl.class);
	
	  @Autowired
	  List<FlowModuleListener> allListeners;
	  
	  private Map<String, FlowModuleInst> moduleMap = new HashMap<String, FlowModuleInst>();

	  public FlowListenerManagerImpl() {
		  
	  }
	  
	  @Override
	  public FlowModuleInfo getModule(String module) {
		  FlowModuleInst inst = moduleMap.get(module);
		  if(inst != null) {
			  return inst.getInfo();
		  }
		  
		  return null;
	  }
	  
	  @Override 
	  public void onFlowCaseStart(FlowCaseState ctx) {
		  FlowModuleInst inst = moduleMap.get(ctx.getModuleName());
		  if(inst != null) {
			  ctx.setModule(inst.getInfo());
			  FlowModuleListener listener = inst.getListener();
			  listener.onFlowCaseStart(ctx);  
		  }
	  }
	  
	  @Override
	  public int getListenerSize() {
		  return allListeners.size();  
	  }

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		  FlowModuleInst inst = moduleMap.get(ctx.getModuleName());
		  if(inst != null) {
			  ctx.setModule(inst.getInfo());
			  FlowModuleListener listener = inst.getListener();
			  listener.onFlowCaseAbsorted(ctx);  
		  }		
	}

	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		  FlowModuleInst inst = moduleMap.get(ctx.getModuleName());
		  if(inst != null) {
			  ctx.setModule(inst.getInfo());
			  FlowModuleListener listener = inst.getListener();
			  listener.onFlowCaseStateChanged(ctx);  
		  }		
	}
	
	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		  FlowModuleInst inst = moduleMap.get(ctx.getModuleName());
		  if(inst != null) {
			  ctx.setModule(inst.getInfo());
			  FlowModuleListener listener = inst.getListener();
			  listener.onFlowButtonFired(ctx);  
		  }				
	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		  FlowModuleInst inst = moduleMap.get(ctx.getModuleName());
		  if(inst != null) {
			  ctx.setModule(inst.getInfo());
			  FlowModuleListener listener = inst.getListener();
			  listener.onFlowCaseEnd(ctx);  
		  }		
	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {
		  FlowModuleInst inst = moduleMap.get(ctx.getModuleName());
		  if(inst != null) {
			  ctx.setModule(inst.getInfo());
			  FlowModuleListener listener = inst.getListener();
			  listener.onFlowCaseActionFired(ctx);  
		  }		
	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		  FlowModuleInst inst = moduleMap.get(flowCase.getModuleName());
		  if(inst != null) {
			  FlowModuleListener listener = inst.getListener();
			  return listener.onFlowCaseBriefRender(flowCase);  
		  }		
		  return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		  FlowModuleInst inst = moduleMap.get(flowCase.getModuleName());
		  if(inst != null) {
			  FlowModuleListener listener = inst.getListener();
			  return listener.onFlowCaseDetailRender(flowCase, flowUserType);  
		  }		
		  return null;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		FlowModuleInst inst = moduleMap.get(ctx.getModuleName());
		  if(inst != null) {
			  FlowModuleListener listener = inst.getListener();
			  return listener.onFlowVariableRender(ctx, variable);  
		  }		
		  return null;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		  for(FlowModuleListener listener : allListeners) {
			  try {
				  FlowModuleInfo info = listener.initModule();
				  FlowModuleInst inst = new FlowModuleInst();
				  inst.setInfo(info);
				  inst.setListener(listener);
				  moduleMap.put(info.getModuleName(), inst);//TODO support TreeSet for listeners ?
			  } catch(Exception ex) {
				  LOGGER.error("module init error cls=" + listener.getClass(), ex);
			  }
			  
		  }
	}
}
