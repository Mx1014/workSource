// @formatter:off
package com.everhomes.salary;

import com.alibaba.fastjson.JSON;
import com.everhomes.archives.ArchivesService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.*;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.salary.*;
import com.everhomes.rest.salary.GetImportFileResultCommand;
import com.everhomes.rest.salary.ListEnterprisesCommand;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.socialSecurity.SocialSecurityConstants;
import com.everhomes.socialSecurity.SocialSecurityService;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.uniongroup.UniongroupService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import com.everhomes.util.RestClient.Response;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;




import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;




import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;




import javax.servlet.http.HttpServletResponse;




import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SalaryServiceImpl implements SalaryService {

    private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };

    //	private static List<SalaryDefaultEntity> salaryDefaultEntities = new ArrayList<SalaryDefaultEntity>();
//
    private List<String> invisibleKeys = new ArrayList<>();

    private StringTemplateLoader templateLoader;

    private Configuration templateConfig;

    public SalaryServiceImpl() {

        super();
        invisibleKeys.add("姓名");
        invisibleKeys.add("手机号码");
        invisibleKeys.add("部门");
        invisibleKeys.add("岗位");
        invisibleKeys.add("序号");
        invisibleKeys.add("所属部门");
        invisibleKeys.add("所在部门");
        invisibleKeys.add("职位");
        invisibleKeys.add("职务");
        invisibleKeys.add("职称");

        templateLoader = new StringTemplateLoader();
        templateConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        templateConfig.setTemplateLoader(templateLoader);
        templateConfig.setTemplateUpdateDelay(0);
        //构造函数初始化entities---这个是不变的
//		List<SalaryDefaultEntity> result = salaryDefaultEntityProvider.listSalaryDefaultEntity();
//		salaryDefaultEntities = result;
    }

    //	private SalaryDefaultEntity findEntity(Long id ){
