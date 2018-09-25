package com.everhomes.energy;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.mail.MailHandler;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.ExecuteImportTaskCallback;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationJobPositionMap;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.pmNotify.PmNotifyRecord;
import com.everhomes.pmNotify.PmNotifyService;
import com.everhomes.portal.PortalService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.MeterFormulaVariable;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.energy.BatchReadEnergyMeterCommand;
import com.everhomes.rest.energy.BatchUpdateEnergyMeterSettingsCommand;
import com.everhomes.rest.energy.BillStatDTO;
import com.everhomes.rest.energy.ChangeEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterDefaultSettingCommand;
import com.everhomes.rest.energy.CreateEnergyMeterFormulaCommand;
import com.everhomes.rest.energy.CreateEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.CreateEnergyTaskCommand;
import com.everhomes.rest.energy.DayStatDTO;
import com.everhomes.rest.energy.DelelteEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterFormulaCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterReadingLogCommand;
import com.everhomes.rest.energy.DeleteEnergyPlanCommand;
import com.everhomes.rest.energy.EnergyAutoReadingFlag;
import com.everhomes.rest.energy.EnergyCategoryDefault;
import com.everhomes.rest.energy.EnergyCategoryType;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.rest.energy.EnergyCommunityYoyStatDTO;
import com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode;
import com.everhomes.rest.energy.EnergyFormulaType;
import com.everhomes.rest.energy.EnergyFormulaVariableDTO;
import com.everhomes.rest.energy.EnergyLocalStringCode;
import com.everhomes.rest.energy.EnergyMeterAddressDTO;
import com.everhomes.rest.energy.EnergyMeterCategoryDTO;
import com.everhomes.rest.energy.EnergyMeterChangeLogDTO;
import com.everhomes.rest.energy.EnergyMeterDTO;
import com.everhomes.rest.energy.EnergyMeterDefaultSettingDTO;
import com.everhomes.rest.energy.EnergyMeterDefaultSettingTemplateDTO;
import com.everhomes.rest.energy.EnergyMeterFormulaDTO;
import com.everhomes.rest.energy.EnergyMeterPriceConfigDTO;
import com.everhomes.rest.energy.EnergyMeterPriceConfigExpressionDTO;
import com.everhomes.rest.energy.EnergyMeterPriceDTO;
import com.everhomes.rest.energy.EnergyMeterReadingLogDTO;
import com.everhomes.rest.energy.EnergyMeterSettingLogDTO;
import com.everhomes.rest.energy.EnergyMeterSettingType;
import com.everhomes.rest.energy.EnergyMeterStatus;
import com.everhomes.rest.energy.EnergyMeterTaskDTO;
import com.everhomes.rest.energy.EnergyMeterType;
import com.everhomes.rest.energy.EnergyPlanDTO;
import com.everhomes.rest.energy.EnergyPlanGroupDTO;
import com.everhomes.rest.energy.EnergyPlanMeterDTO;
import com.everhomes.rest.energy.EnergyStatByYearDTO;
import com.everhomes.rest.energy.EnergyStatCommand;
import com.everhomes.rest.energy.EnergyStatDTO;
import com.everhomes.rest.energy.EnergyStatisticType;
import com.everhomes.rest.energy.EnergyTaskStatus;
import com.everhomes.rest.energy.ExportEnergyMeterQRCodeCommand;
import com.everhomes.rest.energy.FindEnergyMeterByQRCodeCommand;
import com.everhomes.rest.energy.FindEnergyPlanDetailsCommand;
import com.everhomes.rest.energy.GetEnergyMeterCommand;
import com.everhomes.rest.energy.GetEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.GetEnergyMeterQRCodeCommand;
import com.everhomes.rest.energy.ImportEnergyMeterCommand;
import com.everhomes.rest.energy.ImportEnergyMeterDataDTO;
import com.everhomes.rest.energy.ImportTasksByEnergyPlanCommand;
import com.everhomes.rest.energy.ImportTasksByEnergyPlanDataDTO;
import com.everhomes.rest.energy.ListEnergyDefaultSettingsCommand;
import com.everhomes.rest.energy.ListEnergyMeterCategoriesCommand;
import com.everhomes.rest.energy.ListEnergyMeterChangeLogsCommand;
import com.everhomes.rest.energy.ListEnergyMeterFormulasCommand;
import com.everhomes.rest.energy.ListEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.ListEnergyMeterSettingLogsCommand;
import com.everhomes.rest.energy.ListEnergyPlanMetersCommand;
import com.everhomes.rest.energy.ListEnergyPlanMetersResponse;
import com.everhomes.rest.energy.ListUserEnergyPlanTasksCommand;
import com.everhomes.rest.energy.ListUserEnergyPlanTasksResponse;
import com.everhomes.rest.energy.MeterStatDTO;
import com.everhomes.rest.energy.PriceCalculationType;
import com.everhomes.rest.energy.RangeBoundaryType;
import com.everhomes.rest.energy.ReadEnergyMeterCommand;
import com.everhomes.rest.energy.ReadTaskMeterCommand;
import com.everhomes.rest.energy.ReadTaskMeterOfflineCommand;
import com.everhomes.rest.energy.ReadTaskMeterOfflineResponse;
import com.everhomes.rest.energy.ReadTaskMeterOfflineResultLog;
import com.everhomes.rest.energy.SearchEnergyMeterCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsResponse;
import com.everhomes.rest.energy.SearchEnergyMeterResponse;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanCommand;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanResponse;
import com.everhomes.rest.energy.ServiceStatDTO;
import com.everhomes.rest.energy.SetEnergyPlanMeterOrderCommand;
import com.everhomes.rest.energy.SyncOfflineDataCommand;
import com.everhomes.rest.energy.SyncOfflineDataResponse;
import com.everhomes.rest.energy.UpdateEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterDefaultSettingCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterStatusCommand;
import com.everhomes.rest.energy.UpdateEnergyPlanCommand;
import com.everhomes.rest.equipment.ExecuteGroupAndPosition;
import com.everhomes.rest.equipment.StandardRepeatType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.ListOrganizationContactByJobPositionIdCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.pmNotify.PmNotifyMode;
import com.everhomes.rest.pmNotify.PmNotifyReceiver;
import com.everhomes.rest.pmNotify.PmNotifyReceiverList;
import com.everhomes.rest.pmNotify.PmNotifyReceiverType;
import com.everhomes.rest.pmNotify.PmNotifyType;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityByUserResponse;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityCommand;
import com.everhomes.rest.pmtask.PmTaskCheckPrivilegeFlag;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.repeat.RepeatServiceErrorCode;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.scheduler.EnergyAutoReadingJob;
import com.everhomes.scheduler.EnergyTaskScheduleJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.EnergyMeterReadingLogSearcher;
import com.everhomes.search.EnergyMeterSearcher;
import com.everhomes.search.EnergyMeterTaskSearcher;
import com.everhomes.search.EnergyPlanSearcher;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterCategoryMap;
import com.everhomes.techpark.rental.RentalServiceImpl;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.QRCodeConfig;
import com.everhomes.util.QRCodeEncoder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.doc.DocUtil;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.SAXHandlerEventUserModel;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERROR_METER_NAME_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERROR_METER_NUMBER_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_CURR_READING_GREATER_THEN_MAX_READING;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_FORMULA_HAS_BEEN_REFERENCE;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_CATEGORY_HAS_BEEN_REFERENCE;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_CATEGORY_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_NOT_EXIST_TASK;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_READING_LOG_BEFORE_TODAY;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_READING_LOG_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_SETTING_END_TIME_ERROR;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_SETTING_START_TIME_ERROR;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_START_GREATER_THEN_MAX;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_TASK_ALREADY_CLOSE;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_TASK_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_PRICE_CONFIG_HAS_BEEN_REFERENCE;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.SCOPE;
import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * 能耗管理service
 * Created by xq.tian on 2016/10/25.
 */
@Service
public class EnergyConsumptionServiceImpl implements EnergyConsumptionService, ApplicationListener<ContextRefreshedEvent> {

    final String downloadDir ="\\download\\";

    private final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static ThreadLocal<SimpleDateFormat> yearSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy");
        }
    };
    private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };

    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter autoDateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private CommunityProvider communityProvider;

    @Value("${equipment.ip}")
    private String equipmentIp;

    @Autowired
    private EnergyYoyStatisticProvider energyYoyStatisticProvider;

    @Autowired
    private EnergyDateStatisticProvider energyDateStatisticProvider;

    @Autowired
    private EnergyCountStatisticProvider energyCountStatisticProvider;

    @Autowired
    private EnergyMonthStatisticProvider energyMonthStatisticProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private EnergyMeterChangeLogProvider meterChangeLogProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private LocaleStringProvider localeStringProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private EnergyMeterProvider meterProvider;

    @Autowired
    private EnergyMeterSettingLogProvider meterSettingLogProvider;

    @Autowired
    private EnergyMeterFormulaProvider meterFormulaProvider;

    @Autowired
    private EnergyMeterCategoryProvider meterCategoryProvider;

    @Autowired
    private EnergyMeterReadingLogProvider meterReadingLogProvider;

    @Autowired
    private EnergyMeterSearcher meterSearcher;

    @Autowired
    private EnergyMeterReadingLogSearcher readingLogSearcher;

    @Autowired
    private EnergyMeterDefaultSettingProvider defaultSettingProvider;

    @Autowired
    private EnergyMeterFormulaVariableProvider meterFormulaVariableProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private EnergyMeterPriceConfigProvider priceConfigProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private ServiceModuleService serviceModuleService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private EnergyMeterAddressProvider energyMeterAddressProvider;

    @Autowired
    private EnergyPlanProvider energyPlanProvider;

    @Autowired
    private RepeatService repeatService;

    @Autowired
    private EnergyPlanSearcher energyPlanSearcher;

    @Autowired
    private EnergyMeterTaskSearcher energyMeterTaskSearcher;

    @Autowired
    private EnergyMeterTaskProvider energyMeterTaskProvider;

    @Autowired
    private PmNotifyService pmNotifyService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private BuildingProvider buildingProvider;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private EnergyMeterCategoryMapProvider energyMeterCategoryMapProvider;

    @Autowired
    private EnergyMeterFormulaMapProvider energyMeterFormulaMapProvider;

    @Autowired
    private EnergyMeterReadingLogProvider energyMeterReadingLogProvider;

    static final String TASK_EXECUTE = "energyTask.isexecute";
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Autowired
    private PortalService portalService;

    private void checkEnergyAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
//        ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
//        cmd.setNamespaceId(namespaceId);
//        cmd.setModuleId(ServiceModuleConstants.ENERGY_MODULE);
//        cmd.setActionType(ActionType.OFFICIAL_URL.getCode());
//        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(cmd);
//        Long appId = apps.getServiceModuleApps().get(0).getOriginId();
//        if(!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), orgId,
//                orgId, privilegeId, appId, null, communityId)) {
//            LOGGER.error("Permission is prohibited, namespaceId={}, orgId={}, ownerType={}, ownerId={}, privilegeId={}",
//                    namespaceId, orgId, EntityType.COMMUNITY.getCode(), communityId, privilegeId);
//            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
//                    "check user privilege error");
//        }

        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.ENERGY_MODULE, ActionType.OFFICIAL_URL.getCode(), null, orgId, communityId);
    }
    
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }

	
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void init() {
        String cronExpression = configurationProvider.getValue(ConfigConstants.SCHEDULE_EQUIPMENT_TASK_TIME, "0 0 0 * * ? ");
        String energyTaskTriggerName = "EnergyTask " + System.currentTimeMillis();
        String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
        LOGGER.info("================================================energyTaskServer: " + taskServer + ", equipmentIp: " + equipmentIp);
        if (taskServer.equals(equipmentIp)) {
            if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                scheduleProvider.scheduleCronJob(energyTaskTriggerName, energyTaskTriggerName,
                        cronExpression, EnergyTaskScheduleJob.class, null);

                String autoReading = "EnergyAutoReading " + System.currentTimeMillis();
                scheduleProvider.scheduleCronJob(autoReading, autoReading,
                        "0 10 5 L * ?", EnergyAutoReadingJob.class, null);
            }
        }
    }
    
    private void checkEnergyMeterUnique(Long id, Long communityId, String meterNumber, String meterName) {
        EnergyMeter meter = meterProvider.findByName(communityId, meterName);
        if(meter != null && !meter.getId().equals(id)) {
            LOGGER.error("meterName: {} in community: {} already exist!", meterName, communityId);
            throw RuntimeErrorException.errorWith(SCOPE, ERROR_METER_NAME_EXIST,
                    "meterName is already exist");
        }

        meter = meterProvider.findByNumber(communityId, meterNumber);
        if(meter != null && !meter.getId().equals(id)) {
            LOGGER.error("meterNumber: {} in community: {} already exist!", meterNumber, communityId);
            throw RuntimeErrorException.errorWith(SCOPE, ERROR_METER_NUMBER_EXIST,
                    "meterNumber is already exist");
        }
    }
    @Override
    public EnergyMeterDTO createEnergyMeter(CreateEnergyMeterCommand cmd) {
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_CREATE, cmd.getOwnerId(), cmd.getCommunityId());
        checkEnergyMeterUnique(null, cmd.getCommunityId(), cmd.getMeterNumber(), cmd.getName());

        EnergyMeterType meterType = EnergyMeterType.fromCode(cmd.getMeterType());
        if (meterType == null) {
            invalidParameterException("meterType", cmd.getMeterType());
        }
        EnergyMeterCategory category = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getBillCategoryId());
        if (category == null) {
            LOGGER.error("The energy meter category is not exist, id = {}", cmd.getBillCategoryId());
            throw errorWith(SCOPE, ERR_METER_CATEGORY_NOT_EXIST, "The energy meter category is not exist, id = %s", cmd.getBillCategoryId());
        }
        category = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getServiceCategoryId());
        if (category == null) {
            LOGGER.error("The energy meter category is not exist, id = {}", cmd.getBillCategoryId());
            throw errorWith(SCOPE, ERR_METER_CATEGORY_NOT_EXIST, "The energy meter category is not exist, id = %s", cmd.getBillCategoryId());
        }
//        EnergyMeterFormula formula = meterFormulaProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getAmountFormulaId());
//        if (formula == null) {
//            LOGGER.error("The energy meter formula is not exist, id = {}", cmd.getAmountFormulaId());
//            throw errorWith(SCOPE, ERR_METER_FORMULA_NOT_EXIST, "The energy meter formula is not exist, id = %s", cmd.getAmountFormulaId());
//        }
//        if(cmd.getCostFormulaSource() == null || cmd.getCostFormulaSource() == 0) {
//            formula = meterFormulaProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCostFormulaId());
//            if (formula == null) {
//                LOGGER.error("The energy meter formula is not exist, id = {}", cmd.getCostFormulaId());
//                throw errorWith(SCOPE, ERR_METER_FORMULA_NOT_EXIST, "The energy meter formula is not exist, id = %s", cmd.getCostFormulaId());
//            }
//        }

        if (cmd.getStartReading().doubleValue() > cmd.getMaxReading().doubleValue()) {
            LOGGER.error("The energy meter start reading is greater then max reading");
            throw errorWith(SCOPE, ERR_METER_START_GREATER_THEN_MAX, "The energy meter start reading is greater then max reading");
        }

        EnergyMeter meter = ConvertHelper.convert(cmd, EnergyMeter.class);
        meter.setStatus(EnergyMeterStatus.ACTIVE.getCode());
        meter.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));

        dbProvider.execute(r -> {
            meterProvider.createEnergyMeter(meter);

            processEnergyMeterAddresses(meter.getId(), cmd.getAddresses());
            // 创建setting log 记录
            UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
            updateCmd.setPrice(cmd.getPrice());
            updateCmd.setRate(cmd.getRate());
            updateCmd.setCostFormulaId(cmd.getCostFormulaId());
            updateCmd.setCostFormulaSource(cmd.getCostFormulaSource());
            updateCmd.setAmountFormulaId(cmd.getAmountFormulaId());
            updateCmd.setStartTime(Date.valueOf(LocalDate.now()).getTime());
            updateCmd.setMeterId(meter.getId());
            updateCmd.setCalculationType(cmd.getCalculationType());
            updateCmd.setConfigId(cmd.getConfigId());
            updateCmd.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
            //this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd, meter.getCommunityId());
            this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd, meter.getCommunityId());
           // this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd, meter.getCommunityId());
           // this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd, meter.getCommunityId());

            // 创建一条初始读表记录
            ReadEnergyMeterCommand readEnergyMeterCmd = new ReadEnergyMeterCommand();
            readEnergyMeterCmd.setCommunityId(cmd.getCommunityId());
            readEnergyMeterCmd.setCurrReading(meter.getStartReading());
            readEnergyMeterCmd.setMeterId(meter.getId());
            readEnergyMeterCmd.setOrganizationId(cmd.getOwnerId());
            readEnergyMeterCmd.setResetMeterFlag(TrueOrFalseFlag.FALSE.getCode());
            readEnergyMeterCmd.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
            this.readEnergyMeter(readEnergyMeterCmd);
            return true;
        });
        meterSearcher.feedDoc(meter);
        if (cmd.getNamespaceId() == 999961 && EnergyAutoReadingFlag.TURE.equals(EnergyAutoReadingFlag.fromStatus(cmd.getAutoFlag()))) {
            createMeterTask(meter);
        }
        return toEnergyMeterDTO(meter, cmd.getNamespaceId());
    }

    private void createMeterTask(EnergyMeter meter) {
        //task targetType null   planId 0
        EnergyMeterTask task = new EnergyMeterTask();
        task.setNamespaceId(meter.getNamespaceId());
        task.setOwnerId(meter.getOwnerId());
        task.setOwnerType(meter.getOwnerType());
        task.setTargetId(meter.getCommunityId());
        task.setPlanId(0L);
        task.setTargetType("communityId");
        task.setMeterId(meter.getId());
        task.setLastTaskReading(meter.getStartReading() == null ? BigDecimal.ZERO :meter.getStartReading());
        LocalDateTime dateTime = meter.getCreateTime().toLocalDateTime();
        task.setExecutiveStartTime(Timestamp.valueOf(dateTime.format(dateSF)));
        String endDayString = LocalDateTime.of(dateTime.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX).format(dateSF);
        task.setExecutiveExpireTime(Timestamp.valueOf(endDayString));
        energyMeterTaskProvider.createEnergyMeterTask(task);
        energyMeterTaskSearcher.feedDoc(task);
    }

    private void processEnergyMeterAddresses(Long meterId, List<EnergyMeterAddressDTO> addresses) {
        Map<Long, EnergyMeterAddress> existAddress = energyMeterAddressProvider.findByMeterId(meterId);
        if (addresses == null) {
            if (existAddress != null) {
                existAddress.values().forEach((v) -> {
                    v.setStatus(CommonStatus.INACTIVE.getCode());
                    energyMeterAddressProvider.updateEnergyMeterAddress(v);
                });
            }
            return;
        }
        validateBurdenRate(addresses);
        //取出表记关联的所有门牌；传入的参数有id的从exist中去掉，没有id的增加，最后将exist中剩下的门牌关联关系置为inactive
        if(addresses != null && addresses.size() > 0) {
            addresses.forEach(meterAddress -> {
                if (meterAddress.getId() == null) {
                    meterAddress.setMeterId(meterId);
                    EnergyMeterAddress address = ConvertHelper.convert(meterAddress, EnergyMeterAddress.class);
                    energyMeterAddressProvider.createEnergyMeterAddress(address);
                } else {
                    EnergyMeterAddress address = ConvertHelper.convert(meterAddress, EnergyMeterAddress.class);
                    address.setStatus(CommonStatus.ACTIVE.getCode());
                    energyMeterAddressProvider.updateEnergyMeterAddress(address);
                    existAddress.remove(meterAddress.getId());
                }
            });
        }

        if(existAddress != null && existAddress.size() > 0) {
            existAddress.forEach((id, meterAddress) -> {
                meterAddress.setStatus(CommonStatus.INACTIVE.getCode());
                energyMeterAddressProvider.updateEnergyMeterAddress(meterAddress);
            });
        }
    }

    private void validateBurdenRate(List<EnergyMeterAddressDTO> addresses) {
        if (addresses != null && addresses.size() > 0) {
            BigDecimal burdenRate = new BigDecimal(0);
            burdenRate = addresses.stream().map(EnergyMeterAddressDTO::getBurdenRate).reduce( burdenRate, BigDecimal::add);
            if (burdenRate.compareTo(new BigDecimal(1)) > 0) {
                throw RuntimeErrorException.errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERROR_BURDENRATE_GT_ONE, "burden rate of this meter is greater than 1.00");
            }
        }
    }

    private List<EnergyMeterAddressDTO> populateEnergyMeterAddresses(Long meterId) {
        Map<Long, EnergyMeterAddress> existAddress = energyMeterAddressProvider.findByMeterId(meterId);
        List<EnergyMeterAddressDTO> dtos = new ArrayList<>();
        if(existAddress != null && existAddress.size() > 0) {
            existAddress.forEach((id, meterAddress) -> {
                dtos.add(ConvertHelper.convert(meterAddress, EnergyMeterAddressDTO.class));
            });
        }
        return dtos;
    }

    @Override
    public EnergyMeterDTO toEnergyMeterDTO(EnergyMeter meter, Integer namespaceId) {
        EnergyMeterDTO dto = ConvertHelper.convert(meter, EnergyMeterDTO.class);

        // 表的类型
        String meterType = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_TYPE, String.valueOf(meter.getMeterType()), currLocale(), "");
        dto.setMeterType(meterType);

        // 表的状态
        String meterStatus = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_STATUS, String.valueOf(meter.getStatus()), currLocale(), "");
        dto.setStatus(meterStatus);

        // 账单项目
        EnergyMeterCategory billCategory = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(namespaceId), meter.getBillCategoryId());
        dto.setBillCategory(billCategory != null ? billCategory.getName() : null);

        // 分类
        EnergyMeterCategory serviceCategory = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(namespaceId), meter.getServiceCategoryId());
        dto.setServiceCategory(serviceCategory != null ? serviceCategory.getName() : null);

//        // 当前价格
//        EnergyMeterSettingLog priceLog = meterSettingLogProvider.findCurrentSettingByMeterId(UserContext.getCurrentNamespaceId(namespaceId), meter.getId(), EnergyMeterSettingType.PRICE);
//        dto.setPrice(priceLog != null ? priceLog.getSettingValue() : null);
//
//        if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(priceLog.getCalculationType()))) {
//            EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(priceLog.getConfigId(), meter.getOwnerId(),
//                    meter.getOwnerType(), meter.getCommunityId(), UserContext.getCurrentNamespaceId(namespaceId));
//            if(priceConfig != null) {
//                dto.setPriceConfig(toEnergyMeterPriceConfigDTO(priceConfig));
//            }
//        }
        // 当前倍率
        EnergyMeterSettingLog rateLog = meterSettingLogProvider.findCurrentSettingByMeterId(UserContext.getCurrentNamespaceId(namespaceId), meter.getId(), EnergyMeterSettingType.RATE);
        dto.setRate(rateLog != null ? rateLog.getSettingValue() : null);
//
//        // 当前费用公式名称
//        EnergyMeterSettingLog costLog = meterSettingLogProvider.findCurrentSettingByMeterId(UserContext.getCurrentNamespaceId(namespaceId), meter.getId(), EnergyMeterSettingType.COST_FORMULA);
//        if (costLog != null) {
//            EnergyMeterFormula costFormula = meterFormulaProvider.findById(UserContext.getCurrentNamespaceId(namespaceId), costLog.getFormulaId());
//            dto.setCostFormula(toEnergyMeterFormulaDTO(costFormula));
//        }
//
//        // 当前用量公式名称
//        EnergyMeterSettingLog amountLog = meterSettingLogProvider.findCurrentSettingByMeterId(UserContext.getCurrentNamespaceId(namespaceId), meter.getId(), EnergyMeterSettingType.AMOUNT_FORMULA);
//        if (amountLog != null) {
//            EnergyMeterFormula amountFormula = meterFormulaProvider.findById(UserContext.getCurrentNamespaceId(namespaceId), amountLog.getFormulaId());
//            dto.setAmountFormula(toEnergyMeterFormulaDTO(amountFormula));
//        }

        // 抄表提示, 判断今天是否读表
        Timestamp lastReadTime = meter.getLastReadTime();
        if (lastReadTime != null && (lastReadTime.getTime() - Date.valueOf(LocalDate.now()).getTime() >= 0)) {
            dto.setTodayReadStatus(TrueOrFalseFlag.TRUE.getCode());
            // 日读表差
            dto.setDayPrompt(this.processDayPrompt(meter,namespaceId));
            // 月读表差
            dto.setMonthPrompt(this.processMonthPrompt(meter,namespaceId));
        }
        //楼栋门牌信息
        dto.setAddresses(populateEnergyMeterAddresses(meter.getId()));

        List<EnergyMeterTask> tasks = energyMeterTaskProvider.listActiveEnergyMeterTasks(meter.getId());
        if(tasks != null && tasks.size() > 0) {
            dto.setLastTaskReading(tasks.get(0).getLastTaskReading());
        }
        List<PlanMeter> maps = energyPlanProvider.listByEnergyMeter(meter.getId());
        Boolean assignFlag = false;
        if(maps != null && maps.size() > 0) {
            List<String> planNames = new ArrayList<>();
            for(PlanMeter map : maps) {
                if(repeatService.repeatSettingStillWork(map.getRepeatSettingId())) {
                    EnergyPlan plan = energyPlanProvider.findEnergyPlanById(map.getPlanId());
                    if(plan != null && CommonStatus.ACTIVE.equals(CommonStatus.fromCode(plan.getStatus()))) {
                        planNames.add(plan.getName());
                        assignFlag = true;
                    }
                }
            }
            dto.setPlanName(planNames);
        }
        dto.setAssignedPlan(assignFlag);
        return dto;
    }

    @Override
    public BigDecimal processDayPrompt(EnergyMeter meter,Integer namespaceId) {
        Timestamp lastReadTime = meter.getLastReadTime();
        EnergyMeterDefaultSetting dayPromptSetting = defaultSettingProvider.findBySettingType(UserContext.getCurrentNamespaceId(namespaceId), EnergyMeterSettingType.DAY_PROMPT);
        if (lastReadTime != null && dayPromptSetting != null && Objects.equals(dayPromptSetting.getStatus(), EnergyCommonStatus.ACTIVE.getCode())) {
            LocalDateTime lastReadDateTime = lastReadTime.toLocalDateTime();
            // lastReadingTime 前一天的开始
            Date lastReadingTimeLastDayBegin = Date.valueOf(LocalDate.of(lastReadDateTime.getYear(), lastReadDateTime.getMonthValue(), lastReadDateTime.getDayOfMonth()).minusDays(1));
            // lastReadingTime 前一天的结束, 下一天的开始
            Date lastReadingTimeLastDayEnd = Date.valueOf(lastReadDateTime.toLocalDate());

            EnergyDateStatistic statistic = energyDateStatisticProvider.findByMeterAndDate(UserContext.getCurrentNamespaceId(namespaceId), meter.getId(), lastReadingTimeLastDayBegin);
            if (statistic != null) {
                // 上次读表前一天的最后一条读表记录
                EnergyMeterReadingLog lastLastReadingLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(), null, new Timestamp(lastReadingTimeLastDayEnd.getTime()));
                // 上次读表记录
                EnergyMeterReadingLog lastReadingLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(), null, meter.getLastReadTime());

                // 读表用量差
                BigDecimal amount = new BigDecimal(0);

                // 默认初始值为表的初始值，如果有昨天之前和前天之前的读表记录则置换初始值
                BigDecimal readingAnchor = meter.getStartReading();
                BigDecimal dayCurrReading = meter.getStartReading();
                if(null != lastLastReadingLog){
                    readingAnchor = lastLastReadingLog.getReading();
                }
                if(null != lastReadingLog){
                    dayCurrReading = lastReadingLog.getReading();
                }

                // 拿出单个表当天所有的读表记录
                List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByDate(meter.getId(), new Timestamp(lastReadingTimeLastDayEnd.getTime()), meter.getLastReadTime());

                // 查看是否有换表,是否有归零
                if(meterReadingLogs != null){
                    for(EnergyMeterReadingLog log : meterReadingLogs){

                        //有归零 量程设置为最大值-锚点,锚点设置为0
                        if(log.getResetMeterFlag()!=null && TrueOrFalseFlag.TRUE.getCode() == log.getResetMeterFlag()){
                            amount = amount.add(meter.getMaxReading().subtract(readingAnchor));
                            readingAnchor = new BigDecimal(0);
                        }
                        //有换表 量程加上旧表读数-锚点,锚点重置为新读数
                        if(log.getChangeMeterFlag() != null && TrueOrFalseFlag.TRUE.getCode() == log.getChangeMeterFlag()){
                            EnergyMeterChangeLog changeLog = this.meterChangeLogProvider.getEnergyMeterChangeLogByLogId(log.getId());
                            amount = amount.add(changeLog.getOldReading().subtract(readingAnchor));
                            readingAnchor = changeLog.getNewReading();
                        }
                    }
                }
                //计算当天走了多少字 量程+昨天最后一次读数 - 锚点
                amount = amount.add(dayCurrReading.subtract(readingAnchor));

                //获取公式,计算当天的费用
                //EnergyMeterSettingLog priceSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(), meter.getId(), EnergyMeterSettingType.PRICE, meter.getLastReadTime());
                EnergyMeterSettingLog rateSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(), meter.getId(), EnergyMeterSettingType.RATE , meter.getLastReadTime());
