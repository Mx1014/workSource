package com.everhomes.filemanagement;

import java.util.Map;

public interface FileService {

    Map<String, String> getFileIconUrl();

    String findUrlByFileType(String fileSuffix);
}