//		for (SalaryDefaultEntity entity : salaryDefaultEntities) {
//			if (entity.getId().equals(id)) {
//				return entity;
//			}
//		}
//		return null;
//	}
    private static final Logger LOGGER = LoggerFactory.getLogger(SalaryServiceImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private SalaryGroupsReportResourceProvider salaryGroupsReportResourceProvider;
    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private SalaryGroupProvider salaryGroupProvider;

    @Autowired
    private SalaryGroupsFileProvider salaryGroupsFileProvider;

    @Autowired
    private SalaryEmployeesFileProvider salaryEmployeesFileProvider;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ArchivesService archivesService;


    @Autowired
    private SalaryDefaultEntityProvider salaryDefaultEntityProvider;

    @Autowired
    private SalaryEmployeeOriginValProvider salaryEmployeeOriginValProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private SalaryEmployeePeriodValProvider salaryEmployeePeriodValProvider;

    @Autowired
    private SalaryEmployeeProvider salaryEmployeeProvider;

    @Autowired
    private SalaryDepartStatisticProvider salaryDepartStatisticProvider;

    @Autowired
    private SalaryEntityCategoryProvider salaryEntityCategoryProvider;

    @Autowired
    private SalaryGroupEntityProvider salaryGroupEntityProvider;

    @Autowired
    private SalaryPayslipProvider salaryPayslipProvider;

    @Autowired
    private SalaryPayslipDetailProvider salaryPayslipDetailProvider;


    @Autowired
    private PunchService punchService;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private SocialSecurityService socialSecurityService;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private UniongroupService uniongroupService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserProvider userProvider;

    /*******
     * 导入部分
     *******/
    //  数字转换(1-A,2-B...)
    //  接下来会是很长的导入导出代码
    private static String GetExcelLetter(int n) {
        String s = "";
        while (n > 0) {
            int m = n % 26;
            if (m == 0)
                m = 26;
            s = (char) (m + 64) + s;
            n = (n - m) / 26;
        }
        return s;
    }

    @Override
    public String getDptPathNameByDetailId(Long detailId) {
        Long departmentId = organizationService.getDepartmentByDetailId(detailId);
        Organization department = organizationProvider.findOrganizationById(departmentId);
        return getDptPathName(department);
    }

    private String getDptPathName(Organization department) {
        if (department.getPath().contains("/")) {
            StringBuilder sb = new StringBuilder();
            String[] dptIds = department.getPath().split("/");
            for (String dptId : dptIds) {
                try {
                    Organization dpt1 = organizationProvider.findOrganizationById(Long.valueOf(dptId));
                    if (null != dpt1) {
                        if (sb.length() > 0) {
                            sb.append("/");
                        }
                        sb.append(dpt1.getName());
                    }
                } catch (NumberFormatException e) {
                    //不用处理字符串为""的
                } catch (Exception e) {
                    LOGGER.error("找部门的路径名称出了错", e);
                }
            }
            return sb.toString();
        } else
            return department.getName();
    }


    @Override
    public ListEnterprisesResponse listEnterprises(ListEnterprisesCommand cmd) {

        List<String> groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
        if (null == org) {
            return null;
        }
        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "%", groupTypeList);
        ListEnterprisesResponse resp = new ListEnterprisesResponse();
        resp.setEnterprises(orgs.stream().map(r -> {
            return ConvertHelper.convert(r, OrganizationDTO.class);
        }).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public ListGroupEntitiesResponse listGroupEntities(ListGroupEntitiesCommand cmd) {
        ListGroupEntitiesResponse response = new ListGroupEntitiesResponse();
        response.setCategories(new ArrayList<>());
        List<SalaryEntityCategory> categories = salaryEntityCategoryProvider.listSalaryEntityCategory();
        List<SalaryGroupEntity> entities = salaryGroupEntityProvider.listSalaryGroupEntityByOrgId(cmd.getOrganizationId());
        if (null == entities || entities.size() < 10) {
            entities = copyDefaultEntities2Org(cmd.getOrganizationId());
        }
        response.setUpdateTime(entities.get(0).getUpdateTime().getTime());
        OrganizationMember member = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(
                entities.get(0).getOperatorUid(), cmd.getOrganizationId());
        if (null != member) {
            response.setOperatorName(member.getContactName());
        }
        for (SalaryEntityCategory category : categories) {
            CategoryDTO categoryDTO = ConvertHelper.convert(category, CategoryDTO.class);
            categoryDTO.setEntities(new ArrayList<>());
            for (SalaryGroupEntity entity : entities) {

                if (entity.getCategoryId().equals(category.getId())) {
                    SalaryGroupEntityDTO dto = ConvertHelper.convert(entity, SalaryGroupEntityDTO.class);
                    categoryDTO.getEntities().add(dto);
                }
            }
            response.getCategories().add(categoryDTO);
        }
        return response;
    }

    private List<SalaryGroupEntity> copyDefaultEntities2Org(Long organizationId) {
        List<SalaryDefaultEntity> defaultEntities = salaryDefaultEntityProvider.listSalaryDefaultEntity();

        List<SalaryGroupEntity> entities = new ArrayList<>();
        for (SalaryDefaultEntity de : defaultEntities) {
            SalaryGroupEntity entity = ConvertHelper.convert(de, SalaryGroupEntity.class);
            entity.setDefaultId(de.getId());
            entity.setDefaultFlag(NormalFlag.YES.getCode());
            entity.setOrganizationId(organizationId);
            entity.setId(null);
            entity.setOriginEntityId(de.getId());
            if (entity.getStatus() == null) {
                entity.setStatus((byte) 1);
            }
            salaryGroupEntityProvider.createSalaryGroupEntity(entity);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public void updateGroupEntities(UpdateGroupEntitiesCommand cmd) {
        //更新和删除
        List<SalaryGroupEntity> entities = salaryGroupEntityProvider.listSalaryGroupEntityByOrgId(cmd.getOrganizationId());
        for (SalaryGroupEntity entity : entities) {
//            //不可编辑不可删除直接跳过
//            if (NormalFlag.NO == NormalFlag.fromCode(entity.getDeleteFlag()) &&
//                    NormalFlag.NO == NormalFlag.fromCode(entity.getEditableFlag())) {
//                salaryGroupEntityProvider.updateSalaryGroupEntity(entity);
//                continue;
//            }

            SalaryGroupEntityDTO dto = findSalaryGroupEntityDTOById(cmd.getEntities(), entity.getId());
            if (null == dto) {
                //不可删除的跳过
                if (NormalFlag.NO == NormalFlag.fromCode(entity.getDeleteFlag())) {
                    continue;
                }
                salaryGroupEntityProvider.deleteSalaryGroupEntity(entity);

            } else {
                if (entity.getStatus() != (byte) 2) {
                    entity.setStatus(dto.getStatus());
                }
                //不可编辑的跳过
                if (NormalFlag.NO == NormalFlag.fromCode(entity.getEditableFlag())) {
                    salaryGroupEntityProvider.updateSalaryGroupEntity(entity);
                    continue;
                }
                if (null != dto.getDataPolicy()) {
                    entity.setDataPolicy(dto.getDataPolicy());
                }
                if (null != dto.getGrantPolicy()) {
                    if (SalaryEntityType.fromCode(entity.getType()) == SalaryEntityType.DEDUCTION ||
                            SalaryEntityType.fromCode(entity.getType()) == SalaryEntityType.GRANT) {
                        //发放和扣款才更新发放策略
                        entity.setGrantPolicy(dto.getGrantPolicy());
                    }
                }
                if (NormalFlag.fromCode(entity.getDefaultFlag()) == NormalFlag.NO) {
                    entity.setName(dto.getName());
                }
                salaryGroupEntityProvider.updateSalaryGroupEntity(entity);
            }
        }

        //新增
        addNewGroupEntities(cmd.getOrganizationId(), cmd.getEntities());
        salaryGroupEntityProvider.updateSalaryGroupEntityOperator(cmd.getOrganizationId());


    }

    @Override
    public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd) {
        ListSalaryEmployeesResponse response = new ListSalaryEmployeesResponse();
        String month = findSalaryMonth(cmd.getOwnerId());
        response.setMonth(month);
        Integer namespaceId = cmd.getNamespaceId();
        if (null == namespaceId) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }
        //
        Timestamp checkinStartTime = null;
        Timestamp checkinEndTime = null;
        if (null != cmd.getCheckInMonth()) {
            checkinStartTime = new Timestamp(cmd.getCheckInMonth());
            Calendar checkin = Calendar.getInstance();
            checkin.setTimeInMillis(cmd.getCheckInMonth());
            checkin.add(Calendar.MONTH, 1);
            checkinEndTime = new Timestamp(checkin.getTimeInMillis());

        }

        Timestamp dissmisStartTime = null;
        Timestamp dissmisEndTime = null;
        if (null != cmd.getDismissMonth()) {
            dissmisStartTime = new Timestamp(cmd.getDismissMonth());
            Calendar dismissEnd = Calendar.getInstance();
            dismissEnd.setTimeInMillis(cmd.getDismissMonth());
            dismissEnd.add(Calendar.MONTH, 1);
            dissmisEndTime = new Timestamp(dismissEnd.getTimeInMillis());

        }
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (null != cmd.getPageAnchor()) {
            locator.setAnchor(cmd.getPageAnchor());
        }
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        List<Long> inDetails = null;
        List<Long> notinDetails = null;
        if (null != cmd.getSalaryStatus()) {
            if (SalaryEmployeeStatus.NORMAL == SalaryEmployeeStatus.fromCode(cmd.getSalaryStatus())) {
                inDetails = salaryEmployeeProvider.listEmployeeDetailIdsByStatus(cmd.getOwnerId(), SalaryEmployeeStatus.NORMAL.getCode());
            } else {
                notinDetails = salaryEmployeeProvider.listEmployeeDetailIdsByStatus(cmd.getOwnerId(), SalaryEmployeeStatus.NORMAL.getCode());
            }

        }
        List<Long> detailIds = organizationService.listDetailIdWithEnterpriseExclude(cmd.getKeywords(),
                namespaceId, cmd.getOwnerId(), checkinStartTime, checkinEndTime, dissmisStartTime, dissmisEndTime, locator, pageSize + 1, notinDetails, inDetails
        );
        LOGGER.debug(" organizationService.listDetailIdWithEnterpriseExclude(" + cmd.getKeywords() + ",\n" +
                namespaceId + "," + cmd.getOwnerId() + "," + checkinStartTime + "," + checkinEndTime + "," + dissmisStartTime + "," + dissmisEndTime + "," + locator + "," + pageSize + 1 + "," + notinDetails + "," + inDetails + "\n" +
                "        );");
        LOGGER.debug("detail Ids : " + StringHelper.toJsonString(detailIds));

//        List<Long> orgIds = new ArrayList<>();
//        orgIds.add(cmd.getOwnerId());ec“
//        List<OrganizationMember> members = organizationProvider.listOrganizationMemberByOrganizationIds(new ListingLocator(), Integer.MAX_VALUE - 1, null, orgIds);
//        if (null != members) {
//            for (OrganizationMember member : members) {
//                detailIds.add(member.getDetailId());
//            }
//        }

        if (null == detailIds || detailIds.size() == 0) {
            return response;
        }
        Long nextPageAnchor = null;
        if (detailIds.size() > pageSize) {
            detailIds.remove(detailIds.size() - 1);
            nextPageAnchor = detailIds.get(detailIds.size() - 1);
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setSalaryEmployeeDTO(new ArrayList<>());
        for (Long detailId : detailIds) {
            SalaryEmployee employee = salaryEmployeeProvider.findSalaryEmployeeByDetailId(cmd.getOwnerId(), detailId);
            if (null == employee) {
                employee = createSalaryEmployee(cmd.getOwnerId(), detailId, month);
            }
            if (null != employee) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                employee = calculateEmployee(cmd.getOwnerId(), detailId, detail.getOrganizationId());
                response.getSalaryEmployeeDTO().add(processEmployeeDTO(employee));
            }
        }

        return response;
    }

    private SalaryEmployee createSalaryEmployee(Long ownerId, Long detailId, String month) {
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (null == detail) {
            LOGGER.error("出错了找不到detail: detailId =" + detailId);
            return null;
        }
        SalaryEmployee employee = newSalaryEmployee(detail, month, ownerId);
        salaryEmployeeProvider.createSalaryEmployee(employee);
        return employee;
    }

    private SalaryEmployee newSalaryEmployee(OrganizationMemberDetails detail, String month, Long ownerId) {
        SalaryEmployee employee = new SalaryEmployee();
        employee.setStatus(SalaryEmployeeStatus.UN_SET.getCode());
        employee.setOwnerType("organization");
        employee.setOwnerId(ownerId);
        employee.setOrganizationId(detail.getOrganizationId());
        employee.setUserId(detail.getTargetId());
        employee.setUserDetailId(detail.getId());
        employee.setSalaryPeriod(month);
        employee.setRegularSalary(new BigDecimal(0));
        employee.setRealPaySalary(new BigDecimal(0));
        employee.setShouldPaySalary(new BigDecimal(0));
        employee.setNamespaceId(detail.getNamespaceId());
        return employee;
    }

    /**
     *
     * */
    private String findSalaryMonth(Long ownerId) {
        String month = salaryGroupProvider.getMonthByOwnerId(ownerId);
        if (null != month) {
            return month;
        } else {
            //todo 在历史归档找一找(一般不会有这种情况)

        }
        //历史归档也找不到
        if (month == null) {
            month = monthSF.get().format(DateHelper.currentGMTTime());
            createMonthSalaryGroup(ownerId, month);
            batchCreateMonthSalaryEmployees(ownerId, month);
        }
        return month;
    }

    private void createMonthSalaryGroup(Long ownerId, String month) {

        SalaryGroup sg = new SalaryGroup();
        sg.setOwnerType("organization");
        sg.setOwnerId(ownerId);
        sg.setSalaryPeriod(month);
        sg.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                .getTime()));
        sg.setCreatorUid(UserContext.currentUserId());
        sg.setCreatorName(findNameByOwnerAndUser(ownerId, sg.getCreatorUid()));

        salaryGroupProvider.createSalaryGroup(sg);

    }

    @Override
    public String findNameByOwnerAndUser(Long ownerId, Long uid) {
        OrganizationMember member = findMemberByOwnerAndUser(ownerId, uid);

        if (null != member) {
            return member.getContactName();
        }
        User user = userProvider.findUserById(uid);
        if (null != user) {
            return user.getNickName();
        }
        return null;
    }

    public OrganizationMember findMemberByOwnerAndUser(Long ownerId, Long uid) {
        if (null == uid) {
            return null;
        }
        Organization organization = organizationProvider.findOrganizationById(ownerId);
        OrganizationMember member = punchService.findOrganizationMemberByOrgIdAndUId(uid, organization.getPath());

        return member;
    }

    private void batchCreateMonthSalaryEmployees(Long ownerId, String month) {

        List<Long> detailIds = organizationService.listDetailIdWithEnterpriseExclude(null,
                UserContext.getCurrentNamespaceId(), ownerId, null, null, null, null, null, Integer.MAX_VALUE - 1, null, null
        );
        for (Long detailId : detailIds) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            if (null == detail) {
                continue;
            }
            createSalaryEmployee(ownerId, detail.getId(), month);
        }
    }

    private SalaryEmployeeDTO processEmployeeDTO(SalaryEmployee r) {
        if (null == r) {
            return null;
        }
        SalaryEmployeeDTO dto = ConvertHelper.convert(r, SalaryEmployeeDTO.class);
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(r.getUserDetailId());
        if (null != detail) {
            dto.setContactName(detail.getContactName());
            if (null != detail.getDismissTime()) {
                dto.setDismissTime(detail.getDismissTime().getTime());
            }
            if (null != detail.getCheckInTime()) {
                dto.setCheckInTime(detail.getCheckInTime().getTime());
            }
        }
        dto.setSalaryStatus(r.getStatus());
        return dto;
    }

    @Override
    public HttpServletResponse exportEmployeeSalaryTemplate(ExportEmployeeSalaryTemplateCommand cmd,
                                                            HttpServletResponse response) {
        String filePath = "批量更新工资明细模板" + ".xlsx";
        //新建了一个文件

        Workbook wb = createEmployeeSalaryHeadWB(cmd.getOrganizationId());

        return download(wb, filePath, response);
    }

    @Override
    public GetEmployeeEntitiesResponse getEmployeeEntities(GetEmployeeEntitiesCommand cmd) {
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        GetEmployeeEntitiesResponse response = ConvertHelper.convert(detail, GetEmployeeEntitiesResponse.class);
        response.setCategories(new ArrayList<>());
//        response.setCheckInTime(detail.getCheckInTime());
//        response.setDismissTime(detail.getDismissTime());
        List<SalaryEntityCategory> categories = salaryEntityCategoryProvider.listSalaryEntityCategory();
        List<SalaryGroupEntity> entities = salaryGroupEntityProvider.listSalaryGroupEntityByOrgId(cmd.getOrganizationId());
        if (null == entities) {
            entities = copyDefaultEntities2Org(cmd.getOrganizationId());
        }
        for (SalaryEntityCategory category : categories) {
            EmployeeCategoryDTO categoryDTO = ConvertHelper.convert(category, EmployeeCategoryDTO.class);
            categoryDTO.setEntities(new ArrayList<>());
            BigDecimal categoryValue = new BigDecimal(0);
            for (SalaryGroupEntity entity : entities) {
                if (SalaryEntityStatus.CLOSE == SalaryEntityStatus.fromCode(entity.getStatus())) {
                    continue;
                }
                if (entity.getCategoryId().equals(category.getId())) {
                    SalaryPeriodEmployeeEntityDTO dto = processSalaryPeriodEmployeeEntityDTO(entity, detail);
                    BigDecimal entityVal = new BigDecimal(0);
                    if (SalaryEntityType.REDUN != SalaryEntityType.fromCode(dto.getType())) {
                        try {
                            entityVal = new BigDecimal(dto.getSalaryValue());
                        } catch (Exception e) {
                            LOGGER.error("salaryValue :{}不能转换为数字", dto.getSalaryValue());
                        }

                        if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(dto.getType())) {
                            //减
                            categoryValue = categoryValue.subtract(entityVal);
                        } else {
                            //增
                            categoryValue = categoryValue.add(entityVal);
                        }
                    }

                    categoryDTO.getEntities().add(dto);
                }
            }
            categoryDTO.setSalaryValue(categoryValue.toString());
            response.getCategories().add(categoryDTO);
        }
        return response;
    }

    private SalaryPeriodEmployeeEntityDTO processSalaryPeriodEmployeeEntityDTO(SalaryGroupEntity entity, OrganizationMemberDetails detail) {
        SalaryPeriodEmployeeEntityDTO dto = ConvertHelper.convert(entity, SalaryPeriodEmployeeEntityDTO.class);
        dto.setGroupEntityId(entity.getId());
        dto.setGroupEntityName(entity.getName());
        SalaryEmployeeOriginVal val = salaryEmployeeOriginValProvider.findSalaryEmployeeOriginValByDetailId(entity.getId(), detail.getId());
        if (null != val) {
            dto.setSalaryValue(val.getSalaryValue());
        } else {
            dto.setSalaryValue("0");
        }
        return dto;
    }

    @Override
    public void exportEmployeeSalary(ExportEmployeeSalaryTemplateCommand cmd) {
        Map<String, Object> params = new HashedMap();
        params.put("ownerId", cmd.getOwnerId());
        params.put("organizationId", cmd.getOrganizationId());
        params.put("namespaceId", UserContext.getCurrentNamespaceId() + "");
        params.put("excelToken", SalaryReportType.SALARY_EMPLOYEE.getCode() + "");

        String fileName = "员工工资表" + ".xlsx";
//        params.put("name", fileName);

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), SalaryExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
    }

    @Override
    public ImportFileTaskDTO importEmployeeSalary(ExportEmployeeSalaryTemplateCommand cmd, MultipartFile[] files) {
        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(files[0]);
        task.setOwnerType("SALARY_OWNER_TYPE");
        task.setOwnerId(cmd.getOwnerId());
        task.setType(ImportFileTaskType.SALARY_GROUP.getCode());
        task.setCreatorUid(UserContext.currentUserId());

        importFileService.executeTask(new ExecuteImportTaskCallback() {
            @Override
            public ImportFileResponse importFile() {
                ImportFileResponse response = new ImportFileResponse();
                String fileLog = "";
                if (resultList.size() > 0) {
                    RowResult title = (RowResult) resultList.get(0);
                    Map<String, String> titleMap = title.getCells();
                    response.setTitle(titleMap);
                    fileLog = checkImportEmployeeSalaryTitle(titleMap, cmd.getOrganizationId());
                    if (!StringUtils.isEmpty(fileLog)) {
                        response.setFileLog(fileLog);
                        return response;
                    }
                    saveImportEmployeeSalary(resultList, cmd.getOrganizationId(), fileLog, response, cmd.getOwnerId());
                } else {
                    response.setFileLog(ImportFileErrorType.TITLE_ERROR.getCode());
                }
                return response;
            }


        }, task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private void saveImportEmployeeSalary(List resultList, Long organizationId, String fileLog, ImportFileResponse response, Long ownerId) {
        response.setLogs(new ArrayList<>());
        int coverNum = 0;
        for (int i = 1; i < resultList.size(); i++) {
            RowResult r = (RowResult) resultList.get(i);
            ImportFileResultLog<Map<String, String>> log = new ImportFileResultLog<>(SalaryConstants.SCOPE);
            Map<String, String> data = new HashMap();
            for (Map.Entry<String, String> entry : ((Map<String, String>) response.getTitle()).entrySet()) {
                data.put(entry.getKey(), (r.getCells().get(entry.getKey()) == null) ? "" : r.getCells().get(entry.getKey()));
            }
            log.setData(data);
            String userContact = r.getA().trim();
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(ownerId, userContact);
            if (null == detail) {
//                response.setFileLog("找不到用户: 手机号" + userContact);
                LOGGER.error("can not find organization member ,contact token is " + userContact);
                log.setErrorLog("找不到用户: 手机号" + userContact);
                log.setCode(SalaryConstants.ERROR_CHECK_CONTACT);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                continue;
            } else {
                //// TODO: 2018/1/26  检验权限 是否有操作此用户的权限
                SalaryEmployee employee = salaryEmployeeProvider.findSalaryEmployeeByDetailId(ownerId, detail.getId());
                if (null != employee && (employee.getRealPaySalary().compareTo(new BigDecimal(0)) != 0 ||
                        employee.getShouldPaySalary().compareTo(new BigDecimal(0)) != 0 ||
                        employee.getRealPaySalary().compareTo(new BigDecimal(0)) != 0)) {
                    //原本employee 计算的实收,应收,实发等如果不是0 就说明是覆盖
                    coverNum++;
                }
                saveImportEmployeeSalary(organizationId, detail.getId(), response, r, ownerId);
            }
        }
        response.setTotalCount((long) (resultList.size() - 1));
        response.setFailCount((long) response.getLogs().size());
        response.setCoverCount((long) coverNum);
        LOGGER.debug("resp" +
                response);
    }

    private void saveImportEmployeeSalary(Long organizationId, Long detailId, ImportFileResponse response, RowResult r, Long ownerId) {
        List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listOpenSalaryGroupEntityByOrgId(organizationId);
        List<Long> groupEntityIds = new ArrayList<>();
        List<SalaryEmployeeOriginVal> vals = new ArrayList<>();
        LOGGER.debug("groupEntities 是:{}\n导入的数据{}", StringHelper.toJsonString(r.getCells()));
        if (null != groupEntities) {
            for (int i = 0; i < groupEntities.size(); i++) {

                SalaryGroupEntity groupEntity = groupEntities.get(i);

                if (SalaryEntityStatus.CLOSE == SalaryEntityStatus.fromCode(groupEntity.getStatus())) {
                    //跳过不是开启状态的
                    continue;
                }
                if (groupEntity.getDefaultId() != null &&
                        (groupEntity.getDefaultId().equals(SalaryConstants.ENTITY_ID_BONUSTAX) ||
                                groupEntity.getDefaultId().equals(SalaryConstants.ENTITY_ID_SALARYTAX))) {
                    //税收跳过
                    continue;
                }
                groupEntityIds.add(groupEntity.getId());
                //i=0时候对应excel是C,所以i要+3
                String val = r.getCells().get(GetExcelLetter(i + 3));
                if (SalaryEntityType.REDUN != SalaryEntityType.fromCode(groupEntity.getType())) {
                    BigDecimal decimal = new BigDecimal(0);
                    try {
                        if (val != null)
                            decimal = new BigDecimal(val);
                    } catch (Exception e) {
                        LOGGER.error("转化导入数据出错", e);
                    }
                    val = decimal.toString();
                }
//                LOGGER.debug("第[{}]个gourpEntities数据[{}]的值是:[{}]列是{}", i+"",groupEntity.getName(), val,GetExcelLetter(i + 3));
                SalaryEmployeeOriginVal salaryVal = salaryEmployeeOriginValProvider.findSalaryEmployeeOriginValByDetailId(groupEntity.getId(), detailId);
                if (null == salaryVal) {
                    salaryVal = processSalaryEmployeeOriginVal(groupEntity, detailId, val, ownerId);
                    salaryEmployeeOriginValProvider.createSalaryEmployeeOriginVal(salaryVal);
                } else {
                    salaryVal.setSalaryValue(val);
                    salaryEmployeeOriginValProvider.updateSalaryEmployeeOriginVal(salaryVal);
                }
                vals.add(salaryVal);
            }
        }
        salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValNotInList(groupEntityIds, detailId);
        calculateEmployee(ownerId, detailId, vals, organizationId);
    }

    /**
     * 计算某人的EhSalaryEmployee
     */
    private SalaryEmployee calculateEmployee(Long ownerId, Long detailId, List<SalaryEmployeeOriginVal> vals, Long organizationId) {

        String month = findSalaryMonth(ownerId);
        SalaryEmployee employee = salaryEmployeeProvider.findSalaryEmployeeByDetailId(ownerId, detailId);
        if (null == employee) {
            return null;
        }
        employee.setSalaryPeriod(month);

        if (null == employee) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            employee = newSalaryEmployee(detail, month, ownerId);
        }
        /**固定工资*/
        BigDecimal regular = new BigDecimal(0);
        /**应发工资*/
        BigDecimal shouldPay = new BigDecimal(0);
        /**实发工资*/
        BigDecimal realPay = new BigDecimal(0);
        /**人力成本*/
        BigDecimal cost = new BigDecimal(0);
        /**工资计税*/
        BigDecimal salary = new BigDecimal(0);
        /**年终计税*/
        BigDecimal bonus = new BigDecimal(0);
        /**税后*/
        BigDecimal afterTax = new BigDecimal(0);
        if (null != vals) {
            for (SalaryEmployeeOriginVal val : vals) {

                SalaryGroupEntity groupEntity = salaryGroupEntityProvider.findSalaryGroupEntityById(val.getGroupEntityId());
                if (null == groupEntity || groupEntity.getStatus().equals(SalaryEntityStatus.CLOSE.getCode())) {
                    salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValById(val.getId());
                    continue;
                }
                BigDecimal value = new BigDecimal(0);
                try {
                    value = new BigDecimal(val.getSalaryValue());
                } catch (Exception e) {
                    //不能转换数字就不用管它
                }
                //固定工资
                if (val.getCategoryId().equals(1L)) {
                    if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(val.getType())) {
                        //减
                        regular = regular.subtract(value);
                    } else {
                        //增
                        regular = regular.add(value);
                    }
                }
                //应发工资
                if (SalaryEntityType.GRANT == SalaryEntityType.fromCode(val.getType())) {
//                    if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(val.getType())) {
//                        //减
//                        shouldPay = shouldPay.subtract(value);
//                    } else {
                    //增
                    shouldPay = shouldPay.add(value);
//                    }
                }
                if (NormalFlag.NO == NormalFlag.fromCode(val.getGrantPolicy())) {
                    //税前的
                    if (SalaryTaxPolicy.SALARY == SalaryTaxPolicy.fromCode(val.getTaxPolicy())) {
                        //工资
                        if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(val.getType())) {
                            //减
                            salary = salary.subtract(value);
                        } else if (SalaryEntityType.GRANT == SalaryEntityType.fromCode(val.getType())) {
                            //增
                            salary = salary.add(value);
                        }
                    } else if (SalaryTaxPolicy.BONUS == SalaryTaxPolicy.fromCode(val.getTaxPolicy())) {
                        //年终
                        if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(val.getType())) {
                            //减
                            bonus = bonus.subtract(value);
                        } else {
                            //增
                            bonus = bonus.add(value);
                        }
                    }
                } else if (NormalFlag.YES == NormalFlag.fromCode(val.getGrantPolicy())) {
                    //税后的
                    if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(val.getType())) {
                        //减
                        afterTax = afterTax.subtract(value);
                    } else if (SalaryEntityType.GRANT == SalaryEntityType.fromCode(val.getType())) {
                        //增
                        afterTax = afterTax.add(value);
                    }
                }
                //成本计算考勤扣款(或许有加款)和成本项
                if (val.getCategoryId().equals(SalaryConstants.CATEGORY_PUNCH)) {
                    if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(val.getType())) {
                        //减
                        cost = cost.subtract(value);
                    } else if (SalaryEntityType.GRANT == SalaryEntityType.fromCode(val.getType())) {
                        //增
                        cost = cost.add(value);
                    }
                }
                if (SalaryEntityType.COST == SalaryEntityType.fromCode(val.getType())) {
                    cost = cost.add(value);
                }

            }
            //成本= 应发 + 成本项目 -考勤扣款
            cost = cost.add(shouldPay);
            //累加完了开始计税
            BigDecimal salaryTax = calculateSalaryTax(salary);
            BigDecimal bonusTax = calculateBonusTax(bonus, salary);
            //保存计税
            // Long organizationId = punchService.getTopEnterpriseId(ownerId);
            SalaryGroupEntity groupEntity = salaryGroupEntityProvider.findSalaryGroupEntityByOrgANdDefaultId(organizationId, SalaryConstants.ENTITY_ID_SALARYTAX);
            if (null == groupEntity) {
                SalaryDefaultEntity de = salaryDefaultEntityProvider.findSalaryDefaultEntityById(SalaryConstants.ENTITY_ID_SALARYTAX);
                groupEntity = ConvertHelper.convert(de, SalaryGroupEntity.class);
                groupEntity.setDefaultId(de.getId());
                groupEntity.setEditableFlag(NormalFlag.NO.getCode());
                groupEntity.setOrganizationId(organizationId);
                groupEntity.setId(null);
                if (groupEntity.getStatus() == null) {
                    groupEntity.setStatus((byte) 1);
                }
                salaryGroupEntityProvider.createSalaryGroupEntity(groupEntity);
            }
            salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByDetailIdAndGroouEntity(detailId, groupEntity.getId());
            SalaryEmployeeOriginVal salaryVal = processSalaryEmployeeOriginVal(groupEntity, detailId, salaryTax.toString(), ownerId);