//                EnergyMeterSettingLog amountSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(), meter.getId(), EnergyMeterSettingType.AMOUNT_FORMULA , meter.getLastReadTime());
//                if (Stream.of(rateSetting, amountSetting).anyMatch(Objects::isNull)) {
//                    return null;
//                }
                if (rateSetting == null) {
                    return null;
                }
//                EnergyMeterFormula amountFormula = meterFormulaProvider.findById(amountSetting.getNamespaceId(), amountSetting.getFormulaId());
//                if (amountFormula == null) {
//                    return null;
//                }
//                ScriptEngineManager manager = new ScriptEngineManager();
//                ScriptEngine engine = manager.getEngineByName("js");
//
//                engine.put(MeterFormulaVariable.AMOUNT.getCode(), amount);
//                engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
//                engine.put(MeterFormulaVariable.TIMES.getCode(), rateSetting.getSettingValue());

                BigDecimal realAmount;
                try {
                    realAmount = amount.multiply(rateSetting.getSettingValue());
//                    realAmount = BigDecimal.valueOf(Double.valueOf(engine.eval(amountFormula.getExpression()).toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("The energy meter error");
                    throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "The energy meter error");
                }
                if (realAmount.doubleValue() > 0 && statistic.getCurrentAmount().doubleValue() != 0) {
                    BigDecimal percent = realAmount.subtract(statistic.getCurrentAmount()).divide(statistic.getCurrentAmount(), BigDecimal.ROUND_HALF_UP);
                    if ((percent.doubleValue() * 100) >= dayPromptSetting.getSettingValue().doubleValue()) {
                        return dayPromptSetting.getSettingValue();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public  BigDecimal processMonthPrompt(EnergyMeter meter,Integer namespaceId) {
        Timestamp lastReadTime = meter.getLastReadTime();
        EnergyMeterDefaultSetting monthPromptSetting = defaultSettingProvider.findBySettingType(UserContext.getCurrentNamespaceId(namespaceId), EnergyMeterSettingType.MONTH_PROMPT);
        if (lastReadTime != null && monthPromptSetting != null && Objects.equals(monthPromptSetting.getStatus(), EnergyCommonStatus.ACTIVE.getCode())) {
            LocalDateTime lastReadDateTime = lastReadTime.toLocalDateTime();
            // lastReadingTime 前一月的开始
            Date lastReadingTimeLastMonthBegin = Date.valueOf(LocalDate.of(lastReadDateTime.getYear(), lastReadDateTime.getMonth().minus(1), 1));

            EnergyMonthStatistic statistic = energyMonthStatisticProvider.findByMeterAndDate(UserContext.getCurrentNamespaceId(namespaceId), meter.getId(), "" + lastReadDateTime.toLocalDate().getYear() + lastReadDateTime.toLocalDate().getMonthValue());
            if (statistic != null) {
                BigDecimal thisMonthAmount = energyDateStatisticProvider.getSumAmountBetweenDate(meter.getId(), lastReadingTimeLastMonthBegin, Date.valueOf(lastReadDateTime.toLocalDate().plusDays(1)));
                if (thisMonthAmount.doubleValue() > 0 && statistic.getCurrentAmount().doubleValue() != 0) {
                    BigDecimal percent = thisMonthAmount.subtract(statistic.getCurrentAmount()).divide(statistic.getCurrentAmount(), BigDecimal.ROUND_HALF_UP);
                    if ((percent.doubleValue() * 100) >= monthPromptSetting.getSettingValue().doubleValue()) {
                        return monthPromptSetting.getSettingValue();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public EnergyMeterDTO updateEnergyMeter(UpdateEnergyMeterCommand cmd) {
//        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        Tuple<EnergyMeter, Boolean> result = coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER.getCode() + cmd.getMeterId()).enter(() -> {
            EnergyMeter meter = this.findMeterById(cmd.getMeterId(),cmd.getNamespaceId());
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), meter.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_CREATE);
            checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_CREATE, cmd.getOrganizationId(),  meter.getCommunityId());
            checkEnergyMeterUnique(meter.getId(), meter.getCommunityId(), cmd.getMeterNumber(), cmd.getName());
            if (cmd.getName() != null) {
                meter.setName(cmd.getName());
            }
            if (cmd.getMeterNumber() != null) {
                meter.setMeterNumber(cmd.getMeterNumber());
            }
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), meter.getCommunityId(), meter.getOwnerId(), PrivilegeConstants.METER_CREATE);
            dbProvider.execute(r -> {
                if (cmd.getPrice() != null || cmd.getConfigId() != null) {
                    this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, cmd, meter.getCommunityId());
                }
                if (cmd.getRate() != null) {
                    this.insertMeterSettingLog(EnergyMeterSettingType.RATE, cmd, meter.getCommunityId());
                }
                if (cmd.getAmountFormulaId() != null) {
                    this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, cmd, meter.getCommunityId());
                }
                if (cmd.getCostFormulaId() != null) {
                    this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, cmd, meter.getCommunityId());
                }
                meterProvider.updateEnergyMeter(meter);
                processEnergyMeterAddresses(meter.getId(), cmd.getAddresses());
                return true;
            });
            meterSearcher.feedDoc(meter);
            return meter;
        });
        return result.second() ? toEnergyMeterDTO(result.first(),cmd.getNamespaceId()) : new EnergyMeterDTO();
    }

    private void insertMeterSettingLog(EnergyMeterSettingType settingType, UpdateEnergyMeterCommand cmd, Long communityId) {
        checkUpdateCommand(cmd);
        EnergyMeterSettingLog log = new EnergyMeterSettingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setStartTime(new Timestamp(cmd.getStartTime()));
        // Timestamp endTime = cmd.getEndTime() == null ? Timestamp.valueOf(LocalDateTime.now().plusYears(100)): new Timestamp(cmd.getEndTime());
        if (cmd.getEndTime() != null) {
            log.setEndTime(new Timestamp(cmd.getEndTime()));
        }
        log.setMeterId(cmd.getMeterId());
        log.setCommunityId(communityId);
        log.setSettingType(settingType.getCode());
        log.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        switch (settingType) {
//            case PRICE:
//                log.setCalculationType(cmd.getCalculationType());
//                if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
//                    log.setSettingValue(cmd.getPrice());
//                }
//                if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
//                    log.setConfigId(cmd.getConfigId());
//                }

//                break;
            case RATE:
                log.setSettingValue(cmd.getRate());
                break;
//            case AMOUNT_FORMULA:
//                log.setFormulaId(cmd.getAmountFormulaId());
//                break;
//            case COST_FORMULA:
//                log.setFormulaId(cmd.getCostFormulaId());
//                log.setFormulaSource(cmd.getCostFormulaSource());
//                break;
        }
        meterSettingLogProvider.createSettingLog(log);
    }

    private void checkUpdateCommand(UpdateEnergyMeterCommand cmd) {
        // 开始时间不能小于现在
        if (cmd.getStartTime() != null && cmd.getStartTime() < Date.valueOf(LocalDate.now()).getTime()) {
            LOGGER.error("Energy meter setting start time less then now.");
            throw errorWith(SCOPE, ERR_METER_SETTING_START_TIME_ERROR, "Energy meter setting start time less them now.");
        }
        // 结束时间不能小于开始时间
        if (cmd.getStartTime() != null && cmd.getEndTime() != null && cmd.getEndTime() - cmd.getStartTime() < 0) {
            LOGGER.error("Energy meter setting end time less then start time.");
            throw errorWith(SCOPE, ERR_METER_SETTING_END_TIME_ERROR, "Energy meter setting end time less them start time.");
        }
    }

    @Override
    public SearchEnergyMeterResponse searchEnergyMeter(SearchEnergyMeterCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_LIST);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_LIST, cmd.getOrganizationId(),  cmd.getCommunityId());
        return meterSearcher.queryMeters(cmd);
    }

    @Override
    public SearchEnergyMeterResponse searchSimpleEnergyMeter(SearchEnergyMeterCommand cmd) {
        validate(cmd);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_LIST, cmd.getOrganizationId(),  cmd.getCommunityId());
        return meterSearcher.querySimpleEnergyMeters(cmd);
    }

    @Override
    public void changeEnergyMeter(ChangeEnergyMeterCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_CHANGE);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_CHANGE, cmd.getOrganizationId(),  cmd.getCommunityId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId(),cmd.getNamespaceId());

        // 创建读表记录
        EnergyMeterReadingLog log = new EnergyMeterReadingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setReading(cmd.getNewReading());
        log.setCommunityId(cmd.getCommunityId());
        log.setMeterId(meter.getId());
        log.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        log.setOldMeterReading(cmd.getOldReading());
        log.setChangeMeterFlag(TrueOrFalseFlag.TRUE.getCode());
        log.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setOperatorId(UserContext.current().getUser().getId());

        // 设置表记的信息
        meter.setLastReading(cmd.getNewReading());
        meter.setMaxReading(cmd.getMaxReading());
        meter.setLastReadTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        dbProvider.execute(r -> {
            meterReadingLogProvider.createEnergyMeterReadingLog(log);
            meterProvider.updateEnergyMeter(meter);

            // 创建换表记录
            EnergyMeterChangeLog changeLog = new EnergyMeterChangeLog();
            changeLog.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
            changeLog.setMaxReading(cmd.getMaxReading());
            changeLog.setNewReading(cmd.getNewReading());
            changeLog.setOldReading(cmd.getOldReading());
            changeLog.setMeterId(cmd.getMeterId());
            changeLog.setReadingLogId(log.getId());
            changeLog.setStatus(EnergyCommonStatus.ACTIVE.getCode());
            changeLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            changeLog.setCreatorUid(UserContext.current().getUser().getId());

            meterChangeLogProvider.createEnergyMeterChangeLog(changeLog);
            return true;
        });
        // 添加读表记录的索引
        readingLogSearcher.feedDoc(log);
    }

    @Override
    public void updateEnergyMeterStatus(UpdateEnergyMeterStatusCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_INACTIVE);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_INACTIVE, cmd.getOrganizationId(),  cmd.getCommunityId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER.getCode() + cmd.getMeterId()).tryEnter(() -> {
            EnergyMeter meter = this.findMeterById(cmd.getMeterId(),cmd.getNamespaceId());
            meter.setStatus(cmd.getStatus());
            dbProvider.execute(s -> {
                // 1.更新表记状态
                meterProvider.updateEnergyMeter(meter);
                //报废表把对应的任务给置为无效
                if(EnergyMeterStatus.OBSOLETE.equals(EnergyMeterStatus.fromCode(cmd.getStatus()))) {
                    List<EnergyMeterTask> tasks = energyMeterTaskProvider.listActiveEnergyMeterTasks(meter.getId());
                    if(tasks != null && tasks.size() > 0) {
                        tasks.forEach(task -> {
                            task.setStatus(EnergyTaskStatus.INACTIVE.getCode());
                            energyMeterTaskProvider.updateEnergyMeterTask(task);
                            energyMeterTaskSearcher.deleteById(task.getId());
                        });
                    }
                }
                if (EnergyMeterStatus.fromCode(cmd.getStatus()) == EnergyMeterStatus.INACTIVE) {
                    // 2.删除表记对应的读表记录
                    List<EnergyMeterReadingLog> logs = meterReadingLogProvider.listMeterReadingLogsByMeterId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), meter.getId());
                    meterReadingLogProvider.deleteMeterReadingLogsByMeterId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), meter.getId());
                    // 3.删除读表记录的索引
                    logs.forEach(log -> readingLogSearcher.deleteById(log.getId()));
                    // 4.删除表记索引
                    meterSearcher.deleteById(meter.getId());
                } else {
                    meterSearcher.feedDoc(meter);
                }
                return true;
            });
        });
    }

    @Override
    public EnergyMeterReadingLogDTO readEnergyMeter(ReadEnergyMeterCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_READ);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_READ, cmd.getOrganizationId(),  cmd.getCommunityId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId(),cmd.getNamespaceId());

        // 读数大于最大量程
        if (cmd.getCurrReading().doubleValue() > meter.getMaxReading().doubleValue()) {
            LOGGER.error("Current reading greater then meter max reading, meterId = ", meter.getId());
            throw errorWith(SCOPE, ERR_CURR_READING_GREATER_THEN_MAX_READING, "Current reading greater then meter max reading, meterId = %s", meter.getId());
        }
        EnergyMeterReadingLog log = new EnergyMeterReadingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setTaskId(cmd.getTaskId());
        log.setReading(cmd.getCurrReading());
        log.setCommunityId(meter.getCommunityId());
        log.setMeterId(meter.getId());
        log.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        log.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setOperatorId(UserContext.currentUserId());
        log.setResetMeterFlag(cmd.getResetMeterFlag() != null ? cmd.getResetMeterFlag() : TrueOrFalseFlag.FALSE.getCode());
        dbProvider.execute(r -> {
            meterReadingLogProvider.createEnergyMeterReadingLog(log);
            meter.setLastReading(log.getReading());
            meter.setLastReadTime(Timestamp.valueOf(LocalDateTime.now()));
            meterProvider.updateEnergyMeter(meter);
            return true;
        });
        readingLogSearcher.feedDoc(log);
        meterSearcher.feedDoc(meter);
        return toEnergyMeterReadingLogDTO(log);
    }

    private EnergyMeterReadingLogDTO toEnergyMeterReadingLogDTO(EnergyMeterReadingLog log) {
        EnergyMeterReadingLogDTO dto = ConvertHelper.convert(log, EnergyMeterReadingLogDTO.class);
        User user = userProvider.findUserById(log.getOperatorId());
        dto.setOperatorName(user.getNickName());
        EnergyMeter meter = this.findMeterById(log.getMeterId(),log.getNamespaceId());
        dto.setMeterName(meter.getName());
        // 处理抄表提示
        dto.setDayPrompt(this.processDayPrompt(meter,log.getNamespaceId()));
        dto.setMonthPrompt(this.processMonthPrompt(meter,log.getNamespaceId()));
        return dto;
    }

    @Override
    public void batchUpdateEnergyMeterSettings(BatchUpdateEnergyMeterSettingsCommand cmd) {
        validate(cmd);
////        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_BATCHUPDATE);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_BATCHUPDATE, cmd.getOrganizationId(),  cmd.getCommunityId());
        if (cmd.getMeterIds() != null) {
            List<EnergyMeter> meters = meterProvider.listByIds(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getMeterIds());
            if (meters != null && meters.size() > 0) {
                meters.forEach(r -> {
                    UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
                    updateCmd.setMeterId(r.getId());
                    updateCmd.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
                    updateCmd.setOrganizationId(cmd.getOrganizationId());
//                    // 价格
//                    if (cmd.getPrice() != null || cmd.getConfigId() != null) {
//                        updateCmd.setPrice(cmd.getPrice());
//                        updateCmd.setStartTime(cmd.getPriceStart());
//                        updateCmd.setEndTime(cmd.getPriceEnd());
//                        updateCmd.setConfigId(cmd.getConfigId());
//                        updateCmd.setCalculationType(cmd.getCalculationType());
//                        this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd);
//                    }
                    // 倍率
                    if (cmd.getRate() != null) {
                        updateCmd.setRate(cmd.getRate());
                        updateCmd.setStartTime(cmd.getRateStart());
                        updateCmd.setEndTime(cmd.getRateEnd());
                        this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd, r.getCommunityId());
                        r.setRate(cmd.getRate());
                    }
//                    // 费用
//                    if (cmd.getCostFormulaId() != null) {
//                        updateCmd.setCostFormulaId(cmd.getCostFormulaId());
//                        updateCmd.setCostFormulaSource(cmd.getCostFormulaSource());
//                        updateCmd.setStartTime(cmd.getCostFormulaStart());
//                        updateCmd.setEndTime(cmd.getCostFormulaEnd());
//                        this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd);
//                    }
                    // 用量
                    if (cmd.getAmountFormulaId() != null) {
                        updateCmd.setAmountFormulaId(cmd.getAmountFormulaId());
                        updateCmd.setStartTime(cmd.getAmountFormulaStart());
                        updateCmd.setEndTime(cmd.getAmountFormulaEnd());
                        this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd, r.getCommunityId());
                        r.setAmountFormulaId(cmd.getAmountFormulaId());
                    }
                    meterProvider.updateEnergyMeter(r);
                });
            }
        }
    }

    @Override
    public SearchEnergyMeterReadingLogsResponse searchEnergyMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_READING_SEARCH);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_READING_SEARCH, cmd.getOrganizationId(), cmd.getCommunityId());
        return readingLogSearcher.queryMeterReadingLogs(cmd);
    }

    @Override
    public void deleteEnergyMeterReadingLog(DeleteEnergyMeterReadingLogCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_READING_DELETE, cmd.getOrganizationId(),  cmd.getCommunityId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.METER_READING_DELETE);
        EnergyMeterReadingLog log = meterReadingLogProvider.getEnergyMeterReadingLogById(cmd.getLogId());
        if (log == null) {
            LOGGER.error("The energy meter reading log is not exist, id = {}", cmd.getLogId());
            throw errorWith(SCOPE, ERR_METER_READING_LOG_NOT_EXIST, "The energy meter reading log is not exist, id = %s", cmd.getLogId());
        }
        if (log.getOperateTime().toLocalDateTime().toLocalDate().isBefore(LocalDate.now())) {
            LOGGER.error("The energy meter reading log operate time is before today, id = {}", cmd.getLogId());
            throw errorWith(SCOPE, ERR_METER_READING_LOG_BEFORE_TODAY, "The energy meter reading log operate time is before today, id = %s", cmd.getLogId());
        }

        dbProvider.execute(r -> {
            EnergyMeterReadingLog lastReadingLog = meterReadingLogProvider.findLastReadingLogByMeterId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), log.getMeterId());
            meterReadingLogProvider.deleteEnergyMeterReadingLog(log);

            //把关联任务的读数改成新的最后一次读数
            if(lastReadingLog.getTaskId() != null && lastReadingLog.getTaskId() != 0L) {
                EnergyMeterTask task = energyMeterTaskProvider.findEnergyMeterTaskById(lastReadingLog.getTaskId());
                if(task != null) {
                    EnergyMeterReadingLog taskLastReadingLog = meterReadingLogProvider.findLastReadingLogByTaskId(task.getId());
                    if(taskLastReadingLog != null) {
                        task.setReading(taskLastReadingLog.getReading());
                    } else {
                        task.setReading(task.getLastTaskReading());
                    }

                    energyMeterTaskProvider.updateEnergyMeterTask(task);
                    energyMeterTaskSearcher.feedDoc(task);
                }

            }
            // 删除的记录是最后一条, 把表记的lastReading修改成新的最后一次读数

            if (Objects.equals(lastReadingLog.getId(), log.getId())) {
                EnergyMeter meter = meterProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), log.getMeterId());
                lastReadingLog = meterReadingLogProvider.findLastReadingLogByMeterId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), log.getMeterId());
                if (lastReadingLog != null) {
                    meter.setLastReading(lastReadingLog.getReading());
                    meter.setLastReadTime(lastReadingLog.getOperateTime());
                } else {
                    meter.setLastReading(null);
                    meter.setLastReadTime(null);
                }
                meterProvider.updateEnergyMeter(meter);

            }

            return true;
        });
        readingLogSearcher.deleteById(log.getId());
    }

    @Override
    public EnergyMeterDefaultSettingDTO updateEnergyMeterDefaultSetting(UpdateEnergyMeterDefaultSettingCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeterDefaultSetting setting = defaultSettingProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getSettingId());
        if (setting != null) {
            EnergyMeterSettingType settingType = EnergyMeterSettingType.fromCode(setting.getSettingType());
            if (settingType != null) {
                switch (settingType) {
                    case MONTH_PROMPT:
                    case DAY_PROMPT:
                        // 抄表提示类型
                        setting.setStatus(cmd.getSettingStatus() != null ? cmd.getSettingStatus() : setting.getStatus());
                        setting.setSettingValue(cmd.getSettingValue() != null ? cmd.getSettingValue() : setting.getSettingValue());
                        break;
                    case PRICE:
                        setting.setCalculationType(cmd.getCalculationType());
                        if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
                            setting.setSettingValue(cmd.getSettingValue() != null ? cmd.getSettingValue() : setting.getSettingValue());
                            setting.setConfigId(0L);
                        }
                        if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
                            setting.setSettingValue(null);
                            setting.setConfigId(cmd.getConfigId());
                        }
                    case RATE:
                        // 倍率类型
                        setting.setSettingValue(cmd.getSettingValue() != null ? cmd.getSettingValue() : setting.getSettingValue());
                        break;
                    case AMOUNT_FORMULA:
                    case COST_FORMULA:
                        // 公式类型
                        setting.setFormulaId(cmd.getFormulaId() != null ? cmd.getFormulaId() : setting.getFormulaId());
                        break;
                    default:
                        break;
                }
            }
            defaultSettingProvider.updateEnergyMeterDefaultSetting(setting);
            return toEnergyMeterDefaultSettingDTO(setting, cmd.getNamespaceId());
        }
        return null;
    }

    @Override
    public EnergyMeterFormulaDTO createEnergyMeterFormula(CreateEnergyMeterFormulaCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOwnerId());
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOwnerId(),  cmd.getCommunityId());
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        }

        // 预处理公式的表达式
        String processedExpression = this.processFormulaExpression(cmd.getExpression());
        // 检查公式的合法性
        this.checkFormulaExpressionValid(processedExpression);

        EnergyMeterFormula formula = new EnergyMeterFormula();
        formula.setOwnerId(cmd.getOwnerId());
        formula.setOwnerType(cmd.getOwnerType());
        if(cmd.getCommunityId() == null) {
            formula.setCommunityId(0L);
        }
        formula.setCommunityId(cmd.getCommunityId());

        formula.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        formula.setExpression(processedExpression);
        formula.setName(cmd.getName());
        EnergyFormulaType formulaType = EnergyFormulaType.fromCode(cmd.getFormulaType());
        if (formulaType == null) {
            invalidParameterException("formulaType", cmd.getFormulaType());
        }
        formula.setFormulaType(cmd.getFormulaType());
        formula.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        formula.setDisplayExpression(cmd.getExpression());
        meterFormulaProvider.createEnergyMeterFormula(formula);
        if(cmd.getCommunityId() == null) {
            List<CommunityDTO> communitydtos = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOwnerId());
