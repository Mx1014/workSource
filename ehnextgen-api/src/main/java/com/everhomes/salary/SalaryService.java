// @formatter:off
package com.everhomes.salary;

import com.everhomes.rest.salary.AddSalaryGroupCommand;
import com.everhomes.rest.salary.AddSalaryGroupResponse;
import com.everhomes.rest.salary.CheckPeriodSalaryCommand;
import com.everhomes.rest.salary.DeleteSalaryGroupCommand;
import com.everhomes.rest.salary.ExportPeriodSalaryCommand;
import com.everhomes.rest.salary.ExportSalaryGroupCommand;
import com.everhomes.rest.salary.ExportSalarySendHistoryCommand;
import com.everhomes.rest.salary.GetAbnormalStaffNumberCommand;
import com.everhomes.rest.salary.GetAbnormalStaffNumberResponse;
import com.everhomes.rest.salary.GetPeriodSalaryEmailContentCommand;
import com.everhomes.rest.salary.GetPeriodSalaryEmailContentResponse;
import com.everhomes.rest.salary.ImportPeriodSalaryCommand;
import com.everhomes.rest.salary.ImportSalaryGroupCommand;
import com.everhomes.rest.salary.ListPeriodSalaryCommand;
import com.everhomes.rest.salary.ListPeriodSalaryEmployeesCommand;
import com.everhomes.rest.salary.ListPeriodSalaryEmployeesResponse;
import com.everhomes.rest.salary.ListPeriodSalaryResponse;
import com.everhomes.rest.salary.ListSalaryDefaultEntriesResponse;
import com.everhomes.rest.salary.ListSalarySendHistoryCommand;
import com.everhomes.rest.salary.ListSalarySendHistoryResponse;
import com.everhomes.rest.salary.ListSalaryStaffsCommand;
import com.everhomes.rest.salary.ListSalaryStaffsResponse;
import com.everhomes.rest.salary.SaveSalaryEmployeeOriginValsCommand;
import com.everhomes.rest.salary.SendPeriodSalaryCommand;
import com.everhomes.rest.salary.SetSalaryEmailContentCommand;
import com.everhomes.rest.salary.UpdatePeriodSalaryEmployeeCommand;
import com.everhomes.rest.salary.UpdateSalaryGroupCommand;
import com.everhomes.rest.salary.UpdateSalaryGroupEntriesVisableCommand;
import com.everhomes.rest.salary.UpdateSalaryGroupResponse;

public interface SalaryService {


	public ListSalaryDefaultEntriesResponse listSalaryDefaultEntries();


	public AddSalaryGroupResponse addSalaryGroup(AddSalaryGroupCommand cmd);


	public UpdateSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd);


	public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd);


	public ListSalaryStaffsResponse listSalaryStaffs(ListSalaryStaffsCommand cmd);


	public void saveSalaryEmployeeOriginVals(SaveSalaryEmployeeOriginValsCommand cmd);


	public void exportSalaryGroup(ExportSalaryGroupCommand cmd);


	public void importSalaryGroup(ImportSalaryGroupCommand cmd);


	public void exportPeriodSalary(ExportPeriodSalaryCommand cmd);


	public void importPeriodSalary(ImportPeriodSalaryCommand cmd);


	public GetAbnormalStaffNumberResponse getAbnormalStaffNumber(GetAbnormalStaffNumberCommand cmd);


	public ListPeriodSalaryResponse listPeriodSalary(ListPeriodSalaryCommand cmd);


	public ListPeriodSalaryEmployeesResponse listPeriodSalaryEmployees(ListPeriodSalaryEmployeesCommand cmd);


	public void updatePeriodSalaryEmployee(UpdatePeriodSalaryEmployeeCommand cmd);


	public void checkPeriodSalary(CheckPeriodSalaryCommand cmd);


	public GetPeriodSalaryEmailContentResponse getPeriodSalaryEmailContent(GetPeriodSalaryEmailContentCommand cmd);


	public void setSalaryEmailContent(SetSalaryEmailContentCommand cmd);


	public void updateSalaryGroupEntriesVisable(UpdateSalaryGroupEntriesVisableCommand cmd);


	public void sendPeriodSalary(SendPeriodSalaryCommand cmd);


	public ListSalarySendHistoryResponse listSalarySendHistory(ListSalarySendHistoryCommand cmd);


	public void exportSalarySendHistory(ExportSalarySendHistoryCommand cmd);

}