//            salaryVal.setCreatorUid();
            salaryEmployeeOriginValProvider.createSalaryEmployeeOriginVal(salaryVal);

            groupEntity = salaryGroupEntityProvider.findSalaryGroupEntityByOrgANdDefaultId(organizationId, SalaryConstants.ENTITY_ID_BONUSTAX);
            if (null == groupEntity) {
                SalaryDefaultEntity de = salaryDefaultEntityProvider.findSalaryDefaultEntityById(SalaryConstants.ENTITY_ID_BONUSTAX);
                groupEntity = ConvertHelper.convert(de, SalaryGroupEntity.class);
                groupEntity.setDefaultId(de.getId());
                groupEntity.setOrganizationId(organizationId);
                groupEntity.setEditableFlag(NormalFlag.NO.getCode());
                groupEntity.setId(null);
                if (groupEntity.getStatus() == null) {
                    groupEntity.setStatus((byte) 1);
                }
                salaryGroupEntityProvider.createSalaryGroupEntity(groupEntity);
            }
            salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByDetailIdAndGroouEntity(detailId, groupEntity.getId());
            SalaryEmployeeOriginVal bonusVal = processSalaryEmployeeOriginVal(groupEntity, detailId, bonusTax.toString(), ownerId);
            salaryEmployeeOriginValProvider.createSalaryEmployeeOriginVal(bonusVal);
            //实发应该是工资+年终-工资税-年终税+税后款
            //工资和年终是减去了扣款而应发没有,所以计算的时候用工资+年终
            realPay = salary.add(bonus).subtract(salaryTax).subtract(bonusTax).add(afterTax);
//            LOGGER.debug("应付{},工资{},年终{},工资税{},年终税{},实付{}", shouldPay, salary, bonus, salaryTax, bonusTax, realPay);
        }
        employee.setRegularSalary(regular);
        employee.setShouldPaySalary(shouldPay);
        employee.setRealPaySalary(realPay);
        employee.setCostSalary(cost);
        if (regular.compareTo(new BigDecimal(1)) < 0) {
            employee.setStatus(SalaryEmployeeStatus.UN_SET.getCode());
        } else if (realPay.compareTo(new BigDecimal(0)) < 0) {
            employee.setStatus(SalaryEmployeeStatus.REALPAY_ERROR.getCode());
        } else {
            employee.setStatus(SalaryEmployeeStatus.NORMAL.getCode());
        }
        if (employee.getId() == null) {
            salaryEmployeeProvider.createSalaryEmployee(employee);
        } else {
            salaryEmployeeProvider.updateSalaryEmployee(employee);
        }
        return employee;
    }

    private SalaryEmployeeOriginVal processSalaryEmployeeOriginVal(SalaryGroupEntity groupEntity, Long detailId, String val, Long ownerId) {
        SalaryEmployeeOriginVal salaryVal = ConvertHelper.convert(groupEntity, SalaryEmployeeOriginVal.class);
        salaryVal.setSalaryValue(val);
        salaryVal.setUserDetailId(detailId);
        salaryVal.setGroupEntityId(groupEntity.getId());
        salaryVal.setGroupEntityName(groupEntity.getName());
        salaryVal.setOwnerId(ownerId);
        salaryVal.setOwnerType("organization");
        return salaryVal;
    }

    @Override
    public BigDecimal calculateBonusTax(BigDecimal bonus, BigDecimal salary) {
//        LOGGER.debug("参数 bonus: {} salary:{}", bonus, salary);
        BigDecimal muni = new BigDecimal(0);
        if (salary.compareTo(new BigDecimal(3500)) < 0) {
            muni = new BigDecimal(3500).subtract(salary);
//            LOGGER.debug("muni = {}", muni);
        }

        BigDecimal taxBase = bonus.subtract(muni).divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_EVEN);
