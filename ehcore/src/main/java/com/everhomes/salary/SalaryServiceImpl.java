// @formatter:off
package com.everhomes.salary;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.organization.*;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.salary.*;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.rest.uniongroup.*;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.uniongroup.UniongroupMemberDetail;
import com.everhomes.uniongroup.UniongroupService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SalaryServiceImpl implements SalaryService {

	private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMM");
		}
	};

//	private static List<SalaryDefaultEntity> salaryDefaultEntities = new ArrayList<SalaryDefaultEntity>();
//

    private StringTemplateLoader templateLoader;

    private Configuration templateConfig;

	public SalaryServiceImpl(){

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
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private ConfigurationProvider configProvider;


	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
    private SalaryDefaultEntityProvider salaryDefaultEntityProvider;

    @Autowired 
    private SalaryEmployeeOriginValProvider salaryEmployeeOriginValProvider;

    @Autowired 
    private SalaryEmployeePeriodValProvider  salaryEmployeePeriodValProvider;
    
    @Autowired 
    private SalaryEmployeeProvider  salaryEmployeeProvider;
    
    @Autowired 
    private SalaryEntityCategoryProvider  salaryEntityCategoryProvider;
    
    @Autowired 
    private SalaryGroupEntityProvider  salaryGroupEntityProvider;
    
    @Autowired 
    private SalaryGroupProvider  salaryGroupProvider;

	@Autowired
    private PunchService punchService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
    private ImportFileService importFileService;

	@Autowired
    private UniongroupService uniongroupService;

	@Override
	public ListSalaryDefaultEntitiesResponse listSalaryDefaultEntities() {
        ListSalaryDefaultEntitiesResponse response = new ListSalaryDefaultEntitiesResponse();
        List<SalaryDefaultEntity> result = this.salaryDefaultEntityProvider.listSalaryDefaultEntity();
        response.setSalaryDefaultEntities(result.stream().map(r ->{
            SalaryDefaultEntityDTO dto = ConvertHelper.convert(r,SalaryDefaultEntityDTO.class);
            return dto;
        }).collect(Collectors.toList()));
		return response;
    }

    @Override
    public AddSalaryGroupResponse addSalaryGroup(AddSalaryGroupCommand cmd) {

        AddSalaryGroupResponse response = new AddSalaryGroupResponse();
        if (!cmd.getSalaryGroupEntity().isEmpty()) {

            Long groupId;
            if (StringUtils.isEmpty(cmd.getSalaryGroupId())) {
                //	添加批次至组织结构
                Organization org = this.organizationService.createSalaryGroupOrganization(cmd.getOwnerId(), cmd.getSalaryGroupName());
                groupId = org.getId();
            } else
                groupId = cmd.getSalaryGroupId();

            //  添加至薪酬组刘表
            response.setSalaryGroupEntity(cmd.getSalaryGroupEntity().stream().map(r -> {
                SalaryGroupEntity entity = new SalaryGroupEntity();
                if (!StringUtils.isEmpty(cmd.getOwnerType()))
                    entity.setOwnerType(cmd.getOwnerType());
                if (!StringUtils.isEmpty(cmd.getOwnerId()))
                    entity.setOwnerId(cmd.getOwnerId());
                entity.setGroupId(groupId);
                entity.setOriginEntityId(r.getOriginEntityId());
                entity.setType(r.getType());
                entity.setCategoryId(r.getCategoryId());
                entity.setCategoryName(r.getCategoryName());
                entity.setName(r.getName());
                entity.setEditableFlag(r.getEditableFlag());
                entity.setTemplateName(r.getTemplateName());
                if (!StringUtils.isEmpty(r.getNumberType()))
                    entity.setNumberType(r.getNumberType());
                if (!StringUtils.isEmpty(r.getDefaultValue()))
                    entity.setDefaultValue(r.getDefaultValue());
                if (!StringUtils.isEmpty(r.getNeedCheck()))
                    entity.setNeedCheck(r.getNeedCheck());
                entity.setDefaultOrder(r.getDefaultOrder());
                entity.setVisibleFlag(r.getVisibleFlag());
                this.salaryGroupEntityProvider.createSalaryGroupEntity(entity);
                return r;
            }).collect(Collectors.toList()));
        }
        return response;
    }

	@Override
	public AddSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd) {
        if(!StringUtils.isEmpty(cmd.getSalaryGroupId())){

            //  获取组织架构薪酬组名称
            Organization organization = this.organizationProvider.findOrganizationById(cmd.getSalaryGroupId());
            organization.setName(cmd.getSalaryGroupName());
            this.organizationProvider.updateOrganization(organization);

            AddSalaryGroupResponse response = new AddSalaryGroupResponse();
            List<SalaryGroupEntityDTO> salaryGroupEntities = new ArrayList<>();

            if (null == cmd.getSalaryGroupEntity()) {
                return response;
            }
            //  记录所有的id
            List<Long> entityIds = new ArrayList<>();
            for( SalaryGroupEntityDTO dto: cmd.getSalaryGroupEntity()) {
                entityIds.add(dto.getOriginEntityId());
                //  查找对应的字段
                SalaryGroupEntity entityDB = salaryGroupEntityProvider.findSalaryGroupEntityByGroupAndOriginId(dto.getGroupId(), dto.getOriginEntityId());
                //  没查找到的时候就创建新的字段
                if(StringUtils.isEmpty(entityDB)){
                    SalaryGroupEntity newEntity = ConvertHelper.convert(dto, SalaryGroupEntity.class);
                    newEntity.setOwnerType(cmd.getOwnerType());
                    newEntity.setOwnerId(cmd.getOwnerId());
                    newEntity.setGroupId(cmd.getSalaryGroupId());
                    salaryGroupEntityProvider.createSalaryGroupEntity(newEntity);
                }else {
                    //  查找到的时候则做修改
                    entityDB.setGroupId(dto.getGroupId());
                    entityDB.setOriginEntityId(dto.getOriginEntityId());
                    entityDB.setDefaultFlag(dto.getDefaultFlag());
                    entityDB.setType(dto.getType());
                    entityDB.setCategoryId(dto.getCategoryId());
                    entityDB.setCategoryName(dto.getCategoryName());
                    entityDB.setName(dto.getName());
                    entityDB.setEditableFlag(dto.getEditableFlag());
                    entityDB.setNumberType(dto.getNumberType());
                    entityDB.setDefaultValue(dto.getDefaultValue());
                    entityDB.setNeedCheck(dto.getNeedCheck());
                    entityDB.setDefaultOrder(dto.getDefaultOrder());
                    entityDB.setVisibleFlag(dto.getVisibleFlag());
                    entityDB.setTemplateName(dto.getTemplateName());
                    salaryGroupEntityProvider.updateSalaryGroupEntity(entityDB);
                }
                salaryGroupEntities.add(dto);
            }

            //  最后删除多余的字段
            salaryGroupEntityProvider.deleteSalaryGroupEntityByGroupIdNotInOriginIds(cmd.getSalaryGroupId(), entityIds);
            salaryEmployeeOriginValProvider.deleteSalaryEmployeeValsByGroupIdNotInOriginIds(cmd.getSalaryGroupId(), entityIds);
            response.setSalaryGroupEntity(salaryGroupEntities);
            return response;
        }
		return null;
	}

    @Override
    public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd) {
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {

            //  组织架构删除薪酬组
            Organization organization = this.organizationProvider.findOrganizationById(cmd.getSalaryGroupId());
            this.organizationProvider.deleteOrganization(organization);

            //  组织架构删除薪酬组人员关联及配置
            this.uniongroupService.deleteUniongroupConfigresByGroupId(cmd.getSalaryGroupId(),cmd.getOwnerId());
            this.uniongroupService.deleteUniongroupMemberDetailByGroupId(cmd.getSalaryGroupId(),cmd.getOwnerId());

            //  删除薪酬组定义的字段
            this.salaryGroupEntityProvider.deleteSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());

            //  删除个人设定中与薪酬组相关的字段
            this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupId(cmd.getSalaryGroupId());

            // 删除未发放的salaryGroup  employee 和vals
            List<SalaryGroup> salaryGroups = salaryGroupProvider.listUnsendSalaryGroup(cmd.getSalaryGroupId());
            if (null == salaryGroups) {
                return;
            }
            for (SalaryGroup salaryGroup : salaryGroups) {
                List<SalaryEmployee> salaryEmployees = salaryEmployeeProvider.listSalaryEmployeeByPeriodGroupId(salaryGroup.getId());
                if (null == salaryEmployees) {
                    continue;
                }
                for (SalaryEmployee employee : salaryEmployees) {
                    salaryEmployeePeriodValProvider.deletePeriodVals(employee.getId());
                    salaryEmployeeProvider.deleteSalaryEmployee(employee);
                }
                salaryGroupProvider.deleteSalaryGroup(salaryGroup);
            }
        }
    }

    @Override
    public void copySalaryGroup(CopySalaryGroupCommand cmd) {
        List<SalaryGroupEntity> origin = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
        AddSalaryGroupCommand addCommand = new AddSalaryGroupCommand();
        addCommand.setSalaryGroupEntity(origin.stream().map(r -> {
            SalaryGroupEntityDTO dto = ConvertHelper.convert(r, SalaryGroupEntityDTO.class);
            return dto;
        }).collect(Collectors.toList()));
        addCommand.setOwnerId(origin.get(0).getOwnerId());
        addCommand.setOwnerType(origin.get(0).getOwnerType());

        //  判断名字
        String salaryGroupName = cmd.getSalaryGroupName();
        int i = 1;
        boolean flag = true;
        //  从数据库获取已经存在的名字
        Organization organization = this.organizationProvider.findOrganizationById(cmd.getSalaryGroupId());
        while (flag) {
            salaryGroupName = salaryGroupName + " (" + i + ")";
            if (!salaryGroupName.equals(organization.getName()))
                flag = false;
            i++;
        }
        addCommand.setSalaryGroupName(salaryGroupName);
        this.addSalaryGroup(addCommand);
    }

    @Override
    public GetSalaryGroupResponse getSalaryGroup(GetSalaryGroupCommand cmd) {
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {

            Organization organization = this.organizationProvider.findOrganizationById(cmd.getSalaryGroupId());

            //   从组织架构获取名称
            GetSalaryGroupResponse response = new GetSalaryGroupResponse();
            response.setSalaryGroupName(organization.getName());

            //   获取批次对应的字段
            List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
            response.setSalaryGroupEntity(salaryGroupEntities.stream().map(r -> {
                SalaryGroupEntityDTO dto = ConvertHelper.convert(r, SalaryGroupEntityDTO.class);
                return dto;
            }).collect(Collectors.toList()));
            return response;
        }
        return null;
    }

    @Override
    public ListSalaryGroupResponse listSalaryGroup(ListSalaryGroupCommand cmd) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        ListSalaryGroupResponse response = new ListSalaryGroupResponse();
        List<SalaryGroupListDTO> results = new ArrayList<>();

        //  获取所有批次
        List<Organization> organizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.SALARYGROUP.getCode(), cmd.getOwnerId());

        //  存储所有薪酬组 id
        List<Long> salaryGroupIds = organizations.stream().map(r -> {
            Long salaryGroupId = r.getId();
            return salaryGroupId;
        }).collect(Collectors.toList());

        //  获取公司总人数
        Integer totalCount = this.organizationProvider.countOrganizationMemberDetailsByOrgId(namespaceId, cmd.getOwnerId());

        //  关联人数一次性获取
        Integer relevantCount = 0;
        List<Object[]> relevantCounts = this.uniongroupService.listUniongroupMemberCount(namespaceId, salaryGroupIds, cmd.getOwnerId());

        //  拼接关联人数
        for (int i = 0; i < organizations.size(); i++) {

            SalaryGroupListDTO dto = new SalaryGroupListDTO();
            dto.setSalaryGroupId(organizations.get(i).getId());
            dto.setSalaryGroupName(organizations.get(i).getName());
            ListUniongroupMemberDetailsCommand command = new ListUniongroupMemberDetailsCommand();
            command.setGroupId(organizations.get(i).getId());
            command.setOwnerId(cmd.getOwnerId());
            command.setOwnerType(cmd.getOwnerType());
//            List<UniongroupMemberDetailsDTO> lists = this.uniongroupService.listUniongroupMemberDetailsByGroupId(command);
            if (!StringUtils.isEmpty(relevantCounts)) {
                for (int j = 0; j < relevantCounts.size(); j++) {
                    if (relevantCounts.get(j)[0].equals(dto.getSalaryGroupId())) {
                        dto.setRelevantNum((Integer) relevantCounts.get(j)[1]);
                        relevantCount += (Integer) relevantCounts.get(j)[1];
                        break;
                    }
                }
            }
            results.add(dto);
        }

        response.setSalaryGroupList(results);
        response.setTotalCount(totalCount);
        response.setRelevantCount(relevantCount);
        response.setIrrelevantCount(totalCount - relevantCount);

        return response;
    }


    @Override
    public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd) {

	    //  1.将前端信息传递给组织架构的接口获取相关信息
        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setOrganizationId(cmd.getOwnerId());

        //  2.查询所有人员
        ListOrganizationMemberCommandResponse results = this.organizationService.listOrganizationPersonnels(command,false);

        //  3.查询所有批次
        List<Organization> organizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.SALARYGROUP.getCode(), cmd.getOwnerId());

        List<Long> uniongroupDetailIds = new ArrayList<>();

        if(cmd.getSalaryGroupId() != null){
            //将前端信息传递给组织架构的接口获取相关信息
            ListUniongroupMemberDetailsWithConditionCommand uniongroup_command = new ListUniongroupMemberDetailsWithConditionCommand();
            if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {
                uniongroup_command.setGroupId(cmd.getSalaryGroupId());
            }
            if (!StringUtils.isEmpty(cmd.getKeywords()))
                uniongroup_command.setKeywords(cmd.getKeywords());
            if(!StringUtils.isEmpty(cmd.getDepartmentId()))
                uniongroup_command.setDepartmentId(cmd.getDepartmentId());
            uniongroup_command.setGroupType(UniongroupType.SALARYGROUP.getCode());
            uniongroup_command.setOwnerId(cmd.getOwnerId());
            uniongroup_command.setPageAnchor(Long.valueOf("0"));
            uniongroup_command.setPageSize(cmd.getPageSize());

            //  查询关联的人员
            List<UniongroupMemberDetail> uniongroupMemberDetails = this.uniongroupService.listUniongroupMemberDetailsWithCondition(uniongroup_command);

            // 获取关联人员detailId的集合
            uniongroupDetailIds.addAll(uniongroupMemberDetails.stream().map(r ->{return r.getDetailId();}).collect(Collectors.toList()));
        }


        //过滤
        ListSalaryEmployeesResponse response = new ListSalaryEmployeesResponse();

        List<OrganizationMemberDTO> dtos = new ArrayList<>();
        if (!StringUtils.isEmpty(results)) {
            if (uniongroupDetailIds.size() > 0) {
                dtos = results.getMembers().stream().filter(r -> {
                    return uniongroupDetailIds.contains(r.getDetailId());
                }).collect(Collectors.toList());
            } else {
                dtos = results.getMembers();
            }

            response.setSalaryEmployeeDTO(dtos.stream().map(r -> {
                SalaryEmployeeDTO dto = new SalaryEmployeeDTO();
                dto.setUserId(r.getTargetId());
                dto.setDetailId(r.getDetailId());
                dto.setContactName(r.getContactName());
                dto.setSalaryGroupId(r.getGroupId());
                //  拼接薪酬组名称
                if (!StringUtils.isEmpty(organizations)) {
                    organizations.forEach(s -> {
                        if (s.getId().equals(r.getGroupId()))
                            dto.setSalaryGroupName(s.getName());
                    });
                }
                return dto;
            }).collect(Collectors.toList()));
        }
        return response;
    }

