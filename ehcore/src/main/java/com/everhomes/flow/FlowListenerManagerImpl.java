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

    @Autowired
    private List<FlowModuleListener> allListeners;

    @Autowired
    private FlowService flowService;

    private Map<Long, FlowModuleInst> moduleMap = new HashMap<Long, FlowModuleInst>();

    public FlowListenerManagerImpl() {

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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
            try {
                listener.onFlowCaseStart(ctx);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    @Override
    public int getListenerSize() {
        return allListeners.size();
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
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

    /*@Override
    public List<FlowPredefinedParamDTO> listFlowPredefinedParam(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        FlowModuleInst inst = moduleMap.get(flow.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getListener();
            List<FlowPredefinedParamDTO> dtoList = null;
            try {
                dtoList = listener.listFlowPredefinedParam(flow, flowEntityType, ownerType, ownerId);
            } catch (Exception e) {
                wrapError(e, listener);
            }
            if (dtoList != null) {
                return dtoList;
            }
        }
        return new ArrayList<>();
    }*/

    @Override
    public List<FlowServiceTypeDTO> listFlowServiceTypes(Integer namespaceId, Long moduleId, String ownerType, Long ownerId) {
        List<FlowServiceTypeDTO> serviceTypes = new ArrayList<>();
        if (moduleId != null) {
            FlowModuleInst moduleInst = moduleMap.get(moduleId);
            if (moduleInst != null) {
                FlowModuleListener listener = moduleInst.getListener();
                try {
                    List<FlowServiceTypeDTO> types = listener.listServiceTypes(namespaceId, ownerType, ownerId);
                    if (types != null) {
                        serviceTypes.addAll(types);
                    }
                } catch (Exception e) {
                    wrapError(e, listener);
                }
            }
        } else {
            moduleMap.forEach((k, v) -> {
                FlowModuleListener listener = v.getListener();
                try {
                    List<FlowServiceTypeDTO> types = listener.listServiceTypes(namespaceId, ownerType, ownerId);
                    if (types != null) {
                        serviceTypes.addAll(types);
                    }
                } catch (Exception e) {
                    wrapError(e, listener);
                }
            });
        }
        return serviceTypes;
    }

    @Override
    public List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        FlowModuleInst inst = moduleMap.get(flow.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getListener();
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
    public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String extra) {
        FlowModuleInst inst = moduleMap.get(ctx.getModuleId());
        if (inst != null) {
            ctx.setModule(inst.getInfo());
            FlowModuleListener listener = inst.getListener();
            FlowConditionVariable conditionVariable = null;
            try {
                conditionVariable = listener.onFlowConditionVariableRender(ctx, variable, extra);
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
            FlowModuleListener listener = inst.getListener();
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
    public void onScanQRCode(FlowCase flowCase, QRCodeDTO qrCode, Long currentUserId) {
        FlowModuleInst inst = moduleMap.get(flowCase.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getListener();
            listener.onScanQRCode(flowCase, qrCode, currentUserId);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        for (FlowModuleListener listener : allListeners) {
            try {
                FlowModuleInfo info = listener.initModule();
                FlowModuleInst inst = new FlowModuleInst();
                inst.setInfo(info);
                inst.setListener(listener);
                moduleMap.put(info.getModuleId(), inst);//TODO support TreeSet for listeners ?
            } catch (Exception ex) {
                LOGGER.error("module init error cls=" + listener.getClass(), ex);
            }
        }
    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
        FlowModuleInst inst = moduleMap.get(flowCase.getModuleId());
        if (inst != null) {
            FlowModuleListener listener = inst.getListener();
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
            FlowModuleListener listener = inst.getListener();
            try {
                listener.onFlowCaseCreated(flowCase);
            } catch (Exception e) {
                wrapError(e, listener);
            }
        }
    }

    private void wrapError(Exception e, FlowModuleListener listener) {
        if (e instanceof RuntimeErrorException) {
            throw ((RuntimeErrorException) e);
        }
        throw RuntimeErrorException.errorWith(e, ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                "Flow module listener error, listener=%s, cause=%s", listener.getClass().getSimpleName(), e);
    }
}