//        LOGGER.debug("taxBase = {}", taxBase);

        //这里要加一个3500的基数计算税
        return calculateSalaryTax(taxBase.add(new BigDecimal(3500)));
    }

    @Override
    public BigDecimal calculateSalaryTax(BigDecimal salary) {
//        LOGGER.debug("计算salary 参数 " + salary);
        BigDecimal result = new BigDecimal(0);
        if (salary.compareTo(new BigDecimal(3500)) <= 0) {
            return result;
        }
        BigDecimal taxBase = salary.subtract(new BigDecimal(3500));
//        LOGGER.debug("taxBase = {}", taxBase);
        if (taxBase.compareTo(new BigDecimal(1500)) <= 0) {
            result = taxBase.multiply(new BigDecimal(3)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN);
        } else if (taxBase.compareTo(new BigDecimal(4500)) <= 0) {
            result = taxBase.multiply(new BigDecimal(10)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN).subtract(new BigDecimal(105));
        } else if (taxBase.compareTo(new BigDecimal(9000)) <= 0) {
            result = taxBase.multiply(new BigDecimal(20)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN).subtract(new BigDecimal(555));
        } else if (taxBase.compareTo(new BigDecimal(35000)) <= 0) {
            result = taxBase.multiply(new BigDecimal(25)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN).subtract(new BigDecimal(1005));
        } else if (taxBase.compareTo(new BigDecimal(55000)) <= 0) {
            result = taxBase.multiply(new BigDecimal(30)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN).subtract(new BigDecimal(2755));
        } else if (taxBase.compareTo(new BigDecimal(80000)) <= 0) {
            result = taxBase.multiply(new BigDecimal(35)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN).subtract(new BigDecimal(5505));
        } else {
            result = taxBase.multiply(new BigDecimal(45)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN).subtract(new BigDecimal(13505));
        }
        return result;
    }


    private String checkImportEmployeeSalaryTitle(Map<String, String> titleMap, Long organizationId) {
        List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listOpenSalaryGroupEntityByOrgId(organizationId);
//        List<String> titleList = new ArrayList<String>(titleMap.values());
        if (!"手机".equals(titleMap.get("A"))) {
            LOGGER.error("第一列不是手机而是" + titleMap.get("A"));
            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        if (!"姓名".equals(titleMap.get("B"))) {
            LOGGER.error("第2列不是姓名而是" + titleMap.get("B"));

            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        if (null != groupEntities) {
            for (int i = 0; i < groupEntities.size(); i++) {
                try {
                    if (!groupEntities.get(i).getName().equals(titleMap.get(GetExcelLetter(i + 3)))) {
                        LOGGER.error("第{}列不是{}而是{}", (i + 1), groupEntities.get(i).getName(), titleMap.get(GetExcelLetter(i + 1)));
                        return ImportFileErrorType.TITLE_ERROR.getCode();
                    }
                } catch (Exception e) {
                    LOGGER.error("检测title报错了,可能是某个数组越界 ", e);
                    return ImportFileErrorType.TITLE_ERROR.getCode();
                }
            }
        }
        return null;
    }

    private List processorExcel(MultipartFile file) {
        try {
            List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
            if (resultList.isEmpty()) {
                LOGGER.error("File content is empty");
                throw RuntimeErrorException.errorWith(SalaryConstants.SCOPE, SalaryConstants.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            return resultList;
        } catch (IOException e) {
            LOGGER.error("file process excel error ", e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ImportFileResponse getImportResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }


    @Override
    public OutputStream getSalaryDetailsOutPut(Long ownerId, String month, Long taskId, Integer namespaceId) {
//        String toMonth = salaryGroupProvider.getMonthByOwnerId(ownerId);
//        NormalFlag isFile = NormalFlag.fromCode(month.equals(toMonth) ? (byte) 0 : (byte) 1);
        XSSFWorkbook wb = getSalaryDetailsWB(ownerId, month, taskId, namespaceId);
        // 设置response的Header
//        OutputStream outputStream = new ByteArrayOutputStream(out);

        return writeOutPut(wb);
    }

    private XSSFWorkbook getSalaryDetailsWB(Long ownerId, String month, Long taskId, Integer namespaceId) {


        NormalFlag isFile = isMonthFile(month);
//        if (isFile == NormalFlag.NO) {
        Long organizationId = punchService.getTopEnterpriseId(ownerId);
        List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listOpenSalaryGroupEntityByOrgId(organizationId);
//        } else {
//            groupEntities = salaryEmployeePeriodValProvider.listOpenSalaryGroupEntityByOrgId(ownerId, month);
//        }
        List<SalaryEntityCategory> categories = salaryEntityCategoryProvider.listSalaryEntityCategory();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        String sheetName = "sheet1";
        createSalaryDetailsHead(wb, sheet, groupEntities, categories);
        //人员列表
        // TODO: 2018/1/25
        //如果查询month和薪酬正在做的month一样,就是查未归档,否则查已归档
        List<Long> detailIds = organizationService.listDetailIdWithEnterpriseExclude(null,
                namespaceId, ownerId, null, null, null, null, null, Integer.MAX_VALUE - 1, null, null
        );
//        LOGGER.debug("getSalaryDetailsWB detailIds : " + StringHelper.toJsonString(detailIds));

        int processNum = 0;
        for (Long detailId : detailIds) {
            createSalaryDetailRow(sheet, detailId, groupEntities, categories, isFile, month, ownerId);
            if (null != taskId) {
                taskService.updateTaskProcess(taskId, (++processNum * 100) / detailIds.size());
            }

        }
        return wb;
    }

    private NormalFlag isMonthFile(String month) {
        //todo: 现在只导出归档的内容 今后有没有可能导出未归档内容,我认为是没可能的
        return NormalFlag.YES;
    }

    private String getDepartmentName(Long detailId) {
        Long departmentId = organizationService.getDepartmentByDetailId(detailId);
        Organization department = organizationProvider.findOrganizationById(departmentId);
        if (department != null)
            return department.getName();
        return "";
    }

    private void createSalaryDetailRow(XSSFSheet sheet, Long detailId, List<SalaryGroupEntity> groupEntities, List<SalaryEntityCategory> categories, NormalFlag isFile, String month, Long ownerId) {

        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        //  获取部门名称
        String depName = archivesService.convertToOrgNames(archivesService.getEmployeeDepartment(detailId));
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        if (null == detail) {
            LOGGER.error("没有找到这个人 detailId = " + detailId);
            return;
        } else {
            row.createCell(++i).setCellValue(detail.getContactName());
            row.createCell(++i).setCellValue(detail.getContactToken());
            row.createCell(++i).setCellValue(detail.getEmployeeNo());
            row.createCell(++i).setCellValue(depName);
            row.createCell(++i).setCellValue(detail.getIdNumber());
            row.createCell(++i).setCellValue(detail.getSalaryCardNumber());
        }
        //  添加在职离职状态
        String employeeStatus = "";
        switch (EmployeeStatus.fromCode(detail.getEmployeeStatus())) {
            case DISMISSAL:
                employeeStatus = "离职";
                break;
            case INTERNSHIP:
                employeeStatus = "实习";
                break;
            case ON_THE_JOB:
                employeeStatus = "在职";
                break;
            default:
                employeeStatus = "实习";
                break;
        }
        row.createCell(++i).setCellValue(employeeStatus);

        if (null != categories && null != groupEntities) {
            for (SalaryEntityCategory category : categories) {
                Cell categoryCell = null;
                if (!category.getId().equals(SalaryConstants.CATEGORY_REDU)) {
                    //其他项不增加合计,其他的增加
                    categoryCell = row.createCell(++i);
                }
                BigDecimal categorySum = new BigDecimal(0);
                for (SalaryGroupEntity entity : groupEntities) {
                    //category不对的直接跳过先
                    if (!entity.getCategoryId().equals(category.getId())) {
                        continue;
                    }
                    SalaryEmployeeOriginVal val = null;

                    if (isFile == NormalFlag.NO) {
                        //未归档就查当下设置的
                        val = salaryEmployeeOriginValProvider.findSalaryEmployeeOriginValByDetailId(entity.getId(), detailId);
                    } else {
                        //已归档就查历史的
                        SalaryEmployeePeriodVal pval = salaryEmployeePeriodValProvider.findSalaryEmployeePeriodValByGroupEntityIdByDetailId(entity.getId(), detailId, month);
                        if (null != pval) {
                            val = ConvertHelper.convert(pval, SalaryEmployeeOriginVal.class);
                        }
                    }


                    if (val == null) {
                        row.createCell(++i).setCellValue("");
                        continue;
                    }
                    row.createCell(++i).setCellValue(val.getSalaryValue());
                    BigDecimal entityValue = new BigDecimal(0);
                    try {
                        entityValue = new BigDecimal(val.getSalaryValue());
                    } catch (Exception e) {

                    }

                    if (SalaryEntityType.DEDUCTION == SalaryEntityType.fromCode(entity.getType())) {
                        //减
                        categorySum = categorySum.subtract(entityValue);
                    } else {
                        //增
                        categorySum = categorySum.add(entityValue);
                    }

                }
                if (null != categoryCell) {
                    categoryCell.setCellValue(categorySum.toString());
                }
            }
        }
        SalaryEmployee employee = null;
        if (isFile == NormalFlag.NO) {
            employee = salaryEmployeeProvider.findSalaryEmployeeByDetailId(ownerId, detailId);
        } else {
            SalaryEmployeesFile eFile = salaryEmployeesFileProvider.findSalaryEmployeesFileByDetailIDAndMonth(ownerId, detailId, month);
            if (null != eFile) {
                employee = ConvertHelper.convert(eFile, SalaryEmployee.class);
            }
        }

        if (null == employee) {
            row.createCell(++i).setCellValue("0");
            row.createCell(++i).setCellValue("0");
            row.createCell(++i).setCellValue("0");
        } else {
            row.createCell(++i).setCellValue(objToString(employee.getShouldPaySalary()));
            if (null != employee.getShouldPaySalary() && null != employee.getRealPaySalary()) {
                row.createCell(++i).setCellValue(objToString(employee.getShouldPaySalary().subtract(employee.getRealPaySalary())));
            } else {
                row.createCell(++i).setCellValue("0");
            }
            row.createCell(++i).setCellValue(objToString(employee.getRealPaySalary()));
        }
    }

    private void createSalaryDetailsHead(XSSFWorkbook wb, XSSFSheet sheet, List<SalaryGroupEntity> groupEntities, List<SalaryEntityCategory> categories) {
        Row row = sheet.createRow(sheet.getLastRowNum());
        int i = -1;
        row.createCell(++i).setCellValue("姓名");
        row.createCell(++i).setCellValue("手机");
        row.createCell(++i).setCellValue("工号");
        row.createCell(++i).setCellValue("部门");
        row.createCell(++i).setCellValue("身份证号");
        row.createCell(++i).setCellValue("工资卡号");
        row.createCell(++i).setCellValue("在职离职状态");

        if (null != categories && null != groupEntities) {
            for (SalaryEntityCategory category : categories) {
                if (!category.getId().equals(SalaryConstants.CATEGORY_REDU)) {
                    row.createCell(++i).setCellValue(category.getCategoryName() + "合计");
                }
                for (SalaryGroupEntity entity : groupEntities) {
                    if (entity.getCategoryId().equals(category.getId())) {
                        row.createCell(++i).setCellValue(entity.getName());
                    }
                }
            }
        }
        row.createCell(++i).setCellValue("应发合计");
        row.createCell(++i).setCellValue("扣款合计");
        row.createCell(++i).setCellValue("实发合计");

    }

    @Override
    public OutputStream getDepartStatisticsOutPut(Long ownerId, String month, Long taskId, Integer namespaceId) {
        XSSFWorkbook wb = getDepartStatisticsWB(ownerId, month, taskId, namespaceId);
        return writeOutPut(wb);
    }

    private XSSFWorkbook getDepartStatisticsWB(Long ownerId, String month, Long taskId, Integer namespaceId) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        String sheetName = "sheet1";
        createDepartStatisticsHead(wb, sheet);
        List<SalaryDepartStatistic> departmentStats = salaryDepartStatisticProvider.listFiledSalaryDepartStatistic(ownerId, month);
//        LOGGER.debug("归档薪酬部门文件的stats:\n" + StringHelper.toJsonString(departmentStats));
        if (null != departmentStats) {
            for (SalaryDepartStatistic stat : departmentStats) {
                createDepartStatisticsRow(sheet, stat);
            }
        }
        return wb;
    }

    private String objToString(Object o) {
        if (null == o) {
            return "";
        }
        return o.toString();
    }

    private void createDepartStatisticsRow(XSSFSheet sheet, SalaryDepartStatistic stat) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(stat.getDeptName());
        row.createCell(++i).setCellValue(objToString(stat.getRegularSalary()));
        row.createCell(++i).setCellValue(objToString(stat.getShouldPaySalary()));
        row.createCell(++i).setCellValue(objToString(stat.getRealPaySalary()));
        row.createCell(++i).setCellValue(objToString(stat.getCostSalary()));
        row.createCell(++i).setCellValue(objToString(stat.getCostMomSalary()) + "%");
        row.createCell(++i).setCellValue(objToString(stat.getCostYoySalary()) + "%");

    }

    private void createDepartStatisticsHead(XSSFWorkbook wb, XSSFSheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum());
        CreationHelper factory = wb.getCreationHelper();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = factory.createClientAnchor();
        int i = -1;
        Cell cell = row.createCell(++i);
        cell.setCellValue("部门");
        cell = row.createCell(++i);
        cell.setCellValue("固定工资合计");
        cell = row.createCell(++i);
        cell.setCellValue("应发合计");
        cell = row.createCell(++i);
        cell.setCellValue("实发合计");
        cell = row.createCell(++i);
        cell.setCellValue("人工成本合计");
        cell = row.createCell(++i);
        cell.setCellValue("人工成本环比");
        XSSFComment commentA = drawing.createCellComment(anchor);
        RichTextString strA = factory.createRichTextString("(本月人工成本-上月人工成本)/上月人工成本x100%");
        commentA.setString(strA);
        commentA.setAuthor("zuolin");
        cell.setCellComment(commentA);
        cell = row.createCell(++i);
        cell.setCellValue("人工成本同比");
        anchor.setCol1(cell.getColumnIndex());
        commentA = drawing.createCellComment(anchor);
        strA = factory.createRichTextString("(本月人工成本-上年度同月人工成本)/上年度同月人工成本x100%");
        commentA.setString(strA);
        commentA.setAuthor("zuolin");

        cell.setCellComment(commentA);

    }

    /**
     * 获取key在redis操作的valueOperations
     */
    private ValueOperations<String, String> getValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return valueOperations;
    }


    /**
     * 清除redis中key的缓存
     */
    private void deleteValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.delete(key);
    }

    @Override
    public GetSalaryTaskStatusResponse getSalaryTaskStatus(GetSalaryTaskStatusCommand cmd) {
        Byte status = NormalFlag.NO.getCode();
        String key = cmd.getReportToken().trim();
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if (null == value) {
            status = NormalFlag.YES.getCode();
        }
        return new GetSalaryTaskStatusResponse(status);
    }

    @Override
    public OutputStream getEmployeeSalaryOutPut(Long ownerId, Long taskId, Integer namespaceId) {
        XSSFWorkbook wb = getEmployeeSalaryWB(ownerId, taskId, namespaceId);

        // 设置response的Header
//        OutputStream outputStream = new ByteArrayOutputStream(out);

        return writeOutPut(wb);
    }

    private XSSFWorkbook getEmployeeSalaryWB(Long ownerId, Long taskId, Integer namespaceId) {
        Long organizationId = punchService.getTopEnterpriseId(ownerId);
        List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listOpenSalaryGroupEntityByOrgId(organizationId);

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        String sheetName = "sheet1";
        createEmployeeSalaryHead(wb, sheet, groupEntities);
        //人员列表
        // TODO: 2018/1/25
        List<Long> detailIds = organizationService.listDetailIdWithEnterpriseExclude(null,
                namespaceId, ownerId, null, null, null, null, null, Integer.MAX_VALUE - 1, null, null
        );
        int processNum = 0;
        for (Long detailId : detailIds) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            if (null == detail) {
                continue;
            }
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            int i = -1;
            row.createCell(++i).setCellValue(detail.getContactToken());
            row.createCell(++i).setCellValue(detail.getContactName());
            for (SalaryGroupEntity entity : groupEntities) {

                if (SalaryEntityStatus.CLOSE == SalaryEntityStatus.fromCode(entity.getStatus())) {
                    //跳过不是开启状态的
                    continue;
                }
                SalaryEmployeeOriginVal val = salaryEmployeeOriginValProvider.findSalaryEmployeeOriginValByDetailId(entity.getId(), detailId);
                if (val != null) {
                    row.createCell(++i).setCellValue(val.getSalaryValue());
                } else {
                    row.createCell(++i).setCellValue("");
                }
            }
            taskService.updateTaskProcess(taskId, (++processNum * 100) / detailIds.size());

        }
        return wb;
    }

    private OutputStream writeOutPut(XSSFWorkbook wb) {


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    /**
     * 如果month为空:则找归档里面最新的month
     * 如果month不是空,则先找归档 找不到再找未归档,都没有则返回空
     * */
    public GetSalaryGroupStatusResponse getSalaryGroupStatus(GetSalaryGroupStatusCommand cmd) {
        GetSalaryGroupStatusResponse response = new GetSalaryGroupStatusResponse();
        String month = cmd.getMonth();
        if (null == month)
            findSalaryMonth(cmd.getOwnerId());
        response.setMonth(month);
        SalaryGroupsFile groupFile = salaryGroupsFileProvider.findSalaryGroupsFile(cmd.getOwnerId(), cmd.getMonth());
        if (null != groupFile) {
            if (null != groupFile.getCreateTime()) {
                response.setCreateTime(groupFile.getCreateTime().getTime());
                response.setCreatorName(groupFile.getCreatorName());
            }

            if (null != groupFile.getFileTime()) {
                response.setFileTime(groupFile.getFileTime().getTime());
                response.setFilerName(groupFile.getFilerName());
            }

        } else {
            SalaryGroup sg = salaryGroupProvider.findSalaryGroup(cmd.getOwnerId(), cmd.getMonth());
            if (null != sg) {
                if (null != sg.getCreateTime()) {
                    response.setCreateTime(sg.getCreateTime().getTime());
                    response.setCreatorName(sg.getCreatorName());
                }
            }
        }
        response.setExportExcels(getExportExcels(cmd.getOwnerId(), cmd.getMonth()));
        return response;
    }

    private List<ExportExcelDTO> getExportExcels(Long ownerId, String month) {
        List<ExportExcelDTO> result = new ArrayList<>();
        SalaryGroupsReportResource resource = salaryGroupsReportResourceProvider.findSalaryGroupsReportResourceByPeriodAndType(ownerId, month, SalaryReportType.SALARY_DETAIL.getCode());
        if (null != resource) {
            result.add(new ExportExcelDTO(SalaryReportType.SALARY_DETAIL.getDescri(), String.valueOf(SalaryReportType.SALARY_DETAIL.getCode()), contentServerService.parserUri(resource.getUrl())));
        } else {
            result.add(new ExportExcelDTO(SalaryReportType.SALARY_DETAIL.getDescri(), String.valueOf(SalaryReportType.SALARY_DETAIL.getCode())));
        }

        resource = salaryGroupsReportResourceProvider.findSalaryGroupsReportResourceByPeriodAndType(ownerId, month, SalaryReportType.DPT_STATISTIC.getCode());
        if (null != resource) {
            result.add(new ExportExcelDTO(SalaryReportType.DPT_STATISTIC.getDescri(), String.valueOf(SalaryReportType.SALARY_DETAIL.getCode()), contentServerService.parserUri(resource.getUrl())));
        } else {
            result.add(new ExportExcelDTO(SalaryReportType.DPT_STATISTIC.getDescri(), String.valueOf(SalaryReportType.SALARY_DETAIL.getCode())));
        }

        return result;
    }

    @Override
    public void exportSalaryReport(ExportSalaryReportCommand cmd) {
        Map<String, Object> params = new HashedMap();
        params.put("ownerId", cmd.getOwnerId());
        params.put("organizationId", cmd.getOrganizationId());
        params.put("namespaceId", UserContext.getCurrentNamespaceId() + "");
        params.put("month", cmd.getMonth());
        params.put("excelToken", cmd.getExportToken());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date();
        try {
            date = monthSF.get().parse(cmd.getMonth());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Organization org = organizationProvider.findOrganizationById(cmd.getOwnerId());
        String fileName = sf.format(date) + org.getName() +
                SalaryReportType.fromCode(Byte.valueOf(cmd.getExportToken())).getDescri() + ".xlsx";
//        params.put("name", fileName);

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), SalaryExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    private static ExecutorService calculateExecutorPool = Executors.newFixedThreadPool(5);

    @Override
    public FileSalaryGroupResponse fileSalaryGroup(FileSalaryGroupCommand cmd) {
        String month = salaryGroupProvider.getMonthByOwnerId(cmd.getOwnerId());
        if (null == month) {
            throw RuntimeErrorException.errorWith(SalaryConstants.SCOPE, SalaryConstants.ERROR_SALARY_GROUP_NO_DATA,
                    "本月没有数据不能计算和归档");
        }
        if (!month.equals(cmd.getMonth())) {
            throw RuntimeErrorException.errorWith(SalaryConstants.SCOPE, SalaryConstants.ERROR_SALARY_MONTH_WRONG,
                    "归档月份错误! 月份[" + month + "],参数[" + cmd + "]");
        }
        String seed = SocialSecurityConstants.SCOPE + "fileSocialSecurity" + cmd.getOwnerId() + DateHelper.currentGMTTime().getTime();
        String key = Base64.getEncoder().encodeToString(seed.getBytes());
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int timeout = 15;
        TimeUnit unit = TimeUnit.MINUTES;
        final Long userId = UserContext.currentUserId();
        final String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        // 先放一个和key一样的值,表示这个人key有效
        valueOperations.set(key, key, timeout, unit);
        //线程池中处理计算规则
        calculateExecutorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //计算
                    calculateReports(cmd.getOwnerId(), month, userId);

                    //归档
                    fileSalaryGroup(cmd.getOwnerId(), month, cmd.getOrganizationId(), userId, token, namespaceId);
                } catch (Exception e) {
                    LOGGER.error("calculate reports error!! cmd is  :" + cmd, e);
                } finally {
                    //处理完成删除key,表示这个key已经完成了
                    deleteValueOperations(key);
                }
            }
        });

        return new FileSalaryGroupResponse(key);
    }

    private void fileSalaryGroup(Long ownerId, String month, Long organizationId, Long userId, String token, Integer namespaceId) {

        //表:groupfile employeefile employeePeriodVal dptStatistics
//        Long userId = UserContext.currentUserId();
        Timestamp filerTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        salaryGroupsFileProvider.deleteGroupsFile(ownerId, month);
        salaryEmployeesFileProvider.deleteEmployeesFile(ownerId, month);
        salaryEmployeePeriodValProvider.deleteEmployeePeriodVals(ownerId, month);
        // 由于部门汇总的归档未归档在一张表里,所以带上归档状态(删除归档的)
        salaryDepartStatisticProvider.deleteSalaryDepartStatistic(ownerId, NormalFlag.YES.getCode(), month);
        List<SalaryDepartStatistic> dpts = salaryDepartStatisticProvider.listSalaryDepartStatistic(ownerId, NormalFlag.NO.getCode(), month);
        if (null != dpts) {
            for (SalaryDepartStatistic dpt : dpts) {
                dpt.setIsFile(NormalFlag.YES.getCode());
                dpt.setFileTime(filerTime);
                dpt.setFilerUid(userId);
                salaryDepartStatisticProvider.updateSalaryDepartStatistic(dpt);
            }
        }
        SalaryGroup group = salaryGroupProvider.findSalaryGroup(ownerId, month);
        SalaryGroupsFile sgf = ConvertHelper.convert(group, SalaryGroupsFile.class);
        sgf.setFileTime(filerTime);
        sgf.setFilerUid(userId);
        sgf.setFilerName(findNameByOwnerAndUser(ownerId, userId));
//        Organization organization = organizationProvider.findOrganizationById(ownerId);
//        OrganizationMember member = punchService.findOrganizationMemberByUIdAndOrgId(userId, organization.getPath());
//        if (null != member) {
//            sgf.setFilerName(member.getContactName());
//        }

        List<SalaryEmployee> ses = salaryEmployeeProvider.listSalaryEmployees(ownerId, month);
        if (null != ses) {
            for (SalaryEmployee se : ses) {
                SalaryEmployeesFile sef = ConvertHelper.convert(se, SalaryEmployeesFile.class);
                sef.setFilerUid(userId);
                sef.setFileTime(filerTime);

                salaryEmployeesFileProvider.createSalaryEmployeesFile(sef);
            }
        }
        Organization org = organizationProvider.findOrganizationById(ownerId);
        createEmployeePeriodVals(ownerId, month, ses);
        //保存excel到contentServer ,以后直接给归档excel
        //工资明细表
        OutputStream ops = getSalaryDetailsOutPut(ownerId, month, null, namespaceId);

        String fileName = month + org.getName() +
                SalaryReportType.SALARY_DETAIL.getDescri() + ".xlsx";
        ByteArrayOutputStream os = (ByteArrayOutputStream) ops;
        InputStream ins = new ByteArrayInputStream(os.toByteArray());
        UploadCsFileResponse resp = contentServerService.uploadFileToContentServer(ins, fileName, token);
//        LOGGER.debug("上传文件,content serverresponse " + resp);
//        contentServerService.parserUri(token)
        saveSalaryGroupReport(sgf, SalaryReportType.SALARY_DETAIL, resp.getResponse().getUri(), resp.getResponse().getUrl());
        //部门汇总表
        ops = getDepartStatisticsOutPut(ownerId, month, null, UserContext.getCurrentNamespaceId());

        fileName = month + org.getName() +
                SalaryReportType.DPT_STATISTIC.getDescri() + ".xlsx";
        os = (ByteArrayOutputStream) ops;
        ins = new ByteArrayInputStream(os.toByteArray());
        resp = contentServerService.uploadFileToContentServer(ins, fileName, token);
//        contentServerService.parserUri(token)
        saveSalaryGroupReport(sgf, SalaryReportType.DPT_STATISTIC, resp.getResponse().getUri(), resp.getResponse().getUrl());


        salaryGroupsFileProvider.createSalaryGroupsFile(sgf);
    }

    private void saveSalaryGroupReport(SalaryGroupsFile sgf, SalaryReportType reportType, String uri, String url) {
        //先删后插 本月的
        salaryGroupsReportResourceProvider.deleteSalaryGroupsReportResourceByPeriodAndType(sgf.getOwnerId(), sgf.getSalaryPeriod(), reportType.getCode());
        SalaryGroupsReportResource resource = ConvertHelper.convert(sgf, SalaryGroupsReportResource.class);
        resource.setReportType(reportType.getCode());
        resource.setUri(uri);
        resource.setUrl(url);
        salaryGroupsReportResourceProvider.createSalaryGroupsReportResource(resource);
    }

    private void createEmployeePeriodVals(Long ownerId, String month, List<SalaryEmployee> ses) {
        if (null != ses) {
            for (SalaryEmployee se : ses) {
                List<SalaryEmployeeOriginVal> entities = salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValsByDetailId(se.getUserDetailId());
                if (null != entities) {
                    for (SalaryEmployeeOriginVal entity : entities) {
                        SalaryEmployeePeriodVal pv = ConvertHelper.convert(entity, SalaryEmployeePeriodVal.class);
                        pv.setSalaryPeriod(month);
                        salaryEmployeePeriodValProvider.createSalaryEmployeePeriodVal(pv);
                    }
                }
            }
        }
    }

    private void calculateReports(Long ownerId, String month, Long userId) {
        //重新计算每个人的数据
        List<SalaryEmployee> employees = salaryEmployeeProvider.listSalaryEmployees(ownerId, month, SalaryEmployeeStatus.NORMAL);

        if (null != employees) {
            for (SalaryEmployee e : employees) {
                calculateEmployee(ownerId, e.getUserDetailId(), e.getOrganizationId());

            }
        }
        //计算部门汇总
        calculateDptStatistic(ownerId, month, userId);

    }

    private void calculateDptStatistic(Long ownerId, String month, Long userId) {
        List<Organization> orgs = findOrganizationDpts(ownerId);
        //删除未归档的 该子公司的 该月的 所有statistics
        salaryDepartStatisticProvider.deleteSalaryDepartStatistic(ownerId, NormalFlag.NO.getCode(), month);
        //循环子公司所有部门计算部门的汇总
        for (Organization dpt : orgs) {
            if (null == dpt) {
                continue;
            }
            calculateSocialSecurityDptReports(ownerId, dpt, month, userId);
        }


    }

    private void calculateSocialSecurityDptReports(Long ownerId, Organization dpt, String month, Long userId) {
        List<Long> detailIds = organizationService.listDetailIdWithEnterpriseExclude(null,
                dpt.getNamespaceId(), dpt.getId(), null, null, null, null, null, Integer.MAX_VALUE - 1, null, null
        );
        SalaryDepartStatistic statistic = processSalaryDepartStatistic(ownerId, detailIds, month, dpt);
        statistic.setCreatorUid(userId);
        salaryDepartStatisticProvider.createSalaryDepartStatistic(statistic);
    }

    private SalaryDepartStatistic processSalaryDepartStatistic(Long ownerId, List<Long> detailIds, String month, Organization dpt) {
        SalaryDepartStatistic statistic = new SalaryDepartStatistic();
        statistic = salaryEmployeeProvider.calculateDptReport(detailIds, statistic, ownerId, month);
        statistic.setDeptName(getDptPathName(dpt));
        statistic.setDeptId(dpt.getId());
        statistic.setIsFile(NormalFlag.NO.getCode());
        //同比
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(monthSF.get().parse(month));
        } catch (ParseException e) {
            LOGGER.error("month [{}]不能被辨别 ", month, e);
        }
        calendar.add(Calendar.MONTH, -12);
        SalaryDepartStatistic lastYear = salaryDepartStatisticProvider.findSalaryDepartStatisticByDptAndMonth(dpt.getId(), monthSF.get().format(calendar.getTime()));
        if (null != statistic.getCostSalary() && null != lastYear && lastYear.getCostSalary() != null && lastYear.getCostSalary().compareTo(new BigDecimal(0)) > 0) {
            statistic.setCostYoySalary(statistic.getCostSalary().subtract(lastYear.getCostSalary()).divide(lastYear.getCostSalary(), 4).multiply(new BigDecimal(100)));

        }
        //环比
        try {
            calendar.setTime(monthSF.get().parse(month));
        } catch (ParseException e) {
            LOGGER.error("month [{}]不能被辨别 ", month, e);
        }
        calendar.add(Calendar.MONTH, -1);
        SalaryDepartStatistic lastMonth = salaryDepartStatisticProvider.findSalaryDepartStatisticByDptAndMonth(dpt.getId(), monthSF.get().format(calendar.getTime()));
        if (null != statistic.getCostSalary() && null != lastMonth && lastMonth.getCostSalary() != null && lastMonth.getCostSalary().compareTo(new BigDecimal(0)) > 0) {
            statistic.setCostMomSalary(statistic.getCostSalary().subtract(lastMonth.getCostSalary()).divide(lastMonth.getCostSalary(), 4).multiply(new BigDecimal(100)));
        }