/*    private List<UniongroupMemberDetail> listEmployeesFromOrganization(ListSalaryEmployeesCommand cmd){
        //  1.将前端信息传递给组织架构的接口获取相关信息，从而查询所有人员
        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setOrganizationId(cmd.getOwnerId());
        if (!StringUtils.isEmpty(cmd.getDepartmentId()))
            command.setOrganizationId(cmd.getDepartmentId());
        if (!StringUtils.isEmpty(cmd.getKeywords()))
            command.setKeywords(cmd.getKeywords());
        command.setPageSize(20);
        ListOrganizationMemberCommandResponse response = this.organizationService.listOrganizationPersonnels(command, false);

        List<UniongroupMemberDetail> results = new ArrayList<>();
        response.getMembers().forEach(r ->{
            UniongroupMemberDetail result = new UniongroupMemberDetail();
            result.setId(r.getId());
            result.setGroupType(r.getGroupType());
            result.setGroupId(r.getGroupId());

        });

    }*/

    /*private ListSalaryEmployeesResponse listWithoutSalaryGroupId(ListSalaryEmployeesCommand cmd) {

        //  1.将前端信息传递给组织架构的接口获取相关信息，从而查询所有人员
        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setOrganizationId(cmd.getOwnerId());
        if (!StringUtils.isEmpty(cmd.getDepartmentId()))
            command.setOrganizationId(cmd.getDepartmentId());
        if (!StringUtils.isEmpty(cmd.getKeywords()))
            command.setKeywords(cmd.getKeywords());
        command.setPageSize(20);
        ListOrganizationMemberCommandResponse results = this.organizationService.listOrganizationPersonnels(command, false);

        //  2.查询所有批次
        List<Organization> organizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.SALARYGROUP.getCode(), cmd.getOwnerId());
        //  3.查询所有员工工资明细情况
        List<Object[]> wages = this.salaryEmployeeOriginValProvider.listSalaryEmployeeWagesDetails();

        //  4.拼接字段返回结果
        ListSalaryEmployeesResponse response = new ListSalaryEmployeesResponse();
        List<SalaryEmployeeDTO> dtos = new ArrayList<>();

        if (!StringUtils.isEmpty(results)) {
            for (int i = 0; i < results.getMembers().size(); i++) {
                SalaryEmployeeDTO dto = new SalaryEmployeeDTO();
                String department = "";
                String jobPosition = "";
                dto.setUserId(results.getMembers().get(i).getTargetId());
                dto.setDetailId(results.getMembers().get(i).getDetailId());
                dto.setContactName(results.getMembers().get(i).getContactName());
                dto.setSalaryGroupId(results.getMembers().get(i).getGroupId());
                //  拼接部门
                if (!StringUtils.isEmpty(results.getMembers().get(i).getDepartments())) {
                    for (OrganizationDTO k : results.getMembers().get(i).getDepartments())) {
                        department += (k.get+ ",");
                    }
                    department = department.substring(0, department.length() - 1);

                }
                //  拼接岗位
                if (!StringUtils.isEmpty(results.get(i).getJobPosition())) {
                    for (Long k : results.get(i).getJobPosition().keySet()) {
                        jobPosition += (results.get(i).getJobPosition().get(k) + ",");
                    }
                    jobPosition = jobPosition.substring(0, jobPosition.length() - 1);
                }

                //  拼接薪酬组名称
                if (!StringUtils.isEmpty(organizations)) {
                    for (int j = 0; j < organizations.size(); j++) {
                        if (organizations.get(j).getId().equals(results.getMembers().get(i).getGroupId())) {
                            dto.setSalaryGroupName(organizations.get(j).getName());
                            break;
                        }
                    }
                }
                //  拼接工资明细情况
                if (!StringUtils.isEmpty(wages)) {
                    for (int j = 0; j < wages.size(); j++) {
                        if (wages.get(j)[0].equals(results.getMembers().get(i).getDetailId())) {
                            if (org.apache.commons.lang.StringUtils.isNumeric((String) wages.get(j)[1])) {
                                dto.setIsConfirmed(SalaryEmployeeConfirmedType.CONFIRMED.getCode());
                                break;
                            }
                        }
                    }
                }
                //  拼接是否异常员工
                if (StringUtils.isEmpty(dto.getSalaryGroupName())
                        || dto.getIsConfirmed().equals(SalaryEmployeeConfirmedType.CONFIRMED.getCode()))
                    dto.setIsNormal(SalaryEmployeeNormalType.ABNORMAL.getCode());
                if (cmd.getIsException().equals(SalaryEmployeeNormalType.ABNORMAL.getCode()) && dto.getIsNormal().equals(SalaryEmployeeNormalType.NORMAL.getCode()))
                    break;
                dtos.add(dto);
            }
        }
        response.setSalaryEmployeeDTO(dtos);
        return response;
    }

    private ListSalaryEmployeesResponse listWithSalaryGroupId(ListSalaryEmployeesCommand cmd) {

        //  1.将前端信息传递给组织架构的接口获取相关信息
        ListUniongroupMemberDetailsWithConditionCommand command = new ListUniongroupMemberDetailsWithConditionCommand();
        command.setOwnerId(cmd.getOwnerId());
        if (!StringUtils.isEmpty(cmd.getDepartmentId()))
            command.setDepartmentId(cmd.getDepartmentId());
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {
            command.setGroupId(cmd.getSalaryGroupId());
            command.setGroupType(UniongroupType.SALARYGROUP.getCode());
        }
        if (!StringUtils.isEmpty(cmd.getKeywords()))
            command.setKeywords(cmd.getKeywords());
        command.setPageAnchor(Long.valueOf("0"));
        command.setPageSize(20);

        //  2.查询所有人员
        List<UniongroupMemberDetail> results = this.uniongroupService.listUniongroupMemberDetailsWithCondition(command);
        //  3.查询所有批次
        List<Organization> organizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.SALARYGROUP.getCode(), cmd.getOwnerId());
        //  3.查询所有员工工资明细情况
        List<Object[]> wages = this.salaryEmployeeOriginValProvider.listSalaryEmployeeWagesDetails();

        //  4.拼接字段返回结果
        ListSalaryEmployeesResponse response = new ListSalaryEmployeesResponse();
        List<SalaryEmployeeDTO> dtos = new ArrayList<>();

        if (!StringUtils.isEmpty(results)) {
            for (int i = 0; i < results.size(); i++) {
                SalaryEmployeeDTO dto = new SalaryEmployeeDTO();
                String department = "";
                String jobPosition = "";
                dto.setUserId(results.get(i).getTargetId());
                dto.setDetailId(results.get(i).getDetailId());
                dto.setContactName(results.get(i).getContactName());
                dto.setSalaryGroupId(results.get(i).getGroupId());
                if (!StringUtils.isEmpty(results.get(i).getEmployeeNo()))
                    dto.setEmployeeNo(results.get(i).getEmployeeNo());

                //  拼接部门
                if (!StringUtils.isEmpty(results.get(i).getDepartment())) {
                    for (Long k : results.get(i).getDepartment().keySet()) {
                        department += (results.get(i).getDepartment().get(k) + ",");
                    }
                    department = department.substring(0, department.length() - 1);

                }
                //  拼接岗位
                if (!StringUtils.isEmpty(results.get(i).getJobPosition())) {
                    for (Long k : results.get(i).getJobPosition().keySet()) {
                        jobPosition += (results.get(i).getJobPosition().get(k) + ",");
                    }
                    jobPosition = jobPosition.substring(0, jobPosition.length() - 1);
                }
                dto.setDepartment(department);
                dto.setJobPosition(jobPosition);

                //  拼接薪酬组名称
                for (int j = 0; j < organizations.size(); j++) {
                    if (organizations.get(j).getId().equals(results.get(i).getGroupId())) {
                        dto.setSalaryGroupName(organizations.get(j).getName());
                        break;
                    }
                }

                //  拼接工资明细情况
                if (!StringUtils.isEmpty(wages)) {
                    for (int j = 0; j < wages.size(); j++) {
                        if (wages.get(j)[0].equals(results.get(i).getDetailId())) {
                            if (org.apache.commons.lang.StringUtils.isNumeric((String) wages.get(j)[1])) {
                                dto.setIsConfirmed(SalaryEmployeeConfirmedType.CONFIRMED.getCode());
                                break;
                            }
                        }
                    }
                }

                //  拼接是否异常员工
                if (StringUtils.isEmpty(dto.getSalaryGroupName())
                        || dto.getIsConfirmed().equals(SalaryEmployeeConfirmedType.CONFIRMED.getCode()))
                    dto.setIsNormal(SalaryEmployeeNormalType.ABNORMAL.getCode());
                if (cmd.getIsException().equals(SalaryEmployeeNormalType.ABNORMAL.getCode()) && dto.getIsNormal().equals(SalaryEmployeeNormalType.NORMAL.getCode()))
                    break;
                dtos.add(dto);
            }

        }
        response.setSalaryEmployeeDTO(dtos);
        return response;
    }*/

    @Override
    public List<SalaryEmployeeOriginValDTO> getSalaryEmployees(GetSalaryEmployeesCommand cmd) {

        List<SalaryEmployeeOriginValDTO> results = new ArrayList<>();

        //  获取对应批次的项目字段
        List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
        //  获取个人的项目字段
        List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(cmd.getUserId(), cmd.getOwnerType(), cmd.getOwnerId());

        //  若没有关联批次则直接返回空
        if (!salaryGroupEntities.isEmpty()) {
            salaryGroupEntities.stream().forEach(r -> {
                SalaryEmployeeOriginValDTO dto = new SalaryEmployeeOriginValDTO();
                dto.setSalaryGroupId(r.getGroupId());
                dto.setUserId(cmd.getUserId());
                dto.setUserDetailId(cmd.getDetailId());
                dto.setType(r.getType());
                if (!StringUtils.isEmpty(r.getEditableFlag()))
                    dto.setEditableFlag(r.getEditableFlag());
                if (!StringUtils.isEmpty(r.getNumberType()))
                    dto.setNumberType(r.getNumberType());
                dto.setGroupEntityId(r.getId());
                dto.setOriginEntityId(r.getOriginEntityId());
                dto.setGroupEntityName(r.getName());
                dto.setSalaryValue(r.getDefaultValue());
                //  为对应字段赋值
                if (!salaryEmployeeOriginVals.isEmpty()) {
                    for (int i = 0; i < salaryEmployeeOriginVals.size(); i++) {
                        if (r.getName().equals(salaryEmployeeOriginVals.get(i).getGroupEntityName())) {
                            dto.setSalaryValue(salaryEmployeeOriginVals.get(i).getSalaryValue());
                            dto.setId(salaryEmployeeOriginVals.get(i).getId());
                            break;
                        }
                    }
                }
                results.add(dto);
            });
            return results;
        } else
            return null;
    }

    //  变更员工薪酬组
    @Override
    public void updateSalaryEmployeesGroup(UpdateSalaryEmployeesGroupCommand cmd){
	    //  1.更新该员工在组织架构的关联及configure
        AddToOrganizationSalaryGroupCommand command = new AddToOrganizationSalaryGroupCommand();
        command.setOwnerId(cmd.getOwnerId());
        command.setOwnerType(cmd.getOwnerType());
        command.setSalaryGroupId(cmd.getSalaryGroupId());
        List<Long> detailIds = new ArrayList<>();
        detailIds.add(cmd.getDetailId());
        command.setDetailIds(detailIds);
        this.addToOrganizationSalaryGroup(command);

        // 2.删除原有的薪酬设定
        List<SalaryEmployeeOriginVal> originVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(cmd.getUserId(),cmd.getOwnerType(),cmd.getOwnerId());
        if(!StringUtils.isEmpty(originVals) && originVals.size() > 0) {
            this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupIdUserId(originVals.get(0).getGroupId(),
                    originVals.get(0).getUserId(), cmd.getOwnerType(), cmd.getOwnerId());
        }
    }

    @Override
    public void updateSalaryEmployees(UpdateSalaryEmployeesCommand cmd) {

        User user = UserContext.current().getUser();
        if(!cmd.getEmployeeOriginVal().isEmpty()){

            //  添加到组织架构的薪酬组中，没有增加有则覆盖
            AddToOrganizationSalaryGroupCommand command = new AddToOrganizationSalaryGroupCommand();
            command.setOwnerId(cmd.getOwnerId());
            command.setOwnerType(cmd.getOwnerType());
            command.setSalaryGroupId(cmd.getSalaryGroupId());
            List<Long> detailIds = new ArrayList<>();
            detailIds.add(cmd.getUserDetailId());
            command.setDetailIds(detailIds);
            this.addToOrganizationSalaryGroup(command);

            //  添加到薪酬组的个人设定中
            List<SalaryEmployeeOriginVal> originVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(cmd.getUserId(),cmd.getOwnerType(),cmd.getOwnerId());
            if(originVals.isEmpty()){
                cmd.getEmployeeOriginVal().stream().forEach(r -> {
                    this.createSalaryEmployeeOriginVal(r, cmd.getOwnerType(),cmd.getOwnerId(),
                            cmd.getSalaryGroupId(),cmd.getUserId(),cmd.getUserDetailId());
                });
            }else{
                this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupIdUserId(cmd.getSalaryGroupId(),cmd.getUserId(),cmd.getOwnerType(),cmd.getOwnerId());
                cmd.getEmployeeOriginVal().stream().forEach(s ->{
                    this.createSalaryEmployeeOriginVal(s, cmd.getOwnerType(),cmd.getOwnerId(),
                            cmd.getSalaryGroupId(),cmd.getUserId(),cmd.getUserDetailId());
                });
            }
        }
    }

    private void createSalaryEmployeeOriginVal(
            SalaryEmployeeOriginValDTO dto, String ownerType, Long ownerId,
            Long groupId, Long userId, Long userDetailId) {
        SalaryEmployeeOriginVal originVal = new SalaryEmployeeOriginVal();
        originVal.setOwnerType(ownerType);
        originVal.setOwnerId(ownerId);
        originVal.setGroupId(groupId);
        originVal.setUserId(userId);
        originVal.setUserDetailId(userDetailId);
        originVal.setGroupEntityId(dto.getGroupEntityId());
        originVal.setGroupEntityName(dto.getGroupEntityName());
        originVal.setOriginEntityId(dto.getOriginEntityId());
        originVal.setSalaryValue(dto.getSalaryValue());
        this.salaryEmployeeOriginValProvider.createSalaryEmployeeOriginVal(originVal);
    }

    @Override
    public void addToOrganizationSalaryGroup(AddToOrganizationSalaryGroupCommand cmd) {

        //  通过组织架构的接口来实现人员的添加
        SaveUniongroupConfiguresCommand command = new SaveUniongroupConfiguresCommand();
        command.setGroupId(cmd.getSalaryGroupId());
        command.setGroupType(UniongroupType.SALARYGROUP.getCode());
        command.setEnterpriseId(cmd.getOwnerId());
        List<UniongroupTarget> targets = new ArrayList<>();

        //  1.将部门 id 传入targets
        if(!StringUtils.isEmpty(cmd.getDepartmentIds())){
            cmd.getDepartmentIds().forEach(r ->{
                UniongroupTarget target = new UniongroupTarget();
                target.setId(r);
                target.setType(UniongroupTargetType.ORGANIZATION.getCode());
                targets.add(target);
            });
        }

        //  2.将选择的人员的 detailId 传入 targets
        if(!StringUtils.isEmpty(cmd.getDetailIds())){
            cmd.getDetailIds().forEach(r ->{
                UniongroupTarget target = new UniongroupTarget();
                target.setId(r);
                target.setType(UniongroupTargetType.MEMBERDETAIL.getCode());
                targets.add(target);
            });
        }
        command.setTargets(targets);

        // 3.将人员添加至组织架构的薪酬组
        this.uniongroupService.saveUniongroupConfigures(command);
    }

    @Override
    public void exportSalaryGroup(ExportSalaryGroupCommand cmd, HttpServletResponse httpServletResponse) {
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {

            //  根据批次 id 查找批次具体内容
            List<SalaryGroupEntity> results = this.salaryGroupEntityProvider.listSalaryGroupWithExportRegular(cmd.getSalaryGroupId());
            Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());

            ByteArrayOutputStream out = null;
            XSSFWorkbook workbook = this.creatXSSFSalaryGroupFile(results,organization.getName());
            createOutPutSteam(workbook, out, httpServletResponse);
        }

    }

    //  创建 excel
    private XSSFWorkbook creatXSSFSalaryGroupFile( List<SalaryGroupEntity> results, String name){

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName ="Module";
        XSSFSheet sheet = wb.createSheet(sheetName);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 12));
        XSSFCellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        //  创建标题
        XSSFRow rowTitle = sheet.createRow(rowNum++);
        rowTitle.createCell(0).setCellValue(name);
        rowTitle.setRowStyle(titleStyle);

        XSSFRow row = sheet.createRow(rowNum++);
        row.setRowStyle(style);

        row.createCell(0).setCellValue("姓名(必填项)");
        row.createCell(1).setCellValue("手机号(必填项)");
        //  创建模板标题
        for (int i = 0; i < results.size(); i++) {
            row.createCell(i+2).setCellValue(results.get(i).getName());
        }
        return wb;
    }

	@Override
	public ImportFileTaskDTO importSalaryGroup(
	        MultipartFile mfile, Long userId, ImportSalaryGroupCommand cmd) {

        ImportFileTask task = new ImportFileTask();
        try {

            // 解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if(resultList.isEmpty()){
                LOGGER.error("File content is empty");
                throw RuntimeErrorException.errorWith(SalaryServiceErrorCode.SCOPE, SalaryServiceErrorCode.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            task.setOwnerType(cmd.getOwnerType());
            task.setOwnerId(cmd.getOwnerId());
            task.setType(ImportFileTaskType.SALARY_GROUP.getCode());
            task.setCreatorUid(userId);

            //  提前获取批次的字段便于后面方法的调用
            List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupWithExportRegular(cmd.getSalaryGroupId());
            task = this.importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportSalaryEmployeeOriginValDTO> datas = handleImportSalaryFiles(resultList,cmd.getSalaryGroupId(),salaryGroupEntities);
                    if (datas.size() > 0) {
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }
                    List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> results = importSalaryFiles(datas,
                            salaryGroupEntities, userId, cmd, UserContext.getCurrentNamespaceId());
                    response.setTotalCount((long) datas.size());
                    response.setFailCount((long) results.size());
                    response.setLogs(results);
                    return response;

                }
            },task);
        }catch (Exception e){

        }
        return null;
	}

	//  解析 excel 表格
	private List<ImportSalaryEmployeeOriginValDTO> handleImportSalaryFiles(
	        List list, Long groupId, List<SalaryGroupEntity> salaryGroupEntities){

        List<ImportSalaryEmployeeOriginValDTO> datas = new ArrayList<>();

        /*salaryGroupEntities.add(0,new SalaryGroupEntity("姓名"));
       salaryGroupEntities.add(1,new SalaryGroupEntity("手机号"));*/

        for(int i=1; i<list.size(); i++){
            ImportSalaryEmployeeOriginValDTO data = new ImportSalaryEmployeeOriginValDTO();
            List<String> vals = new ArrayList<>();

            for(int j=0; j<salaryGroupEntities.size(); j++){
                RowResult r = (RowResult) list.get(i);
                String val = r.getCells().get(GetExcelLetter(j+1));
                vals.add(val);
            }
            data.setSalaryEmployeeVal(vals);
            datas.add(data);
        }
        return datas;
    }

    //  数字转换(1-A,2-B...)
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

    private List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> importSalaryFiles(
            List<ImportSalaryEmployeeOriginValDTO> datas, List<SalaryGroupEntity> salaryGroupEntities,
            Long userId, ImportSalaryGroupCommand cmd, Integer namespaceId) {

	    ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> log = new ImportFileResultLog<>(SalaryServiceErrorCode.SCOPE);
        List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> errorDataLogs = new ArrayList<>();

        //  读取薪酬组列表中所有的手机号以及对应的userId
        List<Object[]> users = this.uniongroupService.listUniongroupMemberDetailsInfo(
                namespaceId,cmd.getSalaryGroupId(),cmd.getOwnerId());

        //  校验数据，成功则导入
        for ( ImportSalaryEmployeeOriginValDTO data : datas){
	        log = this.checkSalaryGroup(data,users);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }

            this.saveSalaryGroup(data,salaryGroupEntities,cmd.getSalaryGroupId(),cmd.getOrganizationId(),cmd.getOwnerType(),cmd.getOwnerId());
        }
        return errorDataLogs;

    }

    private ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> checkSalaryGroup(
            ImportSalaryEmployeeOriginValDTO data, List<Object[]> users){

        ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> log = new ImportFileResultLog<>(SalaryServiceErrorCode.SCOPE);

        //  get(0)是姓名
        if(StringUtils.isEmpty(data.getSalaryEmployeeVal().get(0))){
            LOGGER.warn("Organization member contactName is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactName is null");
            log.setCode(SalaryServiceErrorCode.ERROR_CONTACTNAME_ISNULL);
            return log;
        }

        //  get(1)是手机号
        if(StringUtils.isEmpty(data.getSalaryEmployeeVal().get(1))){
            LOGGER.warn("Organization member contactToken is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactToken is null");
            log.setCode(SalaryServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL);
            return log;
        }else {
            //  直接查与薪酬组相关的手机号列表中是否存在该号码
            //  存在则将读取的 userId 与 detailId 存入data列表中，便于之后薪酬设定的操作
            //  不存在则返回错误
            for(int i=0; i<users.size(); i++) {
                if (data.getSalaryEmployeeVal().get(1).equals(users.get(i)[0])) {
                    //  取 userId(targetId) 取代姓名
                    data.getSalaryEmployeeVal().set(0, String.valueOf(users.get(i)[1]));
                    //  取 detailId 取代手机号
                    data.getSalaryEmployeeVal().set(1,String.valueOf(users.get(i)[2]));
                    return null;
                }
            }
            //  未比对成功则失败
            LOGGER.warn("Organization member contactToken is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactToken is null");
            log.setCode(SalaryServiceErrorCode.ERROR_CONTACTTOKEN_ISNULL);
            return log;
        }
    }

    private void saveSalaryGroup(ImportSalaryEmployeeOriginValDTO data, List<SalaryGroupEntity> salaryGroupEntities,
                                 Long groupId, Long organizationId, String ownerType, Long ownerId) {
        UpdateSalaryEmployeesCommand command = new UpdateSalaryEmployeesCommand();
        command.setOwnerId(ownerId);
        command.setOwnerType(ownerType);
        List<SalaryEmployeeOriginValDTO> employeeOriginVals = new ArrayList<>(0);
        for (int i = 0; i < salaryGroupEntities.size(); i++) {
            SalaryEmployeeOriginValDTO dto = new SalaryEmployeeOriginValDTO();
            dto.setSalaryGroupId(salaryGroupEntities.get(i).getGroupId());
            dto.setUserId(Long.valueOf(data.getSalaryEmployeeVal().get(0)));
            dto.setUserDetailId(Long.valueOf(data.getSalaryEmployeeVal().get(1)));
            dto.setGroupEntityId(salaryGroupEntities.get(i).getCategoryId());
            dto.setGroupEntityName(salaryGroupEntities.get(i).getCategoryName());
            dto.setOriginEntityId(salaryGroupEntities.get(i).getOriginEntityId());
            dto.setSalaryValue(data.getSalaryEmployeeVal().get(i + 2));
            employeeOriginVals.add(dto);
        }
        command.setEmployeeOriginVal(employeeOriginVals);
    }

	@Override
	public void exportPeriodSalary(ExportPeriodSalaryCommand cmd) {
	

	}

	@Override
	public void importPeriodSalary(ImportPeriodSalaryCommand cmd) {
	

	}

	@Override
	public GetAbnormalEmployeeNumberResponse getAbnormalEmployeeNumber(GetAbnormalEmployeeNumberCommand cmd) {
		Integer abnormalNumber = 0;
		//查询异常员工人数
		//异常员工判断1:未关联批次
		//TODO: 组织架构提供未关联批次的
        Organization org = organizationProvider.findOrganizationById(cmd.getOwnerId());
        //  获取公司总人数
        Integer unLinkNumber = uniongroupService.countUnionGroupMemberDetailsByOrgId(org.getNamespaceId(),org.getId());
		abnormalNumber += unLinkNumber;
		//判断2:关联了批次,但是实发工资为"-"
		//查询eh_salary_employee_period_vals 本期 的 实发工资(entity_id=98)数据为null的记录数
		Integer nullSalaryNumber = salaryEmployeePeriodValProvider.countSalaryEmployeePeriodsByPeriodAndEntity(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getPeriod(), SalaryConstants.ENTITY_ID_SHIFA);
		abnormalNumber += nullSalaryNumber;
		return new GetAbnormalEmployeeNumberResponse(abnormalNumber);
	}

	@Override
	public ListPeriodSalaryResponse listPeriodSalary(ListPeriodSalaryCommand cmd) {
		if(null == cmd.getStatus()){
			cmd.setStatus(new ArrayList<>());
			cmd.getStatus().add(SalaryGroupStatus.CHECKED.getCode());
			cmd.getStatus().add(SalaryGroupStatus.SENDED.getCode());
			cmd.getStatus().add(SalaryGroupStatus.UNCHECK.getCode());
			cmd.getStatus().add(SalaryGroupStatus.WAIT_FOR_SEND.getCode());
		}
		ListPeriodSalaryResponse response = new ListPeriodSalaryResponse();
        List<SalaryGroup> result = this.salaryGroupProvider.listSalaryGroup(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getPeriod(),cmd.getStatus());
		if(null == result )
			return response;
        response.setSalaryPeriodGroups(result.stream().map(r ->{
        	SalaryPeriodGroupDTO dto = processSalaryPeriodGroupDTO(r);
            return dto;
        }).collect(Collectors.toList()));
		return response;
	}

	private SalaryPeriodGroupDTO processSalaryPeriodGroupDTO(SalaryGroup r) {
		SalaryPeriodGroupDTO dto = ConvertHelper.convert(r, SalaryPeriodGroupDTO.class); 
		return dto;
	}

	@Override
	public ListPeriodSalaryEmployeesResponse listPeriodSalaryEmployees(ListPeriodSalaryEmployeesCommand cmd) {
        ListPeriodSalaryEmployeesResponse response = new ListPeriodSalaryEmployeesResponse();
        //1.查entities
        SalaryGroup periodGroup = salaryGroupProvider.findSalaryGroupById(cmd.getSalaryPeriodGroupId());
        List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(periodGroup.getOrganizationGroupId());
        response.setSalaryGroupEntities(groupEntities.stream().map(r -> {
            SalaryGroupEntityDTO dto = ConvertHelper.convert(r, SalaryGroupEntityDTO.class);
            return dto;
        }).collect(Collectors.toList()));
        //2.查人员 periodGroupId 可以确定:公司,薪酬组和期数
        Organization org = organizationProvider.findOrganizationById(punchService.getTopEnterpriseId(cmd.getOwnerId()));
        List<Long> userIds = punchService.listDptUserIds(org, cmd.getOrganizationId(), cmd.getKeyWords(), NormalFlag.YES.getCode());

        List<SalaryEmployee> result = salaryEmployeeProvider.listSalaryEmployees(cmd.getSalaryPeriodGroupId(),userIds,cmd.getCheckFlag());
		if(null == result )
			return response;
        Integer uncheckCount = salaryEmployeeProvider.countSalaryEmployeesByStatus(cmd.getSalaryPeriodGroupId(),userIds,SalaryGroupStatus.UNCHECK.getCode());
        Integer toltalCount = salaryEmployeeProvider.countSalaryEmployeesByStatus(cmd.getSalaryPeriodGroupId(),userIds,null);
        response.setCheckedCount(toltalCount - uncheckCount);
        response.setUncheckCount(uncheckCount);
        response.setSalaryPeriodEmployees(result.stream().map(r ->{
        	SalaryPeriodEmployeeDTO dto = processSalaryPeriodEmployeeDTO(r);
            return dto;
        }).collect(Collectors.toList()));
		return response; 
	}

	private SalaryPeriodEmployeeDTO processSalaryPeriodEmployeeDTO(SalaryEmployee r) {
		SalaryPeriodEmployeeDTO dto = ConvertHelper.convert(r, SalaryPeriodEmployeeDTO.class);
		//岗位 
        // TODO: 2017/7/6 这里和荣男的实现重叠了,重构一下
        SalaryEmployeeDTO detailDTO = getPersonnelInfoByUserIdForSalary(r.getUserId());
        dto.setJobPositions(detailDTO.getJobPosition());
        //批次名
		Organization org = organizationProvider.findOrganizationById(r.getOrganizationGroupId());
		dto.setSalaryGroupId(r.getOrganizationGroupId());
		if(null != org)
			dto.setSalaryGroupName(org.getName());
		//3.查人员的vals
		List<SalaryEmployeePeriodVal> result = salaryEmployeePeriodValProvider.listSalaryEmployeePeriodVals(r.getId());
		if(null == result)
			return null;
		dto.setPeriodEmployeeEntities(result.stream().map(r2 ->{
			SalaryPeriodEmployeeEntityDTO dto2 = processSalaryPeriodEmployeeEntityDTO(r2);
//			dto2.setIsFormula(NormalFlag.NO.getCode());
			if (r2.getOriginEntityId().equals(SalaryConstants.ENTITY_ID_GONGHAO)) {
				dto.setEmployeeNo(r2.getSalaryValue());
			}else if (r2.getOriginEntityId().equals(SalaryConstants.ENTITY_ID_NAME)) {
				dto.setContactName(r2.getSalaryValue());
			}else if (r2.getOriginEntityId().equals(SalaryConstants.ENTITY_ID_BUMEN)) {
				dto.setDepartments(r2.getSalaryValue());
			}else if (r2.getOriginEntityId().equals(SalaryConstants.ENTITY_ID_SHIFA)) {
				dto.setPaidMoney(new BigDecimal(r2.getSalaryValue()));
			}

			return dto2;
        }).collect(Collectors.toList()));
		return dto;
	}

	private SalaryPeriodEmployeeEntityDTO processSalaryPeriodEmployeeEntityDTO(
			SalaryEmployeePeriodVal r) {
		SalaryPeriodEmployeeEntityDTO dto = ConvertHelper.convert(r, SalaryPeriodEmployeeEntityDTO.class);
        SalaryGroupEntity entity = salaryGroupEntityProvider.findSalaryGroupEntityById(r.getGroupEntityId());
        if (null == entity) {
            LOGGER.error("group entity is null nameId is:"+ r.getGroupEntityName()+r.getGroupEntityId());
            return dto;
        }
//        if (null != entity.getNumberType() && entity.getNumberType().equals(NormalFlag.YES.getCode())) {
//            dto.setSalaryValue(entity.getDefaultValue());
//            dto.setIsFormula(NormalFlag.YES.getCode());
//        }
        dto.setEntityType(entity.getType());
        dto.setOriginEntityId(entity.getOriginEntityId());
        dto.setDefaultOrder(entity.getDefaultOrder());
        dto.setSalaryValue(r.getSalaryValue());
        dto.setEditableFlag(entity.getEditableFlag());
        dto.setNeedCheck(entity.getNeedCheck());
        dto.setNumberType(entity.getNumberType());
        dto.setVisibleFlag(entity.getVisibleFlag());
        return dto;
	}

	@Override
	public void updatePeriodSalaryEmployee(UpdatePeriodSalaryEmployeeCommand cmd) {
        this.dbProvider.execute((TransactionStatus status) -> {
            SalaryEmployee salaryEmployee = salaryEmployeeProvider.findSalaryEmployeeById(cmd.getSalaryEmployeeId());
            List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(salaryEmployee.getOrganizationGroupId());
            List<SalaryEmployeeOriginVal> originVals = salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(salaryEmployee.getOwnerType(), salaryEmployee.getOwnerId(), salaryEmployee.getUserId());
            List<SalaryEmployeePeriodVal> periodVals = salaryEmployeePeriodValProvider.listSalaryEmployeePeriodVals(salaryEmployee.getId());
            salaryEmployeePeriodValProvider.deletePeriodVals(salaryEmployee.getId());
            cmd.getPeriodEmployeeEntities().stream().map(r ->{
                //获取到所有periodVa
                for (SalaryEmployeePeriodVal periodVal : periodVals) {
                    if (periodVal.getGroupEntityId().equals(r.getGroupEntityId())) {
                        periodVal.setSalaryValue(r.getSalaryValue());

                    }
                }
//                salaryEmployeePeriodValProvider.updateSalaryEmployeePeriodVal(r.getSalaryEmployeeId(),r.getGroupEntryId(),r.getSalaryValue());
                return null;
            });
            //前置处理originVals --主要是没有设置过个人的设置则继承批次设置
            processSalaryEmployeeOriginValsBeforeCalculate(groupEntities, originVals, salaryEmployee.getUserId());

            processSalaryEmployeePeriodVals(groupEntities, originVals, periodVals);

            for (SalaryEmployeePeriodVal periodVal : periodVals) {
                salaryEmployeePeriodValProvider.createSalaryEmployeePeriodVal(periodVal);
            }
            salaryEmployee.setStatus(cmd.getCheckFlag());
			salaryEmployeeProvider.updateSalaryEmployee(salaryEmployee);
			return null;
		});
	}


	@Override
	public void checkPeriodSalary(CheckPeriodSalaryCommand cmd) {
		//检验是否合算完成
		if(salaryEmployeeProvider.countUnCheckEmployee(cmd.getSalaryPeriodGroupId())>0)
			throw RuntimeErrorException.errorWith( SalaryConstants.SCOPE, SalaryConstants.ERROR_HAS_EMPLOYEE_UNCHECK,"there are some employee uncheck");
		
		//将本期group置为已核算
		SalaryGroup salaryGroup = salaryGroupProvider.findSalaryGroupById(cmd.getSalaryPeriodGroupId());
		salaryGroup.setStatus(SalaryGroupStatus.CHECKED.getCode());
		salaryGroupProvider.updateSalaryGroup(salaryGroup);
		
	}

	@Override
	public GetPeriodSalaryEmailContentResponse getPeriodSalaryEmailContent(GetPeriodSalaryEmailContentCommand cmd) {

		Organization salaryGroup = organizationProvider.findOrganizationById(cmd.getSalaryOrgId());

		List<SalaryGroupEntity> results = salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(salaryGroup.getId());
		List<SalaryGroupEntityDTO> entities = results.stream().map(r -> {
			SalaryGroupEntityDTO dto = ConvertHelper.convert(r, SalaryGroupEntityDTO.class);
			return dto;
		}).collect(Collectors.toList());
        return new GetPeriodSalaryEmailContentResponse(salaryGroup.getEmailContent(), entities, salaryGroup.getName(), salaryGroup.getId());
    }

    @Override
    public ListPeriodSalaryEmailContentsResponse listPeriodSalaryEmailContents(ListPeriodSalaryEmailContentsCommand cmd) {
        ListPeriodSalaryEmailContentsResponse response = new ListPeriodSalaryEmailContentsResponse();
        // : 1.获取所有的薪酬组
        List<Organization> salaryOrganizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.SALARYGROUP.getCode(), cmd.getOwnerId());
        if (null == salaryOrganizations) {
            return response;
        }
        response.setSalaryGroupResps(new ArrayList<>());
        for (Organization salaryOrg : salaryOrganizations) {
            GetPeriodSalaryEmailContentCommand cmd1 = new GetPeriodSalaryEmailContentCommand(cmd.getOwnerType(), cmd.getOwnerId(), salaryOrg.getId());
            GetPeriodSalaryEmailContentResponse resp1 = getPeriodSalaryEmailContent(cmd1);
            response.getSalaryGroupResps().add(resp1);
        }
        return response;
    }

    @Override
    public void batchUpdateSalaryGroupEntitiesVisable(BatchUpdateSalaryGroupEntitiesVisableCommand cmd) {
        for (UpdateSalaryGroupEntitiesVisableCommand cmd1 : cmd.getSalaryGroupCmd()) {
            cmd1.setOwnerId(cmd.getOwnerId());
            cmd1.setOwnerType(cmd.getOwnerType());
            updateSalaryGroupEntitiesVisable(cmd1);
        }
    }

    @Override
	public void setSalaryEmailContent(SetSalaryEmailContentCommand cmd) {
		//email content

		if(cmd.getSalaryGroupId() == null){
			organizationProvider.updateSalaryGroupEmailContent(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getEmailContent());
		}else {
            Organization salaryOrg = organizationProvider.findOrganizationById(cmd.getSalaryGroupId());
            salaryOrg.setEmailContent(cmd.getEmailContent());
            organizationProvider.updateOrganization(salaryOrg);
		}
	}

	@Override
	public void updateSalaryGroupEntitiesVisable(UpdateSalaryGroupEntitiesVisableCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {

            Organization salaryOrg = organizationProvider.findOrganizationById(cmd.getSalaryGroupId());
            if (null == salaryOrg) {
                LOGGER.error(" cmd error no that organization cmd : "+ cmd);
                return null;
            }
            salaryOrg.setEmailContent(cmd.getEmailContent());
            organizationProvider.updateOrganization(salaryOrg);
            if (null == cmd.getSalaryGroupEntities()) {
                LOGGER.error(" cmd error no entities cmd : "+ cmd);
                return null;
            }
            for (SalaryGroupEntityDTO r : cmd.getSalaryGroupEntities()) {
                salaryGroupEntityProvider.updateSalaryGroupEntityVisible(r.getId(), r.getVisibleFlag());
            }
            return null;
		});
	}

	@Override
	public void sendPeriodSalary(SendPeriodSalaryCommand cmd) {
		//将本期group置为已核算
		coordinationProvider
				.getNamedLock(CoordinationLocks.SALARY_GROUP_LOCK.getCode()+cmd.getSalaryPeriodGroupId())
				.enter(() -> {
					// this.groupProvider.updateGroup(group);

			SalaryGroup salaryGroup = salaryGroupProvider.findSalaryGroupById(cmd.getSalaryPeriodGroupId());
            if (null == salaryGroup) {
                LOGGER.error("salaryGroup cannot found + "+cmd);
                throw RuntimeErrorException.errorWith( SalaryConstants.SCOPE,
                        SalaryConstants.ERROR_SALARY_GROUP_NOTFOUND,"salary  period group id wrong can not found ");
            }
			if(cmd.getSendTime() == null){
				salaryGroup.setSendTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				salaryGroup.setStatus(SalaryGroupStatus.SENDED.getCode());
				sendSalary(salaryGroup);
			}else {
				salaryGroup.setSendTime(new Timestamp(cmd.getSendTime()));
				salaryGroup.setStatus(SalaryGroupStatus.WAIT_FOR_SEND.getCode());
			}
			salaryGroupProvider.updateSalaryGroup(salaryGroup);

			return null;
		});
	}


	@Scheduled(cron = "1 0/15 * * * ?")
	private void sendSalaryScheduled(){
		sendSalary(new Timestamp(DateHelper.currentGMTTime().getTime()));
	}
	/**找一个时间点之前的待发送批次,并发送*/
	private void sendSalary(Timestamp date) {
		List<SalaryGroup> groups = salaryGroupProvider.listSalaryGroup(SalaryGroupStatus.WAIT_FOR_SEND.getCode(),date);
		for (SalaryGroup group : groups) {
			coordinationProvider.getNamedLock(CoordinationLocks.SALARY_GROUP_LOCK.getCode()+group.getId())
					.enter(() -> {
				sendSalary(group);
				group.setStatus(SalaryGroupStatus.SENDED.getCode());
				salaryGroupProvider.updateSalaryGroup(group);
				return null;
			});
		}
	}
	/**给某个批次某期发薪酬邮件*/
	private void sendSalary(SalaryGroup salaryPeriodGroup) {
		List<SalaryEmployee> employees = salaryEmployeeProvider.listSalaryEmployeeByPeriodGroupId(salaryPeriodGroup.getId());
        Organization salaryOrg = organizationProvider.findOrganizationById(salaryPeriodGroup.getOrganizationGroupId());
        for (SalaryEmployee employee : employees) {
            List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(salaryPeriodGroup.getOrganizationGroupId(), NormalFlag.YES.getCode());
            List<SalaryEmployeePeriodVal> employeeEntityVals = salaryEmployeePeriodValProvider.listSalaryEmployeePeriodVals(employee.getId());
			String entityTable = processEntityTableString(groupEntities, employeeEntityVals);
            SalaryEmployeeDTO employeeDTO = getPersonnelInfoByUserIdForSalary(employee.getUserId());
            String toAddress = employeeDTO.getEmail();
			String emailSubject = "薪酬发放";
			sendSalaryEmail(salaryPeriodGroup.getNamespaceId(),toAddress, emailSubject,salaryOrg.getEmailContent(), entityTable);
		}
	}

	private void sendSalaryEmail(Integer namespaceId, String toAddress, String emailSubject, String emailContent, String entityTable) {

		try{
//			String address = configProvider.getValue(namespaceId,"mail.smtp.address", "smtp.mxhichina.com");
//			String passwod = configProvider.getValue(namespaceId,"mail.smtp.passwod", "abc123!@#");
//			int port = configProvider.getIntValue(namespaceId,"mail.smtp.port", 25);
////			new Mailer(address, port , account , passwod).sendMail(email);
			//另一种发送方式
			String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
			MailHandler handler = PlatformContext.getComponent(handlerName);
			Map<String, String> map = new HashMap<String, String>();
			map.put("emailContent", emailContent);
			map.put("entityTable", entityTable);
			String content = localeTemplateService.getLocaleTemplateString(SalaryConstants.SEND_MAIL_SCOPE,
					SalaryConstants.SEND_MAIL_CODE, "zh_CN", map, "");
            LOGGER.debug("meailcontent \n "+emailContent+"\ntable"+entityTable+ "\n finalContent"+content);
			handler.sendMail(namespaceId, null,toAddress, emailSubject,content);

		}catch (Exception e){
			LOGGER.debug("had a error in send message !!!!!++++++++++++++++++++++",e);
		}
	}

	private String processEntityTableString(List<SalaryGroupEntity> groupEntities, List<SalaryEmployeePeriodVal> employeeEntityVals) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		if(null != groupEntities){
			sb.append("<tr>");
            for (SalaryGroupEntity r : groupEntities) {
                sb.append("<th>");
                sb.append(r.getName());
                sb.append("</th>");
            }
			sb.append("</tr>");
			sb.append("<tr>");
            for (SalaryGroupEntity r : groupEntities) {
				sb.append("<td>");
				SalaryEmployeePeriodVal val = getSalaryEmployeePeriodVal(r.getId(),employeeEntityVals);
				if(null != val)
					sb.append(val.getSalaryValue());
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	private SalaryEmployeePeriodVal getSalaryEmployeePeriodVal(Long entityId, List<SalaryEmployeePeriodVal> employeeEntityVals) {
		if (null == employeeEntityVals) {
			return null;
		}

		for (SalaryEmployeePeriodVal val : employeeEntityVals) {
			if (val.getGroupEntityId().equals(entityId)) {
				return val;
			}
		}
		return null;
	}

	/**
	 * 每月第一天的0点5分5秒开始执行
	 * 内容: 本期(本月) 薪酬组生成salarygroup和employee
	 * */
	@Scheduled(cron = "5 5 0 1 * ?")
        public void monthScheduled() {
        Calendar periodCalendar = Calendar.getInstance();
        periodCalendar.add(Calendar.MONTH, -1);
        String period = monthSF.get().format(periodCalendar.getTime());
        monthScheduled(period);
    }
    @Override
    public void monthScheduled(String period)  {
        Calendar calendar = Calendar.getInstance();
        String lastPeriod = "";
        try {
            calendar.setTime(monthSF.get().parse(period));
            calendar.set(Calendar.MONTH,-1);
            lastPeriod =  monthSF.get().format(calendar.getTime());
        } catch (ParseException e) {
            LOGGER.error("parse exception ",e);
        }

        // : 1.获取所有的薪酬组
        List<Organization> salaryOrganizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.SALARYGROUP.getCode(), null);

        for (Organization salaryOrg : salaryOrganizations) {
			SalaryGroup salaryGroup = new SalaryGroup();
			salaryGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			salaryGroup.setSalaryPeriod(period);
			salaryGroup.setOrganizationGroupId(salaryOrg.getId());
			salaryGroup.setNamespaceId(salaryOrg.getNamespaceId());
			salaryGroup.setOwnerType("organization");
            salaryGroup.setGroupName(salaryOrg.getName());
			salaryGroup.setOwnerId(punchService.getTopEnterpriseId(salaryOrg.getDirectlyEnterpriseId()));
			salaryGroup.setStatus(SalaryGroupStatus.UNCHECK.getCode());
            SalaryGroup lastGroup = salaryGroupProvider.findSalaryGroupByOrgId(salaryOrg.getId(), lastPeriod);
            salaryGroup.setEmailContent(salaryOrg.getEmailContent());
            salaryGroupProvider.deleteSalaryGroup(salaryGroup.getOrganizationGroupId(), salaryGroup.getSalaryPeriod());
            salaryGroupProvider.createSalaryGroup(salaryGroup);
			// 2.循环薪酬组取里面的人员
			List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(salaryOrg.getId());
            List<UniongroupMemberDetailsDTO> members = uniongroupService.listUniongroupMemberDetailsByGroupId(salaryOrg.getId());
            if(null == members) {
                LOGGER.error("salaryOrg no members :" + salaryOrg);
                continue;
            }
            List<Long> userIds = members.stream().map(r->{
                return r.getTargetId();
            }).collect(Collectors.toList());
			for (Long userId : userIds) {
				SalaryEmployee employee = ConvertHelper.convert(salaryGroup, SalaryEmployee.class);
                employee.setUserId(userId);
				employee.setSalaryGroupId(salaryGroup.getId());
                salaryEmployeeProvider.deleteSalaryEmployee(employee.getOwnerId(),employee.getUserId(),employee.getSalaryGroupId());
				salaryEmployeeProvider.createSalaryEmployee(employee);
				//  获取个人的项目字段
				List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(employee.getOwnerType(), employee.getOwnerId(), userId);
                LOGGER.debug("\n entities ++ " + salaryGroupEntities);
                processSalaryEmployeeOriginValsBeforeCalculate(salaryGroupEntities, salaryEmployeeOriginVals,userId);
                LOGGER.debug("\n after before calculate : salary origin  Vals ++ " + salaryEmployeeOriginVals);
                List<SalaryEmployeePeriodVal> salaryEmployeePeriodVals = new ArrayList<>();
                //3.循环每一个批次设置的字段项，给他做成本期的periodVal 如果entity不是计算公式就设置值，如果是公式就空
				for (SalaryGroupEntity entity : salaryGroupEntities) {
                    //生成 period Val
					SalaryEmployeePeriodVal val = new SalaryEmployeePeriodVal();
                    val.setOwnerType(entity.getOwnerType());
					val.setOwnerId(entity.getOwnerId());
                    val.setOriginEntityId(entity.getOriginEntityId());
					val.setGroupEntityId(entity.getId());
					val.setSalaryEmployeeId(employee.getId());
                    val.setGroupEntityName(entity.getName());
                    if(null != salaryEmployeeOriginVals && !(entity.getNumberType()!= null
                            && entity.getNumberType().equals(SalaryEntityNumberType.FORMULA.getCode()))){
						SalaryEmployeeOriginVal originVal = findOriginVal(entity.getId(), salaryEmployeeOriginVals);
                        if(null != originVal)
                            val.setSalaryValue(originVal.getSalaryValue());
                        else
                            val.setSalaryValue(entity.getDefaultValue());
                    }
                    salaryEmployeePeriodVals.add(val);
                }
				processSalaryEmployeePeriodVals(salaryGroupEntities,salaryEmployeeOriginVals,salaryEmployeePeriodVals);
                LOGGER.debug("\n salary Period Vals ++ " + salaryEmployeePeriodVals);
                salaryEmployeePeriodValProvider.createSalaryEmployeePeriodVals(salaryEmployeePeriodVals);
			}
		}

	}

	private SalaryEmployeeOriginVal findOriginVal(Long id, List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals) {
		if (null != salaryEmployeeOriginVals) {
			for (SalaryEmployeeOriginVal val : salaryEmployeeOriginVals) {
				if (id.equals(val.getGroupEntityId())) {
					return val;
				}
			}
		}
		return null;
	}
    /**
     * 前置处理originVals --主要是没有设置过个人的设置则继承批次设置
     * */
    private void processSalaryEmployeeOriginValsBeforeCalculate(List<SalaryGroupEntity> salaryGroupEntities, List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals, Long userId){
        //如果这个个人没设置vals 就用批次设置的默认值
        if (salaryEmployeeOriginVals.size() == 0) {
            for (SalaryGroupEntity entity : salaryGroupEntities) {
                SalaryEmployeeOriginVal val = ConvertHelper.convert(entity, SalaryEmployeeOriginVal.class);
                val.setGroupEntityId(entity.getId());
                val.setGroupEntityName(entity.getName());
                val.setUserId(userId);
                val.setOriginEntityId(entity.getId());
                if (entity.getType().equals(SalaryEntityType.TEXT.getCode())) {
                    val.setSalaryValue(processSalaryValue(entity.getOriginEntityId(),userId));
                } else if (entity.getNumberType().equals(SalaryEntityNumberType.VALUE.getCode())) {
                    val.setSalaryValue(entity.getDefaultValue());
                }
                salaryEmployeeOriginVals.add(val);
            }
        }
    }

    private String processSalaryValue(Long originEntityId, Long userId) {
        SalaryEmployeeDTO dto = getPersonnelInfoByUserIdForSalary(userId);
        if(originEntityId.equals(SalaryConstants.ENTITY_ID_GONGHAO)){
            return dto.getEmployeeNo();
        }else if(originEntityId.equals(SalaryConstants.ENTITY_ID_BUMEN)){
            return dto.getDepartment();
        }else if(originEntityId.equals(SalaryConstants.ENTITY_ID_NAME)){
            return dto.getContactName();
        }else if(originEntityId.equals(SalaryConstants.ENTITY_ID_BANKNO)){
            return dto.getSalaryCardNumber();
        }else if(originEntityId.equals(SalaryConstants.ENTITY_ID_SHENFENZHENG)){
            return dto.getIdNumber();
        }
        return "";
    }

    /**
     * 计算 period Vals 的值
     * @param salaryEmployeePeriodVals : 要计算的本期Vals列表，传入的时候数值类都需要填写，公式类都为null
     * */
	private void processSalaryEmployeePeriodVals( List<SalaryGroupEntity> salaryGroupEntities,
                                                  List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals,
                                                  List<SalaryEmployeePeriodVal> salaryEmployeePeriodVals) {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        //循环vals 用把公式部分给搞出来
		int loopTimes = 0;
		Map<String, String> valueMap = new HashMap<String, String> ();
		while(true){
			if(++loopTimes > 10){
				//默认不会10层嵌套。如果有 我认栽
				break;
			}

			for (SalaryEmployeePeriodVal val : salaryEmployeePeriodVals) {
				//在这里进行计算
				//如果value不为null说明是直接数值,直接放进map
                if (val.getSalaryValue() != null) {
                    valueMap.put(val.getGroupEntityName(), val.getSalaryValue());
                }
                else {
                    try {
                        //其他的如果计算成功放入map计算失败则继续计算
                        SalaryGroupEntity entity = findGroupEntity(val.getGroupEntityId(), salaryGroupEntities);
                        String format = entity.getDefaultValue();
                        Template freeMarkerTemplate = null;
                        String templateKey = getTemplateKEY(entity);
                        if (entity.getType().equals(SalaryEntityType.TEXT.getCode())) {
                            val.setSalaryValue(entity.getDefaultValue());
                            continue;
                        }
                        templateLoader.putTemplate(templateKey, format);
                        freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
                        String evalString = "result = " + FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, valueMap);
                        if(!evalString.contains("${")){
                            //如果没有${ 说明全部参数都替换成了数字 则进行计算
                            String result = engine.eval(evalString).toString();
                            val.setSalaryValue(result);
                            valueMap.put(val.getGroupEntityName(), val.getSalaryValue());
                        }
                    }catch (Exception e){
                        LOGGER.debug("calculate format catch a exception ",e);
                    }
                }

			}

			if (valueMap.size() == salaryEmployeeOriginVals.size()) {
				//如果都装进了map就是说都有值了,那就跳出
				break;
			}
		}
	}
    /**salary.[ownerId].[批次entity的id]*/
    private String getTemplateKEY(SalaryGroupEntity entity) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(SalaryConstants.SCOPE);
        strBuilder.append(".");
        strBuilder.append(entity.getOwnerId());
        strBuilder.append(".");
        strBuilder.append(entity.getId());

        return strBuilder.toString();
    }

    private SalaryGroupEntity findGroupEntity(Long groupEntityId, List<SalaryGroupEntity> salaryGroupEntities) {
        if (null == salaryGroupEntities) {
            return null;
        }
        for (SalaryGroupEntity entity : salaryGroupEntities) {
            if (entity.getId().equals(groupEntityId)) {
                return entity;
            }
        }
        return null;
    }

    @Override
	public ListSalarySendHistoryResponse listSalarySendHistory(ListSalarySendHistoryCommand cmd) {

		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		List<Long> userIds = punchService.listDptUserIds(org, cmd.getOrganizationId(), cmd.getKeyWord(), NormalFlag.YES.getCode());
		Calendar startClanedar = Calendar.getInstance();
		startClanedar.setTimeInMillis(cmd.getBeginTime());
		Calendar endClanedar = Calendar.getInstance();
		endClanedar.setTimeInMillis(cmd.getEndTime());
		List<String> periods = processPeriods(startClanedar,endClanedar);
		List<SalaryEmployee> results = salaryEmployeeProvider.listSalaryEmployees(userIds, periods);
		List<SalaryPeriodEmployeeDTO> employeeDtos = results.stream().map(r2 -> {
			SalaryPeriodEmployeeDTO dto2 = processSalaryPeriodEmployeeDTO(r2);
			return dto2;
		}).collect(Collectors.toList());
		return new ListSalarySendHistoryResponse(employeeDtos);
	}

	private List<String> processPeriods(Calendar startClanedar, Calendar endClanedar) {
		List<String> result = new ArrayList<>();
		while(startClanedar.before(endClanedar)){
			result.add(this.monthSF.get().format(startClanedar.getTime()));
			startClanedar.add(Calendar.MONTH,1);
		}
		result.add(this.monthSF.get().format(endClanedar.getTime()));
		return result;
	}


	@Override
	public void exportSalarySendHistory(ExportSalarySendHistoryCommand cmd, HttpServletResponse httpResponse) {
        ListSalarySendHistoryCommand listCmd = ConvertHelper.convert(cmd, ListSalarySendHistoryCommand.class);
        ListSalarySendHistoryResponse resp = listSalarySendHistory(listCmd);
        if (null != resp && resp.getSalaryPeriodEmployees() != null) {

            ByteArrayOutputStream out = null;
            XSSFWorkbook workbook = this.createXSSFSalaryHistory(resp.getSalaryPeriodEmployees());
            createOutPutSteam(workbook, out, httpResponse);
        }

	}

    private void createOutPutSteam(XSSFWorkbook workbook, ByteArrayOutputStream out, HttpServletResponse httpResponse) {
        try {
            out = new ByteArrayOutputStream();
            workbook.write(out);
            DownloadUtil.download(out, httpResponse);
        } catch (Exception e) {
            LOGGER.error("EXPORT ERROR, e = {}", e);
        } finally {
            try {
                workbook.close();
                out.close();
            } catch (IOException e) {
                LOGGER.error("CLOSE ERROR", e);
            }
        }
    }

    private XSSFWorkbook createXSSFSalaryHistory(List<SalaryPeriodEmployeeDTO> salaryPeriodEmployees) {

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName ="Module";
        XSSFSheet sheet = wb.createSheet(sheetName);
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 12));
//        XSSFCellStyle style = wb.createCellStyle();
//        Font font = wb.createFont();
//        font.setFontHeightInPoints((short) 20);
//        font.setFontName("Courier New");

