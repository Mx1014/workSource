// @formatter:off
package com.everhomes.salary;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.organization.*;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.salary.*;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.sun.xml.ws.wsdl.writer.document.Import;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.elasticsearch.common.recycler.Recycler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
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
            //	添加批次至组织结构
            //	this.organizationService.xxxxx
            response.setSalaryGroupEntry(cmd.getSalaryGroupEntity().stream().map(r -> {
                SalaryGroupEntity entity = new SalaryGroupEntity();
                if (!StringUtils.isEmpty(cmd.getOwnerType()))
                    entity.setOwnerType(cmd.getOwnerType());
                if (!StringUtils.isEmpty(cmd.getOwnerId()))
                    entity.setOwnerId(cmd.getOwnerId());
                entity.setGroupId(r.getGroupId());
                entity.setOriginEntityId(r.getOriginEntityId());
                entity.setType(r.getType());
                entity.setCategoryId(r.getCategoryId());
                entity.setCategoryName(r.getCategoryName());
                entity.setName(r.getName());
                entity.setEditableFlag(r.getEditableFlag());
                entity.setTemplateName(r.getTemplateName());
                entity.setNumberType(r.getNumberType());
                if (!StringUtils.isEmpty(r.getDefaultValue()))
                    entity.setDefaultValue(r.getDefaultValue());
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
//        List<SalaryGroupEntity> entities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
        if(!StringUtils.isEmpty(cmd.getSalaryGroupId())){
            this.salaryGroupEntityProvider.deleteSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
            AddSalaryGroupCommand addCommand = new AddSalaryGroupCommand();
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
//            this.organizationService.deletexxx(cmd.getSalaryGroupId());

            //  删除薪酬组定义的字段
            this.salaryGroupEntityProvider.deleteSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());

            //  删除个人设定中与薪酬组相关的字段
            this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupId(cmd.getSalaryGroupId());
        }
    }

	@Override
	public void copySalaryGroup(CopySalaryGroupCommand cmd){
        List<SalaryGroupEntity> origin = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
        AddSalaryGroupCommand addCommand = new AddSalaryGroupCommand();
        addCommand.setSalaryGroupEntity(origin.stream().map(r ->{
            SalaryGroupEntityDTO dto = ConvertHelper.convert(r,SalaryGroupEntityDTO.class);
/*            dto.setGroupId(r.getGroupId());
            dto.setOriginEntityId(r.getOriginEntityId());
            dto.setType(r.getType());
            dto.setCategoryId(r.getCategoryId());
            dto.setCategoryName(r.getCategoryName());
            dto.setN*/
            return dto;
        }).collect(Collectors.toList()));
        addCommand.setOwnerId(origin.get(0).getOwnerId());
        addCommand.setOwnerType(origin.get(0).getOwnerType());
        int i = 1;
        addCommand.setSalaryGroupName(cmd.getSalaryGroupName() + " (" + i + ")");
        this.addSalaryGroup(addCommand);
	}

    @Override
    public GetSalaryGroupResponse getSalaryGroup(GetSalaryGroupCommand cmd) {
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {
            GetSalaryGroupResponse response = new GetSalaryGroupResponse();
            // 从组织架构获取名称
//            response.setSalaryGroupName("");
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
    public ListSalaryGroupResponse listSalaryGroup(){

	    return new ListSalaryGroupResponse();
    }


    @Override
	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd) {
        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setKeywords(cmd.getKeywords());
        command.setOrganizationId(cmd.getOrganizationId());
/*        command
        List<OrganizationMember> members = this.organizationService.listOrganizationPersonnels()*/
        ListSalaryEmployeesResponse response = new ListSalaryEmployeesResponse();


		return new ListSalaryEmployeesResponse();
	}

    @Override
    public List<SalaryEmployeeOriginValDTO> getSalaryEmployees(GetSalaryEmployeesCommand cmd) {

        List<SalaryEmployeeOriginValDTO> results = new ArrayList<>();
        //  获取对应批次的项目字段
        List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
        //  获取个人的项目字段
        List<SalaryEmployeeOriginVal> salaryEmployeeOriginVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(cmd.getUserId());

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
            List<SalaryEmployeeOriginVal> originVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(userId);
            if(originVals.isEmpty()){
                cmd.getEmployeeOriginVal().stream().forEach(r -> {
                    this.createSalaryEmployeeOriginVal(r, cmd);
                });
            }else{
                this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupIdUserId(groupId, userId);
                cmd.getEmployeeOriginVal().stream().forEach(s ->{
                    this.createSalaryEmployeeOriginVal(s,cmd);
                });
            }
        }
/*        if (!cmd.getEmployeeOriginVal().isEmpty()) {
            //  获取用户id
            Long userId = cmd.getEmployeeOriginVal().get(0).getUserId();

            List<SalaryEmployeeOriginVal> originVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(userId);

            //  若用户没有个人薪酬设定时直接添加
            if (originVals.isEmpty()) {
                cmd.getEmployeeOriginVal().stream().forEach(s -> {
                    this.createSalaryEmployeeOriginVal(s, cmd);
                });
            } else {
                cmd.getEmployeeOriginVal().stream().forEach(t -> {
                    boolean isCreate = true;
                    for (SalaryEmployeeOriginVal originVal : originVals) {
                        if (t.getGroupEntityId().equals(originVal.getGroupEntityId())) {
                            originVal.setSalaryValue(t.getSalaryValue());
                            originVal.setCreatorUid(user.getId());
                            originVal.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                            this.salaryEmployeeOriginValProvider.updateSalaryEmployeeOriginVal(originVal);
                            isCreate = false;
                        }
                    }
                    if(isCreate)
                        this.createSalaryEmployeeOriginVal(t,cmd);
                });
            }
        }*/
    }

    private void createSalaryEmployeeOriginVal(SalaryEmployeeOriginValDTO dto, UpdateSalaryEmployeesCommand cmd){
        SalaryEmployeeOriginVal originVal = new SalaryEmployeeOriginVal();
        if (StringUtils.isEmpty(cmd.getOwnerType()))
            originVal.setOwnerType(cmd.getOwnerType());
        if (StringUtils.isEmpty(cmd.getOwnerId()))
            originVal.setOwnerId(cmd.getOwnerId());
        originVal.setGroupId(dto.getSalaryGroupId());
        originVal.setUserId(dto.getUserId());
        originVal.setGroupEntityId(dto.getGroupEntityId());
        originVal.setGroupEntityName(dto.getGroupEntityName());
        originVal.setOriginEntityId(dto.getOriginEntityId());
        originVal.setSalaryValue(dto.getSalaryValue());
        this.salaryEmployeeOriginValProvider.createSalaryEmployeeOriginVal(originVal);
    }

    @Override
    public void exportSalaryGroup(ExportSalaryGroupCommand cmd, HttpServletResponse httpServletResponse) {
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {

            //  根据批次 id 查找批次具体内容
            GetSalaryGroupCommand command = new GetSalaryGroupCommand();
            command.setSalaryGroupId(cmd.getSalaryGroupId());
            GetSalaryGroupResponse response = this.getSalaryGroup(command);

            ByteArrayOutputStream out = null;
            XSSFWorkbook workbook = this.creatXSSFSalaryGroupFile(response);
            try {
                out = new ByteArrayOutputStream();
                workbook.write(out);
                DownloadUtil.download(out, httpServletResponse);
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

    }

    private XSSFWorkbook creatXSSFSalaryGroupFile(GetSalaryGroupResponse response){

        List<SalaryGroupEntityDTO> entityDTOs = response.getSalaryGroupEntity();

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
        rowTitle.createCell(0).setCellValue("对应批次");
        rowTitle.setRowStyle(titleStyle);

        XSSFRow row = sheet.createRow(rowNum++);
        row.setRowStyle(style);

        //  创建模板标题
        for (int i = 0; i < entityDTOs.size(); i++) {
            row.createCell(i).setCellValue(entityDTOs.get(i).getName());
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
//            task.setOwnerType();
//            task.setOwnerId();
            task.setType(ImportFileTaskType.SALARY_GROUP.getCode());
            task.setCreatorUid(userId);
            task = this.importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportSalaryEmployeeOriginValDTO> datas = handleImportSalaryGroupFiles(resultList,cmd.getSalaryGroupId());
                    if (datas.size() > 0) {
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }
                    List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> results = importSalaryGroupFiles(datas, userId, cmd);
                    response.setTotalCount((long) datas.size());
                    response.setFailCount((long) results.size());
                    response.setLogs(results);
                    return response;

//                    return null;
                }
            },task);
        }catch (Exception e){

        }
        return null;
	}

	private List<ImportSalaryEmployeeOriginValDTO> handleImportSalaryGroupFiles(List list, Long groupId){

//        List<Map<String,String>> datas = new ArrayList<>();
//        List<Map<String,String>> maps = new ArrayList<>();
        List<ImportSalaryEmployeeOriginValDTO> datas = new ArrayList<>();
        List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(groupId);

        salaryGroupEntities.add(0,new SalaryGroupEntity("姓名"));
        salaryGroupEntities.add(1,new SalaryGroupEntity("手机号"));

        for(int i=1; i<list.size(); i++){
            ImportSalaryEmployeeOriginValDTO data = new ImportSalaryEmployeeOriginValDTO();
            List<String> vals = new ArrayList<>();

            for(int j=0; j<salaryGroupEntities.size(); j++){
                RowResult r = (RowResult) list.get(i);
/*                Map<String,String> map = new HashMap<>();

                map.put(salaryGroupEntities.get(j).getName(),r.getCells().get(GetExcelLetter(j+1)));
                maps.add(map);*/
                String val = r.getCells().get(GetExcelLetter(j+1));
                vals.add(val);
            }
            data.setSalaryEmployeeVal(vals);
            datas.add(data);
        }
        return datas;
    }

    private List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> importSalaryGroupFiles(List<ImportSalaryEmployeeOriginValDTO> datas, Long userId, ImportSalaryGroupCommand cmd){

	    ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> log = new ImportFileResultLog<>(SalaryServiceErrorCode.SCOPE);
        List<ImportFileResultLog<ImportSalaryEmployeeOriginValDTO>> errorDataLogs = new ArrayList<>();

        for ( ImportSalaryEmployeeOriginValDTO data : datas){
	        log = this.checkSalaryGroup(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }

            this.saveSalaryGroup(data,cmd.getSalaryGroupId(),cmd.getOrganizationId());
        }
        return errorDataLogs;

    }

    private ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> checkSalaryGroup(ImportSalaryEmployeeOriginValDTO data){
        ImportFileResultLog<ImportSalaryEmployeeOriginValDTO> log = new ImportFileResultLog<>(SalaryServiceErrorCode.SCOPE);
        if(StringUtils.isEmpty(data.getSalaryEmployeeVal().get(0))){
            LOGGER.warn("Organization member contactName is null. data = {}", data);
            log.setData(data);
            log.setErrorLog("Organization member contactName is null");
            log.setCode(SalaryServiceErrorCode.ERROR_CONTACTNAME_ISNULL);
            return log;
        }
        return null;
    }

    private void saveSalaryGroup(ImportSalaryEmployeeOriginValDTO data, Long groupId, Long organizationId){

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
		//TODO:
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
		//1.查entities
		
		//2.查人员 periodGroupId 可以确定:公司,薪酬组和期数
		List<SalaryEmployee> result = salaryEmployeeProvider.listSalaryEmployeeByPeriodGroupId(cmd.getSalaryPeriodGroupId());
		ListPeriodSalaryEmployeesResponse response = new ListPeriodSalaryEmployeesResponse();
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
		
		//3.查人员的vals
		List<SalaryEmployeePeriodVal> result = salaryEmployeePeriodValProvider.listSalaryEmployeePeriodVals(r.getId());
		if(null == result)
			return null;
		dto.setPeriodEmployeeEntitys(result.stream().map(r2 ->{	
			SalaryPeriodEmployeeEntityDTO dto2 = processSalaryPeriodEmployeeEntityDTO(r2);
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
			cmd.getPeriodEmployeeEntities().stream().map(r ->{
				salaryEmployeePeriodValProvider.updateSalaryEmployeePeriodVal(r.getSalaryEmployeeId(),r.getGroupEntryId(),r.getSalaryValue());
				return null;
			});
			SalaryEmployee salaryEmployee = salaryEmployeeProvider.findSalaryEmployeeById(cmd.getSalaryEmployeeId());
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
	public void exportSalarySendHistory(ExportSalarySendHistoryCommand cmd) {
	

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
