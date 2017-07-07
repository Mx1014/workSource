// @formatter:off
package com.everhomes.salary;

import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.salary.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

public interface SalaryService {


	public ListSalaryDefaultEntitiesResponse listSalaryDefaultEntities();


	public AddSalaryGroupResponse addSalaryGroup(AddSalaryGroupCommand cmd);


	public AddSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd);


	public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd);


	public void copySalaryGroup(CopySalaryGroupCommand cmd);


    public GetSalaryGroupResponse getSalaryGroup(GetSalaryGroupCommand cmd);


    public ListSalaryGroupResponse listSalaryGroup(ListSalaryGroupCommand cmd);


	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd);


	public List<SalaryEmployeeOriginValDTO> getSalaryEmployees(GetSalaryEmployeesCommand cmd);


	public void updateSalaryEmployeesGroup(UpdateSalaryEmployeesGroupCommand cmd);


	public void updateSalaryEmployees(UpdateSalaryEmployeesCommand cmd);


	public void addToOrganizationSalaryGroup(AddToOrganizationSalaryGroupCommand cmd);


	public void exportSalaryGroup(ExportSalaryGroupCommand cmd, HttpServletResponse httpServletResponse);


	public ImportFileTaskDTO importSalaryGroup(MultipartFile mfile, Long userId, ImportSalaryGroupCommand cmd);


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


	void monthScheduled(String period) ;

	public ListSalarySendHistoryResponse listSalarySendHistory(ListSalarySendHistoryCommand cmd);


	public void exportSalarySendHistory(ExportSalarySendHistoryCommand cmd, HttpServletResponse httpResponse);


	public void batchSetEmployeeCheckFlag(BatchSetEmployeeCheckFlagCommand cmd);


	public void revokeSendPeriodSalary(SendPeriodSalaryCommand cmd);

	void batchUpdateSalaryGroupEntitiesVisable(BatchUpdateSalaryGroupEntitiesVisableCommand cmd);

	Object listPeriodSalaryEmailContents(ListPeriodSalaryEmailContentsCommand cmd);


	//  added by R, for salaryGroup 20170706
	SalaryEmployeeDTO getPersonnelInfoByUserIdForSalary(Long userId);
}