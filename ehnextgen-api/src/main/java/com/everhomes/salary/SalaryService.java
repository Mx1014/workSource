// @formatter:off
package com.everhomes.salary;

import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.salary.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;

public interface SalaryService {

//	public ListSalaryContactResponse listSalaryContacts(ListOrganizationContactCommand cmd);
//
//
//	public ListSalaryDefaultEntitiesResponse listSalaryDefaultEntities();
//
//
//	public AddSalaryGroupResponse addSalaryGroup(AddSalaryGroupCommand cmd);
//
//
//	public AddSalaryGroupResponse updateSalaryGroup(UpdateSalaryGroupCommand cmd);
//
//
//	public void deleteSalaryGroup(DeleteSalaryGroupCommand cmd);
//
//
//	public void copySalaryGroup(CopySalaryGroupCommand cmd);
//
//
//    public GetSalaryGroupResponse getSalaryGroup(GetSalaryGroupCommand cmd);
//
//
//    public ListSalaryGroupResponse listSalaryGroup(ListSalaryGroupCommand cmd);
//
//
//	public ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd);
//
//
//	public List<SalaryEmployeeOriginValDTO> getSalaryEmployees(GetSalaryEmployeesCommand cmd);
//
//
//	public void updateSalaryEmployeesGroup(UpdateSalaryEmployeesGroupCommand cmd);
//
//
//	public void updateSalaryEmployees(UpdateSalaryEmployeesCommand cmd);
//
//
//	public void addToOrganizationSalaryGroup(AddToOrganizationSalaryGroupCommand cmd);
//
//
//    public String countAbnormalSalaryEmployees(CountAbnormalSalaryEmployees cmd);
//
//
//	public void exportSalaryGroup(ExportSalaryGroupCommand cmd, HttpServletResponse httpServletResponse);
//
//
//	public ImportFileTaskDTO importSalaryGroup(MultipartFile mfile, Long userId, Integer namespaceId, ImportSalaryInfoCommand cmd);
//
//
//	public void exportPeriodSalary(ExportPeriodSalaryCommand cmd, HttpServletResponse httpServletResponse);
//
//
//	public ImportFileTaskDTO importPeriodSalary(MultipartFile mfile, Long userId, Integer namespaceId, ImportSalaryInfoCommand cmd);
//
//
//	public ImportFileResponse<ImportSalaryEmployeeOriginValDTO> getImportFileResult(GetImportFileResultCommand cmd);
//
//
//	public void exportPeriodSalaryEmployees(ExportPeriodSalaryEmployeesCommand cmd, HttpServletResponse httpResponse);
//
//
//	public GetAbnormalEmployeeNumberResponse getAbnormalEmployeeNumber(GetAbnormalEmployeeNumberCommand cmd);
//
//
//	public ListPeriodSalaryResponse listPeriodSalary(ListPeriodSalaryCommand cmd);
//
//
//	public ListPeriodSalaryEmployeesResponse listPeriodSalaryEmployees(ListPeriodSalaryEmployeesCommand cmd);
//
//
//	public void updatePeriodSalaryEmployee(UpdatePeriodSalaryEmployeeCommand cmd);
//
//
//	public void checkPeriodSalary(CheckPeriodSalaryCommand cmd);
//
//
//	public GetPeriodSalaryEmailContentResponse getPeriodSalaryEmailContent(GetPeriodSalaryEmailContentCommand cmd);
//
//
//	public void setSalaryEmailContent(SetSalaryEmailContentCommand cmd);
//
//
//	public void updateSalaryGroupEntitiesVisable(UpdateSalaryGroupEntitiesVisableCommand cmd);
//
//
//	public void sendPeriodSalary(SendPeriodSalaryCommand cmd);
//
//
//	void monthScheduled(String period) ;
//
//	public ListSalarySendHistoryResponse listSalarySendHistory(ListSalarySendHistoryCommand cmd);
//
//
//	public void exportSalarySendHistory(ExportSalarySendHistoryCommand cmd, HttpServletResponse httpResponse);
//
//
//	public void batchSetEmployeeCheckFlag(BatchSetEmployeeCheckFlagCommand cmd);
//
//
//	public void revokeSendPeriodSalary(SendPeriodSalaryCommand cmd);
//
//	void batchUpdateSalaryGroupEntitiesVisable(BatchUpdateSalaryGroupEntitiesVisableCommand cmd);
//
//	Object listPeriodSalaryEmailContents(ListPeriodSalaryEmailContentsCommand cmd);
//
//
//	//  added by R, for salaryGroup 20170706
//	SalaryEmployeeDTO getPersonnelInfoByDetailIdForSalary(Long userId);

