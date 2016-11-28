package com.everhomes.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowGraphScriptAction extends FlowGraphAction implements ApplicationContextAware {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowGraphScriptAction.class);
	
	@Autowired
	private FlowScriptProvider flowScriptProvider;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
		FlowScript flowScript = flowScriptProvider.getFlowScriptById(this.getFlowAction().getScriptId());
		FlowScriptFire runnableScript = null;
		if(flowScript != null) {
			//TODO SpringWorker
			try {
				runnableScript = (FlowScriptFire)PlatformContext.getComponent(flowScript.getScriptCls());
				if(runnableScript == null) {
					Class clazz = Class.forName(flowScript.getScriptCls());
					String[] beanNames = applicationContext.getBeanNamesForType(clazz, true, false);
					if (applicationContext.containsBeanDefinition(flowScript.getScriptCls())) {
						runnableScript = (FlowScriptFire) applicationContext.getBean(beanNames[0]);
					} else {
							if (beanNames != null && beanNames.length == 1) {
								runnableScript = (FlowScriptFire) applicationContext.getBean(beanNames[0]);
							}
					}
                }
				
			} catch (ClassNotFoundException cnfe) {
            	LOGGER.error("Not bean Id or class definition found {}", flowScript.getScriptCls());
            }
            
         if(runnableScript != null) {
        	 runnableScript.fireAction(ctx, event);
            }

		}
	}

	@Override
	public void setApplicationContext(ApplicationContext paramApplicationContext)
			throws BeansException {
		this.applicationContext = paramApplicationContext;
	}

}
