// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>lockCode: 锁的key必填，参考：{@link com.everhomes.coordinator.CoordinationLocks}</li>
 * <li>lockTimeout: 时间以毫秒为单位，不填是默认为5秒；</li>
 * <li>number: 锁的编号用于在日志中识别是第几个锁，不填则默认为时间戳；</li>
 * </ul>
 */
public class TestLockAquiringCommand extends BaseCommand{
    private String lockCode;
    
    private Long lockTimeout;
    
    private Integer number;

    public TestLockAquiringCommand() {
    }

    public String getLockCode() {
        return lockCode;
    }

    public void setLockCode(String lockCode) {
        this.lockCode = lockCode;
    }

    public Long getLockTimeout() {
        return lockTimeout;
    }

    public void setLockTimeout(Long lockTimeout) {
        this.lockTimeout = lockTimeout;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
