package com.everhomes.relocation;

import com.everhomes.rest.relocation.GetRelocationConfigCommand;
import com.everhomes.rest.relocation.RelocationConfigDTO;
import com.everhomes.rest.relocation.SetRelocationConfigCommand;
import org.apache.poi.xdgf.usermodel.section.geometry.RelCubBezTo;

public interface RelocationConfigService {

    RelocationConfigDTO setRelocationConfig(SetRelocationConfigCommand cmd);
    RelocationConfigDTO searchRelocationConfigById(GetRelocationConfigCommand cmd);
}