//            List<OrganizationCommunity> communities = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
            if(communitydtos != null && communitydtos.size() > 0) {
                Long uId = UserContext.currentUserId();
                communitydtos.forEach(community -> {
                    EnergyMeterFormulaMap map = new EnergyMeterFormulaMap();
                    map.setOwnerId(cmd.getOwnerId());
                    map.setNamespaceId(cmd.getNamespaceId());
                    map.setCommunityId(community.getId());
                    map.setFomularId(formula.getId());
                    map.setCreatorUid(uId);
                    energyMeterFormulaMapProvider.createEnergyMeterFormulaMap(map);
                });
            }
        }
        return toEnergyMeterFormulaDTO(formula);
    }

    private void checkFormulaExpressionValid(String processedExpression) {
        if (processedExpression == null) {
            LOGGER.error("The energy meter formula format error");
            throw errorWith(SCOPE, ERR_METER_FORMULA_ERROR, "The energy meter error");
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        engine.put(MeterFormulaVariable.AMOUNT.getCode(), 1);
        engine.put(MeterFormulaVariable.PRICE.getCode(), 2);
        engine.put(MeterFormulaVariable.TIMES.getCode(), 3);
        engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), 4);

        try {
            engine.eval(processedExpression);
        } catch (Exception e) {
            LOGGER.error("The energy meter formula format error, expression = {}", processedExpression);
            throw errorWith(SCOPE, ERR_METER_FORMULA_ERROR, "The energy meter formula format error, expression = %s", processedExpression);
        }
    }

    private String processFormulaExpression(String inputExpression) {
        List<EnergyMeterFormulaVariable> variables = meterFormulaVariableProvider.listMeterFormulaVariables();
        String processedExpression = inputExpression;
        for (EnergyMeterFormulaVariable var : variables) {
            Pattern pattern = Pattern.compile("(\\[\\[" + var.getDisplayName() + "\\]\\])");
            Matcher matcher = pattern.matcher(processedExpression);
            while (matcher.find()) {
                int start = matcher.start(1);
                int end = matcher.end(1);
                processedExpression = new StringBuilder(processedExpression).replace(start, end, var.getName()).toString();
                matcher = pattern.matcher(processedExpression);
            }
        }
        return processedExpression;
    }

    @Override
    public EnergyMeterCategoryDTO createEnergyMeterCategory(CreateEnergyMeterCategoryCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOwnerId());
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOwnerId(),  cmd.getCommunityId());
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        }

        EnergyMeterCategory category = ConvertHelper.convert(cmd, EnergyMeterCategory.class);
        if(cmd.getCommunityId() == null) {
            category.setCommunityId(0L);
        }
        category.setDeleteFlag(TrueOrFalseFlag.TRUE.getCode());
        category.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        meterCategoryProvider.createEnergyMeterCategory(category);
        //全部里面建的自动关联各个项目 energy3.2
        if(cmd.getCommunityId() == null) {
            List<CommunityDTO> communitydtos = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOwnerId());
//            List<OrganizationCommunity> communities = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
            if(communitydtos != null && communitydtos.size() > 0) {
                Long uId = UserContext.currentUserId();
                communitydtos.forEach(community -> {
                    EnergyMeterCategoryMap map = new EnergyMeterCategoryMap();
                    map.setOwnerId(cmd.getOwnerId());
                    map.setNamespaceId(cmd.getNamespaceId());
                    map.setCommunityId(community.getId());
                    map.setCategoryId(category.getId());
                    map.setCreatorUid(uId);
                    energyMeterCategoryMapProvider.createEnergyMeterCategoryMap(map);
                });
            }
        }
        return ConvertHelper.convert(category, EnergyMeterCategoryDTO.class);
    }

    @Override
    public EnergyMeterCategoryDTO updateEnergyMeterCategory(UpdateEnergyMeterCategoryCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOrganizationId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_SETTING);
//        }
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOrganizationId(),  cmd.getCommunityId());
        Tuple<EnergyMeterCategory, Boolean> result = coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_CATEGORY.getCode() + cmd.getCategoryId()).enter(() -> {
            EnergyMeterCategory category = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCategoryId());
            if (category != null) {
                category.setName(cmd.getName());
                meterCategoryProvider.updateEnergyMeterCategory(category);
            }
            return category;
        });
        return result.second() ? ConvertHelper.convert(result.first(), EnergyMeterCategoryDTO.class) : new EnergyMeterCategoryDTO();
    }

    @Override
    public void deleteEnergyMeterCategory(DeleteEnergyMeterCategoryCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOrganizationId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_SETTING);
//        }
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOrganizationId(),  cmd.getCommunityId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_CATEGORY.getCode() + cmd.getCategoryId()).tryEnter(() -> {
            EnergyMeterCategory category = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCategoryId());

            if (category != null) {
                //项目里删全部的 实质是解除关联关系
                if((category.getCommunityId() == null || category.getCommunityId() == 0L) && cmd.getCommunityId() != null) {
                    EnergyMeter meter = meterProvider.findAnyByCategoryId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCommunityId(), category.getId());
                    if (meter != null) {
                        LOGGER.info("Energy meter category has been reference, categoryId = {}", category.getId());
                        throw errorWith(SCOPE, ERR_METER_CATEGORY_HAS_BEEN_REFERENCE, "Energy meter category has been reference");
                    }
                    EnergyMeterCategoryMap map = energyMeterCategoryMapProvider.findEnergyMeterCategoryMap(cmd.getCommunityId(), cmd.getCategoryId());
                    if(map != null) {
                        map.setStatus(EnergyCommonStatus.INACTIVE.getCode());
                        map.setOperatorUid(UserContext.currentUserId());
                        map.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        energyMeterCategoryMapProvider.updateEnergyMeterCategoryMap(map);
                    }

                } else {
                    //在全部里删全部和项目的，项目里删自己的 都是直接置inactive
                    EnergyMeter meter = meterProvider.findAnyByCategoryId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), category.getId());
                    if (meter != null) {
                        LOGGER.info("Energy meter category has been reference, categoryId = {}", category.getId());
                        throw errorWith(SCOPE, ERR_METER_CATEGORY_HAS_BEEN_REFERENCE, "Energy meter category has been reference");
                    }
                    category.setStatus(EnergyCommonStatus.INACTIVE.getCode());
                    meterCategoryProvider.updateEnergyMeterCategory(category);
                }


