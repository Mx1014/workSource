package com.everhomes.archives;

import org.jooq.Condition;

import java.util.List;

public interface ArchivesProvider {

    void createArchivesContactsSticky(ArchivesStickyContacts archivesContactsSticky);

    void updateArchivesContactsSticky(ArchivesStickyContacts archivesContactsSticky);

    void deleteArchivesContactsSticky(ArchivesStickyContacts archivesContactsSticky);

    ArchivesStickyContacts findArchivesContactsStickyById(Long id);

    List<Long> listArchivesContactsStickyIds(Integer namespaceId, Long organizationId, Integer stickCount);

    ArchivesStickyContacts findArchivesContactsStickyByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId);

    void createArchivesDismissEmployee(ArchivesDismissEmployees archivesDismissEmployee);

    List<ArchivesDismissEmployees> listArchivesDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition);

    void createArchivesForm(ArchivesFroms form);

    void updateArchivesForm(ArchivesFroms form);

    ArchivesFroms findArchivesFormOriginId(Integer namespaceId, Long organizationId);
}
