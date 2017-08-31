package com.everhomes.archives;

import org.jooq.Condition;

import java.util.List;

public interface ArchivesProvider {

    void createArchivesContactsSticky(ArchivesContactsSticky archivesContactsSticky);

    void updateArchivesContactsSticky(ArchivesContactsSticky archivesContactsSticky);

    void deleteArchivesContactsSticky(ArchivesContactsSticky archivesContactsSticky);

    ArchivesContactsSticky findArchivesContactsStickyById(Long id);

    List<Long> listArchivesContactsStickyIds(Integer namespaceId, Long organizationId);

    ArchivesContactsSticky findArchivesContactsStickyByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId);

    void createArchivesDismissEmployee(ArchivesDismissEmployees archivesDismissEmployee);

    List<ArchivesDismissEmployees> listArchivesDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition);

    void createArchivesForm(ArchivesFroms form);

    void updateArchivesForm(ArchivesFroms form);

    ArchivesFroms findArchivesFormOriginId(Long organizationId);
}
