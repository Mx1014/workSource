package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>list: list</li>
 * </ul>
 */
public class GeneralFormValList {

    private List<GeneralFormValDTO> list;

    public List<GeneralFormValDTO> getList() {
        return list;
    }

    public void setList(List<GeneralFormValDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
