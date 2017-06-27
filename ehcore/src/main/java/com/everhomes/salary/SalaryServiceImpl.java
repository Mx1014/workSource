// @formatter:off
package com.everhomes.salary;

import com.everhomes.db.DbProvider;
import com.everhomes.rest.salary.*;

import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalaryServiceImpl implements SalaryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SalaryServiceImpl.class);
	@Autowired
	private DbProvider dbProvider;
	
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
	public UpdateSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd) {
        List<SalaryGroupEntity> entities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());


		return new UpdateSalaryGroupResponse();
	}

    @Override
    public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd) {
        if (!StringUtils.isEmpty(cmd.getSalaryGroupId())) {

            //  组织架构删除薪酬组
//            this.organizationService.deletexxx;
            List<SalaryGroupEntity> entities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
            //entity.setStatus();
            for (SalaryGroupEntity entity : entities) {
                //entity.setStatus();
                //  删除薪酬组定义的字段
                this.salaryGroupEntityProvider.deleteSalaryGroupEntity(entity);
            }
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
    public GetSalaryGroupResponse getSalaryGroup(GetSalaryGroupCommand cmd){
        if(StringUtils.isEmpty(cmd.getSalaryGroupId()))
            return null;
        GetSalaryGroupResponse response = new GetSalaryGroupResponse();
        List<SalaryGroupEntity> salaryGroupEntities = this.salaryGroupEntityProvider.listSalaryGroupEntityByGroupId(cmd.getSalaryGroupId());
	    response.setSalaryGroupEntity(salaryGroupEntities.stream().map(r -> {
            SalaryGroupEntityDTO dto = ConvertHelper.convert(r, SalaryGroupEntityDTO.class);
            return dto;
        }).collect(Collectors.toList()));
		return response;
    }

    @Override
    public ListSalaryGroupResponse listSalaryGroup(){

	    return new ListSalaryGroupResponse();
    }


    @Override
	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd) {

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
            List<SalaryEmployeeOriginVal> originVals = this.salaryEmployeeOriginValProvider.listSalaryEmployeeOriginValByUserId(userId);
            if(originVals.isEmpty()){
                cmd.getEmployeeOriginVal().stream().forEach(r -> {
                    this.createSalaryEmployeeOriginVal(r, cmd);
                });
            }else{
                this.salaryEmployeeOriginValProvider.deleteSalaryEmployeeOriginValByGroupId(userId);
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
	public void exportSalaryGroup(ExportSalaryGroupCommand cmd) {
	

	}

	@Override
	public void importSalaryGroup(ImportSalaryGroupCommand cmd) {
	

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
		Integer unLinkNumber = null;
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
			cmd.getPeriodEmployeeEntitys().stream().map(r ->{	
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
		
		return new GetPeriodSalaryEmailContentResponse();
	}

	@Override
	public void setSalaryEmailContent(SetSalaryEmailContentCommand cmd) {
	

	}

	@Override
	public void updateSalaryGroupEntitiesVisable(UpdateSalaryGroupEntitiesVisableCommand cmd) {
	

	}

	@Override
	public void sendPeriodSalary(SendPeriodSalaryCommand cmd) {
	

	}

	@Override
	public ListSalarySendHistoryResponse listSalarySendHistory(ListSalarySendHistoryCommand cmd) {
	
		return new ListSalarySendHistoryResponse();
	}

	@Override
	public void exportSalarySendHistory(ExportSalarySendHistoryCommand cmd) {
	

	}

	@Override
	public void batchSetEmployeeCheckFlag(BatchSetEmployeeCheckFlagCommand cmd) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void revokeSendPeriodSalary(SendPeriodSalaryCommand cmd) {

    }

}