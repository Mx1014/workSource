package com.everhomes.archives;

import org.jooq.Condition;

import java.util.List;

public interface ArchivesProvider {

    void createArchivesContactsSticky(ArchivesContactsSticky profileContactsSticky);

    void updateArchivesContactsSticky(ArchivesContactsSticky profileContactsSticky);

    void deleteArchivesContactsSticky(ArchivesContactsSticky profileContactsSticky);

    ArchivesContactsSticky findArchivesContactsStickyById(Long id);

    List<Long> listArchivesContactsStickyIds(Integer namespaceId, Long organizationId);

    ArchivesContactsSticky findArchivesContactsStickyByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId);

    void createArchivesDismissEmployee(ArchivesDismissEmployees profileDismissEmployee);

    List<ArchivesDismissEmployees> listArchivesDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition);

}
