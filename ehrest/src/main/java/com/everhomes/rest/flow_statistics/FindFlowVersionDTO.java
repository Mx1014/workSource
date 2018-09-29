package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * <ul>
 * <li>flowVersions: 版本号</li>
 * </ul>
 */
public class FindFlowVersionDTO {

    private List<Integer> flowVersions ;

    public FindFlowVersionDTO(){
        if(flowVersions == null){
            flowVersions = new ArrayList<Integer>();
        }
    }


    public List<Integer> getFlowVersions() {
        return flowVersions;
    }

    public void setFlowVersions(List<Integer> flowVersions) {
        this.flowVersions = flowVersions;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
