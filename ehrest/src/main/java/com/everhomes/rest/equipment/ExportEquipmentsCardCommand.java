package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>ids: 下载设备卡的设备id列表(web用get请求，不能传数组，暂时用逗号隔开id)</li>
 *     <li>filePath: 下载路径</li>
 * </ul>
 * Created by ying.xiong on 2017/4/10.
 */
public class ExportEquipmentsCardCommand {
//    @ItemType(Long.class)
//    private List<Long> ids;

    private String ids;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
