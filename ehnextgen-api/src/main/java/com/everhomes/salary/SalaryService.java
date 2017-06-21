// @formatter:off
package com.everhomes.salary;

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

public interface SalaryService {


	public ListSalaryDefaultEntitiesResponse listSalaryDefaultEntities();


	public AddSalaryGroupResponse addSalaryGroup(AddSalaryGroupCommand cmd);


	public UpdateSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd);


	public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd);


	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd);


	public void saveSalaryEmployeeOriginVals(SaveSalaryEmployeeOriginValsCommand cmd);


	public void exportSalaryGroup(ExportSalaryGroupCommand cmd);


	public void importSalaryGroup(ImportSalaryGroupCommand cmd);


	public void exportPeriodSalary(ExportPeriodSalaryCommand cmd);


	public void importPeriodSalary(ImportPeriodSalaryCommand cmd);


	public GetAbnormalEmployeeNumberResponse getAbnormalEmployeeNumber(GetAbnormalEmployeeNumberCommand cmd);


	public ListPeriodSalaryResponse listPeriodSalary(ListPeriodSalaryCommand cmd);


	public ListPeriodSalaryEmployeesResponse listPeriodSalaryEmployees(ListPeriodSalaryEmployeesCommand cmd);


	public void updatePeriodSalaryEmployee(UpdatePeriodSalaryEmployeeCommand cmd);


	public void checkPeriodSalary(CheckPeriodSalaryCommand cmd);


	public GetPeriodSalaryEmailContentResponse getPeriodSalaryEmailContent(GetPeriodSalaryEmailContentCommand cmd);


	public void setSalaryEmailContent(SetSalaryEmailContentCommand cmd);


	public void updateSalaryGroupEntitiesVisable(UpdateSalaryGroupEntitiesVisableCommand cmd);


	public void sendPeriodSalary(SendPeriodSalaryCommand cmd);


	public ListSalarySendHistoryResponse listSalarySendHistory(ListSalarySendHistoryCommand cmd);


	public void exportSalarySendHistory(ExportSalarySendHistoryCommand cmd);


	public void batchSetEmployeeCheckFlag(BatchSetEmployeeCheckFlagCommand cmd);


	public void revokeSendPeriodSalary(SendPeriodSalaryCommand cmd);

}