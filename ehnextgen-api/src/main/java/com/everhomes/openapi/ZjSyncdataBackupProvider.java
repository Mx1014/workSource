package com.everhomes.openapi;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/8.
 */
public interface ZjSyncdataBackupProvider {
    void createZjSyncdataBackup(ZjSyncdataBackup backup);

    void updateZjSyncdataBackup(ZjSyncdataBackup backup);

    List<ZjSyncdataBackup> listZjSyncdataBackupByParam(Integer namespaceId, Byte dataType);

    void updateZjSyncdataBackupInactive(List<ZjSyncdataBackup> backupList);
}
