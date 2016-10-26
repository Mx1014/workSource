package com.everhomes.energy;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.energy.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.SCOPE;
import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * 能耗管理service
 * Created by xq.tian on 2016/10/25.
 */
@Service
public class EnergyConsumptionServiceImpl implements EnergyConsumptionService {

    private final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private EnergyMeterProvider energyMeterProvider;

    @Autowired
    private MeterPropertyProvider meterPropertyProvider;

    @Autowired
    private MeterCategoryProvider meterCategoryProvider;

    @Autowired
    private MeterFormulaProvider meterFormulaProvider;

    @Autowired
    private MeterChangeRecordProvider meterChangeRecordProvider;

    @Autowired
    private MeterReadingRecordProvider meterReadingRecordProvider;

    @Autowired
    private MeterDefaultPropertyProvider meterDefaultPropertyProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public EnergyMeterDTO createEnergyMeter(CreateEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        Meter meter = ConvertHelper.convert(cmd, Meter.class);
        meter.setStatus(EnergyMeterStatus.ACTIVE.getCode());
        energyMeterProvider.createMeter(meter);
        return ConvertHelper.convert(meter, EnergyMeterDTO.class);
    }

    @Override
    public EnergyMeterDTO updateEnergyMeter(UpdateEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        Meter meter = this.findMeterById(cmd.getMeterId());
        if (cmd.getName() != null) {
            meter.setName(cmd.getName());
        }
        if (cmd.getMeterNumber() != null) {
            meter.setNumber(cmd.getMeterNumber());
        }
        if (cmd.getPrice() != null) {
            meter.setPrice(cmd.getPrice());
            this.insertMeterProperty(EnergyMeterSettingType.PRICE, cmd);
        }
        return null;
    }

    private void insertMeterProperty(EnergyMeterSettingType propertyType, UpdateEnergyMeterCommand cmd) {
        MeterProperty property = new MeterProperty();
        // property.setStatus();
    }

    @Override
    public void changeEnergyMeter(ChangeEnergyMeterCommand cmd) {

    }

    @Override
    public SearchEnergyMeterResponse searchEnergyMeter(SearchEnergyMeterCommand cmd) {
        return null;
    }

    @Override
    public void updateEnergyMeterStatus(UpdateEnergyMeterStatusCommand cmd) {

    }

    @Override
    public EnergyMeterReadingLogDTO readEnergyMeter(ReadEnergyMeterCommand cmd) {
        return null;
    }

    @Override
    public void batchUpdateEnergyMeterSettings(BatchUpdateEnergyMeterSettingsCommand cmd) {

    }

    @Override
    public SearchEnergyMeterReadingLogsResponse searchEnergyMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd) {
        return null;
    }

    @Override
    public void deleteEnergyMeterReadingLog(DeleteEnergyMeterReadingLogCommand cmd) {

    }

    @Override
    public EnergyMeterSettingLogDTO updateEnergyMeterDefaultSetting(UpdateEnergyMeterDefaultSettingCommand cmd) {
        return null;
    }

    @Override
    public EnergyMeterFormulaDTO createEnergyMeterFormula(CreateEnergyMeterFormulaCommand cmd) {
        return null;
    }

    @Override
    public EnergyMeterCategoryDTO createEnergyMeterCategory(CreateEnergyMeterCategoryCommand cmd) {
        return null;
    }

    @Override
    public EnergyMeterCategoryDTO updateEnergyMeterCategory(UpdateEnergyMeterCategoryCommand cmd) {
        return null;
    }

    @Override
    public void deleteEnergyMeterCategory(DeleteEnergyMeterCategoryCommand cmd) {

    }

    @Override
    public void importEnergyMeter(ImportEnergyMeterCommand cmd, MultipartFile file) {

    }

    @Override
    public EnergyStatDTO getEnergyStatByDay(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public EnergyStatDTO getEnergyStatByMonth(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyStatByYearDTO> getEnergyStatisticByYear(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyStatByYearDTO> getEnergyStatisticByYoy(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyMeterChangeLogDTO> listEnergyMeterChangeLogs(ListMeterChangeLogCommand cmd) {

        return null;
    }

    @Override
    public List<EnergyMeterFormulaDTO> listEnergyMeterFormulas(ListEnergyFormulasCommand cmd) {
        return null;
    }

    @Override
    public EnergyMeterDefaultSettingDTO listEnergyDefaultSettings(ListEnergyDefaultSettingsCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyFormulaVariableDTO> listEnergyFormulaVariables() {
        return null;
    }

    @Override
    public List<EnergyMeterCategoryDTO> listEnergyMeterCategories(ListMeterCategoriesCommand cmd) {
        return null;
    }

    private void checkCurrentUserNotInOrg(Long orgId) {
        if (orgId == null) {
            LOGGER.error("Invalid parameter organizationId [ null ]");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter organizationId [ null ]");
        }
        Long userId = UserContext.current().getUser().getId();
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
        if(member == null){
            LOGGER.error("User is not in the organization.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is not in the organization.");
        }
    }

    private void invalidParameterException(String name, Object param) {
        LOGGER.error("Invalid parameter {} [ {} ].", name, param);
        throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "Invalid parameter %s [ %s ].", name, param);
    }

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o) {
        Set<ConstraintViolation<Object>> result = validator.validate(o);
        if (!result.isEmpty()) {
            result.stream().map(r -> r.getPropertyPath().toString() + " [ " + r.getInvalidValue() + " ]")
                    .reduce((i, a) -> i + ", " + a).ifPresent(r -> {
                LOGGER.error("Invalid parameter {}", r);
                throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter %s", r);
            });
        }
    }

    private Integer currentNamespaceId() {
        return UserContext.getCurrentNamespaceId();
    }

    private Meter findMeterById(Long id) {
        Meter meter = energyMeterProvider.findById(currentNamespaceId(), id);
        if (meter != null) {
            return meter;
        }
        LOGGER.error("Meter not exist, id = {}", id);
        throw errorWith(SCOPE, ERR_METER_NOT_EXIST, "The meter is not exist id = %s", id);
    }
}