//                meterCategoryProvider.deleteEnergyMeterCategory(category);
            }
        });
    }

    @Override
    public ImportFileTaskDTO importEnergyMeter(ImportEnergyMeterCommand cmd, MultipartFile file, Long userId) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOwnerId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.METER_IMPORT);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.METER_IMPORT, cmd.getOwnerId(),  cmd.getCommunityId());
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());

            if(null == resultList || resultList.isEmpty()){
                LOGGER.error("File content is empty。userId="+userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(),"File content is empty"));
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOwnerId());
            task.setType(ImportFileTaskType.ENERGY_METER.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(() -> {
                ImportFileResponse response = new ImportFileResponse();
                List<ImportEnergyMeterDataDTO> datas = handleImportEnergyMeterData(resultList);
                if(datas.size() > 0){
                    //设置导出报错的结果excel的标题
                    response.setTitle(datas.get(0));
                    datas.remove(0);
                }
                List<ImportFileResultLog<ImportEnergyMeterDataDTO>> results = importEnergyMeterData(cmd, datas, userId);
                response.setTotalCount((long)datas.size());
                response.setFailCount((long)results.size());
                response.setLogs(results);
                return response;
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        LOGGER.info("task: {}",  task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);

    }



    private List<ImportEnergyMeterDataDTO> handleImportEnergyMeterData(List list){
        List<ImportEnergyMeterDataDTO> result = new ArrayList<>();
        int row = 1;
        for (Object o : list) {
            if(row < 2){
                row ++;
                continue;
            }

            RowResult r = (RowResult)o;
            ImportEnergyMeterDataDTO data = null;
            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getA())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setName(r.getA().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getB())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setMeterNumber(r.getB().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getC())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setMeterType(r.getC().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getD())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setBillCategory(r.getD().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getE())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setServiceCategory(r.getE().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getF())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setBuildingName(r.getF().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getG())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setApartmentName(r.getG().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getH())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setBurdenRate(r.getH().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getI())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setMaxReading(r.getI().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getJ())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setStartReading(r.getJ().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getK())) {
                if(data == null) {
                    data = new ImportEnergyMeterDataDTO();
                }
                data.setRate(r.getK().trim());
            }

//            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getL())) {
//                if(data == null) {
//                    data = new ImportEnergyMeterDataDTO();
//                }
//                data.setAmountFormula(r.getL().trim());
//            }

            if(data != null) {
                result.add(data);
            }
        }
        LOGGER.info("result size : " + result.size());
        return result;

    }

    private List<ImportFileResultLog<ImportEnergyMeterDataDTO>> importEnergyMeterData(ImportEnergyMeterCommand cmd, List<ImportEnergyMeterDataDTO> list, Long userId){
        List<ImportFileResultLog<ImportEnergyMeterDataDTO>> errorDataLogs = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        list.forEach(str -> {
            ImportFileResultLog<ImportEnergyMeterDataDTO> log = new ImportFileResultLog<>(EnergyConsumptionServiceErrorCode.SCOPE);
            EnergyMeter meter = new EnergyMeter();
            List<String> burdenRate = Arrays.stream(str.getBurdenRate().split("，")).filter(r-> !Objects.equals(r, "")).collect(Collectors.toList());
            List<String> apartment = Arrays.stream(str.getApartmentName().split("，")).filter(r-> !Objects.equals(r, "")).collect(Collectors.toList());
            List<String> building = Arrays.stream(str.getBuildingName().split("，")).filter(r-> !Objects.equals(r, "")).collect(Collectors.toList());

            //校验excel的比例系数不大于1
            if (!validateBurdenRateString(burdenRate)){
                LOGGER.error("energy meter number is exist, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter number is exist");
                log.setCode(EnergyConsumptionServiceErrorCode.ERROR_BURDENRATE_GT_ONE);
                errorDataLogs.add(log);
                return;
            }
            if (!validateBurdenRateWithOneAddress(apartment, burdenRate)) {
                LOGGER.error("energy meter burden rate not eq 1, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter burden rate not eq 1");
                log.setCode(EnergyConsumptionServiceErrorCode.ERROR_BURDENRATE_NO_ONE);
                errorDataLogs.add(log);
                return;
            }
            if (burdenRate!=null && building.size() > 0) {
                if(burdenRate.size() == 0){
                    LOGGER.error("energy meter burden rate is not  exist, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("energy meter burden rate  is not exist");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERROR_BURDENRATE_NOT_EXIST);
                    errorDataLogs.add(log);
                    return;
                }
                for (String burden : burdenRate) {
                    if (!NumberUtils.isNumber(burden)) {
                        LOGGER.error("energy meter burdenRate is not number, data = {}", str);
                        log.setData(str);
                        log.setErrorLog("energy meter burdenRate is not number");
                        log.setCode(EnergyConsumptionServiceErrorCode.ERROR_BURDENRATE_IS_NOT_NUMBER);
                        errorDataLogs.add(log);
                        return;
                    }
                }
            }
            if (org.apache.commons.lang.StringUtils.isBlank(str.getName())) {
                LOGGER.error("energy meter name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter name is null");
                log.setCode(EnergyConsumptionServiceErrorCode.ERROR_METER_NAME_IS_NULL);
                errorDataLogs.add(log);
                return;
            }
            EnergyMeter exist = meterProvider.findByName(cmd.getCommunityId(), str.getName());
            if (exist != null) {
                LOGGER.error("energy meter name is exist, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter name is exist");
                log.setCode(EnergyConsumptionServiceErrorCode.ERROR_METER_NAME_EXIST);
                errorDataLogs.add(log);
                return;
            }
            meter.setName(str.getName());

            if (org.apache.commons.lang.StringUtils.isBlank(str.getMeterNumber())) {
                LOGGER.error("energy meter number is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter number is null");
                log.setCode(EnergyConsumptionServiceErrorCode.ERROR_METER_NUMBER_IS_NULL);
                errorDataLogs.add(log);
                return;
            }

            EnergyMeter existNumber = meterProvider.findByNumber(cmd.getCommunityId(), str.getMeterNumber());
            if (existNumber != null) {
                LOGGER.error("energy meter number is exist, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter number is exist");
                log.setCode(EnergyConsumptionServiceErrorCode.ERROR_METER_NUMBER_EXIST);
                errorDataLogs.add(log);
                return;
            }
            meter.setMeterNumber(str.getMeterNumber());


            LocaleString meterTypeLocale = localeStringProvider.findByText(EnergyLocalStringCode.SCOPE_METER_TYPE, str.getMeterType(), currLocale());
            if (meterTypeLocale == null) {
                LOGGER.error("energy meter type is not exist, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter type is not exist");
                log.setCode(EnergyConsumptionServiceErrorCode.ERR_METER_TYPE_NOT_EXIST);
                errorDataLogs.add(log);
                return;
            }
            meter.setMeterType(Byte.valueOf(meterTypeLocale.getCode()));

            EnergyMeterCategory category = meterCategoryProvider.findByName(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCommunityId(), str.getBillCategory());
            if (category == null) {
                //历史问题  只校验了community下的类型 没有去搜所有下面的应用范围
                Map<String, EnergyMeterCategory> categoryMap = getAllScopeCategories(cmd.getCommunityId(),cmd.getOwnerId(), EnergyCategoryType.BILL.getCode());
                if (categoryMap.get(str.getBillCategory()) == null) {
                    LOGGER.error("energy meter bill category is not exist, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("energy meter bill category is not exist");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERR_BILL_CATEGORY_NOT_EXIST);
                    errorDataLogs.add(log);
                    return;
                } else {
                    category = categoryMap.get(str.getBillCategory());
                }
            }
            meter.setBillCategoryId(category.getId());

            category = meterCategoryProvider.findByName(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCommunityId(), str.getServiceCategory());
            if (category == null) {
                //历史问题  只校验了community下的类型 没有去搜所有下面的应用范围
                Map<String, EnergyMeterCategory> categoryMap = getAllScopeCategories(cmd.getCommunityId(),cmd.getOwnerId(), EnergyCategoryType.SERVICE.getCode());
                if (categoryMap.get(str.getServiceCategory()) == null) {
                    LOGGER.error("energy meter service category is not exist, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("energy meter service category is not exist");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERR_SERVICE_CATEGORY_NOT_EXIST);
                    errorDataLogs.add(log);
                    return;
                } else {
                    category = categoryMap.get(str.getServiceCategory());
                }
            }
            meter.setServiceCategoryId(category.getId());

            if (!NumberUtils.isNumber(str.getMaxReading())) {
                LOGGER.error("energy meter MaxReading is not number, data = {}", str);
                log.setData(str);
                log.setErrorLog("energy meter MaxReading is not number");
                log.setCode(EnergyConsumptionServiceErrorCode.ERR_MAX_READING_NOT_EXIST);
                errorDataLogs.add(log);
                return;
            }
            meter.setMaxReading(new BigDecimal(str.getMaxReading()));

            if (NumberUtils.isNumber(str.getStartReading())) {
                meter.setStartReading(new BigDecimal(str.getStartReading()));
            } else {
                meter.setStartReading(new BigDecimal("0"));
            }

            if (NumberUtils.isNumber(str.getRate())) {
                meter.setRate(new BigDecimal(str.getRate()));
            }

      //      EnergyMeterFormula formula = meterFormulaProvider.findByName(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCommunityId(), str.getAmountFormula());
      //      if (formula != null) {
        //        meter.setAmountFormulaId(formula.getId());
        //    }

            meter.setOwnerId(cmd.getOwnerId());
            meter.setOwnerType(cmd.getOwnerType());
            meter.setStatus(EnergyMeterStatus.ACTIVE.getCode());
            meter.setCommunityId(cmd.getCommunityId());
            meter.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));

            dbProvider.execute(r -> {
                meterProvider.createEnergyMeter(meter);

                if (!StringUtils.isEmpty(str.getBuildingName()) && !StringUtils.isEmpty(str.getApartmentName())) {
                    for (int i = 0; i < building.size(); i++) {
                        Address address = addressProvider.findApartmentAddress(meter.getNamespaceId(), meter.getCommunityId(), building.get(i), apartment.get(i));
                        if (address != null) {
                            EnergyMeterAddress ma = new EnergyMeterAddress();
                            ma.setBuildingName(address.getBuildingName());
                            ma.setApartmentName(address.getApartmentName());
                            ma.setAddressId(address.getId());
                            ma.setApartmentFloor(address.getApartmentFloor());
                            ma.setBurdenRate(new BigDecimal(burdenRate.get(i)));
                            Building buildingData = buildingProvider.findBuildingByName(meter.getNamespaceId(), meter.getCommunityId(), address.getBuildingName());
                            if (buildingData != null) {
                                ma.setBuildingId(buildingData.getId());
                            }
                            ma.setMeterId(meter.getId());
                            energyMeterAddressProvider.createEnergyMeterAddress(ma);
                        }
                    }
                }

                // 创建setting log 记录
                UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
                updateCmd.setPrice(meter.getPrice());
                updateCmd.setRate(meter.getRate());
                updateCmd.setCostFormulaId(meter.getCostFormulaId());
                updateCmd.setAmountFormulaId(meter.getAmountFormulaId());
                updateCmd.setStartTime(Date.valueOf(LocalDate.now()).getTime());
                updateCmd.setMeterId(meter.getId());
                updateCmd.setCalculationType(meter.getCalculationType());
                updateCmd.setConfigId(meter.getConfigId());
                updateCmd.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
//                this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd, meter.getCommunityId());
                this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd, meter.getCommunityId());
//                this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd, meter.getCommunityId());
//                this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd, meter.getCommunityId());

                // 创建一条初始读表记录
                ReadEnergyMeterCommand readEnergyMeterCmd = new ReadEnergyMeterCommand();
                readEnergyMeterCmd.setCommunityId(cmd.getCommunityId());
                readEnergyMeterCmd.setCurrReading(meter.getStartReading());
                readEnergyMeterCmd.setMeterId(meter.getId());
                readEnergyMeterCmd.setOrganizationId(cmd.getOwnerId());
                readEnergyMeterCmd.setResetMeterFlag(TrueOrFalseFlag.FALSE.getCode());
                readEnergyMeterCmd.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
                this.readEnergyMeter(readEnergyMeterCmd);

                meterSearcher.feedDoc(meter);
                return true;
            });
        });
        return errorDataLogs;
    }

    private  Map<String, EnergyMeterCategory> getAllScopeCategories(Long communityId,Long ownerId, Byte categoryType) {
        List<EnergyMeterCategoryMap> meterCategoryMaps = energyMeterCategoryMapProvider.listEnergyMeterCategoryMap(communityId,ownerId);
        List<Long> categoryIds = new ArrayList<>();
        if (meterCategoryMaps != null && meterCategoryMaps.size() > 0) {
            categoryIds = meterCategoryMaps.stream().map(EnergyMeterCategoryMap::getCategoryId).collect(Collectors.toList());
        }
        List<EnergyMeterCategory> categories = meterCategoryProvider.listMeterCategories(categoryIds, categoryType);
        Map<String, EnergyMeterCategory> categoryMap = new HashMap<>();
        if (categories != null && categories.size() > 0) {
            categories.forEach((map)-> categoryMap.put(map.getName(), map));
        }
        return categoryMap;
    }

    private boolean validateBurdenRateWithOneAddress(List<String> address, List<String> burdenRate) {
        if (address != null && address.size() == 1 && !"".equals(address.get(0))) {
            if (burdenRate != null && burdenRate.size() > 0) {
                return Objects.equals(burdenRate.get(0), "1");
            }
        }
        return true;
    }

    private Boolean validateBurdenRateString(List<String> burdenRate) {
        Boolean result = true;
        if (burdenRate != null && burdenRate.size() > 0) {
            if(Objects.equals(burdenRate.get(0), "")){
                return true;
            }
            BigDecimal sum = new BigDecimal(0);
            sum = burdenRate.stream().map(BigDecimal::new).reduce(sum, BigDecimal::add);
            result = sum.compareTo(new BigDecimal(1)) <= 0;
        }
        return result;
    }


    private String currLocale() {
        return UserContext.current().getUser().getLocale();
    }

    public ArrayList processorExcel(MultipartFile file) {
        MySheetContentsHandler sheetContentHandler = new MySheetContentsHandler();
        try {
            SAXHandlerEventUserModel userModel = new SAXHandlerEventUserModel(sheetContentHandler);
            userModel.processASheets(file.getInputStream(), 0);
        } catch (Exception e) {
            LOGGER.error("Process excel error.", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Process excel error.");
        }
        return sheetContentHandler.getResultList();
    }

    /**
     * 获取月份起始日期
     */
    public static Calendar getStartDate(Date date)  {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar ;
    }

    /**
     * 获取月份最后日期 -如果是本月就取今天
     */
    private static Calendar getMaxMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        //本月则就是今天了
        if (monthSF.get().format(date).equals(monthSF.get().format(DateHelper.currentGMTTime()))){
            calendar.setTime(DateHelper.currentGMTTime());
        }
        else {
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        return calendar ;
    }

    /**
     * 获取去年12月
     */
    private static Calendar getYearBeginDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        return calendar ;
    }

    /**
     * 获取年查询的endDate - 如果是今年则就上个月 其他则末月
     * @param date
     * @return
     * @throws ParseException
     */
    public static Calendar getYearEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //本月则就是上个月了
        if (yearSF.get().format(date).equals(yearSF.get().format(DateHelper.currentGMTTime()))){
            calendar.setTime(DateHelper.currentGMTTime());
            calendar.add(Calendar.MONTH, -1);
        }
        else {
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        }

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        return calendar ;
    }

    private List<DayStatDTO> deepCopyStatDays(List<DayStatDTO> days){
        List<DayStatDTO> result = new ArrayList<>();
        if(days == null )
            return null;
        for(DayStatDTO day : days ){
            result.add(ConvertHelper.convert(day, DayStatDTO.class));
        }
        return result;
    }

    @Override
    public EnergyStatDTO getEnergyStatByDay(EnergyStatCommand cmd) {
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_STAT_BY_DAY, cmd.getOrganizationId(),  cmd.getCommunityId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_STAT_BY_DAY);
        //查所有符合条件的记录
        EnergyStatDTO result = new EnergyStatDTO();
        result.setBillDayStats(new ArrayList<>());
        result.setDayBurdenStats(new ArrayList<>());
        SimpleDateFormat dateStrSF = new SimpleDateFormat("yyyy年MM月");
        result.setDateStr(dateStrSF.format(new Date(cmd.getStatDate())));
        Community com = this.communityProvider.findCommunityById(cmd.getCommunityId());
        if (null == com) {
            return null;
        }
        result.setCommunityName(com.getName());
        result.setMeterType(EnergyMeterType.fromCode(cmd.getMeterType()) == EnergyMeterType.WATER ? "用水分析"
                : (EnergyMeterType.fromCode(cmd.getMeterType()) == EnergyMeterType.ELECTRIC ? "用电分析" : ""));

        // 参数日期的开始
        Date startDate = new Date(getStartDate(new Date(cmd.getStatDate())).getTime().getTime());
        Date endDate = new Date(getMaxMonthDate(new Date(cmd.getStatDate())).getTime().getTime());

        List<EnergyDateStatistic> dayStats = this.energyDateStatisticProvider.listEnergyDateStatistics(cmd.getMeterType(),
                cmd.getCommunityId(), cmd.getBillCategoryIds(), cmd.getServiceCategoryIds(), startDate, endDate);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        int loopTimes = 0;
        result.setDates(new ArrayList<>());

        while(beginCalendar.compareTo(endCalendar) < 1) {
            dateStrSF = new SimpleDateFormat("MM月dd日");
            DayStatDTO date = new DayStatDTO();
            date.setStatDate(new Date(beginCalendar.getTime().getTime()).getTime());
            date.setDateStr(dateStrSF.format(beginCalendar.getTime()));
            result.getDates().add(date);
            //日期增加每一天,循环不能超过33天
            beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if(loopTimes ++ > 33)
                break;
        }

        //查不到就返回null
        if(null == dayStats)
            return null;

        for(EnergyDateStatistic dayStat : dayStats) {
            dateStrSF = new SimpleDateFormat("MM月dd日");
            DayStatDTO dayDTO = ConvertHelper.convert(dayStat, DayStatDTO.class);
            dayDTO.setStatDate(dayStat.getStatDate().getTime());
            dayDTO.setDateStr(dateStrSF.format(dayStat.getStatDate()));

            //查询是否有项目dto,没有则添加,有则累加用量
            BillStatDTO billDTO = findBillDTO(result, dayStat.getBillCategoryId());
            if(null == billDTO) {
                billDTO = new BillStatDTO();
                EnergyMeterCategory billCategory = this.meterCategoryProvider.findById(dayStat.getBillCategoryId());
                if(billCategory != null) {
                    billDTO.setBillCategoryId(billCategory.getId());
                    billDTO.setBillCategoryName(billCategory.getName());
                } else {
                    billDTO.setBillCategoryId(dayStat.getBillCategoryId());
                    billDTO.setBillCategoryName("");
                }

                billDTO.setDayBillStats(deepCopyStatDays(result.getDates()));
                billDTO.setServiceDayStats(new ArrayList<ServiceStatDTO>());
                result.getBillDayStats().add(billDTO);
            }
            DayStatDTO dayBillStat = findDayStat(billDTO.getDayBillStats(), dayDTO.getStatDate());
            dayBillStat.setCurrentAmount(dayBillStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
            dayBillStat.setCurrentCost(dayBillStat.getCurrentCost().add(dayDTO.getCurrentCost()));

            //查询是否有性质dto,没有则添加,有则累加用量
            ServiceStatDTO serviceDTO = findServiceStatDTO(billDTO, dayStat.getServiceCategoryId());
            if(null == serviceDTO) {
                serviceDTO = new ServiceStatDTO();
                EnergyMeterCategory serviceCategory = this.meterCategoryProvider.findById(dayStat.getServiceCategoryId());
                if(serviceCategory != null) {
                    serviceDTO.setServiceCategoryId(serviceCategory.getId());
                    serviceDTO.setServiceCategoryName(serviceCategory.getName());
                } else {
                    serviceDTO.setServiceCategoryId(dayStat.getServiceCategoryId());
                    serviceDTO.setServiceCategoryName("");
                }

                serviceDTO.setDayServiceStats(deepCopyStatDays(result.getDates()));
                serviceDTO.setMeterDayStats(new ArrayList<MeterStatDTO>());
                billDTO.getServiceDayStats().add(serviceDTO);
            }
            DayStatDTO dayServiceStat = findDayStat(serviceDTO.getDayServiceStats(), dayDTO.getStatDate());
            dayServiceStat.setCurrentAmount(dayServiceStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
            dayServiceStat.setCurrentCost(dayServiceStat.getCurrentCost().add(dayDTO.getCurrentCost()));

            //查询是否有表DTO 没有则添加 有则累加用量
            MeterStatDTO meterDTO = findMeterStatDTO(serviceDTO, dayStat.getMeterId());
            if(meterDTO == null) {
                meterDTO = ConvertHelper.convert(dayStat, MeterStatDTO.class);
                meterDTO.setDayStats(deepCopyStatDays(result.getDates()));
                serviceDTO.getMeterDayStats().add(meterDTO);
            }
            DayStatDTO meterDayStat = findDayStat(meterDTO.getDayStats(), dayDTO.getStatDate());
            meterDayStat.setCurrentAmount(dayDTO.getCurrentAmount());
            meterDayStat.setCurrentCost(dayDTO.getCurrentCost());
            meterDayStat.setCurrentReading(dayDTO.getCurrentReading());
            meterDayStat.setLastReading(dayDTO.getLastReading());
        }

        //最终计算实际负担
        result.setDayBurdenStats(new ArrayList<>());
        BillStatDTO receivableDTO = findBillDTO(result, EnergyCategoryDefault.RECEIVABLE.getCode());
        BillStatDTO payableDTO = findBillDTO(result, EnergyCategoryDefault.PAYABLE.getCode());

        Set<Long> statDates = new HashSet<>();
        if (receivableDTO != null && receivableDTO.getDayBillStats() != null) {
            receivableDTO.getDayBillStats().forEach(r -> {
                statDates.add(r.getStatDate());
            });
        }
        if (payableDTO != null && payableDTO.getDayBillStats() != null) {
            payableDTO.getDayBillStats().forEach(r -> {
                statDates.add(r.getStatDate());
            });
        }

        if (null != receivableDTO && null != payableDTO) {
            for (Long statDate : statDates) {
                DayStatDTO receivableDay = findDayStat(receivableDTO.getDayBillStats(), statDate);
                DayStatDTO payableDay = findDayStat(payableDTO.getDayBillStats(), statDate);
                DayStatDTO burdenDay = new DayStatDTO();
                //应收减应付
                burdenDay.setCurrentAmount(payableDay.getCurrentAmount().subtract(receivableDay.getCurrentAmount()));
                burdenDay.setCurrentCost(payableDay.getCurrentCost().subtract(receivableDay.getCurrentCost()));
                result.getDayBurdenStats().add(burdenDay);
            }
        }

        sumStatDTO(result);
        return result;
    }

    /**
     * 计算result的所有合计
     * */
    private void sumStatDTO(EnergyStatDTO result) {
        sumStatDTO(result.getDayBurdenStats());
        sumStatDTO(result.getLastYearPayableStats());
        if (null != result.getBillDayStats()) {
            for (BillStatDTO bill : result.getBillDayStats()) {
                sumStatDTO(bill.getDayBillStats());
                if (null != bill.getServiceDayStats()) {
                    for (ServiceStatDTO service : bill.getServiceDayStats()) {
                        sumStatDTO(service.getDayServiceStats());
                        if (null != service.getMeterDayStats()) {
                            for (MeterStatDTO meter : service.getMeterDayStats()) {
                                sumStatDTO(meter.getDayStats());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算一个DayStatDTO list 的 合计
     * */
    private void sumStatDTO(List<DayStatDTO> dayStats) {
        if(dayStats == null)
            return;
        DayStatDTO sumDTO = new DayStatDTO();
        sumDTO.setDateStr("合计");
        for(DayStatDTO dto : dayStats){
            sumDTO.setCurrentAmount(sumDTO.getCurrentAmount().add(dto.getCurrentAmount()));
            sumDTO.setCurrentCost(sumDTO.getCurrentCost().add(dto.getCurrentCost()));
        }
        dayStats.add(sumDTO);
    }

    private DayStatDTO findDayStat(List<DayStatDTO> list , Long statDate){
        DayStatDTO dayStatDTO = new DayStatDTO();
        dayStatDTO.setStatDate(statDate);
        dayStatDTO.setCurrentAmount(BigDecimal.ZERO);
        dayStatDTO.setCurrentCost(BigDecimal.ZERO);
        if(null != list && list.size()>0) {
            for (DayStatDTO dto : list) {
                if (dto.getStatDate().equals(statDate)) {
                    return dto;
                }
            }
            list.add(dayStatDTO);
        }
        return dayStatDTO;
    }

    /**
     * 通过id查DTO是否有该项目
     */
    private BillStatDTO findBillDTO(EnergyStatDTO fatherDto , Long id){
        if(null != fatherDto.getBillDayStats() && fatherDto.getBillDayStats().size()>0)
            for(BillStatDTO dto : fatherDto.getBillDayStats() )
                if(dto.getBillCategoryId().equals(id))
                    return dto;
        return null;
    }

    /**
     * 通过id查DTO是否有该性质
     */
    private ServiceStatDTO findServiceStatDTO(BillStatDTO fatherDto , Long id){
        if(null != fatherDto.getServiceDayStats() && fatherDto.getServiceDayStats().size()>0)
            for(ServiceStatDTO dto : fatherDto.getServiceDayStats() )
                if(dto.getServiceCategoryId().equals(id))
                    return dto;
        return null;
    }

    /**
     * 通过id查DTO是否有该表记
     */
    private MeterStatDTO findMeterStatDTO(ServiceStatDTO fatherDto , Long id){
        if(null != fatherDto.getMeterDayStats() && fatherDto.getMeterDayStats().size()>0)
            for(MeterStatDTO dto : fatherDto.getMeterDayStats() )
                if(dto.getMeterId().equals(id))
                    return dto;
        return null;
    }

    @Override
    public EnergyStatDTO getEnergyStatByMonth(EnergyStatCommand cmd)   {
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_STAT_BY_MONTH, cmd.getOrganizationId(),  cmd.getCommunityId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_STAT_BY_MONTH);
        SimpleDateFormat dateStrSF = new SimpleDateFormat("yyyy年MM月");

        //TODO: check cmd
        //查所有符合条件的记录
        EnergyStatDTO result = new EnergyStatDTO();
        result.setBillDayStats(new ArrayList<>());
        result.setDayBurdenStats(new ArrayList<>());
        result.setDateStr(yearSF.get().format(new Date(cmd.getStatDate())));
        Community com = this.communityProvider.findCommunityById(cmd.getCommunityId());
        if(null == com)
            return null;
        result.setCommunityName(com.getName());
        result.setMeterType(EnergyMeterType.WATER.getCode().equals(cmd.getMeterType())?"用水分析"
                :EnergyMeterType.ELECTRIC.getCode().equals(cmd.getMeterType())?"用电分析":"");

        List<EnergyMonthStatistic> monthStats = this.energyMonthStatisticProvider.listEnergyMonthStatistics(cmd.getMeterType(),
                cmd.getCommunityId(),cmd.getBillCategoryIds(),cmd.getServiceCategoryIds(),yearSF.get().format(new Date(cmd.getStatDate())));

        //查不到就返回null
        if(null == monthStats)
            return null;

        try {
            //把所有的日期单独拿出来,做表头
            Calendar beginCalendar = Calendar.getInstance();
            beginCalendar.setTime(getYearBeginDate(new Date(cmd.getStatDate())).getTime());
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(getYearEndDate(new Date(cmd.getStatDate())).getTime());
            int loopTimes = 0;
            result.setDates(new ArrayList<>());
            while(beginCalendar.compareTo(endCalendar) < 1) {
                DayStatDTO date = new DayStatDTO();
                date.setStatDate(beginCalendar.getTime().getTime());
                date.setDateStr(dateStrSF.format(beginCalendar.getTime()));
                result.getDates().add(date);

                //日期增加每一天,循环不能超过13个月
                beginCalendar.add(Calendar.MONTH, 1);
                if(loopTimes ++ > 13)
                    break;
            }

            for(EnergyMonthStatistic monthStat : monthStats) {
                DayStatDTO dayDTO = ConvertHelper.convert(monthStat, DayStatDTO.class);

                dayDTO.setStatDate(monthSF.get().parse(monthStat.getDateStr()).getTime());
                dayDTO.setDateStr(dateStrSF.format(monthSF.get().parse(monthStat.getDateStr())));

                // 查询是否有项目dto,没有则添加,有则累加用量
                BillStatDTO billDTO = findBillDTO(result, monthStat.getBillCategoryId());
                if(null == billDTO){
                    billDTO = new BillStatDTO();
                    EnergyMeterCategory billCategory = this.meterCategoryProvider.findById(monthStat.getBillCategoryId());
                    billDTO.setBillCategoryId(billCategory.getId());
                    billDTO.setBillCategoryName(billCategory.getName());
                    billDTO.setDayBillStats(deepCopyStatDays(result.getDates()));
                    billDTO.setServiceDayStats(new ArrayList<>());
                    result.getBillDayStats().add(billDTO);
                }
                DayStatDTO dayBillStat = findDayStat(billDTO.getDayBillStats(), dayDTO.getStatDate());
                dayBillStat.setCurrentAmount(dayBillStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
                dayBillStat.setCurrentCost(dayBillStat.getCurrentCost().add(dayDTO.getCurrentCost()));

                //查询是否有性质dto,没有则添加,有则累加用量
                ServiceStatDTO serviceDTO = findServiceStatDTO(billDTO, monthStat.getServiceCategoryId());
                if(null == serviceDTO){
                    serviceDTO = new ServiceStatDTO();
                    EnergyMeterCategory serviceCategory = this.meterCategoryProvider.findById(monthStat.getServiceCategoryId());
                    serviceDTO.setServiceCategoryId(serviceCategory.getId());
                    serviceDTO.setServiceCategoryName(serviceCategory.getName());
                    serviceDTO.setDayServiceStats(deepCopyStatDays(result.getDates()));
                    serviceDTO.setMeterDayStats(new ArrayList<>());
                    billDTO.getServiceDayStats().add(serviceDTO);
                }
                DayStatDTO dayServiceStat = findDayStat(serviceDTO.getDayServiceStats(), dayDTO.getStatDate());
                dayServiceStat.setCurrentAmount(dayServiceStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
                dayServiceStat.setCurrentCost(dayServiceStat.getCurrentCost().add(dayDTO.getCurrentCost()));

                //查询是否有表DTO 没有则添加 有则累加用量
                MeterStatDTO meterDTO = findMeterStatDTO(serviceDTO, monthStat.getMeterId());
                if(meterDTO == null){
                    meterDTO = ConvertHelper.convert(monthStat, MeterStatDTO.class);
                    meterDTO.setDayStats(deepCopyStatDays(result.getDates()));
                    serviceDTO.getMeterDayStats().add(meterDTO);
                }
                // TODO did not have to add this amount or cost ?
                DayStatDTO meterDayStat = findDayStat(meterDTO.getDayStats(), dayDTO.getStatDate());
                meterDayStat.setCurrentAmount(dayDTO.getCurrentAmount());
                meterDayStat.setCurrentCost(dayDTO.getCurrentCost());
                meterDayStat.setCurrentReading(dayDTO.getCurrentReading());
                meterDayStat.setLastReading(dayDTO.getLastReading());
            }

            //最终计算实际负担  和同比
            result.setLastYearPayableStats(new ArrayList<>());
            result.setDayBurdenStats(new ArrayList<>());

            BillStatDTO receivableDTO = findBillDTO(result, EnergyCategoryDefault.RECEIVABLE.getCode());
            BillStatDTO payableDTO = findBillDTO(result, EnergyCategoryDefault.PAYABLE.getCode());

            Set<Long> statDates = new HashSet<>();

            if (receivableDTO != null && receivableDTO.getDayBillStats() != null) {
                receivableDTO.getDayBillStats().forEach(r -> statDates.add(r.getStatDate()));
            }
            if (payableDTO != null && payableDTO.getDayBillStats() != null) {
                payableDTO.getDayBillStats().forEach(r -> statDates.add(r.getStatDate()));
            }

            for(Long statDate : statDates) {
                BigDecimal receivableAmount = BigDecimal.ZERO;
                BigDecimal receivableCost = BigDecimal.ZERO;
                BigDecimal payableAmount = BigDecimal.ZERO;
                BigDecimal payableCost = BigDecimal.ZERO;

                if (receivableDTO != null) {
                    DayStatDTO receivableDay = findDayStat(receivableDTO.getDayBillStats(), statDate);
                    receivableAmount = receivableDay.getCurrentAmount();
                    receivableCost = receivableDay.getCurrentCost();
                }
                if (payableDTO != null) {
                    DayStatDTO payableDay = findDayStat(payableDTO.getDayBillStats(), statDate);
                    payableAmount = payableDay.getCurrentAmount();
                    payableCost = payableDay.getCurrentCost();
                }

                DayStatDTO burdenDay = new DayStatDTO();
                DayStatDTO lastYearDTO = new DayStatDTO();
                // 应付减应收
                burdenDay.setStatDate(statDate);
                burdenDay.setCurrentAmount(payableAmount.subtract(receivableAmount));
                burdenDay.setCurrentCost(payableCost.subtract(receivableCost));
                result.getDayBurdenStats().add(burdenDay);
                result.getLastYearPayableStats().add(lastYearDTO);
                lastYearDTO.setStatDate(statDate);
                //得到去年的字符串
                Calendar lastYear = Calendar.getInstance();
                lastYear.setTime(new Date(statDate));
                lastYear.add(Calendar.YEAR,-1);
                lastYearDTO.setCurrentAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear.getTime()),cmd.getMeterType()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()));
                lastYearDTO.setCurrentCost(energyCountStatisticProvider.getSumCost(monthSF.get().format(lastYear.getTime()),cmd.getMeterType()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sumStatDTO(result);
        return result ;
    }

    @Override
    public List<EnergyStatByYearDTO> getEnergyStatisticByYear(EnergyStatCommand cmd) {
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_STAT_BY_YEAR);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_STAT_BY_YEAR, cmd.getOrganizationId(),  cmd.getCommunityId());
        List<EnergyStatByYearDTO> result = new ArrayList<>();
        Calendar statDate = Calendar.getInstance();
        statDate.setTime(new Date(cmd.getStatDate()));
        Calendar anchorDate = Calendar.getInstance();
        anchorDate.setTime(new Date(cmd.getStatDate()));
        //从年初开始
        anchorDate.set(Calendar.DAY_OF_YEAR, anchorDate.getActualMinimum(Calendar.DAY_OF_YEAR));
        int i = 0;
        while(anchorDate.compareTo(statDate)<=0){
            //避免死循环
            if(i++ >12)
                break;
            String dateStr = monthSF.get().format(anchorDate.getTime());
            EnergyStatByYearDTO dto = new EnergyStatByYearDTO();
            dto.setDateStr(dateStr);
            result.add(dto);
            //循环插入每一个数据
            //用量
            //本月-水费-账单项目-应收
            dto.setWaterReceivableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-水费-账单项目-应付
            dto.setWaterPayableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setWaterBurdenAmount(dto.getWaterPayableAmount().subtract(dto.getWaterReceivableAmount()));


            //本月-电费-账单项目-应收
            dto.setElectricReceivableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-电费-账单项目-应付
            dto.setElectricPayableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setElectricBurdenAmount(dto.getElectricPayableAmount().subtract(dto.getElectricReceivableAmount()));

            //费用
            //本月-水费-账单项目-应收
            dto.setWaterReceivableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-水费-账单项目-应付
            dto.setWaterPayableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setWaterBurdenCost(dto.getWaterPayableCost().subtract(dto.getWaterReceivableCost()));


            //本月-电费-账单项目-应收
            dto.setElectricReceivableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-电费-账单项目-应付
            dto.setElectricPayableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setElectricBurdenCost(dto.getElectricPayableCost().subtract(dto.getElectricReceivableCost()));

            //下一个月
            anchorDate.add(Calendar.MONTH, 1);
        }
        return result;

    }

    /* (non-Javadoc)
     * @see com.everhomes.energy.EnergyConsumptionService#getEnergyStatisticByYoy(com.everhomes.rest.energy.EnergyStatCommand)
     */
    @Override
    public List<EnergyCommunityYoyStatDTO> getEnergyStatisticByYoy(EnergyStatCommand cmd) {
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_STAT_BY_YOY);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_STAT_BY_YOY, cmd.getOrganizationId(),  cmd.getCommunityId());
        List<EnergyYoyStatistic> stats = this.energyYoyStatisticProvider.listenergyYoyStatistics(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()),
                monthSF.get().format(new Date(cmd.getStatDate())), cmd.getCommunityId());
        if(null == stats)
            return null;

        Calendar lastYear = Calendar.getInstance();
        lastYear.setTime(new Date(cmd.getStatDate()));
        lastYear.add(Calendar.YEAR,-1);

        List<EnergyCommunityYoyStatDTO> result = new ArrayList<EnergyCommunityYoyStatDTO>();
        EnergyCommunityYoyStatDTO totalDTO = new EnergyCommunityYoyStatDTO();
        totalDTO.setCommunityName("合计");
        EnergyStatByYearDTO totalYearDTO = new EnergyStatByYearDTO();
        totalYearDTO.setDateStr(monthSF.get().format(lastYear.getTime()));
        EnergyStatByYearDTO totalLastYearDTO = new EnergyStatByYearDTO();
        totalLastYearDTO.setDateStr(monthSF.get().format(lastYear.getTime()));
        totalDTO.setStatlist(new ArrayList<EnergyStatByYearDTO>());
        totalDTO.getStatlist().add(totalYearDTO);
        totalDTO.getStatlist().add(totalLastYearDTO);
        for(EnergyYoyStatistic stat : stats){
            EnergyCommunityYoyStatDTO dto = new EnergyCommunityYoyStatDTO();
            result.add(dto);
            dto.setCommunityId(stat.getCommunityId());
            Community com = this.communityProvider.findCommunityById(stat.getCommunityId());
            if (com != null)
                dto.setCommunityName(com.getName());
            dto.setStatlist(new ArrayList<EnergyStatByYearDTO>());
            EnergyStatByYearDTO thisYearDTO = ConvertHelper.convert(stat, EnergyStatByYearDTO.class);
            dto.getStatlist().add(thisYearDTO);
            EnergyStatByYearDTO lastYearDTO = new EnergyStatByYearDTO();
            dto.getStatlist().add(lastYearDTO);
            lastYearDTO.setDateStr(monthSF.get().format(lastYear.getTime()));
            lastYearDTO.setElectricBurdenAmount(stat.getElectricLastBurdenAmount());
            lastYearDTO.setElectricPayableAmount(stat.getElectricLastPayableAmount());
            lastYearDTO.setElectricReceivableAmount(stat.getElectricLastReceivableAmount());
            lastYearDTO.setElectricAverageAmount(stat.getElectricLastAverageAmount());
            lastYearDTO.setWaterBurdenAmount(stat.getWaterLastBurdenAmount());
            lastYearDTO.setWaterPayableAmount(stat.getWaterLastPayableAmount());
            lastYearDTO.setWaterReceivableAmount(stat.getWaterLastReceivableAmount());
            lastYearDTO.setWaterAverageAmount(stat.getWaterLastAverageAmount());

            //合计
            totalYearDTO.setElectricBurdenAmount(totalYearDTO.getElectricBurdenAmount().add(thisYearDTO.getElectricBurdenAmount()));
            totalYearDTO.setElectricPayableAmount(totalYearDTO.getElectricPayableAmount().add(thisYearDTO.getElectricPayableAmount()));
            totalYearDTO.setElectricReceivableAmount(totalYearDTO.getElectricReceivableAmount().add(thisYearDTO.getElectricReceivableAmount()));
            totalYearDTO.setElectricAverageAmount(totalYearDTO.getElectricAverageAmount().add(thisYearDTO.getElectricAverageAmount()));
            totalYearDTO.setWaterBurdenAmount(totalYearDTO.getWaterBurdenAmount().add(thisYearDTO.getWaterBurdenAmount()));
            totalYearDTO.setWaterPayableAmount(totalYearDTO.getWaterPayableAmount().add(thisYearDTO.getWaterPayableAmount()));
            totalYearDTO.setWaterReceivableAmount(totalYearDTO.getWaterReceivableAmount().add(thisYearDTO.getWaterReceivableAmount()));
            totalYearDTO.setWaterAverageAmount(totalYearDTO.getWaterAverageAmount().add(thisYearDTO.getWaterAverageAmount()));

            totalLastYearDTO.setElectricBurdenAmount(totalLastYearDTO.getElectricBurdenAmount().add(totalYearDTO.getElectricBurdenAmount()));
            totalLastYearDTO.setElectricPayableAmount(totalLastYearDTO.getElectricPayableAmount().add(totalYearDTO.getElectricPayableAmount()));
            totalLastYearDTO.setElectricReceivableAmount(totalLastYearDTO.getElectricReceivableAmount().add(totalYearDTO.getElectricReceivableAmount()));
            totalLastYearDTO.setElectricAverageAmount(totalLastYearDTO.getElectricAverageAmount().add(totalYearDTO.getElectricAverageAmount()));
            totalLastYearDTO.setWaterBurdenAmount(totalLastYearDTO.getWaterBurdenAmount().add(totalYearDTO.getWaterBurdenAmount()));
            totalLastYearDTO.setWaterPayableAmount(totalLastYearDTO.getWaterPayableAmount().add(totalYearDTO.getWaterPayableAmount()));
            totalLastYearDTO.setWaterReceivableAmount(totalLastYearDTO.getWaterReceivableAmount().add(totalYearDTO.getWaterReceivableAmount()));
            totalLastYearDTO.setWaterAverageAmount(totalLastYearDTO.getWaterAverageAmount().add(totalYearDTO.getWaterAverageAmount()));
        }
        return result;
    }

    @Override
    public List<EnergyMeterChangeLogDTO> listEnergyMeterChangeLogs(ListEnergyMeterChangeLogsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        List<EnergyMeterChangeLog> logs = meterChangeLogProvider.listEnergyMeterChangeLogsByMeter(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getMeterId());
        return logs.stream().map(r -> ConvertHelper.convert(r, EnergyMeterChangeLogDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<EnergyMeterFormulaDTO> listEnergyMeterFormulas(ListEnergyMeterFormulasCommand cmd) {
        validate(cmd);
        List<EnergyMeterFormula> formulas = new ArrayList<>();
        //全部则查全部的和各个项目的
        if(cmd.getCommunityId() == null) {
            formulas = meterFormulaProvider.listMeterFormulas(cmd.getOwnerId(), cmd.getOwnerType(),
                    null, UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getFormulaType());
        }

        //单项目则查自己加的和关联的全部的
        else if(cmd.getCommunityId() != null) {
            formulas = meterFormulaProvider.listMeterFormulas(cmd.getOwnerId(), cmd.getOwnerType(),
                    cmd.getCommunityId(), UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getFormulaType());
            List<EnergyMeterFormulaMap> maps = energyMeterFormulaMapProvider.listEnergyMeterFormulaMap(cmd.getCommunityId());
            if(maps != null && maps.size() > 0) {
                List<Long> formulaIds = maps.stream().map(map -> {
                    return map.getFomularId();
                }).collect(Collectors.toList());
                List<EnergyMeterFormula> formulaList = meterFormulaProvider.listMeterFormulas(formulaIds, cmd.getFormulaType()) ;
                if(formulaList != null && formulaList.size() > 0) {
                    formulas.addAll(formulaList);
                }
            }

        }
//        checkCurrentUserNotInOrg(cmd.getOwnerId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);

        List<EnergyMeterFormulaDTO> dtos = formulas.stream().map(formula -> {
            EnergyMeterFormulaDTO dto = toEnergyMeterFormulaDTO(formula);
            List<String> communityName = new ArrayList<String>();
            if(dto.getCommunityId() == null || dto.getCommunityId() == 0L) {
                List<Long> communityIds = energyMeterFormulaMapProvider.listCommunityIdByFormula(dto.getId());
                List<Community> communities = communityProvider.findCommunitiesByIds(communityIds);
                if(communities != null && communities.size() > 0) {
                    communities.forEach(community -> {
                        if(community != null) {
                            communityName.add(community.getName());
                        }
                    });
                }
            } else {
                Community community = communityProvider.findCommunityById(dto.getCommunityId());
                if(community != null) {
                    communityName.add(community.getName());
                }
            }
            dto.setCommunityName(communityName);
            return dto;
        }).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public void deleteEnergyMeterFormula(DeleteEnergyMeterFormulaCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOrganizationId());
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOrganizationId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_SETTING);
//        }
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOrganizationId(),  cmd.getCommunityId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_FORMULA.getCode() + cmd.getFormulaId()).tryEnter(() -> {
            EnergyMeterFormula formula = meterFormulaProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getFormulaId());

            if (formula != null) {
                //项目里删全部的 实质是解除关联关系
                if((formula.getCommunityId() == null || formula.getCommunityId() == 0L) && cmd.getCommunityId() != null) {
                    EnergyMeterSettingLog settingLog = meterSettingLogProvider.findAnySettingByFormulaId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCommunityId(), formula.getId());
                    if (settingLog != null) {
                        LOGGER.info("The formula has been reference, formula id = {}", formula.getId());
                        throw errorWith(SCOPE, ERR_FORMULA_HAS_BEEN_REFERENCE, "The formula has been reference");
                    }

                    EnergyMeterFormulaMap map = energyMeterFormulaMapProvider.findEnergyMeterFormulaMap(cmd.getCommunityId(), formula.getId());
                    if(map != null) {
                        map.setStatus(EnergyCommonStatus.INACTIVE.getCode());
                        map.setOperatorUid(UserContext.currentUserId());
                        map.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        energyMeterFormulaMapProvider.updateEnergyMeterFormulaMap(map);
                    }

                } else {
                    //在全部里删全部和项目的，项目里删自己的 都是直接置inactive
                    // 查看当前公式是否被引用, 被引用则无法删除
                    EnergyMeterSettingLog settingLog = meterSettingLogProvider.findSettingByFormulaId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), formula.getId());
                    if (settingLog != null) {
                        LOGGER.info("The formula has been reference, formula id = {}", formula.getId());
                        throw errorWith(SCOPE, ERR_FORMULA_HAS_BEEN_REFERENCE, "The formula has been reference");
                    } else {
                        meterFormulaProvider.deleteFormula(formula);
                    }
                }

            }
        });
    }

    @Override
    public List<EnergyMeterSettingLogDTO> listEnergyMeterSettingLogs(ListEnergyMeterSettingLogsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        if(EnergyMeterSettingType.PRICE.equals(EnergyMeterSettingType.fromCode(cmd.getSettingType()))) {
            //log按createTime升序排 放入map时 对同一天的修改 后改的覆盖先前的
            List<EnergyMeterSettingLog> logs = meterSettingLogProvider.listEnergyMeterSettingLogsOrderByCreateTime(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getMeterId(), cmd.getSettingType());
            Map<Long, EnergyMeterPriceDTO> maps = mapEnergyMeterPriceDTO(logs);
            return dealEnergyMeterPriceDTO(maps);
        } else {
            List<EnergyMeterSettingLog> logs = meterSettingLogProvider.listEnergyMeterSettingLogs(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getMeterId(), cmd.getSettingType());
            return logs.stream().map(log -> this.toEnergyMeterSettingLogDTO(log,cmd.getNamespaceId())).collect(Collectors.toList());
        }

    }

    private Map<Long, EnergyMeterPriceDTO> mapEnergyMeterPriceDTO(List<EnergyMeterSettingLog> logs) {
        Map<Long, EnergyMeterPriceDTO> maps = new HashMap<>();
        logs.forEach(log -> {
            EnergyMeterPriceDTO dto = new EnergyMeterPriceDTO();
            //开始时间-创建时间-价钱    结束时间-创建时间-价钱
            dto.setMeterId(log.getMeterId());
            dto.setCreateTime(log.getCreateTime());
            dto.setTime(log.getStartTime());
            dto.setSettingValue(log.getSettingValue());
            if(log.getConfigId() != null && log.getConfigId() > 0L) {
                //价钱梯度表的方案名
                EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(log.getConfigId());
                if(priceConfig != null)
                    dto.setPlanName(priceConfig.getName());
            }
            if(dto.getTime() != null) {
                maps.put(dto.getTime().getTime(), dto);
            } else {
                maps.put(Long.MAX_VALUE-1, dto);
            }

            EnergyMeterPriceDTO endDTO = new EnergyMeterPriceDTO();
            endDTO.setMeterId(dto.getMeterId());
            endDTO.setCreateTime(dto.getCreateTime());
            endDTO.setSettingValue(dto.getSettingValue());
            endDTO.setPlanName(dto.getPlanName());
            endDTO.setTime(log.getEndTime());
            if(endDTO.getTime() != null) {
                maps.put(endDTO.getTime().getTime(), endDTO);
            } else {
                maps.put(Long.MAX_VALUE-1, endDTO);
            }
        });
        return maps;
    }

    private List<EnergyMeterSettingLogDTO> dealEnergyMeterPriceDTO(Map<Long, EnergyMeterPriceDTO> maps) {
        List<EnergyMeterSettingLogDTO> dtos = new ArrayList<EnergyMeterSettingLogDTO>();
        Object[] key_arr = maps.keySet().toArray();
        if(key_arr.length > 0) {
            Arrays.sort(key_arr);
            if(key_arr.length >= 2) {
                for(int i = 0; i < key_arr.length-2; i++) {
                    EnergyMeterPriceDTO value = maps.get(key_arr[i]);
                    EnergyMeterSettingLogDTO dto = new EnergyMeterSettingLogDTO();
                    dto.setMeterId(value.getMeterId());
                    dto.setSettingValue(value.getSettingValue());
                    dto.setPriceConfigName(value.getPlanName());
//                    dto.setFormulaName(value.getPlanName());
                    dto.setStartTime(value.getTime());
                    EnergyMeterPriceDTO nextValue = maps.get(key_arr[i+1]);
                    dto.setEndTime(nextValue.getTime());

                    dtos.add(dto);
                }

                EnergyMeterPriceDTO value = maps.get(key_arr[key_arr.length-2]);
                EnergyMeterSettingLogDTO dto = new EnergyMeterSettingLogDTO();
                dto.setMeterId(value.getMeterId());
                dto.setStartTime(value.getTime());
                EnergyMeterPriceDTO nextValue = maps.get(key_arr[key_arr.length-1]);
                dto.setEndTime(nextValue.getTime());

                dto.setSettingValue(nextValue.getSettingValue());
                dto.setPriceConfigName(value.getPlanName());

                dtos.add(dto);
            } else if(key_arr.length == 1) {
                EnergyMeterPriceDTO value = maps.get(key_arr[key_arr.length-1]);
                EnergyMeterSettingLogDTO dto = new EnergyMeterSettingLogDTO();
                dto.setMeterId(value.getMeterId());
                dto.setStartTime(value.getTime());
                dto.setSettingValue(value.getSettingValue());
                dto.setPriceConfigName(value.getPlanName());

                dtos.add(dto);
            }

        }
        return dtos;
    }

    private EnergyMeterSettingLogDTO toEnergyMeterSettingLogDTO(EnergyMeterSettingLog log,Integer namespaceId) {
        EnergyMeterSettingLogDTO dto = ConvertHelper.convert(log, EnergyMeterSettingLogDTO.class);
        if (log.getFormulaId() != null) {
            EnergyMeterFormula formula = meterFormulaProvider.findById(UserContext.getCurrentNamespaceId(namespaceId), log.getFormulaId());
            dto.setFormulaName(formula.getName());
        }
        if(log.getConfigId() != null && log.getConfigId() != 0L) {
            EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(log.getConfigId());
            if(priceConfig != null) {
                dto.setPriceConfigName(priceConfig.getName());
            }
        }
        return dto;
    }

    private EnergyMeterFormulaDTO toEnergyMeterFormulaDTO(EnergyMeterFormula formula) {
        if(formula == null) {
            return null;
        }
        EnergyMeterFormulaDTO dto = ConvertHelper.convert(formula, EnergyMeterFormulaDTO.class);
        dto.setExpression(formula.getDisplayExpression());
        return dto;
    }

    @Override
    public List<EnergyMeterDefaultSettingDTO> listEnergyDefaultSettings(ListEnergyDefaultSettingsCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOwnerId());
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        }
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOwnerId(),  cmd.getCommunityId());
        List<EnergyMeterDefaultSetting> settings = new ArrayList<>();
        if (cmd.getMeterType() != null) {
            settings = defaultSettingProvider.listDefaultSetting(cmd.getOwnerId(), cmd.getOwnerType(),
                    cmd.getCommunityId(),UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getMeterType());
        } else {
            List<EnergyMeterDefaultSetting> waterSettings = defaultSettingProvider.listDefaultSetting(cmd.getOwnerId(), cmd.getOwnerType(),
                    cmd.getCommunityId(),UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), EnergyMeterType.WATER.getCode());
            List<EnergyMeterDefaultSetting> elecSettings = defaultSettingProvider.listDefaultSetting(cmd.getOwnerId(), cmd.getOwnerType(),
                    cmd.getCommunityId(),UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), EnergyMeterType.ELECTRIC.getCode());
            settings.addAll(waterSettings);
            settings.addAll(elecSettings);
        }
        return settings.stream().map(setting -> this.toEnergyMeterDefaultSettingDTO(setting,cmd.getNamespaceId())).collect(Collectors.toList());
    }

    private EnergyMeterDefaultSettingDTO toEnergyMeterDefaultSettingDTO(EnergyMeterDefaultSetting setting, Integer namespaceId) {
        EnergyMeterDefaultSettingDTO dto = ConvertHelper.convert(setting, EnergyMeterDefaultSettingDTO.class);
        dto.setSettingStatus(setting.getStatus());
        if (setting.getFormulaId() != null) {
            EnergyMeterFormula formula = meterFormulaProvider.findById(UserContext.getCurrentNamespaceId(namespaceId), setting.getFormulaId());
            if (formula != null) {
                dto.setFormulaName(formula.getName());
                dto.setFormulaType(formula.getFormulaType());
            }
        }
        if(EnergyMeterSettingType.PRICE.equals(EnergyMeterSettingType.fromCode(setting.getSettingType()))) {
            if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(setting.getCalculationType()))) {
                EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(setting.getConfigId(), setting.getOwnerId(),
                        setting.getOwnerType(), setting.getCommunityId(), UserContext.getCurrentNamespaceId(namespaceId));
                dto.setPriceConfigName(priceConfig.getName());
            }

        }
        // 表的类型
        String meterType = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_TYPE, String.valueOf(setting.getMeterType()), currLocale(), "");
        dto.setMeterType(meterType);
        return dto;
    }

    @Override
    public List<EnergyFormulaVariableDTO> listEnergyFormulaVariables() {
        List<EnergyMeterFormulaVariable> variables = meterFormulaVariableProvider.listMeterFormulaVariables();
        return variables.stream().map(r -> ConvertHelper.convert(r, EnergyFormulaVariableDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<EnergyMeterCategoryDTO> listEnergyMeterCategories(ListEnergyMeterCategoriesCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOwnerId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
        List<EnergyMeterCategory> categoryList = new ArrayList<>();
        //全部则查全部的和各个项目的  标准版 这里不存在项目查不到,查询所有的概念，存在应用范围的概念
        if (cmd.getCommunityIds() != null && cmd.getCommunityIds().size() > 1) {
            categoryList = meterCategoryProvider.listMeterCategories(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCategoryType(),
                    null, null, cmd.getCommunityIds());
            // get all scope general data of this owner
            List<EnergyMeterCategory> categories =  meterCategoryProvider.listOrgGeneralMeterCategories(cmd.getNamespaceId(), cmd.getCategoryType(), cmd.getOwnerId(), 0L);
            if(categories!=null && categories.size()>0){
                categoryList.addAll(categories);
            }
        }

        //单项目则查自己加的和关联的全部的
        else if (cmd.getCommunityIds() != null && cmd.getCommunityIds().size() == 1) {
            categoryList = meterCategoryProvider.listMeterCategories(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCategoryType(),
                    null,null, cmd.getCommunityIds());
            List<EnergyMeterCategoryMap> maps = energyMeterCategoryMapProvider.listEnergyMeterCategoryMap(cmd.getCommunityIds().get(0),null);
            if(maps != null && maps.size() > 0) {
                List<Long> categoryIds = maps.stream().map(EhEnergyMeterCategoryMap::getCategoryId).collect(Collectors.toList());
                List<EnergyMeterCategory> categories = meterCategoryProvider.listMeterCategories(categoryIds, cmd.getCategoryType());
                if(categories != null && categories.size() > 0) {
                    categoryList.addAll(categories);
                }
            }

        }

        List<EnergyMeterCategoryDTO> dtos = categoryList.stream().map(category -> {
            EnergyMeterCategoryDTO dto = toMeterCategoryDto(category);
            List<String> communityName = new ArrayList<String>();
            if(dto.getCommunityId() == null || dto.getCommunityId() == 0L) {
                List<Long> communityIds = energyMeterCategoryMapProvider.listCommunityIdByCategory(dto.getId());
                List<Community> communities = communityProvider.findCommunitiesByIds(communityIds);
                if(communities != null && communities.size() > 0) {
                    communities.forEach(community -> {
                        if(community != null) {
                            communityName.add(community.getName());
                        }
                    });
                }
            } else {
                Community community = communityProvider.findCommunityById(dto.getCommunityId());
                if(community != null) {
                    communityName.add(community.getName());
                }
            }
            dto.setCommunityName(communityName);
            return dto;
        }).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public EnergyMeterDTO getEnergyMeter(GetEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId(),cmd.getNamespaceId());
        return toEnergyMeterDTO(meter,cmd.getNamespaceId());
    }

    private EnergyMeterCategoryDTO toMeterCategoryDto(EnergyMeterCategory category) {
        return ConvertHelper.convert(category, EnergyMeterCategoryDTO.class);
    }

    private void invalidParameterException(String name, Object param) {
        LOGGER.error("Invalid parameter {} [ {} ].", name, param);
        throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "Invalid parameter %s [ %s ].", name, param);
    }

    private EnergyMeter findMeterById(Long id,Integer namespaceId) {
        EnergyMeter meter = meterProvider.findById(UserContext.getCurrentNamespaceId(namespaceId), id);
        if (meter != null) {
            return meter;
        }
        LOGGER.error("EnergyMeter not exist, id = {}", id);
        throw errorWith(SCOPE, ERR_METER_NOT_EXIST, "The meter is not exist id = %s", id);
    }

    private Timestamp getDayBegin(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    private void checkCurrentUserNotInOrg(Long orgId) {
//        if (orgId == null) {
//            LOGGER.error("Invalid parameter organizationId [ null ]");
//            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "Invalid parameter organizationId [ null ]");
//        }
//        Long userId = UserContext.current().getUser().getId();
//        OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, orgId);
//        if (member == null) {
//            LOGGER.error("User is not in the organization.");
//            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "User is not in the organization.");
//        }
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

    private Integer currNamespaceId() {
        return UserContext.getCurrentNamespaceId();
    }

    /**
     * 每天早上1点10分刷前一天的读表
     * */
    @Scheduled(cron = "0 10 1 * * ?")
    public void calculateEnergyDayStat(){

        //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_DAY_STAT_SCHEDULE.getCode()).tryEnter(() -> {
                try {
                    LOGGER.info("calculate energy day stat start...");
                    //刷今天的
                    calculateEnergyDayStatByDate(DateHelper.currentGMTTime());
                    LOGGER.info("calculate energy day stat end...");
                } catch (Exception e) {
                    LOGGER.error("calculate energy day stat error...", e);
                    sendErrorMessage(e);
                    e.printStackTrace();
                }
            });
        }
    }

//    /**
//     * 每天早上5点10分刷自动读表
//     */
//    @Scheduled(cron = "0 10 5 L * ?")
    private void readMeterRemote(Boolean createPlansFlag,Long communityId) {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            LOGGER.info("read energy meter reading ...");
            List<EnergyMeter> meters = new ArrayList<>();
            if (communityId != null) {
                meters = meterProvider.listAutoReadingMetersByCommunityId(communityId);
            } else {
                meters = meterProvider.listAutoReadingMeters();
            }
            if (meters != null && meters.size() > 0) {
                meters.forEach((meter) -> {
                    String serverUrl = configurationProvider.getValue(meter.getNamespaceId(), "energy.meter.thirdparty.server", "");
                    String publicKey = configurationProvider.getValue(meter.getNamespaceId(), "energy.meter.thirdparty.publicKey", "");
                    String clientId = configurationProvider.getValue(meter.getNamespaceId(), "energy.meter.thirdparty.client.id", "");
                    EnergyAutoReadHandler handler = null;
                    if (meter.getNamespaceId() == 999961) {
                         handler = PlatformContext.getComponent(EnergyAutoReadHandler.AUTO_PREFIX + EnergyAutoReadHandler.ZHI_FU_HUI);
                    }
                    if (handler != null) {
                        try {
                            String meterReading = handler.readMeterautomatically(meter.getMeterNumber(), serverUrl, publicKey, clientId);
                            LOGGER.info("energy auto reading meter meterId={},meterNumber={}, Reading={}", meter.getId(), meter.getMeterNumber(), meterReading);
                            //parser
                            Map<String, String> result = getRemoteReadingDataMap(meterReading,meter.getId(),meter.getMeterNumber());
                            //log
                            EnergyMeterTask task = energyMeterTaskProvider.findEnergyMeterTaskByMeterId(meter.getId(), new Timestamp(DateHelper.currentGMTTime().getTime()));
                            EnergyMeterReadingLog log = new EnergyMeterReadingLog();
                            log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                            log.setReading(new BigDecimal(result.get("this_read")));
                            if (task != null) {
                                log.setTaskId(task.getId());
                                task.setReading(log.getReading());
                                task.setStatus(EnergyTaskStatus.READ.getCode());
                                task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                            }
                            log.setCommunityId(meter.getCommunityId());
                            log.setMeterId(meter.getId());
                            log.setNamespaceId(meter.getNamespaceId());
                            log.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                            log.setOperatorId(UserContext.currentUserId());
                            if (meter.getLastReading() != null &&
                                    new BigDecimal(result.get("this_read")).subtract(meter.getLastReading()).intValue() < 0) {
                                log.setResetMeterFlag(TrueOrFalseFlag.TRUE.getCode());
                            } else {
                                log.setResetMeterFlag(TrueOrFalseFlag.FALSE.getCode());
                            }
                            dbProvider.execute(r -> {
                                meterReadingLogProvider.createEnergyMeterReadingLog(log);
                                if (task != null) {
                                    energyMeterTaskProvider.updateEnergyMeterTask(task);
                                    energyMeterTaskSearcher.feedDoc(task);
                                }
                                meter.setLastReading(log.getReading());
                                meter.setLastReadTime(Timestamp.valueOf(LocalDateTime.now()));
                                meterProvider.updateEnergyMeter(meter);
                                return true;
                            });
                            readingLogSearcher.feedDoc(log);
                            meterSearcher.feedDoc(meter);
                        } catch (Exception e) {
                            LOGGER.error("read energy meter reading  error...", e);
                            sendErrorMessage(e);
                            e.printStackTrace();
                        }
                    }
                });

            }
            LOGGER.info("read energy meter reading  end...");

            if(createPlansFlag){
                LOGGER.info("starting create  auto  meter plans ...");
                createMonthAutoPlans(meters);
                LOGGER.info("ending create  auto  meter plans ...");
            }
        }
    }

    private Map<String, String> getRemoteReadingDataMap(String meterReading, Long meterId, String meterNumber) {
        try {
            JsonObject jsonObj = new JsonParser().parse(meterReading).getAsJsonObject();
            String data = jsonObj.getAsJsonArray("data").get(0).toString();
            return new Gson().fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("meter id = %s,meterNumber = %s, autoReading = %s", meterId, meterNumber, meterReading), e);
        }
    }

    private void createMonthAutoPlans(List<EnergyMeter> meters) {
        if (meters != null && meters.size() > 0) {
            Map<Integer, List<EnergyMeter>> namespaceMeterMap = new HashMap<>();
            namespaceMeterMap = getprocessNamespaceMeterMap(meters);
            if(namespaceMeterMap!=null && namespaceMeterMap.size()>0){
                for (Integer key : namespaceMeterMap.keySet()) {
                    List<EnergyMeter> namespaceMeter = namespaceMeterMap.get(key);
                    Map<Long, List<EnergyMeter>> communityMeters = getCommunityMapMeters(namespaceMeter);
                    if (communityMeters != null && communityMeters.size() > 0) {
                       communityMeters.forEach((k,v)->{
                           EnergyPlan plan = new EnergyPlan();
                           plan.setRepeatSettingId(0L);
                           plan.setName("autoPlans");
                           plan.setNamespaceId(key);
                           plan.setTargetType("communityId");
                           plan.setStatus(CommonStatus.AUTO.getCode());
                           plan.setOwnerId(v.get(0).getOwnerId());
                           plan.setOwnerType(v.get(0).getOwnerType());
                           plan.setTargetId(k);
                           RepeatSettings repeatSetting = new RepeatSettings();
                           //调用此的时候已经是每月月底
                           LocalDate localDate = LocalDate.now();
                           repeatSetting.setOwnerId(v.get(0).getOwnerId());
                           repeatSetting.setOwnerType(v.get(0).getOwnerType());
                           repeatSetting.setStartDate(Date.valueOf(localDate.format(autoDateSF)));
                           repeatSetting.setEndDate(Date.valueOf(localDate.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).format(autoDateSF)));
                           repeatSetting.setTimeRanges("{\"ranges\":[{\"startTime\":\"00:00:00\",\"duration\":\"1M\"}]}");
                           repeatSetting.setRepeatType(StandardRepeatType.BY_MONTH.getCode());
                           repeatSetting.setRepeatInterval(1);
                           String expression = "{\"expression\":[{\"day\":%s}]}";
                           expression = String.format(expression, String.valueOf(LocalDate.now().plusDays(1).getDayOfMonth()));
                           repeatSetting.setExpression(expression);
                           repeatService.createRepeatSettings(repeatSetting);
                           plan.setRepeatSettingId(repeatSetting.getId());
                           energyPlanProvider.createEnergyPlan(plan);
                           if (v.size() > 0) {
                               v.forEach((meter) -> {
                                   EnergyPlanMeterMap meterMap = new EnergyPlanMeterMap();
                                   meterMap.setPlanId(plan.getId());
                                   meterMap.setMeterId(meter.getId());
                                   energyPlanProvider.createEnergyPlanMeterMap(meterMap);
                               });
                           }
                       });
                   }
                }
            }
        }
    }

    private Map<Long,List<EnergyMeter>> getCommunityMapMeters(List<EnergyMeter> v) {
        Map<Long, List<EnergyMeter>> communityMeterMap = new HashMap<>();
        if (v != null && v.size() > 0) {
            v.forEach((r) -> {
                if(communityMeterMap.get(r.getCommunityId())!=null){
                    communityMeterMap.get(r.getCommunityId()).add(r);
                }else {
                    communityMeterMap.put(r.getCommunityId(), new ArrayList<>(Collections.singletonList(r)));
                }
            });
        }
        return  communityMeterMap;
    }

    private Map<Integer, List<EnergyMeter>> getprocessNamespaceMeterMap(List<EnergyMeter> meters) {
        Map<Integer, List<EnergyMeter>> meterMap = new HashMap<>();
        if (meters != null && meters.size() > 0) {
            meters.forEach(meter -> {
                if (meterMap.get(meter.getNamespaceId()) != null) {
                    meterMap.get(meter.getNamespaceId()).add(meter);
                } else {
                    meterMap.put(meter.getNamespaceId(), new ArrayList<>(Collections.singletonList(meter)));
                }
            });
        }
        return meterMap;
    }

    private void sendErrorMessage(Exception e) {
        String xiongying = "rui.jia@zuolin.com";
        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);
        String account = configurationProvider.getValue(0,"mail.smtp.account", "zuolin@zuolin.com");
        if ("core.zuolin.com".equals(configurationProvider.getValue(0,"home.url", ""))) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 PrintStream stream = new PrintStream(out)) {
                e.printStackTrace(stream);
                String message = out.toString("UTF-8");
                handler.sendMail(0, account, xiongying, "calculateEnergyDayStat error", message);
                // out.reset();
                e.getCause().printStackTrace(stream);
                message = out.toString("UTF-8");
                handler.sendMail(0, account, xiongying, "calculateEnergyDayStat error cause", message);

            } catch (Exception ignored) {
                //
            }
        }
    }

    @Override
    public void calculateEnergyDayStatByDate(java.util.Date date){
        // 查出所有的表
        List<EnergyMeter> meters = meterProvider.listEnergyMeters();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Timestamp todayBegin = getDayBegin(cal);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Timestamp yesterdayBegin = getDayBegin(cal);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        // Timestamp dayBeforeYestBegin = getDayBegin(cal);
        for(EnergyMeter meter : meters){
            try {

                EnergyDateStatistic dayStat = ConvertHelper.convert(meter, EnergyDateStatistic.class);
                // 前天的最后的读数
                EnergyMeterReadingLog dayBeforeYestLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),null,yesterdayBegin);
                // 昨天的最后的读数
                EnergyMeterReadingLog yesterdayLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),null,todayBegin);

                // 读表用量差
                BigDecimal amount = new BigDecimal("0");

                // 默认初始值为表的初始值，如果有昨天之前和前天之前的读表记录则置换初始值
                BigDecimal readingAnchor = meter.getStartReading();
                // 统计当天的开始读数
                BigDecimal dayLastReading = meter.getStartReading();
                // 统计当天的最后读数
                BigDecimal dayCurrReading = meter.getStartReading();
                if(null != dayBeforeYestLastLog){
                    readingAnchor = dayBeforeYestLastLog.getReading();
                    dayLastReading =  dayBeforeYestLastLog.getReading();
                }

                if(null != yesterdayLastLog){
                    dayCurrReading = yesterdayLastLog.getReading();
                }

                // 拿出单个表统计当天的所有的读表记录
                List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByDate(meter.getId(),yesterdayBegin,todayBegin);

                // 重置flag
                Byte resetFlag = TrueOrFalseFlag.FALSE.getCode();
                // 换表flag
                Byte changeFlag = TrueOrFalseFlag.FALSE.getCode();

                //查看是否有换表,是否有归零
                if(meterReadingLogs != null) {
                    for(EnergyMeterReadingLog log : meterReadingLogs) {
                        // 有归零 量程设置为最大值-锚点,锚点设置为0
                        if(TrueOrFalseFlag.fromCode(log.getResetMeterFlag()) == TrueOrFalseFlag.TRUE) {
                            resetFlag = TrueOrFalseFlag.TRUE.getCode();
                            amount = amount.add(meter.getMaxReading().subtract(readingAnchor));
                            readingAnchor = new BigDecimal(0);
                            dayStat.setResetMeterFlag(resetFlag);
                        }
                        // 有换表 量程加上旧表读数-锚点,锚点重置为新读数
                        if(TrueOrFalseFlag.fromCode(log.getChangeMeterFlag()) == TrueOrFalseFlag.TRUE) {
                            changeFlag = TrueOrFalseFlag.TRUE.getCode();
                            EnergyMeterChangeLog changeLog = this.meterChangeLogProvider.getEnergyMeterChangeLogByLogId(log.getId());
                            amount = amount.add(changeLog.getOldReading().subtract(readingAnchor));
                            readingAnchor = changeLog.getNewReading();
                            dayStat.setChangeMeterFlag(changeFlag);
                        }
                    }
                }

                //计算当天走了多少字 量程+昨天最后一次读数-锚点
                amount = amount.add(dayCurrReading.subtract(readingAnchor));
                dayStat.setCurrentAmount(amount);
                LOGGER.info("dayStat amount : {}", dayStat);

                //获取公式,计算当天的费用
