package com.everhomes.archives;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
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

    void createOperationalConfiguration(ArchivesOperationalConfiguration config);

    void deleteLastConfiguration(Integer namespaceId, List<Long> detailIds, Byte operationType);

    void updateOperationalConfiguration(ArchivesOperationalConfiguration config);

    ArchivesOperationalConfiguration findConfigurationByDetailId(Integer namespaceId, Long organizationId, Byte type, Long detailId);

    ArchivesOperationalConfiguration findPendingConfigurationByDetailId(Integer namespaceId, Long detailId, Byte operationType);

    List<ArchivesOperationalConfiguration> listPendingConfigurationsInDetailIds(Integer namespaceId, List<Long> detailIds, Byte operationType);

    List<ArchivesOperationalConfiguration> listPendingConfigurations(Date date);

    void createOperationalLog(ArchivesOperationalLog log);

    List<ArchivesOperationalLog> listArchivesLogs(Long organizationId, Long detailId);

    void createArchivesNotifications(ArchivesNotifications archivesNotification);

    ArchivesNotifications findArchivesNotificationsByOrganizationId(Integer namespaceId, Long organizationId);

    void updateArchivesNotifications(ArchivesNotifications archivesNotification);

    List<ArchivesNotifications> listArchivesNotifications(Integer weekDay, Integer time);

    /* 同步数据接口 start */
    List<OrganizationMemberDetails> listDetailsWithoutCheckInTime();

    OrganizationMember findOrganizationMemberWithoutStatusByDetailId(Long detailId);

    List<Long> listDismissalDetailIds();

    List<OrganizationMemberDetails> listDetailsWithoutDismissalStatus(List<Long> detailIds);

    OrganizationMember findOrganizationMemberWithStatusByDetailId(Long detailId);

    /* 同步数据接口 end */

}