    String getDptPathNameByDetailId(Long detailId);

    ListEnterprisesResponse listEnterprises(ListEnterprisesCommand cmd);

    ListGroupEntitiesResponse listGroupEntities(ListGroupEntitiesCommand cmd);

    void updateGroupEntities(UpdateGroupEntitiesCommand cmd);

    ListSalaryEmployeesResponse listSalaryEmployees(ListSalaryEmployeesCommand cmd);

    String findNameByOwnerAndUser(Long ownerId, Long uid);

    HttpServletResponse exportEmployeeSalaryTemplate(ExportEmployeeSalaryTemplateCommand cmd,
                                                     HttpServletResponse response);

    GetEmployeeEntitiesResponse getEmployeeEntities(GetEmployeeEntitiesCommand cmd);

    void exportEmployeeSalary(ExportEmployeeSalaryTemplateCommand cmd);

    ImportFileTaskDTO importEmployeeSalary(ExportEmployeeSalaryTemplateCommand cmd, MultipartFile[] files);

    BigDecimal calculateBonusTax(BigDecimal bonus, BigDecimal salary);

    BigDecimal calculateSalaryTax(BigDecimal salary);

    ImportFileResponse getImportResult(GetImportFileResultCommand cmd);

    OutputStream getEmployeeSalaryOutPut(Long organizationId, Long taskId, Integer namespaceId);

    GetSalaryGroupStatusResponse getSalaryGroupStatus(GetSalaryGroupStatusCommand cmd);

    void exportSalaryReport(ExportSalaryReportCommand cmd);

    FileSalaryGroupResponse fileSalaryGroup(FileSalaryGroupCommand cmd);

    void newSalaryMonth(NewSalaryMonthCommand cmd);

    OutputStream getSalaryDetailsOutPut(Long ownerId, String month, Long taskId, Integer namespaceId);

    OutputStream getDepartStatisticsOutPut(Long ownerId, String month, Long taskId, Integer namespaceId);

    GetSalaryTaskStatusResponse getSalaryTaskStatus(GetSalaryTaskStatusCommand cmd);

	public ListYearPayslipSummaryResponse listYearPayslipSummary(ListYearPayslipSummaryCommand cmd);


	public ImportPayslipResponse importPayslip(MultipartFile[] files, ImportPayslipCommand cmd);


	public SendPayslipResponse sendPayslip(SendPayslipCommand cmd);


	public ListMonthPayslipSummaryResponse listMonthPayslipSummary(ListMonthPayslipSummaryCommand cmd);


	public ListSendPayslipDetailsResponse listSendPayslipDetails(ListSendPayslipDetailsCommand cmd);


	public void resendPayslip(ResendPayslipCommand cmd);


	public void revokePayslip(RevokePayslipCommand cmd);


	public void deletePayslip(DeletePayslipCommand cmd);


	public ListUserPayslipsResponse listUserPayslips(ListUserPayslipsCommand cmd);


	public ListPayslipsDetailResponse listPayslipsDetail(ListPayslipsDetailCommand cmd);


	public void confirmPayslip(ConfirmPayslipCommand cmd);

}