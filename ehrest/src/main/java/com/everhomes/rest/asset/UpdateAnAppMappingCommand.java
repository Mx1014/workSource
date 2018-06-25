//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/31.
 */

public class UpdateAnAppMappingCommand {
    private Long moduleIdRo;
    private Long originIdRo;
    private Long moduleIdJu;
    private Long originIdJu;

    public Long getModuleIdRo() {
        return moduleIdRo;
    }

    public void setModuleIdRo(Long moduleIdRo) {
        this.moduleIdRo = moduleIdRo;
    }

    public Long getOriginIdRo() {
        return originIdRo;
    }

    public void setOriginIdRo(Long originIdRo) {
        this.originIdRo = originIdRo;
    }

    public Long getModuleIdJu() {
        return moduleIdJu;
    }

    public void setModuleIdJu(Long moduleIdJu) {
        this.moduleIdJu = moduleIdJu;
    }

    public Long getOriginIdJu() {
        return originIdJu;
    }

    public void setOriginIdJu(Long originIdJu) {
        this.originIdJu = originIdJu;
    }
}