//        LOGGER.debug("计算[{}]公司的汇总数据,人员列表[{}] ,结果[{}] ", dpt.getName(), StringHelper.toJsonString(detailIds), StringHelper.toJsonString(statistic));
        return statistic;
    }

    private List<Organization> findOrganizationDpts(Organization org) {
        List<String> groupTypeList = new ArrayList<String>();

//        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        List<Organization> result = new ArrayList<>();

        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "%", groupTypeList);
        if (org.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
            result.add(org);
        }
        if (null != orgs) {
            result.addAll(orgs);
        }
        return result;
    }

    private List<Organization> findOrganizationDpts(Long orgId) {
        Organization org = organizationProvider.findOrganizationById(orgId);
        return findOrganizationDpts(org);
    }

    @Override
    public void newSalaryMonth(NewSalaryMonthCommand cmd) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.SALARY_NEWMONTH_LOCK.getCode() + cmd.getOwnerId()).enter(() -> {
            String month = salaryGroupProvider.getMonthByOwnerId(cmd.getOwnerId());
            if (null == month) {
                throw RuntimeErrorException.errorWith(SalaryConstants.SCOPE, SalaryConstants.ERROR_SALARY_GROUP_NO_DATA,
                        "本月没有数据不能新建报表");
            }
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(monthSF.get().parse(month));
                calendar.add(Calendar.MONTH, 1);
                month = monthSF.get().format(calendar.getTime());
            } catch (ParseException e) {
                LOGGER.error("月份出错了!{} 不是YYYYMM" + month);
            }
            //重新计算group
            salaryGroupProvider.deleteSalaryGroup(cmd.getOwnerId());
            createMonthSalaryGroup(cmd.getOwnerId(), month);
            //把vals的次月清空数据清零
            salaryEmployeeOriginValProvider.setValueBlank(cmd.getOwnerId());
            //重新计算employee
            calculateEmployees(cmd.getOwnerId(), cmd.getOrganizationId(), UserContext.getCurrentNamespaceId());

            //删除未归档的汇总
            salaryDepartStatisticProvider.deleteSalaryDepartStatistic(cmd.getOwnerId(), NormalFlag.NO.getCode(), null);

            return null;
        });
    }

    private void calculateEmployees(Long ownerId, Long organizationId, Integer namespaceId) {
        List<Long> detailIds = organizationService.listDetailIdWithEnterpriseExclude(null,
                namespaceId, ownerId, null, null, null, null, null, Integer.MAX_VALUE - 1, null, null
        );
        if (null != detailIds) {
            for (Long detailId : detailIds) {
                calculateEmployee(ownerId, detailId, organizationId);

            }
        }
    }

    private SalaryEmployee calculateEmployee(Long ownerId, Long detailId, Long organizationId) {
        List<SalaryEmployeeOriginVal> vals = salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValsByDetailId(detailId);
        SalaryEmployee employee = calculateEmployee(ownerId, detailId, vals, organizationId);
        return employee;
    }

    private Workbook createEmployeeSalaryHeadWB(Long orgId) {
        List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listOpenSalaryGroupEntityByOrgId(orgId);

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        String sheetName = "sheet1";
        createEmployeeSalaryHead(wb, sheet, groupEntities);
        return wb;
    }

    private void createEmployeeSalaryHead(XSSFWorkbook wb, XSSFSheet sheet, List<SalaryGroupEntity> groupEntities) {
        Row row = sheet.createRow(sheet.getLastRowNum());
        CreationHelper factory = wb.getCreationHelper();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = factory.createClientAnchor();
        int i = -1;
        Cell cell = row.createCell(++i);
        cell.setCellValue("手机");
        XSSFComment commentA = drawing.createCellComment(anchor);
        RichTextString strA = factory.createRichTextString("必填项：员工手机号，用于匹配系统用户");
        commentA.setString(strA);
        commentA.setAuthor("zuolin");
        cell.setCellComment(commentA);

        cell = row.createCell(++i);
        cell.setCellValue("姓名");
        anchor.setCol1(cell.getColumnIndex());
        XSSFComment commentB = drawing.createCellComment(anchor);
        RichTextString strB = factory.createRichTextString("员工姓名仅作为提示作用，编辑将不会生效");
        commentB.setString(strB);
        commentB.setAuthor("zuolin");
        cell.setCellComment(commentB);

        if (null != groupEntities)
            for (SalaryGroupEntity entity : groupEntities) {
                cell = row.createCell(++i);
                cell.setCellValue(entity.getName());
                anchor.setCol1(cell.getColumnIndex());
                if (SalaryEntityType.REDUN != SalaryEntityType.fromCode(entity.getType())) {
                    XSSFComment comment = drawing.createCellComment(anchor);
                    StringBuilder sb = new StringBuilder();
                    sb.append("金额大于0");
                    sb.append("\n");
                    sb.append("类型：");
                    sb.append(SalaryEntityType.fromCode(entity.getType()).getDescri());
                    sb.append("\n");
                    sb.append("数据策略：");
                    if (NormalFlag.YES == NormalFlag.fromCode(entity.getDataPolicy())) {
                        sb.append("次月清零");
                    } else {
                        sb.append("次月沿用");
                    }
                    sb.append("\n");
                    sb.append("发放规则：");
                    if (NormalFlag.YES == NormalFlag.fromCode(entity.getGrantPolicy())) {
                        sb.append("税后计算");
                    } else {
                        sb.append("税前计算");
                    }
                    sb.append("\n");
                    sb.append("计税方式：");
                    if (NormalFlag.YES == NormalFlag.fromCode(entity.getTaxPolicy())) {
                        sb.append("年终奖");
                    } else {
                        sb.append("工资");
                    }

                    RichTextString str = factory.createRichTextString(sb.toString());
                    comment.setString(str);
                    comment.setAuthor("zuolin");
                    cell.setCellComment(comment);
                }
            }
    }

    public HttpServletResponse download(Workbook workbook, String fileName, HttpServletResponse response) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));// new String(fileName.getBytes()));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/msexcel");
            toClient.write(out.toByteArray());
            toClient.flush();
            toClient.close();

