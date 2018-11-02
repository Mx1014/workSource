package com.everhomes.user.smartcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.SmartCardHandler;


@Component
public class SmartCardModuleManagerImpl implements SmartCardModuleManager, ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmartCardModuleManagerImpl.class);
	
    @Autowired(required = false)
    private List<SmartCardListener> moduleListeners;
    private Map<Long, SmartCardModuleInst> moduleMap = new HashMap<>();
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (SmartCardListener listener : moduleListeners) {
            try {
                SmartCardModuleInfo info = listener.initModule();
                SmartCardModuleInst inst = new SmartCardModuleInst();
                inst.setInfo(info);
                inst.setModuleListener(listener);
                moduleMap.put(info.getModuleId(), inst);
            } catch (Exception ex) {
                LOGGER.error("module init error cls=" + listener.getClass(), ex);
            }
        }
    }

    @Override
    public SmartCardModuleInfo getModule(Long moduleId) {
        SmartCardModuleInst inst = moduleMap.get(moduleId);
        if (inst != null) {
            return inst.getInfo();
        }
        return null;
    }

    @Override
    public List<SmartCardModuleInfo> getModules() {
        return moduleMap.values().stream().map(SmartCardModuleInst::getInfo).collect(Collectors.toList());
    }

	@Override
	public List<SmartCardHandler> generateSmartCards(
			SmartCardProcessorContext ctx) {
		List<SmartCardHandler> handlers = new ArrayList<SmartCardHandler>();
		for(Entry<Long, SmartCardModuleInst> entry : moduleMap.entrySet()) {
			List<SmartCardHandler> hs = entry.getValue().getModuleListener().generateSmartCards(ctx);
			if(hs != null) {
				for(SmartCardHandler h : hs) {
					//force to update moduleId
					h.setModuleId(entry.getKey());
				}
				handlers.addAll(hs);
			}
		}
		
		return handlers;
	}

	@Override
	public List<SmartCardHandler> generateStandaloneCards(
			SmartCardProcessorContext ctx) {
		List<SmartCardHandler> handlers = new ArrayList<SmartCardHandler>();
		for(Entry<Long, SmartCardModuleInst> entry : moduleMap.entrySet()) {
			List<SmartCardHandler> hs = entry.getValue().getModuleListener().generateStandaloneCards(ctx);
			if(hs != null) {
				handlers.addAll(hs);
			}
		}
		
		return handlers;
	}
    
}
