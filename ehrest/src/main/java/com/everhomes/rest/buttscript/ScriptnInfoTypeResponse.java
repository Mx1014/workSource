package com.everhomes.rest.buttscript;


import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>dtos: 参考{@com.everhomes.rest.buttscript.ScriptInfoTypeDTO}</li>
 * </ul>
 */
public class ScriptnInfoTypeResponse {

    private List<ScriptInfoTypeDTO> dtos ;

    public List<ScriptInfoTypeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ScriptInfoTypeDTO> dtos) {
        this.dtos = dtos;
    }

    public ScriptnInfoTypeResponse(){
        if(dtos == null){
            dtos = new ArrayList<ScriptInfoTypeDTO>();
        }
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
