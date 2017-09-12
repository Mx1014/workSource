// @formatter:off
package com.everhomes.statistics.event.step;

import com.everhomes.db.DbProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;
import com.everhomes.statistics.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xq.tian on 2017/8/14.
 */
@Component
public class EventStatStep extends AbstractStatEventStep implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventStatStep.class);

    @Autowired
    private List<StatEventHandler> handlers;

    private final Map<String, StatEventHandler> handlerMap = new ConcurrentHashMap<>();

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private StatEventProvider statEventProvider;

    @Autowired
    private StatEventStatisticProvider statEventStatisticProvider;

    @Autowired
    private NamespaceProvider namespaceProvider;

    @Override
    public void doExecute(StatEventStepExecution execution) {
        LocalDate statDate = execution.getParam("statDate");

        List<Namespace> namespaces = namespaceProvider.listNamespaces();

        List<StatEvent> statEvents = statEventProvider.listStatEvent();

        Map<String, Object> stepMeta = new HashMap<>();
        boolean success = true;
        for (StatEvent statEvent : statEvents) {
            StatEventHandler handler = handlerMap.get(statEvent.getEventName());
            if (handler == null) {
                continue;
            }
            for (Namespace namespace : namespaces) {
                // handler task meta
                Object taskMeta = null;
                try {
                    if (thisHandlerStepFinished(handler, namespace, execution)) {
                        LOGGER.warn("task [{}] handler step [{}] already finished", execution.getTaskDate(), handler.getClass().getSimpleName());
                        continue;
                    }

                    // 处理并在事物中保存
                    save(process(statDate, statEvent, handler, namespace, execution.getInterval()));

                    // success
                    taskMeta = 1;
                } catch (Throwable t) {
                    success = false;
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                         PrintStream stream = new PrintStream(out))
                    {
                        t.printStackTrace(stream);
                        // error
                        taskMeta = out.toString("UTF-8");
                    } catch (Exception ignored) { }
                } finally {
                    stepMeta.put(String.format("%s:%s", handler.getEventName(), namespace.getId()), taskMeta);
                }
            }
        }
        // 自己特殊的stepMeta
        execution.getTaskMeta().put(getStepName(), stepMeta);
        if (success) {
            execution.setStatus(StatEventStepExecution.Status.FINISH);
        } else {
            execution.setStatus(StatEventStepExecution.Status.ERROR);
        }
    }

    private void save(List<StatEventStatistic> statList) {
        dbProvider.execute(status -> {
            statEventStatisticProvider.insertEventStatList(statList);
            return true;
        });
    }

    private List<StatEventStatistic> process(LocalDate statDate, StatEvent statEvent, StatEventHandler handler, Namespace namespace, StatEventStatTimeInterval interval) {
        return handler.process(namespace, statEvent, statDate, interval);
    }

    private String getHandlerStepName(StatEventHandler handler, Integer namespaceId) {
        return String.format("%s:%s", handler.getClass().getName(), namespaceId);
    }

    private boolean thisHandlerStepFinished(StatEventHandler handler, Namespace namespace, StatEventStepExecution execution) {
        Map<String, Object> map = (Map<String, Object>) execution.getTaskMeta().get(getStepName());
        return map != null
                && map.get(getHandlerStepName(handler, namespace.getId())) != null
                && map.get(getHandlerStepName(handler, namespace.getId())).getClass().isInstance(Integer.class)
                && map.get(getHandlerStepName(handler, namespace.getId())).equals(1);
    }

    protected void after(StatEventStepExecution execution) {
        execution.setEndTime(System.currentTimeMillis());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerInit();
    }

    private void handlerInit() {
        if (handlers != null) {
            for (StatEventHandler handler : handlers) {
                handlerMap.put(handler.getEventName(), handler);
            }
        } else {
            LOGGER.warn("not found stat event handler");
        }
    }
}
