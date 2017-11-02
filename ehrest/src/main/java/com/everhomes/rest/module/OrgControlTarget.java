package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

public class OrgControlTarget {
    @NotNull
    private Long id;

    private Byte includeChildFlag;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getIncludeChildFlag() {
        return includeChildFlag;
    }

    public void setIncludeChildFlag(Byte includeChildFlag) {
        this.includeChildFlag = includeChildFlag;
    }
}
