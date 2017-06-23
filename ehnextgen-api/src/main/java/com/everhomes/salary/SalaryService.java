// @formatter:off
package com.everhomes.salary;

import com.everhomes.rest.salary.*;

import java.util.List;

public interface SalaryService {


	public ListSalaryDefaultEntitiesResponse listSalaryDefaultEntities();


	public AddSalaryGroupResponse addSalaryGroup(AddSalaryGroupCommand cmd);


	public UpdateSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd);


	public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd);


	public void copySalaryGroup(CopySalaryGroupCommand cmd);


    public GetSalaryGroupResponse getSalaryGroup(GetSalaryGroupCommand cmd);


    public ListSalaryGroupResponse listSalaryGroup();


	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd);


	public List<SalaryEmployeeOriginValDTO> getSalaryEmployees(GetSalaryEmployeesCommand cmd);


	public void updateSalaryEmployees(UpdateSalaryEmployeesCommand cmd);


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