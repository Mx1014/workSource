package com.everhomes.archives;

import org.jooq.Condition;

import java.sql.Date;
import java.util.List;

public interface ArchivesProvider {

    void createArchivesStickyContacts(ArchivesStickyContacts archivesContactsSticky);

    void updateArchivesStickyContacts(ArchivesStickyContacts archivesContactsSticky);

    void deleteArchivesStickyContacts(ArchivesStickyContacts archivesContactsSticky);

    void deleteArchivesStickyContactsByDetailIds(Integer namespaceId, List<Long> detailIds);

    ArchivesStickyContacts findArchivesStickyContactsById(Long id);

    List<Long> listArchivesStickyContactsIds(Integer namespaceId, Long organizationId, Integer stickCount);

    ArchivesStickyContacts findArchivesStickyContactsByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId);

    void deleteArchivesStickyContactsByDetailId(Integer namespaceId, Long detailId);

    void deleteArchivesDismissEmployees(ArchivesDismissEmployees dismissEmployee);

    ArchivesDismissEmployees getArchivesDismissEmployeesByDetailId(Long detailId);

    void createArchivesDismissEmployee(ArchivesDismissEmployees archivesDismissEmployee);

    void updateArchivesDismissEmployee(ArchivesDismissEmployees dismissEmployee);

    List<ArchivesDismissEmployees> listArchivesDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition);

    List<Long> listDismissEmployeeDetailIdsByDepartmentId(Long departmentId);

    void createArchivesForm(ArchivesFroms form);

    void updateArchivesForm(ArchivesFroms form);

    ArchivesFroms findArchivesFormOriginId(Integer namespaceId, Long organizationId);

    void createOperationalConfiguration(ArchivesOperationalConfiguration config);

    void deleteLastConfiguration(Integer namespaceId, List<Long> detailIds, Byte operationType);

    void updateOperationalConfiguration(ArchivesOperationalConfiguration config);

    ArchivesOperationalConfiguration findConfigurationByDetailId(Integer namespaceId, Long organizationId, Byte type, Long detailId);

    ArchivesOperationalConfiguration findPendingConfigurationByDetailId(Integer namespaceId, Long detailId, Byte operationType);

    List<ArchivesOperationalConfiguration> listPendingConfigurationsInDetailIds(Integer namespaceId, List<Long> detailIds, Byte operationType);

    List<ArchivesOperationalConfiguration> listPendingConfigurations(Date date);

    void createOperationalLog(ArchivesOperationalLog log);

//    void createArchivesLogs(ArchivesLogs log);

    List<ArchivesOperationalLog> listArchivesLogs(Long organizationId, Long detailId);

    void createArchivesNotifications(ArchivesNotifications archivesNotification);

    ArchivesNotifications findArchivesNotificationsByOrganizationId(Integer namespaceId, Long organizationId);

    void updateArchivesNotifications(ArchivesNotifications archivesNotification);

    List<ArchivesNotifications> listArchivesNotifications(Integer weekDay, Integer time);

    List<ArchivesLogs> listAllArchivesLogs();

    List<ArchivesConfigurations> listAllPendingConfigs();

}