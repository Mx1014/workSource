package com.everhomes.filemanagement;

import java.util.List;

public interface FileProvider {

    List<FileIcon> listFileIcons();

    FileIcon findFileIconByFileType(String fileType);
}
