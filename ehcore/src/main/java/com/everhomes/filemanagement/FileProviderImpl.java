package com.everhomes.filemanagement;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhFileIconsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileProviderImpl implements FileProvider {

    @Autowired
    private DbProvider dbProvider;

    @Override
    public List<FileIcon> listFileIcons() {
        List<FileIcon> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileIconsRecord> query = context.selectQuery(Tables.EH_FILE_ICONS);
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, FileIcon.class));
            return null;
        });

        if (results.size() > 0)
            return results;
        return null;
    }

    @Override
    public FileIcon findFileIconByFileType(String fileType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFileIconsRecord> query = context.selectQuery(Tables.EH_FILE_ICONS);
        EhFileIconsRecord record = query.fetchAny();
        if(null == record)
            return null;
        return record.map(r -> ConvertHelper.convert(r, FileIcon.class));
    }
}
