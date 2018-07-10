package com.everhomes.archives;

public interface ArchivesFormProvider {

    void createArchivesFormNaviga(ArchivesFromNavigation navigation);

    void updateArchivesForm(ArchivesFroms form);

    ArchivesFroms findArchivesFormOriginId(Integer namespaceId, Long organizationId);

}
