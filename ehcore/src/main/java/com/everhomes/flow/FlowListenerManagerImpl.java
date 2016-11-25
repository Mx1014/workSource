package com.everhomes.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlowListenerManagerImpl implements FlowListenerManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowListenerManagerImpl.class);
	
	  @Autowired
	  List<FlowModuleListener> allListeners;
	  
	  private Map<String, FlowModuleInst> moduleMap = new HashMap<String, FlowModuleInst>();
	  
	  @PostConstruct
	  void setup() {
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
	  
	  public FlowListenerManagerImpl() {
		  
	  }
	  
	  @Override
	  public void onFlowCaseStart(FlowCaseContext ctx) {
		  FlowModuleInst inst = moduleMap.get(ctx.getModule());
		  FlowModuleListener listener = inst.getListener();
		  listener.onFlowCaseStart(ctx);
	  }
	  
	  @Override
	  public int getListenerSize() {
		  return allListeners.size();  
	  }
}