//                EnergyMeterSettingLog priceSetting  = meterSettingLogProvider
//                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.PRICE,yesterdayBegin);
                EnergyMeterSettingLog rateSetting   = meterSettingLogProvider
                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.RATE ,yesterdayBegin);
                EnergyMeterSettingLog amountSetting = meterSettingLogProvider
                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.AMOUNT_FORMULA ,yesterdayBegin);
//                EnergyMeterSettingLog costSetting   = meterSettingLogProvider
//                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.COST_FORMULA ,yesterdayBegin);

//                Stream<EnergyMeterSettingLog> settings = Stream.of(amountSetting, rateSetting, priceSetting, costSetting);
                Stream<EnergyMeterSettingLog> settings = Stream.of(amountSetting, rateSetting);
                if (settings.anyMatch(Objects::isNull)) {
                    LOGGER.error("not found energy meter settings, meterId={}, rateSetting={}, amountSetting={}",
                            meter.getId(), rateSetting, amountSetting);
                    continue;
                }
//                String amountFormula = meterFormulaProvider.findById(amountSetting.getNamespaceId(), amountSetting.getFormulaId()).getExpression();
//                String costFormula = meterFormulaProvider.findById(costSetting.getNamespaceId(), costSetting.getFormulaId()).getExpression();

                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("js");

                engine.put(MeterFormulaVariable.AMOUNT.getCode(), amount);
                //分段计费后 settingValue可能为null
//                engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
                engine.put(MeterFormulaVariable.TIMES.getCode(), rateSetting.getSettingValue());

                BigDecimal realAmount = new BigDecimal(0);
                BigDecimal realCost = new BigDecimal(0);

//                try {
//                	double ra = Double.valueOf(engine.eval(amountFormula).toString());
//                    realAmount = BigDecimal.valueOf(ra);
//                    engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), realAmount);
////                    realCost = BigDecimal.valueOf((double) engine.eval(costFormula));
//                } catch (ScriptException e) {
//                    String paramsStr = "{AMOUNT:" + amount +
//                            ", TIMES:" + rateSetting.getSettingValue() +
//                            "}";
//                    LOGGER.error("evaluate formula error, amountFormula={}, params={}", amountFormula, paramsStr);
//                    e.printStackTrace();
//                    throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "evaluate formula error", e);
//                }

//                if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(
//                        PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
//                    engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), amount);
//                    realCost = calculateStandingChargeTariff(engine, priceSetting, costFormula);
//                }

