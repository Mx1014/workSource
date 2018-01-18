package com.everhomes.filemanagement;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileProviderImpl implements FileProvider{

    @Override
    public List<FileIcon> listFileIcons(){
        return null;
    }
}
