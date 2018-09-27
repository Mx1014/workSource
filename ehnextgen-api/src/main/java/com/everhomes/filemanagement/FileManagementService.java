package com.everhomes.filemanagement;

import com.everhomes.rest.filemanagement.*;

public interface FileManagementService {

    FileCatalogDTO addFileCatalog(AddFileCatalogCommand cmd);

    void deleteFileCatalog(FileCatalogIdCommand cmd);

    FileCatalogDTO updateFileCatalog(UpdateFileCatalogCommand cmd);

    FileCatalogDTO getFileCatalog(FileCatalogIdCommand cmd);

    ListFileCatalogResponse listFileCatalogs(ListFileCatalogsCommand cmd);

    ListFileCatalogResponse listAvailableFileCatalogs(ListFileCatalogsCommand cmd);

    SearchFileResponse searchFiles(SearchFileCommand cmd);

    FileContentDTO addFileContent(AddFileContentCommand cmd);

    void deleteFileContents(DeleteFileContentCommand cmd);

    FileContentDTO updateFileContentName(UpdateFileContentNameCommand cmd);

    ListFileContentResponse listFileContents(ListFileContentCommand cmd); 

	public void moveFileContent(MoveFileContentCommand cmd);


	public GetFileIconListResponse getFileIconList();

    ListAllFlodersResponse listAllFloders(ListAllFlodersCommand cmd);
}