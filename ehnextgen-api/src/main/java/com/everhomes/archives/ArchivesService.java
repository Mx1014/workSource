package com.everhomes.archives;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.organization.GetImportFileResultCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ArchivesService {

    ArchivesContactDTO addArchivesContact(AddArchivesContactCommand cmd);

    void transferArchivesContacts(TransferArchivesContactsCommand cmd);

    void deleteArchivesContacts(DeleteArchivesContactsCommand cmd);

    void stickArchivesContact(StickArchivesContactCommand cmd);

    ListArchivesContactsResponse listArchivesContacts(ListArchivesContactsCommand cmd);

    ListArchivesEmployeesResponse listArchivesEmployees(ListArchivesEmployeesCommand cmd);

    void addArchivesLog(AddArchivesLogCommand cmd);

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

    ListArchivesDismissEmployeesResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd);

//    void employArchivesEmployees(EmployArchivesEmployeesCommand cmd);

//    void transferArchivesEmployees(TransferArchivesEmployeesCommand cmd);

//    void dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd);

    void deleteArchivesDismissEmployees(Long detailId, Long organizationId);

    void deleteArchivesEmployees(DeleteArchivesEmployeesCommand cmd);

    void executeArchivesConfiguration();

    void employArchivesEmployeesConfig(EmployArchivesEmployeesCommand cmd);

    void transferArchivesEmployeesConfig(TransferArchivesEmployeesCommand cmd);

    void dismissArchivesEmployeesConfig(DismissArchivesEmployeesCommand cmd);

    CheckOperationResponse checkArchivesOperation(CheckOperationCommand cmd);

    ListDismissCategoriesResponse listArchivesDismissCategories();

    ArchivesOperationalConfigurationDTO getArchivesOperationByUserId(Long userId, Long organizationId, Byte operationType);

    List<OrganizationMemberDetails> queryArchivesEmployees(ListingLocator locator, Long organizationId, Long departmentId, ListingQueryBuilderCallback queryBuilderCallback);

    void updateArchivesEmployeeAvatar(UpdateArchivesEmployeeCommand cmd);

    void setArchivesNotification(ArchivesNotificationCommand cmd);

    ArchivesNotificationDTO getArchivesNotification(ArchivesNotificationCommand cmd);

    void executeArchivesNotification(Integer day, Integer time, LocalDateTime nowDateTime);

    /* 同步数据接口 start */
    void makeArchivesCheckInTime();

    void cleanRedundantArchivesDetails();
    /* 同步数据接口 end */
}
