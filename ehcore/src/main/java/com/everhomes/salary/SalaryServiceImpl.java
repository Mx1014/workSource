// @formatter:off
package com.everhomes.salary;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.mail.MailHandler;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.salary.*;

import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalaryServiceImpl implements SalaryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SalaryServiceImpl.class);
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ConfigurationProvider configProvider;

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
	    if(!cmd.getSalaryGroupEntity().isEmpty()){
        }
	
		return new AddSalaryGroupResponse();
	}

	@Override
	public UpdateSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd) {
	
		return new UpdateSalaryGroupResponse();
	}

	@Override
	public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd) {
	

	}

	@Override
	public void copySalaryGroup(CopySalaryGroupCommand cmd){


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
				dto.setEntityName(r.getName());

				//  为对应字段赋值
				if (!salaryEmployeeOriginVals.isEmpty()) {
					salaryEmployeeOriginVals.stream().forEach(s -> {
						if (r.getName().equals(s.getGroupEntityName()))
							dto.setSalaryValue(s.getSalaryValue());
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

    }

    @Override
	public void saveSalaryEmployeeOriginVals(SaveSalaryEmployeeOriginValsCommand cmd) {
	

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
		return new GetPeriodSalaryEmailContentResponse();
	}

	@Override
	public void setSalaryEmailContent(SetSalaryEmailContentCommand cmd) {
		//TODO: email content 应该跟着批次走

	}

	@Override
	public void updateSalaryGroupEntitiesVisable(UpdateSalaryGroupEntitiesVisableCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			cmd.getSalaryGroupEntities().stream().map(r ->{
				salaryGroupEntityProvider.updateSalaryGroupEntityVisible(r.getId(),r.getVisibleFlag());
				return null;
			});
		});
	}

	@Override
	public void sendPeriodSalary(SendPeriodSalaryCommand cmd) {
		//将本期group置为已核算
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

	}


	@Scheduled(cron = "1 0/15 * * * ?")
	private void sendSalaryScheduled(){
		sendSalary((new Timestamp(DateHelper.currentGMTTime().getTime()));
	}
	/**找一个时间点之前的待发送批次,并发送*/
	private void sendSalary(Timestamp date) {
		List<SalaryGroup> groups = salaryGroupProvider.listSalaryGroup(SalaryGroupStatus.WAIT_FOR_SEND.getCode(),date);
		for (SalaryGroup group : groups) {
			sendSalary(group);
			group.setStatus(SalaryGroupStatus.SENDED.getCode());
			salaryGroupProvider.updateSalaryGroup(group);
		}
	}
	/**给某个批次某期发薪酬邮件*/
	private void sendSalary(SalaryGroup salaryGroup) {

		try{
			String address = configProvider.getValue(UserContext.getCurrentNamespaceId(),"mail.smtp.address", "smtp.mxhichina.com");
			String passwod = configProvider.getValue(UserContext.getCurrentNamespaceId(),"mail.smtp.passwod", "abc123!@#");
			int port = configProvider.getIntValue(UserContext.getCurrentNamespaceId(),"mail.smtp.port", 25);
//			new Mailer(address, port , account , passwod).sendMail(email);
			//另一种发送方式
			String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
			MailHandler handler = PlatformContext.getComponent(handlerName);

			handler.sendMail(UserContext.getCurrentNamespaceId(), account,cmd.getEmail(), mailSubject, mailText);

		}catch (Exception e){
			LOGGER.debug("had a error in send message !!!!!++++++++++++++++++++++",e);
		}
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
