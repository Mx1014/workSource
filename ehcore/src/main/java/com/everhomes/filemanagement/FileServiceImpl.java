package com.everhomes.filemanagement;

import com.everhomes.contentserver.ContentServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileServiceImpl implements FileService {

    @Autowired
    private FileProvider fileProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Override
    public Map<String, String> getFileIconUrl() {
        Map<String, String> icons = new HashMap<>();
        List<FileIcon> results = fileProvider.listFileIcons();

        if (results != null && results.size() > 0) {
            results.forEach(r -> {
                icons.put(r.getFileType(), contentServerService.parserUri(r.getIconUri()));
            });
        }
        return icons;
    }

    @Override
    public String findUrlByFileType(String fileSuffix) {
        FileIcon icon = fileProvider.findFileIconByFileType(fileSuffix);
        if (icon == null) {
            icon = fileProvider.findFileIconByFileType("other");
        }
        if (icon == null) {
            return null;
        }
        return contentServerService.parserUri(icon.getIconUri());
    }
}
