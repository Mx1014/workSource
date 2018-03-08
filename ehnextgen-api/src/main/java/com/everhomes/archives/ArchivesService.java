package com.everhomes.archives;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.organization.GetImportFileResultCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import org.jooq.Condition;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ArchivesService {

    ArchivesContactDTO addArchivesContact(AddArchivesContactCommand cmd);

    void transferArchivesContacts(TransferArchivesContactsCommand cmd);

    void deleteArchivesContacts(DeleteArchivesContactsCommand cmd);

    void stickArchivesContact(StickArchivesContactCommand cmd);

//    ArchivesContactDTO getArchivesContact(ArchivesIdCommand cmd);

    ListArchivesContactsResponse listArchivesContacts(ListArchivesContactsCommand cmd);

    ImportFileTaskDTO importArchivesContacts(
            MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesContactsCommand cmd);

    void exportArchivesContacts(ExportArchivesContactsCommand cmd, HttpServletResponse httpResponse);

    void verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd);

    ImportFileResponse<ImportArchivesContactsDTO> getImportContactsResult(GetImportFileResultCommand cmd);

    void exportImportFileFailResults(GetImportFileResultCommand cmd, HttpServletResponse httpResponse);

    ListArchivesEmployeesResponse listArchivesEmployees(ListArchivesEmployeesCommand cmd);

    //  获取员工在企业的真实名称
    String getEmployeeRealName(Long userId, Long organizationId);

    //  获取员工的部门
    Map<Long, String> getEmployeeDepartment(Long detailId);

    //  获取员工的职位
    Map<Long, String> getEmployeeJobPosition(Long detailId);

    //  获取员工的职级
    Map<Long, String> getEmployeeJobLevel(Long detailId);

    //  转化员工的部门、职位、职级信息文本
    String convertToOrgNames(Map<Long, String> map);

    //  转化员工的部门、职位、职级id
    List<Long> convertToOrgIds(Map<Long, String> map);

    ArchivesEmployeeDTO addArchivesEmployee(AddArchivesEmployeeCommand cmd);

    void updateArchivesEmployee(UpdateArchivesEmployeeCommand cmd);

    GetArchivesEmployeeResponse getArchivesEmployee(GetArchivesEmployeeCommand cmd);

    List<ArchivesLogDTO> listArchivesLogs(Long organizationId, Long detailId);

    ListArchivesDismissEmployeesResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd);

    void executeArchivesConfiguration();

    void employArchivesEmployeesConfig(EmployArchivesEmployeesCommand cmd);

    void employArchivesEmployees(EmployArchivesEmployeesCommand cmd);

    void transferArchivesEmployeesConfig(TransferArchivesEmployeesCommand cmd);

    void transferArchivesEmployees(TransferArchivesEmployeesCommand cmd);

    void dismissArchivesEmployeesConfig(DismissArchivesEmployeesCommand cmd);

    void dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd);

    void deleteArchivesDismissEmployees(Long detailId, Long organizationId);

    void deleteArchivesEmployees(DeleteArchivesEmployeesCommand cmd);

    GeneralFormDTO updateArchivesForm(UpdateArchivesFormCommand cmd);

    GetArchivesFormResponse getArchivesForm(GetArchivesFormCommand cmd);

    ArchivesFromsDTO identifyArchivesForm(IdentifyArchivesFormCommand cmd);

    ImportFileTaskDTO importArchivesEmployees(MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesEmployeesCommand cmd);

    void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd, HttpServletResponse httpResponse);

    void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd,HttpServletResponse httpResponse);

    ImportFileResponse<ImportArchivesEmployeesDTO> getImportEmployeesResult(GetImportFileResultCommand cmd);

    List<OrganizationMemberDetails> queryArchivesEmployees(ListingLocator locator, Long organizationId, Long departmentId, ListingQueryBuilderCallback queryBuilderCallback);

    void remindArchivesEmployee(RemindArchivesEmployeeCommand cmd);

    void syncArchivesDismissStatus();
}
