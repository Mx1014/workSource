package com.everhomes.fixedasset;

import java.util.ArrayList;
import java.util.List;

public class FixedAssetConstants {

    public enum FixedAssetExcelShowColumn {
        编号, 分类, 名称, 规格, 单价, 购买时间, 所属供应商, 来源, 其他, 状态, 使用部门, 使用人, 领用时间, 存放地点, 备注;

        public static List<String> getTitleList() {
            List<String> values = new ArrayList<>();
            FixedAssetExcelShowColumn[] columns = FixedAssetExcelShowColumn.values();
            for (FixedAssetExcelShowColumn column : columns) {
                values.add(column.name());
            }
            return values;
        }
    }

    public enum FixedAssetOperationLogShowColumn {
        编号, 类型, 名称, 规格, 单价, 购买时间, 所属供应商, 来源, 其他, 状态, 使用部门, 使用人, 领用时间, 存放地点, 备注, 图片, 条形码;
    }

}
