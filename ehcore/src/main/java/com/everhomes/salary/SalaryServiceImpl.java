// @formatter:off
package com.everhomes.salary;

import com.everhomes.rest.salary.*;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    private SalaryDefaultEntityProvider salaryDefaultEntityProvider;

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
    public ListSalaryGroupResponse listSalaryGroup(){

	    return new ListSalaryGroupResponse();
    }


    @Override
	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd) {
	
		return new ListSalaryEmployeesResponse();
	}

    @Override
    public SalaryEmployeeOriginValDTO updateSalaryEmployees(UpdateSalaryEmployeesCommand cmd) {
        return new SalaryEmployeeOriginValDTO();
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
	
		return new GetAbnormalEmployeeNumberResponse();
	}

	@Override
	public ListPeriodSalaryResponse listPeriodSalary(ListPeriodSalaryCommand cmd) {
	
		return new ListPeriodSalaryResponse();
	}

	@Override
	public ListPeriodSalaryEmployeesResponse listPeriodSalaryEmployees(ListPeriodSalaryEmployeesCommand cmd) {
	
		return new ListPeriodSalaryEmployeesResponse();
	}

	@Override
	public void updatePeriodSalaryEmployee(UpdatePeriodSalaryEmployeeCommand cmd) {
	

	}

	@Override
	public void checkPeriodSalary(CheckPeriodSalaryCommand cmd) {
	

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