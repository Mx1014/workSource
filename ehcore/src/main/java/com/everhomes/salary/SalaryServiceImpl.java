// @formatter:off
package com.everhomes.salary;

import org.springframework.stereotype.Component;

import com.everhomes.rest.salary.AddSalaryGroupCommand;
import com.everhomes.rest.salary.AddSalaryGroupResponse;
import com.everhomes.rest.salary.BatchSetEmployeeCheckFlagCommand;
import com.everhomes.rest.salary.CheckPeriodSalaryCommand;
import com.everhomes.rest.salary.DeleteSalaryGroupCommand;
import com.everhomes.rest.salary.ExportPeriodSalaryCommand;
import com.everhomes.rest.salary.ExportSalaryGroupCommand;
import com.everhomes.rest.salary.ExportSalarySendHistoryCommand;
import com.everhomes.rest.salary.GetAbnormalEmployeeNumberCommand;
import com.everhomes.rest.salary.GetAbnormalEmployeeNumberResponse;
import com.everhomes.rest.salary.GetPeriodSalaryEmailContentCommand;
import com.everhomes.rest.salary.GetPeriodSalaryEmailContentResponse;
import com.everhomes.rest.salary.ImportPeriodSalaryCommand;
import com.everhomes.rest.salary.ImportSalaryGroupCommand;
import com.everhomes.rest.salary.ListPeriodSalaryCommand;
import com.everhomes.rest.salary.ListPeriodSalaryEmployeesCommand;
import com.everhomes.rest.salary.ListPeriodSalaryEmployeesResponse;
import com.everhomes.rest.salary.ListPeriodSalaryResponse;
import com.everhomes.rest.salary.ListSalaryDefaultEntitiesResponse;
import com.everhomes.rest.salary.ListSalarySendHistoryCommand;
import com.everhomes.rest.salary.ListSalarySendHistoryResponse;
import com.everhomes.rest.salary.ListSalaryEmployeesCommand;
import com.everhomes.rest.salary.ListSalaryEmployeesResponse;
import com.everhomes.rest.salary.SaveSalaryEmployeeOriginValsCommand;
import com.everhomes.rest.salary.SendPeriodSalaryCommand;
import com.everhomes.rest.salary.SetSalaryEmailContentCommand;
import com.everhomes.rest.salary.UpdatePeriodSalaryEmployeeCommand;
import com.everhomes.rest.salary.UpdateSalaryGroupCommand;
import com.everhomes.rest.salary.UpdateSalaryGroupEntitiesVisableCommand;
import com.everhomes.rest.salary.UpdateSalaryGroupResponse;

@Component
public class SalaryServiceImpl implements SalaryService {

	@Override
	public ListSalaryDefaultEntitiesResponse listSalaryDefaultEntities() {
	
		return new ListSalaryDefaultEntitiesResponse();
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
	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd) {
	
		return new ListSalaryEmployeesResponse();
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

}