//        style.setFont(font);

//        XSSFCellStyle titleStyle = wb.createCellStyle();
//        titleStyle.setFont(font);
//        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //  创建标题
        createXSSFSalaryHistoryHead(sheet);

        createXSSFSalaryHistoryRows(sheet, salaryPeriodEmployees);
        return wb;
    }

    private void createXSSFSalaryHistoryRows(XSSFSheet sheet, List<SalaryPeriodEmployeeDTO> salaryPeriodEmployees) {
        for (SalaryPeriodEmployeeDTO dto : salaryPeriodEmployees) {
            Row row = sheet.createRow(sheet.getLastRowNum()+1);
            int i = -1;
            row.createCell(++i).setCellValue(dto.getEmployeeNo());
            row.createCell(++i).setCellValue(dto.getContactName());
            row.createCell(++i).setCellValue(dto.getDepartments());
            row.createCell(++i).setCellValue(dto.getSalaryPeriod());
            row.createCell(++i).setCellValue(dto.getSalaryGroupName());
            if(null != dto.getPaidMoney())
                row.createCell(++i).setCellValue("");
            else
                row.createCell(++i).setCellValue(dto.getPaidMoney().toString());

        }
    }

    private void createXSSFSalaryHistoryHead(XSSFSheet sheet) {
        int rowNum = 0;

        XSSFRow row = sheet.createRow(rowNum++);
//        row.setRowStyle(style);
        row.createCell(0).setCellValue("员工编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("部门");
        row.createCell(3).setCellValue("时间");
        row.createCell(4).setCellValue("所属批次");
        row.createCell(5).setCellValue("实发工资");
    }

    @Override
	public void batchSetEmployeeCheckFlag(BatchSetEmployeeCheckFlagCommand cmd) {
		if(null == cmd.getCheckFlag())
			cmd.setCheckFlag(NormalFlag.YES.getCode());
		salaryEmployeeProvider.updateSalaryEmployeeCheckFlag(cmd.getSalaryEmployeeIds(), cmd.getCheckFlag());
	}

	
    @Override
    public void revokeSendPeriodSalary(SendPeriodSalaryCommand cmd) {

		coordinationProvider.getNamedLock(CoordinationLocks.SALARY_GROUP_LOCK.getCode()+cmd.getSalaryPeriodGroupId())
				.enter(() -> {
					SalaryGroup salaryGroup = salaryGroupProvider.findSalaryGroupById(cmd.getSalaryPeriodGroupId());
                    if (null == salaryGroup) {
                        LOGGER.error("salaryGroup cannot found + "+cmd);

                        throw RuntimeErrorException.errorWith( SalaryConstants.SCOPE,
                                SalaryConstants.ERROR_SALARY_GROUP_NOTFOUND,"salary  period group id wrong can not found ");
                    }
                    if (SalaryGroupStatus.WAIT_FOR_SEND.getCode().equals(salaryGroup.getStatus())){
						salaryGroup.setSendTime(null);
						salaryGroup.setStatus(SalaryGroupStatus.CHECKED.getCode());
						salaryGroupProvider.updateSalaryGroup(salaryGroup);
					}else{
						throw RuntimeErrorException.errorWith( SalaryConstants.SCOPE,
								SalaryConstants.ERROR_SALARY_GROUP_STATUS,"salary group status error");
					}
					return null;
				});
    }

    //  added by R, for salaryGroup 20170706
    @Override
    public SalaryEmployeeDTO getPersonnelInfoByUserIdForSalary(Long userId){
//        PersonnelsDetailsV2Response response = new PersonnelsDetailsV2Response();

        SalaryEmployeeDTO result = new SalaryEmployeeDTO();
        OrganizationMemberDetails memberDetails = this.organizationProvider.findOrganizationMemberDetailsByTargetId(userId);
        Integer namespaceId = memberDetails.getNamespaceId();
        Map<Long,String> departMap = this.organizationProvider.listOrganizationsOfDetail(namespaceId,memberDetails.getId(),OrganizationGroupType.DEPARTMENT.getCode());
        String department = "";
        result.setContactName(memberDetails.getContactName());
        if(!StringUtils.isEmpty(memberDetails.getEmployeeNo()))
            result.setEmployeeNo(memberDetails.getEmployeeNo());
        if(!StringUtils.isEmpty(departMap)){
            for(Long k : departMap.keySet()){
                department += (departMap.get(k) + ",");
            }
            department = department.substring(0,department.length()-1);
        }
        result.setDepartment(department);
        if(!StringUtils.isEmpty(memberDetails.getSalaryCardNumber()))
            result.setSalaryCardNumber(memberDetails.getSalaryCardNumber());
        if(!StringUtils.isEmpty(memberDetails.getIdNumber()))
            result.setIdNumber(memberDetails.getIdNumber());
        if(!StringUtils.isEmpty(memberDetails.getEmail()))
            result.setEmail(memberDetails.getEmail());
        return result;
    }
}
