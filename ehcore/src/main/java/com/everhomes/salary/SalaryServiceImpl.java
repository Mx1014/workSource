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
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.salary.*;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.uniongroup.UniongroupType;
import com.everhomes.techpark.punch.PunchService;
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
import org.apache.poi.xssf.usermodel.*;
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

            this.salaryGroupEntityProvider.deleteSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
            AddSalaryGroupCommand addCommand = new AddSalaryGroupCommand();
            addCommand.setSalaryGroupId(cmd.getSalaryGroupId());
            addCommand.setSalaryGroupName(cmd.getSalaryGroupName());
            addCommand.setOwnerType(cmd.getOwnerType());
            addCommand.setOwnerId(cmd.getOwnerId());
            addCommand.setSalaryGroupEntity(cmd.getSalaryGroupEntity().stream().map(r -> {
                SalaryGroupEntityDTO dto = ConvertHelper.convert(r,SalaryGroupEntityDTO.class);
                return dto;
            }).collect(Collectors.toList()));
            AddSalaryGroupResponse response = this.addSalaryGroup(addCommand);
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

            //  删除薪酬组定义的字段
            this.salaryGroupEntityProvider.deleteSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());

            //  删除个人设定中与薪酬组相关的字段
            this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupId(cmd.getSalaryGroupId());
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
    public ListSalaryGroupResponse listSalaryGroup(ListSalaryGroupCommand cmd){

	    ListSalaryGroupResponse response = new ListSalaryGroupResponse();

        //  获取所有批次
	    List<Organization> organizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.SALARYGROUP.getCode(), cmd.getOwnerId());


	    //  查询相关人数