//                if(PriceCalculationType.BLOCK_TARIFF.equals(
//                        PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
//                    realCost = calculateBlockTariff(manager,priceSetting,amount, costFormula);
//                }

                //删除昨天的记录（手工刷的时候）
                energyDateStatisticProvider.deleteEnergyDateStatisticByDate(meter.getId(), new Date(yesterdayBegin.getTime()));

                //写数据库
                dayStat.setMeterId(meter.getId());
                dayStat.setStatDate(new Date(yesterdayBegin.getTime()));
                dayStat.setMeterName(meter.getName());
                dayStat.setMeterBill(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getBillCategoryId()).getName());
                dayStat.setMeterService(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getServiceCategoryId()).getName());
                dayStat.setMeterRate(rateSetting.getSettingValue());
//                dayStat.setMeterPrice(priceSetting.getSettingValue());
                dayStat.setLastReading(dayLastReading);
                dayStat.setCurrentReading(dayCurrReading);
//                dayStat.setCurrentAmount(amount);
                dayStat.setCurrentCost(realCost);
                dayStat.setResetMeterFlag(resetFlag);
                dayStat.setChangeMeterFlag(changeFlag);
                dayStat.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                // dayStat.setCreatorUid(UserContext.current().getUser().getId());
                dayStat.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                energyDateStatisticProvider.createEnergyDateStatistic(dayStat);
            } catch (Exception e) {
                throw new RuntimeException(String.format("meter id = %s, meterName = %s", meter.getId(), meter.getName()), e);
            }
        }
    }
//能耗3.0开始 费用到缴费里面算
//    private BigDecimal calculateStandingChargeTariff(ScriptEngine engine, EnergyMeterSettingLog priceSetting, String costFormula) {
//        engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
//        BigDecimal realCost = new BigDecimal(0);
//        try {
//            realCost = BigDecimal.valueOf(Double.valueOf(engine.eval(costFormula).toString()));
//        } catch (ScriptException e) {
//            String paramsStr = "{PRICE:" + priceSetting.getSettingValue() +
//                    ", AMOUNT:" + engine.get(MeterFormulaVariable.AMOUNT.getCode()) +
//                    ", TIMES:" + engine.get(MeterFormulaVariable.TIMES.getCode()) +
//                    "}";
//            LOGGER.error("evaluate formula error, costFormula={}, params={}", costFormula, paramsStr);
//            e.printStackTrace();
//            throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "evaluate formula error", e);
//        }
//
//        return realCost;
//    }

//    private BigDecimal calculateBlockTariff(ScriptEngineManager manager, EnergyMeterSettingLog priceSetting, BigDecimal realAmount, String costFormula) {
//        EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(priceSetting.getConfigId());
//        EnergyMeterPriceConfigDTO priceConfigDTO = toEnergyMeterPriceConfigDTO(priceConfig);
//        BigDecimal totalCost = new BigDecimal(0);
//        if(priceConfigDTO != null && priceConfigDTO.getExpression() != null) {
//            ScriptEngine engine = manager.getEngineByName("javascript");
//            List<EnergyMeterRangePriceDTO> rangePriceDTOs = priceConfigDTO.getExpression().getRangePrice();
//            if(rangePriceDTOs != null && rangePriceDTOs.size() > 0) {
//                BigDecimal zero = new BigDecimal(0);
//
//                for(EnergyMeterRangePriceDTO rangePriceDTO : rangePriceDTOs) {
//
//                    BigDecimal minValue = StringUtils.isEmpty(rangePriceDTO.getMinValue()) ? new BigDecimal(-1) : new BigDecimal(rangePriceDTO.getMinValue());
//                    BigDecimal maxValue = StringUtils.isEmpty(rangePriceDTO.getMaxValue()) ? new BigDecimal(-1) : new BigDecimal(rangePriceDTO.getMaxValue());
//                    RangeBoundaryType lowerBoundary = RangeBoundaryType.fromCode(rangePriceDTO.getLowerBoundary());
//                    RangeBoundaryType upperBoundary = RangeBoundaryType.fromCode(rangePriceDTO.getUpperBoundary());
//                    //在区间内
//                    if((minValue.compareTo(zero) < 0 || calculateLowerBoundary(lowerBoundary, minValue, realAmount))
//                            && (maxValue.compareTo(zero) < 0  || calculateUpperBoundary(upperBoundary, maxValue, realAmount))) {
//                        engine.put(MeterFormulaVariable.PRICE.getCode(),rangePriceDTO.getPrice());
//                        engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), realAmount.subtract(minValue));
//                        totalCost.add(calculateBlockTariffByCostFormula(engine, costFormula));
//
//                    }
//                    //比该区间最大值大
//                    if(maxValue.compareTo(zero) >= 0 && greaterThanMax(upperBoundary, maxValue, realAmount)) {
//                        if(minValue.compareTo(zero) < 0) {
//                            engine.put(MeterFormulaVariable.PRICE.getCode(),rangePriceDTO.getPrice());
//                            engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), maxValue.subtract(zero));
//                            totalCost.add(calculateBlockTariffByCostFormula(engine, costFormula));
//                        } else {
//                            engine.put(MeterFormulaVariable.PRICE.getCode(),rangePriceDTO.getPrice());
//                            engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), maxValue.subtract(minValue));
//                            totalCost.add(calculateBlockTariffByCostFormula(engine, costFormula));
//
//                        }
//
//                    }
//                    //比该区间最小值小则不计算
//                }
//            }
//        }
//        return totalCost;
//    }
//
//    private BigDecimal calculateBlockTariffByCostFormula(ScriptEngine engine, String costFormula) {
//        BigDecimal cost = new BigDecimal(0);
//        try {
//            cost.add(BigDecimal.valueOf(Double.valueOf(engine.eval(costFormula).toString())));
//        } catch (ScriptException e) {
//            String paramsStr = "{PRICE:" + engine.get(MeterFormulaVariable.REAL_AMOUNT.getCode()) +
//                    ", REALAMOUNT:" + engine.get(MeterFormulaVariable.AMOUNT.getCode()) +
//                    "}";
//            LOGGER.error("evaluate formula error, costFormula={}, params={}", costFormula, paramsStr);
//            e.printStackTrace();
//            throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "evaluate formula error", e);
//        }
//        return cost;
//    }

    private Boolean calculateLowerBoundary(RangeBoundaryType lowerBoundary, BigDecimal minValue, BigDecimal realAmount) {
        Boolean rangeFlag = false;
        switch (lowerBoundary) {
            case LESS:
                rangeFlag = (minValue.compareTo(realAmount) < 0);
                break;

            case LESS_AND_EQUAL:
                rangeFlag = (minValue.compareTo(realAmount) <= 0);
                break;
        }

        return rangeFlag;
    }

    private Boolean calculateUpperBoundary(RangeBoundaryType upperBoundary, BigDecimal maxValue, BigDecimal realAmount) {
        Boolean rangeFlag = false;
        switch (upperBoundary) {
            case GREATER:
                rangeFlag = (maxValue.compareTo(realAmount) > 0);
                break;

            case GREATER_AND_EQUAL:
                rangeFlag = (maxValue.compareTo(realAmount) >= 0);
                break;
        }

        return rangeFlag;
    }

    private Boolean greaterThanMax(RangeBoundaryType upperBoundary, BigDecimal maxValue, BigDecimal realAmount) {
        Boolean flag = false;
        switch (upperBoundary) {
            case GREATER:
                flag = (maxValue.compareTo(realAmount) <= 0);
                break;

            case GREATER_AND_EQUAL:
                flag = (maxValue.compareTo(realAmount) < 0);
                break;
        }

        return flag;
    }

    /**
     * 每月1日  早上3点10分刷前一个月的读表
     * */
    @Scheduled(cron = "0 10 3 1 * ?")
    public void calculateEnergyMonthStat() {

        //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_MONTH_STAT_SCHEDULE.getCode()).tryEnter(() -> {
                try {
                    LOGGER.info("calculate energy month stat start...");
                    calculateEnergyMonthStatByDate(DateHelper.currentGMTTime());
                    LOGGER.info("calculate energy month stat end...");
                } catch (Exception e) {
                    LOGGER.error("calculate energy month stat error...", e);
                    sendErrorMessage(e);
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void calculateEnergyMonthStatByDate(java.util.Date date){
        //查出所有的表
        List<EnergyMeter> meters = meterProvider.listEnergyMeters();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //减一个月取月末和月初
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Timestamp monthEnd = getDayBegin(cal);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Timestamp monthBegin = getDayBegin(cal);
        cal.add(Calendar.YEAR, -1);
        Timestamp lastYear = getDayBegin(cal);
        BigDecimal zero = new BigDecimal(0);
        ScriptEngineManager manager = new ScriptEngineManager();
        for(EnergyMeter meter : meters){

            //取月初的上次度数和月末的当前读数
            EnergyDateStatistic monthEndStat = energyDateStatisticProvider.getEnergyDateStatisticByStatDate(meter.getId(),new Date(monthEnd.getTime()));
            EnergyMonthStatistic monthStat = ConvertHelper.convert(monthEndStat, EnergyMonthStatistic.class);
            if (null == monthEndStat) {
                LOGGER.error("energy meter monthEndStat is null, meterId={}, month={}", meter.getId(), monthEnd.toLocalDateTime());
                continue;
            }
            monthStat.setMeterId(meter.getId());
            monthStat.setDateStr(monthSF.get().format(monthBegin));
            EnergyDateStatistic monthBeginStat = energyDateStatisticProvider.getEnergyDateStatisticByStatDate(meter.getId(),new Date(monthBegin.getTime()));

            if (null == monthBeginStat) {
                LOGGER.error("energy meter monthBeginStat is null, meterId={}, month={}", meter.getId(), monthBegin.toLocalDateTime());
                continue;
            }
            monthStat.setLastReading(monthBeginStat.getLastReading());
            //统计该表sum 用量和费用
            BigDecimal currentAmount = energyDateStatisticProvider.getSumAmountBetweenDate(meter.getId(),new Date(monthBegin.getTime()),new Date(monthEnd.getTime()));
            monthStat.setCurrentAmount(currentAmount);
            //收费按最新规则算
            EnergyMeterSettingLog priceSetting  = meterSettingLogProvider
                    .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.PRICE,monthEnd);
            EnergyMeterSettingLog costSetting   = meterSettingLogProvider
                    .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.COST_FORMULA ,monthEnd);

           // String costFormula = meterFormulaProvider.findById(costSetting.getNamespaceId(), costSetting.getFormulaId()).getExpression();

            BigDecimal realCost = new BigDecimal(0);

//            if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(
//                    PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
                ScriptEngine engine = manager.getEngineByName("js");
                engine.put(MeterFormulaVariable.AMOUNT.getCode(), currentAmount);
                //由于currentAmount其实是realAmount  已经算了一遍times，所以此处times赋值为1 by xiongying20170401
                engine.put(MeterFormulaVariable.TIMES.getCode(), 1);
                engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), currentAmount);
//                realCost = calculateStandingChargeTariff(engine, priceSetting, costFormula);
//            }

//            if(PriceCalculationType.BLOCK_TARIFF.equals(
//                    PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
//                realCost = calculateBlockTariff(manager,priceSetting,currentAmount, costFormula);
//            }
//            monthStat.setCurrentCost(realCost);

            //delete
            energyMonthStatisticProvider.deleteEnergyMonthStatisticByDate(meter.getId(), monthSF.get().format(monthBegin));
            //写数据库

            energyMonthStatisticProvider.createEnergyMonthStatistic(monthStat);
        }
        //总量记录表 -用sql汇总
        List<EnergyCountStatistic> countStats = energyMonthStatisticProvider.listEnergyCountStatistic(monthSF.get().format(monthBegin));
        if(null != countStats){
            List<Long> comIds = new ArrayList<Long>();
            for(EnergyCountStatistic cs : countStats){

                cs.setStatus(EnergyCommonStatus.ACTIVE.getCode());
//				cs.setCreatorUid(UserContext.current().getUser().getId());
                cs.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                        .getTime()));
                energyCountStatisticProvider.createEnergyCountStatistic(cs);
                comIds.add(cs.getCommunityId());
            }
            //各项目月水电能耗情况-同比
            for(Long comId : comIds){
                Community com = communityProvider.findCommunityById(comId);
                EnergyYoyStatistic yoy = new EnergyYoyStatistic();
                yoy.setNamespaceId(com.getNamespaceId());
                yoy.setCommunityId(com.getId());
                yoy.setAreaSize(com.getAreaSize());
                yoy.setDateStr(monthSF.get().format(monthBegin));
                // 通过sql计算每一个值
                //本月-水费-账单项目-应收
                yoy.setWaterReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //本月-水费-账单项目-应付
                yoy.setWaterPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setWaterBurdenAmount(yoy.getWaterPayableAmount().subtract(yoy.getWaterReceivableAmount()));
                yoy.setWaterAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getWaterBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));


                //本月-电费-账单项目-应收
                yoy.setElectricReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //本月-电费-账单项目-应付
                yoy.setElectricPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setElectricBurdenAmount(yoy.getElectricPayableAmount().subtract(yoy.getElectricReceivableAmount()));
                yoy.setElectricAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getElectricBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));



                //去年本月-水费-账单项目-应收
                yoy.setWaterLastReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //去年本月-水费-账单项目-应付
                yoy.setWaterLastPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setWaterLastBurdenAmount(yoy.getWaterLastPayableAmount().subtract(yoy.getWaterLastReceivableAmount()));
                yoy.setWaterLastAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getWaterLastBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));


                //去年本月-电费-账单项目-应收
                yoy.setElectricLastReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //去年本月-电费-账单项目-应付
                yoy.setElectricLastPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setElectricLastBurdenAmount(yoy.getElectricLastPayableAmount().subtract(yoy.getElectricLastReceivableAmount()));
                yoy.setElectricLastAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getElectricLastBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));


                yoy.setStatus(EnergyCommonStatus.ACTIVE.getCode());
