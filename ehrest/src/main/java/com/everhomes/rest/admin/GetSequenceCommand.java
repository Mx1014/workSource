// @formatter:off
package com.everhomes.rest.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sequenceDomain: sequence对应的domain，要与同步sequence中使用的domain保持一致</li>
 * <li>blockSize: 要获取sequence的数量</li>
 * </ul>
 */
public class GetSequenceCommand {
    private String sequenceDomain;
    private Long blockSize;

    public String getSequenceDomain() {
        return sequenceDomain;
    }

    public void setSequenceDomain(String sequenceDomain) {
        this.sequenceDomain = sequenceDomain;
    }

    public Long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(Long blockSize) {
        this.blockSize = blockSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
