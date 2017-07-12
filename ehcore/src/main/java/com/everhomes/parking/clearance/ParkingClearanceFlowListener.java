// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.parking.ParkingLocalStringCode;
import com.everhomes.rest.parking.clearance.ParkingClearanceLogStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.everhomes.rest.parking.clearance.ParkingClearanceConst.MODULE_ID;
import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * Created by xq.tian on 2016/12/23.
 */
@Component
public class ParkingClearanceFlowListener implements FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingClearanceFlowListener.class);

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ParkingClearanceLogProvider clearanceLogProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private ParkingProvider parkingProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(MODULE_ID);
        if (module != null) {
            moduleInfo.setModuleName(module.getName());
            moduleInfo.setModuleId(MODULE_ID);
            return moduleInfo;
        }
        return null;
    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        Long logId = ctx.getFlowCase().getReferId();
        coordinationProvider.getNamedLock(CoordinationLocks.PARKING_CLEARANCE_LOG.getCode() + logId).enter(() -> {
            ParkingClearanceLog log = clearanceLogProvider.findById(logId);
            if (ParkingClearanceLogStatus.fromCode(log.getStatus()) != ParkingClearanceLogStatus.COMPLETED) {
                log.setStatus(ParkingClearanceLogStatus.CANCELLED.getCode());
                clearanceLogProvider.updateClearanceLog(log);
            }
            return null;
        });
    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
        String params = ctx.getCurrentNode().getFlowNode().getParams();
        Map map = (Map)StringHelper.fromJsonString(params, HashMap.class);

        if (map != null) {
            String nodeConfigStatus = String.valueOf(map.get("status"));
            ParkingClearanceLogStatus status = ParkingClearanceLogStatus.fromName(nodeConfigStatus);
            if (status == null) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("ParkingClearanceLogStatus config error, nodeConfig = {}", params);
                }
                return;
            }
            Long logId = ctx.getFlowCase().getReferId();
            coordinationProvider.getNamedLock(CoordinationLocks.PARKING_CLEARANCE_LOG.getCode() + logId).enter(() -> {
                ParkingClearanceLog log = clearanceLogProvider.findById(logId);
                if (log == null) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("can not find clearance log, id = {}", logId);
                    }
                    return null;
                }
                if (ParkingClearanceLogStatus.fromCode(log.getStatus()) != status) {
                    log.setStatus(status.getCode());
                    clearanceLogProvider.updateClearanceLog(log);
                }
                return null;
            });
        }
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        Long logId = ctx.getFlowCase().getReferId();
        coordinationProvider.getNamedLock(CoordinationLocks.PARKING_CLEARANCE_LOG.getCode() + logId).enter(() -> {
            ParkingClearanceLog log = clearanceLogProvider.findById(logId);
            if (log == null) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("can not find clearance log, id = {}", logId);
                }
                return null;
            }
            if (ParkingClearanceLogStatus.fromCode(log.getStatus()) != ParkingClearanceLogStatus.COMPLETED) {
                log.setStatus(ParkingClearanceLogStatus.COMPLETED.getCode());
                clearanceLogProvider.updateClearanceLog(log);
            }
            return null;
        });
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase) {
        return flowCase.getContent();
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {
        String params = ctx.getCurrentNode().getFlowNode().getParams();
        Map map = (Map)StringHelper.fromJsonString(params, HashMap.class);

        if (map != null && ctx.getStepType() == FlowStepType.APPROVE_STEP) {
            String nodeConfigStatus = String.valueOf(map.get("status"));
            ParkingClearanceLogStatus status = ParkingClearanceLogStatus.fromName(nodeConfigStatus);
            if (status == null) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("ParkingClearanceLogStatus config error, nodeConfig = {}", params);
                }
                return;
            }
            Long logId = ctx.getFlowCase().getReferId();
            coordinationProvider.getNamedLock(CoordinationLocks.PARKING_CLEARANCE_LOG.getCode() + logId).enter(() -> {
                ParkingClearanceLog log = clearanceLogProvider.findById(logId);
                if (log == null) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("can not find clearance log, id = {}", logId);
                    }
                    return null;
                }
                ParkingClearanceLogStatus logStatus = ParkingClearanceLogStatus.fromCode(log.getStatus());
                if (logStatus != status) {
                    log.setStatus(status.getCode());
                    clearanceLogProvider.updateClearanceLog(log);
                }
                return null;
            });
        }
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        ParkingClearanceLog log = clearanceLogProvider.findById(flowCase.getReferId());

        User applicant = this.findUserById(log.getApplicantId());
        UserIdentifier userIdentifier = this.findUserIdentifierByUserId(applicant.getId());
        ParkingLot parkingLot = parkingProvider.findParkingLotById(log.getParkingLotId());

        Map<String, Object> map = new HashMap<>();
        String detailJson;

        map.put("parkingLotName", defaultIfNull(parkingLot.getName(), ""));
        map.put("plateNumber", defaultIfNull(log.getPlateNumber(), ""));
        String dateStr = DateHelper.getDateDisplayString(TimeZone.getDefault(), log.getClearanceTime().getTime(), "yyyy-MM-dd");
        map.put("clearanceTime", defaultIfNull(dateStr, ""));
        // 如果remarks为空，显示 "无"
        if (log.getRemarks() == null) {
            String remarksNoneValue = localeStringService.getLocalizedString(ParkingLocalStringCode.SCOPE_STRING,
                    ParkingLocalStringCode.NONE_CODE, currLocale(), "");
            map.put("remarks", defaultIfNull(remarksNoneValue, ""));
        } else {
            map.put("remarks", log.getRemarks());
        }

        if (flowUserType == FlowUserType.APPLIER) {
            detailJson = localeTemplateService.getLocaleTemplateString(ParkingLocalStringCode.SCOPE_TEMPLATE,
                    ParkingLocalStringCode.CLEARANCE_FLOW_CASE_DETAIL_CONTENT_APPLICANT, currLocale(), map, "");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The applicant detail json is: {}", detailJson);
            }
        }
        // 处理人员可以看到申请人的相关信息
        else {
            map.put("applicant", defaultIfNull(applicant.getNickName(), ""));
            map.put("identifierToken", defaultIfNull(userIdentifier.getIdentifierToken(), ""));
            detailJson = localeTemplateService.getLocaleTemplateString(ParkingLocalStringCode.SCOPE_TEMPLATE,
                    ParkingLocalStringCode.CLEARANCE_FLOW_CASE_DETAIL_CONTENT_PROCESSOR, currLocale(), map, "");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The processor detail json is: {}", detailJson);
            }
        }
        flowCase.setCustomObject(this.buildCustomObjectStr(map, log.getCreateTime().getTime()));
        return (FlowCaseEntityList) StringHelper.fromJsonString(detailJson, FlowCaseEntityList.class);
    }

    private String buildCustomObjectStr(Map<String, Object> map, Long createTime) {
        String dateStr = DateHelper.getDateDisplayString(TimeZone.getDefault(), createTime, "yyyy-MM-dd");
        map.put("createTime", dateStr);
        return StringHelper.toJsonString(map);
    }

    private Object defaultIfNull(Object obj, Object defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    @Override
    public void onFlowCreating(Flow flow) {
        // Added by Janson
    }

    private UserIdentifier findUserIdentifierByUserId(Long userId) {
        UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        if (identifier != null) {
            return identifier;
        }
        LOGGER.error("User identifier is not exist, userId = {}", userId);
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User identifier is not exist userId = %s", userId);
    }

    private User findUserById(Long userId) {
        User user = userProvider.findUserById(userId);
        if (user != null) {
            return user;
        }
        LOGGER.error("User is not exist, userId = {}", userId);
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User is not exist userId = %s", userId);
    }

    private String currLocale() {
        return UserContext.current().getUser().getLocale();
    }

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
		// TODO Auto-generated method stub
		
	}
}
