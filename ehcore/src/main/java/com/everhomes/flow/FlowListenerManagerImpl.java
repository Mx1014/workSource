package com.everhomes.flow;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FlowListenerManagerImpl implements FlowListenerManager, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowListenerManagerImpl.class);

    @Autowired(required = false)
    private List<FlowModuleListener> moduleListeners;

    @Autowired
    private FlowService flowService;

    private Map<Long, FlowModuleInst> moduleMap = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // if (event.getApplicationContext().getParent() != null) {
        //     return;
        // }
        for (FlowModuleListener listener : moduleListeners) {
            try {
                FlowModuleInfo info = listener.initModule();
                FlowModuleInst inst = new FlowModuleInst();
                inst.setInfo(info);
                inst.setModuleListener(listener);
                moduleMap.put(info.getModuleId(), inst);
            } catch (Exception ex) {
                LOGGER.error("module init error cls=" + listener.getClass(), ex);
            }
        }
    }

    @Override
    public FlowModuleInfo getModule(Long moduleId) {
        FlowModuleInst inst = moduleMap.get(moduleId);
        if (inst != null) {
            return inst.getInfo();
        }
        return null;
    }

    @Override
    public List<FlowModuleInfo> getModules() {
        return moduleMap.values().stream().map(FlowModuleInst::getInfo).collect(Collectors.toList());
    }

    @Override
    public void onFlowCreating(Flow flow) {
        FlowModuleDTO module = flowService.getModuleById(flow.getModuleId());
        if (module == null) {
            return;
        }

        FlowModuleInst inst = moduleMap.get(module.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCreating(flow);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseStart(ctx);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseAbsorted(ctx);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseStateChanged(ctx);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowButtonFired(ctx);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseEnd(ctx);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseActionFired(ctx);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        FlowModuleInst inst = moduleMap.get(flowCase.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            String briefRender = null;
            try {
                briefRender = listener.onFlowCaseBriefRender(flowCase, flowUserType);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            return briefRender;
        }
        return null;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        FlowModuleInst inst = moduleMap.get(flowCase.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            List<FlowCaseEntity> entities = null;
            try {
                entities = listener.onFlowCaseDetailRender(flowCase, flowUserType);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            return entities;
        }
        return null;
    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowSMSVariableRender(ctx, templateId, variables);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowMessageSend(ctx, messageDto);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            String value = null;
            try {
                value = listener.onFlowVariableRender(ctx, variable);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            return value;
        }
        return null;
    }

    @Override
    public List<FlowServiceTypeDTO> listFlowServiceTypes(Integer namespaceId, Long moduleId, String ownerType, Long ownerId) {
        if (moduleId != null) {
            FlowModuleInst moduleInst = moduleMap.get(moduleId);
            if (moduleInst != null) {
                List<FlowServiceTypeDTO> types = listFlowServiceTypesFromListener(moduleInst, namespaceId, ownerType, ownerId);
                if (types != null) {
                    types.forEach(r -> r.setModuleId(moduleId));
                    return types;
                }
            }
        } else {
            List<FlowServiceTypeDTO> serviceTypes = new ArrayList<>();
            moduleMap.forEach((k, v) -> {
                List<FlowServiceTypeDTO> types = listFlowServiceTypesFromListener(v, namespaceId, ownerType, ownerId);
                if (types != null) {
                    types.forEach(r -> r.setModuleId(k));
                    serviceTypes.addAll(types);
                }
            });
            return serviceTypes;
        }
        return null;
    }

    private List<FlowServiceTypeDTO> listFlowServiceTypesFromListener(FlowModuleInst moduleInst, Integer namespaceId, String ownerType, Long ownerId) {
        FlowModuleListener listener = moduleInst.getModuleListener();
        try {
            return listener.listServiceTypes(namespaceId, ownerType, ownerId);
        } catch (Exception e) {
            wrapError(e, listener);
            throw e; // always not reach here
        }
    }

    @Override
    public List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        FlowModuleInst inst = moduleMap.get(flow.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            List<FlowConditionVariableDTO> dtoList = null;
            try {
                dtoList = listener.listFlowConditionVariables(flow, flowEntityType, ownerType, ownerId);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            if (dtoList != null) {
                return dtoList;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public FlowConditionVariable onFlowConditionVariableRender(
            FlowCaseState ctx, String variable, String entityType, Long entityId, String extra) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            FlowConditionVariable conditionVariable = null;
            try {
                conditionVariable = listener.onFlowConditionVariableRender(ctx, variable, entityType, entityId, extra);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            return conditionVariable;
        }
        return null;
    }

    @Override
    public List<FlowFormDTO> listFlowForms(Flow flow) {
        FlowModuleInst inst = moduleMap.get(flow.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            List<FlowFormDTO> flowForms = null;
            try {
                flowForms = listener.listFlowForms(flow);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            return flowForms;
        }
        return null;
    }

    @Override
    public void onFlowCaseEvaluate(FlowCaseState ctx, List<FlowEvaluate> evaluates) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseEvaluate(ctx, evaluates);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowStateChanged(Flow flow) {
        FlowModuleInst inst = moduleMap.get(flow.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowStateChanged(flow);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowStateChanging(Flow flow) {
        FlowModuleInst inst = moduleMap.get(flow.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowStateChanging(flow);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public FlowConditionVariable onFlowConditionFormVariableRender(FlowCaseState ctx, String variable, String entityType, Long entityId, String extra) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            FlowConditionVariable conditionVariable = null;
            try {
                conditionVariable = listener.onFlowConditionFormVariableRender(ctx, variable, entityType, entityId, extra);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            return conditionVariable;
        }
        return null;
    }

    @Override
    public void onSubFlowEnter(FlowCaseState ctx, FlowServiceMapping mapping, Flow subFlow, CreateFlowCaseCommand cmd) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onSubFlowEnter(ctx, mapping, subFlow, cmd);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onScanQRCode(FlowCase flowCase, QRCodeDTO qrCode, Long currentUserId) {
        FlowModuleInst inst = moduleMap.get(flowCase.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            listener.onScanQRCode(flowCase, qrCode, currentUserId);
        }
    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
        FlowModuleInst inst = moduleMap.get(flowCase.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseCreating(flowCase);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {
        FlowModuleInst inst = moduleMap.get(flowCase.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getModuleListener();
            try {
                listener.onFlowCaseCreated(flowCase);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    private void wrapError(Exception e, FlowModuleListener listener) throws RuntimeException {
        if (e instanceof RuntimeErrorException) {
            throw ((RuntimeErrorException) e);
        }
        LOGGER.error("wrapError old exception", e);
        throw RuntimeErrorException.errorWith(e, ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                "Flow module listener error, listener=%s, cause=%s", listener.getClass().getSimpleName(), e);
    }
}