//				yoy.setCreatorUid(UserContext.current().getUser().getId());
                yoy.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                        .getTime()));
                energyYoyStatisticProvider.deleteEnergyYoyStatistic(yoy.getCommunityId(), yoy.getDateStr());
                energyYoyStatisticProvider.createEnergyYoyStatistic(yoy);
            }

        }

    }

    @Override
    public void syncEnergyMeterReadingLogIndex() {
        readingLogSearcher.syncFromDb();
    }

    @Override
    public void syncEnergyMeterIndex() {
        meterSearcher.syncFromDb();
    }

    @Override
    public EnergyMeterPriceConfigDTO createEnergyMeterPriceConfig(CreateEnergyMeterPriceConfigCommand cmd) {
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        }
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOwnerId(),  cmd.getCommunityId());

        EnergyMeterPriceConfig priceConfig = ConvertHelper.convert(cmd, EnergyMeterPriceConfig.class);
        priceConfig.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        priceConfig.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        priceConfigProvider.createEnergyMeterPriceConfig(priceConfig);
        return toEnergyMeterPriceConfigDTO(priceConfig);
    }

    @Override
    public EnergyMeterPriceConfigDTO updateEnergyMeterPriceConfig(UpdateEnergyMeterPriceConfigCommand cmd) {
        //暂时没有update
        return null;
    }

    @Override
    public EnergyMeterPriceConfigDTO getEnergyMeterPriceConfig(GetEnergyMeterPriceConfigCommand cmd) {
        EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getCommunityId(),UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));

        return toEnergyMeterPriceConfigDTO(priceConfig);
    }

    @Override
    public List<EnergyMeterPriceConfigDTO> listEnergyMeterPriceConfig(ListEnergyMeterPriceConfigCommand cmd) {
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
        List<EnergyMeterPriceConfig> priceConfig = priceConfigProvider.listPriceConfig(cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getCommunityId(), UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        return priceConfig.stream().map(this::toEnergyMeterPriceConfigDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteEnergyMeterPriceConfig(DelelteEnergyMeterPriceConfigCommand cmd) {
        validate(cmd);
//        checkCurrentUserNotInOrg(cmd.getOwnerId());
//        if(cmd.getCommunityId() == null) {
//            userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        } else {
//            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_SETTING);
//        }
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_SETTING, cmd.getOwnerId(),  cmd.getCommunityId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_PRICE_CONFIG.getCode() + cmd.getId()).tryEnter(() -> {
            EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(),
                    cmd.getCommunityId(),UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));

            if(priceConfig != null) {
                EnergyMeterSettingLog settingLog = meterSettingLogProvider.findSettingByPriceConfigId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), priceConfig.getId());
                if(settingLog != null) {
                    LOGGER.info("The price config has been reference, config id = {}", priceConfig.getId());
                    throw errorWith(SCOPE, ERR_PRICE_CONFIG_HAS_BEEN_REFERENCE, "The price config has been reference");
                } else {
                    priceConfigProvider.deletePriceConfig(priceConfig);
                }
            }
        });
    }

    @Override
    public void createEnergyMeterDefaultSetting(CreateEnergyMeterDefaultSettingCommand cmd) {
        if(cmd != null && cmd.getDefaultSettings() != null) {
            cmd.getDefaultSettings().forEach(defaultSetting -> {
                EnergyMeterDefaultSetting df = ConvertHelper.convert(defaultSetting, EnergyMeterDefaultSetting.class);
                defaultSettingProvider.createEnergyMeterDefaultSetting(df);
            });
        }
    }

    @Override
    public List<EnergyMeterDefaultSettingTemplateDTO> listEnergyDefaultSettingTemplates() {
        List<EnergyMeterDefaultSetting> defaultSettings = defaultSettingProvider.listDefaultSetting(0L, "", 0L, 0, null);
        if(defaultSettings != null) {
            List<EnergyMeterDefaultSettingTemplateDTO> dtos = defaultSettings.stream().map(defaultSetting -> {
                EnergyMeterDefaultSettingTemplateDTO dto = ConvertHelper.convert(defaultSetting, EnergyMeterDefaultSettingTemplateDTO.class);
                return dto;
            }).collect(Collectors.toList());

            return dtos;
        }
        return null;
    }

    @Deprecated
    @Override
    public ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {
        if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {
            if(null == cmd.getOrganizationId()) {
                LOGGER.error("Not privilege", cmd);
                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
                        "Not privilege");
            }
        }

        ListAuthorizationCommunityByUserResponse response = new ListAuthorizationCommunityByUserResponse();

        ListUserRelatedProjectByModuleCommand listUserRelatedProjectByModuleCommand = new ListUserRelatedProjectByModuleCommand();
        listUserRelatedProjectByModuleCommand.setOrganizationId(cmd.getOrganizationId());
        listUserRelatedProjectByModuleCommand.setModuleId(49100L);

        List<CommunityDTO> dtos = serviceModuleService.listUserRelatedCommunityByModuleId(listUserRelatedProjectByModuleCommand);

        if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {
            SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
            User user = UserContext.current().getUser();
            List<CommunityDTO> result = new ArrayList<>();
            dtos.forEach(r -> {
                if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), r.getId(), cmd.getOrganizationId(), PrivilegeConstants.REPLACE_CREATE_TASK)) {
                    result.add(r);
                }
            });
            response.setCommunities(result);

        }else{
            response.setCommunities(dtos);
        }
        return response;

    }

    private EnergyMeterPriceConfigDTO toEnergyMeterPriceConfigDTO(EnergyMeterPriceConfig priceConfig) {
        EnergyMeterPriceConfigDTO dto = ConvertHelper.convert(priceConfig, EnergyMeterPriceConfigDTO.class);

        if(priceConfig.getExpression() != null) {
            dto.setExpression(analyzeExpression(priceConfig.getExpression()));
        }
        return dto;
    }

    private EnergyMeterPriceConfigExpressionDTO analyzeExpression(String expression) {
        Gson gson = new Gson();
        EnergyMeterPriceConfigExpressionDTO expressionDTO = gson.fromJson(expression, new TypeToken<EnergyMeterPriceConfigExpressionDTO>() {}.getType());
        return expressionDTO;
    }

    @Override
    public EnergyMeterDTO findEnergyMeterByQRCode(FindEnergyMeterByQRCodeCommand cmd) {
//        EnergyMeterCodeDTO meterCodeDTO = WebTokenGenerator.getInstance().fromWebToken(cmd.getMeterQRCode(), EnergyMeterCodeDTO.class);
        EnergyMeter meter = meterProvider.findById(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getMeterQRCode());
        if (meter == null || !EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
            LOGGER.error("EnergyMeter not exist, id = {}", cmd.getMeterQRCode());
            throw errorWith(SCOPE, ERR_METER_NOT_EXIST, "The meter is not exist id = %s", cmd.getMeterQRCode());
        }
        //判断表计当前是否有任务
        EnergyMeterTask task = energyMeterTaskProvider.findEnergyMeterTaskByMeterId(meter.getId(), new Timestamp(DateHelper.currentGMTTime().getTime()));
        if(task == null) {
            LOGGER.error("EnergyMeter task not exist, id = {}", cmd.getMeterQRCode());
            throw errorWith(SCOPE, ERR_METER_TASK_NOT_EXIST, "The meter task is not exist id = %s", cmd.getMeterQRCode());
        }
        return toEnergyMeterDTO(meter,cmd.getNamespaceId());
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    @Override
    public String getEnergyMeterQRCode(GetEnergyMeterQRCodeCommand cmd) {
        return generateQRString(cmd.getMeterId(), cmd.getNamespaceId());
    }

    @Override
    public void batchReadEnergyMeter(BatchReadEnergyMeterCommand cmd) {
        if(cmd.getReadList() != null && cmd.getReadList().size() > 0) {
            cmd.getReadList().forEach(read -> {
                this.dbProvider.execute((TransactionStatus status) -> {
                    List<EnergyMeterTask> tasks = energyMeterTaskProvider.listActiveEnergyMeterTasks(read.getMeterId());
                    if(tasks != null && tasks.size() > 0) {
                        Boolean readTask = false;
                        for(EnergyMeterTask task : tasks) {
                            if(task.getExecutiveExpireTime().before(new Timestamp(DateHelper.currentGMTTime().getTime()))
                                    || EnergyTaskStatus.INACTIVE.equals(EnergyTaskStatus.fromCode(task.getStatus()))) {
                                continue;
                            }
//                            userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), task.getTargetId(), task.getOwnerId(), PrivilegeConstants.METER_READ);
                            checkEnergyAuth(task.getNamespaceId(), PrivilegeConstants.METER_READ, task.getOwnerId(), task.getTargetId());
                            EnergyMeter meter = meterProvider.findById(task.getNamespaceId(), task.getMeterId());
                            if(!EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
                                continue;
                            }
                            task.setReading(read.getCurrReading());
                            task.setStatus(EnergyTaskStatus.READ.getCode());
                            task.setOperatorUid(UserContext.currentUserId());
                            task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                            energyMeterTaskProvider.updateEnergyMeterTask(task);
                            energyMeterTaskSearcher.feedDoc(task);
                            readTask = true;
                            read.setTaskId(task.getId());
                            break;
                        }
                        if(readTask) {
                            readEnergyMeter(read);
                        }
                    } else {
                        LOGGER.error("meter id = {} don't have task!",read.getMeterId());
                        throw RuntimeErrorException.errorWith(SCOPE, ERR_METER_NOT_EXIST_TASK,
                                "meter has no exist task");
                    }
                    return null;
                });
            });
        }
    }

    @Override
    public void deleteEnergyPlan(DeleteEnergyPlanCommand cmd) {
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_PLAN_DELETE, cmd.getOwnerId(),  cmd.getTargetId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_PLAN_DELETE);
        EnergyPlan plan = energyPlanProvider.findEnergyPlanById(cmd.getPlanId());
        plan.setStatus(CommonStatus.INACTIVE.getCode());
        plan.setDeleterUid(UserContext.currentUserId());
        plan.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        energyPlanProvider.updateEnergyPlan(plan);
        energyPlanSearcher.feedDoc(plan);

        //刷一下计划关联的表记的状态
        List<EnergyPlanMeterMap> maps = energyPlanProvider.listMetersByEnergyPlan(plan.getId());
        if(maps != null && maps.size() > 0) {
            maps.forEach(map -> {
                EnergyMeter meter = meterProvider.findById(plan.getNamespaceId(), map.getMeterId());
                meterSearcher.feedDoc(meter);
            });

        }

    }

    @Override
    public EnergyPlanDTO findEnergyPlanDetails(FindEnergyPlanDetailsCommand cmd) {
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_PLAN_LIST, cmd.getOwnerId(),  cmd.getTargetId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_PLAN_LIST);
        EnergyPlan plan = energyPlanProvider.findEnergyPlanById(cmd.getPlanId());
        return toEnergyPlanDTO(plan);
    }

    @Override
    public ListEnergyPlanMetersResponse listEnergyPlanMeters(ListEnergyPlanMetersCommand cmd) {
        ListEnergyPlanMetersResponse response = new ListEnergyPlanMetersResponse();
        List<EnergyPlanMeterMap> meterMaps = energyPlanProvider.listMetersByEnergyPlan(cmd.getPlanId());
        if(meterMaps != null && meterMaps.size() > 0) {
            List<EnergyPlanMeterDTO> meters = new ArrayList<>();
            meterMaps.forEach(meter -> {
                EnergyPlanMeterDTO meterDTO = ConvertHelper.convert(meter, EnergyPlanMeterDTO.class);
                EnergyMeter energyMeter = meterProvider.findById(cmd.getNamespaceId(), meter.getMeterId());
                if(energyMeter != null) {
                    meterDTO.setMeterName(energyMeter.getName());
                    meterDTO.setMeterNumber(energyMeter.getMeterNumber());
                    meterDTO.setMeterType(energyMeter.getMeterType());
                    // 表的状态
                    String meterStatus = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_STATUS, String.valueOf(energyMeter.getStatus()), currLocale(), "");
                    meterDTO.setStatus(meterStatus);
                }

                meterDTO.setAddresses(populateEnergyMeterAddresses(meter.getMeterId()));
                meters.add(meterDTO);
            });
            response.setMeters(meters);
            response.setTotal(meters.size());
        }
        return response;
    }

    @Override
    public ListEnergyPlanMetersResponse setEnergyPlanMeterOrder(SetEnergyPlanMeterOrderCommand cmd) {
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_PLAN_CREATE, cmd.getOrganizationId(),  cmd.getCommunityId());
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_PLAN_CREATE);
        ListEnergyPlanMetersResponse response = new ListEnergyPlanMetersResponse();
        if(cmd.getMeters() != null && cmd.getMeters().size() > 0) {
            cmd.getMeters().forEach(meter -> {
                energyPlanProvider.updateEnergyPlanMeterMap(ConvertHelper.convert(meter, EnergyPlanMeterMap.class));
            });
            response.setMeters(cmd.getMeters());
            response.setTotal(cmd.getMeters().size());
        }
        return response;
    }

    @Override
    public ListUserEnergyPlanTasksResponse listUserEnergyPlanTasks(ListUserEnergyPlanTasksCommand cmd) {
        ListUserEnergyPlanTasksResponse response = new ListUserEnergyPlanTasksResponse();
        User user = UserContext.current().getUser();
        int pageSize = cmd.getPageSize() == null ? Integer.MAX_VALUE - 1 : cmd.getPageSize();
        if(null == cmd.getPageAnchor()) {
            cmd.setPageAnchor(0L);
        }
        List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
        List<EnergyPlanGroupMap> maps = energyPlanProvider.lisEnergyPlanGroupMapByGroupAndPosition(groupDtos);
        if (maps != null && maps.size() > 0) {
            List<Long> planIds = maps.stream().map(map -> {
                return map.getPlanId();
            }).collect(Collectors.toList());
            List<EnergyMeterTask> tasks = energyMeterTaskProvider.listEnergyMeterTasksByPlan(planIds, cmd.getTargetId(), cmd.getOwnerId(), cmd.getPageAnchor(), pageSize+1);

            if(tasks != null && tasks.size() > 0) {
                if (tasks.size() > pageSize) {
                    tasks.remove(tasks.size() - 1);
                    response.setNextPageAnchor(tasks.get(tasks.size()-1).getId());
                }

                List<EnergyMeterTaskDTO> taskDTOs = tasks.stream().map(task -> {
                    EnergyMeterTaskDTO dto = ConvertHelper.convert(task, EnergyMeterTaskDTO.class);
                    EnergyMeter meter = meterProvider.findById(task.getNamespaceId(), task.getMeterId());
                    dto.setBillCategoryId(meter.getBillCategoryId());
                    // 项目
                    EnergyMeterCategory billCategory = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(task.getNamespaceId()), meter.getBillCategoryId());
                    dto.setBillCategory(billCategory != null ? billCategory.getName() : null);

                    dto.setMeterName(meter.getName());
                    dto.setMeterNumber(meter.getMeterNumber());
                    dto.setMeterType(meter.getMeterType());
                    dto.setMaxReading(meter.getMaxReading());
                    dto.setStartReading(meter.getStartReading());
                    // 日读表差
                    dto.setDayPrompt(this.processDayPrompt(meter, meter.getNamespaceId()));
                    // 月读表差
                    dto.setMonthPrompt(this.processMonthPrompt(meter, meter.getNamespaceId()));
                    List<EnergyMeterAddress> addressMap = energyMeterAddressProvider.listByMeterId(task.getMeterId());
                    if(addressMap != null && addressMap.size() > 0) {
                        dto.setApartmentFloor(addressMap.get(0).getApartmentFloor());
                        dto.setBuildingId(addressMap.get(0).getBuildingId());
                        dto.setBuildingName(addressMap.get(0).getBuildingName());
                        dto.setApartmentName(addressMap.get(0).getApartmentName());
                    }
                    return dto;
                }).collect(Collectors.toList());
                response.setTaskDTOs(taskDTOs);
            }

        }
        return response;
    }

    private List<ExecuteGroupAndPosition> listUserRelateGroups() {
        Long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();

        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(user.getId());
        if(members == null || members.size() == 0) {
            return new ArrayList<>();
        }

        List<ExecuteGroupAndPosition> groupDtos = new ArrayList<ExecuteGroupAndPosition>();
        for (OrganizationMember member : members) {
            Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());

            if (organization != null) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("listUserRelateGroups, organizationId=" + organization.getId());
                }
                if (OrganizationGroupType.JOB_POSITION.equals(OrganizationGroupType.fromCode(organization.getGroupType()))) {
                    //部门岗位
                    ExecuteGroupAndPosition departmentGroup = new ExecuteGroupAndPosition();
                    departmentGroup.setGroupId(organization.getParentId());
                    departmentGroup.setPositionId(organization.getId());
                    groupDtos.add(departmentGroup);

                    //通用岗位
                    List<OrganizationJobPositionMap> maps = organizationProvider.listOrganizationJobPositionMaps(organization.getId());
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("listUserRelateGroups, organizationId = {}, OrganizationJobPositionMaps = {}", organization.getId(), maps);
                    }

                    if (maps != null && maps.size() > 0) {
                        for (OrganizationJobPositionMap map : maps) {
                            String[] path = organization.getPath().split("/");
                            Long organizationId = Long.valueOf(path[1]);
                            ExecuteGroupAndPosition topGroup = new ExecuteGroupAndPosition();
                            topGroup.setGroupId(organizationId);
                            topGroup.setPositionId(map.getJobPositionId());
                            groupDtos.add(topGroup);
                        }

                    }
                } else {
                    ExecuteGroupAndPosition group = new ExecuteGroupAndPosition();
                    group.setGroupId(organization.getId());
                    group.setPositionId(0L);
                    groupDtos.add(group);
                }

            }
        }

        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("listUserRelateGroups, groupDtos = {}" , groupDtos);
        }

        Long endTime = System.currentTimeMillis();
        LOGGER.debug("TrackUserRelatedCost: listUserRelateGroups userId = " + user.getId() + ", elapse=" + (endTime - startTime));
        return groupDtos;
    }

    @Override
    public ReadTaskMeterOfflineResponse readTaskMeterOffline(ReadTaskMeterOfflineCommand cmd) {
        ReadTaskMeterOfflineResponse response = new ReadTaskMeterOfflineResponse();
        if(cmd.getMeterReading() != null && cmd.getMeterReading().size() > 0) {
            List<ReadTaskMeterOfflineResultLog> logs = new ArrayList<>();
            cmd.getMeterReading().forEach(read -> {
                ReadTaskMeterOfflineResultLog log = new ReadTaskMeterOfflineResultLog();
                log.setTaskId(read.getTaskId());
                EnergyMeterTask task = energyMeterTaskProvider.findEnergyMeterTaskById(read.getTaskId());
                if (task == null) {
                    LOGGER.error("EnergyTask not exist, id = {}", read.getTaskId());
                    log.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
                    log.setErrorDescription(localeStringService.getLocalizedString(String.valueOf(EnergyConsumptionServiceErrorCode.SCOPE),
                            String.valueOf(EnergyConsumptionServiceErrorCode.ERR_METER_TASK_NOT_EXIST),
                            UserContext.current().getUser().getLocale(),"EnergyTask not exist"));
                    logs.add(log);
                    return;
                }
                if(task.getExecutiveExpireTime().before(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
                    LOGGER.error("EnergyTask already close, id = {}, expire time: {}", read.getTaskId(), task.getExecutiveExpireTime());
                    log.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
                    log.setErrorDescription(localeStringService.getLocalizedString(String.valueOf(EnergyConsumptionServiceErrorCode.SCOPE),
                            String.valueOf(EnergyConsumptionServiceErrorCode.ERR_METER_TASK_ALREADY_CLOSE),
                            UserContext.current().getUser().getLocale(),"EnergyTask already close"));
                    logs.add(log);
                    return;
                }

                EnergyMeter meter = meterProvider.findById(task.getNamespaceId(), task.getMeterId());
                if(!EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
                    LOGGER.error("EnergyTask meter status is not active, meter = {}", meter);
                    log.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
                    log.setErrorDescription(localeStringService.getLocalizedString(String.valueOf(EnergyConsumptionServiceErrorCode.SCOPE),
                            String.valueOf(EnergyConsumptionServiceErrorCode.ERR_METER_NOT_EXIST),
                            UserContext.current().getUser().getLocale(),"EnergyTask meter status is not active"));
                    logs.add(log);
                    return;
                }

                // 读数大于最大量程
                if (read.getCurrReading().doubleValue() > meter.getMaxReading().doubleValue()) {
                    LOGGER.error("Current reading greater then meter max reading, meterId = ", meter.getId());
                    log.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
                    log.setErrorDescription(localeStringService.getLocalizedString(String.valueOf(EnergyConsumptionServiceErrorCode.SCOPE),
                            String.valueOf(EnergyConsumptionServiceErrorCode.ERR_CURR_READING_GREATER_THEN_MAX_READING),
                            UserContext.current().getUser().getLocale(),"Current reading greater then meter max reading"));
                    logs.add(log);
                    return;
                }

                task.setReading(read.getCurrReading());
                task.setStatus(EnergyTaskStatus.READ.getCode());
                task.setOperatorUid(UserContext.currentUserId());
                task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                energyMeterTaskProvider.updateEnergyMeterTask(task);
                energyMeterTaskSearcher.feedDoc(task);


                EnergyMeterReadingLog readingLog = new EnergyMeterReadingLog();
                readingLog.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                readingLog.setTaskId(read.getTaskId());
                readingLog.setReading(read.getCurrReading());
                readingLog.setCommunityId(meter.getCommunityId());
                readingLog.setMeterId(meter.getId());
                readingLog.setNamespaceId(UserContext.getCurrentNamespaceId(task.getNamespaceId()));
                readingLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                readingLog.setOperatorId(UserContext.currentUserId());
                readingLog.setResetMeterFlag(read.getResetMeterFlag() != null ? read.getResetMeterFlag() : TrueOrFalseFlag.FALSE.getCode());
                dbProvider.execute(r -> {
                    meterReadingLogProvider.createEnergyMeterReadingLog(readingLog);
                    meter.setLastReading(readingLog.getReading());
                    meter.setLastReadTime(Timestamp.valueOf(LocalDateTime.now()));
                    meterProvider.updateEnergyMeter(meter);
                    return true;
                });
                readingLogSearcher.feedDoc(readingLog);
                meterSearcher.feedDoc(meter);

                log.setErrorCode(ErrorCodes.SUCCESS);
                logs.add(log);
            });
            response.setLogs(logs);
        }
        return response;
    }

    @Override
    public void readTaskMeter(ReadTaskMeterCommand cmd) {
        EnergyMeterTask task = energyMeterTaskProvider.findEnergyMeterTaskById(cmd.getTaskId());
        if (task == null) {
            LOGGER.error("EnergyTask not exist, id = {}", cmd.getTaskId());
            throw errorWith(SCOPE, ERR_METER_TASK_NOT_EXIST, "The meter task is not exist id = %s", cmd.getTaskId());
        }
        if(task.getExecutiveExpireTime().before(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
            LOGGER.error("EnergyTask already close, id = {}, expire time: {}", cmd.getTaskId(), task.getExecutiveExpireTime());
            throw errorWith(SCOPE, ERR_METER_TASK_ALREADY_CLOSE, "The meter task is already close id = %s", cmd.getTaskId());

        }

        EnergyMeter meter = meterProvider.findById(task.getNamespaceId(), task.getMeterId());
        if(!EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
            LOGGER.error("EnergyTask meter status is not active, meter = {}", meter);
            throw errorWith(SCOPE, ERR_METER_NOT_EXIST, "The meter status is not active meter id = %s", meter.getId());
        }
        task.setReading(cmd.getCurrReading());
        task.setStatus(EnergyTaskStatus.READ.getCode());
        task.setOperatorUid(UserContext.currentUserId());
        task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        energyMeterTaskProvider.updateEnergyMeterTask(task);
        energyMeterTaskSearcher.feedDoc(task);
        ReadEnergyMeterCommand command = ConvertHelper.convert(cmd, ReadEnergyMeterCommand.class);
        command.setMeterId(task.getMeterId());
        command.setTaskId(task.getId());
        command.setNamespaceId(task.getNamespaceId());
        command.setCommunityId(cmd.getCommunityId());
        command.setOrganizationId(cmd.getOrganizationId());
        readEnergyMeter(command);
    }

    private RepeatSettings dealEnergyPlanRepeat(RepeatSettingsDTO dto) {
        if(dto == null) {
            throw RuntimeErrorException.errorWith(RepeatServiceErrorCode.SCOPE,
                    RepeatServiceErrorCode.ERROR_REPEAT_SETTING_NOT_EXIST,
                    "执行周期为空");
        }

        RepeatSettings repeat = ConvertHelper.convert(dto, RepeatSettings.class);
        if(dto.getStartDate() != null)
            repeat.setStartDate(new Date(dto.getStartDate()));
        if(dto.getEndDate() != null)
            repeat.setEndDate(new Date(dto.getEndDate()));

        if(repeat.getId() == null) {
            repeat.setCreatorUid(UserContext.currentUserId());
            repeatService.createRepeatSettings(repeat);
        } else {
            repeatService.updateRepeatSettings(repeat);
        }
        return repeat;
    }

    private void dealEnergyPlanGroups(EnergyPlan plan, List<EnergyPlanGroupDTO> groups) {
        //groups有的从maps去掉，groups没有的删除，maps没有的新增
        Map<Long, EnergyPlanGroupMap> maps = energyPlanProvider.listGroupMapsByEnergyPlan(plan.getId());
        if(groups != null && groups.size() > 0) {
            groups.forEach(group -> {
                if(group.getId() != null) {
                    maps.remove(group.getId());
                } else {
                    EnergyPlanGroupMap groupMap = ConvertHelper.convert(group, EnergyPlanGroupMap.class);
                    groupMap.setPlanId(plan.getId());
                    energyPlanProvider.createEnergyPlanGroupMap(groupMap);
                }
            });
        }
        if(maps != null && maps.size() > 0) {
            maps.forEach((id, groupMap) -> {
                energyPlanProvider.deleteEnergyPlanGroupMap(groupMap);
            });
        }


    }

    private void dealEnergyPlanMeters(EnergyPlan plan, List<EnergyPlanMeterDTO> meters) {
        //meters有的从maps去掉，meters没有的删除，maps没有的新增
        Map<Long, EnergyPlanMeterMap> maps = energyPlanProvider.listMeterMapsByEnergyPlan(plan.getId());
        if(meters != null && meters.size() > 0) {
            meters.forEach(meter -> {
                if(meter.getId() != null) {
                    maps.remove(meter.getId());
                } else {
                    EnergyPlanMeterMap meterMap = ConvertHelper.convert(meter, EnergyPlanMeterMap.class);
                    meterMap.setPlanId(plan.getId());
                    energyPlanProvider.createEnergyPlanMeterMap(meterMap);
                    //刷一下计划关联的表记的状态
                    EnergyMeter energyMeter = meterProvider.findById(plan.getNamespaceId(), meterMap.getMeterId());
                    meterSearcher.feedDoc(energyMeter);
                }
            });
        }

        if(maps != null && maps.size() > 0) {
            maps.forEach((id, meterMap) -> {
                energyPlanProvider.deleteEnergyPlanMeterMap(meterMap);
                //刷一下计划关联的表记的状态
                EnergyMeter meter = meterProvider.findById(plan.getNamespaceId(), meterMap.getMeterId());
                meterSearcher.feedDoc(meter);

            });
        }

    }

    private EnergyPlanDTO toEnergyPlanDTO(EnergyPlan plan) {
        EnergyPlanDTO dto = ConvertHelper.convert(plan, EnergyPlanDTO.class);
        if(null != plan.getRepeatSettingId() && plan.getRepeatSettingId() != 0) {
            RepeatSettings repeat = repeatService.findRepeatSettingById(plan.getRepeatSettingId());
            RepeatSettingsDTO repeatDTO = ConvertHelper.convert(repeat, RepeatSettingsDTO.class);
            repeatDTO.setStartDate(repeat.getStartDate().getTime());
            repeatDTO.setEndDate(repeat.getEndDate().getTime());
            dto.setRepeat(repeatDTO);
        }
        List<EnergyPlanMeterMap> meterMaps = energyPlanProvider.listMetersByEnergyPlan(plan.getId());
        if(meterMaps != null && meterMaps.size() > 0) {
            List<EnergyPlanMeterDTO> meters = new ArrayList<>();
            meterMaps.forEach(meterMap -> {
                EnergyPlanMeterDTO meterDTO = ConvertHelper.convert(meterMap, EnergyPlanMeterDTO.class);
                EnergyMeter meter = meterProvider.findById(plan.getNamespaceId(), meterMap.getMeterId());
                if(meter != null) {
                    meterDTO.setMeterName(meter.getName());
                    meterDTO.setMeterNumber(meter.getMeterNumber());
                    meterDTO.setMeterType(meter.getMeterType());
                    // 表的状态
                    String meterStatus = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_STATUS, String.valueOf(meter.getStatus()), currLocale(), "");
                    meterDTO.setStatus(meterStatus);
                }

                meterDTO.setAddresses(populateEnergyMeterAddresses(meterMap.getMeterId()));
                meters.add(meterDTO);
            });
            dto.setMeters(meters);
        }
        List<EnergyPlanGroupMap> groupMaps = energyPlanProvider.listGroupsByEnergyPlan(plan.getId());
        if(groupMaps != null && groupMaps.size() > 0) {
            List<EnergyPlanGroupDTO> groups = new ArrayList<>();
            groupMaps.forEach(group -> {
                EnergyPlanGroupDTO groupDTO = ConvertHelper.convert(group, EnergyPlanGroupDTO.class);
                StringBuilder sb = new StringBuilder();
                Organization org = organizationProvider.findOrganizationById(group.getGroupId());
                OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(group.getPositionId());
                if(org != null) {
                    sb.append(org.getName());

                }
                if(position != null) {
                    sb.append(position.getName());
                }
                groupDTO.setGroupName(sb.toString());
                groups.add(groupDTO);
            });
            dto.setGroups(groups);
        }
        return dto;
    }

    @Override
    public EnergyPlanDTO updateEnergyPlan(UpdateEnergyPlanCommand cmd) {
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getTargetId(), cmd.getOwnerId(), PrivilegeConstants.ENERGY_PLAN_CREATE);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_PLAN_CREATE, cmd.getOwnerId(),  cmd.getTargetId());
//        checkMeterPlanAssigment(cmd.getId(), cmd.getMeters());
        EnergyPlan plan = ConvertHelper.convert(cmd, EnergyPlan.class);
        RepeatSettings repeat = dealEnergyPlanRepeat(cmd.getRepeat());
        plan.setRepeatSettingId(repeat.getId());
        plan.setStatus(CommonStatus.ACTIVE.getCode());
        if(cmd.getId() == null) {
            energyPlanProvider.createEnergyPlan(plan);
        } else {
            energyPlanProvider.updateEnergyPlan(plan);
        }

        dealEnergyPlanGroups(plan, cmd.getGroups());
        dealEnergyPlanMeters(plan, cmd.getMeters());

        energyPlanSearcher.feedDoc(plan);
        return toEnergyPlanDTO(plan);
    }

    private void checkMeterPlanAssigment(Long planId, List<EnergyPlanMeterDTO> meters) {
        if(meters != null && meters.size() > 0) {
            meters.forEach(planMeter -> {
                List<PlanMeter> maps = energyPlanProvider.listByEnergyMeter(planMeter.getMeterId());
                if(maps != null && maps.size() > 0) {
                    for(PlanMeter map : maps) {
                        if(repeatService.repeatSettingStillWork(map.getRepeatSettingId())) {
                            if(!map.getPlanId().equals(planId)) {
                                LOGGER.error("meter id: {} already assigned to plan id: {}!", map.getMeterId(), map.getPlanId());
                                throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_HAS_ASSIGN_PLAN, "The energy meter has assigned to plan!");
                            }
                        }
                    }
                }
            });

        }
    }

    @Override
    public void exportEnergyMeterQRCode(ExportEnergyMeterQRCodeCommand cmd, HttpServletResponse response) {
        List<Long> meterIds = new ArrayList<>();
        if(!StringUtils.isEmpty(cmd.getIds())) {
            String[] ids = cmd.getIds().split(",");
            for(String id : ids) {
                meterIds.add(Long.valueOf(id));
            }
        }
        LOGGER.info("meterIds: {}", meterIds);
        List<EnergyMeter> meterList = meterProvider.listByIds(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), meterIds);

        String filePath = cmd.getFilePath();
        URL rootPath = EnergyConsumptionServiceImpl.class.getResource("/");
        filePath = rootPath.getPath() + this.downloadDir ;
        File file = new File(filePath);
        if(!file.exists())
            file.mkdirs();

        DocUtil docUtil=new DocUtil();
        List<String> files = new ArrayList<>();
        for(EnergyMeter meter : meterList) {
            String qrcode = generateQRString(meter.getId(),meter.getNamespaceId());
            String savePath = filePath + meter.getId() + meter.getName() + ".jpg";
            graphicsGeneration("Name: "+meter.getName(), "No.: " + meter.getMeterNumber(), qrcode, savePath);
//            Map<String, Object> dataMap = createEnergyMeterQRCodeDoc(meter);
//            String savePath = filePath + meter.getId()+ "-" + meter.getName() + ".doc";
//            docUtil.createDoc(dataMap, "energyMeter", savePath);
//
            if(org.apache.commons.lang.StringUtils.isEmpty(cmd.getFilePath())) {
                files.add(savePath);
            }
        }


        if(files.size() > 1) {
            List<String> images = imageMosaic(files, filePath);

            if(images.size() == 1) {
                download(images.get(0),response);
            } else {
                String zipPath = filePath + System.currentTimeMillis() + "EnergyMeterCard.zip";
                LOGGER.info("download images filePath:{}, zipPath:{}",filePath,zipPath);
                DownloadUtils.writeZip(images, zipPath);
                download(zipPath,response);
            }
        } else if(files.size() == 1) {
            download(files.get(0),response);
        }


    }

