package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ImportArchivesContactsFilesResponse {

    private List<ImportFileResultLog<ImportArchivesContactsDTO>> results;

    private Long coverCount;

    public ImportArchivesContactsFilesResponse() {
    }

    public List<ImportFileResultLog<ImportArchivesContactsDTO>> getResults() {
        return results;
    }

    public void setResults(List<ImportFileResultLog<ImportArchivesContactsDTO>> results) {
        this.results = results;
    }

    public Long getCoverCount() {
        return coverCount;
    }

    public void setCoverCount(Long coverCount) {
        this.coverCount = coverCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