//            // 读取完成删除文件
//            if (file.isFile() && file.exists()) {
//                file.delete();
//            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
                    ex.getLocalizedMessage());

        }
        return response;
    }

    private void addNewGroupEntities(Long organizationId, List<SalaryGroupEntityDTO> entities) {
        for (SalaryGroupEntityDTO dto : entities) {
            if (dto.getId() == null) {
//                LOGGER.debug("新增" + dto);
                //id为null的新增
                SalaryGroupEntity entity = ConvertHelper.convert(dto, SalaryGroupEntity.class);
                entity.setDefaultFlag(NormalFlag.NO.getCode());
                entity.setOrganizationId(organizationId);
                salaryGroupEntityProvider.createSalaryGroupEntity(entity);
            }
        }
    }

    private SalaryGroupEntityDTO findSalaryGroupEntityDTOById(List<SalaryGroupEntityDTO> entities, Long id) {
        for (SalaryGroupEntityDTO dto : entities) {
            if (dto.getId() != null && dto.getId().equals(id)) {
                return dto;
            }
        }
        return null;
    }

    public String processZLLink2PayslipDetail(Long payslipDetailId, String salaryPeriod,Long organizationId) {
        return "zl://salary/payslip-detail?payslipDetailId=" + payslipDetailId + "&salaryPeriod=" + salaryPeriod + "&organizationId=" + organizationId;
    }

    @Override
    public ListYearPayslipSummaryResponse listYearPayslipSummary(ListYearPayslipSummaryCommand cmd) {
        List<SalaryPayslip> results = salaryPayslipProvider.listSalaryPayslip(cmd.getOwnerId(), cmd.getOrganizationId(), cmd.getPayslipYear(), null, null, Integer.MAX_VALUE - 1);
        if (null == results) {
            return null;
        }
        List<MonthPayslipDTO> monthPayslip = new ArrayList<>();
        SortedMap<String, Integer> payslipMap = processYearMonthMap(cmd.getPayslipYear());
        for (SalaryPayslip r : results) {
            if (payslipMap.get(r.getSalaryPeriod()) == null) {
                payslipMap.put(r.getSalaryPeriod(), salaryPayslipDetailProvider.countSend(r.getId()));
            } else {
                payslipMap.put(r.getSalaryPeriod(), payslipMap.get(r.getSalaryPeriod()) + salaryPayslipDetailProvider.countSend(r.getId()));
            }
        }
        Iterator i = payslipMap.entrySet().iterator();
        // Display elements
        while (i.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry) i.next();
            MonthPayslipDTO e = new MonthPayslipDTO();
            e.setSalaryPeriod(entry.getKey());
            e.setSendCount(entry.getValue());
            monthPayslip.add(e);
        }
        return new ListYearPayslipSummaryResponse(monthPayslip);
    }

    private SortedMap<String, Integer> processYearMonthMap(String payslipYear) {
        SortedMap<String, Integer> result = new TreeMap<>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(monthSF.get().parse(payslipYear + "12"));
            for (int i = 1; i <= 12; i++) {
                result.put(monthSF.get().format(calendar.getTime()), 0);
                calendar.add(Calendar.MONTH, -1);
            }
        } catch (Exception e) {
            LOGGER.error("processYearMonthMap 出错了", e);
        }
        return result;
    }

    @Override
    public ImportPayslipResponse importPayslip(MultipartFile[] files, ImportPayslipCommand cmd) {
        ArrayList resultList = punchService.processImportExcel2ArrayList(files);

        if(resultList == null || resultList.size() < 1)
        	throw RuntimeErrorException.errorWith(SalaryConstants.SCOPE, SalaryConstants.ERROR_EXCEL_BLANK,
                    "导入的数据为空");
        List<PayslipDetailDTO> details = convertArrayList2PayslipDetailDTOList(resultList, cmd.getOwnerId());
        if(details == null || details.size() == 0)
        	throw RuntimeErrorException.errorWith(SalaryConstants.SCOPE, SalaryConstants.ERROR_EXCEL_BLANK,
                    "导入的数据为空");
        return new ImportPayslipResponse(cmd.getSalaryPeriod(), cmd.getPayslipName(), details);
    }

    public static boolean isContain(String s1, String s2) {
        if (s1 == null)
            return false;
        return s1.contains(s2);
    }

    private List<PayslipDetailDTO> convertArrayList2PayslipDetailDTOList(ArrayList resultList, Long ownerId) {
        // TODO Auto-generated method stub
        List<PayslipDetailDTO> payslipDetailDTOs = new ArrayList<PayslipDetailDTO>();

        String nameCellNumber = null;
        String contactCellNumber = null;
        Map<String, String> cellMap = new HashMap<>();
        RowResult title = null;
        while (true) {
            title = (RowResult) resultList.remove(0);
            for (Entry<String, String> entry : title.getCells().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (null != value) {
                    value = value.trim();
                    if (isContain(value, "姓名")) {
                        nameCellNumber = key;
                    } else if (isContain(value, "手机号码")) {
                        contactCellNumber = key;
                    } else if (!isContain(value, "序号")) {
                        cellMap.put(key, value);
                    }
                }
            }
            if (StringUtils.isNotBlank(nameCellNumber) && StringUtils.isNotBlank(contactCellNumber)) {
                break;
            }
            //没找到姓名和手机号说明不是标题, 清空map
            cellMap.clear();
            if (resultList.size() == 0) {
                throw RuntimeErrorException.errorWith(SalaryConstants.SCOPE, SalaryConstants.ERROR_EXCEL_TITILE,
                        "标题错误,找不到姓名手机号");
            }
        }

        for (int rowIndex = 0; rowIndex < resultList.size(); rowIndex++) {
            RowResult r = (RowResult) resultList.get(rowIndex);
            PayslipDetailDTO dto = new PayslipDetailDTO();
            // 名字去空格 
            dto.setName(r.getCells().get(nameCellNumber));
            dto.setUserContact(r.getCells().get(contactCellNumber));
            OrganizationMemberDetails memebrDetail = null;
            // 手机号为空,手机号找不到用户
            if (null == dto.getUserContact()) {
                dto.setImportResult(PayslipImportResult.NULL_CONTACT.getCode());
            } else {
                memebrDetail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(ownerId, dto.getUserContact());
                if (null == memebrDetail ) {
                    dto.setImportResult(PayslipImportResult.NULL_CONTACT.getCode());
                }else if( memebrDetail.getTargetId().equals(0L) || 
                		OrganizationMemberTargetType.UNTRACK == OrganizationMemberTargetType.fromCode(memebrDetail.getTargetType())){
                    dto.setImportResult(PayslipImportResult.USER_UNTRACK.getCode());
                }else {
                	dto.setName(memebrDetail.getContactName());
                    dto.setImportResult(PayslipImportResult.SUCESS.getCode());
                    dto.setUserId(memebrDetail.getTargetId());
                    dto.setUserDetailId(memebrDetail.getId());
                }
            }
            dto.setPayslipContent(processPayslipContent(r, cellMap));
            payslipDetailDTOs.add(dto);
        }
        return payslipDetailDTOs;
    }

    private List<SalaryPeriodEmployeeEntityDTO> processPayslipContent(RowResult r, Map<String, String> cellMap) {
        List<SalaryPeriodEmployeeEntityDTO> result = new ArrayList<>();
        for (Entry<String, String> entry : cellMap.entrySet()) {
            //key 是列号,cellMap取这列的结果是表头,r取这一列的结果是这一行的值
            SalaryPeriodEmployeeEntityDTO dto = new SalaryPeriodEmployeeEntityDTO();
            dto.setGroupEntityName(entry.getValue());
            dto.setSalaryValue(r.getCells().get(entry.getKey()));
            if("- 0".equals(dto.getSalaryValue()) || "-0".equals(dto.getSalaryValue())){
            	dto.setSalaryValue("0");
            }
            result.add(dto);
        }
        return result;
    }

    public String processSalaryPeriodString(String salaryPeriod) {
        return salaryPeriod.substring(0, 4) + "年" + salaryPeriod.substring(4, 6) + "月";
    }

    @Override
    public SendPayslipResponse sendPayslip(SendPayslipCommand cmd) {
        String salaryPeriodString = processSalaryPeriodString(cmd.getSalaryPeriod());
        SalaryPayslip sp = new SalaryPayslip();
        sp.setName(cmd.getPayslipName());
        sp.setNamespaceId(UserContext.getCurrentNamespaceId());
        sp.setOrganizationId(cmd.getOrganizationId());
        sp.setOwnerId(cmd.getOwnerId());
        sp.setOwnerType("organization");
        sp.setSalaryPeriod(cmd.getSalaryPeriod());
        salaryPayslipProvider.createSalaryPayslip(sp);
        //存数据 发通知
        for (PayslipDetailDTO dto : cmd.getDetails()) {
            if (null == dto.getUserId()) {
                continue;
            }
            SalaryPayslipDetail spd = ConvertHelper.convert(dto, SalaryPayslipDetail.class);
            if(null != dto.getUserDetailId()){
            	OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(dto.getUserDetailId());
            	spd.setName(detail.getContactName());
            }
            spd.setPayslipId(sp.getId());
            spd.setNamespaceId(UserContext.getCurrentNamespaceId());
            spd.setOrganizationId(cmd.getOrganizationId());
            spd.setOwnerId(cmd.getOwnerId());
            spd.setOwnerType("organization");
            spd.setSalaryPeriod(cmd.getSalaryPeriod());
            spd.setStatus(PayslipDetailStatus.SENDED.getCode());
            spd.setViewedFlag(NormalFlag.NO.getCode());
            spd.setPayslipContent(StringHelper.toJsonString(dto.getPayslipContent()));
            salaryPayslipDetailProvider.createSalaryPayslipDetail(spd);
            sendPayslipMessage(spd, salaryPeriodString);
        }
        return new SendPayslipResponse(sp.getId());
    }

    private void sendPayslipMessage(SalaryPayslipDetail spd, String salaryPeriodString) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("salaryDate", salaryPeriodString);
        String content = localeTemplateService.getLocaleTemplateString(0, SalaryConstants.SEND_NOTIFICATION_SCOPE, SalaryConstants.SEND_NOTIFICATION_CODE,
                "zh_CN", map, salaryPeriodString + "工资已发放。");
        sendMessage(content, "工资条发放", spd.getUserId(), spd.getId(), spd.getSalaryPeriod(), spd.getOwnerId());
    }

    private void sendMessage(
            String content, String subject, Long receiverId, Long payslipDetailId, String salaryPeriod, Long ownerId) {

        //  set the message
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiverId)));
        //  set the route
        String url = processZLLink2PayslipDetail(payslipDetailId, salaryPeriod, ownerId);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, subject);
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiverId),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }

    @Override
    public ListMonthPayslipSummaryResponse listMonthPayslipSummary(ListMonthPayslipSummaryCommand cmd) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (null != cmd.getPageAnchor()) {
            locator.setAnchor(cmd.getPageAnchor());
        }
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        List<SalaryPayslip> results = salaryPayslipProvider.listSalaryPayslip(cmd.getOrganizationId(), cmd.getOwnerId(),
                cmd.getSalaryPeriod(), cmd.getName(), locator, pageSize + 1);

        Long nextPageAnchor = null;
        if (null == results || results.size() == 0) {
            return null;
        }
        if (results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        ListMonthPayslipSummaryResponse response = new ListMonthPayslipSummaryResponse(results.stream().map(r -> {
            PayslipDTO dto = ConvertHelper.convert(r, PayslipDTO.class);
            dto.setPayslipId(r.getId());
            dto.setCreateTime(r.getCreateTime().getTime());
            dto.setCreatorName(findNameByOwnerAndUser(r.getOrganizationId(), r.getCreatorUid()));
            dto.setSendCount(salaryPayslipDetailProvider.countSend(r.getId()));
            dto.setConfirmCount(salaryPayslipDetailProvider.countConfirm(r.getId()));
            dto.setViewCount(salaryPayslipDetailProvider.countView(r.getId()));
            dto.setRevokeCount(salaryPayslipDetailProvider.countRevoke(r.getId()));
            return dto;
        }).collect(Collectors.toList()), nextPageAnchor);
        Collections.sort(response.getPayslips());
        return response;
    }

    @Override
    public ListSendPayslipDetailsResponse listSendPayslipDetails(ListSendPayslipDetailsCommand cmd) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (null != cmd.getPageAnchor()) {
            locator.setAnchor(cmd.getPageAnchor());
        }
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        List<SalaryPayslipDetail> results = salaryPayslipDetailProvider.listSalaryPayslipDetail(cmd.getOrganizationId(), cmd.getOwnerId(), cmd.getPayslipId(),
                cmd.getName(), cmd.getStatus(), locator, pageSize + 1);

        Long nextPageAnchor = null;
        if (null == results || results.size() == 0) {
            return null;
        }
        if (results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
//        LOGGER.debug("result " + StringHelper.toJsonString(results));

        List<PayslipDetailDTO> detialDTOs = results.stream().map(r -> {
            PayslipDetailDTO dto = convertPayslipDetailDTO(r);
            return dto;
        }).collect(Collectors.toList());
        List<String> titles = new ArrayList<>();
        for (SalaryPeriodEmployeeEntityDTO entity : detialDTOs.get(0).getPayslipContent()) {
            titles.add(entity.getGroupEntityName());
        }
        return new ListSendPayslipDetailsResponse(detialDTOs, titles, nextPageAnchor);
    }

    private PayslipDetailDTO convertPayslipDetailDTO(SalaryPayslipDetail r) {
        PayslipDetailDTO dto = ConvertHelper.convert(r, PayslipDetailDTO.class);
        dto.setPayslipDetailId(r.getId());
        List<SalaryPeriodEmployeeEntityDTO> content = JSON.parseArray(r.getPayslipContent(), SalaryPeriodEmployeeEntityDTO.class);
        dto.setCreateTime(r.getCreateTime().getTime());
        OrganizationMemberDetails userDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(r.getUserDetailId());
        if (null != userDetail) {
            dto.setName(userDetail.getContactName());
        }
        OrganizationMember member = findMemberByOwnerAndUser(r.getOrganizationId(), r.getCreatorUid());
        if (null != member) {
            dto.setCreatorName(member.getContactName());
            dto.setCreatorDetailId(member.getDetailId());
        }
        dto.setPayslipContent(content);
//        LOGGER.debug("dto:" + dto);
        return dto;
    }

    @Override
    public void resendPayslip(ResendPayslipCommand cmd) {
        List<SalaryPayslipDetail> results = listSalaryPayslipDetail(cmd.getPayslipId(), cmd.getPayslipDetailId());
        for (SalaryPayslipDetail spd : results) {
            if (null != spd) {
                String salaryPeriodString = processSalaryPeriodString(spd.getSalaryPeriod());
                spd.setStatus(PayslipDetailStatus.SENDED.getCode());
                spd.setViewedFlag(NormalFlag.NO.getCode());
                spd.setCreatorUid(UserContext.currentUserId());
                spd.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                salaryPayslipDetailProvider.updateSalaryPayslipDetail(spd);
                sendPayslipMessage(spd, salaryPeriodString);
            }
        }
    }

    private List<SalaryPayslipDetail> listSalaryPayslipDetail(Long payslipId, Long payslipDetailId) {
        List<SalaryPayslipDetail> results = new ArrayList<>();
        if (null != payslipDetailId) {
            results.add(salaryPayslipDetailProvider.findSalaryPayslipDetailById(payslipDetailId));
        } else {
            results.addAll(salaryPayslipDetailProvider.listSalaryPayslipDetailBypayslipId(payslipId));
        }
        return results;
    }

    @Override
    public void revokePayslip(RevokePayslipCommand cmd) {
        List<SalaryPayslipDetail> results = listSalaryPayslipDetail(cmd.getPayslipId(), cmd.getPayslipDetailId());
        for (SalaryPayslipDetail spd : results) {
            if (null != spd) {
                spd.setStatus(PayslipDetailStatus.REVOKED.getCode());
                salaryPayslipDetailProvider.updateSalaryPayslipDetail(spd);
            }
        }

    }

    @Override
    public void deletePayslip(DeletePayslipCommand cmd) {
        List<SalaryPayslipDetail> results = listSalaryPayslipDetail(cmd.getPayslipId(), cmd.getPayslipDetailId());
        for (SalaryPayslipDetail spd : results) {
            if (null != spd) {
                salaryPayslipDetailProvider.deleteSalaryPayslipDetail(spd);
            }
        }
        if (null != cmd.getPayslipId()) {
            salaryPayslipProvider.deleteSalaryPayslip(cmd.getPayslipId());
        }

    }

    @Override
    public ListUserPayslipsResponse listUserPayslips(ListUserPayslipsCommand cmd) {
        Long userId = UserContext.currentUserId();
        if (null == userId) {
            return null;
        }
        List<SalaryPayslipDetail> results = salaryPayslipDetailProvider.listUserSalaryPayslipDetail(userId, cmd.getOrganizationId());
        if (null == results) {
            return null;
        }
        ListUserPayslipsResponse response = new ListUserPayslipsResponse(new ArrayList<>());
        // Display elements
        SortedMap<String, List<MonthPayslipDetailDTO>> resultMap = new TreeMap<>();
        for (SalaryPayslipDetail spd : results) {
            if (null != spd) {
                MonthPayslipDetailDTO dto = new MonthPayslipDetailDTO();
                dto.setCreateTime(spd.getCreateTime().getTime());
                dto.setPayslipDetailId(spd.getId());
                dto.setSalaryPeriod(spd.getSalaryPeriod());
                String year = spd.getSalaryPeriod().substring(0, 4);
                if (null == resultMap.get(year)) {
                    resultMap.put(year, new ArrayList<>());
                }
                resultMap.get(year).add(dto);
            }
        }
        Iterator i = resultMap.entrySet().iterator();

        while (i.hasNext()) {
            Map.Entry<String, List<MonthPayslipDetailDTO>> entry = (Map.Entry) i.next();
            PayslipYearDTO yearDTO = new PayslipYearDTO(entry.getKey(), entry.getValue());
            Collections.sort(yearDTO.getMonthPayslipDetails());
            response.getPayslipYears().add(yearDTO);
        }
        Collections.sort(response.getPayslipYears());
        return response;
    }

    //7天自动确认
    private static final Long AUTO_CONFIRM = 7 * 24 * 3600 * 1000L;

    @Override
    public ListPayslipsDetailResponse listPayslipsDetail(ListPayslipsDetailCommand cmd) {

        SalaryPayslipDetail result = salaryPayslipDetailProvider.findSalaryPayslipDetailById(cmd.getPayslipDetailId());
        if (null == result || PayslipDetailStatus.REVOKED == PayslipDetailStatus.fromCode(result.getStatus())) {
            return null;
        }
        if (NormalFlag.NO == NormalFlag.fromCode(result.getViewedFlag())) {
            result.setViewedFlag(NormalFlag.YES.getCode());
            salaryPayslipDetailProvider.updateSalaryPayslipDetail(result);
        }
        if (result.getUserId().equals(UserContext.currentUserId())) {
            PayslipDetailDTO dto = convertPayslipDetailDTO(result);
            for (int i = 0; i < dto.getPayslipContent().size(); i++) {
                SalaryPeriodEmployeeEntityDTO entityDTO = dto.getPayslipContent().get(i);
                //值为空/0 不显示给客户端
                if (entityDTO.getSalaryValue() == null || "0".equals(entityDTO.getSalaryValue()) ||
                        StringUtils.isBlank(entityDTO.getSalaryValue()) ) {
                    //app不用显示的
                    dto.getPayslipContent().remove(entityDTO);
                } else {
                    for (String invisibleKey : invisibleKeys) {
                        //当字段属于不可见的
                        if (isContain(entityDTO.getGroupEntityName(), invisibleKey)
                        		&& !isContain(entityDTO.getGroupEntityName(), "工资")) {
                            dto.getPayslipContent().remove(entityDTO);
                            i--;
                            break;
                        }
                    }
                }
            }
            dto.setPayslipContent(dto.getPayslipContent());

            dto.setConfirmTime(dto.getCreateTime() + AUTO_CONFIRM);
            return new ListPayslipsDetailResponse(dto);

        }
        return new ListPayslipsDetailResponse();
    }

    @Override
    public void confirmPayslip(ConfirmPayslipCommand cmd) {
        SalaryPayslipDetail spd = salaryPayslipDetailProvider.findSalaryPayslipDetailById(cmd.getPayslipDetailId());
        spd.setStatus(PayslipDetailStatus.CONFIRMED.getCode());
        salaryPayslipDetailProvider.updateSalaryPayslipDetail(spd);
    }

    /**
     * 自动确认.每15分钟把发放时间为7天前的都自动确认
     */
    @Scheduled(cron = "1 0/15 * * * ?")
    public void scheduledSendPushToUsers() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            salaryPayslipDetailProvider.confirmSalaryPayslipDetailBeforeDate(new Timestamp(DateHelper.currentGMTTime().getTime() - AUTO_CONFIRM));
        }
    }

}