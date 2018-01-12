package com.everhomes.filemanagement;

import com.everhomes.rest.filemanagement.AddFileCatalogCommand;
import com.everhomes.rest.filemanagement.FileCatalogDTO;
import com.everhomes.rest.filemanagement.FileCatalogIdCommand;
import com.everhomes.rest.filemanagement.UpdateFileCatalogNameCommand;
import org.springframework.stereotype.Component;

@Component
public class FileManagementServiceImpl implements  FileManagementService{
    @Override
    public FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd) {
        return null;
    }

    @Override
    public void deleteFileCatalog(FileCatalogIdCommand cmd) {

    }

    @Override
    public FileCatalogDTO updateFileCatalogName(UpdateFileCatalogNameCommand cmd) {
        return null;
    }
}
