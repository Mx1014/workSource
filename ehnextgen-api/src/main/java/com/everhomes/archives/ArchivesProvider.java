package com.everhomes.archives;

import org.jooq.Condition;

import java.util.List;

public interface ArchivesProvider {

    void createArchivesStickyContacts(ArchivesStickyContacts archivesContactsSticky);

    void updateArchivesStickyContacts(ArchivesStickyContacts archivesContactsSticky);

    void deleteArchivesStickyContacts(ArchivesStickyContacts archivesContactsSticky);

    ArchivesStickyContacts findArchivesStickyContactsById(Long id);

    List<Long> listArchivesStickyContactsIds(Integer namespaceId, Long organizationId, Integer stickCount);

    ArchivesStickyContacts findArchivesStickyContactsByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId);

    void createArchivesDismissEmployee(ArchivesDismissEmployees archivesDismissEmployee);

    List<ArchivesDismissEmployees> listArchivesDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition);

    void createArchivesForm(ArchivesFroms form);

    void updateArchivesForm(ArchivesFroms form);

    ArchivesFroms findArchivesFormOriginId(Integer namespaceId, Long organizationId);

    void createArchivesConfigurations(ArchivesConfigurations configuration);

    void updateArchivesConfigurations(ArchivesConfigurations configuration);
}