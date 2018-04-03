package com.everhomes.rest.field;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>fields: 字段信息， 参考{@link com.everhomes.rest.field.ScopeFieldInfo}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class UpdateFieldsCommand {

    @ItemType(ScopeFieldInfo.class)
    private List<ScopeFieldInfo> fields;

    public List<ScopeFieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<ScopeFieldInfo> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