//    private Map<String, Object> createEnergyMeterQRCodeDoc(EnergyMeter meter) {
//        Map<String, Object> dataMap=new HashMap<String, Object>();
//        dataMap.put("meterNumber", meter.getMeterNumber());
//        dataMap.put("name", meter.getName());
//        String qrCode = generateQRString(meter.getId(), meter.getNamespaceId());
//        ByteArrayOutputStream out = generateQRCode(org.apache.commons.codec.binary.Base64.encodeBase64String(qrCode.getBytes()));
//        byte[] data=out.toByteArray();
//        BASE64Encoder encoder=new BASE64Encoder();
//        dataMap.put("qrCode", encoder.encode(data));
//
//        return dataMap;
//    }

    private String generateQRString(Long id, Integer namespaceId) {
//        EnergyMeterCodeDTO dto = new EnergyMeterCodeDTO();
//        dto.setNamespaceId(namespaceId);
//        dto.setMeterId(id);
//        String qrCode = WebTokenGenerator.getInstance().toWebToken(dto);
        return id.toString();

    }

    private ByteArrayOutputStream generateQRCode(String qrToken) {
        ByteArrayOutputStream out = null;
        try {
            BufferedImage image = QRCodeEncoder.createQrCode(qrToken, 200, 200, null);
            out = new ByteArrayOutputStream();
            ImageIO.write(image, QRCodeConfig.FORMAT_PNG, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return out;
    }

    @Override
    public void exportSearchEnergyMeterQRCode(SearchEnergyMeterCommand cmd, HttpServletResponse response) {
        cmd.setPageSize(Integer.MAX_VALUE-1);
        List<Long> meterIds = meterSearcher.getMeterIds(cmd);

        LOGGER.info("meterIds: {}", meterIds);
        List<EnergyMeter> meterList = meterProvider.listByIds(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), meterIds);

        URL rootPath = EnergyConsumptionServiceImpl.class.getResource("/");
        String filePath = rootPath.getPath() + this.downloadDir ;
        File file = new File(filePath);
        if(!file.exists())
            file.mkdirs();


        DocUtil docUtil=new DocUtil();
        List<String> files = new ArrayList<>();
        for(EnergyMeter meter : meterList) {
            String qrcode = generateQRString(meter.getId(),meter.getNamespaceId());
            String savePath = filePath + meter.getId() + meter.getName() + ".jpg";
            graphicsGeneration("Name: "+meter.getName(), "No.: " + meter.getMeterNumber(), qrcode, savePath);
//            Map<String, Object> dataMap = createEnergyMeterQRCodeDoc(meter);
//            String savePath = filePath + meter.getId()+ "-" + meter.getName() + ".doc";
//            docUtil.createDoc(dataMap, "energyMeter", savePath);
//
            files.add(savePath);
        }

        if(files.size() > 1) {
            List<String> images = imageMosaic(files, filePath);

            if(images.size() == 1) {
                download(images.get(0),response);
            } else {
                String zipPath = filePath + System.currentTimeMillis() + "EnergyMeterCard.zip";
                LOGGER.info("download images filePath:{}, zipPath:{}",filePath,zipPath);
                DownloadUtils.writeZip(images, zipPath);
                download(zipPath,response);
            }
//            String zipPath = filePath + System.currentTimeMillis() + "EnergyMeterCard.zip";
//            LOGGER.info("filePath:{}, zipPath:{}",filePath,zipPath);
//            DownloadUtils.writeZip(files, zipPath);
//            download(zipPath,response);
        } else if(files.size() == 1) {
            download(files.get(0),response);
        }

    }

    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            if ( !file.isFile() ) {
                LOGGER.info("filename:{} is not a file", path);
            }
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            // 读取完成删除文件
            if (file.isFile() && file.exists()) {
                file.delete();
            }

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw RuntimeErrorException.errorWith(EnergyConsumptionServiceErrorCode.SCOPE,
                    EnergyConsumptionServiceErrorCode.ERROR_DOWNLOAD_FILE,
                    ex.getLocalizedMessage());

        }
        return response;
    }

    private void graphicsGeneration(String name, String number, String qrcode, String savePath) {
        int imageWidth = 330;//图片的宽度
        int imageHeight = 413; //图片的高度
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);

        Graphics graphics = image.getGraphics();
        graphics.setFont(new Font("wqy-zenhei", Font.PLAIN, 14));
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setColor(Color.BLACK);
        BufferedImage bimg = null;
        try {
            graphics.drawString(name, 10, 230);
            graphics.drawString(number, 10, 270);
            bimg = QRCodeEncoder.createQrCode(Base64.encodeBase64String(qrcode.getBytes()), 200, 200, null);
        } catch (Exception e) {
        }

        if (bimg != null) {
            graphics.drawImage(bimg, 60, 0, null);
        }
        graphics.dispose();

        try {
            FileOutputStream fos = new FileOutputStream(savePath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
//            encoder.encode(image);
            ImageIO.write(image, "JPEG", bos);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> imageMosaic(List<String> files, String filePath) {
        List<String> images = new ArrayList<>();
        //每张图包含56张二维码
        int size = files.size()/56;
        if(files.size()%56 != 0) {
            size = size + 1;
        }
        int imageWidth = 2480;//图片的宽度
        int imageHeight = 3508; //图片的高度
        BufferedImage imageMosaic;
        try {
            for(int i =0; i < size; i++) {
                imageMosaic = new BufferedImage(imageWidth, imageHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics graphics = imageMosaic.getGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, imageWidth, imageHeight);
                //72张二维码
                int max = (files.size() > (i+1) * 56) ? 56 : files.size()- i*56;
                int height = 0;

                int maxRow = max/7;
                if(max%7 != 0) {
                    maxRow = maxRow + 1;
                }
                LOGGER.info("draw max : {}, maxRow: {}, size: {}" , max, maxRow, size);
                for(int row = 0; row < maxRow; row++) {
                    //每行7个
                    for(int w = 0; w < 7; w++) {
                        LOGGER.info("draw w : {}, row: {}, file size: {}" , w, row, files.size());
                        if(row * 7 +w < max) {
//                            BufferedImage small = ImageIO.read(new File(files.get(j+w)));
                            LOGGER.info("draw page: {}, Width : {}, Height: {}" , i, w * 355, height);
                            File file = new File(files.get(i*56 + row * 7 + w));
                            if ( !file.isFile() ) {
                                LOGGER.info("filename:{} is not a file", files.get(i*56 + row * 7 + w));
                                continue;
                            }
                            graphics.drawImage(ImageIO.read(file), w * 355, height, null);

                            // 读取完成删除文件
                            if (file.isFile() && file.exists()) {
                                file.delete();
                            }
                        }
                    }
                    height = (row+1) * 435;

                }

                graphics.dispose();
                String imagePath = filePath + System.currentTimeMillis() + "EnergyMeterCard.jpg";
                FileOutputStream fos = new FileOutputStream(imagePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ImageIO.write(imageMosaic, "JPEG", bos);
                bos.close();
                images.add(imagePath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return images;
    }

    @Override
    public void createTask(CreateEnergyTaskCommand cmd) {
        EnergyPlan plan = energyPlanProvider.findEnergyPlanById(cmd.getPlanId());
        if(plan == null || CommonStatus.INACTIVE.equals(CommonStatus.fromCode(plan.getStatus()))) {
            LOGGER.info("EnergyScheduleJob plan is not exist or active! planId = " + cmd.getPlanId());
            throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_PLAN_NOT_ACTIVE, "The energy meter plan is not exist or active");
        }

        List<EnergyPlanMeterMap> maps = energyPlanProvider.listMetersByEnergyPlan(cmd.getPlanId());
        if(maps != null && maps.size() > 0) {
            boolean isRepeat = repeatService.isRepeatSettingActive(plan.getRepeatSettingId());
            LOGGER.info("EnergyScheduleJob: plan id = " + plan.getId()
                    + "repeat setting id = "+ plan.getRepeatSettingId() + "is repeat setting active: " + isRepeat);

            for(EnergyPlanMeterMap map : maps) {
                EnergyMeter meter = meterProvider.findById(cmd.getNamespaceId(), map.getMeterId());
                if(meter == null || meter.getStatus() == null
                        || !EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
                    LOGGER.info("EnergyScheduleJob meter is not exist or active! meterId = " + map.getMeterId());
                    continue;
                } else if(isRepeat){
                    this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_ENERGY_TASK.getCode()).tryEnter(()-> {
                        creatMeterTask(map, plan);
                    });
                }
            }
        }

    }

    @Override
    public void creatMeterTask(EnergyPlanMeterMap map, EnergyPlan plan) {
        EnergyMeterTask task = new EnergyMeterTask();
        task.setNamespaceId(plan.getNamespaceId());
        task.setOwnerId(plan.getOwnerId());
        task.setOwnerType(plan.getOwnerType());
        task.setTargetType(plan.getTargetType());
        task.setTargetId(plan.getTargetId());
        task.setPlanId(plan.getId());
        task.setMeterId(map.getMeterId());
        EnergyMeterReadingLog lastReading = meterReadingLogProvider.findLastReadingLogByMeterId(plan.getNamespaceId(), map.getMeterId());
        task.setLastTaskReading(lastReading == null ? BigDecimal.ZERO :lastReading.getReading());
        task.setDefaultOrder(map.getDefaultOrder());

        RepeatSettings rs = repeatService.findRepeatSettingById(plan.getRepeatSettingId());
        List<TimeRangeDTO> timeRanges = repeatService.analyzeTimeRange(rs.getTimeRanges());
        if(timeRanges != null && timeRanges.size() > 0) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("creatMeterTask, timeRanges = " + timeRanges);
            }
            long current = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String day = sdf.format(current);
            int i = 0;
            for (TimeRangeDTO timeRange : timeRanges) {
                i++;
                String duration = timeRange.getDuration();
                String start = timeRange.getStartTime();
                String str = day + " " + start;
                Timestamp startTime = strToTimestamp(str);
                Timestamp expiredTime = repeatService.getEndTimeByAnalyzeDuration(startTime, duration);
                task.setExecutiveStartTime(startTime);
                LocalDateTime endTime = expiredTime.toLocalDateTime();
                task.setExecutiveExpireTime(Timestamp.valueOf(LocalDateTime.of(endTime.toLocalDate().minusDays(1L),LocalTime.MAX).format(dateSF)));

                energyMeterTaskProvider.createEnergyMeterTask(task);
                energyMeterTaskSearcher.feedDoc(task);

//				启动提醒
                List<EnergyPlanGroupMap> groupMaps = energyPlanProvider.listGroupsByEnergyPlan(plan.getId());
                if(groupMaps != null && groupMaps.size() > 0) {
                    PmNotifyRecord record = new PmNotifyRecord();
                    PmNotifyReceiverList receiverList = new PmNotifyReceiverList();
                    List<PmNotifyReceiver> pmNotifyReceivers = new ArrayList<>();
                    groupMaps.forEach(receiver -> {
                        PmNotifyReceiver pmNotifyReceiver = new PmNotifyReceiver();
                        if(receiver != null) {
                            pmNotifyReceiver.setReceiverType(PmNotifyReceiverType.EXECUTOR.getCode());
                            pmNotifyReceivers.add(pmNotifyReceiver);
                        }
                    });
                    receiverList.setReceivers(pmNotifyReceivers);
                    record.setReceiverJson(receiverList.toString());
                    record.setOwnerType(EntityType.ENERGY_TASK.getCode());
                    record.setOwnerId(task.getId());
                    record.setNotifyType(PmNotifyType.BEFORE_DELAY.getCode());
                    record.setNotifyMode(PmNotifyMode.MESSAGE.getCode());

                    //notify_time
                    Timestamp delaytime = minusMinutes(task.getExecutiveExpireTime(), plan.getNotifyTickMinutes());
                    record.setNotifyTime(delaytime);

                    pmNotifyService.pushPmNotifyRecord(record);
                }

            }
        }
    }

    @Override
    public Set<Long> getTaskGroupUsers(Long taskId) {
        EnergyMeterTask task = energyMeterTaskProvider.findEnergyMeterTaskById(taskId);
        List<EnergyPlanGroupMap> groupMaps = energyPlanProvider.listGroupsByEnergyPlan(task.getPlanId());
        if(groupMaps != null && groupMaps.size() > 0) {
            Set<Long> userIds = new HashSet<>();
            groupMaps.forEach(map -> {
                if(map.getPositionId() == null || map.getPositionId() == 0L) {
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembers(map.getGroupId(), null);
                    if (members != null) {
                        for (OrganizationMember member : members) {
                            userIds.add(member.getTargetId());
                        }
                    }
                } else {
                    ListOrganizationContactByJobPositionIdCommand command = new ListOrganizationContactByJobPositionIdCommand();
                    command.setOrganizationId(map.getGroupId());
                    command.setJobPositionId(map.getPositionId());
                    List<OrganizationContactDTO> contacts = organizationService.listOrganizationContactByJobPositionId(command);

                    if (contacts != null && contacts.size() > 0) {
                        for (OrganizationContactDTO contact : contacts) {
                            userIds.add(contact.getTargetId());
                        }
                    }
                }
            });
            return userIds;
        }
        return null;
    }

    private Timestamp minusMinutes(Timestamp startTime, int minus) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, -minus);
        Timestamp time = new Timestamp(calendar.getTimeInMillis());
        return time;
    }

    private Timestamp strToTimestamp(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

        Timestamp ts = null;
        try {
            ts = new Timestamp(sdf.parse(str).getTime());
        } catch (ParseException e) {
            LOGGER.error("validityPeriod data format is not yyyymmdd.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "validityPeriod data format is not yyyymmdd.");
        }

        return ts;
    }

    @Override
    public HttpServletResponse exportTasksByEnergyPlan(SearchTasksByEnergyPlanCommand cmd, HttpServletResponse response) {
        Integer pageSize = Integer.MAX_VALUE;
        cmd.setPageSize(pageSize);

        SearchTasksByEnergyPlanResponse resp = energyMeterTaskSearcher.searchTasksByEnergyPlan(cmd);
        List<EnergyMeterTaskDTO> dtos = resp.getTaskDTOs();


        URL rootPath = RentalServiceImpl.class.getResource("/");
        String filePath =rootPath.getPath() + this.downloadDir ;
        File file = new File(filePath);
        if(!file.exists())
            file.mkdirs();
        filePath = filePath + "EnergyPlanTask"+System.currentTimeMillis()+".xlsx";
        //新建了一个文件
        this.createEnergyPlanTasksBook(filePath, dtos);
        LOGGER.info("filePath:{}", filePath);

        return download(filePath,response);
    }

    public void createEnergyPlanTasksBook(String path,List<EnergyMeterTaskDTO> dtos) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("EnergyPlanTasks");

        this.createEnergyPlanTasksBookSheetHead(sheet);
        for (EnergyMeterTaskDTO dto : dtos ) {
            this.setNewEnergyPlanTasksBookRow(sheet, dto);
        }

        try {
            FileOutputStream out = new FileOutputStream(path);

            wb.write(out);
            wb.close();
            out.close();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw RuntimeErrorException.errorWith(EnergyConsumptionServiceErrorCode.SCOPE,
                    EnergyConsumptionServiceErrorCode.ERROR_DOWNLOAD_FILE,
                    e.getLocalizedMessage());
        }

    }

    private void createEnergyPlanTasksBookSheetHead(Sheet sheet){

        Row row = sheet.createRow(sheet.getLastRowNum());
        int i =-1 ;
        row.createCell(++i).setCellValue("工单号");
        row.createCell(++i).setCellValue("表计号码");
        row.createCell(++i).setCellValue("表计名称");
        row.createCell(++i).setCellValue("表计分类");
        row.createCell(++i).setCellValue("开始日期");
        row.createCell(++i).setCellValue("结束日期");
        row.createCell(++i).setCellValue("工单状态");
        row.createCell(++i).setCellValue("上周期抄表读数");
        row.createCell(++i).setCellValue("上次抄表读数");
    }

    private void setNewEnergyPlanTasksBookRow(Sheet sheet ,EnergyMeterTaskDTO dto){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getId());
        row.createCell(++i).setCellValue(dto.getMeterNumber());
        row.createCell(++i).setCellValue(dto.getMeterName());
        if(EnergyMeterType.ELECTRIC.equals(EnergyMeterType.fromCode(dto.getMeterType())))
            row.createCell(++i).setCellValue("自用电表");
        if(EnergyMeterType.WATER.equals(EnergyMeterType.fromCode(dto.getMeterType())))
            row.createCell(++i).setCellValue("自用水表");
        if(EnergyMeterType.COMMON_WATER.equals(EnergyMeterType.fromCode(dto.getMeterType())))
            row.createCell(++i).setCellValue("公摊水表");
        if(EnergyMeterType.COMMON_ELECTRIC.equals(EnergyMeterType.fromCode(dto.getMeterType())))
            row.createCell(++i).setCellValue("公摊电表");

        row.createCell(++i).setCellValue(timeToStr(dto.getExecutiveStartTime()));
        row.createCell(++i).setCellValue(timeToStr(dto.getExecutiveExpireTime()));
        if(EnergyTaskStatus.NON_READ.equals(EnergyTaskStatus.fromCode(dto.getStatus())))
            row.createCell(++i).setCellValue("未完成");
        if(EnergyTaskStatus.READ.equals(EnergyTaskStatus.fromCode(dto.getStatus())))
            row.createCell(++i).setCellValue("已完成");
        if(EnergyTaskStatus.INACTIVE.equals(EnergyTaskStatus.fromCode(dto.getStatus())))
            row.createCell(++i).setCellValue("无效");
        if(EnergyTaskStatus.NON_READ_DELAY.equals(EnergyTaskStatus.fromCode(dto.getStatus())))
            row.createCell(++i).setCellValue("到期未完成");

        if(dto.getLastTaskReading() == null) {
            row.createCell(++i).setCellValue("");
        } else {
            row.createCell(++i).setCellValue(dto.getLastTaskReading().toString());
        }

        if(dto.getReading() == null) {
            row.createCell(++i).setCellValue("");
        } else {
            row.createCell(++i).setCellValue(dto.getReading().toString());
        }

    }


    private String timeToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    @Override
    public ImportFileTaskDTO importTasksByEnergyPlan(ImportTasksByEnergyPlanCommand cmd, MultipartFile mfile, Long userId) {
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if(null == resultList || resultList.isEmpty()){
                LOGGER.error("File content is empty。userId="+userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(),"File content is empty"));
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOwnerId());
            task.setType(ImportFileTaskType.ENERGY_PLAN_TASK.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportTasksByEnergyPlanDataDTO> datas = handleImportTasksByEnergyPlanData(resultList);
                    if(datas.size() > 0){
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }
                    List<ImportFileResultLog<ImportTasksByEnergyPlanDataDTO>> results = importTasksByEnergyPlanData(cmd, datas, userId);
                    response.setTotalCount((long)datas.size());
                    response.setFailCount((long)results.size());
                    response.setLogs(results);
                    return response;
                }
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        LOGGER.info("task: {}",  task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List<ImportTasksByEnergyPlanDataDTO> handleImportTasksByEnergyPlanData(List list) {
        List<ImportTasksByEnergyPlanDataDTO> result = new ArrayList<>();
        int row = 1;
        for (Object o : list) {
            if(row < 1){
                row ++;
                continue;
            }

            RowResult r = (RowResult)o;
            ImportTasksByEnergyPlanDataDTO data = null;
            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getA())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setId(r.getA().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getB())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setMeterNumber(r.getB().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getC())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setMeterName(r.getC().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getD())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setMeterType(r.getD().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getE())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setExecutiveStartTime(r.getE().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getF())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setExecutiveExpireTime(r.getF().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getG())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setStatus(r.getG().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getH())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setLastTaskReading(r.getH().trim());
            }

            if(org.apache.commons.lang.StringUtils.isNotBlank(r.getI())) {
                if(data == null) {
                    data = new ImportTasksByEnergyPlanDataDTO();
                }
                data.setReading(r.getI().trim());
            }

            if(data != null) {
                result.add(data);
            }
        }
        LOGGER.info("result size : " + result.size());
        return result;
    }

    private List<ImportFileResultLog<ImportTasksByEnergyPlanDataDTO>> importTasksByEnergyPlanData(ImportTasksByEnergyPlanCommand cmd, List<ImportTasksByEnergyPlanDataDTO> list, Long userId){
        List<ImportFileResultLog<ImportTasksByEnergyPlanDataDTO>> errorDataLogs = new ArrayList<>();
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        list.forEach(str -> {
            ImportFileResultLog<ImportTasksByEnergyPlanDataDTO> log = new ImportFileResultLog<>(EnergyConsumptionServiceErrorCode.SCOPE);
            if(org.apache.commons.lang.StringUtils.isNotBlank(str.getId())) {
                EnergyMeterTask task = energyMeterTaskProvider.findEnergyMeterTaskById(Long.valueOf(str.getId()));
                if(task == null || task.getPlanId().equals(cmd.getPlanId())
                        || task.getExecutiveExpireTime().before(now)) {
                    LOGGER.error("EnergyTask not exist or already close, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("EnergyTask not exist or already close");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERR_METER_TASK_NOT_EXIST);
                    errorDataLogs.add(log);
                    return;
                }

                EnergyMeter meter = meterProvider.findById(task.getNamespaceId(), task.getMeterId());
                if(!EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
                    LOGGER.error("EnergyTask meter status is not active, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("EnergyTask meter status is not active");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERR_METER_NOT_EXIST);
                    errorDataLogs.add(log);
                    return;
                }
                if(StringUtils.isEmpty(str.getReading())) {
                    LOGGER.error("EnergyTask reading is null, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("EnergyTask reading is null");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERROR_READING_IS_NULL);
                    errorDataLogs.add(log);
                    return;
                }

                if(!str.getReading().matches("^[0.0-9.0]+$")) {
                    LOGGER.error("EnergyTask reading is not number, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("EnergyTask reading is not number");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERROR_READING_IS_NOT_NUMBER);
                    errorDataLogs.add(log);
                    return;
                }

                BigDecimal reading = new BigDecimal(str.getReading());
                // 读数大于最大量程
                if (reading.doubleValue() > meter.getMaxReading().doubleValue()) {
                    LOGGER.error("EnergyTask reading greater than meter max reading, data = {}", str);
                    log.setData(str);
                    log.setErrorLog("EnergyTask reading greater than meter max reading");
                    log.setCode(EnergyConsumptionServiceErrorCode.ERR_CURR_READING_GREATER_THEN_MAX_READING);
                    errorDataLogs.add(log);
                    return;
                }

                task.setReading(reading);
                task.setStatus(EnergyTaskStatus.READ.getCode());
                task.setOperatorUid(userId);
                task.setUpdateTime(now);
                energyMeterTaskProvider.updateEnergyMeterTask(task);
                energyMeterTaskSearcher.feedDoc(task);


                EnergyMeterReadingLog readingLog = new EnergyMeterReadingLog();
                readingLog.setStatus(EnergyCommonStatus.ACTIVE.getCode());
                readingLog.setReading(reading);
                readingLog.setCommunityId(meter.getCommunityId());
                readingLog.setMeterId(meter.getId());
                readingLog.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
                readingLog.setOperateTime(now);
                readingLog.setOperatorId(userId);
                readingLog.setResetMeterFlag(TrueOrFalseFlag.FALSE.getCode());
                dbProvider.execute(r -> {
                    meterReadingLogProvider.createEnergyMeterReadingLog(readingLog);
                    meter.setLastReading(readingLog.getReading());
                    meter.setLastReadTime(now);
                    meterProvider.updateEnergyMeter(meter);
                    return true;
                });
                readingLogSearcher.feedDoc(readingLog);
            }
        });
        return errorDataLogs;
    }

    private Timestamp dateStrToTimestamp(String str) {
        if(str != null) {
            LocalDateTime localDate = LocalDateTime.parse(str,dateSF);
            Timestamp ts = Timestamp.valueOf(localDate);
            return ts;
        }
        return null;
    }

    @Override
    public SyncOfflineDataResponse syncOfflineData(SyncOfflineDataCommand cmd) {
        SyncOfflineDataResponse response = new SyncOfflineDataResponse();
        Timestamp buildingUpdateTime = dateStrToTimestamp(cmd.getBuildingUpdateTime());
        Timestamp categoryUpdateTime = dateStrToTimestamp(cmd.getCategoryUpdateTime());
        Timestamp taskUpdateTime = dateStrToTimestamp(cmd.getTaskUpdateTime());
        List<EnergyMeterCategory> categoryList = new ArrayList<>();
        categoryList = meterCategoryProvider.listMeterCategories(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), cmd.getCategoryType(),
                cmd.getOwnerId(), null, cmd.getCommunityId(), categoryUpdateTime);
        List<EnergyMeterCategoryMap> categoryMaps = energyMeterCategoryMapProvider.listEnergyMeterCategoryMap(cmd.getCommunityId(),cmd.getOwnerId());
        if(categoryMaps != null && categoryMaps.size() > 0) {
            List<Long> categoryIds = categoryMaps.stream().map(EnergyMeterCategoryMap::getCategoryId).collect(Collectors.toList());
            List<EnergyMeterCategory> categories = meterCategoryProvider.listMeterCategories(categoryIds, cmd.getCategoryType());
            if(categories != null && categories.size() > 0) {
                categoryList.addAll(categories);
            }
        }
        List<EnergyMeterCategoryDTO> dtos = categoryList.stream().map(category -> {
            EnergyMeterCategoryDTO dto = toMeterCategoryDto(category);
            if(category.getUpdateTime() == null) {
                category.setUpdateTime(category.getCreateTime());
            }
            dto.setLastTime(category.getUpdateTime().toLocalDateTime().format(dateSF));
            return dto;
        }).collect(Collectors.toList());
        response.setCategoryDTOs(dtos);

        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<com.everhomes.community.Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, Integer.MAX_VALUE-1,
                cmd.getCommunityId(), UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), null, buildingUpdateTime);
        List<BuildingDTO> buildingDTOs = buildings.stream().map((r) -> {
            BuildingDTO dto = ConvertHelper.convert(r, BuildingDTO.class);
            dto.setBuildingName(dto.getName());
            dto.setName(org.apache.commons.lang.StringUtils.isBlank(dto.getAliasName()) ? dto.getName() : dto.getAliasName());
            if(r.getOperateTime() == null) {
                r.setOperateTime(r.getCreateTime());
            }
            dto.setLastTime(r.getOperateTime().toLocalDateTime().format(dateSF));
            return dto;
        }).collect(Collectors.toList());
        response.setBuildings(buildingDTOs);

        List<ExecuteGroupAndPosition> groupDtos = listUserRelateGroups();
        List<EnergyPlanGroupMap> maps = energyPlanProvider.lisEnergyPlanGroupMapByGroupAndPosition(groupDtos);
        EnergyPlan autoPlans = energyPlanProvider.listNewestAutoReadingPlans(cmd.getNamespaceId(),cmd.getCommunityId());
        List<Long> planIds = new ArrayList<>();
        planIds.add(0L);
        if (autoPlans != null) {
            planIds.add(autoPlans.getId());
        }
        if (maps != null && maps.size() > 0) {
            List<Long> plans = maps.stream().map(EnergyPlanGroupMap::getPlanId).collect(Collectors.toList());
            if (plans != null && plans.size() > 0)
                planIds.addAll(plans);
        }
        if(planIds.size()>0){
            List<EnergyMeterTask> tasks = energyMeterTaskProvider.listEnergyMeterTasksByPlan(planIds, cmd.getCommunityId(),
                    cmd.getOwnerId(), 0L, Integer.MAX_VALUE - 1, taskUpdateTime);

            if(tasks != null && tasks.size() > 0) {
                Set<Long> meterIds = new HashSet<>();
                List<EnergyMeterTaskDTO> taskDTOs = tasks.stream().map(task -> {
                    meterIds.add(task.getMeterId());
                    EnergyMeterTaskDTO dto = ConvertHelper.convert(task, EnergyMeterTaskDTO.class);
                    if(task.getUpdateTime() == null) {
                        task.setUpdateTime(task.getCreateTime());
                    }
                    dto.setLastTime(task.getUpdateTime().toLocalDateTime().format(dateSF));
                    EnergyMeter meter = meterProvider.findById(task.getNamespaceId(), task.getMeterId());
                    dto.setBillCategoryId(meter.getBillCategoryId());
                    // 项目
                    EnergyMeterCategory billCategory = meterCategoryProvider.findById(UserContext.getCurrentNamespaceId(task.getNamespaceId()), meter.getBillCategoryId());
                    dto.setBillCategory(billCategory != null ? billCategory.getName() : null);

                    dto.setMeterName(meter.getName());
                    dto.setMeterNumber(meter.getMeterNumber());
                    dto.setMeterType(meter.getMeterType());
                    dto.setMaxReading(meter.getMaxReading());
                    dto.setStartReading(meter.getStartReading());
                    dto.setMeterStatus(meter.getStatus());
                    dto.setAutoFlag(meter.getAutoFlag());
                    dto.setReading(task.getReading());
                    // 日读表差
                    dto.setDayPrompt(this.processDayPrompt(meter, meter.getNamespaceId()));
                    // 月读表差
                    dto.setMonthPrompt(this.processMonthPrompt(meter, meter.getNamespaceId()));
                    List<EnergyMeterAddress> addressMap = energyMeterAddressProvider.listByMeterId(task.getMeterId());
                    if(addressMap != null && addressMap.size() > 0) {
                        dto.setApartmentFloor(addressMap.get(0).getApartmentFloor());
                        dto.setBuildingId(addressMap.get(0).getBuildingId());
                        dto.setBuildingName(addressMap.get(0).getBuildingName());
                        dto.setApartmentName(addressMap.get(0).getApartmentName());
                    }
                    return dto;
                }).collect(Collectors.toList());
                response.setTaskDTOs(taskDTOs);

                List<EnergyMeterReadingLog> logs = meterReadingLogProvider.listMeterReadingLogsByMeterIds(cmd.getNamespaceId(), meterIds);
                if(logs != null && logs.size() > 0) {
                    List<EnergyMeterReadingLogDTO> logDTOs = new ArrayList<>();
                    logs.forEach(log -> {
                        EnergyMeterReadingLogDTO logDTO = ConvertHelper.convert(log, EnergyMeterReadingLogDTO.class);
                        EnergyMeter meter = meterProvider.findById(log.getNamespaceId(), log.getMeterId());
                        if(meter != null) {
                            logDTO.setMeterType(meter.getMeterType());
                            logDTO.setMeterNumber(meter.getMeterNumber());
                            logDTO.setMeterName(meter.getName());
                            logDTO.setOrganizationId(meter.getOwnerId());

                            List<EnergyMeterAddress> existAddress = energyMeterAddressProvider.listByMeterId(meter.getId());
                            if(existAddress != null && existAddress.size() > 0) {
                                logDTO.setMeterAddress(existAddress.stream()
                                        .map((r) -> ConvertHelper.convert(r, EnergyMeterAddressDTO.class))
                                        .collect(Collectors.toList()));
                            }

                        }
                        User operator = userProvider.findUserById(log.getOperatorId());
                        if(operator != null) {
                            logDTO.setOperatorName(operator.getNickName());
                        }
                        logDTOs.add(logDTO);
                    });
                    response.setLogDTOs(logDTOs);
                }
            }

        }
        LOGGER.info("auto reading auto meter ......");
        try {
            readMeterRemote(false ,cmd.getCommunityId());
        }catch (Exception e){
            LOGGER.error("auto reading error:{}", e);
        }
        return response;
    }

    @Override
    public void meterAutoReading(Boolean createPlansFlag) {
        LOGGER.debug("starting auto reading manual...");
        readMeterRemote(createPlansFlag,null);
        LOGGER.debug("ending auto reading manual...");
    }

    @Override
    public void addMeterPeriodTaskById(Long meterId) {
        EnergyMeter meter = meterProvider.findById(999961, meterId);
        createMeterTask(meter);
    }
}