/*	    List<Long> salaryGroupIds = new ArrayList<>();
	    organizations.forEach(r ->{
	        Long salaryGroupId = r.getId();
            salaryGroupIds.add(salaryGroupId);
        });
	    List<Object[]> lists = this.salaryEmployeeOriginValProvider.getRelevantNumbersByGroupId(salaryGroupIds);
*/

	    //  设置批次信息
	    response.setSalaryGroupList(organizations.stream().map(p ->{
            SalaryGroupListDTO dto = new SalaryGroupListDTO();
            dto.setSalaryGroupId(p.getId());
            dto.setSalaryGroupName(p.getName());
            //  设置相关人数
/*            if(!lists.isEmpty()){
                lists.forEach(q -> {
                    if(q[0].equals(dto.getSalaryGroupId()))
                        dto.setRelevantNum((Integer)q[1]);
                });
            }*/
            return dto;
        }).collect(Collectors.toList()));

	    return response;
    }


    @Override
	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd) {

        //  将前端信息传递给组织架构的接口获取相关信息
/*        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setKeywords(cmd.getKeywords());
        command.setOrganizationId(cmd.getOrganizationId());
        command
        List<OrganizationMember> members = this.organizationService.listOrganizationPersonnels()*/
//        this.organizationService.listUniongroupMemeberDetailsWithCondition();
        //  2.通过对实发工资的判断来拼接字符串，反映是否设置了工资明细
        //  一次性读取所有userid的实发工资值然后做拼接

        ListSalaryEmployeesResponse response = new ListSalaryEmployeesResponse();


		return new ListSalaryEmployeesResponse();
	}

    @Override
    public List<SalaryEmployeeOriginValDTO> getSalaryEmployees(GetSalaryEmployeesCommand cmd) {

            List<SalaryEmployeeOriginValDTO> results = new ArrayList<>();
        //   查询个人对应的批次
             /*   Long salaryGroupId;
        if(StringUtils.isEmpty(cmd.getSalaryGroupId()))
            salaryGroupId = this.organizationService.getSalaryGroupId(cmd.getUserId(),cmd.getOwnerType(),cmd.getOwnerId());
        else
            salaryGroupId = cmd.getSalaryGroupId();*/

        //  获取对应批次的项目字段
        List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
        //  获取个人的项目字段
        List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(cmd.getUserId(),cmd.getOwnerType(),cmd.getOwnerId());

        //  若没有关联批次则直接返回空
        if (!salaryGroupEntities.isEmpty()) {
            salaryGroupEntities.stream().forEach(r -> {
                SalaryEmployeeOriginValDTO dto = new SalaryEmployeeOriginValDTO();
                dto.setSalaryGroupId(r.getGroupId());
                dto.setUserId(cmd.getUserId());
                dto.setGroupEntityId(r.getId());
                dto.setOriginEntityId(r.getOriginEntityId());
                dto.setGroupEntityName(r.getName());

                //  为对应字段赋值
                if (!salaryEmployeeOriginVals.isEmpty()) {
                    salaryEmployeeOriginVals.stream().forEach(s -> {
                        if (r.getName().equals(s.getGroupEntityName())) {
                            dto.setSalaryValue(s.getSalaryValue());
                            dto.setId(s.getId());
                        }
                    });
                }

                results.add(dto);
            });
            return results;
        } else
            return null;
    }

    @Override
    public void updateSalaryEmployees(UpdateSalaryEmployeesCommand cmd) {

        User user = UserContext.current().getUser();
        if(!cmd.getEmployeeOriginVal().isEmpty()){
            Long userId = cmd.getEmployeeOriginVal().get(0).getUserId();
            Long groupId = cmd.getEmployeeOriginVal().get(0).getSalaryGroupId();

            //  先判断是否需要将人员添加到组织架构的薪酬组中
//            this.findOrganizationPersonnel(userId,groupId);
//            if(... == null)
//            this.organizationService.addPersonnelsToSalaryGroup(userId,groupId)

            List<SalaryEmployeeOriginVal> originVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(userId,cmd.getOwnerType(),cmd.getOwnerId());
            if(originVals.isEmpty()){
                cmd.getEmployeeOriginVal().stream().forEach(r -> {
                    this.createSalaryEmployeeOriginVal(r, cmd.getOwnerType(),cmd.getOwnerId());
                });
            }else{
                this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupIdUserId(groupId, userId,cmd.getOwnerType(),cmd.getOwnerId());
                cmd.getEmployeeOriginVal().stream().forEach(s ->{
                    this.createSalaryEmployeeOriginVal(s,cmd.getOwnerType(),cmd.getOwnerId());
                });
            }
        }
    }

    private void createSalaryEmployeeOriginVal(SalaryEmployeeOriginValDTO dto, String ownerType, Long ownerId) {
        SalaryEmployeeOriginVal originVal = new SalaryEmployeeOriginVal();
        originVal.setOwnerType(ownerType);
        originVal.setOwnerId(ownerId);
        originVal.setGroupId(dto.getSalaryGroupId());
        originVal.setUserId(dto.getUserId());
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
        if(!cmd.getDepartmentIds().isEmpty()){
            cmd.getDepartmentIds().forEach(r ->{
                UniongroupTarget target = new UniongroupTarget();
                target.setId(r);
                target.setType(UniongroupTargetType.ORGANIZATION.getCode());
                targets.add(target);
            });
        }

        //  2.将选择的人员的 detailId 传入 targets
        if(!cmd.getUserIds().isEmpty()){
            cmd.getUserIds().forEach(r ->{
                UniongroupTarget target = new UniongroupTarget();
                target.setId(r);
                target.setType(UniongroupTargetType.MEMBERDETAIL.getCode());
                targets.add(target);
            });
        }

        // 3.将人员添加至组织架构的薪酬组
        this.uniongroupService.saveUniongroupConfigures(command);
    }

    @Override
    public void exportSalaryGroup(ExportSalaryGroupCommand cmd, HttpServletResponse httpServletResponse) {
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {

/*            //  根据批次 id 查找批次具体内容
            GetSalaryGroupCommand command = new GetSalaryGroupCommand();
            command.setSalaryGroupId(cmd.getSalaryGroupId());
            GetSalaryGroupResponse response = this.getSalaryGroup(command);*/

            List<SalaryGroupEntity> results = this.salaryGroupEntityProvider.listSalaryGroupWithExportRegular(cmd.getSalaryGroupId());
            Organization organization = this.organizationProvider.findOrganizationById(cmd.getOrganizationId());

            ByteArrayOutputStream out = null;
            XSSFWorkbook workbook = this.creatXSSFSalaryGroupFile(results,organization.getName());
            createOutPutSteam(workbook, out, httpServletResponse);
        }

    }

    private XSSFWorkbook creatXSSFSalaryGroupFile( List<SalaryGroupEntity> results, String name){

/*        List<SalaryGroupEntityDTO> entityDTOs = response.getSalaryGroupEntity();

        List<SalaryGroupEntity>*/

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
	public ImportFileTaskDTO importSalaryGroup(MultipartFile mfile,
                                               Long userId, ImportSalaryGroupCommand cmd) {
        ImportFileTask task = new ImportFileTask();
        try {

            // 解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if(resultList.isEmpty()){
                LOGGER.error("File content is empty");
/*                throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
                        "File content is empty");*/
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
                    List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> results = importSalaryFiles(datas, salaryGroupEntities, userId, cmd);
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

	private List<ImportSalaryEmployeeOriginValDTO> handleImportSalaryFiles(
	        List list, Long groupId, List<SalaryGroupEntity> salaryGroupEntities){

        List<ImportSalaryEmployeeOriginValDTO> datas = new ArrayList<>();

        salaryGroupEntities.add(0,new SalaryGroupEntity("姓名"));
        salaryGroupEntities.add(1,new SalaryGroupEntity("手机号"));

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

    private List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> importSalaryFiles(
            List<ImportSalaryEmployeeOriginValDTO> datas, List<SalaryGroupEntity> salaryGroupEntities,
            Long userId, ImportSalaryGroupCommand cmd) {

	    ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> log = new ImportFileResultLog<>(SalaryServiceErrorCode.SCOPE);
        List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> errorDataLogs = new ArrayList<>();

        //  一次读出 “有薪酬组的” 用户的手机号与id
//        List<String> contactTokens = this.xxx
        List<Long[]> users = new ArrayList<>();

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

    private ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> checkSalaryGroup(ImportSalaryEmployeeOriginValDTO data, List<Long[]> users){

        ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> log = new ImportFileResultLog<>(SalaryServiceErrorCode.SCOPE);
        if(StringUtils.isEmpty(data.getSalaryEmployeeVal().get(1))){
            LOGGER.warn("Organization member contactName is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactName is null");
            log.setCode(SalaryServiceErrorCode.ERROR_CONTACTNAME_ISNULL);
            return log;
        }else {
            //  直接查与薪酬组相关的手机号列表中是否存在该号码，在导入的时候一次性把所有号码读出来
            for(int i=0; i<users.size(); i++) {
                if (data.getSalaryEmployeeVal().get(1).equals(users.get(i)[0])) {
                    data.getSalaryEmployeeVal().add(1, String.valueOf(users.get(i)[1]));
                    return null;
                }
            }

            //  未比对成功则失败
            LOGGER.warn("Organization member contactName is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactName is null");
            log.setCode(SalaryServiceErrorCode.ERROR_CONTACTNAME_ISNULL);
            return log;
        }
    }

    private void saveSalaryGroup(ImportSalaryEmployeeOriginValDTO data, List<SalaryGroupEntity> salaryGroupEntities,
                                 Long groupId, Long organizationId, String ownerType, Long ownerId) {
        for(int i=0; i<data.getSalaryEmployeeVal().size();i++){
            SalaryEmployeeOriginValDTO dto = new SalaryEmployeeOriginValDTO();
            dto.setUserId(Long.valueOf(data.getSalaryEmployeeVal().get(1)));
            dto.setGroupEntityName(salaryGroupEntities.get(i).getName());
            dto.setSalaryGroupId(groupId);
            dto.setGroupEntityId(salaryGroupEntities.get(i).getId());
            dto.setOriginEntityId(salaryGroupEntities.get(i).getOriginEntityId());
            dto.setSalaryValue(data.getSalaryEmployeeVal().get(i+2));
            this.createSalaryEmployeeOriginVal(dto,ownerType,ownerId);
        }
/*        data.getSalaryEmployeeVal().forEach(r -> {
            SalaryEmployeeOriginValDTO dto = new SalaryEmployeeOriginValDTO();
            dto.setGroupEntityName();
        });*/

    }

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
		Integer unLinkNumber = 0;
		abnormalNumber += unLinkNumber;
		//判断2:关联了批次,但是实发工资为"-"
		//查询eh_salary_employee_period_vals 本期 的 实发工资(entity_id=98)数据为null的记录数
		Integer nullSalaryNumber = salaryEmployeePeriodValProvider.countSalaryEmployeePeriodsByPeriodAndEntity(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getPeriod(),SalaryConstants.ENTITY_ID_SHIFA);
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
        response.setSalaryGroupEntitys(groupEntities.stream().map(r -> {
            SalaryGroupEntityDTO dto = ConvertHelper.convert(r, SalaryGroupEntityDTO.class);
            return dto;
        }).collect(Collectors.toList()));
        //2.查人员 periodGroupId 可以确定:公司,薪酬组和期数
        Organization org = organizationProvider.findOrganizationById(punchService.getTopEnterpriseId(cmd.getOwnerId()));
        List<Long> userIds = punchService.listDptUserIds(org, cmd.getOrganizationId(), cmd.getKeyWords(), NormalFlag.YES.getCode());

        List<SalaryEmployee> result = salaryEmployeeProvider.listSalaryEmployees(cmd.getSalaryPeriodGroupId(),userIds,cmd.getCheckFlag());
		if(null == result )
			return response;
        response.setSalaryPeriodEmployees(result.stream().map(r ->{	
        	SalaryPeriodEmployeeDTO dto = processSalaryPeriodEmployeeDTO(r);
            return dto;
        }).collect(Collectors.toList()));
		return response; 
	}

	private SalaryPeriodEmployeeDTO processSalaryPeriodEmployeeDTO(SalaryEmployee r) {
		SalaryPeriodEmployeeDTO dto = ConvertHelper.convert(r, SalaryPeriodEmployeeDTO.class);
		//TODO: 列表一些字段值
		//岗位

		//批次名
		Organization org = organizationProvider.findOrganizationById(r.getOrganizationGroupId());
		dto.setSalaryGroupId(r.getOrganizationGroupId());
		if(null != org)
			dto.setSalaryGroupName(org.getName());
		//3.查人员的vals
		List<SalaryEmployeePeriodVal> result = salaryEmployeePeriodValProvider.listSalaryEmployeePeriodVals(r.getId());
		if(null == result)
			return null;
		dto.setPeriodEmployeeEntitys(result.stream().map(r2 ->{	
			SalaryPeriodEmployeeEntityDTO dto2 = processSalaryPeriodEmployeeEntityDTO(r2);
			dto2.setIsFormula(NormalFlag.NO.getCode());
			if (r2.getGroupEntityId().equals(SalaryConstants.ENTITY_ID_GONGHAO)) {
				dto.setEmployeeNo(r2.getSalaryValue());
			}else if (r2.getGroupEntityId().equals(SalaryConstants.ENTITY_ID_NAME)) {
				dto.setContactName(r2.getSalaryValue());
			}else if (r2.getGroupEntityId().equals(SalaryConstants.ENTITY_ID_BUMEN)) {
				dto.setDepartments(r2.getSalaryValue());
			}else if (r2.getGroupEntityId().equals(SalaryConstants.ENTITY_ID_SHIFA)) {
				dto.setPaidMoney(new BigDecimal(r2.getSalaryValue()));
			}
			SalaryGroupEntity groupEntity = salaryGroupEntityProvider.findSalaryGroupEntityById(r2.getGroupEntityId());
			if (null != groupEntity.getNumberType() && groupEntity.getNumberType().equals(NormalFlag.YES.getCode())) {
				dto2.setSalaryValue(groupEntity.getDefaultValue());
				dto2.setIsFormula(NormalFlag.YES.getCode());
			}
			return dto2;
        }).collect(Collectors.toList()));
		return dto;
	}

	private SalaryPeriodEmployeeEntityDTO processSalaryPeriodEmployeeEntityDTO(
			SalaryEmployeePeriodVal r) {
		SalaryPeriodEmployeeEntityDTO dto = ConvertHelper.convert(r, SalaryPeriodEmployeeEntityDTO.class);
		 
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
			throw RuntimeErrorException.errorWith( SalaryConstants.SCOPE,SalaryConstants.ERROR_HAS_EMPLOYEE_UNCHECK,"there are some employee uncheck");
		
		//将本期group置为已核算
		SalaryGroup salaryGroup = salaryGroupProvider.findSalaryGroupById(cmd.getSalaryPeriodGroupId());
		salaryGroup.setStatus(SalaryGroupStatus.CHECKED.getCode());
		salaryGroupProvider.updateSalaryGroup(salaryGroup);
		
	}

	@Override
	public GetPeriodSalaryEmailContentResponse getPeriodSalaryEmailContent(GetPeriodSalaryEmailContentCommand cmd) {

		SalaryGroup periodGroup = salaryGroupProvider.findSalaryGroupById(cmd.getSalaryPeriodGroupId());
		List<SalaryGroupEntity> results = salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(periodGroup.getOrganizationGroupId());
		List<SalaryGroupEntityDTO> entities = results.stream().map(r -> {
			SalaryGroupEntityDTO dto = ConvertHelper.convert(r, SalaryGroupEntityDTO.class);
			return dto;
		}).collect(Collectors.toList());
		return new GetPeriodSalaryEmailContentResponse(periodGroup.getEmailContent(),entities);
	}

    @Override
    public void batchUpdateSalaryGroupEntitiesVisable(BatchUpdateSalaryGroupEntitiesVisableCommand cmd) {
        for (UpdateSalaryGroupEntitiesVisableCommand cmd1 : cmd.getSalaryGroupCmd()) {
            updateSalaryGroupEntitiesVisable(cmd1);
        }
    }

    @Override
    public Object listPeriodSalaryEmailContents(ListPeriodSalaryEmailContentsCommand cmd) {
        return null;
    }

    @Override
	public void setSalaryEmailContent(SetSalaryEmailContentCommand cmd) {
		//TODO: email content 应该跟着批次走
		if(cmd.getSalaryGroupId() == null){
			salaryGroupProvider.updateSalaryGroupEmailContent(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getEmailContent());
		}else {
			SalaryGroup periodGroup = salaryGroupProvider.findSalaryGroupById(cmd.getSalaryGroupId());
			periodGroup.setEmailContent(cmd.getEmailContent());
			salaryGroupProvider.updateSalaryGroup(periodGroup);
		}
	}

	@Override
	public void updateSalaryGroupEntitiesVisable(UpdateSalaryGroupEntitiesVisableCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			cmd.getSalaryGroupEntities().stream().map(r ->{
				salaryGroupEntityProvider.updateSalaryGroupEntityVisible(r.getId(),r.getVisibleFlag());
				return null;
			});
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
	private void sendSalary(SalaryGroup salaryGroup) {
		List<SalaryEmployee> employees = salaryEmployeeProvider.listSalaryEmployeeByPeriodGroupId(salaryGroup.getId());
		for (SalaryEmployee employee : employees) {
			List<SalaryGroupEntity> groupEntities = salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(salaryGroup.getOrganizationGroupId());
			List<SalaryEmployeePeriodVal> employeeEntityVals = salaryEmployeePeriodValProvider.listSalaryEmployeePeriodVals(employee.getId());
			String entityTable = processEntityTableString(groupEntities, employeeEntityVals);
			//TODO: 人事档案给接口
			String toAddress = "";
			String emailSubject = "";
			sendSalaryEmail(salaryGroup.getNamespaceId(),toAddress, emailSubject,salaryGroup.getEmailContent(), entityTable);
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
			String Content = localeTemplateService.getLocaleTemplateString(SalaryConstants.SEND_MAIL_SCOPE,
					SalaryConstants.SEND_MAIL_CODE, "zh-CN", map, "");
			handler.sendMail(namespaceId, null,toAddress, emailSubject,Content);

		}catch (Exception e){
			LOGGER.debug("had a error in send message !!!!!++++++++++++++++++++++",e);
		}
	}

	private String processEntityTableString(List<SalaryGroupEntity> groupEntities, List<SalaryEmployeePeriodVal> employeeEntityVals) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		if(null != groupEntities){
			sb.append("<tr>");
			groupEntities.stream().map(r->{
				sb.append("<th>");
				sb.append(r.getName());
				sb.append("</th>");
				return null;
			});
			sb.append("</tr>");
			sb.append("<tr>");
			groupEntities.stream().map(r->{
				sb.append("<tr>");
				SalaryEmployeePeriodVal val = getSalaryEmployeePeriodVal(r.getId(),employeeEntityVals);
				if(null != val)
					sb.append(val.getSalaryValue());
				sb.append("</tr>");
				return null;
			});
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
	public void monthScheduled(){
        Calendar periodCalendar = Calendar.getInstance();
        periodCalendar.add(Calendar.MONTH,-1);
        String period = monthSF.get().format(periodCalendar.getTime());
		//TODO : 1.获取所有的薪酬组
		List<Organization> salaryOrganizations = null;
		for (Organization salaryOrg : salaryOrganizations) {
			SalaryGroup salaryGroup = new SalaryGroup();
			salaryGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			salaryGroup.setSalaryPeriod(period);
			salaryGroup.setOrganizationGroupId(salaryOrg.getId());
			salaryGroup.setNamespaceId(salaryOrg.getNamespaceId());
			salaryGroup.setOwnerType("organization");
			salaryGroup.setOwnerId(punchService.getTopEnterpriseId(salaryOrg.getDirectlyEnterpriseId()));
			salaryGroup.setStatus(SalaryGroupStatus.UNCHECK.getCode());
			salaryGroupProvider.createSalaryGroup(salaryGroup);
			//TODO : 2.循环薪酬组取里面的人员
			List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(salaryOrg.getId());
			List<Long> userIds = null;
			for (Long userId : userIds) {
				SalaryEmployee employee = ConvertHelper.convert(salaryGroup, SalaryEmployee.class);
				employee.setSalaryGroupId(salaryGroup.getId());
				salaryEmployeeProvider.createSalaryEmployee(employee);
				//  获取个人的项目字段
				List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(employee.getOwnerType(), employee.getOwnerId(), userId);
                processSalaryEmployeeOriginValsBeforeCalculate(salaryGroupEntities, salaryEmployeeOriginVals,userId);
                List<SalaryEmployeePeriodVal> salaryEmployeePeriodVals = new ArrayList<>();
                //3.循环人员搞vals
				for (SalaryGroupEntity entity : salaryGroupEntities) {
					SalaryEmployeePeriodVal val = new SalaryEmployeePeriodVal();
					val.setOwnerType(entity.getOwnerType());
					val.setOwnerId(entity.getOwnerId());
					val.setGroupEntityId(entity.getId());
					val.setSalaryEmployeeId(employee.getId());
					if(null != salaryEmployeeOriginVals && !(entity.getNumberType()!= null
                            && entity.getNumberType().equals(SalaryEntityNumberType.FORMULA.getCode()))){
						SalaryEmployeeOriginVal originVal = findOriginVal(entity.getId(), salaryEmployeeOriginVals);
                        val.setSalaryValue(originVal.getSalaryValue());
					}
                    salaryEmployeePeriodVals.add(val);
                }
				processSalaryEmployeePeriodVals(salaryGroupEntities,salaryEmployeeOriginVals,salaryEmployeePeriodVals);
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
        if(null == salaryEmployeeOriginVals){
            salaryEmployeeOriginVals = new ArrayList<>();
            for (SalaryGroupEntity entity : salaryGroupEntities) {
                SalaryEmployeeOriginVal val = ConvertHelper.convert(entity, SalaryEmployeeOriginVal.class);
                val.setGroupEntityId(entity.getId());
                val.setGroupEntityName(entity.getName());
                val.setUserId(userId);
                if (entity.getType().equals(SalaryEntityType.TEXT) || entity.getNumberType().equals(SalaryEntityNumberType.VALUE.getCode())) {
                    val.setSalaryValue(entity.getDefaultValue());
                }
                salaryEmployeeOriginVals.add(val);
            }
        }
    }
    /**
     * 计算 period Vals 的值
     * */
	private void processSalaryEmployeePeriodVals( List<SalaryGroupEntity> salaryGroupEntities,
                                                  List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals,
                                                  List<SalaryEmployeePeriodVal> salaryEmployeePeriodVals) {

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
                        templateLoader.putTemplate(templateKey, format);
                        freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
                        String evalString = "result = " + FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, valueMap);
                        if(!evalString.contains("${")){
                            //如果没有${ 说明全部参数都替换成了数字 则进行计算
                            ScriptEngineManager manager = new ScriptEngineManager();
                            ScriptEngine engine = manager.getEngineByName("javascript");
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
		Organization org = organizationProvider.findOrganizationById(punchService.getTopEnterpriseId(cmd.getOrganizationId()));
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
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 12));
        XSSFCellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //  创建标题
        createXSSFSalaryHistoryHead(sheet, style);

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
            row.createCell(++i).setCellValue(dto.getPaidMoney().toString());

        }
    }

    private void createXSSFSalaryHistoryHead(XSSFSheet sheet,  XSSFCellStyle style) {
        int rowNum = 0;

        XSSFRow row = sheet.createRow(rowNum++);
        row.setRowStyle(style);
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


}
